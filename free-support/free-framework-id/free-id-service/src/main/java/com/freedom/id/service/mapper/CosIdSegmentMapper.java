package com.freedom.id.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.freedom.id.service.model.entity.CosIdSegment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface CosIdSegmentMapper extends BaseMapper<CosIdSegment> {

    @Update("update cosid_segment set auto_increment = auto_increment + #{segmentCount} where id = #{id} ")
    Boolean updateCosIdSegmentAutoIncrement(@Param("id") Long id, @Param("segmentCount") Long segmentCount);
}
