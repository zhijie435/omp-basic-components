package com.gb.soa.sequence.model;

import java.io.Serializable;
/***
 * 生成单据号的实体
 * @author bi.cai
 * 2018-03-06
 */
public class GoodsReserveBuildModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long tenantNumId;//租户ID
	
    private Long moduleId;//模块id
    
    private String moduleSimName;//模块简称
    
  	private Boolean isOrganization;//是否需要所属机构
  	
  	private Boolean isYear;//是否需要年
  	
  	private Boolean isMonth;//是否需要月
  	
  	private Boolean isDay;//是否需要日
  	
  	private Boolean isFlowCode;//是否需要流水号
  	
  	private int flowLength;//流水号长度

	public Long getTenantNumId() {
		return tenantNumId;
	}

	public void setTenantNumId(Long tenantNumId) {
		this.tenantNumId = tenantNumId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleSimName() {
		return moduleSimName;
	}

	public void setModuleSimName(String moduleSimName) {
		this.moduleSimName = moduleSimName;
	}

	public void setFlowCode(boolean isFlowCode) {
		this.isFlowCode = isFlowCode;
	}

	public int getFlowLength() {
		return flowLength;
	}

	public void setFlowLength(int flowLength) {
		this.flowLength = flowLength;
	}

	public Boolean getIsOrganization() {
		return isOrganization;
	}

	public void setIsOrganization(Boolean isOrganization) {
		this.isOrganization = isOrganization;
	}

	public Boolean getIsYear() {
		return isYear;
	}

	public void setIsYear(Boolean isYear) {
		this.isYear = isYear;
	}

	public Boolean getIsMonth() {
		return isMonth;
	}

	public void setIsMonth(Boolean isMonth) {
		this.isMonth = isMonth;
	}

	public Boolean getIsDay() {
		return isDay;
	}

	public void setIsDay(Boolean isDay) {
		this.isDay = isDay;
	}

	public Boolean getIsFlowCode() {
		return isFlowCode;
	}

	public void setIsFlowCode(Boolean isFlowCode) {
		this.isFlowCode = isFlowCode;
	}
}
