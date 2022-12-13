package com.gb.soa.omp.ccache.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.gb.soa.omp.ccache.api.model.CacheKeyGenerateRule;
import com.gb.soa.omp.ccache.api.request.CacheDeleteRequest;
import com.gb.soa.omp.ccache.api.request.CacheGetRequest;
import com.gb.soa.omp.ccache.api.request.CacheKeyGenerateRuleByMethodNameGetRequest;
import com.gb.soa.omp.ccache.api.request.CacheKeyGenerateRuleBySubSystemGetRequest;
import com.gb.soa.omp.ccache.api.response.CacheDeleteResponse;
import com.gb.soa.omp.ccache.api.response.CacheGetResponse;
import com.gb.soa.omp.ccache.api.response.CacheKeyGenerateRuleByMethodNameGetResponse;
import com.gb.soa.omp.ccache.api.response.CacheKeyGenerateRuleBySubSystemGetResponse;
import com.gb.soa.omp.ccache.api.service.CacheStoreService;
import com.gb.soa.omp.ccache.dao.EcCacheMethodSchemaDefineDao;
import com.gb.soa.omp.ccache.dao.EcCommonCacheDependenceDao;
import com.gb.soa.omp.ccache.entity.EC_CACHE_METHOD_SCHEMA_DEFINE;
import com.gb.soa.omp.ccache.entity.EC_COMMON_CACHE_DEPENDENCE;
import com.gb.soa.omp.ccache.service.CacheCommonService;
import com.gb.soa.omp.ccache.service.model.SchemaDefineAndTableNameList;
import com.gb.soa.omp.ccache.util.Constant;
import com.gb.soa.omp.ccommon.api.exception.BusinessException;
import com.gb.soa.omp.ccommon.api.exception.ExceptionType;
import com.gb.soa.omp.ccommon.api.exception.ValidateBusinessException;
import com.gb.soa.omp.ccommon.api.exception.ValidateClientException;
import com.gb.soa.omp.ccommon.util.*;
import com.ykcloud.soa.mapping.GatewayMapping;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("cacheStoreService")
@FeignClient(value = "@Value(\"${spring.application.name}\")")
@RestController
@GatewayMapping
public class CacheStoreServiceImpl implements CacheStoreService {
	private final static Logger log = LoggerFactory.getLogger(CacheStoreServiceImpl.class);
	private static JsonMapper mapper;
	private static JsonMapper mapperCache = JsonMapper.nonDefaultMapper();

	static {
		mapper = JsonMapper.nonEmptyMapper();
		mapper.getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.LowerCaseStrategy.SNAKE_CASE);
	}

	@Resource(name = "stringRedisTemplate")
	private StringRedisTemplate stringRedisTemplate;

	@Resource
	private EcCacheMethodSchemaDefineDao ecCacheMethodSchemaDefineDao;

	@Resource
	private EcCommonCacheDependenceDao ecCommonCacheDependenceDao;

	@Resource
	private CacheCommonService cacheCommonService;

	@Value("${schema.define.primary.key.cache.sign:true}")
	private boolean schemaDefinePrimaryKeyCacheSign;


	//获取单个缓存
	public CacheGetResponse getCache(CacheGetRequest request){
		if (log.isDebugEnabled()) {
			log.debug("begin getCache request:{}",mapper.toJson(request));
		}
		CacheGetResponse response = new CacheGetResponse();
		try{
			request.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10030);
			String cacheResult=doGetCache(request.getDataSign(),request.getMethodName(),request.getParams());
			response.setCacheResult(cacheResult);
		} catch(Exception ex){
			//这个地方经常抛异常，修改一下
			if(ex instanceof BusinessException && ExceptionType.BE40071.getCode()==((BusinessException) ex).getCode()) {
				log.error("缓存入参在数据库中没查询出结果集,message:{}",((BusinessException) ex).getMessage());
				response.setCode(ExceptionType.BE40071.getCode());
				response.setMessage(((BusinessException) ex).getMessage());
			} else {
				ExceptionUtil.processException(ex, response);
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("end getCache response:{}",response.toLowerCaseJson());
		}
		return response;
	}

	/***
	 * 操作缓存
	 * @param dataSign
	 * @param methodName
	 * @param params
	 * @return
	 */
	private String doGetCache(Long dataSign,String methodName,String params){
		EC_CACHE_METHOD_SCHEMA_DEFINE schemaDefineEntity;
		List<String> tableNameList;
		if (schemaDefinePrimaryKeyCacheSign) {
			String schemaDefineMethodName="ykcloud.cache.schema.define.and.table.name.list.get";
			String key=schemaDefineMethodName+"_"+methodName;
			String json=stringRedisTemplate.opsForValue().get(key);
			if (json!=null){
				SchemaDefineAndTableNameList schemaDefineAndTableNameList= mapper.fromJson(json, SchemaDefineAndTableNameList.class);
				schemaDefineEntity=schemaDefineAndTableNameList.getSchemaDefine();
				tableNameList=schemaDefineAndTableNameList.getTableNameList();
			} else {
				schemaDefineEntity=ecCacheMethodSchemaDefineDao.getEntityByMethodName(methodName);
				//查询表名解析
				tableNameList=parseTableNameListFromSchemaDefineSqlContent(schemaDefineEntity.getMETHOD_NAME(),
						schemaDefineEntity.getSQL_CONTENT());
				SchemaDefineAndTableNameList schemaDefineAndTableNameList=new SchemaDefineAndTableNameList();
				schemaDefineAndTableNameList.setSchemaDefine(schemaDefineEntity);
				schemaDefineAndTableNameList.setTableNameList(tableNameList);
				json=mapper.toJson(schemaDefineAndTableNameList);
				stringRedisTemplate.opsForValue().set(key,json);
			    stringRedisTemplate.expire(key,1L, TimeUnit.DAYS);
				if (log.isDebugEnabled()){
					log.debug("get cache schema define and table name list from db");
				}
			}
		} else {
			schemaDefineEntity=ecCacheMethodSchemaDefineDao.getEntityByMethodName(methodName);
			//查询表名解析
			tableNameList=parseTableNameListFromSchemaDefineSqlContent(schemaDefineEntity.getMETHOD_NAME(),
					schemaDefineEntity.getSQL_CONTENT());
		}
		//参数由驼峰转下划线
		@SuppressWarnings("unchecked")
		Map<String,Object> camelParamsMap=mapperCache.fromJson(params, Map.class);
		if (!camelParamsMap.containsKey("dataSign")){
			throw new ValidateClientException(Constant.SUB_SYSTEM,ExceptionType.VCE10030,"入参未包含测试标识!");
		}
		Long paramsDataSign=((Number)camelParamsMap.get("dataSign")).longValue();
		if (!dataSign.equals(paramsDataSign)){
			throw new ValidateClientException(Constant.SUB_SYSTEM,ExceptionType.VCE10030,"测试标识与入参不一致!");
		}
		//获取缓存键
		String[] arrCacheKey=schemaDefineEntity.getCACHE_MULTI_COL().split("#");//tenant_num_id#data_sign#ec_shop_num_id
		StringBuilder sb=new StringBuilder(schemaDefineEntity.getCACHE_METHOD());
		for (String cacheCol:arrCacheKey){
			Object obj=camelParamsMap.get(cacheCol);
			if (obj==null){
				throw new ValidateBusinessException(Constant.SUB_SYSTEM,ExceptionType.VBE20030,"获取通用缓存键值失败,方法名:"+methodName+",键名:"+cacheCol);
			}
			sb.append("_").append(obj.toString());
		}
		String cacheKey=sb.toString();
		String cacheResult;
		JdbcTemplate jdbcTemplate=cacheCommonService.getJdbcTemplate(schemaDefineEntity.getDB());
		NamedParameterJdbcTemplate namedParameterJdbcTemplate=new NamedParameterJdbcTemplate(jdbcTemplate);
		List<Map<String,Object>> roughList = namedParameterJdbcTemplate.queryForList(schemaDefineEntity.getSQL_CONTENT(), camelParamsMap);
		if (roughList.isEmpty()) {
			if (schemaDefineEntity.getLIST_SIGN().equals(1L) &&
				schemaDefineEntity.getALLOW_LIST_EMPTY_SIGN().equals(1L) && schemaDefineEntity.getTTL().longValue()>0L) {
				cacheResult="[]";
				stringRedisTemplate.opsForValue().set(cacheKey,cacheResult);
				stringRedisTemplate.expire(cacheKey,schemaDefineEntity.getTTL().longValue(), TimeUnit.SECONDS);
				return cacheResult;
			} else {
				if (StringUtil.isNullOrBlank(schemaDefineEntity.getDESCRIPTION())){
					throw new BusinessException(Constant.SUB_SYSTEM,ExceptionType.BE40071,"获取通用缓存键值失败,数据库中不存在指定值,方法名:"+methodName+",入参:"+params);
				} else {
					throw new BusinessException(Constant.SUB_SYSTEM,ExceptionType.BE40071,schemaDefineEntity.getDESCRIPTION()+"为空,方法名:"+methodName+",入参:"+params);
				}
			}
		}
		if (!schemaDefineEntity.getLIST_SIGN().equals(1L) && roughList.size() > 1) {
			if (StringUtil.isNullOrBlank(schemaDefineEntity.getDESCRIPTION())){
				throw new BusinessException(Constant.SUB_SYSTEM,ExceptionType.BE40072,"获取通用缓存键值失败,数据库中存在多笔指定值,方法名:"+methodName+",入参:"+params);
			} else {
				throw new BusinessException(Constant.SUB_SYSTEM,ExceptionType.BE40072,"获取通用缓存键值失败,数据库中存在多笔指定值,方法名:"+methodName+schemaDefineEntity.getDESCRIPTION()+",入参:"+params);
			}
		}
		Long paramsTenantNumId;
		if (camelParamsMap.containsKey("tenantNumId")){
			paramsTenantNumId=((Number)camelParamsMap.get("tenantNumId")).longValue();
		} else {
			paramsTenantNumId=0L;
		}
		List<Map<String,Object>> mapCacheResults=new ArrayList<Map<String,Object>>(roughList.size());
		List<Object> objectCacheResults=new ArrayList<Object>(roughList.size());

		List<EC_COMMON_CACHE_DEPENDENCE> dependenceList=new ArrayList<>();
		boolean isSingleColumnForList=false;
		boolean yetCheckSingleColumnForList=false;
		for (Map<String,Object> map:roughList){
			Map<String,Object> underlineParamsMap=new HashMap<String,Object>();
			for (String camelKey:camelParamsMap.keySet()){
				String underlineKey=CamelUnderlineUtil.camelToUnderline(camelKey);
				underlineParamsMap.put(underlineKey, camelParamsMap.get(camelKey));
			}
			underlineParamsMap.putAll(map);
			boolean isMultiTable=tableNameList.size()>1;

			//记录缓存关联表主键价
			Set<String> tableSeriesColumnNameSet=new HashSet<String>(tableNameList.size());
			for (String tableName:tableNameList){
				String tableSeriesColumnName;
				if (isMultiTable){
					tableSeriesColumnName=tableName+"_series";
				} else {
					tableSeriesColumnName="series";
				}
				tableSeriesColumnNameSet.add(tableSeriesColumnName);
				Object tableSeriesValueObj=underlineParamsMap.get(tableSeriesColumnName);
				if (tableSeriesValueObj==null){
					log.info("查询结果里没有引用表行号的栏位值,表:"+tableName+",行号栏位:"+tableSeriesColumnName+"methodName:"+methodName+"params:"+params);
					continue;
					//throw new ValidateBusinessException(Constant.SUB_SYSTEM,ExceptionType.VBE20030,"查询结果里没有引用表行号的栏位值,表:"+tableName+",行号栏位:"+tableSeriesColumnName);
				}
				String tableSeries=tableSeriesValueObj.toString();
				EC_COMMON_CACHE_DEPENDENCE dependenceEntity=new EC_COMMON_CACHE_DEPENDENCE();
				dependenceEntity.setTENANT_NUM_ID(paramsTenantNumId);
				dependenceEntity.setDATA_SIGN(paramsDataSign);
				dependenceEntity.setMETHOD_NAME(methodName);
				dependenceEntity.setDB(schemaDefineEntity.getDB());
				dependenceEntity.setCACHE_KEY(cacheKey);
				dependenceEntity.setPARAMS(params);
				dependenceEntity.setTABLE_NAME(tableName);
				dependenceEntity.setTABLE_SERIES(tableSeries);
				dependenceEntity.setDATA_SIGN(dataSign);
				dependenceEntity.setTABLE_NAME(tableName);
				dependenceEntity.setTABLE_SERIES(tableSeries);
				dependenceEntity.setCREATE_USER_ID(1L);
				dependenceEntity.setLAST_UPDATE_USER_ID(1L);
				dependenceEntity.setDUBBO_GROUP(getCurrentThreadDubboGroup());
				dependenceList.add(dependenceEntity);
			}

			//生成缓存结果，由下划线转驼峰
			Map<String,Object> camelCacheResultMap=new HashMap<String,Object>(map.size());
			for (String underlineColName:map.keySet()){
				if (tableSeriesColumnNameSet.contains(underlineColName)){//表行号，一般不返回
					continue;
				}
				Object colValue=map.get(underlineColName);
				String camelColName=CamelUnderlineUtil.underlineToCamel(underlineColName);
				camelCacheResultMap.put(camelColName, colValue);
			}
			//列表返回，map里只有一个栏位，用数组直接返回,比如仓库列表{1,2,3}
			if (!yetCheckSingleColumnForList){
				if (schemaDefineEntity.getLIST_SIGN().equals(1L) && camelCacheResultMap.size()==1){
					isSingleColumnForList=true;
				}
				yetCheckSingleColumnForList=true;
			}
			if (isSingleColumnForList){
				 for (Entry<String,Object> entry : camelCacheResultMap.entrySet()) {
		            Object obj = entry.getValue();
		            objectCacheResults.add(obj);
		            break;
				}
			} else {
				mapCacheResults.add(camelCacheResultMap);
			}

		}

		//异步处理流水表
		String dubboGroup = getCurrentThreadDubboGroup();
		Constant.tExecutor.execute(new Runnable() {
			public void run() {
				dealCacheMethodDependence(methodName,cacheKey,paramsTenantNumId,dataSign,schemaDefineEntity,dependenceList, dubboGroup);
			}
		});

		if (isSingleColumnForList){
			cacheResult= mapper.toJson(objectCacheResults);
		} else {
			if (schemaDefineEntity.getLIST_SIGN().equals(1L)){
				cacheResult= mapper.toJson(mapCacheResults);
			} else {
				cacheResult= mapper.toJson(mapCacheResults.get(0));
			}
		}

		stringRedisTemplate.opsForValue().set(cacheKey,cacheResult);
		if (schemaDefineEntity.getTTL().longValue()>0L){
			stringRedisTemplate.expire(cacheKey,schemaDefineEntity.getTTL().longValue(), TimeUnit.SECONDS);
		}
		return cacheResult;
	}

	private String getCurrentThreadDubboGroup() {
		// 通过dubbo上下文,获取当前dubbo方法的group分组信息
		URL url = RpcContext.getContext().getUrl();
		if (url==null){//非dubbo调用
			return "";
		}
		Map<String, String> urlMap = url.toMap();
		String group = urlMap.get("group");
		if (group == null || group.trim().equals("")) {
			group="";
		}
		if (log.isDebugEnabled()) {
			log.debug("从dubbo URL获取到的dubbo分组group:{}",group);
		}
		return group;
	}

	/***
	 * 处理刷新缓存流水表
	 * @param methodName
	 * @param cacheKey
	 * @param paramsTenantNumId
	 * @param dataSign
	 * @param schemaDefineEntity
	 * @param dependenceList
	 * @param dubboGroup
	 * @return boolean
	 */
	public boolean dealCacheMethodDependence(String methodName,String cacheKey,Long paramsTenantNumId,Long dataSign,EC_CACHE_METHOD_SCHEMA_DEFINE schemaDefineEntity,List<EC_COMMON_CACHE_DEPENDENCE> dependenceList, String dubboGroup) {
		long t=System.currentTimeMillis();
		RedisLock lock = null;
		boolean isSuccess=true;
		try {
			String distributedLockKey=methodName+"_"+cacheKey;
			lock = new RedisLock(stringRedisTemplate, distributedLockKey, 120);
			if (!lock.lock()) {
				throw new BusinessException(Constant.SUB_SYSTEM, ExceptionType.VBE20030, "distributedLockKey："+distributedLockKey +"正在执行中...");
			}

			List<String> series=ecCommonCacheDependenceDao.getSeriesByMethodNameAndCacheKey(paramsTenantNumId,dataSign,methodName,cacheKey,dubboGroup);
			if (CollectionUtils.isNotEmpty(series)){
				for(String serie:series) {//经常出现事物超时，因此优化一下
					ecCommonCacheDependenceDao.deleteEntityByMethodNameAndCacheKeySeries(paramsTenantNumId, dataSign, methodName, cacheKey,serie);
				}
			}
			if((System.currentTimeMillis()-t)>1000) {
				log.info("dealCacheMethodDependence 刷新超时 cacheKey:{},methodName:{},cost:{}",cacheKey,methodName,(System.currentTimeMillis()-t));
			}

			long t1=System.currentTimeMillis();
			ecCommonCacheDependenceDao.batchInsertEntity(dependenceList);
			if((System.currentTimeMillis()-t1)>1000) {
				log.info("dealCacheMethodDependence 批量插入超时 ec_common_cache_dependence cacheKey:{},methodName:{},cost:{}",cacheKey,methodName,(System.currentTimeMillis()-t1));
			}
			//lock.unlock();
		} catch (Exception ex) {
			log.error("dealCacheMethodDependence cacheKey:{},methodName:{},msg:{}，",cacheKey,methodName,ex);
			isSuccess=false;//报错让其不插入缓存
		}finally {
			if(lock!=null) {
				lock.unlock();
			}
		}
		return isSuccess;
	}

	//按子系统获取缓存键生成规则列表
	public CacheKeyGenerateRuleBySubSystemGetResponse getCacheKeyGenerateRuleBySubSystem(CacheKeyGenerateRuleBySubSystemGetRequest request){
		if (log.isDebugEnabled()) {
			log.debug("begin getCacheKeyGenerateRuleBySubSystem request:{}",mapper.toJson(request));
		}
		CacheKeyGenerateRuleBySubSystemGetResponse response = new CacheKeyGenerateRuleBySubSystemGetResponse();
		try{
			request.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10030);
			List<EC_CACHE_METHOD_SCHEMA_DEFINE> list=ecCacheMethodSchemaDefineDao.findEntityBySubSystemNotMustExist(request.getSubSystem());
			List<CacheKeyGenerateRule> rules=new ArrayList<CacheKeyGenerateRule>(list.size());
			for (EC_CACHE_METHOD_SCHEMA_DEFINE sd:list){
				CacheKeyGenerateRule rule=new CacheKeyGenerateRule();
				rule.setCacheMethod(sd.getCACHE_METHOD());
				rule.setCacheMultiCol(sd.getCACHE_MULTI_COL());
				rule.setMethodName(sd.getMETHOD_NAME());
				rules.add(rule);
			}
			response.setRules(rules);
		} catch(Exception ex){
			ExceptionUtil.processException(ex, response);
		}
		if (log.isDebugEnabled()) {
			log.debug("end getCacheKeyGenerateRuleBySubSystem response:{}",response.toLowerCaseJson());
		}
		return response;
	}

	//按方法名称获取缓存键生成规则列表
	public CacheKeyGenerateRuleByMethodNameGetResponse getCacheKeyGenerateRuleByMethodName(CacheKeyGenerateRuleByMethodNameGetRequest request){
		if (log.isDebugEnabled()) {
			log.debug("begin getCacheKeyGenerateRuleByMethodName request:{}",mapper.toJson(request));
		}
		CacheKeyGenerateRuleByMethodNameGetResponse response = new CacheKeyGenerateRuleByMethodNameGetResponse();
		try{
			request.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10030);
			EC_CACHE_METHOD_SCHEMA_DEFINE schemaDefineEntity=ecCacheMethodSchemaDefineDao.getEntityByMethodName(request.getMethodName());
			CacheKeyGenerateRule rule=new CacheKeyGenerateRule();
			rule.setCacheMethod(schemaDefineEntity.getCACHE_METHOD());
			rule.setCacheMultiCol(schemaDefineEntity.getCACHE_MULTI_COL());
			rule.setMethodName(schemaDefineEntity.getMETHOD_NAME());
			response.setRule(rule);
		} catch(Exception ex){
			ExceptionUtil.processException(ex, response);
		}
		if (log.isDebugEnabled()) {
			log.debug("end getCacheKeyGenerateRuleByMethodName response:{}",response.toLowerCaseJson());
		}
		return response;
	}


	/**
	 *
	 */
	@Override
	public CacheDeleteResponse deleteCache(CacheDeleteRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("begin deleteCache request:{}",mapper.toJson(request));
		}
		CacheDeleteResponse response = new CacheDeleteResponse();
		try{
			request.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10030);
			String cacheKey = request.getCacheKey();
			Object[] keyValues = request.getKeyValues();
			if (keyValues!=null) {
				for (Object obj : keyValues) {
					cacheKey=cacheKey+"_"+obj;
				}
			}
			Set<String> keys =  stringRedisTemplate.keys(cacheKey);
			stringRedisTemplate.delete(keys);
		} catch(Exception ex){
			ExceptionUtil.processException(ex, response);
		}
		if (log.isDebugEnabled()) {
			log.debug("end deleteCache response:{}",response.toLowerCaseJson());
		}
		return response;
	}

	//从sql解析表名列表
	//参考:https://blog.csdn.net/qichangjian/article/details/89151013
	private List<String> parseTableNameListFromSchemaDefineSqlContent(String methodName,String sqlContent) {
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sqlContent, JdbcConstants.MYSQL);
		if (stmtList.isEmpty()) {
			throw new ValidateBusinessException(Constant.SUB_SYSTEM,ExceptionType.VBE20030,"未能从通用缓存sql定义中解析出表名,方法名:"+methodName+"sql内容:"+sqlContent);
		}
		if (stmtList.size()>1) {
			throw new ValidateBusinessException(Constant.SUB_SYSTEM,ExceptionType.VBE20030,"从通用缓存sql定义中解析出表名出错，请确认sql正确定义,方法名:"+methodName+"sql内容:"+sqlContent);
		}
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        stmtList.get(0).accept(visitor);
        return visitor.getTables().keySet().stream().map(TableStat.Name::getName).collect(Collectors.toList());
	}

}
