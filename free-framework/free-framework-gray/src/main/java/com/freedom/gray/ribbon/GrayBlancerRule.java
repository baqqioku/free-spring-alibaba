package com.freedom.gray.ribbon;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.ExtendBalancer;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.free.common.util.TraceUtil;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: GrayBlancerRule
 * @Author: guoguo
 * @Date: 2023/2/3 11:08
 * @version: 1.0
 */
public class GrayBlancerRule extends AbstractLoadBalancerRule {

    protected static final Logger logger = LoggerFactory.getLogger(GrayBlancerRule.class);

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;


    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    @Override
    public Server choose(Object key) {
        try{
            boolean grayInvocation = false;
            String grayTag = MDC.get(TraceUtil.TAG);
            if(StringUtils.isNotBlank(grayTag) && grayTag.equals(TraceUtil.GRAY)){
                grayInvocation = true;
            }
            String clusterName = this.nacosDiscoveryProperties.getClusterName();
            String group = this.nacosDiscoveryProperties.getGroup();
            //Map<String, String> ribbonAttributes = this.nacosDiscoveryProperties.getMetadata();
            DynamicServerListLoadBalancer   loadBalancer = (DynamicServerListLoadBalancer) getLoadBalancer();
            String serviceName = loadBalancer.getName();


            String name = loadBalancer.getName();
            NamingService namingService = this.nacosDiscoveryProperties.namingServiceInstance();
            //List<Instance> instances = namingService.selectInstances(name, true);
            //NamingService namingService = this.nacosServiceManager.getNamingService(this.nacosDiscoveryProperties.getNacosProperties());
            List<Instance> allInstances = namingService.selectInstances(serviceName, group, true);
            List<Instance> grayInstances = new ArrayList<>();
            List<Instance> noneGrayInstances = new ArrayList<>();
            Instance toBeChooseInstance;
            if (CollectionUtils.isEmpty(allInstances)) {
                logger.warn("no instance in service {}", serviceName);
                return null;
            } else {
                if (StringUtils.isNotBlank(clusterName)) {
                    for (Instance instance : allInstances) {
                        Map<String, String> metadata = instance.getMetadata();
                        //当前服务的灰度标签和被调用的服务的灰度标签比,相同则灰色
                        if(metadata.containsKey(TraceUtil.TAG) && metadata.get(TraceUtil.TAG).equals(TraceUtil.GRAY)){
                            grayInstances.add(instance);
                        } else {
                            noneGrayInstances.add(instance);
                        }
                    }
                }
               /* if (grayInvocation && grayInstances.size() > 0) {
                    toBeChooseInstance = ExtendBalancer.getHostByRandomWeight2(grayInstances);
                    logger.debug("进入灰度实例：{}", toBeChooseInstance.toString());
                    return new NacosServer(toBeChooseInstance);
                }else if(grayInvocation && grayInstances.size() == 0){
                    logger.error("无灰度实例：{}", serviceName,"no gray instance();");
                    return null;
                }else if (noneGrayInstances.size() > 0) {
                    toBeChooseInstance = ExtendBalancer.getHostByRandomWeight2(noneGrayInstances);
                } else {
                    logger.error("无服务实例：{}", serviceName,"no gray instance();");
                    return null;
                }*/

                 if (grayInvocation && grayInstances.size() > 0) {
                    toBeChooseInstance = ExtendBalancer.getHostByRandomWeight2(grayInstances);
                    logger.debug("进入灰度实例：{}", toBeChooseInstance.toString());
                    return new NacosServer(toBeChooseInstance);
                }else if (noneGrayInstances.size() > 0) {
                    toBeChooseInstance = ExtendBalancer.getHostByRandomWeight2(noneGrayInstances);
                } else {
                    logger.error("无服务实例：{}", serviceName,"no gray instance();");
                    return null;
                }
                logger.debug("正常实例：{}",serviceName);
                return new NacosServer(toBeChooseInstance);
            }
        } catch (Exception e) {
            logger.warn("NacosRule error", e);
            return null;
        }finally {
            MDC.clear();
        }

    }
}
