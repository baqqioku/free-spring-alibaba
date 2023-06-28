package com.freedom.framework.web.swagger;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import springfox.documentation.builders.PropertySpecificationBuilder;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author liao
 */
@Slf4j
public class SwaggerUtils {

    private SwaggerUtils() {
    }

    public static void addTo(ModelPropertyContext context, String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        PropertySpecificationBuilder specificationBuilder = context.getSpecificationBuilder();
        try {
            Field descriptionField = specificationBuilder.getClass().getDeclaredField("description");
            descriptionField.setAccessible(true);
            Object description = descriptionField.get(specificationBuilder);
            if (Objects.nonNull(description) && StringUtils.isNotBlank(description.toString())) {
                specificationBuilder.description(description + message);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("swagger追加描述失败", e);
        }
    }


}
