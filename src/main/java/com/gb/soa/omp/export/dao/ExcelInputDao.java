/*
 * PROJECT NAME: omp_cexport
 * PACKAGE NAME: com.gb.soa.omp.export.dao
 * FILE    NAME: ExcelInputDao.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */
package com.gb.soa.omp.export.dao;

import com.gb.soa.omp.ccommon.api.exception.DatabaseOperateException;
import com.gb.soa.omp.ccommon.api.exception.ExceptionType;
import com.gb.soa.omp.ccommon.datasource.DataSourceContextHolder;
import com.gb.soa.omp.ccommon.util.StringUtil;
import com.gb.soa.omp.exchange.model.CommonQuery;
import com.gb.soa.omp.export.api.model.ExcelInput;
import com.gb.soa.omp.export.entity.EXCEL_INPUT;
import com.gb.soa.omp.export.util.ExportUtil;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 */
@Repository
public class ExcelInputDao {

	@Resource(name = "dataexchangeJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	public void batchInsertEntity(List<Object[]> params){
		String sql ="INSERT INTO excel_input (series, batch_no, sheet_id, excel_line, create_dtme, last_updtme, create_user_id, last_update_user_id, cancel_sign, tenant_num_id, data_sign, status, excel_a, excel_b, excel_c, excel_d, excel_e, excel_f, excel_g, excel_h, excel_i, excel_j, excel_k, excel_l, excel_m, excel_n, excel_o, excel_p, excel_q, excel_r, excel_s, excel_t, excel_u, excel_v, excel_w, excel_x, excel_y, excel_z) VALUES (?, ?, ?, ?, now(), now(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.batchUpdate(sql, params);
	}

	public int[] batchInsertEntityNew(List<Object[]> params) {
		String sql ="INSERT INTO excel_input (series, batch_no, sheet_id, excel_line, create_dtme, last_updtme, create_user_id, last_update_user_id, cancel_sign, tenant_num_id, data_sign, status, excel_a, excel_b, excel_c, excel_d, excel_e, excel_f, excel_g, excel_h, excel_i, excel_j, excel_k, excel_l, excel_m, excel_n, excel_o, excel_p, excel_q, excel_r, excel_s, excel_t, excel_u, excel_v, excel_w, excel_x, excel_y, excel_z) VALUES (?, ?, ?, ?, now(), now(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setLong(1, Long.valueOf(params.get(i)[0].toString()));
				ps.setLong(2, Long.valueOf(params.get(i)[1].toString()));
				ps.setString(3, params.get(i)[2].toString());
				ps.setLong(4, Long.valueOf(params.get(i)[3].toString()));
				ps.setLong(5, Long.valueOf(params.get(i)[4].toString()));
				ps.setLong(6, Long.valueOf(params.get(i)[5].toString()));
				ps.setString(7, params.get(i)[6].toString());
				ps.setLong(8, Long.valueOf(params.get(i)[7].toString()));
				ps.setInt(9, Integer.valueOf(params.get(i)[8].toString()));
				ps.setInt(10, 0);
				ps.setString(11, params.get(i)[10].toString());
				ps.setString(12, params.get(i)[11] == null ? "" : params.get(i)[11].toString());
				ps.setString(13, params.get(i)[12] == null ? "" : params.get(i)[12].toString());
				ps.setString(14, params.get(i)[13] == null ? "" : params.get(i)[13].toString());
				ps.setString(15, params.get(i)[14] == null ? "" : params.get(i)[14].toString());
				ps.setString(16, params.get(i)[15] == null ? "" : params.get(i)[15].toString());
				ps.setString(17, params.get(i)[16] == null ? "" : params.get(i)[16].toString());
				ps.setString(18, params.get(i)[17] == null ? "" : params.get(i)[17].toString());
				ps.setString(19, params.get(i)[18] == null ? "" : params.get(i)[18].toString());
				ps.setString(20, params.get(i)[19] == null ? "" : params.get(i)[19].toString());
				ps.setString(21, params.get(i)[20] == null ? "" : params.get(i)[20].toString());
				ps.setString(22, params.get(i)[21] == null ? "" : params.get(i)[21].toString());
				ps.setString(23, params.get(i)[22] == null ? "" : params.get(i)[22].toString());
				ps.setString(24, params.get(i)[23] == null ? "" : params.get(i)[23].toString());
				ps.setString(25, params.get(i)[24] == null ? "" : params.get(i)[24].toString());
				ps.setString(26, params.get(i)[25] == null ? "" : params.get(i)[25].toString());
				ps.setString(27, params.get(i)[26] == null ? "" : params.get(i)[26].toString());
				ps.setString(28, params.get(i)[27] == null ? "" : params.get(i)[27].toString());
				ps.setString(29, params.get(i)[28] == null ? "" : params.get(i)[28].toString());
				ps.setString(30, params.get(i)[29] == null ? "" : params.get(i)[29].toString());
				ps.setString(31, params.get(i)[30] == null ? "" : params.get(i)[30].toString());
				ps.setString(32, params.get(i)[31] == null ? "" : params.get(i)[31].toString());
				ps.setString(33, params.get(i)[32] == null ? "" : params.get(i)[32].toString());
				ps.setString(34, params.get(i)[33] == null ? "" : params.get(i)[33].toString());
				ps.setString(35, params.get(i)[34] == null ? "" : params.get(i)[34].toString());
				ps.setString(36, params.get(i)[35] == null ? "" : params.get(i)[35].toString());
			}

			@Override
			public int getBatchSize() {
				return params.size();
			}
		});
	}

	/**
	 *
	 */
	public List<ExcelInput> getExelInputs(Long tenantNumId, Long dataSign, Long batchNo, String sheetId, Integer fromIndex, Integer pageSize) {
		String sql = "select series, batch_no, sheet_id, excel_line, create_dtme, last_updtme, create_user_id, last_update_user_id, cancel_sign, tenant_num_id, data_sign, status, excel_a, excel_b, excel_c, excel_d, excel_e, excel_f, excel_g, excel_h, excel_i, excel_j, excel_k, excel_l, excel_m, excel_n, excel_o, excel_p, excel_q, excel_r, excel_s, excel_t, excel_u, excel_v, excel_w, excel_x, excel_y, excel_z from excel_input where tenant_num_id = ? and data_sign = ? and batch_no = ? and status = 0 ";
		if (StringUtil.isAllNotNullOrBlank(sheetId)) {
			sql = sql +" and sheet_id = '"+sheetId+"'";
		}
		if (pageSize!=null&&pageSize!=0) {
			sql = sql +"order by series limit "+fromIndex+"," +pageSize;
		}else{
			sql = sql +" limit 5000 ";
		}
		return jdbcTemplate.query(sql, new Object[]{tenantNumId, dataSign, batchNo}, new BeanPropertyRowMapper<ExcelInput>(ExcelInput.class));
	}

	public int getCountBySheetId(Long tenantNumId, Long dataSign, Long batchNo, String sheetId) {
		String sql = "select count(1) from excel_input where tenant_num_id = ? and data_sign = ? and batch_no = ? and cancel_sign = 'N'";
		if (StringUtil.isAllNotNullOrBlank(sheetId)) {
			sql = sql +" and sheet_id = '"+sheetId+"'";
		}
		return jdbcTemplate.queryForObject(sql, new Object[]{tenantNumId, dataSign, batchNo}, Integer.class);
	}

	/**
	 */
	public void updateInputBySeries(String sql) {
		int row = jdbcTemplate.update(sql);
	}

	/**
	 */
	public Integer getDataCountByBatchNo(Long tenantNumId, Long dataSign, Long batchNo, Long status) {
		String sql = "select count(1) from excel_input where tenant_num_id = ? and data_sign = ? and batch_no = ? and status = ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{tenantNumId, dataSign, batchNo, status}, Integer.class);
	}

	public List<EXCEL_INPUT> getEntityListByBatchNo(Long tenantNumId, Long dataSign, Long batchNo, Long status){

		String sql = "select tenant_num_id,data_sign,series ,excel_line, excel_a, excel_b, excel_c, excel_d, excel_e, excel_f, excel_g, excel_h, excel_i, excel_j, excel_k, excel_l, excel_m, excel_n, excel_o, excel_p, excel_q, excel_r, excel_s, excel_t, excel_u, excel_v, excel_w, excel_x, excel_y, excel_z from excel_input where tenant_num_id = ? and data_sign = ? and batch_no = ? and status = ? limit 100000";//100000
		return jdbcTemplate.query(sql, new Object[]{tenantNumId, dataSign, batchNo, status}, new BeanPropertyRowMapper<EXCEL_INPUT>(EXCEL_INPUT.class));
	}

	/**
	 */
	public void batchUpdateStatusByBatchNo(Long tenantNumId, Long dataSign, Long batchNo, Integer status) {
		String sql ="update excel_input set status = ?, last_updtme = now() where tenant_num_id = ? and data_sign = ? and batch_no = ?";
		jdbcTemplate.update(sql, status, tenantNumId, dataSign, batchNo);
	}
}
