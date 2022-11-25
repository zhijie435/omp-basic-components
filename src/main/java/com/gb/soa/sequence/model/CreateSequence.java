package com.gb.soa.sequence.model;

import java.util.Date;

public class CreateSequence implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long series; //序列号
	private String seqName;//序列名称
	private String seqProject;//序列所属项目名称
	private String seqPrefix;//序列前缀
	private String seqNum;//序列num
	private String seqVal;//序列val
	private Long currentNum;//当前序列
	private Date CreateTime;//创建时间
	private Long seqNumStart;//seqNum开始值
	private Long seqNumEnd;//seqNum结束值
	private Long disrupt;//是否打乱 0：否 ，1 是
	private String squenceTime;
	private Long isStoreLocal;//是否存本地 0：否 ，1 是
	
	public Long getIsStoreLocal() {
		return isStoreLocal;
	}
	public void setIsStoreLocal(Long isStoreLocal) {
		this.isStoreLocal = isStoreLocal;
	}
	public Long getDisrupt() {
		return disrupt;
	}
	public void setDisrupt(Long disrupt) {
		this.disrupt = disrupt;
	}
	public Long getSeqNumStart() {
		return seqNumStart;
	}
	public void setSeqNumStart(Long seqNumStart) {
		this.seqNumStart = seqNumStart;
	}
	public Long getSeqNumEnd() {
		return seqNumEnd;
	}
	public void setSeqNumEnd(Long seqNumEnd) {
		this.seqNumEnd = seqNumEnd;
	}
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
	public Long getCurrentNum() {
		return currentNum;
	}
	public void setCurrentNum(Long currentNum) {
		this.currentNum = currentNum;
	}
	public String getSeqProject() {
		return seqProject;
	}
	public void setSeqProject(String seqProject) {
		this.seqProject = seqProject;
	}
	public String getSeqPrefix() {
		return seqPrefix;
	}
	public void setSeqPrefix(String seqPrefix) {
		this.seqPrefix = seqPrefix;
	}
	public String getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	public String getSeqVal() {
		return seqVal;
	}
	public void setSeqVal(String seqVal) {
		this.seqVal = seqVal;
	}
	public Date getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}
	public String getSquenceTime() {
		return squenceTime;
	}
	public void setSquenceTime(String squenceTime) {
		this.squenceTime = squenceTime;
	}
	
	
	

}
