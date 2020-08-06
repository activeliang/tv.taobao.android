package com.ali.user.open.core.webview;

import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.service.UserTrackerService;
import com.ut.mini.UTHitBuilders;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public class UserTrackBridge {
    @BridgeMethod
    public void commitEvent(BridgeCallbackContext bridgeCallbackContext, String paramString) {
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
            } else if (eventId == 2001) {
                ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).updatePageName(bridgeCallbackContext.getActivity(), page, paramMap);
            }
        } catch (Throwable th2) {
        }
    }
}
