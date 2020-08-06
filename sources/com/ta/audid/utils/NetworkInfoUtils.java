package com.ta.audid.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkInfoUtils {
    public static final String NETWORK_CLASS_2_G = "2G";
    public static final String NETWORK_CLASS_3_G = "3G";
    public static final String NETWORK_CLASS_4_G = "4G";
    public static final String NETWORK_CLASS_UNKNOWN = "Unknown";
    public static final String NETWORK_CLASS_WIFI = "Wi-Fi";
    private static String[] arrayOfString = {"Unknown", "Unknown"};

    public static String getNetworkType(Context context) {
        if (context == null) {
            return "Unknown";
        }
        try {
            if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) != 0) {
                return "Unknown";
            }
            NetworkInfo nInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (nInfo == null) {
                return "Unknown";
            }
            if (nInfo.isConnected()) {
                if (nInfo.getType() == 1) {
                    return "Wi-Fi";
                }
                if (nInfo.getType() == 0) {
                    return getNetworkClass(nInfo.getSubtype());
                }
            }
            return "Unknown";
        } catch (Throwable th) {
        }
    }

    private static String getNetworkClass(int networkType) {
        switch (networkType) {
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
                return "Unknown";
        }
    }

    public static boolean isConnectInternet(Context pContext) {
        if (pContext != null) {
            try {
                ConnectivityManager conManager = (ConnectivityManager) pContext.getSystemService("connectivity");
                if (conManager != null && pContext.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", pContext.getPackageName()) == 0) {
                    NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
                    if (networkInfo != null) {
                        return networkInfo.isConnected();
                    }
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private static String[] getNetworkState(Context context) {
        if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) != 0) {
            arrayOfString[0] = "Unknown";
            return arrayOfString;
        }
        ConnectivityManager localConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (localConnectivityManager == null) {
            arrayOfString[0] = "Unknown";
            return arrayOfString;
        }
        NetworkInfo nInfo = localConnectivityManager.getActiveNetworkInfo();
        if (nInfo == null || !nInfo.isConnected()) {
            arrayOfString[0] = "Unknown";
            arrayOfString[1] = "Unknown";
        } else if (1 == nInfo.getType()) {
            arrayOfString[0] = "Wi-Fi";
        } else if (nInfo.getType() == 0) {
            arrayOfString[0] = "2G/3G";
            arrayOfString[1] = nInfo.getSubtypeName();
        }
        return arrayOfString;
    }

    public static String getAccess(Context aContext) {
        try {
            return getNetworkState(aContext)[0];
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public static String getAccsssSubType(Context aContext) {
        try {
            String[] networkStatus = getNetworkState(aContext);
            if (networkStatus[0].equals("2G/3G")) {
                return networkStatus[1];
            }
            return "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public static String getWifiIpAddress(Context context) {
        if (context == null) {
            return null;
        }
        try {
            WifiInfo wifiinfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
            if (wifiinfo != null) {
                return convertIntToIp(wifiinfo.getIpAddress());
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static String convertIntToIp(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    public static boolean isWifi(Context context) {
        if (context == null) {
            return false;
        }
        try {
            if (getNetworkState(context)[0].equals("Wi-Fi")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
