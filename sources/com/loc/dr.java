package com.loc;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;
import com.amap.api.location.AMapLocationClientOption;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.util.ArrayList;

@SuppressLint({"NewApi"})
/* compiled from: Aps */
public final class dr {
    static int D = -1;
    public static boolean G = true;
    private static boolean L = false;
    private static int N = -1;
    int A = 12;
    dx B = null;
    boolean C = false;
    dv E = null;
    String F = null;
    IntentFilter H = null;
    LocationManager I = null;
    private int J = 0;
    private String K = null;
    private String M = null;
    private boolean O = true;
    private Handler P;
    private String Q;
    private ds R;
    Context a = null;
    ConnectivityManager b = null;
    eb c = null;
    ea d = null;
    ed e = null;
    dt f = null;
    ek g = null;
    ArrayList<ScanResult> h = new ArrayList<>();
    a i = null;
    AMapLocationClientOption j = new AMapLocationClientOption();
    dw k = null;
    long l = 0;
    el m = null;
    boolean n = false;
    ei o = null;
    StringBuilder p = new StringBuilder();
    boolean q = true;
    boolean r = true;
    AMapLocationClientOption.GeoLanguage s = AMapLocationClientOption.GeoLanguage.DEFAULT;
    boolean t = true;
    boolean u = false;
    WifiInfo v = null;
    boolean w = true;
    StringBuilder x = null;
    boolean y = false;
    public boolean z = false;

    /* compiled from: Aps */
    class a extends BroadcastReceiver {
        a() {
        }

        public final void onReceive(Context context, Intent intent) {
            if (context != null && intent != null) {
                try {
                    String action = intent.getAction();
                    if (TextUtils.isEmpty(action)) {
                        return;
                    }
                    if (action.equals("android.net.wifi.SCAN_RESULTS")) {
                        if (dr.this.c != null) {
                            dr.this.c.i();
                        }
                        try {
                            if (intent.getExtras() != null && intent.getExtras().getBoolean("resultsUpdated", true) && dr.this.c != null) {
                                dr.this.c.h();
                            }
                        } catch (Throwable th) {
                        }
                    } else if (action.equals("android.net.wifi.WIFI_STATE_CHANGED") && dr.this.c != null) {
                        dr.this.c.j();
                    }
                } catch (Throwable th2) {
                    en.a(th2, "Aps", "onReceive");
                }
            }
        }
    }

    private static dw a(int i2, String str) {
        dw dwVar = new dw("");
        dwVar.setErrorCode(i2);
        dwVar.setLocationDetail(str);
        if (i2 == 15) {
            er.a((String) null, 2151);
        }
        return dwVar;
    }

    private dw a(dw dwVar, ba baVar, dq dqVar) {
        if (baVar != null) {
            try {
                if (!(baVar.a == null || baVar.a.length == 0)) {
                    ek ekVar = new ek();
                    String str = new String(baVar.a, "UTF-8");
                    if (str.contains("\"status\":\"0\"")) {
                        dw a2 = ekVar.a(str, this.a, baVar, dqVar);
                        a2.h(this.x.toString());
                        return a2;
                    } else if (!str.contains("</body></html>")) {
                        return null;
                    } else {
                        dwVar.setErrorCode(5);
                        if (this.c == null || !this.c.a(this.b)) {
                            dqVar.f("#0502");
                            this.p.append("请求可能被劫持了#0502");
                            er.a((String) null, 2052);
                        } else {
                            dqVar.f("#0501");
                            this.p.append("您连接的是一个需要登录的网络，请确认已经登入网络#0501");
                            er.a((String) null, 2051);
                        }
                        dwVar.setLocationDetail(this.p.toString());
                        return dwVar;
                    }
                }
            } catch (Throwable th) {
                dwVar.setErrorCode(4);
                en.a(th, "Aps", "checkResponseEntity");
                dqVar.f("#0403");
                this.p.append("check response exception ex is" + th.getMessage() + "#0403");
                dwVar.setLocationDetail(this.p.toString());
                return dwVar;
            }
        }
        dwVar.setErrorCode(4);
        this.p.append("网络异常,请求异常#0403");
        dqVar.f("#0403");
        dwVar.h(this.x.toString());
        dwVar.setLocationDetail(this.p.toString());
        if (baVar == null) {
            return dwVar;
        }
        er.a(baVar.d, 2041);
        return dwVar;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:71:0x0265=Splitter:B:71:0x0265, B:34:0x00ff=Splitter:B:34:0x00ff} */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.loc.dw a(boolean r12, boolean r13, com.loc.dq r14) {
        /*
            r11 = this;
            java.lang.String r0 = r11.Q     // Catch:{ Throwable -> 0x0483 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x0483 }
            if (r0 == 0) goto L_0x0032
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0483 }
            r0.<init>()     // Catch:{ Throwable -> 0x0483 }
            android.content.Context r1 = r11.a     // Catch:{ Throwable -> 0x0483 }
            java.lang.String r1 = com.loc.n.a((android.content.Context) r1)     // Catch:{ Throwable -> 0x0483 }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x0483 }
            java.lang.String r1 = ","
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x0483 }
            android.content.Context r1 = r11.a     // Catch:{ Throwable -> 0x0483 }
            java.lang.String r1 = com.loc.n.h(r1)     // Catch:{ Throwable -> 0x0483 }
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x0483 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0483 }
            java.lang.String r0 = com.loc.u.b((java.lang.String) r0)     // Catch:{ Throwable -> 0x0483 }
            r11.Q = r0     // Catch:{ Throwable -> 0x0483 }
        L_0x0032:
            java.lang.StringBuilder r0 = r11.p     // Catch:{ Throwable -> 0x0483 }
            java.lang.String r1 = "#id:"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch:{ Throwable -> 0x0483 }
            java.lang.String r1 = r11.Q     // Catch:{ Throwable -> 0x0483 }
            r0.append(r1)     // Catch:{ Throwable -> 0x0483 }
        L_0x0040:
            com.loc.dw r8 = new com.loc.dw
            java.lang.String r0 = ""
            r8.<init>(r0)
            com.loc.el r0 = r11.m     // Catch:{ Throwable -> 0x0150 }
            if (r0 != 0) goto L_0x0053
            com.loc.el r0 = new com.loc.el     // Catch:{ Throwable -> 0x0150 }
            r0.<init>()     // Catch:{ Throwable -> 0x0150 }
            r11.m = r0     // Catch:{ Throwable -> 0x0150 }
        L_0x0053:
            com.amap.api.location.AMapLocationClientOption r0 = r11.j     // Catch:{ Throwable -> 0x0150 }
            if (r0 != 0) goto L_0x005e
            com.amap.api.location.AMapLocationClientOption r0 = new com.amap.api.location.AMapLocationClientOption     // Catch:{ Throwable -> 0x0150 }
            r0.<init>()     // Catch:{ Throwable -> 0x0150 }
            r11.j = r0     // Catch:{ Throwable -> 0x0150 }
        L_0x005e:
            com.loc.el r0 = r11.m     // Catch:{ Throwable -> 0x0150 }
            android.content.Context r1 = r11.a     // Catch:{ Throwable -> 0x0150 }
            com.amap.api.location.AMapLocationClientOption r2 = r11.j     // Catch:{ Throwable -> 0x0150 }
            boolean r2 = r2.isNeedAddress()     // Catch:{ Throwable -> 0x0150 }
            com.amap.api.location.AMapLocationClientOption r3 = r11.j     // Catch:{ Throwable -> 0x0150 }
            boolean r3 = r3.isOffset()     // Catch:{ Throwable -> 0x0150 }
            com.loc.ea r4 = r11.d     // Catch:{ Throwable -> 0x0150 }
            com.loc.eb r5 = r11.c     // Catch:{ Throwable -> 0x0150 }
            android.net.ConnectivityManager r6 = r11.b     // Catch:{ Throwable -> 0x0150 }
            java.lang.String r7 = r11.F     // Catch:{ Throwable -> 0x0150 }
            r0.a(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x0150 }
            com.loc.el r0 = r11.m     // Catch:{ Throwable -> 0x0150 }
            byte[] r2 = r0.a()     // Catch:{ Throwable -> 0x0150 }
            long r0 = com.loc.et.b()
            r11.l = r0
            long r0 = r11.l
            r14.a((long) r0)
            android.content.Context r0 = r11.a     // Catch:{ Throwable -> 0x01c1 }
            com.loc.en.c(r0)     // Catch:{ Throwable -> 0x01c1 }
            com.loc.ei r0 = r11.o     // Catch:{ Throwable -> 0x01c1 }
            android.content.Context r1 = r11.a     // Catch:{ Throwable -> 0x01c1 }
            java.lang.String r3 = com.loc.en.a()     // Catch:{ Throwable -> 0x01c1 }
            java.lang.String r4 = com.loc.en.b()     // Catch:{ Throwable -> 0x01c1 }
            r5 = r13
            com.loc.ej r2 = r0.a(r1, r2, r3, r4, r5)     // Catch:{ Throwable -> 0x01c1 }
            java.lang.String r3 = r2.c()     // Catch:{ Throwable -> 0x01c1 }
            java.lang.String r4 = r2.h()     // Catch:{ Throwable -> 0x01c1 }
            android.content.Context r0 = r11.a     // Catch:{ Throwable -> 0x01c1 }
            com.loc.l.a((android.content.Context) r0)     // Catch:{ Throwable -> 0x01c1 }
            boolean r0 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x01c1 }
            if (r0 != 0) goto L_0x0192
            java.lang.String r0 = "dualstack"
            boolean r0 = r4.contains(r0)     // Catch:{ Throwable -> 0x01c1 }
            if (r0 == 0) goto L_0x0192
            r0 = 1
        L_0x00bd:
            boolean r1 = com.loc.l.a()     // Catch:{ Throwable -> 0x01c1 }
            if (r1 == 0) goto L_0x022c
            boolean r1 = com.loc.l.c()     // Catch:{ Throwable -> 0x01c1 }
            if (r1 == 0) goto L_0x022c
            if (r0 == 0) goto L_0x022c
            java.lang.String r0 = "v6"
            r14.a((java.lang.String) r0)     // Catch:{ Throwable -> 0x01c1 }
            r5 = 0
            boolean r0 = com.loc.l.b()     // Catch:{ Throwable -> 0x01c1 }
            if (r0 != 0) goto L_0x00e4
            android.content.Context r0 = r11.a     // Catch:{ Throwable -> 0x01c1 }
            com.loc.eg r0 = com.loc.eg.a((android.content.Context) r0)     // Catch:{ Throwable -> 0x01c1 }
            int r1 = com.loc.eg.b     // Catch:{ Throwable -> 0x01c1 }
            java.lang.String r5 = r0.a((com.loc.ej) r2, (int) r1)     // Catch:{ Throwable -> 0x01c1 }
        L_0x00e4:
            boolean r0 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x01c1 }
            if (r0 != 0) goto L_0x021a
            com.loc.ei r0 = r11.o     // Catch:{ Throwable -> 0x0195 }
            r1 = 2
            com.loc.ba r6 = r0.a(r2, r1)     // Catch:{ Throwable -> 0x0195 }
            long r0 = com.loc.et.b()     // Catch:{ Throwable -> 0x0195 }
            r14.b((java.lang.String) r5)     // Catch:{ Throwable -> 0x0195 }
            java.lang.String r7 = "SUCCESS"
            r14.c((java.lang.String) r7)     // Catch:{ Throwable -> 0x0195 }
            r2 = r6
        L_0x00ff:
            android.content.Context r3 = r11.a     // Catch:{ Throwable -> 0x01c1 }
            com.loc.eg r3 = com.loc.eg.a((android.content.Context) r3)     // Catch:{ Throwable -> 0x01c1 }
            r4 = 1
            int r5 = com.loc.eg.b     // Catch:{ Throwable -> 0x01c1 }
            r3.a((boolean) r4, (int) r5)     // Catch:{ Throwable -> 0x01c1 }
        L_0x010b:
            com.loc.ds r3 = r11.R     // Catch:{ Throwable -> 0x01c1 }
            if (r3 == 0) goto L_0x0114
            com.loc.ds r3 = r11.R     // Catch:{ Throwable -> 0x01c1 }
            r3.d()     // Catch:{ Throwable -> 0x01c1 }
        L_0x0114:
            r14.b((long) r0)
            java.lang.String r0 = ""
            if (r2 == 0) goto L_0x0146
            java.lang.String r0 = r2.c
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x013b
            java.lang.StringBuilder r0 = r11.p
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r3 = "#csid:"
            r1.<init>(r3)
            java.lang.String r3 = r2.c
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r1 = r1.toString()
            r0.append(r1)
        L_0x013b:
            java.lang.String r0 = r2.d
            java.lang.StringBuilder r1 = r11.x
            java.lang.String r1 = r1.toString()
            r8.h(r1)
        L_0x0146:
            if (r12 != 0) goto L_0x0486
            com.loc.dw r1 = r11.a((com.loc.dw) r8, (com.loc.ba) r2, (com.loc.dq) r14)
            if (r1 == 0) goto L_0x034d
            r8 = r1
        L_0x014f:
            return r8
        L_0x0150:
            r0 = move-exception
            java.lang.String r1 = "#0301"
            r14.f(r1)
            java.lang.StringBuilder r1 = r11.p
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "get parames error:"
            r2.<init>(r3)
            java.lang.String r0 = r0.getMessage()
            java.lang.StringBuilder r0 = r2.append(r0)
            java.lang.String r2 = "#0301"
            java.lang.StringBuilder r0 = r0.append(r2)
            java.lang.String r0 = r0.toString()
            r1.append(r0)
            r0 = 0
            r1 = 2031(0x7ef, float:2.846E-42)
            com.loc.er.a((java.lang.String) r0, (int) r1)
            r0 = 3
            java.lang.StringBuilder r1 = r11.p
            java.lang.String r1 = r1.toString()
            com.loc.dw r8 = a((int) r0, (java.lang.String) r1)
            java.lang.StringBuilder r0 = r11.x
            java.lang.String r0 = r0.toString()
            r8.h(r0)
            goto L_0x014f
        L_0x0192:
            r0 = 0
            goto L_0x00bd
        L_0x0195:
            r0 = move-exception
            int r6 = com.loc.eg.b     // Catch:{ Throwable -> 0x01c1 }
            r0 = r11
            r1 = r14
            r0.a(r1, r2, r3, r4, r5, r6)     // Catch:{ Throwable -> 0x01c1 }
            boolean r0 = com.loc.l.b()     // Catch:{ Throwable -> 0x01c1 }
            if (r0 == 0) goto L_0x0212
            com.loc.ei r0 = r11.o     // Catch:{ Throwable -> 0x01c1 }
            r1 = 4
            com.loc.ba r2 = r0.a(r2, r1)     // Catch:{ Throwable -> 0x01c1 }
        L_0x01aa:
            long r0 = com.loc.et.b()     // Catch:{ Throwable -> 0x01c1 }
            java.lang.String r3 = "SUCCESS"
            r14.d((java.lang.String) r3)     // Catch:{ Throwable -> 0x01c1 }
            android.content.Context r3 = r11.a     // Catch:{ Throwable -> 0x01c1 }
            com.loc.eg r3 = com.loc.eg.a((android.content.Context) r3)     // Catch:{ Throwable -> 0x01c1 }
            int r4 = com.loc.eg.b     // Catch:{ Throwable -> 0x01c1 }
            r3.a((int) r4)     // Catch:{ Throwable -> 0x01c1 }
            goto L_0x00ff
        L_0x01c1:
            r1 = move-exception
            com.loc.et.b()
            java.lang.String r0 = "FAIL"
            r14.d((java.lang.String) r0)
            android.content.Context r0 = r11.a
            com.loc.eg r0 = com.loc.eg.a((android.content.Context) r0)
            r2 = 0
            int r3 = com.loc.eg.a
            r0.a((boolean) r2, (int) r3)
            java.lang.String r0 = "Aps"
            java.lang.String r2 = "getApsLoc req"
            com.loc.en.a(r1, r0, r2)
            java.lang.String r0 = "/mobile/binary"
            com.loc.er.a((java.lang.String) r0, (java.lang.Throwable) r1)
            android.content.Context r0 = r11.a
            boolean r0 = com.loc.et.d((android.content.Context) r0)
            if (r0 != 0) goto L_0x02aa
            java.lang.String r0 = "#0401"
            r14.f(r0)
            java.lang.StringBuilder r0 = r11.p
            java.lang.String r1 = "网络异常，未连接到网络，请连接网络#0401"
            r0.append(r1)
        L_0x01fc:
            r0 = 4
            java.lang.StringBuilder r1 = r11.p
            java.lang.String r1 = r1.toString()
            com.loc.dw r8 = a((int) r0, (java.lang.String) r1)
            java.lang.StringBuilder r0 = r11.x
            java.lang.String r0 = r0.toString()
            r8.h(r0)
            goto L_0x014f
        L_0x0212:
            com.loc.ei r0 = r11.o     // Catch:{ Throwable -> 0x01c1 }
            r1 = 1
            com.loc.ba r2 = r0.a(r2, r1)     // Catch:{ Throwable -> 0x01c1 }
            goto L_0x01aa
        L_0x021a:
            com.loc.ei r0 = r11.o     // Catch:{ Throwable -> 0x01c1 }
            com.loc.ba r2 = r0.a((com.loc.ej) r2)     // Catch:{ Throwable -> 0x01c1 }
            long r0 = com.loc.et.b()     // Catch:{ Throwable -> 0x01c1 }
            java.lang.String r3 = "SUCCESS"
            r14.d((java.lang.String) r3)     // Catch:{ Throwable -> 0x01c1 }
            goto L_0x00ff
        L_0x022c:
            java.lang.String r0 = "v4"
            r14.a((java.lang.String) r0)     // Catch:{ Throwable -> 0x01c1 }
            android.content.Context r0 = r11.a     // Catch:{ Throwable -> 0x01c1 }
            com.loc.eg r0 = com.loc.eg.a((android.content.Context) r0)     // Catch:{ Throwable -> 0x01c1 }
            int r1 = com.loc.eg.a     // Catch:{ Throwable -> 0x01c1 }
            java.lang.String r5 = r0.a((com.loc.ej) r2, (int) r1)     // Catch:{ Throwable -> 0x01c1 }
            boolean r0 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x01c1 }
            if (r0 != 0) goto L_0x0298
            com.loc.ei r0 = r11.o     // Catch:{ Throwable -> 0x0273 }
            r1 = 1
            com.loc.ba r6 = r0.a(r2, r1)     // Catch:{ Throwable -> 0x0273 }
            long r0 = com.loc.et.b()     // Catch:{ Throwable -> 0x0273 }
            r14.b((java.lang.String) r5)     // Catch:{ Throwable -> 0x0273 }
            java.lang.String r7 = "SUCCESS"
            r14.c((java.lang.String) r7)     // Catch:{ Throwable -> 0x0273 }
            android.content.Context r7 = r11.a     // Catch:{ Throwable -> 0x0273 }
            com.loc.eg r7 = com.loc.eg.a((android.content.Context) r7)     // Catch:{ Throwable -> 0x0273 }
            r9 = 1
            int r10 = com.loc.eg.a     // Catch:{ Throwable -> 0x0273 }
            r7.a((boolean) r9, (int) r10)     // Catch:{ Throwable -> 0x0273 }
            r2 = r6
        L_0x0265:
            android.content.Context r3 = r11.a     // Catch:{ Throwable -> 0x01c1 }
            com.loc.eg r3 = com.loc.eg.a((android.content.Context) r3)     // Catch:{ Throwable -> 0x01c1 }
            r4 = 1
            int r5 = com.loc.eg.a     // Catch:{ Throwable -> 0x01c1 }
            r3.a((boolean) r4, (int) r5)     // Catch:{ Throwable -> 0x01c1 }
            goto L_0x010b
        L_0x0273:
            r0 = move-exception
            int r6 = com.loc.eg.a     // Catch:{ Throwable -> 0x01c1 }
            r0 = r11
            r1 = r14
            r0.a(r1, r2, r3, r4, r5, r6)     // Catch:{ Throwable -> 0x01c1 }
            com.loc.ei r0 = r11.o     // Catch:{ Throwable -> 0x01c1 }
            r1 = 1
            com.loc.ba r2 = r0.a(r2, r1)     // Catch:{ Throwable -> 0x01c1 }
            long r0 = com.loc.et.b()     // Catch:{ Throwable -> 0x01c1 }
            java.lang.String r3 = "SUCCESS"
            r14.d((java.lang.String) r3)     // Catch:{ Throwable -> 0x01c1 }
            android.content.Context r3 = r11.a     // Catch:{ Throwable -> 0x01c1 }
            com.loc.eg r3 = com.loc.eg.a((android.content.Context) r3)     // Catch:{ Throwable -> 0x01c1 }
            int r4 = com.loc.eg.a     // Catch:{ Throwable -> 0x01c1 }
            r3.a((int) r4)     // Catch:{ Throwable -> 0x01c1 }
            goto L_0x0265
        L_0x0298:
            com.loc.ei r0 = r11.o     // Catch:{ Throwable -> 0x01c1 }
            r1 = 1
            com.loc.ba r2 = r0.a(r2, r1)     // Catch:{ Throwable -> 0x01c1 }
            long r0 = com.loc.et.b()     // Catch:{ Throwable -> 0x01c1 }
            java.lang.String r3 = "SUCCESS"
            r14.d((java.lang.String) r3)     // Catch:{ Throwable -> 0x01c1 }
            goto L_0x0265
        L_0x02aa:
            boolean r0 = r1 instanceof com.loc.j
            if (r0 == 0) goto L_0x032c
            r0 = r1
            com.loc.j r0 = (com.loc.j) r0
            java.lang.String r0 = r0.a()
            java.lang.String r2 = "网络异常状态码"
            boolean r0 = r0.contains(r2)
            if (r0 == 0) goto L_0x02d8
            java.lang.String r0 = "#0404"
            r14.f(r0)
            java.lang.StringBuilder r0 = r11.p
            java.lang.String r2 = "网络异常，状态码错误#0404"
            java.lang.StringBuilder r0 = r0.append(r2)
            com.loc.j r1 = (com.loc.j) r1
            int r1 = r1.f()
            r0.append(r1)
            goto L_0x01fc
        L_0x02d8:
            r0 = r1
            com.loc.j r0 = (com.loc.j) r0
            int r0 = r0.f()
            r2 = 23
            if (r0 == r2) goto L_0x02fb
            long r2 = com.loc.et.b()
            long r4 = r11.l
            long r2 = r2 - r4
            com.amap.api.location.AMapLocationClientOption r0 = r11.j
            long r4 = r0.getHttpTimeOut()
            long r2 = r2 - r4
            long r2 = java.lang.Math.abs(r2)
            r4 = 500(0x1f4, double:2.47E-321)
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 >= 0) goto L_0x030b
        L_0x02fb:
            java.lang.String r0 = "#0402"
            r14.f(r0)
            java.lang.StringBuilder r0 = r11.p
            java.lang.String r1 = "网络异常，连接超时#0402"
            r0.append(r1)
            goto L_0x01fc
        L_0x030b:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = "#0403,"
            r0.<init>(r2)
            java.lang.String r1 = r1.getMessage()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r14.f(r0)
            java.lang.StringBuilder r0 = r11.p
            java.lang.String r1 = "网络异常,请求异常#0403"
            r0.append(r1)
            goto L_0x01fc
        L_0x032c:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = "#0403,"
            r0.<init>(r2)
            java.lang.String r1 = r1.getMessage()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r14.f(r0)
            java.lang.StringBuilder r0 = r11.p
            java.lang.String r1 = "网络异常,请求异常#0403"
            r0.append(r1)
            goto L_0x01fc
        L_0x034d:
            byte[] r1 = r2.a
            byte[] r1 = com.loc.ec.b((byte[]) r1)
            if (r1 != 0) goto L_0x0377
            r1 = 5
            r8.setErrorCode(r1)
            java.lang.String r1 = "#0503"
            r14.f(r1)
            java.lang.StringBuilder r1 = r11.p
            java.lang.String r2 = "解密数据失败#0503"
            r1.append(r2)
            java.lang.StringBuilder r1 = r11.p
            java.lang.String r1 = r1.toString()
            r8.setLocationDetail(r1)
            r1 = 2053(0x805, float:2.877E-42)
            com.loc.er.a((java.lang.String) r0, (int) r1)
            goto L_0x014f
        L_0x0377:
            com.loc.ek r2 = r11.g
            com.loc.dw r1 = r2.a(r8, r1, r14)
            boolean r2 = com.loc.et.a((com.loc.dw) r1)
            if (r2 != 0) goto L_0x03f4
            java.lang.String r2 = r1.b()
            r11.K = r2
            java.lang.String r2 = r11.K
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L_0x03eb
            r2 = 2062(0x80e, float:2.89E-42)
            com.loc.er.a((java.lang.String) r0, (int) r2)
        L_0x0396:
            r0 = 6
            r1.setErrorCode(r0)
            java.lang.String r0 = "#0601"
            r14.f(r0)
            java.lang.StringBuilder r2 = r11.p
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r3 = "location faile retype:"
            r0.<init>(r3)
            java.lang.String r3 = r1.d()
            java.lang.StringBuilder r0 = r0.append(r3)
            java.lang.String r3 = " rdesc:"
            java.lang.StringBuilder r3 = r0.append(r3)
            java.lang.String r0 = r11.K
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x03f1
            java.lang.String r0 = ""
        L_0x03c4:
            java.lang.StringBuilder r0 = r3.append(r0)
            java.lang.String r3 = "#0601"
            java.lang.StringBuilder r0 = r0.append(r3)
            java.lang.String r0 = r0.toString()
            r2.append(r0)
            java.lang.StringBuilder r0 = r11.x
            java.lang.String r0 = r0.toString()
            r1.h(r0)
            java.lang.StringBuilder r0 = r11.p
            java.lang.String r0 = r0.toString()
            r1.setLocationDetail(r0)
            r8 = r1
            goto L_0x014f
        L_0x03eb:
            r2 = 2061(0x80d, float:2.888E-42)
            com.loc.er.a((java.lang.String) r0, (int) r2)
            goto L_0x0396
        L_0x03f1:
            java.lang.String r0 = r11.K
            goto L_0x03c4
        L_0x03f4:
            int r0 = r1.getErrorCode()
            if (r0 != 0) goto L_0x0452
            int r0 = r1.getLocationType()
            if (r0 != 0) goto L_0x0452
            java.lang.String r0 = "-5"
            java.lang.String r2 = r1.d()
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x044e
            java.lang.String r0 = "1"
            java.lang.String r2 = r1.d()
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x044e
            java.lang.String r0 = "2"
            java.lang.String r2 = r1.d()
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x044e
            java.lang.String r0 = "14"
            java.lang.String r2 = r1.d()
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x044e
            java.lang.String r0 = "24"
            java.lang.String r2 = r1.d()
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x044e
            java.lang.String r0 = "-1"
            java.lang.String r2 = r1.d()
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x047e
        L_0x044e:
            r0 = 5
            r1.setLocationType(r0)
        L_0x0452:
            boolean r0 = r11.r
            r1.setOffset(r0)
            boolean r0 = r11.q
            r1.a((boolean) r0)
            com.amap.api.location.AMapLocationClientOption$GeoLanguage r0 = r11.s
            java.lang.String r0 = java.lang.String.valueOf(r0)
            r1.f(r0)
            r0 = r1
        L_0x0466:
            java.lang.String r1 = "new"
            r0.e(r1)
            java.lang.StringBuilder r1 = r11.p
            java.lang.String r1 = r1.toString()
            r0.setLocationDetail(r1)
            java.lang.String r1 = r0.a()
            r11.F = r1
            r8 = r0
            goto L_0x014f
        L_0x047e:
            r0 = 6
            r1.setLocationType(r0)
            goto L_0x0452
        L_0x0483:
            r0 = move-exception
            goto L_0x0040
        L_0x0486:
            r0 = r8
            goto L_0x0466
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.dr.a(boolean, boolean, com.loc.dq):com.loc.dw");
    }

    private StringBuilder a(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder(700);
        } else {
            sb.delete(0, sb.length());
        }
        sb.append(this.d.l());
        sb.append(this.c.m());
        return sb;
    }

    private void a(dq dqVar, ej ejVar, String str, String str2, String str3, int i2) {
        dqVar.b(str3);
        dqVar.c("FAIL");
        eg.a(this.a).a(false, i2);
        ejVar.a(str);
        ejVar.b(str2);
        if (this.o.a() > em.x()) {
            ejVar.a(em.x() * 1000);
            ejVar.b(em.x() * 1000);
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String c(com.loc.dq r11) {
        /*
            r10 = this;
            r3 = 1
            r9 = 2121(0x849, float:2.972E-42)
            r8 = 12
            r2 = 0
            r7 = 0
            java.lang.String r4 = ""
            java.lang.String r5 = "network"
            com.loc.ea r0 = r10.d
            int r1 = r0.f()
            com.loc.ea r0 = r10.d
            com.loc.dz r6 = r0.d()
            java.util.ArrayList<android.net.wifi.ScanResult> r0 = r10.h
            if (r0 == 0) goto L_0x0025
            java.util.ArrayList<android.net.wifi.ScanResult> r0 = r10.h
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L_0x0061
        L_0x0025:
            r0 = r3
        L_0x0026:
            if (r6 != 0) goto L_0x01c2
            if (r0 == 0) goto L_0x01c2
            android.net.ConnectivityManager r0 = r10.b
            if (r0 != 0) goto L_0x003b
            android.content.Context r0 = r10.a
            java.lang.String r1 = "connectivity"
            java.lang.Object r0 = com.loc.et.a((android.content.Context) r0, (java.lang.String) r1)
            android.net.ConnectivityManager r0 = (android.net.ConnectivityManager) r0
            r10.b = r0
        L_0x003b:
            android.content.Context r0 = r10.a
            boolean r0 = com.loc.et.a((android.content.Context) r0)
            if (r0 == 0) goto L_0x0063
            com.loc.eb r0 = r10.c
            boolean r0 = r0.q
            if (r0 != 0) goto L_0x0063
            r0 = 18
            r10.A = r0
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "飞行模式下关闭了WIFI开关，请关闭飞行模式或者打开WIFI开关#1801"
            r0.append(r1)
            r0 = 2132(0x854, float:2.988E-42)
            com.loc.er.a((java.lang.String) r7, (int) r0)
            java.lang.String r0 = "#1801"
            r11.f(r0)
        L_0x0060:
            return r4
        L_0x0061:
            r0 = r2
            goto L_0x0026
        L_0x0063:
            int r0 = com.loc.et.c()
            r1 = 28
            if (r0 < r1) goto L_0x00a7
            android.location.LocationManager r0 = r10.I
            if (r0 != 0) goto L_0x0080
            android.content.Context r0 = r10.a
            android.content.Context r0 = r0.getApplicationContext()
            java.lang.String r1 = "location"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.location.LocationManager r0 = (android.location.LocationManager) r0
            r10.I = r0
        L_0x0080:
            android.location.LocationManager r0 = r10.I
            java.lang.String r1 = "isLocationEnabled"
            java.lang.Object[] r3 = new java.lang.Object[r2]
            java.lang.Object r0 = com.loc.eq.a(r0, r1, r3)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 != 0) goto L_0x00a7
            r10.A = r8
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "定位服务没有开启，请在设置中打开定位服务开关#1206"
            r0.append(r1)
            java.lang.String r0 = "#1206"
            r11.f(r0)
            com.loc.er.a((java.lang.String) r7, (int) r9)
            goto L_0x0060
        L_0x00a7:
            android.content.Context r0 = r10.a
            boolean r0 = com.loc.et.f((android.content.Context) r0)
            if (r0 != 0) goto L_0x00c3
            r10.A = r8
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "定位权限被禁用,请授予应用定位权限#1201"
            r0.append(r1)
            java.lang.String r0 = "#1201"
            r11.f(r0)
            com.loc.er.a((java.lang.String) r7, (int) r9)
            goto L_0x0060
        L_0x00c3:
            int r0 = com.loc.et.c()
            r1 = 24
            if (r0 < r1) goto L_0x00f7
            int r0 = com.loc.et.c()
            r1 = 28
            if (r0 >= r1) goto L_0x00f7
            android.content.Context r0 = r10.a
            android.content.ContentResolver r0 = r0.getContentResolver()
            java.lang.String r1 = "location_mode"
            int r0 = android.provider.Settings.Secure.getInt(r0, r1, r2)
            if (r0 != 0) goto L_0x00f7
            r10.A = r8
            java.lang.String r0 = "#1206"
            r11.f(r0)
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "定位服务没有开启，请在设置中打开定位服务开关#1206"
            r0.append(r1)
            com.loc.er.a((java.lang.String) r7, (int) r9)
            goto L_0x0060
        L_0x00f7:
            com.loc.ea r0 = r10.d
            java.lang.String r0 = r0.j()
            com.loc.eb r1 = r10.c
            java.lang.String r1 = r1.d()
            com.loc.eb r2 = r10.c
            android.net.ConnectivityManager r3 = r10.b
            boolean r2 = r2.a((android.net.ConnectivityManager) r3)
            if (r2 == 0) goto L_0x0124
            if (r1 == 0) goto L_0x0124
            r10.A = r8
            java.lang.String r0 = "#1202"
            r11.f(r0)
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "获取基站与获取WIFI的权限都被禁用，请在安全软件中打开应用的定位权限#1202"
            r0.append(r1)
            com.loc.er.a((java.lang.String) r7, (int) r9)
            goto L_0x0060
        L_0x0124:
            if (r0 == 0) goto L_0x0150
            r10.A = r8
            com.loc.eb r0 = r10.c
            boolean r0 = r0.q
            if (r0 != 0) goto L_0x0141
            java.lang.String r0 = "#1204"
            r11.f(r0)
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "WIFI开关关闭，并且获取基站权限被禁用，请在安全软件中打开应用的定位权限或者打开WIFI开关#1204"
            r0.append(r1)
        L_0x013c:
            com.loc.er.a((java.lang.String) r7, (int) r9)
            goto L_0x0060
        L_0x0141:
            java.lang.String r0 = "#1205"
            r11.f(r0)
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "获取的WIFI列表为空，并且获取基站权限被禁用，请在安全软件中打开应用的定位权限#1205"
            r0.append(r1)
            goto L_0x013c
        L_0x0150:
            com.loc.eb r0 = r10.c
            boolean r0 = r0.q
            if (r0 != 0) goto L_0x0177
            com.loc.ea r0 = r10.d
            boolean r0 = r0.m()
            if (r0 != 0) goto L_0x0177
            r0 = 19
            r10.A = r0
            java.lang.String r0 = "#1901"
            r11.f(r0)
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "没有检查到SIM卡，并且WIFI开关关闭，请打开WIFI开关或者插入SIM卡#1901"
            r0.append(r1)
            r0 = 2133(0x855, float:2.989E-42)
            com.loc.er.a((java.lang.String) r7, (int) r0)
            goto L_0x0060
        L_0x0177:
            android.content.Context r0 = r10.a
            boolean r0 = com.loc.et.g((android.content.Context) r0)
            if (r0 != 0) goto L_0x0194
            r10.A = r8
            java.lang.String r0 = "#1207"
            r11.f(r0)
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "后台定位服务没有开启，请在设置中打开后台定位服务开关#1207"
            r0.append(r1)
            com.loc.er.a((java.lang.String) r7, (int) r9)
            goto L_0x0060
        L_0x0194:
            com.loc.eb r0 = r10.c
            boolean r0 = r0.q
            if (r0 != 0) goto L_0x01b3
            java.lang.String r0 = "#1301"
            r11.f(r0)
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "获取到的基站为空，并且关闭了WIFI开关，请您打开WIFI开关再发起定位#1301"
            r0.append(r1)
        L_0x01a8:
            r0 = 13
            r10.A = r0
            r0 = 2131(0x853, float:2.986E-42)
            com.loc.er.a((java.lang.String) r7, (int) r0)
            goto L_0x0060
        L_0x01b3:
            java.lang.String r0 = "#1302"
            r11.f(r0)
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "获取到的基站和WIFI信息均为空，请移动到有WIFI的区域，若确定当前区域有WIFI，请检查是否授予APP定位权限#1302"
            r0.append(r1)
            goto L_0x01a8
        L_0x01c2:
            com.loc.eb r0 = r10.c
            android.net.wifi.WifiInfo r0 = r0.k()
            r10.v = r0
            android.net.wifi.WifiInfo r0 = r10.v
            boolean r0 = com.loc.eb.a((android.net.wifi.WifiInfo) r0)
            r10.w = r0
            switch(r1) {
                case 0: goto L_0x02eb;
                case 1: goto L_0x0225;
                case 2: goto L_0x0282;
                default: goto L_0x01d5;
            }
        L_0x01d5:
            r0 = 11
            r10.A = r0
            r0 = 2111(0x83f, float:2.958E-42)
            com.loc.er.a((java.lang.String) r7, (int) r0)
            java.lang.String r0 = "#1101"
            r11.f(r0)
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "get cgi failure#1101"
            r0.append(r1)
        L_0x01ec:
            r0 = r4
        L_0x01ed:
            java.lang.String r1 = "#"
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 != 0) goto L_0x0222
            boolean r2 = r0.startsWith(r1)
            if (r2 != 0) goto L_0x020d
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.StringBuilder r1 = r2.append(r1)
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r0 = r0.toString()
        L_0x020d:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = com.loc.et.e()
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r0 = r0.toString()
        L_0x0222:
            r4 = r0
            goto L_0x0060
        L_0x0225:
            if (r6 == 0) goto L_0x01ec
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            int r0 = r6.a
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r2 = "#"
            r0.append(r2)
            int r0 = r6.b
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r2 = "#"
            r0.append(r2)
            int r0 = r6.c
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r2 = "#"
            r0.append(r2)
            int r0 = r6.d
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r2 = "#"
            r0.append(r2)
            java.lang.StringBuilder r0 = r1.append(r5)
            java.lang.String r2 = "#"
            r0.append(r2)
            java.util.ArrayList<android.net.wifi.ScanResult> r0 = r10.h
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L_0x0272
            boolean r0 = r10.w
            if (r0 == 0) goto L_0x027e
        L_0x0272:
            java.lang.String r0 = "cgiwifi"
        L_0x0275:
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            goto L_0x01ed
        L_0x027e:
            java.lang.String r0 = "cgi"
            goto L_0x0275
        L_0x0282:
            if (r6 == 0) goto L_0x01ec
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            int r0 = r6.a
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r2 = "#"
            r0.append(r2)
            int r0 = r6.b
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r2 = "#"
            r0.append(r2)
            int r0 = r6.g
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r2 = "#"
            r0.append(r2)
            int r0 = r6.h
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r2 = "#"
            r0.append(r2)
            int r0 = r6.i
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r2 = "#"
            r0.append(r2)
            java.lang.StringBuilder r0 = r1.append(r5)
            java.lang.String r2 = "#"
            r0.append(r2)
            java.util.ArrayList<android.net.wifi.ScanResult> r0 = r10.h
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L_0x02db
            boolean r0 = r10.w
            if (r0 == 0) goto L_0x02e7
        L_0x02db:
            java.lang.String r0 = "cgiwifi"
        L_0x02de:
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            goto L_0x01ed
        L_0x02e7:
            java.lang.String r0 = "cgi"
            goto L_0x02de
        L_0x02eb:
            java.util.ArrayList<android.net.wifi.ScanResult> r0 = r10.h
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L_0x02f7
            boolean r0 = r10.w
            if (r0 == 0) goto L_0x03cd
        L_0x02f7:
            r1 = r3
        L_0x02f8:
            boolean r0 = r10.w
            if (r0 == 0) goto L_0x031c
            java.util.ArrayList<android.net.wifi.ScanResult> r0 = r10.h
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L_0x031c
            r0 = 2
            r10.A = r0
            java.lang.String r0 = "#0201"
            r11.f(r0)
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "当前基站为伪基站，并且WIFI权限被禁用，请在安全软件中打开应用的定位权限#0201"
            r0.append(r1)
            r0 = 2021(0x7e5, float:2.832E-42)
            com.loc.er.a((java.lang.String) r7, (int) r0)
            goto L_0x0060
        L_0x031c:
            java.util.ArrayList<android.net.wifi.ScanResult> r0 = r10.h
            int r0 = r0.size()
            if (r0 != r3) goto L_0x036f
            r0 = 2
            r10.A = r0
            boolean r0 = r10.w
            if (r0 != 0) goto L_0x0340
            java.lang.String r0 = "#0202"
            r11.f(r0)
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "当前基站为伪基站，并且搜到的WIFI数量不足，请移动到WIFI比较丰富的区域#0202"
            r0.append(r1)
            r0 = 2022(0x7e6, float:2.833E-42)
            com.loc.er.a((java.lang.String) r7, (int) r0)
            goto L_0x0060
        L_0x0340:
            java.util.ArrayList<android.net.wifi.ScanResult> r0 = r10.h
            java.lang.Object r0 = r0.get(r2)
            android.net.wifi.ScanResult r0 = (android.net.wifi.ScanResult) r0
            java.lang.String r0 = r0.BSSID
            com.loc.eb r6 = r10.c
            android.net.wifi.WifiInfo r6 = r6.k()
            java.lang.String r6 = r6.getBSSID()
            boolean r0 = r6.equals(r0)
            if (r0 == 0) goto L_0x036f
            java.lang.String r0 = "#0202"
            r11.f(r0)
            java.lang.StringBuilder r0 = r10.p
            java.lang.String r1 = "当前基站为伪基站，并且搜到的WIFI数量不足，请移动到WIFI比较丰富的区域#0202"
            r0.append(r1)
            r0 = 2021(0x7e5, float:2.832E-42)
            com.loc.er.a((java.lang.String) r7, (int) r0)
            goto L_0x0060
        L_0x036f:
            java.util.Locale r0 = java.util.Locale.US
            java.lang.String r4 = "#%s#"
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r3[r2] = r5
            java.lang.String r0 = java.lang.String.format(r0, r4, r3)
            if (r1 == 0) goto L_0x0394
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r1 = "wifi"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            goto L_0x01ed
        L_0x0394:
            java.lang.String r1 = "network"
            boolean r1 = r5.equals(r1)
            if (r1 == 0) goto L_0x01ed
            java.lang.String r0 = ""
            r1 = 2
            r10.A = r1
            com.loc.eb r1 = r10.c
            boolean r1 = r1.q
            if (r1 != 0) goto L_0x03be
            java.lang.String r1 = "#0203"
            r11.f(r1)
            java.lang.StringBuilder r1 = r10.p
            java.lang.String r2 = "当前基站为伪基站,并且关闭了WIFI开关，请在设置中打开WIFI开关#0203"
            r1.append(r2)
        L_0x03b7:
            r1 = 2022(0x7e6, float:2.833E-42)
            com.loc.er.a((java.lang.String) r7, (int) r1)
            goto L_0x01ed
        L_0x03be:
            java.lang.String r1 = "#0204"
            r11.f(r1)
            java.lang.StringBuilder r1 = r10.p
            java.lang.String r2 = "当前基站为伪基站,并且没有搜索到WIFI，请移动到WIFI比较丰富的区域#0204"
            r1.append(r2)
            goto L_0x03b7
        L_0x03cd:
            r1 = r2
            goto L_0x02f8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.dr.c(com.loc.dq):java.lang.String");
    }

    private void c(dw dwVar) {
        if (dwVar != null) {
            this.k = dwVar;
        }
    }

    private void j() {
        int i2;
        boolean z2 = true;
        if (this.o != null) {
            try {
                if (this.j == null) {
                    this.j = new AMapLocationClientOption();
                }
                if (this.j.getGeoLanguage() != null) {
                    switch (this.j.getGeoLanguage()) {
                        case DEFAULT:
                            i2 = 0;
                            break;
                        case ZH:
                            i2 = 1;
                            break;
                        case EN:
                            i2 = 2;
                            break;
                        default:
                            i2 = 0;
                            break;
                    }
                } else {
                    i2 = 0;
                }
                ei eiVar = this.o;
                long httpTimeOut = this.j.getHttpTimeOut();
                if (!this.j.getLocationProtocol().equals(AMapLocationClientOption.AMapLocationProtocol.HTTPS)) {
                    z2 = false;
                }
                eiVar.a(httpTimeOut, z2, i2);
            } catch (Throwable th) {
            }
        }
    }

    private void k() {
        try {
            if (this.i == null) {
                this.i = new a();
            }
            if (this.H == null) {
                this.H = new IntentFilter();
                this.H.addAction("android.net.wifi.WIFI_STATE_CHANGED");
                this.H.addAction("android.net.wifi.SCAN_RESULTS");
            }
            this.a.registerReceiver(this.i, this.H);
        } catch (Throwable th) {
            en.a(th, "Aps", "initBroadcastListener");
        }
    }

    private boolean l() {
        this.h = this.c.e();
        return this.h == null || this.h.size() <= 0;
    }

    public final dw a(double d2, double d3) {
        try {
            String a2 = this.o.a(this.a, d2, d3);
            if (a2.contains("\"status\":\"1\"")) {
                dw a3 = this.g.a(a2);
                a3.setLatitude(d2);
                a3.setLongitude(d3);
                return a3;
            }
        } catch (Throwable th) {
        }
        return null;
    }

    public final dw a(dq dqVar) throws Throwable {
        boolean z2;
        c();
        dqVar.e("conitue");
        if (this.a == null) {
            dqVar.f("#0101");
            this.p.append("context is null#0101");
            return a(1, this.p.toString());
        }
        this.J++;
        if (this.J == 1 && this.c != null) {
            this.c.a(this.n);
        }
        long j2 = this.l;
        if (!this.O) {
            this.O = true;
            z2 = false;
        } else {
            z2 = false;
            if (et.b() - j2 < 800) {
                long j3 = 0;
                if (et.a(this.k)) {
                    j3 = et.a() - this.k.getTime();
                }
                if (j3 <= 10000) {
                    z2 = true;
                }
            }
        }
        if (!z2 || !et.a(this.k)) {
            if (this.B != null) {
                if (this.C) {
                    this.B.a();
                } else {
                    this.B.b();
                }
            }
            boolean z3 = false;
            try {
                if (this.j.isOnceLocationLatest() || !this.j.isOnceLocation()) {
                    z3 = true;
                }
                this.c.b(z3);
                this.h = this.c.e();
            } catch (Throwable th) {
                en.a(th, "Aps", "getLocation getScanResultsParam");
            }
            try {
                this.d.a(false, l());
            } catch (Throwable th2) {
                en.a(th2, "Aps", "getLocation getCgiListParam");
            }
            this.M = c(dqVar);
            if (TextUtils.isEmpty(this.M)) {
                return a(this.A, this.p.toString());
            }
            this.x = a(this.x);
            if (this.c.l()) {
                dw a2 = a(15, "networkLocation has been mocked!#1502");
                dqVar.f("#1502");
                a2.setMock(true);
                a2.setTrustedLevel(4);
                return a2;
            }
            dw a3 = this.e.a(this.d, this.l == 0 ? true : et.b() - this.l > 20000, this.k, this.c, this.x, this.M, this.a, false);
            if (et.a(a3)) {
                a3.setTrustedLevel(2);
                c(a3);
            } else {
                dw a4 = a(false, true, dqVar);
                if (et.a(a4)) {
                    a4.e("new");
                    this.e.a(this.x.toString());
                    this.e.a(this.d.d());
                    c(a4);
                    a3 = a4;
                } else {
                    a3 = this.e.a(this.d, false, this.k, this.c, this.x, this.M, this.a, true);
                    if (et.a(a3)) {
                        dqVar.f("#0001");
                        a3.setTrustedLevel(2);
                        c(a3);
                    } else {
                        a3 = a4;
                    }
                }
            }
            try {
                if (!(this.c == null || a3 == null)) {
                    long b2 = eb.b();
                    if (b2 <= 15) {
                        a3.setTrustedLevel(1);
                    } else if (b2 <= 120) {
                        a3.setTrustedLevel(2);
                    } else if (b2 <= 600) {
                        a3.setTrustedLevel(3);
                    } else {
                        a3.setTrustedLevel(4);
                    }
                }
            } catch (Throwable th3) {
            }
            this.e.a(this.M, this.x, a3, this.a, true);
            et.a(a3);
            this.x.delete(0, this.x.length());
            if (a3 != null) {
                if (!this.C || this.B == null) {
                    a3.setAltitude(ClientTraceData.b.f47a);
                    a3.setBearing(0.0f);
                    a3.setSpeed(0.0f);
                } else {
                    a3.setAltitude(this.B.f);
                    a3.setBearing(this.B.c());
                    a3.setSpeed((float) this.B.d());
                }
            }
            c(a3);
            return this.k;
        }
        if (this.t && em.b(this.k.getTime())) {
            this.k.setLocationType(2);
        }
        return this.k;
    }

    public final dw a(dw dwVar) {
        this.E.a(this.t);
        return this.E.a(dwVar);
    }

    public final dw a(boolean z2, dq dqVar) {
        if (z2) {
            dqVar.e("statics");
        } else {
            dqVar.e("first");
        }
        if (this.a == null) {
            dqVar.f("#0101");
            this.p.append("context is null#0101");
            er.a((String) null, 2011);
            return a(1, this.p.toString());
        } else if (this.c.l()) {
            dqVar.f("#1502");
            return a(15, "networkLocation has been mocked!#1502");
        } else {
            a();
            if (TextUtils.isEmpty(this.M)) {
                return a(this.A, this.p.toString());
            }
            dw a2 = a(false, z2, dqVar);
            if (!et.a(a2)) {
                return a2;
            }
            this.e.a(this.x.toString());
            this.e.a(this.d.d());
            c(a2);
            return a2;
        }
    }

    public final void a() {
        this.o = ei.a(this.a);
        j();
        if (this.b == null) {
            this.b = (ConnectivityManager) et.a(this.a, "connectivity");
        }
        if (this.m == null) {
            this.m = new el();
        }
    }

    public final void a(Context context) {
        try {
            if (this.a == null) {
                this.E = new dv();
                this.a = context.getApplicationContext();
                em.c(this.a);
                et.b(this.a);
                if (this.c == null) {
                    this.c = new eb(this.a, (WifiManager) et.a(this.a, "wifi"));
                }
                if (this.d == null) {
                    this.d = new ea(this.a);
                }
                if (this.e == null) {
                    this.e = new ed();
                }
                if (this.g == null) {
                    this.g = new ek();
                }
            }
        } catch (Throwable th) {
            en.a(th, "Aps", "initBase");
        }
    }

    public final void a(Handler handler) {
        this.P = handler;
    }

    public final void a(AMapLocationClientOption aMapLocationClientOption) {
        boolean z2;
        boolean z3;
        boolean z4;
        this.j = aMapLocationClientOption;
        if (this.j == null) {
            this.j = new AMapLocationClientOption();
        }
        if (this.c != null) {
            eb ebVar = this.c;
            this.j.isWifiActiveScan();
            ebVar.a(this.j.isWifiScan(), this.j.isMockEnable(), AMapLocationClientOption.isOpenAlwaysScanWifi(), aMapLocationClientOption.getScanWifiInterval());
        }
        j();
        if (this.e != null) {
            this.e.a(this.j);
        }
        if (this.g != null) {
            this.g.a(this.j);
        }
        AMapLocationClientOption.GeoLanguage geoLanguage = AMapLocationClientOption.GeoLanguage.DEFAULT;
        try {
            geoLanguage = this.j.getGeoLanguage();
            z3 = this.j.isNeedAddress();
            try {
                z4 = this.j.isOffset();
                try {
                    z2 = this.j.isLocationCacheEnable();
                    try {
                        this.u = this.j.isOnceLocationLatest();
                        this.C = this.j.isSensorEnable();
                        if (!(z4 == this.r && z3 == this.q && z2 == this.t && geoLanguage == this.s)) {
                            if (this.e != null) {
                                this.e.a();
                            }
                            c((dw) null);
                            this.O = false;
                            if (this.E != null) {
                                this.E.a();
                            }
                        }
                    } catch (Throwable th) {
                    }
                } catch (Throwable th2) {
                    z2 = true;
                }
            } catch (Throwable th3) {
                z2 = true;
                z4 = true;
            }
        } catch (Throwable th4) {
            z2 = true;
            z3 = true;
            z4 = true;
        }
        this.r = z4;
        this.q = z3;
        this.t = z2;
        this.s = geoLanguage;
    }

    public final void b() {
        if (this.B == null) {
            this.B = new dx(this.a);
        }
        if (this.f == null) {
            this.f = new dt(this.a);
        }
        k();
        this.c.b(false);
        this.h = this.c.e();
        this.d.a(false, l());
        this.e.a(this.a);
        this.f.b();
        try {
            if (this.a.checkCallingOrSelfPermission(u.c("EYW5kcm9pZC5wZXJtaXNzaW9uLldSSVRFX1NFQ1VSRV9TRVRUSU5HUw==")) == 0) {
                this.n = true;
            }
        } catch (Throwable th) {
        }
        this.z = true;
    }

    public final void b(Context context) {
        try {
            if (N == -1 || em.e(context)) {
                N = 1;
                em.a(context);
                if (this.R != null) {
                    this.R.d();
                }
            }
        } catch (Throwable th) {
            en.a(th, "Aps", "initAuth");
        }
    }

    public final void b(dq dqVar) {
        try {
            if (!this.y) {
                if (this.M != null) {
                    this.M = null;
                }
                if (this.x != null) {
                    this.x.delete(0, this.x.length());
                }
                if (this.u) {
                    k();
                }
                this.c.b(this.u);
                this.h = this.c.e();
                this.d.a(true, l());
                this.M = c(dqVar);
                if (!TextUtils.isEmpty(this.M)) {
                    this.x = a(this.x);
                }
                this.y = true;
            }
        } catch (Throwable th) {
            en.a(th, "Aps", "initFirstLocateParam");
        }
    }

    public final void b(dw dwVar) {
        if (et.a(dwVar)) {
            this.e.a(this.M, this.x, dwVar, this.a, true);
        }
    }

    public final void c() {
        if (this.p.length() > 0) {
            this.p.delete(0, this.p.length());
        }
    }

    public final void d() {
        try {
            a(this.a);
            a(this.j);
            dq dqVar = new dq();
            b(dqVar);
            b(a(true, true, dqVar));
        } catch (Throwable th) {
            en.a(th, "Aps", "doFusionLocation");
        }
    }

    @SuppressLint({"NewApi"})
    public final void e() {
        this.F = null;
        this.y = false;
        this.z = false;
        if (this.f != null) {
            this.f.a();
        }
        if (this.e != null) {
            this.e.b(this.a);
        }
        if (this.E != null) {
            this.E.a();
        }
        if (this.g != null) {
            this.g = null;
        }
        try {
            if (!(this.a == null || this.i == null)) {
                this.a.unregisterReceiver(this.i);
            }
        } catch (Throwable th) {
            en.a(th, "Aps", "destroy");
        } finally {
            this.i = null;
        }
        if (this.d != null) {
            this.d.h();
        }
        if (this.c != null) {
            this.c.n();
        }
        if (this.h != null) {
            this.h.clear();
        }
        if (this.B != null) {
            this.B.e();
        }
        this.k = null;
        this.a = null;
        this.x = null;
        this.I = null;
    }

    public final void f() {
        try {
            if (this.f != null) {
                this.f.c();
            }
        } catch (Throwable th) {
            en.a(th, "Aps", "bindAMapService");
        }
    }

    public final dw g() {
        if (this.c.l()) {
            return a(15, "networkLocation has been mocked!#1502");
        }
        if (TextUtils.isEmpty(this.M)) {
            return a(this.A, this.p.toString());
        }
        dw a2 = this.e.a(this.a, this.M, this.x, true);
        if (!et.a(a2)) {
            return a2;
        }
        c(a2);
        return a2;
    }

    public final void h() {
        try {
            if (em.i && this.a != null) {
                if (this.R == null) {
                    this.R = new ds(this.a);
                }
                this.R.a(this.d, this.c, this.P);
            }
        } catch (Throwable th) {
            ab.b(th, "as", "stc");
        }
    }

    public final void i() {
        if (this.R != null) {
            this.R.a();
        }
    }
}
