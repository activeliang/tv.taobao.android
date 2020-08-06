package com.alibaba.analytics.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.model.UTMCLogFields;
import com.alibaba.analytics.core.network.NetworkUtil;
import com.ut.device.UTDevice;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DeviceUtil {
    private static Map<String, String> s_deviceInfoMap = null;

    public static synchronized Map<String, String> getDeviceInfo(Context pContext) {
        Map<String, String> lDeviceInfoMap;
        synchronized (DeviceUtil.class) {
            if (s_deviceInfoMap != null) {
                lDeviceInfoMap = s_deviceInfoMap;
            } else if (pContext != null) {
                lDeviceInfoMap = new HashMap<>();
                try {
                    lDeviceInfoMap.put(LogField.UTDID.toString(), UTDevice.getUtdid(pContext));
                    lDeviceInfoMap.put(LogField.IMEI.toString(), PhoneInfoUtils.getImei(pContext));
                    lDeviceInfoMap.put(LogField.IMSI.toString(), PhoneInfoUtils.getImsi(pContext));
                    lDeviceInfoMap.put(LogField.DEVICE_MODEL.toString(), Build.MODEL);
                    lDeviceInfoMap.put(LogField.BRAND.toString(), Build.BRAND);
                    lDeviceInfoMap.put(LogField.OSVERSION.toString(), Build.VERSION.RELEASE);
                    lDeviceInfoMap.put(LogField.OS.toString(), "a");
                    PackageInfo packageInfo = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), 0);
                    lDeviceInfoMap.put(LogField.APPVERSION.toString(), packageInfo.versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    lDeviceInfoMap.put(LogField.APPVERSION.toString(), "Unknown");
                } catch (Throwable th) {
                }
                if (isYunOSSystem()) {
                    lDeviceInfoMap.put(LogField.OS.toString(), "y");
                    String lUUID = getUUID();
                    if (!StringUtils.isEmpty(lUUID)) {
                        lDeviceInfoMap.put(UTMCLogFields.DEVICE_ID.toString(), lUUID);
                    }
                    String lOSVersion = System.getProperty("ro.yunos.version");
                    if (!StringUtils.isEmpty(lOSVersion)) {
                        lDeviceInfoMap.put(LogField.OSVERSION.toString(), lOSVersion);
                    }
                    String lOSVersion2 = getBuildVersion();
                    if (!StringUtils.isEmpty(lOSVersion2)) {
                        lDeviceInfoMap.put(LogField.OSVERSION.toString(), lOSVersion2);
                    }
                }
                if (isYunOSTvSystem()) {
                    lDeviceInfoMap.put(LogField.OS.toString(), "a");
                }
                try {
                    Configuration configuration = new Configuration();
                    Settings.System.getConfiguration(pContext.getContentResolver(), configuration);
                    if (configuration == null || configuration.locale == null) {
                        lDeviceInfoMap.put(LogField.LANGUAGE.toString(), "Unknown");
                    } else {
                        lDeviceInfoMap.put(LogField.LANGUAGE.toString(), configuration.locale.toString());
                    }
                    DisplayMetrics dm = pContext.getResources().getDisplayMetrics();
                    int width = dm.widthPixels;
                    int height = dm.heightPixels;
                    if (width > height) {
                        int width2 = width ^ height;
                        height ^= width2;
                        width = width2 ^ height;
                    }
                    lDeviceInfoMap.put(LogField.RESOLUTION.toString(), height + "*" + width);
                } catch (Exception e2) {
                    lDeviceInfoMap.put(LogField.RESOLUTION.toString(), "Unknown");
                }
                try {
                    String[] networkStatus = NetworkUtil.getNetworkState(pContext);
                    lDeviceInfoMap.put(LogField.ACCESS.toString(), networkStatus[0]);
                    if (networkStatus[0].equals("2G/3G")) {
                        lDeviceInfoMap.put(LogField.ACCESS_SUBTYPE.toString(), networkStatus[1]);
                    } else {
                        lDeviceInfoMap.put(LogField.ACCESS_SUBTYPE.toString(), "Unknown");
                    }
                } catch (Exception e3) {
                    lDeviceInfoMap.put(LogField.ACCESS.toString(), "Unknown");
                    lDeviceInfoMap.put(LogField.ACCESS_SUBTYPE.toString(), "Unknown");
                }
                try {
                    TelephonyManager telephonyManager = (TelephonyManager) pContext.getSystemService("phone");
                    String networkName = "";
                    if (telephonyManager != null && telephonyManager.getSimState() == 5) {
                        networkName = telephonyManager.getNetworkOperatorName();
                    }
                    if (StringUtils.isEmpty(networkName)) {
                        networkName = "Unknown";
                    }
                    lDeviceInfoMap.put(LogField.CARRIER.toString(), networkName);
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
                try {
                    if (pContext.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == 0) {
                        String lMacAddr = NetworkUtil.getWifiAddress(pContext);
                        if (!StringUtils.isEmpty(lMacAddr)) {
                            lDeviceInfoMap.put("_mac", lMacAddr);
                        }
                    }
                } catch (Exception e5) {
                }
                s_deviceInfoMap = lDeviceInfoMap;
                lDeviceInfoMap = s_deviceInfoMap;
            } else {
                lDeviceInfoMap = null;
            }
        }
        return lDeviceInfoMap;
    }

    @Deprecated
    private static String getOsVersion() {
        String osVersion = Build.VERSION.RELEASE;
        if (isYunOSSystem()) {
            String osVersion2 = System.getProperty("ro.yunos.version");
            osVersion = getBuildVersion();
            if (!TextUtils.isEmpty(osVersion)) {
            }
        }
        return osVersion;
    }

    @Deprecated
    private static String getOs() {
        if (!isYunOSSystem() || isYunOSTvSystem()) {
            return "a";
        }
        return "y";
    }

    @Deprecated
    private static void refreshNetworkStatus(Map<String, String> deviceInfoMap, Context pContext) {
        try {
            String[] networkStatus = NetworkUtil.getNetworkState(pContext);
            deviceInfoMap.put(LogField.ACCESS.toString(), networkStatus[0]);
            if (networkStatus[0].equals("2G/3G")) {
                deviceInfoMap.put(LogField.ACCESS_SUBTYPE.toString(), networkStatus[1]);
            } else {
                deviceInfoMap.put(LogField.ACCESS_SUBTYPE.toString(), "Unknown");
            }
        } catch (Exception e) {
            deviceInfoMap.put(LogField.ACCESS.toString(), "Unknown");
            deviceInfoMap.put(LogField.ACCESS_SUBTYPE.toString(), "Unknown");
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) pContext.getSystemService("phone");
            String networkName = "";
            if (telephonyManager != null && telephonyManager.getSimState() == 5) {
                networkName = telephonyManager.getNetworkOperatorName();
            }
            if (TextUtils.isEmpty(networkName)) {
                networkName = "Unknown";
            }
            deviceInfoMap.put(LogField.CARRIER.toString(), networkName);
        } catch (Exception e2) {
        }
    }

    @Deprecated
    private static String getLanguage(Context pContext) {
        try {
            return Locale.getDefault().getLanguage();
        } catch (Throwable th) {
            return "Unknown";
        }
    }

    @Deprecated
    private static String getResolution(Context pContext) {
        try {
            DisplayMetrics dm = pContext.getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            if (width > height) {
                int width2 = width ^ height;
                height ^= width2;
                width = width2 ^ height;
            }
            return height + "*" + width;
        } catch (Exception e) {
            return "Unknown";
        }
    }

    @Deprecated
    public static String getAppVersion(Context pContext) {
        try {
            PackageInfo packageInfo = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), 0);
            if (packageInfo == null) {
                return "Unknown";
            }
            s_deviceInfoMap.put(LogField.APPVERSION.toString(), packageInfo.versionName);
            return packageInfo.versionName;
        } catch (Throwable th) {
            return "Unknown";
        }
    }

    public static boolean isYunOSSystem() {
        if ((System.getProperty("java.vm.name") == null || !System.getProperty("java.vm.name").toLowerCase().contains("lemur")) && System.getProperty("ro.yunos.version") == null && TextUtils.isEmpty(SystemProperties.get("ro.yunos.build.version"))) {
            return isYunOSTvSystem();
        }
        return true;
    }

    private static boolean isYunOSTvSystem() {
        if (!TextUtils.isEmpty(getSystemProperties("ro.yunos.product.chip")) || !TextUtils.isEmpty(getSystemProperties("ro.yunos.hardware"))) {
            return true;
        }
        return false;
    }

    public static String getSystemProperties(String key) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class}).invoke(cls.newInstance(), new Object[]{key});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUUID() {
        String lUUID = SystemProperties.get("ro.aliyun.clouduuid", "");
        if (TextUtils.isEmpty(lUUID)) {
            lUUID = SystemProperties.get("ro.sys.aliyun.clouduuid", "");
        }
        if (TextUtils.isEmpty(lUUID)) {
            return getYunOsTvUuid();
        }
        return lUUID;
    }

    private static String getYunOsTvUuid() {
        try {
            return (String) Class.forName("com.yunos.baseservice.clouduuid.CloudUUID").getMethod("getCloudUUID", new Class[0]).invoke((Object) null, new Object[0]);
        } catch (Exception e) {
            return null;
        }
    }

    private static String getBuildVersion() {
        try {
            Field lField = Build.class.getDeclaredField("YUNOS_BUILD_VERSION");
            if (lField != null) {
                lField.setAccessible(true);
                return (String) lField.get(new String());
            }
        } catch (Exception e) {
        }
        return null;
    }
}
