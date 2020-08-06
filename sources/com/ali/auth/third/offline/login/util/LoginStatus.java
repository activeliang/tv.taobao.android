package com.ali.auth.third.offline.login.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.CommonUtils;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginStatus {
    private static final String CURRENT_PROCESS = "currentProcess";
    private static final String IS_LOGIGING = "isLogining";
    private static final String NOTIFY_LOGIN_STATUS_CHANGE = "NOTIFY_LOGIN_STATUS_CHANGE";
    public static final String TAG = "login.LoginStatus";
    /* access modifiers changed from: private */
    public static AtomicBoolean isLogining = new AtomicBoolean(false);
    private static Context mContext;
    private static BroadcastReceiver mStatusReceiver;

    public static void init(Context context) {
        mContext = context;
        mStatusReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null && !TextUtils.equals(CommonUtils.getCurrentProcessName(), intent.getStringExtra(LoginStatus.CURRENT_PROCESS))) {
                    LoginStatus.isLogining.set(intent.getBooleanExtra(LoginStatus.IS_LOGIGING, false));
                }
            }
        };
        mContext.registerReceiver(mStatusReceiver, new IntentFilter(NOTIFY_LOGIN_STATUS_CHANGE));
    }

    private static void nofityStatusChange() {
        if (mContext != null && mStatusReceiver != null) {
            Intent intent = new Intent(NOTIFY_LOGIN_STATUS_CHANGE);
            intent.putExtra(CURRENT_PROCESS, CommonUtils.getCurrentProcessName());
            intent.putExtra(IS_LOGIGING, isLogining.get());
            intent.setPackage(mContext.getPackageName());
            mContext.sendBroadcast(intent);
        }
    }

    public static boolean isLogining() {
        return isLogining.get();
    }

    public static synchronized boolean compareAndSetLogining(boolean expect, boolean update) {
        boolean result;
        synchronized (LoginStatus.class) {
            result = isLogining.compareAndSet(expect, update);
            if (!result || !expect || !update) {
                nofityStatusChange();
            }
        }
        return result;
    }

    public static void resetLoginFlag() {
        SDKLogger.w(TAG, "reset login status");
        if (isLogining.compareAndSet(true, false)) {
            nofityStatusChange();
        }
    }
}
