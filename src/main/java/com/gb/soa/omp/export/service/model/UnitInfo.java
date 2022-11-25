/*
 * PROJECT NAME: omp_cexport
 * PACKAGE NAME: com.gb.soa.omp.export.service.model
 * FILE    NAME: UnitInfo.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.export.service.model;

/**
 * TODO（描述类的职责）
 * @author zhiyu.long
 * @date 2017年6月13日 下午2:31:47
 * @version <b>1.0.0</b>
 */
public class UnitInfo {
	
	private Long custUnitNumId;
	
	private Long unitNumId;
	
	private Long itemNumId;
	
	private Long tmlLine;

	/**
	 * 获得custUnitNumId
	 * @return Long
	 */
	public Long getCustUnitNumId() {
		return custUnitNumId;
	}

	/**
	 * 设置custUnitNumId
	 * @param custUnitNumId
	 */
	public void setCustUnitNumId(Long custUnitNumId) {
		this.custUnitNumId = custUnitNumId;
	}

	/**
	 * 获得unitNumId
	 * @return Long
	 */
	public Long getUnitNumId() {
		return unitNumId;
	}

	/**
	 * 设置unitNumId
	 * @param unitNumId
	 */
	public void setUnitNumId(Long unitNumId) {
		this.unitNumId = unitNumId;
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
