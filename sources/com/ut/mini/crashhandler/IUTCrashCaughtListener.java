package com.ut.mini.crashhandler;

import java.util.Map;

public interface IUTCrashCaughtListener {
    Map<String, Object> onCrashCaught(Thread thread, Throwable th);
}
