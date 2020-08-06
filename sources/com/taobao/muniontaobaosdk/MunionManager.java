package com.taobao.muniontaobaosdk;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.taobao.muniontaobaosdk.util.SdkUtil;
import org.json.JSONException;
import org.json.JSONObject;

@Keep
@Deprecated
public class MunionManager {
    private static String cna;
    private static String eCode;
    private static String eurl;
    private static String ext;
    private static boolean invaild = false;
    private static MunionManager istance;
    private static Location localinfo;
    private static JSONObject munionParams;
    private static String referer;
    private static int type;
    private static String unid;

    private MunionManager() {
    }

    public static String getCna() {
        return cna;
    }

    public static String getEcode() {
        return eCode;
    }

    public static String getEurl() {
        return eurl;
    }

    public static String getExt() {
        return ext;
    }

    public static synchronized MunionManager getInstance(Context context, Bundle bundle) {
        MunionManager munionManager;
        synchronized (MunionManager.class) {
            if (istance == null) {
                istance = new MunionManager();
            }
            munionManager = istance;
        }
        return munionManager;
    }

    public static Location getLocal() {
        return localinfo;
    }

    public static boolean getMunionState() {
        return invaild;
    }

    public static JSONObject getParams() {
        return munionParams;
    }

    public static String getReferer() {
        return referer;
    }

    public static int getType() {
        return type;
    }

    public static void parseEurl(String str) {
        String str2 = null;
        if (str.indexOf(WVUtils.URL_DATA_CHAR) > 0) {
            String[] split = str.split("\\?");
            try {
                if (split.length > 1 && split[1] != null) {
                    str2 = split[1];
                }
            } catch (Exception e) {
            }
        }
        JSONObject parseStr = SdkUtil.getParseStr(str2, "&");
        if (parseStr != null) {
            setParams(parseStr);
            try {
                if (parseStr.isNull("type")) {
                    setType("0");
                } else {
                    setType(parseStr.get("type").toString());
                    parseStr.remove("type");
                }
                if (!parseStr.isNull("cna")) {
                    setCna(parseStr.get("cna").toString());
                    parseStr.remove("cna");
                }
                if (!parseStr.isNull("unid")) {
                    setUnid(parseStr.get("unid").toString());
                    parseStr.remove("unid");
                }
                if (!parseStr.isNull("e")) {
                    setMunionState(true);
                    setEcode(parseStr.get("e").toString());
                    parseStr.remove("e");
                }
                if (!parseStr.isNull(RequestParameters.SUBRESOURCE_REFERER)) {
                    setReferer(parseStr.get(RequestParameters.SUBRESOURCE_REFERER).toString());
                    parseStr.remove(RequestParameters.SUBRESOURCE_REFERER);
                }
                setExt(parseStr.toString());
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void setCna(String str) {
        cna = str;
    }

    public static void setEcode(String str) {
        eCode = str;
    }

    public static void setEurl(String str) {
        eurl = str;
    }

    public static void setExt(String str) {
        ext = str;
    }

    public static void setMunionState(boolean z) {
        invaild = z;
    }

    public static void setParams(JSONObject jSONObject) {
        munionParams = jSONObject;
    }

    public static void setReferer(String str) {
        referer = str;
    }

    public static void setType(String str) {
        type = Integer.parseInt(str);
    }

    public static void setUnid(String str) {
        unid = str;
    }
}
