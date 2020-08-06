package com.uc.webview.export.internal.interfaces;

import com.uc.webview.export.annotations.Api;

@Api
/* compiled from: ProGuard */
public interface IBreakpad {
    public static final String CRASH_LOG_PREFIX = "SDKBrowser";

    void addHeaderInfo(String str, String str2);

    void setCrashLogFileName(String str);

    void uploadCrashLogs();
}
