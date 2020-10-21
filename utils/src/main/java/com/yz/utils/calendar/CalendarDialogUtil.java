package com.yz.utils.calendar;


import com.yz.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CalendarDialogUtil {

    //当前月
    public static int nowYear = 2018;
    public static int nowMonth = 1;//当前月份
    public int day;//当前日

    public static String chooseDay;

    public List<String> chooseDays;

    private onNowDate nowDate;

    private List<CalenderListBean> data = new ArrayList<>();

//    private List<CalenderBean> list = new ArrayList<>();

    /**
     * 初始化
     *
     * @param time 当前时间 格式 yyyy-MM-dd
     */
    public CalendarDialogUtil(String time) {
        String[] times = time.split("-");
        if (times.length != 3) return;
        //当前年月赋值
        nowYear = Integer.parseInt(times[0]);
        nowMonth = Integer.parseInt(times[1]);
        day = Integer.parseInt(times[2]);
        chooseDay = nowYear + "-" + nowMonth + "-" + day;
    }

    public List<CalenderBean> initData() {
        int empty = oneDayOfweekOnYearAndMonth();
        List<CalenderBean> list = new ArrayList<>();
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
            data.setChoose(false);
            data.setClick(false);
           /* if (nowYear>= Integer.parseInt(TimeUtils.getNowDate("yyyy"))&&nowMonth>=Integer.parseInt(TimeUtils.getNowDate("MM")) &&(i - empty )>Integer.parseInt(TimeUtils.getNowDate("dd"))){
                data.setClick(false);//点击事件由后台数据决定
            }else{
                data.setClick(false);
           }*/

            if ((String.valueOf(nowYear) + "-" + String.valueOf(nowMonth) + "-" + (String.valueOf(i - empty).length() == 1 ? "0" + String.valueOf(i - empty) : i - empty)).equals(TimeUtils.getNowDate("yyyy-M-d"))) {
                data.setToday(true);
            } else {
                data.setToday(false);
                data.setChoose(false);
            }
//            if (chooseDay.equals((String.valueOf(nowYear) + "-" + String.valueOf(nowMonth) + "-" +  (i - empty))))
//                data.setChoose(true);
            list.add(data);
        }
        return list;
    }

    public void saveDate(List<CalenderBean> date) {
        //存储所有请求过的日期
        CalenderListBean calenderListBean = new CalenderListBean();
        calenderListBean.setCalenderBeans(date);
        calenderListBean.setDate(String.valueOf(nowYear) + "-" + String.valueOf(nowMonth));

        boolean isExist = false;
//        //如果请求的日期已经存在就替换
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getDate().equals(String.valueOf(nowYear) + "-" + String.valueOf(nowMonth))) {
                data.set(i, calenderListBean);
                isExist = true;
            }
        }
        if (!isExist) data.add(calenderListBean);
        if (nowDate != null) nowDate.onNowDate(String.valueOf(nowYear), String.valueOf(nowMonth));
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
        if (nowDate != null) nowDate.onNowDate(String.valueOf(nowYear), String.valueOf(nowMonth));
        //如果已经请求过的数据就不要重复请求
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getDate().equals(String.valueOf(nowYear) + "-" + String.valueOf(nowMonth))) {
                return data.get(i).getCalenderBeans();
            }
        }
        return initData();
    }

    public static String getUpMonth() {
        String year;
        String month;
        if (nowMonth == 1) {
            year = String.valueOf(nowYear - 1);
            month = String.valueOf(12);
        } else {
            year = String.valueOf(nowYear);
            month = String.valueOf(nowMonth - 1);
        }
        return year + "-" + (month.length() > 1 ? month : 0 + month);
    }

    public static String getNextMonth() {
        String year;
        String month;
        if (nowMonth == 12) {
            year = String.valueOf(nowYear + 1);
            month = String.valueOf(1);
        } else {
            year = String.valueOf(nowYear);
            month = String.valueOf(nowMonth + 1);
        }
        return year + "-" + (month.length() > 1 ? month : 0 + month);
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
        if (nowDate != null) nowDate.onNowDate(String.valueOf(nowYear), String.valueOf(nowMonth));
        //如果已经请求过的数据就不要重复请求
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getDate().equals(String.valueOf(nowYear) + "-" + String.valueOf(nowMonth))) {
                return data.get(i).getCalenderBeans();
            }
        }
        return initData();
    }

//    //单选 每次滑动请求数据
//    public List<CalenderBean> getOneDay(String day) {
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getDate().equals(day)) {
//                list.get(i).setChoose(true);
//                chooseDay = String.valueOf(nowYear) + "-" + String.valueOf(nowMonth) + "-" +list.get(i).getDate();
//            } else list.get(i).setChoose(false);
//        }
//        return list;
//    }

    //单选  每次滑动如果请求过数据就不重新请求
    public List<CalenderBean> getOneDay(String day) {
        int nowPosition = 0;
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).getCalenderBeans().size(); j++) {
                if (data.get(i).getDate().equals(String.valueOf(nowYear) + "-" + String.valueOf(nowMonth)) && data.get(i).getCalenderBeans().get(j).getDate().equals(day)) {
                    nowPosition = i;
                    if (data.get(i).getCalenderBeans().get(j).isChoose())
                        data.get(i).getCalenderBeans().get(j).setChoose(false);
                    else data.get(i).getCalenderBeans().get(j).setChoose(true);
//                    chooseDay = String.valueOf(nowYear) + "-" + String.valueOf(nowMonth) + "-" + data.get(i).getCalenderBeans().get(j).getDate();
                }
            }
        }
        return data.get(nowPosition).getCalenderBeans();
    }
/*

    public List<Map> chooseDays() {
        List<Map> dateList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).getCalenderBeans().size(); j++) {
                if (data.get(i).getCalenderBeans().get(j).isChoose()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("saleDate", TimeUtils.dateFormat(data.get(i).getDate() + "-" + data.get(i).getCalenderBeans().get(j).getDate()));
                    dateList.add(map);
                }
            }
        }
        return dateList;
    }
*/

    public List<String> chooseDays() {
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).getCalenderBeans().size(); j++) {
                if (data.get(i).getCalenderBeans().get(j).isChoose()) {
                    dateList.add(TimeUtils.dateFormat(data.get(i).getDate() + "-" + data.get(i).getCalenderBeans().get(j).getDate()));
                }
            }
        }
        return dateList;
    }

    public String getYearAndMonth() {
        return nowYear + "-" + nowMonth;
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

    public void setNowDateListen(onNowDate nowDate) {
        this.nowDate = nowDate;
    }

    public interface onNowDate {
        void onNowDate(String year, String month);
    }

}
