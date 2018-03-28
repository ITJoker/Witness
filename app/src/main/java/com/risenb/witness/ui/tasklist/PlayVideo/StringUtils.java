package com.risenb.witness.ui.tasklist.PlayVideo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {
    public static int hour = 1000 * 60 * 60;
    public static int minute = 1000 * 60;
    public static int second = 1000;

    public static String formatMediaTime(int millsec) {
        // "hh:mm:ss"
        // "mm:ss"
        int h = millsec / hour;
        int m = millsec % hour / minute;
        int sec = millsec % minute / second;

        if (h > 0) {
            // "hh:mm:ss",%02d是占位符,若不使用则为"1:36:2"
            return String.format("%02d:%02d:%02d", h, m, sec);
        } else {
            return String.format("%02d:%02d", m, sec);
        }
    }

    /**
     * 获取当前系统时间,以格式"HH:mm:ss"返回时间值
     */
    public static String formatSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }
}
