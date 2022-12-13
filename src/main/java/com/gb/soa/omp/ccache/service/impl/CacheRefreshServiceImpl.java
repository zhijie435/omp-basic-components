package com.gb.soa.omp.ccache.service.impl;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.gb.soa.omp.ccache.api.request.CacheDeleteRequest;
import com.gb.soa.omp.ccache.api.request.CacheGetRequest;
import com.gb.soa.omp.ccache.api.request.CommonCacheRefreshRequest;
import com.gb.soa.omp.ccache.api.response.CacheDeleteResponse;
import com.gb.soa.omp.ccache.api.response.CommonCacheRefreshResponse;
import com.gb.soa.omp.ccache.api.service.CacheRefreshService;
import com.gb.soa.omp.ccache.api.service.CacheStoreService;
import com.gb.soa.omp.ccache.dao.EcCacheMethodSchemaDefineDao;
import com.gb.soa.omp.ccache.dao.EcCommonCacheDependenceDao;
import com.gb.soa.omp.ccache.dao.MdmsSConfigDao;
import com.gb.soa.omp.ccache.entity.EC_CACHE_METHOD_SCHEMA_DEFINE;
import com.gb.soa.omp.ccache.entity.EC_COMMON_CACHE_DEPENDENCE;
import com.gb.soa.omp.ccache.service.CacheCommonService;
import com.gb.soa.omp.ccache.util.Constant;
import com.gb.soa.omp.ccommon.api.exception.ExceptionType;
import com.gb.soa.omp.ccommon.api.exception.ValidateBusinessException;
import com.gb.soa.omp.ccommon.util.ExceptionUtil;
import com.gb.soa.omp.ccommon.util.JsonMapper;
import com.gb.soa.omp.ccommon.util.TransactionUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ykcloud.soa.mapping.GatewayMapping;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service("cacheRefreshService")
@GatewayMapping
@RestController
@FeignClient(value = "@Value(\"${spring.application.name}\")")
public class CacheRefreshServiceImpl implements CacheRefreshService {
    private final static Logger log = LoggerFactory.getLogger(CacheRefreshServiceImpl.class);
    private static JsonMapper mapper;

    static {
        mapper = JsonMapper.nonEmptyMapper();
        mapper.getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.LowerCaseStrategy.SNAKE_CASE);
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private EcCacheMethodSchemaDefineDao ecCacheMethodSchemaDefineDao;

    @Resource(name = "cacheStoreService")
    private CacheStoreService cacheStoreService;

    @Resource(name = "msgcenterTransactionManager")
    private PlatformTransactionManager msgcenterTransactionManager;
    @Resource
    private EcCommonCacheDependenceDao ecCommonCacheDependenceDao;

    public static Cache<String, Object> cache = CacheBuilder
            .newBuilder().maximumSize(200000).expireAfterWrite(1L, TimeUnit.DAYS).build();
    @Resource
    private MdmsSConfigDao mdmsSConfigDao;

    @Override
    public CommonCacheRefreshResponse refreshCommonCache(CommonCacheRefreshRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("begin refreshCommonCache request:{}", mapper.toJson(request));
        }
        CommonCacheRefreshResponse response = new CommonCacheRefreshResponse();
        try {
            request.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10030);
            Long tenantNumId = request.getTenantNumId(), dataSign = request.getDataSign();
            String db = getDbDataSource(tenantNumId, dataSign, request.getDatabase(), getDbCacheKey(request.getDatabase()));
            List<EC_COMMON_CACHE_DEPENDENCE> commonCacheDependenceList = ecCommonCacheDependenceDao.getCommonCacheDependenceByTableNameAndSeries(tenantNumId, dataSign, db, request.getTableName(), request.getTableSeries());
            if (CollectionUtils.isNotEmpty(commonCacheDependenceList)) {
                //删除同一缓存键关联所有依赖记录
                commonCacheDependenceList.stream().forEach(commonCacheDependence -> {
                    TransactionStatus status = msgcenterTransactionManager.getTransaction(TransactionUtil.newTransactionDefinition());
                    try {
                        ecCommonCacheDependenceDao.deleteEntityByMethodNameAndCacheKeySeries(commonCacheDependence.getTENANT_NUM_ID(), commonCacheDependence.getDATA_SIGN(), commonCacheDependence.getMETHOD_NAME(), commonCacheDependence.getCACHE_KEY(), commonCacheDependence.getSERIES());
                        //删除所有对应的缓存
                        //if (StringUtils.isNotEmpty(refreshCacheSpecialGroup) && refreshCacheSpecialGroup.equals(commonCacheDependence.getDUBBO_GROUP())) {

                        stringRedisTemplate.delete(commonCacheDependence.getCACHE_KEY());

                        EC_CACHE_METHOD_SCHEMA_DEFINE entity = ecCacheMethodSchemaDefineDao.getEntityByMethodName(commonCacheDependence.getMETHOD_NAME());
                        if (entity.getLIST_SIGN().equals(1L)) {
                            List<String> series = ecCommonCacheDependenceDao.getSeriesByMethodNameAndCacheKey(commonCacheDependence.getTENANT_NUM_ID(), commonCacheDependence.getDATA_SIGN(), commonCacheDependence.getMETHOD_NAME(), commonCacheDependence.getCACHE_KEY());
                            if (CollectionUtils.isNotEmpty(series)) {
                                for (String serie : series) {
                                    ecCommonCacheDependenceDao.deleteEntityByMethodNameAndCacheKeySeries(commonCacheDependence.getTENANT_NUM_ID(), commonCacheDependence.getDATA_SIGN(), commonCacheDependence.getMETHOD_NAME(), commonCacheDependence.getCACHE_KEY(), serie);
                                }
                            }
                        }
                        if (request.getOptType().equals(2)) {
                            //修改操作，产生新的缓存
                            CacheGetRequest cacheGetRequest = new CacheGetRequest();
                            cacheGetRequest.setTenantNumId(commonCacheDependence.getTENANT_NUM_ID());
                            cacheGetRequest.setDataSign(commonCacheDependence.getDATA_SIGN());
                            cacheGetRequest.setMethodName(commonCacheDependence.getMETHOD_NAME());
                            cacheGetRequest.setParams(commonCacheDependence.getPARAMS());

                            // if (StringUtils.isNotEmpty(refreshCacheSpecialGroup) && refreshCacheSpecialGroup.equals(commonCacheDependence.getDUBBO_GROUP())) {
                            cacheStoreService.getCache(cacheGetRequest);
                        }
                        msgcenterTransactionManager.commit(status);
                    } catch (Exception e) {
                        msgcenterTransactionManager.rollback(status);
                        log.info("清理缓存数据异常！{}", e.getMessage());
                    }
                });

            }
        } catch (Exception ex) {
            ExceptionUtil.processException(ex, response);
        }
        if (log.isDebugEnabled()) {
            log.debug("end refreshCommonCache response:{}", response.toLowerCaseJson());
        }
        return response;
    }

    private String getDbDataSource(Long tenantNumId, Long dataSign, String dbName, String cacheKey) {
        try {
            return (String) cache.get(cacheKey, () -> {
                String dataSource = mdmsSConfigDao.getConfigValueByConfigName(0L, 0L, "refresh_common_cache_" + dbName);
                if (StringUtils.isBlank(dataSource)) {
                    throw new ValidateBusinessException(Constant.SUB_SYSTEM, ExceptionType.BE40073, "从本地内存缓存中获取数据源信息出错!database:" + dbName);
                }
                return dataSource;
            });
        } catch (ExecutionException e) {
            throw new ValidateBusinessException(Constant.SUB_SYSTEM, ExceptionType.BE40073, e.getMessage());
        }
    }

    private String getDbCacheKey(String dbName) {
        return new StringBuilder().append("refresh_db_name_").append(dbName).toString();
    }
}
