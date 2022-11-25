/*
 * PROJECT NAME: omp_cexport
 * PACKAGE NAME: com.gb.soa.omp.export.dao
 * FILE    NAME: ExcelInputHdrDao.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */
package com.gb.soa.omp.export.dao;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gb.soa.omp.ccommon.dao.CommonAbstractDao;
import com.gb.soa.omp.export.api.exception.ExportExceptionType;
import com.gb.soa.omp.export.entity.EXCEL_INPUT_HDR;
import com.gb.soa.omp.export.util.ExportUtil;

/**
 */
@Repository
public class ExcelInputHdrDao extends CommonAbstractDao{

	/**
	 * 构造
	 */
	public ExcelInputHdrDao() {
		super(EXCEL_INPUT_HDR.class, ExportUtil.SUB_SYSTEM, ExportExceptionType.DOE134301);
		super.child = this;
	}

	@Resource(name = "dataexchangeJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 *
	 */
	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


}
