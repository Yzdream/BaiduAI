package com.yz.utils.calendar;

public class CalenderStateBean {

    private String status;

    private String saleDate;

    private String scheduledStatus;

    private boolean isHaveNext;

    private boolean isHaveUp;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public String getScheduledStatus() {
        return scheduledStatus;
    }

    public void setScheduledStatus(String scheduledStatus) {
        this.scheduledStatus = scheduledStatus;
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
