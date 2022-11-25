package com.gb.soa.omp.export.entity;

import java.util.Date;

public class EXCEL_INPUT {
	   
	 //行号
      private  Long SERIES;
     
	 //批次号
      private  Long BATCH_NO;
     
	 //批次sheet
      private  String SHEET_ID;
     
	 //excel行号
      private  Long EXCEL_LINE;
     
	 //系统编号
      private  Long SYS_NUM_ID = 0L;
     
	 //系统名称
      private  String SYS_NAME;
     
	 //创建时间
      private  Date CREATE_DTME;
     
	 //最后更新时间
      private  Date LAST_UPDTME;
     
	 //用户
      private  Long CREATE_USER_ID = 0L;
     
	 //更新用户
      private  Long LAST_UPDATE_USER_ID = 0L;
     
	 //n为可使用,y为不使用
      private  String CANCEL_SIGN = "N";
     
	 //租户id
      private  Long TENANT_NUM_ID = 0L;
     
	 //0是正式,1是测试
      private  Integer DATA_SIGN = 0;
     
	 //0失败,1是成功
      private  Integer STATUS = 0;
     
	
      private  String EXCEL_A;
     
	
      private  String EXCEL_B;
     
	
      private  String EXCEL_C;
     
	
      private  String EXCEL_D;
     
	
      private  String EXCEL_E;
     
	
      private  String EXCEL_F;
     
	
      private  String EXCEL_G;
     
	
      private  String EXCEL_H;
     
	
      private  String EXCEL_I;
     
	
      private  String EXCEL_J;
     
	
      private  String EXCEL_K;
     
	
      private  String EXCEL_L;
     
	
      private  String EXCEL_M;
     
	
      private  String EXCEL_N;
     
	
      private  String EXCEL_O;
     
	
      private  String EXCEL_P;
     
	
      private  String EXCEL_Q;
     
	
      private  String EXCEL_R;
     
	
      private  String EXCEL_S;
     
	
      private  String EXCEL_T;
     
	
      private  String EXCEL_U;
     
	
      private  String EXCEL_V;
     
	
      private  String EXCEL_W;
     
	
      private  String EXCEL_X;
     
	
      private  String EXCEL_Y;
     
	
      private  String EXCEL_Z;
     
      
      public  Long  getSERIES(){
      		return  this.SERIES;
      };
      public  void  setSERIES(Long SERIES){
      	this.SERIES=SERIES;
      }  
     
      public  Long  getBATCH_NO(){
      		return  this.BATCH_NO;
      };
      public  void  setBATCH_NO(Long BATCH_NO){
      	this.BATCH_NO=BATCH_NO;
      }  
     
      public  String  getSHEET_ID(){
      		return  this.SHEET_ID;
      };
      public  void  setSHEET_ID(String SHEET_ID){
      	this.SHEET_ID=SHEET_ID;
      }  
     
      public  Long  getEXCEL_LINE(){
      		return  this.EXCEL_LINE;
      };
      public  void  setEXCEL_LINE(Long EXCEL_LINE){
      	this.EXCEL_LINE=EXCEL_LINE;
      }  
     
      public  Long  getSYS_NUM_ID(){
      		return  this.SYS_NUM_ID;
      };
      public  void  setSYS_NUM_ID(Long SYS_NUM_ID){
      	this.SYS_NUM_ID=SYS_NUM_ID;
      }  
     
      public  String  getSYS_NAME(){
      		return  this.SYS_NAME;
      };
      public  void  setSYS_NAME(String SYS_NAME){
      	this.SYS_NAME=SYS_NAME;
      }  
     
      public  Date  getCREATE_DTME(){
      		return  this.CREATE_DTME;
      };
      public  void  setCREATE_DTME(Date CREATE_DTME){
      	this.CREATE_DTME=CREATE_DTME;
      }  
     
      public  Date  getLAST_UPDTME(){
      		return  this.LAST_UPDTME;
      };
      public  void  setLAST_UPDTME(Date LAST_UPDTME){
      	this.LAST_UPDTME=LAST_UPDTME;
      }  
     
      public  Long  getCREATE_USER_ID(){
      		return  this.CREATE_USER_ID;
      };
      public  void  setCREATE_USER_ID(Long CREATE_USER_ID){
      	this.CREATE_USER_ID=CREATE_USER_ID;
      }  
     
      public  Long  getLAST_UPDATE_USER_ID(){
      		return  this.LAST_UPDATE_USER_ID;
      };
      public  void  setLAST_UPDATE_USER_ID(Long LAST_UPDATE_USER_ID){
      	this.LAST_UPDATE_USER_ID=LAST_UPDATE_USER_ID;
      }  
     
      public  String  getCANCEL_SIGN(){
      		return  this.CANCEL_SIGN;
      };
      public  void  setCANCEL_SIGN(String CANCEL_SIGN){
      	this.CANCEL_SIGN=CANCEL_SIGN;
      }  
     
      public  Long  getTENANT_NUM_ID(){
      		return  this.TENANT_NUM_ID;
      };
      public  void  setTENANT_NUM_ID(Long TENANT_NUM_ID){
      	this.TENANT_NUM_ID=TENANT_NUM_ID;
      }  
     
      public  Integer  getDATA_SIGN(){
      		return  this.DATA_SIGN;
      };
      public  void  setDATA_SIGN(Integer DATA_SIGN){
      	this.DATA_SIGN=DATA_SIGN;
      }  
     
      public  Integer  getSTATUS(){
      		return  this.STATUS;
      };
      public  void  setSTATUS(Integer STATUS){
      	this.STATUS=STATUS;
      }  
     
      public  String  getEXCEL_A(){
      		return  this.EXCEL_A;
      };
      public  void  setEXCEL_A(String EXCEL_A){
      	this.EXCEL_A=EXCEL_A;
      }  
     
      public  String  getEXCEL_B(){
      		return  this.EXCEL_B;
      };
      public  void  setEXCEL_B(String EXCEL_B){
      	this.EXCEL_B=EXCEL_B;
      }  
     
      public  String  getEXCEL_C(){
      		return  this.EXCEL_C;
      };
      public  void  setEXCEL_C(String EXCEL_C){
      	this.EXCEL_C=EXCEL_C;
      }  
     
      public  String  getEXCEL_D(){
      		return  this.EXCEL_D;
      };
      public  void  setEXCEL_D(String EXCEL_D){
      	this.EXCEL_D=EXCEL_D;
      }  
     
      public  String  getEXCEL_E(){
      		return  this.EXCEL_E;
      };
      public  void  setEXCEL_E(String EXCEL_E){
      	this.EXCEL_E=EXCEL_E;
      }  
     
      public  String  getEXCEL_F(){
      		return  this.EXCEL_F;
      };
      public  void  setEXCEL_F(String EXCEL_F){
      	this.EXCEL_F=EXCEL_F;
      }  
     
      public  String  getEXCEL_G(){
      		return  this.EXCEL_G;
      };
      public  void  setEXCEL_G(String EXCEL_G){
      	this.EXCEL_G=EXCEL_G;
      }  
     
      public  String  getEXCEL_H(){
      		return  this.EXCEL_H;
      };
      public  void  setEXCEL_H(String EXCEL_H){
      	this.EXCEL_H=EXCEL_H;
      }  
     
      public  String  getEXCEL_I(){
      		return  this.EXCEL_I;
      };
      public  void  setEXCEL_I(String EXCEL_I){
      	this.EXCEL_I=EXCEL_I;
      }  
     
      public  String  getEXCEL_J(){
      		return  this.EXCEL_J;
      };
      public  void  setEXCEL_J(String EXCEL_J){
      	this.EXCEL_J=EXCEL_J;
      }  
     
      public  String  getEXCEL_K(){
      		return  this.EXCEL_K;
      };
      public  void  setEXCEL_K(String EXCEL_K){
      	this.EXCEL_K=EXCEL_K;
      }  
     
      public  String  getEXCEL_L(){
      		return  this.EXCEL_L;
      };
      public  void  setEXCEL_L(String EXCEL_L){
      	this.EXCEL_L=EXCEL_L;
      }  
     
      public  String  getEXCEL_M(){
      		return  this.EXCEL_M;
      };
      public  void  setEXCEL_M(String EXCEL_M){
      	this.EXCEL_M=EXCEL_M;
      }  
     
      public  String  getEXCEL_N(){
      		return  this.EXCEL_N;
      };
      public  void  setEXCEL_N(String EXCEL_N){
      	this.EXCEL_N=EXCEL_N;
      }  
     
      public  String  getEXCEL_O(){
      		return  this.EXCEL_O;
      };
      public  void  setEXCEL_O(String EXCEL_O){
      	this.EXCEL_O=EXCEL_O;
      }  
     
      public  String  getEXCEL_P(){
      		return  this.EXCEL_P;
      };
      public  void  setEXCEL_P(String EXCEL_P){
      	this.EXCEL_P=EXCEL_P;
      }  
     
      public  String  getEXCEL_Q(){
      		return  this.EXCEL_Q;
      };
      public  void  setEXCEL_Q(String EXCEL_Q){
      	this.EXCEL_Q=EXCEL_Q;
      }  
     
      public  String  getEXCEL_R(){
      		return  this.EXCEL_R;
      };
      public  void  setEXCEL_R(String EXCEL_R){
      	this.EXCEL_R=EXCEL_R;
      }  
     
      public  String  getEXCEL_S(){
      		return  this.EXCEL_S;
      };
      public  void  setEXCEL_S(String EXCEL_S){
      	this.EXCEL_S=EXCEL_S;
      }  
     
      public  String  getEXCEL_T(){
      		return  this.EXCEL_T;
      };
      public  void  setEXCEL_T(String EXCEL_T){
      	this.EXCEL_T=EXCEL_T;
      }  
     
      public  String  getEXCEL_U(){
      		return  this.EXCEL_U;
      };
      public  void  setEXCEL_U(String EXCEL_U){
      	this.EXCEL_U=EXCEL_U;
      }  
     
      public  String  getEXCEL_V(){
      		return  this.EXCEL_V;
      };
      public  void  setEXCEL_V(String EXCEL_V){
      	this.EXCEL_V=EXCEL_V;
      }  
     
      public  String  getEXCEL_W(){
      		return  this.EXCEL_W;
      };
      public  void  setEXCEL_W(String EXCEL_W){
      	this.EXCEL_W=EXCEL_W;
      }  
     
      public  String  getEXCEL_X(){
      		return  this.EXCEL_X;
      };
      public  void  setEXCEL_X(String EXCEL_X){
      	this.EXCEL_X=EXCEL_X;
      }  
     
      public  String  getEXCEL_Y(){
      		return  this.EXCEL_Y;
      };
      public  void  setEXCEL_Y(String EXCEL_Y){
      	this.EXCEL_Y=EXCEL_Y;
      }  
     
      public  String  getEXCEL_Z(){
      		return  this.EXCEL_Z;
      };
      public  void  setEXCEL_Z(String EXCEL_Z){
      	this.EXCEL_Z=EXCEL_Z;
      }  
     
    
}