package com.loc;

import android.content.Context;
import android.os.Build;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.alibaba.motu.videoplayermonitor.VPMConstants;
import com.taobao.wireless.detail.DetailConfig;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: AuthConfigManager */
public final class l {
    public static int a = -1;
    public static String b = "";
    private static Context c = null;
    private static volatile boolean d = true;
    private static Vector<c> e = new Vector<>();
    private static Map<String, Integer> f = new HashMap();
    private static String g = null;
    private static long h = 0;

    /* compiled from: AuthConfigManager */
    public static class a {
        @Deprecated
        public c A;
        public c B;
        public b C;
        public b D;
        public b E;
        public b F;
        public e G;
        /* access modifiers changed from: private */
        public boolean H;
        public String a;
        public int b = -1;
        @Deprecated
        public JSONObject c;
        @Deprecated
        public JSONObject d;
        @Deprecated
        public JSONObject e;
        @Deprecated
        public JSONObject f;
        @Deprecated
        public JSONObject g;
        @Deprecated
        public JSONObject h;
        @Deprecated
        public JSONObject i;
        @Deprecated
        public JSONObject j;
        @Deprecated
        public JSONObject k;
        @Deprecated
        public JSONObject l;
        @Deprecated
        public JSONObject m;
        @Deprecated
        public JSONObject n;
        @Deprecated
        public JSONObject o;
        @Deprecated
        public JSONObject p;
        @Deprecated
        public JSONObject q;
        @Deprecated
        public JSONObject r;
        @Deprecated
        public JSONObject s;
        @Deprecated
        public JSONObject t;
        @Deprecated
        public JSONObject u;
        @Deprecated
        public JSONObject v;
        public JSONObject w;
        public C0002a x;
        public d y;
        public f z;

        /* renamed from: com.loc.l$a$a  reason: collision with other inner class name */
        /* compiled from: AuthConfigManager */
        public static class C0002a {
            public boolean a;
            public boolean b;
            public JSONObject c;
        }

        /* compiled from: AuthConfigManager */
        public static class b {
            public boolean a;
            public String b;
            public String c;
            public String d;
            public boolean e;
        }

        /* compiled from: AuthConfigManager */
        public static class c {
            public String a;
            public String b;
        }

        /* compiled from: AuthConfigManager */
        public static class d {
            public String a;
            public String b;
            public String c;
        }

        /* compiled from: AuthConfigManager */
        public static class e {
            public boolean a;
            public boolean b;
            public boolean c;
            public String d;
            public String e;
            public String f;
        }

        /* compiled from: AuthConfigManager */
        public static class f {
            public boolean a;
        }

        public final boolean a() {
            return this.H;
        }
    }

    /* compiled from: AuthConfigManager */
    static class b extends ax {
        private String f;
        private Map<String, String> g = null;
        private boolean h;

        b(Context context, t tVar, String str) {
            super(context, tVar);
            this.f = str;
            this.h = Build.VERSION.SDK_INT != 19;
        }

        public final boolean a() {
            return this.h;
        }

        public final byte[] a_() {
            return null;
        }

        public final Map<String, String> b() {
            return null;
        }

        public final String c() {
            return this.h ? "https://restapi.amap.com/v3/iasdkauth" : "http://restapi.amap.com/v3/iasdkauth";
        }

        public final byte[] e() {
            String x = n.x(this.a);
            if (TextUtils.isEmpty(x)) {
                x = n.i(this.a);
            }
            if (!TextUtils.isEmpty(x)) {
                x = r.a(new StringBuilder(x).reverse().toString());
            }
            HashMap hashMap = new HashMap();
            hashMap.put("authkey", this.f);
            hashMap.put("plattype", DispatchConstants.ANDROID);
            hashMap.put("product", this.b.a());
            hashMap.put("version", this.b.b());
            hashMap.put("output", "json");
            hashMap.put("androidversion", new StringBuilder().append(Build.VERSION.SDK_INT).toString());
            hashMap.put("deviceId", x);
            hashMap.put("manufacture", Build.MANUFACTURER);
            if (this.g != null && !this.g.isEmpty()) {
                hashMap.putAll(this.g);
            }
            hashMap.put("abitype", u.a(this.a));
            hashMap.put("ext", this.b.d());
            return u.a(u.a((Map<String, String>) hashMap));
        }

        /* access modifiers changed from: protected */
        public final String f() {
            return "3.0";
        }
    }

    /* compiled from: AuthConfigManager */
    private static class c {
        /* access modifiers changed from: private */
        public String a;
        /* access modifiers changed from: private */
        public String b;
        /* access modifiers changed from: private */
        public AtomicInteger c;

        public c(String str, String str2, int i) {
            this.a = str;
            this.b = str2;
            this.c = new AtomicInteger(i);
        }

        public static c b(String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                return new c(jSONObject.optString("a"), jSONObject.optString("f"), jSONObject.optInt("h"));
            } catch (Throwable th) {
                return null;
            }
        }

        public final int a() {
            if (this.c == null) {
                return 0;
            }
            return this.c.get();
        }

        public final void a(String str) {
            this.b = str;
        }

        public final String b() {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("a", this.a);
                jSONObject.put("f", this.b);
                jSONObject.put("h", this.c.get());
                return jSONObject.toString();
            } catch (Throwable th) {
                return "";
            }
        }
    }

    public static a a(Context context, t tVar, String str) {
        return b(context, tVar, str);
    }

    private static String a(JSONObject jSONObject, String str) throws JSONException {
        return (jSONObject != null && jSONObject.has(str) && !jSONObject.getString(str).equals("[]")) ? jSONObject.optString(str) : "";
    }

    public static void a(int i) {
        if (i == 2) {
            try {
                c b2 = b(c, "IPV6_CONFIG_NAME");
                String a2 = u.a(System.currentTimeMillis(), "yyyyMMdd");
                if (!a2.equals(b2.b)) {
                    b2.a(a2);
                    b2.c.set(0);
                }
                b2.c.incrementAndGet();
                Context context = c;
                if (b2 != null && !TextUtils.isEmpty(b2.a)) {
                    String b3 = b2.b();
                    if (!TextUtils.isEmpty(b3) && context != null) {
                        new al("IPV6_CONFIG_NAME").a(context, UploadQueueMgr.MSGTYPE_INTERVAL, b3);
                    }
                }
            } catch (Throwable th) {
            }
        }
    }

    public static void a(Context context) {
        if (context != null) {
            c = context.getApplicationContext();
        }
    }

    private static void a(Context context, t tVar, Throwable th) {
        c(context, tVar, th.getMessage());
    }

    public static void a(Context context, String str) {
        k.a(context, str);
    }

    private static void a(Context context, String str, a aVar, JSONObject jSONObject) throws JSONException {
        JSONObject jSONObject2;
        boolean a2;
        a.C0002a aVar2 = new a.C0002a();
        aVar2.a = false;
        aVar2.b = false;
        aVar.x = aVar2;
        try {
            String[] split = str.split(SymbolExpUtil.SYMBOL_SEMICOLON);
            if (split != null && split.length > 0) {
                for (String str2 : split) {
                    if (jSONObject.has(str2)) {
                        aVar.w.putOpt(str2, jSONObject.get(str2));
                    }
                }
            }
        } catch (Throwable th) {
            y.a(th, "at", "co");
        }
        if (u.a(jSONObject, "16H")) {
            boolean unused = aVar.H = a(jSONObject.getJSONObject("16H").optString("able"), false);
        }
        if (u.a(jSONObject, "11K")) {
            try {
                JSONObject jSONObject3 = jSONObject.getJSONObject("11K");
                aVar2.a = a(jSONObject3.getString("able"), false);
                if (jSONObject3.has("off")) {
                    aVar2.c = jSONObject3.getJSONObject("off");
                }
            } catch (Throwable th2) {
                y.a(th2, "AuthConfigManager", "loadException");
            }
        }
        if (u.a(jSONObject, "001")) {
            JSONObject jSONObject4 = jSONObject.getJSONObject("001");
            a.d dVar = new a.d();
            if (jSONObject4 != null) {
                try {
                    String a3 = a(jSONObject4, "md5");
                    String a4 = a(jSONObject4, "url");
                    String a5 = a(jSONObject4, "sdkversion");
                    if (!TextUtils.isEmpty(a3) && !TextUtils.isEmpty(a4) && !TextUtils.isEmpty(a5)) {
                        dVar.a = a4;
                        dVar.b = a3;
                        dVar.c = a5;
                    }
                } catch (Throwable th3) {
                    y.a(th3, "at", "psu");
                }
            }
            aVar.y = dVar;
        }
        if (u.a(jSONObject, "002")) {
            JSONObject jSONObject5 = jSONObject.getJSONObject("002");
            a.c cVar = new a.c();
            a(jSONObject5, cVar);
            aVar.A = cVar;
        }
        if (u.a(jSONObject, "14S")) {
            JSONObject jSONObject6 = jSONObject.getJSONObject("14S");
            a.c cVar2 = new a.c();
            a(jSONObject6, cVar2);
            aVar.B = cVar2;
        }
        a(aVar, jSONObject);
        if (u.a(jSONObject, "14Z")) {
            JSONObject jSONObject7 = jSONObject.getJSONObject("14Z");
            a.e eVar = new a.e();
            try {
                String a6 = a(jSONObject7, "md5");
                String a7 = a(jSONObject7, "md5info");
                String a8 = a(jSONObject7, "url");
                String a9 = a(jSONObject7, "able");
                String a10 = a(jSONObject7, "on");
                String a11 = a(jSONObject7, "mobileable");
                eVar.e = a6;
                eVar.f = a7;
                eVar.d = a8;
                eVar.a = a(a9, false);
                eVar.b = a(a10, false);
                eVar.c = a(a11, false);
            } catch (Throwable th4) {
                y.a(th4, "at", "pes");
            }
            aVar.G = eVar;
        }
        if (u.a(jSONObject, "151")) {
            JSONObject jSONObject8 = jSONObject.getJSONObject("151");
            a.f fVar = new a.f();
            if (jSONObject8 != null) {
                fVar.a = a(jSONObject8.optString("able"), false);
            }
            aVar.z = fVar;
        }
        if (!(!u.a(jSONObject, "17S") || (jSONObject2 = jSONObject.getJSONObject("17S")) == null || (a2 = a(jSONObject2.optString("able"), false)) == d)) {
            d = a2;
            if (context != null) {
                new al("IPV6_CONFIG_NAME").a(context, "k", a2);
            }
        }
        a(aVar, jSONObject);
    }

    private static void a(a aVar, JSONObject jSONObject) {
        try {
            if (u.a(jSONObject, "11B")) {
                aVar.h = jSONObject.getJSONObject("11B");
            }
            if (u.a(jSONObject, "11C")) {
                aVar.k = jSONObject.getJSONObject("11C");
            }
            if (u.a(jSONObject, "11I")) {
                aVar.l = jSONObject.getJSONObject("11I");
            }
            if (u.a(jSONObject, "11H")) {
                aVar.m = jSONObject.getJSONObject("11H");
            }
            if (u.a(jSONObject, "11E")) {
                aVar.n = jSONObject.getJSONObject("11E");
            }
            if (u.a(jSONObject, "11F")) {
                aVar.o = jSONObject.getJSONObject("11F");
            }
            if (u.a(jSONObject, "13A")) {
                aVar.q = jSONObject.getJSONObject("13A");
            }
            if (u.a(jSONObject, "13J")) {
                aVar.i = jSONObject.getJSONObject("13J");
            }
            if (u.a(jSONObject, "11G")) {
                aVar.p = jSONObject.getJSONObject("11G");
            }
            if (u.a(jSONObject, "006")) {
                aVar.r = jSONObject.getJSONObject("006");
            }
            if (u.a(jSONObject, "010")) {
                aVar.s = jSONObject.getJSONObject("010");
            }
            if (u.a(jSONObject, "11Z")) {
                JSONObject jSONObject2 = jSONObject.getJSONObject("11Z");
                a.b bVar = new a.b();
                a(jSONObject2, bVar);
                aVar.C = bVar;
            }
            if (u.a(jSONObject, "135")) {
                aVar.j = jSONObject.getJSONObject("135");
            }
            if (u.a(jSONObject, "13S")) {
                aVar.g = jSONObject.getJSONObject("13S");
            }
            if (u.a(jSONObject, "121")) {
                JSONObject jSONObject3 = jSONObject.getJSONObject("121");
                a.b bVar2 = new a.b();
                a(jSONObject3, bVar2);
                aVar.D = bVar2;
            }
            if (u.a(jSONObject, "122")) {
                JSONObject jSONObject4 = jSONObject.getJSONObject("122");
                a.b bVar3 = new a.b();
                a(jSONObject4, bVar3);
                aVar.E = bVar3;
            }
            if (u.a(jSONObject, "123")) {
                JSONObject jSONObject5 = jSONObject.getJSONObject("123");
                a.b bVar4 = new a.b();
                a(jSONObject5, bVar4);
                aVar.F = bVar4;
            }
            if (u.a(jSONObject, "011")) {
                aVar.c = jSONObject.getJSONObject("011");
            }
            if (u.a(jSONObject, "012")) {
                aVar.d = jSONObject.getJSONObject("012");
            }
            if (u.a(jSONObject, "013")) {
                aVar.e = jSONObject.getJSONObject("013");
            }
            if (u.a(jSONObject, "014")) {
                aVar.f = jSONObject.getJSONObject("014");
            }
            if (u.a(jSONObject, "145")) {
                aVar.t = jSONObject.getJSONObject("145");
            }
            if (u.a(jSONObject, "14B")) {
                aVar.u = jSONObject.getJSONObject("14B");
            }
            if (u.a(jSONObject, "14D")) {
                aVar.v = jSONObject.getJSONObject("14D");
            }
        } catch (Throwable th) {
            ab.b(th, "at", "pe");
        }
    }

    public static void a(String str, boolean z, boolean z2, boolean z3, long j) {
        if (!TextUtils.isEmpty(str) && c != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("url", str);
            hashMap.put("downLevel", String.valueOf(z2));
            hashMap.put("type", z ? Constants.LogTransferLevel.L6 : "4");
            hashMap.put("status", z3 ? "0" : "1");
            hashMap.put(VPMConstants.MEASURE_DURATION, String.valueOf(j));
            String jSONObject = new JSONObject(hashMap).toString();
            if (!TextUtils.isEmpty(jSONObject)) {
                try {
                    bf bfVar = new bf(c, "core", "1.0", "O002");
                    bfVar.a(jSONObject);
                    bg.a(bfVar, c);
                } catch (j e2) {
                }
            }
        }
    }

    private static void a(JSONObject jSONObject, a.b bVar) {
        try {
            String a2 = a(jSONObject, DetailConfig.ONLINE);
            String a3 = a(jSONObject, "u");
            String a4 = a(jSONObject, "v");
            String a5 = a(jSONObject, "able");
            String a6 = a(jSONObject, "on");
            bVar.c = a2;
            bVar.b = a3;
            bVar.d = a4;
            bVar.a = a(a5, false);
            bVar.e = a(a6, true);
        } catch (Throwable th) {
            y.a(th, "at", "pe");
        }
    }

    private static void a(JSONObject jSONObject, a.c cVar) {
        if (jSONObject != null) {
            try {
                String a2 = a(jSONObject, "md5");
                String a3 = a(jSONObject, "url");
                cVar.b = a2;
                cVar.a = a3;
            } catch (Throwable th) {
                y.a(th, "at", "psc");
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x003a  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x010c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a() {
        /*
            r3 = 1
            r2 = 0
            android.content.Context r0 = c
            if (r0 == 0) goto L_0x0115
            android.content.Context r0 = c     // Catch:{ Throwable -> 0x00e7 }
            if (r0 == 0) goto L_0x009e
            android.content.Context r0 = c     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r0 = com.loc.n.w(r0)     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r1 = g     // Catch:{ Throwable -> 0x00e7 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x00e7 }
            if (r1 != 0) goto L_0x003c
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x00e7 }
            if (r1 != 0) goto L_0x003c
            java.lang.String r1 = g     // Catch:{ Throwable -> 0x00e7 }
            boolean r1 = r1.equals(r0)     // Catch:{ Throwable -> 0x00e7 }
            if (r1 == 0) goto L_0x003c
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00e7 }
            long r6 = h     // Catch:{ Throwable -> 0x00e7 }
            long r4 = r4 - r6
            r6 = 60000(0xea60, double:2.9644E-319)
            int r1 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r1 >= 0) goto L_0x003c
        L_0x0034:
            boolean r0 = c()
            if (r0 != 0) goto L_0x010c
            r0 = r2
        L_0x003b:
            return r0
        L_0x003c:
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x00e7 }
            if (r1 != 0) goto L_0x0044
            g = r0     // Catch:{ Throwable -> 0x00e7 }
        L_0x0044:
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00e7 }
            h = r0     // Catch:{ Throwable -> 0x00e7 }
            java.util.Map<java.lang.String, java.lang.Integer> r0 = f     // Catch:{ Throwable -> 0x00e7 }
            r0.clear()     // Catch:{ Throwable -> 0x00e7 }
            java.util.Enumeration r0 = java.net.NetworkInterface.getNetworkInterfaces()     // Catch:{ Throwable -> 0x00e7 }
            java.util.ArrayList r0 = java.util.Collections.list(r0)     // Catch:{ Throwable -> 0x00e7 }
            java.util.Iterator r4 = r0.iterator()     // Catch:{ Throwable -> 0x00e7 }
        L_0x005b:
            boolean r0 = r4.hasNext()     // Catch:{ Throwable -> 0x00e7 }
            if (r0 == 0) goto L_0x0034
            java.lang.Object r0 = r4.next()     // Catch:{ Throwable -> 0x00e7 }
            java.net.NetworkInterface r0 = (java.net.NetworkInterface) r0     // Catch:{ Throwable -> 0x00e7 }
            java.util.List r1 = r0.getInterfaceAddresses()     // Catch:{ Throwable -> 0x00e7 }
            boolean r1 = r1.isEmpty()     // Catch:{ Throwable -> 0x00e7 }
            if (r1 != 0) goto L_0x005b
            java.lang.String r5 = r0.getDisplayName()     // Catch:{ Throwable -> 0x00e7 }
            java.util.List r0 = r0.getInterfaceAddresses()     // Catch:{ Throwable -> 0x00e7 }
            java.util.Iterator r6 = r0.iterator()     // Catch:{ Throwable -> 0x00e7 }
            r1 = r2
        L_0x007e:
            boolean r0 = r6.hasNext()     // Catch:{ Throwable -> 0x00e7 }
            if (r0 == 0) goto L_0x00cc
            java.lang.Object r0 = r6.next()     // Catch:{ Throwable -> 0x00e7 }
            java.net.InterfaceAddress r0 = (java.net.InterfaceAddress) r0     // Catch:{ Throwable -> 0x00e7 }
            java.net.InetAddress r0 = r0.getAddress()     // Catch:{ Throwable -> 0x00e7 }
            boolean r7 = r0 instanceof java.net.Inet6Address     // Catch:{ Throwable -> 0x00e7 }
            if (r7 == 0) goto L_0x00ac
            java.net.Inet6Address r0 = (java.net.Inet6Address) r0     // Catch:{ Throwable -> 0x00e7 }
            boolean r0 = a((java.net.InetAddress) r0)     // Catch:{ Throwable -> 0x00e7 }
            if (r0 != 0) goto L_0x0137
            r0 = r1 | 2
        L_0x009c:
            r1 = r0
            goto L_0x007e
        L_0x009e:
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00e7 }
            long r4 = h     // Catch:{ Throwable -> 0x00e7 }
            long r0 = r0 - r4
            r4 = 10000(0x2710, double:4.9407E-320)
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 >= 0) goto L_0x0044
            goto L_0x0034
        L_0x00ac:
            boolean r7 = r0 instanceof java.net.Inet4Address     // Catch:{ Throwable -> 0x00e7 }
            if (r7 == 0) goto L_0x007e
            java.net.Inet4Address r0 = (java.net.Inet4Address) r0     // Catch:{ Throwable -> 0x00e7 }
            boolean r7 = a((java.net.InetAddress) r0)     // Catch:{ Throwable -> 0x00e7 }
            if (r7 != 0) goto L_0x007e
            java.lang.String r0 = r0.getHostAddress()     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r7 = "FMTkyLjE2OC40My4"
            java.lang.String r7 = com.loc.u.c((java.lang.String) r7)     // Catch:{ Throwable -> 0x00e7 }
            boolean r0 = r0.startsWith(r7)     // Catch:{ Throwable -> 0x00e7 }
            if (r0 != 0) goto L_0x007e
            r1 = r1 | 1
            goto L_0x007e
        L_0x00cc:
            if (r1 == 0) goto L_0x005b
            if (r5 == 0) goto L_0x00f3
            java.lang.String r0 = "wlan"
            boolean r0 = r5.startsWith(r0)     // Catch:{ Throwable -> 0x00e7 }
            if (r0 == 0) goto L_0x00f3
            java.util.Map<java.lang.String, java.lang.Integer> r0 = f     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r5 = "WIFI"
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ Throwable -> 0x00e7 }
            r0.put(r5, r1)     // Catch:{ Throwable -> 0x00e7 }
            goto L_0x005b
        L_0x00e7:
            r0 = move-exception
            java.lang.String r1 = "at"
            java.lang.String r4 = "ipstack"
            com.loc.y.a((java.lang.Throwable) r0, (java.lang.String) r1, (java.lang.String) r4)
            goto L_0x0034
        L_0x00f3:
            if (r5 == 0) goto L_0x005b
            java.lang.String r0 = "rmnet"
            boolean r0 = r5.startsWith(r0)     // Catch:{ Throwable -> 0x00e7 }
            if (r0 == 0) goto L_0x005b
            java.util.Map<java.lang.String, java.lang.Integer> r0 = f     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r5 = "MOBILE"
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ Throwable -> 0x00e7 }
            r0.put(r5, r1)     // Catch:{ Throwable -> 0x00e7 }
            goto L_0x005b
        L_0x010c:
            boolean r0 = b()
            if (r0 == 0) goto L_0x0115
            r0 = r3
            goto L_0x003b
        L_0x0115:
            boolean r0 = d
            if (r0 != 0) goto L_0x011c
            r0 = r2
            goto L_0x003b
        L_0x011c:
            android.content.Context r0 = c
            java.lang.String r1 = "IPV6_CONFIG_NAME"
            com.loc.l$c r0 = b(r0, r1)
            if (r0 != 0) goto L_0x012a
            r0 = r2
            goto L_0x003b
        L_0x012a:
            int r0 = r0.a()
            r1 = 5
            if (r0 >= r1) goto L_0x0134
            r0 = r3
            goto L_0x003b
        L_0x0134:
            r0 = r2
            goto L_0x003b
        L_0x0137:
            r0 = r1
            goto L_0x009c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.l.a():boolean");
    }

    public static boolean a(String str, boolean z) {
        try {
            if (TextUtils.isEmpty(str)) {
                return z;
            }
            String[] split = URLDecoder.decode(str).split(WVNativeCallbackUtil.SEPERATER);
            return split[split.length + -1].charAt(4) % 2 == 1;
        } catch (Throwable th) {
            return z;
        }
    }

    private static boolean a(InetAddress inetAddress) {
        return inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress() || inetAddress.isAnyLocalAddress();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00be, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00bf, code lost:
        r1 = null;
        r3 = null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0098  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00be A[ExcHandler: IllegalBlockSizeException (e javax.crypto.IllegalBlockSizeException), PHI: r12 
      PHI: (r12v4 java.lang.String) = (r12v0 java.lang.String), (r12v0 java.lang.String), (r12v0 java.lang.String), (r12v8 java.lang.String), (r12v9 java.lang.String), (r12v9 java.lang.String), (r12v10 java.lang.String) binds: [B:5:0x002e, B:6:?, B:7:0x0033, B:25:0x00b5, B:19:0x009b, B:20:?, B:8:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:5:0x002e] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00d7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.loc.l.a b(android.content.Context r10, com.loc.t r11, java.lang.String r12) {
        /*
            r9 = 1
            r2 = 0
            com.loc.l$a r6 = new com.loc.l$a
            r6.<init>()
            org.json.JSONObject r0 = new org.json.JSONObject
            r0.<init>()
            r6.w = r0
            com.loc.p r0 = com.loc.p.a.a
            r0.a((android.content.Context) r10)
            if (r10 == 0) goto L_0x0026
            java.lang.String r0 = "k"
            com.loc.al r1 = new com.loc.al
            java.lang.String r3 = "IPV6_CONFIG_NAME"
            r1.<init>(r3)
            boolean r0 = r1.a(r10, r0)
            d = r0
        L_0x0026:
            if (r10 == 0) goto L_0x002e
            android.content.Context r0 = r10.getApplicationContext()
            c = r0
        L_0x002e:
            com.loc.aw r0 = new com.loc.aw     // Catch:{ j -> 0x009c, IllegalBlockSizeException -> 0x00be, Throwable -> 0x00c6 }
            r0.<init>()     // Catch:{ j -> 0x009c, IllegalBlockSizeException -> 0x00be, Throwable -> 0x00c6 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ j -> 0x009a, Throwable -> 0x00b4, IllegalBlockSizeException -> 0x00be }
            r0.<init>()     // Catch:{ j -> 0x009a, Throwable -> 0x00b4, IllegalBlockSizeException -> 0x00be }
            java.lang.StringBuilder r0 = r0.append(r12)     // Catch:{ j -> 0x009a, Throwable -> 0x00b4, IllegalBlockSizeException -> 0x00be }
            java.lang.String r1 = ";15K;16H;17I;17S"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ j -> 0x009a, Throwable -> 0x00b4, IllegalBlockSizeException -> 0x00be }
            java.lang.String r12 = r0.toString()     // Catch:{ j -> 0x009a, Throwable -> 0x00b4, IllegalBlockSizeException -> 0x00be }
            com.loc.l$b r0 = new com.loc.l$b     // Catch:{ j -> 0x009a, Throwable -> 0x00b4, IllegalBlockSizeException -> 0x00be }
            r0.<init>(r10, r11, r12)     // Catch:{ j -> 0x009a, Throwable -> 0x00b4, IllegalBlockSizeException -> 0x00be }
            boolean r1 = r0.a()     // Catch:{ j -> 0x009a, Throwable -> 0x00b4, IllegalBlockSizeException -> 0x00be }
            com.loc.ba r1 = com.loc.aw.a(r0, r1)     // Catch:{ j -> 0x009a, Throwable -> 0x00b4, IllegalBlockSizeException -> 0x00be }
            if (r1 == 0) goto L_0x0202
            byte[] r3 = r1.a     // Catch:{ j -> 0x01fb, IllegalBlockSizeException -> 0x01f4, Throwable -> 0x01ed }
        L_0x0058:
            r0 = 16
            byte[] r0 = new byte[r0]     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            int r4 = r3.length     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            int r4 = r4 + -16
            byte[] r4 = new byte[r4]     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            r5 = 0
            r7 = 0
            r8 = 16
            java.lang.System.arraycopy(r3, r5, r0, r7, r8)     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            r5 = 16
            r7 = 0
            int r8 = r3.length     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            int r8 = r8 + -16
            java.lang.System.arraycopy(r3, r5, r4, r7, r8)     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            javax.crypto.spec.SecretKeySpec r5 = new javax.crypto.spec.SecretKeySpec     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            java.lang.String r7 = "AES"
            r5.<init>(r0, r7)     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            java.lang.String r0 = "AES/CBC/PKCS5Padding"
            javax.crypto.Cipher r0 = javax.crypto.Cipher.getInstance(r0)     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            javax.crypto.spec.IvParameterSpec r7 = new javax.crypto.spec.IvParameterSpec     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            byte[] r8 = com.loc.u.c()     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            r7.<init>(r8)     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            r8 = 2
            r0.init(r8, r5, r7)     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            byte[] r0 = r0.doFinal(r4)     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            java.lang.String r2 = com.loc.u.a((byte[]) r0)     // Catch:{ j -> 0x01ff, IllegalBlockSizeException -> 0x01f8, Throwable -> 0x01f1 }
            r0 = r2
        L_0x0096:
            if (r3 != 0) goto L_0x00d7
            r0 = r6
        L_0x0099:
            return r0
        L_0x009a:
            r0 = move-exception
            throw r0     // Catch:{ j -> 0x009c, IllegalBlockSizeException -> 0x00be, Throwable -> 0x00c6 }
        L_0x009c:
            r0 = move-exception
            r1 = r2
            r3 = r2
        L_0x009f:
            java.lang.String r4 = r0.a()
            r6.a = r4
            java.lang.String r4 = r0.a()
            c(r10, r11, r4)
            java.lang.String r4 = "/v3/iasdkauth"
            com.loc.ab.a((com.loc.t) r11, (java.lang.String) r4, (com.loc.j) r0)
            r0 = r2
            goto L_0x0096
        L_0x00b4:
            r0 = move-exception
            com.loc.j r0 = new com.loc.j     // Catch:{ j -> 0x009c, IllegalBlockSizeException -> 0x00be, Throwable -> 0x00c6 }
            java.lang.String r1 = "未知的错误"
            r0.<init>(r1)     // Catch:{ j -> 0x009c, IllegalBlockSizeException -> 0x00be, Throwable -> 0x00c6 }
            throw r0     // Catch:{ j -> 0x009c, IllegalBlockSizeException -> 0x00be, Throwable -> 0x00c6 }
        L_0x00be:
            r0 = move-exception
            r1 = r2
            r3 = r2
        L_0x00c1:
            a((android.content.Context) r10, (com.loc.t) r11, (java.lang.Throwable) r0)
            r0 = r2
            goto L_0x0096
        L_0x00c6:
            r0 = move-exception
            r1 = r2
            r3 = r2
        L_0x00c9:
            java.lang.String r4 = "at"
            java.lang.String r5 = "lc"
            com.loc.ab.b((java.lang.Throwable) r0, (java.lang.String) r4, (java.lang.String) r5)
            a((android.content.Context) r10, (com.loc.t) r11, (java.lang.Throwable) r0)
            r0 = r2
            goto L_0x0096
        L_0x00d7:
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 == 0) goto L_0x00e1
            java.lang.String r0 = com.loc.u.a((byte[]) r3)
        L_0x00e1:
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 == 0) goto L_0x00ed
            java.lang.String r2 = "result is null"
            c(r10, r11, r2)
        L_0x00ed:
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ Throwable -> 0x01da }
            r7.<init>(r0)     // Catch:{ Throwable -> 0x01da }
            java.lang.String r0 = "status"
            boolean r0 = r7.has(r0)     // Catch:{ Throwable -> 0x01da }
            if (r0 == 0) goto L_0x017e
            java.lang.String r0 = "status"
            int r0 = r7.getInt(r0)     // Catch:{ Throwable -> 0x01da }
            if (r0 != r9) goto L_0x0181
            r0 = 1
            a = r0     // Catch:{ Throwable -> 0x01da }
        L_0x0107:
            java.lang.String r0 = "ver"
            boolean r0 = r7.has(r0)     // Catch:{ Throwable -> 0x01ce }
            if (r0 == 0) goto L_0x0119
            java.lang.String r0 = "ver"
            int r0 = r7.getInt(r0)     // Catch:{ Throwable -> 0x01ce }
            r6.b = r0     // Catch:{ Throwable -> 0x01ce }
        L_0x0119:
            java.lang.String r0 = "result"
            boolean r0 = com.loc.u.a((org.json.JSONObject) r7, (java.lang.String) r0)     // Catch:{ Throwable -> 0x01da }
            if (r0 == 0) goto L_0x017e
            java.lang.String r0 = "result"
            org.json.JSONObject r0 = r7.getJSONObject(r0)     // Catch:{ Throwable -> 0x01da }
            a(r10, r12, r6, r0)     // Catch:{ Throwable -> 0x01da }
            java.lang.String r1 = "17I"
            boolean r1 = com.loc.u.a((org.json.JSONObject) r0, (java.lang.String) r1)     // Catch:{ Throwable -> 0x01da }
            if (r1 == 0) goto L_0x0158
            java.lang.String r1 = "17I"
            org.json.JSONObject r1 = r0.getJSONObject(r1)     // Catch:{ Throwable -> 0x01da }
            java.lang.String r2 = "na"
            java.lang.String r2 = r1.optString(r2)     // Catch:{ Throwable -> 0x01da }
            r3 = 0
            boolean r2 = a((java.lang.String) r2, (boolean) r3)     // Catch:{ Throwable -> 0x01da }
            java.lang.String r3 = "aa"
            java.lang.String r1 = r1.optString(r3)     // Catch:{ Throwable -> 0x01da }
            r3 = 0
            boolean r1 = a((java.lang.String) r1, (boolean) r3)     // Catch:{ Throwable -> 0x01da }
            com.loc.w.e = r2     // Catch:{ Throwable -> 0x01da }
            com.loc.w.f = r1     // Catch:{ Throwable -> 0x01da }
        L_0x0158:
            java.lang.String r1 = "15K"
            org.json.JSONObject r0 = r0.getJSONObject(r1)     // Catch:{ Throwable -> 0x01eb }
            java.lang.String r1 = "isTargetAble"
            java.lang.String r1 = r0.optString(r1)     // Catch:{ Throwable -> 0x01eb }
            r2 = 0
            boolean r1 = a((java.lang.String) r1, (boolean) r2)     // Catch:{ Throwable -> 0x01eb }
            java.lang.String r2 = "able"
            java.lang.String r0 = r0.optString(r2)     // Catch:{ Throwable -> 0x01eb }
            r2 = 0
            boolean r0 = a((java.lang.String) r0, (boolean) r2)     // Catch:{ Throwable -> 0x01eb }
            if (r0 != 0) goto L_0x01e5
            com.loc.p r0 = com.loc.p.a.a     // Catch:{ Throwable -> 0x01eb }
            r0.b((android.content.Context) r10)     // Catch:{ Throwable -> 0x01eb }
        L_0x017e:
            r0 = r6
            goto L_0x0099
        L_0x0181:
            if (r0 != 0) goto L_0x0107
            java.lang.String r4 = "authcsid"
            java.lang.String r3 = "authgsid"
            if (r1 == 0) goto L_0x018f
            java.lang.String r4 = r1.c     // Catch:{ Throwable -> 0x01da }
            java.lang.String r3 = r1.d     // Catch:{ Throwable -> 0x01da }
        L_0x018f:
            com.loc.u.a(r10, r4, r3, r7)     // Catch:{ Throwable -> 0x01da }
            r0 = 0
            a = r0     // Catch:{ Throwable -> 0x01da }
            java.lang.String r0 = "info"
            boolean r0 = r7.has(r0)     // Catch:{ Throwable -> 0x01da }
            if (r0 == 0) goto L_0x01a7
            java.lang.String r0 = "info"
            java.lang.String r0 = r7.getString(r0)     // Catch:{ Throwable -> 0x01da }
            b = r0     // Catch:{ Throwable -> 0x01da }
        L_0x01a7:
            java.lang.String r5 = ""
            java.lang.String r0 = "infocode"
            boolean r0 = r7.has(r0)     // Catch:{ Throwable -> 0x01da }
            if (r0 == 0) goto L_0x01ba
            java.lang.String r0 = "infocode"
            java.lang.String r5 = r7.getString(r0)     // Catch:{ Throwable -> 0x01da }
        L_0x01ba:
            java.lang.String r1 = "/v3/iasdkauth"
            java.lang.String r2 = b     // Catch:{ Throwable -> 0x01da }
            r0 = r11
            com.loc.ab.a(r0, r1, r2, r3, r4, r5)     // Catch:{ Throwable -> 0x01da }
            int r0 = a     // Catch:{ Throwable -> 0x01da }
            if (r0 != 0) goto L_0x0107
            java.lang.String r0 = b     // Catch:{ Throwable -> 0x01da }
            r6.a = r0     // Catch:{ Throwable -> 0x01da }
            r0 = r6
            goto L_0x0099
        L_0x01ce:
            r0 = move-exception
            java.lang.String r1 = "at"
            java.lang.String r2 = "lc"
            com.loc.y.a((java.lang.Throwable) r0, (java.lang.String) r1, (java.lang.String) r2)     // Catch:{ Throwable -> 0x01da }
            goto L_0x0119
        L_0x01da:
            r0 = move-exception
            java.lang.String r1 = "at"
            java.lang.String r2 = "lc"
            com.loc.y.a((java.lang.Throwable) r0, (java.lang.String) r1, (java.lang.String) r2)
            goto L_0x017e
        L_0x01e5:
            com.loc.p r0 = com.loc.p.a.a     // Catch:{ Throwable -> 0x01eb }
            r0.a(r10, r1)     // Catch:{ Throwable -> 0x01eb }
            goto L_0x017e
        L_0x01eb:
            r0 = move-exception
            goto L_0x017e
        L_0x01ed:
            r0 = move-exception
            r3 = r2
            goto L_0x00c9
        L_0x01f1:
            r0 = move-exception
            goto L_0x00c9
        L_0x01f4:
            r0 = move-exception
            r3 = r2
            goto L_0x00c1
        L_0x01f8:
            r0 = move-exception
            goto L_0x00c1
        L_0x01fb:
            r0 = move-exception
            r3 = r2
            goto L_0x009f
        L_0x01ff:
            r0 = move-exception
            goto L_0x009f
        L_0x0202:
            r3 = r2
            goto L_0x0058
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.l.b(android.content.Context, com.loc.t, java.lang.String):com.loc.l$a");
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0032  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized com.loc.l.c b(android.content.Context r6, java.lang.String r7) {
        /*
            r1 = 0
            r0 = 0
            java.lang.Class<com.loc.l> r3 = com.loc.l.class
            monitor-enter(r3)
            boolean r2 = android.text.TextUtils.isEmpty(r7)     // Catch:{ all -> 0x0077 }
            if (r2 != 0) goto L_0x0030
            r2 = r0
        L_0x000c:
            java.util.Vector<com.loc.l$c> r0 = e     // Catch:{ all -> 0x0077 }
            int r0 = r0.size()     // Catch:{ all -> 0x0077 }
            if (r2 >= r0) goto L_0x0030
            java.util.Vector<com.loc.l$c> r0 = e     // Catch:{ all -> 0x0077 }
            java.lang.Object r0 = r0.get(r2)     // Catch:{ all -> 0x0077 }
            com.loc.l$c r0 = (com.loc.l.c) r0     // Catch:{ all -> 0x0077 }
            if (r0 == 0) goto L_0x002c
            java.lang.String r4 = r0.a     // Catch:{ all -> 0x0077 }
            boolean r4 = r7.equals(r4)     // Catch:{ all -> 0x0077 }
            if (r4 == 0) goto L_0x002c
        L_0x0028:
            if (r0 == 0) goto L_0x0032
        L_0x002a:
            monitor-exit(r3)
            return r0
        L_0x002c:
            int r0 = r2 + 1
            r2 = r0
            goto L_0x000c
        L_0x0030:
            r0 = r1
            goto L_0x0028
        L_0x0032:
            if (r6 != 0) goto L_0x0036
            r0 = r1
            goto L_0x002a
        L_0x0036:
            com.loc.al r0 = new com.loc.al     // Catch:{ all -> 0x0077 }
            r0.<init>(r7)     // Catch:{ all -> 0x0077 }
            java.lang.String r1 = "i"
            java.lang.String r0 = r0.b(r6, r1)     // Catch:{ all -> 0x0077 }
            com.loc.l$c r0 = com.loc.l.c.b((java.lang.String) r0)     // Catch:{ all -> 0x0077 }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0077 }
            java.lang.String r1 = "yyyyMMdd"
            java.lang.String r1 = com.loc.u.a((long) r4, (java.lang.String) r1)     // Catch:{ all -> 0x0077 }
            if (r0 != 0) goto L_0x005c
            com.loc.l$c r0 = new com.loc.l$c     // Catch:{ all -> 0x0077 }
            java.lang.String r2 = "IPV6_CONFIG_NAME"
            r4 = 0
            r0.<init>(r2, r1, r4)     // Catch:{ all -> 0x0077 }
        L_0x005c:
            java.lang.String r2 = r0.b     // Catch:{ all -> 0x0077 }
            boolean r2 = r1.equals(r2)     // Catch:{ all -> 0x0077 }
            if (r2 != 0) goto L_0x0071
            r0.a((java.lang.String) r1)     // Catch:{ all -> 0x0077 }
            java.util.concurrent.atomic.AtomicInteger r1 = r0.c     // Catch:{ all -> 0x0077 }
            r2 = 0
            r1.set(r2)     // Catch:{ all -> 0x0077 }
        L_0x0071:
            java.util.Vector<com.loc.l$c> r1 = e     // Catch:{ all -> 0x0077 }
            r1.add(r0)     // Catch:{ all -> 0x0077 }
            goto L_0x002a
        L_0x0077:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.l.b(android.content.Context, java.lang.String):com.loc.l$c");
    }

    public static boolean b() {
        if (c == null) {
            return false;
        }
        String w = n.w(c);
        if (TextUtils.isEmpty(w)) {
            return false;
        }
        Integer num = f.get(w.toUpperCase());
        if (num == null) {
            return false;
        }
        return num.intValue() == 2;
    }

    private static void c(Context context, t tVar, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("amap_sdk_auth_fail", "1");
        hashMap.put("amap_sdk_auth_fail_type", str);
        hashMap.put("amap_sdk_name", tVar.a());
        hashMap.put("amap_sdk_version", tVar.c());
        String jSONObject = new JSONObject(hashMap).toString();
        if (!TextUtils.isEmpty(jSONObject)) {
            try {
                bf bfVar = new bf(context, "core", "1.0", "O001");
                bfVar.a(jSONObject);
                bg.a(bfVar, context);
            } catch (j e2) {
            }
        }
    }

    public static boolean c() {
        if (c == null) {
            return false;
        }
        String w = n.w(c);
        if (TextUtils.isEmpty(w)) {
            return false;
        }
        Integer num = f.get(w.toUpperCase());
        if (num == null) {
            return false;
        }
        return num.intValue() >= 2;
    }
}
