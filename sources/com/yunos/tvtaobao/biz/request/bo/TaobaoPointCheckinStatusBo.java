package com.yunos.tvtaobao.biz.request.bo;

public class TaobaoPointCheckinStatusBo {
    private String checked;
    private String days;
    private String todayPoints;
    private String tomorrowPoints;

    public void setChecked(String checked2) {
        this.checked = checked2;
    }

    public String getChecked() {
        return this.checked;
    }

    public void setDays(String days2) {
        this.days = days2;
    }

    public String getDays() {
        return this.days;
    }

    public void setTomorrowPoints(String tomorrowPoints2) {
        this.tomorrowPoints = tomorrowPoints2;
    }

    public String getTomorrowPoints() {
        return this.tomorrowPoints;
    }

    public void setTodayPoints(String todayPoints2) {
        this.todayPoints = todayPoints2;
    }

    public String getTodayPoints() {
        return this.todayPoints;
    }
}
