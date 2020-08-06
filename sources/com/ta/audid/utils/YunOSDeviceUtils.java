package com.ta.audid.utils;

import android.os.Build;
import android.text.TextUtils;
import com.ta.utdid2.android.utils.StringUtils;
import java.lang.reflect.Field;

public class YunOSDeviceUtils {
    public static boolean isYunOSPhoneSystem() {
        if ((System.getProperty("java.vm.name") == null || !System.getProperty("java.vm.name").toLowerCase().contains("lemur")) && System.getProperty("ro.yunos.version") == null && TextUtils.isEmpty(SystemProperties.get("ro.yunos.build.version"))) {
            return false;
        }
        return true;
    }

    public static boolean isYunOSTvSystem() {
        if (!TextUtils.isEmpty(SystemProperties.get("ro.yunos.product.chip")) || !TextUtils.isEmpty(SystemProperties.get("ro.yunos.hardware"))) {
            return true;
        }
        return false;
    }

    public static String getYunOSUuid() {
        String lUUID = SystemProperties.get("ro.aliyun.clouduuid", "");
        if (TextUtils.isEmpty(lUUID)) {
            lUUID = SystemProperties.get("ro.sys.aliyun.clouduuid", "");
        }
        if (TextUtils.isEmpty(lUUID)) {
            return getYunOSTVUuid();
        }
        return lUUID;
    }

    private static String getYunOSTVUuid() {
        try {
            return (String) Class.forName("com.yunos.baseservice.clouduuid.CloudUUID").getMethod("getCloudUUID", new Class[0]).invoke((Object) null, new Object[0]);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getYunOSVersion() {
        String oSVersion = System.getProperty("ro.yunos.version", "");
        String buildVersion = getBuildVersion();
        if (!StringUtils.isEmpty(buildVersion)) {
            return buildVersion;
        }
        return oSVersion;
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
        return "";
    }
}
