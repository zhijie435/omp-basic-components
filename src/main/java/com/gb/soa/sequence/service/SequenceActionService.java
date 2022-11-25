package com.gb.soa.sequence.service;

import com.gb.soa.sequence.model.CreateSequence;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient("basic-components")
public interface SequenceActionService {
	/**
	 * 创建序列号 ：前缀+时间+num+val 组成（98 171109 0091 719）
	 *
	 * @param systemName
	 *            项目名称
	 * @param seqName
	 *            序列名称
	 * @param businessId
	 *            前缀
	 * @param maxVal
	 *            最大序列val(时间后面的数字)
	 * @param maxNum
	 *            最大序列num（最后几位数）
	 */

	public void createSeq(String systemName, String seqName, String businessId, String maxVal,
			String maxNum);

	@PostMapping("/com.gb.soa.sequence.service.insertSeq")
	public void insertSeq(@RequestBody CreateSequence createSequence);

	@PostMapping("/com.gb.soa.sequence.service.getSequence")
	public List<CreateSequence> getSequence(@RequestBody CreateSequence createSequence);

	public void updateSeqValnum(CreateSequence createSequence);

	public CreateSequence getSequenceToClient(CreateSequence createSequence);

	public void updateSeqValnumAndSeqnum(CreateSequence createSequence);

	public Boolean getCountBy(String systemName, String seqName);

	public List<CreateSequence> getSequenceModelToClientCheck(CreateSequence createSequence);

	@GetMapping("/com.gb.soa.sequence.service.getAutomicSeq")
	public String getAutomicSeq(@RequestParam String SeqName, @RequestParam Integer num, @RequestParam Long tenantNumId, @RequestParam Long dataSign);

	//获取序列存储的状态
	public Integer getSeqStoreStatus(String SeqName);

	/**
	 * 返回离线序列信息
	* @author changhong.deng
	* @date 2018年7月23日
	 */
	public List<Map<String, Object>> getOfflineSeqInfo(Long subUnitNumId);

}
