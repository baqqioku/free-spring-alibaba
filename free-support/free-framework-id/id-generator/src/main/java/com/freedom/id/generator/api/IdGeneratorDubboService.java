package com.freedom.id.generator.api;

import com.freedom.id.generator.model.SegmentIdVo;

public interface IdGeneratorDubboService {

    SegmentIdVo generateSegmentId(String businessId, Long segmentCount);
}
