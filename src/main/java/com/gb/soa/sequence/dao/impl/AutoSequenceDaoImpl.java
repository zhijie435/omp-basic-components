package com.gb.soa.sequence.dao.impl;

import com.gb.soa.omp.ccommon.util.MyJdbcTemplate;
import com.gb.soa.sequence.dao.AutoSequenceDao;
import com.gb.soa.sequence.model.AtuoSeqClearModel;
import com.gb.soa.sequence.model.AtuoSequenceModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class AutoSequenceDaoImpl implements AutoSequenceDao {

	@Resource(name = "platformJdbcTemplate")
	public MyJdbcTemplate jdbcTemplate;

	@Value("${db.annotate.prefix}")
	private String mycatGoMaster;

	/**
	 * 查询自增长序列配置表
	 *
	 * @author changhong.deng
	 * @date 2018年6月8日
	 */
	@Override
	public List<AtuoSequenceModel> getAutoSequenceInfo(String seqName, Long tenantNumId, Long dataSign) {
		String sql = "select * from platform_auto_sequence  where seq_name = ? and TENANT_NUM_ID = ? and DATA_SIGN = ? order by create_time desc LIMIT 10000";
		Object[] params = new Object[] { seqName, tenantNumId, dataSign };
		return jdbcTemplate.query(mycatGoMaster + sql, params,
				new BeanPropertyRowMapper<AtuoSequenceModel>(AtuoSequenceModel.class));
	}

	/**
	 * 更新自增序列的值
	 *
	 * @author changhong.deng
	 * @date 2018年6月8日
	 */
	@Override
	public void updateAutoCurrentVal(Long currentVal, Long series) {
		String sql = "UPDATE platform_auto_sequence SET current_num=? where series = ?";
		Object[] params = new Object[] { currentVal, series};
		int update = jdbcTemplate.update(mycatGoMaster + sql, params);
		if (update < 1) {// 如果沒有更新成功
			throw new RuntimeException("更新自增序列失败!");
		}
	}

	@Override
	public void updateAutoCurrentNum() {
		String sql = "UPDATE platform_auto_sequence SET current_num=init_value where is_clear = 6";
		jdbcTemplate.update(mycatGoMaster + sql);

	}

	@Override
	public List<AtuoSeqClearModel> getClearAutoSeq() {
		String sql = "select SERIES,TENANT_NUM_ID,DATA_SIGN,SEQ_NAME from platform_auto_sequence  where is_clear = 6 LIMIT 10000";
		return jdbcTemplate.query(mycatGoMaster + sql,
				new BeanPropertyRowMapper<AtuoSeqClearModel>(AtuoSeqClearModel.class));
	}

}
