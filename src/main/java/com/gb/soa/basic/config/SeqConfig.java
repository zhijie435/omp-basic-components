package com.gb.soa.basic.config;

import javax.annotation.Resource;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.gb.soa.omp.export.util.Constant;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gb.soa.omp.ccommon.util.StringUtil;
import com.gb.soa.sequence.util.SeqGetUtil;

@Component()
//@Lazy(false)
@Scope("singleton")
public class SeqConfig implements ApplicationListener<ApplicationReadyEvent> {
    @Resource
    NacosConfigManager nacosConfigManager;

    public static volatile boolean recordSign = false;

//    @PostConstruct
//    public void init() {
//        System.out.println("序列号延迟加载");
//        if (StringUtil.isAllNotNullOrBlank(System.getProperty("record.log"))) {
//            recordSign = true;
//        }
//        SeqGetUtil.initeSequenceConfig(nacosConfigManager);
//    }

    public static String getSeqNextValue(String SeqName, String routeId) {
        return SeqGetUtil.getSequence(Constant.SUB_SYSTEM, SeqName, routeId);
    }

    public static String getSeqNextValueByUsrNumId(String SeqName, Long usrNumId) {
        return SeqGetUtil.getSequence(Constant.SUB_SYSTEM, SeqName, String.valueOf(usrNumId));
    }

    public static String getSeqNextValueByTenantNumId(String SeqName, Long tenantNumId) {
        return SeqGetUtil.getSequence(Constant.SUB_SYSTEM, SeqName, String.valueOf(tenantNumId));
    }

    public static Long getSeq(String SeqName) {
        return SeqGetUtil.getNoSubSequence(Constant.SUB_SYSTEM, SeqName);
    }

    public static String getSeqNextValue(String seqName) {
        return String.valueOf(SeqGetUtil.getNoSubSequence(com.gb.soa.omp.ccache.util.Constant.SUB_SYSTEM, seqName));
    }

    public static final String EXCEL_INPUT_SERIES = "excel_input_series";

    public static final String EXCEL_INPUT_HDR_SERIES = "excel_input_hdr_series";

    public static final String EC_COMMON_CACHE_DEPENDENCE_SERIES = "ec_common_cache_dependence_series";


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("序列号延迟加载");
        if (StringUtil.isAllNotNullOrBlank(System.getProperty("record.log"))) {
            recordSign = true;
        }
        SeqGetUtil.initeSequenceConfig(nacosConfigManager);
    }
}
