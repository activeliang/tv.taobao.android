package com.ali.auth.third.core.task;

import android.annotation.TargetApi;
import android.os.AsyncTask;

public class CoordinatorWrapper<Params> {
    @TargetApi(11)
    public void execute(Runnable runnable) {
        if (runnable != null) {
            Coordinator.execute(runnable);
        }
    }

    private boolean executeWithSki(Runnable runnable) {
        try {
            Class.forName("com.taobao.android.task.Coordinator").getMethod("execute", new Class[]{Runnable.class}).invoke((Object) null, new Object[]{runnable});
            return true;
        } catch (ClassNotFoundException | Exception e) {
            return false;
        }
    }

    public void execute(AsyncTask asyncTask, Params... params) {
        if (asyncTask != null) {
            try {
                asyncTask.execute(params);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
