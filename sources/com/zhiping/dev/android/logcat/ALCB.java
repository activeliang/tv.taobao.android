package com.zhiping.dev.android.logcat;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.lang.ref.WeakReference;

public class ALCB implements Application.ActivityLifecycleCallbacks {
    private WeakReference<Activity> top;

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public Activity getTop() {
        if (this.top != null) {
            return (Activity) this.top.get();
        }
        return null;
    }
}
