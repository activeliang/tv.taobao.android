package com.yunos.tv.tvsdk.exception;

import android.content.Context;
import android.widget.Toast;

public class NoNetworkException extends BaseException {
    public static final String NETWORK_UNCONNECTED = "未连接网络，请检查网络设置";
    public static NoNetworkHanler mNoNetworkHanler = null;
    private static final long serialVersionUID = 3684196302788800707L;

    public interface NoNetworkHanler {
        boolean handle(Context context);
    }

    public NoNetworkException() {
    }

    public NoNetworkException(String message) {
        super(message);
    }

    public NoNetworkException(Throwable cause) {
        super(cause);
    }

    public NoNetworkException(int code, String detailMessage, Throwable throwable) {
        super(code, detailMessage, throwable);
    }

    public NoNetworkException(int code, String errorMessage) {
        super(code, errorMessage);
    }

    public NoNetworkException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public boolean handle(Context context) {
        if (mNoNetworkHanler != null) {
            return mNoNetworkHanler.handle(context);
        }
        Toast.makeText(context, "未连接网络，请检查网络设置", 0).show();
        return true;
    }

    public static void setNoNetworkHanler(NoNetworkHanler handler) {
        mNoNetworkHanler = handler;
    }
}
