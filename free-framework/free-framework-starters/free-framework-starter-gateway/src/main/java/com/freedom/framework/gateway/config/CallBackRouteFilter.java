package com.freedom.framework.gateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.free.common.util.TraceUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.free.common.util.TraceUtil.REQUEST_COLOR;
import static com.free.common.util.TraceUtil.TRACE_ID;


@Component
public class CallBackRouteFilter implements GlobalFilter, Ordered {
    private Logger log = LoggerFactory.getLogger(GrayRoundRobinLoadBalancer.class);

    private static final String CALL_BACK_PREF = "callback:";

    private static List<String> webChatCallBack = new ArrayList<>();
    private static List<String> aliPayCallBack = new ArrayList<>();

    static {
        webChatCallBack.add("/pay/agencyWeChatNotify");
        webChatCallBack.add("/order/pay/");
        webChatCallBack.add("/first/test4");
        aliPayCallBack.add("/pay/agencyAliPayNotify");
        aliPayCallBack.add("/pay/aliPayNotify");
        aliPayCallBack.add("/first/test5");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String tag = request.getHeaders().getFirst(REQUEST_COLOR);

        String finalTarget = tag;

        Map<String, String> headers = new HashMap<>();
        URI url = request.getURI();
        String targetUrl = url.getPath();
        if (request.getMethod() == HttpMethod.POST) {
            AtomicReference<String> requestBody = new AtomicReference<>("");
            ServerHttpRequestDecorator requestDecorator = new ServerHttpRequestDecorator(request);
            Flux<DataBuffer> body = requestDecorator.getBody();
            body.subscribe(buffer -> {
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
                requestBody.set(charBuffer.toString());
            });
            //获取body参数
            JSONObject requestParams = JSONObject.parseObject(requestBody.get());
            log.info(JSON.toJSONString(requestParams));
            if(requestParams != null){
                if(webChatCallBack.contains(targetUrl)){
                    finalTarget = webChatNoitify(requestParams,finalTarget);
                }else if(aliPayCallBack.contains(targetUrl)){
                    finalTarget = aliPayNoitify(requestParams,finalTarget);
                }
            }
        }
        headers.put(REQUEST_COLOR, finalTarget);
        headers.put(TRACE_ID, TraceUtil.getTraceId());
        setHeader(exchange, headers);

        return chain.filter(exchange);
    }

    private String webChatNoitify(JSONObject requestParams,String finalTarget){
        String sb = requestParams.getString("sb");

        //将xml字符串转换为map
        //SortedMap<String, String> resultMap = new TreeMap<>(parseMapFromXmlStr(sb));
        JSONObject jsonObject = JSON.parseObject(sb);
        String outTradeNo = jsonObject.getString("out_trade_no");
        if(StringUtils.isNotBlank(outTradeNo)){
            //String target = redisTemplate.opsForValue().get(CALL_BACK_PREF+outTradeNo);
            return "gray";
        }
        return finalTarget;
    }

    private String aliPayNoitify(JSONObject requestParams,String finalTarget){

        JSONObject jsonObject = requestParams.getJSONObject("requestParams");
        if(jsonObject!=null){
            JSONArray jsonArray = jsonObject.getJSONArray("out_trade_no");
            if(!jsonArray.isEmpty()){
                //String target = redisTemplate.opsForValue().get(CALL_BACK_PREF+jsonArray.getString(0));
                return "gray";
            }
        }

        return finalTarget;
    }



    @Override
    public int getOrder() {
        return -10;
    }

    /**
     * 设置请求头 批量
     *
     * @param exchange
     * @param headers
     */
    protected void setHeader(ServerWebExchange exchange, Map<String, String> headers) {
        exchange.getRequest().mutate().headers(httpHeaders -> {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                if (value != null) {
                    httpHeaders.add(key, value);
                }
            }
        });
    }

    /**
     * 将xml字符串转换成map
     *
     * @param xml xml
     * @return Map
     *//*
    private static Map<String, String> parseMapFromXmlStr(String xml) {
        Map<String, String> map = new HashMap<>();
        Document doc;
        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点
            List<Element> list = rootElt.elements();//获取根节点下所有节点
            for (Element element : list) {  //遍历节点
                map.put(element.getName(), element.getText()); //节点的name为map的key，text为map的value
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }*/



}
