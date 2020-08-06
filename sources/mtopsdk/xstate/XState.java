package mtopsdk.xstate;

import android.content.Context;
import android.os.RemoteException;
import com.ta.utdid2.device.UTDevice;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.util.AsyncServiceBinder;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.xstate.aidl.IXState;
import mtopsdk.xstate.util.PhoneInfo;
import mtopsdk.xstate.util.XStateConstants;

public class XState {
    private static final String TAG = "mtopsdk.XState";
    private static AsyncServiceBinder<IXState> asyncServiceBinder;
    private static AtomicBoolean isInited = new AtomicBoolean(false);
    static volatile AtomicBoolean isSyncToRemote = new AtomicBoolean(false);
    private static final ConcurrentHashMap<String, String> localMap = new ConcurrentHashMap<>();
    private static Context mContext = null;

    public static void init(Context context) {
        if (context == null) {
            TBSdkLog.e(TAG, "[init]init error,context is null");
        } else if (isInited.compareAndSet(false, true)) {
            mContext = context.getApplicationContext();
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[init]XState init called");
            }
            initPhoneInfo(context);
            if (asyncServiceBinder == null) {
                asyncServiceBinder = new AsyncServiceBinder<IXState>(IXState.class, XStateService.class) {
                    /* access modifiers changed from: protected */
                    public void afterAsyncBind() {
                        XState.isSyncToRemote.compareAndSet(true, false);
                        MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
                            public void run() {
                                XState.syncToRemote();
                            }
                        });
                    }
                };
                asyncServiceBinder.asyncBind(context);
                return;
            }
            syncToRemote();
        }
    }

    public static void unInit() {
        if (checkBindAndRetryAsyncBind()) {
            try {
                asyncServiceBinder.getService().unInit();
            } catch (RemoteException e) {
                TBSdkLog.e(TAG, "[unInit] unInit error", (Throwable) e);
            }
        }
        localMap.clear();
        isInited.set(false);
    }

    private static void initPhoneInfo(Context context) {
        try {
            String user_agent = PhoneInfo.getPhoneBaseInfo(context);
            if (user_agent != null) {
                localMap.put("ua", user_agent);
            }
            String utdid = UTDevice.getUtdid(context);
            if (utdid != null) {
                localMap.put("utdid", utdid);
            }
            localMap.put(XStateConstants.KEY_TIME_OFFSET, "0");
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[initPhoneInfo]initPhoneInfo error", e);
        }
    }

    public static String getTimeOffset() {
        return getValue(XStateConstants.KEY_TIME_OFFSET);
    }

    public static String getLat() {
        return getValue("lat");
    }

    public static String getLng() {
        return getValue("lng");
    }

    public static String getNetworkQuality() {
        return getValue(XStateConstants.KEY_NQ);
    }

    public static String getNetworkType() {
        return getValue("netType");
    }

    public static boolean isAppBackground() {
        String ret = getValue(XStateConstants.KEY_APP_BACKGROUND);
        if (ret == null) {
            return false;
        }
        try {
            return Boolean.valueOf(ret).booleanValue();
        } catch (Exception e) {
            TBSdkLog.e(TAG, "[isAppBackground] parse KEY_APP_BACKGROUND error");
            return false;
        }
    }

    public static void setAppBackground(boolean background) {
        setValue(XStateConstants.KEY_APP_BACKGROUND, String.valueOf(background));
    }

    public static String getValue(String key) {
        return getValue((String) null, key);
    }

    public static String getValue(String instanceId, String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        if (StringUtils.isNotBlank(instanceId)) {
            key = StringUtils.concatStr(instanceId, key);
        }
        if (!checkBindAndRetryAsyncBind() || !isSyncToRemote.get()) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[getValue]Attention :Use XState Local Mode: key:" + key);
            }
            return localMap.get(key);
        }
        try {
            return asyncServiceBinder.getService().getValue(key);
        } catch (Exception e) {
            TBSdkLog.e(TAG, "[getValue] IXState.getValue(Key) failed,key:" + key, (Throwable) e);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[getValue]Attention :Use XState Local Mode: key:" + key);
            }
            return localMap.get(key);
        }
    }

    public static String removeKey(String key) {
        return removeKey((String) null, key);
    }

    public static String removeKey(String instanceId, String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        if (StringUtils.isNotBlank(instanceId)) {
            key = StringUtils.concatStr(instanceId, key);
        }
        if (!checkBindAndRetryAsyncBind() || !isSyncToRemote.get()) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[removeKey]Attention :Use XState Local Mode: key:" + key);
            }
            localMap.remove(key);
        } else {
            try {
                return asyncServiceBinder.getService().removeKey(key);
            } catch (Exception e) {
                TBSdkLog.e(TAG, "[removeKey] IXState.removeKey(key) failed,key:" + key, (Throwable) e);
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, "[removeKey]Attention :Use XState Local Mode: key:" + key);
                }
                localMap.remove(key);
            }
        }
        return null;
    }

    public static void setValue(String key, String value) {
        setValue((String) null, key, value);
    }

    public static void setValue(String instanceId, String key, String value) {
        if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
            if (StringUtils.isNotBlank(instanceId)) {
                key = StringUtils.concatStr(instanceId, key);
            }
            if (!checkBindAndRetryAsyncBind() || !isSyncToRemote.get()) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                    TBSdkLog.i(TAG, "[setValue]Attention :Use XState Local Mode: key:" + key + ",value:" + value);
                }
                localMap.put(key, value);
                return;
            }
            try {
                asyncServiceBinder.getService().setValue(key, value);
            } catch (Exception e) {
                TBSdkLog.e(TAG, "[setValue] IXState.setValue(key,value) failed,key:" + key + ",value:" + value, (Throwable) e);
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, "[setValue]Attention :Use XState Local Mode: key:" + key + ",value:" + value);
                }
                localMap.put(key, value);
            }
        }
    }

    static void syncToRemote() {
        String key;
        String value;
        if (checkBindAndRetryAsyncBind()) {
            IXState service = asyncServiceBinder.getService();
            try {
                service.init();
                for (Map.Entry<String, String> entry : localMap.entrySet()) {
                    key = entry.getKey();
                    value = entry.getValue();
                    service.setValue(key, value);
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(TAG, "[syncToRemote] sync succeed, key:" + key + ",value:" + value);
                    }
                }
                isSyncToRemote.compareAndSet(false, true);
            } catch (Exception e) {
                TBSdkLog.e(TAG, "[syncToRemote] sync error, key:" + key + ",value:" + value, (Throwable) e);
            } catch (Throwable e2) {
                TBSdkLog.e(TAG, "syncToRemote error.", e2);
            }
        }
    }

    private static boolean checkBindAndRetryAsyncBind() {
        if (asyncServiceBinder == null) {
            return false;
        }
        if (asyncServiceBinder.getService() != null) {
            return true;
        }
        asyncServiceBinder.asyncBind(mContext);
        return false;
    }
}
