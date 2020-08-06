package com.yunos.tvtaobao.biz.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import android.text.format.DateUtils;
import anetwork.channel.util.RequestConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.RtEnv;
import com.yunos.tv.core.aqm.ActivityQueueManager;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.RunMode;
import com.yunos.tv.core.config.UpdateStatus;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.activity.NotForceUpdateActivity;
import com.yunos.tvtaobao.biz.activity.UpdateActivity;
import com.yunos.tvtaobao.biz.dialog.UpdateDialog;
import com.yunos.tvtaobao.biz.model.AppInfo;
import com.yunos.tvtaobao.biz.net.network.NetworkManager;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.service.UpdateService;
import com.yunos.tvtaobao.biz.util.CheckAPK;
import com.yunos.tvtaobao.biz.util.StringUtil;
import com.yunos.tvtaobao.biz.util.UserTrackUtil;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Update {
    private static final long FORCE_INSTALL_DELAY_TIME = 0;
    private static final int MTOP_UNLOAD_FINISH = -1;
    private static final String TAG = "Update";
    private static final long UNFORCE_INSTALL_DELAY_TIME = 1;
    /* access modifiers changed from: private */
    public static MyHandler mMyHandler;
    /* access modifiers changed from: private */
    public static Handler mServiceHandler;
    private static Map<String, Update> sUpdateMap = new HashMap();
    private boolean isDirectShowUpdate = false;
    private String mAppCode;
    /* access modifiers changed from: private */
    public AppInfo mAppInfo;
    private Bundle mBundle;
    private AsyncTask<Void, Void, Boolean> mCheckApkTask;
    private AsyncTask<Void, Void, Boolean> mCheckNetworkTask;
    /* access modifiers changed from: private */
    public UpdateService mContext;
    /* access modifiers changed from: private */
    public int mDetectConnectionRetryTimes = 2;
    private DownloadProgressListner mDownloadProgressListner;
    /* access modifiers changed from: private */
    public int mDownloadRetryTimes = 2;
    private Thread mDownloadThread;
    /* access modifiers changed from: private */
    public ABDownloader mDownloader;
    /* access modifiers changed from: private */
    public int mExceptionRetryTimes = 2;
    /* access modifiers changed from: private */
    public boolean mIsForced = false;
    /* access modifiers changed from: private */
    public boolean mIsNeedRequest = false;
    private boolean mIsStartActivity = false;
    /* access modifiers changed from: private */
    public boolean mIsStop = false;
    /* access modifiers changed from: private */
    public int mMTopRetryTimes = 2;
    private String mMtopApi;
    private String mMtopApiInfo;
    private NetworkManager.INetworkListener mNetworkListner = new NetworkManager.INetworkListener() {
        public void onNetworkChanged(boolean isConnected, boolean lastIsConnected) {
            ZpLogger.d(Update.TAG, "onNetworkChanged, isConnected: " + isConnected + " isNeedRequest: " + Update.this.mIsNeedRequest);
            Activity activity = ActivityQueueManager.getTop();
            if (activity != null && (UpdateActivity.class.getSimpleName().equals(activity.getClass().getSimpleName()) || NotForceUpdateActivity.class.getSimpleName().equals(activity.getClass().getSimpleName()))) {
                ZpLogger.i(Update.TAG, "Update.INetworkListener TopActivity=" + activity);
            } else if (isConnected && Update.this.mIsNeedRequest) {
                boolean unused = Update.this.mIsNeedRequest = false;
                Update.this.onResumeDownload();
                Update.this.retry();
            }
        }
    };
    /* access modifiers changed from: private */
    public String mReleaseNote;
    /* access modifiers changed from: private */
    public int mRequestStatus = -1;
    /* access modifiers changed from: private */
    public String mTargetFile;
    /* access modifiers changed from: private */
    public String mTargetMd5;
    /* access modifiers changed from: private */
    public long mTargetSize;
    private AsyncTask<Void, Void, Boolean> mTopAppCheckTask;
    private String mUpdateParams;
    /* access modifiers changed from: private */
    public String mVersionCode;
    private UpdateDialog updateDialog;

    public interface DownloadProgressListner {
        void onChangeDownloadType(int i);

        void onError(int i);

        void onFildValid();

        void onFileExists();

        void onInstall();

        void onResumeDownload();

        void onRetryDownload(int i);

        void onUpdateProgress(int i);
    }

    static /* synthetic */ int access$1610(Update x0) {
        int i = x0.mDownloadRetryTimes;
        x0.mDownloadRetryTimes = i - 1;
        return i;
    }

    static /* synthetic */ int access$1710(Update x0) {
        int i = x0.mExceptionRetryTimes;
        x0.mExceptionRetryTimes = i - 1;
        return i;
    }

    static /* synthetic */ int access$2610(Update x0) {
        int i = x0.mDetectConnectionRetryTimes;
        x0.mDetectConnectionRetryTimes = i - 1;
        return i;
    }

    static /* synthetic */ int access$510(Update x0) {
        int i = x0.mMTopRetryTimes;
        x0.mMTopRetryTimes = i - 1;
        return i;
    }

    private static class LogReceiveListener implements RequestListener<String> {
        public void onRequestDone(String data, int resultCode, String handleMessagemsg) {
            ZpLogger.d(Update.TAG, "onRequestDone " + data);
        }
    }

    private static class UpgradeAppListener implements RequestListener<String> {
        private Context mContext;
        private WeakReference<MyHandler> mMyHandlerWeakReference;

        public UpgradeAppListener(WeakReference<MyHandler> handler, Context context) {
            this.mMyHandlerWeakReference = handler;
            this.mContext = context;
        }

        public void onRequestDone(String data, int resultCode, String handleMessagemsg) {
            ZpLogger.d(Update.TAG, "onRequestDone " + data);
            if (this.mMyHandlerWeakReference != null && this.mMyHandlerWeakReference.get() != null) {
                MyHandler myHandler = (MyHandler) this.mMyHandlerWeakReference.get();
                if (resultCode != 200 || data == null) {
                    myHandler.sendEmptyMessage(1001);
                    return;
                }
                if (Utils.ExistSDCard()) {
                    boolean isMoHeOn = SharePreferences.getBoolean(UpdatePreference.IS_MOHE_LOG_ON, false).booleanValue();
                    boolean isLianMengLogOn = SharePreferences.getBoolean(UpdatePreference.IS_LIANMNEG_LOG_ON, false).booleanValue();
                    boolean isYiTiJiLogOn = SharePreferences.getBoolean(UpdatePreference.IS_YITIJI_LOG_ON, false).booleanValue();
                    if (Config.MOHE.equals(Config.getChannel()) && isMoHeOn) {
                        LogUtils.getInstance(this.mContext).start();
                    } else if (Config.LIANMENG.equals(Config.getChannel()) && isLianMengLogOn) {
                        LogUtils.getInstance(this.mContext).start();
                    } else if (Config.YITIJI.equals(Config.getChannel()) && isYiTiJiLogOn) {
                        LogUtils.getInstance(this.mContext).start();
                    }
                }
                AppInfo appInfo = new AppInfo(JSON.parseObject(data));
                ZpLogger.d(Update.TAG, "onRequestDone " + appInfo.toString());
                Message handleMessage = new Message();
                handleMessage.what = 1000;
                handleMessage.obj = appInfo;
                myHandler.sendMessage(handleMessage);
                SharedPreferences.Editor e = this.mContext.getSharedPreferences("updateInfo", 0).edit();
                e.putString(UpdatePreference.UPDATE_TIPS, StringUtil.isEmpty(appInfo.getReleaseNote()) ? "" : appInfo.getReleaseNote());
                e.putString(UpdatePreference.IMAGE1, StringUtil.isEmpty(appInfo.getImage1()) ? "" : appInfo.getImage1());
                e.putString(UpdatePreference.IMAGE2, StringUtil.isEmpty(appInfo.getImage2()) ? "" : appInfo.getImage2());
                e.putString(UpdatePreference.IMAGE3, StringUtil.isEmpty(appInfo.getImage3()) ? "" : appInfo.getImage3());
                e.putString(UpdatePreference.IMAGE4, StringUtil.isEmpty(appInfo.getImage4()) ? "" : appInfo.getImage4());
                e.putString(UpdatePreference.IMAGE5, StringUtil.isEmpty(appInfo.getImage5()) ? "" : appInfo.getImage5());
                e.putString(UpdatePreference.IMAGE6, StringUtil.isEmpty(appInfo.getImage6()) ? "" : appInfo.getImage6());
                e.putString(UpdatePreference.NEW_RELEASE_NOTE, StringUtil.isEmpty(appInfo.getNewReleaseNote()) ? "" : appInfo.getNewReleaseNote());
                e.putString(UpdatePreference.UPGRADE_MODE, StringUtil.isEmpty(appInfo.getUpgradeMode()) ? "" : appInfo.getUpgradeMode());
                e.putString(UpdatePreference.COLOR, StringUtil.isEmpty(appInfo.getColor()) ? "" : appInfo.getColor());
                e.putString(UpdatePreference.LATER_ON, StringUtil.isEmpty(appInfo.getLaterOn()) ? "" : appInfo.getLaterOn());
                e.putString(UpdatePreference.UPGRADE_NOW, StringUtil.isEmpty(appInfo.getUpgradeNow()) ? "" : appInfo.getUpgradeNow());
                e.putString("versionName", StringUtil.isEmpty(appInfo.versionName) ? "" : appInfo.versionName);
                e.commit();
            }
        }
    }

    private static class MyHandler extends Handler {
        private WeakReference<Update> mOuter;

        public MyHandler(Update up) {
            this.mOuter = new WeakReference<>(up);
        }

        public void handleMessage(Message msg) {
            Update up = (Update) this.mOuter.get();
            if (up != null) {
                switch (msg.what) {
                    case 0:
                        ZpLogger.d(Update.TAG, "Update.handleMessage.ERROR_TYPE_NETWORK_DISCONNECT");
                        UserTrackUtil.onErrorEvent(3);
                        up.onError(0);
                        return;
                    case 1000:
                        ZpLogger.i(Update.TAG, "Update.handleMessage.MTOP_DONE");
                        AppInfo unused = up.mAppInfo = (AppInfo) msg.obj;
                        if (up.mAppInfo.type == 0) {
                            up.processUpdateInfo();
                        } else if (up.mAppInfo.type == 1) {
                            up.processTpatchUpdateInfo();
                        }
                        if (!up.mAppInfo.isForced) {
                            SharedPreferences.Editor editor = up.mContext.getSharedPreferences("updateInfo", 0).edit();
                            editor.putString(UpdatePreference.SP_KEY_RELEASE_NOTE, up.mAppInfo.releaseNote);
                            editor.apply();
                        }
                        int unused2 = up.mRequestStatus = 1000;
                        return;
                    case 1001:
                        ZpLogger.e(Update.TAG, "Update.handleMessage.fail to get info from mtop, return null, try " + up.mMTopRetryTimes + " more times");
                        UserTrackUtil.onErrorEvent(4);
                        if (up.mMTopRetryTimes != 0) {
                            Update.access$510(up);
                            up.onRetryDownload();
                            up.start();
                        } else {
                            UserTrackUtil.onErrorEvent(1);
                            if (up.mIsForced) {
                                boolean unused3 = up.mIsNeedRequest = true;
                                up.onError(1);
                            } else {
                                up.sendTerminatedMessage();
                            }
                        }
                        int unused4 = up.mRequestStatus = 1001;
                        return;
                    case 1002:
                        ZpLogger.e(Update.TAG, "Update.handleMessage.fail to delete old file for some reason, try " + up.mExceptionRetryTimes + " more times,EXCEPTION");
                        LogUtils.getInstance(up.mContext).logReceive(up.mContext);
                        UserTrackUtil.onErrorEvent(3);
                        if (up.mExceptionRetryTimes != 0) {
                            Update.access$1710(up);
                            up.onRetryDownload();
                            up.start();
                            return;
                        } else if (up.mIsForced) {
                            up.onError(3);
                            return;
                        } else {
                            up.sendTerminatedMessage();
                            return;
                        }
                    case 1003:
                        ZpLogger.d(Update.TAG, "Update.handleMessage.new apk exist, wait to be checked");
                        up.onFileExists();
                        return;
                    case 1004:
                        ZpLogger.d(Update.TAG, "Update.handleMessage.finish downloading, file size: " + msg.obj + ".mIsForced = " + up.mIsForced);
                        UserTrackUtil.onCustomEvent(UpdatePreference.UT_DOWNLOAD_SUCCESS);
                        up.checkApk();
                        return;
                    case 1005:
                        ZpLogger.e(Update.TAG, "Update.handleMessage.invalid new apk, need to download again, try " + up.mDownloadRetryTimes + " more times" + ". mIsForced = " + up.mIsForced);
                        UserTrackUtil.onErrorEvent(2);
                        if (up.mDownloadRetryTimes != 0) {
                            Update.access$1610(up);
                            up.onRetryDownload();
                            up.start();
                            return;
                        } else if (up.mIsForced) {
                            up.onError(2);
                            return;
                        } else {
                            up.sendTerminatedMessage();
                            return;
                        }
                    case 1006:
                        ZpLogger.e(Update.TAG, "Update.handleMessage.download timeout, restart update progress, try " + up.mDownloadRetryTimes + " more times, DOWNLOAD_TIMEOUT");
                        LogUtils.getInstance(up.mContext).logReceive(up.mContext);
                        UserTrackUtil.onErrorEvent(5);
                        if (up.mDownloadRetryTimes != 0) {
                            Update.access$1610(up);
                            up.onRetryDownload();
                            up.start();
                            return;
                        }
                        UserTrackUtil.onErrorEvent(1);
                        if (up.mIsForced) {
                            boolean unused5 = up.mIsNeedRequest = true;
                            up.onError(1);
                            return;
                        }
                        up.sendTerminatedMessage();
                        return;
                    case 1007:
                        ZpLogger.d(Update.TAG, "Update.handleMessage.download interrupt, DOWNLOAD_INTERRUPT");
                        LogUtils.getInstance(up.mContext).logReceive(up.mContext);
                        return;
                    case 1008:
                        ZpLogger.d(Update.TAG, "Update.handleMessage.update terminated, UPDATE_TERMINATED");
                        up.sendTerminatedMessage();
                        return;
                    case 1010:
                        ZpLogger.d(Update.TAG, "Update.handleMessage.update download msg.arg1=" + msg.arg1);
                        up.onUpdateProgress(msg.arg1);
                        return;
                    case 1011:
                        ZpLogger.d(Update.TAG, "Update.handleMessage.integrated file NEW_APK_VALID");
                        up.onFileValid();
                        up.checkTopApp();
                        return;
                    case 2000:
                    case 2011:
                        Updater.update(Update.mServiceHandler, up.mContext, up.mAppInfo);
                        return;
                    case 2001:
                        ZpLogger.e(Update.TAG, "Update.handleMessage.invalid new apk, need to download again, try " + up.mDownloadRetryTimes + " more times" + ". mIsForced = " + up.mIsForced);
                        up.sendTerminatedMessage();
                        return;
                    case 2002:
                        ZpLogger.d(Update.TAG, "Update.handleMessage.DOWNLOAD_TPATCH_INTERRUPT");
                        LogUtils.getInstance(up.mContext).logReceive(up.mContext);
                        return;
                    case 2003:
                        ZpLogger.d(Update.TAG, "Update.handleMessage.DOWNLOAD_TPATCH_TIMEOUT");
                        LogUtils.getInstance(up.mContext).logReceive(up.mContext);
                        up.sendTerminatedMessage();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public Update(UpdateService context, Handler serviceHandler, String paramsJson) {
        ZpLogger.d(TAG, "Update.Update paramsJson = " + paramsJson);
        this.mContext = context;
        mServiceHandler = serviceHandler;
        this.mUpdateParams = paramsJson;
        mMyHandler = new MyHandler(this);
        init();
    }

    private void init() {
        JSONObject jsonParams = JSON.parseObject(this.mUpdateParams);
        this.mAppCode = jsonParams.getString("code");
        this.isDirectShowUpdate = jsonParams.getBoolean("isDirectShowUpdate").booleanValue();
        jsonParams.put("code", (Object) "tvtaobao");
        jsonParams.put("version", (Object) Build.VERSION.RELEASE);
        jsonParams.put("systemInfo", (Object) Build.MODEL + WVNativeCallbackUtil.SEPERATER + Build.VERSION.SDK);
        JSONObject json = new JSONObject();
        if (Config.getRunMode() == RunMode.DAILY) {
            json.put("uuid", (Object) "762A775537A9C517964028B557C52D64");
            json.put("server", (Object) RequestConstant.ENV_TEST);
        } else if (Config.getRunMode() == RunMode.PREDEPLOY) {
            json.put("server", (Object) "prerelease");
        } else {
            json.put("server", (Object) RequestConstant.ENV_ONLINE);
        }
        json.put("v", (Object) "1.0");
        this.mMtopApiInfo = json.toString();
        if (jsonParams != null) {
            this.mUpdateParams = jsonParams.toString();
        }
        this.mMtopApi = UpdatePreference.API;
        ZpLogger.d(TAG, "Update.full request paramsJson: " + this.mUpdateParams + ", mMtopApiInfo = " + this.mMtopApiInfo + ", mMtopApi = " + this.mMtopApi);
    }

    public static void add(String code, Update up) {
        if (sUpdateMap == null) {
            sUpdateMap = new HashMap();
        }
        ZpLogger.i(TAG, "Update.add.code = " + code);
        sUpdateMap.put(code, up);
    }

    public static Update get(String code) {
        if (sUpdateMap == null) {
            return null;
        }
        ZpLogger.i(TAG, "Update.get.code = " + code);
        return sUpdateMap.get(code);
    }

    public static void remove(String code) {
        if (sUpdateMap != null) {
            ZpLogger.i(TAG, "Update.remove.code = " + code);
            sUpdateMap.remove(code);
        }
    }

    public void start() {
        ZpLogger.d(TAG, "Update.start.update start");
        this.mIsNeedRequest = false;
        registerNetworkListner();
        checkNetwork();
    }

    public void stop() {
        ZpLogger.d(TAG, "Update.stop.update stop");
        this.mIsStop = true;
        releaseNetworkListner();
        if (this.mCheckApkTask != null) {
            this.mCheckNetworkTask.cancel(true);
        }
        if (this.mTopAppCheckTask != null) {
            this.mTopAppCheckTask.cancel(true);
        }
        if (this.mDownloadThread != null) {
            this.mDownloadThread.interrupt();
        }
    }

    /* access modifiers changed from: private */
    public void retry() {
        ZpLogger.d(TAG, "Update.retry, init retry times");
        this.mIsNeedRequest = false;
        this.mMTopRetryTimes = 2;
        this.mDownloadRetryTimes = 2;
        this.mExceptionRetryTimes = 2;
        this.mDetectConnectionRetryTimes = 2;
        start();
    }

    public Thread getDownloaderThread() {
        return this.mDownloadThread;
    }

    public boolean checkUpdateInfo() {
        ZpLogger.i(TAG, "Update.checkUpdateInfo");
        while (this.mRequestStatus == -1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.mAppInfo == null || !this.mAppInfo.isSuccess) {
            ZpLogger.i(TAG, "Update.checkUpdateInfo (application don't need update!)");
            return false;
        }
        if (!checkEmpty(this.mAppInfo.downloadUrl, this.mAppInfo.downloadMd5, this.mAppInfo.version, this.mAppInfo.size, this.mAppInfo.apkName) && this.mAppInfo.apkName.contains(".apk")) {
            return true;
        }
        ZpLogger.i(TAG, "Update.checkUpdateInfo " + this.mAppInfo.toString());
        return false;
    }

    public static void syncCheckUpdateAndShowPage() {
        try {
            new Thread(new Runnable() {
                public void run() {
                    Update update = Update.get("tvtaobao");
                    while (update == null) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (update.isDirectShowUpdate() && update.checkUpdateInfo()) {
                        update.startUpdateActivity();
                    }
                }
            }).start();
        } catch (Exception e) {
            ZpLogger.e(TAG, e.getMessage());
        }
    }

    public boolean isDirectShowUpdate() {
        return this.isDirectShowUpdate;
    }

    public boolean isStop() {
        return this.mIsStop;
    }

    public Bundle getBundle() {
        return this.mBundle;
    }

    public boolean getIsStartActivity() {
        return this.mIsStartActivity;
    }

    public void setIsStartActivity(boolean isStart) {
        ZpLogger.d(TAG, "Update.setIsStartActivity: " + isStart);
        this.mIsStartActivity = isStart;
    }

    public void setOnDownloadProgressListner(DownloadProgressListner l) {
        this.mDownloadProgressListner = l;
    }

    /* access modifiers changed from: private */
    public void onRetryDownload() {
        int progress;
        if (this.mDownloadProgressListner != null && !TextUtils.isEmpty(this.mTargetFile)) {
            File file = new File(this.mTargetFile);
            if (file == null || !file.exists() || !file.isFile() || file.length() == 0) {
                progress = 0;
            } else {
                progress = (int) ((file.length() * 100) / this.mTargetSize);
            }
            ZpLogger.d(TAG, "Update.onRetryDownload, process = " + progress);
            this.mDownloadProgressListner.onRetryDownload(progress);
        }
    }

    /* access modifiers changed from: private */
    public void onUpdateProgress(int progress) {
        if (this.mDownloadProgressListner != null) {
            this.mDownloadProgressListner.onUpdateProgress(progress);
        }
    }

    /* access modifiers changed from: private */
    public void onFileExists() {
        if (this.mDownloadProgressListner != null) {
            this.mDownloadProgressListner.onFileExists();
        }
    }

    /* access modifiers changed from: private */
    public void onFileValid() {
        if (this.mDownloadProgressListner != null) {
            this.mDownloadProgressListner.onFildValid();
        }
    }

    private void onInstall() {
        if (this.mDownloadProgressListner != null) {
            this.mDownloadProgressListner.onInstall();
        }
    }

    /* access modifiers changed from: private */
    public void onError(int errorType) {
        if (this.mDownloadProgressListner != null) {
            this.mDownloadProgressListner.onError(errorType);
        }
    }

    /* access modifiers changed from: private */
    public void onResumeDownload() {
        if (this.mDownloadProgressListner != null) {
            this.mDownloadProgressListner.onResumeDownload();
        }
    }

    private void onChangeDownloadType(int type) {
        if (this.mDownloadProgressListner != null) {
            this.mDownloadProgressListner.onChangeDownloadType(type);
        }
    }

    /* access modifiers changed from: private */
    public void getUpdateInfo() {
        if (mMyHandler == null || this.mContext == null || this.mMtopApi == null || this.mMtopApiInfo == null || this.mUpdateParams == null) {
            ZpLogger.e(TAG, "Update.getUpdateInfoparams error, at least one param is null");
            if (mMyHandler != null) {
                mMyHandler.sendMessage(mMyHandler.obtainMessage(1008));
                return;
            }
            return;
        }
        ZpLogger.v(TAG, "Update.getUpdateInfo start AccessMTop.get");
        JSONObject json = JSON.parseObject(this.mUpdateParams);
        String code = json.getString("code");
        String versionCode = json.getString("versionCode");
        String versionName = json.getString("versionName");
        String uuid = json.getString("uuid");
        String channelId = json.getString("channelId");
        String systemInfo = json.getString("systemInfo");
        String version = json.getString("version");
        SharedPreferences.Editor e = this.mContext.getSharedPreferences("updateInfo", 0).edit();
        e.putString(UpdatePreference.UPDATE_OBJECT, StringUtil.isEmpty(this.mUpdateParams) ? "" : this.mUpdateParams);
        e.commit();
        try {
            String umtoken = Config.getUmtoken(this.mContext);
            org.json.JSONObject object = new org.json.JSONObject();
            object.put("umToken", Config.getUmtoken(this.mContext));
            object.put("wua", Config.getWua(this.mContext));
            object.put("isSimulator", Config.isSimulator(this.mContext));
            object.put("userAgent", Config.getAndroidSystem(this.mContext));
            BusinessRequest.getBusinessRequest().requestUpGrade(version, uuid, channelId, code, versionCode, versionName, systemInfo, umtoken, Config.getModelInfo(this.mContext), object.toString(), new UpgradeAppListener(new WeakReference(mMyHandler), this.mContext));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void processTpatchUpdateInfo() {
        ZpLogger.v(TAG, "Update.processTpatchUpdateInfo.mAppInfo = " + this.mAppInfo);
        if (this.mAppInfo == null) {
            ZpLogger.e(TAG, "Update.processUpdateInfo appInfo is null");
            if (mMyHandler != null) {
                mMyHandler.sendMessage(mMyHandler.obtainMessage(1008));
                onError(6);
                UserTrackUtil.onErrorEvent(7);
            }
        } else if (!this.mAppInfo.isSuccess) {
            ZpLogger.e(TAG, "Update.processUpdateInfo.fail reason from server: " + this.mAppInfo.returnText);
            if (mMyHandler != null) {
                mMyHandler.sendMessage(mMyHandler.obtainMessage(1008));
                onError(6);
                UserTrackUtil.onErrorEvent(7);
            }
        } else if (!this.mAppInfo.isLatest) {
            if (checkEmpty(this.mAppInfo.downloadUrl, this.mAppInfo.downloadMd5, this.mAppInfo.version, this.mAppInfo.size) || !this.mAppInfo.apkName.contains(".tpatch")) {
                ZpLogger.e(TAG, "Update.processUpdateInfo.appInfo params error(from MTOP)");
                if (mMyHandler != null) {
                    mMyHandler.sendMessage(mMyHandler.obtainMessage(1008));
                    onError(2);
                    return;
                }
                return;
            }
            if (Utils.ExistSDCard()) {
                this.mTargetFile = this.mContext.getExternalCacheDir() + File.separator + "patch-" + this.mAppInfo.mUpdateInfo.updateVersion + Constant.NLP_CACHE_TYPE + this.mAppInfo.mUpdateInfo.baseVersion + ".tpatch";
            } else {
                File filesFolder = this.mContext.getFilesDir();
                if (filesFolder == null) {
                    ZpLogger.e(TAG, "Update.processUpdateInfo.files folder does not exist");
                    sendTerminatedMessage();
                    releaseNetworkListner();
                    onError(3);
                    UserTrackUtil.onErrorEvent(3);
                    return;
                }
                this.mTargetFile = filesFolder.getPath() + File.separator + this.mAppInfo.apkName;
            }
            this.mTargetMd5 = this.mAppInfo.downloadMd5;
            this.mIsForced = this.mAppInfo.isForced;
            this.mReleaseNote = this.mAppInfo.releaseNote;
            this.mVersionCode = this.mAppInfo.version;
            try {
                this.mTargetSize = Long.parseLong(this.mAppInfo.size);
            } catch (NumberFormatException e) {
                ZpLogger.e(TAG, "Update.processUpdateInfo.size parse error: " + e.getLocalizedMessage());
            }
            putBundle();
            UserTrackUtil.setNewVersionCode(this.mVersionCode);
            UserTrackUtil.setIsForcedInstall(this.mIsForced);
            ZpLogger.v(TAG, "Update.processUpdateInfo.mIsStartActivity = " + this.mIsStartActivity + ". mIsForced = " + this.mIsForced);
            startDownload(1);
        }
    }

    /* access modifiers changed from: private */
    public void processUpdateInfo() {
        ZpLogger.v(TAG, "Update.processUpdateInfo.mAppInfo = " + this.mAppInfo);
        if (this.mAppInfo == null) {
            ZpLogger.e(TAG, "Update.processUpdateInfo appInfo is null");
            if (mMyHandler != null) {
                mMyHandler.sendMessage(mMyHandler.obtainMessage(1008));
                onError(2);
                UserTrackUtil.onErrorEvent(4);
            }
        } else if (!this.mAppInfo.isSuccess) {
            ZpLogger.e(TAG, "Update.processUpdateInfo.fail reason from server: " + this.mAppInfo.returnText);
            if (mMyHandler != null) {
                mMyHandler.sendMessage(mMyHandler.obtainMessage(1008));
                onError(2);
                UserTrackUtil.onErrorEvent(4);
            }
        } else if (this.mAppInfo.isLatest) {
            ZpLogger.d(TAG, "Update.processUpdateInfo.current version is latest, no new version to update");
            File oldFile = new File(this.mContext.getSharedPreferences("updateInfo", 0).getString(UpdatePreference.SP_KEY_PATH, ""));
            if (oldFile != null && oldFile.exists()) {
                UserTrackUtil.onCustomEvent(UpdatePreference.UT_INSTALL_SUCCESS);
                ZpLogger.d(TAG, "Update.processUpdateInfo.delete old update file: " + oldFile.delete());
            }
            sendTerminatedMessage();
            releaseNetworkListner();
            onChangeDownloadType(0);
        } else {
            if (checkEmpty(this.mAppInfo.downloadUrl, this.mAppInfo.downloadMd5, this.mAppInfo.version, this.mAppInfo.size, this.mAppInfo.apkName) || !this.mAppInfo.apkName.contains(".apk")) {
                ZpLogger.e(TAG, "Update.processUpdateInfo.appInfo params error(from MTOP)");
                if (mMyHandler != null) {
                    mMyHandler.sendMessage(mMyHandler.obtainMessage(1008));
                    onError(2);
                    return;
                }
                return;
            }
            if (!Utils.ExistSDCard() || this.mContext.getExternalCacheDir() == null) {
                File filesFolder = this.mContext.getFilesDir();
                if (filesFolder == null) {
                    ZpLogger.e(TAG, "Update.processUpdateInfo.files folder does not exist");
                    sendTerminatedMessage();
                    releaseNetworkListner();
                    onError(3);
                    UserTrackUtil.onErrorEvent(3);
                    return;
                }
                this.mTargetFile = filesFolder.getPath() + File.separator + this.mAppInfo.apkName;
            } else {
                this.mTargetFile = this.mContext.getExternalCacheDir() + File.separator + this.mAppInfo.apkName;
            }
            this.mTargetMd5 = this.mAppInfo.downloadMd5;
            this.mIsForced = this.mAppInfo.isForced;
            this.mReleaseNote = this.mAppInfo.releaseNote;
            this.mVersionCode = this.mAppInfo.version;
            try {
                this.mTargetSize = Long.parseLong(this.mAppInfo.size);
            } catch (NumberFormatException e) {
                ZpLogger.e(TAG, "Update.processUpdateInfo.size parse error: " + e.getLocalizedMessage());
            }
            putBundle();
            UserTrackUtil.setNewVersionCode(this.mVersionCode);
            UserTrackUtil.setIsForcedInstall(this.mIsForced);
            ZpLogger.v(TAG, "Update.processUpdateInfo.mIsStartActivity = " + this.mIsStartActivity + ". mIsForced = " + this.mIsForced);
            onChangeDownloadType(1);
            startDownload(1);
        }
    }

    public void startDownload(final long sleepTime) {
        if (this.mAppInfo == null || this.mContext == null) {
            ZpLogger.e(TAG, "Update.startDownload.appinfo or context is null, cannot call this method directly");
        } else if (this.mDownloadThread == null || !this.mDownloadThread.isAlive()) {
            this.mDownloadThread = new Thread() {
                public void run() {
                    ZpLogger.d(Update.TAG, "Update.startDownload.mDownloadRetryTimes: " + Update.this.mDownloadRetryTimes);
                    String url = Update.this.mAppInfo.downloadUrl;
                    if (Update.this.mDownloadRetryTimes <= 0) {
                        ZpLogger.d(Update.TAG, "Update.startDownload.change cdn url to oss url");
                        url = Update.this.mAppInfo.ossDownloadUrl;
                    }
                    ZpLogger.d(Update.TAG, "Update.startDownload.apk name: " + Update.this.mAppInfo.apkName + ".url = " + url + ", mTargetFile = " + Update.this.mTargetFile + ",mTargetMd5 = " + Update.this.mTargetMd5 + ", mVersionCode = " + Update.this.mVersionCode + ", mTargetSize = " + Update.this.mTargetSize + ", sleepTime = " + sleepTime);
                    if (Update.this.mAppInfo.type == 0) {
                        ABDownloader unused = Update.this.mDownloader = new ApkDownloader(url, Update.this.mTargetFile, Update.this.mTargetMd5, Update.this.mVersionCode, Update.this.mReleaseNote, Update.this.mTargetSize, sleepTime, Update.this.mContext, Update.mMyHandler);
                    } else if (Update.this.mAppInfo.type == 1) {
                        ABDownloader unused2 = Update.this.mDownloader = new TpatchDownLoader(url, Update.this.mTargetFile, Update.this.mTargetMd5, Update.this.mAppInfo.versionName, Update.this.mTargetSize, sleepTime, Update.this.mContext, Update.mMyHandler);
                    }
                    try {
                        Update.this.mDownloader.download();
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (Update.mMyHandler != null) {
                            Update.mMyHandler.sendMessage(Update.mMyHandler.obtainMessage(1002));
                        }
                    }
                }
            };
            this.mDownloadThread.setPriority(3);
            if (!this.mIsStop) {
                this.mDownloadThread.start();
            }
        } else {
            ZpLogger.w(TAG, "Update.startDownload.alive download thread exists, will not create a new thread");
        }
    }

    public void setDownloadDelayTime(int sleepTime) {
        if (this.mDownloader != null) {
            this.mDownloader.setDownloadDelayTime(sleepTime);
        }
    }

    private void checkNetwork() {
        this.mCheckNetworkTask = new AsyncTask<Void, Void, Boolean>() {
            /* access modifiers changed from: protected */
            public Boolean doInBackground(Void... params) {
                while (!NetworkManager.instance().isNetworkConnected()) {
                    if (isCancelled()) {
                        ZpLogger.d(Update.TAG, "Update.checkNetwork.stop check network");
                        return false;
                    } else if (Update.this.mDetectConnectionRetryTimes != 0) {
                        ZpLogger.e(Update.TAG, "Update.checkNetwork.network disconnected, check " + Update.this.mDetectConnectionRetryTimes + " more times 1s later");
                        Update.access$2610(Update.this);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            if (isCancelled()) {
                                ZpLogger.d(Update.TAG, "Update.checkNetwork.stop check network(interrupt)");
                                return false;
                            }
                        }
                    } else {
                        boolean unused = Update.this.mIsNeedRequest = true;
                        return false;
                    }
                }
                return true;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Boolean result) {
                ZpLogger.v(Update.TAG, "Update.checkNetwork.onPostExecute.result = " + result + ".mIsStop = " + Update.this.mIsStop);
                if (!Update.this.mIsStop) {
                    if (result.booleanValue()) {
                        if (Boolean.TRUE.equals(RtEnv.get(RtEnv.KEY_SHOULD_CHECK_UPDATE, true))) {
                            Update.this.getUpdateInfo();
                            RtEnv.set(RtEnv.KEY_SHOULD_CHECK_UPDATE, false);
                        }
                    } else if (Update.mMyHandler != null) {
                        Update.mMyHandler.sendMessage(Update.mMyHandler.obtainMessage(0));
                    }
                }
            }
        };
        ZpLogger.v(TAG, "Update.checkNetwork.mIsStop = " + this.mIsStop);
        if (!this.mIsStop) {
            this.mCheckNetworkTask.execute(new Void[0]);
        }
    }

    /* access modifiers changed from: private */
    public void checkTopApp() {
        this.mTopAppCheckTask = new AsyncTask<Void, Void, Boolean>() {
            /* access modifiers changed from: protected */
            public Boolean doInBackground(Void... params) {
                while (!CoreApplication.getApplication().getMyLifecycleHandler().isApplicationInForeground()) {
                    if (isCancelled()) {
                        ZpLogger.d(Update.TAG, "Update.checkTopApp.stop check the current app, termiante update process");
                        return false;
                    }
                    ZpLogger.d(Update.TAG, "Update.checkTopApp.current app is not " + Update.this.mContext.getPackageName() + " retry checking in 5s");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        if (isCancelled()) {
                            ZpLogger.d(Update.TAG, "Update.checkTopApp.stop check the current app(interrupt), termiante update process");
                            return false;
                        }
                    }
                }
                return true;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Boolean result) {
                ZpLogger.d(Update.TAG, "Update.checkTopApp. onPostExecute result = " + result);
                if (result.booleanValue()) {
                    ZpLogger.d(Update.TAG, "Update.checkTopApp.current app is: " + Update.this.mContext.getPackageName() + ", start update activity");
                    Update.this.startUpdateActivity();
                } else if (Update.mMyHandler != null) {
                    Update.mMyHandler.sendMessage(Update.mMyHandler.obtainMessage(1008));
                }
            }

            /* access modifiers changed from: protected */
            public void onCancelled(Boolean result) {
                ZpLogger.d(Update.TAG, "Update.checkTopApp.onCancelled");
                onPostExecute(result);
            }
        };
        ZpLogger.v(TAG, "Update.checkTopApp.mIsStop = " + this.mIsStop);
        if (!this.mIsStop) {
            this.mTopAppCheckTask.execute(new Void[0]);
        }
    }

    /* access modifiers changed from: private */
    public void checkApk() {
        ZpLogger.d(TAG, "Update.checkApk check apk when forced update download finish");
        this.mCheckApkTask = new AsyncTask<Void, Void, Boolean>() {
            /* access modifiers changed from: protected */
            public Boolean doInBackground(Void... params) {
                return Boolean.valueOf(CheckAPK.checkAPKFile(Update.this.mContext, Update.this.mTargetFile, Update.this.mVersionCode));
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Boolean result) {
                ZpLogger.v(Update.TAG, "Update.checkApk.onPostExecute.result = " + result);
                if (result.booleanValue()) {
                    Update.mMyHandler.sendMessage(Update.mMyHandler.obtainMessage(1011));
                    return;
                }
                ZpLogger.e(Update.TAG, "Update.onPostExecute.apk checked fail");
                File file = new File(Update.this.mTargetFile);
                if (file != null && file.exists()) {
                    file.delete();
                }
                Update.mMyHandler.sendMessage(Update.mMyHandler.obtainMessage(1005));
            }
        };
        this.mCheckApkTask.execute(new Void[0]);
    }

    public void startUpdateActivity() {
        ZpLogger.v(TAG, "Update.startUpdateActivity.mIsStop = " + this.mIsStop + ", mIsForced = " + this.mIsForced);
        if (!this.mIsStop) {
            Activity activity = ActivityQueueManager.getTop();
            if (activity == null || UpdateActivity.class.getSimpleName().equals(activity.getClass().getSimpleName()) || NotForceUpdateActivity.class.getSimpleName().equals(activity.getClass().getSimpleName())) {
                ZpLogger.i(TAG, "Update.startUpdateActivity activity=" + activity);
                return;
            }
            if (this.mBundle == null) {
                putBundle();
            }
            try {
                if (!Boolean.valueOf(this.mBundle.getBoolean(UpdatePreference.INTENT_KEY_FORCE_INSTALL)).booleanValue()) {
                    SharedPreferences sp = this.mContext.getSharedPreferences("updateInfo", 0);
                    String upgradeMode = sp.getString(UpdatePreference.UPGRADE_MODE, "");
                    ZpLogger.d(TAG, "Update.startUpdateActivity upgradeMode=" + upgradeMode);
                    if (!"everyDay".equals(upgradeMode) || this.isDirectShowUpdate || !DateUtils.isToday(sp.getLong("update_dialog_show_time", 0))) {
                        showNotFroceActivity();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putLong("update_dialog_show_time", Calendar.getInstance().getTime().getTime());
                        editor.apply();
                    } else {
                        return;
                    }
                } else {
                    showFroceActivity();
                }
                UserTrackUtil.onCustomEvent(UpdatePreference.UT_SHOW_UPDATE_ACTIVITY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            releaseNetworkListner();
        }
    }

    private void showNotFroceActivity() {
        if (this.mBundle == null) {
            putBundle();
        }
        if (this.mBundle != null) {
            try {
                Intent startIntent = new Intent();
                startIntent.setData(Uri.parse("not_force_update://yunos_tvtaobao_not_force_update"));
                startIntent.putExtras(this.mBundle);
                startIntent.setFlags(268435456);
                this.mContext.startActivity(startIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showFroceActivity() {
        if (this.mBundle == null) {
            putBundle();
        }
        if (this.mBundle != null) {
            try {
                Intent startIntent = new Intent();
                startIntent.setData(Uri.parse("update://yunos_tvtaobao_update"));
                startIntent.putExtras(this.mBundle);
                startIntent.setFlags(268435456);
                this.mContext.startActivity(startIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadAgain() {
        if (this.mDownloadThread == null) {
            start();
            return;
        }
        ZpLogger.i(TAG, "Update.downloadAgain thread isAlive=" + this.mDownloadThread.isAlive());
        if (!this.mDownloadThread.isAlive()) {
            startDownload(0);
        }
    }

    private void putBundle() {
        this.mBundle = new Bundle();
        this.mBundle.putString(UpdatePreference.INTENT_KEY_APP_CODE, this.mAppCode);
        this.mBundle.putBoolean(UpdatePreference.INTENT_KEY_FORCE_INSTALL, this.mIsForced);
        this.mBundle.putString("updateInfo", this.mReleaseNote);
        this.mBundle.putString(UpdatePreference.INTENT_KEY_TARGET_FILE, this.mTargetFile);
        this.mBundle.putString(UpdatePreference.INTENT_KEY_TARGET_MD5, this.mTargetMd5);
        this.mBundle.putLong(UpdatePreference.INTENT_KEY_TARGET_SIZE, this.mTargetSize);
    }

    private void sendBroadcastToStartActivity(Bundle bundle) {
        ZpLogger.d(TAG, "Update.sendBroadcastToStartActivity.send broadcast to start update activity, bundle: " + bundle + ".mIsStop = " + this.mIsStop);
        if (!this.mIsStop) {
            UpdateStatus.setUpdateStatus(UpdateStatus.START_ACTIVITY, bundle);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.yunos.taobaotv.update.action.BROADCAST");
            broadcastIntent.putExtras(bundle);
            this.mContext.sendBroadcast(broadcastIntent);
            UserTrackUtil.onCustomEvent(UpdatePreference.UT_SHOW_UPDATE_ACTIVITY);
        }
    }

    private void showUpdateDialog(Bundle bundle, String str) {
        if (this.updateDialog == null) {
            this.updateDialog = new UpdateDialog((Context) this.mContext, str);
        }
        if (!this.updateDialog.isShowing()) {
            this.updateDialog.setBundle(bundle);
            RtEnv.set(RtEnv.KEY_UPDATE_BUNDLE, bundle);
            this.updateDialog.show();
        }
    }

    /* access modifiers changed from: private */
    public void sendTerminatedMessage() {
        stop();
    }

    private void registerNetworkListner() {
        ZpLogger.d(TAG, "Update.registerNetworkListner");
        NetworkManager.instance().init(this.mContext);
        NetworkManager.instance().registerStateChangedListener(this.mNetworkListner);
    }

    private void releaseNetworkListner() {
        ZpLogger.d(TAG, "Update.releaseNetworkListner");
        NetworkManager.instance().unregisterStateChangedListener(this.mNetworkListner);
        try {
            NetworkManager.instance().release();
        } catch (Exception e) {
        }
    }

    private boolean checkEmpty(String... strings) {
        for (String str : strings) {
            if (TextUtils.isEmpty(str)) {
                return true;
            }
        }
        return false;
    }
}
