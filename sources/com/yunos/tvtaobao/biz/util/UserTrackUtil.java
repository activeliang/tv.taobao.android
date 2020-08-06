package com.yunos.tvtaobao.biz.util;

import android.text.TextUtils;
import com.taobao.statistic.CT;
import com.taobao.statistic.TBS;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Arrays;
import java.util.Map;

public class UserTrackUtil {
    private static final String TAG = "UserTrackUtil";
    private static String sCurVersionCode;
    private static boolean sIsForced;
    private static String sNewVersionCode;

    public static void onCtrlClicked(CT ct, String name, String... arg) {
        if (arg != null) {
            Map<String, String> p = getProperties();
            String[] fullArgs = new String[(arg.length + p.size())];
            System.arraycopy(arg, 0, fullArgs, 0, arg.length);
            int i = 0;
            for (Object keyObject : p.keySet()) {
                String key = String.valueOf(keyObject);
                fullArgs[arg.length + i] = key + "=" + p.put(key, "");
                i++;
            }
            ZpLogger.d(TAG, "name: " + name + " args: " + Arrays.asList(fullArgs));
            TBS.Adv.ctrlClicked(ct, name, fullArgs);
        }
    }

    public static void onCustomEvent(String name) {
        onCustomEvent(name, (Map<String, String>) null);
    }

    public static void onCustomEvent(String name, Map<String, String> properties) {
        Map<String, String> fullProperties = Utils.getProperties();
        copyProperties(properties, fullProperties);
        copyProperties(getProperties(), fullProperties);
        ZpLogger.d(TAG, "name: " + name + " properties: " + fullProperties);
        Utils.utCustomHit(Utils.utGetCurrentPage(), name, fullProperties);
    }

    public static void onErrorEvent(int errorCode) {
        Map<String, String> properties = Utils.getProperties();
        properties.put("info_code", errorCode + "");
        properties.put("info", UpdatePreference.ERROR_TYPE_INFO_MAP.get(errorCode, "unknown"));
        onCustomEvent(UpdatePreference.UT_ERROR, properties);
    }

    public static void setCurVersionCode(String versionCode) {
        if (!TextUtils.isEmpty(versionCode)) {
            sCurVersionCode = versionCode;
        }
    }

    public static String getCurVersionCode() {
        if (sCurVersionCode == null) {
            return "";
        }
        return sCurVersionCode;
    }

    public static void setNewVersionCode(String versionCode) {
        if (!TextUtils.isEmpty(versionCode)) {
            sNewVersionCode = versionCode;
        }
    }

    public static String getNewVersionCode() {
        if (sNewVersionCode == null) {
            return "";
        }
        return sNewVersionCode;
    }

    public static void setIsForcedInstall(boolean isForced) {
        sIsForced = isForced;
    }

    public static Boolean getIsForcedInstall() {
        return Boolean.valueOf(sIsForced);
    }

    public static String getUUID() {
        String uuid = CloudUUIDWrapper.getCloudUUID();
        if (TextUtils.isEmpty(uuid)) {
            return "";
        }
        return uuid;
    }

    public static Map<String, String> getProperties() {
        Map<String, String> p = Utils.getProperties();
        p.put("new_version", getNewVersionCode());
        p.put("now_version", getCurVersionCode());
        p.put("is_force", getIsForcedInstall() + "");
        return p;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0019, code lost:
        r0 = r1.toString();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void copyProperties(java.util.Map<java.lang.String, java.lang.String> r5, java.util.Map<java.lang.String, java.lang.String> r6) {
        /*
            if (r5 == 0) goto L_0x0004
            if (r6 != 0) goto L_0x0005
        L_0x0004:
            return
        L_0x0005:
            java.util.Set r3 = r5.keySet()
            java.util.Iterator r3 = r3.iterator()
        L_0x000d:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0004
            java.lang.Object r1 = r3.next()
            if (r1 == 0) goto L_0x000d
            java.lang.String r0 = r1.toString()
            java.lang.Object r2 = r5.get(r0)
            java.lang.String r2 = (java.lang.String) r2
            if (r2 == 0) goto L_0x000d
            r6.put(r0, r2)
            goto L_0x000d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.biz.util.UserTrackUtil.copyProperties(java.util.Map, java.util.Map):void");
    }
}
