package com.freedom.framework.gateway.config;

import com.free.common.util.TraceUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GrayLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private Logger logger = LoggerFactory.getLogger(GrayLoadBalancer.class);

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private final String serviceId;

    private final AtomicInteger position;

    public GrayLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this(serviceInstanceListSupplierProvider,serviceId,new Random().nextInt(1000));
    }

    public GrayLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                                   String serviceId, int seedPosition) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.position = new AtomicInteger(seedPosition);
    }


    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = this.serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);

        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(serviceInstances,request));

    }

    private Response<ServiceInstance> processInstanceResponse(List<ServiceInstance> instances, Request request) {
        if (instances.isEmpty()) {
            logger.warn("No servers available for service: " + this.serviceId);
            return new EmptyResponse();
        } else {
            DefaultRequestContext requestContext = (DefaultRequestContext) request.getContext();
            RequestData clientRequest = (RequestData) requestContext.getClientRequest();
            HttpHeaders headers = clientRequest.getHeaders();

            // get Request Header
            String tag = headers.getFirst(TraceUtil.TAG);

            if(StringUtils.isEmpty(tag)){
                return processRibbonInstanceResponse(instances);
            }

            logger.info("request header tag : {}",tag );
            // filter service instances
            List<ServiceInstance> serviceInstances = instances.stream()
                    .filter(instance -> tag.equals(instance.getMetadata().get("tag")))
                    .collect(Collectors.toList());

            if(serviceInstances.size() > 0){
                return processRibbonInstanceResponse(serviceInstances);
            }else{
                return processRibbonInstanceResponse(instances);
            }
        }
    }

    /**
     * 负载均衡器
     * 参考 org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer#getInstanceResponse
     * @author javadaily
     */
    private Response<ServiceInstance> processRibbonInstanceResponse(List<ServiceInstance> instances) {
        int pos = Math.abs(this.position.incrementAndGet());
        ServiceInstance instance = instances.get(pos % instances.size());
        return new DefaultResponse(instance);
    }



}
