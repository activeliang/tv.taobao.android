package com.alibaba.motu.crashreporter;

import java.util.Map;

public interface IUTCrashCaughtListener {
    Map<String, Object> onCrashCaught(Thread thread, Throwable th);
}
