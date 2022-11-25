package com.gb.soa.sequence.dao.impl;

import com.gb.soa.omp.ccommon.util.MyJdbcTemplate;
import com.gb.soa.sequence.dao.SequenceDAO;
import com.gb.soa.sequence.model.CreateSequence;
import com.gb.soa.sequence.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class SequenceDAOImpl implements SequenceDAO {
	@Resource(name = "platformJdbcTemplate")
	public MyJdbcTemplate jdbcTemplate;

	@Value("${db.annotate.prefix}")
	private String mycatGoMaster;

	protected static Logger logger = LoggerFactory.getLogger(SequenceDAOImpl.class);

	/**
	 * 新增序列信息
	 */
	public void insertSeq(CreateSequence createSequence) throws Exception {
		String sql = "insert into PLATFORM_SEQUENCE(SERIES,SEQ_NAME,SEQ_PROJECT,SEQ_PREFIX,SEQ_NUM,SEQ_VAL,CURRENT_NUM,CREATE_TIME,SEQ_NUM_START,SEQ_NUM_END)";
		Object args[] = new Object[] { createSequence.getSeries(), createSequence.getSeqName(),
				createSequence.getSeqProject(), createSequence.getSeqPrefix(), createSequence.getSeqNum(),
				createSequence.getSeqVal(), createSequence.getCurrentNum(), createSequence.getCreateTime(),
				createSequence.getSeqNumStart(), createSequence.getSeqNumEnd() };
		sql = sql + StringUtil.sqlField(args.length);
		jdbcTemplate.update(sql, args);
	}

	/**
	 * 更新cunrrentNum
	 */
	@Override
	public void updateSeqValnum(CreateSequence createSequence) {
		String sql = mycatGoMaster
				+ "update PLATFORM_SEQUENCE set CURRENT_NUM = ? where SERIES = ? and CURRENT_NUM = ?";
		int x = jdbcTemplate.update(sql, new Object[] { createSequence.getCurrentNum(), createSequence.getSeries(),
				createSequence.getCurrentNum() - 1 });
		logger.info("主从同库：" + mycatGoMaster);
		logger.info("sql语句：====" + sql);
		if (x < 1) {// 如果沒有更新成功
			throw new RuntimeException("CURRENT_NU值更新失敗!");
		}
	}

	/**
	 * 更新cunrrentNum和seqNum
	 */
	@Override
	public void updateSeqValnumAndSeqnum(CreateSequence createSequence) {
		String sql = mycatGoMaster + "update PLATFORM_SEQUENCE set CURRENT_NUM = ?,SEQ_NUM = ? where SEQ_NAME = ?";
		jdbcTemplate.update(sql, new Object[] { createSequence.getCurrentNum(), createSequence.getSeqNum(),
				createSequence.getSeqName() });
	}

	/**
	 * 获取序列信息
	 */

	@Override
	public List<CreateSequence> getSequence(String seqName) {
		String sql = mycatGoMaster
				+ "select series,SEQ_NAME seqName,SEQ_PROJECT seqProject,SEQ_PREFIX seqPrefix,SEQ_NUM seqNum,SEQ_VAL seqVal,CURRENT_NUM currentNum,SEQ_NUM_START seqNumStart,SEQ_NUM_END seqNumEnd,disrupt,is_store_local isStoreLocal from PLATFORM_SEQUENCE where SEQ_NAME = ?" ;
		return jdbcTemplate.query(sql,new Object[]{seqName},new BeanPropertyRowMapper(CreateSequence.class));

	}

	/**
	 * 根据项目名和序列名称查看是否存在
	 *
	 * @param systemName
	 * @param seqName
	 * @return
	 */
	@Override
	public Integer getCountBy(String systemName, String seqName) throws Exception {
		String sql = mycatGoMaster + "select count(*) from PLATFORM_SEQUENCE where SEQ_NAME = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { seqName }, Integer.class);

	}

	@Override
	public void updateCurrentNum(Long currentNum, Long series) {
		String sql = mycatGoMaster + "UPDATE PLATFORM_SEQUENCE SET CURRENT_NUM=? where SERIES=?";
		jdbcTemplate.update(sql, new Object[] { currentNum, series });
	}

	/**
	 * 获取配置了current_num的序列
	 *
	 * @return
	 * @author changhong.deng
	 * @date 2018年1月10日下午12:29:14
	 * @return List<CreateSequence>
	 */
	@Override
	public List<CreateSequence> getSequenceWithCurrentNum() {
		String sql = mycatGoMaster
				+ "select SERIES,SEQ_NAME seqName,SEQ_NUM_START seqNumStart from PLATFORM_SEQUENCE WHERE SEQ_NUM_START IS NOT NULL LIMIT 10000";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(CreateSequence.class));

	}

	@Override
	public void updateAllCurrentNum() {
		String sql = mycatGoMaster + "UPDATE PLATFORM_SEQUENCE SET CURRENT_NUM=1 WHERE SEQ_NUM_START is null";
		jdbcTemplate.update(sql);

	}

	/**
	 * 更新currentVal值
	 *
	 * @param currentVal
	 * @param seqName
	 * @author changhong.deng
	 * @date 2018年3月12日上午11:14:48
	 * @return void
	 */
	@Override
	public void updateCurrentVal(Long currentVal, String seqName) {
		String sql = mycatGoMaster + "UPDATE PLATFORM_SEQUENCE SET SEQ_NUM_END=? where SEQ_NAME=?";
		int update = jdbcTemplate.update(sql, new Object[] { currentVal, seqName });
		if (update < 1) {// 如果沒有更新成功
			throw new RuntimeException("更新失敗!");
		}
	}

	/**
	 * 获取存储的状态
	 *
	 * @author changhong.deng
	 * @date 2018年7月14日
	 */
	@Override
	public Integer getSeqStoreStatus(String seqName) throws Exception {
		String sql = mycatGoMaster + "select is_store_local from PLATFORM_SEQUENCE where SEQ_NAME = ? limit 1";
		return jdbcTemplate.queryForObject(sql, new Object[] { seqName }, Integer.class);

	}

	/**
	 * 更新离线序列的开始值和结束值
	 *
	 * @author changhong.deng
	 * @date 2018年7月23日
	 */
	@Override
	public Integer updateSeqStartAndEndNum(String seqName, Long seqNumStart, Long seqNumEnd) throws Exception {
		String sql = mycatGoMaster
				+ "update PLATFORM_SEQUENCE set SEQ_NUM_START = ?,SEQ_NUM_END = ? where SEQ_NAME = ?";
		int x = jdbcTemplate.update(sql, new Object[] { seqNumStart, seqNumEnd, seqName });
		logger.info("主从同库：" + mycatGoMaster);
		logger.info("sql语句====：" + sql);
		return x;
	}
}
