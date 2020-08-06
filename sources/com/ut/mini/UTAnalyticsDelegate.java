package com.ut.mini;

import android.app.Application;
import android.os.RemoteException;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.store.LogStoreMgr;
import com.alibaba.analytics.utils.Logger;
import com.ut.mini.core.UTLogTransferMain;
import com.ut.mini.internal.UTTeamWork;
import java.util.HashMap;
import java.util.Map;

public final class UTAnalyticsDelegate {
    private static UTAnalyticsDelegate s_instance;
    private Application mApplication;
    private UTTracker mDefaultTracker;
    private Map<String, UTTracker> mTrackerMap = new HashMap();

    public static synchronized UTAnalyticsDelegate getInstance() {
        UTAnalyticsDelegate uTAnalyticsDelegate;
        synchronized (UTAnalyticsDelegate.class) {
            if (s_instance == null) {
                s_instance = new UTAnalyticsDelegate();
            }
            uTAnalyticsDelegate = s_instance;
        }
        return uTAnalyticsDelegate;
    }

    private UTAnalyticsDelegate() {
    }

    public void initUT(Application application) {
        this.mApplication = application;
        UTTeamWork.getInstance().initialized();
    }

    public void setAppVersion(String aAppVersion) {
        Variables.getInstance().setAppVersion(aAppVersion);
    }

    public void setChannel(String aChannel) {
        Logger.d((String) null, "channel", aChannel);
        Variables.getInstance().setChannel(aChannel);
    }

    public void turnOnDebug() {
        Variables.getInstance().turnOnDebug();
    }

    public void updateUserAccount(String aUsernick, String aUserid, String openid) {
        Variables.getInstance().updateUserAccount(aUsernick, aUserid, openid);
    }

    public void updateSessionProperties(Map aMap) {
        Map<String, String> lOldMap = Variables.getInstance().getSessionProperties();
        Map<String, String> lNewMap = new HashMap<>();
        if (lOldMap != null) {
            lNewMap.putAll(lOldMap);
        }
        if (aMap != null) {
            lNewMap.putAll(aMap);
        }
        Variables.getInstance().setSessionProperties(lNewMap);
    }

    public void transferLog(Map<String, String> aLogMap) {
        UTLogTransferMain.getInstance().transferLog(aLogMap);
    }

    public void turnOnRealTimeDebug(Map aMap) throws RemoteException {
        Variables.getInstance().turnOnRealTimeDebug(aMap);
    }

    public void turnOffRealTimeDebug() throws RemoteException {
        Variables.getInstance().turnOffRealTimeDebug();
    }

    public void saveCacheDataToLocal() throws RemoteException {
        LogStoreMgr.getInstance().store();
    }

    public void setSessionProperties(Map aMap) {
        Variables.getInstance().setSessionProperties(aMap);
    }
}
