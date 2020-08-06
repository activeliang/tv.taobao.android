package com.yunos.tv.core.listener;

public class TvBaoELemeBindGuideListener {
    private static IElemBindCallBack eLemeBindCallBack;

    public interface IElemBindCallBack {
        void onFailure();

        void onSuccess();
    }

    public static void seteLemeBindCallBack(IElemBindCallBack listener) {
        eLemeBindCallBack = listener;
    }

    public static void onSuccess() {
        if (eLemeBindCallBack != null) {
            eLemeBindCallBack.onSuccess();
            eLemeBindCallBack = null;
        }
    }
}
