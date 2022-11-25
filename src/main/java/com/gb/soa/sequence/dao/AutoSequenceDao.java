package com.gb.soa.sequence.dao;

import com.gb.soa.sequence.model.AtuoSeqClearModel;
import com.gb.soa.sequence.model.AtuoSequenceModel;

import java.util.List;

/**
 * 自增序列
 *
 * @author changhong.deng
 * @date 2018年6月8日
 */
public interface AutoSequenceDao {

	/**
	 * 查询自增配置表信息
	 *
	 * @author changhong.deng
	 * @date 2018年6月8日
	 */
	public List<AtuoSequenceModel> getAutoSequenceInfo(String seqName,Long tenantNumId,Long dataSign);

	/**
	 * 更新配置表current_num值
	 *
	 * @author changhong.deng
	 * @date 2018年6月8日
	 */
	public void updateAutoCurrentVal(Long currentVal, Long series);

	/**
	 * 更新currentnum初始化值
	 *
	 * @author changhong.deng
	 * @date 2018年7月14日
	 */
	public void updateAutoCurrentNum();

	/**
	 * 查询需要清零的自增序列
	 *
	 * @author changhong.deng
	 * @date 2018年7月18日
	 */
	public List<AtuoSeqClearModel> getClearAutoSeq();
}
