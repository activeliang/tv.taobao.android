package com.yunos.tv.blitz.video.data;

import android.text.TextUtils;
import com.yunos.tv.tvsdk.media.view.VideoView;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoItem {
    private String icon;
    private String id;
    private String m3u8_data;
    private int start = 0;
    private String title;
    private int type = 0;
    private String video;

    public static VideoItem fromJson(String json) {
        if (json == null) {
            return null;
        }
        try {
            return fromJson(new JSONObject(json));
        } catch (JSONException e) {
            return null;
        }
    }

    public static VideoItem fromJson(JSONObject obj) {
        VideoItem item = new VideoItem();
        item.id = obj.optString("id");
        item.type = obj.optInt("type");
        item.title = obj.optString("title");
        item.icon = obj.optString("icon");
        item.video = obj.optString("video");
        item.start = obj.optInt("start", 0);
        item.m3u8_data = obj.optString(VideoView.HEADER_DATASOURCE_M3U8);
        return item;
    }

    public String toJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("type", this.type);
            jsonObj.put("title", this.title);
            jsonObj.put("icon", this.icon);
            jsonObj.put("video", this.video);
            jsonObj.put("start", this.start);
            jsonObj.put(VideoView.HEADER_DATASOURCE_M3U8, this.m3u8_data);
        } catch (JSONException e) {
        }
        return jsonObj.toString();
    }

    public boolean isUseFul() {
        if (!TextUtils.isEmpty(getId()) || !TextUtils.isEmpty(getVideo())) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "MenuItem [id=" + this.id + ", type=" + this.type + ", title=" + this.title + ", icon=" + this.icon + ", video=" + this.video.replaceAll("\\n", " ") + ", start=" + this.start + ", m3u8_data=" + this.m3u8_data + "]";
    }

    public String getId() {
        return this.id;
    }

    public int getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public void setIcon(String icon2) {
        this.icon = icon2;
    }

    public String getVideo() {
        return this.video;
    }

    public void setVideo(String video2) {
        this.video = video2;
    }

    public int getStart() {
        return this.start;
    }

    public String getM3u8_data() {
        return this.m3u8_data;
    }

    public void setStart(int start2) {
        this.start = start2;
    }

    public void setM3u8_data(String m3u8_data2) {
        this.m3u8_data = m3u8_data2;
    }
}
