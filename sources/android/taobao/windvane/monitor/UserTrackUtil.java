package android.taobao.windvane.monitor;

import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import com.alibaba.analytics.core.device.Constants;
import java.lang.reflect.Method;
import java.util.Map;

public class UserTrackUtil {
    public static final int EVENTID_DEV_STORAGE = 15307;
    public static final int EVENTID_ERROR = 15306;
    public static final int EVENTID_MONITOR = 15301;
    public static final int EVENTID_PA_APPS = 15305;
    public static final int EVENTID_PA_SAPP = 15303;
    public static final int EVENTID_PA_UCSDK = 15309;
    public static boolean OFF = false;
    private static final String TAG = "UserTrackUtil";
    private static boolean isInit = false;
    private static Method utCommitEvent;
    private static Method utCommitEventWithArgs;
    private static Method utCommitPage;

    public static void init() {
        if (!isInit) {
            try {
                Class<?> cls = Class.forName("com.taobao.statistic.TBS$Ext");
                utCommitEvent = cls.getDeclaredMethod("commitEvent", new Class[]{Integer.TYPE, Object.class, Object.class, Object.class});
                utCommitPage = cls.getDeclaredMethod("commitEvent", new Class[]{String.class, Integer.TYPE, Object.class, Object.class, Object.class, String[].class});
                utCommitEventWithArgs = cls.getDeclaredMethod("commitEvent", new Class[]{Integer.TYPE, Object.class, Object.class, Object.class, String[].class});
                isInit = true;
            } catch (ClassNotFoundException e) {
                TaoLog.d(TAG, "UT class not found");
            } catch (NoSuchMethodException e2) {
                TaoLog.d(TAG, "UT method not found");
            }
        }
    }

    public static void commitEvent(int eventId, String arg1, String arg2, String arg3) {
        if (utCommitEvent != null && WVCommonConfig.commonConfig.monitorStatus != 0) {
            try {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "commitEvent: " + eventId + Constants.SEPARATOR + arg1 + Constants.SEPARATOR + arg2 + Constants.SEPARATOR + arg3);
                }
                utCommitEvent.invoke((Object) null, new Object[]{Integer.valueOf(eventId), arg1, arg2, arg3});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void commitEvent(java.lang.String r7, int r8, java.lang.String r9, java.lang.String r10, java.lang.String r11, java.lang.String r12) {
        /*
            java.lang.reflect.Method r1 = utCommitPage
            if (r1 == 0) goto L_0x000a
            android.taobao.windvane.config.WVCommonConfigData r1 = android.taobao.windvane.config.WVCommonConfig.commonConfig
            int r1 = r1.monitorStatus
            if (r1 != 0) goto L_0x000b
        L_0x000a:
            return
        L_0x000b:
            boolean r1 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ Exception -> 0x008b }
            if (r1 == 0) goto L_0x0065
            java.lang.String r1 = "UserTrackUtil"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x008b }
            r2.<init>()     // Catch:{ Exception -> 0x008b }
            java.lang.String r3 = "commitEvent: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x008b }
            java.lang.StringBuilder r2 = r2.append(r7)     // Catch:{ Exception -> 0x008b }
            java.lang.String r3 = "||"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x008b }
            java.lang.StringBuilder r2 = r2.append(r8)     // Catch:{ Exception -> 0x008b }
            java.lang.String r3 = "||"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x008b }
            java.lang.StringBuilder r2 = r2.append(r9)     // Catch:{ Exception -> 0x008b }
            java.lang.String r3 = "||"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x008b }
            java.lang.StringBuilder r2 = r2.append(r10)     // Catch:{ Exception -> 0x008b }
            java.lang.String r3 = "||"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x008b }
            java.lang.StringBuilder r2 = r2.append(r11)     // Catch:{ Exception -> 0x008b }
            r3 = 1
            java.lang.CharSequence[] r3 = new java.lang.CharSequence[r3]     // Catch:{ Exception -> 0x008b }
            r4 = 0
            r3[r4] = r12     // Catch:{ Exception -> 0x008b }
            java.lang.CharSequence r3 = android.text.TextUtils.concat(r3)     // Catch:{ Exception -> 0x008b }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x008b }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x008b }
            android.taobao.windvane.util.TaoLog.d(r1, r2)     // Catch:{ Exception -> 0x008b }
        L_0x0065:
            java.lang.reflect.Method r1 = utCommitPage     // Catch:{ Exception -> 0x008b }
            r2 = 0
            r3 = 6
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x008b }
            r4 = 0
            r3[r4] = r7     // Catch:{ Exception -> 0x008b }
            r4 = 1
            java.lang.Integer r5 = java.lang.Integer.valueOf(r8)     // Catch:{ Exception -> 0x008b }
            r3[r4] = r5     // Catch:{ Exception -> 0x008b }
            r4 = 2
            r3[r4] = r9     // Catch:{ Exception -> 0x008b }
            r4 = 3
            r3[r4] = r10     // Catch:{ Exception -> 0x008b }
            r4 = 4
            r3[r4] = r11     // Catch:{ Exception -> 0x008b }
            r4 = 5
            r5 = 1
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch:{ Exception -> 0x008b }
            r6 = 0
            r5[r6] = r12     // Catch:{ Exception -> 0x008b }
            r3[r4] = r5     // Catch:{ Exception -> 0x008b }
            r1.invoke(r2, r3)     // Catch:{ Exception -> 0x008b }
            goto L_0x000a
        L_0x008b:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x000a
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.monitor.UserTrackUtil.commitEvent(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String):void");
    }

    public static void commitEvent(int eventId, String arg1, String arg2, String arg3, String[] args) {
        if (utCommitEventWithArgs != null && WVCommonConfig.commonConfig.monitorStatus != 0) {
            try {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "commitEvent: " + eventId + Constants.SEPARATOR + arg1 + Constants.SEPARATOR + arg2 + Constants.SEPARATOR + arg3 + TextUtils.concat(args));
                }
                utCommitEventWithArgs.invoke((Object) null, new Object[]{Integer.valueOf(eventId), arg1, arg2, arg3, args});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String toArgString(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(map.size() * 28);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            if (!TextUtils.isEmpty(key)) {
                builder.append(key).append("=").append(entry.getValue()).append(",");
            }
        }
        return builder.substring(0, builder.length() - 1);
    }
}
