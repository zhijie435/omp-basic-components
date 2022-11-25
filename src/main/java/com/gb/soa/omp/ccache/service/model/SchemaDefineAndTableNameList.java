package com.gb.soa.omp.ccache.service.model;

import com.gb.soa.omp.ccache.entity.EC_CACHE_METHOD_SCHEMA_DEFINE;

import java.util.List;

public class SchemaDefineAndTableNameList {
	private EC_CACHE_METHOD_SCHEMA_DEFINE schemaDefine;
	private List<String> tableNameList;

	public EC_CACHE_METHOD_SCHEMA_DEFINE getSchemaDefine() {
		return schemaDefine;
	}
	public void setSchemaDefine(EC_CACHE_METHOD_SCHEMA_DEFINE schemaDefine) {
		this.schemaDefine = schemaDefine;
	}
	public List<String> getTableNameList() {
		return tableNameList;
	}
	public void setTableNameList(List<String> tableNameList) {
		this.tableNameList = tableNameList;
	}


}
