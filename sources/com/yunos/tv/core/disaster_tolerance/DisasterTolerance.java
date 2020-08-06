package com.yunos.tv.core.disaster_tolerance;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;
import com.alibaba.motu.crashreporter.MotuHelper;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.config.Config;
import com.zhiping.dev.android.logcat.ZpLogCat;
import java.util.WeakHashMap;

public class DisasterTolerance {
    private static final String TAG = DisasterTolerance.class.getSimpleName();
    private static DisasterTolerance ourInstance;
    private boolean enable;
    /* access modifiers changed from: private */
    public WeakHashMap<Looper, ExceptionFilter> leWeakHashMap;

    public interface ExceptionFilter {
        boolean filterException(Throwable th);
    }

    public static DisasterTolerance getInstance() {
        if (ourInstance == null) {
            synchronized (DisasterTolerance.class) {
                if (ourInstance == null) {
                    ourInstance = new DisasterTolerance();
                }
            }
        }
        return ourInstance;
    }

    private DisasterTolerance() {
        this.enable = false;
        this.leWeakHashMap = new WeakHashMap<>();
        this.enable = SharePreferences.getBoolean("isDisasterToleranceOn", false).booleanValue();
        if (Config.isDebug()) {
            this.enable = true;
        }
    }

    public boolean isEnable() {
        return this.enable;
    }

    public static void destroy() {
        synchronized (DisasterTolerance.class) {
            if (ourInstance != null) {
                ourInstance.leWeakHashMap.clear();
            }
            ourInstance = null;
        }
    }

    public void catchLooperException(Looper looper, ExceptionFilter exceptionFilter) {
        if (isEnable() && looper != null) {
            if (!this.leWeakHashMap.containsKey(looper)) {
                new Handler(looper).post(new Runnable() {
                    private void doOriginLoop() {
                        try {
                            Looper.myLooper();
                            Looper.loop();
                        } catch (Throwable e) {
                            ExceptionFilter exceptionFilter1 = (ExceptionFilter) DisasterTolerance.this.leWeakHashMap.get(Looper.myLooper());
                            if (exceptionFilter1 == null || !exceptionFilter1.filterException(e)) {
                                throw e;
                            }
                            doOriginLoop();
                        }
                    }

                    public void run() {
                        doOriginLoop();
                    }
                });
            }
            this.leWeakHashMap.put(looper, exceptionFilter);
        }
    }

    public boolean catchRequestDoneException(Throwable throwable, String... params) {
        if (!isEnable() || throwable == null) {
            return false;
        }
        report(throwable, params);
        if (Config.isDebug()) {
            Toast.makeText(CoreApplication.getApplication(), "监测到接口数据使用异常，已上报！\n开发人员正在紧急处理这个问题...", 1).show();
        }
        return true;
    }

    public boolean catchKeyEventException(Throwable throwable, String... params) {
        if (!isEnable() || throwable == null) {
            return false;
        }
        report(throwable, params);
        if (Config.isDebug()) {
            Toast.makeText(CoreApplication.getApplication(), "监测到按键事件处理异常，已上报！\n开发人员正在紧急处理这个问题...", 1).show();
        }
        return true;
    }

    public boolean catchNullPointerException(Throwable throwable, String... params) {
        if (!isEnable() || throwable == null) {
            return false;
        }
        report(throwable, params);
        if (Config.isDebug()) {
            Toast.makeText(CoreApplication.getApplication(), "监测到空指针异常，已上报！\n开发人员正在紧急处理这个问题...", 1).show();
        }
        return true;
    }

    public boolean catchClassCastException(Throwable throwable, String... params) {
        if (!isEnable() || throwable == null) {
            return false;
        }
        report(throwable, params);
        if (Config.isDebug()) {
            Toast.makeText(CoreApplication.getApplication(), "监测到类型转换异常，已上报！\n开发人员正在紧急处理这个问题...", 1).show();
        }
        return true;
    }

    public boolean catchException(Throwable throwable, String tip, String... params) {
        if (!isEnable() || throwable == null) {
            return false;
        }
        report(throwable, params);
        if (!TextUtils.isEmpty(tip) && Config.isDebug()) {
            Toast.makeText(CoreApplication.getApplication(), tip, 1).show();
        }
        return true;
    }

    private void report(Throwable throwable, String... params) {
        if (throwable != null) {
            throwable.printStackTrace();
            ZpLogCat.getInstance((Application) null).recordException(true, (Thread) null, throwable, params);
            MotuHelper.report(throwable, params);
        }
    }
}
