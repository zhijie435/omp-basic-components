package com.gb.soa.sequence.dao;

import java.util.Date;

/**
 * 序列存储dao
 * 
 * @author dch
 *
 */
public interface SequenceTimeDAO {

	public void insertTime(String time,Date updateTime) throws Exception;

	public String getSequence() throws Exception;

	public void updateTime(String time,Date updateTime) throws Exception;

	public Integer getCount() throws Exception;
}