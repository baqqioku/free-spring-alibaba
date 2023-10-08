package com.freedom.event;

import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class MyRefreshEvent implements ApplicationListener<RefreshEvent> {
    @Override
    public void onApplicationEvent(RefreshEvent event) {
        System.out.println("onApplicationEvent");
    }
}
