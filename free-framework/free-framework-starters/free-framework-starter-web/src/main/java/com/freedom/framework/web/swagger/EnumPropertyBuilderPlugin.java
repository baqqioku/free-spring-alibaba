package com.freedom.framework.web.swagger;

import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.freedom.common.model.IDict;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Swagger枚举属性增强插件
 *
 * @author pxy, lgy
 * @date 2022-5-10
 */

@Slf4j
public class EnumPropertyBuilderPlugin implements ModelPropertyBuilderPlugin, ExpandedParameterBuilderPlugin {


    /**
     * 枚举类型属性自动扩展注释
     * 适用于非get请求接口中DTO的属性（有注解@RequestBody标识）
     *
     * @param context context
     */
    @Override
    public void apply(ModelPropertyContext context) {
        context.getBeanPropertyDefinition()
                .map(BeanPropertyDefinition::getField)
                .map(AnnotatedField::getAnnotated)
                .ifPresent(field -> {
                    ApiModelPropertyEnum apiModelPropertyEnum = field.getAnnotation(ApiModelPropertyEnum.class);
                    ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                    if (apiModelPropertyEnum != null && apiModelProperty != null) {
                        String description = this.getDescription(apiModelPropertyEnum);

                        SwaggerUtils.addTo(context, "（" + description + "）");
                    }
                });
    }

    /**
     * 枚举类型属性自动扩展注释
     * 适用于get请求参数中DTO的属性
     *
     * @param context context
     */
    @Override
    public void apply(ParameterExpansionContext context) {
        Optional<ApiModelProperty> apiModelPropertyOptional = context.findAnnotation(ApiModelProperty.class);
        Optional<ApiModelPropertyEnum> apiModelPropertyEnumOptional = context.findAnnotation(ApiModelPropertyEnum.class);
        if (apiModelPropertyOptional.isPresent() && apiModelPropertyEnumOptional.isPresent()) {
            ApiModelPropertyEnum apiModelPropertyEnum = apiModelPropertyEnumOptional.get();
            context.getRequestParameterBuilder().description(this.getDescription(apiModelPropertyEnum));
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }

    /**
     * 增强注释，将枚举信息添加到原注释之后
     */
    private String getDescription(ApiModelPropertyEnum apiModelPropertyEnum) {
        Class<? extends IDict<?>> dictEnum = apiModelPropertyEnum.dictEnum();
        return Arrays.stream(dictEnum.getEnumConstants()).map(dict -> dict.getCode() + "：" + dict.getText()).collect((Collectors.joining("，")));
    }


}
