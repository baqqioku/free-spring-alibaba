package com.freedom.framework.statemechine.config;

import com.freedom.framework.statemechine.core.OrderEvent;
import com.freedom.framework.statemechine.core.OrderStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

@Configuration
public class OrderStateMachineFactory {

    @Autowired
    private StateMachineFactory<OrderStateEnum, OrderEvent> stateMachineFactory;

    public StateMachine<OrderStateEnum, OrderEvent> createStateMachine() {
        StateMachine<OrderStateEnum, OrderEvent> stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.start();
        return stateMachine;
    }
}
