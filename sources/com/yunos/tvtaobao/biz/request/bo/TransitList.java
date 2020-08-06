package com.yunos.tvtaobao.biz.request.bo;

public class TransitList {
    private String action;
    private String message;
    private String status;
    private String statusDesc;
    private String statusIcon;
    private String time;

    public void setAction(String action2) {
        this.action = action2;
    }

    public String getAction() {
        return this.action;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatusDesc(String statusDesc2) {
        this.statusDesc = statusDesc2;
    }

    public String getStatusDesc() {
        return this.statusDesc;
    }

    public void setStatusIcon(String statusIcon2) {
        this.statusIcon = statusIcon2;
    }

    public String getStatusIcon() {
        return this.statusIcon;
    }

    public void setTime(String time2) {
        this.time = time2;
    }

    public String getTime() {
        return this.time;
    }
}
