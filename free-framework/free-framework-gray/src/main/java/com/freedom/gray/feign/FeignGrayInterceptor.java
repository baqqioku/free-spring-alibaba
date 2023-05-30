package com.freedom.gray.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.free.common.util.TraceUtil.*;


@Component
@ConditionalOnClass(RequestInterceptor.class)
public class FeignGrayInterceptor implements RequestInterceptor {

    public void apply(RequestTemplate requestTemplate) {
        String traceId = MDC.get(TRACE_ID);
        //String traceId = "";
        String tag = MDC.get(TAG);
        //String tag = "";

        if ((StringUtils.isBlank(traceId) || StringUtils.isBlank(tag))) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(attributes!=null && attributes.getRequest()!=null){
                HttpServletRequest request = attributes.getRequest();
                traceId = request.getHeader(TRACE_ID);
                tag = request.getHeader(TAG);
                requestTemplate.header(TRACE_ID, traceId);
                requestTemplate.header(TAG, tag);
                MDC.put(TRACE_ID,traceId);
                MDC.put(TAG,tag);
                return;
            }
        }
        //这里可能定时任务进来的
        requestTemplate.header(TRACE_ID, traceId);
        requestTemplate.header(TAG, tag);
    }
}
