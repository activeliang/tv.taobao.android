package com.yunos.tv.core.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.zhiping.dev.android.logger.ZpLogger;
import java.net.NetworkInterface;
import java.util.Collections;
import mtopsdk.common.util.SymbolExpUtil;

public class DeviceUtil {
    private static final String TAG = DeviceUtil.class.getSimpleName();
    private static String stb_result = null;

    public static class SCREENTYPE {
        public static final int ScreenType_1080P = 1920;
        public static final int ScreenType_720p = 1280;
    }

    public static float getScreenScaleFromDevice(Context mContext) {
        Context context = mContext.getApplicationContext();
        new DisplayMetrics();
        if (((double) (((float) context.getResources().getDisplayMetrics().widthPixels) / 1280.0f)) > 1.2d) {
            return 1.5f;
        }
        return 1.0f;
    }

    public static float getDensityFromDevice(Context mContext) {
        Context context = mContext.getApplicationContext();
        new DisplayMetrics();
        return context.getResources().getDisplayMetrics().density;
    }

    public static DisplayMetrics getDisplayMetricsFromDevice(Context mContext) {
        return new DisplayMetrics();
    }

    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getDeviceProduct() {
        return Build.PRODUCT;
    }

    public static String getStbID() {
        return stb_result;
    }

    public static String initMacAddress(Context context) {
        byte[] mac;
        try {
            for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (intf.getName().equalsIgnoreCase("eth0") && (mac = intf.getHardwareAddress()) != null) {
                    StringBuilder buf = new StringBuilder();
                    for (int idx = 0; idx < mac.length; idx++) {
                        buf.append(String.format("%02X:", new Object[]{Byte.valueOf(mac[idx])}));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    if (buf != null) {
                        stb_result = "MAC" + buf.toString().replaceAll(SymbolExpUtil.SYMBOL_COLON, "");
                    }
                }
            }
            if (TextUtils.isEmpty(stb_result)) {
                stb_result = "MAC" + ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress().replace(SymbolExpUtil.SYMBOL_COLON, "");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stb_result;
    }

    public static int getYuyinPackageCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.yunos.tv.alitvasr", 16384);
            ZpLogger.e(TAG, "packageInfo------" + packageInfo.versionCode);
            ZpLogger.e(TAG, "packageInfo------" + packageInfo.versionName);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
