package com.freedom.framework.mq.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;

public class TraceIdUtil {

    public  static final String TRACE_ID = "traceId";

    public  static final String TAG = "tag";

    public static final String PROD = "prod";

    public static final String GRAY = "gray";

    public static String getTraceId(){
        String traceId = MDC.get(TRACE_ID);
        if(StringUtils.isBlank(traceId)){
            traceId= UUID.randomUUID().toString().replace("-", "");
        }
        return traceId;
    }

    public static String getGrayTag(){
        String tag = MDC.get(TAG);
        if(StringUtils.isBlank(tag)){
            MDC.put(TAG,"prod");
            return PROD;
        };
        return tag;
    }
}
