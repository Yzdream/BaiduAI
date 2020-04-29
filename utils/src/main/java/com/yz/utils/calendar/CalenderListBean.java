package com.yz.utils.calendar;

import java.util.List;

public class CalenderListBean {

    private String date;

    private List<CalenderBean> calenderBeans;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<CalenderBean> getCalenderBeans() {
        return calenderBeans;
    }

    public void setCalenderBeans(List<CalenderBean> calenderBeans) {
        this.calenderBeans = calenderBeans;
    }
}
