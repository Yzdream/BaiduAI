package com.yz.utils.calendar;

import android.text.TextUtils;

import com.yz.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import static com.yz.utils.TimeUtils.daysOfYearAndMonth;

public class CalendarUtils {

    //yyyy-MM-dd
    private String chooseDate = null;
    //显示的年
    private Integer nowYear;
    //显示的月
    private Integer nowMonth;

    private List<CalenderBean> calenderlist = new ArrayList<>();

    /**
     * 设置默认选中日期
     *
     * @param date yyyy-MM-dd
     */
    public void setChooseDate(String date) {
        chooseDate = date;
        if (!TextUtils.isEmpty(chooseDate)) {
            nowYear = Integer.parseInt(chooseDate.split("-")[0]);
            nowMonth = Integer.parseInt(chooseDate.split("-")[1]);
        }
    }

    public List<CalenderBean> initData() {
        if (nowYear == null || nowMonth == null)
            return new ArrayList<>();
        //一个月第一天之前的空格数
        int empty = TimeUtils.oneDayOfweekOnYearAndMonth(nowYear, nowMonth);
        List<CalenderBean> list = new ArrayList<>();
        for (int i = 1; i <= daysOfYearAndMonth(nowYear, nowMonth) + empty; i++) {
            CalenderBean data = new CalenderBean();
            //补前面的空日期
            if (i <= empty) {
                data.setDate("");
                data.setClick(false);
                data.setChoose(false);
                list.add(data);
                continue;
            }

            data.setDate(TimeUtils.getDateToFormat(nowYear + "-" + nowMonth + "-" + String.valueOf(i - empty), "yyyy-MM-dd"));
            data.setClick(true);
            data.setChoose(false);

            //今天
            if (data.getDate().equals(TimeUtils.getNowDate("yyyy-MM-dd"))) {
                data.setToday(true);
            } else {
                data.setToday(false);
            }

            //默认选中的日期
            if (!TextUtils.isEmpty(chooseDate) && chooseDate.equals(data.getDate())) {
                data.setChoose(true);
            }
            list.add(data);
        }
        calenderlist = list;
        return calenderlist;
    }

    /**
     * 单选 日期
     *
     * @param date yyyyMMdd
     * @return list集合
     */
    public List<CalenderBean> getOneDay(String date) {
        for (int i = 0; i < calenderlist.size(); i++) {
            if (calenderlist.get(i).getDate().equals(date)) {
                calenderlist.get(i).setChoose(true);
                chooseDate = calenderlist.get(i).getDate();
            } else {
                calenderlist.get(i).setChoose(false);
            }
        }
        return calenderlist;
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
        return initData();
    }

    public String getNextMonth() {
        if (nowMonth == 12) {
            nowYear += 1;
            nowMonth = 1;
            return TimeUtils.getDateToFormat((nowYear + 1) + "-" + 1 + "-" + 1, "yyyy-MM-dd");
        } else {
            return TimeUtils.getDateToFormat(nowYear + "-" + (nowMonth + 1) + "-" + 1, "yyyy-MM-dd");
        }
    }

    public String getUpMonth() {
        if (nowMonth == 1) {
            return TimeUtils.getDateToFormat((nowYear - 1) + "-" + "12" + "-" + "1", "yyyy-MM-dd");
        } else {
            return TimeUtils.getDateToFormat(nowYear + "-" + (nowMonth - 1) + "-" + "1", "yyyy-MM-dd");
        }
    }


    public String getChooseYear() {
        return TextUtils.isEmpty(chooseDate) ? "" : chooseDate.split("-")[0];
    }

    public String getChooseMonth() {
        return TextUtils.isEmpty(chooseDate) ? "" : chooseDate.split("-")[1];
    }

    public String getChooseDay() {
        return TextUtils.isEmpty(chooseDate) ? "" : chooseDate.split("-")[2];
    }

    public String getChooseDate() {
        return chooseDate;
    }

    public String getNowYear() {
        return String.valueOf(nowYear);
    }

    public String getNowMonth() {
        return String.valueOf(nowMonth);
    }

}
