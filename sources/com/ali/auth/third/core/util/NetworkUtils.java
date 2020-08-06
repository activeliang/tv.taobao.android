package com.ali.auth.third.core.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkUtils {
    public static MobileNetworkType getMobileNetworkType(Context context) {
        NetworkInfo activeNetInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetInfo == null || activeNetInfo.getType() != 0 || !activeNetInfo.isConnected()) {
            return MobileNetworkType.MOBILE_NETWORK_TYPE_UNKNOWN;
        }
        return getNetWorkType(activeNetInfo.getSubtype());
    }

    private static MobileNetworkType getNetWorkType(int networkType) {
        switch (networkType) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return MobileNetworkType.MOBILE_NETWORK_TYPE_2G;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return MobileNetworkType.MOBILE_NETWORK_TYPE_3G;
            case 13:
                return MobileNetworkType.MOBILE_NETWORK_TYPE_4G;
            default:
                return MobileNetworkType.MOBILE_NETWORK_TYPE_UNKNOWN;
        }
    }

    public static ConnectType getConnectType(Context context) {
        NetworkInfo activeNetInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isConnected()) {
            return ConnectType.CONNECT_TYPE_DISCONNECT;
        }
        switch (activeNetInfo.getType()) {
            case 0:
                return ConnectType.CONNECT_TYPE_MOBILE;
            case 1:
                return ConnectType.CONNECT_TYPE_WIFI;
            default:
                return ConnectType.CONNECT_TYPE_OTHER;
        }
    }

    public static boolean isNetworkAvaiable(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null || !info.isAvailable() || !info.isConnected()) {
            return false;
        }
        return true;
    }

    public static String getLocalMacAddress(Context context) {
        WifiInfo info;
        WifiManager wifi = (WifiManager) context.getSystemService("wifi");
        if (wifi == null || (info = wifi.getConnectionInfo()) == null) {
            return null;
        }
        return info.getMacAddress();
    }
}
