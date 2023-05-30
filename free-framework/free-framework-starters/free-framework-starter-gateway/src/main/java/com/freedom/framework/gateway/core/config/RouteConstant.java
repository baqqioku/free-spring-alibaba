package com.freedom.framework.gateway.core.config;

import java.util.ArrayList;
import java.util.List;

public  class RouteConstant {

    public static String TOKEN = "X-Token";
    public static String VERSION = "X-Version";
    public static String ALL = "X-All";

    public static String TYPE_SPILIT= ":";

    public static String ALL_VALUE = "1";

    public static List<String> webChatCallBack = new ArrayList<>();
    public static List<String> aliPayCallBack = new ArrayList<>();

    static {
        webChatCallBack.add("/pay/agencyWeChatNotify");
        webChatCallBack.add("/order/pay/");
        webChatCallBack.add("/first/test4");
        webChatCallBack.add("/first/test7");

        aliPayCallBack.add("/pay/agencyAliPayNotify");
        aliPayCallBack.add("/pay/aliPayNotify");
        aliPayCallBack.add("/first/test5");
    }

}
