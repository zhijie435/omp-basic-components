/*
 * PROJECT NAME: omp_cbillsync
 * PACKAGE NAME: com.gb.soa.omp.billsync.model
 * FILE    NAME: SyncDataInfo.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */
package com.gb.soa.omp.export.service.model;

public class SyncDataInfo {
	/** SQLID */
	private String sqlId;
	/** 表名 */
	private String tableName;
	/** sql内容 */
	private String sqlContent;
	/** 分页 */
	private Long pageSize;
	/** 最后更新时间 */
	private String preMaxUpdtme;
	
	private String currentMaxUpdtme;
	/** 是否记录左右推送日志 */
	private String isLogAll;
	/** 推送类型 */
	private String businessType;
	/** jdbc名称 */
	private String jdbcName;
	/** 上次最后跟新时间 */
	private String nextUpdate;
	/** 标记 */
	private String cancelsign;
	
	private Long count;
	
	private Long sendType;

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

	public String getSqlContent() {
		return sqlContent;
	}

	public void setSqlContent(String sqlContent) {
		this.sqlContent = sqlContent;
	}

	/**
	 * 获得pageSize
	 * @return Long
	 */
	public Long getPageSize() {
		return pageSize;
	}

	/**
	 * 设置pageSize
	 * @param pageSize
	 */
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}


	public String getIsLogAll() {
		return isLogAll;
	}

	public void setIsLogAll(String isLogAll) {
		this.isLogAll = isLogAll;
	}

	/**
	 * 获得businessType
	 * @return String
	 */
	public String getBusinessType() {
		return businessType;
	}

	/**
	 * 设置businessType
	 * @param businessType
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getJdbcName() {
		return jdbcName;
	}

	public void setJdbcName(String jdbcName) {
		this.jdbcName = jdbcName;
	}

	public String getNextUpdate() {
		return nextUpdate;
	}

	public void setNextUpdate(String nextUpdate) {
		this.nextUpdate = nextUpdate;
	}

	/**
	 * 获得cancelsign
	 * @return String
	 */
	public String getCancelsign() {
		return cancelsign;
	}

	/**
	 * 设置cancelsign
	 * @param cancelsign
	 */
	public void setCancelsign(String cancelsign) {
		this.cancelsign = cancelsign;
	}

	/**
	 * 获得preMaxUpdtme
	 * @return String
	 */
	public String getPreMaxUpdtme() {
		return preMaxUpdtme;
	}

	/**
	 * 设置preMaxUpdtme
	 * @param preMaxUpdtme
	 */
	public void setPreMaxUpdtme(String preMaxUpdtme) {
		this.preMaxUpdtme = preMaxUpdtme;
	}

	/**
	 * 获得currentMaxUpdtme
	 * @return String
	 */
	public String getCurrentMaxUpdtme() {
		return currentMaxUpdtme;
	}

	/**
	 * 设置currentMaxUpdtme
	 * @param currentMaxUpdtme
	 */
	public void setCurrentMaxUpdtme(String currentMaxUpdtme) {
		this.currentMaxUpdtme = currentMaxUpdtme;
	}

	/**
	 * 获得count
	 * @return Long
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * 设置count
	 * @param count
	 */
	public void setCount(Long count) {
		this.count = count;
	}

	/**
	 * 获得sendType
	 * @return Long
	 */
	public Long getSendType() {
		return sendType;
	}

	/**
	 * 设置sendType
	 * @param sendType
	 */
	public void setSendType(Long sendType) {
		this.sendType = sendType;
	}
	
}
