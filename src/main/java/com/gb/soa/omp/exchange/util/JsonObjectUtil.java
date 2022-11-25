/*
 * PROJECT NAME: exange_core
 * PACKAGE NAME: com.gb.soa.omp.export.util
 * FILE    NAME: JsonObjectUtil.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */
package com.gb.soa.omp.exchange.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.gb.soa.omp.ccommon.api.exception.ExceptionType;
import com.gb.soa.omp.ccommon.api.exception.ValidateBusinessException;
import com.gb.soa.omp.ccommon.util.JsonMapper;


public class JsonObjectUtil {

	private static JsonMapper mapper;
	static {
		mapper = new JsonMapper();
		mapper.getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
	}

	public static Object getJsonObject(JSONObject json, String config){
		String[] con = config.split("\\.");
		return getObject(json, con, 0);
	}

	private static Object getObject(JSONObject jsonObj, String[] con, int k){
		Object object = null;
		JSONArray childJsonAry = new JSONArray();
		Object conObj = jsonObj.get(con[k]);
		if (conObj == null) {
			throw new ValidateBusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.VBE20033, "入参数据："+jsonObj+"中，节点为"+con[k]+"的对象为空");
		}
		String str = conObj.toString();
		String childStr = null;
		if (str.startsWith("[")) {
			JSONArray jsonAry = (JSONArray) jsonObj.get(con[k]);
			for (Object obj : jsonAry) {
				if (con.length > k+1) {
					Object childObj = getObject(JSONObject.fromObject(obj), con, k+1);
					childStr = childObj.toString();
					if (childStr.startsWith("[")) {
						JSONArray ary = (JSONArray)childObj;
						for (Object object2 : ary) {
							childJsonAry.add(object2);
						}
					}else {
						childJsonAry.add(childObj);
					}
				} else {
					childJsonAry.add(obj);
				}
			}
			object = (Object)childJsonAry;
		} else if (str.startsWith("{")) {
			JSONObject o = (JSONObject) jsonObj.get(con[k]);
			if (con.length > k+1) {
				Object childObj =getObject(o, con, k+1);
				childStr = childObj.toString();
				if (childStr.startsWith("[")) {
					JSONArray ary = (JSONArray)childObj;
					for (Object object2 : ary) {
						childJsonAry.add(object2);
					}
					object = (Object)childJsonAry;
				} else {
					object = childObj;
				}
			}else {
				object = o;
			}
		} else {
			object = str;
		}
		return object;
	}


	public static Object getJsonObjectNoException(JSONObject json, String config){
		String[] con = config.split("\\.");
		return getObjectnNoException(json, con, 0);
	}
	private static Object getObjectnNoException(JSONObject jsonObj, String[] con, int k){
		Object object = null;
		JSONArray childJsonAry = new JSONArray();
		Object conObj = jsonObj.get(con[k]);
		if (conObj == null) {
			return null;
		}
		String str = conObj.toString();
		String childStr = null;
		if (str.startsWith("[")) {
			JSONArray jsonAry = (JSONArray) jsonObj.get(con[k]);
			for (Object obj : jsonAry) {
				if (con.length > k+1) {
					Object childObj = getObjectnNoException(JSONObject.fromObject(obj), con, k+1);
					if (childObj == null) {
						childJsonAry.add(childObj);
					} else {
						childStr = childObj.toString();
						if (childStr.startsWith("[")) {
							JSONArray ary = (JSONArray)childObj;
							for (Object object2 : ary) {
								childJsonAry.add(object2);
							}
						}else {
							childJsonAry.add(childObj);
						}
					}
				} else {
					childJsonAry.add(obj);
				}
			}
			object = (Object)childJsonAry;
		} else if (str.startsWith("{")) {
			JSONObject o = (JSONObject) jsonObj.get(con[k]);
			if (con.length > k+1) {
				Object childObj =getObjectnNoException(o, con, k+1);
				if (childObj == null) {
					object = childObj;
				} else {
					childStr = childObj.toString();
					if (childStr.startsWith("[")) {
						JSONArray ary = (JSONArray)childObj;
						for (Object object2 : ary) {
							childJsonAry.add(object2);
						}
						object = (Object)childJsonAry;
					} else {
						object = childObj;
					}
				}
			}else {
				object = o;
			}
		} else {
			object = str;
		}
		return object;
	}

	public static JsonConfig getJsonConfig() {
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class,
		             new DateJsonValueProcessor());
		config.registerJsonValueProcessor(Timestamp.class,
                new DateJsonValueProcessor());
		config.registerJsonValueProcessor(java.sql.Date.class,
                new DateJsonValueProcessor());
		return config;
	}
}
