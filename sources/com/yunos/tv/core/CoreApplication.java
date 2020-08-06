package com.yunos.tv.core;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;
import com.yunos.tv.blitz.account.LoginHelper;
import com.yunos.tv.blitz.global.BzApplication;
import com.yunos.tv.core.account.LoginHelperImpl;
import com.yunos.tv.core.aqm.MyLifecycleHandler;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.location.LocationAssist;
import com.yunos.tvtaobao.payment.PaymentApplication;
import com.zhiping.dev.android.logger.ZpLogger;

public class CoreApplication extends PaymentApplication {
    private static final String TAG = CoreApplication.class.getSimpleName();
    protected static CoreApplication mApplication = null;
    protected static long onCreateTime = 0;
    private MyLifecycleHandler myLifecycleHandler = new MyLifecycleHandler();

    public MyLifecycleHandler getMyLifecycleHandler() {
        return this.myLifecycleHandler;
    }

    public void onCreate() {
        if (Config.isDebug()) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDropBox().build());
        } else if (Build.VERSION.SDK_INT >= 24) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().penaltyLog().build());
        }
        registerActivityLifecycleCallbacks(this.myLifecycleHandler);
        super.onCreate();
        LocationAssist.getInstance().startLocation((LocationAssist.OnAMapLocationChange) null);
    }

    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        ZpLogger.w(TAG, "onTrimMemory: critical level: " + level);
        switch (level) {
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        ZpLogger.w(TAG, " onLowMemory called");
    }

    public void onTerminate() {
        super.onTerminate();
        clear();
    }

    public void clear() {
        User.clearUser();
        SharePreferences.destroy();
        RtEnv.destroy();
    }

    public static void toast(String s) {
        Toast toast = Toast.makeText(getApplication(), "", 0);
        toast.setText(s);
        toast.show();
    }

    public static LoginHelper getLoginHelper(Context context) {
        LoginHelper loginHelper = BzApplication.getLoginHelper(context);
        if (loginHelper != null) {
            return loginHelper;
        }
        LoginHelper loginHelper2 = LoginHelperImpl.getJuLoginHelper();
        setLoginHelper(loginHelper2);
        return loginHelper2;
    }

    public static CoreApplication getApplication() {
        return mApplication;
    }

    public static long getOnCreateTime() {
        return onCreateTime;
    }
}
