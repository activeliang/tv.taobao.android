package com.taobao.statistic.core;

public class Device {
    private String imei = "";
    private String imsi = "";
    private String udid = "";

    public void setUdid(String udid2) {
        this.udid = udid2;
    }

    public void setImei(String imei2) {
        this.imei = imei2;
    }

    public void setImsi(String imsi2) {
        this.imsi = imsi2;
    }

    public String getUdid() {
        return this.udid;
    }

    public String getImei() {
        return this.imei;
    }

    public String getImsi() {
        return this.imsi;
    }
}
