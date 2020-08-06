package com.alibaba.motu.crashreporter.utrestapi;

import android.content.Context;
import com.alibaba.motu.crashreporter.Constants;
import com.alibaba.motu.crashreporter.CrashReporter;
import com.alibaba.motu.crashreporter.LogUtil;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import java.util.HashMap;
import java.util.Map;

public class UTRestReq {
    public static boolean sendLog(Context aContext, String aPage, int aEventId, Object aArg1, Object aArg2, Object aArg3, Map<String, Object> aExtData) {
        try {
            Map<String, String> extData = new HashMap<>();
            if (aExtData != null) {
                for (Map.Entry<String, Object> entry : aExtData.entrySet()) {
                    try {
                        extData.put(entry.getKey(), entry.getValue().toString());
                    } catch (Exception e) {
                        LogUtil.w("build extData", e);
                    }
                }
            }
            return sendLog(aContext, System.currentTimeMillis(), aPage, aEventId, aArg1, aArg2, aArg3, extData);
        } catch (Exception e2) {
            LogUtil.e("sendLog", e2);
            return false;
        }
    }

    public static boolean sendLog(Context aContext, long aTimestamp, String aPage, int aEventId, Object aArg1, Object aArg2, Object aArg3, Map<String, String> aExtData) {
        try {
            String appKey = CrashReporter.getInstance().getProperty(Constants.APP_KEY);
            if (StringUtils.isBlank(appKey)) {
                return false;
            }
            String appVersion = CrashReporter.getInstance().getProperty(Constants.APP_VERSION);
            if (StringUtils.isBlank(appVersion)) {
                return false;
            }
            Map<String, String> data = new HashMap<>();
            data.put(Constants.APP_KEY, appKey);
            data.put(Constants.APP_VERSION, appVersion);
            data.put(Constants.UTDID, CrashReporter.getInstance().getPropertyAndSet(Constants.UTDID));
            data.put("IMEI", CrashReporter.getInstance().getPropertyAndSet("IMEI"));
            data.put("IMSI", CrashReporter.getInstance().getPropertyAndSet("IMSI"));
            data.put(Constants.USERNICK, CrashReporter.getInstance().getProperty(Constants.USERNICK));
            data.put(Constants.CHANNEL, CrashReporter.getInstance().getProperty(Constants.CHANNEL));
            return UTRestReqSend.sendLog(aContext, data, aTimestamp, aPage, aEventId, aArg1, aArg2, aArg3, aExtData);
        } catch (Exception e) {
            LogUtil.e("sendLog", e);
            return false;
        }
    }
}
