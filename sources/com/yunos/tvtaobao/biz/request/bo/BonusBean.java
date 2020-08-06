package com.yunos.tvtaobao.biz.request.bo;

public class BonusBean {
    private String awardDisEffectDate;
    private String awardEffectDate;
    private String awardFace;
    private String awardType;
    private String retCode;

    public String getRetCode() {
        return this.retCode;
    }

    public void setRetCode(String retCode2) {
        this.retCode = retCode2;
    }

    public String getAwardType() {
        return this.awardType;
    }

    public void setAwardType(String awardType2) {
        this.awardType = awardType2;
    }

    public String getAwardFace() {
        return this.awardFace;
    }

    public void setAwardFace(String awardFace2) {
        this.awardFace = awardFace2;
    }

    public String getAwardEffectDate() {
        return this.awardEffectDate;
    }

    public void setAwardEffectDate(String awardEffectDate2) {
        this.awardEffectDate = awardEffectDate2;
    }

    public String getAwardDisEffectDate() {
        return this.awardDisEffectDate;
    }

    public void setAwardDisEffectDate(String awardDisEffectDate2) {
        this.awardDisEffectDate = awardDisEffectDate2;
    }
}
