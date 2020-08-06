package com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo;

import android.content.Context;
import android.content.DialogInterface;

public class DialogParams {
    private boolean cancelable = true;
    private Context context;
    private String msg;
    private Integer msgResId;
    private boolean negativeButtonClickDismiss = true;
    private DialogInterface.OnClickListener negativeButtonListener;
    private String negativeButtonText;
    private Integer negativeButtonTextResId;
    private DialogInterface.OnKeyListener onKeyListener;
    private boolean positiveButtonClickDismiss = true;
    private DialogInterface.OnClickListener positiveButtonListener;
    private String positiveButtonText;
    private Integer positiveButtonTextResId;

    public static DialogParams makeParams(Context context2) {
        return new DialogParams(context2);
    }

    public DialogParams(Context context2) {
        this.context = context2;
    }

    public Context getContext() {
        return this.context;
    }

    public String getMsg() {
        return this.msg;
    }

    public DialogParams setMsg(String msg2) {
        this.msg = msg2;
        return this;
    }

    public Integer getMsgResId() {
        return this.msgResId;
    }

    public DialogParams setMsgResId(Integer msgResId2) {
        this.msgResId = msgResId2;
        return this;
    }

    public String getPositiveButtonText() {
        return this.positiveButtonText;
    }

    public DialogParams setPositiveButtonText(String positiveButtonText2) {
        this.positiveButtonText = positiveButtonText2;
        return this;
    }

    public Integer getPositiveButtonTextResId() {
        return this.positiveButtonTextResId;
    }

    public DialogParams setPositiveButtonTextResId(Integer positiveButtonTextResId2) {
        this.positiveButtonTextResId = positiveButtonTextResId2;
        return this;
    }

    public DialogInterface.OnClickListener getPositiveButtonListener() {
        return this.positiveButtonListener;
    }

    public DialogParams setPositiveButtonListener(DialogInterface.OnClickListener positiveButtonListener2) {
        this.positiveButtonListener = positiveButtonListener2;
        return this;
    }

    public String getNegativeButtonText() {
        return this.negativeButtonText;
    }

    public DialogParams setNegativeButtonText(String negativeButtonText2) {
        this.negativeButtonText = negativeButtonText2;
        return this;
    }

    public Integer getNegativeButtonTextResId() {
        return this.negativeButtonTextResId;
    }

    public DialogParams setNegativeButtonTextResId(Integer negativeButtonTextResId2) {
        this.negativeButtonTextResId = negativeButtonTextResId2;
        return this;
    }

    public DialogInterface.OnClickListener getNegativeButtonListener() {
        return this.negativeButtonListener;
    }

    public DialogParams setNegativeButtonListener(DialogInterface.OnClickListener negativeButtonListener2) {
        this.negativeButtonListener = negativeButtonListener2;
        return this;
    }

    public DialogInterface.OnKeyListener getOnKeyListener() {
        return this.onKeyListener;
    }

    public DialogParams setOnKeyListener(DialogInterface.OnKeyListener onKeyListener2) {
        this.onKeyListener = onKeyListener2;
        return this;
    }

    public Boolean getCancelable() {
        return Boolean.valueOf(this.cancelable);
    }

    public DialogParams setCancelable(Boolean cancelable2) {
        this.cancelable = cancelable2.booleanValue();
        return this;
    }

    public boolean isPositiveButtonClickDismiss() {
        return this.positiveButtonClickDismiss;
    }

    public DialogParams setPositiveButtonClickDismiss(boolean positiveButtonClickDismiss2) {
        this.positiveButtonClickDismiss = positiveButtonClickDismiss2;
        return this;
    }

    public boolean isNegativeButtonClickDismiss() {
        return this.negativeButtonClickDismiss;
    }

    public DialogParams setNegativeButtonClickDismiss(boolean negativeButtonClickDismiss2) {
        this.negativeButtonClickDismiss = negativeButtonClickDismiss2;
        return this;
    }
}
