package com.ali.user.open.jsbridge;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.service.UserTrackerService;
import com.ut.mini.UTHitBuilders;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public class UserTrackBridge extends WVApiPlugin {
    public boolean execute(String action, String params, WVCallBackContext callback) {
        if ("commitEvent".equals(action)) {
            commitEvent(params, callback);
            return true;
        }
        callback.error();
        return false;
    }

    private void commitEvent(String paramString, WVCallBackContext callbackContext) {
        try {
            JSONObject params = new JSONObject(paramString);
            String page = params.optString("page");
            int eventId = params.optInt("eventID");
            String arg1 = params.optString("arg1");
            String arg2 = params.optString("arg2");
            String args = params.optString("args");
            Map<String, String> paramMap = new HashMap<>();
            if (!TextUtils.isEmpty(arg2)) {
                paramMap.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, arg2);
            }
            if (!TextUtils.isEmpty(args)) {
                try {
                    JSONObject argsJS = new JSONObject(args);
                    Iterator iterator = argsJS.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        paramMap.put(key, argsJS.optString(key));
                    }
                } catch (Throwable th) {
                }
            }
            if (eventId == 19999) {
                ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send(page, arg1, paramMap);
            } else if (eventId == 2101) {
                ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).sendControl(page, arg1, arg2, paramMap);
            }
        } catch (Throwable th2) {
        }
    }
}
