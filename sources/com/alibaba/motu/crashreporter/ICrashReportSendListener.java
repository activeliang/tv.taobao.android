package com.alibaba.motu.crashreporter;

public interface ICrashReportSendListener {
    void afterSend(boolean z, CrashReport crashReport);

    void beforeSend(CrashReport crashReport);

    String getName();
}
