package com.freedom.config;

import com.free.common.util.TraceUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
@ConditionalOnClass(RequestInterceptor.class)
public class FeignGrayInterceptor implements RequestInterceptor {

    public void apply(RequestTemplate requestTemplate) {
        String traceId = TraceUtil.getTraceId();
        String tag = MDC.get(TraceUtil.TAG);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(attributes)) {
            requestTemplate.header(TraceUtil.TRACE_ID, attributes.getRequest().getHeader(TraceUtil.TRACE_ID));
            requestTemplate.header(TraceUtil.TAG, attributes.getRequest().getHeader(TraceUtil.TAG));
        }else {
            //这里可能定时任务进来的
            requestTemplate.header(TraceUtil.TRACE_ID, traceId);
            requestTemplate.header(TraceUtil.TAG, tag);
        }
    }
}
