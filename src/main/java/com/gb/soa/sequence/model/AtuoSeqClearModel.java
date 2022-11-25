package com.gb.soa.sequence.model;

import java.io.Serializable;

/**
 * 需要清零的自增序列
 * <p>
 * Description:
 * </p>
 * 
 * @author changhong.deng
 * @date 2018年6月5日
 */
public class AtuoSeqClearModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long series; // 序列号
	private Long tenantNumId;// 租户ID
	private Long dataSign;// 测试标识
	private String seqName;// 序列名称
	
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

}
