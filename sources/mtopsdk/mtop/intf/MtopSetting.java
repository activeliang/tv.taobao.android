package mtopsdk.mtop.intf;

import android.support.annotation.NonNull;
import anetwork.network.cache.Cache;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.config.MtopConfigListener;
import mtopsdk.common.log.LogAdapter;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.SwitchConfigUtil;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.antiattack.AntiAttackHandler;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.features.MtopFeatureManager;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.stat.IUploadStats;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.network.Call;
import mtopsdk.security.ISign;

public final class MtopSetting {
    private static final String TAG = "mtopsdk.MtopSetting";
    protected static final Map<String, MtopConfig> mtopConfigMap = new HashMap();

    private MtopSetting() {
    }

    static MtopConfig getMtopConfigByID(String instanceId) {
        String id = instanceId != null ? instanceId : Mtop.Id.INNER;
        Mtop mtopInstance = Mtop.instanceMap.get(id);
        if (mtopInstance == null) {
            synchronized (Mtop.class) {
                mtopInstance = Mtop.instanceMap.get(id);
                if (mtopInstance == null) {
                    MtopConfig mtopConfig = mtopConfigMap.get(id);
                    if (mtopConfig == null) {
                        synchronized (MtopSetting.class) {
                            mtopConfig = mtopConfigMap.get(id);
                            if (mtopConfig == null) {
                                MtopConfig mtopConfig2 = new MtopConfig(id);
                                try {
                                    mtopConfigMap.put(id, mtopConfig2);
                                    mtopConfig = mtopConfig2;
                                } catch (Throwable th) {
                                    th = th;
                                    MtopConfig mtopConfig3 = mtopConfig2;
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
                    return mtopConfig;
                }
            }
        }
        return mtopInstance.getMtopConfig();
    }

    public static void setAppKeyIndex(String instanceId, int onlineIndex, int dailyIndex) {
        MtopConfig mtopConfig = getMtopConfigByID(instanceId);
        mtopConfig.onlineAppKeyIndex = onlineIndex;
        mtopConfig.dailyAppkeyIndex = dailyIndex;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfig.instanceId + " [setAppKeyIndex] onlineAppKeyIndex=" + onlineIndex + ",dailyAppkeyIndex=" + dailyIndex);
        }
    }

    @Deprecated
    public static void setAppKeyIndex(int onlineIndex, int dailyIndex) {
        setAppKeyIndex((String) null, onlineIndex, dailyIndex);
    }

    public static void setAuthCode(String instanceId, String authCode) {
        MtopConfig mtopConfig = getMtopConfigByID(instanceId);
        mtopConfig.authCode = authCode;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfig.instanceId + " [setAuthCode] authCode=" + authCode);
        }
    }

    @Deprecated
    public static void setAuthCode(String authCode) {
        setAuthCode((String) null, authCode);
    }

    public static void setWuaAuthCode(String instanceId, String wuaAuthCode) {
        MtopConfig mtopConfig = getMtopConfigByID(instanceId);
        mtopConfig.wuaAuthCode = wuaAuthCode;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfig.instanceId + " [setWuaAuthCode] wuaAuthCode=" + wuaAuthCode);
        }
    }

    public static void setAppVersion(String instanceId, String appVersion) {
        MtopConfig mtopConfig = getMtopConfigByID(instanceId);
        mtopConfig.appVersion = appVersion;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfig.instanceId + " [setAppVersion] appVersion=" + appVersion);
        }
    }

    @Deprecated
    public static void setAppVersion(String appVersion) {
        setAppVersion((String) null, appVersion);
    }

    public static void setMtopDomain(String instanceId, String onlineDomain, String preDomain, String dailyDomain) {
        MtopConfig mtopConfig = getMtopConfigByID(instanceId);
        if (StringUtils.isNotBlank(onlineDomain)) {
            mtopConfig.mtopDomain.updateDomain(EnvModeEnum.ONLINE, onlineDomain);
        }
        if (StringUtils.isNotBlank(preDomain)) {
            mtopConfig.mtopDomain.updateDomain(EnvModeEnum.PREPARE, preDomain);
        }
        if (StringUtils.isNotBlank(dailyDomain)) {
            mtopConfig.mtopDomain.updateDomain(EnvModeEnum.TEST, dailyDomain);
        }
    }

    @Deprecated
    public static void setMtopDomain(String onlineDomain, String preDomain, String dailyDomain) {
        setMtopDomain((String) null, onlineDomain, preDomain, dailyDomain);
    }

    @Deprecated
    public static void setMtopFeatureFlag(MtopFeatureManager.MtopFeatureEnum featureEnum, boolean openFlag) {
        setMtopFeatureFlag((String) null, MtopFeatureManager.getMtopFeatureByFeatureEnum(featureEnum), openFlag);
    }

    public static void setMtopFeatureFlag(String instanceId, int mtopFeature, boolean openFlag) {
        if (mtopFeature >= 1) {
            MtopConfig mtopConfig = getMtopConfigByID(instanceId);
            if (openFlag) {
                mtopConfig.mtopFeatures.add(Integer.valueOf(mtopFeature));
            } else {
                mtopConfig.mtopFeatures.remove(Integer.valueOf(mtopFeature));
            }
        }
    }

    @Deprecated
    public static void setSecurityAppKey(String securityAppKey) {
    }

    public static void setAntiAttackHandler(String instanceId, AntiAttackHandler antiAttackHandler) {
        MtopConfig mtopConfig = getMtopConfigByID(instanceId);
        mtopConfig.antiAttackHandler = antiAttackHandler;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfig.instanceId + " [setAntiAttackHandler] set antiAttackHandler succeed.");
        }
    }

    @Deprecated
    public static void setXOrangeQ(String xOrangeQ) {
        setXOrangeQ((String) null, xOrangeQ);
    }

    public static void setXOrangeQ(String instanceId, String xOrangeQ) {
        if (StringUtils.isNotBlank(xOrangeQ)) {
            MtopConfig mtopConfig = getMtopConfigByID(instanceId);
            mtopConfig.xOrangeQ = xOrangeQ;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopConfig.instanceId + " [setXOrangeQ] set xOrangeQ succeed.xOrangeQ=" + xOrangeQ);
            }
        }
    }

    public static void setMtopConfigListener(final MtopConfigListener mtopConfigListener) {
        SwitchConfig.getInstance().setMtopConfigListener(mtopConfigListener);
        SwitchConfigUtil.setMtopConfigListener(mtopConfigListener);
        TBSdkLog.i(TAG, "[setMtopConfigListener] set MtopConfigListener succeed.");
        MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
            public void run() {
                if (mtopConfigListener != null) {
                    MtopConfig mtopConfig = MtopSetting.getMtopConfigByID((String) null);
                    if (mtopConfig.context != null) {
                        mtopConfigListener.initConfig(mtopConfig.context);
                    }
                }
            }
        });
    }

    public static void setLogAdapterImpl(LogAdapter logAdapterImpl) {
        if (logAdapterImpl != null) {
            MtopConfig.logAdapterImpl = logAdapterImpl;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setLogAdapterImpl] set logAdapter succeed.logAdapterImpl=" + logAdapterImpl);
            }
        }
    }

    @Deprecated
    public static void setCacheImpl(Cache cacheImpl) {
        setCacheImpl((String) null, cacheImpl);
    }

    public static void setCacheImpl(String instanceId, Cache cacheImpl) {
        if (cacheImpl != null) {
            MtopConfig mtopConfig = getMtopConfigByID(instanceId);
            mtopConfig.cacheImpl = cacheImpl;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopConfig.instanceId + " [setCacheImpl] set CacheImpl succeed.cacheImpl=" + cacheImpl);
            }
        }
    }

    public static void setISignImpl(String instanceId, ISign signImpl) {
        MtopConfig mtopConfig = getMtopConfigByID(instanceId);
        mtopConfig.sign = signImpl;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfig.instanceId + "[setISignImpl] set ISign succeed.signImpl=" + signImpl);
        }
    }

    public static void setUploadStats(String instanceId, IUploadStats uploadStats) {
        MtopConfig mtopConfig = getMtopConfigByID(instanceId);
        mtopConfig.uploadStats = uploadStats;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtopConfig.instanceId + "[setUploadStats] set IUploadStats succeed.uploadStats=" + uploadStats);
        }
    }

    public static void setCallFactoryImpl(String instanceId, Call.Factory callFactory) {
        if (callFactory != null) {
            MtopConfig mtopConfig = getMtopConfigByID(instanceId);
            mtopConfig.callFactory = callFactory;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopConfig.instanceId + "[setCallFactoryImpl] set CallFactoryImpl succeed.callFactory=" + callFactory);
            }
        }
    }

    public static void setEnableProperty(String instanceId, String property, boolean enable) {
        if (property != null) {
            MtopConfig mtopConfig = getMtopConfigByID(instanceId);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopConfig.instanceId + "[setEnableProperty] set enableProperty succeed.property=" + property + ",enable=" + enable);
            }
            char c = 65535;
            switch (property.hashCode()) {
                case -514993282:
                    if (property.equals(MtopEnablePropertyType.ENABLE_NOTIFY_SESSION_RET)) {
                        c = 0;
                        break;
                    }
                    break;
                case -309052356:
                    if (property.equals(MtopEnablePropertyType.ENABLE_HEADER_URL_ENCODE)) {
                        c = 2;
                        break;
                    }
                    break;
                case 1971193321:
                    if (property.equals(MtopEnablePropertyType.ENABLE_NEW_DEVICE_ID)) {
                        c = 1;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    mtopConfig.notifySessionResult = enable;
                    return;
                case 1:
                    mtopConfig.enableNewDeviceId = enable;
                    return;
                case 2:
                    mtopConfig.enableHeaderUrlEncode = enable;
                    return;
                default:
                    return;
            }
        }
    }

    public static void setParam(String instanceId, String mtopParamType, @NonNull String key, @NonNull String value) {
        if (mtopParamType != null && key != null && value != null) {
            MtopConfig mtopConfig = getMtopConfigByID(instanceId);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopConfig.instanceId + "[setParam] set Param succeed.mtopParamType=" + mtopParamType + ",key=" + key + ",value=" + value);
            }
            char c = 65535;
            switch (mtopParamType.hashCode()) {
                case 77406376:
                    if (mtopParamType.equals(MtopParamType.QUERY)) {
                        c = 1;
                        break;
                    }
                    break;
                case 1924418611:
                    if (mtopParamType.equals(MtopParamType.ABTEST)) {
                        c = 2;
                        break;
                    }
                    break;
                case 2127025805:
                    if (mtopParamType.equals(MtopParamType.HEADER)) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    mtopConfig.mtopGlobalHeaders.put(key, value);
                    return;
                case 1:
                    mtopConfig.mtopGlobalQuerys.put(key, value);
                    return;
                case 2:
                    mtopConfig.mtopGlobalABTestParams.put(key, value);
                    return;
                default:
                    return;
            }
        }
    }

    public static void removeParam(String instanceId, String mtopParamType, @NonNull String key) {
        if (mtopParamType != null && key != null) {
            MtopConfig mtopConfig = getMtopConfigByID(instanceId);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, mtopConfig.instanceId + "[removeParam] remove Param succeed.mtopParamType=" + mtopParamType + ",key=" + key);
            }
            char c = 65535;
            switch (mtopParamType.hashCode()) {
                case 77406376:
                    if (mtopParamType.equals(MtopParamType.QUERY)) {
                        c = 1;
                        break;
                    }
                    break;
                case 1924418611:
                    if (mtopParamType.equals(MtopParamType.ABTEST)) {
                        c = 2;
                        break;
                    }
                    break;
                case 2127025805:
                    if (mtopParamType.equals(MtopParamType.HEADER)) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    mtopConfig.mtopGlobalHeaders.remove(key);
                    return;
                case 1:
                    mtopConfig.mtopGlobalQuerys.remove(key);
                    return;
                case 2:
                    mtopConfig.mtopGlobalABTestParams.remove(key);
                    return;
                default:
                    return;
            }
        }
    }
}
