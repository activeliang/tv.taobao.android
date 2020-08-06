package com.ta.audid.store;

import com.ta.utdid2.android.utils.StringUtils;
import java.util.HashMap;
import org.json.JSONObject;

public class RSModle {
    private static final String BODY = "{\"src\":%s,\"target\":%s}";

    public static String buildJsonString(String udidSrc, String appkey, String appname, String utdidTarget, String appkeyTarget, String appnameTarget) {
        return String.format(BODY, new Object[]{buildSortJsonString(udidSrc, appkey, appname), buildSortJsonString(utdidTarget, appkeyTarget, appnameTarget)});
    }

    private static String buildSortJsonString(String utdid, String appkey, String appname) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("utdid", utdid);
        hashMap.put("appkey", appkey);
        hashMap.put("appName", appname);
        return new JSONObject(StringUtils.sortMapByKey(hashMap)).toString();
    }
}
