package com.ta.audid.store;

import android.content.Context;
import com.ta.audid.Variables;
import com.ta.audid.collect.DeviceFPCollect;
import com.ta.audid.upload.UtdidKeyFile;
import com.ta.audid.utils.MD5Utils;
import com.ta.utdid2.android.utils.StringUtils;
import java.util.HashMap;
import org.json.JSONObject;

public class UtdidContentBuilder {
    private static final String BODY = "{\"type\":\"%s\",\"timestamp\":%s,\"data\":%s}";
    public static final String TYPE_AUDID = "audid";
    public static final String TYPE_FP = "fp";
    public static final String TYPE_RS = "rs";

    public static String buildUDID(String utdid) {
        Context context = Variables.getInstance().getContext();
        if (context == null) {
            return "";
        }
        return StringUtils.getStringWithoutBlank(String.format(BODY, new Object[]{"audid", Variables.getInstance().getCurrentTimeMillisString(), buildAudidDataJsonString(utdid, UtdidKeyFile.readAudidFile(), Variables.getInstance().getAppkey(), context.getPackageName())}));
    }

    private static String buildAudidDataJsonString(String utdid, String audid, String appkey, String appname) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("audid", audid);
        hashMap.put("utdid", utdid);
        hashMap.put("appkey", appkey);
        hashMap.put("appName", appname);
        return new JSONObject(StringUtils.sortMapByKey(hashMap)).toString();
    }

    public static String buildRS(String udidSrc, String utdidTarget, String appkeyTarget, String appnameTarget) {
        Context context = Variables.getInstance().getContext();
        if (context == null) {
            return "";
        }
        return StringUtils.getStringWithoutBlank(String.format(BODY, new Object[]{TYPE_RS, Variables.getInstance().getCurrentTimeMillisString(), RSModle.buildJsonString(udidSrc, Variables.getInstance().getAppkey(), context.getPackageName(), utdidTarget, appkeyTarget, appnameTarget)}));
    }

    public static String buildUtdidFp(String utdid) {
        Context context = Variables.getInstance().getContext();
        if (context == null) {
            return "";
        }
        return StringUtils.getStringWithoutBlank(String.format(BODY, new Object[]{TYPE_FP, Variables.getInstance().getCurrentTimeMillisString(), buildFPDataJsonString(utdid, Variables.getInstance().getAppkey(), context.getPackageName())}));
    }

    private static String buildFPDataJsonString(String utdid, String appkey, String appname) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("utdid", utdid);
        hashMap.put("appkey", appkey);
        hashMap.put("appName", appname);
        hashMap.put(Module.MODULE_FP_INFO, DeviceFPCollect.getFPInfo(Variables.getInstance().getContext()));
        return new JSONObject(hashMap).toString();
    }

    public static String getRS_MD5(String jsonStr) {
        try {
            JSONObject json = new JSONObject(jsonStr);
            if (!json.has("type") || !json.has("data") || !json.getString("type").equals(TYPE_RS)) {
                return "";
            }
            return MD5Utils.getHmacMd5Hex(json.getString("data"));
        } catch (Exception e) {
            return "";
        }
    }
}
