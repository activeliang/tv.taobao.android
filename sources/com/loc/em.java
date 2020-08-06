package com.loc;

import android.content.Context;
import android.content.SharedPreferences;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.loc.l;
import java.util.ArrayList;
import org.json.JSONObject;

/* compiled from: AuthUtil */
public final class em {
    private static boolean A = false;
    private static int B = 20;
    private static boolean C = true;
    private static boolean D = true;
    private static int E = -1;
    private static long F = 0;
    private static ArrayList<String> G = new ArrayList<>();
    private static int H = -1;
    private static long I = 0;
    private static ArrayList<String> J = new ArrayList<>();
    private static boolean K = false;
    private static int L = 3000;
    private static int M = 3000;
    private static boolean N = true;
    private static long O = 300000;
    private static boolean P = false;
    private static long Q = 0;
    private static int R = 0;
    private static int S = 0;
    private static boolean T = true;
    private static int U = 80;
    private static boolean V = false;
    private static boolean W = true;
    private static boolean X = false;
    private static boolean Y = true;
    private static long Z = -1;
    public static boolean a = true;
    private static boolean aa = true;
    private static int ab = 1;
    private static boolean ac = true;
    private static int ad = 5;
    private static boolean ae = false;
    private static String af = "CMjAzLjEwNy4xLjEvMTU0MDgxL2Q";
    private static long ag = 0;
    static boolean b = false;
    static boolean c = false;
    static int d = 3600000;
    static long e = 0;
    static long f = 0;
    static boolean g = false;
    static boolean h = true;
    public static boolean i = false;
    public static boolean j = false;
    public static int k = 20480;
    public static int l = 10800000;
    private static boolean m = false;
    private static boolean n = true;
    private static boolean o = false;
    private static long p = 0;
    private static long q = 0;
    private static long r = 5000;
    private static boolean s = false;
    private static int t = 0;
    private static boolean u = false;
    private static int v = 0;
    private static boolean w = false;
    private static boolean x = true;
    private static int y = 1000;
    private static int z = 200;

    /* compiled from: AuthUtil */
    static class a {
        boolean a = false;
        String b = "0";
        boolean c = false;
        int d = 5;

        a() {
        }
    }

    public static int A() {
        return ab;
    }

    public static long B() {
        return ag;
    }

    private static a a(JSONObject jSONObject, String str) {
        Throwable th;
        a aVar;
        if (jSONObject != null) {
            try {
                JSONObject jSONObject2 = jSONObject.getJSONObject(str);
                if (jSONObject2 != null) {
                    aVar = new a();
                    try {
                        aVar.a = l.a(jSONObject2.optString("b"), false);
                        aVar.b = jSONObject2.optString("t");
                        aVar.c = l.a(jSONObject2.optString("st"), false);
                        aVar.d = jSONObject2.optInt(UploadQueueMgr.MSGTYPE_INTERVAL, 0);
                        return aVar;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
            } catch (Throwable th3) {
                Throwable th4 = th3;
                aVar = null;
                th = th4;
                en.a(th, "AuthUtil", "getLocateObj");
                return aVar;
            }
        }
        return null;
    }

    public static boolean a() {
        return s;
    }

    public static boolean a(long j2) {
        long b2 = et.b();
        return o && b2 - q <= p && b2 - j2 >= r;
    }

    public static boolean a(Context context) {
        boolean z2 = false;
        C = true;
        try {
            l.a a2 = l.a(context, en.c(), en.d());
            if (a2 != null) {
                n = a2.a();
                z2 = a(context, a2);
            }
        } catch (Throwable th) {
            en.a(th, "AuthUtil", "getConfig");
        }
        f = et.b();
        e = et.b();
        return z2;
    }

    public static boolean a(Context context, long j2) {
        if (!K) {
            return false;
        }
        long a2 = et.a();
        if (a2 - j2 < ((long) L)) {
            return false;
        }
        if (M == -1) {
            return true;
        }
        if (!et.b(a2, es.a(context, "pref", "ngpsTime", 0))) {
            try {
                SharedPreferences.Editor edit = context.getSharedPreferences("pref", 0).edit();
                edit.putLong("ngpsTime", a2);
                edit.putInt("ngpsCount", 0);
                es.a(edit);
            } catch (Throwable th) {
                en.a(th, "AuthUtil", "resetPrefsNGPS");
            }
            SharedPreferences.Editor a3 = es.a(context, "pref");
            es.a(a3, "ngpsCount", 1);
            es.a(a3);
            return true;
        }
        int a4 = es.a(context, "pref", "ngpsCount", 0);
        if (a4 >= M) {
            return false;
        }
        int i2 = a4 + 1;
        SharedPreferences.Editor a5 = es.a(context, "pref");
        es.a(a5, "ngpsCount", i2);
        es.a(a5);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:164:0x02fa, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:?, code lost:
        com.loc.en.a(r2, "AuthUtil", "loadConfigDataCallAMapSer");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:174:0x030f, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:177:?, code lost:
        com.loc.en.a(r2, "AuthUtil", "loadConfigDataUploadException");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:178:0x031b, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:181:?, code lost:
        com.loc.en.a(r2, "AuthUtil", "loadconfig part2");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:182:0x0327, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:185:?, code lost:
        com.loc.en.a(r2, "AuthUtil", "loadConfigDataLocate");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:186:0x0333, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:189:?, code lost:
        com.loc.en.a(r2, "AuthUtil", "loadconfig part1");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:190:0x033f, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:191:0x0340, code lost:
        com.loc.en.a(r2, "AuthUtil", "loadconfig part");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:195:0x0351, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:198:?, code lost:
        com.loc.en.a(r2, "AuthUtil", "loadConfigDataNgps");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:199:0x035d, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:200:0x035e, code lost:
        com.loc.en.a(r2, "AuthUtil", "loadConfigDataCacheAble");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:201:0x0369, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:202:0x036a, code lost:
        com.loc.en.a(r2, "AuthUtil", "loadConfigDataGpsGeoAble");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0115, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
        com.loc.es.a(r3);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0115 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:43:0x00a5] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0118 A[SYNTHETIC, Splitter:B:68:0x0118] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean a(android.content.Context r11, com.loc.l.a r12) {
        /*
            r9 = 500(0x1f4, float:7.0E-43)
            r10 = 20
            r0 = 1
            r8 = 30
            r1 = 0
            r2 = 0
            java.lang.String r3 = "pref"
            android.content.SharedPreferences$Editor r3 = com.loc.es.a(r11, r3)     // Catch:{ Throwable -> 0x04ed, all -> 0x04e9 }
            org.json.JSONObject r4 = r12.g     // Catch:{ Throwable -> 0x009b, all -> 0x0115 }
            if (r4 == 0) goto L_0x00a5
            java.lang.String r2 = "at"
            r5 = 123(0x7b, float:1.72E-43)
            int r2 = r4.optInt(r2, r5)     // Catch:{ Throwable -> 0x0090, all -> 0x0115 }
            int r2 = r2 * 60
            int r2 = r2 * 1000
            d = r2     // Catch:{ Throwable -> 0x0090, all -> 0x0115 }
        L_0x0023:
            java.lang.String r2 = "ila"
            java.lang.String r2 = r4.optString(r2)     // Catch:{ Throwable -> 0x0109, all -> 0x0115 }
            boolean r5 = V     // Catch:{ Throwable -> 0x0109, all -> 0x0115 }
            boolean r2 = com.loc.l.a((java.lang.String) r2, (boolean) r5)     // Catch:{ Throwable -> 0x0109, all -> 0x0115 }
            V = r2     // Catch:{ Throwable -> 0x0109, all -> 0x0115 }
        L_0x0032:
            java.lang.String r2 = "icbd"
            java.lang.String r2 = r4.optString(r2)     // Catch:{ Throwable -> 0x011c, all -> 0x0115 }
            boolean r5 = c     // Catch:{ Throwable -> 0x011c, all -> 0x0115 }
            boolean r2 = com.loc.l.a((java.lang.String) r2, (boolean) r5)     // Catch:{ Throwable -> 0x011c, all -> 0x0115 }
            c = r2     // Catch:{ Throwable -> 0x011c, all -> 0x0115 }
        L_0x0041:
            if (r11 == 0) goto L_0x0045
            if (r4 != 0) goto L_0x0128
        L_0x0045:
            java.lang.String r2 = "nla"
            java.lang.String r2 = r4.optString(r2)     // Catch:{ Throwable -> 0x04f7, all -> 0x0115 }
            boolean r5 = W     // Catch:{ Throwable -> 0x04f7, all -> 0x0115 }
            boolean r2 = com.loc.l.a((java.lang.String) r2, (boolean) r5)     // Catch:{ Throwable -> 0x04f7, all -> 0x0115 }
            W = r2     // Catch:{ Throwable -> 0x04f7, all -> 0x0115 }
        L_0x0054:
            java.lang.String r2 = "asw"
            java.lang.String r2 = r4.optString(r2)     // Catch:{ Throwable -> 0x04f4, all -> 0x0115 }
            boolean r5 = Y     // Catch:{ Throwable -> 0x04f4, all -> 0x0115 }
            boolean r2 = com.loc.l.a((java.lang.String) r2, (boolean) r5)     // Catch:{ Throwable -> 0x04f4, all -> 0x0115 }
            Y = r2     // Catch:{ Throwable -> 0x04f4, all -> 0x0115 }
            java.lang.String r2 = "asw"
            boolean r5 = Y     // Catch:{ Throwable -> 0x04f4, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r2, (boolean) r5)     // Catch:{ Throwable -> 0x04f4, all -> 0x0115 }
        L_0x006b:
            java.lang.String r2 = "mlpl"
            org.json.JSONArray r4 = r4.optJSONArray(r2)     // Catch:{ Throwable -> 0x04f1, all -> 0x0115 }
            if (r4 == 0) goto L_0x00a5
            int r2 = r4.length()     // Catch:{ Throwable -> 0x04f1, all -> 0x0115 }
            if (r2 <= 0) goto L_0x00a5
            r2 = r1
        L_0x007b:
            int r5 = r4.length()     // Catch:{ Throwable -> 0x04f1, all -> 0x0115 }
            if (r2 >= r5) goto L_0x00a5
            java.lang.String r5 = r4.getString(r2)     // Catch:{ Throwable -> 0x04f1, all -> 0x0115 }
            boolean r5 = com.loc.et.b((android.content.Context) r11, (java.lang.String) r5)     // Catch:{ Throwable -> 0x04f1, all -> 0x0115 }
            X = r5     // Catch:{ Throwable -> 0x04f1, all -> 0x0115 }
            if (r5 != 0) goto L_0x00a5
            int r2 = r2 + 1
            goto L_0x007b
        L_0x0090:
            r2 = move-exception
            java.lang.String r5 = "AuthUtil"
            java.lang.String r6 = "requestSdkAuthInterval"
            com.loc.en.a(r2, r5, r6)     // Catch:{ Throwable -> 0x009b, all -> 0x0115 }
            goto L_0x0023
        L_0x009b:
            r2 = move-exception
            java.lang.String r4 = "AuthUtil"
            java.lang.String r5 = "loadConfigAbleStatus"
            com.loc.en.a(r2, r4, r5)     // Catch:{ Throwable -> 0x0306, all -> 0x0115 }
        L_0x00a5:
            org.json.JSONObject r4 = r12.h     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            if (r4 == 0) goto L_0x0174
            java.lang.String r2 = "callamapflag"
            java.lang.String r2 = r4.optString(r2)     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            boolean r5 = D     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            boolean r2 = com.loc.l.a((java.lang.String) r2, (boolean) r5)     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            D = r2     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            java.lang.String r2 = "co"
            java.lang.String r2 = r4.optString(r2)     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            r5 = 0
            boolean r2 = com.loc.l.a((java.lang.String) r2, (boolean) r5)     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            if (r2 == 0) goto L_0x014d
            boolean r2 = D     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            if (r2 == 0) goto L_0x014d
            r2 = r0
        L_0x00cb:
            b = r2     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            boolean r2 = D     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            if (r2 == 0) goto L_0x0174
            java.lang.String r2 = "count"
            int r5 = E     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            int r2 = r4.optInt(r2, r5)     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            E = r2     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            java.lang.String r2 = "sysTime"
            long r6 = F     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            long r6 = r4.optLong(r2, r6)     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            F = r6     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            java.lang.String r2 = "sn"
            org.json.JSONArray r4 = r4.optJSONArray(r2)     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            if (r4 == 0) goto L_0x0150
            int r2 = r4.length()     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            if (r2 <= 0) goto L_0x0150
            r2 = r1
        L_0x00f7:
            int r5 = r4.length()     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            if (r2 >= r5) goto L_0x0150
            java.util.ArrayList<java.lang.String> r5 = G     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            java.lang.String r6 = r4.getString(r2)     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            r5.add(r6)     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            int r2 = r2 + 1
            goto L_0x00f7
        L_0x0109:
            r2 = move-exception
            java.lang.String r5 = "AuthUtil"
            java.lang.String r6 = "loadConfigData_indoor"
            com.loc.en.a(r2, r5, r6)     // Catch:{ Throwable -> 0x009b, all -> 0x0115 }
            goto L_0x0032
        L_0x0115:
            r0 = move-exception
        L_0x0116:
            if (r3 == 0) goto L_0x011b
            com.loc.es.a(r3)     // Catch:{ Throwable -> 0x04e6 }
        L_0x011b:
            throw r0
        L_0x011c:
            r2 = move-exception
            java.lang.String r5 = "AuthUtil"
            java.lang.String r6 = "loadConfigData_CallBackDex"
            com.loc.en.a(r2, r5, r6)     // Catch:{ Throwable -> 0x009b, all -> 0x0115 }
            goto L_0x0041
        L_0x0128:
            java.lang.String r2 = "re"
            java.lang.String r2 = r4.optString(r2)     // Catch:{ Throwable -> 0x0141, all -> 0x0115 }
            boolean r5 = h     // Catch:{ Throwable -> 0x0141, all -> 0x0115 }
            boolean r2 = com.loc.l.a((java.lang.String) r2, (boolean) r5)     // Catch:{ Throwable -> 0x0141, all -> 0x0115 }
            h = r2     // Catch:{ Throwable -> 0x0141, all -> 0x0115 }
            java.lang.String r2 = "fr"
            boolean r5 = h     // Catch:{ Throwable -> 0x0141, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r2, (boolean) r5)     // Catch:{ Throwable -> 0x0141, all -> 0x0115 }
            goto L_0x0045
        L_0x0141:
            r2 = move-exception
            java.lang.String r5 = "AuthUtil"
            java.lang.String r6 = "checkReLocationAble"
            com.loc.en.a(r2, r5, r6)     // Catch:{ Throwable -> 0x009b, all -> 0x0115 }
            goto L_0x0045
        L_0x014d:
            r2 = r1
            goto L_0x00cb
        L_0x0150:
            int r2 = E     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            r4 = -1
            if (r2 == r4) goto L_0x0174
            long r4 = F     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            r6 = 0
            int r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r2 == 0) goto L_0x0174
            java.lang.String r2 = "pref"
            java.lang.String r4 = "nowtime"
            r6 = 0
            long r4 = com.loc.es.a((android.content.Context) r11, (java.lang.String) r2, (java.lang.String) r4, (long) r6)     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            long r6 = F     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            boolean r2 = com.loc.et.a((long) r6, (long) r4)     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
            if (r2 != 0) goto L_0x0174
            f(r11)     // Catch:{ Throwable -> 0x02fa, all -> 0x0115 }
        L_0x0174:
            com.loc.l$a$a r2 = r12.x     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            if (r2 == 0) goto L_0x01f2
            boolean r4 = r2.a     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            x = r4     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            java.lang.String r4 = "exception"
            boolean r5 = x     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r4, (boolean) r5)     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            org.json.JSONObject r2 = r2.c     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            java.lang.String r4 = "fn"
            int r5 = y     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            int r4 = r2.optInt(r4, r5)     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            y = r4     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            java.lang.String r4 = "mpn"
            int r5 = z     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            int r4 = r2.optInt(r4, r5)     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            z = r4     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            if (r4 <= r9) goto L_0x01a2
            r4 = 500(0x1f4, float:7.0E-43)
            z = r4     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
        L_0x01a2:
            int r4 = z     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            if (r4 >= r8) goto L_0x01aa
            r4 = 30
            z = r4     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
        L_0x01aa:
            java.lang.String r4 = "igu"
            java.lang.String r4 = r2.optString(r4)     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            boolean r5 = A     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            boolean r4 = com.loc.l.a((java.lang.String) r4, (boolean) r5)     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            A = r4     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            java.lang.String r4 = "ms"
            int r5 = B     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            int r2 = r2.optInt(r4, r5)     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            B = r2     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            int r2 = y     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            boolean r4 = A     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            int r5 = B     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            com.loc.be.a(r2, r4, r5)     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            boolean r2 = A     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            com.loc.bg.a((boolean) r2)     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            java.lang.String r2 = "fn"
            int r4 = y     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r2, (int) r4)     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            java.lang.String r2 = "mpn"
            int r4 = z     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r2, (int) r4)     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            java.lang.String r2 = "igu"
            boolean r4 = A     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r2, (boolean) r4)     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            java.lang.String r2 = "ms"
            int r4 = B     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r2, (int) r4)     // Catch:{ Throwable -> 0x030f, all -> 0x0115 }
        L_0x01f2:
            org.json.JSONObject r4 = r12.m     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            if (r4 == 0) goto L_0x0261
            java.lang.String r2 = "able"
            java.lang.String r2 = r4.optString(r2)     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            r5 = 0
            boolean r2 = com.loc.l.a((java.lang.String) r2, (boolean) r5)     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            if (r2 == 0) goto L_0x0261
            java.lang.String r2 = "fs"
            com.loc.em$a r2 = a((org.json.JSONObject) r4, (java.lang.String) r2)     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            if (r2 == 0) goto L_0x0219
            boolean r5 = r2.c     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            s = r5     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            java.lang.String r2 = r2.b     // Catch:{ Throwable -> 0x031b, all -> 0x0115 }
            int r2 = java.lang.Integer.parseInt(r2)     // Catch:{ Throwable -> 0x031b, all -> 0x0115 }
            t = r2     // Catch:{ Throwable -> 0x031b, all -> 0x0115 }
        L_0x0219:
            java.lang.String r2 = "us"
            com.loc.em$a r2 = a((org.json.JSONObject) r4, (java.lang.String) r2)     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            if (r2 == 0) goto L_0x023a
            boolean r5 = r2.c     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            u = r5     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            boolean r5 = r2.a     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            w = r5     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            java.lang.String r2 = r2.b     // Catch:{ Throwable -> 0x0333, all -> 0x0115 }
            int r2 = java.lang.Integer.parseInt(r2)     // Catch:{ Throwable -> 0x0333, all -> 0x0115 }
            v = r2     // Catch:{ Throwable -> 0x0333, all -> 0x0115 }
        L_0x0232:
            int r2 = v     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            r5 = 2
            if (r2 >= r5) goto L_0x023a
            r2 = 0
            u = r2     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
        L_0x023a:
            java.lang.String r2 = "rs"
            com.loc.em$a r2 = a((org.json.JSONObject) r4, (java.lang.String) r2)     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            if (r2 == 0) goto L_0x0261
            boolean r4 = r2.c     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            o = r4     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            if (r4 == 0) goto L_0x0256
            long r4 = com.loc.et.b()     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            q = r4     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            int r4 = r2.d     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            int r4 = r4 * 1000
            long r4 = (long) r4     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            r = r4     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
        L_0x0256:
            java.lang.String r2 = r2.b     // Catch:{ Throwable -> 0x033f, all -> 0x0115 }
            int r2 = java.lang.Integer.parseInt(r2)     // Catch:{ Throwable -> 0x033f, all -> 0x0115 }
            int r2 = r2 * 1000
            long r4 = (long) r2     // Catch:{ Throwable -> 0x033f, all -> 0x0115 }
            p = r4     // Catch:{ Throwable -> 0x033f, all -> 0x0115 }
        L_0x0261:
            org.json.JSONObject r2 = r12.o     // Catch:{ Throwable -> 0x0351, all -> 0x0115 }
            if (r2 == 0) goto L_0x028f
            java.lang.String r4 = "able"
            java.lang.String r4 = r2.optString(r4)     // Catch:{ Throwable -> 0x0351, all -> 0x0115 }
            boolean r5 = K     // Catch:{ Throwable -> 0x0351, all -> 0x0115 }
            boolean r4 = com.loc.l.a((java.lang.String) r4, (boolean) r5)     // Catch:{ Throwable -> 0x0351, all -> 0x0115 }
            K = r4     // Catch:{ Throwable -> 0x0351, all -> 0x0115 }
            if (r4 == 0) goto L_0x028f
            java.lang.String r4 = "c"
            r5 = 0
            int r4 = r2.optInt(r4, r5)     // Catch:{ Throwable -> 0x0351, all -> 0x0115 }
            if (r4 != 0) goto L_0x034b
            r4 = 3000(0xbb8, float:4.204E-42)
            L = r4     // Catch:{ Throwable -> 0x0351, all -> 0x0115 }
        L_0x0284:
            java.lang.String r4 = "t"
            int r2 = r2.getInt(r4)     // Catch:{ Throwable -> 0x0351, all -> 0x0115 }
            int r2 = r2 / 2
            M = r2     // Catch:{ Throwable -> 0x0351, all -> 0x0115 }
        L_0x028f:
            org.json.JSONObject r2 = r12.p     // Catch:{ Throwable -> 0x035d, all -> 0x0115 }
            if (r2 == 0) goto L_0x02c2
            java.lang.String r4 = "able"
            java.lang.String r4 = r2.optString(r4)     // Catch:{ Throwable -> 0x035d, all -> 0x0115 }
            boolean r5 = N     // Catch:{ Throwable -> 0x035d, all -> 0x0115 }
            boolean r4 = com.loc.l.a((java.lang.String) r4, (boolean) r5)     // Catch:{ Throwable -> 0x035d, all -> 0x0115 }
            N = r4     // Catch:{ Throwable -> 0x035d, all -> 0x0115 }
            if (r4 == 0) goto L_0x02b2
            java.lang.String r4 = "c"
            r5 = 300(0x12c, float:4.2E-43)
            int r2 = r2.optInt(r4, r5)     // Catch:{ Throwable -> 0x035d, all -> 0x0115 }
            int r2 = r2 * 1000
            long r4 = (long) r2     // Catch:{ Throwable -> 0x035d, all -> 0x0115 }
            O = r4     // Catch:{ Throwable -> 0x035d, all -> 0x0115 }
        L_0x02b2:
            java.lang.String r2 = "ca"
            boolean r4 = N     // Catch:{ Throwable -> 0x035d, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r2, (boolean) r4)     // Catch:{ Throwable -> 0x035d, all -> 0x0115 }
            java.lang.String r2 = "ct"
            long r4 = O     // Catch:{ Throwable -> 0x035d, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r2, (long) r4)     // Catch:{ Throwable -> 0x035d, all -> 0x0115 }
        L_0x02c2:
            org.json.JSONObject r2 = r12.i     // Catch:{ Throwable -> 0x0369, all -> 0x0115 }
            if (r2 == 0) goto L_0x02e2
            java.lang.String r4 = "able"
            java.lang.String r4 = r2.optString(r4)     // Catch:{ Throwable -> 0x0369, all -> 0x0115 }
            boolean r5 = T     // Catch:{ Throwable -> 0x0369, all -> 0x0115 }
            boolean r4 = com.loc.l.a((java.lang.String) r4, (boolean) r5)     // Catch:{ Throwable -> 0x0369, all -> 0x0115 }
            T = r4     // Catch:{ Throwable -> 0x0369, all -> 0x0115 }
            if (r4 == 0) goto L_0x02e2
            java.lang.String r4 = "c"
            int r5 = U     // Catch:{ Throwable -> 0x0369, all -> 0x0115 }
            int r2 = r2.optInt(r4, r5)     // Catch:{ Throwable -> 0x0369, all -> 0x0115 }
            U = r2     // Catch:{ Throwable -> 0x0369, all -> 0x0115 }
        L_0x02e2:
            org.json.JSONObject r1 = r12.w     // Catch:{ Throwable -> 0x0306, all -> 0x0115 }
            if (r11 == 0) goto L_0x02e8
            if (r1 != 0) goto L_0x0375
        L_0x02e8:
            if (r11 == 0) goto L_0x02ec
            if (r1 != 0) goto L_0x03c7
        L_0x02ec:
            if (r11 == 0) goto L_0x02f0
            if (r1 != 0) goto L_0x040a
        L_0x02f0:
            if (r11 == 0) goto L_0x02f4
            if (r1 != 0) goto L_0x0473
        L_0x02f4:
            if (r3 == 0) goto L_0x02f9
            com.loc.es.a(r3)     // Catch:{ Throwable -> 0x04e0 }
        L_0x02f9:
            return r0
        L_0x02fa:
            r2 = move-exception
            java.lang.String r4 = "AuthUtil"
            java.lang.String r5 = "loadConfigDataCallAMapSer"
            com.loc.en.a(r2, r4, r5)     // Catch:{ Throwable -> 0x0306, all -> 0x0115 }
            goto L_0x0174
        L_0x0306:
            r0 = move-exception
            r0 = r3
        L_0x0308:
            if (r0 == 0) goto L_0x030d
            com.loc.es.a(r0)     // Catch:{ Throwable -> 0x04e3 }
        L_0x030d:
            r0 = r1
            goto L_0x02f9
        L_0x030f:
            r2 = move-exception
            java.lang.String r4 = "AuthUtil"
            java.lang.String r5 = "loadConfigDataUploadException"
            com.loc.en.a(r2, r4, r5)     // Catch:{ Throwable -> 0x0306, all -> 0x0115 }
            goto L_0x01f2
        L_0x031b:
            r2 = move-exception
            java.lang.String r5 = "AuthUtil"
            java.lang.String r6 = "loadconfig part2"
            com.loc.en.a(r2, r5, r6)     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            goto L_0x0219
        L_0x0327:
            r2 = move-exception
            java.lang.String r4 = "AuthUtil"
            java.lang.String r5 = "loadConfigDataLocate"
            com.loc.en.a(r2, r4, r5)     // Catch:{ Throwable -> 0x0306, all -> 0x0115 }
            goto L_0x0261
        L_0x0333:
            r2 = move-exception
            java.lang.String r5 = "AuthUtil"
            java.lang.String r6 = "loadconfig part1"
            com.loc.en.a(r2, r5, r6)     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            goto L_0x0232
        L_0x033f:
            r2 = move-exception
            java.lang.String r4 = "AuthUtil"
            java.lang.String r5 = "loadconfig part"
            com.loc.en.a(r2, r4, r5)     // Catch:{ Throwable -> 0x0327, all -> 0x0115 }
            goto L_0x0261
        L_0x034b:
            int r4 = r4 * 1000
            L = r4     // Catch:{ Throwable -> 0x0351, all -> 0x0115 }
            goto L_0x0284
        L_0x0351:
            r2 = move-exception
            java.lang.String r4 = "AuthUtil"
            java.lang.String r5 = "loadConfigDataNgps"
            com.loc.en.a(r2, r4, r5)     // Catch:{ Throwable -> 0x0306, all -> 0x0115 }
            goto L_0x028f
        L_0x035d:
            r2 = move-exception
            java.lang.String r4 = "AuthUtil"
            java.lang.String r5 = "loadConfigDataCacheAble"
            com.loc.en.a(r2, r4, r5)     // Catch:{ Throwable -> 0x0306, all -> 0x0115 }
            goto L_0x02c2
        L_0x0369:
            r2 = move-exception
            java.lang.String r4 = "AuthUtil"
            java.lang.String r5 = "loadConfigDataGpsGeoAble"
            com.loc.en.a(r2, r4, r5)     // Catch:{ Throwable -> 0x0306, all -> 0x0115 }
            goto L_0x02e2
        L_0x0375:
            java.lang.String r2 = "15O"
            org.json.JSONObject r2 = r1.optJSONObject(r2)     // Catch:{ Throwable -> 0x03bf, all -> 0x0115 }
            if (r2 == 0) goto L_0x02e8
            java.lang.String r4 = "able"
            java.lang.String r4 = r2.optString(r4)     // Catch:{ Throwable -> 0x03bf, all -> 0x0115 }
            r5 = 0
            boolean r4 = com.loc.l.a((java.lang.String) r4, (boolean) r5)     // Catch:{ Throwable -> 0x03bf, all -> 0x0115 }
            if (r4 == 0) goto L_0x03c2
            java.lang.String r4 = "fl"
            org.json.JSONArray r4 = r2.optJSONArray(r4)     // Catch:{ Throwable -> 0x03bf, all -> 0x0115 }
            if (r4 == 0) goto L_0x03a7
            int r5 = r4.length()     // Catch:{ Throwable -> 0x03bf, all -> 0x0115 }
            if (r5 <= 0) goto L_0x03a7
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x03bf, all -> 0x0115 }
            java.lang.String r5 = android.os.Build.MANUFACTURER     // Catch:{ Throwable -> 0x03bf, all -> 0x0115 }
            boolean r4 = r4.contains(r5)     // Catch:{ Throwable -> 0x03bf, all -> 0x0115 }
            if (r4 == 0) goto L_0x03c2
        L_0x03a7:
            java.lang.String r4 = "iv"
            r5 = 30
            int r2 = r2.optInt(r4, r5)     // Catch:{ Throwable -> 0x03bf, all -> 0x0115 }
            int r2 = r2 * 1000
            long r4 = (long) r2     // Catch:{ Throwable -> 0x03bf, all -> 0x0115 }
            Z = r4     // Catch:{ Throwable -> 0x03bf, all -> 0x0115 }
        L_0x03b5:
            java.lang.String r2 = "awsi"
            long r4 = Z     // Catch:{ Throwable -> 0x03bf, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r2, (long) r4)     // Catch:{ Throwable -> 0x03bf, all -> 0x0115 }
            goto L_0x02e8
        L_0x03bf:
            r2 = move-exception
            goto L_0x02e8
        L_0x03c2:
            r4 = -1
            Z = r4     // Catch:{ Throwable -> 0x03bf, all -> 0x0115 }
            goto L_0x03b5
        L_0x03c7:
            java.lang.String r2 = "15U"
            org.json.JSONObject r2 = r1.optJSONObject(r2)     // Catch:{ Throwable -> 0x0407, all -> 0x0115 }
            if (r2 == 0) goto L_0x02ec
            java.lang.String r4 = "able"
            java.lang.String r4 = r2.optString(r4)     // Catch:{ Throwable -> 0x0407, all -> 0x0115 }
            boolean r5 = aa     // Catch:{ Throwable -> 0x0407, all -> 0x0115 }
            boolean r4 = com.loc.l.a((java.lang.String) r4, (boolean) r5)     // Catch:{ Throwable -> 0x0407, all -> 0x0115 }
            java.lang.String r5 = "yn"
            int r6 = ab     // Catch:{ Throwable -> 0x0407, all -> 0x0115 }
            int r5 = r2.optInt(r5, r6)     // Catch:{ Throwable -> 0x0407, all -> 0x0115 }
            java.lang.String r6 = "sysTime"
            long r8 = ag     // Catch:{ Throwable -> 0x0407, all -> 0x0115 }
            long r6 = r2.optLong(r6, r8)     // Catch:{ Throwable -> 0x0407, all -> 0x0115 }
            ag = r6     // Catch:{ Throwable -> 0x0407, all -> 0x0115 }
            java.lang.String r2 = "15ua"
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r2, (boolean) r4)     // Catch:{ Throwable -> 0x0407, all -> 0x0115 }
            java.lang.String r2 = "15un"
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r2, (int) r5)     // Catch:{ Throwable -> 0x0407, all -> 0x0115 }
            java.lang.String r2 = "15ust"
            long r4 = ag     // Catch:{ Throwable -> 0x0407, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r2, (long) r4)     // Catch:{ Throwable -> 0x0407, all -> 0x0115 }
            goto L_0x02ec
        L_0x0407:
            r2 = move-exception
            goto L_0x02ec
        L_0x040a:
            java.lang.String r2 = "17Y"
            org.json.JSONObject r2 = r1.getJSONObject(r2)     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            if (r2 == 0) goto L_0x02f0
            java.lang.String r4 = "able"
            java.lang.String r4 = r2.optString(r4)     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            boolean r5 = i     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            boolean r4 = com.loc.l.a((java.lang.String) r4, (boolean) r5)     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            i = r4     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            java.lang.String r4 = "17ya"
            boolean r5 = i     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r4, (boolean) r5)     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            java.lang.String r4 = "mup"
            java.lang.String r4 = r2.optString(r4)     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            boolean r5 = j     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            boolean r4 = com.loc.l.a((java.lang.String) r4, (boolean) r5)     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            j = r4     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            java.lang.String r4 = "17ym"
            boolean r5 = j     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r4, (boolean) r5)     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            java.lang.String r4 = "max"
            r5 = 20
            int r4 = r2.optInt(r4, r5)     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            if (r4 <= 0) goto L_0x0456
            java.lang.String r5 = "17yx"
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r5, (int) r4)     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            int r4 = r4 * 1024
            k = r4     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
        L_0x0456:
            java.lang.String r4 = "inv"
            r5 = 3
            int r2 = r2.optInt(r4, r5)     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            if (r2 <= 0) goto L_0x02f0
            java.lang.String r4 = "17yi"
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r4, (int) r2)     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            int r2 = r2 * 60
            int r2 = r2 * 60
            int r2 = r2 * 1000
            l = r2     // Catch:{ Throwable -> 0x0470, all -> 0x0115 }
            goto L_0x02f0
        L_0x0470:
            r2 = move-exception
            goto L_0x02f0
        L_0x0473:
            java.lang.String r2 = "17J"
            org.json.JSONObject r1 = r1.optJSONObject(r2)     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            if (r1 == 0) goto L_0x02f4
            java.lang.String r2 = "able"
            java.lang.String r2 = r1.optString(r2)     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            r4 = 0
            boolean r2 = com.loc.l.a((java.lang.String) r2, (boolean) r4)     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            ac = r2     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            java.lang.String r4 = "ok9"
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r4, (boolean) r2)     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            if (r2 == 0) goto L_0x02f4
            java.lang.String r2 = "auth"
            java.lang.String r2 = r1.optString(r2)     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            java.lang.String r4 = "ht"
            java.lang.String r4 = r1.optString(r4)     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            af = r4     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            java.lang.String r4 = "ok11"
            java.lang.String r5 = af     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r4, (java.lang.String) r5)     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            r4 = 0
            com.loc.l.a((java.lang.String) r2, (boolean) r4)     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            java.lang.String r2 = "nr"
            java.lang.String r2 = r1.optString(r2)     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            r4 = 0
            boolean r2 = com.loc.l.a((java.lang.String) r2, (boolean) r4)     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            ae = r2     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            java.lang.String r2 = "tm"
            java.lang.String r1 = r1.optString(r2)     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            if (r2 != 0) goto L_0x02f4
            int r1 = java.lang.Integer.parseInt(r1)     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            if (r1 <= 0) goto L_0x02f4
            if (r1 >= r10) goto L_0x02f4
            ad = r1     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            java.lang.String r1 = "ok10"
            int r2 = ad     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            com.loc.es.a((android.content.SharedPreferences.Editor) r3, (java.lang.String) r1, (int) r2)     // Catch:{ Throwable -> 0x04dd, all -> 0x0115 }
            goto L_0x02f4
        L_0x04dd:
            r1 = move-exception
            goto L_0x02f4
        L_0x04e0:
            r1 = move-exception
            goto L_0x02f9
        L_0x04e3:
            r0 = move-exception
            goto L_0x030d
        L_0x04e6:
            r1 = move-exception
            goto L_0x011b
        L_0x04e9:
            r0 = move-exception
            r3 = r2
            goto L_0x0116
        L_0x04ed:
            r0 = move-exception
            r0 = r2
            goto L_0x0308
        L_0x04f1:
            r2 = move-exception
            goto L_0x00a5
        L_0x04f4:
            r2 = move-exception
            goto L_0x006b
        L_0x04f7:
            r2 = move-exception
            goto L_0x0054
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.em.a(android.content.Context, com.loc.l$a):boolean");
    }

    public static int b() {
        return t;
    }

    public static boolean b(long j2) {
        if (!N) {
            return false;
        }
        return O < 0 || et.a() - j2 < O;
    }

    public static boolean b(Context context) {
        if (!D) {
            return false;
        }
        if (E == -1 || F == 0) {
            return true;
        }
        if (!et.a(F, es.a(context, "pref", "nowtime", 0))) {
            f(context);
            SharedPreferences.Editor a2 = es.a(context, "pref");
            es.a(a2, "count", 1);
            es.a(a2);
            return true;
        }
        int a3 = es.a(context, "pref", "count", 0);
        if (a3 >= E) {
            return false;
        }
        int i2 = a3 + 1;
        SharedPreferences.Editor a4 = es.a(context, "pref");
        es.a(a4, "count", i2);
        es.a(a4);
        return true;
    }

    public static void c(Context context) {
        try {
            x = es.a(context, "pref", "exception", x);
            d(context);
        } catch (Throwable th) {
            en.a(th, "AuthUtil", "loadLastAbleState p1");
        }
        try {
            y = es.a(context, "pref", "fn", y);
            z = es.a(context, "pref", "mpn", z);
            A = es.a(context, "pref", "igu", A);
            B = es.a(context, "pref", "ms", B);
            be.a(y, A, B);
            bg.a(A);
        } catch (Throwable th2) {
        }
        try {
            N = es.a(context, "pref", "ca", N);
            O = es.a(context, "pref", "ct", O);
        } catch (Throwable th3) {
        }
        try {
            h = es.a(context, "pref", "fr", h);
        } catch (Throwable th4) {
        }
        try {
            Y = es.a(context, "pref", "asw", Y);
        } catch (Throwable th5) {
        }
        try {
            Z = es.a(context, "pref", "awsi", Z);
        } catch (Throwable th6) {
        }
        try {
            aa = es.a(context, "pref", "15ua", aa);
            ab = es.a(context, "pref", "15un", ab);
            ag = es.a(context, "pref", "15ust", ag);
        } catch (Throwable th7) {
        }
        try {
            ac = es.a(context, "pref", "ok9", ac);
            ad = es.a(context, "pref", "ok10", ad);
            af = es.a(context, "pref", "ok11", af);
        } catch (Throwable th8) {
        }
        try {
            i = es.a(context, "pref", "17ya", false);
            j = es.a(context, "pref", "17ym", false);
            l = es.a(context, "pref", "17yi", 2) * 60 * 60 * 1000;
            k = es.a(context, "pref", "17yx", 100) * 1024;
        } catch (Throwable th9) {
        }
    }

    public static boolean c() {
        return u;
    }

    public static int d() {
        return v;
    }

    public static void d(Context context) {
        try {
            t c2 = en.c();
            c2.a(x);
            ab.a(context, c2);
        } catch (Throwable th) {
        }
    }

    public static boolean e() {
        return w;
    }

    public static boolean e(Context context) {
        if (context == null) {
            return false;
        }
        try {
            if (et.b() - f < ((long) d)) {
                return false;
            }
            g = true;
            return true;
        } catch (Throwable th) {
            en.a(th, "Aps", "isConfigNeedUpdate");
            return false;
        }
    }

    public static ArrayList<String> f() {
        return G;
    }

    private static void f(Context context) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences("pref", 0).edit();
            edit.putLong("nowtime", F);
            edit.putInt("count", 0);
            es.a(edit);
        } catch (Throwable th) {
            en.a(th, "AuthUtil", "resetPrefsBind");
        }
    }

    public static boolean g() {
        return x;
    }

    public static int h() {
        return z;
    }

    public static boolean i() {
        return C;
    }

    public static void j() {
        C = false;
    }

    public static boolean k() {
        return K;
    }

    public static long l() {
        return O;
    }

    public static boolean m() {
        return N;
    }

    public static boolean n() {
        return T;
    }

    public static int o() {
        return U;
    }

    public static boolean p() {
        return W;
    }

    public static boolean q() {
        return X;
    }

    public static boolean r() {
        if (!g) {
            return g;
        }
        g = false;
        return true;
    }

    public static boolean s() {
        return h;
    }

    public static boolean t() {
        return Y;
    }

    public static long u() {
        return Z;
    }

    public static boolean v() {
        return ae;
    }

    public static boolean w() {
        return ac;
    }

    public static int x() {
        return ad;
    }

    public static String y() {
        return u.c(af);
    }

    public static boolean z() {
        return aa && ab > 0;
    }
}
