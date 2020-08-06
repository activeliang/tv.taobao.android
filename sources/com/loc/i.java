package com.loc;

import android.content.Context;
import android.text.TextUtils;
import com.amap.api.location.AMapLocation;

/* compiled from: LastLocationManager */
public final class i {
    static ee b = null;
    static af e = null;
    static long g = 0;
    String a = null;
    ee c = null;
    ee d = null;
    long f = 0;
    boolean h = false;
    private Context i;

    public i(Context context) {
        this.i = context.getApplicationContext();
    }

    private void e() {
        if (b == null || et.b() - g > 180000) {
            ee f2 = f();
            g = et.b();
            if (f2 != null && et.a(f2.a())) {
                b = f2;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0036, code lost:
        r3 = com.loc.ec.c(r2, r6.a);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.loc.ee f() {
        /*
            r6 = this;
            r1 = 0
            android.content.Context r0 = r6.i
            if (r0 != 0) goto L_0x0007
            r0 = r1
        L_0x0006:
            return r0
        L_0x0007:
            r6.a()
            com.loc.af r0 = e     // Catch:{ Throwable -> 0x009a }
            if (r0 != 0) goto L_0x0010
            r0 = r1
            goto L_0x0006
        L_0x0010:
            com.loc.af r0 = e     // Catch:{ Throwable -> 0x009a }
            java.lang.String r2 = "_id=1"
            java.lang.Class<com.loc.ee> r3 = com.loc.ee.class
            r4 = 0
            java.util.List r0 = r0.a((java.lang.String) r2, r3, (boolean) r4)     // Catch:{ Throwable -> 0x009a }
            int r2 = r0.size()     // Catch:{ Throwable -> 0x009a }
            if (r2 <= 0) goto L_0x00a1
            r2 = 0
            java.lang.Object r0 = r0.get(r2)     // Catch:{ Throwable -> 0x009a }
            com.loc.ee r0 = (com.loc.ee) r0     // Catch:{ Throwable -> 0x009a }
            java.lang.String r2 = r0.c()     // Catch:{ Throwable -> 0x008e }
            byte[] r2 = com.loc.o.b((java.lang.String) r2)     // Catch:{ Throwable -> 0x008e }
            if (r2 == 0) goto L_0x009f
            int r3 = r2.length     // Catch:{ Throwable -> 0x008e }
            if (r3 <= 0) goto L_0x009f
            java.lang.String r3 = r6.a     // Catch:{ Throwable -> 0x008e }
            byte[] r3 = com.loc.ec.c(r2, r3)     // Catch:{ Throwable -> 0x008e }
            if (r3 == 0) goto L_0x009f
            int r2 = r3.length     // Catch:{ Throwable -> 0x008e }
            if (r2 <= 0) goto L_0x009f
            java.lang.String r2 = new java.lang.String     // Catch:{ Throwable -> 0x008e }
            java.lang.String r4 = "UTF-8"
            r2.<init>(r3, r4)     // Catch:{ Throwable -> 0x008e }
        L_0x0049:
            java.lang.String r3 = r0.b()     // Catch:{ Throwable -> 0x008e }
            byte[] r3 = com.loc.o.b((java.lang.String) r3)     // Catch:{ Throwable -> 0x008e }
            if (r3 == 0) goto L_0x0069
            int r4 = r3.length     // Catch:{ Throwable -> 0x008e }
            if (r4 <= 0) goto L_0x0069
            java.lang.String r4 = r6.a     // Catch:{ Throwable -> 0x008e }
            byte[] r3 = com.loc.ec.c(r3, r4)     // Catch:{ Throwable -> 0x008e }
            if (r3 == 0) goto L_0x0069
            int r4 = r3.length     // Catch:{ Throwable -> 0x008e }
            if (r4 <= 0) goto L_0x0069
            java.lang.String r1 = new java.lang.String     // Catch:{ Throwable -> 0x008e }
            java.lang.String r4 = "UTF-8"
            r1.<init>(r3, r4)     // Catch:{ Throwable -> 0x008e }
        L_0x0069:
            r0.a((java.lang.String) r1)     // Catch:{ Throwable -> 0x008e }
            r1 = r2
        L_0x006d:
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x008e }
            if (r2 != 0) goto L_0x0006
            com.amap.api.location.AMapLocation r2 = new com.amap.api.location.AMapLocation     // Catch:{ Throwable -> 0x008e }
            java.lang.String r3 = ""
            r2.<init>((java.lang.String) r3)     // Catch:{ Throwable -> 0x008e }
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Throwable -> 0x008e }
            r3.<init>(r1)     // Catch:{ Throwable -> 0x008e }
            com.loc.en.a((com.amap.api.location.AMapLocation) r2, (org.json.JSONObject) r3)     // Catch:{ Throwable -> 0x008e }
            boolean r1 = com.loc.et.b((com.amap.api.location.AMapLocation) r2)     // Catch:{ Throwable -> 0x008e }
            if (r1 == 0) goto L_0x0006
            r0.a((com.amap.api.location.AMapLocation) r2)     // Catch:{ Throwable -> 0x008e }
            goto L_0x0006
        L_0x008e:
            r1 = move-exception
        L_0x008f:
            java.lang.String r2 = "LastLocationManager"
            java.lang.String r3 = "readLastFix"
            com.loc.en.a(r1, r2, r3)
            goto L_0x0006
        L_0x009a:
            r0 = move-exception
            r5 = r0
            r0 = r1
            r1 = r5
            goto L_0x008f
        L_0x009f:
            r2 = r1
            goto L_0x0049
        L_0x00a1:
            r0 = r1
            goto L_0x006d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.i.f():com.loc.ee");
    }

    public final AMapLocation a(AMapLocation aMapLocation, String str, long j) {
        boolean z = true;
        if (aMapLocation == null || aMapLocation.getErrorCode() == 0 || aMapLocation.getLocationType() == 1 || aMapLocation.getErrorCode() == 7) {
            return aMapLocation;
        }
        try {
            e();
            if (b == null || b.a() == null) {
                return aMapLocation;
            }
            if (TextUtils.isEmpty(str)) {
                long b2 = et.b() - b.d();
                if (b2 < 0 || b2 > j) {
                    z = false;
                }
                aMapLocation.setTrustedLevel(3);
            } else {
                if (!et.a(b.b(), str)) {
                    z = false;
                }
                aMapLocation.setTrustedLevel(2);
            }
            if (!z) {
                return aMapLocation;
            }
            AMapLocation a2 = b.a();
            try {
                a2.setLocationType(9);
                a2.setFixLastLocation(true);
                a2.setLocationDetail(aMapLocation.getLocationDetail());
                return a2;
            } catch (Throwable th) {
                aMapLocation = a2;
                th = th;
                en.a(th, "LastLocationManager", "fixLastLocation");
                return aMapLocation;
            }
        } catch (Throwable th2) {
            th = th2;
            en.a(th, "LastLocationManager", "fixLastLocation");
            return aMapLocation;
        }
    }

    public final void a() {
        if (!this.h) {
            try {
                if (this.a == null) {
                    this.a = ec.a("MD5", n.x(this.i));
                }
                if (e == null) {
                    e = new af(this.i, af.a((Class<? extends ae>) ef.class));
                }
            } catch (Throwable th) {
                en.a(th, "LastLocationManager", "<init>:DBOperation");
            }
            this.h = true;
        }
    }

    public final boolean a(AMapLocation aMapLocation, String str) {
        if (this.i == null || aMapLocation == null || !et.a(aMapLocation) || aMapLocation.getLocationType() == 2 || aMapLocation.isMock() || aMapLocation.isFixLastLocation()) {
            return false;
        }
        ee eeVar = new ee();
        eeVar.a(aMapLocation);
        if (aMapLocation.getLocationType() == 1) {
            eeVar.a((String) null);
        } else {
            eeVar.a(str);
        }
        try {
            b = eeVar;
            g = et.b();
            this.c = eeVar;
            return (this.d == null || et.a(this.d.a(), eeVar.a()) > 500.0f) && et.b() - this.f > 30000;
        } catch (Throwable th) {
            en.a(th, "LastLocationManager", "setLastFix");
            return false;
        }
    }

    public final AMapLocation b() {
        e();
        if (b != null && et.a(b.a())) {
            return b.a();
        }
        return null;
    }

    public final void c() {
        try {
            d();
            this.f = 0;
            this.h = false;
            this.c = null;
            this.d = null;
        } catch (Throwable th) {
            en.a(th, "LastLocationManager", "destroy");
        }
    }

    public final void d() {
        String str;
        String str2 = null;
        try {
            a();
            if (this.c != null && et.a(this.c.a()) && e != null && this.c != this.d && this.c.d() == 0) {
                String str3 = this.c.a().toStr();
                String b2 = this.c.b();
                this.d = this.c;
                if (!TextUtils.isEmpty(str3)) {
                    str = o.b(ec.b(str3.getBytes("UTF-8"), this.a));
                    if (!TextUtils.isEmpty(b2)) {
                        str2 = o.b(ec.b(b2.getBytes("UTF-8"), this.a));
                    }
                } else {
                    str = null;
                }
                if (!TextUtils.isEmpty(str)) {
                    ee eeVar = new ee();
                    eeVar.b(str);
                    eeVar.a(et.b());
                    eeVar.a(str2);
                    e.a((Object) eeVar, "_id=1");
                    this.f = et.b();
                    if (b != null) {
                        b.a(et.b());
                    }
                }
            }
        } catch (Throwable th) {
            en.a(th, "LastLocationManager", "saveLastFix");
        }
    }
}
