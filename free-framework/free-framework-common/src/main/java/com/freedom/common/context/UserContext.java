package com.freedom.common.context;

import com.freedom.common.model.UserLoginInfo;

import java.util.Optional;

public class UserContext {


    private static final ThreadLocal<UserLoginInfo> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void set(UserLoginInfo user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static UserLoginInfo get() {
        return USER_THREAD_LOCAL.get();
    }

    public static boolean isLogin() {
        return Optional.ofNullable(USER_THREAD_LOCAL.get()).isPresent();
    }

    public static Long getUserId() {
        return USER_THREAD_LOCAL.get().getId();
    }

    public static Long getUserIdOrNull() {
        Optional<UserLoginInfo> optional = Optional.ofNullable(USER_THREAD_LOCAL.get());
        return optional.map(UserLoginInfo::getId).orElse(null);
    }

    public static Long getUserIdOrDefault() {
        Optional<UserLoginInfo> optional = Optional.ofNullable(USER_THREAD_LOCAL.get());
        return optional.isPresent() ? optional.get().getId() : 0L;
    }

    public static long getSysUserId() {
        return 1L;
    }

    public static void remove() {
        USER_THREAD_LOCAL.remove();
    }
}
