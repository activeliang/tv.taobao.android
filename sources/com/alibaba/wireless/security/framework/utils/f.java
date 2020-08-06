package com.alibaba.wireless.security.framework.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Process;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.lang.reflect.Method;

public class f {
    private static String[] a = {"armeabi", "armeabi-v7a", "x86", "arm64-v8a", "x86_64"};
    private static boolean b = true;
    private static boolean c = false;
    private static boolean d = true;
    private static boolean e = false;
    private static boolean f = true;
    private static boolean g = false;
    private static boolean h = true;
    private static boolean i = false;

    public static String a(ClassLoader classLoader, String str) {
        if (classLoader == null || str == null || "".equals(str)) {
            return null;
        }
        String a2 = a(classLoader, str, true);
        return a2 == null ? a(classLoader, str, false) : a2;
    }

    private static String a(ClassLoader classLoader, String str, boolean z) {
        Method declaredMethod;
        String str2;
        if (classLoader == null) {
            return null;
        }
        if (z) {
            try {
                declaredMethod = classLoader.getClass().getMethod("findLibrary", new Class[]{String.class});
            } catch (Exception e2) {
                return null;
            }
        } else {
            declaredMethod = classLoader.getClass().getDeclaredMethod("findLibrary", new Class[]{String.class});
        }
        if (declaredMethod == null) {
            str2 = null;
        } else {
            if (!declaredMethod.isAccessible()) {
                declaredMethod.setAccessible(true);
            }
            Object invoke = declaredMethod.invoke(classLoader, new Object[]{str});
            str2 = invoke == null ? null : invoke instanceof String ? (String) invoke : null;
        }
        return str2;
    }

    public static boolean a() {
        return Build.VERSION.SDK_INT > 27 || "P".equalsIgnoreCase(Build.VERSION.RELEASE);
    }

    public static boolean a(Context context) {
        boolean z;
        if (b) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                z = (packageInfo == null || (packageInfo.applicationInfo.flags & 1) == 0 || (packageInfo.applicationInfo.flags & 128) != 0) ? false : true;
            } catch (Throwable th) {
                z = false;
            }
            c = z;
            b = false;
        }
        return c;
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00c1 A[SYNTHETIC, Splitter:B:56:0x00c1] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00c6 A[SYNTHETIC, Splitter:B:59:0x00c6] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00d2 A[SYNTHETIC, Splitter:B:65:0x00d2] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x00d7 A[SYNTHETIC, Splitter:B:68:0x00d7] */
    /* JADX WARNING: Removed duplicated region for block: B:95:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(java.util.zip.ZipFile r11, java.util.zip.ZipEntry r12, java.io.File r13) {
        /*
            r0 = 1
            r1 = 0
            r3 = 0
            if (r11 == 0) goto L_0x0009
            if (r12 == 0) goto L_0x0009
            if (r13 != 0) goto L_0x000b
        L_0x0009:
            r0 = r1
        L_0x000a:
            return r0
        L_0x000b:
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x00bc, all -> 0x00cd }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00bc, all -> 0x00cd }
            r4.<init>()     // Catch:{ Exception -> 0x00bc, all -> 0x00cd }
            java.lang.String r5 = r13.getAbsolutePath()     // Catch:{ Exception -> 0x00bc, all -> 0x00cd }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Exception -> 0x00bc, all -> 0x00cd }
            java.lang.String r5 = ".tmp."
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Exception -> 0x00bc, all -> 0x00cd }
            int r5 = android.os.Process.myPid()     // Catch:{ Exception -> 0x00bc, all -> 0x00cd }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ Exception -> 0x00bc, all -> 0x00cd }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x00bc, all -> 0x00cd }
            r2.<init>(r4)     // Catch:{ Exception -> 0x00bc, all -> 0x00cd }
            boolean r4 = r2.exists()     // Catch:{ Exception -> 0x00f6, all -> 0x00ea }
            if (r4 == 0) goto L_0x0039
            r2.delete()     // Catch:{ Exception -> 0x00f6, all -> 0x00ea }
        L_0x0039:
            java.io.InputStream r5 = r11.getInputStream(r12)     // Catch:{ Exception -> 0x00f6, all -> 0x00ea }
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00f9, all -> 0x00ed }
            r4.<init>(r2)     // Catch:{ Exception -> 0x00f9, all -> 0x00ed }
            r6 = 8192(0x2000, float:1.14794E-41)
            byte[] r7 = new byte[r6]     // Catch:{ Exception -> 0x00fc, all -> 0x00ef }
            int r6 = r5.read(r7)     // Catch:{ Exception -> 0x00fc, all -> 0x00ef }
        L_0x004a:
            r8 = -1
            if (r6 == r8) goto L_0x0056
            r8 = 0
            r4.write(r7, r8, r6)     // Catch:{ Exception -> 0x00fc, all -> 0x00ef }
            int r6 = r5.read(r7)     // Catch:{ Exception -> 0x00fc, all -> 0x00ef }
            goto L_0x004a
        L_0x0056:
            r5.close()     // Catch:{ Exception -> 0x00fc, all -> 0x00ef }
            r5 = 0
            r4.flush()     // Catch:{ Exception -> 0x0100, all -> 0x00f2 }
            r4.close()     // Catch:{ Exception -> 0x0100, all -> 0x00f2 }
            r4 = 0
            boolean r6 = r13.exists()     // Catch:{ Exception -> 0x00f6, all -> 0x00ea }
            if (r6 == 0) goto L_0x00a2
            long r6 = r13.length()     // Catch:{ Exception -> 0x00f6, all -> 0x00ea }
            long r8 = r12.getSize()     // Catch:{ Exception -> 0x00f6, all -> 0x00ea }
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 != 0) goto L_0x00a2
            r1 = r0
        L_0x0074:
            if (r3 == 0) goto L_0x0079
            r5.close()     // Catch:{ Exception -> 0x00de }
        L_0x0079:
            if (r3 == 0) goto L_0x007e
            r4.close()     // Catch:{ Exception -> 0x00e0 }
        L_0x007e:
            r2.delete()
        L_0x0081:
            if (r1 != 0) goto L_0x009f
            boolean r2 = r13.exists()
            if (r2 == 0) goto L_0x009f
            long r2 = r13.length()
            r4 = 0
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 <= 0) goto L_0x009f
            long r2 = r13.length()
            long r4 = r12.getSize()
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 == 0) goto L_0x000a
        L_0x009f:
            r0 = r1
            goto L_0x000a
        L_0x00a2:
            boolean r6 = r2.exists()     // Catch:{ Exception -> 0x00f6, all -> 0x00ea }
            if (r6 == 0) goto L_0x0074
            long r6 = r2.length()     // Catch:{ Exception -> 0x00f6, all -> 0x00ea }
            long r8 = r12.getSize()     // Catch:{ Exception -> 0x00f6, all -> 0x00ea }
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 != 0) goto L_0x0074
            r13.delete()     // Catch:{ Exception -> 0x00f6, all -> 0x00ea }
            boolean r1 = r2.renameTo(r13)     // Catch:{ Exception -> 0x00f6, all -> 0x00ea }
            goto L_0x0074
        L_0x00bc:
            r2 = move-exception
            r2 = r3
            r4 = r3
        L_0x00bf:
            if (r4 == 0) goto L_0x00c4
            r4.close()     // Catch:{ Exception -> 0x00e2 }
        L_0x00c4:
            if (r3 == 0) goto L_0x00c9
            r3.close()     // Catch:{ Exception -> 0x00e4 }
        L_0x00c9:
            r2.delete()
            goto L_0x0081
        L_0x00cd:
            r0 = move-exception
            r2 = r3
            r5 = r3
        L_0x00d0:
            if (r5 == 0) goto L_0x00d5
            r5.close()     // Catch:{ Exception -> 0x00e6 }
        L_0x00d5:
            if (r3 == 0) goto L_0x00da
            r3.close()     // Catch:{ Exception -> 0x00e8 }
        L_0x00da:
            r2.delete()
            throw r0
        L_0x00de:
            r5 = move-exception
            goto L_0x0079
        L_0x00e0:
            r3 = move-exception
            goto L_0x007e
        L_0x00e2:
            r4 = move-exception
            goto L_0x00c4
        L_0x00e4:
            r3 = move-exception
            goto L_0x00c9
        L_0x00e6:
            r1 = move-exception
            goto L_0x00d5
        L_0x00e8:
            r1 = move-exception
            goto L_0x00da
        L_0x00ea:
            r0 = move-exception
            r5 = r3
            goto L_0x00d0
        L_0x00ed:
            r0 = move-exception
            goto L_0x00d0
        L_0x00ef:
            r0 = move-exception
            r3 = r4
            goto L_0x00d0
        L_0x00f2:
            r0 = move-exception
            r5 = r3
            r3 = r4
            goto L_0x00d0
        L_0x00f6:
            r4 = move-exception
            r4 = r3
            goto L_0x00bf
        L_0x00f9:
            r4 = move-exception
            r4 = r5
            goto L_0x00bf
        L_0x00fc:
            r3 = move-exception
            r3 = r4
            r4 = r5
            goto L_0x00bf
        L_0x0100:
            r5 = move-exception
            r10 = r4
            r4 = r3
            r3 = r10
            goto L_0x00bf
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.utils.f.a(java.util.zip.ZipFile, java.util.zip.ZipEntry, java.io.File):boolean");
    }

    public static boolean b(Context context) {
        boolean z;
        if (f) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                z = (packageInfo == null || (packageInfo.applicationInfo.flags & 128) == 0) ? false : true;
            } catch (Throwable th) {
                z = false;
            }
            g = z;
            f = false;
        }
        return g;
    }

    public static boolean c(Context context) {
        if (h) {
            try {
                i = d(context).equals(context.getPackageName());
                h = false;
            } catch (Exception e2) {
            }
        }
        return i;
    }

    public static String d(Context context) {
        try {
            int myPid = Process.myPid();
            if (context != null) {
                for (ActivityManager.RunningAppProcessInfo next : ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
                    if (next.pid == myPid) {
                        return next.processName != null ? next.processName : "";
                    }
                }
            }
            return "";
        } catch (Throwable th) {
            return "";
        }
    }
}
