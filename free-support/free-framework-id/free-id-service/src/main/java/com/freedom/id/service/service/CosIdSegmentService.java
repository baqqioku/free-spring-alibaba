package com.freedom.id.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedom.framework.redis.RedisClient;
import com.freedom.id.service.mapper.CosIdSegmentMapper;
import com.freedom.id.service.model.entity.CosIdSegment;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.freedom.id.service.model.constansts.RedisKeysConstants.INIT_BUSINESS_COS_ID_LOCK;

@Slf4j
@Service
public class CosIdSegmentService extends ServiceImpl<CosIdSegmentMapper, CosIdSegment> {

    @Autowired
    private RedisClient redisClient;

    public CosIdSegment saveOrUpdate(String businessId){
        CosIdSegment cosIdSegment = this.lambdaQuery().eq(CosIdSegment::getBusinessId,businessId).one();

        if(cosIdSegment!=null){
            return cosIdSegment;
        }

        RLock lock = redisClient.getLock(INIT_BUSINESS_COS_ID_LOCK.concat(businessId));
        try {
            lock.tryLock(20,20, TimeUnit.SECONDS);

            cosIdSegment = this.lambdaQuery().eq(CosIdSegment::getBusinessId,businessId).one();
            if(Objects.isNull(cosIdSegment)){
                cosIdSegment = new CosIdSegment()
                        .setBusinessId(businessId)
                        .setAutoIncrement(1L)
                ;
                this.save(cosIdSegment);
            }
        } catch (Exception e) {
            log.error("获取号段失败", e);
            Thread.currentThread().interrupt();

        }finally {
            lock.unlock();
        }
        return null;
    }
}
