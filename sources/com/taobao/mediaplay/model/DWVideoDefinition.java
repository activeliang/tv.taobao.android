package com.taobao.mediaplay.model;

import android.text.TextUtils;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import org.json.JSONObject;

public class DWVideoDefinition {
    private int mBitrate;
    private String mCacheKey;
    private JSONObject mData;
    private String mDefinition;
    private int mHeight;
    private int mSize;
    private String mVideoUrl;
    private int mWeight;
    private int mWidth;

    public DWVideoDefinition(JSONObject jsonObject) {
        this.mData = jsonObject;
    }

    public String getVideoUrl() {
        if (TextUtils.isEmpty(this.mVideoUrl) && this.mData != null) {
            Object obj = this.mData.opt("video_url");
            this.mVideoUrl = obj == null ? null : obj.toString();
        }
        return this.mVideoUrl;
    }

    public String getDefinition() {
        if (TextUtils.isEmpty(this.mDefinition) && this.mData != null) {
            Object obj = this.mData.opt("definition");
            this.mDefinition = obj == null ? null : obj.toString();
        }
        return this.mDefinition;
    }

    public String getCacheKey() {
        if (TextUtils.isEmpty(this.mCacheKey) && this.mData != null) {
            Object obj = this.mData.opt("cacheKey");
            this.mCacheKey = obj == null ? null : obj.toString();
        }
        return this.mCacheKey;
    }

    public int getVideoSize() {
        try {
            if (this.mSize == 0 && this.mData != null) {
                Object obj = this.mData.opt("length");
                double size = (obj == null || !TextUtils.isDigitsOnly(obj.toString())) ? ClientTraceData.b.f47a : (double) Integer.parseInt(obj.toString());
                this.mSize = (size < 102400.0d || size > 2.097152E8d) ? -1 : (int) size;
            }
        } catch (Exception e) {
            this.mSize = -1;
        }
        return this.mSize;
    }

    public int getVideoWidth() {
        if (this.mWidth == 0 && this.mData != null) {
            Object obj = this.mData.opt("width");
            this.mWidth = (obj == null || !TextUtils.isDigitsOnly(obj.toString())) ? 0 : Integer.parseInt(obj.toString());
        }
        return this.mWidth;
    }

    public int getVideoHeight() {
        if (this.mHeight == 0 && this.mData != null) {
            Object obj = this.mData.opt("height");
            this.mHeight = (obj == null || !TextUtils.isDigitsOnly(obj.toString())) ? 0 : Integer.parseInt(obj.toString());
        }
        return this.mHeight;
    }

    public int getVideoBitrate() {
        if (this.mBitrate == 0 && this.mData != null) {
            Object obj = this.mData.opt("bitrate");
            this.mBitrate = (obj == null || !TextUtils.isDigitsOnly(obj.toString())) ? 0 : Integer.parseInt(obj.toString());
        }
        return this.mBitrate;
    }

    public int getWeight() {
        return this.mWeight;
    }

    public void addWeight(int weight) {
        this.mWeight += weight;
    }
}
