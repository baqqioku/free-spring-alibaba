package com.freedom.controller;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.free.common.util.TraceUtil;
import com.freedom.config.GrayRouteConfig;
import com.freedom.framework.mq.config.WsRocketMQTemplate;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.free.common.web.vo.ResponseVo;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/first")
public class FirstController {

    @Autowired
    private WsRocketMQTemplate rocketMQTemplate;

    @Autowired
    private NacosDiscoveryProperties properties;

    @Autowired
    private GrayRouteConfig grayRouteConfig;

    @Autowired
    //private GrayRouteService grayRouteService;

    @RequestMapping("/test")
    @ResponseBody
    public ResponseVo test(){
        MDC.put(TraceUtil.TAG,"gray");
        MDC.put(TraceUtil.TRACE_ID,TraceUtil.getTraceId());
        List<String> messages = new ArrayList<String>();
        messages.add("1");
        messages.add("2");
        messages.add("3");
        messages.add("4");
        messages.add("51");
        rocketMQTemplate.syncSend("guoguo",messages);
        return ResponseVo.success("果果你好");
    }

    @RequestMapping("/test1")
    @ResponseBody
    public ResponseVo test1() throws NacosException {
        MDC.put(TraceUtil.TAG,"gray");
        MDC.put(TraceUtil.TRACE_ID,TraceUtil.getTraceId());
        rocketMQTemplate.syncSend("guoguo${tag}", "hello guoguo");

        return ResponseVo.success("gray");
    }
}
