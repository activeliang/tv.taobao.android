package com.taobao.taobaoavsdk.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.taobao.adapter.INetworkUtilsAdapter;

public class TBAVNetworkUtils {
    public static final String NET_2G = "2G";
    public static final String NET_3G = "3G";
    public static final String NET_4G = "4G";
    public static final String NET_WIFI = "WIFI";

    public static String getNetworkType(INetworkUtilsAdapter adapter, Context context) {
        if (adapter != null && !TextUtils.isEmpty(adapter.getNetworkStutas())) {
            return adapter.getNetworkStutas();
        }
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return "";
        }
        if (networkInfo.getType() == 1) {
            return "WIFI";
        }
        if (networkInfo.getType() != 0) {
            return "";
        }
        String _strSubTypeName = networkInfo.getSubtypeName();
        switch (networkInfo.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return "2G";
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return "3G";
            case 13:
                return "4G";
            default:
                if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                    return "3G";
                }
                return _strSubTypeName;
        }
    }

    public static boolean isConnected(INetworkUtilsAdapter adapter, Context context) {
        if (adapter != null && !TextUtils.isEmpty(adapter.getNetworkStutas())) {
            return adapter.isConnected();
        }
        NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
