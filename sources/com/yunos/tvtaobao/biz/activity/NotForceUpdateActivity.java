package com.yunos.tvtaobao.biz.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.aqm.ActGloballyUnique;
import com.yunos.tv.core.common.CoreIntentKey;
import com.yunos.tv.core.common.ImageLoaderManager;
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
public class NotForceUpdateActivity extends CoreActivity {
    /* access modifiers changed from: private */
    public static String TAG = "UpdateActivity";
    private ImageView bg_img;
    private TextView bs_up_fature_text1;
    private TextView bs_up_fature_text2;
    private TextView bs_up_fature_text3;
    private String color;
    /* access modifiers changed from: private */
    public Update.DownloadProgressListner downloadProgressListner = new Update.DownloadProgressListner() {
        public void onRetryDownload(int progress) {
            ZpLogger.i(NotForceUpdateActivity.TAG, NotForceUpdateActivity.TAG + ".onRetryDownload progress=" + progress);
            NotForceUpdateActivity.this.hideInfoAndShowBar();
            NotForceUpdateActivity.this.updateProgressView.onResumeDownload();
            NotForceUpdateActivity.this.updateProgressView.setDownloadProgress(progress);
        }

        public void onUpdateProgress(int progress) {
            ZpLogger.i(NotForceUpdateActivity.TAG, NotForceUpdateActivity.TAG + ".onUpdateProgress progress=" + progress);
            if (progress != 100) {
                NotForceUpdateActivity.this.hideInfoAndShowBar();
                NotForceUpdateActivity.this.updateProgressView.onResumeDownload();
                NotForceUpdateActivity.this.updateProgressView.setDownloadProgress(progress);
            }
        }

        public void onFileExists() {
            ZpLogger.i(NotForceUpdateActivity.TAG, NotForceUpdateActivity.TAG + ".onFileExists ");
            NotForceUpdateActivity.this.updateProgressView.setDownloadProgress(100);
            NotForceUpdateActivity.this.install();
        }

        public void onFildValid() {
            ZpLogger.i(NotForceUpdateActivity.TAG, NotForceUpdateActivity.TAG + ".onFildValid ");
            NotForceUpdateActivity.this.updateProgressView.setDownloadProgress(100);
            NotForceUpdateActivity.this.install();
        }

        public void onInstall() {
            ZpLogger.i(NotForceUpdateActivity.TAG, NotForceUpdateActivity.TAG + ".onInstall ");
        }

        public void onError(int errorType) {
            ZpLogger.i(NotForceUpdateActivity.TAG, NotForceUpdateActivity.TAG + ".onError errorType=" + errorType);
            NotForceUpdateActivity.this.updateProgressView.downloadError();
        }

        public void onResumeDownload() {
            ZpLogger.i(NotForceUpdateActivity.TAG, NotForceUpdateActivity.TAG + ".onResumeDownload ");
        }

        public void onChangeDownloadType(int type) {
            ZpLogger.i(NotForceUpdateActivity.TAG, NotForceUpdateActivity.TAG + ".onChangeDownloadType type=" + type);
        }
    };
    private TextView feature1;
    private TextView feature2;
    private TextView feature3;
    private boolean forceType;
    private String image1;
    private String image2;
    /* access modifiers changed from: private */
    public String image3;
    /* access modifiers changed from: private */
    public String image4;
    /* access modifiers changed from: private */
    public String image5;
    /* access modifiers changed from: private */
    public String image6;
    private String laterOn;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    /* access modifiers changed from: private */
    public ImageView left_image;
    private LinearLayout llUpdateInfo;
    /* access modifiers changed from: private */
    public String mAppCode;
    private boolean mIsForcedInstall;
    private String mTargetFile;
    private String mTargetMd5;
    private long mTargetSize;
    private ImageView main_image;
    private RelativeLayout main_layout;
    private String newVersionName;
    private TextView new_version;
    /* access modifiers changed from: private */
    public ImageView right_image;
    private String updateInfoText;
    /* access modifiers changed from: private */
    public UpdateProgressView updateProgressView;
    private TextView update_title;
    private String upgradeNow;
    private ImageView view1;
    private ImageView view2;
    private ImageView view3;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        int parseColor;
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("updateInfo", 0);
        this.updateInfoText = sp.getString(UpdatePreference.NEW_RELEASE_NOTE, getString(R.string.bs_before_up_default_memo));
        this.image1 = sp.getString(UpdatePreference.IMAGE1, "");
        this.image2 = sp.getString(UpdatePreference.IMAGE2, "");
        this.image3 = sp.getString(UpdatePreference.IMAGE3, "");
        this.image4 = sp.getString(UpdatePreference.IMAGE4, "");
        this.image5 = sp.getString(UpdatePreference.IMAGE5, "");
        this.image6 = sp.getString(UpdatePreference.IMAGE6, "");
        this.color = sp.getString(UpdatePreference.COLOR, "");
        this.laterOn = sp.getString(UpdatePreference.LATER_ON, "");
        this.upgradeNow = sp.getString(UpdatePreference.UPGRADE_NOW, "");
        this.newVersionName = sp.getString("versionName", "");
        this.forceType = sp.getBoolean(UpdatePreference.INTENT_KEY_FORCE_INSTALL, false);
        setContentView(R.layout.bs_up_not_force_update_mandatory_activity);
        this.llUpdateInfo = (LinearLayout) findViewById(R.id.ll_update_info);
        this.layout1 = (LinearLayout) findViewById(R.id.bs_up_new_feature_content1);
        this.layout2 = (LinearLayout) findViewById(R.id.bs_up_new_feature_content2);
        this.layout3 = (LinearLayout) findViewById(R.id.bs_up_new_feature_content3);
        this.view1 = (ImageView) findViewById(R.id.view1);
        this.view2 = (ImageView) findViewById(R.id.view2);
        this.view3 = (ImageView) findViewById(R.id.view3);
        this.new_version = (TextView) findViewById(R.id.new_version);
        this.bs_up_fature_text1 = (TextView) findViewById(R.id.bs_up_fature_text1);
        this.bs_up_fature_text2 = (TextView) findViewById(R.id.bs_up_fature_text2);
        this.bs_up_fature_text3 = (TextView) findViewById(R.id.bs_up_fature_text3);
        this.feature1 = (TextView) findViewById(R.id.bs_up_fature_text1);
        this.feature2 = (TextView) findViewById(R.id.bs_up_fature_text2);
        this.feature3 = (TextView) findViewById(R.id.bs_up_fature_text3);
        this.left_image = (ImageView) findViewById(R.id.left_image);
        this.right_image = (ImageView) findViewById(R.id.right_image);
        this.main_layout = (RelativeLayout) findViewById(R.id.main_layout);
        this.main_image = (ImageView) findViewById(R.id.main_image);
        this.bg_img = (ImageView) findViewById(R.id.bg_img);
        this.updateProgressView = new UpdateProgressView(this);
        this.updateProgressView.setOnClickListener(new UpdateProgressView.OnClickListener() {
            public void back() {
                NotForceUpdateActivity.this.finish();
            }

            public void again() {
                Update up;
                if (NetworkManager.instance().isNetworkConnected() && (up = Update.get(NotForceUpdateActivity.this.mAppCode)) != null) {
                    up.downloadAgain();
                }
            }
        });
        if (this.upgradeNow.equals(UpdatePreference.IMAGE5)) {
            this.right_image.requestFocus();
        } else {
            this.left_image.requestFocus();
        }
        if (!TextUtils.isEmpty(this.newVersionName)) {
            this.new_version.setText("版本: " + this.newVersionName);
        }
        ImageLoaderManager.get().displayImage(this.image1, this.main_image);
        ImageLoaderManager.get().displayImage(this.laterOn.equals(UpdatePreference.IMAGE4) ? this.image4 : this.image3, this.left_image);
        ImageLoaderManager.get().displayImage(this.upgradeNow.equals(UpdatePreference.IMAGE5) ? this.image5 : this.image6, this.right_image);
        ImageLoaderManager.get().displayImage(this.image2, this.view1);
        ImageLoaderManager.get().displayImage(this.image2, this.view2);
        ImageLoaderManager.get().displayImage(this.image2, this.view3);
        this.bs_up_fature_text1.setTextColor(this.color.charAt(0) == '#' ? Color.parseColor(this.color) : Color.parseColor('#' + this.color));
        this.bs_up_fature_text2.setTextColor(this.color.charAt(0) == '#' ? Color.parseColor(this.color) : Color.parseColor('#' + this.color));
        TextView textView = this.bs_up_fature_text3;
        if (this.color.charAt(0) == '#') {
            parseColor = Color.parseColor(this.color);
        } else {
            parseColor = Color.parseColor('#' + this.color);
        }
        textView.setTextColor(parseColor);
        this.left_image.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ImageLoaderManager.get().displayImage(NotForceUpdateActivity.this.image3, NotForceUpdateActivity.this.left_image);
                } else {
                    ImageLoaderManager.get().displayImage(NotForceUpdateActivity.this.image4, NotForceUpdateActivity.this.left_image);
                }
            }
        });
        this.right_image.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ImageLoaderManager.get().displayImage(NotForceUpdateActivity.this.image5, NotForceUpdateActivity.this.right_image);
                } else {
                    ImageLoaderManager.get().displayImage(NotForceUpdateActivity.this.image6, NotForceUpdateActivity.this.right_image);
                }
            }
        });
        this.left_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.utControlHit(NotForceUpdateActivity.this.getPageName(), "Expose_update_button_refuse", NotForceUpdateActivity.this.initTBSProperty());
                NotForceUpdateActivity.this.finish();
            }
        });
        this.right_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.utControlHit(NotForceUpdateActivity.this.getPageName(), "Expose_update_button_update", NotForceUpdateActivity.this.initTBSProperty());
                Update up = Update.get(NotForceUpdateActivity.this.mAppCode);
                if (up != null) {
                    up.setOnDownloadProgressListner(NotForceUpdateActivity.this.downloadProgressListner);
                    Thread thread = up.getDownloaderThread();
                    if (thread == null) {
                        up.start();
                        return;
                    }
                    ZpLogger.i(NotForceUpdateActivity.TAG, NotForceUpdateActivity.TAG + ".onKeyDown thread isAlive=" + thread.isAlive());
                    if (!thread.isAlive()) {
                        up.startDownload(0);
                    } else {
                        up.setDownloadDelayTime(0);
                    }
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
        Utils.utCustomHit("Expose_update", initTBSProperty());
    }

    public Map<String, String> initTBSProperty() {
        Map<String, String> p = Utils.getProperties();
        p.put("type", this.forceType ? "force" : "optional");
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
        if (this.bg_img != null) {
            this.bg_img.setVisibility(8);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.bg_img != null) {
            this.bg_img.setVisibility(0);
        }
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

    /* access modifiers changed from: private */
    public void hideInfoAndShowBar() {
        if (this.llUpdateInfo.getVisibility() == 0) {
            this.llUpdateInfo.setVisibility(8);
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

    private void finishAndStop() {
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
        return "Update_NotForcedinstall_Expore";
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
