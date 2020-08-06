package mtopsdk.mtop.antiattack;

import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.global.SwitchConfig;

public class ApiLockHelper {
    private static final long LOCK_PERIOD = 10;
    private static final String TAG = "mtopsdk.ApiLockHelper";
    private static ConcurrentHashMap<String, LockedEntity> lockedMap = new ConcurrentHashMap<>();

    public static boolean iSApiLocked(String apiFullName, long currentTime) {
        boolean isLocked = false;
        if (StringUtils.isBlank(apiFullName)) {
            return false;
        }
        LockedEntity entity = lockedMap.get(apiFullName);
        if (entity != null) {
            if (Math.abs(currentTime - entity.lockStartTime) < entity.lockInterval) {
                isLocked = true;
            } else {
                lockedMap.remove(apiFullName);
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                    TBSdkLog.w(TAG, "[iSApiLocked]remove apiKey=" + apiFullName);
                }
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                TBSdkLog.w(TAG, "[iSApiLocked] isLocked=" + isLocked + ", " + buildApiLockLog(currentTime, entity));
            }
        }
        return isLocked;
    }

    public static void lock(String apiFullName, long currentTime) {
        if (!StringUtils.isBlank(apiFullName)) {
            LockedEntity entity = lockedMap.get(apiFullName);
            long lockInterval = SwitchConfig.getInstance().getIndividualApiLockInterval(apiFullName);
            if (lockInterval <= 0) {
                lockInterval = SwitchConfig.getInstance().getGlobalApiLockInterval();
                if (lockInterval <= 0) {
                    lockInterval = LOCK_PERIOD;
                }
            }
            if (entity == null) {
                entity = new LockedEntity(apiFullName, currentTime, lockInterval);
            } else {
                entity.lockStartTime = currentTime;
                entity.lockInterval = lockInterval;
            }
            lockedMap.put(apiFullName, entity);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                TBSdkLog.w(TAG, "[lock]" + buildApiLockLog(currentTime, entity));
            }
        }
    }

    private static String buildApiLockLog(long currentTime, LockedEntity entity) {
        StringBuilder logBuilder = new StringBuilder(32);
        logBuilder.append(", currentTime=").append(currentTime);
        logBuilder.append(", lockEntity=" + entity.toString());
        return logBuilder.toString();
    }
}
