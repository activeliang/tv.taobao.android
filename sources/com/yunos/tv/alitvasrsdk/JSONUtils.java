package com.yunos.tv.alitvasrsdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
    public static String getJSONString(JSONObject object, String key) {
        if (object == null || key == null) {
            return null;
        }
        try {
            return object.getString(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public static String getJSONString(JSONArray object, int index) {
        if (object == null || index < 0 || index >= object.length()) {
            return null;
        }
        try {
            return object.getString(index);
        } catch (JSONException e) {
            return null;
        }
    }

    public static JSONObject getJSONObject(JSONObject object, String key) {
        if (object == null || key == null) {
            return null;
        }
        try {
            return object.getJSONObject(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public static JSONObject getJSONObject(JSONArray object, int index) {
        if (object == null || index < 0 || index >= object.length()) {
            return null;
        }
        try {
            return object.getJSONObject(index);
        } catch (JSONException e) {
            return null;
        }
    }

    public static Object getObject(JSONArray object, int index) {
        if (object == null || index < 0 || index >= object.length()) {
            return null;
        }
        try {
            return object.get(index);
        } catch (JSONException e) {
            return null;
        }
    }

    public static int getJSONInt(JSONObject object, String key) {
        if (object == null || key == null) {
            return -1;
        }
        try {
            return object.getInt(key);
        } catch (JSONException e) {
            return -1;
        }
    }

    public static JSONArray getJSONArray(JSONObject object, String key) {
        if (object == null || key == null) {
            return null;
        }
        try {
            return object.getJSONArray(key);
        } catch (JSONException e) {
            return null;
        }
    }
}
