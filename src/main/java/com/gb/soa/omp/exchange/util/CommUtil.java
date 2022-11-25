package com.gb.soa.omp.exchange.util;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.gb.soa.omp.ccommon.util.JsonMapper;
import com.gb.soa.omp.ccommon.util.StringUtil;
import com.gb.soa.omp.exchange.model.CommonQuery;
import com.gb.soa.omp.exchange.model.ExcuteSqlResultModel;
import com.gb.soa.sequence.util.SeqGetUtil;

public class CommUtil {
	
	private static Logger log = LoggerFactory.getLogger(CommUtil.class);
	private static JsonMapper mapper;
	static {
		mapper = new JsonMapper();
		mapper.getMapper().setPropertyNamingStrategy(
				PropertyNamingStrategy.SNAKE_CASE);
	}
	public static String sqlHandler(String sqlContent, Map<String, Object> map) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object value =  entry.getValue();
			String key = entry.getKey();
			/*//时间
			if(value.contains(":") && value.contains("-") ){
				System.out.println("时间参数"+key );
			}*/
			sqlContent = sqlContent.replaceFirst("\\?",  value.toString().length() <=0 ? "##" + key + "##" :  "@@" + key+ "@@");
			//空日期处理
			if("NULL_DATE_DEFAULTVALUE".equals(value)){
				map.put(key, null);
			}
		}
	 
		Pattern p = Pattern.compile("\\[([^\\[]+)\\]");
		Matcher matcher1 = p.matcher(sqlContent);
		while (matcher1.find()) {
			String matchStr = matcher1.group(1);
			if (matchStr.indexOf("##") != -1) {
				removeFromMap(matchStr, map);
				sqlContent = sqlContent.replace(matcher1.group(), "");

			}

		}
		sqlContent = sqlContent.replaceAll("@@[^@@]+@@", "\\?");
		return sqlContent;
	}
	public static String removeFromMap(String sqlContent,
			Map<String, Object> map) {
		String removeKey = "";
		Pattern p = Pattern.compile("[@|#](\\S+)[#|@]");
		Matcher matcher1 = p.matcher(sqlContent);
		while (matcher1.find()) {
			String matchStr = matcher1.group(1);
			matchStr = matchStr.replace("@", "").replace("#", "");
			map.remove(matchStr);
			removeKey = matchStr;
		}
		return removeKey;
	}
	
	/*
	public static Map<String, Object> getParamsWithMap(CommonQuery commonQuery,
			JSONArray ja, JSONObject jsonOb) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			String name = jo.optString("NAME").trim();
			*/
			/*if(name.toLowerCase().equals("productlowclassid")){
				System.out.println("---------");
			}*/
			/*
			if(StringUtil.isNullOrBlankTrim(name)){
				name = jo.optString("name").trim();
			}
			if(StringUtil.isNullOrBlankTrim(name)){
				throw new RuntimeException("sqlId为" + commonQuery.getSqlId() + "的查询中配置name参数值为空"); 
			}else{
				name = name.toLowerCase();
			}
		
			String value = "";
			String type = jo.getString("TYPE");
			String function = jo.optString("FUNCTION");
			//in 查询处理
			String qtype = jo.optString("QTYPE");
			if(StringUtil.isNullOrBlankTrim(qtype)){
				qtype = jo.optString("qtype");
			}
			// 模糊查询标志 参数为:before after around
			String fuzzyQuery = ""; 
			if (jo.has("fuzzyquery") || jo.containsKey("fuzzyquery")) {
				fuzzyQuery = jo.getString("fuzzyquery");
			}
			if (jo.has("FUZZYQUERY") || jo.containsKey("FUZZYQUERY")) {
				fuzzyQuery = jo.getString("FUZZYQUERY");
			}
			
			boolean mustHave = jo.getBoolean("MUSTHAVE");
			
			if(jsonOb.containsKey(name) || jsonOb.containsKey(name.toUpperCase())){
				value = jsonOb.optString(name);
				//如果没有取值到，通过大写key取值
				if(StringUtil.isAllNullOrBlank(value) || "null".equals(value)){
					name = name.toUpperCase();
					value = jsonOb.optString(name);
				}
			}
			//是否必须输入判断
			if(value == null || value.length() == 0 || "null".equals(value)){
				if(true == mustHave){
					throw new RuntimeException("sqlId为" + commonQuery.getSqlId() + "的查询中,缺少参数(值):" + name); 
				}else{
					if("SEQ".equals(function)){//序列Seq功能实现
						if (StringUtil.isNullOrBlankTrim(value)) {
							String seqName = jo.getString("SEQ_NAME");
							if (StringUtil.isNullOrBlankTrim(seqName)) {
								throw new RuntimeException("sqlId为" + commonQuery.getSqlId() + "中,缺少序列名称:" + name); 
							}
							value = SeqGetUtil.getNoSubSequence(ExportUtil.SUB_SYSTEM, jo.getString("SEQ_NAME").toLowerCase()).toString();
							if (map.containsKey(name)) {
								name = name + "_" + UUID.randomUUID();
							}
							map.put(name, value);
							jsonOb.put(name, value);
							continue;
						}
					}else if ("AUTO_SEQ".equals(function)) {//自增序列
						if (StringUtil.isNullOrBlankTrim(value)) {
							String seqName = jo.getString("SEQ_NAME");
							if (StringUtil.isNullOrBlankTrim(seqName)) {
								throw new RuntimeException("sqlId为" + commonQuery.getSqlId() + "中,缺少序列名称:" + name); 
							}
							value = SeqGetUtil.getAutomicSequence(jo.getString("SEQ_NAME").toLowerCase(), 0L, 0L);
							if (map.containsKey(name)) {
								name = name + "_" + UUID.randomUUID();
							}
							map.put(name, value);
							jsonOb.put(name, value);
							continue;
						}
					}
					//不是必须输入，使用默认值填充
					String defaultValue = jo.optString("DEFAULT");
					value = defaultValue;
					String excuteFlag =  jo.optString("DEFAULT_FLAG");
					boolean excute = true;
					if (!StringUtil.isNullOrBlankTrim(excuteFlag)) {
						excute = false;
					}
					//填充失败使用，系统默认值填充,仅限插入，更新语句
					if(value == null || value.length() == 0 || "null".equals(value)){
						if ((CommonQuery.SQL_FLAG_INSERT.equals(commonQuery.getSqlFlag())|| CommonQuery.SQL_FLAG_UPDATE.equals(commonQuery.getSqlFlag()))&&excute ) {
							if ("NUMBER".equals(type.toUpperCase())) {
								defaultValue = "0";
							} else if ("STRING".equals(type.toUpperCase())) {
								defaultValue = " ";
							} else if ("DECIMAL".equals(type.toUpperCase())) {
								defaultValue = "0.0";
							} else if ("DATETIME".equals(type.toUpperCase())) {
								defaultValue = "NULL_DATE_DEFAULTVALUE";
							}
							value = defaultValue;
						}
					}
					
				}
			}
			jsonOb.put(name, value);
			
			//如果value值不為空，且長度大於 0 
			if(value != null && value.length() > 0 && !"null".equals(value)){
				if ("before".equals(fuzzyQuery)) {
					value = "%" + value;
				} else if ("after".equals(fuzzyQuery)) {
					value = value + "%";
				} else if ("around".equals(fuzzyQuery)) {
					value = "%" + value + "%";
				}
				 
				if("in".equals(qtype)){ //查询条件是 in
					value = value.replace("'", "");
					String[] ary = value.split(",");
					StringBuffer buffer = new StringBuffer();
					for (int j = 0; j < ary.length; j++) {
						if(buffer.length()> 0){
							buffer.append(",");
						}
						if(!"number".equals(jo.optString("type"))){
							buffer.append("'"+ary[j]+"'");
						}else{
							buffer.append(ary[j]);
						}
					}
					
					String sqlContent = commonQuery.getSqlContent();
					commonQuery.setSqlContent(sqlContent.replaceFirst("in\\s*\\?", " in ("+ buffer.toString()+")"));
					continue;
				}
			}else if("in".equals(qtype)){ //查询条件是 in,但是没有输入值
				String sqlContent = commonQuery.getSqlContent();
				commonQuery.setSqlContent(sqlContent.replaceFirst("in\\s*\\?", " in (?)"));
			}
			String key = name;
			if (map.containsKey(key)) {
				key = name + "_" + UUID.randomUUID();
			}
			//分库shard_id功能实现
			if(StringUtil.isAllNotNullOrBlank(function)){
				if("SHARD_ID".equals(function)){
					value = SeqGetUtil.getSharedId(value).toString();
				}
			}
			if ("PGSQL".equals(commonQuery.getDbType().toUpperCase())) {
				if (StringUtil.isNullOrBlankTrim(value)) {
					map.put(key, value);
				}else if ("NUMBER".equals(type.toUpperCase())) {
					Long s = Long.valueOf(value);
					map.put(key, s);
				} else if ("STRING".equals(type.toUpperCase())) {
					map.put(key, value);
				} else if ("DECIMAL".equals(type.toUpperCase())) {
					Double s = Double.valueOf(value);
					map.put(key, s);
				} else if ("DATETIME".equals(type.toUpperCase())||"DATE".equals(type.toUpperCase())) {
					Date s = Date.valueOf(value);
					map.put(key, s);
				}
			}else{
				map.put(key, value);
			}
			if(!StringUtil.isNullOrBlankTrim(jo.optString("mapfile"))){
				commonQuery.paramMap.put(key, jo.optString("mapfile"));
			}
		}
		 
		return map;	
	}
	*/
	
	//用Map改写
	public static Map<String, Object> getParamsWithMap(CommonQuery commonQuery,
			JSONArray ja, Map<String,Object> inputParam) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			String name = jo.optString("NAME").trim();
			/*if(name.toLowerCase().equals("productlowclassid")){
				System.out.println("---------");
			}*/
			if(StringUtil.isNullOrBlankTrim(name)){
				name = jo.optString("name").trim();
			}
			if(StringUtil.isNullOrBlankTrim(name)){
				throw new RuntimeException("sqlId为" + commonQuery.getSqlId() + "的查询中配置name参数值为空"); 
			}else{
				name = name.toLowerCase();
			}
		
			String value = "";
			String type = jo.getString("TYPE");
			String function = jo.optString("FUNCTION");
			//in 查询处理
			String qtype = jo.optString("QTYPE");
			if(StringUtil.isNullOrBlankTrim(qtype)){
				qtype = jo.optString("qtype");
			}
			// 模糊查询标志 参数为:before after around
			String fuzzyQuery = ""; 
			if (jo.has("fuzzyquery") || jo.containsKey("fuzzyquery")) {
				fuzzyQuery = jo.getString("fuzzyquery");
			}
			if (jo.has("FUZZYQUERY") || jo.containsKey("FUZZYQUERY")) {
				fuzzyQuery = jo.getString("FUZZYQUERY");
			}
			
			boolean mustHave = jo.getBoolean("MUSTHAVE");
			
			//if(jsonOb.containsKey(name) || jsonOb.containsKey(name.toUpperCase())){
			if(inputParam.containsKey(name) || inputParam.containsKey(name.toUpperCase())){
				//value = jsonOb.optString(name);
				Object obj=inputParam.get(name);
				if (obj==null) {
					value=null;
				} else {
					value=obj.toString();
				}
				//如果没有取值到，通过大写key取值
				if(StringUtil.isAllNullOrBlank(value) || "null".equals(value)){
					name = name.toUpperCase();
					obj=inputParam.get(name);
					if (obj==null) {
						value=null;
					} else {
						value=obj.toString();
					}
				}
			}
			//是否必须输入判断
			if(value == null || value.length() == 0 || "null".equals(value)){
				if(true == mustHave){
					throw new RuntimeException("sqlId为" + commonQuery.getSqlId() + "的查询中,缺少参数(值):" + name); 
				}else{
					if("SEQ".equals(function)){//序列Seq功能实现
						if (StringUtil.isNullOrBlankTrim(value)) {
							String seqName = jo.getString("SEQ_NAME");
							if (StringUtil.isNullOrBlankTrim(seqName)) {
								throw new RuntimeException("sqlId为" + commonQuery.getSqlId() + "中,缺少序列名称:" + name); 
							}
							value = SeqGetUtil.getNoSubSequence(ExportUtil.SUB_SYSTEM, jo.getString("SEQ_NAME").toLowerCase()).toString();
							if (map.containsKey(name)) {
								name = name + "_" + UUID.randomUUID();
							}
							map.put(name, value);
							//jsonOb.put(name, value);
							inputParam.put(name, value);
							continue;
						}
					}else if ("AUTO_SEQ".equals(function)) {//自增序列
						if (StringUtil.isNullOrBlankTrim(value)) {
							String seqName = jo.getString("SEQ_NAME");
							if (StringUtil.isNullOrBlankTrim(seqName)) {
								throw new RuntimeException("sqlId为" + commonQuery.getSqlId() + "中,缺少序列名称:" + name); 
							}
							value = SeqGetUtil.getAutomicSequence(jo.getString("SEQ_NAME").toLowerCase(), 0L, 0L);
							if (map.containsKey(name)) {
								name = name + "_" + UUID.randomUUID();
							}
							map.put(name, value);
							//jsonOb.put(name, value);
							inputParam.put(name, value);
							continue;
						}
					}
					//不是必须输入，使用默认值填充
					String defaultValue = jo.optString("DEFAULT");
					value = defaultValue;
					String excuteFlag =  jo.optString("DEFAULT_FLAG");
					boolean excute = true;
					if (!StringUtil.isNullOrBlankTrim(excuteFlag)) {
						excute = false;
					}
					//填充失败使用，系统默认值填充,仅限插入，更新语句
					if(value == null || value.length() == 0 || "null".equals(value)){
						if ((CommonQuery.SQL_FLAG_INSERT.equals(commonQuery.getSqlFlag())|| CommonQuery.SQL_FLAG_UPDATE.equals(commonQuery.getSqlFlag()))&&excute ) {
							if ("NUMBER".equals(type.toUpperCase())) {
								defaultValue = "0";
							} else if ("STRING".equals(type.toUpperCase())) {
								defaultValue = " ";
							} else if ("DECIMAL".equals(type.toUpperCase())) {
								defaultValue = "0.0";
							} else if ("DATETIME".equals(type.toUpperCase())) {
								defaultValue = "NULL_DATE_DEFAULTVALUE";
							}
							value = defaultValue;
						}
					}
					
				}
			}
			//jsonOb.put(name, value);
			inputParam.put(name, value);
			
			//如果value值不為空，且長度大於 0 
			if(value != null && value.length() > 0 && !"null".equals(value)){
				if ("before".equals(fuzzyQuery)) {
					value = "%" + value;
				} else if ("after".equals(fuzzyQuery)) {
					value = value + "%";
				} else if ("around".equals(fuzzyQuery)) {
					value = "%" + value + "%";
				}
				 
				if("in".equals(qtype)){ //查询条件是 in
					value = value.replace("'", "");
					String[] ary = value.split(",");
					StringBuffer buffer = new StringBuffer();
					for (int j = 0; j < ary.length; j++) {
						if(buffer.length()> 0){
							buffer.append(",");
						}
						if(!"number".equals(jo.optString("type"))){
							buffer.append("'"+ary[j].trim()+"'"); 
						}else{
							buffer.append(ary[j]);
						}
					}
					
					String sqlContent = commonQuery.getSqlContent();
					commonQuery.setSqlContent(sqlContent.replaceFirst("in\\s*\\?", " in ("+ buffer.toString()+")"));
					continue;
				}
			}else if("in".equals(qtype)){ //查询条件是 in,但是没有输入值
				String sqlContent = commonQuery.getSqlContent();
				commonQuery.setSqlContent(sqlContent.replaceFirst("in\\s*\\?", " in (?)"));
			}
			String key = name;
			if (map.containsKey(key)) {
				key = name + "_" + UUID.randomUUID();
			}
			//分库shard_id功能实现
			if(StringUtil.isAllNotNullOrBlank(function)){
				if("SHARD_ID".equals(function)){
					value = SeqGetUtil.getSharedId(value).toString();
				}
			}
			if ("PGSQL".equals(commonQuery.getDbType().toUpperCase())) {
				if (StringUtil.isNullOrBlankTrim(value)) {
					map.put(key, value);
				}else if ("NUMBER".equals(type.toUpperCase())) {
					Long s = Long.valueOf(value);
					map.put(key, s);
				} else if ("STRING".equals(type.toUpperCase())) {
					map.put(key, value);
				} else if ("DECIMAL".equals(type.toUpperCase())) {
					Double s = Double.valueOf(value);
					map.put(key, s);
				} else if ("DATETIME".equals(type.toUpperCase())||"DATE".equals(type.toUpperCase())) {
					Date s = Date.valueOf(value);
					map.put(key, s);
				}
			}else{
				map.put(key, value);
			}
			if(!StringUtil.isNullOrBlankTrim(jo.optString("mapfile"))){
				commonQuery.paramMap.put(key, jo.optString("mapfile"));
			}
		}
		 
		return map;	
	}

	
	public static Object[] parseSqlById(CommonQuery commonQuery,JSONObject inputParam){
		ExcuteSqlResultModel resModel = new ExcuteSqlResultModel();
		String sqlContent = commonQuery.getSqlContent();
		String jsonContent = commonQuery.getParamContent();
		JSONArray ja = JSONArray.fromObject(jsonContent);
		Map<String, Object> map = CommUtil.getParamsWithMap(commonQuery, ja, inputParam);
		sqlContent = CommUtil.sqlHandler(sqlContent, map);
		List arrList= new ArrayList();
		Object os [] = map.values().toArray();
		for(Object o : os){
			if(o!=null){
				arrList.add(o);
			}
		}
		Object arr [] = arrList.toArray();
		return arr;
	}
	 
}
