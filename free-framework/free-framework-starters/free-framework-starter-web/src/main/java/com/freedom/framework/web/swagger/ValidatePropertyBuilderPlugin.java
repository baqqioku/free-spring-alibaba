package com.freedom.framework.web.swagger;

import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTermType;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.TokenCollector;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.TokenIterator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.core.annotation.AnnotationUtils;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import javax.validation.Constraint;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Swagger基于validation注解生成 提示文档
 *
 * @author lgy
 * @date 2022-5-24
 */
@Slf4j
public class ValidatePropertyBuilderPlugin implements ModelPropertyBuilderPlugin {


    private final PlatformResourceBundleLocator platformResourceBundleLocator = new PlatformResourceBundleLocator("org.hibernate.validator.ValidationMessages");

    private final ResourceBundle defaultResourceBundle = platformResourceBundleLocator.getResourceBundle(Locale.CHINA);

    @Override
    public void apply(ModelPropertyContext context) {
        try {
            context.getBeanPropertyDefinition()
                    .map(BeanPropertyDefinition::getField)
                    .map(AnnotatedField::getAnnotated)
                    .ifPresent(field -> {
                        Annotation[] annotations = field.getAnnotations();
                        String message = Stream.of(annotations)
                                .filter(annotation -> annotation.annotationType().isAnnotationPresent(Constraint.class))
                                .map(this::annotationToMessage).collect(Collectors.joining(",", "ℹ️ ", ""));
                        if (message.isEmpty()) {
                            return;
                        }
                        SwaggerUtils.addTo(context, "<br><span style='color:#2067ae'>" + message + "</span>");
                    });
        } catch (Exception e) {
            log.error("追加validation 注解描述失败", e);
        }
    }

    private String annotationToMessage(Annotation annotation) {
        Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation);
        String message = annotationAttributes.get("message").toString();
        if (message.indexOf("{") != 0) {
            return message;
        }
        String resolvedMessage = defaultResourceBundle.getString(removeCurlyBraces(message));
        TokenIterator tokenIterator = new TokenIterator(new TokenCollector(resolvedMessage, InterpolationTermType.PARAMETER).getTokenList());
        while (tokenIterator.hasMoreInterpolationTerms()) {
            String term = tokenIterator.nextInterpolationTerm();
            String resolvedExpression = annotationAttributes.get(removeCurlyBraces(term)).toString();
            tokenIterator.replaceCurrentInterpolationTerm(resolvedExpression);
        }
        return tokenIterator.getInterpolatedMessage();
    }

    private String removeCurlyBraces(String parameter) {
        return parameter.substring(1, parameter.length() - 1);
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }


}
