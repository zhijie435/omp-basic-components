package com.gb.soa.sequence.task;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.gb.soa.omp.ccommon.util.RedisLock;
import com.gb.soa.sequence.dao.SequenceDAO;
import com.gb.soa.sequence.model.CreateSequence;
import com.gb.soa.sequence.service.SequenceTimeService;
import com.gb.soa.sequence.util.SeqConstant;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 跨天更新序列时间节点
 *
 * @author dch
 *
 */
@Component
public class UpdateSequenceTime {

	@Autowired
	NacosConfigManager nacosConfigManager;

	@Autowired
	private SequenceTimeService sequenceTimeService;

	@Autowired
	private SequenceDAO sequenceDAO;

	@Resource(name = "stringRedisTemplate")
	private StringRedisTemplate stringRedisTemplate;

	protected static Logger logger = LoggerFactory.getLogger(UpdateSequenceTime.class);

	public void updateSequenceTime() {
		RedisLock lock = null;
		String lockKey = SeqConstant.UPDATE_JOB_LOCK;
		try {
			lock = new RedisLock(stringRedisTemplate, lockKey, 100, 30, 10);
			if (!lock.tryLock()) {
				logger.info("******获取锁超时时间:另外一台服务器正在更新");
			} else {
				//提前更新
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar c = Calendar.getInstance();
				logger.info("当前日期********:"+sdf.format(c.getTime()));
				c.add(Calendar.DAY_OF_MONTH, 1);
				String nowDate = sdf.format(c.getTime());
				logger.info("增加一天的日期为************:"+nowDate);
				// 更新序列时间表的信息(先去查询时间，判断是否和现在时间一样)
				String times = sequenceTimeService.getTime();
				if (!times.equals(nowDate)) {
					// 更新的时间必须比数据库查出来的时间大
					if (Integer.valueOf(times.replaceAll("-", "")) < Integer.valueOf(nowDate.replaceAll("-", ""))) {

						sequenceTimeService.updateTime(nowDate, new Date());
						// 更新没有配置current_num的序列
						sequenceDAO.updateAllCurrentNum();
						// 更新配置了current_num的序列
						List<CreateSequence> allSequence = sequenceDAO.getSequenceWithCurrentNum();
						if (allSequence.size() > 0) {
							for (CreateSequence createSequence : allSequence) {
								Long seqNumStart = createSequence.getSeqNumStart();
								Long currentNum = 1L;
								if (seqNumStart > 0) {
									currentNum = createSequence.getSeqNumStart();
								}
								sequenceDAO.updateCurrentNum(currentNum, createSequence.getSeries());
							}
						}
						//sequence.time.date: 2020-11-01
						//sequence.time.last_updtme: 2020-11-01 00:00:01
						String sequenceTimeContext="sequence.time: "+nowDate+"\r\nsequence.time.last_updtme: "+ DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
						nacosConfigManager.getConfigService().publishConfig("sequence-time.yml","DEFAULT_GROUP",sequenceTimeContext);
						logger.info("序列号更新节点成功，本次更新的时间是 " + nowDate);
					}
				}
			}
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (lock != null) {
				lock.unlock();
				logger.info("Redis解锁时间：" + System.currentTimeMillis());
			}
		}

	}

}
