package anet.channel.util;

import android.content.Context;
import android.content.pm.PackageManager;
import anet.channel.monitor.BandWidthSampler;
import anet.channel.monitor.NetworkSpeed;
import anet.channel.status.NetworkStatusHelper;
import com.ta.utdid2.device.UTDevice;
import java.lang.reflect.Method;

public class Utils {
    private static final String TAG = "awcn.Utils";
    public static int accsVersion = 0;
    public static Context context = null;

    public static String getDeviceId(Context context2) {
        return UTDevice.getUtdid(context2);
    }

    public static String getMainProcessName(Context context2) {
        String processName = "";
        if (context2 == null) {
            return processName;
        }
        try {
            processName = context2.getPackageManager().getPackageInfo(context2.getPackageName(), 0).applicationInfo.processName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return processName;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x003a  */
    /* JADX WARNING: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getProcessName(android.content.Context r13, int r14) {
        /*
            r12 = -108(0xffffffffffffff94, float:NaN)
            java.lang.String r6 = ""
            java.lang.String r8 = "activity"
            java.lang.Object r1 = r13.getSystemService(r8)     // Catch:{ Exception -> 0x0071 }
            android.app.ActivityManager r1 = (android.app.ActivityManager) r1     // Catch:{ Exception -> 0x0071 }
            java.util.List r7 = r1.getRunningAppProcesses()     // Catch:{ Exception -> 0x0071 }
            if (r7 == 0) goto L_0x003f
            int r8 = r7.size()     // Catch:{ Exception -> 0x0071 }
            if (r8 <= 0) goto L_0x003f
            java.util.Iterator r3 = r7.iterator()     // Catch:{ Exception -> 0x0071 }
        L_0x001e:
            boolean r8 = r3.hasNext()     // Catch:{ Exception -> 0x0071 }
            if (r8 == 0) goto L_0x0034
            java.lang.Object r8 = r3.next()     // Catch:{ Exception -> 0x0071 }
            android.app.ActivityManager$RunningAppProcessInfo r8 = (android.app.ActivityManager.RunningAppProcessInfo) r8     // Catch:{ Exception -> 0x0071 }
            r0 = r8
            android.app.ActivityManager$RunningAppProcessInfo r0 = (android.app.ActivityManager.RunningAppProcessInfo) r0     // Catch:{ Exception -> 0x0071 }
            r4 = r0
            int r8 = r4.pid     // Catch:{ Exception -> 0x0071 }
            if (r8 != r14) goto L_0x001e
            java.lang.String r6 = r4.processName     // Catch:{ Exception -> 0x0071 }
        L_0x0034:
            boolean r8 = android.text.TextUtils.isEmpty(r6)
            if (r8 == 0) goto L_0x003e
            java.lang.String r6 = getProcessNameNew(r14)
        L_0x003e:
            return r6
        L_0x003f:
            r8 = -108(0xffffffffffffff94, float:NaN)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0071 }
            r9.<init>()     // Catch:{ Exception -> 0x0071 }
            java.lang.String r10 = "BuildVersion="
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x0071 }
            int r10 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x0071 }
            java.lang.String r10 = java.lang.String.valueOf(r10)     // Catch:{ Exception -> 0x0071 }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x0071 }
            java.lang.String r9 = r9.toString()     // Catch:{ Exception -> 0x0071 }
            java.lang.String r5 = anet.channel.util.ErrorConstant.formatMsg(r8, r9)     // Catch:{ Exception -> 0x0071 }
            anet.channel.appmonitor.IAppMonitor r8 = anet.channel.appmonitor.AppMonitor.getInstance()     // Catch:{ Exception -> 0x0071 }
            anet.channel.statist.ExceptionStatistic r9 = new anet.channel.statist.ExceptionStatistic     // Catch:{ Exception -> 0x0071 }
            r10 = -108(0xffffffffffffff94, float:NaN)
            java.lang.String r11 = "rt"
            r9.<init>(r10, r5, r11)     // Catch:{ Exception -> 0x0071 }
            r8.commitStat(r9)     // Catch:{ Exception -> 0x0071 }
            goto L_0x0034
        L_0x0071:
            r2 = move-exception
            anet.channel.appmonitor.IAppMonitor r8 = anet.channel.appmonitor.AppMonitor.getInstance()
            anet.channel.statist.ExceptionStatistic r9 = new anet.channel.statist.ExceptionStatistic
            java.lang.String r10 = r2.toString()
            java.lang.String r11 = "rt"
            r9.<init>(r12, r10, r11)
            r8.commitStat(r9)
            goto L_0x0034
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.util.Utils.getProcessName(android.content.Context, int):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x00e0 A[SYNTHETIC, Splitter:B:38:0x00e0] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00e5 A[Catch:{ IOException -> 0x00e9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00f9 A[SYNTHETIC, Splitter:B:46:0x00f9] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00fe A[Catch:{ IOException -> 0x0102 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getProcessNameNew(int r15) {
        /*
            r14 = 0
            r13 = 0
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "ps  |  grep  "
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.StringBuilder r9 = r9.append(r15)
            java.lang.String r0 = r9.toString()
            r2 = 0
            r5 = 0
            java.lang.Runtime r9 = java.lang.Runtime.getRuntime()     // Catch:{ Exception -> 0x00d0 }
            java.lang.String r10 = "sh"
            java.lang.Process r7 = r9.exec(r10)     // Catch:{ Exception -> 0x00d0 }
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00d0 }
            java.io.InputStreamReader r9 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x00d0 }
            java.io.InputStream r10 = r7.getInputStream()     // Catch:{ Exception -> 0x00d0 }
            r9.<init>(r10)     // Catch:{ Exception -> 0x00d0 }
            r3.<init>(r9)     // Catch:{ Exception -> 0x00d0 }
            java.io.DataOutputStream r6 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x0116, all -> 0x010f }
            java.io.OutputStream r9 = r7.getOutputStream()     // Catch:{ Exception -> 0x0116, all -> 0x010f }
            r6.<init>(r9)     // Catch:{ Exception -> 0x0116, all -> 0x010f }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            r9.<init>()     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            java.lang.StringBuilder r9 = r9.append(r0)     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            java.lang.String r10 = "  &\n"
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            java.lang.String r9 = r9.toString()     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            r6.writeBytes(r9)     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            r6.flush()     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            java.lang.String r9 = "exit\n"
            r6.writeBytes(r9)     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            r7.waitFor()     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
        L_0x005d:
            java.lang.String r4 = r3.readLine()     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            if (r4 == 0) goto L_0x00b1
            java.lang.String r9 = "\\s+"
            java.lang.String r10 = "  "
            java.lang.String r4 = r4.replaceAll(r9, r10)     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            java.lang.String r9 = "  "
            java.lang.String[] r8 = r4.split(r9)     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            int r9 = r8.length     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            r10 = 9
            if (r9 < r10) goto L_0x005d
            r9 = 1
            r9 = r8[r9]     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            boolean r9 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            if (r9 != 0) goto L_0x005d
            r9 = 1
            r9 = r8[r9]     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            java.lang.String r9 = r9.trim()     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            java.lang.String r10 = java.lang.String.valueOf(r15)     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            boolean r9 = r9.equals(r10)     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            if (r9 == 0) goto L_0x005d
            r9 = 8
            r9 = r8[r9]     // Catch:{ Exception -> 0x0119, all -> 0x0112 }
            if (r3 == 0) goto L_0x009c
            r3.close()     // Catch:{ IOException -> 0x00a4 }
        L_0x009c:
            if (r6 == 0) goto L_0x00a1
            r6.close()     // Catch:{ IOException -> 0x00a4 }
        L_0x00a1:
            r5 = r6
            r2 = r3
        L_0x00a3:
            return r9
        L_0x00a4:
            r1 = move-exception
            java.lang.String r10 = "awcn.Utils"
            java.lang.String r11 = "getProcessNameNew "
            java.lang.Object[] r12 = new java.lang.Object[r13]
            anet.channel.util.ALog.e(r10, r11, r14, r1, r12)
            goto L_0x00a1
        L_0x00b1:
            if (r3 == 0) goto L_0x00b6
            r3.close()     // Catch:{ IOException -> 0x00c1 }
        L_0x00b6:
            if (r6 == 0) goto L_0x00bb
            r6.close()     // Catch:{ IOException -> 0x00c1 }
        L_0x00bb:
            r5 = r6
            r2 = r3
        L_0x00bd:
            java.lang.String r9 = ""
            goto L_0x00a3
        L_0x00c1:
            r1 = move-exception
            java.lang.String r9 = "awcn.Utils"
            java.lang.String r10 = "getProcessNameNew "
            java.lang.Object[] r11 = new java.lang.Object[r13]
            anet.channel.util.ALog.e(r9, r10, r14, r1, r11)
            r5 = r6
            r2 = r3
            goto L_0x00bd
        L_0x00d0:
            r1 = move-exception
        L_0x00d1:
            java.lang.String r9 = "awcn.Utils"
            java.lang.String r10 = "getProcessNameNew "
            r11 = 0
            r12 = 0
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x00f6 }
            anet.channel.util.ALog.e(r9, r10, r11, r1, r12)     // Catch:{ all -> 0x00f6 }
            if (r2 == 0) goto L_0x00e3
            r2.close()     // Catch:{ IOException -> 0x00e9 }
        L_0x00e3:
            if (r5 == 0) goto L_0x00bd
            r5.close()     // Catch:{ IOException -> 0x00e9 }
            goto L_0x00bd
        L_0x00e9:
            r1 = move-exception
            java.lang.String r9 = "awcn.Utils"
            java.lang.String r10 = "getProcessNameNew "
            java.lang.Object[] r11 = new java.lang.Object[r13]
            anet.channel.util.ALog.e(r9, r10, r14, r1, r11)
            goto L_0x00bd
        L_0x00f6:
            r9 = move-exception
        L_0x00f7:
            if (r2 == 0) goto L_0x00fc
            r2.close()     // Catch:{ IOException -> 0x0102 }
        L_0x00fc:
            if (r5 == 0) goto L_0x0101
            r5.close()     // Catch:{ IOException -> 0x0102 }
        L_0x0101:
            throw r9
        L_0x0102:
            r1 = move-exception
            java.lang.String r10 = "awcn.Utils"
            java.lang.String r11 = "getProcessNameNew "
            java.lang.Object[] r12 = new java.lang.Object[r13]
            anet.channel.util.ALog.e(r10, r11, r14, r1, r12)
            goto L_0x0101
        L_0x010f:
            r9 = move-exception
            r2 = r3
            goto L_0x00f7
        L_0x0112:
            r9 = move-exception
            r5 = r6
            r2 = r3
            goto L_0x00f7
        L_0x0116:
            r1 = move-exception
            r2 = r3
            goto L_0x00d1
        L_0x0119:
            r1 = move-exception
            r5 = r6
            r2 = r3
            goto L_0x00d1
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.util.Utils.getProcessNameNew(int):java.lang.String");
    }

    public static Context getAppContext() {
        if (context != null) {
            return context;
        }
        synchronized (Utils.class) {
            if (context != null) {
                Context context2 = context;
                return context2;
            }
            try {
                Class<?> clazz = Class.forName("android.app.ActivityThread");
                Object object = clazz.getMethod("currentActivityThread", new Class[0]).invoke(clazz, new Object[0]);
                context = (Context) object.getClass().getMethod("getApplication", new Class[0]).invoke(object, new Object[0]);
            } catch (Exception e) {
                ALog.w(TAG, "getAppContext", (String) null, e, new Object[0]);
            }
            Context context3 = context;
            return context3;
        }
    }

    public static int getAccsVersion() {
        if (accsVersion != 0) {
            return accsVersion;
        }
        try {
            accsVersion = ((Integer) Utils.class.getClassLoader().loadClass("com.taobao.accs.ChannelService").getDeclaredField("SDK_VERSION_CODE").get((Object) null)).intValue();
        } catch (Exception e) {
            ALog.w(TAG, "getAccsVersion", (String) null, e, new Object[0]);
        }
        return accsVersion;
    }

    public static String getStackMsg(Throwable e) {
        StringBuffer sb = new StringBuffer();
        try {
            StackTraceElement[] stackArray = e.getStackTrace();
            if (stackArray != null && stackArray.length > 0) {
                for (int i = 0; i < stackArray.length; i++) {
                    sb.append(stackArray[i].toString() + "\n");
                }
            }
        } catch (Exception e1) {
            ALog.e(TAG, "getStackMsg", (String) null, e1, new Object[0]);
        }
        return sb.toString();
    }

    public static Object invokeStaticMethodThrowException(String className, String methodName, Class<?>[] parameterTypes, Object... param) throws Exception {
        Method method;
        if (className == null || methodName == null) {
            return null;
        }
        Class clazz = Class.forName(className);
        if (parameterTypes != null) {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
        } else {
            method = clazz.getDeclaredMethod(methodName, new Class[0]);
        }
        if (method == null) {
            return null;
        }
        method.setAccessible(true);
        if (param != null) {
            return method.invoke(clazz, param);
        }
        return method.invoke(clazz, new Object[0]);
    }

    public static float getNetworkTimeFactor() {
        float factor = 1.0f;
        NetworkStatusHelper.NetworkStatus status = NetworkStatusHelper.getStatus();
        if (status == NetworkStatusHelper.NetworkStatus.G4 || status == NetworkStatusHelper.NetworkStatus.WIFI) {
            factor = 1.0f * 0.8f;
        }
        if (BandWidthSampler.getInstance().getNetworkSpeed() == NetworkSpeed.Fast.getCode()) {
            return factor * 0.75f;
        }
        return factor;
    }
}
