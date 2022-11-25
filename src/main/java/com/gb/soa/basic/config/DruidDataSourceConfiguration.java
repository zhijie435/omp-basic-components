package com.gb.soa.basic.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.gb.soa.omp.ccommon.datasource.DynamicDataSource;
import com.gb.soa.omp.ccommon.util.MyJdbcTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Configuration
public class DruidDataSourceConfiguration {

    @Value("${db.annotate.prefix}")
    private String dbAnnotatePrefix;

    @Bean(name = "commonCacheDyDataSource")
    public DynamicDataSource commonCacheDataSource() {
        DynamicDataSource dataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("commonDossier", commondossierDataSource());
        targetDataSources.put("tenantdoss", tenantdossDataSource());
        targetDataSources.put("tenantInventory", tenanstorDataSource());
        targetDataSources.put("dataExchange", dataexchangeDataSource());
        targetDataSources.put("tenantOrder", tenanordDataSource());
        targetDataSources.put("masterData", masterDataSource());
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(commondossierDataSource());
        return dataSource;
    }

    @Bean(name = "commonQueryDyDataSource")
    public DynamicDataSource commonQueryDyDataSource() {
        DynamicDataSource dataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("commondossierDataSource", commondossierDataSource());
        targetDataSources.put("tenantdossDataSource", tenantdossDataSource());
        targetDataSources.put("tenanstorDataSource", tenanstorDataSource());
        targetDataSources.put("dataexchangeDataSource", dataexchangeDataSource());
        targetDataSources.put("tenanordDataSource", tenanordDataSource());
        targetDataSources.put("mdmDataSource", masterDataSource());
        targetDataSources.put("financeDataSource", financeataSource());
        targetDataSources.put("logisticsDataSource", logisticsDataSource());
        targetDataSources.put("supplychainDataSource", supplychainDataSource());
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(commondossierDataSource());
        return dataSource;
    }

    @Bean(name = "commonQueryDyJdbcTemplate")
    public MyJdbcTemplate commonQueryDyJdbcTemplate() {
        MyJdbcTemplate jdbcTemplate = new MyJdbcTemplate();
        jdbcTemplate.setDataSource(commonQueryDyDataSource());
        return jdbcTemplate;
    }

    @Bean(name = "commonQueryDyTransactionManager")
    public DataSourceTransactionManager commonQueryDyTransactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(commonQueryDyDataSource());
        tm.setNestedTransactionAllowed(true);
        return tm;
    }

    @Bean(name = "platformDataSource")
    @ConfigurationProperties("spring.datasource.druid.platform")
    public DruidDataSource platformDataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    @Bean(name = "platformJdbcTemplate")
    public MyJdbcTemplate platformJdbcTemplate() {
        MyJdbcTemplate jdbcTemplate = new MyJdbcTemplate();
        jdbcTemplate.setDataSource(platformDataSource());
        return jdbcTemplate;
    }

    @Bean(name = "platformTransactionManager")
    public DataSourceTransactionManager platformTransactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(platformDataSource());
        tm.setNestedTransactionAllowed(true);
        return tm;
    }

    @Bean(name = "commonDossierDataSource")
    @ConfigurationProperties("spring.datasource.druid.commondossier")
    public DruidDataSource commondossierDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "commonDossierJdbcTemplate")
    public MyJdbcTemplate commondossierJdbcTemplate() {
        MyJdbcTemplate jdbcTemplate = new MyJdbcTemplate();
        jdbcTemplate.setDataSource(commondossierDataSource());
        return jdbcTemplate;
    }

    @Bean(name = "masterDataSource")
    @ConfigurationProperties("spring.datasource.druid.mdm")
    public DruidDataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "masterJdbcTemplate")
    public MyJdbcTemplate masterJdbcTemplate() {
        MyJdbcTemplate jdbcTemplate = new MyJdbcTemplate();
        jdbcTemplate.setDataSource(masterDataSource());
        return jdbcTemplate;
    }

    @Bean(name = "msgcenterDataSource")
    @ConfigurationProperties("spring.datasource.druid.msgcenter")
    public DruidDataSource msgcenterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "msgcenterJdbcTemplate")
    public MyJdbcTemplate msgcenterJdbcTemplate() {
        MyJdbcTemplate jdbcTemplate = new MyJdbcTemplate();
        jdbcTemplate.setDataSource(msgcenterDataSource());
        return jdbcTemplate;
    }

    @Bean(name = "msgcenterTransactionManager")
    public DataSourceTransactionManager msgcenterTransactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(msgcenterDataSource());
        tm.setNestedTransactionAllowed(true);
        return tm;
    }

    @Bean(name = "tenantdossDataSource")
    @ConfigurationProperties("spring.datasource.druid.tenantdoss")
    public DruidDataSource tenantdossDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "tenanstorDataSource")
    @ConfigurationProperties("spring.datasource.druid.tenanstor")
    public DruidDataSource tenanstorDataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    @Bean(name = "dataexchangeDataSource")
    @ConfigurationProperties("spring.datasource.druid.dataexchange")
    public DruidDataSource dataexchangeDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dataexchangeJdbcTemplate")
    public MyJdbcTemplate dataexchangeJdbcTemplate() {
        MyJdbcTemplate jdbcTemplate = new MyJdbcTemplate();
        jdbcTemplate.setDataSource(dataexchangeDataSource());
        return jdbcTemplate;
    }

    @Bean(name = "tenanordDataSource")
    @ConfigurationProperties("spring.datasource.druid.tenanord")
    public DruidDataSource tenanordDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "financeDataSource")
    @ConfigurationProperties("spring.datasource.druid.finance")
    public DruidDataSource financeataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    @Bean(name = "logisticsDataSource")
    @ConfigurationProperties("spring.datasource.druid.logistics")
    public DruidDataSource logisticsDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "supplychainDataSource")
    @ConfigurationProperties("spring.datasource.druid.supplychain")
    public DruidDataSource supplychainDataSource() {
        return DruidDataSourceBuilder.create().build();
    }
}
