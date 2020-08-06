package com.ali.auth.third.offline.login.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import com.ali.auth.third.offline.R;
import com.ali.auth.third.offline.widget.AUProgressDialog;

public class DialogHelper {
    protected static final String TAG = "login.DialogHelper";
    /* access modifiers changed from: private */
    public Activity mActivity;
    /* access modifiers changed from: private */
    public AlertDialog mAlertDialog;
    /* access modifiers changed from: private */
    public AlertDialog mProgressDialog;
    /* access modifiers changed from: private */
    public Toast mToast;

    public DialogHelper(Activity activity) {
        this.mActivity = activity;
    }

    public void alert(String title, String msg, String positive, DialogInterface.OnClickListener positiveListener, String negative, DialogInterface.OnClickListener negativeListener) {
        alert(title, msg, positive, positiveListener, negative, negativeListener, false);
    }

    public void alert(String title, String msg, String positive, DialogInterface.OnClickListener positiveListener, String negative, DialogInterface.OnClickListener negativeListener, Boolean isCanceledOnTouchOutside) {
        dismissAlertDialog();
        final String str = title;
        final String str2 = msg;
        final String str3 = positive;
        final DialogInterface.OnClickListener onClickListener = positiveListener;
        final String str4 = negative;
        final DialogInterface.OnClickListener onClickListener2 = negativeListener;
        final Boolean bool = isCanceledOnTouchOutside;
        this.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (DialogHelper.this.mActivity != null && !DialogHelper.this.mActivity.isFinishing()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(DialogHelper.this.mActivity, 16973939));
                    if (!TextUtils.isEmpty(str)) {
                        builder.setTitle(str);
                    }
                    if (!TextUtils.isEmpty(str2)) {
                        builder.setMessage(str2);
                    }
                    if (!TextUtils.isEmpty(str3)) {
                        builder.setPositiveButton(str3, onClickListener);
                    }
                    if (!TextUtils.isEmpty(str4)) {
                        builder.setNegativeButton(str4, onClickListener2);
                    }
                    AlertDialog unused = DialogHelper.this.mAlertDialog = builder.show();
                    DialogHelper.this.mAlertDialog.setCanceledOnTouchOutside(bool.booleanValue());
                    DialogHelper.this.mAlertDialog.setCancelable(false);
                }
            }
        });
    }

    public void alertList(final String[] items, final DialogInterface.OnClickListener listener, final boolean isCanceledOnTouchOutside) {
        dismissAlertDialog();
        this.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (DialogHelper.this.mActivity != null && !DialogHelper.this.mActivity.isFinishing()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(DialogHelper.this.mActivity, 16973939));
                    builder.setItems(items, listener);
                    AlertDialog unused = DialogHelper.this.mAlertDialog = builder.show();
                    DialogHelper.this.mAlertDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
                    DialogHelper.this.mAlertDialog.setCancelable(isCanceledOnTouchOutside);
                }
            }
        });
    }

    public void toast(final String msg, final int period) {
        this.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    Toast unused = DialogHelper.this.mToast = new Toast(DialogHelper.this.mActivity);
                    View view = LayoutInflater.from(DialogHelper.this.mActivity.getApplicationContext()).inflate(R.layout.aliuser_transient_notification, (ViewGroup) null);
                    ((TextView) view.findViewById(16908299)).setText(msg);
                    DialogHelper.this.mToast.setView(view);
                    DialogHelper.this.mToast.setDuration(period);
                    DialogHelper.this.mToast.setGravity(17, 0, 0);
                    DialogHelper.this.mToast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void hideInputMethod() {
        if (this.mActivity != null && this.mActivity.getCurrentFocus() != null) {
            ((InputMethodManager) this.mActivity.getSystemService("input_method")).hideSoftInputFromWindow(this.mActivity.getCurrentFocus().getWindowToken(), 2);
        }
    }

    public void showProgressDialog(boolean showProgressBar, String msg) {
        showProgressDialog(msg, true, (DialogInterface.OnCancelListener) null, showProgressBar);
    }

    public void showProgressDialog(String msg) {
        showProgressDialog(msg, false, (DialogInterface.OnCancelListener) null, true);
    }

    public void showProgressDialog(String msg, boolean cancelable, DialogInterface.OnCancelListener cancelListener, boolean showProgressBar) {
        dismissProgressDialog();
        final String str = msg;
        final boolean z = cancelable;
        final boolean z2 = showProgressBar;
        final DialogInterface.OnCancelListener onCancelListener = cancelListener;
        this.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (DialogHelper.this.mActivity != null && !DialogHelper.this.mActivity.isFinishing()) {
                    AlertDialog unused = DialogHelper.this.mProgressDialog = new AUProgressDialog(DialogHelper.this.mActivity);
                    DialogHelper.this.mProgressDialog.setMessage(str);
                    DialogHelper.this.mProgressDialog.setCancelable(z);
                    ((AUProgressDialog) DialogHelper.this.mProgressDialog).setProgressVisiable(z2);
                    DialogHelper.this.mProgressDialog.setOnCancelListener(onCancelListener);
                    try {
                        DialogHelper.this.mProgressDialog.show();
                    } catch (Exception e) {
                    }
                    DialogHelper.this.mProgressDialog.setCanceledOnTouchOutside(false);
                }
            }
        });
    }

    public void dismissProgressDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    if (DialogHelper.this.mProgressDialog != null && DialogHelper.this.mProgressDialog.isShowing()) {
                        try {
                            DialogHelper.this.mProgressDialog.dismiss();
                        } catch (Exception e) {
                            Log.w(DialogHelper.TAG, "dismissProgressDialog", e);
                        } finally {
                            AlertDialog unused = DialogHelper.this.mProgressDialog = null;
                        }
                    }
                }
            });
        }
    }

    public void dismissAlertDialog() {
        if (this.mAlertDialog != null && this.mAlertDialog.isShowing()) {
            this.mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    if (DialogHelper.this.mAlertDialog != null && DialogHelper.this.mAlertDialog.isShowing()) {
                        try {
                            DialogHelper.this.mAlertDialog.dismiss();
                        } catch (Exception e) {
                            Log.w(DialogHelper.TAG, "dismissProgressDialog", e);
                        } finally {
                            AlertDialog unused = DialogHelper.this.mAlertDialog = null;
                        }
                    }
                }
            });
        }
    }
}
