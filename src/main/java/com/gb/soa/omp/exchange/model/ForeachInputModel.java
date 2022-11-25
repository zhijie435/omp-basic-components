/*
 * PROJECT NAME: omp_cbillsync
 * PACKAGE NAME: com.gb.soa.omp.billsync.model
 * FILE    NAME: ForeachInput.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.exchange.model;

public class ForeachInputModel {

	/** json 对象名称 */
	private String jsonAryName; 
	/** 数组内存放的类型  object表示是对象 value表示是数据值*/
	private String contentType;
	

	public static final String OBJECT 	="object";
	public static final String VALUE 	="value";
	
	
	public String getJsonAryName() {
		return jsonAryName;
	}
	public void setJsonAryName(String jsonAryName) {
		this.jsonAryName = jsonAryName;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	
}
