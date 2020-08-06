package com.yunos.tvtaobao.payment;

import com.yunos.tv.blitz.global.BzApplication;

public class PaymentApplication extends BzApplication {
    public static boolean loginFragmentHasShowed = false;
    protected static PaymentApplication mApplication = null;

    public static PaymentApplication getApplication() {
        return mApplication;
    }

    public static boolean isLoginFragmentHasShowed() {
        return loginFragmentHasShowed;
    }

    public static void setLoginFragmentHasShowed(boolean loginFragmentHasShowed2) {
        loginFragmentHasShowed = loginFragmentHasShowed2;
    }

    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }
}
