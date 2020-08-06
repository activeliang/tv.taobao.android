package com.loc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.bumptech.glide.BuildConfig;
import java.util.ArrayList;

@SuppressLint({"NewApi"})
/* compiled from: Req */
public final class el {
    protected static String J = null;
    protected static String L = null;
    protected String A = null;
    protected String B = null;
    protected ArrayList<dz> C = new ArrayList<>();
    protected String D = null;
    protected String E = null;
    protected ArrayList<ScanResult> F = new ArrayList<>();
    protected String G = null;
    protected String H = null;
    protected byte[] I = null;
    protected String K = null;
    protected String M = null;
    protected String N = null;
    private byte[] O = null;
    private int P = 0;
    public String a = "1";
    protected short b = 0;
    protected String c = null;
    protected String d = null;
    protected String e = null;
    protected String f = null;
    protected String g = null;
    public String h = null;
    public String i = null;
    protected String j = null;
    protected String k = null;
    protected String l = null;
    protected String m = null;
    protected String n = null;
    protected String o = null;
    protected String p = null;
    protected String q = null;
    protected String r = null;
    protected String s = null;
    protected String t = null;
    protected String u = null;
    protected String v = null;
    protected String w = null;
    protected String x = null;
    protected String y = null;
    protected int z = 0;

    private static int a(String str, byte[] bArr, int i2) {
        int i3 = 127;
        try {
            if (TextUtils.isEmpty(str)) {
                bArr[i2] = 0;
                return i2 + 1;
            }
            byte[] bytes = str.getBytes("GBK");
            int length = bytes.length;
            if (length <= 127) {
                i3 = length;
            }
            bArr[i2] = (byte) i3;
            int i4 = i2 + 1;
            System.arraycopy(bytes, 0, bArr, i4, i3);
            return i3 + i4;
        } catch (Throwable th) {
            en.a(th, "Req", "copyContentWithByteLen");
            bArr[i2] = 0;
            return i2 + 1;
        }
    }

    private String a(String str, int i2) {
        String[] split = this.B.split("\\*")[i2].split(",");
        if ("lac".equals(str)) {
            return split[0];
        }
        if ("cellid".equals(str)) {
            return split[1];
        }
        if ("signal".equals(str)) {
            return split[2];
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000f, code lost:
        if (r0.length != 6) goto L_0x0011;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] a(java.lang.String r8) {
        /*
            r7 = this;
            r6 = 2
            r2 = 0
            r5 = 6
            java.lang.String r0 = ":"
            java.lang.String[] r0 = r8.split(r0)
            byte[] r1 = new byte[r5]
            if (r0 == 0) goto L_0x0011
            int r3 = r0.length     // Catch:{ Throwable -> 0x0044 }
            if (r3 == r5) goto L_0x001f
        L_0x0011:
            r0 = 6
            java.lang.String[] r0 = new java.lang.String[r0]     // Catch:{ Throwable -> 0x0044 }
            r3 = r2
        L_0x0015:
            if (r3 >= r5) goto L_0x001f
            java.lang.String r4 = "0"
            r0[r3] = r4     // Catch:{ Throwable -> 0x0044 }
            int r3 = r3 + 1
            goto L_0x0015
        L_0x001f:
            int r3 = r0.length     // Catch:{ Throwable -> 0x0044 }
            if (r2 >= r3) goto L_0x0042
            r3 = r0[r2]     // Catch:{ Throwable -> 0x0044 }
            int r3 = r3.length()     // Catch:{ Throwable -> 0x0044 }
            if (r3 <= r6) goto L_0x0034
            r3 = r0[r2]     // Catch:{ Throwable -> 0x0044 }
            r4 = 0
            r5 = 2
            java.lang.String r3 = r3.substring(r4, r5)     // Catch:{ Throwable -> 0x0044 }
            r0[r2] = r3     // Catch:{ Throwable -> 0x0044 }
        L_0x0034:
            r3 = r0[r2]     // Catch:{ Throwable -> 0x0044 }
            r4 = 16
            int r3 = java.lang.Integer.parseInt(r3, r4)     // Catch:{ Throwable -> 0x0044 }
            byte r3 = (byte) r3     // Catch:{ Throwable -> 0x0044 }
            r1[r2] = r3     // Catch:{ Throwable -> 0x0044 }
            int r2 = r2 + 1
            goto L_0x001f
        L_0x0042:
            r0 = r1
        L_0x0043:
            return r0
        L_0x0044:
            r0 = move-exception
            java.lang.String r1 = "Req"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "getMacBa "
            r2.<init>(r3)
            java.lang.StringBuilder r2 = r2.append(r8)
            java.lang.String r2 = r2.toString()
            com.loc.en.a(r0, r1, r2)
            java.lang.String r0 = "00:00:00:00:00:00"
            byte[] r0 = r7.a(r0)
            goto L_0x0043
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.el.a(java.lang.String):byte[]");
    }

    private String b(String str) {
        if (!this.A.contains(str + ">")) {
            return "0";
        }
        int indexOf = this.A.indexOf(str + ">");
        return this.A.substring(indexOf + str.length() + 1, this.A.indexOf("</" + str));
    }

    public final void a(Context context, boolean z2, boolean z3, ea eaVar, eb ebVar, ConnectivityManager connectivityManager, String str) {
        String str2;
        String str3;
        String str4;
        String sb;
        String f2 = k.f(context);
        int d2 = et.d();
        String str5 = "";
        String str6 = "";
        this.K = str;
        if (!z3) {
            str2 = "UC_nlp_20131029";
            str3 = "BKZCHMBBSSUK7U8GLUKHBB56CCFF78U";
        } else {
            str2 = "api_serverSDK_130905";
            str3 = "S128DF1572465B890OE3F7A13167KLEI";
        }
        StringBuilder sb2 = new StringBuilder();
        int e2 = eaVar.e();
        int f3 = eaVar.f();
        TelephonyManager g2 = eaVar.g();
        ArrayList<dz> b2 = eaVar.b();
        ArrayList<dz> c2 = eaVar.c();
        ArrayList<ScanResult> e3 = ebVar.e();
        String str7 = f3 == 2 ? "1" : "0";
        if (g2 != null) {
            if (TextUtils.isEmpty(en.e)) {
                try {
                    en.e = n.x(context);
                } catch (Throwable th) {
                    en.a(th, "Aps", "getApsReq part4");
                }
            }
            if (TextUtils.isEmpty(en.e) && Build.VERSION.SDK_INT < 29) {
                en.e = "888888888888888";
            }
            if (TextUtils.isEmpty(en.f)) {
                try {
                    en.f = n.A(context);
                } catch (SecurityException e4) {
                } catch (Throwable th2) {
                    en.a(th2, "Aps", "getApsReq part2");
                }
            }
            if (TextUtils.isEmpty(en.f) && Build.VERSION.SDK_INT < 29) {
                en.f = "888888888888888";
            }
        }
        NetworkInfo networkInfo = null;
        try {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Throwable th3) {
            en.a(th3, "Aps", "getApsReq part");
        }
        boolean a2 = ebVar.a(connectivityManager);
        if (et.a(networkInfo) != -1) {
            String b3 = et.b(g2);
            if (a2) {
                str5 = b3;
                str6 = "2";
            } else {
                str5 = b3;
                str6 = "1";
            }
        }
        if (!b2.isEmpty()) {
            StringBuilder sb3 = new StringBuilder();
            switch (f3) {
                case 1:
                    dz dzVar = b2.get(0);
                    sb3.delete(0, sb3.length());
                    sb3.append("<mcc>").append(dzVar.a).append("</mcc>");
                    sb3.append("<mnc>").append(dzVar.b).append("</mnc>");
                    sb3.append("<lac>").append(dzVar.c).append("</lac>");
                    sb3.append("<cellid>").append(dzVar.d);
                    sb3.append("</cellid>");
                    sb3.append("<signal>").append(dzVar.j);
                    sb3.append("</signal>");
                    String sb4 = sb3.toString();
                    int i2 = 1;
                    while (true) {
                        int i3 = i2;
                        if (i3 >= b2.size()) {
                            sb = sb4;
                            break;
                        } else {
                            dz dzVar2 = b2.get(i3);
                            sb2.append(dzVar2.c).append(",");
                            sb2.append(dzVar2.d).append(",");
                            sb2.append(dzVar2.j);
                            if (i3 < b2.size() - 1) {
                                sb2.append("*");
                            }
                            i2 = i3 + 1;
                        }
                    }
                case 2:
                    dz dzVar3 = b2.get(0);
                    sb3.delete(0, sb3.length());
                    sb3.append("<mcc>").append(dzVar3.a).append("</mcc>");
                    sb3.append("<sid>").append(dzVar3.g).append("</sid>");
                    sb3.append("<nid>").append(dzVar3.h).append("</nid>");
                    sb3.append("<bid>").append(dzVar3.i).append("</bid>");
                    if (dzVar3.f > 0 && dzVar3.e > 0) {
                        sb3.append("<lon>").append(dzVar3.f).append("</lon>");
                        sb3.append("<lat>").append(dzVar3.e).append("</lat>");
                    }
                    sb3.append("<signal>").append(dzVar3.j).append("</signal>");
                    sb = sb3.toString();
                    break;
                default:
                    sb = "";
                    break;
            }
            sb3.delete(0, sb3.length());
            str4 = sb;
        } else {
            str4 = "";
        }
        if ((e2 & 4) != 4 || c2.isEmpty()) {
            this.C.clear();
        } else {
            this.C.clear();
            this.C.addAll(c2);
        }
        StringBuilder sb5 = new StringBuilder();
        if (ebVar.q) {
            if (a2) {
                WifiInfo k2 = ebVar.k();
                if (eb.a(k2)) {
                    sb5.append(k2.getBSSID()).append(",");
                    int rssi = k2.getRssi();
                    if (rssi < -128) {
                        rssi = 0;
                    } else if (rssi > 127) {
                        rssi = 0;
                    }
                    sb5.append(rssi).append(",");
                    String ssid = k2.getSSID();
                    int i4 = 32;
                    try {
                        i4 = k2.getSSID().getBytes("UTF-8").length;
                    } catch (Exception e5) {
                    }
                    if (i4 >= 32) {
                        ssid = "unkwn";
                    }
                    sb5.append(ssid.replace("*", "."));
                }
            }
            if (!(e3 == null || this.F == null)) {
                this.F.clear();
                this.F.addAll(e3);
            }
        } else {
            ebVar.g();
            if (this.F != null) {
                this.F.clear();
            }
        }
        this.b = 0;
        if (!z2) {
            this.b = (short) (this.b | 2);
        }
        this.c = str2;
        this.d = str3;
        this.f = Build.MODEL;
        this.g = DispatchConstants.ANDROID + Build.VERSION.RELEASE;
        this.h = et.b(context);
        this.i = str7;
        this.j = "0";
        this.k = "0";
        this.l = "0";
        this.m = "0";
        this.n = "0";
        this.o = f2;
        this.p = en.e;
        this.q = en.f;
        this.s = String.valueOf(d2);
        this.t = et.j(context);
        this.v = BuildConfig.VERSION_NAME;
        this.w = null;
        this.u = "";
        this.x = str5;
        this.y = str6;
        this.z = e2;
        this.A = str4;
        this.B = sb2.toString();
        this.D = eaVar.k();
        this.G = eb.o();
        this.E = sb5.toString();
        try {
            if (TextUtils.isEmpty(J)) {
                J = n.h(context);
            }
        } catch (Throwable th4) {
        }
        try {
            if (TextUtils.isEmpty(L)) {
                L = n.a(context);
            }
        } catch (Throwable th5) {
        }
        try {
            if (TextUtils.isEmpty(this.N)) {
                this.N = n.i(context);
            }
        } catch (Throwable th6) {
        }
        sb2.delete(0, sb2.length());
        sb5.delete(0, sb5.length());
    }

    /* JADX WARNING: Removed duplicated region for block: B:149:0x05b9  */
    /* JADX WARNING: Removed duplicated region for block: B:152:0x05ce  */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x05e8 A[Catch:{ Throwable -> 0x081c }] */
    /* JADX WARNING: Removed duplicated region for block: B:159:0x05eb A[Catch:{ Throwable -> 0x081c }] */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x0600 A[Catch:{ Throwable -> 0x0824 }] */
    /* JADX WARNING: Removed duplicated region for block: B:169:0x0615  */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x0652  */
    /* JADX WARNING: Removed duplicated region for block: B:186:0x0665  */
    /* JADX WARNING: Removed duplicated region for block: B:189:0x0689  */
    /* JADX WARNING: Removed duplicated region for block: B:192:0x06bd  */
    /* JADX WARNING: Removed duplicated region for block: B:216:0x0754  */
    /* JADX WARNING: Removed duplicated region for block: B:247:0x080d A[SYNTHETIC, Splitter:B:247:0x080d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final byte[] a() {
        /*
            r19 = this;
            r2 = 28
            java.lang.String[] r3 = new java.lang.String[r2]
            r2 = 0
            r0 = r19
            java.lang.String r4 = r0.a
            r3[r2] = r4
            r2 = 1
            r0 = r19
            java.lang.String r4 = r0.c
            r3[r2] = r4
            r2 = 2
            r0 = r19
            java.lang.String r4 = r0.d
            r3[r2] = r4
            r2 = 3
            r0 = r19
            java.lang.String r4 = r0.e
            r3[r2] = r4
            r2 = 4
            r0 = r19
            java.lang.String r4 = r0.f
            r3[r2] = r4
            r2 = 5
            r0 = r19
            java.lang.String r4 = r0.g
            r3[r2] = r4
            r2 = 6
            r0 = r19
            java.lang.String r4 = r0.h
            r3[r2] = r4
            r2 = 7
            r0 = r19
            java.lang.String r4 = r0.i
            r3[r2] = r4
            r2 = 8
            r0 = r19
            java.lang.String r4 = r0.l
            r3[r2] = r4
            r2 = 9
            r0 = r19
            java.lang.String r4 = r0.m
            r3[r2] = r4
            r2 = 10
            r0 = r19
            java.lang.String r4 = r0.n
            r3[r2] = r4
            r2 = 11
            r0 = r19
            java.lang.String r4 = r0.o
            r3[r2] = r4
            r2 = 12
            r0 = r19
            java.lang.String r4 = r0.p
            r3[r2] = r4
            r2 = 13
            r0 = r19
            java.lang.String r4 = r0.q
            r3[r2] = r4
            r2 = 14
            r0 = r19
            java.lang.String r4 = r0.r
            r3[r2] = r4
            r2 = 15
            r0 = r19
            java.lang.String r4 = r0.s
            r3[r2] = r4
            r2 = 16
            r0 = r19
            java.lang.String r4 = r0.t
            r3[r2] = r4
            r2 = 17
            r0 = r19
            java.lang.String r4 = r0.u
            r3[r2] = r4
            r2 = 18
            r0 = r19
            java.lang.String r4 = r0.v
            r3[r2] = r4
            r2 = 19
            r0 = r19
            java.lang.String r4 = r0.w
            r3[r2] = r4
            r2 = 20
            r0 = r19
            java.lang.String r4 = r0.x
            r3[r2] = r4
            r2 = 21
            r0 = r19
            java.lang.String r4 = r0.A
            r3[r2] = r4
            r2 = 22
            r0 = r19
            java.lang.String r4 = r0.B
            r3[r2] = r4
            r2 = 23
            r0 = r19
            java.lang.String r4 = r0.E
            r3[r2] = r4
            r2 = 24
            r0 = r19
            java.lang.String r4 = r0.G
            r3[r2] = r4
            r2 = 25
            r0 = r19
            java.lang.String r4 = r0.H
            r3[r2] = r4
            r2 = 26
            java.lang.String r4 = J
            r3[r2] = r4
            r2 = 27
            r0 = r19
            java.lang.String r4 = r0.N
            r3[r2] = r4
            r2 = 0
        L_0x00db:
            r4 = 28
            if (r2 >= r4) goto L_0x00ef
            r4 = r3[r2]
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 == 0) goto L_0x00ec
            java.lang.String r4 = ""
            r3[r2] = r4
        L_0x00ec:
            int r2 = r2 + 1
            goto L_0x00db
        L_0x00ef:
            r0 = r19
            java.lang.String r2 = r0.j
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x03bf
            java.lang.String r2 = "0"
            r0 = r19
            r0.j = r2
        L_0x0100:
            r0 = r19
            java.lang.String r2 = r0.k
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x03e2
            java.lang.String r2 = "0"
            r0 = r19
            r0.k = r2
        L_0x0111:
            r0 = r19
            java.lang.String r2 = r0.y
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x0405
            java.lang.String r2 = "0"
            r0 = r19
            r0.y = r2
        L_0x0122:
            r0 = r19
            int r2 = r0.z
            boolean r2 = com.loc.ea.a((int) r2)
            if (r2 != 0) goto L_0x0131
            r2 = 0
            r0 = r19
            r0.z = r2
        L_0x0131:
            r0 = r19
            byte[] r2 = r0.I
            if (r2 != 0) goto L_0x013e
            r2 = 0
            byte[] r2 = new byte[r2]
            r0 = r19
            r0.I = r2
        L_0x013e:
            r2 = 2
            byte[] r10 = new byte[r2]
            r2 = 4
            byte[] r8 = new byte[r2]
            r2 = 4096(0x1000, float:5.74E-42)
            r0 = r19
            byte[] r3 = r0.I
            if (r3 == 0) goto L_0x0155
            r0 = r19
            byte[] r2 = r0.I
            int r2 = r2.length
            int r2 = r2 + 1
            int r2 = r2 + 4096
        L_0x0155:
            r0 = r19
            byte[] r3 = r0.O
            if (r3 == 0) goto L_0x0161
            r0 = r19
            int r3 = r0.P
            if (r2 <= r3) goto L_0x0428
        L_0x0161:
            byte[] r3 = new byte[r2]
            r0 = r19
            r0.O = r3
            r0 = r19
            r0.P = r2
        L_0x016b:
            r2 = 0
            r0 = r19
            java.lang.String r4 = r0.a
            byte r4 = com.loc.et.i((java.lang.String) r4)
            r3[r2] = r4
            r0 = r19
            short r2 = r0.b
            r4 = 0
            byte[] r2 = com.loc.et.a((int) r2, (byte[]) r4)
            r4 = 0
            r5 = 1
            int r6 = r2.length
            java.lang.System.arraycopy(r2, r4, r3, r5, r6)
            int r2 = r2.length
            int r2 = r2 + 1
            r0 = r19
            java.lang.String r4 = r0.c
            int r2 = a(r4, r3, r2)
            r0 = r19
            java.lang.String r4 = r0.d
            int r2 = a(r4, r3, r2)
            r0 = r19
            java.lang.String r4 = r0.o
            int r2 = a(r4, r3, r2)
            r0 = r19
            java.lang.String r4 = r0.e
            int r2 = a(r4, r3, r2)
            r0 = r19
            java.lang.String r4 = r0.f
            int r2 = a(r4, r3, r2)
            r0 = r19
            java.lang.String r4 = r0.g
            int r2 = a(r4, r3, r2)
            r0 = r19
            java.lang.String r4 = r0.u
            int r2 = a(r4, r3, r2)
            r0 = r19
            java.lang.String r4 = r0.h
            int r2 = a(r4, r3, r2)
            r0 = r19
            java.lang.String r4 = r0.p
            int r2 = a(r4, r3, r2)
            r0 = r19
            java.lang.String r4 = r0.q
            int r4 = a(r4, r3, r2)
            r0 = r19
            java.lang.String r2 = r0.t     // Catch:{ Throwable -> 0x0448 }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x0448 }
            if (r2 == 0) goto L_0x042f
            r2 = 0
            r3[r4] = r2     // Catch:{ Throwable -> 0x0448 }
            int r2 = r4 + 1
        L_0x01e7:
            r0 = r19
            java.lang.String r4 = r0.v
            int r2 = a(r4, r3, r2)
            r0 = r19
            java.lang.String r4 = r0.w
            int r2 = a(r4, r3, r2)
            java.lang.String r4 = J
            int r2 = a(r4, r3, r2)
            java.lang.String r4 = L
            int r2 = a(r4, r3, r2)
            r0 = r19
            java.lang.String r4 = r0.x
            int r2 = a(r4, r3, r2)
            r0 = r19
            java.lang.String r4 = r0.y
            byte r4 = java.lang.Byte.parseByte(r4)
            r3[r2] = r4
            int r2 = r2 + 1
            r0 = r19
            java.lang.String r4 = r0.j
            byte r4 = java.lang.Byte.parseByte(r4)
            r3[r2] = r4
            int r2 = r2 + 1
            r0 = r19
            int r4 = r0.z
            r5 = r4 & 3
            r0 = r19
            int r4 = r0.z
            byte r4 = (byte) r4
            r3[r2] = r4
            int r2 = r2 + 1
            r4 = 1
            if (r5 == r4) goto L_0x0238
            r4 = 2
            if (r5 != r4) goto L_0x02c0
        L_0x0238:
            java.lang.String r4 = "mcc"
            r0 = r19
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.et.b((java.lang.String) r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            r4 = 1
            if (r5 != r4) goto L_0x0459
            java.lang.String r4 = "mnc"
            r0 = r19
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.et.b((java.lang.String) r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            java.lang.String r4 = "lac"
            r0 = r19
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.et.b((java.lang.String) r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            java.lang.String r4 = "cellid"
            r0 = r19
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.et.c((java.lang.String) r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
        L_0x028b:
            java.lang.String r4 = "signal"
            r0 = r19
            java.lang.String r4 = r0.b(r4)
            int r4 = java.lang.Integer.parseInt(r4)
            r6 = 127(0x7f, float:1.78E-43)
            if (r4 <= r6) goto L_0x04c2
            r4 = 0
        L_0x029d:
            byte r4 = (byte) r4
            r3[r2] = r4
            int r2 = r2 + 1
            r4 = 0
            byte[] r4 = com.loc.et.a((int) r4, (byte[]) r10)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r2 = r2 + 2
            r4 = 1
            if (r5 != r4) goto L_0x052c
            r0 = r19
            java.lang.String r4 = r0.B
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 == 0) goto L_0x04c9
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
        L_0x02c0:
            r0 = r19
            java.lang.String r4 = r0.D
            if (r4 == 0) goto L_0x0537
            r0 = r19
            int r5 = r0.z
            r5 = r5 & 8
            r6 = 8
            if (r5 != r6) goto L_0x0537
            java.lang.String r5 = "GBK"
            byte[] r4 = r4.getBytes(r5)     // Catch:{ Exception -> 0x0536 }
            int r5 = r4.length     // Catch:{ Exception -> 0x0536 }
            r6 = 60
            int r5 = java.lang.Math.min(r5, r6)     // Catch:{ Exception -> 0x0536 }
            byte r6 = (byte) r5     // Catch:{ Exception -> 0x0536 }
            r3[r2] = r6     // Catch:{ Exception -> 0x0536 }
            int r2 = r2 + 1
            r6 = 0
            java.lang.System.arraycopy(r4, r6, r3, r2, r5)     // Catch:{ Exception -> 0x0536 }
            int r2 = r2 + r5
            r4 = r2
        L_0x02e9:
            r0 = r19
            java.util.ArrayList<com.loc.dz> r9 = r0.C
            int r5 = r9.size()
            r0 = r19
            int r2 = r0.z
            r2 = r2 & 4
            r6 = 4
            if (r2 != r6) goto L_0x05aa
            if (r5 <= 0) goto L_0x05aa
            r2 = 0
            java.lang.Object r2 = r9.get(r2)
            com.loc.dz r2 = (com.loc.dz) r2
            boolean r2 = r2.p
            if (r2 != 0) goto L_0x030a
            int r2 = r5 + -1
            r5 = r2
        L_0x030a:
            byte r2 = (byte) r5
            r3[r4] = r2
            int r4 = r4 + 1
            r2 = 0
            r7 = r2
        L_0x0311:
            if (r7 >= r5) goto L_0x05af
            java.lang.Object r2 = r9.get(r7)
            com.loc.dz r2 = (com.loc.dz) r2
            boolean r6 = r2.p
            if (r6 == 0) goto L_0x03ba
            int r6 = r2.k
            r11 = 1
            if (r6 == r11) goto L_0x032c
            int r6 = r2.k
            r11 = 3
            if (r6 == r11) goto L_0x032c
            int r6 = r2.k
            r11 = 4
            if (r6 != r11) goto L_0x053f
        L_0x032c:
            int r6 = r2.k
            byte r6 = (byte) r6
            boolean r11 = r2.n
            if (r11 == 0) goto L_0x0336
            r6 = r6 | 8
            byte r6 = (byte) r6
        L_0x0336:
            r3[r4] = r6
            int r4 = r4 + 1
            int r6 = r2.a
            byte[] r6 = com.loc.et.a((int) r6, (byte[]) r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.b
            byte[] r6 = com.loc.et.a((int) r6, (byte[]) r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.c
            byte[] r6 = com.loc.et.a((int) r6, (byte[]) r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.d
            byte[] r6 = com.loc.et.b((int) r6, (byte[]) r8)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
        L_0x036e:
            int r6 = r2.j
            r11 = 127(0x7f, float:1.78E-43)
            if (r6 <= r11) goto L_0x05a2
            r6 = 99
        L_0x0376:
            byte r6 = (byte) r6
            r3[r4] = r6
            int r4 = r4 + 1
            short r6 = r2.l
            byte[] r6 = com.loc.et.a((int) r6, (byte[]) r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            java.lang.String r6 = "5.1"
            java.lang.Double r6 = java.lang.Double.valueOf(r6)
            double r12 = r6.doubleValue()
            r14 = 4617315517961601024(0x4014000000000000, double:5.0)
            int r6 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
            if (r6 < 0) goto L_0x03ba
            int r6 = r2.k
            r11 = 3
            if (r6 == r11) goto L_0x03a3
            int r6 = r2.k
            r11 = 4
            if (r6 != r11) goto L_0x03ba
        L_0x03a3:
            int r2 = r2.o
            r6 = 32767(0x7fff, float:4.5916E-41)
            if (r2 <= r6) goto L_0x03ab
            r2 = 32767(0x7fff, float:4.5916E-41)
        L_0x03ab:
            if (r2 >= 0) goto L_0x03af
            r2 = 32767(0x7fff, float:4.5916E-41)
        L_0x03af:
            byte[] r2 = com.loc.et.a((int) r2, (byte[]) r10)
            r6 = 0
            int r11 = r2.length
            java.lang.System.arraycopy(r2, r6, r3, r4, r11)
            int r2 = r2.length
            int r4 = r4 + r2
        L_0x03ba:
            int r2 = r7 + 1
            r7 = r2
            goto L_0x0311
        L_0x03bf:
            java.lang.String r2 = "0"
            r0 = r19
            java.lang.String r3 = r0.j
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0100
            java.lang.String r2 = "2"
            r0 = r19
            java.lang.String r3 = r0.j
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0100
            java.lang.String r2 = "0"
            r0 = r19
            r0.j = r2
            goto L_0x0100
        L_0x03e2:
            java.lang.String r2 = "0"
            r0 = r19
            java.lang.String r3 = r0.k
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0111
            java.lang.String r2 = "1"
            r0 = r19
            java.lang.String r3 = r0.k
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0111
            java.lang.String r2 = "0"
            r0 = r19
            r0.k = r2
            goto L_0x0111
        L_0x0405:
            java.lang.String r2 = "1"
            r0 = r19
            java.lang.String r3 = r0.y
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0122
            java.lang.String r2 = "2"
            r0 = r19
            java.lang.String r3 = r0.y
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0122
            java.lang.String r2 = "0"
            r0 = r19
            r0.y = r2
            goto L_0x0122
        L_0x0428:
            r0 = r19
            byte[] r2 = r0.O
            r3 = r2
            goto L_0x016b
        L_0x042f:
            r0 = r19
            java.lang.String r2 = r0.t     // Catch:{ Throwable -> 0x0448 }
            r0 = r19
            byte[] r2 = r0.a(r2)     // Catch:{ Throwable -> 0x0448 }
            int r5 = r2.length     // Catch:{ Throwable -> 0x0448 }
            byte r5 = (byte) r5     // Catch:{ Throwable -> 0x0448 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x0448 }
            int r4 = r4 + 1
            r5 = 0
            int r6 = r2.length     // Catch:{ Throwable -> 0x0448 }
            java.lang.System.arraycopy(r2, r5, r3, r4, r6)     // Catch:{ Throwable -> 0x0448 }
            int r2 = r2.length     // Catch:{ Throwable -> 0x0448 }
            int r2 = r2 + r4
            goto L_0x01e7
        L_0x0448:
            r2 = move-exception
            java.lang.String r5 = "Req"
            java.lang.String r6 = "buildV4Dot219"
            com.loc.en.a(r2, r5, r6)
            r2 = 0
            r3[r4] = r2
            int r2 = r4 + 1
            goto L_0x01e7
        L_0x0459:
            r4 = 2
            if (r5 != r4) goto L_0x028b
            java.lang.String r4 = "sid"
            r0 = r19
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.et.b((java.lang.String) r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            java.lang.String r4 = "nid"
            r0 = r19
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.et.b((java.lang.String) r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            java.lang.String r4 = "bid"
            r0 = r19
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.et.b((java.lang.String) r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            java.lang.String r4 = "lon"
            r0 = r19
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.et.c((java.lang.String) r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            java.lang.String r4 = "lat"
            r0 = r19
            java.lang.String r4 = r0.b(r4)
            byte[] r4 = com.loc.et.c((java.lang.String) r4)
            r6 = 0
            int r7 = r4.length
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)
            int r4 = r4.length
            int r2 = r2 + r4
            goto L_0x028b
        L_0x04c2:
            r6 = -128(0xffffffffffffff80, float:NaN)
            if (r4 >= r6) goto L_0x029d
            r4 = 0
            goto L_0x029d
        L_0x04c9:
            r0 = r19
            java.lang.String r4 = r0.B
            java.lang.String r5 = "\\*"
            java.lang.String[] r4 = r4.split(r5)
            int r6 = r4.length
            byte r4 = (byte) r6
            r3[r2] = r4
            int r4 = r2 + 1
            r2 = 0
            r18 = r2
            r2 = r4
            r4 = r18
        L_0x04e0:
            if (r4 >= r6) goto L_0x02c0
            java.lang.String r5 = "lac"
            r0 = r19
            java.lang.String r5 = r0.a(r5, r4)
            byte[] r5 = com.loc.et.b((java.lang.String) r5)
            r7 = 0
            int r9 = r5.length
            java.lang.System.arraycopy(r5, r7, r3, r2, r9)
            int r5 = r5.length
            int r2 = r2 + r5
            java.lang.String r5 = "cellid"
            r0 = r19
            java.lang.String r5 = r0.a(r5, r4)
            byte[] r5 = com.loc.et.c((java.lang.String) r5)
            r7 = 0
            int r9 = r5.length
            java.lang.System.arraycopy(r5, r7, r3, r2, r9)
            int r5 = r5.length
            int r5 = r5 + r2
            java.lang.String r2 = "signal"
            r0 = r19
            java.lang.String r2 = r0.a(r2, r4)
            int r2 = java.lang.Integer.parseInt(r2)
            r7 = 127(0x7f, float:1.78E-43)
            if (r2 <= r7) goto L_0x0526
            r2 = 0
        L_0x051c:
            byte r2 = (byte) r2
            r3[r5] = r2
            int r5 = r5 + 1
            int r2 = r4 + 1
            r4 = r2
            r2 = r5
            goto L_0x04e0
        L_0x0526:
            r7 = -128(0xffffffffffffff80, float:NaN)
            if (r2 >= r7) goto L_0x051c
            r2 = 0
            goto L_0x051c
        L_0x052c:
            r4 = 2
            if (r5 != r4) goto L_0x02c0
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
            goto L_0x02c0
        L_0x0536:
            r4 = move-exception
        L_0x0537:
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
            r4 = r2
            goto L_0x02e9
        L_0x053f:
            int r6 = r2.k
            r11 = 2
            if (r6 != r11) goto L_0x036e
            int r6 = r2.k
            byte r6 = (byte) r6
            boolean r11 = r2.n
            if (r11 == 0) goto L_0x054e
            r6 = r6 | 8
            byte r6 = (byte) r6
        L_0x054e:
            r3[r4] = r6
            int r4 = r4 + 1
            int r6 = r2.a
            byte[] r6 = com.loc.et.a((int) r6, (byte[]) r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.g
            byte[] r6 = com.loc.et.a((int) r6, (byte[]) r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.h
            byte[] r6 = com.loc.et.a((int) r6, (byte[]) r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.i
            byte[] r6 = com.loc.et.a((int) r6, (byte[]) r10)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.f
            byte[] r6 = com.loc.et.b((int) r6, (byte[]) r8)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            int r6 = r2.e
            byte[] r6 = com.loc.et.b((int) r6, (byte[]) r8)
            r11 = 0
            int r12 = r6.length
            java.lang.System.arraycopy(r6, r11, r3, r4, r12)
            int r6 = r6.length
            int r4 = r4 + r6
            goto L_0x036e
        L_0x05a2:
            r11 = -128(0xffffffffffffff80, float:NaN)
            if (r6 >= r11) goto L_0x0376
            r6 = 99
            goto L_0x0376
        L_0x05aa:
            r2 = 0
            r3[r4] = r2
            int r4 = r4 + 1
        L_0x05af:
            r0 = r19
            java.lang.String r2 = r0.E
            int r2 = r2.length()
            if (r2 != 0) goto L_0x06bd
            r2 = 0
            r3[r4] = r2
            int r2 = r4 + 1
        L_0x05be:
            r0 = r19
            java.util.ArrayList<android.net.wifi.ScanResult> r11 = r0.F
            int r4 = r11.size()
            r5 = 25
            int r12 = java.lang.Math.min(r4, r5)
            if (r12 != 0) goto L_0x0754
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
        L_0x05d3:
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
            r0 = r19
            java.lang.String r4 = r0.H     // Catch:{ Throwable -> 0x081c }
            java.lang.String r5 = "GBK"
            byte[] r4 = r4.getBytes(r5)     // Catch:{ Throwable -> 0x081c }
            int r5 = r4.length     // Catch:{ Throwable -> 0x081c }
            r6 = 127(0x7f, float:1.78E-43)
            if (r5 <= r6) goto L_0x05e9
            r4 = 0
        L_0x05e9:
            if (r4 != 0) goto L_0x080d
            r4 = 0
            r3[r2] = r4     // Catch:{ Throwable -> 0x081c }
            int r2 = r2 + 1
        L_0x05f0:
            r4 = 2
            byte[] r4 = new byte[r4]
            r4 = {0, 0} // fill-array
            r0 = r19
            java.lang.String r5 = r0.K     // Catch:{ Throwable -> 0x0824 }
            boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x0824 }
            if (r5 != 0) goto L_0x060c
            r0 = r19
            java.lang.String r4 = r0.K     // Catch:{ Throwable -> 0x0824 }
            int r4 = r4.length()     // Catch:{ Throwable -> 0x0824 }
            byte[] r4 = com.loc.et.a((int) r4, (byte[]) r10)     // Catch:{ Throwable -> 0x0824 }
        L_0x060c:
            r6 = 0
            r7 = 2
            java.lang.System.arraycopy(r4, r6, r3, r2, r7)     // Catch:{ Throwable -> 0x0824 }
            int r2 = r2 + 2
            if (r5 != 0) goto L_0x0627
            r0 = r19
            java.lang.String r4 = r0.K     // Catch:{ Throwable -> 0x0833 }
            java.lang.String r5 = "GBK"
            byte[] r4 = r4.getBytes(r5)     // Catch:{ Throwable -> 0x0833 }
            r5 = 0
            int r6 = r4.length     // Catch:{ Throwable -> 0x0833 }
            java.lang.System.arraycopy(r4, r5, r3, r2, r6)     // Catch:{ Throwable -> 0x0833 }
            int r4 = r4.length     // Catch:{ Throwable -> 0x0833 }
            int r2 = r2 + r4
        L_0x0627:
            r4 = 2
            byte[] r4 = new byte[r4]
            r5 = 0
            r6 = 0
            r4[r5] = r6
            r5 = 1
            r6 = 0
            r4[r5] = r6
            r4 = 0
            byte[] r4 = com.loc.et.a((int) r4, (byte[]) r10)     // Catch:{ Throwable -> 0x0829 }
            r5 = 0
            r6 = 2
            java.lang.System.arraycopy(r4, r5, r3, r2, r6)     // Catch:{ Throwable -> 0x0829 }
            int r2 = r2 + 2
        L_0x063e:
            r4 = 2
            byte[] r4 = new byte[r4]
            r4 = {0, 0} // fill-array
            r5 = 0
            r6 = 2
            java.lang.System.arraycopy(r4, r5, r3, r2, r6)     // Catch:{ Throwable -> 0x082e }
            int r2 = r2 + 2
        L_0x064b:
            r4 = 0
            r0 = r19
            byte[] r5 = r0.I
            if (r5 == 0) goto L_0x0657
            r0 = r19
            byte[] r4 = r0.I
            int r4 = r4.length
        L_0x0657:
            r5 = 0
            byte[] r5 = com.loc.et.a((int) r4, (byte[]) r5)
            r6 = 0
            int r7 = r5.length
            java.lang.System.arraycopy(r5, r6, r3, r2, r7)
            int r5 = r5.length
            int r2 = r2 + r5
            if (r4 <= 0) goto L_0x0678
            r0 = r19
            byte[] r4 = r0.I
            r5 = 0
            r0 = r19
            byte[] r6 = r0.I
            int r6 = r6.length
            java.lang.System.arraycopy(r4, r5, r3, r2, r6)
            r0 = r19
            byte[] r4 = r0.I
            int r4 = r4.length
            int r2 = r2 + r4
        L_0x0678:
            java.lang.String r4 = "5.1"
            java.lang.Double r4 = java.lang.Double.valueOf(r4)
            double r4 = r4.doubleValue()
            r6 = 4617315517961601024(0x4014000000000000, double:5.0)
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 < 0) goto L_0x0696
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
            r0 = r19
            java.lang.String r4 = r0.N
            int r2 = a(r4, r3, r2)
        L_0x0696:
            byte[] r4 = new byte[r2]
            r5 = 0
            r6 = 0
            java.lang.System.arraycopy(r3, r5, r4, r6, r2)
            java.util.zip.CRC32 r3 = new java.util.zip.CRC32
            r3.<init>()
            r3.update(r4)
            long r6 = r3.getValue()
            byte[] r3 = com.loc.et.a((long) r6)
            int r5 = r2 + 8
            byte[] r5 = new byte[r5]
            r6 = 0
            r7 = 0
            java.lang.System.arraycopy(r4, r6, r5, r7, r2)
            r4 = 0
            r6 = 8
            java.lang.System.arraycopy(r3, r4, r5, r2, r6)
            return r5
        L_0x06bd:
            r2 = 1
            r3[r4] = r2
            int r4 = r4 + 1
            r0 = r19
            java.lang.String r2 = r0.E     // Catch:{ Throwable -> 0x0728 }
            java.lang.String r5 = ","
            java.lang.String[] r5 = r2.split(r5)     // Catch:{ Throwable -> 0x0728 }
            r2 = 0
            r2 = r5[r2]     // Catch:{ Throwable -> 0x0728 }
            r0 = r19
            byte[] r2 = r0.a(r2)     // Catch:{ Throwable -> 0x0728 }
            r6 = 0
            int r7 = r2.length     // Catch:{ Throwable -> 0x0728 }
            java.lang.System.arraycopy(r2, r6, r3, r4, r7)     // Catch:{ Throwable -> 0x0728 }
            int r2 = r2.length     // Catch:{ Throwable -> 0x0728 }
            int r4 = r4 + r2
            r2 = 2
            r2 = r5[r2]     // Catch:{ Throwable -> 0x0712 }
            java.lang.String r6 = "GBK"
            byte[] r6 = r2.getBytes(r6)     // Catch:{ Throwable -> 0x0712 }
            int r2 = r6.length     // Catch:{ Throwable -> 0x0712 }
            r7 = 127(0x7f, float:1.78E-43)
            if (r2 <= r7) goto L_0x06ee
            r2 = 127(0x7f, float:1.78E-43)
        L_0x06ee:
            byte r7 = (byte) r2     // Catch:{ Throwable -> 0x0712 }
            r3[r4] = r7     // Catch:{ Throwable -> 0x0712 }
            int r4 = r4 + 1
            r7 = 0
            java.lang.System.arraycopy(r6, r7, r3, r4, r2)     // Catch:{ Throwable -> 0x0712 }
            int r4 = r4 + r2
        L_0x06f8:
            r2 = 1
            r2 = r5[r2]     // Catch:{ Throwable -> 0x0728 }
            int r2 = java.lang.Integer.parseInt(r2)     // Catch:{ Throwable -> 0x0728 }
            r5 = 127(0x7f, float:1.78E-43)
            if (r2 <= r5) goto L_0x0722
            r2 = 0
        L_0x0704:
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Throwable -> 0x0728 }
            byte r2 = java.lang.Byte.parseByte(r2)     // Catch:{ Throwable -> 0x0728 }
            r3[r4] = r2     // Catch:{ Throwable -> 0x0728 }
            int r2 = r4 + 1
            goto L_0x05be
        L_0x0712:
            r2 = move-exception
            java.lang.String r6 = "Req"
            java.lang.String r7 = "buildV4Dot214"
            com.loc.en.a(r2, r6, r7)     // Catch:{ Throwable -> 0x0728 }
            r2 = 0
            r3[r4] = r2     // Catch:{ Throwable -> 0x0728 }
            int r4 = r4 + 1
            goto L_0x06f8
        L_0x0722:
            r5 = -128(0xffffffffffffff80, float:NaN)
            if (r2 >= r5) goto L_0x0704
            r2 = 0
            goto L_0x0704
        L_0x0728:
            r2 = move-exception
            java.lang.String r5 = "Req"
            java.lang.String r6 = "buildV4Dot216"
            com.loc.en.a(r2, r5, r6)
            java.lang.String r2 = "00:00:00:00:00:00"
            r0 = r19
            byte[] r2 = r0.a(r2)
            r5 = 0
            int r6 = r2.length
            java.lang.System.arraycopy(r2, r5, r3, r4, r6)
            int r2 = r2.length
            int r2 = r2 + r4
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
            java.lang.String r4 = "0"
            byte r4 = java.lang.Byte.parseByte(r4)
            r3[r2] = r4
            int r2 = r2 + 1
            goto L_0x05be
        L_0x0754:
            byte r4 = (byte) r12
            r3[r2] = r4
            int r6 = r2 + 1
            int r2 = com.loc.et.c()
            r4 = 17
            if (r2 < r4) goto L_0x07e7
            r2 = 1
            r9 = r2
        L_0x0763:
            r4 = 0
            if (r9 == 0) goto L_0x076e
            long r4 = com.loc.et.b()
            r14 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 / r14
        L_0x076e:
            r2 = 0
            r8 = r2
        L_0x0770:
            if (r8 >= r12) goto L_0x07f8
            java.lang.Object r2 = r11.get(r8)
            android.net.wifi.ScanResult r2 = (android.net.wifi.ScanResult) r2
            java.lang.String r7 = r2.BSSID
            r0 = r19
            byte[] r7 = r0.a(r7)
            r13 = 0
            int r14 = r7.length
            java.lang.System.arraycopy(r7, r13, r3, r6, r14)
            int r7 = r7.length
            int r6 = r6 + r7
            java.lang.String r7 = r2.SSID     // Catch:{ Exception -> 0x07eb }
            java.lang.String r13 = "GBK"
            byte[] r7 = r7.getBytes(r13)     // Catch:{ Exception -> 0x07eb }
            int r13 = r7.length     // Catch:{ Exception -> 0x07eb }
            byte r13 = (byte) r13     // Catch:{ Exception -> 0x07eb }
            r3[r6] = r13     // Catch:{ Exception -> 0x07eb }
            int r6 = r6 + 1
            r13 = 0
            int r14 = r7.length     // Catch:{ Exception -> 0x07eb }
            java.lang.System.arraycopy(r7, r13, r3, r6, r14)     // Catch:{ Exception -> 0x07eb }
            int r7 = r7.length     // Catch:{ Exception -> 0x07eb }
            int r6 = r6 + r7
        L_0x079d:
            int r7 = r2.level
            r13 = 127(0x7f, float:1.78E-43)
            if (r7 <= r13) goto L_0x07f2
            r7 = 0
        L_0x07a4:
            java.lang.String r7 = java.lang.String.valueOf(r7)
            byte r7 = java.lang.Byte.parseByte(r7)
            r3[r6] = r7
            int r7 = r6 + 1
            if (r9 == 0) goto L_0x07c2
            long r14 = r2.timestamp
            r16 = 1000000(0xf4240, double:4.940656E-318)
            long r14 = r14 / r16
            r16 = 1
            long r14 = r14 + r16
            long r14 = r4 - r14
            int r6 = (int) r14
            if (r6 >= 0) goto L_0x07c3
        L_0x07c2:
            r6 = 0
        L_0x07c3:
            r13 = 65535(0xffff, float:9.1834E-41)
            if (r6 <= r13) goto L_0x07cb
            r6 = 65535(0xffff, float:9.1834E-41)
        L_0x07cb:
            byte[] r6 = com.loc.et.a((int) r6, (byte[]) r10)
            r13 = 0
            int r14 = r6.length
            java.lang.System.arraycopy(r6, r13, r3, r7, r14)
            int r6 = r6.length
            int r6 = r6 + r7
            int r2 = r2.frequency
            byte[] r2 = com.loc.et.a((int) r2, (byte[]) r10)
            r7 = 0
            int r13 = r2.length
            java.lang.System.arraycopy(r2, r7, r3, r6, r13)
            int r2 = r2.length
            int r6 = r6 + r2
            int r2 = r8 + 1
            r8 = r2
            goto L_0x0770
        L_0x07e7:
            r2 = 0
            r9 = r2
            goto L_0x0763
        L_0x07eb:
            r7 = move-exception
            r7 = 0
            r3[r6] = r7
            int r6 = r6 + 1
            goto L_0x079d
        L_0x07f2:
            r13 = -128(0xffffffffffffff80, float:NaN)
            if (r7 >= r13) goto L_0x07a4
            r7 = 0
            goto L_0x07a4
        L_0x07f8:
            r0 = r19
            java.lang.String r2 = r0.G
            int r2 = java.lang.Integer.parseInt(r2)
            byte[] r2 = com.loc.et.a((int) r2, (byte[]) r10)
            r4 = 0
            int r5 = r2.length
            java.lang.System.arraycopy(r2, r4, r3, r6, r5)
            int r2 = r2.length
            int r2 = r2 + r6
            goto L_0x05d3
        L_0x080d:
            int r5 = r4.length     // Catch:{ Throwable -> 0x081c }
            byte r5 = (byte) r5     // Catch:{ Throwable -> 0x081c }
            r3[r2] = r5     // Catch:{ Throwable -> 0x081c }
            int r2 = r2 + 1
            r5 = 0
            int r6 = r4.length     // Catch:{ Throwable -> 0x081c }
            java.lang.System.arraycopy(r4, r5, r3, r2, r6)     // Catch:{ Throwable -> 0x081c }
            int r4 = r4.length     // Catch:{ Throwable -> 0x081c }
            int r2 = r2 + r4
            goto L_0x05f0
        L_0x081c:
            r4 = move-exception
            r4 = 0
            r3[r2] = r4
            int r2 = r2 + 1
            goto L_0x05f0
        L_0x0824:
            r4 = move-exception
            int r2 = r2 + 2
            goto L_0x0627
        L_0x0829:
            r4 = move-exception
            int r2 = r2 + 2
            goto L_0x063e
        L_0x082e:
            r4 = move-exception
            int r2 = r2 + 2
            goto L_0x064b
        L_0x0833:
            r4 = move-exception
            goto L_0x0627
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.el.a():byte[]");
    }
}
