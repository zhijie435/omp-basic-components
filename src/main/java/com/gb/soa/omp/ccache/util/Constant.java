package com.gb.soa.omp.ccache.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈世荣
 * @date 2016年10月13日 下午3:50:44
 * @version <b>1.0.0</b>
 */
public class Constant {
	
	public static final String SUB_SYSTEM="ccache";
	
	public static final Long SYS_NUM_ID=18L;
	
	public final static String COMMON_DOSSIER_DATA_SOURCE_KEY="commonDossier";
	
	public static ThreadPoolExecutor tExecutor = new ThreadPoolExecutor(10, 30, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(500),new ThreadPoolExecutor.DiscardPolicy());
}
