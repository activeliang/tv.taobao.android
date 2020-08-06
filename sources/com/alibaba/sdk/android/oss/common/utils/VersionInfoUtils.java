package com.alibaba.sdk.android.oss.common.utils;

import android.os.Build;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import com.alibaba.sdk.android.oss.common.OSSLog;
import mtopsdk.common.util.SymbolExpUtil;

public class VersionInfoUtils {
    private static String userAgent = null;

    public static String getUserAgent(String customInfo) {
        if (OSSUtils.isEmptyString(userAgent)) {
            userAgent = "aliyun-sdk-android/" + getVersion() + getSystemInfo();
        }
        if (OSSUtils.isEmptyString(customInfo)) {
            return userAgent;
        }
        return userAgent + WVNativeCallbackUtil.SEPERATER + customInfo;
    }

    public static String getVersion() {
        return "2.9.2";
    }

    private static String getSystemInfo() {
        StringBuilder customUA = new StringBuilder();
        customUA.append("(");
        customUA.append(System.getProperty("os.name"));
        customUA.append("/Android " + Build.VERSION.RELEASE);
        customUA.append(WVNativeCallbackUtil.SEPERATER);
        customUA.append(HttpUtil.urlEncode(Build.MODEL, "utf-8") + SymbolExpUtil.SYMBOL_SEMICOLON + HttpUtil.urlEncode(Build.ID, "utf-8"));
        customUA.append(")");
        String ua = customUA.toString();
        OSSLog.logDebug("user agent : " + ua);
        if (OSSUtils.isEmptyString(ua)) {
            return System.getProperty("http.agent").replaceAll("[^\\p{ASCII}]", WVUtils.URL_DATA_CHAR);
        }
        return ua;
    }
}
