/*
 * PROJECT NAME: omp_cexport
 * PACKAGE NAME: com.gb.soa.omp.export.service
 * FILE    NAME: ExportServiceImpl.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */
package com.gb.soa.omp.export.service.impl;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.gb.soa.omp.ccommon.api.exception.BusinessException;
import com.gb.soa.omp.ccommon.api.exception.ExceptionType;
import com.gb.soa.omp.ccommon.datasource.DataSourceContextHolder;
import com.gb.soa.omp.ccommon.util.*;
import com.gb.soa.omp.exchange.model.CommonQuery;
import com.gb.soa.omp.exchange.model.DataExportModel;
import com.gb.soa.omp.exchange.model.ExcuteSqlResultModel;
import com.gb.soa.omp.export.api.model.CommonBatchInsert;
import com.gb.soa.omp.export.api.request.*;
import com.gb.soa.omp.export.api.response.*;
import com.gb.soa.omp.export.api.service.ExportDataService;
import com.gb.soa.omp.export.dao.CommonQueryDao;
import com.gb.soa.omp.export.dao.ExArcDocSystemDao;
import com.gb.soa.omp.export.service.CommonJsonQueryService;
import com.gb.soa.omp.export.util.ExportUtil;
import com.gb.soa.sequence.util.SeqGetUtil;
import com.ykcloud.soa.mapping.GatewayMapping;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出服务实现类
 * @author qiang.li
 * @date 2017年4月11日 下午1:47:31
 * @version <b>1.0.0</b>
 */
@Service("exportDataService")
@FeignClient(value = "@Value(\"${spring.application.name}\")")
@RestController
@GatewayMapping
public class ExportDataServiceImpl implements ExportDataService {
	private static final Logger log = LoggerFactory
			.getLogger(ExportDataServiceImpl.class);

	private static JsonMapper mapper;
	static {
		mapper = new JsonMapper();
		mapper.getMapper().setPropertyNamingStrategy(
				PropertyNamingStrategy.SNAKE_CASE);
	}

	@Resource(name = "commonQueryDao")
	private CommonQueryDao commonQueryDao;

	@Resource(name="exportCommonJsonQueryService")
	private CommonJsonQueryService commonJsonQueryService;

	@Value("${commom.query.cache.sign}")
	private boolean commomQueryCacheSign;

	@Resource(name = "commonQueryDyTransactionManager")
	private PlatformTransactionManager dynamicTransactionManager;

	@Resource
	private ExArcDocSystemDao exArcDocSystemDao;


	/**
	 *
	 * @author qiang.li
	 * @date 2017年4月11日 下午1:49:53
	 * @see ExportDataService#exportData(DataExportRequest)
	 * @param request
	 * @return
	 */
	@Override
	public DataExportResponse exportData(DataExportRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("begin exportData request:{}",mapper.toJson(request));
		}
		DataExportResponse response = new DataExportResponse();
		try {
			throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40160, "此方法已废弃!");
		} catch (Throwable e) {
			ExceptionUtil.processException(e, response);
		}
		if (log.isDebugEnabled()) {
			log.debug("end exportData return:{}",response.toLowerCaseJson());
		}
		return response;
	}



	@Override
	public DataCheckResponse checkData(DataCheckRequest request) {
		DataCheckResponse response = new DataCheckResponse();
		DataExportModel model = null;
		int i = 1;
		try {
			request.validate(ExportUtil.SUB_SYSTEM, ExceptionType.VCE10033);
			JSONArray configJsonAry = ExportUtil.getJsonArray(request
					.getConfigDtl());
			for (; i <= configJsonAry.size(); i++) {
				model = mapper.fromJson(configJsonAry.get(i - 1).toString(),
						DataExportModel.class);
				ExportUtil.checkDataExportModel(model, i);
			}
		} catch (Exception e) {
			ExceptionUtil.processException(e, response);
		}
		return response;
	}

	@Override
	public CommonExcuteBySqlIdResponse commonExcuteBySqlId(CommonExcuteBySqlIdRequest request) {
		CommonExcuteBySqlIdResponse response = new CommonExcuteBySqlIdResponse();
		try {
			long start = System.currentTimeMillis();
			List<String> sqlidList = new ArrayList<String>();
			commonExcuteBySqlId(request,response,sqlidList);
			long time = System.currentTimeMillis() -start ;
			if (time>=1000) {
				log.info("通用查询耗时超过一秒 sqlId:" +request.getSqlId()+"查询时间:"+time+"参数:"+request.getInputParam().toString()+",返回集合大小:"+response.getRecordCount());
			}
		} catch (Exception e) {
			ExceptionUtil.processException(e, response);
		}
		return response;
	}
	private void commonExcuteBySqlId(CommonExcuteBySqlIdRequest request,CommonExcuteBySqlIdResponse response, List<String> sqlidList){
		CommonQuery commonQuery;
		if (commomQueryCacheSign) {
			commonQuery = commonJsonQueryService.getCommonQuery(request.getTenantNumId(), request.getDataSign(), request.getSqlId());
		}else{
			commonQuery = commonQueryDao.getModelWithTenant(request.getSqlId(), request.getTenantNumId(), request.getDataSign());
			if (commonQuery==null) {
				commonQuery = commonQueryDao.getModelNoTenant(request.getSqlId(), request.getDataSign());
			}
		}
		if (log.isDebugEnabled()) {
			log.info(mapper.toJson(commonQuery));
		}
		response.setSqlName(commonQuery.getSqlName());
		Long currentPage = 0L;
		if (request.getInputParam()==null) {
			request.setInputParam(new HashMap<String, Object>());
		}
		request.getInputParam().put("tenant_num_id", request.getTenantNumId());
		request.getInputParam().put("data_sign", request.getDataSign());
		//JSONObject inputParam =new JSONObject();
		Map<String,Object> inputParam=new HashMap<String,Object>();
		inputParam.putAll(request.getInputParam());
		if (request.getDataSourceName()==null||request.getDataSourceName().isEmpty()) {
			request.setDataSourceName(commonQuery.getJdbcName());
		}
		if (request.getPageSize()!=null) {
			commonQuery.setPageSize(request.getPageSize());
		}
		if (request.getPageNum()!=null) {
			currentPage = request.getPageNum();
		}
		ExcuteSqlResultModel resultModel=new ExcuteSqlResultModel();
		if (log.isDebugEnabled()) {
			log.info("resultModel:"+mapper.toJson(request.getInputParam()));
		}
		//通用更新先处理子查询  通过自查旬进行参数转换
		if (commonQuery.getSqlFlag().equals(CommonQuery.SQL_FLAG_INSERT)||commonQuery.getSqlFlag().equals(CommonQuery.SQL_FLAG_INSERT)) {
			if (commonQuery.getSubSqlId()!=null&&!commonQuery.getSubSqlId().isEmpty()) {
				String[] sqlId=commonQuery.getSubSqlId().split(",");
				for(int j=0;j<sqlId.length;j++){
					CommonExcuteBySqlIdRequest subRequest=new CommonExcuteBySqlIdRequest();
					subRequest.setSubRuerySign(2L);
					subRequest.setDataSign(request.getDataSign());
					subRequest.setInputParam(inputParam);
					subRequest.setSqlId(sqlId[j]);
					subRequest.setTenantNumId(request.getTenantNumId());
					subRequest.setCount(0);
					List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
					list.add(new HashMap<String, Object>());
					response.setResults(list);
					commonExcuteBySqlId(subRequest, response, sqlidList);
					inputParam.putAll(response.getResults().get(0));
				}
			}
		}
		if (request.getSubRuerySign().equals(2L)) {
			commonQuery.setSubQuerySign(2L);
		}
		sqlidList.add(commonQuery.getSqlId());
		resultModel = commonJsonQueryService.excuteSqlById(currentPage,
					request.getDataSourceName(), commonQuery,
					inputParam, request.getTenantNumId(),request.getDataSign());
		if (commonQuery.getNoDataException().equals("Y")&&resultModel.getRecordCount()<1L) {
			throw new BusinessException(ExportUtil.SUB_SYSTEM,ExceptionType.BE40203, "通过sqlId:"+request.getSqlId()+"没有查询到记录!");
		}
		if (log.isDebugEnabled()) {
			log.info("resultModel:"+mapper.toJson(resultModel));
		}
		if (request.getSubRuerySign().equals(1L)) {
			response.setPageCount(resultModel.getPageCount());
			response.setRecordCount(resultModel.getRecordCount());
		}
		if (request.getSubRuerySign().equals(2L)) {
			if (resultModel.getRecordCount()>1L) {//子查询多条记录报错
				 throw new BusinessException(ExportUtil.SUB_SYSTEM,ExceptionType.BE40202, "数据异常！子查询记录存在多条");
			}
			if (!resultModel.getData().isEmpty()) {
				response.getResults().get(request.getCount()).putAll(resultModel.getData().get(0));
			}
			if (commonQuery.getSubSqlId()!=null&&!commonQuery.getSubSqlId().isEmpty()) {
				Map<String,Object> subSqlInputParam=new HashMap<String,Object>();
				subSqlInputParam.putAll(resultModel.getData().get(0));
				subSqlInputParam.put("tenant_num_id", request.getTenantNumId());
				subSqlInputParam.put("data_sign", request.getDataSign());
				String[] sqlId=commonQuery.getSubSqlId().split(",");
				for(int j=0;j<sqlId.length;j++){
					CommonExcuteBySqlIdRequest subRequest=new CommonExcuteBySqlIdRequest();
					subRequest.setSubRuerySign(2L);
					subRequest.setDataSign(request.getDataSign());
					//subRequest.setInputParam(jsonObject);
					subRequest.setInputParam(subSqlInputParam);
					subRequest.setSqlId(sqlId[j]);
					subRequest.setTenantNumId(request.getTenantNumId());
					subRequest.setCount(request.getCount());
					commonExcuteBySqlId(subRequest, response, sqlidList);
				}
			}
		}else if (request.getSubRuerySign().equals(1L)) {
			response.setResults(new ArrayList(Long.valueOf(resultModel.getCount()).intValue()));
			for (int i=0; i<resultModel.getCount();i++) {
				if (resultModel.getSqlFlag().equals("I")||resultModel.getSqlFlag().equals("U")) {
				}else{
					response.getResults().add(resultModel.getData().get(i));
				}
				if (commonQuery.getSubSqlId()!=null&&!commonQuery.getSubSqlId().isEmpty()) {
					Map<String,Object> subSqlInputParam=new HashMap<String,Object>();
					subSqlInputParam.putAll(resultModel.getData().get(i));
					subSqlInputParam.put("tenant_num_id", request.getTenantNumId());
					subSqlInputParam.put("data_sign", request.getDataSign());
					String[] sqlId=commonQuery.getSubSqlId().split(",");
					for(int j=0;j<sqlId.length;j++){
						CommonExcuteBySqlIdRequest subRequest=new CommonExcuteBySqlIdRequest();
						subRequest.setSubRuerySign(2L);
						subRequest.setDataSign(request.getDataSign());
						//subRequest.setInputParam(jsonObject);
						subRequest.setInputParam(subSqlInputParam);
						subRequest.setSqlId(sqlId[j]);
						subRequest.setTenantNumId(request.getTenantNumId());
						subRequest.setCount(i);
						commonExcuteBySqlId(subRequest, response, sqlidList);
					}
				}
			}
			if (log.isDebugEnabled()) {
				log.info("response:"+mapper.toJson(response));
			}
		}
		response.setSqlFlag(resultModel.getSqlFlag());
		response.setSql(resultModel.getSql());
		response.setArg(resultModel.getArg());
	}

	/**
	 *
	 */
	@Override
	public CommonBatchUpdateExcuteResponse excuteCommonBatchUpdate(CommonBatchUpdateExcuteRequest request) {
		CommonBatchUpdateExcuteResponse response = new CommonBatchUpdateExcuteResponse();
		TransactionStatus status =null;
		try {
			if (log.isDebugEnabled()) {
				log.debug(mapper.toJson(request));
			}
			request.validate(ExportUtil.SUB_SYSTEM, ExceptionType.VCE10033);
			String dataSource = null;
			if (StringUtil.isNullOrBlankTrim(request.getDataSourceName())) {
				CommonQuery commonQuery = commonQueryDao.getModelNoTenant(request.getInputParams().get(0).getSqlId(), request.getDataSign());
				dataSource = commonQuery.getJdbcName();
			}else{
				dataSource = request.getDataSourceName();
			}
			DataSourceContextHolder.setDataSourceType(dataSource);
			status = dynamicTransactionManager.getTransaction(TransactionUtil.newTransactionDefinition());
			List<CommonBatchInsert> inputParams = request.getInputParams();
			for (CommonBatchInsert param : inputParams) {
				for (int i = 0; i < param.getParamList().size(); i++) {
					CommonExcuteBySqlIdRequest excuteRequest = new CommonExcuteBySqlIdRequest();
					excuteRequest.setTenantNumId(request.getTenantNumId());
					excuteRequest.setDataSign(request.getDataSign());
					excuteRequest.setInputParam(param.getParamList().get(i));
					excuteRequest.setSqlId(param.getSqlId());
					CommonExcuteBySqlIdResponse excuteResponse = commonExcuteBySqlId(excuteRequest);
					ExceptionUtil.checkDubboException(excuteResponse);
				}
			}
			dynamicTransactionManager.commit(status);
		} catch (Exception e) {
			if (status!=null) {
				dynamicTransactionManager.rollback(status);
			}
			ExceptionUtil.processException(e, response);
		}
		return response;
	}

	/**
	 *
	 * @author zhiyu.long
	 * @date 2017年9月27日 下午3:03:54
	 * @param request
	 * @return
	 */
	@Override
	public MessageCommonRefoundResponse messageCommonRefound(MessageCommonRefoundRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("begin messageCommonRefound request:{}",mapper.toJson(request));
		}
		MessageCommonRefoundResponse response = new MessageCommonRefoundResponse();
		try {
			request.validate(ExportUtil.SUB_SYSTEM, ExceptionType.VCE10034);
			Long tenantNumId=request.getTenantNumId();
			Long dataSign=request.getDataSign();
			Long sysNumId=request.getSysNumId();
			String msgSeries=request.getMsgSeries();
			String datasourceName;
			if (StringUtil.isAllNotNullOrBlank(request.getDatasouceName())){
				datasourceName=request.getDatasouceName();
			} else {
				datasourceName=exArcDocSystemDao.getDatasourceNameBySysNumId(tenantNumId, dataSign, sysNumId);
			}
			String confirmSeries=exArcDocSystemDao.getMessageSeriesFromSysMsgRefound(msgSeries,datasourceName);
			if (confirmSeries==null) {
				throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40160, "事务消息回查消息序号查询失败!消息序号:"+msgSeries);
			}
		} catch (Throwable e) {
			ExceptionUtil.processException(e, response);
		}
		if (log.isDebugEnabled()) {
			log.debug("end messageCommonRefound return:{}",response.toLowerCaseJson());
		}
		return response;
	}

	/**
	 * 获取一个序列
	 */
	@Override
	public SequenceClientSeriesGetResponse getNextSequence(SequenceClientSeriesGetRequest request) {
		Long sequenceNum = null;
		SequenceClientSeriesGetResponse response = new SequenceClientSeriesGetResponse();
		try {
			sequenceNum = SeqGetUtil.getNoSubSequence(request.getSeriesName());
			response.setSequenceNum(sequenceNum);
		} catch (Exception e) {
			response.setCode(-1L);
			response.setMessage(e.getMessage());
//			throw new SequenceException(request.getSeriesName() + "获取序列失败:原因" + e.getMessage());
		}
		return response;
	}

	/**
	 * 批次获取序列
	 */
	@Override
	public SequenceSeriesListGetResponse getListSequences(SequenceClientSeriesGetRequest request) {
		List<Long> list = new ArrayList<Long>();
		SequenceSeriesListGetResponse response = new SequenceSeriesListGetResponse();
		try {
			if (request.getSize() > 0) {
				for (int i = 0; i < request.getSize(); i++) {
					SequenceClientSeriesGetResponse nextSequence = getNextSequence(request);
					list.add(nextSequence.getSequenceNum());
				}
			}

		} catch (Exception e) {
			response.setCode(-1L);
			response.setMessage(e.getMessage());
//			throw new SequenceException(request.getSeriesName() + "批次获取序列失败:原因" + e.getMessage());
		}
		response.setSequenceList(list);
		return response;
	}

	@Override
	public CcoreAutoGrowSequenceResponse getAutoGrowSequence(CcoreAutoGrowSequenceRequest request) {
		String autoSeqNum = null;

		CcoreAutoGrowSequenceResponse response = new CcoreAutoGrowSequenceResponse();
		try {
			// 验证数据库中是否存在
//			Integer count = platformSmsTemplateDao.queryExistSequence(request.getSeqName(), request.getTenantNumId(),
//					request.getDataSign());
//			if (count <= 0) {
//				response.setCode(-1);
//				response.setMessage("未查询到匹配的自增序列,序列为：" + request.getSeqName());
//				return response;
//			}
			autoSeqNum = SeqGetUtil.getAutomicSequence(request.getSeqName(), request.getCacheNum(),
					request.getTenantNumId(), request.getDataSign());
			response.setAutoSeqNum(autoSeqNum);
		} catch (Exception e) {
			response.setCode(-1L);
			response.setMessage(e.getMessage());
//			throw new SequenceException(request.getSeqName() + "获取序列失败:原因" + e.getMessage());
		}
		return response;
	}
}
