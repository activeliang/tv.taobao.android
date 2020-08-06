package com.yunos.tvtaobao.blitz.account;

import android.app.Activity;
import android.text.TextUtils;
import com.yunos.tv.blitz.account.LoginHelper;
import com.yunos.tv.core.CoreApplication;
import com.zhiping.dev.android.logger.ZpLogger;

public class AccountActivityHelper {
    private final String TAG = "AccountActivityHelper";
    private AccountActivityState mAccountActivityState = AccountActivityState.HIDED;
    private AccountLoginState mAccountLoginState = AccountLoginState.UNKNOWN;
    /* access modifiers changed from: private */
    public boolean mCurrLoginInvalid;
    private LoginHelper mLoginHelper;
    private LoginHelper.SyncLoginListener mLoginLister;
    private OnAccountStateChangedListener mOnAccountStateChangedListener;

    private enum AccountActivityState {
        START_SHOWING,
        SHOWED,
        HIDED
    }

    public enum AccountLoginState {
        UNKNOWN,
        LOGIN,
        LOGOUT,
        CANCEL
    }

    public interface OnAccountStateChangedListener {
        void onAccountStateChanged(AccountLoginState accountLoginState);
    }

    public void setAccountActivityHide() {
        ZpLogger.i("AccountActivityHelper", "setAccountActivityHide mAccountActivityState=" + this.mAccountActivityState);
        if (this.mAccountActivityState.compareTo(AccountActivityState.SHOWED) == 0) {
            this.mAccountActivityState = AccountActivityState.HIDED;
            if (!checkLoginState()) {
                onStateChanged(AccountLoginState.CANCEL);
            }
        }
    }

    public void setAccountActivityShowed() {
        ZpLogger.i("AccountActivityHelper", "setAccountActivityShowed mAccountActivityState=" + this.mAccountActivityState);
        if (this.mAccountActivityState.compareTo(AccountActivityState.START_SHOWING) == 0) {
            this.mAccountActivityState = AccountActivityState.SHOWED;
        }
    }

    public void setAccountActivityStartShowing() {
        boolean digital;
        boolean compareTo;
        ZpLogger.i("AccountActivityHelper", "setAccountActivityStartShowing mAccountActivityState" + this.mAccountActivityState);
        this.mAccountLoginState = AccountLoginState.LOGOUT;
        this.mAccountActivityState = AccountActivityState.START_SHOWING;
        if (this.mAccountLoginState == AccountLoginState.LOGOUT) {
            digital = true;
        } else {
            digital = false;
        }
        boolean equal = this.mAccountLoginState.equals(AccountLoginState.LOGOUT);
        if (this.mAccountLoginState.compareTo(AccountLoginState.LOGOUT) == 0) {
            compareTo = true;
        } else {
            compareTo = false;
        }
        ZpLogger.i("AccountActivityHelper", "test mAccountLoginState=" + this.mAccountLoginState + " digital=" + digital + " equal=" + equal + " compareTo=" + compareTo);
    }

    public void setAccountInvalid() {
        ZpLogger.i("AccountActivityHelper", "setAccountInvalid");
        ZpLogger.i("lihaile------------", "设置合法");
        this.mAccountLoginState = AccountLoginState.LOGOUT;
        this.mCurrLoginInvalid = true;
    }

    public void startAccountActivity(Activity activity, String from, boolean forceLogin) {
        ZpLogger.i("AccountActivityHelper", "startLoginActivity");
        setAccountActivityStartShowing();
        boolean ifChangeAccount = true;
        if (!TextUtils.isEmpty(from) || !forceLogin) {
            ifChangeAccount = false;
        }
        CoreApplication.getLoginHelper(CoreApplication.getApplication()).startYunosAccountActivity(activity, ifChangeAccount);
    }

    public void startAccountActivity(String from, boolean forceLogin) {
        ZpLogger.i("AccountActivityHelper", "startLoginActivity");
        setAccountActivityStartShowing();
        boolean ifChangeAccount = true;
        if (!TextUtils.isEmpty(from) || !forceLogin) {
            ifChangeAccount = false;
        }
        CoreApplication.getLoginHelper(CoreApplication.getApplication()).startYunosAccountActivity(CoreApplication.getApplication(), ifChangeAccount);
    }

    public boolean loginIsCanceled() {
        ZpLogger.i("AccountActivityHelper", "loginIsCanceled mAccountLoginState=" + this.mAccountLoginState);
        if (this.mAccountLoginState.compareTo(AccountLoginState.CANCEL) == 0) {
            return true;
        }
        return false;
    }

    public void registerAccountActivity(OnAccountStateChangedListener listener) {
        this.mOnAccountStateChangedListener = listener;
        registerLoginListener();
    }

    public void unRegisterAccountActivity(OnAccountStateChangedListener listener) {
        this.mOnAccountStateChangedListener = null;
        unRegisterLoginListener();
    }

    private void registerLoginListener() {
        this.mLoginHelper = CoreApplication.getLoginHelper(CoreApplication.getApplication());
        if (this.mLoginLister == null) {
            this.mLoginLister = new LoginHelper.SyncLoginListener() {
                public void onLogin(boolean isSuccess) {
                    ZpLogger.i("AccountActivityHelper", "onLogin isSuccess=" + isSuccess);
                    boolean unused = AccountActivityHelper.this.mCurrLoginInvalid = false;
                    if (isSuccess) {
                        AccountActivityHelper.this.onStateChanged(AccountLoginState.LOGIN);
                    } else {
                        AccountActivityHelper.this.onStateChanged(AccountLoginState.LOGOUT);
                    }
                }
            };
            if (this.mLoginHelper != null) {
                this.mLoginHelper.addReceiveLoginListener(this.mLoginLister);
            }
        }
    }

    private void unRegisterLoginListener() {
        if (this.mLoginLister != null && this.mLoginHelper != null) {
            this.mLoginHelper.removeReceiveLoginListener(this.mLoginLister);
            this.mLoginLister = null;
            this.mLoginHelper = null;
        }
    }

    private boolean checkLoginState() {
        ZpLogger.i("AccountActivityHelper", "checkLoginState mCurrLoginInvalid=" + this.mCurrLoginInvalid);
        if (this.mCurrLoginInvalid) {
            return false;
        }
        return CoreApplication.getLoginHelper(CoreApplication.getApplication()).isLogin();
    }

    /* access modifiers changed from: private */
    public void onStateChanged(AccountLoginState state) {
        ZpLogger.i("AccountActivityHelper", "onStateChanged state=" + state);
        this.mAccountLoginState = state;
        if (this.mOnAccountStateChangedListener != null) {
            this.mOnAccountStateChangedListener.onAccountStateChanged(state);
        }
    }
}
