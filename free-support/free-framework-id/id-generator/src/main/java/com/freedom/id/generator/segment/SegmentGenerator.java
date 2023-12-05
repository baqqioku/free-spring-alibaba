package com.freedom.id.generator.segment;


import com.freedom.id.generator.api.IdGeneratorDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author liao
 */
@Component
public class SegmentGenerator {

    @DubboReference
    private IdGeneratorDubboService idGeneratorDubboService;



}
