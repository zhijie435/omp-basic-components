package com.gb.soa.sequence.service;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 序列存储dao
 *
 * @author dch
 *
 */
@Service
public interface SequenceTimeService {

	public void insertTime(String time,Date updateTime);

	public String getTime();

	public void updateTime(String time,Date updateTime);

	public Boolean getCount();

	public void editTime();


}
