package com.freedom.id.generator;

import com.freedom.id.generator.segment.BusinessCosIdSegmentChain;

public interface IdGenerator {

    /**
     * 获取整型id
     * @param businessId 业务标识，唯一
     * @return 主键id
     */
    Long getLongCosId(String businessId);


    /**
     * 先预获取号段链 ，调用号段链getId 方法性能最佳
     * @param businessId 业务id
     * @return
     */
    BusinessCosIdSegmentChain getBusinessCosIdSegmentChain(String businessId);
}
