package com.gb.soa.sequence.dao.impl;

import com.gb.soa.omp.ccommon.util.MyJdbcTemplate;
import com.gb.soa.sequence.dao.ModuleSequenceDao;
import com.gb.soa.sequence.model.GoodsReserveBuildModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ModuleSequenceDaoImpl implements ModuleSequenceDao {
	@Resource(name = "platformJdbcTemplate")
	public MyJdbcTemplate jdbcTemplate;

	@Value("${db.annotate.prefix}")
	private String mycatGoMaster;

	@Override
	public List<GoodsReserveBuildModel> getModuleInfo(Long moduleId, Long dataSign, Long tenantNumId) {
		String sql = "select mrb.flow_length,mrb.is_day,mrb.is_flow_code,mrb.is_month,mrb.is_organization,mrb.is_year,mrb.module_sim_name,mrb.module_id,mrb.tenant_num_id from platform_module_build mrb where mrb.tenant_num_id= ? and mrb.data_sign= ? and mrb.module_id= ? and mrb.cancelsign = 'N' LIMIT 10000";
		return jdbcTemplate.query(mycatGoMaster + sql, new Object[] { tenantNumId, dataSign, moduleId },
				new BeanPropertyRowMapper<GoodsReserveBuildModel>(GoodsReserveBuildModel.class));

	}

}
