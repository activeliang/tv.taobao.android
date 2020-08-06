package com.ta.audid.collect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import com.ta.audid.utils.UtdidLogger;
import com.ta.utdid2.android.utils.StringUtils;

public class NetworkInfo {
    public static String getBssid(Context aContext) {
        try {
            String bssid = ((WifiManager) aContext.getSystemService("wifi")).getConnectionInfo().getBSSID();
            if (StringUtils.isBlank(bssid)) {
                return "";
            }
            return bssid;
        } catch (Throwable th) {
            return "";
        }
    }

    public static String getPhoneNetworkType(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                return String.valueOf(telephonyManager.getNetworkType());
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static String getPhoneOperatorName(Context context) {
        if (context == null) {
            return "";
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            return telephonyManager != null ? telephonyManager.getSimOperatorName() : "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getPhoneOperatorType(Context context) {
        if (context == null) {
            return "";
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                return telephonyManager.getSimOperator();
            }
            return null;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getRssi(Context context) {
        if (context == null) {
            return "";
        }
        try {
            int level = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getRssi();
            if (level <= 0 && level >= -50) {
                return "1";
            }
            if (level < -50 && level >= -70) {
                return "2";
            }
            if (level < -70 && level >= -80) {
                return "3";
            }
            if (level >= -80 || level < -100) {
                return "5";
            }
            return "4";
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isStrongSemaphore(Context context) {
        if (context == null) {
            return false;
        }
        try {
            int level = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getRssi();
            if (level > 0 || level < -70) {
                return false;
            }
            return true;
        } catch (Exception e) {
            UtdidLogger.d("", e);
            return false;
        }
    }

    public static boolean isBluetoothEnable(Context context) {
        if (context == null || Build.VERSION.SDK_INT < 18) {
            return false;
        }
        try {
            BluetoothAdapter bluetoothAdapter = ((BluetoothManager) context.getSystemService("bluetooth")).getAdapter();
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
