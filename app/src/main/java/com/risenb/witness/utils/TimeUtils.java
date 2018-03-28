package com.risenb.witness.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.net.ParseException;

public class TimeUtils {

    /**
     * 一分钟
     */
    public static final int minute = 1000 * 60;

    /**
     * 一小时
     */
    public static final int hour = minute * 60;

    /**
     * 一天
     */
    public static final int day = hour * 24;

    /**
     * 一星期
     */
    public static final int week = day * 7;

    /**
     * 10天
     */
    public static final int tenDay = day * 10;

    public static String timeDifference(String timeString) {
//        long time = Long.valueOf(stringTodata(timeString));
        long time = Long.valueOf(timeString);
        long now = System.currentTimeMillis();
        long differenceTime = time - now;
        String switchTime = "";
        int dd, hh, mm, ss;
        if (differenceTime <= minute) {
            // 时间<1分钟
            dd = 00;
            hh = 00;
            mm = 00;
            ss = (int) differenceTime / 1000;
        } else if (differenceTime > minute && differenceTime <= hour) {
            // 时间<1小时
            dd = 00;
            hh = 00;
            mm = (int) (differenceTime / minute);
            ss = (int) (differenceTime % minute / 1000);
        } else if (differenceTime > hour && differenceTime <= day) {
            // 时间< 1天
            dd = 00;
            hh = (int) (differenceTime / hour);
            mm = (int) (differenceTime % hour / minute);
            ss = (int) (differenceTime % hour % minute / 1000);
        } else {
            // 时间 < 10 天
            dd = (int) (differenceTime / day);
            hh = (int) (differenceTime % day / hour);
            mm = (int) (differenceTime % day % hour / minute);
            ss = (int) (differenceTime % day % hour % minute / 1000);
        }
        hh += dd * 24;
        switchTime = String.format("%02d", dd) + "," + String.format("%02d", hh) + "," + String.format("%02d", mm) + "," + String.format("%02d", ss);
        return switchTime;
    }

    /**
     * 掉此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     */
    public static String stringTodata(String time) {
        if (time.length() == 15) {
            time = time.substring(0, 8) + "0" + time.substring(8, time.length());
        }
        SimpleDateFormat sdr;
        if (time.length() == 10) {
            sdr = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        } else if (time.length() == 16) {
            sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        } else {
            sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        }
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 13);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String toFormat(long time) {
        StringBuffer sb = new StringBuffer();
        int hour;
        int minute;
        int second;
        second = (int) (time % 60);
        minute = (int) ((time / 60) % 60);
        hour = (int) (time / 3600);
        if (hour != 0) {
            sb.append(hour);
            sb.append("°");
        }
        sb.append(minute);
        sb.append("'");
        sb.append(second);
        sb.append("''");
        return sb.toString();

    }

    public static String toTimeFormat(long time) {
        StringBuffer sb = new StringBuffer();
        int hour;
        int minute;
        int second;
        second = (int) (time % 60);
        minute = (int) ((time / 60) % 60);
        hour = (int) (time / 3600);
        if (hour != 0) {
            if (hour < 10) {
                sb.append("0");
            }
            sb.append(hour);
            sb.append(":");
        }
        if (minute < 10) {
            sb.append("0");
        }
        sb.append(minute);
        sb.append(":");
        if (second < 10) {
            sb.append("0");
        }
        sb.append(second);
        return sb.toString();

    }

}
