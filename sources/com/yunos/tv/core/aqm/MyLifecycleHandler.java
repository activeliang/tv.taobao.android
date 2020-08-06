package com.yunos.tv.core.aqm;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tv.core.degrade.PageStackDegradeManager;
import com.zhiping.dev.android.logger.ZpLogger;

public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = MyLifecycleHandler.class.getSimpleName();
    private static int createCount;
    private static int destroyCount;
    private static int pauseCount;
    private static int resumeCount;
    private static int startCount;
    private static int stopCount;

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        createCount++;
        ZpLogger.i(TAG, getOBJ(activity) + ".onActivityCreated()");
        ActivityQueueManager.getInstance().onCreated(activity);
    }

    public void onActivityStarted(Activity activity) {
        startCount++;
        ZpLogger.i(TAG, getOBJ(activity) + ".onActivityStarted()");
    }

    public void onActivityResumed(Activity activity) {
        resumeCount++;
        ZpLogger.i(TAG, getOBJ(activity) + ".onActivityResumed()");
        ActivityQueueManager.getInstance().onResumed(activity);
        PageStackDegradeManager.getInstance().check();
    }

    public void onActivityPaused(Activity activity) {
        pauseCount++;
        ZpLogger.i(TAG, getOBJ(activity) + ".onActivityPaused()");
    }

    public void onActivityStopped(Activity activity) {
        stopCount++;
        ZpLogger.i(TAG, getOBJ(activity) + ".onActivityStopped()");
    }

    public void onActivityDestroyed(Activity activity) {
        destroyCount++;
        ZpLogger.i(TAG, getOBJ(activity) + ".onActivityDestroyed()");
        ActivityQueueManager.getInstance().onDestroyed(activity);
        PageStackDegradeManager.getInstance().check();
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        ZpLogger.i(TAG, getOBJ(activity) + ".onActivitySaveInstanceState()");
    }

    public boolean isApplicationVisible() {
        return startCount > stopCount;
    }

    public boolean isApplicationInForeground() {
        return resumeCount > pauseCount;
    }

    public boolean isLastActivityInForeground() {
        ZpLogger.e(TAG, "createCount==" + createCount + "---------destroyCount" + destroyCount);
        if (createCount - destroyCount == 1) {
            return true;
        }
        return false;
    }

    private String getOBJ(Activity activity) {
        if (activity != null) {
            return activity.getClass().getName() + Constant.NLP_CACHE_TYPE + Integer.toHexString(hashCode());
        }
        return Constant.NULL;
    }
}
