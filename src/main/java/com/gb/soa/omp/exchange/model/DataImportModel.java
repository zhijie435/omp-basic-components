/*
 * PROJECT NAME: omp_cbillsync
 * PACKAGE NAME: com.gb.soa.omp.billsync.model
 * FILE    NAME: DataImportModel.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.exchange.model;

import java.util.List;

public class DataImportModel {


	/** 导入结果：0表示对象;1 表示集合 */
	public final static Long DATA_TYPE_OF_OBJECT = 0L;
	
	/** 导入结果：0表示对象;1 表示集合 */
	public final static Long DATA_TYPE_OF_ARY = 1L;
	
	/** 导入方式：0表示直接导入;1 表示遍历导入 */
	public final static Long PROCCESS_TYPE_OF_FOREACH = 1L;
	
	/** 导入方式：0表示直接导入;1 表示遍历导入 */
	public final static Long PROCCESS_TYPE_OF_COMM = 0L;
	
	/** 数据为空 需跑出异常 */
	public final static String DATA_EXCEPTION_YES 	= "Y";
	/** 数据为空 需记录错误日志 */
	public final static String DATA_EXCEPTION_NO 	= "N";
	/** 导入名称 */
	private String importName;
	/** 导入方式：0表示直接导入;1 表示遍历导入 */
	private Long proccessType;
	/** 导入ID */
	private String sqlId;
	/** sql 执行结果为对象或集合  0表示对象；1表示集合*/
	private Long dataType;
	
	/** 数据为空时，Y为抛出异常，N为记录错误日志 默认Y */
	private String dataException = "Y";
	
	
	/**
	 * 没有更新到数据行时，执行的其它sql
	 */
	
	private String noDataUpdateSqlId;
	
	private List<AddParamModel> addParam;
	
	private List<ForeachInputModel> foreachInput;
	
	private List<DataImportModel> childImport;
	
	private  String exsitCheckSqlId;
	
	private BillMappingExtendData billMappingExtendData;
	
	public String getImportName() {
		return importName;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

	public Long getProccessType() {
		return proccessType;
	}

	public void setProccessType(Long proccessType) {
		this.proccessType = proccessType;
	}

	public String getSqlId() {
		return sqlId;
	}

	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}

	public Long getDataType() {
		return dataType;
	}

	public void setDataType(Long dataType) {
		this.dataType = dataType;
	}

	public String getDataException() {
		return dataException;
	}

	public void setDataException(String dataException) {
		this.dataException = dataException;
	}

	public List<AddParamModel> getAddParam() {
		return addParam;
	}

	public void setAddParam(List<AddParamModel> addParam) {
		this.addParam = addParam;
	}

	public List<ForeachInputModel> getForeachInput() {
		return foreachInput;
	}

	public void setForeachInput(List<ForeachInputModel> foreachInput) {
		this.foreachInput = foreachInput;
	}

	public List<DataImportModel> getChildImport() {
		return childImport;
	}

	public void setChildImport(List<DataImportModel> childImport) {
		this.childImport = childImport;
	}

	public String getNoDataUpdateSqlId() {
		return noDataUpdateSqlId;
	}

	public void setNoDataUpdateSqlId(String noDataUpdateSqlId) {
		this.noDataUpdateSqlId = noDataUpdateSqlId;
	}

	/**
	 * 获得exsitCheckSqlId
	 * @return String
	 */
	public String getExsitCheckSqlId() {
		return exsitCheckSqlId;
	}

	/**
	 * 设置exsitCheckSqlId
	 * @param exsitCheckSqlId
	 */
	public void setExsitCheckSqlId(String exsitCheckSqlId) {
		this.exsitCheckSqlId = exsitCheckSqlId;
	}

	/**
	 * 获得billMappingExtendData
	 * @return BillMappingExtendData
	 */
	public BillMappingExtendData getBillMappingExtendData() {
		return billMappingExtendData;
	}

	/**
	 * 设置billMappingExtendData
	 * @param billMappingExtendData
	 */
	public void setBillMappingExtendData(BillMappingExtendData billMappingExtendData) {
		this.billMappingExtendData = billMappingExtendData;
	}
	
}
