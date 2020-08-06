package com.ut.mini.anti_cheat;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.alibaba.analytics.utils.Logger;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.UTPageHitHelper;
import com.ut.mini.anti_cheat.ScreenshotDetector;
import com.ut.mini.module.appstatus.UTAppStatusCallbacks;
import com.ut.mini.module.appstatus.UTAppStatusRegHelper;

public class AntiCheatTracker implements UTAppStatusCallbacks, ScreenshotDetector.ScreenshotListener {
    private static AntiCheatTracker instance = new AntiCheatTracker();
    private boolean init = false;
    private Activity mActivity = null;
    private ScreenshotDetector mDetector = null;

    public static AntiCheatTracker getInstance() {
        return instance;
    }

    public void init(Application app) {
        Logger.i();
        if (!this.init) {
            this.init = true;
            this.mDetector = new ScreenshotDetector(app.getBaseContext());
            UTAppStatusRegHelper.registerAppStatusCallbacks(this);
        }
    }

    public void onSwitchBackground() {
        if (this.mDetector != null) {
            this.mDetector.stop();
        }
    }

    public void onSwitchForeground() {
        if (this.mDetector != null) {
            this.mDetector.start(this);
        }
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
        this.mActivity = null;
    }

    public void onActivityResumed(Activity activity) {
        this.mActivity = activity;
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onScreenCaptured(String path) {
        Logger.i();
        try {
            String utPageName = UTPageHitHelper.getInstance().getCurrentPageName();
            String nContainName = "";
            if (this.mActivity != null) {
                nContainName = this.mActivity.getClass().getCanonicalName();
            }
            UTHitBuilders.UTCustomHitBuilder builder = new UTHitBuilders.UTCustomHitBuilder("screen_capture");
            builder.setEventPage("anti_cheat");
            builder.setProperty("page_name", utPageName);
            builder.setProperty("contain_name", nContainName);
            builder.setProperty("current_time", System.currentTimeMillis() + "");
            UTAnalytics.getInstance().getDefaultTracker().send(builder.build());
        } catch (Throwable th) {
        }
    }

    public void onScreenCapturedWithDeniedPermission() {
    }
}
