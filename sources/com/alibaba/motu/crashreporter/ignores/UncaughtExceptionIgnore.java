package com.alibaba.motu.crashreporter.ignores;

public interface UncaughtExceptionIgnore {
    String getName();

    boolean uncaughtExceptionIgnore(Thread thread, Throwable th);
}
