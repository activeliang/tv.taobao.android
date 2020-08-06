package com.uc.webview.export.cyclone;

import android.os.SystemClock;

/* compiled from: ProGuard */
public class UCElapseTime {
    private long a = SystemClock.uptimeMillis();
    private long b = SystemClock.currentThreadTimeMillis();

    public long getMilis() {
        return SystemClock.uptimeMillis() - this.a;
    }

    public long getMilisCpu() {
        return SystemClock.currentThreadTimeMillis() - this.b;
    }
}
