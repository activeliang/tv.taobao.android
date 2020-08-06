package com.yunos.tvtaobao.biz.request.bo;

import com.alibaba.motu.videoplayermonitor.VPMConstants;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import org.json.JSONException;
import org.json.JSONObject;

public class YGAcrVideoItem {
    private String duration;
    private String endAt;
    private String hotline;
    private String iconUrl;
    private String id;
    private String itemImageURL;
    private String shortTitle;
    private String startAt;
    private String subTitle;
    private String thirdItemId;
    private String tid;

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getStartAt() {
        return this.startAt;
    }

    public void setStartAt(String startAt2) {
        this.startAt = startAt2;
    }

    public String getEndAt() {
        return this.endAt;
    }

    public void setEndAt(String endAt2) {
        this.endAt = endAt2;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration2) {
        this.duration = duration2;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl2) {
        this.iconUrl = iconUrl2;
    }

    public String getHotline() {
        return this.hotline;
    }

    public void setHotline(String hotline2) {
        this.hotline = hotline2;
    }

    public String getTid() {
        return this.tid;
    }

    public void setTid(String tid2) {
        this.tid = tid2;
    }

    public String getThirdItemId() {
        return this.thirdItemId;
    }

    public void setThirdItemId(String thirdItemId2) {
        this.thirdItemId = thirdItemId2;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(String subTitle2) {
        this.subTitle = subTitle2;
    }

    public String getShortTitle() {
        return this.shortTitle;
    }

    public void setShortTitle(String shortTitle2) {
        this.shortTitle = shortTitle2;
    }

    public String getItemImageURL() {
        return this.itemImageURL;
    }

    public void setItemImageURL(String itemImageURL2) {
        this.itemImageURL = itemImageURL2;
    }

    public static YGAcrVideoItem fromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        YGAcrVideoItem item = new YGAcrVideoItem();
        item.setId(obj.getString("id"));
        item.setStartAt(obj.getString("startAt"));
        item.setEndAt(obj.getString("endAt"));
        item.setDuration(obj.getString(VPMConstants.MEASURE_DURATION));
        item.setIconUrl(obj.getString("iconUrl"));
        item.setHotline(obj.getString("hotline"));
        JSONObject itemInfo = obj.getJSONObject("itemInfo");
        item.setTid(itemInfo.getString(BaseConfig.INTENT_KEY_TID));
        item.setThirdItemId(itemInfo.getString("thirdItemId"));
        item.setSubTitle(itemInfo.getString("subTitle"));
        item.setShortTitle(itemInfo.getString("shortTitle"));
        item.setItemImageURL(itemInfo.getString("itemImageURL"));
        return item;
    }
}
