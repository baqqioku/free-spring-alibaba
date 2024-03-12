package com.freedom.framework.redis.consts;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Time {
    public final int SECOND = 1;

    public final int MINUTE = 60;
    public final int HOUR = 60 * MINUTE;
    public final int DAY = 24 * HOUR;
    public final int MONTH = 30 * DAY;
    public final int YEAR = 12 * MONTH;
    /**
     * 永不过期
     */
    public final int FOREVER = 0;
}
