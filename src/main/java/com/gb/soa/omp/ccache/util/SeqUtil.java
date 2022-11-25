package com.gb.soa.omp.ccache.util;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.gb.soa.sequence.util.SeqGetUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Component()
@Lazy(false)
@Scope("singleton")
public class SeqUtil {

	@Resource
	NacosConfigManager nacosConfigManager;

	@PostConstruct
	public void init() {
		SeqGetUtil.initeSequenceConfig(nacosConfigManager);
	}

	public static String getSeqNextValue(String seqName,String routeId){
		return SeqGetUtil.getSequence(seqName,routeId);
	}

	public static String getSeqNextValue(String seqName){
		return String.valueOf(SeqGetUtil.getNoSubSequence(Constant.SUB_SYSTEM, seqName));
	}

	public static final String EC_CACHE_METHOD_DEPENDENCE_SERIES = "ec_cache_method_dependence_series";

	public static final String EC_CACHE_INDEX_DEPENDENCE_SERIES = "ec_cache_index_dependence_series";

	public static final String EC_CACHE_TABLE_RECORD_SERIES = "ec_cache_table_record_series";

	public static final String EC_COMMON_CACHE_DEPENDENCE_SERIES = "ec_common_cache_dependence_series";

}
