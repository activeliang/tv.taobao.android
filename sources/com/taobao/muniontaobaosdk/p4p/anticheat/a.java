package com.taobao.muniontaobaosdk.p4p.anticheat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.muniontaobaosdk.util.TaoLog;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class a {
    /* access modifiers changed from: private */
    public static final String a = a.class.getName();

    /* renamed from: com.taobao.muniontaobaosdk.p4p.anticheat.a$a  reason: collision with other inner class name */
    public static class C0007a {
        private static final String a = "SHA-1";

        public static byte[] a(byte[] bArr) {
            if (bArr == null || bArr.length <= 0) {
                return null;
            }
            try {
                MessageDigest instance = MessageDigest.getInstance("SHA-1");
                instance.update(bArr);
                return instance.digest();
            } catch (NoSuchAlgorithmException e) {
                TaoLog.Logd(a.a, "SHA-1 encode exception,info:" + e.toString());
                return null;
            } catch (Exception e2) {
                TaoLog.Logd(a.a, "SHA-1 encode exception,info:" + e2.toString());
                return null;
            }
        }
    }

    public static double a(double d) {
        return (double) new BigDecimal(Double.toString(d)).subtract(new BigDecimal(Integer.toString((int) d))).floatValue();
    }

    public static int a() {
        return (Build.VERSION.SDK_INT & 255) | 256;
    }

    public static int a(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static String m7a() {
        return Build.MODEL;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static String m8a(Context context) {
        WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
        if (connectionInfo != null) {
            return connectionInfo.getMacAddress();
        }
        return null;
    }

    public static String a(String str) {
        if (str != null) {
            return str.replace("\r\n", "").replace("\r", "").replace("\n", "");
        }
        return null;
    }

    public static int b() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static int b(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /* renamed from: b  reason: collision with other method in class */
    public static String m9b() {
        String str = Build.MANUFACTURER;
        return (str == null || str.length() <= 32) ? str : str.substring(0, 32);
    }

    /* renamed from: b  reason: collision with other method in class */
    public static String m10b(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
    }

    public static int c(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.densityDpi;
    }

    /* renamed from: c  reason: collision with other method in class */
    public static String m11c(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
    }

    public static int d(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), "screen_brightness");
        } catch (Settings.SettingNotFoundException e) {
            TaoLog.Logd(a, "Get screen bright exception,info:" + e.toString());
            return 0;
        }
    }

    /* renamed from: d  reason: collision with other method in class */
    public static String m12d(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo != null) {
                return packageInfo.packageName;
            }
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            TaoLog.Logd(a, "Get package name exception,info:" + e.toString());
            return null;
        }
    }

    public static int e(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() == 1) {
                return 1;
            }
            if (activeNetworkInfo.getType() == 0) {
                return (activeNetworkInfo.getSubtype() == 2 || activeNetworkInfo.getSubtype() == 1 || activeNetworkInfo.getSubtype() == 4) ? 2 : 3;
            }
        }
        return 0;
    }

    public static int f(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getNetworkType();
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x009f A[SYNTHETIC, Splitter:B:27:0x009f] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00a4 A[SYNTHETIC, Splitter:B:30:0x00a4] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0109 A[SYNTHETIC, Splitter:B:41:0x0109] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x010e A[SYNTHETIC, Splitter:B:44:0x010e] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0157 A[SYNTHETIC, Splitter:B:53:0x0157] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x015c A[SYNTHETIC, Splitter:B:56:0x015c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int g(android.content.Context r7) {
        /*
            r3 = 0
            r0 = -1
            java.lang.String r1 = "/proc/meminfo"
            java.io.FileReader r4 = new java.io.FileReader     // Catch:{ IOException -> 0x007e, Exception -> 0x00e7, all -> 0x0152 }
            r4.<init>(r1)     // Catch:{ IOException -> 0x007e, Exception -> 0x00e7, all -> 0x0152 }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x01ad, Exception -> 0x01a6, all -> 0x019e }
            r1 = 1024(0x400, float:1.435E-42)
            r2.<init>(r4, r1)     // Catch:{ IOException -> 0x01ad, Exception -> 0x01a6, all -> 0x019e }
            java.lang.String r1 = r2.readLine()     // Catch:{ IOException -> 0x01b2, Exception -> 0x01aa }
            if (r1 == 0) goto L_0x0035
            java.lang.String r3 = r1.trim()     // Catch:{ IOException -> 0x01b2, Exception -> 0x01aa }
            int r3 = r3.length()     // Catch:{ IOException -> 0x01b2, Exception -> 0x01aa }
            if (r3 <= 0) goto L_0x0035
            java.lang.String r3 = "\\s+"
            java.lang.String[] r1 = r1.split(r3)     // Catch:{ IOException -> 0x01b2, Exception -> 0x01aa }
            r3 = 1
            r1 = r1[r3]     // Catch:{ IOException -> 0x01b2, Exception -> 0x01aa }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ IOException -> 0x01b2, Exception -> 0x01aa }
            int r1 = r1.intValue()     // Catch:{ IOException -> 0x01b2, Exception -> 0x01aa }
            int r0 = r1 / 1024
        L_0x0035:
            if (r2 == 0) goto L_0x003a
            r2.close()     // Catch:{ IOException -> 0x0040 }
        L_0x003a:
            if (r4 == 0) goto L_0x003f
            r4.close()     // Catch:{ IOException -> 0x005f }
        L_0x003f:
            return r0
        L_0x0040:
            r1 = move-exception
            java.lang.String r2 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "After get total memory,close buffer reader exception,info:"
            java.lang.StringBuilder r3 = r3.append(r5)
            java.lang.String r1 = r1.toString()
            java.lang.StringBuilder r1 = r3.append(r1)
            java.lang.String r1 = r1.toString()
            com.taobao.muniontaobaosdk.util.TaoLog.Logd(r2, r1)
            goto L_0x003a
        L_0x005f:
            r1 = move-exception
            java.lang.String r2 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "After get total memory,close file reader exception,info:"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r1 = r1.toString()
            java.lang.StringBuilder r1 = r3.append(r1)
            java.lang.String r1 = r1.toString()
            com.taobao.muniontaobaosdk.util.TaoLog.Logd(r2, r1)
            goto L_0x003f
        L_0x007e:
            r1 = move-exception
            r2 = r3
        L_0x0080:
            java.lang.String r4 = a     // Catch:{ all -> 0x01a3 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x01a3 }
            r5.<init>()     // Catch:{ all -> 0x01a3 }
            java.lang.String r6 = "Get total memory exception,info:"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x01a3 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x01a3 }
            java.lang.StringBuilder r1 = r5.append(r1)     // Catch:{ all -> 0x01a3 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x01a3 }
            com.taobao.muniontaobaosdk.util.TaoLog.Logd(r4, r1)     // Catch:{ all -> 0x01a3 }
            if (r2 == 0) goto L_0x00a2
            r2.close()     // Catch:{ IOException -> 0x00c8 }
        L_0x00a2:
            if (r3 == 0) goto L_0x003f
            r3.close()     // Catch:{ IOException -> 0x00a8 }
            goto L_0x003f
        L_0x00a8:
            r1 = move-exception
            java.lang.String r2 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "After get total memory,close file reader exception,info:"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r1 = r1.toString()
            java.lang.StringBuilder r1 = r3.append(r1)
            java.lang.String r1 = r1.toString()
            com.taobao.muniontaobaosdk.util.TaoLog.Logd(r2, r1)
            goto L_0x003f
        L_0x00c8:
            r1 = move-exception
            java.lang.String r2 = a
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "After get total memory,close buffer reader exception,info:"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r1 = r1.toString()
            java.lang.StringBuilder r1 = r4.append(r1)
            java.lang.String r1 = r1.toString()
            com.taobao.muniontaobaosdk.util.TaoLog.Logd(r2, r1)
            goto L_0x00a2
        L_0x00e7:
            r1 = move-exception
            r2 = r3
            r4 = r3
        L_0x00ea:
            java.lang.String r3 = a     // Catch:{ all -> 0x01a1 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x01a1 }
            r5.<init>()     // Catch:{ all -> 0x01a1 }
            java.lang.String r6 = "Get total memory exception,info:"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x01a1 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x01a1 }
            java.lang.StringBuilder r1 = r5.append(r1)     // Catch:{ all -> 0x01a1 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x01a1 }
            com.taobao.muniontaobaosdk.util.TaoLog.Logd(r3, r1)     // Catch:{ all -> 0x01a1 }
            if (r2 == 0) goto L_0x010c
            r2.close()     // Catch:{ IOException -> 0x0133 }
        L_0x010c:
            if (r4 == 0) goto L_0x003f
            r4.close()     // Catch:{ IOException -> 0x0113 }
            goto L_0x003f
        L_0x0113:
            r1 = move-exception
            java.lang.String r2 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "After get total memory,close file reader exception,info:"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r1 = r1.toString()
            java.lang.StringBuilder r1 = r3.append(r1)
            java.lang.String r1 = r1.toString()
            com.taobao.muniontaobaosdk.util.TaoLog.Logd(r2, r1)
            goto L_0x003f
        L_0x0133:
            r1 = move-exception
            java.lang.String r2 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "After get total memory,close buffer reader exception,info:"
            java.lang.StringBuilder r3 = r3.append(r5)
            java.lang.String r1 = r1.toString()
            java.lang.StringBuilder r1 = r3.append(r1)
            java.lang.String r1 = r1.toString()
            com.taobao.muniontaobaosdk.util.TaoLog.Logd(r2, r1)
            goto L_0x010c
        L_0x0152:
            r0 = move-exception
            r2 = r3
            r4 = r3
        L_0x0155:
            if (r2 == 0) goto L_0x015a
            r2.close()     // Catch:{ IOException -> 0x0160 }
        L_0x015a:
            if (r4 == 0) goto L_0x015f
            r4.close()     // Catch:{ IOException -> 0x017f }
        L_0x015f:
            throw r0
        L_0x0160:
            r1 = move-exception
            java.lang.String r2 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "After get total memory,close buffer reader exception,info:"
            java.lang.StringBuilder r3 = r3.append(r5)
            java.lang.String r1 = r1.toString()
            java.lang.StringBuilder r1 = r3.append(r1)
            java.lang.String r1 = r1.toString()
            com.taobao.muniontaobaosdk.util.TaoLog.Logd(r2, r1)
            goto L_0x015a
        L_0x017f:
            r1 = move-exception
            java.lang.String r2 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "After get total memory,close file reader exception,info:"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r1 = r1.toString()
            java.lang.StringBuilder r1 = r3.append(r1)
            java.lang.String r1 = r1.toString()
            com.taobao.muniontaobaosdk.util.TaoLog.Logd(r2, r1)
            goto L_0x015f
        L_0x019e:
            r0 = move-exception
            r2 = r3
            goto L_0x0155
        L_0x01a1:
            r0 = move-exception
            goto L_0x0155
        L_0x01a3:
            r0 = move-exception
            r4 = r3
            goto L_0x0155
        L_0x01a6:
            r1 = move-exception
            r2 = r3
            goto L_0x00ea
        L_0x01aa:
            r1 = move-exception
            goto L_0x00ea
        L_0x01ad:
            r1 = move-exception
            r2 = r3
            r3 = r4
            goto L_0x0080
        L_0x01b2:
            r1 = move-exception
            r3 = r4
            goto L_0x0080
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.muniontaobaosdk.p4p.anticheat.a.g(android.content.Context):int");
    }

    public static int h(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getMemoryInfo(memoryInfo);
        return new Long(memoryInfo.availMem / 1048576).intValue();
    }
}
