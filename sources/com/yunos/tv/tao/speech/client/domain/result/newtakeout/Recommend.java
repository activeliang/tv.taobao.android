package com.yunos.tv.tao.speech.client.domain.result.newtakeout;

public class Recommend {
    private String isAd;
    private String noneAdWithTrackableReason;
    private String reason;

    public void setReason(String reason2) {
        this.reason = reason2;
    }

    public String getReason() {
        return this.reason;
    }

    public void setNoneAdWithTrackableReason(String noneAdWithTrackableReason2) {
        this.noneAdWithTrackableReason = noneAdWithTrackableReason2;
    }

    public String getNoneAdWithTrackableReason() {
        return this.noneAdWithTrackableReason;
    }

    public void setIsAd(String isAd2) {
        this.isAd = isAd2;
    }

    public String getIsAd() {
        return this.isAd;
    }
}
