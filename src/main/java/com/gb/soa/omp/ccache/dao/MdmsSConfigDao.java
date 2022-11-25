package com.gb.soa.omp.ccache.dao;

import com.gb.soa.omp.ccommon.util.MyJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @auther: wenfeng
 * @date: 2020/6/12 11:54
 */
@Repository
public class MdmsSConfigDao {
    @Resource(name = "masterJdbcTemplate")
    private MyJdbcTemplate jdbcTemplate;

    public String getConfigValueByConfigName(Long tenantNumId, Long dataSign, String configName) {
        String sql = "select config_value from mdms_s_config where tenant_num_id=? and data_sign=? and config_name=? limit 1 ";
        return jdbcTemplate.queryForObject(sql, new Object[]{tenantNumId,dataSign,configName}, String.class);
    }

}
