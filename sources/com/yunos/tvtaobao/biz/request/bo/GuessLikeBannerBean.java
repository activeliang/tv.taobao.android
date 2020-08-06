package com.yunos.tvtaobao.biz.request.bo;

import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tv.tvsdk.media.data.HuasuVideo;
import org.json.JSONException;
import org.json.JSONObject;

public class GuessLikeBannerBean {
    private String activityCode;
    private String fkid;
    private String picUrl;
    private String title;
    private String type;
    private String uri;

    public void setActivityCode(String activityCode2) {
        this.activityCode = activityCode2;
    }

    public String getActivityCode() {
        return this.activityCode;
    }

    public void setFkid(String fkid2) {
        this.fkid = fkid2;
    }

    public String getFkid() {
        return this.fkid;
    }

    public void setPicUrl(String picUrl2) {
        this.picUrl = picUrl2;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getType() {
        return this.type;
    }

    public void setUri(String uri2) {
        this.uri = uri2;
    }

    public String getUri() {
        return this.uri;
    }

    public static GuessLikeBannerBean resolveData(JSONObject object) throws JSONException {
        GuessLikeBannerBean guessLikeBannerBean = new GuessLikeBannerBean();
        guessLikeBannerBean.setActivityCode(object.getString("activityCode"));
        guessLikeBannerBean.setType(object.getString("type"));
        guessLikeBannerBean.setPicUrl(object.getString(TuwenConstants.PARAMS.PIC_URL));
        guessLikeBannerBean.setUri(object.getString(HuasuVideo.TAG_URI));
        guessLikeBannerBean.setTitle(object.getString("title"));
        return guessLikeBannerBean;
    }
}
