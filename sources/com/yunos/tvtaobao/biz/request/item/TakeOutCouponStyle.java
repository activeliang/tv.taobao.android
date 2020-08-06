package com.yunos.tvtaobao.biz.request.item;

public class TakeOutCouponStyle {
    private String bgColor;
    private String bgPic;
    private String buttonPic;
    private String buttonUrl;
    private String couponFlag;
    private String safeCode;

    public String getBgPic() {
        return this.bgPic;
    }

    public void setBgPic(String bgPic2) {
        this.bgPic = bgPic2;
    }

    public String getBgColor() {
        return this.bgColor;
    }

    public void setBgColor(String bgColor2) {
        this.bgColor = bgColor2;
    }

    public String getButtonPic() {
        return this.buttonPic;
    }

    public void setButtonPic(String buttonPic2) {
        this.buttonPic = buttonPic2;
    }

    public String getButtonUrl() {
        return this.buttonUrl;
    }

    public void setButtonUrl(String buttonUrl2) {
        this.buttonUrl = buttonUrl2;
    }

    public String getCouponFlag() {
        return this.couponFlag;
    }

    public void setCouponFlag(String couponFlag2) {
        this.couponFlag = couponFlag2;
    }

    public String getSafeCode() {
        return this.safeCode;
    }

    public void setSafeCode(String safeCode2) {
        this.safeCode = safeCode2;
    }
}
