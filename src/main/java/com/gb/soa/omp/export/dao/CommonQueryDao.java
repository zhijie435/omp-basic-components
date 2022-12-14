/*
 * PROJECT NAME: omp_cbillsync
 * PACKAGE NAME: com.gb.soa.omp.billsync.dao
 * FILE    NAME: GenericQueryDao.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */
package com.gb.soa.omp.export.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gb.soa.omp.ccommon.api.exception.DatabaseOperateException;
import com.gb.soa.omp.ccommon.api.exception.ExceptionType;
import com.gb.soa.omp.ccommon.datasource.DataSourceContextHolder;
import com.gb.soa.omp.ccommon.util.StringUtil;
import com.gb.soa.omp.exchange.model.CommonQuery;
import com.gb.soa.omp.export.entity.COMMON_QUERY;
import com.gb.soa.omp.export.util.ExportUtil;

/**
 * GenericQueryDao
 * @author qiang.li
 * @date 2016年10月19日 上午10:59:17
 * @version <b>1.0.0</b>
 */
@Repository("commonQueryDao")
public class CommonQueryDao {

	@Resource(name = "commonQueryDyJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	private final static String dataSource = "dataexchangeDataSource";

	// 通用SQL
	public static final String SELECT_GENERICQUERY_BY_SQLID_NO_TENANT = "SELECT series, sql_name AS sqlName, sql_id AS sqlId, sql_content AS sqlContent, param_content AS paramContent, jdbc_name AS jdbcName, create_user_id AS createUserId, 'N' as cancelSign, tenant_num_id AS tenantNumId, data_sign AS dataSign, db_type AS dbType,annotate_prefix AS annotatePrefix,sub_sql_id,no_data_exception,cache_sign,method_name,cache_live_time,return_handle_content FROM common_query WHERE sql_id = ?  and data_sign = ?";

	// 通用SQL
	public static final String SELECT_GENERICQUERY_BY_SQLID_WITH_TENANT = "SELECT series, sql_name AS sqlName, sql_id AS sqlId, sql_content AS sqlContent, param_content AS paramContent, jdbc_name AS jdbcName, create_user_id AS createUserId, 'N' as cancelSign, tenant_num_id AS tenantNumId, data_sign AS dataSign, db_type AS dbType,annotate_prefix AS annotatePrefix,sub_sql_id,no_data_exception,cache_sign,method_name,cache_live_time FROM common_query WHERE sql_id = ? and tenant_num_id = ? and data_sign = ?";

	public CommonQuery getModelWithTenant(String sqlId, Long tenantNumId, Long dataSign) {
		DataSourceContextHolder.clearDataSourceType();
		DataSourceContextHolder.setDataSourceType(dataSource);
		CommonQuery gq = jdbcTemplate.queryForObject(CommonQueryDao.SELECT_GENERICQUERY_BY_SQLID_WITH_TENANT,
						new Object[] { sqlId, tenantNumId, dataSign },
						new BeanPropertyRowMapper<CommonQuery>(
								CommonQuery.class));
		DataSourceContextHolder.clearDataSourceType();
		return gq;
	}

	public void updateModelBySeries(COMMON_QUERY model) {
		DataSourceContextHolder.clearDataSourceType();
		DataSourceContextHolder.setDataSourceType(dataSource);
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("UPDATE common_query  set ");
		List<Object> paramList = new ArrayList<Object>();
		if(!StringUtil.isNullOrBlankTrim(model.getSQL_ID())){
			sqlBuffer.append("sql_id = ?,");
			paramList.add(model.getSQL_ID());
		}
		if(!StringUtil.isNullOrBlankTrim(model.getSQL_NAME())){
			sqlBuffer.append("sql_name = ?,");
			paramList.add(model.getSQL_NAME());
		}
		if(!StringUtil.isNullOrBlankTrim(model.getSQL_CONTENT())){
			sqlBuffer.append("sql_content = ?,");
			paramList.add(model.getSQL_CONTENT());
		}
		if(!StringUtil.isNullOrBlankTrim(model.getPARAM_CONTENT())){
			sqlBuffer.append("param_content = ?,");
			paramList.add(model.getSQL_CONTENT());
		}
		if(!StringUtil.isNullOrBlankTrim(model.getJDBC_NAME())){
			sqlBuffer.append("jdbc_name = ?,");
			paramList.add(model.getJDBC_NAME());
		}
		if(!StringUtil.isNullOrBlankTrim(model.getDB_TYPE())){
			sqlBuffer.append("db_type = ?,");
			paramList.add(model.getDB_TYPE());
		}
		if(sqlBuffer.length()<=0){
			throw new DatabaseOperateException(ExportUtil.SUB_SYSTEM,ExceptionType.DOE30051, "更新表common_query的属性不可以为空！");
		}
		//删除逗号
		sqlBuffer.delete(sqlBuffer.length()-1, sqlBuffer.length());
		if(!StringUtil.isNullOrBlankTrim(model.getSERIES())){
			sqlBuffer.append("  where series = ?");
			paramList.add(model.getSERIES());
		}else{
			throw new DatabaseOperateException(ExportUtil.SUB_SYSTEM,ExceptionType.DOE30051, "更新表common_query的序列号不可以为空！");
		}
	   if(jdbcTemplate.update(sqlBuffer.toString(), paramList.toArray())<=0){
		   throw new DatabaseOperateException(ExportUtil.SUB_SYSTEM,ExceptionType.DOE30051, "更新表common_query失败！");
	   }
	}

	/**
	 * @author zhiyu.long
	 * @date 2018年2月7日 下午2:41:58
	 */
	public CommonQuery getModelNoTenant(String sqlId, Long dataSign) {
		DataSourceContextHolder.clearDataSourceType();
		DataSourceContextHolder.setDataSourceType(dataSource);
		CommonQuery gq = jdbcTemplate.queryForObject(CommonQueryDao.SELECT_GENERICQUERY_BY_SQLID_NO_TENANT,
				new Object[] { sqlId, dataSign },
				new BeanPropertyRowMapper<CommonQuery>(
						CommonQuery.class));
		if (gq == null) {
			throw new DatabaseOperateException(ExportUtil.SUB_SYSTEM,
					ExceptionType.DOE30051, "在通用查询配置没有sql" + sqlId + "对应的记录!");
		}
		DataSourceContextHolder.clearDataSourceType();
		return gq;
	}


	/**
	 */
	public CommonQuery getNoTenantModel(String sqlId, Long dataSign) {
		DataSourceContextHolder.clearDataSourceType();
		DataSourceContextHolder.setDataSourceType(dataSource);
		CommonQuery gq = jdbcTemplate
				.queryForObject(
						"SELECT series, sql_name AS sqlName, sql_id AS sqlId, sql_content AS sqlContent, param_content AS paramContent, jdbc_name AS jdbcName, create_user_id AS createUserId, 'N' as cancelSign, tenant_num_id AS tenantNumId, data_sign AS dataSign, db_type AS dbType,annotate_prefix AS annotatePrefix,sub_sql_id,no_data_exception,cache_sign,method_name,cache_live_time,return_handle_content FROM common_query WHERE sql_id = ?  and data_sign = ?",
						new Object[] { sqlId, dataSign },
						new BeanPropertyRowMapper<CommonQuery>(
								CommonQuery.class));
		if (gq == null) {
			throw new DatabaseOperateException(ExportUtil.SUB_SYSTEM,
					ExceptionType.DOE30052, "没有找到" + sqlId + "对应的sql数据");
		}
		DataSourceContextHolder.clearDataSourceType();
		return gq;
	}

}
