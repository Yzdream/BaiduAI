package com.yz.utils.calendar;

public class CalenderBean {

    private String date;

    private boolean isClick;

    private boolean isChoose;

    private boolean isToday;

    private String status;

    private String workType;

    private boolean isHaveNext;

    private boolean isHaveUp;

    public String getDate() {
        return date == null ? "" : date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkType() {
        return workType == null ? "" : workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public boolean isHaveNext() {
        return isHaveNext;
    }

    public void setHaveNext(boolean haveNext) {
        isHaveNext = haveNext;
    }

    public boolean isHaveUp() {
        return isHaveUp;
    }

    public void setHaveUp(boolean haveUp) {
        isHaveUp = haveUp;
    }
}
