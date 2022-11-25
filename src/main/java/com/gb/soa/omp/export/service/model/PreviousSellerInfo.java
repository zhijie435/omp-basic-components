package com.gb.soa.omp.export.service.model;

import java.util.Date;

public class PreviousSellerInfo {

	// 客户市场业务单元
	private String unitNumId;
	// 商品价格分类
	private String ptyNum8;
	// 事业部主键
	private String divNumId;
	// 供应商市场业务单元
	private String provideUnitNumId;
	// 折扣
	private String discount;
	
	private Long itemNumId;
	
	private Long tmlLine;
	
	private Long orderLine;
	
	private Date lastUpdtme;
	

	/**
	 * 获得unitNumId
	 * @return String
	 */
	public String getUnitNumId() {
		return unitNumId;
	}

	/**
	 * 设置unitNumId
	 * @param unitNumId
	 */
	public void setUnitNumId(String unitNumId) {
		this.unitNumId = unitNumId;
	}

	/**
	 * 获得ptyNum8
	 * @return String
	 */
	public String getPtyNum8() {
		return ptyNum8;
	}

	/**
	 * 设置ptyNum8
	 * @param ptyNum8
	 */
	public void setPtyNum8(String ptyNum8) {
		this.ptyNum8 = ptyNum8;
	}

	/**
	 * 获得divNumId
	 * @return String
	 */
	public String getDivNumId() {
		return divNumId;
	}

	/**
	 * 设置divNumId
	 * @param divNumId
	 */
	public void setDivNumId(String divNumId) {
		this.divNumId = divNumId;
	}

	/**
	 * 获得provideUnitNumId
	 * @return String
	 */
	public String getProvideUnitNumId() {
		return provideUnitNumId;
	}

	/**
	 * 设置provideUnitNumId
	 * @param provideUnitNumId
	 */
	public void setProvideUnitNumId(String provideUnitNumId) {
		this.provideUnitNumId = provideUnitNumId;
	}

	/**
	 * 获得discount
	 * @return String
	 */
	public String getDiscount() {
		return discount;
	}

	/**
	 * 设置discount
	 * @param discount
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
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

	/**
	 * 获得lastUpdtme
	 * @return Date
	 */
	public Date getLastUpdtme() {
		return lastUpdtme;
	}

	/**
	 * 设置lastUpdtme
	 * @param lastUpdtme
	 */
	public void setLastUpdtme(Date lastUpdtme) {
		this.lastUpdtme = lastUpdtme;
	}

	/**
	 * 获得orderLine
	 * @return Long
	 */
	public Long getOrderLine() {
		return orderLine;
	}

	/**
	 * 设置orderLine
	 * @param orderLine
	 */
	public void setOrderLine(Long orderLine) {
		this.orderLine = orderLine;
	}
	
}