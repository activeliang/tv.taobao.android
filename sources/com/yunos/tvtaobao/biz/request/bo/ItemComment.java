package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONObject;

public class ItemComment implements Serializable {
    private static final long serialVersionUID = 1725661734124096938L;
    private String content;
    private Integer level1;
    private Integer level2;
    private String time;
    private String userNick;

    public String toString() {
        return "ItemComment [content=" + this.content + ", userNick=" + this.userNick + ", level1=" + this.level1 + ", level2=" + this.level2 + "]";
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content2) {
        this.content = content2;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time2) {
        this.time = time2;
    }

    public String getUserNick() {
        return this.userNick;
    }

    public void setUserNick(String userNick2) {
        this.userNick = userNick2;
    }

    public static ItemComment resolveFromTop(JSONObject dataObj) throws Exception {
        ItemComment comment = new ItemComment();
        if (!dataObj.isNull("displayUserNick")) {
            comment.setUserNick(dataObj.getString("displayUserNick"));
        }
        if (!dataObj.isNull("rateContent")) {
            comment.setContent(dataObj.getString("rateContent"));
        }
        if (!dataObj.isNull("dispalyRateLevel1")) {
            comment.setLevel1(Integer.valueOf(dataObj.getInt("dispalyRateLevel1")));
        }
        if (!dataObj.isNull("dispalyRateLevel2")) {
            comment.setLevel2(Integer.valueOf(dataObj.getInt("dispalyRateLevel2")));
        }
        if (!dataObj.isNull("rateDate")) {
            comment.setTime(dataObj.getString("rateDate"));
        }
        return comment;
    }

    public Integer getLevel1() {
        return this.level1;
    }

    public void setLevel1(Integer level12) {
        this.level1 = level12;
    }

    public Integer getLevel2() {
        return this.level2;
    }

    public void setLevel2(Integer level22) {
        this.level2 = level22;
    }
}
