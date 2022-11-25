package com.gb.soa.omp.export.entity;

import java.util.Date;

public class COMMON_QUERY {
	private Long SERIES;
	private String SQL_NAME;
	private String SQL_ID;
	
	private String  SQL_CONTENT;
	private String  PARAM_CONTENT;
	private String  JDBC_NAME;
	private Date CREATE_DTME;
	
	private Date  LAST_UPDTME;
	private String CREATE_USER_ID;
	private String LAST_UPDATE_USER_ID;
	private  String CANCEL_SIGN;
	private Long  TENANT_NUM_ID;
	private Long  DATA_SIGN;
	private String DB_TYPE;
	public Long getSERIES() {
		return SERIES;
	}
	public void setSERIES(Long sERIES) {
		SERIES = sERIES;
	}
	public String getSQL_NAME() {
		return SQL_NAME;
	}
	public void setSQL_NAME(String sQL_NAME) {
		SQL_NAME = sQL_NAME;
	}
	public String getSQL_ID() {
		return SQL_ID;
	}
	public void setSQL_ID(String sQL_ID) {
		SQL_ID = sQL_ID;
	}
	public String getSQL_CONTENT() {
		return SQL_CONTENT;
	}
	public void setSQL_CONTENT(String sQL_CONTENT) {
		SQL_CONTENT = sQL_CONTENT;
	}
	public String getPARAM_CONTENT() {
		return PARAM_CONTENT;
	}
	public void setPARAM_CONTENT(String pARAM_CONTENT) {
		PARAM_CONTENT = pARAM_CONTENT;
	}
	public String getJDBC_NAME() {
		return JDBC_NAME;
	}
	public void setJDBC_NAME(String jDBC_NAME) {
		JDBC_NAME = jDBC_NAME;
	}
	public Date getCREATE_DTME() {
		return CREATE_DTME;
	}
	public void setCREATE_DTME(Date cREATE_DTME) {
		CREATE_DTME = cREATE_DTME;
	}
	public Date getLAST_UPDTME() {
		return LAST_UPDTME;
	}
	public void setLAST_UPDTME(Date lAST_UPDTME) {
		LAST_UPDTME = lAST_UPDTME;
	}
	public String getCREATE_USER_ID() {
		return CREATE_USER_ID;
	}
	public void setCREATE_USER_ID(String cREATE_USER_ID) {
		CREATE_USER_ID = cREATE_USER_ID;
	}
	public String getLAST_UPDATE_USER_ID() {
		return LAST_UPDATE_USER_ID;
	}
	public void setLAST_UPDATE_USER_ID(String lAST_UPDATE_USER_ID) {
		LAST_UPDATE_USER_ID = lAST_UPDATE_USER_ID;
	}
	public String getCANCEL_SIGN() {
		return CANCEL_SIGN;
	}
	public void setCANCEL_SIGN(String cANCEL_SIGN) {
		CANCEL_SIGN = cANCEL_SIGN;
	}
	public Long getTENANT_NUM_ID() {
		return TENANT_NUM_ID;
	}
	public void setTENANT_NUM_ID(Long tENANT_NUM_ID) {
		TENANT_NUM_ID = tENANT_NUM_ID;
	}
	public Long getDATA_SIGN() {
		return DATA_SIGN;
	}
	public void setDATA_SIGN(Long dATA_SIGN) {
		DATA_SIGN = dATA_SIGN;
	}
	public String getDB_TYPE() {
		return DB_TYPE;
	}
	public void setDB_TYPE(String dB_TYPE) {
		DB_TYPE = dB_TYPE;
	}
	
	

}
