package com.gb.soa.omp.export.entity;

import java.util.Date;

public class EXCEL_INPUT_HDR {

	// 行号
	private Long SERIES;

	// 批次号
	private Long BATCH_NO;

	// 导入编码
	private Long INPUT_NUM_ID = 0L;

	// 文件名自动加时分秒后缀
	private String FILE_NAME = "";

	// 文件路径
	private String FILE_PATH = "";

	// 下载次数
	private Long DOWNLOAD_QTY = 0L;

	// 文件记录数
	private Long TOTAL_ROWS = 0L;

	// 成功导入记录数
	private Long IMPORT_ROWS = 0L;

	// 失败记录数
	private Long FAIL_ROWS = 0L;

	// 警告记录数
	private Long WARNING_ROWS = 0L;

	// 失败原因
	private String FAIL_CAUSE = "0";

	private Integer EXCEL_TYPE = 0;

	// 创建时间
	private Date CREATE_DTME = new Date();

	// 最后更新时间
	private Date LAST_UPDTME = new Date();

	// 用户
	private Long CREATE_USER_ID = 0L;

	// 更新用户
	private Long LAST_UPDATE_USER_ID = 0L;

	// n为可使用,y为不使用
	private String CANCEL_SIGN = "N";

	// 租户id
	private Long TENANT_NUM_ID = 0L;

	// 0是正式,1是测试
	private Long DATA_SIGN = 0L;

	public Integer getEXCEL_TYPE() {
		return EXCEL_TYPE;
	}

	public void setEXCEL_TYPE(Integer eXCEL_TYPE) {
		EXCEL_TYPE = eXCEL_TYPE;
	}

	public Long getSERIES() {
		return this.SERIES;
	};

	public void setSERIES(Long SERIES) {
		this.SERIES = SERIES;
	}

	public Long getBATCH_NO() {
		return this.BATCH_NO;
	};

	public void setBATCH_NO(Long BATCH_NO) {
		this.BATCH_NO = BATCH_NO;
	}

	public Long getINPUT_NUM_ID() {
		return this.INPUT_NUM_ID;
	};

	public void setINPUT_NUM_ID(Long INPUT_NUM_ID) {
		this.INPUT_NUM_ID = INPUT_NUM_ID;
	}

	public String getFILE_NAME() {
		return this.FILE_NAME;
	};

	public void setFILE_NAME(String FILE_NAME) {
		this.FILE_NAME = FILE_NAME;
	}

	public String getFILE_PATH() {
		return this.FILE_PATH;
	};

	public void setFILE_PATH(String FILE_PATH) {
		this.FILE_PATH = FILE_PATH;
	}

	public Long getDOWNLOAD_QTY() {
		return this.DOWNLOAD_QTY;
	};

	public void setDOWNLOAD_QTY(Long DOWNLOAD_QTY) {
		this.DOWNLOAD_QTY = DOWNLOAD_QTY;
	}

	public Long getTOTAL_ROWS() {
		return this.TOTAL_ROWS;
	};

	public void setTOTAL_ROWS(Long TOTAL_ROWS) {
		this.TOTAL_ROWS = TOTAL_ROWS;
	}

	public Long getIMPORT_ROWS() {
		return this.IMPORT_ROWS;
	};

	public void setIMPORT_ROWS(Long IMPORT_ROWS) {
		this.IMPORT_ROWS = IMPORT_ROWS;
	}

	public Long getFAIL_ROWS() {
		return this.FAIL_ROWS;
	};

	public void setFAIL_ROWS(Long FAIL_ROWS) {
		this.FAIL_ROWS = FAIL_ROWS;
	}

	public Long getWARNING_ROWS() {
		return this.WARNING_ROWS;
	};

	public void setWARNING_ROWS(Long WARNING_ROWS) {
		this.WARNING_ROWS = WARNING_ROWS;
	}

	public String getFAIL_CAUSE() {
		return this.FAIL_CAUSE;
	};

	public void setFAIL_CAUSE(String FAIL_CAUSE) {
		this.FAIL_CAUSE = FAIL_CAUSE;
	}

	public Date getCREATE_DTME() {
		return this.CREATE_DTME;
	};

	public void setCREATE_DTME(Date CREATE_DTME) {
		this.CREATE_DTME = CREATE_DTME;
	}

	public Date getLAST_UPDTME() {
		return this.LAST_UPDTME;
	};

	public void setLAST_UPDTME(Date LAST_UPDTME) {
		this.LAST_UPDTME = LAST_UPDTME;
	}

	public Long getCREATE_USER_ID() {
		return this.CREATE_USER_ID;
	};

	public void setCREATE_USER_ID(Long CREATE_USER_ID) {
		this.CREATE_USER_ID = CREATE_USER_ID;
	}

	public Long getLAST_UPDATE_USER_ID() {
		return this.LAST_UPDATE_USER_ID;
	};

	public void setLAST_UPDATE_USER_ID(Long LAST_UPDATE_USER_ID) {
		this.LAST_UPDATE_USER_ID = LAST_UPDATE_USER_ID;
	}

	public String getCANCEL_SIGN() {
		return this.CANCEL_SIGN;
	};

	public void setCANCEL_SIGN(String CANCEL_SIGN) {
		this.CANCEL_SIGN = CANCEL_SIGN;
	}

	public Long getTENANT_NUM_ID() {
		return this.TENANT_NUM_ID;
	};

	public void setTENANT_NUM_ID(Long TENANT_NUM_ID) {
		this.TENANT_NUM_ID = TENANT_NUM_ID;
	}

	public Long getDATA_SIGN() {
		return this.DATA_SIGN;
	};

	public void setDATA_SIGN(Long DATA_SIGN) {
		this.DATA_SIGN = DATA_SIGN;
	}

}