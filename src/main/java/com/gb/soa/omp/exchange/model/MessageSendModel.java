/*
 * PROJECT NAME: omp_cexport
 * PACKAGE NAME: com.gb.soa.omp.export.service.model
 * FILE    NAME: MessageSendModel.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */ 
package com.gb.soa.omp.exchange.model;

import java.util.List;


/**
 * TODO（描述类的职责）
 * @author cjw
 * @date 2017年4月28日 下午3:51:53
 * @version <b>1.0.0</b>
 */
public class MessageSendModel {

	private String topic;
	private String tag;
	private String messageKey;
	private List<AddParamModel> addParam;
	private List<ForeachInputModel> foreachInput;
	private long importSystemId;
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getMessageKey() {
		return messageKey;
	}
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	public List<AddParamModel> getAddParam() {
		return addParam;
	}
	public void setAddParam(List<AddParamModel> addParam) {
		this.addParam = addParam;
	}
	public List<ForeachInputModel> getForeachInput() {
		return foreachInput;
	}
	public void setForeachInput(List<ForeachInputModel> foreachInput) {
		this.foreachInput = foreachInput;
	}
	public long getImportSystemId() {
		return importSystemId;
	}
	public void setImportSystemId(long importSystemId) {
		this.importSystemId = importSystemId;
	}
}
