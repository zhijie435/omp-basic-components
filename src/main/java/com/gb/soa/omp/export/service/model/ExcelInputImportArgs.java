/*
 * PROJECT NAME: omp_cimport
 * PACKAGE NAME: com.gb.soa.omp.cimport.service.model
 * FILE    NAME: ExcelInputImportArgs.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.export.service.model;

/**
 */
public class ExcelInputImportArgs {
	
	private Object[] arg;
	
	private String failMsg;
	
	private Long inputSeries;

	

	/**
	 * 获得arg
	 * @return Object[]
	 */
	public Object[] getArg() {
		return arg;
	}

	/**
	 * 设置arg
	 * @param arg
	 */
	public void setArg(Object[] arg) {
		this.arg = arg;
	}

	/**
	 * 获得failMsg
	 * @return String
	 */
	public String getFailMsg() {
		return failMsg;
	}

	/**
	 * 设置failMsg
	 * @param failMsg
	 */
	public void setFailMsg(String failMsg) {
		this.failMsg = failMsg;
	}

	/**
	 * 获得inputSeries
	 * @return Long
	 */
	public Long getInputSeries() {
		return inputSeries;
	}

	/**
	 * 设置inputSeries
	 * @param inputSeries
	 */
	public void setInputSeries(Long inputSeries) {
		this.inputSeries = inputSeries;
	}

}
