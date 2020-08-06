package com.ut.mini.internal;

import android.util.Log;
import com.alibaba.analytics.utils.ILogger;
import com.taobao.tlog.adapter.AdapterForTLog;
import java.util.HashMap;

public class LogAdapter implements ILogger {
    private boolean isNoClassDefFoundError = false;
    private HashMap<String, Integer> mTlogMap = new HashMap<>();

    public LogAdapter() {
        this.mTlogMap.put("V", 5);
        this.mTlogMap.put("D", 4);
        this.mTlogMap.put("I", 3);
        this.mTlogMap.put("W", 2);
        this.mTlogMap.put("E", 1);
        this.mTlogMap.put("L", 0);
    }

    public boolean isValid() {
        if (this.isNoClassDefFoundError) {
            return false;
        }
        try {
            return AdapterForTLog.isValid();
        } catch (Throwable th) {
            Log.d("Analytics", "java.lang.NoClassDefFoundError: Failed resolution of: Lcom/taobao/tlog/adapter/AdapterForTLog");
            this.isNoClassDefFoundError = true;
            return false;
        }
    }

    public int getLogLevel() {
        return this.mTlogMap.get(AdapterForTLog.getLogLevel("Analytics")).intValue();
    }

    public void logd(String tag, String content) {
        AdapterForTLog.logd(tag, content);
    }

    public void logw(String tag, String content) {
        AdapterForTLog.logw(tag, content);
    }

    public void logw(String tag, String content, Throwable throwable) {
        AdapterForTLog.logw(tag, content, throwable);
    }

    public void logi(String tag, String content) {
        AdapterForTLog.logi(tag, content);
    }

    public void loge(String tag, String content) {
        AdapterForTLog.loge(tag, content);
    }

    public void loge(String tag, String content, Throwable throwable) {
        AdapterForTLog.loge(tag, content, throwable);
    }
}
