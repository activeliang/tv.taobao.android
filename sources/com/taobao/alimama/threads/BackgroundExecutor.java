package com.taobao.alimama.threads;

import android.support.annotation.Keep;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Keep
public class BackgroundExecutor {
    private static Executor executor = Executors.newCachedThreadPool();

    public static void execute(Runnable runnable) {
        try {
            executor.execute(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
