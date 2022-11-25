package com.gb.soa.sequence.task;

import com.gb.soa.omp.ccommon.util.RedisLock;
import com.gb.soa.sequence.dao.AutoSequenceDao;
import com.gb.soa.sequence.model.AtuoSeqClearModel;
import com.gb.soa.sequence.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class AutoSequenceClearListener {

	@Resource(name = "stringRedisTemplate")
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private AutoSequenceDao autoSequenceDao;

	protected static Logger logger = LoggerFactory.getLogger(AutoSequenceClearListener.class);

	/**
	 * 监听需要清零的自增序列
	 *
	 * @author changhong.deng
	 * @date 2018年7月18日
	 */
	public void listenAutoSequenceClear() {
		List<AtuoSeqClearModel> clearAutoSeqList = null;
		RedisLock lock = null;
		Boolean weatherLock = true;
		String lockKey = Constant.SEQUENCE_AUTO_CLEAR;
		String existKey = "auto_clear_exist";// 自增序列已经更新过
		try {
			lock = new RedisLock(stringRedisTemplate, lockKey, 100, 30, 10);
			if (!lock.tryLock()) {
				weatherLock = false;
				logger.info("获取锁超时时间:另外一台服务器正在更新");
			} else {
				String existNum = stringRedisTemplate.opsForValue().get(existKey);
				if (existNum == null || existNum.trim().equals("")) {
					// 查询需要清零的自增序列
					clearAutoSeqList = autoSequenceDao.getClearAutoSeq();
					if (clearAutoSeqList.size() <= 0) {
						return;
					}
					autoSequenceDao.updateAutoCurrentNum();// 自增序列是否清零
					for (AtuoSeqClearModel model : clearAutoSeqList) {
						String seqName = model.getSeqName().trim();
						Long tenantNumId = model.getTenantNumId();
						Long dataSign = model.getDataSign();
						String redisKey = seqName + tenantNumId + dataSign + "_auto_single";
						if (tenantNumId == 0L && dataSign == 0L) {
							redisKey = seqName.trim() + "_auto_com";// 序列共用，不分租户和测试标识，在数据库中配置都配置成0
						}
						stringRedisTemplate.opsForValue().set(redisKey, "");
						String autoSeqKey = redisKey + "_info_key";
						stringRedisTemplate.opsForValue().set(autoSeqKey, "");
					}
					stringRedisTemplate.opsForValue().set(existKey, "exist", 1, TimeUnit.HOURS);
					logger.info("自增序列共更新：" + clearAutoSeqList.size() + "条");
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

		} finally {
			if (lock != null && weatherLock) {
				lock.unlock();
				logger.info("Redis解锁时间：" + System.currentTimeMillis());
			}
		}

	}

}
