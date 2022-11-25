package com.gb.soa.sequence.service.impl;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.gb.soa.omp.ccommon.util.JsonMapper;
import com.gb.soa.sequence.dao.PlatformOfflineSubUnitSequenceDao;
import com.gb.soa.sequence.dao.SequenceDAO;
import com.gb.soa.sequence.model.PlatformOfflineSequence;
import com.gb.soa.sequence.service.SequenceOfflineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service("sequenceOfflineService")
public class SequenceOfflineServiceImpl implements SequenceOfflineService {

	protected static Logger logger = LoggerFactory.getLogger(SequenceOfflineServiceImpl.class);

	private static JsonMapper mapper = JsonMapper.nonDefaultMapper();

	static {
		mapper = JsonMapper.nonEmptyMapper();
		mapper.getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.LowerCaseStrategy.SNAKE_CASE);
	}

	@Autowired
	private PlatformOfflineSubUnitSequenceDao platformOfflineSubUnitSequenceDao;
	@Autowired
	private SequenceDAO sequenceDAO;

	// 项目启动时刷新
	@PostConstruct
	@Override
	public void initSequence() {
		List<PlatformOfflineSequence> offlineSubSeqList = null;
		Integer updateNum = 0;
		try {
			// 查询离线序列
			offlineSubSeqList = platformOfflineSubUnitSequenceDao.getOfflineSubSequence();
			if (offlineSubSeqList.size() > 0) {
				for (PlatformOfflineSequence model : offlineSubSeqList) {
					// 更新离线序列的开始值和结束值
					String seqName = model.getSeqName();
					Long seqNumStart = model.getOnlineStartNum();
					Long seqNumEnd = model.getOnlineEndNum();
					Integer update = sequenceDAO.updateSeqStartAndEndNum(seqName, seqNumStart, seqNumEnd);
					updateNum += update;
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("共更新序列：" + updateNum + "条");

	}

}
