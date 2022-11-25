/*
 * PROJECT NAME: omp_cimport
 * PACKAGE NAME: com.gb.soa.omp.cimport.service.model
 * FILE    NAME: MatchesInfoAndKeyValue.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.export.service.model;

import com.gb.soa.omp.exchange.model.CommonQuery;

import java.util.concurrent.ConcurrentHashMap;

/**
 */
public class MatchesInfoAndKeyValue {
	private String[] paramNames = new String[]{};
	
	private ConcurrentHashMap<String, Object> resultMap ;
	
	private CommonQuery commonQuery;

	/**
	 */
	public MatchesInfoAndKeyValue(int mapSize) {
		resultMap = new ConcurrentHashMap<String, Object>(mapSize);
	}
	
	/**
	 * 获得paramNames
	 * @return String[]
	 */
	public String[] getParamNames() {
		return paramNames;
	}

	/**
	 * 设置paramNames
	 * @param paramNames
	 */
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	/**
	 * 获得resultMap
	 * @return ConcurrentHashMap<String,Object>
	 */
	public ConcurrentHashMap<String, Object> getResultMap() {
		return resultMap;
	}

	/**
	 * 设置resultMap
	 * @param resultMap
	 */
	public void setResultMap(ConcurrentHashMap<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	/**
	 * 获得commonQuery
	 * @return CommonQuery
	 */
	public CommonQuery getCommonQuery() {
		return commonQuery;
	}

	/**
	 * 设置commonQuery
	 * @param commonQuery
	 */
	public void setCommonQuery(CommonQuery commonQuery) {
		this.commonQuery = commonQuery;
	}
	
	
}
