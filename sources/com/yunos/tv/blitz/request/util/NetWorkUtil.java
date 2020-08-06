package com.yunos.tv.blitz.request.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.yunos.tv.blitz.global.BzAppConfig;

public class NetWorkUtil {
    public static int getNetWorkType() {
        if (!isNetWorkAvailable()) {
            return -1;
        }
        if (((ConnectivityManager) BzAppConfig.context.getContext().getSystemService("connectivity")).getNetworkInfo(9).isConnected()) {
            return 9;
        }
        if (isWifi()) {
            return 1;
        }
        return -1;
    }

    public static boolean isNetWorkAvailable() {
        NetworkInfo info;
        try {
            ConnectivityManager cm = getConnectivityManager();
            if (cm == null || (info = cm.getActiveNetworkInfo()) == null) {
                return false;
            }
            return info.isAvailable();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isWifi() {
        NetworkInfo ni;
        ConnectivityManager cm = getConnectivityManager();
        if (cm == null || (ni = cm.getActiveNetworkInfo()) == null || !ni.getTypeName().equals("WIFI")) {
            return false;
        }
        return true;
    }

    private static ConnectivityManager getConnectivityManager() {
        try {
            return (ConnectivityManager) BzAppConfig.context.getContext().getSystemService("connectivity");
        } catch (Exception e) {
            return null;
        }
    }
}
