package com.freedom.framework.statemechine.core;

import com.free.common.model.IDict;

public enum OrderStateEnum {

    CREATED, // 订单已创建
    PAID, // 订单已支付
    SHIPPED, // 订单已发货
    DELIVERED, // 订单已送达
    CANCELED, // 订单已取消
    CLOSED // 订单已关闭
}
