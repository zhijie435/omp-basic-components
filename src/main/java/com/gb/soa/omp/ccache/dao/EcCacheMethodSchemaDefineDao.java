package com.gb.soa.omp.ccache.dao;

import com.gb.soa.omp.ccache.entity.EC_CACHE_METHOD_SCHEMA_DEFINE;
import com.gb.soa.omp.ccache.util.Constant;
import com.gb.soa.omp.ccommon.api.exception.DatabaseOperateException;
import com.gb.soa.omp.ccommon.api.exception.ExceptionType;
import com.gb.soa.omp.ccommon.util.MyJdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class EcCacheMethodSchemaDefineDao {
	@Resource(name="commonDossierJdbcTemplate")
    private MyJdbcTemplate commonDossierJdbcTemplate;

	//按方法获取
	public EC_CACHE_METHOD_SCHEMA_DEFINE getEntityByMethodName(String methodName){
		String sql = "select series,tenant_num_id,data_sign,sub_system,method_name,sql_content,db,cache_method,cache_multi_col,ttl,list_sign,allow_list_empty_sign,"+
				"create_dtme,last_updtme,create_user_id,last_update_user_id,description from ec_cache_method_schema_define "+
				"where tenant_num_id=0 and data_sign= 0 and method_name= ? ";
		EC_CACHE_METHOD_SCHEMA_DEFINE entity =commonDossierJdbcTemplate.queryForObject(sql, new Object[]{methodName},new BeanPropertyRowMapper<EC_CACHE_METHOD_SCHEMA_DEFINE>(EC_CACHE_METHOD_SCHEMA_DEFINE.class));
		if (entity==null){
			throw new DatabaseOperateException(Constant.SUB_SYSTEM,ExceptionType.DOE30042,"获取缓存对应sql定义失败,方法名:"+methodName);
		}
		return entity;
	}

	//按子系统获取
	public List<EC_CACHE_METHOD_SCHEMA_DEFINE> findEntityBySubSystemNotMustExist(String subSystem){
		String sql = "select series,tenant_num_id,data_sign,sub_system,method_name,sql_content,db,cache_method,cache_multi_col,ttl,list_sign,"+
				"create_dtme,last_updtme,create_user_id,last_update_user_id from ec_cache_method_schema_define where tenant_num_id=0 and data_sign= 0 and sub_system= ? ";
		List<EC_CACHE_METHOD_SCHEMA_DEFINE> list =commonDossierJdbcTemplate.query(sql, new Object[]{subSystem},new BeanPropertyRowMapper<EC_CACHE_METHOD_SCHEMA_DEFINE>(EC_CACHE_METHOD_SCHEMA_DEFINE.class));
		return list;
	}



}
