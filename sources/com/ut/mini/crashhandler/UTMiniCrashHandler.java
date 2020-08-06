package com.ut.mini.crashhandler;

import android.content.Context;
import android.os.Process;
import com.alibaba.analytics.utils.Logger;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTTracker;
import com.ut.mini.crashhandler.UTExceptionParser;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import java.lang.Thread;
import java.util.HashMap;
import java.util.Map;

public class UTMiniCrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "UTCrashHandler";
    private static volatile boolean mCrashing = false;
    private static UTMiniCrashHandler s_instance = new UTMiniCrashHandler();
    private Context mContext = null;
    private Thread.UncaughtExceptionHandler mDefaultHandler = null;
    private boolean mIsTurnOff = true;
    private IUTCrashCaughtListner mListener = null;

    private UTMiniCrashHandler() {
    }

    public static UTMiniCrashHandler getInstance() {
        return s_instance;
    }

    public boolean isTurnOff() {
        return this.mIsTurnOff;
    }

    public void turnOff() {
        if (this.mDefaultHandler != null) {
            Thread.setDefaultUncaughtExceptionHandler(this.mDefaultHandler);
            this.mDefaultHandler = null;
        }
        this.mIsTurnOff = true;
    }

    public void turnOn(Context aContext) {
        _initialize();
    }

    public void setCrashCaughtListener(IUTCrashCaughtListner aListener) {
        this.mListener = aListener;
    }

    private void _initialize() {
        if (this.mIsTurnOff) {
            this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
            this.mIsTurnOff = false;
        }
    }

    public void uncaughtException(Thread pThread, Throwable pException) {
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
        try {
            if (!mCrashing) {
                mCrashing = true;
                if (pException != null) {
                    Logger.e("Caught Exception By UTCrashHandler.Please see log as follows!", new Object[0]);
                    pException.printStackTrace();
                }
                UTExceptionParser.UTExceptionItem lExceptionItem = UTExceptionParser.parse(pException);
                if (!(lExceptionItem == null || lExceptionItem.mCrashDetail == null || lExceptionItem.getExpName() == null || lExceptionItem.getMd5() == null)) {
                    Map<String, String> lMap = null;
                    if (this.mListener != null) {
                        lMap = this.mListener.onCrashCaught(pThread, pException);
                    }
                    if (lMap == null) {
                        lMap = new HashMap<>();
                    }
                    lMap.put("StackTrace", lExceptionItem.getCrashDetail());
                    UTOriginalCustomHitBuilder lBuilder = new UTOriginalCustomHitBuilder("UT", 1, lExceptionItem.getMd5(), lExceptionItem.getExpName(), (String) null, lMap);
                    lBuilder.setProperty("_priority", "5");
                    lBuilder.setProperty("_sls", "yes");
                    UTTracker lTracker = UTAnalytics.getInstance().getDefaultTracker();
                    if (lTracker != null) {
                        lTracker.send(lBuilder.build());
                    } else {
                        Logger.e("Record crash stacktrace error", "Fatal Error,must call setRequestAuthentication method first.");
                    }
                }
                if (this.mDefaultHandler != null) {
                    this.mDefaultHandler.uncaughtException(pThread, pException);
                    return;
                }
                Process.killProcess(Process.myPid());
                System.exit(10);
            } else if (this.mDefaultHandler != null) {
                this.mDefaultHandler.uncaughtException(pThread, pException);
            } else {
                Process.killProcess(Process.myPid());
                System.exit(10);
            }
        } catch (Throwable t2) {
            try {
                t2.printStackTrace();
                if (uncaughtExceptionHandler == null) {
                    Process.killProcess(Process.myPid());
                    System.exit(10);
                }
            } finally {
                if (this.mDefaultHandler != null) {
                    this.mDefaultHandler.uncaughtException(pThread, pException);
                } else {
                    Process.killProcess(Process.myPid());
                    System.exit(10);
                }
            }
        }
    }
}
