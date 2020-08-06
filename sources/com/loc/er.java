package com.loc;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.SparseArray;
import com.ali.user.open.tbauth.TbAuthConstants;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.bftv.fui.constantplugin.Constant;
import com.bumptech.glide.BuildConfig;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: ReportUtil */
public final class er {
    static AMapLocation g = null;
    static boolean h = false;
    private static List<bf> i = new ArrayList();
    private static JSONArray j = null;
    public SparseArray<Long> a = new SparseArray<>();
    public int b = -1;
    public long c = 0;
    String[] d = {"ol", "cl", "gl", "ha", "bs", "ds"};
    public int e = -1;
    public long f = -1;

    public static void a(long j2, long j3) {
        try {
            if (!h) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("gpsTime:").append(et.a(j2, "yyyy-MM-dd HH:mm:ss.SSS")).append(",");
                stringBuffer.append("sysTime:").append(et.a(j3, "yyyy-MM-dd HH:mm:ss.SSS")).append(",");
                long B = em.B();
                String str = "0";
                if (0 != B) {
                    str = et.a(B, "yyyy-MM-dd HH:mm:ss.SSS");
                }
                stringBuffer.append("serverTime:").append(str);
                a("checkgpstime", stringBuffer.toString());
                if (0 != B && Math.abs(j2 - B) < 31536000000L) {
                    stringBuffer.append(", correctError");
                    a("checkgpstimeerror", stringBuffer.toString());
                }
                stringBuffer.delete(0, stringBuffer.length());
                h = true;
            }
        } catch (Throwable th) {
        }
    }

    public static synchronized void a(Context context) {
        synchronized (er.class) {
            if (context != null) {
                try {
                    if (em.g()) {
                        if (i != null && i.size() > 0) {
                            ArrayList arrayList = new ArrayList();
                            arrayList.addAll(i);
                            bg.a((List<bf>) arrayList, context);
                            i.clear();
                        }
                        f(context);
                    }
                } catch (Throwable th) {
                    en.a(th, "ReportUtil", "destroy");
                }
            }
        }
        return;
    }

    public static void a(Context context, int i2, int i3, long j2, long j3) {
        if (i2 != -1 && i3 != -1 && context != null) {
            try {
                if (em.g()) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("param_int_first", i2);
                    jSONObject.put("param_int_second", i3);
                    jSONObject.put("param_long_first", j2);
                    jSONObject.put("param_long_second", j3);
                    a(context, "O012", jSONObject);
                }
            } catch (Throwable th) {
                en.a(th, "ReportUtil", "reportServiceAliveTime");
            }
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void a(android.content.Context r5, long r6, boolean r8) {
        /*
            if (r5 == 0) goto L_0x0008
            boolean r0 = com.loc.em.g()     // Catch:{ Throwable -> 0x005d }
            if (r0 != 0) goto L_0x0009
        L_0x0008:
            return
        L_0x0009:
            java.lang.Long r0 = java.lang.Long.valueOf(r6)     // Catch:{ Throwable -> 0x005d }
            int r1 = r0.intValue()     // Catch:{ Throwable -> 0x005d }
            java.lang.String r0 = "domestic"
            if (r8 != 0) goto L_0x0019
            java.lang.String r0 = "abroad"
        L_0x0019:
            java.lang.String r2 = "O015"
            if (r5 == 0) goto L_0x0008
            boolean r3 = com.loc.em.g()     // Catch:{ Throwable -> 0x0052 }
            if (r3 == 0) goto L_0x0008
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Throwable -> 0x0052 }
            r3.<init>()     // Catch:{ Throwable -> 0x0052 }
            boolean r4 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x0052 }
            if (r4 != 0) goto L_0x0035
            java.lang.String r4 = "param_string_first"
            r3.put(r4, r0)     // Catch:{ Throwable -> 0x0052 }
        L_0x0035:
            r0 = 0
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x0052 }
            if (r0 != 0) goto L_0x0043
            java.lang.String r0 = "param_string_second"
            r4 = 0
            r3.put(r0, r4)     // Catch:{ Throwable -> 0x0052 }
        L_0x0043:
            r0 = 2147483647(0x7fffffff, float:NaN)
            if (r1 == r0) goto L_0x004e
            java.lang.String r0 = "param_int_first"
            r3.put(r0, r1)     // Catch:{ Throwable -> 0x0052 }
        L_0x004e:
            a((android.content.Context) r5, (java.lang.String) r2, (org.json.JSONObject) r3)     // Catch:{ Throwable -> 0x0052 }
            goto L_0x0008
        L_0x0052:
            r0 = move-exception
            java.lang.String r1 = "ReportUtil"
            java.lang.String r2 = "applyStatisticsEx"
            com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x005d }
            goto L_0x0008
        L_0x005d:
            r0 = move-exception
            java.lang.String r1 = "ReportUtil"
            java.lang.String r2 = "reportGPSLocUseTime"
            com.loc.en.a(r0, r1, r2)
            goto L_0x0008
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.er.a(android.content.Context, long, boolean):void");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void a(android.content.Context r6, com.amap.api.location.AMapLocation r7) {
        /*
            r2 = 2
            r1 = 0
            r0 = 1
            java.lang.Class<com.loc.er> r3 = com.loc.er.class
            monitor-enter(r3)
            boolean r4 = com.loc.et.a((com.amap.api.location.AMapLocation) r7)     // Catch:{ Throwable -> 0x00c7 }
            if (r4 != 0) goto L_0x000e
        L_0x000c:
            monitor-exit(r3)
            return
        L_0x000e:
            int r4 = r7.getLocationType()     // Catch:{ Throwable -> 0x00c7 }
            switch(r4) {
                case 1: goto L_0x0016;
                case 2: goto L_0x00d6;
                case 3: goto L_0x0015;
                case 4: goto L_0x00d6;
                case 5: goto L_0x0015;
                case 6: goto L_0x0015;
                case 7: goto L_0x0015;
                case 8: goto L_0x00d9;
                case 9: goto L_0x00dc;
                default: goto L_0x0015;
            }     // Catch:{ Throwable -> 0x00c7 }
        L_0x0015:
            r0 = r1
        L_0x0016:
            if (r0 == 0) goto L_0x000c
            org.json.JSONArray r0 = j     // Catch:{ Throwable -> 0x00c7 }
            if (r0 != 0) goto L_0x0023
            org.json.JSONArray r0 = new org.json.JSONArray     // Catch:{ Throwable -> 0x00c7 }
            r0.<init>()     // Catch:{ Throwable -> 0x00c7 }
            j = r0     // Catch:{ Throwable -> 0x00c7 }
        L_0x0023:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Throwable -> 0x00c7 }
            r0.<init>()     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r2 = "lon"
            double r4 = r7.getLongitude()     // Catch:{ Throwable -> 0x00c7 }
            double r4 = com.loc.et.b((double) r4)     // Catch:{ Throwable -> 0x00c7 }
            r0.put(r2, r4)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r2 = "lat"
            double r4 = r7.getLatitude()     // Catch:{ Throwable -> 0x00c7 }
            double r4 = com.loc.et.b((double) r4)     // Catch:{ Throwable -> 0x00c7 }
            r0.put(r2, r4)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r2 = "type"
            r0.put(r2, r1)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r2 = "timestamp"
            long r4 = com.loc.et.a()     // Catch:{ Throwable -> 0x00c7 }
            r0.put(r2, r4)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r2 = r7.getCoordType()     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r4 = "WGS84"
            boolean r2 = r2.equalsIgnoreCase(r4)     // Catch:{ Throwable -> 0x00c7 }
            if (r2 == 0) goto L_0x00df
            java.lang.String r2 = "coordType"
            r4 = 1
            r0.put(r2, r4)     // Catch:{ Throwable -> 0x00c7 }
        L_0x0068:
            if (r1 != 0) goto L_0x00b0
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Throwable -> 0x00c7 }
            r1.<init>()     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r2 = "accuracy"
            float r4 = r7.getAccuracy()     // Catch:{ Throwable -> 0x00c7 }
            double r4 = (double) r4     // Catch:{ Throwable -> 0x00c7 }
            double r4 = com.loc.et.c((double) r4)     // Catch:{ Throwable -> 0x00c7 }
            r1.put(r2, r4)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r2 = "altitude"
            double r4 = r7.getAltitude()     // Catch:{ Throwable -> 0x00c7 }
            double r4 = com.loc.et.c((double) r4)     // Catch:{ Throwable -> 0x00c7 }
            r1.put(r2, r4)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r2 = "bearing"
            float r4 = r7.getBearing()     // Catch:{ Throwable -> 0x00c7 }
            double r4 = (double) r4     // Catch:{ Throwable -> 0x00c7 }
            double r4 = com.loc.et.c((double) r4)     // Catch:{ Throwable -> 0x00c7 }
            r1.put(r2, r4)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r2 = "speed"
            float r4 = r7.getSpeed()     // Catch:{ Throwable -> 0x00c7 }
            double r4 = (double) r4     // Catch:{ Throwable -> 0x00c7 }
            double r4 = com.loc.et.c((double) r4)     // Catch:{ Throwable -> 0x00c7 }
            r1.put(r2, r4)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r2 = "extension"
            r0.put(r2, r1)     // Catch:{ Throwable -> 0x00c7 }
        L_0x00b0:
            org.json.JSONArray r1 = j     // Catch:{ Throwable -> 0x00c7 }
            org.json.JSONArray r0 = r1.put(r0)     // Catch:{ Throwable -> 0x00c7 }
            j = r0     // Catch:{ Throwable -> 0x00c7 }
            int r0 = r0.length()     // Catch:{ Throwable -> 0x00c7 }
            int r1 = com.loc.em.h()     // Catch:{ Throwable -> 0x00c7 }
            if (r0 < r1) goto L_0x000c
            f(r6)     // Catch:{ Throwable -> 0x00c7 }
            goto L_0x000c
        L_0x00c7:
            r0 = move-exception
            java.lang.String r1 = "ReportUtil"
            java.lang.String r2 = "recordOfflineLocLog"
            com.loc.en.a(r0, r1, r2)     // Catch:{ all -> 0x00d3 }
            goto L_0x000c
        L_0x00d3:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        L_0x00d6:
            r1 = r0
            goto L_0x0016
        L_0x00d9:
            r1 = 3
            goto L_0x0016
        L_0x00dc:
            r1 = r2
            goto L_0x0016
        L_0x00df:
            java.lang.String r2 = "coordType"
            r4 = 2
            r0.put(r2, r4)     // Catch:{ Throwable -> 0x00c7 }
            goto L_0x0068
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.er.a(android.content.Context, com.amap.api.location.AMapLocation):void");
    }

    public static void a(Context context, AMapLocation aMapLocation, dq dqVar) {
        boolean z;
        String str;
        int i2 = 0;
        if (aMapLocation != null) {
            try {
                if (!"gps".equalsIgnoreCase(aMapLocation.getProvider()) && aMapLocation.getLocationType() != 1) {
                    if (et.a(aMapLocation)) {
                        if (!en.a(aMapLocation.getLatitude(), aMapLocation.getLongitude())) {
                            z = true;
                        }
                        z = false;
                    } else {
                        if ("http://abroad.apilocate.amap.com/mobile/binary".equals(en.a)) {
                            z = true;
                        }
                        z = false;
                    }
                    String str2 = z ? "abroad" : "domestic";
                    if (aMapLocation.getErrorCode() == 0) {
                        switch (aMapLocation.getLocationType()) {
                            case 5:
                            case 6:
                                str = "net";
                                i2 = 1;
                                break;
                            default:
                                str = "cache";
                                i2 = 1;
                                break;
                        }
                    } else {
                        switch (aMapLocation.getErrorCode()) {
                            case 4:
                            case 5:
                            case 6:
                            case 11:
                                str = "net";
                                break;
                            default:
                                str = "cache";
                                break;
                        }
                    }
                    int errorCode = aMapLocation.getErrorCode();
                    if (context == null) {
                        return;
                    }
                    if (em.g()) {
                        JSONObject jSONObject = new JSONObject();
                        if (!TextUtils.isEmpty(str)) {
                            jSONObject.put("param_string_first", str);
                        }
                        if (!TextUtils.isEmpty(str2)) {
                            jSONObject.put("param_string_second", str2);
                        }
                        if (i2 != Integer.MAX_VALUE) {
                            jSONObject.put("param_int_first", i2);
                        }
                        if (errorCode != Integer.MAX_VALUE) {
                            jSONObject.put("param_int_second", errorCode);
                        }
                        if (dqVar != null) {
                            if (!TextUtils.isEmpty(dqVar.d())) {
                                jSONObject.put("dns", dqVar.d());
                            }
                            if (!TextUtils.isEmpty(dqVar.e())) {
                                jSONObject.put("domain", dqVar.e());
                            }
                            if (!TextUtils.isEmpty(dqVar.f())) {
                                jSONObject.put("type", dqVar.f());
                            }
                            if (!TextUtils.isEmpty(dqVar.g())) {
                                jSONObject.put("reason", dqVar.g());
                            }
                            if (!TextUtils.isEmpty(dqVar.c())) {
                                jSONObject.put(TbAuthConstants.IP, dqVar.c());
                            }
                            if (!TextUtils.isEmpty(dqVar.b())) {
                                jSONObject.put("stack", dqVar.b());
                            }
                            if (dqVar.h() > 0) {
                                jSONObject.put("ctime", String.valueOf(dqVar.h()));
                            }
                            if (dqVar.a() > 0) {
                                jSONObject.put("ntime", String.valueOf(dqVar.a()));
                            }
                        }
                        a(context, "O016", jSONObject);
                    }
                }
            } catch (Throwable th) {
                en.a(th, "ReportUtil", "reportBatting");
            }
        }
    }

    public static void a(Context context, String str, JSONObject jSONObject) {
        if (context != null) {
            try {
                if (em.g()) {
                    bf bfVar = new bf(context, "loc", BuildConfig.VERSION_NAME, str);
                    if (jSONObject != null) {
                        bfVar.a(jSONObject.toString());
                    }
                    i.add(bfVar);
                    if (i.size() >= 30) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.addAll(i);
                        bg.a((List<bf>) arrayList, context);
                        i.clear();
                    }
                }
            } catch (Throwable th) {
                en.a(th, "ReportUtil", "applyStatistics");
            }
        }
    }

    public static void a(AMapLocation aMapLocation, AMapLocation aMapLocation2) {
        try {
            if (g == null) {
                if (!et.a(aMapLocation)) {
                    g = aMapLocation2;
                    return;
                }
                g = aMapLocation.clone();
            }
            if (et.a(g) && et.a(aMapLocation2)) {
                AMapLocation clone = aMapLocation2.clone();
                if (!(g.getLocationType() == 1 || g.getLocationType() == 9 || "gps".equalsIgnoreCase(g.getProvider()) || g.getLocationType() == 7 || clone.getLocationType() == 1 || clone.getLocationType() == 9 || "gps".equalsIgnoreCase(clone.getProvider()) || clone.getLocationType() == 7)) {
                    long abs = Math.abs(clone.getTime() - g.getTime()) / 1000;
                    if (abs <= 0) {
                        abs = 1;
                    }
                    if (abs <= 1800) {
                        float a2 = et.a(g, clone);
                        float f2 = a2 / ((float) abs);
                        if (a2 > 30000.0f && f2 > 1000.0f) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(g.getLatitude()).append(",");
                            sb.append(g.getLongitude()).append(",");
                            sb.append(g.getAccuracy()).append(",");
                            sb.append(g.getLocationType()).append(",");
                            if (aMapLocation.getTime() != 0) {
                                sb.append(et.a(g.getTime(), "yyyyMMdd_HH:mm:ss:SS"));
                            } else {
                                sb.append(g.getTime());
                            }
                            sb.append(Constant.INTENT_JSON_MARK);
                            sb.append(clone.getLatitude()).append(",");
                            sb.append(clone.getLongitude()).append(",");
                            sb.append(clone.getAccuracy()).append(",");
                            sb.append(clone.getLocationType()).append(",");
                            if (clone.getTime() != 0) {
                                sb.append(et.a(clone.getTime(), "yyyyMMdd_HH:mm:ss:SS"));
                            } else {
                                sb.append(clone.getTime());
                            }
                            a("bigshiftstatistics", sb.toString());
                            sb.delete(0, sb.length());
                        }
                    }
                }
                g = clone;
            }
        } catch (Throwable th) {
        }
    }

    public static void a(String str, int i2) {
        String valueOf = String.valueOf(i2);
        String str2 = "";
        switch (i2) {
            case 2011:
                str2 = "ContextIsNull";
                break;
            case 2021:
                str2 = "OnlyMainWifi";
                break;
            case 2022:
                str2 = "OnlyOneWifiButNotMain";
                break;
            case 2031:
                str2 = "CreateApsReqException";
                break;
            case 2041:
                str2 = "ResponseResultIsNull";
                break;
            case 2051:
                str2 = "NeedLoginNetWork\t";
                break;
            case 2052:
                str2 = "MaybeIntercepted";
                break;
            case 2053:
                str2 = "DecryptResponseException";
                break;
            case 2054:
                str2 = "ParserDataException";
                break;
            case 2061:
                str2 = "ServerRetypeError";
                break;
            case 2062:
                str2 = "ServerLocFail";
                break;
            case 2081:
                str2 = "LocalLocException";
                break;
            case 2091:
                str2 = "InitException";
                break;
            case 2101:
                str2 = "BindAPSServiceException";
                break;
            case 2102:
                str2 = "AuthClientScodeFail";
                break;
            case 2103:
                str2 = "NotConfigAPSService";
                break;
            case 2111:
                str2 = "ErrorCgiInfo";
                break;
            case 2121:
                str2 = "NotLocPermission";
                break;
            case 2131:
                str2 = "NoCgiOAndWifiInfo";
                break;
            case 2132:
                str2 = "AirPlaneModeAndWifiOff";
                break;
            case 2133:
                str2 = "NoCgiAndWifiOff";
                break;
            case 2141:
                str2 = "NoEnoughStatellites";
                break;
            case 2151:
                str2 = "MaybeMockNetLoc";
                break;
            case 2152:
                str2 = "MaybeMockGPSLoc";
                break;
        }
        a(str, valueOf, str2);
    }

    public static void a(String str, String str2) {
        try {
            ab.b(en.c(), str2, str);
        } catch (Throwable th) {
            en.a(th, "ReportUtil", "reportLog");
        }
    }

    public static void a(String str, String str2, String str3) {
        try {
            ab.a(en.c(), "/mobile/binary", str3, str, str2);
        } catch (Throwable th) {
        }
    }

    public static void a(String str, Throwable th) {
        try {
            if (th instanceof j) {
                ab.a(en.c(), str, (j) th);
            }
        } catch (Throwable th2) {
        }
    }

    private static void f(Context context) {
        try {
            if (j != null && j.length() > 0) {
                be.a(new bd(context, en.c(), j.toString()), context);
                j = null;
            }
        } catch (Throwable th) {
            en.a(th, "ReportUtil", "writeOfflineLocLog");
        }
    }

    public final void a(Context context, int i2) {
        try {
            if (this.b != i2) {
                if (!(this.b == -1 || this.b == i2)) {
                    long b2 = et.b() - this.c;
                    this.a.append(this.b, Long.valueOf(this.a.get(this.b, 0L).longValue() + b2));
                }
                this.c = et.b() - es.a(context, "pref", this.d[i2], 0);
                this.b = i2;
            }
        } catch (Throwable th) {
            en.a(th, "ReportUtil", "setLocationType");
        }
    }

    public final void a(Context context, AMapLocationClientOption aMapLocationClientOption) {
        int i2;
        try {
            switch (aMapLocationClientOption.getLocationMode()) {
                case Battery_Saving:
                    i2 = 4;
                    break;
                case Device_Sensors:
                    i2 = 5;
                    break;
                case Hight_Accuracy:
                    i2 = 3;
                    break;
                default:
                    i2 = -1;
                    break;
            }
            if (this.e != i2) {
                if (!(this.e == -1 || this.e == i2)) {
                    this.a.append(this.e, Long.valueOf((et.b() - this.f) + this.a.get(this.e, 0L).longValue()));
                }
                this.f = et.b() - es.a(context, "pref", this.d[i2], 0);
                this.e = i2;
            }
        } catch (Throwable th) {
            en.a(th, "ReportUtil", "setLocationMode");
        }
    }

    public final void b(Context context) {
        try {
            long b2 = et.b() - this.c;
            if (this.b != -1) {
                this.a.append(this.b, Long.valueOf(this.a.get(this.b, 0L).longValue() + b2));
            }
            long b3 = et.b() - this.f;
            if (this.e != -1) {
                this.a.append(this.e, Long.valueOf(this.a.get(this.e, 0L).longValue() + b3));
            }
            SharedPreferences.Editor a2 = es.a(context, "pref");
            for (int i2 = 0; i2 < this.d.length; i2++) {
                long longValue = this.a.get(i2, 0L).longValue();
                if (longValue > 0 && longValue > es.a(context, "pref", this.d[i2], 0)) {
                    es.a(a2, this.d[i2], longValue);
                }
            }
            es.a(a2);
        } catch (Throwable th) {
            en.a(th, "ReportUtil", "saveLocationTypeAndMode");
        }
    }

    public final int c(Context context) {
        try {
            long a2 = es.a(context, "pref", this.d[2], 0);
            long a3 = es.a(context, "pref", this.d[0], 0);
            long a4 = es.a(context, "pref", this.d[1], 0);
            if (a2 == 0 && a3 == 0 && a4 == 0) {
                return -1;
            }
            long j2 = a3 - a2;
            long j3 = a4 - a2;
            return a2 > j2 ? a2 > j3 ? 2 : 1 : j2 > j3 ? 0 : 1;
        } catch (Throwable th) {
            return -1;
        }
    }

    public final int d(Context context) {
        try {
            long a2 = es.a(context, "pref", this.d[3], 0);
            long a3 = es.a(context, "pref", this.d[4], 0);
            long a4 = es.a(context, "pref", this.d[5], 0);
            if (a2 == 0 && a3 == 0 && a4 == 0) {
                return -1;
            }
            return a2 > a3 ? a2 > a4 ? 3 : 5 : a3 > a4 ? 4 : 5;
        } catch (Throwable th) {
            return -1;
        }
    }

    public final void e(Context context) {
        try {
            SharedPreferences.Editor a2 = es.a(context, "pref");
            for (String a3 : this.d) {
                es.a(a2, a3, 0);
            }
            es.a(a2);
        } catch (Throwable th) {
        }
    }
}
