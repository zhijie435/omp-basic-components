package com.gb.soa.sequence.dao.impl;

import com.gb.soa.omp.ccommon.util.MyJdbcTemplate;
import com.gb.soa.sequence.dao.SequenceTimeDAO;
import com.gb.soa.sequence.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;

@Repository
public class SequenceTimeDAOImpl implements SequenceTimeDAO {
	@Resource(name = "platformJdbcTemplate")
	public MyJdbcTemplate jdbcTemplate;

	@Value("${db.annotate.prefix}")
	private String mycatGoMaster;

	@Override
	public void insertTime(String time, Date updateTime) throws Exception {
		String sql = "insert into PLATFORM_SEQUENCE_TIME(SEQUENCE_TIME,LAST_UPDTME)";
		Object args[] = new Object[] { time, updateTime };
		sql = sql + StringUtil.sqlField(args.length);
		jdbcTemplate.update(mycatGoMaster + sql, args);

	}

	@Override
	public String getSequence() throws Exception {
		String sql = "select SEQUENCE_TIME from PLATFORM_SEQUENCE_TIME limit 1";
		return jdbcTemplate.queryForObject(mycatGoMaster + sql, String.class);
	}

	@Override
	public void updateTime(String time, Date updateTime) throws Exception {
		String sql = "update PLATFORM_SEQUENCE_TIME set SEQUENCE_TIME = ?,LAST_UPDTME = ?";
		jdbcTemplate.update(mycatGoMaster + sql, new Object[] { time, updateTime });
	}

	@Override
	public Integer getCount() throws Exception {
		String sql = "select count(1) from PLATFORM_SEQUENCE_TIME";
		return jdbcTemplate.queryForObject(mycatGoMaster + sql, Integer.class);
	}

}
