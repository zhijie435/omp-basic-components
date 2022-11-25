/*
 * PROJECT NAME: omp_cimport
 * PACKAGE NAME: com.gb.soa.omp.cimport.dao
 * FILE    NAME: SysDatasourceDao.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */
package com.gb.soa.omp.export.dao;

import com.gb.soa.omp.ccommon.api.exception.DatabaseOperateException;
import com.gb.soa.omp.ccommon.api.exception.ExceptionType;
import com.gb.soa.omp.ccommon.datasource.DataSourceContextHolder;
import com.gb.soa.omp.export.util.ExportUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO（描述类的职责）
 *
 * @author zhiyu.long
 * @version <b>1.0.0</b>
 * @date 2017年9月27日 下午3:13:43
 */
@Repository
public class ExArcDocSystemDao {
    @Resource(name = "commonQueryDyJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public String getDatasourceNameBySysNumId(Long tenantNumId, Long dataSign, Long sysNumId) {
        DataSourceContextHolder.clearDataSourceType();
        DataSourceContextHolder.setDataSourceType("platformDataSource");
        String sql = "select datasouce_name from ex_arc_doc_system where tenant_num_id=? and data_sign=? and system_num_id=?";
        List<String> datasourceNames = jdbcTemplate.queryForList(sql, new Object[]{tenantNumId, dataSign, sysNumId}, String.class);
        if (datasourceNames == null || datasourceNames.size() == 0) {
            throw new DatabaseOperateException(ExportUtil.SUB_SYSTEM, ExceptionType.DOE30052, "查询表ex_arc_doc_system失败!系统编号:" + sysNumId);
        }
        return datasourceNames.get(0);
    }

    /**
     * TODO（方法详细描述说明、方法参数的具体涵义）
     *
     * @param msgSeries
     * @param datasourceName
     * @return
     * @author zhiyu.long
     * @date 2017年9月27日 下午3:51:14
     */
    public String getMessageSeriesFromSysMsgRefound(String msgSeries, String datasourceName) {
        DataSourceContextHolder.clearDataSourceType();
        DataSourceContextHolder.setDataSourceType(datasourceName);
        String sql = "select series from sys_msg_trans_refind_id where  series=?";
        List<String> seriesList = jdbcTemplate.queryForList(sql, new Object[]{msgSeries}, String.class);
        if (seriesList == null || seriesList.size() == 0) {
            return null;
        }
        return seriesList.get(0);
    }
}
