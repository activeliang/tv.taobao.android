package com.yunos.tvtaobao.biz.request.bo;

import com.yunos.tvtaobao.biz.common.BaseConfig;
import org.json.JSONException;
import org.json.JSONObject;

public class YGAttachInfo {
    private String coverUrl;
    private String hotline;
    private String id;
    private String itemImageURL;
    private String name;
    private String shortTitle;
    private String subTitle;
    private String thirdItemId;
    private String tid;
    private String topic;
    private String videoUrl;

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String coverUrl2) {
        this.coverUrl = coverUrl2;
    }

    public String getHotline() {
        return this.hotline;
    }

    public void setHotline(String hotline2) {
        this.hotline = hotline2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getItemImageURL() {
        return this.itemImageURL;
    }

    public void setItemImageURL(String itemImageURL2) {
        this.itemImageURL = itemImageURL2;
    }

    public String getShortTitle() {
        return this.shortTitle;
    }

    public void setShortTitle(String shortTitle2) {
        this.shortTitle = shortTitle2;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(String subTitle2) {
        this.subTitle = subTitle2;
    }

    public String getThirdItemId() {
        return this.thirdItemId;
    }

    public void setThirdItemId(String thirdItemId2) {
        this.thirdItemId = thirdItemId2;
    }

    public String getTid() {
        return this.tid;
    }

    public void setTid(String tid2) {
        this.tid = tid2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic2) {
        this.topic = topic2;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public void setVideoUrl(String videoUrl2) {
        this.videoUrl = videoUrl2;
    }

    public static YGAttachInfo fromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        YGAttachInfo item = new YGAttachInfo();
        item.setCoverUrl(obj.getString("coverUrl"));
        item.setHotline(obj.getString("hotline"));
        item.setId(obj.getString("id"));
        if (obj.has("itemInfo")) {
            JSONObject itemInfo = obj.getJSONObject("itemInfo");
            item.setItemImageURL(itemInfo.getString("itemImageURL"));
            item.setShortTitle(itemInfo.getString("shortTitle"));
            item.setSubTitle(itemInfo.getString("subTitle"));
            item.setThirdItemId(itemInfo.getString("thirdItemId"));
            item.setTid(itemInfo.getString(BaseConfig.INTENT_KEY_TID));
        } else {
            item.setItemImageURL("");
            item.setShortTitle("");
            item.setSubTitle("");
            item.setThirdItemId("");
            item.setTid("");
        }
        item.setName(obj.getString("name"));
        item.setTopic(obj.getString("topic"));
        item.setVideoUrl(obj.getString("videoUrl"));
        return item;
    }
}
