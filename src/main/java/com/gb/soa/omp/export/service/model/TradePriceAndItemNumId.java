/*
 * PROJECT NAME: omp_cexport
 * PACKAGE NAME: com.gb.soa.omp.export.service.impl
 * FILE    NAME: TradePriceAndItemNumId.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.export.service.model;

/**
 * TODO（描述类的职责）
 * @author zhiyu.long
 * @date 2017年7月24日 下午5:57:10
 * @version <b>1.0.0</b>
 */
public class TradePriceAndItemNumId {

	private Double tradePrice;
	
	private Long itemNumId;
	
	private Long tmlLine;

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
	 * 获得itemNumId
	 * @return Long
	 */
	public Long getItemNumId() {
		return itemNumId;
	}

	/**
	 * 设置itemNumId
	 * @param itemNumId
	 */
	public void setItemNumId(Long itemNumId) {
		this.itemNumId = itemNumId;
	}

	/**
	 * 获得tmlLine
	 * @return Long
	 */
	public Long getTmlLine() {
		return tmlLine;
	}

	/**
	 * 设置tmlLine
	 * @param tmlLine
	 */
	public void setTmlLine(Long tmlLine) {
		this.tmlLine = tmlLine;
	}
}
