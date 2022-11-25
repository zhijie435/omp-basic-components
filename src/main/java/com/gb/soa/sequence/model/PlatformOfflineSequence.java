package com.gb.soa.sequence.model;

import java.io.Serializable;
import java.util.Date;

public class PlatformOfflineSequence implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long series;
	// 序号名字
	private String seqName;
	// 在线序号开始值
	private Long onlineStartNum;
	// 在线序号结束值
	private Long onlineEndNum;
	// 离线序号结束值
	private Long offlineEndNum;
	// 离线序号已经分配到的值
	private Long offlineCurrentNum;
	// 离线获取当前序号的个数
	private Long offlineGetNumCount;
	// 创建时间
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

	public Long getOnlineStartNum() {
		return onlineStartNum;
	}

	public void setOnlineStartNum(Long onlineStartNum) {
		this.onlineStartNum = onlineStartNum;
	}

	public Long getOnlineEndNum() {
		return onlineEndNum;
	}

	public void setOnlineEndNum(Long onlineEndNum) {
		this.onlineEndNum = onlineEndNum;
	}

	public Long getOfflineEndNum() {
		return offlineEndNum;
	}

	public void setOfflineEndNum(Long offlineEndNum) {
		this.offlineEndNum = offlineEndNum;
	}

	public Long getOfflineCurrentNum() {
		return offlineCurrentNum;
	}

	public void setOfflineCurrentNum(Long offlineCurrentNum) {
		this.offlineCurrentNum = offlineCurrentNum;
	}

	public Long getOfflineGetNumCount() {
		return offlineGetNumCount;
	}

	public void setOfflineGetNumCount(Long offlineGetNumCount) {
		this.offlineGetNumCount = offlineGetNumCount;
	}

	public Date getCreateDtme() {
		return createDtme;
	}

	public void setCreateDtme(Date createDtme) {
		this.createDtme = createDtme;
	}

}
