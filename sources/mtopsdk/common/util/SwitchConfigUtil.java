package mtopsdk.common.util;

import java.util.Map;
import mtopsdk.common.config.MtopConfigListener;

public class SwitchConfigUtil {
    public static final String ANTI_ATTACK_WAIT_INTERVAL_KEY = "antiAttackWaitInterval";
    public static final String API_LOCK_INTERVAL_KEY = "apiLockInterval";
    public static final String BIZ_ERROR_MAPPING_CODE_LENGTH_KEY = "bizErrorMappingCodeLength";
    public static final String CONFIG_GROUP_MTOPSDK_ANDROID_SWITCH = "mtopsdk_android_switch";
    public static final String CONFIG_GROUP_MTOPSDK_APICACHE_BLOCKINFO_SWITCH = "mtopsdk_apicache_blockinfo";
    public static final String CONFIG_GROUP_MTOPSDK_UPLOAD_SWITCH = "mtopsdk_upload_switch";
    public static final String DEGRADE_API_CACHE_LIST_KEY = "degradeApiCacheList";
    public static final String DEGRADE_BIZCODE_SET_KEY = "degradeBizcodeSet";
    public static final String DEGRADE_BIZ_ERROR_MAPPING_API_LIST_KEY = "degradeBizErrorMappingApiList";
    public static final String DEGRADE_TO_SQLITE_KEY = "degradeToSQLite";
    public static final String ENABLE_BIZ_ERROR_CODE_MAPPING_KEY = "enableBizErrorCodeMapping";
    public static final String ENABLE_CACHE_KEY = "enableCache";
    public static final String ENABLE_ERROR_CODE_MAPPING_KEY = "enableErrorCodeMapping";
    public static final String ENABLE_MTOPSDK_PROPERTY_KEY = "enableProperty";
    public static final String ENABLE_SPDY_KEY = "enableSpdy";
    public static final String ENABLE_SSL_KEY = "enableSsl";
    @Deprecated
    public static final String ENABLE_UNIT_KEY = "enableUnit";
    public static final String ERROR_MAPPING_MSG_KEY = "errorMappingMsg";
    @Deprecated
    public static final String GZIP_THRESHOLD_KEY = "gzipThresHold";
    public static final String INDIVIDUAL_API_LOCK_INTERVAL_KEY = "individualApiLockInterval";
    public static final String REMOVE_CACHE_BLOCK_LIST_KEY = "removeCacheBlockList";
    public static final String SEGMENT_RETRY_TIMES_KEY = "segmentRetryTimes";
    public static final String SEGMENT_SIZE_MAP_KEY = "segmentSizeMap";
    private static final String TAG = "mtopsdk.SwitchConfigUtil";
    public static final String UPLOAD_THREAD_NUMS_KEY = "uploadThreadNums";
    public static final String USEHTTPS_BIZCODE_SET_KEY = "useHttpsBizcodeSet";
    private static MtopConfigListener listener = null;

    public static void setMtopConfigListener(MtopConfigListener listener2) {
        if (listener2 != null) {
            listener = listener2;
        }
    }

    public static String getSwitchConfig(String groupName, String key, String defaultValue) {
        if (listener != null) {
            return listener.getConfig(groupName, key, defaultValue);
        }
        TBSdkLog.w(TAG, "[getSwitchConfig] MtopConfigListener is null");
        return defaultValue;
    }

    public static Map<String, String> getSwitchConfigByGroupName(String groupName) {
        if (listener != null) {
            return listener.getSwitchConfigByGroupName(groupName);
        }
        TBSdkLog.w(TAG, "[getSwitchConfigByGroupName] MtopConfigListener is null");
        return null;
    }
}
