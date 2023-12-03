package com.freedom.id.service.dubbo;

import com.freedom.id.generator.api.IdGeneratorDubboService;
import com.freedom.id.generator.model.SegmentIdVo;
import com.freedom.id.service.manager.CosIdManager;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class IdGeneratorDubboServiceImpl   implements IdGeneratorDubboService {

    @Resource
    private CosIdManager cosIdManager;

    @Override
    public SegmentIdVo generateSegmentId(String businessId, Long segmentCount) {
        return cosIdManager.getSegment(businessId, segmentCount);
    }
}
