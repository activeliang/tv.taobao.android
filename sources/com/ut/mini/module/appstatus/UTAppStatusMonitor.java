package com.ut.mini.module.appstatus;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.alibaba.analytics.utils.TaskExecutor;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@TargetApi(14)
public class UTAppStatusMonitor implements Application.ActivityLifecycleCallbacks {
    private static UTAppStatusMonitor s_instance = new UTAppStatusMonitor();
    private int mActivitiesActive = 0;
    /* access modifiers changed from: private */
    public List<UTAppStatusCallbacks> mAppStatusCallbacksList = new LinkedList();
    /* access modifiers changed from: private */
    public Object mAppStatusCallbacksLockObj = new Object();
    private Object mApplicationStatusLockObj = new Object();
    private ScheduledFuture<?> mApplicationStatusScheduledFuture = null;
    /* access modifiers changed from: private */
    public boolean mIsInForeground = false;

    private UTAppStatusMonitor() {
    }

    public static UTAppStatusMonitor getInstance() {
        return s_instance;
    }

    public void registerAppStatusCallbacks(UTAppStatusCallbacks aCallbacks) {
        if (aCallbacks != null) {
            synchronized (this.mAppStatusCallbacksLockObj) {
                this.mAppStatusCallbacksList.add(aCallbacks);
            }
        }
    }

    public void unregisterAppStatusCallbacks(UTAppStatusCallbacks aCallbacks) {
        if (aCallbacks != null) {
            synchronized (this.mAppStatusCallbacksLockObj) {
                this.mAppStatusCallbacksList.remove(aCallbacks);
            }
        }
    }

    private void _clearApplicationStatusCheckExistingTimer() {
        synchronized (this.mApplicationStatusLockObj) {
            if (this.mApplicationStatusScheduledFuture != null) {
                this.mApplicationStatusScheduledFuture.cancel(true);
            }
        }
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        synchronized (this.mAppStatusCallbacksLockObj) {
            for (int i = 0; i < this.mAppStatusCallbacksList.size(); i++) {
                this.mAppStatusCallbacksList.get(i).onActivityCreated(activity, savedInstanceState);
            }
        }
    }

    public void onActivityDestroyed(Activity activity) {
        synchronized (this.mAppStatusCallbacksLockObj) {
            for (int i = 0; i < this.mAppStatusCallbacksList.size(); i++) {
                this.mAppStatusCallbacksList.get(i).onActivityDestroyed(activity);
            }
        }
    }

    public void onActivityPaused(Activity activity) {
        synchronized (this.mAppStatusCallbacksLockObj) {
            for (int i = 0; i < this.mAppStatusCallbacksList.size(); i++) {
                this.mAppStatusCallbacksList.get(i).onActivityPaused(activity);
            }
        }
    }

    public void onActivityResumed(Activity activity) {
        synchronized (this.mAppStatusCallbacksLockObj) {
            for (int i = 0; i < this.mAppStatusCallbacksList.size(); i++) {
                this.mAppStatusCallbacksList.get(i).onActivityResumed(activity);
            }
        }
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        synchronized (this.mAppStatusCallbacksLockObj) {
            for (int i = 0; i < this.mAppStatusCallbacksList.size(); i++) {
                this.mAppStatusCallbacksList.get(i).onActivitySaveInstanceState(activity, outState);
            }
        }
    }

    public void onActivityStarted(Activity activity) {
        _clearApplicationStatusCheckExistingTimer();
        this.mActivitiesActive++;
        if (!this.mIsInForeground) {
            synchronized (this.mAppStatusCallbacksLockObj) {
                for (int i = 0; i < this.mAppStatusCallbacksList.size(); i++) {
                    this.mAppStatusCallbacksList.get(i).onSwitchForeground();
                }
            }
        }
        this.mIsInForeground = true;
    }

    public void onActivityStopped(Activity activity) {
        this.mActivitiesActive--;
        if (this.mActivitiesActive == 0) {
            _clearApplicationStatusCheckExistingTimer();
            this.mApplicationStatusScheduledFuture = TaskExecutor.getInstance().schedule((ScheduledFuture) null, new NotInForegroundRunnable(), 1000);
        }
    }

    private class NotInForegroundRunnable implements Runnable {
        private NotInForegroundRunnable() {
        }

        public void run() {
            boolean unused = UTAppStatusMonitor.this.mIsInForeground = false;
            synchronized (UTAppStatusMonitor.this.mAppStatusCallbacksLockObj) {
                for (int i = 0; i < UTAppStatusMonitor.this.mAppStatusCallbacksList.size(); i++) {
                    ((UTAppStatusCallbacks) UTAppStatusMonitor.this.mAppStatusCallbacksList.get(i)).onSwitchBackground();
                }
            }
        }
    }
}
