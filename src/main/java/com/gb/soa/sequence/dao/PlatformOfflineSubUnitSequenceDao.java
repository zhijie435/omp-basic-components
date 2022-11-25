package com.gb.soa.sequence.dao;

import com.gb.soa.sequence.model.PlatformOfflineSequence;
import com.gb.soa.sequence.model.PlatformOfflineSubUnitSequence;

import java.util.List;


public interface PlatformOfflineSubUnitSequenceDao {

	/**
	 * 插入离线日志
	* @author changhong.deng
	* @date 2018年7月23日
	 */
	public void insertOfflineSeq(PlatformOfflineSubUnitSequence entity) throws Exception;

	/**
	 * 根据门店查询离线日志
	* @author changhong.deng
	* @date 2018年7月23日
	 */
	public List<PlatformOfflineSubUnitSequence> getOfflineSubUnitSequence(Long subUnitNumId) throws Exception;

	/**
	 * 查询门店下面的序列名称
	* @author changhong.deng
	* @date 2018年7月23日
	 */
	public List<String> getOfflineSubUnitSeqName(Long subUnitNumId) throws Exception;

	/**
	 * 查询离线序列信息
	* @author changhong.deng
	* @date 2018年7月23日
	 */
	public List<PlatformOfflineSequence> getOfflineSubSequence() throws Exception;


	/**
	 * 更新离线序号已经分配到的值
	* @author changhong.deng
	* @date 2018年7月23日
	 */
	public void updateOfflineCurrentNum(String seqName)throws Exception;

	/**
	 * 根据离线序列查询
	* @author changhong.deng
	* @date 2018年7月23日
	 */
	public PlatformOfflineSequence getOfflineSeqModel(String seqName) throws Exception;




}
