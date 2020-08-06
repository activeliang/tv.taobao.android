package com.ut.mini.crashhandler;

import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;
import java.util.Map;

public class UTCrashHandlerWapper implements IUTCrashCaughtListener {
    private IUTCrashCaughtListener crashCaughtListener;

    public UTCrashHandlerWapper(IUTCrashCaughtListener crashCaughtListener2) {
        this.crashCaughtListener = crashCaughtListener2;
    }

    public Map<String, Object> onCrashCaught(Thread pThread, Throwable pException) {
        if (this.crashCaughtListener != null) {
            return this.crashCaughtListener.onCrashCaught(pThread, pException);
        }
        return null;
    }
}
