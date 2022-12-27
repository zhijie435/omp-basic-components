package com.gb.soa.sequence.service.impl;

import com.gb.soa.omp.ccommon.util.RedisLock;
import com.gb.soa.sequence.dao.AutoSequenceDao;
import com.gb.soa.sequence.dao.PlatformOfflineSubUnitSequenceDao;
import com.gb.soa.sequence.dao.SequenceDAO;
import com.gb.soa.sequence.exception.SequenceException;
import com.gb.soa.sequence.model.AtuoSequenceModel;
import com.gb.soa.sequence.model.CreateSequence;
import com.gb.soa.sequence.model.PlatformOfflineSequence;
import com.gb.soa.sequence.model.PlatformOfflineSubUnitSequence;
import com.gb.soa.sequence.service.SequenceActionService;
import com.gb.soa.sequence.service.SequenceTimeService;
import com.gb.soa.sequence.util.SeqConstant;
import com.gb.soa.sequence.util.DateUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("sequenceActionService")
//@FeignClient(value = "@Value(\"${spring.application.name}\")")
//@RestController
//@GatewayMapping
public class SequenceActionServiceImpl implements SequenceActionService {

    @Resource
    private SequenceDAO sequenceDAO;

    @Resource
    private AutoSequenceDao autoSequenceDao;

    @Resource
    private PlatformOfflineSubUnitSequenceDao platformOfflineSubUnitSequenceDao;

    @Resource
    private SequenceTimeService sequenceTimeService;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    protected static Logger logger = LoggerFactory.getLogger(SequenceActionServiceImpl.class);

    /**
     * 创建序列号 ：前缀+时间+num+val 组成（98 171109 0091 719）
     *
     * @param systemName      项目名称
     * @param seqName         序列名称
     * @param businessId      前缀
     * @param maxVal(时间后面的数字)
     * @param maxNum          最大序列num（最后几位数）
     */
    @Override
    public void createSeq(String systemName, String seqName, String businessId, String maxVal, String maxNum) {
        // =======添加到数据库, 判断数据库中是否存在
        if (getCountBy(systemName, seqName)) {
            CreateSequence createSequence = new CreateSequence();
            long series = System.currentTimeMillis();
            createSequence.setSeries(series);
            createSequence.setSeqProject(systemName);
            createSequence.setSeqName(seqName);
            createSequence.setSeqPrefix(businessId);
            createSequence.setSeqVal(maxVal);
            createSequence.setSeqNum(maxNum);
            createSequence.setCreateTime(new Date());
            createSequence.setCurrentNum((long) 1);
            insertSeq(createSequence);
        }
    }

    /**
     * 添加序列信息到数据库
     */
    @Override
    public void insertSeq(CreateSequence createSequence) {
        try {
            sequenceDAO.insertSeq(createSequence);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SequenceException("数据库添加序列信息失败" + e.getMessage());
        }

    }

    @Override
    public List<CreateSequence> getSequence(CreateSequence createSequence) {
        try {
            return sequenceDAO.getSequence(createSequence.getSeqName());
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            throw new SequenceException("查询数据库序列信息失败" + e.getMessage());
        }
    }

    @Override
    public void updateSeqValnum(CreateSequence createSequence) {
        try {
            sequenceDAO.updateSeqValnum(createSequence);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SequenceException(e.getMessage());
        }
    }

    @Override
    public void updateSeqValnumAndSeqnum(CreateSequence createSequence) {
        try {
            sequenceDAO.updateSeqValnumAndSeqnum(createSequence);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SequenceException("数据库更新序列信息失败" + e.getMessage());
        }
    }

    /**
     * 获取序列并更新currentNum + 1
     *
     * @param createSequence
     * @return
     */
    @Override
    public CreateSequence getSequenceToClient(CreateSequence createSequence) {
        RedisLock lock = null;
        List<CreateSequence> createSequence2 = null;
        String time = "";
        Boolean weatherLock = true;
        try {
            if (getCountBy(createSequence.getSeqProject(), createSequence.getSeqName())) {
                throw new SequenceException(createSequence.getSeqName() + "序列在数据库中不存在");
            }

            long start = System.currentTimeMillis();
            // redis实例,锁名字,过期时间(单位秒),重试次数,等待时间(单位毫秒)
            String seqName = createSequence.getSeqName();

            String lockKey = seqName + "_common";
            boolean belongCalendar = belongCalendar("common");
            if (belongCalendar) {
                lockKey = SeqConstant.UPDATE_JOB_LOCK;
            }
            lock = new RedisLock(stringRedisTemplate, lockKey, 10, 30, 30);
            if (!lock.tryLock()) {
                weatherLock = false;
                logger.info("=============获取锁超时时间：" + (System.currentTimeMillis() - start));
                throw new Exception("Redis分布式锁超时,序列号名称为：" + seqName);
            } else {
                logger.info("获取到的reids的key是：" + lockKey);
                createSequence2 = getSequence(createSequence);
                // 从序列时间数据库中查询时间
                time = sequenceTimeService.getTime();
                if (createSequence2.size() > 0) {
                    for (CreateSequence createSequence3 : createSequence2) {

                        createSequence3.setSquenceTime(time);
                        if (null != createSequence3) {
                            Long currentNu = createSequence3.getCurrentNum() + 1;
                            String maxNum = createSequence3.getSeqNum();
                            Long seqNumEnd = createSequence3.getSeqNumEnd();
                            Long series = createSequence3.getSeries();
                            // 如果配置了结束值,最大值就是结束值
                            if (seqNumEnd != null && seqNumEnd.intValue() > 0) {
                                if (currentNu.intValue() > seqNumEnd.intValue()) {
                                    throw new SequenceException(
                                            "序列名[" + createSequence3.getSeqName() + "]的currentNum超出结束值范围!");
                                }
                            } else {
                                if (currentNu.intValue() > Integer.valueOf(maxNum)) {
                                    throw new SequenceException("当前num值超出最大num值范围");
                                }
                            }
                            createSequence.setCurrentNum(currentNu);
                            createSequence.setSeries(series);
                            updateSeqValnum(createSequence);

                            logger.info("序列名为：" + createSequence3.getSeqName() + " 当前currentnum为："
                                    + createSequence3.getCurrentNum());
                            return createSequence3;

                        }
                    }
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new SequenceException(ex.getMessage());
        } finally {
            if (lock != null && weatherLock) {
                lock.unlock();
                logger.info("解锁时间：" + System.currentTimeMillis());
            }
        }
        return null;
    }

    /**
     * 根据项目名和序列名称查看是否存在
     *
     * @param systemName
     * @param seqName
     * @return
     */
    @Override
    public Boolean getCountBy(String systemName, String seqName) {
        boolean trm = true;
        Integer num = 0;
        try {
            num = sequenceDAO.getCountBy(systemName, seqName.trim());
            if (num > 0) {
                trm = false;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SequenceException("查询数据库序列数量失败" + e.getMessage());
        }

        return trm;
    }

    /**
     * 查询序列model用于客户端配置文件验证
     *
     * @param createSequence
     * @return
     * @return List<CreateSequence>
     * @author changhong.deng
     * @date 2018年1月23日上午9:59:29
     */
    @Override
    public List<CreateSequence> getSequenceModelToClientCheck(CreateSequence createSequence) {
        List<CreateSequence> sequence = getSequence(createSequence);
        if (sequence.size() > 0) {
            String time = sequenceTimeService.getTime();
            for (CreateSequence sequence2 : sequence) {
                sequence2.setSquenceTime(time);
            }
        }
        return sequence;
    }

    /**
     * 自增长序列获取方式
     *
     * @author changhong.deng
     * @date 2018年6月8日
     */
    @Override
    public String getAutomicSeq(String seqName, Integer num, Long tenantNumId, Long dataSign) {
        long start = System.currentTimeMillis();
        RedisLock lock = null;
        Boolean weatherLock = true;
        Long currentSeqVal = null;
        List<AtuoSequenceModel> autoSequenceInfo = null;
        AtuoSequenceModel atuoSequenceModel = null;
        StringBuffer stringBuffer = new StringBuffer();
        String lockKey = seqName + "_auto";
        boolean belongCalendar = belongCalendar("auto");
        if (belongCalendar) {
            lockKey = SeqConstant.SEQUENCE_AUTO_CLEAR;
        }
        try {
            // redis实例,锁名字,过期时间(单位秒),重试次数,等待时间(单位毫秒)
            lock = new RedisLock(stringRedisTemplate, lockKey, 10, 40, 10);
            if (!lock.tryLock()) {
                weatherLock = false;
                logger.info("=============获取锁超时时间：" + (System.currentTimeMillis() - start));
                throw new Exception("Redis分布式锁超时,序列号名称为：" + seqName);
            } else {
                logger.info("获取到的reids的key是：" + lockKey);
                String redisKey = seqName + tenantNumId + dataSign + "_auto_single";

                if (tenantNumId == 0L && dataSign == 0L) {
                    redisKey = seqName.trim() + "_auto_com";//序列共用，不分租户和测试标识，在数据库中配置都配置成0
                }

                String redisCurrentVal = "";
                // 数据库配置信息
                String autoSeqKey = redisKey + "_info_key";
                String redisAutoInfo = "";
                try {
                    if (num == -2) {// 清空redis,读取数据库
                        stringRedisTemplate.delete(redisKey);
                        stringRedisTemplate.delete(autoSeqKey);
                    }
                    redisCurrentVal = stringRedisTemplate.opsForValue().get(redisKey);
                    redisAutoInfo = stringRedisTemplate.opsForValue().get(autoSeqKey);
                } catch (Exception e) {
                    stringRedisTemplate.opsForValue().set(redisKey, "");
                    stringRedisTemplate.opsForValue().set(autoSeqKey, "");
                    logger.error(e.getMessage(), e);
                    throw new SequenceException("redis服务器异常！" + e.getMessage());
                }

                if (redisAutoInfo == null || redisAutoInfo.trim().equals("")) {
                    autoSequenceInfo = autoSequenceDao.getAutoSequenceInfo(seqName, tenantNumId, dataSign);
                    if (autoSequenceInfo.isEmpty() || autoSequenceInfo.size() <= 0) {
                        throw new SequenceException("在platform_auto_sequence配置表中未查询到匹配的信息,序列号名称为：" + seqName);
                    }
                    atuoSequenceModel = autoSequenceInfo.get(0);
                    String atuoSequenceValue = JSONObject.fromObject(atuoSequenceModel).toString();
                    stringRedisTemplate.opsForValue().set(autoSeqKey, atuoSequenceValue);
                } else {
                    JSONObject jsonobject = JSONObject.fromObject(redisAutoInfo);
                    atuoSequenceModel = (AtuoSequenceModel) JSONObject.toBean(jsonobject, AtuoSequenceModel.class);

                }
                Long initValue = atuoSequenceModel.getInitValue();
                Long currentNum = atuoSequenceModel.getCurrentNum();
                Long series = atuoSequenceModel.getSeries();
                num = atuoSequenceModel.getCacheNum();
                if (redisCurrentVal == null || redisCurrentVal.trim().equals("")) {
                    // 第一次进入
                    if (currentNum == 1L || (initValue != null && initValue.equals(currentNum))) {
                        currentSeqVal = currentNum;
                        autoSequenceDao.updateAutoCurrentVal(currentNum + num, series);
                        stringRedisTemplate.opsForValue().set(redisKey, String.valueOf(currentSeqVal));
                    } else {
                        currentSeqVal = currentNum + num;
                        stringRedisTemplate.opsForValue().set(redisKey, String.valueOf(currentSeqVal));
                        autoSequenceDao.updateAutoCurrentVal(currentSeqVal, series);
                    }
                } else {
                    Long cacheSeqVal = Long.valueOf(redisCurrentVal);
                    currentSeqVal = cacheSeqVal + 1;
                    // 当redis的值是num的倍数时，更新数据库
                    if (cacheSeqVal % num == 0) {
                        // 更新curentval值
                        autoSequenceDao.updateAutoCurrentVal(currentSeqVal, series);
                        logger.info(">>>>>>>>>更新数据库：" + (System.currentTimeMillis() - start));
                    }
                    // 更新到redis的值
                    stringRedisTemplate.opsForValue().set(redisKey, String.valueOf(currentSeqVal));

                }
                String seqPrefix = atuoSequenceModel.getSeqPrefix().trim();
                Integer isYear = atuoSequenceModel.getIsYear();
                Integer isMonth = atuoSequenceModel.getIsMonth();
                Integer isDay = atuoSequenceModel.getIsDay();
                // 是否需要流水号
                Integer isFlowCode = atuoSequenceModel.getIsFlowCode();
                stringBuffer.append(seqPrefix);
                Date now = new Date();
                if (isYear == 6) {
                    stringBuffer.append(DateUtils.format(now, "yy"));
                }

                if (isMonth == 6) {
                    stringBuffer.append(DateUtils.format(now, "MM"));
                }

                if (isDay == 6) {
                    stringBuffer.append(DateUtils.format(now, "dd"));
                }

                if (isFlowCode != 6) {// 如果是6不需要流水
                    Long flowCodeLength = atuoSequenceModel.getFlowCodeLength();
                    if (flowCodeLength > 0) {
                        int currentLength = currentSeqVal.toString().length();
                        // 当前val的长度小于指定的长度时,自动补零
                        if (currentLength <= flowCodeLength.intValue()) {
                            String resultStringVal = this.frontCompWithZore(currentSeqVal,
                                    flowCodeLength.intValue());
                            stringBuffer.append(resultStringVal);
                        } else if (currentLength > flowCodeLength.intValue()) {
                            throw new SequenceException("流水号的长度超过指定的长度,自增长序列为：" + seqName);
                        }
                    } else {
                        stringBuffer.append(currentSeqVal);
                    }
                }
            }

        } catch (Exception ex) {
            logger.info("抛异常时间：" + System.currentTimeMillis());
            logger.error(ex.getMessage(), ex);
            throw new SequenceException("获取自增长序列失败！" + ex.getMessage());
        } finally {
            if (lock != null && weatherLock) {
                lock.unlock();
                logger.info("解锁时间：" + System.currentTimeMillis());
            }
        }
        long endtime = System.currentTimeMillis() - start;
        if (endtime > 100) {
            logger.info("****************************" + (System.currentTimeMillis() - start));
        }
        logger.info("=======获取自增序列结束时间：" + (System.currentTimeMillis() - start));
        return stringBuffer.toString();
    }

    /***
     * 获取序列存储的方式
     *
     * @author changhong.deng
     * @date 2018年7月14日
     */
    @Override
    public Integer getSeqStoreStatus(String SeqName) {
        Integer status = 0;
        try {
            status = sequenceDAO.getSeqStoreStatus(SeqName.trim());
            if (status == null) {
                status = 0;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SequenceException("查询数据库序列存储方式失败" + e.getMessage());
        }
        return status;
    }

    /*
     * 同步离线序列
     */
    @Override
    public List<Map<String, Object>> getOfflineSeqInfo(Long subUnitNumId) {

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        List<PlatformOfflineSequence> offlineSeqList = null;
        List<PlatformOfflineSubUnitSequence> offlineSubUnit = null;
        try {
            if (subUnitNumId == null) {
                throw new SequenceException("门店编号不能为空");
            }

            List<CreateSequence> sequenceList = null;
            // 查询该店铺的离线日志，如果有不插入,直接返回
            offlineSubUnit = platformOfflineSubUnitSequenceDao.getOfflineSubUnitSequence(subUnitNumId);
            if (offlineSubUnit.size() > 0) {
                for (PlatformOfflineSubUnitSequence esistSeqModel : offlineSubUnit) {
                    String offSeqName = esistSeqModel.getSeqName();
                    // 查询线上序列信息
                    sequenceList = sequenceDAO.getSequence(offSeqName);
                    if (sequenceList.size() <= 0) {
                        throw new SequenceException("platform_sequence表未查询到配置的序列,序列名称为：" + offSeqName);
                    }
                    CreateSequence seqModel = sequenceList.get(0);// 正在用的序列(包含离线)
                    // 离线开始值
                    Long offStartNum = esistSeqModel.getStartNum();
                    // 离线结束值
                    Long offEndNum = esistSeqModel.getEndNum();

                    // 添加返回信息
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("seq_name", offSeqName);
                    map.put("SEQ_PROJECT", seqModel.getSeqProject());
                    map.put("SEQ_PREFIX", seqModel.getSeqPrefix());
                    map.put("SEQ_NUM", seqModel.getSeqNum());
                    map.put("SEQ_VAL", seqModel.getSeqVal());
                    map.put("CURRENT_NUM", offStartNum);
                    map.put("SEQ_NUM_START", offStartNum);
                    map.put("SEQ_NUM_END", offEndNum);
                    map.put("disrupt", seqModel.getDisrupt());
                    map.put("is_store_local", 1);
                    resultList.add(map);

                }
                return resultList;

            }

            // 查询离线序列表
            offlineSeqList = platformOfflineSubUnitSequenceDao.getOfflineSubSequence();
            if (offlineSeqList.size() <= 0) {
                return resultList;
            }
            for (PlatformOfflineSequence offSeqModel : offlineSeqList) {
                String offSeqName = offSeqModel.getSeqName();
                // 查询线上序列信息
                sequenceList = sequenceDAO.getSequence(offSeqName);
                if (sequenceList.size() <= 0) {
                    throw new SequenceException("platform_sequence表未查询到配置的序列,序列名称为：" + offSeqModel.getSeqName());
                }
                CreateSequence seqModel = sequenceList.get(0);// 正在用的序列(包含离线)
                // 离线获取当前序号的个数
                Long offlineGetNumCount = offSeqModel.getOfflineGetNumCount();
                // 离线序号已经分配到的值
                Long offlineCurrentNum = offSeqModel.getOfflineCurrentNum();
                // 离线开始值
                Long offStartNum = offlineCurrentNum;
                // 离线结束值
                Long offEndNum = offlineCurrentNum + offlineGetNumCount;
                // 离线序号结束值
                Long offlineEndNum = offSeqModel.getOfflineEndNum();
                if (offEndNum.intValue() >= offlineEndNum.intValue()) {
                    throw new SequenceException("离线序列的结束值不能超过限定的结束值,序列名称为：" + offSeqModel.getSeqName());
                }
                // 更新离线表已经分配到的值
                platformOfflineSubUnitSequenceDao.updateOfflineCurrentNum(offSeqName);
                // 离线表插入一条日志
                PlatformOfflineSubUnitSequence subUnitSequence = new PlatformOfflineSubUnitSequence();
                subUnitSequence.setSeries(System.currentTimeMillis());
                subUnitSequence.setSeqName(offSeqName);
                subUnitSequence.setSubUnitNumId(subUnitNumId);
                subUnitSequence.setStartNum(offStartNum);
                subUnitSequence.setEndNum(offEndNum);
                platformOfflineSubUnitSequenceDao.insertOfflineSeq(subUnitSequence);

                // 添加返回信息
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("seq_name", offSeqName);
                map.put("SEQ_PROJECT", seqModel.getSeqProject());
                map.put("SEQ_PREFIX", seqModel.getSeqPrefix());
                map.put("SEQ_NUM", seqModel.getSeqNum());
                map.put("SEQ_VAL", seqModel.getSeqVal());
                map.put("CURRENT_NUM", offStartNum);
                map.put("SEQ_NUM_START", offStartNum);
                map.put("SEQ_NUM_END", offEndNum);
                map.put("disrupt", seqModel.getDisrupt());
                map.put("is_store_local", 1);
                resultList.add(map);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resultList;

    }

    private String frontCompWithZore(Long sourceNum, int formatLength) {
        return String.format("%0" + formatLength + "d", sourceNum);
    }

    private boolean belongCalendar(String fromWhere) {
        Integer intDateBegin = 0;
        Integer intDateEnd = 0;
        logger.info("序列号来自哪里fromWhere============" + fromWhere.trim());
        if (fromWhere.trim().equals("common")) {
            intDateBegin = 2354;
            intDateEnd = 2356;
        } else if (fromWhere.trim().equals("auto")) {
            intDateBegin = 2400;
            intDateEnd = 2402;
        }
        Boolean flag = false;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = sdf.format(new Date());
            // 截取当前时间时分
            int strDateH = Integer.parseInt(strDate.substring(11, 13));
            String strDateM = strDate.substring(14, 16);
            if (strDateH < 12) {
                strDateH = strDateH + 24;
            }
            String strNow = String.valueOf(strDateH) + strDateM;
            int intNow = Integer.parseInt(strNow);
            if (intDateBegin <= intNow && intNow <= intDateEnd) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

}
