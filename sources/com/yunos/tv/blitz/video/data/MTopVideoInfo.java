package com.yunos.tv.blitz.video.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import org.json.JSONObject;

public class MTopVideoInfo implements Parcelable {
    public static final Parcelable.Creator<MTopVideoInfo> CREATOR = new Parcelable.Creator<MTopVideoInfo>() {
        public MTopVideoInfo createFromParcel(Parcel source) {
            MTopVideoInfo p = new MTopVideoInfo();
            p.v4k = source.readString();
            p.v1080 = source.readString();
            p.v720 = source.readString();
            p.v480 = source.readString();
            p.v320 = source.readString();
            return p;
        }

        public MTopVideoInfo[] newArray(int size) {
            return new MTopVideoInfo[size];
        }
    };
    public static final String TAG = "MTopVideoInfo";
    public String dnsAddress = "";
    public String drmToken = "";
    public String hlsContent = "";
    public String method = "";
    public boolean tokenValid = false;
    public boolean trial = true;
    public String tvHost = "";
    public String v1080 = "";
    public String v320 = "";
    public String v480 = "";
    public String v4k = "";
    public String v720 = "";

    public static MTopVideoInfo fromJson(JSONObject obj) {
        if (obj != null) {
            return new MTopVideoInfo(obj);
        }
        return null;
    }

    public MTopVideoInfo() {
    }

    public MTopVideoInfo(JSONObject ext) {
        try {
            this.trial = ext.optBoolean("trial");
            this.tokenValid = ext.optBoolean("tokenValid");
            if (ext.has("hlsContent")) {
                this.hlsContent = ext.optString("hlsContent");
            }
            if (ext.has("drmToken")) {
                this.drmToken = ext.optString("drmToken");
            }
            if (ext.has("httpDns")) {
                JSONObject httpdns = ext.optJSONObject("httpDns");
                this.method = httpdns.optString("method");
                this.tvHost = httpdns.optString("tvHost");
                this.dnsAddress = httpdns.optString("dnsAddress");
            }
            if (ext.has("sourceInfo")) {
                JSONObject sourceInfo = ext.optJSONObject("sourceInfo");
                this.v1080 = sourceInfo.optString("v1080tv");
                this.v720 = sourceInfo.optString("v720tv");
                this.v480 = sourceInfo.optString("v720");
                this.v320 = sourceInfo.optString("v480");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getVideoUrl() {
        if (!TextUtils.isEmpty(this.v720)) {
            return this.v720;
        }
        if (!TextUtils.isEmpty(this.v480)) {
            return this.v480;
        }
        if (!TextUtils.isEmpty(this.v320)) {
            return this.v320;
        }
        if (!TextUtils.isEmpty(this.v1080)) {
            return this.v1080;
        }
        if (!TextUtils.isEmpty(this.v4k)) {
            return this.v4k;
        }
        return null;
    }

    public boolean isEmpty() {
        if (TextUtils.isEmpty(this.v1080) && TextUtils.isEmpty(this.v720) && TextUtils.isEmpty(this.v480) && TextUtils.isEmpty(this.v320) && TextUtils.isEmpty(this.v4k)) {
            return true;
        }
        return false;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.v4k);
        dest.writeString(this.v1080);
        dest.writeString(this.v720);
        dest.writeString(this.v480);
        dest.writeString(this.v320);
    }

    public int describeContents() {
        return 0;
    }
}
