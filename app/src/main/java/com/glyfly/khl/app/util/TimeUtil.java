package com.glyfly.khl.app.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by gly on 2017/8/24.
 */

public class TimeUtil {

    private static Calendar calendar;
    private static TimeUtil timeUtil;

    private TimeUtil() {
    }

    public static TimeUtil getInstance() {
        calendar = Calendar.getInstance();
        return timeUtil == null ? new TimeUtil() : timeUtil;
    }

    public String getDetailTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String time = format.format(new Date());
        return time;
    }

    public String getChineseDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String time = format.format(new Date());
        return time;
    }

    public String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(new Date());
        return time;
    }

    public String getDelayedDay(int delay) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = new Date(new Date().getTime() + delay*24*60*60*1000);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
        }
        String time = format.format(date);
        return time;
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time = format.format(new Date());
        return time;
    }

    /*获取星期几*/
    public String getWeek() {
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }

    /**
     * 获取某一时间当天的0点时间戳
     * @param now 某一时间点
     * @param timeZone 时区
     */
    public static long getStartTimeOfDay(long now, String timeZone) {
        String tz = TextUtils.isEmpty(timeZone) ? "GMT+8" : timeZone;
        TimeZone curTimeZone = TimeZone.getTimeZone(tz);
        Calendar calendar = Calendar.getInstance(curTimeZone);
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取某一时间当天的24点时间戳
     * @param now 某一时间点
     * @param timeZone 时区
     */
    public static long getEndTimeOfDay(long now, String timeZone) {
        String tz = TextUtils.isEmpty(timeZone) ? "GMT+8" : timeZone;
        TimeZone curTimeZone = TimeZone.getTimeZone(tz);
        Calendar calendar = Calendar.getInstance(curTimeZone);
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}
