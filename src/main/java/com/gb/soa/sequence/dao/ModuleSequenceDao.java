package com.gb.soa.sequence.dao;

import com.gb.soa.sequence.model.GoodsReserveBuildModel;

import java.util.List;

/**
 * 业务单据序列
 * @author changhong.deng
 * @date 2018年3月8日下午1:11:51
 */
public interface ModuleSequenceDao {

	public List<GoodsReserveBuildModel> getModuleInfo(Long moduleId, Long dataSign, Long tenantNumId);
}
