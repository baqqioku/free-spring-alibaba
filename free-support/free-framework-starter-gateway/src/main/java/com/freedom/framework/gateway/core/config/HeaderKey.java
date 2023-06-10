package com.freedom.framework.gateway.core.config;

/**
 * @Description
 * @Author HuangFuBin
 * @Date 2021-02-18
 */
public interface HeaderKey {

    /**
     * 来源 根据端来划分
     */
    String SOURCE = "SOURCE";

    /**
     * 渠道来源，根据载体划分
     */
    String CHANNEL_SOURCE = "CHANNEL-SOURCE";

    /**
     * 请求客户端的ip
     */
    String X_FORWARDED_FOR = "R-IP";
}
