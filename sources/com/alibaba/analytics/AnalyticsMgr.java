package com.alibaba.analytics;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import com.alibaba.analytics.IAnalytics;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.version.UTBuildInfo;
import com.alibaba.motu.crashreporter.utrestapi.UTRestUrlWrapper;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AnalyticsMgr {
    private static final String TAG = "AnalyticsMgr";
    private static String appKey = null;
    private static String appVersion = null;
    private static Application application = null;
    private static String channel = null;
    public static WaitingHandler handler;
    private static HandlerThread handlerThread = null;
    public static IAnalytics iAnalytics;
    /* access modifiers changed from: private */
    public static boolean isBindSuccess = false;
    public static boolean isDebug = false;
    private static boolean isEncode = false;
    public static volatile boolean isInit = false;
    /* access modifiers changed from: private */
    public static boolean isNeedRestart = false;
    private static boolean isSecurity = false;
    private static boolean isTurnOnRealTimeDebug = false;
    /* access modifiers changed from: private */
    public static ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.d("onServiceConnected", "this", AnalyticsMgr.mConnection);
            if (RunMode.Service == AnalyticsMgr.mode) {
                AnalyticsMgr.iAnalytics = IAnalytics.Stub.asInterface(service);
                Logger.i("onServiceConnected", "iAnalytics", AnalyticsMgr.iAnalytics);
            }
            synchronized (AnalyticsMgr.sWaitServiceConnectedLock) {
                AnalyticsMgr.sWaitServiceConnectedLock.notifyAll();
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            Logger.d(AnalyticsMgr.TAG, "[onServiceDisconnected]");
            synchronized (AnalyticsMgr.sWaitServiceConnectedLock) {
                AnalyticsMgr.sWaitServiceConnectedLock.notifyAll();
            }
            boolean unused = AnalyticsMgr.isNeedRestart = true;
        }
    };
    private static String mOpenid = null;
    public static final List<Entity> mRegisterList = Collections.synchronizedList(new ArrayList());
    public static RunMode mode = RunMode.Service;
    private static Map<String, String> realTimeDebugParams = null;
    /* access modifiers changed from: private */
    public static final Object sWaitMainProcessLock = new Object();
    /* access modifiers changed from: private */
    public static final Object sWaitServiceConnectedLock = new Object();
    private static String secret = null;
    private static Map<String, String> updateSessionProperties = null;
    private static String userId = null;
    private static String userNick = null;

    public static class Entity {
        public DimensionSet dimensionSet;
        public boolean isCommitDetail;
        public MeasureSet measureSet;
        public String module;
        public String monitorPoint;
    }

    enum RunMode {
        Local,
        Service
    }

    public static synchronized void init(Application application2) {
        synchronized (AnalyticsMgr.class) {
            try {
                if (!isInit) {
                    Logger.i("AnalyticsMgr[init] start", UTRestUrlWrapper.FIELD_SDK_VERSION, UTBuildInfo.getInstance().getFullSDKVersion());
                    application = application2;
                    handlerThread = new HandlerThread("Analytics_Client");
                    Looper looper = null;
                    handlerThread.start();
                    int i = 0;
                    while (i < 3) {
                        try {
                            looper = handlerThread.getLooper();
                            if (looper == null) {
                                Thread.sleep(10);
                                i++;
                            }
                        } catch (Throwable e) {
                            Logger.e(TAG, "3", e);
                        }
                    }
                    handler = new WaitingHandler(looper);
                    try {
                        handler.postAtFrontOfQueue(new UtDelayInitTask());
                    } catch (Throwable throwable) {
                        Logger.e(TAG, "4", throwable);
                    }
                    isInit = true;
                    Logger.d("外面init完成", new Object[0]);
                }
            } catch (Throwable e2) {
                Logger.w(TAG, "5", e2);
            }
            Logger.w(TAG, "isInit", Boolean.valueOf(isInit), UTRestUrlWrapper.FIELD_SDK_VERSION, UTBuildInfo.getInstance().getFullSDKVersion());
        }
        return;
    }

    public static void notifyWaitLocked() {
        try {
            synchronized (sWaitMainProcessLock) {
                sWaitMainProcessLock.notifyAll();
            }
        } catch (Throwable th) {
        }
    }

    public static void setChanel(String channel2) {
        if (checkInit()) {
            handler.postWatingTask(createSetChannelTask(channel2));
            channel = channel2;
        }
    }

    public static void setRequestAuthInfo(boolean isSecurity2, boolean isEncode2, String appkey, String secret2) {
        if (checkInit()) {
            handler.postWatingTask(createSetRequestAuthTask(isSecurity2, isEncode2, appkey, secret2));
            isSecurity = isSecurity2;
            appKey = appkey;
            secret = secret2;
            isEncode = isEncode2;
        }
    }

    public static void turnOnRealTimeDebug(Map<String, String> params) {
        if (checkInit()) {
            handler.postWatingTask(createTurnOnRealTimeDebugTask(params));
            realTimeDebugParams = params;
            isTurnOnRealTimeDebug = true;
        }
    }

    public static void turnOffRealTimeDebug() {
        if (checkInit()) {
            handler.postWatingTask(createTurnOffRealTimeDebugTask());
            isTurnOnRealTimeDebug = false;
        }
    }

    public static void setAppVersion(String aAppVersion) {
        Logger.i((String) null, "aAppVersion", aAppVersion);
        if (checkInit()) {
            handler.postWatingTask(createSetAppVersionTask(aAppVersion));
            appVersion = aAppVersion;
        }
    }

    public static void turnOnDebug() {
        Logger.i("turnOnDebug", new Object[0]);
        if (checkInit()) {
            handler.postWatingTask(createTurnOnDebugTask());
            isDebug = true;
            Logger.setDebug(true);
        }
    }

    public static void updateUserAccount(String aUsernick, String aUserid, String aOpenid) {
        Logger.i("", "Usernick", aUsernick, "Userid", aUserid, "openid", aOpenid);
        if (checkInit()) {
            handler.postWatingTask(createUpdateUserAccountTask(aUsernick, aUserid, aOpenid));
            userNick = aUsernick;
            userId = aUserid;
            mOpenid = aOpenid;
        }
    }

    public static void updateSessionProperties(Map<String, String> aMap) {
        if (checkInit()) {
            handler.postWatingTask(createUpdateSessionProperties(aMap));
            updateSessionProperties = aMap;
        }
    }

    public static String getValue(String aKey) {
        if (iAnalytics == null) {
            return null;
        }
        try {
            return iAnalytics.getValue(aKey);
        } catch (Exception e) {
            return null;
        }
    }

    public static void setSessionProperties(Map<String, String> aMap) {
        if (checkInit()) {
            handler.postWatingTask(createSetSessionPropertiesTask(aMap));
        }
    }

    public static boolean checkInit() {
        if (!isInit) {
            Logger.d("Please call init() before call other method", new Object[0]);
        }
        return isInit;
    }

    /* access modifiers changed from: private */
    public static void newLocalAnalytics() {
        mode = RunMode.Local;
        iAnalytics = new AnalyticsImp(application);
        Logger.w("Start AppMonitor Service failed,AppMonitor run in local Mode...", new Object[0]);
    }

    /* access modifiers changed from: private */
    public static boolean bindService() {
        if (application == null) {
            return false;
        }
        boolean bindsuccess = application.getApplicationContext().bindService(new Intent(application.getApplicationContext(), AnalyticsService.class), mConnection, 1);
        if (!bindsuccess) {
            newLocalAnalytics();
        }
        Logger.i(TAG, "bindsuccess", Boolean.valueOf(bindsuccess));
        return bindsuccess;
    }

    public static void dispatchLocalHits() {
        if (checkInit()) {
            handler.postWatingTask(createDispatchLocalHitTrask());
        }
    }

    /* access modifiers changed from: private */
    public static Runnable createInitTask() {
        return new Runnable() {
            public void run() {
                Logger.i("call Remote init start...", new Object[0]);
                try {
                    AnalyticsMgr.iAnalytics.initUT();
                } catch (Throwable e2) {
                    Logger.e("initut error", e2, new Object[0]);
                }
                Logger.i("call Remote init end", new Object[0]);
            }
        };
    }

    public static void restart() {
        Logger.d("[restart]", new Object[0]);
        try {
            if (isNeedRestart) {
                isNeedRestart = false;
                newLocalAnalytics();
                createInitTask().run();
                createSetRequestAuthTask(isSecurity, isEncode, appKey, secret).run();
                createSetChannelTask(channel).run();
                createSetAppVersionTask(appVersion).run();
                createUpdateUserAccountTask(userNick, userId, mOpenid).run();
                createUpdateSessionProperties(updateSessionProperties).run();
                if (isDebug) {
                    createTurnOnDebugTask().run();
                }
                if (isTurnOnRealTimeDebug && realTimeDebugParams != null) {
                    createSetSessionPropertiesTask(realTimeDebugParams).run();
                } else if (isTurnOnRealTimeDebug) {
                    createTurnOffRealTimeDebugTask().run();
                }
                synchronized (mRegisterList) {
                    for (int i = 0; i < mRegisterList.size(); i++) {
                        Entity entity = mRegisterList.get(i);
                        if (entity != null) {
                            try {
                                createRegisterTask(entity.module, entity.monitorPoint, entity.measureSet, entity.dimensionSet, entity.isCommitDetail).run();
                            } catch (Throwable e) {
                                Logger.e(TAG, "[RegisterTask.run]", e);
                            }
                        }
                    }
                }
            }
        } catch (Throwable e2) {
            Logger.e(TAG, "[restart]", e2);
        }
    }

    private static Runnable createTurnOnRealTimeDebugTask(final Map<String, String> params) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.turnOnRealTimeDebug(params);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private static Runnable createTurnOffRealTimeDebugTask() {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.turnOffRealTimeDebug();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private static Runnable createSetRequestAuthTask(final boolean isSecurity2, final boolean isEncode2, final String appkey, final String secret2) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.setRequestAuthInfo(isSecurity2, isEncode2, appkey, secret2);
                } catch (Throwable th) {
                }
            }
        };
    }

    private static Runnable createSetChannelTask(final String channel2) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.setChannel(channel2);
                } catch (Throwable th) {
                }
            }
        };
    }

    private static Runnable createRegisterTask(String module, String monitorPoint, MeasureSet measures, DimensionSet dimensions, boolean isCommitDetail) {
        Logger.d("", new Object[0]);
        final String str = module;
        final String str2 = monitorPoint;
        final MeasureSet measureSet = measures;
        final DimensionSet dimensionSet = dimensions;
        final boolean z = isCommitDetail;
        return new Runnable() {
            public void run() {
                try {
                    Logger.d("register stat event", "module", str, " monitorPoint: ", str2);
                    AnalyticsMgr.iAnalytics.register4(str, str2, measureSet, dimensionSet, z);
                } catch (RemoteException e) {
                    AnalyticsMgr.handleRemoteException(e);
                }
            }
        };
    }

    private static Runnable createSetAppVersionTask(final String appVersion2) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.setAppVersion(appVersion2);
                } catch (Throwable th) {
                }
            }
        };
    }

    private static Runnable createTurnOnDebugTask() {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.turnOnDebug();
                } catch (Throwable th) {
                }
            }
        };
    }

    private static Runnable createUpdateUserAccountTask(final String aUsernick, final String aUserid, final String aOpenid) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.updateUserAccount(aUsernick, aUserid, aOpenid);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private static Runnable createUpdateSessionProperties(final Map<String, String> aMap) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.updateSessionProperties(aMap);
                } catch (Throwable th) {
                }
            }
        };
    }

    private static Runnable createDispatchLocalHitTrask() {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.dispatchLocalHits();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private static Runnable createSetSessionPropertiesTask(final Map<String, String> aMap) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.setSessionProperties(aMap);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static void handleRemoteException(Exception e) {
        Logger.w("", e, new Object[0]);
        if (e instanceof DeadObjectException) {
            restart();
        }
    }

    public static class WaitingHandler extends Handler {
        public WaitingHandler(Looper looper) {
            super(looper);
        }

        public void postWatingTask(Runnable runnable) {
            Logger.d();
            if (runnable != null) {
                try {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = runnable;
                    sendMessage(msg);
                } catch (Throwable th) {
                }
            }
        }

        public void handleMessage(Message msg) {
            try {
                if (msg.obj != null && (msg.obj instanceof Runnable)) {
                    ((Runnable) msg.obj).run();
                }
            } catch (Throwable e) {
                Logger.e(AnalyticsMgr.TAG, e, new Object[0]);
            }
            super.handleMessage(msg);
        }
    }

    public static class UtDelayInitTask implements Runnable {
        public void run() {
            try {
                Logger.i("延时启动任务", new Object[0]);
                synchronized (AnalyticsMgr.sWaitMainProcessLock) {
                    int waitSecond = AnalyticsMgr.getCoreProcessWaitTime();
                    if (waitSecond > 0) {
                        Logger.i("delay " + waitSecond + " second to start service,waiting...", new Object[0]);
                        try {
                            AnalyticsMgr.sWaitMainProcessLock.wait((long) (waitSecond * 1000));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                boolean unused = AnalyticsMgr.isBindSuccess = AnalyticsMgr.bindService();
                AnalyticsMgr.handler.postAtFrontOfQueue(new UTInitTimeoutTask());
            } catch (Throwable e2) {
                Logger.e(AnalyticsMgr.TAG, Constants.LogTransferLevel.L6, e2);
            }
        }
    }

    /* access modifiers changed from: private */
    public static int getCoreProcessWaitTime() {
        String delaySecondStr = AppInfoUtil.getString(application.getApplicationContext(), "UTANALYTICS_REMOTE_SERVICE_DELAY_SECOND");
        if (TextUtils.isEmpty(delaySecondStr)) {
            return 10;
        }
        try {
            int delayS = Integer.valueOf(delaySecondStr).intValue();
            if (delayS < 0 || delayS > 30) {
                return 10;
            }
            return delayS;
        } catch (Throwable th) {
            return 10;
        }
    }

    public static class UTInitTimeoutTask implements Runnable {
        public void run() {
            try {
                if (AnalyticsMgr.isBindSuccess) {
                    Logger.i("delay 30 sec to wait the Remote service connected,waiting...", new Object[0]);
                    synchronized (AnalyticsMgr.sWaitServiceConnectedLock) {
                        try {
                            AnalyticsMgr.sWaitServiceConnectedLock.wait(30000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (AnalyticsMgr.iAnalytics == null) {
                    Logger.i("cannot get remote analytics object,new local object", new Object[0]);
                    AnalyticsMgr.newLocalAnalytics();
                }
                AnalyticsMgr.createInitTask().run();
            } catch (Throwable e2) {
                Logger.e(AnalyticsMgr.TAG, Constants.LogTransferLevel.L7, e2);
            }
        }
    }
}
