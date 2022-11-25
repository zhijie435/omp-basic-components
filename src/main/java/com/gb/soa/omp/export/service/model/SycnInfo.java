/*
 * PROJECT NAME: omp_cexport
 * PACKAGE NAME: com.gb.soa.omp.export.service.model
 * FILE    NAME: SycnInfo.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.export.service.model;

/**
 * TODO（描述类的职责）
 * @author zhiyu.long
 * @date 2017年6月6日 上午9:27:02
 * @version <b>1.0.0</b>
 */
public class SycnInfo {
	
	private String tableName;
	
	private Long pageSize;
	
	private Long taskNumId;
	
	private String sqlId;
	
	private String topic;
	
	private String tag;

	/**
	 * 获得tableName
	 * @return String
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置tableName
	 * @param tableName
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
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

	/**
	 * 获得taskNumId
	 * @return Long
	 */
	public Long getTaskNumId() {
		return taskNumId;
	}

	/**
	 * 设置taskNumId
	 * @param taskNumId
	 */
	public void setTaskNumId(Long taskNumId) {
		this.taskNumId = taskNumId;
	}

	/**
	 * 获得sqlId
	 * @return String
	 */
	public String getSqlId() {
		return sqlId;
	}

	/**
	 * 设置sqlId
	 * @param sqlId
	 */
	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	

}
