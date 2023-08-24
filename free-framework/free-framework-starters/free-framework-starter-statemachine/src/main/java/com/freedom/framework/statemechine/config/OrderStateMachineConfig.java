package com.freedom.framework.statemechine.config;

import com.freedom.framework.statemechine.core.OrderEvent;
import com.freedom.framework.statemechine.core.OrderStateEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStateEnum, OrderEvent> {

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStateEnum, OrderEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderStateEnum.CREATED).target(OrderStateEnum.PAID).event(OrderEvent.PAY)
                .and()
                .withExternal()
                .source(OrderStateEnum.PAID).target(OrderStateEnum.SHIPPED).event(OrderEvent.SHIP)
                .and()
                .withExternal()
                .source(OrderStateEnum.SHIPPED).target(OrderStateEnum.DELIVERED).event(OrderEvent.DELIVER)
                .and()
                .withExternal()
                .source(OrderStateEnum.CREATED).target(OrderStateEnum.CANCELED).event(OrderEvent.CANCEL)
                .and()
                .withExternal()
                .source(OrderStateEnum.PAID).target(OrderStateEnum.CANCELED).event(OrderEvent.CANCEL)
                .and()
                .withExternal()
                .source(OrderStateEnum.SHIPPED).target(OrderStateEnum.CANCELED).event(OrderEvent.CANCEL);
    }
}
