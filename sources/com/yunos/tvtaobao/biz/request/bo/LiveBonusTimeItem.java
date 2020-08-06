package com.yunos.tvtaobao.biz.request.bo;

public class LiveBonusTimeItem {
    private String drawBeginAt;
    private String drawEndAt;
    private String id;
    private String ruleType;
    private String safeCode;
    private String showAt;
    private String timeType;

    public void setDrawBeginAt(String drawBeginAt2) {
        this.drawBeginAt = drawBeginAt2;
    }

    public String getDrawBeginAt() {
        return this.drawBeginAt;
    }

    public void setDrawEndAt(String drawEndAt2) {
        this.drawEndAt = drawEndAt2;
    }

    public String getDrawEndAt() {
        return this.drawEndAt;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getId() {
        return this.id;
    }

    public void setRuleType(String ruleType2) {
        this.ruleType = ruleType2;
    }

    public String getRuleType() {
        return this.ruleType;
    }

    public void setSafeCode(String safeCode2) {
        this.safeCode = safeCode2;
    }

    public String getSafeCode() {
        return this.safeCode;
    }

    public void setShowAt(String showAt2) {
        this.showAt = showAt2;
    }

    public String getShowAt() {
        return this.showAt;
    }

    public void setTimeType(String timeType2) {
        this.timeType = timeType2;
    }

    public String getTimeType() {
        return this.timeType;
    }
}
