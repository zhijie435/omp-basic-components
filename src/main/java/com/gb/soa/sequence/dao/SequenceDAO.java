package com.gb.soa.sequence.dao;

import com.gb.soa.sequence.model.CreateSequence;

import java.util.List;


public interface SequenceDAO {

	public void insertSeq(CreateSequence createSequence) throws Exception;

	public List<CreateSequence> getSequence(String seqName);

	public void updateSeqValnum(CreateSequence createSequence) throws Exception;

	public void updateSeqValnumAndSeqnum(CreateSequence createSequence);

	public Integer getCountBy(String systemName, String seqName) throws Exception;

    public void updateCurrentNum(Long currentNum,Long series);

    public void updateAllCurrentNum();

    public List<CreateSequence> getSequenceWithCurrentNum();

    public void updateCurrentVal(Long currentVal,String seqName);

    public Integer getSeqStoreStatus(String seqName) throws Exception;

    //更新离线序列的开始值和结束值
    public Integer updateSeqStartAndEndNum(String seqName,Long seqNumStart,Long seqNumEnd) throws Exception;
}
