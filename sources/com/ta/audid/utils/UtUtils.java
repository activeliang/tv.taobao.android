package com.ta.audid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.ta.audid.Variables;
import com.ta.utdid2.android.utils.Base64;
import com.ta.utdid2.android.utils.StringUtils;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.json.JSONObject;

public class UtUtils {
    private static final String UTDID_MODULE = "UtdidMonitor";

    public static String getUserNick() {
        SharedPreferences sp;
        Context context = Variables.getInstance().getContext();
        if (context == null || (sp = context.getSharedPreferences("UTCommon", 0)) == null) {
            return "";
        }
        String lun = sp.getString("_lun", "");
        if (StringUtils.isEmpty(lun)) {
            return "";
        }
        try {
            return new String(Base64.decode(lun.getBytes(), 2), "UTF-8");
        } catch (Exception e) {
            UtdidLogger.d("", e);
            return "";
        }
    }

    public static String getUserId() {
        SharedPreferences sp;
        Context context = Variables.getInstance().getContext();
        if (context == null || (sp = context.getSharedPreferences("UTCommon", 0)) == null) {
            return "";
        }
        String luid = sp.getString("_luid", "");
        if (StringUtils.isEmpty(luid)) {
            return "";
        }
        try {
            return new String(Base64.decode(luid.getBytes(), 2), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            UtdidLogger.d("", e);
            return "";
        }
    }

    public static void sendUtdidMonitorEvent(String monitorPoint, Map<String, String> properties) {
        try {
            AppMonitor.Counter.commit(UTDID_MODULE, monitorPoint, new JSONObject(properties).toString(), 1.0d);
        } catch (Throwable e) {
            UtdidLogger.d("", e);
        }
    }
}
