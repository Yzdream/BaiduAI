package com.yz.utils.calendar;


import com.yz.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CalendarUtil {

    //当前月
    public int nowYear = 2018;
    public int nowMonth = 1;//当前月份
    public int day;//当前日

    public static String chooseDay;

    private onNowDate nowDate;

    private List<CalenderListBean> data = new ArrayList<>();

    private List<CalenderBean> list = new ArrayList<>();

    /**
     * 初始化
     *
     * @param time 当前时间 格式 yyyy-MM-dd
     */
    public CalendarUtil(String time) {
        String[] times = time.split("-");
        if (times.length != 3) return;
        //当前年月赋值
        nowYear = Integer.parseInt(times[0]);
        nowMonth = Integer.parseInt(times[1]);
        day = Integer.parseInt(times[2]);
        chooseDay = TimeUtils.dateFormat(nowYear + "-" + nowMonth + "-" + day);
    }

    public List<CalenderBean> initData() {
        int empty = oneDayOfweekOnYearAndMonth();
        list.clear();
        for (int i = 1; i <= daysOfYearAndMonth() + empty; i++) {
            CalenderBean data = new CalenderBean();

            data.setStatus("");
            //加入一号之前的留白
            if (i <= empty) {
                data.setDate("");
                data.setClick(false);
                data.setChoose(false);
                list.add(data);
                continue;
            }

            data.setDate(String.valueOf(i - empty));
            data.setClick(true);
            data.setChoose(false);

            if (TimeUtils.dateFormat(String.valueOf(nowYear) + "-" + String.valueOf(nowMonth) + "-" + (i - empty)).equals(TimeUtils.getNowDate("yyyy-MM-dd"))) {
                data.setToday(true);
            } else {
                data.setToday(false);
                data.setChoose(false);
            }
            if (chooseDay.equals(TimeUtils.dateFormat(String.valueOf(nowYear) + "-" + String.valueOf(nowMonth) + "-" + (i - empty))))
                data.setChoose(true);
            list.add(data);
        }

        //最后一天周几，空格要加到周六  保证横向翻页正常
        int emptyEnd = dayOfweekOnYearAndMonth(daysOfYearAndMonth()) == 7 ? 6 : 6 - dayOfweekOnYearAndMonth(daysOfYearAndMonth());
        for (int j = 0; j < emptyEnd; j++) {
            CalenderBean data = new CalenderBean();
            data.setStatus("");
            data.setDate("");
            data.setClick(false);
            data.setChoose(false);
            list.add(data);
        }
//        //存储所有请求过的日期
//        CalenderListBean calenderListBean = new CalenderListBean();
//        calenderListBean.setCalenderBeans(list);
//        calenderListBean.setDate(String.valueOf(nowYear) + "-" + String.valueOf(nowMonth));
//
//        //如果请求的日期已经存在就替换
//        for (int i = 0; i < data.size(); i++) {
//            if (data.get(i).getDate().equals(String.valueOf(nowYear) + "-" + String.valueOf(nowMonth))) {
//                data.set(i, calenderListBean);
//            }
//        }
//
//        data.add(calenderListBean);
        if (nowDate != null) nowDate.onNowDate(String.valueOf(nowYear), String.valueOf(nowMonth));
        return list;
    }

    /**
     * 下一个月
     */
    public List<CalenderBean> nextMonth() {
        if (nowMonth == 12) {
            nowYear += 1;
            nowMonth = 1;
        } else {
            nowMonth += 1;
        }
//        //如果已经请求过的数据就不要重复请求
//        for (int i = 0; i < data.size(); i++) {
//            if (data.get(i).getDate().equals(String.valueOf(nowYear) + "-" + String.valueOf(nowMonth))) {
//                return data.get(i).getCalenderBeans();
//            }
//        }
        return initData();
    }

    /**
     * 上一个月
     */
    public List<CalenderBean> upMonth() {
        if (nowMonth == 1) {
            nowYear -= 1;
            nowMonth = 12;
        } else {
            nowMonth -= 1;
        }
//        //如果已经请求过的数据就不要重复请求
//        for (int i = 0; i < data.size(); i++) {
//            if (data.get(i).getDate().equals(String.valueOf(nowYear) + "-" + String.valueOf(nowMonth))) {
//                return data.get(i).getCalenderBeans();
//            }
//        }
        return initData();
    }

    //单选 每次滑动请求数据
    public List<CalenderBean> getOneDay(String day) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDate().equals(day)) {
                list.get(i).setChoose(true);
                chooseDay = TimeUtils.dateFormat(nowYear + "-" + nowMonth + "-" + list.get(i).getDate());
            } else list.get(i).setChoose(false);
        }
        return list;
    }


//    //单选  每次滑动如果请求过数据就不重新请求
//    public List<CalenderBean> getOneDay(String day) {
//        int nowPosition = 0;
//        for (int i = 0; i < data.size(); i++) {
//            for (int j = 0; j < data.get(i).getCalenderBeans().size(); j++) {
//                if (data.get(i).getDate().equals(String.valueOf(nowYear) + "-" + String.valueOf(nowMonth)) && data.get(i).getCalenderBeans().get(j).getDate().equals(day)) {
//                    nowPosition = i;
//                    data.get(i).getCalenderBeans().get(j).setChoose(true);
//                    chooseDay = String.valueOf(nowYear) + "-" + String.valueOf(nowMonth) + "-" + data.get(i).getCalenderBeans().get(j).getDate();
//                } else data.get(i).getCalenderBeans().get(j).setChoose(false);
//            }
//        }
//        return data.get(nowPosition).getCalenderBeans();
//    }

    public String getNowDate() {
        return TimeUtils.dateFormat(nowYear + String.valueOf(nowMonth));
    }

    /**
     * 某年某月有多少天
     *
     * @return 天数
     */
    public int daysOfYearAndMonth() {
        Calendar c = Calendar.getInstance();
        c.set(nowYear, nowMonth, 0);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int daysOfYearAndMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 判断某年某月第一天是周几
     *
     * @return 1 周一 2 周二  以此类推
     */
    public int oneDayOfweekOnYearAndMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(nowYear, nowMonth - 1, 1);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public int dayOfweekOnYearAndMonth(int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(nowYear, nowMonth - 1, day);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public void setNowDateListen(onNowDate nowDate) {
        this.nowDate = nowDate;
    }

    public interface onNowDate {
        void onNowDate(String year, String month);
    }

}
