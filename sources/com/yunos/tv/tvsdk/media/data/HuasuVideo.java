package com.yunos.tv.tvsdk.media.data;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class HuasuVideo {
    private static final String TAG = "AdoVideo";
    public static final String TAG_M3U8 = "m3u8";
    public static final String TAG_STARTTIME = "starttime";
    public static final String TAG_URI = "uri";
    public static final String TAG_VID = "vid";
    public static final String TAG_VNAME = "name";
    public JSONObject mJson;

    public HuasuVideo() {
        try {
            this.mJson = new JSONObject();
        } catch (Exception e) {
            Log.w(TAG, Log.getStackTraceString(e));
        }
    }

    public void putValue(String tag, Object value) {
        if (this.mJson == null) {
            Log.w(TAG, "json object is null");
            return;
        }
        try {
            this.mJson.put(tag, value);
        } catch (JSONException e) {
            Log.w(TAG, "tag = " + tag + Log.getStackTraceString(e));
        }
    }

    public String toString() {
        if (this.mJson != null) {
            return this.mJson.toString();
        }
        return "";
    }
}
