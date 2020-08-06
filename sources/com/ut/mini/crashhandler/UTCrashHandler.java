package com.ut.mini.crashhandler;

import com.alibaba.motu.crashreporter.MotuCrashReporter;

public class UTCrashHandler {
    private static UTCrashHandler s_instance = new UTCrashHandler();

    public static UTCrashHandler getInstance() {
        return s_instance;
    }

    public void setCrashCaughtListener(IUTCrashCaughtListener pListener) {
        MotuCrashReporter.getInstance().setCrashCaughtListener(new UTCrashHandlerWapper(pListener));
    }
}
