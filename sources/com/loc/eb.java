package com.loc;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import com.alibaba.appmonitor.offline.TempEvent;
import com.bftv.fui.constantplugin.Constant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

/* compiled from: WifiManagerWrapper */
public final class eb {
    static long d = 0;
    static long e = 0;
    static long f = 0;
    public static long g = 0;
    static long h = 0;
    public static HashMap<String, Long> s = new HashMap<>(36);
    public static long t = 0;
    static int u = 0;
    public static long w = 0;
    private ds A;
    WifiManager a;
    ArrayList<ScanResult> b = new ArrayList<>();
    ArrayList<dh> c = new ArrayList<>();
    Context i;
    boolean j = false;
    StringBuilder k = null;
    boolean l = true;
    boolean m = true;
    boolean n = true;
    String o = null;
    TreeMap<Integer, ScanResult> p = null;
    public boolean q = true;
    public boolean r = false;
    ConnectivityManager v = null;
    volatile boolean x = false;
    private volatile WifiInfo y = null;
    private long z = 30000;

    public eb(Context context, WifiManager wifiManager) {
        this.a = wifiManager;
        this.i = context;
    }

    private static boolean a(int i2) {
        int i3 = 20;
        try {
            i3 = WifiManager.calculateSignalLevel(i2, 20);
        } catch (ArithmeticException e2) {
            en.a(e2, "Aps", "wifiSigFine");
        }
        return i3 > 0;
    }

    public static boolean a(WifiInfo wifiInfo) {
        return wifiInfo != null && !TextUtils.isEmpty(wifiInfo.getSSID()) && et.a(wifiInfo.getBSSID());
    }

    public static long b() {
        return ((et.b() - t) / 1000) + 1;
    }

    private void c(boolean z2) {
        if (this.b != null && !this.b.isEmpty()) {
            if (et.b() - g > 3600000) {
                g();
            }
            if (this.p == null) {
                this.p = new TreeMap<>(Collections.reverseOrder());
            }
            this.p.clear();
            if (this.r && z2) {
                try {
                    this.c.clear();
                } catch (Throwable th) {
                }
            }
            int size = this.b.size();
            for (int i2 = 0; i2 < size; i2++) {
                ScanResult scanResult = this.b.get(i2);
                if (et.a(scanResult != null ? scanResult.BSSID : "") && (size <= 20 || a(scanResult.level))) {
                    if (this.r && z2) {
                        try {
                            dh dhVar = new dh(false);
                            dhVar.b = scanResult.SSID;
                            dhVar.d = scanResult.frequency;
                            dhVar.e = scanResult.timestamp;
                            dhVar.a = dh.a(scanResult.BSSID);
                            dhVar.c = (short) scanResult.level;
                            if (Build.VERSION.SDK_INT >= 17) {
                                dhVar.g = (short) ((int) ((SystemClock.elapsedRealtime() - (scanResult.timestamp / 1000)) / 1000));
                                if (dhVar.g < 0) {
                                    dhVar.g = 0;
                                }
                            }
                            dhVar.f = System.currentTimeMillis();
                            this.c.add(dhVar);
                        } catch (Throwable th2) {
                        }
                    }
                    if (TextUtils.isEmpty(scanResult.SSID)) {
                        scanResult.SSID = "unkwn";
                    } else if (!"<unknown ssid>".equals(scanResult.SSID)) {
                        scanResult.SSID = String.valueOf(i2);
                    }
                    this.p.put(Integer.valueOf((scanResult.level * 25) + i2), scanResult);
                }
            }
            this.b.clear();
            for (ScanResult add : this.p.values()) {
                this.b.add(add);
            }
            this.p.clear();
        }
    }

    public static String o() {
        return String.valueOf(et.b() - g);
    }

    private List<ScanResult> p() {
        if (this.a != null) {
            try {
                List<ScanResult> scanResults = this.a.getScanResults();
                if (Build.VERSION.SDK_INT >= 17) {
                    HashMap<String, Long> hashMap = new HashMap<>(36);
                    for (ScanResult next : scanResults) {
                        hashMap.put(next.BSSID, Long.valueOf(next.timestamp));
                    }
                    if (s.isEmpty() || !s.equals(hashMap)) {
                        s = hashMap;
                        t = et.b();
                    }
                } else {
                    t = et.b();
                }
                this.o = null;
                return scanResults;
            } catch (SecurityException e2) {
                this.o = e2.getMessage();
            } catch (Throwable th) {
                this.o = null;
                en.a(th, "WifiManagerWrapper", "getScanResults");
            }
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004a, code lost:
        if (r4 < r0) goto L_0x007f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0069 A[Catch:{ Throwable -> 0x0081 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:? A[Catch:{ Throwable -> 0x0081 }, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void q() {
        /*
            r8 = this;
            r2 = 30000(0x7530, double:1.4822E-319)
            boolean r0 = r8.r()
            if (r0 == 0) goto L_0x006f
            long r0 = com.loc.et.b()     // Catch:{ Throwable -> 0x0081 }
            long r4 = d     // Catch:{ Throwable -> 0x0081 }
            long r4 = r0 - r4
            r0 = 4900(0x1324, double:2.421E-320)
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x007f
            android.net.ConnectivityManager r0 = r8.v     // Catch:{ Throwable -> 0x0081 }
            if (r0 != 0) goto L_0x0027
            android.content.Context r0 = r8.i     // Catch:{ Throwable -> 0x0081 }
            java.lang.String r1 = "connectivity"
            java.lang.Object r0 = com.loc.et.a((android.content.Context) r0, (java.lang.String) r1)     // Catch:{ Throwable -> 0x0081 }
            android.net.ConnectivityManager r0 = (android.net.ConnectivityManager) r0     // Catch:{ Throwable -> 0x0081 }
            r8.v = r0     // Catch:{ Throwable -> 0x0081 }
        L_0x0027:
            android.net.ConnectivityManager r0 = r8.v     // Catch:{ Throwable -> 0x0081 }
            boolean r0 = r8.a((android.net.ConnectivityManager) r0)     // Catch:{ Throwable -> 0x0081 }
            if (r0 == 0) goto L_0x0035
            r0 = 9900(0x26ac, double:4.8912E-320)
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x007f
        L_0x0035:
            int r0 = u     // Catch:{ Throwable -> 0x0081 }
            r1 = 1
            if (r0 <= r1) goto L_0x004c
            long r0 = r8.z     // Catch:{ Throwable -> 0x0081 }
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 == 0) goto L_0x0070
            long r0 = r8.z     // Catch:{ Throwable -> 0x0081 }
        L_0x0042:
            int r2 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0081 }
            r3 = 28
            if (r2 < r3) goto L_0x004c
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x007f
        L_0x004c:
            android.net.wifi.WifiManager r0 = r8.a     // Catch:{ Throwable -> 0x0081 }
            if (r0 == 0) goto L_0x007f
            long r0 = com.loc.et.b()     // Catch:{ Throwable -> 0x0081 }
            d = r0     // Catch:{ Throwable -> 0x0081 }
            int r0 = u     // Catch:{ Throwable -> 0x0081 }
            r1 = 2
            if (r0 >= r1) goto L_0x0061
            int r0 = u     // Catch:{ Throwable -> 0x0081 }
            int r0 = r0 + 1
            u = r0     // Catch:{ Throwable -> 0x0081 }
        L_0x0061:
            android.net.wifi.WifiManager r0 = r8.a     // Catch:{ Throwable -> 0x0081 }
            boolean r0 = r0.startScan()     // Catch:{ Throwable -> 0x0081 }
        L_0x0067:
            if (r0 == 0) goto L_0x006f
            long r0 = com.loc.et.b()     // Catch:{ Throwable -> 0x0081 }
            f = r0     // Catch:{ Throwable -> 0x0081 }
        L_0x006f:
            return
        L_0x0070:
            long r0 = com.loc.em.u()     // Catch:{ Throwable -> 0x0081 }
            r6 = -1
            int r0 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r0 == 0) goto L_0x008c
            long r0 = com.loc.em.u()     // Catch:{ Throwable -> 0x0081 }
            goto L_0x0042
        L_0x007f:
            r0 = 0
            goto L_0x0067
        L_0x0081:
            r0 = move-exception
            java.lang.String r1 = "WifiManager"
            java.lang.String r2 = "wifiScan"
            com.loc.en.a(r0, r1, r2)
            goto L_0x006f
        L_0x008c:
            r0 = r2
            goto L_0x0042
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.eb.q():void");
    }

    private boolean r() {
        this.q = this.a == null ? false : et.h(this.i);
        if (!this.q || !this.l) {
            return false;
        }
        if (f == 0) {
            return true;
        }
        if (et.b() - f < 4900 || et.b() - g < 1500) {
            return false;
        }
        return et.b() - g > 4900 ? true : true;
    }

    public final ArrayList<dh> a() {
        if (!this.r) {
            return this.c;
        }
        b(true);
        return this.c;
    }

    public final void a(ds dsVar) {
        this.A = dsVar;
    }

    public final void a(boolean z2) {
        Context context = this.i;
        if (em.t() && this.n && this.a != null && context != null && z2 && et.c() > 17) {
            ContentResolver contentResolver = context.getContentResolver();
            try {
                if (((Integer) eq.a("android.provider.Settings$Global", "getInt", new Object[]{contentResolver, "wifi_scan_always_enabled"}, (Class<?>[]) new Class[]{ContentResolver.class, String.class})).intValue() == 0) {
                    eq.a("android.provider.Settings$Global", "putInt", new Object[]{contentResolver, "wifi_scan_always_enabled", 1}, (Class<?>[]) new Class[]{ContentResolver.class, String.class, Integer.TYPE});
                }
            } catch (Throwable th) {
                en.a(th, "WifiManagerWrapper", "enableWifiAlwaysScan");
            }
        }
    }

    public final void a(boolean z2, boolean z3, boolean z4, long j2) {
        this.l = z2;
        this.m = z3;
        this.n = z4;
        if (j2 < 10000) {
            this.z = 10000;
        } else {
            this.z = j2;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0019, code lost:
        if (a(r2.getConnectionInfo()) != false) goto L_0x001b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean a(android.net.ConnectivityManager r5) {
        /*
            r4 = this;
            r0 = 1
            r1 = 0
            android.net.wifi.WifiManager r2 = r4.a
            if (r2 != 0) goto L_0x0007
        L_0x0006:
            return r1
        L_0x0007:
            android.net.NetworkInfo r3 = r5.getActiveNetworkInfo()     // Catch:{ Throwable -> 0x001d }
            int r3 = com.loc.et.a((android.net.NetworkInfo) r3)     // Catch:{ Throwable -> 0x001d }
            if (r3 != r0) goto L_0x0027
            android.net.wifi.WifiInfo r2 = r2.getConnectionInfo()     // Catch:{ Throwable -> 0x001d }
            boolean r2 = a((android.net.wifi.WifiInfo) r2)     // Catch:{ Throwable -> 0x001d }
            if (r2 == 0) goto L_0x0027
        L_0x001b:
            r1 = r0
            goto L_0x0006
        L_0x001d:
            r0 = move-exception
            java.lang.String r2 = "WifiManagerWrapper"
            java.lang.String r3 = "wifiAccess"
            com.loc.en.a(r0, r2, r3)
        L_0x0027:
            r0 = r1
            goto L_0x001b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.eb.a(android.net.ConnectivityManager):boolean");
    }

    public final void b(boolean z2) {
        boolean z3;
        if (!z2) {
            q();
        } else if (r()) {
            long b2 = et.b();
            if (b2 - e >= 10000) {
                this.b.clear();
                h = g;
            }
            q();
            if (b2 - e >= 10000) {
                for (int i2 = 20; i2 > 0 && g == h; i2--) {
                    try {
                        Thread.sleep(150);
                    } catch (Throwable th) {
                    }
                }
            }
        }
        if (this.x) {
            this.x = false;
            g();
        }
        if (h != g) {
            List<ScanResult> list = null;
            try {
                list = p();
            } catch (Throwable th2) {
                en.a(th2, "WifiManager", "updateScanResult");
            }
            h = g;
            if (list != null) {
                this.b.clear();
                this.b.addAll(list);
            } else {
                this.b.clear();
            }
        }
        if (et.b() - g > 20000) {
            this.b.clear();
        }
        e = et.b();
        if (this.b.isEmpty()) {
            g = et.b();
            List<ScanResult> p2 = p();
            if (p2 != null) {
                this.b.addAll(p2);
                z3 = true;
                c(z3);
            }
        }
        z3 = false;
        c(z3);
    }

    public final WifiInfo c() {
        try {
            if (this.a != null) {
                return this.a.getConnectionInfo();
            }
        } catch (Throwable th) {
            en.a(th, "WifiManagerWrapper", "getConnectionInfo");
        }
        return null;
    }

    public final String d() {
        return this.o;
    }

    public final ArrayList<ScanResult> e() {
        if (this.b == null) {
            return null;
        }
        ArrayList<ScanResult> arrayList = new ArrayList<>();
        if (this.b.isEmpty()) {
            return arrayList;
        }
        arrayList.addAll(this.b);
        return arrayList;
    }

    public final void f() {
        try {
            this.r = true;
            List<ScanResult> p2 = p();
            if (p2 != null) {
                this.b.clear();
                this.b.addAll(p2);
            }
            c(true);
        } catch (Throwable th) {
        }
    }

    public final void g() {
        this.y = null;
        this.b.clear();
    }

    public final void h() {
        w = System.currentTimeMillis();
        if (this.A != null) {
            this.A.b();
        }
    }

    public final void i() {
        if (this.a != null && et.b() - g > 4900) {
            g = et.b();
        }
    }

    public final void j() {
        int i2 = 4;
        if (this.a != null) {
            try {
                if (this.a != null) {
                    i2 = this.a.getWifiState();
                }
            } catch (Throwable th) {
                en.a(th, "Aps", "onReceive part");
            }
            if (this.b == null) {
                this.b = new ArrayList<>();
            }
            switch (i2) {
                case 0:
                case 1:
                case 4:
                    this.x = true;
                    return;
                default:
                    return;
            }
        }
    }

    public final WifiInfo k() {
        this.y = c();
        return this.y;
    }

    public final boolean l() {
        return this.j;
    }

    public final String m() {
        if (this.k == null) {
            this.k = new StringBuilder(700);
        } else {
            this.k.delete(0, this.k.length());
        }
        this.j = false;
        this.y = k();
        String bssid = a(this.y) ? this.y.getBSSID() : "";
        int size = this.b.size();
        int i2 = 0;
        boolean z2 = false;
        boolean z3 = false;
        while (i2 < size) {
            String str = this.b.get(i2).BSSID;
            boolean z4 = (this.m || "<unknown ssid>".equals(this.b.get(i2).SSID)) ? z3 : true;
            String str2 = "nb";
            if (bssid.equals(str)) {
                str2 = TempEvent.TAG_ACCESS;
                z2 = true;
            }
            this.k.append(String.format(Locale.US, "#%s,%s", new Object[]{str, str2}));
            i2++;
            z3 = z4;
        }
        if (this.b.size() == 0) {
            z3 = true;
        }
        if (!this.m && !z3) {
            this.j = true;
        }
        if (!z2 && !TextUtils.isEmpty(bssid)) {
            this.k.append(Constant.INTENT_JSON_MARK).append(bssid);
            this.k.append(",access");
        }
        return this.k.toString();
    }

    public final void n() {
        g();
        this.b.clear();
    }
}
