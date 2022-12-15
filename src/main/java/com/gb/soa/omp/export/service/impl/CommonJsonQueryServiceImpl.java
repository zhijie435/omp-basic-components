package com.gb.soa.omp.export.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;

import com.gb.soa.omp.exchange.util.ExportUtil;
import com.gb.soa.omp.export.service.model.BatchExcuteModel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.gb.soa.omp.ccache.client.util.CacheUtil;
import com.gb.soa.omp.ccommon.api.exception.DatabaseOperateException;
import com.gb.soa.omp.ccommon.api.exception.ExceptionType;
import com.gb.soa.omp.ccommon.datasource.DataSourceContextHolder;
import com.gb.soa.omp.ccommon.util.CamelUnderlineUtil;
import com.gb.soa.omp.ccommon.util.JsonMapper;
import com.gb.soa.omp.ccommon.util.StringUtil;
import com.gb.soa.omp.exchange.model.CommonQuery;
import com.gb.soa.omp.exchange.model.ExcuteSqlResultModel;
import com.gb.soa.omp.exchange.util.CommUtil;
import com.gb.soa.omp.export.service.CommonJsonQueryService;
import com.gb.soa.omp.export.service.model.CommonQueryGetRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service("exportCommonJsonQueryService")
public class CommonJsonQueryServiceImpl implements CommonJsonQueryService {

	private static Logger log = LoggerFactory.getLogger(CommonJsonQueryServiceImpl.class);

	private static JsonMapper mapper;
	static {
		mapper = new JsonMapper();
		mapper.getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
	}

	@Resource(name="commonQueryDyJdbcTemplate")
	private JdbcTemplate dynamicJdbcTemplate;
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	public ExcuteSqlResultModel excuteSqlById(Long currentPage,String dataSourceName,CommonQuery commonQuery,Map<String,Object> inputParam,Long tenantNumId, Long dataSign ){

		if (StringUtil.isNullOrBlankTrim(dataSourceName)) {
			dataSourceName = commonQuery.getJdbcName();
		}
		if (!commonQuery.getCacheSign().equals(0L)&&StringUtil.isNullOrBlank(commonQuery.getMethodName())) {
			commonQuery.setMethodName(commonQuery.getSqlId());
		}
		ExcuteSqlResultModel resModel = new ExcuteSqlResultModel();
		List<Map<String,Object>> list=null;
		if (commonQuery.getCacheSign().equals(2L)) {
			list=new ArrayList<Map<String,Object>>();
			Map<String,Object> jsResult = CacheUtil.getCache(commonQuery.getDataSign(), commonQuery.getMethodName(), CamelUnderlineUtil.underlineToCamel(gson.toJson(inputParam)),Map.class, true);
			list.add(jsResult);
			resModel.setData(list);
			resModel.setRecordCount(list.size());
			resModel.setCount(list.size());
			return resModel;
		}
		// 获取对应的数据源
		DataSourceContextHolder.setDataSourceType(dataSourceName);
		String jsonContent = commonQuery.getParamContent();
		JSONArray ja = JSONArray.fromObject(jsonContent);
		Map<String, Object> map = CommUtil.getParamsWithMap(commonQuery, ja, inputParam);

		String sqlContent = commonQuery.getSqlContent();
		sqlContent = CommUtil.sqlHandler(sqlContent, map);
		Long pageSize = commonQuery.getPageSize();
		resModel.setSqlFlag(commonQuery.getSqlFlag());
		//select 情况时的分页处理
		if( currentPage > 0  && pageSize > 0 && CommonQuery.SQL_FLAG_SELECT.equals(commonQuery.getSqlFlag())) {
			// 总页数
			long totalPage = 1;
			long recordCount = 0;
			// 计算总页数
			String recordCountSql = "select count(1) CT from ("
					+ sqlContent + ") t";
			List<Map<String,Object>> res = queryRes(commonQuery, recordCountSql, map,resModel, false);
			Map<String,Object> resTempMap = (Map<String,Object>) res.get(0);

			recordCount = Long.valueOf(resTempMap.get("CT").toString());
			totalPage = ((recordCount - 1) / pageSize) + 1;
			resModel.setRecordCount(recordCount);
			resModel.setPageCount(totalPage);
			if(recordCount == 0){
				resModel.setData(res);
				return resModel;
			}

			long startIndex = (currentPage - 1) * pageSize + 1;
			long endIndex = currentPage * pageSize;
			if("MYSQL".equals(commonQuery.getDbType().toUpperCase())){
				sqlContent = sqlContent + " limit "+(startIndex - 1)+","+pageSize;;
			}else{
				sqlContent = "select *" + "  from (select a.*, rownum as rnum"
						+ "          from (" + sqlContent + ") a"
						+ "         where rownum <= " + endIndex + ")"
						+ " where rnum >= " + startIndex;
			}
		}else{
			resModel.setPageCount(1);
		}
		String annotatePrefix =  commonQuery.getAnnotatePrefix();
		if(StringUtil.isAllNotNullOrBlank(annotatePrefix)){
			sqlContent = annotatePrefix + sqlContent;
		}
		if(CommonQuery.SQL_FLAG_SELECT.equals(commonQuery.getSqlFlag())){
			//通用查询
			List<Map<String,Object>> res = queryRes(commonQuery, sqlContent, map,resModel, true);
			resModel.setData(res);
			resModel.setCount(res.size());
			if (!(currentPage > 0  && pageSize > 0)) {//分页查询时，不再设置返回数，
				resModel.setRecordCount(res.size());
			}
		}else{
			//通用保存，更新
			int count = excuteUpdate(commonQuery, sqlContent, map,resModel);
			resModel.setRecordCount(count);
		}
		if(resModel.getRecordCount() == 0){
			resModel.setPageCount(0);
		}
		if (StringUtil.isAllNotNullOrBlank(commonQuery.getReturnHandleContent())) {
			String[] returnArgs = commonQuery.getReturnHandleContent().split(",");
			for (int i = 0; i < returnArgs.length; i++) {
				String returnArg = returnArgs[i];
				for (int j = 0; j < resModel.getData().size(); j++) {
					Map<String,Object> js = resModel.getData().get(j);
					Object value = js.get(returnArg);
					js.replace(returnArg, String.valueOf(value));
				}
			}
		}
		return resModel;
	}

	private List<Map<String,Object>> queryRes(CommonQuery commonQuery, String sqlContent,
			Map<String, Object> map,ExcuteSqlResultModel resModel, boolean useCache) {
		List<Object> arrList= new ArrayList<Object>();
		Object os [] = map.values().toArray();
		for(Object o : os){
			if(o!=null){
				arrList.add(o);
			}
		}
		Object arr [] = arrList.toArray();
		sqlContent = sqlContent.replaceAll("[\\[\\]]*", "");
		resModel.setArg(arr);
		resModel.setSql(sqlContent);
		String strArr[] = null;
		if (commonQuery.getSubQuerySign().equals(2L)) {
			String tmpStr = sqlContent;
			sqlContent = sqlContent.toLowerCase();
			String colums = sqlContent.substring(sqlContent.indexOf("select")+7,sqlContent.indexOf("from"));
			strArr = colums.split(",");
			sqlContent = tmpStr;
		}
		List<Map<String,Object>> list;
		try {
			if (commonQuery.getCacheSign()==null) {
				commonQuery.setCacheSign(0L);
			}
			if (commonQuery.getCacheSign().equals(1L)&&useCache) {//直接用缓存
				if (log.isDebugEnabled()) {
					log.info("use cache");
				}
				String cacheKey = commonQuery.getSqlId();
				for (Object object : arr) {
					cacheKey += "_"+object;
				}
				log.info("cacheKey:" + cacheKey);
				String reult = stringRedisTemplate.opsForValue().get(cacheKey);
				if (reult==null) {
					list = dynamicJdbcTemplate.queryForList(sqlContent, arr);
					if (!list.isEmpty()) {
						reult = gson.toJson(list);
						stringRedisTemplate.opsForValue().set(cacheKey, reult,commonQuery.getCacheLiveTime(),TimeUnit.SECONDS);
					}
				}else{
					//list = gson.fromJson(reult, List.class);
					list=mapper.fromJson(reult, List.class);
				}
			}
			else{
				try {
					list = dynamicJdbcTemplate.queryForList(sqlContent, arr);
				} catch (Throwable e) {
					e.printStackTrace();
					throw e;
				}
			}
		} catch (Exception e) {
			throw new DatabaseOperateException(ExportUtil.SUB_SYSTEM, ExceptionType.DOE30051,
					"sqlId为" + commonQuery.getSqlId() + "执行sql语句异常"+e.getMessage()+ "," + "入参为:" + JSONArray.fromObject(arr).toString());
		}
		if (list.isEmpty()&&commonQuery.getSubQuerySign().equals(2L)) {
			Map<String,Object> mapForEmptResult = null;
			for (int i = 0; i < strArr.length; i++) {
				mapForEmptResult = new HashMap<String, Object>();
				if (commonQuery.getSqlId().contains("APP-MESSAGE-CENTER")) {
					if (strArr[i].indexOf(" as ")!=-1) {
						mapForEmptResult.put(strArr[i].substring(strArr[i].indexOf(" as ")+3).trim(), null);
					}else{
						mapForEmptResult.put(strArr[i].trim(), null);
					}
				}else{
					if (strArr[i].indexOf(" as ")!=-1) {
						mapForEmptResult.put(strArr[i].substring(strArr[i].indexOf(" as ")+3).trim(), "");
					}else{
						mapForEmptResult.put(strArr[i].trim(), "");
					}
				}

			}
			list.add(mapForEmptResult);
		}
		return list;
	}


	private int  excuteUpdate(CommonQuery commonQuery, String sqlContent,
			Map<String, Object> map,ExcuteSqlResultModel resModel) {
		List<Object> arrList= new ArrayList<Object>();
		Object os [] = map.values().toArray();
		for(Object o : os){
			if(o!=null){
				arrList.add(o);
			}
		}
		Object arr [] = arrList.toArray();
		sqlContent = sqlContent.replaceAll("[\\[\\]]*", "");
		int count;
		try {
			count = dynamicJdbcTemplate.update(sqlContent, arr);
		} catch (Exception e) {
			resModel.setArg(arr);
			resModel.setSql(sqlContent);
			throw new DatabaseOperateException(ExportUtil.SUB_SYSTEM, ExceptionType.DOE30051,
					"sqlId为" + commonQuery.getSqlId() + "执行sql语句异常"+e.getMessage()+",sql语句为:" + sqlContent+"入参为:" + JSONArray.fromObject(arr).toString());
		}
		return count;
	}

	/**
	 *
	 * @author zhiyu.long
	 * @date 2017年11月22日 上午10:21:57
	 * @param dataSourceName
	 * @param commonQuery
	 * @param inputParam
	 * @param tenantNumId
	 * @param dataSign
	 * @return
	 */
	@Override
	public List excuteSqlById(String dataSourceName, CommonQuery commonQuery,
			List<Long> inputParam, Long tenantNumId, Long dataSign) {
			String sql=commonQuery.getSqlContent();
			sql=sql.replace("D.RESERVED_NO = ?", "");
			sql=sql.replace("WM_BL_SHIP_DTL", " unitepos.WM_BL_SHIP_DTL ");
			sql=sql.replace("WM_BL_SHIP_CONTAINER_HDR", " unitepos.WM_BL_SHIP_CONTAINER_HDR ");
			sql=sql.replace("SD_BL_SO_TML_HDR", " unitepos.SD_BL_SO_TML_HDR ");
			sql=sql.replace("SD_BL_SO_TML_DTL", " unitepos.SD_BL_SO_TML_DTL ");
			sql=sql+"D.series in(select ITEMID from unitepos.abc)";
			sql="select (select b.id from  unitepos.abc b where b.itemid=a.series) ZT_RESERVED_NO, a.* from ("+
					sql+") a ";

			DataSourceContextHolder.clearDataSourceType();
			DataSourceContextHolder.setDataSourceType(dataSourceName);
			List list = dynamicJdbcTemplate.queryForList(sql);
			return list;
	}

	public List<Long> getSeries(String dataSourceName){
		DataSourceContextHolder.clearDataSourceType();
		DataSourceContextHolder.setDataSourceType(dataSourceName);
		List<Long> seriesList=dynamicJdbcTemplate.queryForList("select ITEMID from unitepos.abc",Long.class);
		return seriesList;
	}

	/**
	 *
	 * @date 2017年12月6日 下午4:36:15
	 */
	@Override
	public CommonQuery getCommonQuery(Long tenantNumId, Long dataSign,String sqlId) {
		CommonQueryGetRequest request=new CommonQueryGetRequest();
		request.setDataSign(dataSign);
		request.setTenantNumId(tenantNumId);
		request.setSqlId(sqlId);
		CommonQuery commonQuery= CacheUtil.getCache("getCommonQueryBySqlId", request, CommonQuery.class);
		commonQuery.setSqlContent(commonQuery.getSqlContent());
		return commonQuery;
	}

	@Override
	public ExcuteSqlResultModel parseSqlById(Long currentPage,
	                                         String dataSourceName, CommonQuery commonQuery,
	                                         JSONObject inputParam, Long tenantNumId, Long dataSign) {

		ExcuteSqlResultModel resModel = new ExcuteSqlResultModel();

		String sqlContent = commonQuery.getSqlContent();
		String jsonContent = commonQuery.getParamContent();
		JSONArray ja = JSONArray.fromObject(jsonContent);
		Map<String, Object> map = CommUtil.getParamsWithMap(commonQuery, ja, inputParam);
		sqlContent = CommUtil.sqlHandler(sqlContent, map);

		sqlContent = sqlContent.replaceAll("[\\[\\]]*", "");
		Long pageSize = commonQuery.getPageSize();
		resModel.setSql(sqlContent);
		List arrList= new ArrayList();
		Object os [] = map.values().toArray();
		for(Object o : os){
			arrList.add(o);
		}
		Object arr [] = arrList.toArray();
		resModel.setArg(arr);
		return resModel;
	}

	public void batchExcuteSql(List<BatchExcuteModel> excuteModelList, String dataSourceName) {
		try {
			int size = excuteModelList.size();
			for (int i = 0; i < size; i++) {
				BatchExcuteModel batchExcuteModel = excuteModelList.get(i);
				String sql = batchExcuteModel.getSql();
				final List<Object[]> argList  = batchExcuteModel.getArgList();
				final List<Object[]> noDataUpdateArgList   = batchExcuteModel.getNoDataUpdateArgList();
				if (StringUtil.isAllNullOrBlank(sql) || argList==null) {
					continue;
				}
				boolean useBatch = true;
				//有则修改，无则插入的处理,那麼不能批處理
				if(batchExcuteModel.isHasNoDataUpdate() ){
					useBatch = false;
				}
				if(useBatch){
					int arg[] = dynamicJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
							Object[] obj = argList.get(i);
							for (int j = 1; j <= obj.length; j++) {
								Object object = obj[j-1];
								preparedStatement.setObject(j, object);
							}
						}
						@Override
						public int getBatchSize() {
							return argList.size();
						}
					});
					//有则修改，无则插入的处理
					if(batchExcuteModel.isHasNoDataUpdate()){
						if(noDataUpdateArgList != null && !noDataUpdateArgList.isEmpty()){
							for (int j =  arg.length-1; j >= 0; j--) {
								int count = arg[j];
								if(count  > 0){
									noDataUpdateArgList.remove(j);
								}
							}
							if(!noDataUpdateArgList.isEmpty()){
								sql = batchExcuteModel.getNoDataUpdateSql();
								dynamicJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
									@Override
									public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
										Object[] obj = noDataUpdateArgList.get(i);
										for (int j = 1; j <= obj.length; j++) {
											Object object = obj[j-1];
											preparedStatement.setObject(j, object);
										}
									}
									@Override
									public int getBatchSize() {
										return noDataUpdateArgList.size();
									}
								});
							}
						}
					}
				}else{
					//每一條進行執行
					for (int j = argList.size() - 1; j >=0 ; j--) {
						int resCount = dynamicJdbcTemplate.update(sql,  argList.get(j));
						if(resCount <= 0){
							//執行配置的插入
							dynamicJdbcTemplate.update(batchExcuteModel.getNoDataUpdateSql(),  noDataUpdateArgList.get(j));
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new DatabaseOperateException(ExportUtil.SUB_SYSTEM,
					ExceptionType.DOE30052, "通用导入插入数据异常，异常信息：" + e.getMessage());
		}
	}
}
