package com.freedom.framework.web.swagger;

import com.msb.framework.common.model.IDict;

import java.lang.annotation.*;

/**
 * Swagger文档注释枚举翻译
 *
 * @author peng.xy
 * @date 2022/5/9
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelPropertyEnum {

    /**
     * 属性对应的字段枚举
     */
    Class<? extends IDict<?>> dictEnum();

}
