/*
 * PROJECT NAME: omp_cbillsync_api
 * PACKAGE NAME: com.gb.soa.omp.cbillsync.dto
 * FILE    NAME: DataSyncDto.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */
package com.gb.soa.omp.export.service.model;

/**
 * 数据同步 实体
 * @author cjw
 * @date 2016年10月9日 下午3:48:39
 * @version <b>1.0.0</b>
 */
public class DataSync {

	private Long series;
	private String sqlId;
	private String tableName;
	private String batch;
	private Long sourceSeries;
	private String createDtme;
	private String isUpdate;
	private Long businessType;
	private Long tenantNumId;
	private Long dataSign;

	public Long getSeries() {
		return series;
	}

	public void setSeries(Long series) {
		this.series = series;
	}

	public String getSqlId() {
		return sqlId;
	}

	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public Long getSourceSeries() {
		return sourceSeries;
	}

	public void setSourceSeries(Long sourceSeries) {
		this.sourceSeries = sourceSeries;
	}

	public String getCreateDtme() {
		return createDtme;
	}

	public void setCreateDtme(String createDtme) {
		this.createDtme = createDtme;
	}

	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	/**
	 * 获得businessType
	 * @return Long
	 */
	public Long getBusinessType() {
		return businessType;
	}

	/**
	 * 设置businessType
	 * @param businessType
	 */
	public void setBusinessType(Long businessType) {
		this.businessType = businessType;
	}

	/**
	 * 获得tenantNumId
	 * @return Long
	 */
	public Long getTenantNumId() {
		return tenantNumId;
	}

	/**
	 * 设置tenantNumId
	 * @param tenantNumId
	 */
	public void setTenantNumId(Long tenantNumId) {
		this.tenantNumId = tenantNumId;
	}

	/**
	 * 获得dataSign
	 * @return Long
	 */
	public Long getDataSign() {
		return dataSign;
	}

	/**
	 * 设置dataSign
	 * @param dataSign
	 */
	public void setDataSign(Long dataSign) {
		this.dataSign = dataSign;
	}

}
