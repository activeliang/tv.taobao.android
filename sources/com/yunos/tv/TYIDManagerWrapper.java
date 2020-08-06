package com.yunos.tv;

import android.content.Context;

public class TYIDManagerWrapper {
    private static TYIDManagerWrapper instance;
    private static int status = 0;

    public interface IServiceConnectStatus {
        void onServiceConnectStatus(int i);
    }

    public static TYIDManagerWrapper get(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null!");
        }
        synchronized (TYIDManagerWrapper.class) {
            if (instance == null) {
                instance = new TYIDManagerWrapper(context);
            }
        }
        return instance;
    }

    public static TYIDManagerWrapper get(Context context, IServiceConnectStatus callback) {
        if (context == null) {
            throw new IllegalArgumentException("context is null!");
        }
        synchronized (TYIDManagerWrapper.class) {
            if (instance == null) {
                instance = new TYIDManagerWrapper(context, callback);
            } else {
                callback.onServiceConnectStatus(status);
            }
        }
        return instance;
    }

    private TYIDManagerWrapper(Context context) {
    }

    private TYIDManagerWrapper(Context context, IServiceConnectStatus callback) {
    }

    public String peekToken(String param) {
        return null;
    }
}
