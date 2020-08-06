package anet.channel.util;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import anet.channel.GlobalAppRuntimeInfo;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

public class AppLifecycle {
    public static volatile long lastEnterBackgroundTime = 0;
    private static CopyOnWriteArraySet<AppLifecycleListener> listeners = new CopyOnWriteArraySet<>();
    private static Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
            AppLifecycle.onForeground();
        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivityStopped(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityDestroyed(Activity activity) {
        }
    };
    private static ComponentCallbacks2 mComponentCallbacks2 = new ComponentCallbacks2() {
        public static final String TAG = "awcn.ComponentCallbacks2";

        public void onTrimMemory(int level) {
            ALog.i(TAG, "onTrimMemory", (String) null, "level", Integer.valueOf(level));
            if (level == 20) {
                AppLifecycle.onBackground();
            }
        }

        public void onConfigurationChanged(Configuration newConfig) {
        }

        public void onLowMemory() {
        }
    };

    public interface AppLifecycleListener {
        void background();

        void forground();
    }

    private AppLifecycle() {
    }

    public static void initialize() {
        if (Build.VERSION.SDK_INT >= 14) {
            ((Application) GlobalAppRuntimeInfo.getContext().getApplicationContext()).registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            GlobalAppRuntimeInfo.getContext().registerComponentCallbacks(mComponentCallbacks2);
        }
    }

    public static void registerLifecycleListener(AppLifecycleListener lifecycleListener) {
        if (lifecycleListener != null) {
            listeners.add(lifecycleListener);
        }
    }

    public static void unregisterLifecycleListener(AppLifecycleListener lifecycleListener) {
        listeners.remove(lifecycleListener);
    }

    public static void onForeground() {
        if (GlobalAppRuntimeInfo.isBackground) {
            GlobalAppRuntimeInfo.isBackground = false;
            Iterator i$ = listeners.iterator();
            while (i$.hasNext()) {
                i$.next().forground();
            }
        }
    }

    public static void onBackground() {
        if (!GlobalAppRuntimeInfo.isBackground) {
            GlobalAppRuntimeInfo.isBackground = true;
            lastEnterBackgroundTime = System.currentTimeMillis();
            Iterator i$ = listeners.iterator();
            while (i$.hasNext()) {
                i$.next().background();
            }
        }
    }
}
