package com.yunos.tv.core.config;

import android.os.Bundle;
import com.zhiping.dev.android.logger.ZpLogger;

public enum UpdateStatus {
    START_ACTIVITY,
    FETUREDAILOG,
    UPDATE_DIALOG,
    UNKNOWN;
    
    private static final String TAG = "UpdateStatus";
    private static Bundle sBundle;
    private static UpdateStatus sUpdateStatus;

    static {
        sUpdateStatus = UNKNOWN;
    }

    public static void setUpdateStatus(UpdateStatus status, Bundle bundle) {
        ZpLogger.d(TAG, "setUpdateStatus: " + status + " Bundle: " + bundle);
        sUpdateStatus = status;
        sBundle = bundle;
    }

    public static UpdateStatus getUpdateStatus() {
        return sUpdateStatus;
    }

    public static Bundle getBundle() {
        return sBundle;
    }
}
