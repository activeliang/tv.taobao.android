package mtopsdk.mtop.intf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import anetwork.network.cache.Cache;
import com.taobao.orange.model.NameSpaceDO;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.global.init.IMtopInitTask;
import mtopsdk.mtop.global.init.MtopInitTaskFactory;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.xstate.XState;
import mtopsdk.xstate.util.XStateConstants;

public class Mtop {
    private static final String TAG = "mtopsdk.Mtop";
    protected static final Map<String, Mtop> instanceMap = new ConcurrentHashMap();
    final byte[] initLock = new byte[0];
    final IMtopInitTask initTask;
    volatile String instanceId = null;
    private volatile boolean isInit = false;
    volatile boolean isInited = false;
    final MtopConfig mtopConfig;

    public interface Id {
        public static final String INNER = "INNER";
        public static final String OPEN = "OPEN";
        public static final String PRODUCT = "PRODUCT";

        @Retention(RetentionPolicy.SOURCE)
        public @interface Definition {
        }
    }

    private Mtop(String instanceId2, @NonNull MtopConfig mtopConfig2) {
        this.instanceId = instanceId2;
        this.mtopConfig = mtopConfig2;
        this.initTask = MtopInitTaskFactory.getMtopInitTask(instanceId2);
        if (this.initTask == null) {
            throw new RuntimeException("IMtopInitTask is null,instanceId=" + instanceId2);
        }
    }

    @Deprecated
    public static Mtop instance(Context context) {
        return instance((String) null, context, (String) null);
    }

    @Deprecated
    public static Mtop instance(Context context, String ttid) {
        return instance((String) null, context, ttid);
    }

    public static Mtop instance(String instanceId2, @NonNull Context context) {
        return instance(instanceId2, context, (String) null);
    }

    public static Mtop instance(String instanceId2, @NonNull Context context, String ttid) {
        String id = instanceId2 != null ? instanceId2 : Id.INNER;
        Mtop instance = instanceMap.get(id);
        if (instance == null) {
            synchronized (Mtop.class) {
                instance = instanceMap.get(id);
                if (instance == null) {
                    MtopConfig mtopConfig2 = MtopSetting.mtopConfigMap.get(id);
                    if (mtopConfig2 == null) {
                        mtopConfig2 = new MtopConfig(id);
                    }
                    Mtop instance2 = new Mtop(id, mtopConfig2);
                    try {
                        mtopConfig2.mtopInstance = instance2;
                        instanceMap.put(id, instance2);
                        instance = instance2;
                    } catch (Throwable th) {
                        th = th;
                        Mtop mtop = instance2;
                        throw th;
                    }
                }
                try {
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        }
        if (!instance.isInit) {
            instance.init(context, ttid);
        }
        return instance;
    }

    public String getInstanceId() {
        return this.instanceId;
    }

    public MtopConfig getMtopConfig() {
        return this.mtopConfig;
    }

    private synchronized void init(Context context, String ttid) {
        if (!this.isInit) {
            if (context == null) {
                TBSdkLog.e(TAG, this.instanceId + " [init] The Parameter context can not be null.");
            } else {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, this.instanceId + " [init] context=" + context + ", ttid=" + ttid);
                }
                this.mtopConfig.context = context.getApplicationContext();
                if (StringUtils.isNotBlank(ttid)) {
                    this.mtopConfig.ttid = ttid;
                }
                MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
                    public void run() {
                        try {
                            synchronized (Mtop.this.initLock) {
                                long startTime = System.currentTimeMillis();
                                try {
                                    Mtop.this.updateAppKeyIndex();
                                    Mtop.this.initTask.executeCoreTask(Mtop.this.mtopConfig);
                                    MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
                                        public void run() {
                                            try {
                                                Mtop.this.initTask.executeExtraTask(Mtop.this.mtopConfig);
                                            } catch (Throwable e) {
                                                TBSdkLog.e(Mtop.TAG, Mtop.this.instanceId + " [init] executeExtraTask error.", e);
                                            }
                                        }
                                    });
                                    TBSdkLog.i(Mtop.TAG, Mtop.this.instanceId + " [init]do executeCoreTask cost[ms]: " + (System.currentTimeMillis() - startTime));
                                    Mtop.this.isInited = true;
                                    Mtop.this.initLock.notifyAll();
                                } catch (Throwable th) {
                                    TBSdkLog.i(Mtop.TAG, Mtop.this.instanceId + " [init]do executeCoreTask cost[ms]: " + (System.currentTimeMillis() - startTime));
                                    Mtop.this.isInited = true;
                                    Mtop.this.initLock.notifyAll();
                                    throw th;
                                }
                            }
                        } catch (Exception e) {
                            TBSdkLog.e(Mtop.TAG, Mtop.this.instanceId + " [init] executeCoreTask error.", (Throwable) e);
                        }
                    }
                });
                this.isInit = true;
            }
        }
    }

    public void unInit() {
        this.isInited = false;
        this.isInit = false;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, this.instanceId + "[unInit] MTOPSDK unInit called");
        }
    }

    /* access modifiers changed from: package-private */
    public void updateAppKeyIndex() {
        EnvModeEnum envMode = this.mtopConfig.envMode;
        if (envMode != null) {
            switch (envMode) {
                case ONLINE:
                case PREPARE:
                    this.mtopConfig.appKeyIndex = this.mtopConfig.onlineAppKeyIndex;
                    return;
                case TEST:
                case TEST_SANDBOX:
                    this.mtopConfig.appKeyIndex = this.mtopConfig.dailyAppkeyIndex;
                    return;
                default:
                    return;
            }
        }
    }

    public Mtop switchEnvMode(final EnvModeEnum envMode) {
        if (!(envMode == null || this.mtopConfig.envMode == envMode)) {
            if (MtopUtils.isApkDebug(this.mtopConfig.context) || this.mtopConfig.isAllowSwitchEnv.compareAndSet(true, false)) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, this.instanceId + " [switchEnvMode]MtopSDK switchEnvMode called.envMode=" + envMode);
                }
                MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
                    public void run() {
                        Mtop.this.checkMtopSDKInit();
                        if (Mtop.this.mtopConfig.envMode == envMode) {
                            TBSdkLog.i(Mtop.TAG, Mtop.this.instanceId + " [switchEnvMode] Current EnvMode matches target EnvMode,envMode=" + envMode);
                            return;
                        }
                        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                            TBSdkLog.i(Mtop.TAG, Mtop.this.instanceId + " [switchEnvMode]MtopSDK switchEnvMode start");
                        }
                        Mtop.this.mtopConfig.envMode = envMode;
                        try {
                            Mtop.this.updateAppKeyIndex();
                            if (EnvModeEnum.ONLINE == envMode) {
                                TBSdkLog.setPrintLog(false);
                            }
                            Mtop.this.initTask.executeCoreTask(Mtop.this.mtopConfig);
                            Mtop.this.initTask.executeExtraTask(Mtop.this.mtopConfig);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                            TBSdkLog.i(Mtop.TAG, Mtop.this.instanceId + " [switchEnvMode]MtopSDK switchEnvMode end. envMode =" + envMode);
                        }
                    }
                });
            } else {
                TBSdkLog.e(TAG, this.instanceId + " [switchEnvMode]release package can switch environment only once!");
            }
        }
        return this;
    }

    public boolean checkMtopSDKInit() {
        if (this.isInited) {
            return this.isInited;
        }
        synchronized (this.initLock) {
            try {
                if (!this.isInited) {
                    this.initLock.wait(60000);
                    if (!this.isInited) {
                        TBSdkLog.e(TAG, this.instanceId + " [checkMtopSDKInit]Didn't call Mtop.instance(...),please execute global init.");
                    }
                }
            } catch (Exception e) {
                TBSdkLog.e(TAG, this.instanceId + " [checkMtopSDKInit] wait Mtop initLock failed---" + e.toString());
            }
        }
        return this.isInited;
    }

    public boolean isInited() {
        return this.isInited;
    }

    @Deprecated
    public Mtop registerSessionInfo(String sid, @Deprecated String ecode, String userId) {
        return registerMultiAccountSession((String) null, sid, userId);
    }

    @Deprecated
    public static void setAppKeyIndex(int onlineIndex, int dailyIndex) {
        MtopSetting.setAppKeyIndex(onlineIndex, dailyIndex);
    }

    @Deprecated
    public static void setAppVersion(String appVersion) {
        MtopSetting.setAppVersion(appVersion);
    }

    @Deprecated
    public static void setSecurityAppKey(String securityAppKey) {
        MtopSetting.setSecurityAppKey(securityAppKey);
    }

    @Deprecated
    public static void setMtopDomain(String onlineDomain, String preDomain, String dailyDomain) {
        MtopSetting.setMtopDomain(onlineDomain, preDomain, dailyDomain);
    }

    private String getFullUserInfo(String userInfo) {
        String userInfoExt;
        if (StringUtils.isBlank(userInfo)) {
            userInfoExt = NameSpaceDO.LEVEL_DEFAULT;
        } else {
            userInfoExt = userInfo;
        }
        return StringUtils.concatStr(this.instanceId, userInfoExt);
    }

    public Mtop registerSessionInfo(String sid, String userId) {
        return registerMultiAccountSession((String) null, sid, userId);
    }

    public Mtop logout() {
        return logoutMultiAccountSession((String) null);
    }

    public Mtop registerMultiAccountSession(@Nullable String userInfo, String sid, String userId) {
        String fullUserInfo = getFullUserInfo(userInfo);
        XState.setValue(fullUserInfo, "sid", sid);
        XState.setValue(fullUserInfo, XStateConstants.KEY_UID, userId);
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            StringBuilder builder = new StringBuilder(64);
            builder.append(fullUserInfo);
            builder.append(" [registerSessionInfo]register sessionInfo succeed: sid=").append(sid);
            builder.append(",uid=").append(userId);
            TBSdkLog.i(TAG, builder.toString());
        }
        if (this.mtopConfig.networkPropertyService != null) {
            this.mtopConfig.networkPropertyService.setUserId(userId);
        }
        return this;
    }

    public Mtop logoutMultiAccountSession(@Nullable String userInfo) {
        String fullUserInfo = getFullUserInfo(userInfo);
        XState.removeKey(fullUserInfo, "sid");
        XState.removeKey(fullUserInfo, XStateConstants.KEY_UID);
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            StringBuilder builder = new StringBuilder(32);
            builder.append(fullUserInfo).append(" [logout] remove sessionInfo succeed.");
            TBSdkLog.i(TAG, builder.toString());
        }
        if (this.mtopConfig.networkPropertyService != null) {
            this.mtopConfig.networkPropertyService.setUserId((String) null);
        }
        return this;
    }

    public Mtop registerTtid(String ttid) {
        if (ttid != null) {
            this.mtopConfig.ttid = ttid;
            XState.setValue(this.instanceId, "ttid", ttid);
            if (this.mtopConfig.networkPropertyService != null) {
                this.mtopConfig.networkPropertyService.setTtid(ttid);
            }
        }
        return this;
    }

    public Mtop registerUtdid(String utdid) {
        if (utdid != null) {
            this.mtopConfig.utdid = utdid;
            XState.setValue("utdid", utdid);
        }
        return this;
    }

    public Mtop registerDeviceId(String deviceId) {
        if (deviceId != null) {
            this.mtopConfig.deviceId = deviceId;
            XState.setValue(this.instanceId, "deviceId", deviceId);
        }
        return this;
    }

    @Deprecated
    public String getSid() {
        return getMultiAccountSid((String) null);
    }

    public String getMultiAccountSid(String userInfo) {
        return XState.getValue(getFullUserInfo(userInfo), "sid");
    }

    @Deprecated
    public String getUserId() {
        return getMultiAccountUserId((String) null);
    }

    public String getMultiAccountUserId(String userInfo) {
        return XState.getValue(getFullUserInfo(userInfo), XStateConstants.KEY_UID);
    }

    public String getTtid() {
        return XState.getValue(this.instanceId, "ttid");
    }

    public String getDeviceId() {
        return XState.getValue(this.instanceId, "deviceId");
    }

    public String getUtdid() {
        return XState.getValue("utdid");
    }

    public Mtop setCoordinates(String longitude, String latitude) {
        XState.setValue("lng", longitude);
        XState.setValue("lat", latitude);
        return this;
    }

    public boolean removeCacheBlock(String blockName) {
        Cache cache = this.mtopConfig.cacheImpl;
        return cache != null && cache.remove(blockName);
    }

    public boolean unintallCacheBlock(String blockName) {
        Cache cache = this.mtopConfig.cacheImpl;
        return cache != null && cache.uninstall(blockName);
    }

    public boolean removeCacheItem(String blockName, String cacheKey) {
        if (StringUtils.isBlank(cacheKey)) {
            TBSdkLog.e(TAG, "[removeCacheItem] remove CacheItem failed,invalid cacheKey=" + cacheKey);
            return false;
        }
        Cache cache = this.mtopConfig.cacheImpl;
        if (cache == null || !cache.remove(blockName, cacheKey)) {
            return false;
        }
        return true;
    }

    public Mtop logSwitch(boolean open) {
        TBSdkLog.setPrintLog(open);
        return this;
    }

    public MtopBuilder build(IMTOPDataObject mtopData, String ttid) {
        return new MtopBuilder(this, mtopData, ttid);
    }

    public MtopBuilder build(MtopRequest request, String ttid) {
        return new MtopBuilder(this, request, ttid);
    }

    @Deprecated
    public MtopBuilder build(Object inputDo, String ttid) {
        return new MtopBuilder(this, inputDo, ttid);
    }
}
