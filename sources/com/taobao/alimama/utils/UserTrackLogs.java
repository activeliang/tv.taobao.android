package com.taobao.alimama.utils;

import android.support.annotation.Keep;
import android.text.TextUtils;
import com.taobao.alimama.global.Constants;
import com.taobao.muniontaobaosdk.MunionManager;
import com.taobao.muniontaobaosdk.util.SdkUtil;
import com.taobao.statistic.TBS;
import mtopsdk.common.util.SymbolExpUtil;

@Keep
public class UserTrackLogs {
    private static final String UT_PAGE_NAME = "Munion";

    public static void traceInvokeLog(String... strArr) {
        String buildUTKvs = SdkUtil.buildUTKvs(new Throwable(), 1, 5);
        if (strArr != null && strArr.length > 0) {
            buildUTKvs = buildUTKvs + "," + TextUtils.join(",", strArr);
        }
        trackLog(Constants.UtEventId.CUSTOM, "Munion_Invoke_Trace", "", "", buildUTKvs);
    }

    public static void trackAdLog(String str, String... strArr) {
        String str2 = "";
        if (strArr != null && strArr.length > 0) {
            str2 = TextUtils.join(",", strArr);
        }
        trackLog(Constants.UtEventId.TRACK, str, "", "", str2);
    }

    public static void trackClick(int i, String str, String str2) {
        String[] strArr = new String[5];
        strArr[0] = "sdkversion=5.0.2-proguard";
        strArr[1] = "clickid=" + str2;
        strArr[2] = "localinfo=" + (MunionManager.getLocal() != null ? MunionManager.getLocal() : "");
        strArr[3] = "bucket=" + TextUtils.join(SymbolExpUtil.SYMBOL_SEMICOLON, BucketTools.a());
        strArr[4] = str;
        TBS.Ext.commitEvent(i, (Object) "", (Object) "", (Object) "", strArr);
    }

    public static void trackClick(int i, String str, String str2, String str3) {
        String[] strArr = new String[6];
        strArr[0] = "sdkversion=5.0.2-proguard";
        strArr[1] = "clickid=" + str2;
        strArr[2] = "localinfo=" + (MunionManager.getLocal() != null ? MunionManager.getLocal() : "");
        strArr[3] = "bucket=" + TextUtils.join(SymbolExpUtil.SYMBOL_SEMICOLON, BucketTools.a());
        strArr[4] = str;
        strArr[5] = "epid=" + str3;
        TBS.Ext.commitEvent(i, (Object) "", (Object) "", (Object) "", strArr);
    }

    public static void trackCustomLog(String str, String... strArr) {
        String str2 = "";
        if (strArr != null && strArr.length > 0) {
            str2 = TextUtils.join(",", strArr);
        }
        trackLog(Constants.UtEventId.CUSTOM, str, "", "", str2);
    }

    public static void trackDebugLog(String str, String... strArr) {
        String str2 = "";
        if (strArr != null && strArr.length > 0) {
            str2 = TextUtils.join(",", strArr);
        }
        trackLog(Constants.UtEventId.DEBUG, str, "", "", str2);
    }

    public static void trackExceptionLog(int i, String str, String str2) {
        TBS.Page.create("Munion");
        TBS.Ext.commitEvent(i, (Object) "", (Object) "", (Object) "", "sdkversion=5.0.2-proguard", "tag=" + str, str2);
    }

    public static void trackExceptionLog(Exception exc) {
        trackLog(Constants.UtEventId.CUSTOM, "Munion_Exception_Trace", "", "", "message=" + exc.getMessage() + "," + "className=" + exc.getClass().getName() + "," + SdkUtil.buildUTKvs(exc, 0, 4));
    }

    @Deprecated
    public static void trackLog(int i, String str) {
        trackClick(i, str, (String) null);
    }

    public static void trackLog(int i, String str, String str2, String str3, String str4) {
        TBS.Ext.commitEvent("Munion", i, str, str2, str3, String.format("sdkversion=%s,bucket=%s", new Object[]{"5.0.2-proguard", TextUtils.join(SymbolExpUtil.SYMBOL_SEMICOLON, BucketTools.a())}), str4);
    }
}
