package com.tvtaobao.voicesdk.utils;

import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {
    public static String getString(JSONObject json, String key) {
        if (key == null || json == null) {
            return null;
        }
        try {
            return json.getString(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public static String getString(JSONArray jsonArray, int index) {
        if (index < 0 || jsonArray == null) {
            return null;
        }
        try {
            return jsonArray.getString(index);
        } catch (JSONException e) {
            return null;
        }
    }

    public static int getInt(JSONObject json, String key) {
        if (key == null || json == null) {
            return -1;
        }
        try {
            return json.getInt(key);
        } catch (JSONException e) {
            return -1;
        }
    }

    public static long getLong(JSONObject json, String key) {
        if (key == null || json == null) {
            return -1;
        }
        try {
            return json.getLong(key);
        } catch (JSONException e) {
            return -1;
        }
    }

    public static double getDouble(JSONObject json, String key) {
        if (key == null || json == null) {
            return ClientTraceData.b.f47a;
        }
        try {
            return json.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return ClientTraceData.b.f47a;
        }
    }

    public static JSONObject getJSON(JSONObject json, String key) {
        if (key == null || json == null) {
            return null;
        }
        try {
            return json.getJSONObject(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public static boolean getBoolean(JSONObject json, String key) {
        if (key == null || json == null) {
            return false;
        }
        try {
            return json.getBoolean(key);
        } catch (JSONException e) {
            return false;
        }
    }

    public static boolean getBoolean(JSONObject json, String key, boolean def) {
        if (key == null || json == null) {
            return def;
        }
        try {
            return json.getBoolean(key);
        } catch (JSONException e) {
            return def;
        }
    }

    public static JSONArray getArray(JSONObject json, String key) {
        if (key == null || json == null) {
            return null;
        }
        try {
            return json.getJSONArray(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public static JSONObject put(JSONObject object, String name, Object value) {
        if (object == null || name == null) {
            return null;
        }
        try {
            object.put(name, value);
            return object;
        } catch (JSONException e) {
            return object;
        }
    }
}
