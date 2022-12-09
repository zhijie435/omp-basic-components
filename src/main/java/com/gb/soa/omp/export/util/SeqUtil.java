package com.gb.soa.omp.export.util;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.alibaba.cloud.nacos.NacosConfigManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gb.soa.omp.ccommon.util.MyJdbcTemplate;
import com.gb.soa.omp.ccommon.util.StringUtil;
import com.gb.soa.sequence.util.SeqGetUtil;

@Component()
@Lazy(false)
@Scope("singleton")
public class SeqUtil {
    @Resource
    NacosConfigManager nacosConfigManager;

    public static volatile boolean recordSign = false;

    @PostConstruct
    public void init() {
        if (StringUtil.isAllNotNullOrBlank(System.getProperty("record.log"))) {
            recordSign = true;
        }
        SeqGetUtil.initeSequenceConfig(nacosConfigManager);
    }

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

    public static final String EXPORT_TENANT_TASK_BILL_SERIES = "export_tenant_task_bill_series";

    public static final String EXPORT_TENANT_TASK_BATCH_SERIES = "export_tenant_task_batch_series";

    public static final String EXPORT_TENANT_TASK_SERIES = "export_tenant_task_series";

    public static final String EXCHANGE_TASK_CONFIG_CONTENT_SERIES = "exchange_task_config_content_series";

    public static final String EXPORT_TENANT_NOTIFY_SERIES = "export_tenant_notify_series";

    public static final String TENANT_TASK_NUM_ID = "tenant_task_num_id";

    public static final String EXCEL_INPUT_SERIES = "excel_input_series";

    public static final String EXCEL_INPUT_HDR_SERIES = "excel_input_hdr_series";

    public static final String EXPORT_RECORD_LOG_SERIES = "export_record_log_series";

}
