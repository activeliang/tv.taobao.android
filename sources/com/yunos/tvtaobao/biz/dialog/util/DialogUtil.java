package com.yunos.tvtaobao.biz.dialog.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import com.yunos.tvtaobao.biz.dialog.TextProgressDialog;
import com.yunos.tvtaobao.biz.dialog.TvTaoBaoDialog;
import com.yunos.tvtaobao.biz.dialog.WaitProgressDialog;
import com.yunos.tvtaobao.businessview.R;
import com.yunos.tvtaobao.payment.utils.ChannelUtils;
import com.zhiping.dev.android.logger.ZpLogger;

public class DialogUtil {
    private final String TAG = "DialogUtil";
    /* access modifiers changed from: private */
    public boolean mCloseActivityOfNetworkDialog;
    protected final Context mContext;
    private boolean mFinished;
    protected TvTaoBaoDialog mNetworkDialog;
    protected TvTaoBaoDialog mNormalErrorDialog;
    protected TextProgressDialog mTextProgressDialog;
    protected WaitProgressDialog mWaitProgressDialog;

    public DialogUtil(Context context) {
        this.mContext = context;
        this.mCloseActivityOfNetworkDialog = false;
    }

    public TvTaoBaoDialog getmNetworkDialog() {
        return this.mNetworkDialog;
    }

    public TvTaoBaoDialog getmNormalErrorDialog() {
        return this.mNormalErrorDialog;
    }

    public WaitProgressDialog getmWaitProgressDialog() {
        return this.mWaitProgressDialog;
    }

    public TextProgressDialog getTextProgressDialog() {
        return this.mTextProgressDialog;
    }

    public void networkDialogDismiss() {
        if (this.mNetworkDialog != null) {
            this.mNetworkDialog.dismiss();
        }
    }

    public void normalDialogDismiss() {
        if (this.mNormalErrorDialog != null) {
            this.mNormalErrorDialog.dismiss();
            this.mNormalErrorDialog = null;
        }
    }

    public void onDestroy() {
        this.mFinished = true;
        if (this.mNetworkDialog != null) {
            this.mNetworkDialog.dismiss();
            this.mNetworkDialog = null;
        }
        if (this.mNormalErrorDialog != null) {
            this.mNormalErrorDialog.dismiss();
            this.mNormalErrorDialog = null;
        }
        if (this.mWaitProgressDialog != null) {
            this.mWaitProgressDialog.dismiss();
            this.mWaitProgressDialog = null;
        }
        if (this.mTextProgressDialog != null) {
            this.mTextProgressDialog.dismiss();
            this.mTextProgressDialog = null;
        }
    }

    public boolean isFinished() {
        return this.mFinished;
    }

    private void creatWaitProgressDialog() {
        this.mWaitProgressDialog = new WaitProgressDialog(this.mContext);
        if (ChannelUtils.isThisChannel(ChannelUtils.KKA)) {
            this.mWaitProgressDialog.setCanceledOnTouchOutside(true);
            Window window = this.mWaitProgressDialog.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            window.setFlags(8, 8);
        }
    }

    private void creatTextProgressDialog() {
        this.mTextProgressDialog = new TextProgressDialog(this.mContext);
    }

    private void onCreatSetNetworkDialog() {
        if (this.mNetworkDialog == null) {
            this.mNetworkDialog = new TvTaoBaoDialog.Builder(this.mContext).setMessage(this.mContext.getString(R.string.ytbv_network_error_goto_set)).setPositiveButton(this.mContext.getString(R.string.ytbv_setting), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
                    com.zhiping.dev.android.logger.ZpLogger.i("DialogUtil", "onCreatSetNetworkDialog --> Exception --> e = " + r1.toString());
                    com.yunos.tv.core.util.Utils.startNetWorkSettingActivity(r5.this$0.mContext, r5.this$0.mContext.getString(com.yunos.tvtaobao.businessview.R.string.ytbv_open_setting_activity_error));
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:13:0x0061, code lost:
                    r1 = move-exception;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:14:0x0062, code lost:
                    r1.printStackTrace();
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
                    return;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
                    return;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:9:0x0030, code lost:
                    r1 = move-exception;
                 */
                /* JADX WARNING: Failed to process nested try/catch */
                /* JADX WARNING: Removed duplicated region for block: B:13:0x0061 A[ExcHandler: ClassCastException (r1v1 'e' java.lang.ClassCastException A[CUSTOM_DECLARE]), Splitter:B:1:0x0003] */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void onClick(android.content.DialogInterface r6, int r7) {
                    /*
                        r5 = this;
                        java.lang.String r2 = "DialogUtil"
                        java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        r3.<init>()     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        java.lang.String r4 = "DialogUtil.onCreatSetNetworkDialog.onClick.SYSTEM_YUNOS_4_0 = "
                        java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        boolean r4 = com.yunos.tv.core.config.SystemConfig.SYSTEM_YUNOS_4_0     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        java.lang.String r3 = r3.toString()     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        com.zhiping.dev.android.logger.ZpLogger.v(r2, r3)     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        boolean r2 = com.yunos.tv.core.config.SystemConfig.SYSTEM_YUNOS_4_0     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        if (r2 == 0) goto L_0x0075
                        com.yunos.tvtaobao.biz.dialog.util.DialogUtil r2 = com.yunos.tvtaobao.biz.dialog.util.DialogUtil.this     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        android.content.Context r0 = r2.mContext     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        com.yunos.tvtaobao.biz.activity.BaseActivity r0 = (com.yunos.tvtaobao.biz.activity.BaseActivity) r0     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        if (r0 == 0) goto L_0x0066
                        r2 = 0
                        java.lang.String r3 = "page://settingrelease.tv.yunos.com/settingrelease"
                        r0.showYunosHostPage(r2, r3)     // Catch:{ Exception -> 0x0030, ClassCastException -> 0x0061 }
                    L_0x002f:
                        return
                    L_0x0030:
                        r1 = move-exception
                        java.lang.String r2 = "DialogUtil"
                        java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        r3.<init>()     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        java.lang.String r4 = "onCreatSetNetworkDialog --> Exception --> e = "
                        java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        java.lang.String r4 = r1.toString()     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        java.lang.String r3 = r3.toString()     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        com.yunos.tvtaobao.biz.dialog.util.DialogUtil r2 = com.yunos.tvtaobao.biz.dialog.util.DialogUtil.this     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        android.content.Context r2 = r2.mContext     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        com.yunos.tvtaobao.biz.dialog.util.DialogUtil r3 = com.yunos.tvtaobao.biz.dialog.util.DialogUtil.this     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        android.content.Context r3 = r3.mContext     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        int r4 = com.yunos.tvtaobao.businessview.R.string.ytbv_open_setting_activity_error     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        java.lang.String r3 = r3.getString(r4)     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        com.yunos.tv.core.util.Utils.startNetWorkSettingActivity(r2, r3)     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        goto L_0x002f
                    L_0x0061:
                        r1 = move-exception
                        r1.printStackTrace()
                        goto L_0x002f
                    L_0x0066:
                        java.lang.String r2 = "DialogUtil"
                        java.lang.String r3 = "DialogUtil.onCreatSetNetworkDialog.onClick.baseActivity == null"
                        com.zhiping.dev.android.logger.ZpLogger.e(r2, r3)     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        goto L_0x002f
                    L_0x0070:
                        r1 = move-exception
                        r1.printStackTrace()
                        goto L_0x002f
                    L_0x0075:
                        com.yunos.tvtaobao.biz.dialog.util.DialogUtil r2 = com.yunos.tvtaobao.biz.dialog.util.DialogUtil.this     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        android.content.Context r2 = r2.mContext     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        com.yunos.tvtaobao.biz.dialog.util.DialogUtil r3 = com.yunos.tvtaobao.biz.dialog.util.DialogUtil.this     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        android.content.Context r3 = r3.mContext     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        int r4 = com.yunos.tvtaobao.businessview.R.string.ytbv_open_setting_activity_error     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        java.lang.String r3 = r3.getString(r4)     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        com.yunos.tv.core.util.Utils.startNetWorkSettingActivity(r2, r3)     // Catch:{ ClassCastException -> 0x0061, Exception -> 0x0070 }
                        goto L_0x002f
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.biz.dialog.util.DialogUtil.AnonymousClass1.onClick(android.content.DialogInterface, int):void");
                }
            }).create();
            this.mNetworkDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode != 4 || event.getAction() != 0) {
                        return false;
                    }
                    dialog.dismiss();
                    if (DialogUtil.this.mCloseActivityOfNetworkDialog && DialogUtil.this.mContext != null && (DialogUtil.this.mContext instanceof Activity)) {
                        ((Activity) DialogUtil.this.mContext).finish();
                    }
                    return true;
                }
            });
        }
    }

    public void showNetworkErrorDialog(boolean isFinishActivity) {
        ZpLogger.d("DialogUtil", " showNetworkErrorDialog;   ; mNetworkDialog = " + this.mNetworkDialog);
        if (!((Activity) this.mContext).isFinishing()) {
            if (this.mNormalErrorDialog != null) {
                this.mNormalErrorDialog.dismiss();
            }
            if (this.mNetworkDialog == null) {
                onCreatSetNetworkDialog();
            }
            this.mCloseActivityOfNetworkDialog = isFinishActivity;
            if (this.mNetworkDialog.isShowing()) {
                this.mNetworkDialog.dismiss();
            }
            this.mNetworkDialog.show();
        }
    }

    public void showErrorDialog(String msg) {
        ZpLogger.d("DialogUtil", " showErrorDialog;  msg =  " + msg);
        showErrorDialog(msg, this.mContext.getString(R.string.ytbv_confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DialogUtil.this.mNormalErrorDialog.dismiss();
            }
        }, (DialogInterface.OnKeyListener) null);
    }

    public void showErrorDialog(String msg, DialogInterface.OnClickListener listener, DialogInterface.OnKeyListener onKeyListener) {
        showErrorDialog(msg, this.mContext.getString(R.string.ytbv_confirm), listener, onKeyListener);
    }

    public void showErrorDialog(String msg, String firstButtonTitle, DialogInterface.OnClickListener firstButtonOnClickListener, DialogInterface.OnKeyListener onKeyListener) {
        ZpLogger.d("DialogUtil", "showErrorDialog;  msg =  " + msg + "; firstButtonTitle = " + firstButtonTitle);
        if (!TextUtils.isEmpty(msg)) {
            if (this.mNetworkDialog != null) {
                this.mNetworkDialog.dismiss();
            }
            if (this.mNormalErrorDialog != null && this.mNormalErrorDialog.isShowing()) {
                this.mNormalErrorDialog.dismiss();
                this.mNormalErrorDialog = null;
            }
            Activity mActivity = (Activity) this.mContext;
            if (!mActivity.isFinishing()) {
                this.mNormalErrorDialog = new TvTaoBaoDialog.Builder(this.mContext).setMessage(msg).setPositiveButton(firstButtonTitle, firstButtonOnClickListener).create();
                if (onKeyListener == null) {
                    onKeyListener = new DialogInterface.OnKeyListener() {
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode != 4) {
                                return false;
                            }
                            dialog.dismiss();
                            return true;
                        }
                    };
                }
                this.mNormalErrorDialog.setOnKeyListener(onKeyListener);
                if (!mActivity.isFinishing()) {
                    this.mNormalErrorDialog.show();
                }
            }
        }
    }

    public void OnWaitProgressDialog(boolean show) {
        try {
            ZpLogger.d("DialogUtil", "OnWaitProgressDialog;  show =  " + show + "; this = " + this);
            Activity mActivity = (Activity) this.mContext;
            if (Build.VERSION.SDK_INT >= 17) {
                if (mActivity.isFinishing() || this.mFinished || mActivity.isDestroyed()) {
                    this.mFinished = false;
                    return;
                }
            } else if (mActivity.isFinishing() || this.mFinished) {
                this.mFinished = false;
                return;
            }
            if (this.mWaitProgressDialog == null) {
                creatWaitProgressDialog();
            }
            if (show && this.mWaitProgressDialog.isShowing()) {
                return;
            }
            if (!show && !this.mWaitProgressDialog.isShowing()) {
                return;
            }
            if (show) {
                this.mWaitProgressDialog.show();
            } else {
                this.mWaitProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onTextProgressDialog(CharSequence text, boolean show) {
        ZpLogger.d("DialogUtil", "onTextProgressDialog;  show =  " + show + "; this = " + this);
        Activity mActivity = (Activity) this.mContext;
        if (Build.VERSION.SDK_INT >= 17) {
            if (mActivity.isFinishing() || this.mFinished || mActivity.isDestroyed()) {
                this.mFinished = false;
                return;
            }
        } else if (mActivity.isFinishing() || this.mFinished) {
            this.mFinished = false;
            return;
        }
        if (this.mTextProgressDialog == null) {
            creatTextProgressDialog();
        }
        if (show && this.mTextProgressDialog.isShowing()) {
            this.mTextProgressDialog.setText(text);
        } else if (!show && !this.mTextProgressDialog.isShowing()) {
        } else {
            if (show) {
                this.mTextProgressDialog.setText(text);
                this.mTextProgressDialog.show();
                return;
            }
            this.mTextProgressDialog.dismiss();
        }
    }

    public void setProgressCancelable(boolean flag) {
        ZpLogger.d("DialogUtil", "setProgressCancelable;  flag =  " + flag + "; this = " + this);
        if (this.mWaitProgressDialog != null) {
            this.mWaitProgressDialog.setCancelable(flag);
        }
        if (this.mTextProgressDialog != null) {
            this.mTextProgressDialog.setCancelable(flag);
        }
    }
}
