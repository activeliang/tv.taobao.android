package com.ut.mini;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import com.alibaba.analytics.AnalyticsMgr;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.utils.Logger;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import com.ut.mini.module.appstatus.UTAppStatusCallbacks;
import java.util.List;
import java.util.Map;

class UTMI1010_2001Event implements UTAppStatusCallbacks {
    private long mHowLongForegroundStay = 0;
    private long mToBackgroundTimestamp = 0;
    private long mToForegroundTimestamp = 0;

    UTMI1010_2001Event() {
    }

    /* access modifiers changed from: package-private */
    public void onEventArrive(Object aMsgObject) {
        Map<String, String> lLogMap = (Map) aMsgObject;
        if (lLogMap.containsKey(LogField.EVENTID.toString()) && "2001".equals(lLogMap.get(LogField.EVENTID.toString()))) {
            long lDuration = 0;
            if (lLogMap.containsKey(LogField.ARG3.toString())) {
                try {
                    lDuration = Long.parseLong(lLogMap.get(LogField.ARG3.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.mHowLongForegroundStay += lDuration;
            if (_isSwitchBackgroundByGetTask()) {
                _send1010Hit(this.mHowLongForegroundStay);
                this.mHowLongForegroundStay = 0;
            }
        }
    }

    private static boolean _isSwitchBackgroundByGetTask() {
        String lPackageName;
        ActivityManager manager;
        ComponentName cn;
        try {
            Context lContext = ClientVariables.getInstance().getContext();
            if (!(lContext == null || (lPackageName = lContext.getPackageName()) == null || (manager = (ActivityManager) lContext.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)) == null)) {
                try {
                    List<ActivityManager.RunningTaskInfo> task_info = manager.getRunningTasks(1);
                    if (task_info != null && task_info.size() > 0 && (cn = task_info.get(0).topActivity) != null && lPackageName.contains(cn.getPackageName())) {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    private void _send1010Hit(long aHowLongForegroundStay) {
        if (!ClientVariables.getInstance().is1010AutoTrackClosed() && aHowLongForegroundStay > 0) {
            long lStayBackgroundDuration = 0;
            if (0 != this.mToBackgroundTimestamp) {
                lStayBackgroundDuration = SystemClock.elapsedRealtime() - this.mToBackgroundTimestamp;
            }
            UTOriginalCustomHitBuilder lHitBuilder = new UTOriginalCustomHitBuilder("UT", 1010, "" + aHowLongForegroundStay, "" + lStayBackgroundDuration, (String) null, (Map<String, String>) null);
            lHitBuilder.setProperty("_priority", "5");
            UTTracker lTracker = UTAnalytics.getInstance().getDefaultTracker();
            if (lTracker != null) {
                lTracker.send(lHitBuilder.build());
                return;
            }
            Logger.d("Record app display event error", "Fatal Error,must call setRequestAuthentication method first.");
        }
    }

    public void onSwitchBackground() {
        UTPageHitHelper.getInstance().pageSwitchBackground();
        _send1010Hit(SystemClock.elapsedRealtime() - this.mToForegroundTimestamp);
        this.mToBackgroundTimestamp = SystemClock.elapsedRealtime();
        AnalyticsMgr.dispatchLocalHits();
    }

    public void onSwitchForeground() {
        this.mToForegroundTimestamp = SystemClock.elapsedRealtime();
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityDestroyed(Activity activity) {
        UTPageHitHelper.getInstance().pageDestroyed(activity);
    }

    public void onActivityPaused(Activity activity) {
        UTPageHitHelper.getInstance().pageDisAppearByAuto(activity);
    }

    public void onActivityResumed(Activity activity) {
        UTPageHitHelper.getInstance().pageAppearByAuto(activity);
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }
}
