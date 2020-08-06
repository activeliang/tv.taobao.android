package com.yunos.tv.blitz.account;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountUtils {
    public static Bundle getYoukuLoginInfo() {
        return null;
    }

    public static boolean checkYoukuLogin() {
        return false;
    }

    public static String getYoukuName() {
        return null;
    }

    public static String getYoukuAvatar() {
        return null;
    }

    public static String getYoukuYktk() {
        return null;
    }

    public static String getYoukuYkID() {
        return null;
    }

    public static Bundle getAccountInfo(String type) {
        return null;
    }

    public static String getMethodString(HashMap<String, String> apiParams) {
        JSONObject obj = new JSONObject();
        for (Map.Entry<String, String> entry : apiParams.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
                try {
                    obj.put(name, value);
                } catch (JSONException e) {
                }
            }
        }
        Log.d("AccountUtils", "getMethodString = " + obj.toString());
        return obj.toString();
    }

    public static int getVersioncode(Context context, String packageName) {
        try {
            return context.getPackageManager().getPackageInfo(packageName, 16384).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
