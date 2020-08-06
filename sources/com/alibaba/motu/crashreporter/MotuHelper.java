package com.alibaba.motu.crashreporter;

import java.util.HashMap;
import java.util.Map;

public class MotuHelper {
    public static void report(Throwable throwable, String... params) {
        Map<String, Object> extParams = new HashMap<>();
        extParams.put("MotuHelper", "这是个外部调用魔兔异常上报逻辑，上报上来的异常，非魔兔的监测");
        int i = 0;
        while (params != null && i < params.length) {
            extParams.put("" + i, params[i]);
            i++;
        }
        CrashReporter.getInstance().mSendManager.sendReport(CrashReporter.getInstance().mReportBuilder.buildUncaughtExceptionReport(throwable, Thread.currentThread(), extParams));
    }
}
