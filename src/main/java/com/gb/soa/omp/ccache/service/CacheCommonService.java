/*
 * PROJECT NAME: omp_ccache
 * PACKAGE NAME: com.gb.soa.omp.ccache.service
 * FILE    NAME: CacheCommoneService.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */
package com.gb.soa.omp.ccache.service;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author gang.wang
 * @date 2017年2月6日 下午2:18:31
 * @version <b>1.0.0</b>
 */
public interface CacheCommonService {

	public JdbcTemplate getJdbcTemplate(String db);

}
