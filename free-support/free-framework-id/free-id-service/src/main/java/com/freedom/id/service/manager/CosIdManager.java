package com.freedom.id.service.manager;

import com.freedom.framework.web.result.BizAssert;
import com.freedom.id.generator.model.SegmentIdVo;
import com.freedom.id.service.model.entity.CosIdSegment;
import com.freedom.id.service.service.CosIdSegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CosIdManager {

    @Autowired
    private CosIdSegmentService cosIdSegmentService;

    @Transactional(rollbackFor = Exception.class)
    public SegmentIdVo getSegment(String businessId,Long segmentCount){
        CosIdSegment cosIdSegment = initBusinessSegment(businessId);
        Boolean result = cosIdSegmentService.getBaseMapper().updateCosIdSegmentAutoIncrement(cosIdSegment.getId(), segmentCount);
        BizAssert.isTrue(result, "获取号段异常");
        cosIdSegment = cosIdSegmentService.getById(cosIdSegment.getId());
        return new SegmentIdVo()
                .setStartId(cosIdSegment.getAutoIncrement() - segmentCount)
                .setEndId(cosIdSegment.getAutoIncrement())
                .setBusinessId(businessId);
    }

    public CosIdSegment initBusinessSegment(String businessId) {
        return cosIdSegmentService.saveOrUpdate(businessId);
    }

}
