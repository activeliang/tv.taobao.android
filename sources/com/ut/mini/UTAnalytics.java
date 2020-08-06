package com.ut.mini;

import android.app.Application;
import android.os.Build;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.analytics.AnalyticsMgr;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.core.config.UTClientConfigMgr;
import com.alibaba.analytics.core.config.UTTPKBiz;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.SpSetting;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.analytics.version.UTBuildInfo;
import com.ut.mini.anti_cheat.AntiCheatTracker;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import com.ut.mini.core.sign.UTBaseRequestAuthentication;
import com.ut.mini.core.sign.UTSecurityThridRequestAuthentication;
import com.ut.mini.crashhandler.UTMiniCrashHandler;
import com.ut.mini.exposure.TrackerManager;
import com.ut.mini.extend.TLogExtend;
import com.ut.mini.extend.UTExtendSwitch;
import com.ut.mini.extend.WindvaneExtend;
import com.ut.mini.internal.RealtimeDebugSwitch;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import com.ut.mini.module.UTOperationStack;
import com.ut.mini.module.appstatus.UTAppBackgroundTimeoutDetector;
import com.ut.mini.module.appstatus.UTAppStatusRegHelper;
import com.ut.mini.module.plugin.UTPlugin;
import com.ut.mini.module.plugin.UTPluginMgr;
import java.util.HashMap;
import java.util.Map;

public class UTAnalytics {
    private static final String TAG = "UTAnalytics";
    private static volatile boolean mInit = false;
    private static volatile boolean mInit4app = false;
    private static UTAnalytics s_instance = new UTAnalytics();
    private HashMap<String, UTTracker> mAppkeyTrackMap = new HashMap<>();
    private UTTracker mDefaultTracker;
    private Map<String, UTTracker> mTrackerMap = new HashMap();

    public static void setDisableWindvane(boolean bDisableWindvane) {
        UTExtendSwitch.bWindvaneExtend = !bDisableWindvane;
    }

    private UTAnalytics() {
    }

    public static UTAnalytics getInstance() {
        return s_instance;
    }

    private void initialize(Application application, IUTApplication utCallback, boolean app) {
        Logger.i("", "[i_initialize] start...");
        setAppVersion(utCallback.getUTAppVersion());
        setChannel(utCallback.getUTChannel());
        if (utCallback.isAliyunOsSystem()) {
            getInstance().setToAliyunOsPlatform();
        }
        if (utCallback.isUTCrashHandlerDisable()) {
            UTMiniCrashHandler.getInstance().turnOff();
        } else {
            UTMiniCrashHandler.getInstance().turnOn(application.getApplicationContext());
            if (utCallback.getUTCrashCraughtListener() != null) {
                UTMiniCrashHandler.getInstance().setCrashCaughtListener(utCallback.getUTCrashCraughtListener());
            }
        }
        if (utCallback.isUTLogEnable()) {
            turnOnDebug();
        }
        if (!mInit || app) {
            setRequestAuthentication(utCallback.getUTRequestAuthInstance());
        }
        if (!mInit) {
            UTMI1010_2001Event l1010and2001EventInstance = new UTMI1010_2001Event();
            UTVariables.getInstance().setUTMI1010_2001EventInstance(l1010and2001EventInstance);
            if (Build.VERSION.SDK_INT >= 14) {
                UTAppStatusRegHelper.registeActivityLifecycleCallbacks(application);
                UTAppStatusRegHelper.registerAppStatusCallbacks(UTAppBackgroundTimeoutDetector.getInstance());
                UTAppStatusRegHelper.registerAppStatusCallbacks(l1010and2001EventInstance);
                UTAppStatusRegHelper.registerAppStatusCallbacks(new RealtimeDebugSwitch());
                TrackerManager.getInstance().init(application);
                AntiCheatTracker.getInstance().init(application);
            }
        }
    }

    public void registerWindvane() {
        WindvaneExtend.registerWindvane(mInit);
    }

    public void setAppApplicationInstance(Application application, IUTApplication utCallback) {
        try {
            if (!mInit4app) {
                if (application == null || utCallback == null || application.getBaseContext() == null) {
                    throw new IllegalArgumentException("application and callback must not be null");
                }
                ClientVariables.getInstance().setContext(application.getBaseContext());
                UTClientConfigMgr.getInstance().init();
                TLogExtend.registerTLog();
                AnalyticsMgr.init(application);
                initialize(application, utCallback, true);
                registerWindvane();
                mInit = true;
                mInit4app = true;
            }
        } catch (Throwable th) {
        }
    }

    public void setAppApplicationInstance4sdk(Application application, IUTApplication utCallback) {
        try {
            if (!mInit) {
                if (application == null || utCallback == null || application.getBaseContext() == null) {
                    throw new IllegalArgumentException("application and callback must not be null");
                }
                ClientVariables.getInstance().setContext(application.getBaseContext());
                UTClientConfigMgr.getInstance().init();
                TLogExtend.registerTLog();
                AnalyticsMgr.init(application);
                initialize(application, utCallback, false);
                registerWindvane();
                mInit = true;
            }
        } catch (Throwable th) {
        }
    }

    private boolean checkInit() {
        if (!AnalyticsMgr.isInit) {
            Logger.w("Please call  () before call other method", new Object[0]);
        }
        return AnalyticsMgr.isInit;
    }

    private void setRequestAuthentication(IUTRequestAuthentication aRequestAuthenticationInstance) {
        String appKey;
        String secret;
        boolean isSecurity;
        Logger.i(TAG, "[setRequestAuthentication] start...", UTBuildInfo.getInstance().getFullSDKVersion(), Boolean.valueOf(AnalyticsMgr.isInit));
        if (aRequestAuthenticationInstance == null) {
            throw new NullPointerException("签名不能为空!");
        }
        boolean isEncode = false;
        if (aRequestAuthenticationInstance instanceof UTSecurityThridRequestAuthentication) {
            UTSecurityThridRequestAuthentication temp = (UTSecurityThridRequestAuthentication) aRequestAuthenticationInstance;
            appKey = temp.getAppkey();
            secret = temp.getAuthcode();
            isSecurity = true;
        } else if (aRequestAuthenticationInstance instanceof UTBaseRequestAuthentication) {
            UTBaseRequestAuthentication temp2 = (UTBaseRequestAuthentication) aRequestAuthenticationInstance;
            appKey = temp2.getAppkey();
            secret = temp2.getAppSecret();
            isSecurity = false;
            isEncode = temp2.isEncode();
        } else {
            throw new IllegalArgumentException("此签名方式暂不支持!请使用 UTSecuritySDKRequestAuthentication 或 UTBaseRequestAuthentication 设置签名!");
        }
        ClientVariables.getInstance().setAppKey(appKey);
        AnalyticsMgr.setRequestAuthInfo(isSecurity, isEncode, appKey, secret);
    }

    private void setAppVersion(String aAppVersion) {
        AnalyticsMgr.setAppVersion(aAppVersion);
    }

    private void setChannel(final String aChannel) {
        AnalyticsMgr.setChanel(aChannel);
        try {
            AnalyticsMgr.handler.postWatingTask(new Runnable() {
                public void run() {
                    SpSetting.put(ClientVariables.getInstance().getContext(), "channel", aChannel);
                }
            });
        } catch (Throwable th) {
        }
    }

    private void turnOffCrashHandler() {
        UTMiniCrashHandler.getInstance().turnOff();
    }

    private void turnOnDebug() {
        AnalyticsMgr.turnOnDebug();
    }

    public void registerPlugin(UTPlugin aPlugin) {
        UTPluginMgr.getInstance().registerPlugin(aPlugin);
    }

    public void unregisterPlugin(UTPlugin aPlugin) {
        UTPluginMgr.getInstance().unregisterPlugin(aPlugin);
    }

    @Deprecated
    public void updateUserAccount(String aUsernick, String aUserid) {
        try {
            throw new Exception("this interface is Deprecated，please call UTAnalytics.getInstance().updateUserAccount(String aUsernick, String aUserid,String openid)");
        } catch (Throwable e) {
            Log.w("Analytics", "", e);
            updateUserAccount(aUsernick, aUserid, (String) null);
        }
    }

    public void updateUserAccount(String aUsernick, String aUserid, String openid) {
        AnalyticsMgr.updateUserAccount(aUsernick, aUserid, openid);
        if (!StringUtils.isEmpty(aUsernick)) {
            UTOriginalCustomHitBuilder lBuilder = new UTOriginalCustomHitBuilder("UT", 1007, aUsernick, aUserid, (String) null, (Map<String, String>) null);
            lBuilder.setProperty("_priority", "5");
            getInstance().getDefaultTracker().send(lBuilder.build());
        }
    }

    public void userRegister(String aUsernick) {
        if (!StringUtils.isEmpty(aUsernick)) {
            getDefaultTracker().send(new UTOriginalCustomHitBuilder("UT", 1006, aUsernick, (String) null, (String) null, (Map<String, String>) null).build());
            return;
        }
        throw new IllegalArgumentException("Usernick can not be null or empty!");
    }

    public void updateSessionProperties(Map<String, String> aMap) {
        AnalyticsMgr.updateSessionProperties(aMap);
    }

    public void sessionTimeout() {
        UTTPKBiz.getInstance().sessionTimeout();
    }

    public void turnOffAutoPageTrack() {
        UTPageHitHelper.getInstance().turnOffAutoPageTrack();
    }

    public synchronized UTTracker getDefaultTracker() {
        if (this.mDefaultTracker == null && !TextUtils.isEmpty(ClientVariables.getInstance().getAppKey())) {
            this.mDefaultTracker = new UTTracker();
        }
        if (this.mDefaultTracker == null) {
            throw new RuntimeException("getDefaultTracker error,must call setRequestAuthentication method first");
        }
        return this.mDefaultTracker;
    }

    public synchronized UTTracker getTracker(String aTrackId) {
        UTTracker uTTracker;
        if (StringUtils.isEmpty(aTrackId)) {
            throw new IllegalArgumentException("TrackId is null");
        } else if (this.mTrackerMap.containsKey(aTrackId)) {
            uTTracker = this.mTrackerMap.get(aTrackId);
        } else {
            UTTracker lTracker = new UTTracker();
            lTracker.setTrackId(aTrackId);
            this.mTrackerMap.put(aTrackId, lTracker);
            uTTracker = lTracker;
        }
        return uTTracker;
    }

    public synchronized UTTracker getTrackerByAppkey(String appkey) {
        UTTracker uTTracker;
        if (StringUtils.isEmpty(appkey)) {
            throw new IllegalArgumentException("appkey is null");
        } else if (this.mAppkeyTrackMap.containsKey(appkey)) {
            uTTracker = this.mAppkeyTrackMap.get(appkey);
        } else {
            UTTracker lTracker = new UTTracker();
            lTracker.setAppKey(appkey);
            this.mAppkeyTrackMap.put(appkey, lTracker);
            uTTracker = lTracker;
        }
        return uTTracker;
    }

    /* access modifiers changed from: protected */
    public void transferLog(Map<String, String> aLogMap) {
        if (checkInit()) {
            if (aLogMap.containsKey("_sls")) {
                try {
                    if (AnalyticsMgr.iAnalytics != null) {
                        AnalyticsMgr.iAnalytics.transferLog(aLogMap);
                        return;
                    }
                    Logger.w(TAG, "iAnalytics", AnalyticsMgr.iAnalytics);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                AnalyticsMgr.handler.postWatingTask(createTransferLogTask(aLogMap));
            }
        }
    }

    public void turnOnRealTimeDebug(Map<String, String> aMap) {
        AnalyticsMgr.turnOnRealTimeDebug(aMap);
    }

    public void turnOffRealTimeDebug() {
        AnalyticsMgr.turnOffRealTimeDebug();
    }

    public void setToAliyunOsPlatform() {
        ClientVariables.getInstance().setToAliyunOSPlatform();
    }

    public String getOperationHistory(int aAmount, String aSeparator) {
        return UTOperationStack.getInstance().getOperationHistory(aAmount, aSeparator);
    }

    public void dispatchLocalHits() {
        if (checkInit()) {
            AnalyticsMgr.handler.postWatingTask(new Runnable() {
                public void run() {
                    try {
                        AnalyticsMgr.iAnalytics.dispatchLocalHits();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Deprecated
    public void saveCacheDataToLocal() {
        if (checkInit()) {
            AnalyticsMgr.handler.postWatingTask(new Runnable() {
                public void run() {
                    try {
                        AnalyticsMgr.iAnalytics.saveCacheDataToLocal();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public String selfCheck(String value) {
        if (!checkInit()) {
            return "local not init";
        }
        if (AnalyticsMgr.iAnalytics == null) {
            return "not bind remote service，waitting 10 second";
        }
        try {
            return AnalyticsMgr.iAnalytics.selfCheck(value);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Runnable createTransferLogTask(final Map<String, String> aLogMap) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.transferLog(aLogMap);
                } catch (Throwable th) {
                }
            }
        };
    }
}
