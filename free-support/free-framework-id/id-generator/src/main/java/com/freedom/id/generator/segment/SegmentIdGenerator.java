package com.freedom.id.generator.segment;

import com.freedom.id.generator.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SegmentIdGenerator implements IdGenerator {
    @Autowired
    private BusinessCosIdSegmentCache businessCosIdSegment;

    @Override
    public Long getLongCosId(String businessId) {
        BusinessCosIdSegmentChain businessCosIdSegmentChain = businessCosIdSegment.getBusinessCosIdGenerator(businessId);
        return businessCosIdSegmentChain.getId();
    }

    @Override
    public BusinessCosIdSegmentChain getBusinessCosIdSegmentChain(String businessId) {
        return businessCosIdSegment.getBusinessCosIdGenerator(businessId);
    }
}
