package com.ut.mini.module.plugin;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import com.ut.mini.module.appstatus.UTAppStatusCallbacks;
import com.ut.mini.module.appstatus.UTAppStatusRegHelper;
import java.util.LinkedList;
import java.util.List;

public class UTPluginMgr implements UTAppStatusCallbacks {
    private static UTPluginMgr s_instance = new UTPluginMgr();
    private List<UTPlugin> mPluginList = new LinkedList();

    private UTPluginMgr() {
        if (Build.VERSION.SDK_INT >= 14) {
            UTAppStatusRegHelper.registerAppStatusCallbacks(this);
        }
    }

    public static UTPluginMgr getInstance() {
        return s_instance;
    }

    public void forEachPlugin(IUTPluginForEachDelegate aForEachDelegate) {
        if (aForEachDelegate != null) {
            try {
                for (UTPlugin lPlugin : this.mPluginList) {
                    aForEachDelegate.onPluginForEach(lPlugin);
                }
            } catch (Throwable th) {
            }
        }
    }

    public synchronized void registerPlugin(UTPlugin aPlugin) {
        if (!this.mPluginList.contains(aPlugin)) {
            this.mPluginList.add(aPlugin);
        }
    }

    public synchronized void unregisterPlugin(UTPlugin aPlugin) {
        this.mPluginList.remove(aPlugin);
    }

    public void onSwitchBackground() {
    }

    public void onSwitchForeground() {
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }
}
