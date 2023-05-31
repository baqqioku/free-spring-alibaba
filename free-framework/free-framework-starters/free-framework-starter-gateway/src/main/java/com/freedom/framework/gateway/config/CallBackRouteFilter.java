package com.freedom.framework.gateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.free.common.util.TraceUtil;

import com.freedom.framework.gateway.core.config.CallBackConfigProperties;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.free.common.util.TraceUtil.REQUEST_COLOR;
import static com.free.common.util.TraceUtil.TRACE_ID;
import static com.freedom.framework.gateway.core.config.RouteConstant.aliPayCallBack;
import static com.freedom.framework.gateway.core.config.RouteConstant.webChatCallBack;


@Component
public class CallBackRouteFilter implements GlobalFilter, Ordered {
    private Logger log = LoggerFactory.getLogger(GrayRoundRobinLoadBalancer.class);

    private static final String CALL_BACK_PREF = "callback:";

    @Autowired
    private CallBackConfigProperties callBackConfigProperties;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String tag = request.getHeaders().getFirst(REQUEST_COLOR);

        String finalTarget = tag;

        Map<String, String> headers = new HashMap<>();
        URI url = request.getURI();
        String targetUrl = url.getPath();
        if (request.getMethod() == HttpMethod.POST) {
            if (callBackConfigProperties.getAliPayCallBack().contains(targetUrl) || callBackConfigProperties.webChatCallBack.contains(targetUrl)) {
                try {
                    AtomicReference<String> requestBody = new AtomicReference<>("");
                    ServerHttpRequestDecorator requestDecorator = new ServerHttpRequestDecorator(request);

                    Flux<DataBuffer> body = requestDecorator.getBody();
                    body.subscribe(buffer -> {
                        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
                        requestBody.set(charBuffer.toString());
                    });
                    //获取body参数
                    String requestParams = requestBody.get().toString();

                    if (requestParams != null) {
                        if (callBackConfigProperties.getWebChatCallBack().contains(targetUrl)) {
                            finalTarget = webChatNoitifyNext(requestParams, finalTarget);
                        } else if (callBackConfigProperties.getAliPayCallBack().contains(targetUrl)) {
                            finalTarget = aliPayNoitifyNext(requestDecorator, requestParams, finalTarget);
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    log.error("支付回调解析请求参数异常", e);
                }
            }
            headers.put(REQUEST_COLOR, finalTarget);
            headers.put(TRACE_ID, TraceUtil.getTraceId());
            setHeader(exchange, headers);
        }

        return chain.filter(exchange);
    }

    private Map<String, String>  handlerFromGateway(String str, ServerHttpRequest request) {
        String contentType = request.getHeaders().getContentType().toString();
        String sep = "--" + contentType.replace("multipart/form-data;boundary=", "");
        String[] strs = str.split("\r\n");
        boolean bankRow = false;// 空行
        boolean keyRow = true;// name=xxx行
        boolean append = false;// 内容是否拼接换行符
        Map<String, String> params = new LinkedHashMap<>();// 这里保证接收顺序，所以用linkedhashmap
        String s = null, key = null;
        StringBuffer sb = new StringBuffer();
        for (int i = 1, len = strs.length - 1; i < len; i++) {
            s = strs[i];
            if (keyRow) {
                key = s.replace("Content-Disposition: form-data; name=", "");
                key = key.substring(1, key.length() - 1);
                keyRow = false;
                bankRow = true;
                sb = new StringBuffer();
                append = false;
                continue;
            }
            if (sep.equals(s)) {
                keyRow = true;
                if (null != key) {
                    params.put(key, sb.toString());
                }
                append = false;
                continue;
            }
            if (bankRow) {
                bankRow = false;
                append = false;
                continue;
            }
            if (append) {
                sb.append("\r\n");
            }
            sb.append(s);
            append = true;
        }
        if (null != key) {
            params.put(key, sb.toString());
        }

        request.getQueryParams().forEach((k, v) -> params.put(k, v.toString()));

        return params;
    }





    private String webChatNoitifyNext(String requestParams,String finalTarget){
        SortedMap<String, String> resultMap = new TreeMap<>(parseMapFromXmlStr(requestParams));
        String outTradeNo = resultMap.get("out_trade_no");
        System.out.println(1/0);
        if(StringUtils.isNotBlank(outTradeNo)){
            //String target = redisTemplate.opsForValue().get(CALL_BACK_PREF+outTradeNo);
            return "gray";
        }

        return finalTarget;
    }


    private String aliPayNoitifyNext(ServerHttpRequestDecorator requestDecorator,String requestParams,String finalTarget){

        Map json = handlerFromGateway(requestParams,requestDecorator);
        log.info("json:{}",json);
        System.out.println(1/0);
        if(!json.isEmpty()){
            json.get("out_trade_no");
            //if(!jsonArray.isEmpty()){
                //String target = redisTemplate.opsForValue().get(CALL_BACK_PREF+jsonArray.getString(0));
                return "gray";
            //}
        }
        System.out.println(1/0);
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
     */
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
    }



}
