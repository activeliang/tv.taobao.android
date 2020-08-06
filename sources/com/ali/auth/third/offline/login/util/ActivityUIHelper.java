package com.ali.auth.third.offline.login.util;

import android.app.Activity;
import android.content.DialogInterface;

public class ActivityUIHelper {
    public static final int PERIOD = 3000;
    static final String TAG = ActivityUIHelper.class.getSimpleName();
    private Activity mActivity;
    private DialogHelper mDialogHelper = new DialogHelper(this.mActivity);

    public ActivityUIHelper(Activity activity) {
        this.mActivity = activity;
    }

    public void hideInputMethod() {
        this.mDialogHelper.hideInputMethod();
    }

    public void finish() {
        this.mDialogHelper.dismissProgressDialog();
        this.mDialogHelper.dismissAlertDialog();
        this.mDialogHelper.hideInputMethod();
    }

    public void alert(String title, String msg, String positive, DialogInterface.OnClickListener positiveListener, String negative, DialogInterface.OnClickListener negativeListener) {
        this.mDialogHelper.alert(title, msg, positive, positiveListener, negative, negativeListener);
    }

    public void alert(String title, String msg, String positive, DialogInterface.OnClickListener positiveListener, String negative, DialogInterface.OnClickListener negativeListener, Boolean isCanceledOnTouchOutside) {
        this.mDialogHelper.alert(title, msg, positive, positiveListener, negative, negativeListener, isCanceledOnTouchOutside);
    }

    public void alertList(String[] items, DialogInterface.OnClickListener listener, boolean isCanceledOnTouchOutside) {
        this.mDialogHelper.alertList(items, listener, isCanceledOnTouchOutside);
    }

    public void toast(String msg, int period) {
        this.mDialogHelper.toast(msg, period);
    }

    public void showProgress(String msg) {
        this.mDialogHelper.showProgressDialog(msg);
    }

    public void showProgress(String msg, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        this.mDialogHelper.showProgressDialog(msg, cancelable, cancelListener, true);
    }

    public void dismissProgressDialog() {
        this.mDialogHelper.dismissProgressDialog();
    }

    public void dismissAlertDialog() {
        this.mDialogHelper.dismissAlertDialog();
    }
}
