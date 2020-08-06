package com.yunos.tv.blitz.global;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.ut.mini.crashhandler.IUTCrashCaughtListner;
import com.yunos.tv.blitz.account.LoginHelper;
import com.yunos.tv.blitz.listener.BzAppGlobalListener;
import com.yunos.tv.blitz.listener.BzJsCallBaseListener;
import com.yunos.tv.blitz.listener.BzJsCallUIListener;
import com.yunos.tv.blitz.listener.BzMiscListener;
import com.yunos.tv.blitz.listener.BzMtopParamSetListner;
import com.yunos.tv.blitz.listener.BzPageStatusListener;
import com.yunos.tv.blitz.listener.internal.BzJsCallAccountListener;
import com.yunos.tv.blitz.listener.internal.BzJsCallNetListener;
import com.yunos.tv.blitz.service.BlitzServiceClient;
import com.yunos.tv.blitz.usertrack.BzUserTrackHandler;
import com.yunos.tv.blitz.utils.NetworkUtil;
import java.lang.ref.WeakReference;

public class BzApplication extends Application {
    private static LoginHelper sLoginHelper;
    BzAppMain mBzAppMain;

    public void onCreate() {
        super.onCreate();
        this.mBzAppMain = new BzAppMain(this);
        this.mBzAppMain.initBlitz();
        NetworkUtil.getInstance().init();
        BlitzServiceClient.getInstance(getApplicationContext()).init();
    }

    public static LoginHelper getLoginHelper(Context context) {
        return sLoginHelper;
    }

    public static void setLoginHelper(LoginHelper helper) {
        sLoginHelper = helper;
    }

    public void onTerminate() {
        super.onTerminate();
        NetworkUtil.getInstance().uninit();
    }

    public void initSecureGuard() {
        try {
            SecurityGuardManager.getInitializer().initialize(getApplicationContext());
        } catch (SecException e) {
            System.out.println("error code is " + e.getErrorCode());
        }
    }

    public void initBzParam(BzAppParams param) {
        this.mBzAppMain.initBzParam(param);
    }

    public void setEnvMode(BzEnvEnum mode) {
        this.mBzAppMain.setEnvMode(mode);
    }

    public void initMtop(int onlineIndex, int dailyIndex) {
        this.mBzAppMain.initMtopSdk(onlineIndex, dailyIndex);
    }

    public void initUT(String channel, boolean isDebug) {
        BzUserTrackHandler.initTBS(channel, this, isDebug);
    }

    public void initUT(String channel, boolean isDebug, String appKey, String appVersion, boolean autoPageTrack, IUTCrashCaughtListner iutCrashCaughtListner) {
        BzUserTrackHandler.initTBS(this, channel, isDebug, appKey, appVersion, autoPageTrack, iutCrashCaughtListner);
    }

    public void setBackgroundImgFromAssets(String path, int coreIndex) {
        this.mBzAppMain.setBackgroundImgFromAssets(path, coreIndex);
    }

    public void clearBackgroundImg(int coreIndex) {
        this.mBzAppMain.clearBackgroundImg(coreIndex);
    }

    public void setAppGlobalListener(BzAppGlobalListener listener) {
        this.mBzAppMain.setAppGlobalListener(listener);
    }

    public BzAppGlobalListener getAppGlobalListener() {
        return this.mBzAppMain.getAppGlobalListener();
    }

    public void setPageStatusListener(BzPageStatusListener listener) {
        this.mBzAppMain.setPageStatusListener(listener);
    }

    public BzPageStatusListener getPageStatusListener() {
        return this.mBzAppMain.getPageStatusListener();
    }

    public void setJsCallUIListener(BzJsCallUIListener listener) {
        this.mBzAppMain.setJsCallUIListener(listener);
    }

    public BzJsCallUIListener getJsCallUIListener() {
        return this.mBzAppMain.getJsCallUIListener();
    }

    public void setJsCallBaseListener(BzJsCallBaseListener listener) {
        this.mBzAppMain.setJsCallBaseListener(listener);
    }

    public BzJsCallBaseListener getJsCallBaseListener() {
        return this.mBzAppMain.getJsCallBaseListener();
    }

    public void setJsCallNetListener(BzJsCallNetListener listener) {
        this.mBzAppMain.setJsCallNetListener(listener);
    }

    public BzJsCallNetListener getJsCallNetListener() {
        return this.mBzAppMain.getJsCallNetListener();
    }

    public void setJsCallAccountListener(BzJsCallAccountListener listener) {
        this.mBzAppMain.setJsCallAccountListener(listener);
    }

    public BzJsCallAccountListener getJsCallAccountListener() {
        return this.mBzAppMain.getJsCallAccountListener();
    }

    public void setMiscListener(BzMiscListener listener) {
        this.mBzAppMain.setMiscListener(listener);
    }

    public BzMiscListener getMiscListener() {
        return this.mBzAppMain.getMiscListener();
    }

    public void setMtopParamListener(BzMtopParamSetListner listener) {
        this.mBzAppMain.setMtopParamListener(listener);
    }

    public BzMtopParamSetListner getMtopParamListener() {
        return this.mBzAppMain.getMtopParamListener();
    }

    public boolean replyCallBack(int callback, boolean success, String result) {
        return this.mBzAppMain.replyCallBack(callback, success, result);
    }

    public WeakReference<Activity> getCurrentActivity() {
        return this.mBzAppMain.getCurrentActivity();
    }

    public int getCurrentActivityCount() {
        return this.mBzAppMain.getCurrentActivityCount();
    }

    public void setCurrentDialog(Dialog dialog) {
        this.mBzAppMain.setCurrentDialog(dialog);
    }

    public WeakReference<Dialog> getCurrentDialog() {
        return this.mBzAppMain.getCurrentDialog();
    }

    public boolean getUCacheEnable() {
        return this.mBzAppMain.getUCacheEnable();
    }
}
