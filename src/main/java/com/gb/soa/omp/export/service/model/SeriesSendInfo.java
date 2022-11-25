/*
 * PROJECT NAME: omp_cbillsync
 * PACKAGE NAME: com.gb.soa.omp.billsync.service.model
 * FILE    NAME: SeriesSendInfo.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */
package com.gb.soa.omp.export.service.model;

public class SeriesSendInfo {

	private String series;

	private String sourceSeries;

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	/**
	 * 获得sourceSeries
	 * @return String
	 */
	public String getSourceSeries() {
		return sourceSeries;
	}

	/**
	 * 设置sourceSeries
	 * @param sourceSeries
	 */
	public void setSourceSeries(String sourceSeries) {
		this.sourceSeries = sourceSeries;
	}

}
