package com.ali.user.open.core.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import com.ali.user.open.core.R;

public class DialogHelper {
    private static volatile DialogHelper instance;
    /* access modifiers changed from: private */
    public AlertDialog mAlertDialog;

    public static DialogHelper getInstance() {
        if (instance == null) {
            synchronized (DialogHelper.class) {
                if (instance == null) {
                    instance = new DialogHelper();
                }
            }
        }
        return instance;
    }

    public void alert(Activity activity, String title, String msg, String positive, DialogInterface.OnClickListener positiveListener, String negative, DialogInterface.OnClickListener negativeListener) {
        if (activity != null) {
            dismissAlertDialog(activity);
            final Activity activity2 = activity;
            final String str = title;
            final String str2 = msg;
            final String str3 = positive;
            final DialogInterface.OnClickListener onClickListener = positiveListener;
            final String str4 = negative;
            final DialogInterface.OnClickListener onClickListener2 = negativeListener;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (activity2 != null && !activity2.isFinishing()) {
                        int theme = 16973939;
                        if (Build.VERSION.SDK_INT >= 21) {
                            theme = 16974393;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity2, theme));
                        if (!TextUtils.isEmpty(str)) {
                            builder.setTitle(str);
                        }
                        if (!TextUtils.isEmpty(str2)) {
                            builder.setMessage(str2);
                        } else {
                            builder.setMessage(activity2.getString(R.string.member_sdk_network_not_available_message));
                        }
                        if (!TextUtils.isEmpty(str3)) {
                            builder.setPositiveButton(str3, onClickListener);
                        }
                        if (!TextUtils.isEmpty(str4)) {
                            builder.setNegativeButton(str4, onClickListener2);
                        }
                        try {
                            AlertDialog unused = DialogHelper.this.mAlertDialog = builder.show();
                            DialogHelper.this.mAlertDialog.getButton(-1).setTextColor(activity2.getResources().getColor(17170444));
                            DialogHelper.this.mAlertDialog.getButton(-2).setTextColor(activity2.getResources().getColor(17170444));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void dismissAlertDialog(Activity activity) {
        if (this.mAlertDialog != null && this.mAlertDialog.isShowing()) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (DialogHelper.this.mAlertDialog != null && DialogHelper.this.mAlertDialog.isShowing()) {
                        try {
                            DialogHelper.this.mAlertDialog.dismiss();
                        } catch (Exception e) {
                        } finally {
                            AlertDialog unused = DialogHelper.this.mAlertDialog = null;
                        }
                    }
                }
            });
        }
    }
}
