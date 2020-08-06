package com.taobao.alimama.threads;

import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Keep;
import com.alibaba.mtl.appmonitor.AppMonitor;

@Keep
public class AdThreadExecutor {
    public static void execute(Runnable runnable) {
        execute(runnable, 0);
    }

    public static void execute(final Runnable runnable, long j) {
        final long uptimeMillis = SystemClock.uptimeMillis();
        new Handler(AdLooper.getLooper()).postDelayed(new Runnable() {
            public void run() {
                AppMonitor.Alarm.commitSuccess("Munion", "ad_thread_waiting_time", String.valueOf(SystemClock.uptimeMillis() - uptimeMillis));
                runnable.run();
            }
        }, j);
    }
}
