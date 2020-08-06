package android.taobao.windvane.monitor;

import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.util.TaoLog;

public class WVPackageMonitorImpl implements WVPackageMonitorInterface {
    private static final String TAG = WVPackageMonitorImpl.class.getSimpleName();
    private long appResumeTime = 0;
    private long diffTime = 0;

    public void packageApp(ZipAppInfo info, String packageApp, String version, String updateType, boolean isSuccess, long updateTime, long downloadTime, int errorType, String errorMessage, boolean isWifi, long updateStartTime) {
        String onlineState;
        String wifiState = isWifi ? "1" : "0";
        long serverTime = info.t + this.diffTime;
        if (this.appResumeTime > serverTime) {
            onlineState = "0";
        } else if (this.appResumeTime > 0) {
            onlineState = "1";
        } else {
            onlineState = "2";
        }
        AppMonitorUtil.commitPackageAppUpdateInfo(info, onlineState, wifiState, updateTime, downloadTime, System.currentTimeMillis() - serverTime, updateStartTime - serverTime);
        if (isSuccess) {
            AppMonitorUtil.commitPackageAppUpdateSuccess(info.name);
        } else {
            AppMonitorUtil.commitPackageAppUpdateError(String.valueOf(errorType), errorMessage, info.name);
        }
    }

    public void uploadStartAppTime(long time) {
        this.appResumeTime = time;
        TaoLog.d(TAG, "uploadStartAppTime : " + time);
    }

    public void uploadDiffTimeTime(long time) {
        this.diffTime = time;
        TaoLog.d(TAG, "uploadDiffTimeTime : " + time);
    }

    public void onStartCleanAppCache(long beforeDelSpace, int expectedNum, int installedNum, int willDeleteCount, float customRadio, int noCacheCount, int normalCount, float noCacheRatio, int cleanCause) {
        AppMonitorUtil.commitPackageClearUpInfo(beforeDelSpace, expectedNum, installedNum, willDeleteCount, customRadio, noCacheCount, normalCount, noCacheRatio, cleanCause);
    }

    public void commitPackageQueueInfo(String isInitialUpdate, long updateCount, long succesCount) {
        AppMonitorUtil.commitPackageQueueInfo(isInitialUpdate, updateCount, succesCount);
    }

    public void commitPackageVisitInfo(String pkgName, String hasVerifyTime, long time, long matchTime, long readTime, long verifyTime, long seq) {
        AppMonitorUtil.commitPackageVisitInfo(pkgName, hasVerifyTime, time, matchTime, readTime, verifyTime, seq);
    }

    public void commitPackageVisitError(String pkgName, String errorMsg, String errorCode) {
        AppMonitorUtil.commitPackageAppVisitError(pkgName, errorMsg, errorCode);
        TaoLog.d(TAG, "pkgName : " + pkgName + " errorMsg : " + errorMsg + " errorCode :" + errorCode);
    }

    public void commitPackageVisitSuccess(String pkgName, long seq) {
        AppMonitorUtil.commitPackageVisitSuccess(pkgName, seq);
    }

    public void commitPackageUpdateStartInfo(long startTime, long endTime) {
        AppMonitorUtil.commitPackageUpdateStartInfo(startTime, endTime);
    }
}
