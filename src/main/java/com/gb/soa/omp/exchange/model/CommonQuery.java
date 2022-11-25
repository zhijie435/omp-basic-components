package com.gb.soa.omp.exchange.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 前段返回对象封装类
 *
 *
 */
public class CommonQuery implements Serializable {

	private static final long serialVersionUID = 5182957677633744995L;
	

	public static final String SQL_FLAG_INSERT = "I";
	
	public static final String SQL_FLAG_UPDATE = "U";

	public static final String SQL_FLAG_SELECT = "S";

	// 行号
	private String series;

	// SQLNAME
	private String sqlName;

	// SQLID
	private String sqlId;

	// SQL内容
	private String sqlContent;

	// SQL参数
	private String paramContent;

	// JDBC名称
	private String jdbcName;

	// 创建用户ID
	private Long createUserId;

	// 离开标识
	private String cancelSign;

	// 租户
	private Long tenantNumId;

	// 标识
	private Long dataSign;

	// 数据库类型
	private String dbType;
	
	//SQL注解
	private String annotatePrefix;
	
	public Map<String, String> paramMap = new HashMap<String, String>();
	 
	private Long pageSize;
	
	
	//sqlFlag  I 表示insert，U表示 update，其它视为查询
	private String sqlFlag;
	
	//通用子查询
	private String subSqlId;
	
	//无数据是否报错，Y是，N不报错
	private String noDataException;
	
	//无数据时的报错信息
	private String noDataExceptionMsg;
	
	//缓存方式0不使用缓存1普通缓存2调用缓存服务
	private Long cacheSign;
	
	//使用缓存时的方法名称，作为缓存键值的一部分
	private String methodName;
	
	//缓存存活时间
	private Long cacheLiveTime;
	
	//返回参数处理（bigInt型转String）
	private String returnHandleContent;
	
	//子查询标识 查询结果查不到纪录时是否返回默认值 2是子查询
	private Long subQuerySign = 1L;
	
	//用于导出Excel的列名,使用逗号隔开
	private String excelColumn;
	

	public String getExcelColumn() {
		return excelColumn;
	}

	public void setExcelColumn(String excelColumn) {
		this.excelColumn = excelColumn;
	}

	/**
	 * 获得series
	 * @return String
	 */
	public String getSeries() {
		return series;
	}

	/**
	 * 设置series
	 * @param series
	 */
	public void setSeries(String series) {
		this.series = series;
	}

	/**
	 * 获得sqlName
	 * @return String
	 */
	public String getSqlName() {
		return sqlName;
	}

	/**
	 * 设置sqlName
	 * @param sqlName
	 */
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
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

	/**
	 * 获得sqlContent
	 * @return String
	 */
	public String getSqlContent() {
		return sqlContent;
	}

	/**
	 * 设置sqlContent
	 * @param sqlContent
	 */
	public void setSqlContent(String sqlContent) {
		this.sqlContent = sqlContent;
		if(sqlContent.trim().toLowerCase().startsWith("insert")){
			this.sqlFlag = CommonQuery.SQL_FLAG_INSERT;
		}else if(sqlContent.trim().toLowerCase().startsWith("update")){
			this.sqlFlag = CommonQuery.SQL_FLAG_UPDATE;
		}else if(sqlContent.trim().toLowerCase().startsWith("delete")){
			this.sqlFlag = CommonQuery.SQL_FLAG_UPDATE;
		}else if(sqlContent.trim().toLowerCase().startsWith("select")){
			this.sqlFlag = CommonQuery.SQL_FLAG_SELECT;
		}else if(sqlContent.trim().toLowerCase().startsWith("merge into")){
			this.sqlFlag = CommonQuery.SQL_FLAG_INSERT;
		}
	}

	/**
	 * 获得paramContent
	 * @return String
	 */
	public String getParamContent() {
		return paramContent;
	}

	/**
	 * 设置paramContent
	 * @param paramContent
	 */
	public void setParamContent(String paramContent) {
		this.paramContent = paramContent;
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
	 * 获得createUserId
	 * @return Long
	 */
	public Long getCreateUserId() {
		return createUserId;
	}

	/**
	 * 设置createUserId
	 * @param createUserId
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * 获得cancelSign
	 * @return String
	 */
	public String getCancelSign() {
		return cancelSign;
	}

	/**
	 * 设置cancelSign
	 * @param cancelSign
	 */
	public void setCancelSign(String cancelSign) {
		this.cancelSign = cancelSign;
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

	/**
	 * 获得dbType
	 * @return String
	 */
	public String getDbType() {
		return dbType;
	}

	/**
	 * 设置dbType
	 * @param dbType
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getSqlFlag() {
		return sqlFlag;
	}

	public void setSqlFlag(String sqlFlag) {
		this.sqlFlag = sqlFlag;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	public String getAnnotatePrefix() {
		return annotatePrefix;
	}

	public void setAnnotatePrefix(String annotatePrefix) {
		this.annotatePrefix = annotatePrefix;
	}

	/**
	 * 获得subSqlId
	 * @return String
	 */
	public String getSubSqlId() {
		return subSqlId;
	}

	/**
	 * 设置subSqlId
	 * @param subSqlId
	 */
	public void setSubSqlId(String subSqlId) {
		this.subSqlId = subSqlId;
	}

	/**
	 * 获得noDataException
	 * @return String
	 */
	public String getNoDataException() {
		return noDataException;
	}

	/**
	 * 设置noDataException
	 * @param noDataException
	 */
	public void setNoDataException(String noDataException) {
		this.noDataException = noDataException;
	}

	/**
	 * 获得noDataExceptionMsg
	 * @return String
	 */
	public String getNoDataExceptionMsg() {
		return noDataExceptionMsg;
	}

	/**
	 * 设置noDataExceptionMsg
	 * @param noDataExceptionMsg
	 */
	public void setNoDataExceptionMsg(String noDataExceptionMsg) {
		this.noDataExceptionMsg = noDataExceptionMsg;
	}

	/**
	 * 获得cacheSign
	 * @return Long
	 */
	public Long getCacheSign() {
		return cacheSign;
	}

	/**
	 * 设置cacheSign
	 * @param cacheSign
	 */
	public void setCacheSign(Long cacheSign) {
		this.cacheSign = cacheSign;
	}

	/**
	 * 获得methodName
	 * @return String
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * 设置methodName
	 * @param methodName
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * 获得cacheLiveTime
	 * @return Long
	 */
	public Long getCacheLiveTime() {
		return cacheLiveTime;
	}

	/**
	 * 设置cacheLiveTime
	 * @param cacheLiveTime
	 */
	public void setCacheLiveTime(Long cacheLiveTime) {
		this.cacheLiveTime = cacheLiveTime;
	}

	/**
	 * 获得returnHandleContent
	 * @return String
	 */
	public String getReturnHandleContent() {
		return returnHandleContent;
	}

	/**
	 * 设置returnHandleContent
	 * @param returnHandleContent
	 */
	public void setReturnHandleContent(String returnHandleContent) {
		this.returnHandleContent = returnHandleContent;
	}

	/**
	 * 获得subQuerySign
	 * @return Long
	 */
	public Long getSubQuerySign() {
		return subQuerySign;
	}

	/**
	 * 设置subQuerySign
	 * @param subQuerySign
	 */
	public void setSubQuerySign(Long subQuerySign) {
		this.subQuerySign = subQuerySign;
	}
	
	
	
}
