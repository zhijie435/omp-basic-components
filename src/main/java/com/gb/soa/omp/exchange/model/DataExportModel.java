/*
 * PROJECT NAME: omp_cbillsync
 * PACKAGE NAME: com.gb.soa.omp.billsync.model
 * FILE    NAME: DataExportModel.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.exchange.model;

import java.util.List;

public class DataExportModel {

	/** 导出方式：0表示直接导出;1 表示遍历导出 */
	public final static Long DATA_TYPE_OF_OBJECT = 0L;
	
	/** 导出方式：0表示直接导出;1 表示遍历导出 */
	public final static Long DATA_TYPE_OF_ARY = 1L;
	
	/** 导出方式：0表示直接导出;1 表示遍历导出 */
	public final static Long PROCCESS_TYPE_OF_FOREACH = 1L;
	
	/** 导出方式：0表示直接导出;1 表示遍历导出 */
	public final static Long PROCCESS_TYPE_OF_COMM = 0L;
	
	/** 导出查询结果 无数据时 抛出异常*/
	public final static String DATA_EXCEPTION_YES	= "Y";
	/** 导出查询结果 无数据时 记录告警日志*/
	public final static String DATA_EXCEPTION_NO	= "N";
	/** 导出查询  校验入参为null */
	public final static String DATA_CHECK_YES		= "Y";
	/** 导出查询  不校验入参为null */
	public final static String DATA_CHECK_NO		= "N";
	
	/** 导出名称 */
	private String exportName;
	/** 导出方式：0表示直接导出;1 表示遍历导出 */
	private Long proccessType ;
	/** 导出ID */
	private String sqlId;
	/** sql 执行结果为对象或集合  0表示对象；1表示集合*/
	private Long dataType;
	/** 导出查询当输入的数剧为空时 是否抛异常  默认Y抛异常*/
	private String noDataException = "Y";
	/** 导出查询结果无数据时是否进行报错异常检查  默认Y抛异常* */
	private String noDataCheck = "Y";
	
	/**是否使用多线程执行**/
	private String useParallel = "N";
	
	private List<AddParamModel> addParam;
	
	private List<ForeachInputModel> foreachInput;

	private List<DataExportModel> childExport;
	
	private List<MessageSendModel> messageSendModel;

	public String getExportName() {
		return exportName;
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
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

	public String getNoDataException() {
		return noDataException;
	}

	public void setNoDataException(String noDataException) {
		this.noDataException = noDataException;
	}

	public String getNoDataCheck() {
		return noDataCheck;
	}

	public void setNoDataCheck(String noDataCheck) {
		this.noDataCheck = noDataCheck;
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

	public List<DataExportModel> getChildExport() {
		return childExport;
	}

	public void setChildExport(List<DataExportModel> childExport) {
		this.childExport = childExport;
	}

	public List<MessageSendModel> getMessageSendModel() {
		return messageSendModel;
	}

	public void setMessageSendModel(List<MessageSendModel> messageSendModel) {
		this.messageSendModel = messageSendModel;
	}

	public String getUseParallel() {
		return useParallel;
	}

	public void setUseParallel(String useParallel) {
		this.useParallel = useParallel;
	}
}
