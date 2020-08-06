package com.yunos.tvtaobao.biz.request.bo.resource.entrances;

import android.text.TextUtils;
import java.io.Serializable;
import java.util.List;

public class ShopProm implements Serializable {
    private String actionUrl;
    private String activityId;
    private String content;
    private List<String> contentArrau;
    private String iconText;
    private String period;
    private int tag;
    private String title;
    private String type;

    public String getActivityId() {
        return this.activityId;
    }

    public void setActivityId(String activityId2) {
        this.activityId = activityId2;
    }

    public String getIconText() {
        return this.iconText;
    }

    public void setIconText(String iconText2) {
        this.iconText = iconText2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getPeriod() {
        return this.period;
    }

    public void setPeriod(String period2) {
        this.period = period2;
    }

    public List<String> getContentArrau() {
        return this.contentArrau;
    }

    public void setContentArrau(List<String> contentArrau2) {
        this.contentArrau = contentArrau2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content2) {
        this.content = content2;
    }

    public String getActionUrl() {
        return this.actionUrl;
    }

    public int getTag() {
        return this.tag;
    }

    public void setActionUrl(String actionUrl2) {
        this.actionUrl = actionUrl2;
        if (TextUtils.isEmpty(actionUrl2)) {
            return;
        }
        if (actionUrl2.contains("wh_tag=148226")) {
            this.tag = 2;
        } else if (actionUrl2.contains("wh_tag=148162")) {
            this.tag = 3;
        } else if (actionUrl2.contains("wh_tag=148098")) {
            this.tag = 4;
        }
    }

    public String toString() {
        return "ShopProm{activityId='" + this.activityId + '\'' + ", iconText='" + this.iconText + '\'' + ", title='" + this.title + '\'' + ", period='" + this.period + '\'' + ", contentArrau=" + this.contentArrau + ", type='" + this.type + '\'' + ", content='" + this.content + '\'' + ", actionUrl='" + this.actionUrl + '\'' + ", tag=" + this.tag + '}';
    }
}
