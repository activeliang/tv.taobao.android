package com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo;

import anetwork.channel.util.RequestConstant;
import org.json.JSONException;
import org.json.JSONObject;

public class OptionExtend extends BaseMO {
    private static final long serialVersionUID = -3149187785595709744L;
    private String brandLogoUrl;
    private Double lowestDiscount;
    private String online;
    private String onlineEndTime;
    private String onlineStartTime;
    private Integer soldCount;
    private String wlBannerImgUrl;
    private String wlBrandDesc;

    public static OptionExtend resolveFromJson(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        OptionExtend item = new OptionExtend();
        item.setLowestDiscount(Double.valueOf(obj.optDouble("lowestDiscount")));
        item.setBrandLogoUrl(obj.optString("brandLogoUrl"));
        item.setOnlineStartTime(obj.optString("onlineStartTime"));
        item.setOnlineEndTime(obj.optString("onlineEndTime"));
        item.setWlBannerImgUrl(obj.optString("wlBannerImgUrl"));
        item.setWlBrandDesc(obj.optString("wlBrandDesc"));
        item.setSoldCount(Integer.valueOf(obj.optInt("soldCount")));
        item.setOnline(obj.optString(RequestConstant.ENV_ONLINE));
        return item;
    }

    public String toString() {
        return "OptionExtend [lowestDiscount=" + this.lowestDiscount + ", brandLogoUrl=" + this.brandLogoUrl + ", onlineStartTime=" + this.onlineStartTime + ", onlineEndTime=" + this.onlineEndTime + ", wlBannerImgUrl=" + this.wlBannerImgUrl + ", wlBrandDesc=" + this.wlBrandDesc + ", soldCount=" + this.soldCount + ", online=" + this.online + "]";
    }

    public Double getLowestDiscount() {
        return this.lowestDiscount;
    }

    public void setLowestDiscount(Double lowestDiscount2) {
        this.lowestDiscount = lowestDiscount2;
    }

    public String getBrandLogoUrl() {
        return this.brandLogoUrl;
    }

    public void setBrandLogoUrl(String brandLogoUrl2) {
        this.brandLogoUrl = brandLogoUrl2;
    }

    public String getOnlineStartTime() {
        return this.onlineStartTime;
    }

    public void setOnlineStartTime(String onlineStartTime2) {
        this.onlineStartTime = onlineStartTime2;
    }

    public String getOnlineEndTime() {
        return this.onlineEndTime;
    }

    public void setOnlineEndTime(String onlineEndTime2) {
        this.onlineEndTime = onlineEndTime2;
    }

    public String getWlBannerImgUrl() {
        return this.wlBannerImgUrl;
    }

    public void setWlBannerImgUrl(String wlBannerImgUrl2) {
        this.wlBannerImgUrl = wlBannerImgUrl2;
    }

    public String getWlBrandDesc() {
        return this.wlBrandDesc;
    }

    public void setWlBrandDesc(String wlBrandDesc2) {
        this.wlBrandDesc = wlBrandDesc2;
    }

    public Integer getSoldCount() {
        return this.soldCount;
    }

    public void setSoldCount(Integer soldCount2) {
        this.soldCount = soldCount2;
    }

    public String getOnline() {
        return this.online;
    }

    public void setOnline(String online2) {
        this.online = online2;
    }
}
