package com.yunos.tvtaobao.biz.request.bo;

import com.yunos.tv.tvsdk.media.data.HuasuVideo;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.io.Serializable;

public class LoadingBo implements Serializable {
    private static final long serialVersionUID = 172222154019535038L;
    private int duration;
    private String endTime;
    private String imgUrl;
    private String itemIdStr;
    private String md5;
    private String shopIdStr;
    private String startTime;
    private String uri;
    private String zpAdCT;
    private String zpAdId;

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl2) {
        this.imgUrl = imgUrl2;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md52) {
        this.md5 = md52;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime2) {
        this.startTime = startTime2;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime2) {
        this.endTime = endTime2;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration2) {
        this.duration = duration2;
    }

    public String getItemIdStr() {
        return this.itemIdStr;
    }

    public void setItemIdStr(String itemIdStr2) {
        this.itemIdStr = itemIdStr2;
    }

    public String getShopIdStr() {
        return this.shopIdStr;
    }

    public void setShopIdStr(String shopIdStr2) {
        this.shopIdStr = shopIdStr2;
    }

    public String getZpAdId() {
        return this.zpAdId;
    }

    public void setZpAdId(String zpAdId2) {
        this.zpAdId = zpAdId2;
    }

    public String getZpAdCT() {
        return this.zpAdCT;
    }

    public void setZpAdCT(String zpAdCT2) {
        this.zpAdCT = zpAdCT2;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri2) {
        this.uri = uri2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("imgUrl:" + getImgUrl() + ",");
        stringBuilder.append("md5:" + getMd5() + ",");
        stringBuilder.append("startTime" + getStartTime() + ",");
        stringBuilder.append("endTime" + getEndTime());
        stringBuilder.append("itemIdStr" + getItemIdStr());
        stringBuilder.append("shopIdStr" + getShopIdStr());
        stringBuilder.append(BaseConfig.zpAdId + getZpAdId());
        stringBuilder.append("zpAdType" + getZpAdCT());
        stringBuilder.append(HuasuVideo.TAG_URI + getUri());
        return stringBuilder.toString();
    }
}
