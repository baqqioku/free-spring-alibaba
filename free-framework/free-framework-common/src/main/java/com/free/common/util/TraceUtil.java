package com.free.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;

public class TraceUtil {
    public static final String REQUEST_COLOR = "X-WS-Request-Color";

    public  static final String TRACE_ID = "traceId";

    public  static final String TAG = "tag";

    public static final String PROD = "prod";

    public static final String GRAY = "gray";

    public static String getTraceId(){
        String traceId = MDC.get(TRACE_ID);
        if(StringUtils.isBlank(traceId)){
            traceId= UUID.randomUUID().toString().replace("-", "");
            //MDC.put(TRACE_ID,traceId);
        }
        return traceId;
    }

    public static String getGrayTag(){
        String tag = MDC.get(TAG);
        if(StringUtils.isBlank(tag)){
            //MDC.put(TAG,PROD);
            return PROD;
        };
        return tag;
    }

    public static void putTag(String tag){
        if(StringUtils.isBlank(tag)){
            MDC.put(TAG,PROD);
        }else {
            MDC.put(TAG,tag);
        };
    }
}
