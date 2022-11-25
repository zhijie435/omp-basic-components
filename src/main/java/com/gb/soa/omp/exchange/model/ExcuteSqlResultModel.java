/*
 * PROJECT NAME: omp_cexport
 * PACKAGE NAME: com.gb.soa.omp.export.service.model
 * FILE    NAME: ExcuteSqlResultModel.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.exchange.model;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * TODO（描述类的职责）
 * @author hss
 * @date 2017年4月20日 下午6:15:17
 * @version <b>1.0.0</b>
 */
public class ExcuteSqlResultModel {

	private long pageCount;
	
	private long recordCount;//总数居数量
	
	//private List<JSONObject> data;
	private List<Map<String,Object>> data;

	private String sql;
	
	private Object[] arg = new Object[0];

	//sqlFlag  I 表示insert，U表示 update，其它视为查询
	private String sqlFlag;
	
	private long count;//查询返回数量
	
	
	
	public long getPageCount() {
		return pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}

	public long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getArg() {
		return arg;
	}

	public void setArg(Object[] arg) {
		this.arg = arg;
	}

	public String getSqlFlag() {
		return sqlFlag;
	}

	public void setSqlFlag(String sqlFlag) {
		this.sqlFlag = sqlFlag;
	}

	/**
	 * 获得count
	 * @return long
	 */
	public long getCount() {
		return count;
	}

	/**
	 * 设置count
	 * @param count
	 */
	public void setCount(long count) {
		this.count = count;
	}
	
}
