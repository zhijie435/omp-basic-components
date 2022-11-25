/*
 * PROJECT NAME: exchange_core
 * PACKAGE NAME: com.gb.soa.omp.exchange.model
 * FILE    NAME: BillMappingExtendData.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.exchange.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * TODO（描述类的职责）
 * @author zhiyu.long
 * @date 2017年11月27日 下午2:19:50
 * @version <b>1.0.0</b>
 */
public class BillMappingExtendData implements Serializable{

	private static final long serialVersionUID = -4037402699816641407L;
	
	private String mappingName;
	
	
	private String oldValueFromKey;
	
	private String extendValueFromKey;
	 

	/**
	 * 获得extendValueFromKey
	 * @return String
	 */
	public String getExtendValueFromKey() {
		return extendValueFromKey;
	}

	/**
	 * 设置extendValueFromKey
	 * @param extendValueFromKey
	 */
	public void setExtendValueFromKey(String extendValueFromKey) {
		this.extendValueFromKey = extendValueFromKey;
	}

	/**
	 * 获得mappingName
	 * @return String
	 */
	public String getMappingName() {
		return mappingName;
	}

	/**
	 * 设置mappingName
	 * @param mappingName
	 */
	public void setMappingName(String mappingName) {
		this.mappingName = mappingName;
	}

	/**
	 * 获得oldValueFromKey
	 * @return String
	 */
	public String getOldValueFromKey() {
		return oldValueFromKey;
	}

	/**
	 * 设置oldValueFromKey
	 * @param oldValueFromKey
	 */
	public void setOldValueFromKey(String oldValueFromKey) {
		this.oldValueFromKey = oldValueFromKey;
	}


 
	
	
}
