package com.ta.audid.collect;

import android.content.Context;
import android.os.Build;
import com.ta.audid.utils.SystemProperties;
import com.ta.audid.utils.YunOSDeviceUtils;

class SystemInfo {
    SystemInfo() {
    }

    public static boolean isEmulator(Context context) {
        try {
            if (Build.HARDWARE.contains("goldfish") || Build.PRODUCT.contains("sdk") || Build.FINGERPRINT.contains("generic")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getOSName() {
        if (YunOSDeviceUtils.isYunOSPhoneSystem()) {
            return "yp";
        }
        if (YunOSDeviceUtils.isYunOSTvSystem()) {
            return "yt";
        }
        return "a";
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getBuildVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    public static String getBuildVersionSDK() {
        return Build.VERSION.SDK;
    }

    public static String getBuildType() {
        return Build.TYPE;
    }

    public static String getBuildTags() {
        return Build.TAGS;
    }

    public static String getBuildDisplay() {
        return Build.DISPLAY;
    }

    public static String getBuildID() {
        return Build.ID;
    }

    public static String getBuildTime() {
        return "" + Build.TIME;
    }

    public static String getBoard() {
        return Build.BOARD;
    }

    public static String getDevice() {
        return Build.DEVICE;
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getProduct() {
        return Build.PRODUCT;
    }

    public static String getBuildVersionIncremental() {
        return Build.VERSION.INCREMENTAL;
    }

    public static String getGsmSimState() {
        return SystemProperties.get("gsm.sim.state", "");
    }

    public static String getGsmSimState2() {
        return SystemProperties.get("gsm.sim.state.2", "");
    }

    public static String getKernelQemu() {
        return SystemProperties.get("ro.kernel.qemu", "0");
    }

    public static String getUsbState() {
        return SystemProperties.get("sys.usb.state", "");
    }

    public static String getWifiInterface() {
        return SystemProperties.get("wifi.interface", "");
    }

    public static String getBandVersion() {
        return SystemProperties.get("gsm.version.baseband", "");
    }
}
