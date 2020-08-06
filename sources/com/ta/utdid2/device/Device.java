package com.ta.utdid2.device;

public class Device {
    private String deviceId = "";
    private String imei = "";
    private String imsi = "";
    private long mCheckSum = 0;
    private long mCreateTimestamp = 0;
    private String utdid = "";

    /* access modifiers changed from: package-private */
    public long getCheckSum() {
        return this.mCheckSum;
    }

    /* access modifiers changed from: package-private */
    public void setCheckSum(long mCheckSum2) {
        this.mCheckSum = mCheckSum2;
    }

    /* access modifiers changed from: package-private */
    public long getCreateTimestamp() {
        return this.mCreateTimestamp;
    }

    /* access modifiers changed from: package-private */
    public void setCreateTimestamp(long mCreateTimestamp2) {
        this.mCreateTimestamp = mCreateTimestamp2;
    }

    public String getImei() {
        return this.imei;
    }

    /* access modifiers changed from: package-private */
    public void setImei(String imei2) {
        this.imei = imei2;
    }

    public String getImsi() {
        return this.imsi;
    }

    /* access modifiers changed from: package-private */
    public void setImsi(String imsi2) {
        this.imsi = imsi2;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    /* access modifiers changed from: package-private */
    public void setDeviceId(String deviceId2) {
        this.deviceId = deviceId2;
    }

    public String getUtdid() {
        return this.utdid;
    }

    /* access modifiers changed from: package-private */
    public void setUtdid(String utdid2) {
        this.utdid = utdid2;
    }
}
