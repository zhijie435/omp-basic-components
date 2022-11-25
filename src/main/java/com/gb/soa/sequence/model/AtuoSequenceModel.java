package com.gb.soa.sequence.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 自增序列
 * <p>
 * Description:
 * </p>
 * 
 * @author changhong.deng
 * @date 2018年6月5日
 */
public class AtuoSequenceModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long series; // 序列号
	private Long tenantNumId;// 租户ID
	private Long dataSign;// 测试标识
	private String seqName;// 序列名称
	private String seqProject;// 序列所属项目名称
	private String seqPrefix;// 序列前缀
	private Long currentNum;// 当前序列
	private Integer isYear;// 是否需要年 0：否 ，6 是
	private Integer isMonth;// 是否需要月 0：否 ，6 是
	private Integer isDay;// 是否需要日 0：否 ，6 是
	private Integer isFlowCode;// 是否需要流水号 6 否
	private Date CreateTime;// 创建时间
	private String remark;// 备注
	private String spare;// 备用字段
	private Long initValue;// 初始化自增长序列
	private Long isClear;// 是否清零 0：否 ，6 是
	private Long flowCodeLength;// 流水号的长度
	private Integer cacheNum;// 缓存的个数

	public Integer getCacheNum() {
		return cacheNum;
	}

	public void setCacheNum(Integer cacheNum) {
		this.cacheNum = cacheNum;
	}

	public Long getFlowCodeLength() {
		return flowCodeLength;
	}

	public void setFlowCodeLength(Long flowCodeLength) {
		this.flowCodeLength = flowCodeLength;
	}

	public Long getInitValue() {
		return initValue;
	}

	public void setInitValue(Long initValue) {
		this.initValue = initValue;
	}

	public Long getIsClear() {
		return isClear;
	}

	public void setIsClear(Long isClear) {
		this.isClear = isClear;
	}

	public Integer getIsYear() {
		return isYear;
	}

	public void setIsYear(Integer isYear) {
		this.isYear = isYear;
	}

	public Integer getIsMonth() {
		return isMonth;
	}

	public void setIsMonth(Integer isMonth) {
		this.isMonth = isMonth;
	}

	public Integer getIsDay() {
		return isDay;
	}

	public void setIsDay(Integer isDay) {
		this.isDay = isDay;
	}

	public Integer getIsFlowCode() {
		return isFlowCode;
	}

	public void setIsFlowCode(Integer isFlowCode) {
		this.isFlowCode = isFlowCode;
	}

	public String getSpare() {
		return spare;
	}

	public void setSpare(String spare) {
		this.spare = spare;
	}

	public Long getSeries() {
		return series;
	}

	public void setSeries(Long series) {
		this.series = series;
	}

	public Long getTenantNumId() {
		return tenantNumId;
	}

	public void setTenantNumId(Long tenantNumId) {
		this.tenantNumId = tenantNumId;
	}

	public Long getDataSign() {
		return dataSign;
	}

	public void setDataSign(Long dataSign) {
		this.dataSign = dataSign;
	}

	public String getSeqName() {
		return seqName;
	}

	public void setSeqName(String seqName) {
		this.seqName = seqName;
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

	public Long getCurrentNum() {
		return currentNum;
	}

	public void setCurrentNum(Long currentNum) {
		this.currentNum = currentNum;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
