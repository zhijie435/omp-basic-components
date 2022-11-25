/*
 * PROJECT NAME: omp_cexport
 * PACKAGE NAME: com.gb.soa.omp.export.service.model
 * FILE    NAME: ExArcSyncBillInfo.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.export.service.model;

/**
 * TODO（描述类的职责）
 * @author zhiyu.long
 * @date 2018年4月10日 下午4:31:47
 * @version <b>1.0.0</b>
 */
public class ExArcSyncBillInfo {

	  private Long sysTaskNumId;// bigint(20) NOT NULL COMMENT '同步作业编号',
	  private String topic;// varchar(20) DEFAULT NULL,
	  private String tag;// varchar(20) DEFAULT NULL,
	  private String sqlid;// varchar(100) NOT NULL DEFAULT ' ' COMMENT '查询SQL对应标识',
	  private String sqlcontent;// varchar(4000) NOT NULL COMMENT 'SQL内容',
	  private String jdbcName;// varchar(50) NOT NULL COMMENT 'jdbc名称',
	  private String arrayFlag;// char(1) NOT NULL DEFAULT 'N' COMMENT '是否数组',
	  private String arrayName;//varchar(50) DEFAULT NULL COMMENT '数组名称',
	  private Long excuteInterval;// int(5) DEFAULT '0' COMMENT '执行间隔时间 单位分钟',
	  private String updateSql ;
	  private String billKey;
	/**
	 * 获得sysTaskNumId
	 * @return Long
	 */
	public Long getSysTaskNumId() {
		return sysTaskNumId;
	}
	/**
	 * 设置sysTaskNumId
	 * @param sysTaskNumId
	 */
	public void setSysTaskNumId(Long sysTaskNumId) {
		this.sysTaskNumId = sysTaskNumId;
	}
	/**
	 * 获得topic
	 * @return String
	 */
	public String getTopic() {
		return topic;
	}
	/**
	 * 设置topic
	 * @param topic
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
	/**
	 * 获得tag
	 * @return String
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * 设置tag
	 * @param tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	/**
	 * 获得sqlid
	 * @return String
	 */
	public String getSqlid() {
		return sqlid;
	}
	/**
	 * 设置sqlid
	 * @param sqlid
	 */
	public void setSqlid(String sqlid) {
		this.sqlid = sqlid;
	}
	/**
	 * 获得sqlcontent
	 * @return Long
	 */
	public String getSqlcontent() {
		return sqlcontent;
	}
	/**
	 * 设置sqlcontent
	 * @param sqlcontent
	 */
	public void setSqlcontent(String sqlcontent) {
		this.sqlcontent = sqlcontent;
	}
	/**
	 * 获得jdbcName
	 * @return String
	 */
	public String getJdbcName() {
		return jdbcName;
	}
	/**
	 * 设置jdbcName
	 * @param jdbcName
	 */
	public void setJdbcName(String jdbcName) {
		this.jdbcName = jdbcName;
	}
	/**
	 * 获得arrayFlag
	 * @return String
	 */
	public String getArrayFlag() {
		return arrayFlag;
	}
	/**
	 * 设置arrayFlag
	 * @param arrayFlag
	 */
	public void setArrayFlag(String arrayFlag) {
		this.arrayFlag = arrayFlag;
	}
	/**
	 * 获得arrayName
	 * @return String
	 */
	public String getArrayName() {
		return arrayName;
	}
	/**
	 * 设置arrayName
	 * @param arrayName
	 */
	public void setArrayName(String arrayName) {
		this.arrayName = arrayName;
	}
	/**
	 * 获得excuteInterval
	 * @return Long
	 */
	public Long getExcuteInterval() {
		return excuteInterval;
	}
	/**
	 * 设置excuteInterval
	 * @param excuteInterval
	 */
	public void setExcuteInterval(Long excuteInterval) {
		this.excuteInterval = excuteInterval;
	}
	/**
	 * 获得updateSql
	 * @return String
	 */
	public String getUpdateSql() {
		return updateSql;
	}
	/**
	 * 设置updateSql
	 * @param updateSql
	 */
	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}
	/**
	 * 获得billKey
	 * @return String
	 */
	public String getBillKey() {
		return billKey;
	}
	/**
	 * 设置billKey
	 * @param billKey
	 */
	public void setBillKey(String billKey) {
		this.billKey = billKey;
	}
	  
}
