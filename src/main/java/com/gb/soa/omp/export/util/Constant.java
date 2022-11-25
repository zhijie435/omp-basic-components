/*
 * PROJECT NAME: omp_cmember
 * PACKAGE NAME: com.gb.soa.omp.cmember.util
 * FILE    NAME: Constant.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.export.util;

public class Constant {
	
	public static final String SUB_SYSTEM = "cexport";
	
	public static final String TENANT_DATA_SOURCE_KEY="tenant";
	
	public static final Integer PAGE_SIZE = 10000;
	
	/** */
	public static final String OMS_JDBC = "omsDataSource";

	public static final String WMS_JDBC = "wmsDataSource";
	/** */
	public static final String QJD_JDBC = "qjdDataSource";
	
	public static final String OMS_JDBC_PRO = "omsProductionDataSource";
	
	public static final String WMS_JDBC_PRO = "wmsProductionDataSource";
	
	public static final String GROUP_JDBC_PRO = "groupdossierProductionDataSource";
	
	public static final String GROUP_JDBC = "groupdossierDataSource";
	
	public static final String WEXIN_JDBC_PRO = "wexinProductionDataSource";
	
	public static final String OMS_STRESS_JDBC_PRO = "omsStressProductionDataSource";

	public static final String CRMQJD_JDBC = "crmqjdDataSource";
	
	
	
	/** 数据库类型 Oracle */
	public static final String ORACLE_DB = "Oracle";
	/** 数据库类型 MySQL */
	public static final String MYSQL_DB = "MySQL";
	
	/** 主表更新 REMARK */
	public static final String REMARK_N = "推送失败";
	public static final String REMARK_Y = "最近一次推送的时间";
	
	/** 更新 初始默认值 */
	public static final String UPDATE_N = "N";
	public static final String UPDATE_Y = "Y";

	public static final Long SYSTEM_FLAG = 34L;


	
	

}
