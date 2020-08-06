package com.yunos.tvtaobao.biz.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.statistic.CT;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.aqm.ActGloballyUnique;
import com.yunos.tv.core.common.CoreIntentKey;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tv.core.config.UpdateStatus;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.controller.Update;
import com.yunos.tvtaobao.biz.net.network.NetworkManager;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.yunos.tvtaobao.biz.util.MD5Util;
import com.yunos.tvtaobao.biz.util.UserTrackUtil;
import com.yunos.tvtaobao.biz.widget.UpdateProgressView;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.File;
import java.util.Map;

@ActGloballyUnique
public class UpdateActivity extends CoreActivity {
    /* access modifiers changed from: private */
    public static String TAG = "UpdateActivity";
    private Update.DownloadProgressListner downloadProgressListner = new Update.DownloadProgressListner() {
        public void onRetryDownload(int progress) {
            ZpLogger.i(UpdateActivity.TAG, UpdateActivity.TAG + ".onRetryDownload progress=" + progress);
            UpdateActivity.this.hideInfoAndShowBar();
            UpdateActivity.this.updateProgressView.setDownloadProgress(progress);
        }

        public void onUpdateProgress(int progress) {
            ZpLogger.i(UpdateActivity.TAG, UpdateActivity.TAG + ".onUpdateProgress progress=" + progress);
            if (progress != 100) {
                UpdateActivity.this.hideInfoAndShowBar();
                UpdateActivity.this.updateProgressView.setDownloadProgress(progress);
            }
        }

        public void onFileExists() {
            ZpLogger.i(UpdateActivity.TAG, UpdateActivity.TAG + ".onFileExists ");
            UpdateActivity.this.updateProgressView.setDownloadProgress(100);
            UpdateActivity.this.install();
        }

        public void onFildValid() {
            ZpLogger.i(UpdateActivity.TAG, UpdateActivity.TAG + ".onFildValid ");
            UpdateActivity.this.updateProgressView.setDownloadProgress(100);
            UpdateActivity.this.install();
        }

        public void onInstall() {
            ZpLogger.i(UpdateActivity.TAG, UpdateActivity.TAG + ".onInstall ");
        }

        public void onError(int errorType) {
            ZpLogger.i(UpdateActivity.TAG, UpdateActivity.TAG + ".onError errorType=" + errorType);
            UpdateActivity.this.updateProgressView.downloadError();
        }

        public void onResumeDownload() {
            ZpLogger.i(UpdateActivity.TAG, UpdateActivity.TAG + ".onResumeDownload ");
        }

        public void onChangeDownloadType(int type) {
            ZpLogger.i(UpdateActivity.TAG, UpdateActivity.TAG + ".onChangeDownloadType type=" + type);
        }
    };
    private TextView feature1;
    private TextView feature2;
    private TextView feature3;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    /* access modifiers changed from: private */
    public String mAppCode;
    private boolean mIsForcedInstall;
    private String mTargetFile;
    private String mTargetMd5;
    private long mTargetSize;
    private RelativeLayout rlUpdateInfo;
    private String updateInfoText;
    /* access modifiers changed from: private */
    public UpdateProgressView updateProgressView;
    private TextView update_title;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.updateInfoText = getSharedPreferences("updateInfo", 0).getString(UpdatePreference.UPDATE_TIPS, getString(R.string.bs_before_up_default_memo));
        setContentView(R.layout.bs_up_update_mandatory_activity);
        this.rlUpdateInfo = (RelativeLayout) findViewById(R.id.rl_update_info);
        this.layout1 = (LinearLayout) findViewById(R.id.bs_up_new_feature_content1);
        this.layout2 = (LinearLayout) findViewById(R.id.bs_up_new_feature_content2);
        this.layout3 = (LinearLayout) findViewById(R.id.bs_up_new_feature_content3);
        this.feature1 = (TextView) findViewById(R.id.bs_up_fature_text1);
        this.feature2 = (TextView) findViewById(R.id.bs_up_fature_text2);
        this.feature3 = (TextView) findViewById(R.id.bs_up_fature_text3);
        this.updateProgressView = new UpdateProgressView(this);
        this.updateProgressView.setOnClickListener(new UpdateProgressView.OnClickListener() {
            public void back() {
                UpdateActivity.this.finishAndStop();
            }

            public void again() {
                Update up;
                if (NetworkManager.instance().isNetworkConnected() && (up = Update.get(UpdateActivity.this.mAppCode)) != null) {
                    up.downloadAgain();
                }
            }
        });
        LinearLayout[] layouts = {this.layout1, this.layout2, this.layout3};
        TextView[] textViews = {this.feature1, this.feature2, this.feature3};
        if (!TextUtils.isEmpty(this.updateInfoText)) {
            String[] tmp = this.updateInfoText.split("\n");
            ZpLogger.e("NewFeature", "NewFeature.updateInfoText : " + this.updateInfoText + " ,tmp.length : " + tmp.length);
            for (int i = 0; i < tmp.length; i++) {
                ZpLogger.e("NewFeature", "NewFeature.tmp : " + tmp[i] + " ,textview : " + textViews[i]);
                layouts[i].setVisibility(0);
                textViews[i].setText(tmp[i]);
            }
        }
        setCheckNetWork(true);
        initProperty();
        Utils.utCustomHit("Update_Forcedinstall_Expore", Utils.getProperties());
        Utils.utCustomHit("Expose_update", getPropertiesForceUpdate());
    }

    public Map<String, String> getPropertiesForceUpdate() {
        Map<String, String> p = Utils.getProperties();
        p.put("type", "force");
        if (!TextUtils.isEmpty(CloudUUIDWrapper.getCloudUUID())) {
            p.put("uuid", CloudUUIDWrapper.getCloudUUID());
        }
        try {
            if (!TextUtils.isEmpty(AppInfo.getPackageName()) && !TextUtils.isEmpty(AppInfo.getAppVersionName())) {
                p.put(CoreIntentKey.URI_FROM_APP, AppInfo.getPackageName() + AppInfo.getAppVersionName());
            }
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
        if (CoreApplication.getLoginHelper(getApplicationContext()).isLogin()) {
            p.put("is_login", "1");
        } else {
            p.put("is_login", "0");
        }
        return p;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        ZpLogger.d(TAG, TAG + ".onDestroy");
        super.onDestroy();
        this.updateProgressView.onDestroy();
        Update up = Update.get(this.mAppCode);
        if (up != null) {
            up.setOnDownloadProgressListner((Update.DownloadProgressListner) null);
        }
    }

    public void finish() {
        super.finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ZpLogger.v(TAG, TAG + ".onKeyDown.event = " + event);
        if (keyCode == 4) {
            UserTrackUtil.onCtrlClicked(CT.Button, UpdatePreference.UT_CANCEL, new String[0]);
            setCheckNetWork(false);
            finishAndStop();
            return true;
        }
        if (keyCode == 23 || keyCode == 66) {
            Utils.utControlHit(getFullPageName(), "Update_Forcedinstall_update", Utils.getProperties());
            Update up = Update.get(this.mAppCode);
            if (up != null) {
                up.setOnDownloadProgressListner(this.downloadProgressListner);
                Thread thread = up.getDownloaderThread();
                if (thread == null) {
                    up.start();
                } else {
                    ZpLogger.i(TAG, TAG + ".onKeyDown thread isAlive=" + thread.isAlive());
                    if (!thread.isAlive()) {
                        up.startDownload(0);
                    } else {
                        up.setDownloadDelayTime(0);
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /* access modifiers changed from: private */
    public void hideInfoAndShowBar() {
        if (this.rlUpdateInfo.getVisibility() == 0) {
            this.rlUpdateInfo.setVisibility(8);
        }
        this.updateProgressView.showProgressInfo();
    }

    private void initProperty() {
        Intent intent = getIntent();
        this.mAppCode = intent.getStringExtra(UpdatePreference.INTENT_KEY_APP_CODE);
        this.mTargetFile = intent.getStringExtra(UpdatePreference.INTENT_KEY_TARGET_FILE);
        this.mTargetMd5 = intent.getStringExtra(UpdatePreference.INTENT_KEY_TARGET_MD5);
        this.mTargetSize = intent.getLongExtra(UpdatePreference.INTENT_KEY_TARGET_SIZE, 0);
        this.mIsForcedInstall = intent.getBooleanExtra(UpdatePreference.INTENT_KEY_FORCE_INSTALL, false);
    }

    /* access modifiers changed from: private */
    public void install() {
        try {
            File newAPK = new File(this.mTargetFile);
            newAPK.setReadable(true, false);
            if (newAPK.length() != this.mTargetSize || !this.mTargetMd5.equalsIgnoreCase(MD5Util.getMD5(newAPK))) {
                UserTrackUtil.onErrorEvent(2);
                ZpLogger.e(TAG, TAG + ".install,invalid file, file size: " + newAPK.length() + " correct size: " + this.mTargetSize + " file md5: " + MD5Util.getMD5(newAPK) + " correct MD5: " + this.mTargetMd5);
                ZpLogger.d(TAG, TAG + ".install,delete invalid file: " + newAPK.delete());
                finishAndStop();
            }
        } catch (Exception e) {
            UserTrackUtil.onErrorEvent(3);
            ZpLogger.e(TAG, "get md5 exception: " + e.getLocalizedMessage());
            setResult(0);
            finishAndStop();
        }
        ZpLogger.d(TAG, TAG + ".install, MD5 check success, start to install new apk");
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags(268435456);
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setFlags(1);
            intent.setDataAndType(FileProvider.getUriForFile(this, "com.tvtao.fileprovider", new File(this.mTargetFile)), "application/vnd.android.package-archive");
            if (getPackageManager() != null && intent.resolveActivity(getPackageManager()) == null) {
                intent.setFlags(268435456);
                intent.setDataAndType(Uri.parse("file://" + this.mTargetFile), "application/vnd.android.package-archive");
            }
        } else {
            intent.setFlags(268435456);
            intent.setDataAndType(Uri.parse("file://" + this.mTargetFile), "application/vnd.android.package-archive");
        }
        try {
            startActivity(intent);
        } catch (Exception e2) {
            UserTrackUtil.onErrorEvent(3);
            ZpLogger.e(TAG, TAG + ".install,PackageInstaller exception: " + e2.getLocalizedMessage());
            setResult(0);
            finishAndStop();
        }
    }

    /* access modifiers changed from: private */
    public void finishAndStop() {
        UpdateStatus.setUpdateStatus(UpdateStatus.UNKNOWN, (Bundle) null);
        Update up = Update.get(this.mAppCode);
        if (up != null) {
            up.stop();
        }
        exitChildProcess();
        clearAllOpenedActivity(this);
        finish();
        CoreApplication.getApplication().clear();
        Process.killProcess(Process.myPid());
    }

    /* access modifiers changed from: protected */
    public String getAppTag() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public String getAppName() {
        return "tvtaobao.update";
    }

    /* access modifiers changed from: protected */
    public void onStartActivityNetWorkError() {
    }

    public String getPageName() {
        return "Update_Forcedinstall_Expore";
    }

    private void exitChildProcess() {
        for (ActivityManager.RunningAppProcessInfo appProcess : ((ActivityManager) getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
            if (appProcess.processName.compareTo("com.yunos.tvtaobao:bs_webbroser") == 0) {
                ZpLogger.i(TAG, "kill processName=" + appProcess.processName);
                Process.killProcess(appProcess.pid);
            }
        }
    }
}
