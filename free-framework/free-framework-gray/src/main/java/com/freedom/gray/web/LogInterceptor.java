package com.freedom.gray.web;

import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static com.freedom.common.util.TraceUtil.TAG;
import static com.freedom.common.util.TraceUtil.TRACE_ID;


/**
 * @Author:
 * @Date:
 * @Description:
 */
public class LogInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = request.getHeader(TRACE_ID);
        String tag = request.getHeader(TAG);
        //可以考虑让客户端传入链路ID，但需保证一定的复杂度唯一性；如果没使用默认UUID自动生成
        if (StringUtils.isEmpty(request.getHeader(TRACE_ID))){
            traceId= UUID.randomUUID().toString().replace("-", "");
            request.setAttribute(TRACE_ID,traceId);
        }
        MDC.put(TRACE_ID, traceId);
        MDC.put(TAG, tag);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) {
        MDC.clear();
    }

}
