package com.gb.soa.sequence.dao.impl;

import com.gb.soa.omp.ccommon.util.MyJdbcTemplate;
import com.gb.soa.sequence.dao.PlatformOfflineSubUnitSequenceDao;
import com.gb.soa.sequence.model.PlatformOfflineSequence;
import com.gb.soa.sequence.model.PlatformOfflineSubUnitSequence;
import com.gb.soa.sequence.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class PlatformOfflineSubUnitSequenceDaoImpl implements PlatformOfflineSubUnitSequenceDao {
	@Resource(name = "platformJdbcTemplate")
	public MyJdbcTemplate jdbcTemplate;

	@Value("${db.annotate.prefix}")
	private String mycatGoMaster;

	@Override
	public void insertOfflineSeq(PlatformOfflineSubUnitSequence entity) throws Exception {
		String sql = "insert into platform_offline_sub_unit_sequence(SERIES,SEQ_NAME,start_num,end_num,sub_unit_num_id)";
		Object args[] = new Object[] { entity.getSeries(), entity.getSeqName(), entity.getStartNum(),
				entity.getEndNum(), entity.getSubUnitNumId() };
		sql = sql + StringUtil.sqlField(args.length);
		jdbcTemplate.update(mycatGoMaster + sql, args);
	}

	@Override
	public List<PlatformOfflineSubUnitSequence> getOfflineSubUnitSequence(Long subUnitNumId) {
		String sql = "select * from platform_offline_sub_unit_sequence where sub_unit_num_id = ? LIMIT 10000";
		return jdbcTemplate.query(mycatGoMaster + sql, new Object[] { subUnitNumId },
				new BeanPropertyRowMapper(PlatformOfflineSubUnitSequence.class));

	}

	@Override
	public List<PlatformOfflineSequence> getOfflineSubSequence() throws Exception {
		String sql = "select * from platform_offline_sequence LIMIT 10000";
		return jdbcTemplate.query(mycatGoMaster + sql, new BeanPropertyRowMapper(PlatformOfflineSequence.class));

	}

	@Override
	public void updateOfflineCurrentNum(String seqName) throws Exception {
		String sql = "update platform_offline_sequence set offline_current_num = offline_current_num + offline_get_num_count + 1  where seq_name = ?";
		jdbcTemplate.update(mycatGoMaster + sql, new Object[] { seqName });
	}

	@Override
	public List<String> getOfflineSubUnitSeqName(Long subUnitNumId) throws Exception {
		String sql = "select distinct seq_name from platform_offline_sub_unit_sequence where sub_unit_num_id = ? LIMIT 10000";
		return jdbcTemplate.queryForList(mycatGoMaster + sql, new Object[] { subUnitNumId }, String.class);
	}

	@Override
	public PlatformOfflineSequence getOfflineSeqModel(String seqName) throws Exception {
		String sql = "select * from platform_offline_sequence where seq_name = ? limit 1";
		return (PlatformOfflineSequence) jdbcTemplate.queryForObject(mycatGoMaster + sql, new Object[] { seqName },
				new BeanPropertyRowMapper(PlatformOfflineSequence.class));

	}

}
