package com.yunos.tv.alitvasrsdk;

import android.taobao.windvane.config.WVConfigManager;
import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppContextData {
    public JSONObject context;
    public JSONObject data;
    public String packageName;
    public int pageType = 0;
    public int soundType = 0;

    public static AppContextData toObject(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject data2 = new JSONObject(str);
                AppContextData appContextData = new AppContextData();
                appContextData.pageType = data2.getInt("pageType");
                appContextData.context = data2.optJSONObject("context");
                appContextData.data = data2.optJSONObject("data");
                return appContextData;
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static String toString(AppContextData result) {
        if (result != null) {
            JSONObject object = new JSONObject();
            try {
                object.put("pageType", result.pageType);
                object.put("soundType", result.soundType);
                object.put(WVConfigManager.CONFIGNAME_PACKAGE, result.packageName);
                object.put("context", result.context);
                object.put("data", result.data);
                return object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject createContextFromCommand(JSONArray command) {
        JSONObject context2 = new JSONObject();
        if (command != null && command.length() > 0) {
            try {
                context2.put("command", command);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return context2;
    }
}
