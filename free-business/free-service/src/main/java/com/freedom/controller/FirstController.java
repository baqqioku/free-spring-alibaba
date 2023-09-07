package com.freedom.controller;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.free.common.util.TraceUtil;
import com.free.common.web.vo.ResponseVo;
import com.freedom.ao.*;
import com.freedom.config.GrayRouteConfig;
import com.freedom.framework.mq.config.WsRocketMQTemplate;
import com.freedom.model.AccountTbl;
import com.freedom.model.mapper.AccountTblMapper;
import com.freedom.second.api.SecondApi;
import com.freedom.second.api.ao.FirstApi;
import com.freedom.service.FristService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/first")
@Api
public class FirstController {
    Logger logger = LoggerFactory.getLogger(FirstController.class);

    @Autowired
    private WsRocketMQTemplate rocketMQTemplate;

    @Autowired
    private NacosDiscoveryProperties properties;

    @Autowired
    private GrayRouteConfig grayRouteConfig;

    @Autowired
    SecondApi sercondApi;

    @Autowired
    private AccountTblMapper accountTblMapper;


    //private GrayRouteService grayRouteService;

    @Autowired
    private FristService fristService;

    @RequestMapping("/test")
    @ResponseBody
    public ResponseVo test(){
        //MDC.put(TraceUtil.TAG,"gray");
        //MDC.put(TraceUtil.TRACE_ID,TraceUtil.getTraceId());
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
        logger.info("test1");
        return ResponseVo.success("gray");
    }

    @RequestMapping("/test3")
    @ResponseBody
    public ResponseVo test3(@RequestBody String text) throws NacosException {
        //MDC.put(TraceUtil.TAG,"gray");
        //MDC.put(TraceUtil.TRACE_ID,TraceUtil.getTraceId());


        return ResponseVo.success(text);
    }

    @RequestMapping("/test4")
    @ResponseBody
    public ResponseVo test3(@RequestBody CallbackVo vo) throws NacosException {
        //MDC.put(TraceUtil.TAG,"gray");
        //MDC.put(TraceUtil.TRACE_ID,TraceUtil.getTraceId());


        return ResponseVo.success(vo);
    }

    @RequestMapping("/test5")
    @ResponseBody
    public ResponseVo test5(@RequestBody AliPayNotifyAo vo) {
        //MDC.put(TraceUtil.TAG,"gray");
        //MDC.put(TraceUtil.TRACE_ID,TraceUtil.getTraceId());
        logger.info("test5");
        System.out.println(1/0);
        return ResponseVo.success(vo);
    }


    @RequestMapping("/test6")
    @ResponseBody
    public ResponseVo test5() {
        //MDC.put(TraceUtil.TAG,"gray");
        //MDC.put(TraceUtil.TRACE_ID,TraceUtil.getTraceId());
        logger.info("test6");

        return ResponseVo.success(sercondApi.sayHello("guoguo"));
    }

    @RequestMapping("/test7")
    public ResponseVo test7(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //MDC.put(TraceUtil.TAG,"gray");
        //MDC.put(TraceUtil.TRACE_ID,TraceUtil.getTraceId());
        String json = JSON.toJSONString(request.getParameterMap());

        logger.info(json);

        return ResponseVo.success(sercondApi.sayHello("guoguo"));
    }

    @RequestMapping("/test8")
    @ResponseBody
    public ResponseVo test8(HttpServletRequest request, HttpServletResponse response) {
        //MDC.put(TraceUtil.TAG,"gray");
        //MDC.put(TraceUtil.TRACE_ID,TraceUtil.getTraceId());
        String json = JSON.toJSONString(request.getParameterMap());
        logger.info(json);
        return ResponseVo.success(json);
    }

    @RequestMapping("/test9")
    @ResponseBody
    public ResponseVo test9() {
        GuoguoVo guoguoVo = new GuoguoVo();
        guoguoVo.setName("guoguo");
        guoguoVo.setSignType(SignType.ADD);
        return ResponseVo.success(guoguoVo);
    }

    @RequestMapping("/test10")
    @ResponseBody
    public ResponseVo test10(@RequestBody GuoguoAo guoguoAo) {
        GuoguoVo guoguoVo = new GuoguoVo();
        guoguoVo.setName("guoguo");
        guoguoVo.setSignType(SignType.ADD);
        return ResponseVo.success(guoguoVo);
    }

    @RequestMapping("/test11")
    @ResponseBody
    public ResponseVo test11(@RequestBody GuoguoAo guoguoAo) {
        GuoguoVo guoguoVo = new GuoguoVo();
        guoguoVo.setName("guoguo");
        guoguoVo.setSignType(SignType.ADD);
        FirstApi firstApi = sercondApi.sayHello1("guoguo");
        return ResponseVo.success(firstApi);
    }

    @RequestMapping("/test12")
    @ResponseBody
    public ResponseVo test12(@RequestBody @Valid GuoguoAo guoguoAo) {
        GuoguoVo guoguoVo = new GuoguoVo();
        guoguoVo.setName("guoguo");
        guoguoVo.setSignType(SignType.ADD);
        FirstApi firstApi = sercondApi.sayHello1("guoguo");
        return ResponseVo.success(firstApi);
    }

    @RequestMapping("/test13")
    @ResponseBody
    public ResponseVo test13() {
        AccountTbl accountTbl = new AccountTbl();
        accountTblMapper.insertSelective(accountTbl);
        accountTbl.setUserId("U100002");
        accountTbl.setMoney(1000);
        return ResponseVo.success(accountTbl);
    }

    //@Async
    @RequestMapping("/test14")
    public void test14(Integer num) throws InterruptedException {
        logger.info(num+ "");

        TimeUnit.HOURS.sleep(1);
    }

    @RequestMapping("/test15/{num}")
    public void test15(@PathVariable String num) throws InterruptedException {


        fristService.test1(num);
    }

    public  String streamToString(InputStream inputStream) {
        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                br.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
