package com.ali.user.open.ucc.util;

import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.service.UserTrackerService;
import com.ali.user.open.ucc.model.UccParams;
import com.ut.mini.UTHitBuilders;
import java.util.HashMap;
import java.util.Map;

public class UTHitUtils {
    public static void send(String pageName, String label, UccParams params, Map<String, String> extParams) {
        Map<String, String> props = new HashMap<>();
        if (params == null || TextUtils.isEmpty(params.site)) {
            props.put("site", AliMemberSDK.getMasterSite());
        } else {
            props.put("site", params.site);
        }
        if (params != null) {
            props.put("bindSite", params.bindSite);
            props.put("userToken", params.userToken);
            if (!TextUtils.isEmpty(params.miniAppId)) {
                props.put("miniAppId", params.miniAppId);
            }
            props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, params.traceId);
        }
        if (extParams != null) {
            props.putAll(extParams);
        }
        ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send(pageName, label, props);
    }
}
