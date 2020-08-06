package com.yunos.tvtaobao.biz.listener;

import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tvtaobao.biz.activity.BaseActivity;
import com.yunos.tvtaobao.biz.request.core.ServiceCode;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;

public abstract class BizRequestListener<T> implements RequestListener.RequestListenerWithLogin<T> {
    /* access modifiers changed from: private */
    public final String TAG = BizRequestListener.class.getSimpleName();
    protected final WeakReference<BaseActivity> mBaseActivityRef;
    private RequestErrorListener mRequestErrorListener;

    public abstract boolean ifFinishWhenCloseErrorDialog();

    public abstract boolean onError(int i, String str);

    public abstract void onSuccess(T t);

    public BizRequestListener(WeakReference<BaseActivity> baseActivityRef) {
        this.mBaseActivityRef = baseActivityRef;
    }

    public void onStartLogin() {
        if (this.mBaseActivityRef != null && this.mBaseActivityRef.get() != null) {
            ((BaseActivity) this.mBaseActivityRef.get()).setLoginActivityStartShowing();
        }
    }

    public void onRequestDone(T data, int resultCode, String msg) {
        ZpLogger.e(this.TAG, "data : " + data + " resultCode : " + resultCode + " msg : " + msg);
        if (this.mBaseActivityRef.get() == null || ((BaseActivity) this.mBaseActivityRef.get()).isFinishing()) {
            return;
        }
        if (resultCode == 200) {
            onSuccess(data);
            return;
        }
        initErrorListener(resultCode);
        if (!onError(resultCode, msg) && this.mRequestErrorListener != null) {
            this.mRequestErrorListener.onError(resultCode, msg);
        }
    }

    public boolean isShowErrorDialog() {
        return true;
    }

    private void initErrorListener(int resultCode) {
        if (resultCode == 1) {
            this.mRequestErrorListener = new RequestErrorListener() {
                public boolean onError(int errorCode, String errorMsg) {
                    BaseActivity baseActivity = (BaseActivity) BizRequestListener.this.mBaseActivityRef.get();
                    if (baseActivity == null || baseActivity.isFinishing()) {
                        return true;
                    }
                    baseActivity.showNetworkErrorDialog(BizRequestListener.this.ifFinishWhenCloseErrorDialog());
                    return true;
                }
            };
        } else if (resultCode == ServiceCode.CLIENT_LOGIN_ERROR.getCode()) {
            this.mRequestErrorListener = null;
        } else if (resultCode == ServiceCode.API_NOT_LOGIN.getCode() || resultCode == ServiceCode.API_SID_INVALID.getCode()) {
            this.mRequestErrorListener = new RequestErrorListener() {
                public boolean onError(int errorCode, String errorMsg) {
                    BaseActivity baseActivity = (BaseActivity) BizRequestListener.this.mBaseActivityRef.get();
                    if (baseActivity != null) {
                        try {
                            boolean loginStatus = CoreApplication.getLoginHelper(CoreApplication.getApplication()).isLogin();
                            if (loginStatus) {
                                baseActivity.setCurrLoginInvalid();
                                baseActivity.startLoginActivity(baseActivity.getApplicationInfo().packageName, true);
                                ZpLogger.i(BizRequestListener.this.TAG, "startLoginActivity loginStatus = 200,forceLogin=true");
                            } else {
                                baseActivity.setCurrLoginInvalid();
                                baseActivity.setLoginActivityStartShowing();
                                baseActivity.startLoginActivity(baseActivity.getApplicationInfo().packageName, false);
                                ZpLogger.i(BizRequestListener.this.TAG, "startLoginActivity loginStatus = " + loginStatus + ",forceLogin=true");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }
            };
        } else {
            this.mRequestErrorListener = new RequestErrorListener() {
                public boolean onError(int errorCode, String errorMsg) {
                    BaseActivity baseActivity = (BaseActivity) BizRequestListener.this.mBaseActivityRef.get();
                    if (baseActivity == null || baseActivity.isFinishing() || !BizRequestListener.this.isShowErrorDialog()) {
                        return true;
                    }
                    baseActivity.showErrorDialog(errorMsg, BizRequestListener.this.ifFinishWhenCloseErrorDialog());
                    return true;
                }
            };
        }
    }
}
