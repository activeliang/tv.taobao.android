package com.yunos.tv.blitz.video.data;

import java.io.Serializable;
import org.json.JSONObject;

public class ChannelVideoInfo implements Serializable {
    private static final long serialVersionUID = 6628258462312986516L;
    private String channelID;
    private String httpUrl;

    public static ChannelVideoInfo fromJson(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        ChannelVideoInfo item = new ChannelVideoInfo();
        item.httpUrl = obj.optString("httpUrl");
        item.channelID = obj.optString("channelID");
        return item;
    }

    public String toString() {
        return "ChannelVideoInfo [httpUrl=" + this.httpUrl + ", channelID=" + this.channelID + "]";
    }

    public String getHttpUrl() {
        return this.httpUrl;
    }

    public String getChannelID() {
        return this.channelID;
    }

    public void setHttpUrl(String httpUrl2) {
        this.httpUrl = httpUrl2;
    }

    public void setChannelID(String channelID2) {
        this.channelID = channelID2;
    }
}
