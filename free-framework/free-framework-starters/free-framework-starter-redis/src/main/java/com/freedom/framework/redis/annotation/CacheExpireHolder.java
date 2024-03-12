package com.freedom.framework.redis.annotation;

import lombok.experimental.UtilityClass;

import java.util.Stack;

@UtilityClass
public class CacheExpireHolder {

    /**
     * 使用Stack适应方法嵌套的情况
     */
    private static final ThreadLocal<Stack<Long>> TIME_LOCAL = ThreadLocal.withInitial(Stack::new);

    /**
     * 获取当前方法失效时间（毫秒）
     */
    public static Long get() {
        Stack<Long> stack = TIME_LOCAL.get();
        // 使用peek方法而不是pop，因为在缓存命中时就不会执行到get方法
        return !stack.isEmpty() ? stack.peek() : null;
    }


    /**
     * 设置当前方法对应的缓存失效时间（毫秒）
     */
    public static void set(long milliseconds) {
        TIME_LOCAL.get().push(milliseconds);
    }


    /**
     * 移除
     */
    public static void remove() {
        Stack<Long> stack = TIME_LOCAL.get();
        stack.pop();
        if (stack.isEmpty()) {
            TIME_LOCAL.remove();
        }
    }

}

