package mtopsdk.mtop.global;

import android.content.Context;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.config.MtopConfigListener;
import mtopsdk.common.util.LocalConfig;
import mtopsdk.common.util.RemoteConfig;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.util.ErrorConstant;

public class SwitchConfig {
    private static final String TAG = "mtopsdk.SwitchConfig";
    public static final HashSet<String> authErrorCodeSet = new HashSet<>(8);
    private static final SwitchConfig config = new SwitchConfig();
    public static final Map<String, String> errorMappingMsgMap = new ConcurrentHashMap(8);
    private static volatile Map<String, String> individualApiLockIntervalMap = new ConcurrentHashMap(8);
    private static final LocalConfig localConfig = LocalConfig.getInstance();
    private static MtopConfigListener mtopConfigListener = null;
    private static final RemoteConfig remoteConfig = RemoteConfig.getInstance();
    public volatile Set<String> degradeApiCacheSet = null;
    public volatile Set<String> degradeBizErrorMappingApiSet = null;

    static {
        errorMappingMsgMap.put(ErrorConstant.ErrorMappingType.NETWORK_ERROR_MAPPING, ErrorConstant.MappingMsg.NETWORK_MAPPING_MSG);
        errorMappingMsgMap.put(ErrorConstant.ErrorMappingType.FLOW_LIMIT_ERROR_MAPPING, ErrorConstant.MappingMsg.FLOW_LIMIT_MAPPING_MSG);
        errorMappingMsgMap.put(ErrorConstant.ErrorMappingType.SERVICE_ERROR_MAPPING, ErrorConstant.MappingMsg.SERVICE_MAPPING_MSG);
        authErrorCodeSet.add(ErrorConstant.ERRCODE_FAIL_SYS_ACCESS_TOKEN_EXPIRED);
        authErrorCodeSet.add(ErrorConstant.ERRCODE_FAIL_SYS_ILLEGAL_ACCESS_TOKEN);
    }

    private SwitchConfig() {
    }

    public static SwitchConfig getInstance() {
        return config;
    }

    public void initConfig(Context context) {
        if (mtopConfigListener != null) {
            mtopConfigListener.initConfig(context);
        }
    }

    public void setMtopConfigListener(MtopConfigListener mtopConfigListener2) {
        mtopConfigListener = mtopConfigListener2;
    }

    public static MtopConfigListener getMtopConfigListener() {
        return mtopConfigListener;
    }

    public boolean isGlobalErrorCodeMappingOpen() {
        return localConfig.enableErrorCodeMapping && remoteConfig.enableErrorCodeMapping;
    }

    public boolean isBizErrorCodeMappingOpen() {
        return localConfig.enableBizErrorCodeMapping && remoteConfig.enableBizErrorCodeMapping;
    }

    public boolean isGlobalSpdySwitchOpen() {
        return localConfig.enableSpdy && remoteConfig.enableSpdy;
    }

    public SwitchConfig setGlobalSpdySwitchOpen(boolean spdySwitchOpen) {
        localConfig.enableSpdy = spdySwitchOpen;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[setGlobalSpdySwitchOpen]set local spdySwitchOpen=" + spdySwitchOpen);
        }
        return this;
    }

    public boolean isGlobalSpdySslSwitchOpen() {
        return localConfig.enableSsl && remoteConfig.enableSsl;
    }

    public SwitchConfig setGlobalSpdySslSwitchOpen(boolean spdySwlSwitchOpen) {
        localConfig.enableSsl = spdySwlSwitchOpen;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[setGlobalSpdySslSwitchOpen]set local spdySslSwitchOpen=" + spdySwlSwitchOpen);
        }
        return this;
    }

    public long getGlobalApiLockInterval() {
        return remoteConfig.apiLockInterval;
    }

    public long getGlobalAttackAttackWaitInterval() {
        return remoteConfig.antiAttackWaitInterval;
    }

    public long getGlobalBizErrorMappingCodeLength() {
        return remoteConfig.bizErrorMappingCodeLength;
    }

    @Deprecated
    public boolean isGlobalUnitSwitchOpen() {
        return localConfig.enableUnit && remoteConfig.enableUnit;
    }

    public boolean isGlobalCacheSwitchOpen() {
        return remoteConfig.enableCache;
    }

    @Deprecated
    public SwitchConfig setGlobalUnitSwitchOpen(boolean unitSwitchOpen) {
        localConfig.enableUnit = unitSwitchOpen;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[setGlobalUnitSwitchOpen]set local unitSwitchOpen=" + unitSwitchOpen);
        }
        return this;
    }

    public boolean isMtopsdkPropertySwitchOpen() {
        return localConfig.enableProperty && remoteConfig.enableProperty;
    }

    public SwitchConfig setMtopsdkPropertySwitchOpen(boolean mtopsdkPropertySwitchOpen) {
        localConfig.enableProperty = mtopsdkPropertySwitchOpen;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[setMtopsdkPropertySwitchOpen]set local mtopsdkPropertySwitchOpen=" + mtopsdkPropertySwitchOpen);
        }
        return this;
    }

    public Map<String, String> getIndividualApiLockIntervalMap() {
        return individualApiLockIntervalMap;
    }

    public long getIndividualApiLockInterval(String apiKey) {
        long interval = 0;
        if (StringUtils.isBlank(apiKey)) {
            return 0;
        }
        String intervalStr = individualApiLockIntervalMap.get(apiKey);
        if (StringUtils.isBlank(intervalStr)) {
            return 0;
        }
        try {
            interval = Long.parseLong(intervalStr);
        } catch (Exception e) {
            TBSdkLog.e(TAG, "[getIndividualApiLockInterval]parse individual apiLock interval error.apiKey=" + apiKey + " ---" + e.toString());
        }
        return interval;
    }
}
