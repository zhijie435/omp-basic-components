package com.gb.soa.omp.ccache.service.impl;

import com.gb.soa.omp.ccache.service.CacheCommonService;
import com.gb.soa.omp.ccommon.datasource.DataSourceContextHolder;
import com.gb.soa.omp.ccommon.datasource.DynamicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author gang.wang
 * @version <b>1.0.0</b>
 * @date 2017年2月6日 下午2:19:12
 */
@Service
public class CacheCommonServiceImpl implements CacheCommonService {

    @Resource(name = "commonCacheDyDataSource")
    DynamicDataSource dataSource;

    public JdbcTemplate getJdbcTemplate(String db) {
        DataSourceContextHolder.setDataSourceType(db);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }


}
