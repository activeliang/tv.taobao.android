package com.taobao.alimama.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.alibaba.mtl.appmonitor.AppMonitor;

public class MainThreadExecutor {
    private static Handler a = new Handler(Looper.getMainLooper());

    public static void abortExecuting(Runnable runnable) {
        a.removeCallbacks(runnable);
    }

    public static void execute(Runnable runnable) {
        execute(runnable, 0);
    }

    public static void execute(final Runnable runnable, long j) {
        final long uptimeMillis = SystemClock.uptimeMillis();
        a.postDelayed(new Runnable() {
            public void run() {
                AppMonitor.Alarm.commitSuccess("Munion", "main_thread_waiting_time", String.valueOf(SystemClock.uptimeMillis() - uptimeMillis));
                runnable.run();
            }
        }, j);
    }
}
