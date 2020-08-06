package com.uc.webview.export.internal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.WebView;
import com.uc.webview.export.extension.InitCallback;
import com.uc.webview.export.extension.NotAvailableUCListener;
import com.uc.webview.export.internal.a.p;
import com.uc.webview.export.internal.c.a.c;
import com.uc.webview.export.internal.interfaces.CommonDef;
import com.uc.webview.export.internal.interfaces.IGlobalSettings;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.interfaces.UCMobileWebKit;
import com.uc.webview.export.internal.setup.UCMPackageInfo;
import com.uc.webview.export.internal.setup.UCSetupException;
import com.uc.webview.export.utility.SetupTask;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/* compiled from: ProGuard */
public final class d {
    /* access modifiers changed from: private */
    public static int A = 0;
    private static IGlobalSettings B = null;
    private static a C = new a((byte) 0);
    private static int D = -1;
    private static long E = 4000;
    private static final Object F = new Object();
    private static boolean G = false;
    public static NotAvailableUCListener a = null;
    public static ValueCallback<Pair<String, HashMap<String, String>>> b;
    public static ClassLoader c = d.class.getClassLoader();
    public static UCMobileWebKit d = null;
    public static Context e = null;
    public static boolean f = false;
    public static String g;
    public static int h = -1;
    public static int i = 1;
    public static boolean j = false;
    public static boolean k = false;
    public static int l = 0;
    public static InitCallback m;
    public static boolean n = false;
    public static boolean o = false;
    public static boolean p = false;
    public static boolean q = true;
    public static String r = null;
    public static String s = null;
    public static ValueCallback<String> t = null;
    public static long u = 0;
    public static Map<String, Integer> v = new HashMap();
    public static Map<String, String> w = null;
    public static String x = null;
    public static SetupTask y;
    static boolean z = false;

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v0, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v1, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v25, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v4, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v5, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v7, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v93, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v8, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v9, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v10, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v14, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v49, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v15, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v50, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v55, resolved type: java.io.FileWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v141, resolved type: java.io.FileReader} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object a(int r13, java.lang.Object... r14) {
        /*
            r7 = 10029(0x272d, float:1.4054E-41)
            r8 = 0
            r12 = 2
            r2 = 1
            r6 = 0
            switch(r13) {
                case 10001: goto L_0x000a;
                case 10002: goto L_0x0018;
                case 10003: goto L_0x0029;
                case 10004: goto L_0x0042;
                case 10005: goto L_0x00a7;
                case 10006: goto L_0x00ba;
                case 10007: goto L_0x00e3;
                case 10008: goto L_0x00fb;
                case 10009: goto L_0x0103;
                case 10010: goto L_0x0107;
                case 10011: goto L_0x0113;
                case 10012: goto L_0x0167;
                case 10013: goto L_0x01d4;
                case 10014: goto L_0x01fc;
                case 10015: goto L_0x022b;
                case 10016: goto L_0x0241;
                case 10017: goto L_0x025d;
                case 10018: goto L_0x0279;
                case 10019: goto L_0x0295;
                case 10020: goto L_0x02b1;
                case 10021: goto L_0x02c8;
                case 10022: goto L_0x02dd;
                case 10023: goto L_0x02fd;
                case 10024: goto L_0x036a;
                case 10025: goto L_0x038b;
                case 10026: goto L_0x03a4;
                case 10027: goto L_0x03b6;
                case 10028: goto L_0x03bf;
                case 10029: goto L_0x03d8;
                case 10030: goto L_0x041f;
                case 10031: goto L_0x04a0;
                case 10032: goto L_0x04c4;
                case 10033: goto L_0x04e8;
                case 10034: goto L_0x05ae;
                case 10035: goto L_0x0679;
                case 10036: goto L_0x072a;
                case 10037: goto L_0x0767;
                case 10038: goto L_0x07ce;
                case 10039: goto L_0x0865;
                case 10040: goto L_0x0936;
                case 10041: goto L_0x0438;
                case 10042: goto L_0x048c;
                case 10043: goto L_0x0494;
                case 10044: goto L_0x0561;
                case 10045: goto L_0x0956;
                case 10046: goto L_0x0135;
                default: goto L_0x0009;
            }
        L_0x0009:
            return r8
        L_0x000a:
            r0 = r14[r6]
            java.lang.Long r0 = (java.lang.Long) r0
            long r0 = r0.longValue()
            long r2 = u
            long r0 = r0 | r2
            u = r0
            goto L_0x0009
        L_0x0018:
            r0 = r14[r6]
            java.lang.Long r0 = (java.lang.Long) r0
            long r0 = r0.longValue()
            long r2 = u
            r4 = -1
            long r0 = r0 ^ r4
            long r0 = r0 & r2
            u = r0
            goto L_0x0009
        L_0x0029:
            r0 = r14[r6]
            java.lang.Long r0 = (java.lang.Long) r0
            long r0 = r0.longValue()
            long r4 = u
            long r0 = r0 & r4
            r4 = 0
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 == 0) goto L_0x0040
            r0 = r2
        L_0x003b:
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r0)
            goto L_0x0009
        L_0x0040:
            r0 = r6
            goto L_0x003b
        L_0x0042:
            r0 = r14[r6]
            java.lang.String r0 = (java.lang.String) r0
            if (r0 == 0) goto L_0x0052
            java.lang.String r1 = r0.trim()
            int r1 = r1.length()
            if (r1 != 0) goto L_0x0055
        L_0x0052:
            w = r8
            goto L_0x0009
        L_0x0055:
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            java.lang.String r3 = ";"
            java.lang.String[] r3 = r0.split(r3)
            int r4 = r3.length
            r0 = r6
        L_0x0063:
            if (r0 >= r4) goto L_0x007f
            r5 = r3[r0]
            java.lang.String r7 = "="
            java.lang.String[] r5 = r5.split(r7)
            int r7 = r5.length
            if (r7 != r12) goto L_0x007c
            r7 = r5[r6]
            java.lang.String r7 = r7.trim()
            r5 = r5[r2]
            r1.put(r7, r5)
        L_0x007c:
            int r0 = r0 + 1
            goto L_0x0063
        L_0x007f:
            java.lang.String r0 = "tag_test_log"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "CDParam:"
            r2.<init>(r3)
            java.lang.StringBuilder r2 = r2.append(r1)
            java.lang.String r2 = r2.toString()
            com.uc.webview.export.internal.utility.Log.i(r0, r2)
            w = r1
            r0 = 10033(0x2731, float:1.4059E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            a((int) r0, (java.lang.Object[]) r1)
            r0 = 10038(0x2736, float:1.4066E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            a((int) r0, (java.lang.Object[]) r1)
            goto L_0x0009
        L_0x00a7:
            r0 = r14[r6]
            java.lang.String r0 = (java.lang.String) r0
            java.util.Map<java.lang.String, java.lang.String> r1 = w
            if (r1 == 0) goto L_0x0009
            java.util.Map<java.lang.String, java.lang.String> r1 = w
            java.lang.Object r0 = r1.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            r8 = r0
            goto L_0x0009
        L_0x00ba:
            r0 = r14[r6]
            java.lang.String r0 = (java.lang.String) r0
            r1 = r14[r2]
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            r3 = 10005(0x2715, float:1.402E-41)
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r2[r6] = r0
            java.lang.Object r0 = a((int) r3, (java.lang.Object[]) r2)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x00db
            r0 = r1
        L_0x00d5:
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r0)
            goto L_0x0009
        L_0x00db:
            java.lang.String r1 = "true"
            boolean r0 = r1.equalsIgnoreCase(r0)
            goto L_0x00d5
        L_0x00e3:
            r0 = r14[r6]
            java.lang.String r0 = (java.lang.String) r0
            java.util.Map<java.lang.String, java.lang.String> r1 = w
            if (r1 == 0) goto L_0x00f9
            java.util.Map<java.lang.String, java.lang.String> r1 = w
            boolean r0 = r1.containsKey(r0)
            if (r0 == 0) goto L_0x00f9
        L_0x00f3:
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r2)
            goto L_0x0009
        L_0x00f9:
            r2 = r6
            goto L_0x00f3
        L_0x00fb:
            r0 = r14[r6]
            com.uc.webview.export.internal.a r0 = (com.uc.webview.export.internal.a) r0
            C = r0
            goto L_0x0009
        L_0x0103:
            f = r2
            goto L_0x0009
        L_0x0107:
            int r0 = A
            if (r0 == 0) goto L_0x0111
        L_0x010b:
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r2)
            goto L_0x0009
        L_0x0111:
            r2 = r6
            goto L_0x010b
        L_0x0113:
            r0 = 10010(0x271a, float:1.4027E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r1)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0133
            java.util.concurrent.ConcurrentLinkedQueue r0 = com.uc.webview.export.internal.d.b.a
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L_0x0133
        L_0x012d:
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r2)
            goto L_0x0009
        L_0x0133:
            r2 = r6
            goto L_0x012d
        L_0x0135:
            r0 = 10010(0x271a, float:1.4027E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r1)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 != 0) goto L_0x014e
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.String r1 = "UC WebView Sdk not inited."
            r0.<init>(r1)
            throw r0
        L_0x014e:
            r0 = 10020(0x2724, float:1.4041E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r1)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r0 != r12) goto L_0x0163
            java.lang.String r8 = "System WebView"
            goto L_0x0009
        L_0x0163:
            java.lang.String r8 = g
            goto L_0x0009
        L_0x0167:
            r1 = r14[r6]
            android.content.Context r1 = (android.content.Context) r1
            r2 = r14[r2]
            android.util.AttributeSet r2 = (android.util.AttributeSet) r2
            r3 = r14[r12]
            com.uc.webview.export.WebView r3 = (com.uc.webview.export.WebView) r3
            r0 = 3
            r0 = r14[r0]
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r4 = r0.booleanValue()
            r0 = 4
            r5 = r14[r0]
            int[] r5 = (int[]) r5
            android.content.Context r0 = e
            if (r0 != 0) goto L_0x018b
            android.content.Context r0 = r1.getApplicationContext()
            e = r0
        L_0x018b:
            boolean r0 = f
            if (r0 == 0) goto L_0x0194
            java.lang.Object[] r0 = new java.lang.Object[r6]
            a((int) r7, (java.lang.Object[]) r0)
        L_0x0194:
            boolean r0 = f     // Catch:{ Throwable -> 0x0a00 }
            if (r0 != 0) goto L_0x01a9
            com.uc.webview.export.internal.setup.a r0 = new com.uc.webview.export.internal.setup.a     // Catch:{ Throwable -> 0x0a00 }
            r0.<init>()     // Catch:{ Throwable -> 0x0a00 }
            java.lang.String r6 = "CONTEXT"
            com.uc.webview.export.internal.setup.UCSubSetupTask r0 = r0.setup(r6, r1)     // Catch:{ Throwable -> 0x0a00 }
            com.uc.webview.export.internal.setup.a r0 = (com.uc.webview.export.internal.setup.a) r0     // Catch:{ Throwable -> 0x0a00 }
            r0.start()     // Catch:{ Throwable -> 0x0a00 }
        L_0x01a9:
            android.webkit.ValueCallback<android.util.Pair<java.lang.String, java.util.HashMap<java.lang.String, java.lang.String>>> r0 = b
            if (r0 == 0) goto L_0x01ba
            android.webkit.ValueCallback<android.util.Pair<java.lang.String, java.util.HashMap<java.lang.String, java.lang.String>>> r0 = b
            android.util.Pair r6 = new android.util.Pair
            java.lang.String r7 = "sdk_wv_b"
            r6.<init>(r7, r8)
            r0.onReceiveValue(r6)
        L_0x01ba:
            com.uc.webview.export.internal.a r0 = C
            com.uc.webview.export.internal.interfaces.IWebView r0 = r0.a(r1, r2, r3, r4, r5)
            android.webkit.ValueCallback<android.util.Pair<java.lang.String, java.util.HashMap<java.lang.String, java.lang.String>>> r1 = b
            if (r1 == 0) goto L_0x01d1
            android.webkit.ValueCallback<android.util.Pair<java.lang.String, java.util.HashMap<java.lang.String, java.lang.String>>> r1 = b
            android.util.Pair r2 = new android.util.Pair
            java.lang.String r3 = "sdk_wv_a"
            r2.<init>(r3, r8)
            r1.onReceiveValue(r2)
        L_0x01d1:
            r8 = r0
            goto L_0x0009
        L_0x01d4:
            r0 = r14[r6]
            android.content.Context r0 = (android.content.Context) r0
            r1 = r14[r2]
            com.uc.webview.export.internal.interfaces.IWebView r1 = (com.uc.webview.export.internal.interfaces.IWebView) r1
            r2 = r14[r12]
            java.lang.Integer r2 = (java.lang.Integer) r2
            int r2 = r2.intValue()
            android.content.Context r3 = e
            if (r3 != 0) goto L_0x01ee
            android.content.Context r0 = r0.getApplicationContext()
            e = r0
        L_0x01ee:
            java.lang.Object[] r0 = new java.lang.Object[r6]
            a((int) r7, (java.lang.Object[]) r0)
            if (r2 == r12) goto L_0x0009
            com.uc.webview.export.extension.UCExtension r8 = new com.uc.webview.export.extension.UCExtension
            r8.<init>(r1)
            goto L_0x0009
        L_0x01fc:
            r0 = r14[r6]
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r1 = r0.intValue()
            r0 = r14[r2]
            android.content.Context r0 = (android.content.Context) r0
            android.content.Context r2 = e
            if (r2 != 0) goto L_0x0212
            android.content.Context r2 = r0.getApplicationContext()
            e = r2
        L_0x0212:
            java.lang.Object[] r2 = new java.lang.Object[r6]
            a((int) r7, (java.lang.Object[]) r2)
            if (r1 != r12) goto L_0x0220
            com.uc.webview.export.internal.a.s r8 = new com.uc.webview.export.internal.a.s
            r8.<init>()
            goto L_0x0009
        L_0x0220:
            boolean r1 = f
            if (r1 != 0) goto L_0x0009
            com.uc.webview.export.internal.c.d r8 = new com.uc.webview.export.internal.c.d
            r8.<init>(r0)
            goto L_0x0009
        L_0x022b:
            r0 = 10020(0x2724, float:1.4041E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r1)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r0 == r12) goto L_0x0009
            com.uc.webview.export.internal.interfaces.UCMobileWebKit r8 = com.uc.webview.export.internal.c.b.m()
            goto L_0x0009
        L_0x0241:
            r0 = r14[r6]
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            java.lang.Object[] r1 = new java.lang.Object[r6]
            a((int) r7, (java.lang.Object[]) r1)
            if (r0 != r12) goto L_0x0257
            com.uc.webview.export.internal.a.o r8 = new com.uc.webview.export.internal.a.o
            r8.<init>()
            goto L_0x0009
        L_0x0257:
            com.uc.webview.export.internal.interfaces.IWebStorage r8 = com.uc.webview.export.internal.c.b.o()
            goto L_0x0009
        L_0x025d:
            r0 = r14[r6]
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            java.lang.Object[] r1 = new java.lang.Object[r6]
            a((int) r7, (java.lang.Object[]) r1)
            if (r0 != r12) goto L_0x0273
            com.uc.webview.export.internal.a.a r8 = new com.uc.webview.export.internal.a.a
            r8.<init>()
            goto L_0x0009
        L_0x0273:
            com.uc.webview.export.internal.interfaces.ICookieManager r8 = com.uc.webview.export.internal.c.b.l()
            goto L_0x0009
        L_0x0279:
            r0 = r14[r6]
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            java.lang.Object[] r1 = new java.lang.Object[r6]
            a((int) r7, (java.lang.Object[]) r1)
            if (r0 != r12) goto L_0x028f
            com.uc.webview.export.internal.a.c r8 = new com.uc.webview.export.internal.a.c
            r8.<init>()
            goto L_0x0009
        L_0x028f:
            com.uc.webview.export.internal.interfaces.IGeolocationPermissions r8 = com.uc.webview.export.internal.c.b.n()
            goto L_0x0009
        L_0x0295:
            r0 = r14[r6]
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            java.lang.Object[] r1 = new java.lang.Object[r6]
            a((int) r7, (java.lang.Object[]) r1)
            if (r0 != r12) goto L_0x02ab
            com.uc.webview.export.internal.a.g r8 = new com.uc.webview.export.internal.a.g
            r8.<init>()
            goto L_0x0009
        L_0x02ab:
            com.uc.webview.export.internal.interfaces.IMimeTypeMap r8 = com.uc.webview.export.internal.c.b.p()
            goto L_0x0009
        L_0x02b1:
            java.lang.Object[] r0 = new java.lang.Object[r6]
            a((int) r7, (java.lang.Object[]) r0)
            int r0 = A
            if (r0 != 0) goto L_0x02c0
            java.lang.Integer r8 = java.lang.Integer.valueOf(r2)
            goto L_0x0009
        L_0x02c0:
            int r0 = A
            java.lang.Integer r8 = java.lang.Integer.valueOf(r0)
            goto L_0x0009
        L_0x02c8:
            r0 = r14[r6]
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r0 == 0) goto L_0x0009
            A = r0
            r0 = 10025(0x2729, float:1.4048E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            a((int) r0, (java.lang.Object[]) r1)
            goto L_0x0009
        L_0x02dd:
            com.uc.webview.export.internal.interfaces.IGlobalSettings r0 = B
            if (r0 == 0) goto L_0x02e5
            com.uc.webview.export.internal.interfaces.IGlobalSettings r8 = B
            goto L_0x0009
        L_0x02e5:
            r0 = 10020(0x2724, float:1.4041E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r1)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r0 == r12) goto L_0x0009
            com.uc.webview.export.internal.interfaces.IGlobalSettings r8 = com.uc.webview.export.internal.c.b.k()
            B = r8
            goto L_0x0009
        L_0x02fd:
            r0 = r14[r6]
            android.content.Context r0 = (android.content.Context) r0
            android.content.Context r1 = e
            if (r1 != 0) goto L_0x030b
            android.content.Context r1 = r0.getApplicationContext()
            e = r1
        L_0x030b:
            boolean r1 = f
            if (r1 != 0) goto L_0x0009
            r1 = 10010(0x271a, float:1.4027E-41)
            java.lang.Object[] r2 = new java.lang.Object[r6]
            java.lang.Object r1 = a((int) r1, (java.lang.Object[]) r2)
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            if (r1 != 0) goto L_0x0009
            java.lang.Class<com.uc.webview.export.internal.d> r1 = com.uc.webview.export.internal.d.class
            monitor-enter(r1)
            boolean r2 = n     // Catch:{ all -> 0x0367 }
            if (r2 != 0) goto L_0x032f
            com.uc.webview.export.utility.SetupTask r2 = y     // Catch:{ all -> 0x0367 }
            if (r2 == 0) goto L_0x0339
            com.uc.webview.export.utility.SetupTask r0 = y     // Catch:{ all -> 0x0367 }
            r0.start()     // Catch:{ all -> 0x0367 }
        L_0x032f:
            monitor-exit(r1)     // Catch:{ all -> 0x0367 }
            r0 = 10045(0x273d, float:1.4076E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            a((int) r0, (java.lang.Object[]) r1)
            goto L_0x0009
        L_0x0339:
            com.uc.webview.export.internal.setup.ak r2 = new com.uc.webview.export.internal.setup.ak     // Catch:{ all -> 0x0367 }
            r2.<init>()     // Catch:{ all -> 0x0367 }
            java.lang.String r3 = "CONTEXT"
            android.content.Context r0 = r0.getApplicationContext()     // Catch:{ all -> 0x0367 }
            com.uc.webview.export.internal.setup.UCSubSetupTask r0 = r2.setup(r3, r0)     // Catch:{ all -> 0x0367 }
            com.uc.webview.export.internal.setup.u r0 = (com.uc.webview.export.internal.setup.u) r0     // Catch:{ all -> 0x0367 }
            java.lang.String r2 = "AC"
            java.lang.String r3 = "true"
            com.uc.webview.export.internal.setup.UCSubSetupTask r0 = r0.setup(r2, r3)     // Catch:{ all -> 0x0367 }
            com.uc.webview.export.internal.setup.u r0 = (com.uc.webview.export.internal.setup.u) r0     // Catch:{ all -> 0x0367 }
            java.lang.String r2 = "VIDEO_AC"
            java.lang.String r3 = "false"
            com.uc.webview.export.internal.setup.UCSubSetupTask r0 = r0.setup(r2, r3)     // Catch:{ all -> 0x0367 }
            com.uc.webview.export.internal.setup.u r0 = (com.uc.webview.export.internal.setup.u) r0     // Catch:{ all -> 0x0367 }
            r0.start()     // Catch:{ all -> 0x0367 }
            goto L_0x032f
        L_0x0367:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0367 }
            throw r0
        L_0x036a:
            r0 = r14[r6]
            java.lang.Long r0 = (java.lang.Long) r0
            long r0 = r0.longValue()
            java.lang.Object r2 = F
            monitor-enter(r2)
            java.lang.Object r3 = F     // Catch:{ Exception -> 0x0380 }
            r3.wait(r0)     // Catch:{ Exception -> 0x0380 }
        L_0x037a:
            monitor-exit(r2)     // Catch:{ all -> 0x037d }
            goto L_0x0009
        L_0x037d:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x037d }
            throw r0
        L_0x0380:
            r0 = move-exception
            java.lang.String r1 = "tag_test_log"
            java.lang.String r3 = "getLock"
            com.uc.webview.export.internal.utility.Log.i(r1, r3, r0)     // Catch:{ all -> 0x037d }
            goto L_0x037a
        L_0x038b:
            java.lang.Object r1 = F
            monitor-enter(r1)
            java.lang.Object r0 = F     // Catch:{ Exception -> 0x0399 }
            r0.notifyAll()     // Catch:{ Exception -> 0x0399 }
        L_0x0393:
            monitor-exit(r1)     // Catch:{ all -> 0x0396 }
            goto L_0x0009
        L_0x0396:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0396 }
            throw r0
        L_0x0399:
            r0 = move-exception
            java.lang.String r2 = "tag_test_log"
            java.lang.String r3 = "releaseLock"
            com.uc.webview.export.internal.utility.Log.i(r2, r3, r0)     // Catch:{ all -> 0x0396 }
            goto L_0x0393
        L_0x03a4:
            android.os.Looper r0 = android.os.Looper.getMainLooper()
            android.os.Looper r1 = android.os.Looper.myLooper()
            if (r0 != r1) goto L_0x03b4
        L_0x03ae:
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r2)
            goto L_0x0009
        L_0x03b4:
            r2 = r6
            goto L_0x03ae
        L_0x03b6:
            r0 = r14[r6]
            java.lang.Runnable r0 = (java.lang.Runnable) r0
            com.uc.webview.export.internal.d.b.a((java.lang.Runnable) r0)
            goto L_0x0009
        L_0x03bf:
            r0 = r14[r6]
            android.content.Context r0 = (android.content.Context) r0
            android.content.Context r1 = e
            if (r1 != 0) goto L_0x03cd
            android.content.Context r1 = r0.getApplicationContext()
            e = r1
        L_0x03cd:
            boolean r1 = G
            if (r1 != 0) goto L_0x0009
            com.uc.webview.export.internal.c.a.a.a((android.content.Context) r0)
            G = r2
            goto L_0x0009
        L_0x03d8:
            r0 = 10011(0x271b, float:1.4028E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r1)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 != 0) goto L_0x0009
            boolean r0 = f
            if (r0 == 0) goto L_0x0405
            r0 = 10010(0x271a, float:1.4027E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r1)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 != 0) goto L_0x0405
            com.uc.webview.export.extension.InitCallback r0 = m
            if (r0 == 0) goto L_0x0405
            com.uc.webview.export.extension.InitCallback r0 = m
            r0.notInit()
        L_0x0405:
            boolean r0 = f
            if (r0 == 0) goto L_0x040d
            boolean r0 = com.uc.webview.export.Build.IS_INTERNATIONAL_VERSION
            if (r0 != 0) goto L_0x0009
        L_0x040d:
            com.uc.webview.export.utility.SetupTask r0 = y
            if (r0 == 0) goto L_0x0416
            com.uc.webview.export.utility.SetupTask r0 = y
            r0.start()
        L_0x0416:
            r0 = 10045(0x273d, float:1.4076E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            a((int) r0, (java.lang.Object[]) r1)
            goto L_0x0009
        L_0x041f:
            r0 = r14[r6]
            java.lang.String r0 = (java.lang.String) r0
            r1 = 10020(0x2724, float:1.4041E-41)
            java.lang.Object[] r2 = new java.lang.Object[r6]
            java.lang.Object r1 = a((int) r1, (java.lang.Object[]) r2)
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            if (r1 == r12) goto L_0x0009
            com.uc.webview.export.internal.c.b.a((java.lang.String) r0)
            goto L_0x0009
        L_0x0438:
            r0 = 10020(0x2724, float:1.4041E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r1)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r0 == r12) goto L_0x0009
            r0 = 10015(0x271f, float:1.4034E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r1)
            com.uc.webview.export.internal.interfaces.UCMobileWebKit r0 = (com.uc.webview.export.internal.interfaces.UCMobileWebKit) r0
            java.lang.Class r1 = r0.getClass()     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r2 = "sTrafficSent"
            java.lang.reflect.Field r1 = r1.getField(r2)     // Catch:{ Throwable -> 0x0480 }
            long r2 = r1.getLong(r0)     // Catch:{ Throwable -> 0x0480 }
            java.lang.Class r1 = r0.getClass()     // Catch:{ Throwable -> 0x0480 }
            java.lang.String r4 = "sTrafficReceived"
            java.lang.reflect.Field r1 = r1.getField(r4)     // Catch:{ Throwable -> 0x0480 }
            long r4 = r1.getLong(r0)     // Catch:{ Throwable -> 0x0480 }
            android.util.Pair r0 = new android.util.Pair     // Catch:{ Throwable -> 0x0480 }
            java.lang.Long r1 = java.lang.Long.valueOf(r2)     // Catch:{ Throwable -> 0x0480 }
            java.lang.Long r2 = java.lang.Long.valueOf(r4)     // Catch:{ Throwable -> 0x0480 }
            r0.<init>(r1, r2)     // Catch:{ Throwable -> 0x0480 }
            r8 = r0
            goto L_0x0009
        L_0x0480:
            r0 = move-exception
            java.lang.String r1 = "tag_test_log"
            java.lang.String r2 = "getTraffic"
            com.uc.webview.export.internal.utility.Log.d(r1, r2, r0)
            goto L_0x0009
        L_0x048c:
            int r0 = D
            java.lang.Integer r8 = java.lang.Integer.valueOf(r0)
            goto L_0x0009
        L_0x0494:
            r0 = r14[r6]
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            D = r0
            goto L_0x0009
        L_0x04a0:
            r0 = r14[r6]
            java.lang.String r0 = (java.lang.String) r0
            r1 = 10020(0x2724, float:1.4041E-41)
            java.lang.Object[] r2 = new java.lang.Object[r6]
            java.lang.Object r1 = a((int) r1, (java.lang.Object[]) r2)
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            if (r1 == r12) goto L_0x0009
            com.uc.webview.export.WebResourceResponse r8 = com.uc.webview.export.internal.c.b.b(r0)     // Catch:{ Throwable -> 0x04ba }
            goto L_0x0009
        L_0x04ba:
            r0 = move-exception
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.String r1 = "The getResponseByUrl() is not support in this version."
            r0.<init>(r1)
            throw r0
        L_0x04c4:
            r0 = r14[r6]
            android.content.Context r0 = (android.content.Context) r0
            r1 = r14[r2]
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            android.content.Context r2 = e
            if (r2 != 0) goto L_0x04da
            android.content.Context r2 = r0.getApplicationContext()
            e = r2
        L_0x04da:
            java.lang.Object[] r2 = new java.lang.Object[r6]
            a((int) r7, (java.lang.Object[]) r2)
            if (r1 != r12) goto L_0x0009
            com.uc.webview.export.internal.a.r r8 = new com.uc.webview.export.internal.a.r
            r8.<init>(r0)
            goto L_0x0009
        L_0x04e8:
            r0 = 10006(0x2716, float:1.4021E-41)
            java.lang.Object[] r1 = new java.lang.Object[r12]
            java.lang.String r3 = "apollo"
            r1[r6] = r3
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r2)
            r1[r2] = r3
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r1)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r1 = r0.booleanValue()
            if (r1 != 0) goto L_0x0545
            r0 = 10001(0x2711, float:1.4014E-41)
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r4 = 262144(0x40000, double:1.295163E-318)
            java.lang.Long r3 = java.lang.Long.valueOf(r4)
            r2[r6] = r3
            a((int) r0, (java.lang.Object[]) r2)
        L_0x0513:
            r0 = 10010(0x271a, float:1.4027E-41)
            java.lang.Object[] r2 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r2)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0009
            r0 = 10022(0x2726, float:1.4044E-41)
            java.lang.Object[] r2 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r2)
            com.uc.webview.export.internal.interfaces.IGlobalSettings r0 = (com.uc.webview.export.internal.interfaces.IGlobalSettings) r0
            if (r0 == 0) goto L_0x0009
            if (r1 != 0) goto L_0x0556
            java.lang.String r1 = "tag_test_log"
            java.lang.String r2 = "sdk cd forbid apollo"
            com.uc.webview.export.internal.utility.Log.i(r1, r2)
            java.lang.String r1 = "sdk_apollo_forbid"
            java.lang.String r2 = "1"
            r0.setStringValue(r1, r2)
            goto L_0x0009
        L_0x0545:
            r0 = 10002(0x2712, float:1.4016E-41)
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r4 = 262144(0x40000, double:1.295163E-318)
            java.lang.Long r3 = java.lang.Long.valueOf(r4)
            r2[r6] = r3
            a((int) r0, (java.lang.Object[]) r2)
            goto L_0x0513
        L_0x0556:
            java.lang.String r1 = "sdk_apollo_forbid"
            java.lang.String r2 = "0"
            r0.setStringValue(r1, r2)
            goto L_0x0009
        L_0x0561:
            r0 = r14[r6]
            java.util.Map r0 = (java.util.Map) r0
            if (r0 == 0) goto L_0x0587
            java.lang.String r1 = "ucPlayerRoot"
            java.lang.Object r1 = r0.get(r1)
            if (r1 == 0) goto L_0x0576
            java.lang.String r1 = r1.toString()
            r = r1
        L_0x0576:
            java.lang.String r1 = "ucPlayer"
            java.lang.Object r0 = r0.get(r1)
            if (r0 == 0) goto L_0x0587
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            q = r0
        L_0x0587:
            java.lang.String r0 = "tag_test_log"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "sUseUCPlayer:"
            r1.<init>(r2)
            boolean r2 = q
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = ",sUCPlayerSoRoot:"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = r
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            goto L_0x0009
        L_0x05ae:
            r0 = r14[r6]
            android.content.Context r0 = (android.content.Context) r0
            android.content.Context r1 = e
            if (r1 != 0) goto L_0x05bc
            android.content.Context r1 = r0.getApplicationContext()
            e = r1
        L_0x05bc:
            boolean r1 = q
            if (r1 == 0) goto L_0x0009
            boolean r1 = z
            if (r1 != 0) goto L_0x0009
            r1 = 10010(0x271a, float:1.4027E-41)
            java.lang.Object[] r3 = new java.lang.Object[r6]
            java.lang.Object r1 = a((int) r1, (java.lang.Object[]) r3)
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x0009
            int r1 = A
            if (r1 == r12) goto L_0x0009
            r1 = 10036(0x2734, float:1.4063E-41)
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r2[r6] = r0
            java.lang.Object r0 = a((int) r1, (java.lang.Object[]) r2)
            java.io.File r0 = (java.io.File) r0
            if (r0 == 0) goto L_0x0009
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = r0.getAbsolutePath()
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = "/"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = r1.toString()
            java.lang.String r1 = "tag_test_log"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "ucPlayerDir:"
            r3.<init>(r4)
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.String r3 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r1, r3)
            java.lang.String r1 = "com.uc.media.interfaces.IApolloHelper$Global"
            r3 = 1
            java.lang.ClassLoader r4 = c     // Catch:{ Exception -> 0x0655, all -> 0x0674 }
            java.lang.Class r1 = java.lang.Class.forName(r1, r3, r4)     // Catch:{ Exception -> 0x0655, all -> 0x0674 }
            java.lang.String r3 = "setApolloSoPath"
            r4 = 1
            java.lang.Class[] r4 = new java.lang.Class[r4]     // Catch:{ Exception -> 0x0655, all -> 0x0674 }
            r5 = 0
            java.lang.Class<java.lang.String> r6 = java.lang.String.class
            r4[r5] = r6     // Catch:{ Exception -> 0x0655, all -> 0x0674 }
            r5 = 1
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x0655, all -> 0x0674 }
            r6 = 0
            r5[r6] = r2     // Catch:{ Exception -> 0x0655, all -> 0x0674 }
            com.uc.webview.export.internal.utility.ReflectionUtil.invoke((java.lang.Class<?>) r1, (java.lang.String) r3, (java.lang.Class[]) r4, (java.lang.Object[]) r5)     // Catch:{ Exception -> 0x0655, all -> 0x0674 }
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x0655, all -> 0x0674 }
            java.lang.String r1 = ".lock"
            r3.<init>(r0, r1)     // Catch:{ Exception -> 0x0655, all -> 0x0674 }
            boolean r0 = r3.exists()     // Catch:{ Exception -> 0x0655, all -> 0x0674 }
            if (r0 != 0) goto L_0x0a09
            java.io.FileWriter r1 = new java.io.FileWriter     // Catch:{ Exception -> 0x0655, all -> 0x0674 }
            r1.<init>(r3)     // Catch:{ Exception -> 0x0655, all -> 0x0674 }
            java.lang.String r0 = "2.6.0.167"
            r1.write(r0)     // Catch:{ Exception -> 0x09fd }
        L_0x064b:
            s = r2     // Catch:{ Exception -> 0x09fd }
            r0 = 1
            z = r0     // Catch:{ Exception -> 0x09fd }
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            goto L_0x0009
        L_0x0655:
            r0 = move-exception
            r1 = r8
        L_0x0657:
            java.lang.String r2 = "tag_test_log"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x09f9 }
            java.lang.String r4 = "setupForUCPlayer:"
            r3.<init>(r4)     // Catch:{ all -> 0x09f9 }
            java.lang.ClassLoader r4 = c     // Catch:{ all -> 0x09f9 }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x09f9 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x09f9 }
            com.uc.webview.export.internal.utility.Log.d(r2, r3, r0)     // Catch:{ all -> 0x09f9 }
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            goto L_0x0009
        L_0x0674:
            r0 = move-exception
        L_0x0675:
            com.uc.webview.export.cyclone.UCCyclone.close(r8)
            throw r0
        L_0x0679:
            r0 = r14[r6]
            android.content.Context r0 = (android.content.Context) r0
            r1 = r14[r2]
            java.lang.String r1 = (java.lang.String) r1
            android.content.Context r2 = e
            if (r2 != 0) goto L_0x068b
            android.content.Context r2 = r0.getApplicationContext()
            e = r2
        L_0x068b:
            r2 = 2
            android.content.Context r0 = r0.createPackageContext(r1, r2)     // Catch:{ Exception -> 0x070f, all -> 0x0721 }
            java.lang.String r1 = "sdk_2"
            java.io.File r0 = r0.getFileStreamPath(r1)     // Catch:{ Exception -> 0x070f, all -> 0x0721 }
            boolean r1 = r0.exists()     // Catch:{ Exception -> 0x070f, all -> 0x0721 }
            if (r1 == 0) goto L_0x0a06
            java.io.FileReader r1 = new java.io.FileReader     // Catch:{ Exception -> 0x070f, all -> 0x0721 }
            r1.<init>(r0)     // Catch:{ Exception -> 0x070f, all -> 0x0721 }
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ Exception -> 0x09f1 }
            r2 = 500(0x1f4, float:7.0E-43)
            r0.<init>(r1, r2)     // Catch:{ Exception -> 0x09f1 }
            java.lang.String r2 = r0.readLine()     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            if (r2 == 0) goto L_0x0702
            java.lang.String r2 = r2.trim()     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            r3.<init>(r2)     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            boolean r3 = r3.exists()     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            if (r3 == 0) goto L_0x0702
            java.lang.String r3 = "tag_test_log"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            java.lang.String r5 = "setupUCPlayerForThin:"
            r4.<init>(r5)     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            java.lang.StringBuilder r4 = r4.append(r2)     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            com.uc.webview.export.internal.utility.Log.i(r3, r4)     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            java.lang.String r3 = "com.uc.media.interfaces.IApolloHelper$Global"
            r4 = 1
            java.lang.ClassLoader r5 = c     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            java.lang.Class r3 = java.lang.Class.forName(r3, r4, r5)     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            java.lang.String r4 = "setApolloSoPath"
            r5 = 1
            java.lang.Class[] r5 = new java.lang.Class[r5]     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            r7 = 0
            java.lang.Class<java.lang.String> r8 = java.lang.String.class
            r5[r7] = r8     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            r7 = 1
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            r8 = 0
            r7[r8] = r2     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            com.uc.webview.export.internal.utility.ReflectionUtil.invoke((java.lang.Class<?>) r3, (java.lang.String) r4, (java.lang.Class[]) r5, (java.lang.Object[]) r7)     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            r2 = 1
            z = r2     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            r2 = 1
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r2)     // Catch:{ Exception -> 0x09f4, all -> 0x09ec }
            com.uc.webview.export.cyclone.UCCyclone.close(r0)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            goto L_0x0009
        L_0x0702:
            r8 = r1
        L_0x0703:
            com.uc.webview.export.cyclone.UCCyclone.close(r0)
            com.uc.webview.export.cyclone.UCCyclone.close(r8)
        L_0x0709:
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r6)
            goto L_0x0009
        L_0x070f:
            r0 = move-exception
            r1 = r8
        L_0x0711:
            java.lang.String r2 = "tag_test_log"
            java.lang.String r3 = "setupUCPlayerForThin"
            com.uc.webview.export.internal.utility.Log.i(r2, r3, r0)     // Catch:{ all -> 0x09e9 }
            com.uc.webview.export.cyclone.UCCyclone.close(r8)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            goto L_0x0709
        L_0x0721:
            r0 = move-exception
            r1 = r8
        L_0x0723:
            com.uc.webview.export.cyclone.UCCyclone.close(r8)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            throw r0
        L_0x072a:
            r0 = r14[r6]
            android.content.Context r0 = (android.content.Context) r0
            android.content.Context r1 = e
            if (r1 != 0) goto L_0x0738
            android.content.Context r1 = r0.getApplicationContext()
            e = r1
        L_0x0738:
            r1 = 1
            java.io.File[] r1 = new java.io.File[r1]     // Catch:{ Throwable -> 0x075b }
            r2 = 0
            r3 = 0
            r1[r2] = r3     // Catch:{ Throwable -> 0x075b }
            java.io.File r0 = com.uc.webview.export.utility.download.UpdateTask.getUCPlayerRoot(r0)     // Catch:{ Throwable -> 0x075b }
            r2 = 10037(0x2735, float:1.4065E-41)
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x075b }
            r4 = 0
            r3[r4] = r0     // Catch:{ Throwable -> 0x075b }
            r0 = 1
            r3[r0] = r1     // Catch:{ Throwable -> 0x075b }
            a((int) r2, (java.lang.Object[]) r3)     // Catch:{ Throwable -> 0x075b }
            r0 = 0
            r0 = r1[r0]     // Catch:{ Throwable -> 0x075b }
            if (r0 == 0) goto L_0x0009
            r0 = 0
            r8 = r1[r0]     // Catch:{ Throwable -> 0x075b }
            goto L_0x0009
        L_0x075b:
            r0 = move-exception
            java.lang.String r1 = "tag_test_log"
            java.lang.String r2 = "getUCPlayerDir"
            com.uc.webview.export.internal.utility.Log.e(r1, r2, r0)
            goto L_0x0009
        L_0x0767:
            r0 = r14[r6]
            java.io.File r0 = (java.io.File) r0
            r1 = r14[r2]
            java.io.File[] r1 = (java.io.File[]) r1
            boolean r3 = r0.exists()
            if (r3 == 0) goto L_0x0009
            boolean r3 = r0.isDirectory()
            if (r3 == 0) goto L_0x0009
            java.io.File r3 = new java.io.File
            java.lang.String r4 = "libu3player.so"
            r3.<init>(r0, r4)
            boolean r4 = r3.exists()
            if (r4 == 0) goto L_0x07ae
            java.lang.String r4 = "libu3player.so"
            boolean r4 = com.uc.webview.export.utility.download.UpdateTask.isFinished(r0, r4)
            if (r4 == 0) goto L_0x07ae
            r4 = r1[r6]
            if (r4 == 0) goto L_0x07ac
            long r4 = r3.lastModified()
            java.io.File r3 = new java.io.File
            r7 = r1[r6]
            java.lang.String r9 = "libu3player.so"
            r3.<init>(r7, r9)
            long r10 = r3.lastModified()
            int r3 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1))
            if (r3 <= 0) goto L_0x07ae
        L_0x07ac:
            r1[r6] = r0
        L_0x07ae:
            java.io.File[] r3 = r0.listFiles()
            if (r3 == 0) goto L_0x0009
            int r4 = r3.length
            r0 = r6
        L_0x07b6:
            if (r0 >= r4) goto L_0x0009
            r5 = r3[r0]
            boolean r7 = r5.isDirectory()
            if (r7 == 0) goto L_0x07cb
            r7 = 10037(0x2735, float:1.4065E-41)
            java.lang.Object[] r9 = new java.lang.Object[r12]
            r9[r6] = r5
            r9[r2] = r1
            a((int) r7, (java.lang.Object[]) r9)
        L_0x07cb:
            int r0 = r0 + 1
            goto L_0x07b6
        L_0x07ce:
            r0 = 10006(0x2716, float:1.4021E-41)
            java.lang.Object[] r1 = new java.lang.Object[r12]
            java.lang.String r3 = "swv"
            r1[r6] = r3
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r6)
            r1[r2] = r3
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r1)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r1 = r0.booleanValue()
            r0 = 10010(0x271a, float:1.4027E-41)
            java.lang.Object[] r3 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r3)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x083f
            boolean r0 = j
            if (r0 == 0) goto L_0x0009
            if (r1 == 0) goto L_0x0009
            java.lang.String r0 = com.uc.webview.export.Build.UCM_VERSION     // Catch:{ Exception -> 0x0833 }
            java.lang.String r3 = "\\."
            java.lang.String[] r0 = r0.split(r3)     // Catch:{ Exception -> 0x0833 }
            r3 = 1
            r3 = r0[r3]     // Catch:{ Exception -> 0x0833 }
            int r3 = com.uc.webview.export.internal.utility.c.b((java.lang.String) r3)     // Catch:{ Exception -> 0x0833 }
            r4 = 9
            if (r3 < r4) goto L_0x081c
            r3 = 2
            r0 = r0[r3]     // Catch:{ Exception -> 0x0833 }
            int r0 = com.uc.webview.export.internal.utility.c.b((java.lang.String) r0)     // Catch:{ Exception -> 0x0833 }
            r3 = 13
            if (r0 >= r3) goto L_0x0a03
        L_0x081c:
            r0 = r6
        L_0x081d:
            if (r0 == 0) goto L_0x0009
            A = r12
            r0 = 10001(0x2711, float:1.4014E-41)
            java.lang.Object[] r1 = new java.lang.Object[r2]
            r2 = 131072(0x20000, double:6.47582E-319)
            java.lang.Long r2 = java.lang.Long.valueOf(r2)
            r1[r6] = r2
            a((int) r0, (java.lang.Object[]) r1)
            goto L_0x0009
        L_0x0833:
            r0 = move-exception
            java.lang.String r1 = "tag_test_log"
            java.lang.String r3 = "checkForceSystemWebViewParam"
            com.uc.webview.export.internal.utility.Log.i(r1, r3, r0)
            r0 = r6
            goto L_0x081d
        L_0x083f:
            if (r1 == 0) goto L_0x0853
            r0 = 10001(0x2711, float:1.4014E-41)
            java.lang.Object[] r1 = new java.lang.Object[r2]
            r2 = 131072(0x20000, double:6.47582E-319)
            java.lang.Long r2 = java.lang.Long.valueOf(r2)
            r1[r6] = r2
            a((int) r0, (java.lang.Object[]) r1)
            goto L_0x0009
        L_0x0853:
            r0 = 10002(0x2712, float:1.4016E-41)
            java.lang.Object[] r1 = new java.lang.Object[r2]
            r2 = 131072(0x20000, double:6.47582E-319)
            java.lang.Long r2 = java.lang.Long.valueOf(r2)
            r1[r6] = r2
            a((int) r0, (java.lang.Object[]) r1)
            goto L_0x0009
        L_0x0865:
            r3 = r14[r6]
            java.lang.String r3 = (java.lang.String) r3
            r7 = r14[r2]
            java.lang.Runnable r7 = (java.lang.Runnable) r7
            r0 = 10020(0x2724, float:1.4041E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r1)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r0 == r12) goto L_0x0881
            java.lang.String r0 = x
            if (r0 != 0) goto L_0x089b
        L_0x0881:
            java.lang.String r0 = "tag_test_log"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "is system webView - "
            r1.<init>(r2)
            java.lang.String r2 = x
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.w(r0, r1)
            goto L_0x0009
        L_0x089b:
            java.io.File r4 = new java.io.File
            java.lang.String r0 = x
            java.lang.String r1 = "shared_prefs"
            r4.<init>(r0, r1)
            java.io.File r5 = new java.io.File
            java.lang.String r0 = "typeface.xml"
            r5.<init>(r4, r0)
            java.io.File r1 = new java.io.File
            java.lang.String r0 = "uc_typeface_hash_"
            r1.<init>(r4, r0)
            if (r3 == 0) goto L_0x08c1
            java.lang.String r0 = r3.trim()
            int r0 = r0.length()
            if (r0 != 0) goto L_0x08f2
        L_0x08c1:
            if (r2 == 0) goto L_0x08f4
            java.lang.String r0 = "uc_font_sys"
            int r0 = r0.hashCode()
            java.lang.String r0 = java.lang.String.valueOf(r0)
        L_0x08ce:
            r6 = 45
            r9 = 95
            java.lang.String r0 = r0.replace(r6, r9)
            java.io.File r6 = new java.io.File
            r6.<init>(r1, r0)
            boolean r0 = r6.exists()
            if (r0 == 0) goto L_0x0920
            boolean r0 = r5.exists()
            if (r0 == 0) goto L_0x0920
            java.lang.String r0 = "tag_test_log"
            java.lang.String r1 = "字体没变化..."
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            goto L_0x0009
        L_0x08f2:
            r2 = r6
            goto L_0x08c1
        L_0x08f4:
            java.io.File r0 = new java.io.File
            r0.<init>(r3)
            boolean r6 = r0.exists()
            if (r6 != 0) goto L_0x0917
            java.lang.String r1 = "tag_test_log"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "字体文件不存在-"
            r2.<init>(r3)
            java.lang.StringBuilder r0 = r2.append(r0)
            java.lang.String r0 = r0.toString()
            com.uc.webview.export.internal.utility.Log.d(r1, r0)
            goto L_0x0009
        L_0x0917:
            int r0 = r3.hashCode()
            java.lang.String r0 = java.lang.String.valueOf(r0)
            goto L_0x08ce
        L_0x0920:
            com.uc.webview.export.internal.e r0 = new com.uc.webview.export.internal.e     // Catch:{ Exception -> 0x092a }
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x092a }
            r0.start()     // Catch:{ Exception -> 0x092a }
            goto L_0x0009
        L_0x092a:
            r0 = move-exception
            java.lang.String r1 = "tag_test_log"
            java.lang.String r2 = "updateTypefacePath"
            com.uc.webview.export.internal.utility.Log.i(r1, r2, r0)
            goto L_0x0009
        L_0x0936:
            r0 = 10020(0x2724, float:1.4041E-41)
            java.lang.Object[] r1 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r1)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r0 != r12) goto L_0x094c
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r2)
            goto L_0x0009
        L_0x094c:
            boolean r0 = com.uc.webview.export.internal.c.b.q()
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r0)
            goto L_0x0009
        L_0x0956:
            com.uc.webview.export.cyclone.UCElapseTime r1 = new com.uc.webview.export.cyclone.UCElapseTime
            r1.<init>()
        L_0x095b:
            boolean r0 = com.uc.webview.export.utility.SetupTask.isSetupThread()
            if (r0 == 0) goto L_0x097c
            r0 = 10010(0x271a, float:1.4027E-41)
            java.lang.Object[] r3 = new java.lang.Object[r6]
            java.lang.Object r0 = a((int) r0, (java.lang.Object[]) r3)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 != 0) goto L_0x097c
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException
            r1 = 3013(0xbc5, float:4.222E-42)
            java.lang.String r2 = "Waitting for init in setup thread is wrong."
            r0.<init>((int) r1, (java.lang.String) r2)
            throw r0
        L_0x097c:
            com.uc.webview.export.internal.d.b.a((java.lang.Runnable) r8)
            boolean r0 = n
            if (r0 == 0) goto L_0x0986
            com.uc.webview.export.utility.SetupTask.resumeAll()
        L_0x0986:
            int r0 = A
            if (r0 != 0) goto L_0x09a7
            r0 = 10024(0x2728, float:1.4047E-41)
            java.lang.Object[] r3 = new java.lang.Object[r2]
            r4 = 1000(0x3e8, double:4.94E-321)
            java.lang.Long r4 = java.lang.Long.valueOf(r4)
            r3[r6] = r4
            a((int) r0, (java.lang.Object[]) r3)
            long r4 = r1.getMilis()
            long r10 = E
            int r0 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1))
            if (r0 < 0) goto L_0x095b
            int r0 = i
            if (r0 == r2) goto L_0x095b
        L_0x09a7:
            com.uc.webview.export.internal.d.b.a((java.lang.Runnable) r8)
            java.lang.Class<com.uc.webview.export.internal.d> r1 = com.uc.webview.export.internal.d.class
            monitor-enter(r1)
            int r0 = A     // Catch:{ all -> 0x09bb }
            if (r0 != 0) goto L_0x09b8
            int r0 = i     // Catch:{ all -> 0x09bb }
            if (r0 != r12) goto L_0x09be
            r0 = 2
            A = r0     // Catch:{ all -> 0x09bb }
        L_0x09b8:
            monitor-exit(r1)     // Catch:{ all -> 0x09bb }
            goto L_0x0009
        L_0x09bb:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x09bb }
            throw r0
        L_0x09be:
            int r0 = i     // Catch:{ all -> 0x09bb }
            r2 = 3
            if (r0 != r2) goto L_0x09b8
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ all -> 0x09bb }
            r2 = 4009(0xfa9, float:5.618E-42)
            java.lang.String r3 = "Thread [%s] waitting for init is up to [%s] milis."
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x09bb }
            r5 = 0
            java.lang.Thread r6 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x09bb }
            java.lang.String r6 = r6.getName()     // Catch:{ all -> 0x09bb }
            r4[r5] = r6     // Catch:{ all -> 0x09bb }
            r5 = 1
            long r6 = E     // Catch:{ all -> 0x09bb }
            java.lang.String r6 = java.lang.String.valueOf(r6)     // Catch:{ all -> 0x09bb }
            r4[r5] = r6     // Catch:{ all -> 0x09bb }
            java.lang.String r3 = java.lang.String.format(r3, r4)     // Catch:{ all -> 0x09bb }
            r0.<init>((int) r2, (java.lang.String) r3)     // Catch:{ all -> 0x09bb }
            throw r0     // Catch:{ all -> 0x09bb }
        L_0x09e9:
            r0 = move-exception
            goto L_0x0723
        L_0x09ec:
            r2 = move-exception
            r8 = r0
            r0 = r2
            goto L_0x0723
        L_0x09f1:
            r0 = move-exception
            goto L_0x0711
        L_0x09f4:
            r2 = move-exception
            r8 = r0
            r0 = r2
            goto L_0x0711
        L_0x09f9:
            r0 = move-exception
            r8 = r1
            goto L_0x0675
        L_0x09fd:
            r0 = move-exception
            goto L_0x0657
        L_0x0a00:
            r0 = move-exception
            goto L_0x01a9
        L_0x0a03:
            r0 = r1
            goto L_0x081d
        L_0x0a06:
            r0 = r8
            goto L_0x0703
        L_0x0a09:
            r1 = r8
            goto L_0x064b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.d.a(int, java.lang.Object[]):java.lang.Object");
    }

    /* compiled from: ProGuard */
    private static class b extends Handler {
        /* access modifiers changed from: private */
        public static final ConcurrentLinkedQueue<Runnable> a = new ConcurrentLinkedQueue<>();
        /* access modifiers changed from: private */
        public static UCSetupException b = null;
        private static final Runnable c = new f();

        private b(Looper looper) {
            super(looper);
        }

        public final void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    if (d.a != null) {
                        d.a.onNotAvailableUC(message.arg1);
                        d.a = null;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        static void a(Runnable runnable) {
            if (runnable != null) {
                a.add(runnable);
                new b(Looper.getMainLooper()).post(c);
            }
            if (((Boolean) d.a((int) UCMPackageInfo.hadInstallUCMobile, new Object[0])).booleanValue()) {
                if (b == null) {
                    c.run();
                }
                if (b != null) {
                    throw b;
                }
            }
        }
    }

    /* compiled from: ProGuard */
    private static class a extends a {
        private a() {
        }

        /* synthetic */ a(byte b) {
            this();
        }

        public final IWebView a(Context context, AttributeSet attributeSet, WebView webView, boolean z, int[] iArr) {
            if (d.e == null) {
                d.e = context.getApplicationContext();
            }
            d.a((int) UCMPackageInfo.getLibFilter, context);
            if (com.uc.webview.export.internal.c.a.a.a == null && d.e != null) {
                com.uc.webview.export.internal.c.a.a.a(d.e);
            }
            com.uc.webview.export.internal.c.a.a aVar = com.uc.webview.export.internal.c.a.a.a;
            if (!d.f) {
                if (((Boolean) d.a(10006, "stat", true)).booleanValue()) {
                    aVar.c.postDelayed(new c(aVar), 15000);
                }
            }
            int a = d.A;
            if (CommonDef.sOnCreateWindowType == 1 || z) {
                a = 2;
            }
            if (CommonDef.sOnCreateWindowType == 2) {
                a = com.uc.webview.export.internal.c.b.r().intValue();
            }
            iArr[0] = a;
            if (a == 2) {
                return new p(context, attributeSet, webView);
            }
            return com.uc.webview.export.internal.c.b.a(context, attributeSet);
        }
    }

    public static String a(UCMPackageInfo uCMPackageInfo, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("Type:").append(str);
        if (uCMPackageInfo.pkgName != null) {
            sb.append("\nPackage Name:").append(uCMPackageInfo.pkgName);
        }
        sb.append("\nSo files path:").append(uCMPackageInfo.soDirPath).append("\nDex files:\n").append((String) uCMPackageInfo.sdkShellModule.first).append("\n").append((String) uCMPackageInfo.browserIFModule.first).append("\n").append((String) uCMPackageInfo.coreImplModule.first);
        return sb.toString();
    }

    public static void a(String str) {
        g = str;
    }
}
