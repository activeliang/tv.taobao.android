package com.alibaba.motu.crashreporter;

import java.util.Map;

public interface ICrashReportDataListener {
    void onCrashCaught(Map<String, Object> map);
}
