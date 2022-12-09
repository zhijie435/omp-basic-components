package com.gb.soa.sequence.service.impl;

import com.gb.soa.sequence.dao.SequenceTimeDAO;
import com.gb.soa.sequence.exception.SequenceException;
import com.gb.soa.sequence.service.SequenceTimeService;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service("sequenceTimeService")
public class SequenceTimeServiceImpl implements SequenceTimeService {

    @Autowired
    private SequenceTimeDAO sequenceTimeDAO;

    protected static Logger logger = LoggerFactory.getLogger(SequenceTimeServiceImpl.class);

    @Override
    public void insertTime(String time, Date updateTime) {

        try {
            sequenceTimeDAO.insertTime(time, updateTime);
        } catch (Exception e) {
            logger.error("数据库添加序列时间失败" + e.getMessage(), e);
            throw new SequenceException("数据库添加序列时间失败" + e.getMessage());
        }
    }

    @Override
    public String getTime() {
        try {
            return sequenceTimeDAO.getSequence();
        } catch (Exception e) {
            logger.error("查询数据库序列时间失败" + e.getMessage(), e);
            throw new SequenceException("查询数据库序列时间失败" + e.getMessage());
        }
    }

    @Override
    public void updateTime(String time, Date updateTime) {

        try {
            sequenceTimeDAO.updateTime(time, updateTime);
        } catch (Exception e) {
            logger.error("数据库更新序列时间失败" + e.getMessage(), e);
            throw new SequenceException("数据库更新序列时间失败" + e.getMessage());
        }
    }

    @Override
    public Boolean getCount() {
        boolean trm = true;
        Integer num = 0;
        try {
            num = sequenceTimeDAO.getCount();
            if (num > 0) {
                trm = false;
            }

        } catch (Exception e) {
            logger.error("查询数据库序列时间失败" + e.getMessage(), e);
            throw new SequenceException("查询数据库序列时间失败" + e.getMessage());
        }

        return trm;
    }

    /**
     * 查询数据是否存在时间，如何没有插入，如果有更新
     */
    @Override
    public void editTime() {
        String time = "";
        try {
            time = toDateStr(new Date());
            // 如果存在更新
            if (!getCount()) {

                updateTime(time, new Date());
            } else {
                insertTime(time, new Date());
            }
        } catch (Exception e) {
            logger.error("数据库序列时间更新失败" + e.getMessage(), e);
            throw new SequenceException("数据库序列时间更新失败" + e.getMessage());
        }

    }

    private String toDateStr(Date d) {
        String Simple_Date_Format = "yyyy-MM-dd";
        if (d == null) {
            return null;
        } else {
            return (new SimpleDateFormat(Simple_Date_Format)).format(d);
        }
    }
}
