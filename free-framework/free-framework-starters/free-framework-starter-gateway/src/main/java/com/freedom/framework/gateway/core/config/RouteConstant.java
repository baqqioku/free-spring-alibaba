package com.freedom.framework.gateway.core.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public  class RouteConstant {

    public static String TOKEN = "X-Token";
    public static String VERSION = "X-Version";
    public static String ALL = "X-All";

    public static String TYPE_SPILIT= ":";

    public static String ALL_VALUE = "1";

    public static Set<String> webChatCallBack = new HashSet<>();
    public static Set<String> aliPayCallBack = new HashSet<>();

    static {
        webChatCallBack.add("/pay/agencyWeChatNotify");
        webChatCallBack.add("/order/pay/");
        webChatCallBack.add("/first/test4");
        //webChatCallBack.add("/first/test7");
        webChatCallBack.add("/first/test8");

        aliPayCallBack.add("/pay/agencyAliPayNotify");
        aliPayCallBack.add("/pay/aliPayNotify");
        aliPayCallBack.add("/first/test5");
        aliPayCallBack.add("/first/test7");
    }

}
