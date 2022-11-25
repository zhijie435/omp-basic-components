package com.gb.soa.sequence.model;

import java.io.Serializable;
import java.util.Date;

public class PlatformOfflineSubUnitSequence implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long series;
	private String seqName;
	// 离线序号分配到的开始值
	private Long startNum;
	// 离线序号分配到的结束值
	private Long endNum;
	// 门店编号
	private Long subUnitNumId;
	private Date createDtme;

	public Long getSeries() {
		return series;
	}

	public void setSeries(Long series) {
		this.series = series;
	}

	public String getSeqName() {
		return seqName;
	}

	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}

	public Long getStartNum() {
		return startNum;
	}

	public void setStartNum(Long startNum) {
		this.startNum = startNum;
	}

	public Long getEndNum() {
		return endNum;
	}

	public void setEndNum(Long endNum) {
		this.endNum = endNum;
	}

	public Long getSubUnitNumId() {
		return subUnitNumId;
	}

	public void setSubUnitNumId(Long subUnitNumId) {
		this.subUnitNumId = subUnitNumId;
	}

	public Date getCreateDtme() {
		return createDtme;
	}

	public void setCreateDtme(Date createDtme) {
		this.createDtme = createDtme;
	}

}
