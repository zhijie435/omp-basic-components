package com.gb.soa.omp.export.service;

import java.util.List;
import java.util.Map;

import com.gb.soa.omp.export.service.model.BatchExcuteModel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.gb.soa.omp.exchange.model.CommonQuery;
import com.gb.soa.omp.exchange.model.ExcuteSqlResultModel;

/**
 * 通用查询
 * 在common_query中配置查询语句的SQL，然后调用该方法查询出结果集
 * @author Tigers.Chen
 *
 */
public interface CommonJsonQueryService {
	
	//public ExcuteSqlResultModel excuteSqlById(Long currentPage,String dataSourceName,CommonQuery commonQuery,JSONObject inputParam,Long tenantNumId, Long dataSign );
	public ExcuteSqlResultModel excuteSqlById(Long currentPage,String dataSourceName,CommonQuery commonQuery,Map<String,Object> inputParam,Long tenantNumId, Long dataSign );
	
	public List excuteSqlById(String dataSourceName,CommonQuery commonQuery,List<Long> inputParam,Long tenantNumId, Long dataSign);
	
//	public void excuteSqlByIdForUpdate(String dataSourceName,CommonQuery commonQuery,List<Map<String,Object>> inputParam,Long tenantNumId, Long dataSign);

	public List<Long> getSeries(String dataSourceName);
	
	public CommonQuery getCommonQuery(Long tenantNumId,Long dataSign,String sqlId);

	public ExcuteSqlResultModel parseSqlById(Long currentPage, String dataSourceName, CommonQuery commonQuery,
	                                         JSONObject inputParam, Long tenantNumId, Long dataSign);

	public void batchExcuteSql(List<BatchExcuteModel> excuteModelList,
	                           String dataSourceName);
}
