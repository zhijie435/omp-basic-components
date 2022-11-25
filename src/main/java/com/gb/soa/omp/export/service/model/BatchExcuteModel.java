/*
 * PROJECT NAME: omp_cimport
 * PACKAGE NAME: com.gb.soa.omp.cimport.service.model
 * FILE    NAME: Snippet.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.export.service.model;

import java.util.List;

/**
 * TODO（描述类的职责）
 * @author cjw
 * @date 2017年4月22日 下午3:23:15
 * @version <b>1.0.0</b>
 */
public class BatchExcuteModel {
	
	//更新sql
	private String sql;
	 
	private List<Object[]> argList;
	
	//插入sql
	private String noDataUpdateSql;
	 
	private List<Object[]> noDataUpdateArgList;

	
	private boolean hasNoDataUpdate = false;
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Object[]> getArgList() {
		return argList;
	}

	public void setArgList(List<Object[]> argList) {
		this.argList = argList;
	}

	public String getNoDataUpdateSql() {
		return noDataUpdateSql;
	}

	public void setNoDataUpdateSql(String noDataUpdateSql) {
		this.noDataUpdateSql = noDataUpdateSql;
	}

	public List<Object[]> getNoDataUpdateArgList() {
		return noDataUpdateArgList;
	}

	public void setNoDataUpdateArgList(List<Object[]> noDataUpdateArgList) {
		this.noDataUpdateArgList = noDataUpdateArgList;
	}

	public boolean isHasNoDataUpdate() {
		return hasNoDataUpdate;
	}

	public void setHasNoDataUpdate(boolean hasNoDataUpdate) {
		this.hasNoDataUpdate = hasNoDataUpdate;
	}
}

