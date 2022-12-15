package com.gb.soa.omp.ccache.entity;

import java.util.Date;

public class EC_CACHE_METHOD_SCHEMA_DEFINE {

	private String SERIES;
	private Long TENANT_NUM_ID;
	private Long DATA_SIGN;
	private String SUB_SYSTEM;
	private String METHOD_NAME;
	private String SQL_CONTENT;
	private String DB;
	private String CACHE_METHOD;
	private String CACHE_MULTI_COL;
	private Long TTL;
	private Long LIST_SIGN;
	private Long ALLOW_LIST_EMPTY_SIGN;
	private Date CREATE_DTME;
	private Date LAST_UPDTME;
	private Long CREATE_USER_ID;
	private Long LAST_UPDATE_USER_ID;
	private String DESCRIPTION;

	/**
	 * 获得SERIES
	 *
	 * @return String
	 */
	public String getSERIES() {
		return SERIES;
	}

	/**
	 * 设置SERIES
	 *
	 */
	public void setSERIES(String sERIES) {
		SERIES = sERIES;
	}

	/**
	 * 获得TENANT_NUM_ID
	 *
	 * @return Long
	 */
	public Long getTENANT_NUM_ID() {
		return TENANT_NUM_ID;
	}

	/**
	 * 设置TENANT_NUM_ID
	 *
	 */
	public void setTENANT_NUM_ID(Long tENANT_NUM_ID) {
		TENANT_NUM_ID = tENANT_NUM_ID;
	}

	/**
	 * 获得DATA_SIGN
	 *
	 * @return Long
	 */
	public Long getDATA_SIGN() {
		return DATA_SIGN;
	}

	/**
	 * 设置DATA_SIGN
	 *
	 */
	public void setDATA_SIGN(Long dATA_SIGN) {
		DATA_SIGN = dATA_SIGN;
	}

	/**
	 * 获得SUB_SYSTEM
	 *
	 * @return String
	 */
	public String getSUB_SYSTEM() {
		return SUB_SYSTEM;
	}

	/**
	 * 设置SUB_SYSTEM
	 *
	 */
	public void setSUB_SYSTEM(String sUB_SYSTEM) {
		SUB_SYSTEM = sUB_SYSTEM;
	}

	/**
	 * 获得METHOD_NAME
	 *
	 * @return String
	 */
	public String getMETHOD_NAME() {
		return METHOD_NAME;
	}

	/**
	 * 设置METHOD_NAME
	 *
	 */
	public void setMETHOD_NAME(String mETHOD_NAME) {
		METHOD_NAME = mETHOD_NAME;
	}

	/**
	 * 获得SQL_CONTENT
	 *
	 * @return String
	 */
	public String getSQL_CONTENT() {
		return SQL_CONTENT;
	}

	/**
	 * 设置SQL_CONTENT
	 *
	 */
	public void setSQL_CONTENT(String sQL_CONTENT) {
		SQL_CONTENT = sQL_CONTENT;
	}

	/**
	 * 获得DB
	 *
	 * @return String
	 */
	public String getDB() {
		return DB;
	}

	/**
	 * 设置DB
	 *
	 */
	public void setDB(String dB) {
		DB = dB;
	}

	/**
	 * 获得CACHE_METHOD
	 *
	 * @return String
	 */
	public String getCACHE_METHOD() {
		return CACHE_METHOD;
	}

	/**
	 * 设置CACHE_METHOD
	 *
	 */
	public void setCACHE_METHOD(String cACHE_METHOD) {
		CACHE_METHOD = cACHE_METHOD;
	}

	/**
	 * 获得CACHE_MULTI_COL
	 *
	 * @return String
	 */
	public String getCACHE_MULTI_COL() {
		return CACHE_MULTI_COL;
	}

	/**
	 * 设置CACHE_MULTI_COL
	 *
	 */
	public void setCACHE_MULTI_COL(String cACHE_MULTI_COL) {
		CACHE_MULTI_COL = cACHE_MULTI_COL;
	}

	/**
	 * 获得TTL
	 *
	 * @return Long
	 */
	public Long getTTL() {
		return TTL;
	}

	/**
	 * 设置TTL
	 *
	 */
	public void setTTL(Long tTL) {
		TTL = tTL;
	}

	/**
	 * 获得LIST_SIGN
	 *
	 * @return Long
	 */
	public Long getLIST_SIGN() {
		return LIST_SIGN;
	}

	/**
	 * 设置LIST_SIGN
	 *
	 */
	public void setLIST_SIGN(Long lIST_SIGN) {
		LIST_SIGN = lIST_SIGN;
	}

	/**
	 * 获得CREATE_DTME
	 *
	 * @return Date
	 */
	public Date getCREATE_DTME() {
		return CREATE_DTME;
	}

	/**
	 * 设置CREATE_DTME
	 *
	 */
	public void setCREATE_DTME(Date cREATE_DTME) {
		CREATE_DTME = cREATE_DTME;
	}

	/**
	 * 获得LAST_UPDTME
	 *
	 * @return Date
	 */
	public Date getLAST_UPDTME() {
		return LAST_UPDTME;
	}

	/**
	 * 设置LAST_UPDTME
	 *
	 */
	public void setLAST_UPDTME(Date lAST_UPDTME) {
		LAST_UPDTME = lAST_UPDTME;
	}

	/**
	 * 获得CREATE_USER_ID
	 *
	 * @return Long
	 */
	public Long getCREATE_USER_ID() {
		return CREATE_USER_ID;
	}

	/**
	 * 设置CREATE_USER_ID
	 *
	 */
	public void setCREATE_USER_ID(Long cREATE_USER_ID) {
		CREATE_USER_ID = cREATE_USER_ID;
	}

	/**
	 * 获得LAST_UPDATE_USER_ID
	 *
	 * @return Long
	 */
	public Long getLAST_UPDATE_USER_ID() {
		return LAST_UPDATE_USER_ID;
	}

	/**
	 * 设置LAST_UPDATE_USER_ID
	 *
	 */
	public void setLAST_UPDATE_USER_ID(Long lAST_UPDATE_USER_ID) {
		LAST_UPDATE_USER_ID = lAST_UPDATE_USER_ID;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}

	public Long getALLOW_LIST_EMPTY_SIGN() {
		return ALLOW_LIST_EMPTY_SIGN;
	}

	public void setALLOW_LIST_EMPTY_SIGN(Long aLLOW_LIST_EMPTY_SIGN) {
		ALLOW_LIST_EMPTY_SIGN = aLLOW_LIST_EMPTY_SIGN;
	}

}
