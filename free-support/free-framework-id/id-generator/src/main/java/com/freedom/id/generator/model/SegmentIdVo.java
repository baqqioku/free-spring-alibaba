package com.freedom.id.generator.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Setter
@Getter
@ToString
@Accessors(chain = true)
public class SegmentIdVo implements Serializable {

    private String businessId;

    private Long startId;

    private Long endId;
}
