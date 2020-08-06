package com.loc;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.ServiceInfo;
import android.location.Location;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.alibaba.analytics.core.Constants;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.DPoint;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import org.json.JSONObject;

/* compiled from: Utils */
public final class et {
    static WifiManager a = null;
    private static int b = 0;
    private static String[] c = null;
    private static String[] d = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    private static String e = "android.permission.ACCESS_BACKGROUND_LOCATION";
    private static boolean f = false;

    public static double a(double d2) {
        return b(d2);
    }

    public static float a(float f2) {
        return (float) (((double) ((long) (((double) f2) * 100.0d))) / 100.0d);
    }

    public static float a(AMapLocation aMapLocation, AMapLocation aMapLocation2) {
        return a(new double[]{aMapLocation.getLatitude(), aMapLocation.getLongitude(), aMapLocation2.getLatitude(), aMapLocation2.getLongitude()});
    }

    public static float a(DPoint dPoint, DPoint dPoint2) {
        return a(new double[]{dPoint.getLatitude(), dPoint.getLongitude(), dPoint2.getLatitude(), dPoint2.getLongitude()});
    }

    public static float a(double[] dArr) {
        float[] fArr = new float[1];
        Location.distanceBetween(dArr[0], dArr[1], dArr[2], dArr[3], fArr);
        return fArr[0];
    }

    public static int a(int i) {
        return (i * 2) - 113;
    }

    public static int a(NetworkInfo networkInfo) {
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            return networkInfo.getType();
        }
        return -1;
    }

    public static long a() {
        return System.currentTimeMillis();
    }

    public static Object a(Context context, String str) {
        if (context == null) {
            return null;
        }
        try {
            return context.getApplicationContext().getSystemService(str);
        } catch (Throwable th) {
            en.a(th, "Utils", "getServ");
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x001a  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0020 A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(long r4, java.lang.String r6) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 == 0) goto L_0x0009
            java.lang.String r6 = "yyyy-MM-dd HH:mm:ss"
        L_0x0009:
            r2 = 0
            java.text.SimpleDateFormat r1 = new java.text.SimpleDateFormat     // Catch:{ Throwable -> 0x0024 }
            java.util.Locale r0 = java.util.Locale.CHINA     // Catch:{ Throwable -> 0x0024 }
            r1.<init>(r6, r0)     // Catch:{ Throwable -> 0x0024 }
            r1.applyPattern(r6)     // Catch:{ Throwable -> 0x0039 }
        L_0x0014:
            r2 = 0
            int r0 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r0 > 0) goto L_0x001e
            long r4 = java.lang.System.currentTimeMillis()
        L_0x001e:
            if (r1 != 0) goto L_0x0030
            java.lang.String r0 = "NULL"
        L_0x0023:
            return r0
        L_0x0024:
            r0 = move-exception
            r1 = r2
        L_0x0026:
            java.lang.String r2 = "Utils"
            java.lang.String r3 = "formatUTC"
            com.loc.en.a(r0, r2, r3)
            goto L_0x0014
        L_0x0030:
            java.lang.Long r0 = java.lang.Long.valueOf(r4)
            java.lang.String r0 = r1.format(r0)
            goto L_0x0023
        L_0x0039:
            r0 = move-exception
            goto L_0x0026
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.et.a(long, java.lang.String):java.lang.String");
    }

    public static boolean a(long j, long j2) {
        String a2 = a(j, "yyyyMMdd");
        String a3 = a(j2, "yyyyMMdd");
        if ("NULL".equals(a2) || "NULL".equals(a3)) {
            return false;
        }
        return a2.equals(a3);
    }

    public static boolean a(Context context) {
        if (context == null) {
            return false;
        }
        try {
            return c() < 17 ? c(context, "android.provider.Settings$System") : c(context, "android.provider.Settings$Global");
        } catch (Throwable th) {
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0082  */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(android.database.sqlite.SQLiteDatabase r11, java.lang.String r12) {
        /*
            r8 = 1
            r10 = 0
            r9 = 0
            boolean r0 = android.text.TextUtils.isEmpty(r12)
            if (r0 == 0) goto L_0x000b
            r0 = r9
        L_0x000a:
            return r0
        L_0x000b:
            java.lang.String r0 = "2.0.201501131131"
            java.lang.String r1 = "."
            java.lang.String r2 = ""
            java.lang.String r0 = r0.replace(r1, r2)
            if (r11 == 0) goto L_0x0020
            boolean r1 = r11.isOpen()     // Catch:{ Throwable -> 0x006c, all -> 0x0075 }
            if (r1 != 0) goto L_0x0022
        L_0x0020:
            r0 = r9
            goto L_0x000a
        L_0x0022:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x006c, all -> 0x0075 }
            java.lang.String r2 = "type = 'table' AND name = '"
            r1.<init>(r2)     // Catch:{ Throwable -> 0x006c, all -> 0x0075 }
            java.lang.String r2 = r12.trim()     // Catch:{ Throwable -> 0x006c, all -> 0x0075 }
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x006c, all -> 0x0075 }
            java.lang.StringBuilder r0 = r1.append(r0)     // Catch:{ Throwable -> 0x006c, all -> 0x0075 }
            java.lang.String r1 = "'"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x006c, all -> 0x0075 }
            java.lang.String r3 = r0.toString()     // Catch:{ Throwable -> 0x006c, all -> 0x0075 }
            java.lang.String r1 = "sqlite_master"
            r0 = 1
            java.lang.String[] r2 = new java.lang.String[r0]     // Catch:{ Throwable -> 0x006c, all -> 0x0075 }
            r0 = 0
            java.lang.String r4 = "count(*) as c"
            r2[r0] = r4     // Catch:{ Throwable -> 0x006c, all -> 0x0075 }
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r0 = r11
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x006c, all -> 0x0075 }
            if (r1 == 0) goto L_0x0084
            boolean r0 = r1.moveToFirst()     // Catch:{ Throwable -> 0x007f, all -> 0x007c }
            if (r0 == 0) goto L_0x0084
            r0 = 0
            int r0 = r1.getInt(r0)     // Catch:{ Throwable -> 0x007f, all -> 0x007c }
            if (r0 <= 0) goto L_0x0084
            r0 = r8
        L_0x0066:
            if (r1 == 0) goto L_0x000a
            r1.close()
            goto L_0x000a
        L_0x006c:
            r0 = move-exception
            r0 = r10
        L_0x006e:
            if (r0 == 0) goto L_0x0082
            r0.close()
            r0 = r8
            goto L_0x000a
        L_0x0075:
            r0 = move-exception
        L_0x0076:
            if (r10 == 0) goto L_0x007b
            r10.close()
        L_0x007b:
            throw r0
        L_0x007c:
            r0 = move-exception
            r10 = r1
            goto L_0x0076
        L_0x007f:
            r0 = move-exception
            r0 = r1
            goto L_0x006e
        L_0x0082:
            r0 = r8
            goto L_0x000a
        L_0x0084:
            r0 = r9
            goto L_0x0066
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.et.a(android.database.sqlite.SQLiteDatabase, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0024  */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x001f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(android.location.Location r7, int r8) {
        /*
            r6 = 0
            r2 = 1
            r3 = 0
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r3)
            int r0 = android.os.Build.VERSION.SDK_INT
            r4 = 18
            if (r0 < r4) goto L_0x0022
            java.lang.String r0 = "isFromMockProvider"
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0021 }
            java.lang.Object r0 = com.loc.eq.a(r7, r0, r4)     // Catch:{ Throwable -> 0x0021 }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x0021 }
        L_0x0019:
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0024
            r0 = r2
        L_0x0020:
            return r0
        L_0x0021:
            r0 = move-exception
        L_0x0022:
            r0 = r1
            goto L_0x0019
        L_0x0024:
            android.os.Bundle r0 = r7.getExtras()
            if (r0 == 0) goto L_0x0055
            java.lang.String r1 = "satellites"
            int r0 = r0.getInt(r1)
        L_0x0031:
            if (r0 > 0) goto L_0x0035
            r0 = r2
            goto L_0x0020
        L_0x0035:
            if (r8 != 0) goto L_0x0053
            double r0 = r7.getAltitude()
            r4 = 0
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 != 0) goto L_0x0053
            float r0 = r7.getBearing()
            int r0 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r0 != 0) goto L_0x0053
            float r0 = r7.getSpeed()
            int r0 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r0 != 0) goto L_0x0053
            r0 = r2
            goto L_0x0020
        L_0x0053:
            r0 = r3
            goto L_0x0020
        L_0x0055:
            r0 = r3
            goto L_0x0031
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.et.a(android.location.Location, int):boolean");
    }

    public static boolean a(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            return b(aMapLocation);
        }
        return false;
    }

    public static boolean a(dw dwVar) {
        if (dwVar != null && !Constants.LogTransferLevel.HIGH.equals(dwVar.d()) && !"5".equals(dwVar.d()) && !Constants.LogTransferLevel.L6.equals(dwVar.d())) {
            return b((AMapLocation) dwVar);
        }
        return false;
    }

    public static boolean a(String str) {
        return !TextUtils.isEmpty(str) && !"00:00:00:00:00:00".equals(str) && !str.contains(" :");
    }

    public static boolean a(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        ArrayList<String> d2 = d(str);
        String[] split = str2.toString().split(Constant.INTENT_JSON_MARK);
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < split.length; i3++) {
            if (split[i3].contains(",nb") || split[i3].contains(",access")) {
                i++;
                if (d2.contains(split[i3])) {
                    i2++;
                }
            }
        }
        return ((double) (i2 * 2)) >= ((double) (d2.size() + i)) * 0.618d;
    }

    public static boolean a(JSONObject jSONObject, String str) {
        return u.a(jSONObject, str);
    }

    public static byte[] a(int i, byte[] bArr) {
        if (bArr == null || bArr.length < 2) {
            bArr = new byte[2];
        }
        bArr[0] = (byte) (i & 255);
        bArr[1] = (byte) ((65280 & i) >> 8);
        return bArr;
    }

    public static byte[] a(long j) {
        byte[] bArr = new byte[8];
        for (int i = 0; i < 8; i++) {
            bArr[i] = (byte) ((int) ((j >> (i * 8)) & 255));
        }
        return bArr;
    }

    public static byte[] a(byte[] bArr) {
        return u.b(bArr);
    }

    public static String[] a(TelephonyManager telephonyManager) {
        int i;
        String str = null;
        if (telephonyManager != null) {
            str = telephonyManager.getNetworkOperator();
        }
        String[] strArr = {"0", "0"};
        if (TextUtils.isEmpty(str) ? false : !TextUtils.isDigitsOnly(str) ? false : str.length() > 4) {
            strArr[0] = str.substring(0, 3);
            char[] charArray = str.substring(3).toCharArray();
            int i2 = 0;
            while (i2 < charArray.length && Character.isDigit(charArray[i2])) {
                i2++;
            }
            strArr[1] = str.substring(3, i2 + 3);
        }
        try {
            i = Integer.parseInt(strArr[0]);
        } catch (Throwable th) {
            en.a(th, "Utils", "getMccMnc");
            i = 0;
        }
        if (i == 0) {
            strArr[0] = "0";
        }
        if ("0".equals(strArr[0]) || "0".equals(strArr[1])) {
            return (!"0".equals(strArr[0]) || !"0".equals(strArr[1]) || c == null) ? strArr : c;
        }
        c = strArr;
        return strArr;
    }

    public static double b(double d2) {
        return ((double) ((long) (d2 * 1000000.0d))) / 1000000.0d;
    }

    public static int b(byte[] bArr) {
        int i = 0;
        for (int i2 = 0; i2 < 2; i2++) {
            i |= (bArr[i2] & OnReminderListener.RET_FULL) << ((1 - i2) * 8);
        }
        return i;
    }

    public static long b() {
        return SystemClock.elapsedRealtime();
    }

    public static String b(int i) {
        switch (i) {
            case 0:
                return BlitzServiceUtils.CSUCCESS;
            case 1:
                return "重要参数为空";
            case 2:
                return "WIFI信息不足";
            case 3:
                return "请求参数获取出现异常";
            case 4:
                return "网络连接异常";
            case 5:
                return "解析数据异常";
            case 6:
                return "定位结果错误";
            case 7:
                return "KEY错误";
            case 8:
                return "其他错误";
            case 9:
                return "初始化异常";
            case 10:
                return "定位服务启动失败";
            case 11:
                return "错误的基站信息，请检查是否插入SIM卡";
            case 12:
                return "缺少定位权限";
            case 13:
                return "网络定位失败，请检查设备是否插入sim卡，是否开启移动网络或开启了wifi模块";
            case 14:
                return "GPS 定位失败，由于设备当前 GPS 状态差,建议持设备到相对开阔的露天场所再次尝试";
            case 15:
                return "当前返回位置为模拟软件返回，请关闭模拟软件，或者在option中设置允许模拟";
            case 18:
                return "定位失败，飞行模式下关闭了WIFI开关，请关闭飞行模式或者打开WIFI开关";
            case 19:
                return "定位失败，没有检查到SIM卡，并且关闭了WIFI开关，请打开WIFI开关或者插入SIM卡";
            default:
                return "其他错误";
        }
    }

    public static String b(Context context) {
        PackageInfo packageInfo;
        CharSequence charSequence = null;
        if (!TextUtils.isEmpty(en.h)) {
            return en.h;
        }
        if (context == null) {
            return null;
        }
        try {
            packageInfo = context.getPackageManager().getPackageInfo(k.c(context), 64);
        } catch (Throwable th) {
            en.a(th, "Utils", "getAppName part");
            packageInfo = null;
        }
        try {
            if (TextUtils.isEmpty(en.i)) {
                en.i = null;
            }
        } catch (Throwable th2) {
            en.a(th2, "Utils", "getAppName");
        }
        StringBuilder sb = new StringBuilder();
        if (packageInfo != null) {
            if (packageInfo.applicationInfo != null) {
                charSequence = packageInfo.applicationInfo.loadLabel(context.getPackageManager());
            }
            if (charSequence != null) {
                sb.append(charSequence.toString());
            }
            if (!TextUtils.isEmpty(packageInfo.versionName)) {
                sb.append(packageInfo.versionName);
            }
        }
        String c2 = k.c(context);
        if (!TextUtils.isEmpty(c2)) {
            sb.append(",").append(c2);
        }
        if (!TextUtils.isEmpty(en.i)) {
            sb.append(",").append(en.i);
        }
        String sb2 = sb.toString();
        en.h = sb2;
        return sb2;
    }

    public static String b(TelephonyManager telephonyManager) {
        int i = 0;
        if (telephonyManager != null) {
            try {
                i = telephonyManager.getNetworkType();
            } catch (Throwable th) {
            }
        }
        switch (i) {
            case 0:
                return "UNKWN";
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 4:
                return "CDMA";
            case 5:
                return "EVDO_0";
            case 6:
                return "EVDO_A";
            case 7:
                return "1xRTT";
            case 8:
                return "HSDPA";
            case 9:
                return "HSUPA";
            case 10:
                return "HSPA";
            case 11:
                return "IDEN";
            case 12:
                return "EVDO_B";
            case 13:
                return "LTE";
            case 14:
                return "EHRPD";
            case 15:
                return "HSPAP";
            default:
                return "UNKWN";
        }
    }

    public static boolean b(long j, long j2) {
        boolean z = true;
        if (!a(j, j2)) {
            return false;
        }
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        instance.setTimeInMillis(j);
        int i = instance.get(11);
        instance.setTimeInMillis(j2);
        int i2 = instance.get(11);
        if (i <= 12 ? i2 > 12 : i2 <= 12) {
            z = false;
        }
        return z;
    }

    public static boolean b(Context context, String str) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(str, 256);
        } catch (Throwable th) {
        }
        return packageInfo != null;
    }

    public static boolean b(AMapLocation aMapLocation) {
        double longitude = aMapLocation.getLongitude();
        double latitude = aMapLocation.getLatitude();
        return !(longitude == ClientTraceData.b.f47a && latitude == ClientTraceData.b.f47a) && longitude <= 180.0d && latitude <= 90.0d && longitude >= -180.0d && latitude >= -90.0d;
    }

    public static byte[] b(int i, byte[] bArr) {
        if (bArr == null || bArr.length < 4) {
            bArr = new byte[4];
        }
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = (byte) ((i >> (i2 * 8)) & 255);
        }
        return bArr;
    }

    public static byte[] b(String str) {
        return a(Integer.parseInt(str), (byte[]) null);
    }

    public static double c(double d2) {
        return ((double) ((long) (d2 * 100.0d))) / 100.0d;
    }

    public static int c() {
        if (b > 0) {
            return b;
        }
        try {
            return eq.b("android.os.Build$VERSION", "SDK_INT");
        } catch (Throwable th) {
            return 0;
        }
    }

    public static NetworkInfo c(Context context) {
        try {
            return n.s(context);
        } catch (Throwable th) {
            en.a(th, "Utils", "getNetWorkInfo");
            return null;
        }
    }

    private static boolean c(Context context, String str) throws Throwable {
        return ((Integer) eq.a(str, "getInt", new Object[]{context.getContentResolver(), ((String) eq.a(str, "AIRPLANE_MODE_ON")).toString()}, (Class<?>[]) new Class[]{ContentResolver.class, String.class})).intValue() == 1;
    }

    public static byte[] c(String str) {
        return b(Integer.parseInt(str), (byte[]) null);
    }

    public static int d() {
        return new Random().nextInt(65536) - 32768;
    }

    public static ArrayList<String> d(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split(Constant.INTENT_JSON_MARK);
            for (int i = 0; i < split.length; i++) {
                if (split[i].contains(",nb") || split[i].contains(",access")) {
                    arrayList.add(split[i]);
                }
            }
        }
        return arrayList;
    }

    public static boolean d(Context context) {
        try {
            NetworkInfo c2 = c(context);
            return c2 != null && c2.isConnectedOrConnecting();
        } catch (Throwable th) {
            return false;
        }
    }

    public static double e(String str) throws NumberFormatException {
        return Double.parseDouble(str);
    }

    public static String e() {
        try {
            return o.b("S128DF1572465B890OE3F7A13167KLEI".getBytes("UTF-8")).substring(20);
        } catch (Throwable th) {
            return "";
        }
    }

    public static boolean e(Context context) {
        try {
            for (ActivityManager.RunningAppProcessInfo next : ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
                if (next.processName.equals(k.c(context))) {
                    return next.importance != 100;
                }
            }
            return false;
        } catch (Throwable th) {
            en.a(th, "Utils", "isApplicationBroughtToBackground");
            return true;
        }
    }

    public static float f(String str) throws NumberFormatException {
        return Float.parseFloat(str);
    }

    public static boolean f(Context context) {
        int i;
        if (Build.VERSION.SDK_INT < 23 || context.getApplicationInfo().targetSdkVersion < 23) {
            for (String checkCallingOrSelfPermission : d) {
                if (context.checkCallingOrSelfPermission(checkCallingOrSelfPermission) != 0) {
                    return false;
                }
            }
            return true;
        }
        Application application = (Application) context;
        for (String str : d) {
            try {
                i = eq.b(application.getBaseContext(), "checkSelfPermission", str);
            } catch (Throwable th) {
                i = 0;
            }
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    public static int g(String str) throws NumberFormatException {
        return Integer.parseInt(str);
    }

    public static boolean g(Context context) {
        int i;
        if (context.getApplicationInfo().targetSdkVersion >= 29) {
            try {
                i = eq.b(((Application) context).getBaseContext(), "checkSelfPermission", e);
            } catch (Throwable th) {
                i = 0;
            }
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    public static int h(String str) throws NumberFormatException {
        return Integer.parseInt(str, 16);
    }

    @SuppressLint({"NewApi"})
    public static boolean h(Context context) {
        boolean z;
        if (context == null) {
            return true;
        }
        if (a == null) {
            a = (WifiManager) a(context, "wifi");
        }
        try {
            z = a.isWifiEnabled();
        } catch (Throwable th) {
            z = false;
        }
        if (z || c() <= 17) {
            return z;
        }
        try {
            return "true".equals(String.valueOf(eq.a(a, "isScanAlwaysAvailable", new Object[0])));
        } catch (Throwable th2) {
            return z;
        }
    }

    public static byte i(String str) throws NumberFormatException {
        return Byte.parseByte(str);
    }

    public static String i(Context context) {
        NetworkInfo c2 = c(context);
        if (c2 == null || !c2.isConnectedOrConnecting()) {
            return "DISCONNECTED";
        }
        int type = c2.getType();
        if (type == 1) {
            return "WIFI";
        }
        if (type != 0) {
            return "UNKNOWN";
        }
        String subtypeName = c2.getSubtypeName();
        switch (c2.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
            case 16:
                return "2G";
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
            case 17:
                return "3G";
            case 13:
                return "4G";
            default:
                return "GSM".equalsIgnoreCase(subtypeName) ? "2G" : ("TD-SCDMA".equalsIgnoreCase(subtypeName) || "WCDMA".equalsIgnoreCase(subtypeName) || "CDMA2000".equalsIgnoreCase(subtypeName)) ? "3G" : subtypeName;
        }
    }

    public static String j(Context context) {
        String m = n.m(context);
        if (TextUtils.isEmpty(m) || m.equals("00:00:00:00:00:00")) {
            m = "00:00:00:00:00:00";
            if (context != null) {
                m = es.a(context, "pref", "smac", m);
            }
        }
        if (TextUtils.isEmpty(m)) {
            m = "00:00:00:00:00:00";
        }
        if (!f) {
            if (context != null && !TextUtils.isEmpty(m)) {
                SharedPreferences.Editor a2 = es.a(context, "pref");
                es.a(a2, "smac", m);
                es.a(a2);
            }
            f = true;
        }
        return m;
    }

    public static boolean k(Context context) {
        return Build.VERSION.SDK_INT >= 28 && context.getApplicationInfo().targetSdkVersion >= 28;
    }

    public static boolean l(Context context) {
        ServiceInfo serviceInfo = null;
        try {
            serviceInfo = context.getPackageManager().getServiceInfo(new ComponentName(context, "com.amap.api.location.APSService"), 128);
        } catch (Throwable th) {
        }
        return serviceInfo != null;
    }
}
