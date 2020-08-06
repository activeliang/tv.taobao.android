package com.taobao.alimama.utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Keep;
import com.ut.mini.module.appstatus.UTAppStatusCallbacks;

@Keep
public class UTAppBackgroundTimeoutDetector implements UTAppStatusCallbacks {
    private static UTAppBackgroundTimeoutDetector sInstance = null;
    private boolean mShouldRewriteTpkCache = true;
    private long mSwitchBackgroundTimestamp;

    public static UTAppBackgroundTimeoutDetector getInstance() {
        if (sInstance == null) {
            sInstance = new UTAppBackgroundTimeoutDetector();
        }
        return sInstance;
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onSwitchBackground() {
        this.mSwitchBackgroundTimestamp = SystemClock.elapsedRealtime();
    }

    public void onSwitchForeground() {
        if (0 != this.mSwitchBackgroundTimestamp && SystemClock.elapsedRealtime() - this.mSwitchBackgroundTimestamp > 600000) {
            this.mShouldRewriteTpkCache = true;
        }
        this.mSwitchBackgroundTimestamp = 0;
    }

    public void setShouldRewriteTpkCache(boolean z) {
        this.mShouldRewriteTpkCache = z;
    }

    public boolean shouldRewriteTpkCache() {
        return this.mShouldRewriteTpkCache;
    }
}
