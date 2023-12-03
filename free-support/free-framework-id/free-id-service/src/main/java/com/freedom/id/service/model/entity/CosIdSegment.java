package com.freedom.id.service.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.freedom.framework.mysql.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("cosid_segment")
public class CosIdSegment extends BaseEntity {

    @TableId
    private Long id;
    private String businessId;
    private Long autoIncrement;
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;


}
