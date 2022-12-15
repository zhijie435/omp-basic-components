package com.gb.soa.omp.ccache.util;

import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈世荣
 * @date 2016年10月13日 下午3:50:44
 * @version <b>1.0.0</b>
 */
public class Constant {
	@Value("${spring.application.name}")
	public static final String SUB_SYSTEM="";

	public static ThreadPoolExecutor tExecutor = new ThreadPoolExecutor(10, 30, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(500),new ThreadPoolExecutor.DiscardPolicy());
}
