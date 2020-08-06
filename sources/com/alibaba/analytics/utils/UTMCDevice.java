package com.alibaba.analytics.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.model.UTMCLogFields;
import com.alibaba.analytics.core.network.NetworkOperatorUtil;
import com.alibaba.analytics.core.network.NetworkUtil;
import com.ut.device.UTDevice;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class UTMCDevice {
    private static Map<String, String> s_deviceInfoMap = null;

    public static synchronized void updateUTMCDeviceNetworkStatus(Context aContext) {
        synchronized (UTMCDevice.class) {
            if (s_deviceInfoMap != null) {
                Logger.d("UTMCDevice", "updateUTMCDeviceNetworkStatus");
                try {
                    String[] networkStatus = NetworkUtil.getNetworkState(aContext);
                    s_deviceInfoMap.put(LogField.ACCESS.toString(), networkStatus[0]);
                    if (networkStatus[0].equals("2G/3G")) {
                        s_deviceInfoMap.put(LogField.ACCESS_SUBTYPE.toString(), networkStatus[1]);
                    } else {
                        s_deviceInfoMap.put(LogField.ACCESS_SUBTYPE.toString(), "Unknown");
                    }
                } catch (Exception e) {
                    s_deviceInfoMap.put(LogField.ACCESS.toString(), "Unknown");
                    s_deviceInfoMap.put(LogField.ACCESS_SUBTYPE.toString(), "Unknown");
                }
                s_deviceInfoMap.put(LogField.CARRIER.toString(), NetworkOperatorUtil.getCurrentNetworkOperatorName());
            }
        }
        return;
    }

    public static synchronized Map<String, String> getDeviceInfo(Context pContext) {
        Map<String, String> map = null;
        synchronized (UTMCDevice.class) {
            if (s_deviceInfoMap != null) {
                map = s_deviceInfoMap;
            } else if (pContext != null) {
                Map<String, String> lDeviceInfoMap = new HashMap<>();
                try {
                    lDeviceInfoMap.put(LogField.UTDID.toString(), UTDevice.getUtdid(pContext));
                    try {
                        lDeviceInfoMap.put(LogField.IMEI.toString(), PhoneInfoUtils.getImei(pContext));
                        lDeviceInfoMap.put(LogField.IMSI.toString(), PhoneInfoUtils.getImsi(pContext));
                        lDeviceInfoMap.put(LogField.DEVICE_MODEL.toString(), Build.MODEL);
                        lDeviceInfoMap.put(LogField.BRAND.toString(), Build.BRAND);
                        lDeviceInfoMap.put(LogField.OSVERSION.toString(), Build.VERSION.RELEASE);
                        lDeviceInfoMap.put(LogField.OS.toString(), "a");
                        try {
                            lDeviceInfoMap.put(LogField.APPVERSION.toString(), pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), 0).versionName);
                        } catch (PackageManager.NameNotFoundException e) {
                            lDeviceInfoMap.put(LogField.APPVERSION.toString(), "Unknown");
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
                            if (configuration.locale != null) {
                                lDeviceInfoMap.put(LogField.LANGUAGE.toString(), configuration.locale.toString());
                            } else {
                                lDeviceInfoMap.put(LogField.LANGUAGE.toString(), "Unknown");
                            }
                            DisplayMetrics dm = pContext.getResources().getDisplayMetrics();
                            int width = dm.widthPixels;
                            int height = dm.heightPixels;
                            if (height > width) {
                                lDeviceInfoMap.put(LogField.RESOLUTION.toString(), height + "*" + width);
                            } else {
                                lDeviceInfoMap.put(LogField.RESOLUTION.toString(), width + "*" + height);
                            }
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
                        lDeviceInfoMap.put(LogField.CARRIER.toString(), NetworkOperatorUtil.getCurrentNetworkOperatorName());
                        s_deviceInfoMap = lDeviceInfoMap;
                        map = s_deviceInfoMap;
                    } catch (Exception e4) {
                    }
                } catch (Exception e5) {
                    Log.e("", "utdid4all jar doesn't exist");
                }
            }
        }
        return map;
    }

    private static boolean isYunOSSystem() {
        if ((System.getProperty("java.vm.name") == null || !System.getProperty("java.vm.name").toLowerCase().contains("lemur")) && TextUtils.isEmpty(System.getProperty("ro.yunos.version")) && TextUtils.isEmpty(SystemProperties.get("ro.yunos.build.version"))) {
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
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getUUID() {
        String lUUID = SystemProperties.get("ro.aliyun.clouduuid", "false");
        if ("false".equals(lUUID)) {
            lUUID = SystemProperties.get("ro.sys.aliyun.clouduuid", "false");
        }
        if (StringUtils.isEmpty(lUUID)) {
            return getYunOSTVUuid();
        }
        return lUUID;
    }

    private static String getYunOSTVUuid() {
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
        } catch (Throwable th) {
        }
        return null;
    }

    public static boolean isPad(Context context) {
        if (context == null) {
            return false;
        }
        if (isPadByPhoneType(context) || isPadByScrren(context)) {
            return true;
        }
        return false;
    }

    private static boolean isPadByScrren(Context context) {
        try {
            return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
        } catch (Throwable th) {
            return false;
        }
    }

    private static boolean isPadByPhoneType(Context context) {
        try {
            if (((TelephonyManager) context.getSystemService("phone")).getPhoneType() == 0) {
                return true;
            }
            return false;
        } catch (Throwable th) {
            return false;
        }
    }
}
