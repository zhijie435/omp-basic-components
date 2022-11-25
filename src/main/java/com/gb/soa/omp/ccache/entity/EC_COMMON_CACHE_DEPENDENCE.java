package com.gb.soa.omp.ccache.entity;

import java.util.Date;

/**
 * @auther: wenfeng
 * @date: 2020/6/12 11:14
 */
public class EC_COMMON_CACHE_DEPENDENCE {
    private String SERIES;                //行号
    private Long TENANT_NUM_ID;            //租户ID
    private Long DATA_SIGN;             //0: 正式  1：测试
    private String METHOD_NAME;          //方法名
    private String DB;                   //数据库
    private String CACHE_KEY;           //缓存键名
    private String PARAMS;              //方法入参,刷新用
    private String TABLE_NAME;           //表名
    private String TABLE_SERIES;          //引用表的行号
    private Date CREATE_DTME;             //创建时间
    private Date LAST_UPDTME;             //最后更新时间
    private Long CREATE_USER_ID;         //用户
    private Long LAST_UPDATE_USER_ID;     //更新用户
    private String DUBBO_GROUP; // dubbo分组

    public String getDUBBO_GROUP() {
        return DUBBO_GROUP;
    }

    public void setDUBBO_GROUP(String DUBBO_GROUP) {
        this.DUBBO_GROUP = DUBBO_GROUP;
    }

    public String getSERIES() {
		return SERIES;
	}

	public void setSERIES(String sERIES) {
		SERIES = sERIES;
	}

	public Long getTENANT_NUM_ID() {
        return TENANT_NUM_ID;
    }

    public void setTENANT_NUM_ID(Long TENANT_NUM_ID) {
        this.TENANT_NUM_ID = TENANT_NUM_ID;
    }

    public Long getDATA_SIGN() {
        return DATA_SIGN;
    }

    public void setDATA_SIGN(Long DATA_SIGN) {
        this.DATA_SIGN = DATA_SIGN;
    }

    public String getMETHOD_NAME() {
        return METHOD_NAME;
    }

    public void setMETHOD_NAME(String METHOD_NAME) {
        this.METHOD_NAME = METHOD_NAME;
    }

    public String getDB() {
        return DB;
    }

    public void setDB(String DB) {
        this.DB = DB;
    }

    public String getCACHE_KEY() {
        return CACHE_KEY;
    }

    public void setCACHE_KEY(String CACHE_KEY) {
        this.CACHE_KEY = CACHE_KEY;
    }

    public String getPARAMS() {
        return PARAMS;
    }

    public void setPARAMS(String PARAMS) {
        this.PARAMS = PARAMS;
    }

    public String getTABLE_NAME() {
        return TABLE_NAME;
    }

    public void setTABLE_NAME(String TABLE_NAME) {
        this.TABLE_NAME = TABLE_NAME;
    }

    public String getTABLE_SERIES() {
        return TABLE_SERIES;
    }

    public void setTABLE_SERIES(String TABLE_SERIES) {
        this.TABLE_SERIES = TABLE_SERIES;
    }

    public Date getCREATE_DTME() {
        return CREATE_DTME;
    }

    public void setCREATE_DTME(Date CREATE_DTME) {
        this.CREATE_DTME = CREATE_DTME;
    }

    public Date getLAST_UPDTME() {
        return LAST_UPDTME;
    }

    public void setLAST_UPDTME(Date LAST_UPDTME) {
        this.LAST_UPDTME = LAST_UPDTME;
    }

    public Long getCREATE_USER_ID() {
        return CREATE_USER_ID;
    }

    public void setCREATE_USER_ID(Long CREATE_USER_ID) {
        this.CREATE_USER_ID = CREATE_USER_ID;
    }

    public Long getLAST_UPDATE_USER_ID() {
        return LAST_UPDATE_USER_ID;
    }

    public void setLAST_UPDATE_USER_ID(Long LAST_UPDATE_USER_ID) {
        this.LAST_UPDATE_USER_ID = LAST_UPDATE_USER_ID;
    }
    
    
}
