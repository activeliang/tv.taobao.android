package com.yunos.tvtaobao.biz.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yunos.tv.core.config.UpdateStatus;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.controller.Update;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import com.yunos.tvtaobao.biz.util.MD5Util;
import com.yunos.tvtaobao.biz.util.StringUtil;
import com.yunos.tvtaobao.biz.util.UserTrackUtil;
import com.yunos.tvtaobao.businessview.R;
import com.yunos.tvtaobao.payment.request.GlobalConfig;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.File;
import java.util.Calendar;

public class UpdateDialog extends Dialog {
    private static final String PAGENAME = "update_dialog";
    private final String TAG;
    private Animation animation;
    private Button leave_update_btn;
    private String mAppCode;
    private Context mContext;
    private Handler mHandler;
    private boolean mIsForcedInstall;
    private String mTargetFile;
    private String mTargetMd5;
    private long mTargetSize;
    private Button now_update_btn;
    private String title;
    private RelativeLayout update_btn_layout;
    private TextView update_title;

    public UpdateDialog(Context context) {
        this(context, R.style.update_top_Dialog);
    }

    public UpdateDialog(Context context, String str) {
        this(context, R.style.update_top_Dialog);
        this.title = str;
    }

    public UpdateDialog(Context context, int theme) {
        super(context, theme);
        this.TAG = "UpdateDialog";
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                UpdateDialog.this.dismiss();
            }
        };
        this.mContext = context;
        WindowManager.LayoutParams l = getWindow().getAttributes();
        l.dimAmount = 0.8f;
        l.gravity = 48;
        getWindow().setType(2003);
        getWindow().setWindowAnimations(R.style.bs_up_dialog_animation);
        getWindow().setAttributes(l);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_up_update_top_dialog);
        GlobalConfig globalConfig = GlobalConfigInfo.getInstance().getGlobalConfig();
        this.update_title = (TextView) findViewById(R.id.update_title);
        this.update_title.setText(StringUtil.isEmpty(this.title) ? this.mContext.getString(R.string.bs_up_dilog_title) : this.title);
        this.update_btn_layout = (RelativeLayout) findViewById(R.id.dialog_update_btn_layout);
        this.now_update_btn = (Button) findViewById(R.id.dialog_update_top_now);
        this.leave_update_btn = (Button) findViewById(R.id.dialog_update_top_later);
        this.animation = AnimationUtils.loadAnimation(this.mContext, R.anim.bs_up_dialog_btnlayout_enter);
        this.animation.setInterpolator(new DecelerateInterpolator());
        this.now_update_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.utControlHit(UpdateDialog.PAGENAME, "Update_passive_update", Utils.getProperties());
                UpdateDialog.this.install();
            }
        });
        this.leave_update_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.utControlHit(UpdateDialog.PAGENAME, "Update_passive_cancel", Utils.getProperties());
                UpdateDialog.this.finishAndStop();
            }
        });
        this.mHandler.sendEmptyMessageDelayed(0, 6000);
    }

    public void show() {
        super.show();
        this.now_update_btn.requestFocus();
        this.update_btn_layout.setAnimation(this.animation);
        Utils.utPageAppear(PAGENAME, PAGENAME);
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences("updateInfo", 0).edit();
        editor.putLong("update_dialog_show_time", Calendar.getInstance().getTime().getTime());
        editor.apply();
    }

    public void setBundle(Bundle bundle) {
        this.mAppCode = bundle.getString(UpdatePreference.INTENT_KEY_APP_CODE);
        this.mTargetFile = bundle.getString(UpdatePreference.INTENT_KEY_TARGET_FILE);
        this.mTargetMd5 = bundle.getString(UpdatePreference.INTENT_KEY_TARGET_MD5);
        this.mTargetSize = bundle.getLong(UpdatePreference.INTENT_KEY_TARGET_SIZE, 0);
        this.mIsForcedInstall = bundle.getBoolean(UpdatePreference.INTENT_KEY_FORCE_INSTALL, false);
    }

    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if ((keyCode == 21 || keyCode == 22) && this.mHandler != null && this.mHandler.hasMessages(0)) {
            this.mHandler.removeMessages(0);
            this.mHandler.sendEmptyMessageDelayed(0, 6000);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void dismiss() {
        super.dismiss();
        Utils.utPageDisAppear(PAGENAME);
        this.mHandler.removeCallbacksAndMessages((Object) null);
    }

    /* access modifiers changed from: private */
    public void install() {
        try {
            File newAPK = new File(this.mTargetFile);
            newAPK.setReadable(true, false);
            if (newAPK.length() != this.mTargetSize || !this.mTargetMd5.equalsIgnoreCase(MD5Util.getMD5(newAPK))) {
                UserTrackUtil.onErrorEvent(2);
                ZpLogger.e("UpdateDialog", "UpdateDialog.install,invalid file, file size: " + newAPK.length() + " correct size: " + this.mTargetSize + " file md5: " + MD5Util.getMD5(newAPK) + " correct MD5: " + this.mTargetMd5);
                ZpLogger.d("UpdateDialog", "UpdateDialog.install,delete invalid file: " + newAPK.delete());
                finishAndStop();
            }
        } catch (Exception e) {
            UserTrackUtil.onErrorEvent(3);
            ZpLogger.e("UpdateDialog", "get md5 exception: " + e.getLocalizedMessage());
            finishAndStop();
        }
        ZpLogger.d("UpdateDialog", "UpdateDialog.install, MD5 check success, start to install new apk");
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags(268435456);
        intent.setDataAndType(Uri.parse("file://" + this.mTargetFile), "application/vnd.android.package-archive");
        try {
            this.mContext.startActivity(intent);
        } catch (Exception e2) {
            UserTrackUtil.onErrorEvent(3);
            ZpLogger.e("UpdateDialog", "UpdateDialog.install,PackageInstaller exception: " + e2.getLocalizedMessage());
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
        dismiss();
    }
}
