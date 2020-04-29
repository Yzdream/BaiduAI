package com.yz.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    /**
     * 获取当前系统时间
     *
     * @param format 时间格式 如：yyyy-MM-dd HH:mm:ss
     * @return time
     */
    public static String getNowDate(String format) {
        //默认日期格式
        String defaultFormat = "yyyy-MM-dd HH:mm:ss";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat(TextUtils.isEmpty(format) ? defaultFormat : format);
        // new Date()为获取当前系统时间
        return df.format(new Date());
    }

    /**
     * @param date 必须yyyy-MM-dd
     * @return 星期几  如：星期一
     */
    public static String getDayOfWeek(String date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdw = new SimpleDateFormat("E");
        Date d = null;
        try {
            d = sd.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdw.format(d);
    }

    /**
     * 获取前后分钟时间
     *
     * @param time     0800 当前时间
     * @param minutes  60 分钟
     * @param isBefore 之前还是之后
     * @return 0900
     */
    public static String getBeforeAfterTime(String time, int minutes, boolean isBefore) {
        Date date = new Date();
        String hour = time.substring(0, time.length() - 2);
        String minute = time.substring(time.length() - 2, time.length());
        date.setHours(Integer.valueOf(hour));
        date.setMinutes(Integer.valueOf(minute));
        date.setSeconds(0);

        Date before = new Date(date.getTime() - minutes * 60 * 1000);
        Date after = new Date(date.getTime() + minutes * 60 * 1000);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String beforeHour = formatter.format(before);
        String afterHour = formatter.format(after);

        return isBefore ? beforeHour : afterHour;
    }

    /**
     * 时间间隔是否在 N分钟之内
     *
     * @param time    时间  yyyyMMddHHmm
     * @param minutes 分钟
     * @return true or false
     */
    public static boolean isUnderMin(String time, int minutes) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA);//设置日期格式
        String currentTime = df.format(new Date());
        try {
            Date startDate = df.parse(time);
            Date currentDate = df.parse(currentTime);
            Long diff = startDate.getTime() - currentDate.getTime();
            long minute = diff / (1000 * 60);
            if (minutes >= minute) return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * @param str       需要加0的时间
     * @param strLength 长度
     * @return 加0后
     */
    public static String addZeroLeft(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuilder sb = new StringBuilder();
                sb.append("0").append(str);// 左补0
                // sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }

    /**
     * 时间转换
     *
     * @param s 时间
     * @return 转换后的时间
     */
    public static String convertStringTime(String s) {
        if (TextUtils.isEmpty(s)) return "";
        if (s.length() == 5 && s.contains(":")) return s;
        if (!s.contains(":") && s.length() == 4) {
            String s1 = s.substring(s.length() - 2);
            return s.substring(0, s.length() - 2) + ":" + s1;
        }
        if (!s.contains(":") && s.length() < 4) {
            s = addZeroLeft(s, 4);
            if (s.length() == 4) {
                String s1 = s.substring(s.length() - 2);
                return s.substring(0, s.length() - 2) + ":" + s1;
            }
        }
        return s;
    }

    /**
     * //2018-9-1  ===> 2018-09-01
     *
     * @param date 转换时间
     * @return 转换后的时间
     */
    @SuppressLint("SimpleDateFormat")
    public static String dateFormat(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(date);
            return  sdf1.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 某年某月某日在这一年这个月是第几周
     * @param time 日期 yyyy-MM-dd
     * @return 1 周一  2 周二 以次类推
     */
    public static int dayOfWeekOnMonth(String time,boolean isDayOrWeek) {
        int week = 0;
        int day = 0;
        if (TextUtils.isEmpty(time))
            return week;
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            //第几周
            week = calendar.get(Calendar.WEEK_OF_MONTH);
            //第几天，从周日开始
             day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        }catch (Exception e){
            e.printStackTrace();
            return isDayOrWeek ? day:week;
        }
        return isDayOrWeek ? day:week;
    }

    /**
     * 日期转换  yyyy-MM-dd ==> yyyy-MM
     * @param time 日期  yyyy-MM-dd
     * @return yyyy-MM
     */
    @SuppressLint("SimpleDateFormat")
    public static String getYearAndMonth(String time){
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        try {
            Date d = sdf.parse(time);
            return  sdf1.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateFormat(String time,String format){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat sdf1 = new SimpleDateFormat(format);
        try {
            Date d = sdf.parse(time);
            return  sdf1.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 时间格式化
     * @param time  时间
     * @param format  格式
     * @return 格式化的时间  输入时间要与格式统一  2018-1-1 ==》 yyyy-MM-dd
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateToFormat(String time,String format){
        if (TextUtils.isEmpty(time))
            return "";
        SimpleDateFormat sdf1 = new SimpleDateFormat(format);
        try {
            Date d = sdf1.parse(time);
            return  sdf1.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 判断某年某月第一天是周几
     *
     * @param year  年
     * @param month 月
     * @return 1 周一 2 周二  以此类推
     */
    public static int oneDayOfweekOnYearAndMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 某年某月有多少天
     *
     * @return 天数
     */
    public static int daysOfYearAndMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0);
        return c.get(Calendar.DAY_OF_MONTH);
    }
}
