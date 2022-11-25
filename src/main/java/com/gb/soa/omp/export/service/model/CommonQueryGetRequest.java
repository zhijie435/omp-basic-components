/*
 * PROJECT NAME: omp_cexport
 * PACKAGE NAME: com.gb.soa.omp.export.service.model
 * FILE    NAME: CommonQueryGetRequest.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.export.service.model;

import com.gb.soa.omp.ccommon.api.request.AbstractRequest;

/**
 * TODO（描述类的职责）
 * @author zhiyu.long
 * @date 2017年12月6日 下午4:39:17
 * @version <b>1.0.0</b>
 */
public class CommonQueryGetRequest extends AbstractRequest{

	private static final long serialVersionUID = 1673356207290846918L;
	
	private String sqlId;

	/**
	 * 获得sqlId
	 * @return String
	 */
	public String getSqlId() {
		return sqlId;
	}

	/**
	 * 设置sqlId
	 * @param sqlId
	 */
	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}

}
