package com.gb.soa.omp.export.service.model;

public class StorageTypeAndDivInfo {
	// 不同发货
	private Integer typeNumId;
	// 事业部
	private Integer divNumId;
	
	private Long mgUnitNumId;
	
	private Long sysNumId;
	
	private Long custUnitNumId;

	/**
	 * 获得typeNumId
	 * @return Integer
	 */
	public Integer getTypeNumId() {
		return typeNumId;
	}

	/**
	 * 设置typeNumId
	 * @param typeNumId
	 */
	public void setTypeNumId(Integer typeNumId) {
		this.typeNumId = typeNumId;
	}

	/**
	 * 获得divNumId
	 * @return Integer
	 */
	public Integer getDivNumId() {
		return divNumId;
	}

	/**
	 * 设置divNumId
	 * @param divNumId
	 */
	public void setDivNumId(Integer divNumId) {
		this.divNumId = divNumId;
	}

	/**
	 * 获得mgUnitNumId
	 * @return Long
	 */
	public Long getMgUnitNumId() {
		return mgUnitNumId;
	}

	/**
	 * 设置mgUnitNumId
	 * @param mgUnitNumId
	 */
	public void setMgUnitNumId(Long mgUnitNumId) {
		this.mgUnitNumId = mgUnitNumId;
	}

	/**
	 * 获得sysNumId
	 * @return Long
	 */
	public Long getSysNumId() {
		return sysNumId;
	}

	/**
	 * 设置sysNumId
	 * @param sysNumId
	 */
	public void setSysNumId(Long sysNumId) {
		this.sysNumId = sysNumId;
	}

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
}
