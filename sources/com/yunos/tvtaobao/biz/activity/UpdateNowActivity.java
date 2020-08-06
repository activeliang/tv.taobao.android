package com.yunos.tvtaobao.biz.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.yunos.tv.core.aqm.ActGloballyUnique;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.UpdateStatus;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.controller.Update;
import com.yunos.tvtaobao.biz.model.AppInfo;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.yunos.tvtaobao.biz.util.MD5Util;
import com.yunos.tvtaobao.biz.util.UserTrackUtil;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.File;
import java.util.Map;

@ActGloballyUnique
public class UpdateNowActivity extends CoreActivity implements View.OnClickListener {
    private final int MSG_PROGRESS_UPDATE = 0;
    private final String TAG = "UpdateNowActivity";
    private AppInfo appInfo;
    private String mAppCode;
    /* access modifiers changed from: private */
    public ProgressBar mDownloadProgressBar;
    private Update.DownloadProgressListner mDownloadProgressListner = new Update.DownloadProgressListner() {
        public void onUpdateProgress(final int progress) {
            ZpLogger.d("UpdateNowActivity", "UpdateNowActivity.onUpdateProgress progress = " + progress);
            UpdateNowActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    UpdateNowActivity.this.mDownloadProgressBar.setProgress(progress);
                    if (progress >= 100) {
                        UpdateNowActivity.this.install();
                    }
                }
            });
        }

        public void onFileExists() {
            ZpLogger.d("UpdateNowActivity", "UpdateNowActivity.onFileExists");
            UpdateNowActivity.this.mHandler.sendEmptyMessage(0);
        }

        public void onFildValid() {
        }

        public void onInstall() {
            ZpLogger.d("UpdateNowActivity", "UpdateNowActivity.onInstall");
            UpdateNowActivity.this.install();
        }

        public void onError(int errorType) {
            ZpLogger.e("UpdateNowActivity", "UpdateNowActivity.onError error type: " + errorType);
            UpdateNowActivity.this.onProcessError(errorType);
        }

        public void onResumeDownload() {
            ZpLogger.e("UpdateNowActivity", "UpdateNowActivity.onResumeDownload");
        }

        public void onRetryDownload(int progress) {
            ZpLogger.v("UpdateNowActivity", "UpdateNowActivity.onRetryDownload, progress: " + progress);
            UpdateNowActivity.this.mDownloadProgressBar.setProgress(progress);
        }

        public void onChangeDownloadType(int type) {
            ZpLogger.e("UpdateNowActivity", "UpdateNowActivity.onChangeDownloadType type : " + type);
        }
    };
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int progress = UpdateNowActivity.this.mDownloadProgressBar.getProgress() + 1;
            UpdateNowActivity.this.mDownloadProgressBar.setProgress(progress);
            UpdateNowActivity.this.mHandler.sendEmptyMessageDelayed(0, 20);
            ZpLogger.w("UpdateNowActivity", "UpdateNowActivity.mHandler progress : " + progress);
            if (progress >= 100) {
                UpdateNowActivity.this.install();
                UpdateNowActivity.this.mHandler.removeMessages(0);
            }
        }
    };
    private boolean mIsForcedInstall;
    private String mTargetFile;
    private String mTargetMd5;
    private long mTargetSize;
    private Update up;
    private LinearLayout update_btn_layout;
    private Button update_btn_leave;
    private Button update_btn_now;
    private TextView update_text;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_up_update_now_activity);
        initProperty();
        initView();
        this.up = Update.get(this.mAppCode);
        if (this.up == null) {
            this.up = Update.get(UpdatePreference.TVTAOBAO_EXTERNAL);
        }
    }

    private void initProperty() {
        Intent intent = getIntent();
        this.mAppCode = intent.getStringExtra(UpdatePreference.INTENT_KEY_APP_CODE);
        this.mTargetFile = intent.getStringExtra(UpdatePreference.INTENT_KEY_TARGET_FILE);
        this.mTargetMd5 = intent.getStringExtra(UpdatePreference.INTENT_KEY_TARGET_MD5);
        this.mTargetSize = intent.getLongExtra(UpdatePreference.INTENT_KEY_TARGET_SIZE, 0);
        this.mIsForcedInstall = intent.getBooleanExtra(UpdatePreference.INTENT_KEY_FORCE_INSTALL, false);
    }

    private void initView() {
        this.update_text = (TextView) findViewById(R.id.bs_up_update_text);
        this.update_btn_layout = (LinearLayout) findViewById(R.id.bs_up_update_btn_layout);
        this.update_btn_leave = (Button) findViewById(R.id.bs_up_update_btn_leave);
        this.update_btn_now = (Button) findViewById(R.id.bs_up_update_btn_now);
        this.mDownloadProgressBar = (ProgressBar) findViewById(R.id.bs_up_update_progress);
        this.update_btn_now.requestFocus();
        this.update_btn_leave.setOnClickListener(this);
        this.update_btn_now.setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        if (this.up != null) {
            this.up.setIsStartActivity(true);
        }
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mHandler.removeCallbacksAndMessages((Object) null);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.mHandler = null;
    }

    public void finish() {
        if (this.up != null) {
            this.up.setIsStartActivity(false);
        }
        super.finish();
    }

    /* access modifiers changed from: protected */
    public void onStartActivityNetWorkError() {
    }

    /* access modifiers changed from: protected */
    public String getAppTag() {
        return "UpdateNowActivity";
    }

    /* access modifiers changed from: protected */
    public String getAppName() {
        return "tvtaobao.update.now";
    }

    public void onClick(View v) {
        Map<String, String> properties = Utils.getProperties();
        int i = v.getId();
        if (i == R.id.bs_up_update_btn_leave) {
            properties.put("controlName", UpdatePreference.UT_CANCEL);
            Utils.utControlHit(getFullPageName(), UpdatePreference.UT_CANCEL, properties);
            finish();
        } else if (i == R.id.bs_up_update_btn_now) {
            ZpLogger.d("UpdateNowActivity", "UpdateNowActivity.mInstallButton.onClick.onInstallClick, up: " + this.up + ", mAppCode = " + this.mAppCode);
            if (this.up != null) {
                showProgressLayout();
                this.up.setOnDownloadProgressListner(this.mDownloadProgressListner);
                this.up.start();
            } else {
                showErrorExitLayout();
                ZpLogger.d("UpdateNowActivity", "UpdateNowActivity.mInstallButton.onClick.showErrorExitLayout");
            }
            properties.put("controlName", UpdatePreference.UT_CLICK_UPDATE);
            Utils.utControlHit(getFullPageName(), UpdatePreference.UT_CLICK_UPDATE, properties);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            finishAndStop();
        }
        return super.onKeyDown(keyCode, event);
    }

    /* access modifiers changed from: private */
    public void onProcessError(int errorType) {
        switch (errorType) {
            case 0:
            case 1:
                showErrorExitLayout();
                return;
            case 2:
            case 3:
                showErrorExitLayout();
                return;
            default:
                return;
        }
    }

    private void showErrorExitLayout() {
        this.update_btn_layout.setVisibility(0);
        this.update_btn_now.setText("再试一次");
        this.mDownloadProgressBar.setProgress(0);
        this.mDownloadProgressBar.setVisibility(8);
        this.update_text.setText("更新失败，可能因为您的网络状况不佳");
    }

    private void showNetworkErrorLayout() {
    }

    private void showProgressLayout() {
        this.update_btn_layout.setVisibility(8);
        this.mDownloadProgressBar.setVisibility(0);
        this.update_text.setText("下载中，请勿返回或关闭电源");
    }

    /* access modifiers changed from: private */
    public void install() {
        try {
            File newAPK = new File(this.mTargetFile);
            newAPK.setReadable(true, false);
            if (newAPK.length() != this.mTargetSize || !this.mTargetMd5.equalsIgnoreCase(MD5Util.getMD5(newAPK))) {
                UserTrackUtil.onErrorEvent(2);
                ZpLogger.e("UpdateNowActivity", "UpdateNowActivity.install,invalid file, file size: " + newAPK.length() + " correct size: " + this.mTargetSize + " file md5: " + MD5Util.getMD5(newAPK) + " correct MD5: " + this.mTargetMd5);
                ZpLogger.d("UpdateNowActivity", "UpdateNowActivity.install,delete invalid file: " + newAPK.delete());
                finishAndStop();
            }
        } catch (Exception e) {
            UserTrackUtil.onErrorEvent(3);
            ZpLogger.e("UpdateNowActivity", "get md5 exception: " + e.getLocalizedMessage());
            setResult(0);
            finishAndStop();
        }
        ZpLogger.d("UpdateNowActivity", "UpdateNowActivity.install, MD5 check success, start to install new apk");
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags(268435456);
        intent.setDataAndType(Uri.parse("file://" + this.mTargetFile), "application/vnd.android.package-archive");
        try {
            startActivity(intent);
        } catch (Exception e2) {
            UserTrackUtil.onErrorEvent(3);
            ZpLogger.e("UpdateNowActivity", "UpdateNowActivity.install,PackageInstaller exception: " + e2.getLocalizedMessage());
            setResult(0);
            finishAndStop();
        }
    }

    private void finishAndStop() {
        UpdateStatus.setUpdateStatus(UpdateStatus.UNKNOWN, (Bundle) null);
        ZpLogger.d("UpdateNowActivity", "UpdateNowActivity.finishAndStop up : " + this.up);
        if (this.up != null) {
            this.up.stop();
        }
        finish();
    }

    public String getPageName() {
        return "Page_Voice_error_Lowversion";
    }

    public Map<String, String> getPageProperties() {
        Map<String, String> properties = Utils.getProperties();
        properties.put("appkey", Config.getAppKey());
        return properties;
    }
}
