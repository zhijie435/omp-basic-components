/*
 * PROJECT NAME: omp_cbillsync
 * PACKAGE NAME: com.gb.soa.omp.billsync.model
 * FILE    NAME: AddInputParam.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.exchange.model;

public class AddParamModel {

	/** 参数key */
	private String paramName;
	/** 来源方式 */
	private String fromType;
	/** seq的map的key */
	private String seqStoreName;
	
	private String oldValueFromKey;
	
	private String mappingName;

	/** 输入参数的名称 */
	private String inputParamName;
	
	/**
	 * 当设置input为seq时,那么可以讲seq值进行存储
	 */
	private String storeMappingName;

	/** 父节点 */
	private String parentNode;
	/** 固定值 */
	private String constValue;
	
	
	
	public static final String FROM_TYPE_OF_SEQ 	= "seq";
	public static final String FROM_TYPE_OF_INPUT 	= "input";
	public static final String FROM_TYPE_OF_MAPPING = "mapping";
	public static final String FROM_TYPE_OF_CONST 	= "const";
	
	
	
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getFromType() {
		return fromType;
	}
	public void setFromType(String fromType) {
		this.fromType = fromType;
	}
	public String getSeqStoreName() {
		return seqStoreName;
	}
	public void setSeqStoreName(String seqStoreName) {
		this.seqStoreName = seqStoreName;
	}
	 
	public String getOldValueFromKey() {
		return oldValueFromKey;
	}
	public void setOldValueFromKey(String oldValueFromKey) {
		this.oldValueFromKey = oldValueFromKey;
	}
	public String getMappingName() {
		return mappingName;
	}
	public void setMappingName(String mappingName) {
		this.mappingName = mappingName;
	}
	public String getStoreMappingName() {
		return storeMappingName;
	}
	public void setStoreMappingName(String storeMappingName) {
		this.storeMappingName = storeMappingName;
	}
	public String getParentNode() {
		return parentNode;
	}
	public void setParentNode(String parentNode) {
		this.parentNode = parentNode;
	}
	public String getConstValue() {
		return constValue;
	}
	public void setConstValue(String constValue) {
		this.constValue = constValue;
	}
	public String getInputParamName() {
		return inputParamName;
	}
	public void setInputParamName(String inputParamName) {
		this.inputParamName = inputParamName;
	}
}
