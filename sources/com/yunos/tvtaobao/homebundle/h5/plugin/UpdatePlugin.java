package com.yunos.tvtaobao.homebundle.h5.plugin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.alibaba.fastjson.JSON;
import com.yunos.tv.blitz.BlitzPlugin;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.core.RtEnv;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tv.core.util.DeviceUtil;
import com.yunos.tvtaobao.biz.controller.Update;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.yunos.tvtaobao.biz.util.CheckAPK;
import com.yunos.tvtaobao.biz.util.MD5Util;
import com.yunos.tvtaobao.biz.util.UserTrackUtil;
import com.yunos.tvtaobao.homebundle.activity.HomeActivity;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.File;
import java.lang.ref.WeakReference;

public class UpdatePlugin {
    private static final int GOTO_UPDATE = 100001;
    private static final int IS_UPDATE = 100002;
    private static final String RESULT = "result";
    /* access modifiers changed from: private */
    public static final String TAG = UpdatePlugin.class.getName();
    /* access modifiers changed from: private */
    public long cbData_final;
    private JsHandler mHandler;
    /* access modifiers changed from: private */
    public WeakReference<HomeActivity> mReference;
    private BlitzPlugin.JsCallback mStbIDGetCallback = new BlitzPlugin.JsCallback() {
        public void onCall(String param, long cdData) {
            if (UpdatePlugin.this.mReference != null) {
                String stbId = DeviceUtil.initMacAddress((HomeActivity) UpdatePlugin.this.mReference.get());
                ZpLogger.i(UpdatePlugin.TAG, "mStbIDGetCallback  stb == " + stbId);
                if (stbId != null) {
                    BzResult result = new BzResult();
                    result.addData("result", "true");
                    result.addData("stbID", stbId);
                    result.setSuccess();
                    BlitzPlugin.responseJs(true, result.toJsonString(), cdData);
                    return;
                }
                UpdatePlugin.responseFalse(cdData);
            }
        }
    };
    private UpdateCallback plugin;
    private Update update;

    public UpdatePlugin(WeakReference<HomeActivity> reference) {
        this.mReference = reference;
        this.mHandler = new JsHandler(new WeakReference(this));
        onInitPlugin();
    }

    private void onInitPlugin() {
        this.plugin = new UpdateCallback(new WeakReference(this));
        BlitzPlugin.bindingJs("tvtaobao_isUpdate", this.plugin);
        BlitzPlugin.bindingJs("tvtaobao_gotoUpdate", this.plugin);
        ZpLogger.i(TAG, "mStbIDGetCallback");
        BlitzPlugin.bindingJs("tvtaobao_stbId_get", this.mStbIDGetCallback);
    }

    /* access modifiers changed from: private */
    public boolean onHandleCall(String param, long cbData) {
        this.cbData_final = cbData;
        String methodName = JSON.parseObject(param).getString("methodName");
        ZpLogger.e(TAG, TAG + ".onHandleCallPay methodName : " + methodName);
        if (param == null || methodName == null) {
            return true;
        }
        ZpLogger.e(TAG, "param : " + param + "     methodName : " + methodName);
        if (methodName.equalsIgnoreCase("tvtaobao_isUpdate")) {
            Message msg = new Message();
            msg.obj = param;
            msg.what = IS_UPDATE;
            this.mHandler.sendMessage(msg);
            return true;
        } else if (!methodName.equalsIgnoreCase("tvtaobao_gotoUpdate")) {
            return true;
        } else {
            Message msg2 = new Message();
            msg2.obj = param;
            msg2.what = GOTO_UPDATE;
            this.mHandler.sendMessage(msg2);
            return true;
        }
    }

    private static class UpdateCallback implements BlitzPlugin.JsCallback {
        private WeakReference<UpdatePlugin> mReference;

        private UpdateCallback(WeakReference<UpdatePlugin> reference) {
            this.mReference = reference;
        }

        public void onCall(String param, long cbData) {
            ZpLogger.i(UpdatePlugin.TAG, "onCall --> param  =" + param + ";  cbData = " + cbData);
            if (this.mReference != null && this.mReference.get() != null) {
                boolean unused = ((UpdatePlugin) this.mReference.get()).onHandleCall(param, cbData);
            }
        }
    }

    private static class JsHandler extends Handler {
        private WeakReference<UpdatePlugin> mReference;

        private JsHandler(WeakReference<UpdatePlugin> reference) {
            this.mReference = reference;
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.mReference != null && this.mReference.get() != null) {
                UpdatePlugin plugin = (UpdatePlugin) this.mReference.get();
                switch (msg.what) {
                    case UpdatePlugin.GOTO_UPDATE /*100001*/:
                        ZpLogger.e(UpdatePlugin.TAG, UpdatePlugin.TAG + ".Handler GOTO_UPDATE");
                        BzResult result = new BzResult();
                        result.addData("version", AppInfo.getAppVersionNum());
                        if (((UpdatePlugin) this.mReference.get()).install()) {
                            result.setSuccess();
                            BlitzPlugin.responseJs(true, result.toJsonString(), ((UpdatePlugin) this.mReference.get()).cbData_final);
                            return;
                        }
                        result.addData("result", "false");
                        BlitzPlugin.responseJs(false, result.toJsonString(), ((UpdatePlugin) this.mReference.get()).cbData_final);
                        return;
                    case UpdatePlugin.IS_UPDATE /*100002*/:
                        ZpLogger.e(UpdatePlugin.TAG, UpdatePlugin.TAG + ".Handler IS_UPDATE");
                        Context mContext = (Context) plugin.mReference.get();
                        BzResult result2 = new BzResult();
                        if (mContext != null) {
                            SharedPreferences sp = mContext.getSharedPreferences("updateInfo", 0);
                            String oldVersion = sp.getString("version", "");
                            String oldFilePath = sp.getString(UpdatePreference.SP_KEY_PATH, "");
                            File newAPK = new File(oldFilePath);
                            if (newAPK != null && newAPK.exists() && CheckAPK.checkAPKFile(mContext, oldFilePath, oldVersion)) {
                                ZpLogger.e(UpdatePlugin.TAG, UpdatePlugin.TAG + ".onHandleCall File Exists and valid");
                                result2.addData("isUpdate", true);
                                result2.addData("version", AppInfo.getAppVersionNum());
                                result2.setSuccess();
                                BlitzPlugin.responseJs(true, result2.toJsonString(), ((UpdatePlugin) this.mReference.get()).cbData_final);
                                return;
                            }
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean install() {
        if (this.update == null) {
            this.update = Update.get("tvtaobao");
        }
        Bundle bundle = this.update.getBundle();
        if (bundle == null) {
            bundle = (Bundle) RtEnv.get(RtEnv.KEY_UPDATE_BUNDLE);
            if (bundle == null) {
                return false;
            }
        } else {
            RtEnv.set(RtEnv.KEY_UPDATE_BUNDLE, bundle);
        }
        String mTargetFile = bundle.getString(UpdatePreference.INTENT_KEY_TARGET_FILE);
        long mTargetSize = bundle.getLong(UpdatePreference.INTENT_KEY_TARGET_SIZE);
        String mTargetMd5 = bundle.getString(UpdatePreference.INTENT_KEY_TARGET_MD5);
        try {
            File newAPK = new File(mTargetFile);
            newAPK.setReadable(true, false);
            if (newAPK.length() != mTargetSize || !mTargetMd5.equalsIgnoreCase(MD5Util.getMD5(newAPK))) {
                UserTrackUtil.onErrorEvent(2);
                ZpLogger.e(TAG, TAG + ".install,invalid file, file size: " + newAPK.length() + " correct size: " + mTargetSize + " file md5: " + MD5Util.getMD5(newAPK) + " correct MD5: " + mTargetMd5);
                ZpLogger.d(TAG, TAG + ".install,delete invalid file: " + newAPK.delete());
            }
            ZpLogger.d(TAG, TAG + ".install, MD5 check success, start to install new apk");
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addFlags(268435456);
            intent.setDataAndType(Uri.parse("file://" + mTargetFile), "application/vnd.android.package-archive");
            try {
                if (this.mReference == null) {
                    return true;
                }
                ((HomeActivity) this.mReference.get()).startActivity(intent);
                return true;
            } catch (Exception e) {
                UserTrackUtil.onErrorEvent(3);
                ZpLogger.e(TAG, TAG + ".install,PackageInstaller exception: " + e.getLocalizedMessage());
                return false;
            }
        } catch (Exception e2) {
            UserTrackUtil.onErrorEvent(3);
            ZpLogger.e(TAG, "get md5 exception: " + e2.getLocalizedMessage());
            return false;
        }
    }

    /* access modifiers changed from: private */
    public static void responseFalse(long cdData) {
        BzResult result = new BzResult();
        result.addData("result", "false");
        BlitzPlugin.responseJs(false, result.toJsonString(), cdData);
    }
}
