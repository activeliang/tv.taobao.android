package com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo;

import anetwork.channel.util.RequestConstant;
import org.json.JSONException;
import org.json.JSONObject;

public class BrandMO extends BaseMO {
    private static final long serialVersionUID = -6777979610604470707L;
    private String code;
    private Integer itemCount;
    private String juBanner;
    private String juBrand_id;
    private String juDiscount;
    private String juLogo;
    private String juSlogo;
    private String name;
    private String online;
    private String onlineEndTime;
    private Long onlineEndTimeLong;
    private Long onlineStartTimeLong;

    public static BrandMO fromTOP(JSONObject obj) throws JSONException {
        BrandMO item = new BrandMO();
        item.setCode(obj.getString("code"));
        item.setName(obj.optString("name"));
        item.setJuLogo(obj.optString("juLogo"));
        item.setJuBanner(obj.optString("juBanner"));
        item.setJuDiscount(obj.optString("juDiscount"));
        item.setJuSlogo(obj.optString("juSlogo"));
        item.setJuBrand_id(obj.optString("juBrand_id"));
        item.setItemCount(Integer.valueOf(obj.optInt("itemCount")));
        item.setOnlineStartTimeLong(Long.valueOf(obj.optLong("onlineStartTimeLong")));
        item.setOnlineEndTimeLong(Long.valueOf(obj.optLong("onlineEndTimeLong")));
        item.setOnlineEndTime(obj.optString("onlineEndTime"));
        item.setOnline(obj.optString(RequestConstant.ENV_ONLINE));
        return item;
    }

    public String toString() {
        return "BrandMO [code=" + this.code + ", name=" + this.name + ", juLogo=" + this.juLogo + ", juBanner=" + this.juBanner + ", juDiscount=" + this.juDiscount + ", juSlogo=" + this.juSlogo + ", juBrand_id=" + this.juBrand_id + ", itemCount=" + this.itemCount + ", onlineStartTimeLong=" + this.onlineStartTimeLong + ", onlineEndTimeLong=" + this.onlineEndTimeLong + ", onlineEndTime=" + this.onlineEndTime + ", online=" + this.online + "]";
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
        this.code = code2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getJuLogo() {
        return this.juLogo;
    }

    public void setJuLogo(String juLogo2) {
        this.juLogo = juLogo2;
    }

    public String getJuBanner() {
        return this.juBanner;
    }

    public void setJuBanner(String juBanner2) {
        this.juBanner = juBanner2;
    }

    public String getJuDiscount() {
        return this.juDiscount;
    }

    public void setJuDiscount(String juDiscount2) {
        this.juDiscount = juDiscount2;
    }

    public String getJuSlogo() {
        return this.juSlogo;
    }

    public void setJuSlogo(String juSlogo2) {
        this.juSlogo = juSlogo2;
    }

    public String getJuBrand_id() {
        return this.juBrand_id;
    }

    public void setJuBrand_id(String juBrand_id2) {
        this.juBrand_id = juBrand_id2;
    }

    public Integer getItemCount() {
        return this.itemCount;
    }

    public void setItemCount(Integer itemCount2) {
        this.itemCount = itemCount2;
    }

    public Long getOnlineStartTimeLong() {
        return this.onlineStartTimeLong;
    }

    public void setOnlineStartTimeLong(Long onlineStartTimeLong2) {
        this.onlineStartTimeLong = onlineStartTimeLong2;
    }

    public Long getOnlineEndTimeLong() {
        return this.onlineEndTimeLong;
    }

    public void setOnlineEndTimeLong(Long onlineEndTimeLong2) {
        this.onlineEndTimeLong = onlineEndTimeLong2;
    }

    public String getOnlineEndTime() {
        return this.onlineEndTime;
    }

    public void setOnlineEndTime(String onlineEndTime2) {
        this.onlineEndTime = onlineEndTime2;
    }

    public String getOnline() {
        return this.online;
    }

    public void setOnline(String online2) {
        this.online = online2;
    }
}
