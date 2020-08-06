package com.alibaba.motu.crashreporter.ignores;

public class FakeFinallzeExceptionIgnore implements UncaughtExceptionIgnore {
    public String getName() {
        return "FakeFinallzeExceptionIgnore";
    }

    public boolean uncaughtExceptionIgnore(Thread thread, Throwable throwable) {
        String threadName = thread.getName();
        if (("FinalizerDaemon".equals(threadName) || "FakeFinalizerDaemon".equals(threadName) || "FinalizerWatchdogDaemon".equals(threadName) || "FakeFinalizerWatchdogDaemon".equals(threadName)) && (throwable instanceof IllegalStateException) && ("not running".equals(throwable.getMessage()) || "already running".equals(throwable.getMessage()))) {
            return true;
        }
        return false;
    }
}
