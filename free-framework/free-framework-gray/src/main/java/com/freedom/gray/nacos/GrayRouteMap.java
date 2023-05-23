package com.freedom.gray.nacos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.freedom.gray.nacos.RouteConstant.TYPE_SPILIT;

public class GrayRouteMap {


    private static Map<String,String> routeMap = new ConcurrentHashMap<String,String>();

    public static void parseRoute(String routeType,String routeValue,String target){
        routeMap.put(routeType+ TYPE_SPILIT+routeValue,target);
    }

    public static String getTokenRoute(String headerValue){
        return routeMap.get(RouteConstant.TOKEN+TYPE_SPILIT+headerValue);
    }

    public static String getVersionRoute(String headerValue){
        return routeMap.get(RouteConstant.VERSION+TYPE_SPILIT+headerValue);
    }

    public static String getAllRoute(String headerValue){
        return routeMap.get(RouteConstant.ALL+TYPE_SPILIT+headerValue);
    }

    public static void clean(){
        routeMap.clear();
    }




}
