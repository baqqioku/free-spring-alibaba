package com.freedom.framework.gateway.config;

import com.free.common.util.TraceUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static com.free.common.util.TraceUtil.REQUEST_COLOR;
import static com.free.common.util.TraceUtil.*;


public class GrayRoundRobinLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private Logger log = LoggerFactory.getLogger(GrayRoundRobinLoadBalancer.class);


    private static final String GaryKey = TAG;


    private final AtomicInteger position;
    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private final String serviceId;

    public GrayRoundRobinLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.position = new AtomicInteger(new Random().nextInt(1000));
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        HttpHeaders headers = (HttpHeaders) request.getContext();
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(list -> processInstanceResponse(list, headers));
    }

    private Response<ServiceInstance> processInstanceResponse(List<ServiceInstance> serviceInstances, HttpHeaders headers) {
        Response<ServiceInstance> serviceInstanceResponse = this.getInstanceResponse(serviceInstances, headers);
        if (serviceInstanceResponse instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) serviceInstanceResponse).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    /**
     * 获取可用实例
     *
     * @param instances 匹配serverId的应用列表
     * @param headers   请求头
     * @return 可用server
     */
    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, HttpHeaders headers) {
        String tag = headers.getFirst(TAG);
        String traceId = headers.getFirst(TRACE_ID);
        //存储链路
       /* TraceUtil.putTag(tag);
        MDC.put(TRACE_ID,traceId);*/
        boolean grayInvocation = false;
        if(StringUtils.isNotBlank(tag) && tag.equals(GRAY)){
            grayInvocation = true;
        }
        //存放灰度server
        List<ServiceInstance> grayInstanceServer = new ArrayList<>();
        //非灰度server
        List<ServiceInstance> defaultInstanceServer = new ArrayList<>();

        for (ServiceInstance serviceInstance : instances) {
            Map<String, String> metadata = serviceInstance.getMetadata();
            if(metadata.containsKey(TAG) && metadata.get(TAG).equals(GRAY)){
                grayInstanceServer.add(serviceInstance);
            } else {
                defaultInstanceServer.add(serviceInstance);
            }
        }

        int pos = Math.abs(this.position.incrementAndGet());
        ServiceInstance toBeChooseInstance;
        if (grayInvocation && grayInstanceServer.size() > 0) {
            toBeChooseInstance = grayInstanceServer.get(pos % grayInstanceServer.size());
            log.info(traceId+"<-----------------------------[网关-路由选择]:[{}]灰度路由,携带版本号为:[{}],请求地址[{}]----------------------------->", serviceId,tag, toBeChooseInstance.getUri());
            return new DefaultResponse(toBeChooseInstance);
        } else if(grayInvocation && grayInstanceServer.size() == 0){
            log.error("请求灰度应用[{}]无可用路由", serviceId);
            return new EmptyResponse();
        }else if (defaultInstanceServer.size()>0) {
            toBeChooseInstance = defaultInstanceServer.get(pos % defaultInstanceServer.size());
            log.info(traceId+"<-----------------------------[网关-路由选择]:[{}]默认路由,携带版本号为:[{}],请求地址:[{}]----------------------------->", serviceId,tag, toBeChooseInstance.getUri());
            return new DefaultResponse(toBeChooseInstance);
        }else {
            log.error("请求应用[{}]无可用路由", serviceId);
            return new EmptyResponse();
        }

    }
}

