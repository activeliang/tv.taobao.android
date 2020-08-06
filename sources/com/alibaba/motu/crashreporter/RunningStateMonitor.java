package com.alibaba.motu.crashreporter;

import android.content.Context;
import android.os.Process;
import android.os.SystemClock;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import com.alibaba.motu.crashreporter.CrashReporter;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import java.io.File;

final class RunningStateMonitor {
    public static final int STARTUP_STATE_CRASH_TOO_MANY = 16;
    public static final int STARTUP_STATE_NORMAL = 0;
    public static final int STARTUP_STATE_STARTUP_TOO_FAST = 1;
    Context mContext;
    RunningState mLastRunningState;
    File mMonitorFile = this.mStorageManager.getProcessTombstoneFile("STARTUP_MONITOR");
    RunningState mRunningState;
    StorageManager mStorageManager;

    public RunningStateMonitor(Context context, String appId, String appKey, String appVersion, String processName, long startupTime, StorageManager storageManager) {
        this.mContext = context;
        this.mStorageManager = storageManager;
        this.mRunningState = new RunningState(this.mContext, appId, appKey, appVersion, processName, startupTime);
        String strStatupState = Utils.readLineAndDel(this.mMonitorFile);
        if (StringUtils.isNotBlank(strStatupState)) {
            RunningState lastRunningState = new RunningState();
            try {
                lastRunningState.deserialize(strStatupState);
                this.mLastRunningState = lastRunningState;
            } catch (Exception e) {
                LogUtil.e("lastRunningState deserialize", e);
            }
        }
        if (this.mLastRunningState != null) {
            boolean deviceRestart = this.mRunningState.mElapsedRealtime < this.mLastRunningState.mElapsedRealtime;
            this.mRunningState.mTotalStartCount += this.mLastRunningState.mTotalStartCount;
            if (!deviceRestart) {
                this.mRunningState.mContinuousStartCount += this.mLastRunningState.mContinuousStartCount;
                if (this.mRunningState.mElapsedRealtime / 60000 == this.mLastRunningState.mElapsedRealtime / 60000) {
                    this.mRunningState.mContinuousStartCount1Minute += this.mLastRunningState.mContinuousStartCount1Minute;
                    this.mRunningState.mContinuousStartCount5Minute += this.mLastRunningState.mContinuousStartCount5Minute;
                    this.mRunningState.mContinuousStartCount1Hour += this.mLastRunningState.mContinuousStartCount1Hour;
                    this.mRunningState.mContinuousStartCount24Hour += this.mLastRunningState.mContinuousStartCount24Hour;
                } else if (this.mRunningState.mElapsedRealtime / 300000 == this.mLastRunningState.mElapsedRealtime / 300000) {
                    this.mRunningState.mContinuousStartCount5Minute += this.mLastRunningState.mContinuousStartCount5Minute;
                    this.mRunningState.mContinuousStartCount1Hour += this.mLastRunningState.mContinuousStartCount1Hour;
                    this.mRunningState.mContinuousStartCount24Hour += this.mLastRunningState.mContinuousStartCount24Hour;
                } else if (this.mRunningState.mElapsedRealtime / 3600000 == this.mLastRunningState.mElapsedRealtime / 3600000) {
                    this.mRunningState.mContinuousStartCount1Hour += this.mLastRunningState.mContinuousStartCount1Hour;
                    this.mRunningState.mContinuousStartCount24Hour += this.mLastRunningState.mContinuousStartCount24Hour;
                } else if (this.mRunningState.mElapsedRealtime / ZipAppConstants.UPDATEGROUPID_AGE == this.mLastRunningState.mElapsedRealtime / ZipAppConstants.UPDATEGROUPID_AGE) {
                    this.mRunningState.mContinuousStartCount24Hour += this.mLastRunningState.mContinuousStartCount24Hour;
                }
            }
        }
        flushRunningState();
    }

    /* access modifiers changed from: package-private */
    public void analyzeStartupState(CrashReporter.DefaultStartupStateAnalyzeCallback startupStateAnalyzeCallback) {
        int startupState = 0;
        if (this.mRunningState.mContinuousStartCount1Minute >= 3 || this.mRunningState.mContinuousStartCount5Minute >= 10) {
            startupState = 0 | 16;
        }
        if (this.mLastRunningState != null && this.mRunningState.mElapsedRealtime - this.mLastRunningState.mElapsedRealtime < 30000) {
            startupState |= 1;
        }
        if (startupStateAnalyzeCallback != null) {
            startupStateAnalyzeCallback.onComplete(startupState);
        }
    }

    private synchronized void flushRunningState() {
        Utils.writeFile(this.mMonitorFile, this.mRunningState.serialize());
    }

    public void refreshAppVersion(String appVersion) {
        if (StringUtils.isNotBlank(appVersion) && !appVersion.equals(this.mRunningState.mAppVersion)) {
            this.mRunningState.mAppVersion = appVersion;
            flushRunningState();
        }
    }

    class RunningState {
        String mAppId;
        String mAppKey;
        String mAppVersion;
        int mContinuousStartCount;
        int mContinuousStartCount1Hour;
        int mContinuousStartCount1Minute;
        int mContinuousStartCount24Hour;
        int mContinuousStartCount5Minute;
        long mElapsedRealtime;
        int mPid;
        String mProcessName;
        long mStartupTime;
        long mTimestamp;
        int mTotalStartCount;
        long mUptimeMillis;

        RunningState() {
        }

        RunningState(Context context, String appId, String appKey, String appVersion, String processName, long startupTime) {
            this.mAppId = appId;
            this.mAppKey = appKey;
            this.mAppVersion = appVersion;
            this.mStartupTime = startupTime;
            this.mUptimeMillis = SystemClock.uptimeMillis();
            this.mElapsedRealtime = SystemClock.elapsedRealtime();
            this.mTimestamp = System.currentTimeMillis();
            this.mPid = Process.myPid();
            this.mProcessName = processName;
            this.mTotalStartCount = 1;
            this.mContinuousStartCount = 1;
            this.mContinuousStartCount24Hour = 1;
            this.mContinuousStartCount1Hour = 1;
            this.mContinuousStartCount1Minute = 1;
            this.mContinuousStartCount5Minute = 1;
        }

        /* access modifiers changed from: package-private */
        public String serialize() {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", new Object[]{this.mAppId, this.mAppKey, this.mAppVersion, Long.valueOf(this.mStartupTime), Long.valueOf(this.mUptimeMillis), Long.valueOf(this.mElapsedRealtime), Long.valueOf(this.mTimestamp), Integer.valueOf(this.mPid), this.mProcessName, Integer.valueOf(this.mTotalStartCount), Integer.valueOf(this.mContinuousStartCount), Integer.valueOf(this.mContinuousStartCount24Hour), Integer.valueOf(this.mContinuousStartCount1Hour), Integer.valueOf(this.mContinuousStartCount1Minute), Integer.valueOf(this.mContinuousStartCount5Minute)});
        }

        /* access modifiers changed from: package-private */
        public void deserialize(String strStatupState) {
            String[] parts = strStatupState.split(",");
            this.mAppId = parts[0];
            this.mAppKey = parts[1];
            this.mAppVersion = parts[2];
            this.mStartupTime = Long.parseLong(parts[3]);
            this.mUptimeMillis = Long.parseLong(parts[4]);
            this.mElapsedRealtime = Long.parseLong(parts[5]);
            this.mTimestamp = Long.parseLong(parts[6]);
            this.mPid = Integer.parseInt(parts[7]);
            this.mProcessName = parts[8];
            this.mTotalStartCount = Integer.parseInt(parts[9]);
            this.mContinuousStartCount = Integer.parseInt(parts[10]);
            this.mContinuousStartCount24Hour = Integer.parseInt(parts[11]);
            this.mContinuousStartCount1Hour = Integer.parseInt(parts[12]);
            this.mContinuousStartCount1Minute = Integer.parseInt(parts[13]);
            this.mContinuousStartCount5Minute = Integer.parseInt(parts[14]);
        }
    }
}
