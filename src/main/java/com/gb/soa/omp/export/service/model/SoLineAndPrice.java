/*
 * PROJECT NAME: omp_cexport
 * PACKAGE NAME: com.gb.soa.omp.export.service.model
 * FILE    NAME: SoLineAndPrice.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.export.service.model;

/**
 */
public class SoLineAndPrice {
	
	private String soNumId;
	
	private String series;
	
	private Double tradePrice;
	

	/**
	 * 获得soNumId
	 * @return String
	 */
	public String getSoNumId() {
		return soNumId;
	}

	/**
	 * 设置soNumId
	 * @param soNumId
	 */
	public void setSoNumId(String soNumId) {
		this.soNumId = soNumId;
	}

	/**
	 * 获得tradePrice
	 * @return Double
	 */
	public Double getTradePrice() {
		return tradePrice;
	}

	/**
	 * 设置tradePrice
	 * @param tradePrice
	 */
	public void setTradePrice(Double tradePrice) {
		this.tradePrice = tradePrice;
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


}
