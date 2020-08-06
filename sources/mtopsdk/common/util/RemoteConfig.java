package mtopsdk.common.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import mtopsdk.common.util.TBSdkLog;

public class RemoteConfig {
    private static final String TAG = "mtopsdk.RemoteConfig";
    private static Map<String, Integer> segmentSizeMap = new HashMap();
    public long antiAttackWaitInterval;
    public long apiLockInterval;
    public long bizErrorMappingCodeLength;
    private Map<String, String> configItemsMap;
    public String degradeApiCacheList;
    public String degradeBizErrorMappingApiList;
    public final Set<String> degradeBizcodeSets;
    public boolean degradeToSQLite;
    public boolean enableArupTlog;
    public boolean enableBizErrorCodeMapping;
    public boolean enableCache;
    public boolean enableErrorCodeMapping;
    public boolean enableProperty;
    public boolean enableSpdy;
    public boolean enableSsl;
    @Deprecated
    public boolean enableUnit;
    public String errorMappingMsg;
    public String individualApiLockInterval;
    public String removeCacheBlockList;
    public int segmentRetryTimes;
    public int uploadThreadNums;
    public final Set<String> useHttpsBizcodeSets;

    private static class RemoteConfigInstanceHolder {
        /* access modifiers changed from: private */
        public static RemoteConfig instance = new RemoteConfig();

        private RemoteConfigInstanceHolder() {
        }
    }

    public static RemoteConfig getInstance() {
        return RemoteConfigInstanceHolder.instance;
    }

    private RemoteConfig() {
        this.configItemsMap = null;
        this.enableErrorCodeMapping = true;
        this.enableBizErrorCodeMapping = false;
        this.bizErrorMappingCodeLength = 24;
        this.enableSpdy = true;
        this.enableUnit = true;
        this.enableSsl = true;
        this.enableCache = true;
        this.enableProperty = false;
        this.degradeToSQLite = false;
        this.apiLockInterval = 10;
        this.individualApiLockInterval = "";
        this.degradeApiCacheList = "";
        this.removeCacheBlockList = "";
        this.degradeBizErrorMappingApiList = "";
        this.errorMappingMsg = "";
        this.antiAttackWaitInterval = 20;
        this.segmentRetryTimes = -1;
        this.uploadThreadNums = -1;
        this.useHttpsBizcodeSets = new HashSet();
        this.degradeBizcodeSets = new HashSet();
        this.enableArupTlog = true;
    }

    static {
        segmentSizeMap.put("2G", 32768);
        segmentSizeMap.put("3G", 65536);
        segmentSizeMap.put("4G", 524288);
        segmentSizeMap.put("WIFI", 524288);
        segmentSizeMap.put("UNKONWN", 131072);
        segmentSizeMap.put("NET_NO", 131072);
    }

    public Integer getSegmentSize(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return segmentSizeMap.get(key);
    }

    public void setSegmentSize(String key, int segmentSize) {
        if (!StringUtils.isBlank(key) && segmentSize > 0) {
            segmentSizeMap.put(key, Integer.valueOf(segmentSize));
        }
    }

    public void updateRemoteConfig() {
        this.configItemsMap = SwitchConfigUtil.getSwitchConfigByGroupName(SwitchConfigUtil.CONFIG_GROUP_MTOPSDK_ANDROID_SWITCH);
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[updateRemoteConfig] configItemsMap=" + this.configItemsMap);
        }
        if (this.configItemsMap != null) {
            String enableErrorCodeMappingConfig = getConfigItemByKey(SwitchConfigUtil.ENABLE_ERROR_CODE_MAPPING_KEY, "true");
            this.enableErrorCodeMapping = "true".equals(enableErrorCodeMappingConfig);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setEnableErrorCodeMapping]remote enableErrorCodeMappingConfig=" + enableErrorCodeMappingConfig + ",enableErrorCodeMapping=" + this.enableErrorCodeMapping);
            }
            String enableBizErrorCodeMappingConfig = getConfigItemByKey(SwitchConfigUtil.ENABLE_BIZ_ERROR_CODE_MAPPING_KEY, "false");
            this.enableBizErrorCodeMapping = "true".equals(enableBizErrorCodeMappingConfig);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setEnableBizErrorCodeMapping]remote enableBizErrorCodeMappingConfig=" + enableBizErrorCodeMappingConfig + ",enableBizErrorCodeMapping=" + this.enableBizErrorCodeMapping);
            }
            String spdySwitchConfig = getConfigItemByKey(SwitchConfigUtil.ENABLE_SPDY_KEY, "true");
            this.enableSpdy = "true".equals(spdySwitchConfig);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setEnableSpdy]remote spdySwitchConfig=" + spdySwitchConfig + ",enableSpdy=" + this.enableSpdy);
            }
            String spdySslSwitchConfig = getConfigItemByKey(SwitchConfigUtil.ENABLE_SSL_KEY, "true");
            this.enableSsl = "true".equals(spdySslSwitchConfig);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setEnableSsl]remote spdySslSwitchConfig=" + spdySslSwitchConfig + ",enableSsl=" + this.enableSsl);
            }
            String cacheSwitchConfig = getConfigItemByKey(SwitchConfigUtil.ENABLE_CACHE_KEY, "true");
            this.enableCache = "true".equalsIgnoreCase(cacheSwitchConfig);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setEnableCache]remote cacheSwitchConfig=" + cacheSwitchConfig + ",enableCache=" + this.enableCache);
            }
            String mtopsdkPropertySwitchConfig = getConfigItemByKey(SwitchConfigUtil.ENABLE_MTOPSDK_PROPERTY_KEY, "false");
            this.enableProperty = !"false".equalsIgnoreCase(mtopsdkPropertySwitchConfig);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setEnableProperty]remote mtopsdkPropertySwitchConfig=" + mtopsdkPropertySwitchConfig + ",enableProperty=" + this.enableProperty);
            }
            String degradeToSQLiteConfig = getConfigItemByKey(SwitchConfigUtil.DEGRADE_TO_SQLITE_KEY, "false");
            this.degradeToSQLite = !"false".equalsIgnoreCase(degradeToSQLiteConfig);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setDegradeToSQLite]remote degradeToSQLiteConfig=" + degradeToSQLiteConfig + ",degradeToSQLite=" + this.degradeToSQLite);
            }
            String apiLockIntervalConfig = getConfigItemByKey(SwitchConfigUtil.API_LOCK_INTERVAL_KEY, (String) null);
            if (StringUtils.isNotBlank(apiLockIntervalConfig)) {
                try {
                    this.apiLockInterval = Long.parseLong(apiLockIntervalConfig);
                } catch (Exception e) {
                    TBSdkLog.e(TAG, "[setApiLockInterval]parse apiLockIntervalConfig error,apiLockIntervalConfig=" + apiLockIntervalConfig);
                }
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setApiLockInterval]remote apiLockIntervalConfig=" + apiLockIntervalConfig + ",apiLockInterval=" + this.apiLockInterval);
            }
            String antiAttackWaitIntervalConfig = getConfigItemByKey(SwitchConfigUtil.ANTI_ATTACK_WAIT_INTERVAL_KEY, (String) null);
            if (StringUtils.isNotBlank(antiAttackWaitIntervalConfig)) {
                try {
                    this.antiAttackWaitInterval = Long.parseLong(antiAttackWaitIntervalConfig);
                } catch (Exception e2) {
                    TBSdkLog.e(TAG, "[setAntiAttackWaitInterval]parse antiAttackWaitIntervalConfig error,antiAttackWaitIntervalConfig=" + antiAttackWaitIntervalConfig);
                }
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setAntiAttackWaitInterval]remote antiAttackWaitIntervalConfig=" + antiAttackWaitIntervalConfig + ",antiAttackWaitInterval=" + this.antiAttackWaitInterval);
            }
            String bizErrorMappingCodeLengthConfig = getConfigItemByKey(SwitchConfigUtil.BIZ_ERROR_MAPPING_CODE_LENGTH_KEY, (String) null);
            if (StringUtils.isNotBlank(bizErrorMappingCodeLengthConfig)) {
                try {
                    this.bizErrorMappingCodeLength = Long.parseLong(bizErrorMappingCodeLengthConfig);
                } catch (Exception e3) {
                    TBSdkLog.e(TAG, "[setBizErrorMappingCodeLength]parse bizErrorMappingCodeLengthConfig error,bizErrorMappingCodeLengthConfig=" + bizErrorMappingCodeLengthConfig);
                }
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[setBizErrorMappingCodeLength]remote bizErrorMappingCodeLengthConfig=" + bizErrorMappingCodeLengthConfig + ",bizErrorMappingCodeLength=" + this.bizErrorMappingCodeLength);
            }
            this.individualApiLockInterval = getConfigItemByKey(SwitchConfigUtil.INDIVIDUAL_API_LOCK_INTERVAL_KEY, "");
            this.degradeApiCacheList = getConfigItemByKey(SwitchConfigUtil.DEGRADE_API_CACHE_LIST_KEY, "");
            this.removeCacheBlockList = getConfigItemByKey(SwitchConfigUtil.REMOVE_CACHE_BLOCK_LIST_KEY, "");
            this.degradeBizErrorMappingApiList = getConfigItemByKey(SwitchConfigUtil.DEGRADE_BIZ_ERROR_MAPPING_API_LIST_KEY, "");
            this.errorMappingMsg = getConfigItemByKey(SwitchConfigUtil.ERROR_MAPPING_MSG_KEY, "");
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                StringBuilder builder = new StringBuilder(128);
                builder.append("[setOtherConfigItemKey] individualApiLockInterval =").append(this.individualApiLockInterval);
                builder.append(", degradeApiCacheList =").append(this.degradeApiCacheList);
                builder.append(", removeCacheBlockList =").append(this.removeCacheBlockList);
                builder.append(", degradeBizErrorMappingApiList =").append(this.degradeBizErrorMappingApiList);
                builder.append(", errorMappingMsg =").append(this.errorMappingMsg);
                TBSdkLog.i(TAG, builder.toString());
            }
        }
    }

    public void updateUploadRemoteConfig() {
        Map<String, String> uploadConfigItemsMap = SwitchConfigUtil.getSwitchConfigByGroupName(SwitchConfigUtil.CONFIG_GROUP_MTOPSDK_UPLOAD_SWITCH);
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[updateUploadRemoteConfig] uploadConfigItemsMap=" + uploadConfigItemsMap);
        }
        if (uploadConfigItemsMap != null) {
            String segmentRetryTimesStr = uploadConfigItemsMap.get(SwitchConfigUtil.SEGMENT_RETRY_TIMES_KEY);
            if (StringUtils.isNotBlank(segmentRetryTimesStr)) {
                try {
                    int remoteSegmentRetryTimes = Integer.parseInt(segmentRetryTimesStr);
                    if (remoteSegmentRetryTimes >= 0) {
                        this.segmentRetryTimes = remoteSegmentRetryTimes;
                    }
                } catch (Exception e) {
                    TBSdkLog.w(TAG, "[updateUploadRemoteConfig]parse segmentRetryTimes error,segmentRetryTimesStr=" + segmentRetryTimesStr);
                }
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, "[updateUploadRemoteConfig]remote segmentRetryTimesStr=" + segmentRetryTimesStr + ",segmentRetryTimes=" + this.segmentRetryTimes);
                }
            }
            String uploadThreadNumsStr = uploadConfigItemsMap.get(SwitchConfigUtil.UPLOAD_THREAD_NUMS_KEY);
            if (StringUtils.isNotBlank(segmentRetryTimesStr)) {
                try {
                    int remoteUploadThreadNums = Integer.parseInt(uploadThreadNumsStr);
                    if (remoteUploadThreadNums >= 0) {
                        this.uploadThreadNums = remoteUploadThreadNums;
                    }
                } catch (Exception e2) {
                    TBSdkLog.w(TAG, "[updateUploadRemoteConfig]parse uploadThreadNums error,uploadThreadNumsStr=" + uploadThreadNumsStr);
                }
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, "[updateUploadRemoteConfig]remote uploadThreadNumsStr=" + uploadThreadNumsStr + ",uploadThreadNums=" + this.uploadThreadNums);
                }
            }
        }
    }

    private String getConfigItemByKey(String key, String defValue) {
        String value = null;
        try {
            if (this.configItemsMap != null) {
                value = this.configItemsMap.get(key);
            }
        } catch (Exception e) {
            TBSdkLog.w(TAG, "[getConfigItemByKey] get config item error; key=" + key, (Throwable) e);
        }
        if (value == null) {
            return defValue;
        }
        return value;
    }
}
