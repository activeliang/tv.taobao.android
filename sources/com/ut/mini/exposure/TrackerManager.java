package com.ut.mini.exposure;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.Application;
import android.app.TabActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.view.ViewGroup;
import com.ut.mini.internal.ExposureViewHandle;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TrackerManager {
    private static TrackerManager instance = new TrackerManager();
    public HashMap<String, String> commonInfoMap = new HashMap<>();
    private ActivityLifecycleForTracker mActivityLifecycle;
    private ExposureViewHandle mExposureViewHandle;
    private Handler mHandle;
    /* access modifiers changed from: private */
    public Set<Class> mNeedToTrackActivitys = Collections.synchronizedSet(new HashSet());

    private TrackerManager() {
        if (this.mHandle == null) {
            HandlerThread ht = new HandlerThread("ut_exposure");
            ht.start();
            this.mHandle = new Handler(ht.getLooper());
        }
    }

    public static TrackerManager getInstance() {
        return instance;
    }

    public void init(Application application) {
        if (ExposureConfigMgr.trackerExposureOpen) {
            this.mActivityLifecycle = new ActivityLifecycleForTracker();
            application.registerActivityLifecycleCallbacks(this.mActivityLifecycle);
        }
        ExposureConfigMgr.init();
    }

    public void unregisterActivityLifecycleCallbacks(Application application) {
        if (this.mActivityLifecycle != null) {
            application.unregisterActivityLifecycleCallbacks(this.mActivityLifecycle);
        }
    }

    public void setCommonInfoMap(HashMap<String, String> commonMap) {
        this.commonInfoMap.clear();
        this.commonInfoMap.putAll(commonMap);
    }

    public Handler getThreadHandle() {
        return this.mHandle;
    }

    private class ActivityLifecycleForTracker implements Application.ActivityLifecycleCallbacks {
        private ActivityLifecycleForTracker() {
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
            if (activity != null && !(activity instanceof TabActivity) && !(activity instanceof ActivityGroup)) {
                if (("com.taobao.weex.WXActivity".equalsIgnoreCase(activity.getClass().getName()) || TrackerManager.this.mNeedToTrackActivitys.contains(activity.getClass())) && ExposureConfigMgr.trackerExposureOpen) {
                    try {
                        ViewGroup container = (ViewGroup) activity.findViewById(16908290);
                        if (container != null && container.getChildCount() > 0) {
                            if (container.getChildAt(0) instanceof TrackerFrameLayout) {
                                ExpLogger.d((String) null, "no attachTrackerFrameLayout ", activity.toString());
                                return;
                            }
                            TrackerFrameLayout trackerFrameLayout = new TrackerFrameLayout(activity);
                            while (container.getChildCount() > 0) {
                                View view = container.getChildAt(0);
                                container.removeViewAt(0);
                                trackerFrameLayout.addView(view, view.getLayoutParams());
                            }
                            container.addView(trackerFrameLayout, new ViewGroup.LayoutParams(-1, -1));
                        }
                    } catch (Exception e) {
                        ExpLogger.e((String) null, e, new Object[0]);
                    }
                }
            }
        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivityStopped(Activity activity) {
        }

        public void onActivityDestroyed(Activity activity) {
            if (activity != null && !(activity instanceof TabActivity) && !(activity instanceof ActivityGroup)) {
                if ("com.taobao.weex.WXActivity".equalsIgnoreCase(activity.getClass().getName()) || TrackerManager.this.mNeedToTrackActivitys.contains(activity.getClass())) {
                    try {
                        ViewGroup container = (ViewGroup) activity.findViewById(16908290);
                        if (container != null && (container.getChildAt(0) instanceof TrackerFrameLayout)) {
                            container.removeViewAt(0);
                        }
                    } catch (Exception e) {
                        ExpLogger.e((String) null, e, new Object[0]);
                    }
                }
            }
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }
    }

    public void registerExposureViewHandler(ExposureViewHandle handle) {
        this.mExposureViewHandle = handle;
    }

    public void unRegisterExposureViewHandler(ExposureViewHandle handle) {
        this.mExposureViewHandle = null;
    }

    public ExposureViewHandle getExposureViewHandle() {
        return this.mExposureViewHandle;
    }

    public boolean addToTrack(Activity activity) {
        if (activity == null) {
            return false;
        }
        return this.mNeedToTrackActivitys.add(activity.getClass());
    }

    public boolean removeToTrack(Activity activity) {
        if (activity == null) {
            return false;
        }
        return this.mNeedToTrackActivitys.remove(activity.getClass());
    }

    public void enableExposureLog(boolean enable) {
        ExpLogger.enableLog = enable;
    }
}
