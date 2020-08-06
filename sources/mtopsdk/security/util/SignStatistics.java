package mtopsdk.security.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.stat.IUploadStats;

public class SignStatistics {
    private static final String SIGN_EXCEPTION_MONITOR_POINT = "signException";
    private static final String SIGN_STATS_MODULE = "mtopsdk";
    private static final String TAG = "mtopsdk.SignStatistics";
    private static volatile IUploadStats mUploadStats = null;
    private static volatile AtomicBoolean registerFlag = new AtomicBoolean(false);

    public interface SignStatsType {
        public static final String TYPE_AVMP_INSTANCE = "AVMPInstance";
        public static final String TYPE_GET_APPKEY = "GetAppKey";
        public static final String TYPE_GET_SECBODY = "GetSecBody";
        public static final String TYPE_INIT_UMID = "InitUMID";
        public static final String TYPE_INVOKE_AVMP = "InvokeAVMP";
        public static final String TYPE_SG_MANAGER = "SGManager";
        public static final String TYPE_SIGN_HMAC_SHA1 = "SignHMACSHA1";
        public static final String TYPE_SIGN_MTOP_REQUEST = "SignMtopRequest";

        @Retention(RetentionPolicy.SOURCE)
        public @interface Definition {
        }
    }

    public static void setIUploadStats(IUploadStats uploadStats) {
        mUploadStats = uploadStats;
        TBSdkLog.i(TAG, "set IUploadStats =" + uploadStats);
    }

    private static void registerStats() {
        Set<String> dimensions = new HashSet<>();
        dimensions.add("type");
        dimensions.add("errorcode");
        dimensions.add("flag");
        if (mUploadStats != null) {
            mUploadStats.onRegister(SIGN_STATS_MODULE, SIGN_EXCEPTION_MONITOR_POINT, dimensions, (Set<String>) null, false);
        }
    }

    public static void commitStats(String signStatsType, String errorCode, String flag) {
        if (mUploadStats != null) {
            if (registerFlag.compareAndSet(false, true)) {
                registerStats();
            }
            Map<String, String> dimensions = new HashMap<>();
            dimensions.put("type", signStatsType);
            dimensions.put("errorcode", errorCode);
            dimensions.put("flag", flag);
            if (mUploadStats != null) {
                mUploadStats.onCommit(SIGN_STATS_MODULE, SIGN_EXCEPTION_MONITOR_POINT, dimensions, (Map<String, Double>) null);
            }
        }
    }
}
