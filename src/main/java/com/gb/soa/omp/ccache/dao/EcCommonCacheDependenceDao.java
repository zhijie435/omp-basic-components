package com.gb.soa.omp.ccache.dao;

import com.gb.soa.basic.config.SeqConfig;
import com.gb.soa.omp.ccache.entity.EC_COMMON_CACHE_DEPENDENCE;
import com.gb.soa.omp.ccommon.api.exception.DatabaseOperateException;
import com.gb.soa.omp.ccommon.api.exception.ExceptionType;
import com.gb.soa.omp.ccommon.util.MyJdbcTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.gb.soa.omp.ccache.util.Constant.SUB_SYSTEM;

/**
 * @auther: wenfeng
 * @date: 2020/6/12 11:09
 */
@Repository
public class EcCommonCacheDependenceDao {

	@Value("${db.annotate.prefix}")
	private String dbAnnotatePrefix;

    @Resource(name = "msgcenterJdbcTemplate")
    private MyJdbcTemplate msgcenterJdbcTemplate;

	//批量插入
	public void batchInsertEntity(List<EC_COMMON_CACHE_DEPENDENCE> entityList) {
		String sql = "insert into ec_common_cache_dependence(series,tenant_num_id,data_sign,method_name,db,cache_key,params,"+
					"table_name,table_series,create_dtme,last_updtme,create_user_id,last_update_user_id,dubbo_group"
				+ ") values (?,?,?,?,?,?,?,?,?,now(),now(),?,?,?)";
		int[] num = msgcenterJdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return entityList.size();
					}
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						EC_COMMON_CACHE_DEPENDENCE entity = entityList.get(i);
						int index = 1;
						entity.setSERIES(SeqConfig.getSeqNextValue(SeqConfig.EC_COMMON_CACHE_DEPENDENCE_SERIES));
						ps.setString(index++, entity.getSERIES());
						ps.setLong(index++, entity.getTENANT_NUM_ID());
						ps.setLong(index++, entity.getDATA_SIGN());
						ps.setString(index++, entity.getMETHOD_NAME());
						ps.setString(index++, entity.getDB());
						ps.setString(index++, entity.getCACHE_KEY());
						ps.setString(index++, entity.getPARAMS());
						ps.setString(index++, entity.getTABLE_NAME());
						ps.setString(index++, entity.getTABLE_SERIES());
						ps.setLong(index++, entity.getCREATE_USER_ID());
						ps.setLong(index++, entity.getLAST_UPDATE_USER_ID());
						ps.setString(index++, entity.getDUBBO_GROUP());
					}
				});
		if (num.length!=entityList.size()) {
			throw new DatabaseOperateException(SUB_SYSTEM,ExceptionType.DOE30042,"批量插入缓存方法值关联表记录失败!");
		}
	}

    public List<EC_COMMON_CACHE_DEPENDENCE> getCommonCacheDependenceByTableNameAndSeries(Long tenantNumId, Long dataSign,String db, String tableName, Long series) {
		String sql = "select series,tenant_num_id,data_sign,method_name,db,cache_key,params,table_name,table_series,create_dtme,last_updtme,create_user_id,last_update_user_id,dubbo_group from ec_common_cache_dependence where data_sign=? and table_name=? and table_series=? and db=? ";
		if (!tenantNumId.equals(0L)) {
			sql += String.format(" and tenant_num_id=%s  ", tenantNumId);
		}
		return msgcenterJdbcTemplate.query(sql, new Object[]{dataSign, tableName, series, db}, new BeanPropertyRowMapper<>(EC_COMMON_CACHE_DEPENDENCE.class));
    }

    public void deleteEntityByMethodNameAndCacheKey(Long tenantNumId,Long dataSign,String methodName,String cacheKey) {
        String sql ="delete from ec_common_cache_dependence where tenant_num_id =? and data_sign =? and cache_key=? and method_name=? ";
        int row =msgcenterJdbcTemplate.update(sql, tenantNumId,dataSign,cacheKey,methodName);
        if (row<1) {
            throw new DatabaseOperateException(SUB_SYSTEM, ExceptionType.DOE30042, "删除已过期缓存方法值关联表记录失败!方法名:"+methodName+",缓存键值:"+cacheKey);
        }
    }

	/***
	 * 查询所有存在
	 * @param tenantNumId
	 * @param dataSign
	 * @param methodName
	 * @param cacheKey
	 * @return
	 */
	public List<String> getSeriesByMethodNameAndCacheKey(Long tenantNumId,Long dataSign,String methodName,String cacheKey,String dubboGroup){
		String sql= dbAnnotatePrefix+"select series from ec_common_cache_dependence where tenant_num_id =? and data_sign =? and cache_key=? and method_name=? and dubbo_group=? ";
		List<String> series = msgcenterJdbcTemplate.queryForList(sql,new Object[]{tenantNumId,dataSign,cacheKey,methodName,dubboGroup},String.class);
		return series;
	}

	public List<String> getSeriesByMethodNameAndCacheKey(Long tenantNumId,Long dataSign,String methodName,String cacheKey){
		String sql= dbAnnotatePrefix+"select series from ec_common_cache_dependence where tenant_num_id =? and data_sign =? and cache_key=? and method_name=? ";
		List<String> series = msgcenterJdbcTemplate.queryForList(sql,new Object[]{tenantNumId,dataSign,cacheKey,methodName},String.class);
		return series;
	}

	/***
	 * 删除所有数据
	 * @param tenantNumId
	 * @param dataSign
	 * @param methodName
	 * @param cacheKey
	 * @param series
	 */
	public void deleteEntityByMethodNameAndCacheKeySeries(Long tenantNumId,Long dataSign,String methodName,String cacheKey,String series) {
		String sql ="delete from ec_common_cache_dependence where tenant_num_id =? and data_sign =? and cache_key=? and method_name=? and series=? ";
		int row =msgcenterJdbcTemplate.update(sql, tenantNumId,dataSign,cacheKey,methodName,series);
		if (row<0) {
			throw new DatabaseOperateException(SUB_SYSTEM, ExceptionType.DOE30042, "删除已过期缓存方法值关联表记录失败!方法名:"+methodName+",缓存键值:"+cacheKey);
		}
	}
}


