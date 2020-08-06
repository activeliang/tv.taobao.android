package com.loc;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.uc.webview.export.internal.setup.UCAsyncTask;
import com.yunos.tv.alitvasr.sdk.AbstractClientManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

@SuppressLint({"NewApi"})
/* compiled from: GeoFenceManager */
public final class a {
    private static boolean A = false;
    er a = null;
    Context b = null;
    PendingIntent c = null;
    String d = null;
    GeoFenceListener e = null;
    volatile int f = 1;
    ArrayList<GeoFence> g = new ArrayList<>();
    c h = null;
    Object i = new Object();
    Object j = new Object();
    C0000a k = null;
    b l = null;
    volatile boolean m = false;
    volatile boolean n = false;
    volatile boolean o = false;
    b p = null;
    c q = null;
    AMapLocationClient r = null;
    volatile AMapLocation s = null;
    long t = 0;
    AMapLocationClientOption u = null;
    int v = 0;
    AMapLocationListener w = new AMapLocationListener() {
        /* JADX WARNING: Removed duplicated region for block: B:12:0x0034 A[Catch:{ Throwable -> 0x0043 }] */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x006f A[Catch:{ Throwable -> 0x0043 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void onLocationChanged(com.amap.api.location.AMapLocation r10) {
            /*
                r9 = this;
                r0 = 8
                r1 = 1
                r2 = 0
                com.loc.a r3 = com.loc.a.this     // Catch:{ Throwable -> 0x0043 }
                boolean r3 = r3.y     // Catch:{ Throwable -> 0x0043 }
                if (r3 == 0) goto L_0x000b
            L_0x000a:
                return
            L_0x000b:
                com.loc.a r3 = com.loc.a.this     // Catch:{ Throwable -> 0x0043 }
                boolean r3 = r3.o     // Catch:{ Throwable -> 0x0043 }
                if (r3 == 0) goto L_0x000a
                com.loc.a r3 = com.loc.a.this     // Catch:{ Throwable -> 0x0043 }
                r3.s = r10     // Catch:{ Throwable -> 0x0043 }
                if (r10 == 0) goto L_0x006d
                int r0 = r10.getErrorCode()     // Catch:{ Throwable -> 0x0043 }
                int r3 = r10.getErrorCode()     // Catch:{ Throwable -> 0x0043 }
                if (r3 != 0) goto L_0x0045
                com.loc.a r2 = com.loc.a.this     // Catch:{ Throwable -> 0x0043 }
                long r4 = com.loc.et.b()     // Catch:{ Throwable -> 0x0043 }
                r2.t = r4     // Catch:{ Throwable -> 0x0043 }
                com.loc.a r2 = com.loc.a.this     // Catch:{ Throwable -> 0x0043 }
                r3 = 5
                r4 = 0
                r6 = 0
                r2.a((int) r3, (android.os.Bundle) r4, (long) r6)     // Catch:{ Throwable -> 0x0043 }
            L_0x0032:
                if (r1 == 0) goto L_0x006f
                com.loc.a r0 = com.loc.a.this     // Catch:{ Throwable -> 0x0043 }
                r1 = 0
                r0.v = r1     // Catch:{ Throwable -> 0x0043 }
                com.loc.a r0 = com.loc.a.this     // Catch:{ Throwable -> 0x0043 }
                r1 = 6
                r2 = 0
                r4 = 0
                r0.a((int) r1, (android.os.Bundle) r2, (long) r4)     // Catch:{ Throwable -> 0x0043 }
                goto L_0x000a
            L_0x0043:
                r0 = move-exception
                goto L_0x000a
            L_0x0045:
                java.lang.String r1 = "定位失败"
                int r3 = r10.getErrorCode()     // Catch:{ Throwable -> 0x0043 }
                java.lang.String r4 = r10.getErrorInfo()     // Catch:{ Throwable -> 0x0043 }
                r5 = 1
                java.lang.String[] r5 = new java.lang.String[r5]     // Catch:{ Throwable -> 0x0043 }
                r6 = 0
                java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0043 }
                java.lang.String r8 = "locationDetail:"
                r7.<init>(r8)     // Catch:{ Throwable -> 0x0043 }
                java.lang.String r8 = r10.getLocationDetail()     // Catch:{ Throwable -> 0x0043 }
                java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Throwable -> 0x0043 }
                java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0043 }
                r5[r6] = r7     // Catch:{ Throwable -> 0x0043 }
                com.loc.a.a(r1, r3, r4, r5)     // Catch:{ Throwable -> 0x0043 }
            L_0x006d:
                r1 = r2
                goto L_0x0032
            L_0x006f:
                android.os.Bundle r1 = new android.os.Bundle     // Catch:{ Throwable -> 0x0043 }
                r1.<init>()     // Catch:{ Throwable -> 0x0043 }
                com.loc.a r2 = com.loc.a.this     // Catch:{ Throwable -> 0x0043 }
                boolean r2 = r2.m     // Catch:{ Throwable -> 0x0043 }
                if (r2 != 0) goto L_0x0091
                com.loc.a r2 = com.loc.a.this     // Catch:{ Throwable -> 0x0043 }
                r3 = 7
                r2.b((int) r3)     // Catch:{ Throwable -> 0x0043 }
                java.lang.String r2 = "interval"
                r4 = 2000(0x7d0, double:9.88E-321)
                r1.putLong(r2, r4)     // Catch:{ Throwable -> 0x0043 }
                com.loc.a r2 = com.loc.a.this     // Catch:{ Throwable -> 0x0043 }
                r3 = 8
                r4 = 2000(0x7d0, double:9.88E-321)
                r2.a((int) r3, (android.os.Bundle) r1, (long) r4)     // Catch:{ Throwable -> 0x0043 }
            L_0x0091:
                com.loc.a r2 = com.loc.a.this     // Catch:{ Throwable -> 0x0043 }
                int r3 = r2.v     // Catch:{ Throwable -> 0x0043 }
                int r3 = r3 + 1
                r2.v = r3     // Catch:{ Throwable -> 0x0043 }
                com.loc.a r2 = com.loc.a.this     // Catch:{ Throwable -> 0x0043 }
                int r2 = r2.v     // Catch:{ Throwable -> 0x0043 }
                r3 = 3
                if (r2 < r3) goto L_0x000a
                java.lang.String r2 = "location_errorcode"
                r1.putInt(r2, r0)     // Catch:{ Throwable -> 0x0043 }
                com.loc.a r0 = com.loc.a.this     // Catch:{ Throwable -> 0x0043 }
                r2 = 1002(0x3ea, float:1.404E-42)
                r0.a((int) r2, (android.os.Bundle) r1)     // Catch:{ Throwable -> 0x0043 }
                goto L_0x000a
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.a.AnonymousClass1.onLocationChanged(com.amap.api.location.AMapLocation):void");
        }
    };
    final int x = 3;
    volatile boolean y = false;
    private Object z = new Object();

    /* renamed from: com.loc.a$a  reason: collision with other inner class name */
    /* compiled from: GeoFenceManager */
    class C0000a extends Handler {
        public C0000a(Looper looper) {
            super(looper);
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void handleMessage(android.os.Message r11) {
            /*
                r10 = this;
                r0 = 2000(0x7d0, double:9.88E-321)
                r2 = 1
                int r3 = r11.what     // Catch:{ Throwable -> 0x00bc }
                switch(r3) {
                    case 0: goto L_0x0009;
                    case 1: goto L_0x00d1;
                    case 2: goto L_0x013b;
                    case 3: goto L_0x0146;
                    case 4: goto L_0x0151;
                    case 5: goto L_0x0167;
                    case 6: goto L_0x015c;
                    case 7: goto L_0x016e;
                    case 8: goto L_0x0197;
                    case 9: goto L_0x01e3;
                    case 10: goto L_0x01ee;
                    case 11: goto L_0x01f5;
                    case 12: goto L_0x0200;
                    case 13: goto L_0x020b;
                    default: goto L_0x0008;
                }     // Catch:{ Throwable -> 0x00bc }
            L_0x0008:
                return
            L_0x0009:
                com.loc.a r3 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                android.os.Bundle r4 = r11.getData()     // Catch:{ Throwable -> 0x00bc }
                java.util.ArrayList r5 = new java.util.ArrayList     // Catch:{ Throwable -> 0x00b0 }
                r5.<init>()     // Catch:{ Throwable -> 0x00b0 }
                java.lang.String r0 = ""
                if (r4 == 0) goto L_0x00cf
                boolean r1 = r4.isEmpty()     // Catch:{ Throwable -> 0x00b0 }
                if (r1 != 0) goto L_0x00cf
                java.lang.String r0 = "centerPoint"
                android.os.Parcelable r0 = r4.getParcelable(r0)     // Catch:{ Throwable -> 0x00b0 }
                com.amap.api.location.DPoint r0 = (com.amap.api.location.DPoint) r0     // Catch:{ Throwable -> 0x00b0 }
                java.lang.String r1 = "customId"
                java.lang.String r1 = r4.getString(r1)     // Catch:{ Throwable -> 0x00b0 }
                if (r0 == 0) goto L_0x00ce
                double r6 = r0.getLatitude()     // Catch:{ Throwable -> 0x00b0 }
                r8 = 4636033603912859648(0x4056800000000000, double:90.0)
                int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r6 > 0) goto L_0x0065
                double r6 = r0.getLatitude()     // Catch:{ Throwable -> 0x00b0 }
                r8 = -4587338432941916160(0xc056800000000000, double:-90.0)
                int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r6 < 0) goto L_0x0065
                double r6 = r0.getLongitude()     // Catch:{ Throwable -> 0x00b0 }
                r8 = 4640537203540230144(0x4066800000000000, double:180.0)
                int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r6 > 0) goto L_0x0065
                double r6 = r0.getLongitude()     // Catch:{ Throwable -> 0x00b0 }
                r8 = -4582834833314545664(0xc066800000000000, double:-180.0)
                int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r6 >= 0) goto L_0x00bf
            L_0x0065:
                java.lang.String r4 = "添加围栏失败"
                r6 = 1
                java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00b0 }
                java.lang.String r8 = "经纬度错误，传入的纬度："
                r7.<init>(r8)     // Catch:{ Throwable -> 0x00b0 }
                double r8 = r0.getLatitude()     // Catch:{ Throwable -> 0x00b0 }
                java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Throwable -> 0x00b0 }
                java.lang.String r8 = "传入的经度:"
                java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Throwable -> 0x00b0 }
                double r8 = r0.getLongitude()     // Catch:{ Throwable -> 0x00b0 }
                java.lang.StringBuilder r0 = r7.append(r8)     // Catch:{ Throwable -> 0x00b0 }
                java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x00b0 }
                r7 = 0
                java.lang.String[] r7 = new java.lang.String[r7]     // Catch:{ Throwable -> 0x00b0 }
                com.loc.a.a(r4, r6, r0, r7)     // Catch:{ Throwable -> 0x00b0 }
            L_0x0092:
                android.os.Bundle r0 = new android.os.Bundle     // Catch:{ Throwable -> 0x00b0 }
                r0.<init>()     // Catch:{ Throwable -> 0x00b0 }
                java.lang.String r4 = "errorCode"
                r0.putInt(r4, r2)     // Catch:{ Throwable -> 0x00b0 }
                java.lang.String r2 = "resultList"
                r0.putParcelableArrayList(r2, r5)     // Catch:{ Throwable -> 0x00b0 }
                java.lang.String r2 = "customId"
                r0.putString(r2, r1)     // Catch:{ Throwable -> 0x00b0 }
                r1 = 1000(0x3e8, float:1.401E-42)
                r3.a((int) r1, (android.os.Bundle) r0)     // Catch:{ Throwable -> 0x00b0 }
                goto L_0x0008
            L_0x00b0:
                r0 = move-exception
                java.lang.String r1 = "GeoFenceManager"
                java.lang.String r2 = "doAddGeoFenceRound"
                com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x00bc }
                goto L_0x0008
            L_0x00bc:
                r0 = move-exception
                goto L_0x0008
            L_0x00bf:
                r0 = 0
                com.amap.api.fence.GeoFence r0 = r3.a((android.os.Bundle) r4, (boolean) r0)     // Catch:{ Throwable -> 0x00b0 }
                int r2 = r3.b((com.amap.api.fence.GeoFence) r0)     // Catch:{ Throwable -> 0x00b0 }
                if (r2 != 0) goto L_0x0092
                r5.add(r0)     // Catch:{ Throwable -> 0x00b0 }
                goto L_0x0092
            L_0x00ce:
                r0 = r1
            L_0x00cf:
                r1 = r0
                goto L_0x0092
            L_0x00d1:
                com.loc.a r3 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                android.os.Bundle r4 = r11.getData()     // Catch:{ Throwable -> 0x00bc }
                java.util.ArrayList r5 = new java.util.ArrayList     // Catch:{ Throwable -> 0x011d }
                r5.<init>()     // Catch:{ Throwable -> 0x011d }
                java.lang.String r0 = ""
                if (r4 == 0) goto L_0x0138
                boolean r1 = r4.isEmpty()     // Catch:{ Throwable -> 0x011d }
                if (r1 != 0) goto L_0x0138
                java.lang.String r0 = "pointList"
                java.util.ArrayList r0 = r4.getParcelableArrayList(r0)     // Catch:{ Throwable -> 0x011d }
                java.lang.String r1 = "customId"
                java.lang.String r1 = r4.getString(r1)     // Catch:{ Throwable -> 0x011d }
                if (r0 == 0) goto L_0x00fe
                int r0 = r0.size()     // Catch:{ Throwable -> 0x011d }
                r6 = 2
                if (r0 > r6) goto L_0x0129
            L_0x00fe:
                r0 = r2
            L_0x00ff:
                android.os.Bundle r2 = new android.os.Bundle     // Catch:{ Throwable -> 0x011d }
                r2.<init>()     // Catch:{ Throwable -> 0x011d }
                java.lang.String r4 = "customId"
                r2.putString(r4, r1)     // Catch:{ Throwable -> 0x011d }
                java.lang.String r1 = "errorCode"
                r2.putInt(r1, r0)     // Catch:{ Throwable -> 0x011d }
                java.lang.String r0 = "resultList"
                r2.putParcelableArrayList(r0, r5)     // Catch:{ Throwable -> 0x011d }
                r0 = 1000(0x3e8, float:1.401E-42)
                r3.a((int) r0, (android.os.Bundle) r2)     // Catch:{ Throwable -> 0x011d }
                goto L_0x0008
            L_0x011d:
                r0 = move-exception
                java.lang.String r1 = "GeoFenceManager"
                java.lang.String r2 = "doAddGeoFencePolygon"
                com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x00bc }
                goto L_0x0008
            L_0x0129:
                r0 = 1
                com.amap.api.fence.GeoFence r2 = r3.a((android.os.Bundle) r4, (boolean) r0)     // Catch:{ Throwable -> 0x011d }
                int r0 = r3.b((com.amap.api.fence.GeoFence) r2)     // Catch:{ Throwable -> 0x011d }
                if (r0 != 0) goto L_0x00ff
                r5.add(r2)     // Catch:{ Throwable -> 0x011d }
                goto L_0x00ff
            L_0x0138:
                r1 = r0
                r0 = r2
                goto L_0x00ff
            L_0x013b:
                com.loc.a r0 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                android.os.Bundle r1 = r11.getData()     // Catch:{ Throwable -> 0x00bc }
                r0.c((android.os.Bundle) r1)     // Catch:{ Throwable -> 0x00bc }
                goto L_0x0008
            L_0x0146:
                com.loc.a r0 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                android.os.Bundle r1 = r11.getData()     // Catch:{ Throwable -> 0x00bc }
                r0.b((android.os.Bundle) r1)     // Catch:{ Throwable -> 0x00bc }
                goto L_0x0008
            L_0x0151:
                com.loc.a r0 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                android.os.Bundle r1 = r11.getData()     // Catch:{ Throwable -> 0x00bc }
                r0.d(r1)     // Catch:{ Throwable -> 0x00bc }
                goto L_0x0008
            L_0x015c:
                com.loc.a r0 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                com.loc.a r1 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                com.amap.api.location.AMapLocation r1 = r1.s     // Catch:{ Throwable -> 0x00bc }
                r0.a((com.amap.api.location.AMapLocation) r1)     // Catch:{ Throwable -> 0x00bc }
                goto L_0x0008
            L_0x0167:
                com.loc.a r0 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                r0.f()     // Catch:{ Throwable -> 0x00bc }
                goto L_0x0008
            L_0x016e:
                com.loc.a r0 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                com.amap.api.location.AMapLocationClient r1 = r0.r     // Catch:{ Throwable -> 0x018b }
                if (r1 == 0) goto L_0x0008
                r0.e()     // Catch:{ Throwable -> 0x018b }
                com.amap.api.location.AMapLocationClientOption r1 = r0.u     // Catch:{ Throwable -> 0x018b }
                r2 = 1
                r1.setOnceLocation(r2)     // Catch:{ Throwable -> 0x018b }
                com.amap.api.location.AMapLocationClient r1 = r0.r     // Catch:{ Throwable -> 0x018b }
                com.amap.api.location.AMapLocationClientOption r2 = r0.u     // Catch:{ Throwable -> 0x018b }
                r1.setLocationOption(r2)     // Catch:{ Throwable -> 0x018b }
                com.amap.api.location.AMapLocationClient r0 = r0.r     // Catch:{ Throwable -> 0x018b }
                r0.startLocation()     // Catch:{ Throwable -> 0x018b }
                goto L_0x0008
            L_0x018b:
                r0 = move-exception
                java.lang.String r1 = "GeoFenceManager"
                java.lang.String r2 = "doStartOnceLocation"
                com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x00bc }
                goto L_0x0008
            L_0x0197:
                com.loc.a r2 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                android.os.Bundle r3 = r11.getData()     // Catch:{ Throwable -> 0x00bc }
                com.amap.api.location.AMapLocationClient r4 = r2.r     // Catch:{ Throwable -> 0x01d7 }
                if (r4 == 0) goto L_0x0008
                if (r3 == 0) goto L_0x01b2
                boolean r4 = r3.isEmpty()     // Catch:{ Throwable -> 0x01d7 }
                if (r4 != 0) goto L_0x01b2
                java.lang.String r0 = "interval"
                r4 = 2000(0x7d0, double:9.88E-321)
                long r0 = r3.getLong(r0, r4)     // Catch:{ Throwable -> 0x01d7 }
            L_0x01b2:
                com.amap.api.location.AMapLocationClientOption r3 = r2.u     // Catch:{ Throwable -> 0x01d7 }
                r4 = 0
                r3.setOnceLocation(r4)     // Catch:{ Throwable -> 0x01d7 }
                com.amap.api.location.AMapLocationClientOption r3 = r2.u     // Catch:{ Throwable -> 0x01d7 }
                r3.setInterval(r0)     // Catch:{ Throwable -> 0x01d7 }
                com.amap.api.location.AMapLocationClient r0 = r2.r     // Catch:{ Throwable -> 0x01d7 }
                com.amap.api.location.AMapLocationClientOption r1 = r2.u     // Catch:{ Throwable -> 0x01d7 }
                r0.setLocationOption(r1)     // Catch:{ Throwable -> 0x01d7 }
                boolean r0 = r2.m     // Catch:{ Throwable -> 0x01d7 }
                if (r0 != 0) goto L_0x0008
                com.amap.api.location.AMapLocationClient r0 = r2.r     // Catch:{ Throwable -> 0x01d7 }
                r0.stopLocation()     // Catch:{ Throwable -> 0x01d7 }
                com.amap.api.location.AMapLocationClient r0 = r2.r     // Catch:{ Throwable -> 0x01d7 }
                r0.startLocation()     // Catch:{ Throwable -> 0x01d7 }
                r0 = 1
                r2.m = r0     // Catch:{ Throwable -> 0x01d7 }
                goto L_0x0008
            L_0x01d7:
                r0 = move-exception
                java.lang.String r1 = "GeoFenceManager"
                java.lang.String r2 = "doStartContinueLocation"
                com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x00bc }
                goto L_0x0008
            L_0x01e3:
                com.loc.a r0 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                android.os.Bundle r1 = r11.getData()     // Catch:{ Throwable -> 0x00bc }
                r0.a((android.os.Bundle) r1)     // Catch:{ Throwable -> 0x00bc }
                goto L_0x0008
            L_0x01ee:
                com.loc.a r0 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                r0.c()     // Catch:{ Throwable -> 0x00bc }
                goto L_0x0008
            L_0x01f5:
                com.loc.a r0 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                android.os.Bundle r1 = r11.getData()     // Catch:{ Throwable -> 0x00bc }
                r0.f(r1)     // Catch:{ Throwable -> 0x00bc }
                goto L_0x0008
            L_0x0200:
                com.loc.a r0 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                android.os.Bundle r1 = r11.getData()     // Catch:{ Throwable -> 0x00bc }
                r0.e(r1)     // Catch:{ Throwable -> 0x00bc }
                goto L_0x0008
            L_0x020b:
                com.loc.a r0 = com.loc.a.this     // Catch:{ Throwable -> 0x00bc }
                r0.h()     // Catch:{ Throwable -> 0x00bc }
                goto L_0x0008
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.a.C0000a.handleMessage(android.os.Message):void");
        }
    }

    /* compiled from: GeoFenceManager */
    static class b extends HandlerThread {
        public b(String str) {
            super(str);
        }

        public final void run() {
            try {
                super.run();
            } catch (Throwable th) {
            }
        }
    }

    /* compiled from: GeoFenceManager */
    class c extends Handler {
        public c() {
        }

        public c(Looper looper) {
            super(looper);
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void handleMessage(android.os.Message r7) {
            /*
                r6 = this;
                android.os.Bundle r1 = r7.getData()     // Catch:{ Throwable -> 0x0057 }
                int r0 = r7.what     // Catch:{ Throwable -> 0x0057 }
                switch(r0) {
                    case 1000: goto L_0x000a;
                    case 1001: goto L_0x0059;
                    case 1002: goto L_0x006d;
                    default: goto L_0x0009;
                }     // Catch:{ Throwable -> 0x0057 }
            L_0x0009:
                return
            L_0x000a:
                com.loc.a r3 = com.loc.a.this     // Catch:{ Throwable -> 0x0057 }
                if (r1 == 0) goto L_0x0009
                boolean r0 = r1.isEmpty()     // Catch:{ Throwable -> 0x004c }
                if (r0 != 0) goto L_0x0009
                java.lang.String r0 = "errorCode"
                int r4 = r1.getInt(r0)     // Catch:{ Throwable -> 0x004c }
                java.lang.String r0 = "resultList"
                java.util.ArrayList r0 = r1.getParcelableArrayList(r0)     // Catch:{ Throwable -> 0x004c }
                if (r0 != 0) goto L_0x0081
                java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ Throwable -> 0x004c }
                r0.<init>()     // Catch:{ Throwable -> 0x004c }
                r2 = r0
            L_0x002a:
                java.lang.String r0 = "customId"
                java.lang.String r0 = r1.getString(r0)     // Catch:{ Throwable -> 0x004c }
                if (r0 != 0) goto L_0x007f
                java.lang.String r0 = ""
                r1 = r0
            L_0x0037:
                com.amap.api.fence.GeoFenceListener r0 = r3.e     // Catch:{ Throwable -> 0x004c }
                if (r0 == 0) goto L_0x0046
                com.amap.api.fence.GeoFenceListener r5 = r3.e     // Catch:{ Throwable -> 0x004c }
                java.lang.Object r0 = r2.clone()     // Catch:{ Throwable -> 0x004c }
                java.util.ArrayList r0 = (java.util.ArrayList) r0     // Catch:{ Throwable -> 0x004c }
                r5.onGeoFenceCreateFinished(r0, r4, r1)     // Catch:{ Throwable -> 0x004c }
            L_0x0046:
                if (r4 != 0) goto L_0x0009
                r3.d()     // Catch:{ Throwable -> 0x004c }
                goto L_0x0009
            L_0x004c:
                r0 = move-exception
                java.lang.String r1 = "GeoFenceManager"
                java.lang.String r2 = "resultAddGeoFenceFinished"
                com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x0057 }
                goto L_0x0009
            L_0x0057:
                r0 = move-exception
                goto L_0x0009
            L_0x0059:
                java.lang.String r0 = "geoFence"
                android.os.Parcelable r0 = r1.getParcelable(r0)     // Catch:{ Throwable -> 0x0068 }
                com.amap.api.fence.GeoFence r0 = (com.amap.api.fence.GeoFence) r0     // Catch:{ Throwable -> 0x0068 }
                com.loc.a r1 = com.loc.a.this     // Catch:{ Throwable -> 0x0068 }
                r1.c((com.amap.api.fence.GeoFence) r0)     // Catch:{ Throwable -> 0x0068 }
                goto L_0x0009
            L_0x0068:
                r0 = move-exception
                r0.printStackTrace()     // Catch:{ Throwable -> 0x0057 }
                goto L_0x0009
            L_0x006d:
                java.lang.String r0 = "location_errorcode"
                int r0 = r1.getInt(r0)     // Catch:{ Throwable -> 0x007a }
                com.loc.a r1 = com.loc.a.this     // Catch:{ Throwable -> 0x007a }
                r1.c((int) r0)     // Catch:{ Throwable -> 0x007a }
                goto L_0x0009
            L_0x007a:
                r0 = move-exception
                r0.printStackTrace()     // Catch:{ Throwable -> 0x0057 }
                goto L_0x0009
            L_0x007f:
                r1 = r0
                goto L_0x0037
            L_0x0081:
                r2 = r0
                goto L_0x002a
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.a.c.handleMessage(android.os.Message):void");
        }
    }

    public a(Context context) {
        try {
            this.b = context.getApplicationContext();
            k();
        } catch (Throwable th) {
            en.a(th, "GeoFenceManger", "<init>");
        }
    }

    static float a(DPoint dPoint, List<DPoint> list) {
        float f2 = Float.MAX_VALUE;
        if (dPoint == null || list == null || list.isEmpty()) {
            return Float.MAX_VALUE;
        }
        Iterator<DPoint> it = list.iterator();
        while (true) {
            float f3 = f2;
            if (!it.hasNext()) {
                return f3;
            }
            f2 = Math.min(f3, et.a(dPoint, it.next()));
        }
    }

    private int a(List<GeoFence> list) {
        try {
            if (this.g == null) {
                this.g = new ArrayList<>();
            }
            for (GeoFence b2 : list) {
                b(b2);
            }
            return 0;
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "addGeoFenceList");
            a("添加围栏失败", 8, th.getMessage(), new String[0]);
            return 8;
        }
    }

    private static Bundle a(GeoFence geoFence, String str, String str2, int i2, int i3) {
        Bundle bundle = new Bundle();
        if (str == null) {
            str = "";
        }
        bundle.putString(GeoFence.BUNDLE_KEY_FENCEID, str);
        bundle.putString(GeoFence.BUNDLE_KEY_CUSTOMID, str2);
        bundle.putInt("event", i2);
        bundle.putInt(GeoFence.BUNDLE_KEY_LOCERRORCODE, i3);
        bundle.putParcelable(GeoFence.BUNDLE_KEY_FENCE, geoFence);
        return bundle;
    }

    static void a(String str, int i2, String str2, String... strArr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("===========================================\n");
        stringBuffer.append("              " + str + "                ").append("\n");
        stringBuffer.append("-------------------------------------------\n");
        stringBuffer.append("errorCode:" + i2).append("\n");
        stringBuffer.append("错误信息:" + str2).append("\n");
        if (strArr.length > 0) {
            for (String append : strArr) {
                stringBuffer.append(append).append("\n");
            }
        }
        stringBuffer.append("===========================================\n");
        Log.i("fenceErrLog", stringBuffer.toString());
    }

    private static boolean a(GeoFence geoFence, int i2) {
        boolean z2 = false;
        if ((i2 & 1) == 1) {
            try {
                if (geoFence.getStatus() == 1) {
                    z2 = true;
                }
            } catch (Throwable th) {
                en.a(th, "Utils", "remindStatus");
                return false;
            }
        }
        if ((i2 & 2) == 2 && geoFence.getStatus() == 2) {
            z2 = true;
        }
        if ((i2 & 4) == 4 && geoFence.getStatus() == 3) {
            return true;
        }
        return z2;
    }

    private static boolean a(AMapLocation aMapLocation, GeoFence geoFence) {
        boolean z2;
        boolean z3;
        try {
            if (et.a(aMapLocation) && geoFence != null && geoFence.getPointList() != null && !geoFence.getPointList().isEmpty()) {
                switch (geoFence.getType()) {
                    case 0:
                    case 2:
                        DPoint center = geoFence.getCenter();
                        if (et.a(new double[]{center.getLatitude(), center.getLongitude(), aMapLocation.getLatitude(), aMapLocation.getLongitude()}) <= geoFence.getRadius()) {
                            return true;
                        }
                        break;
                    case 1:
                    case 3:
                        z3 = false;
                        for (List next : geoFence.getPointList()) {
                            try {
                                z3 = next.size() < 3 ? false : en.a(new DPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()), (List<DPoint>) next) ? true : z3;
                            } catch (Throwable th) {
                                th = th;
                                z2 = z3;
                                en.a(th, "Utils", "isInGeoFence");
                                return z2;
                            }
                        }
                        break;
                }
            }
            z3 = false;
            return z3;
        } catch (Throwable th2) {
            th = th2;
            z2 = false;
            en.a(th, "Utils", "isInGeoFence");
            return z2;
        }
    }

    static float b(DPoint dPoint, List<DPoint> list) {
        float f2 = Float.MIN_VALUE;
        if (dPoint == null || list == null || list.isEmpty()) {
            return Float.MIN_VALUE;
        }
        Iterator<DPoint> it = list.iterator();
        while (true) {
            float f3 = f2;
            if (!it.hasNext()) {
                return f3;
            }
            f2 = Math.max(f3, et.a(dPoint, it.next()));
        }
    }

    private static DPoint b(List<DPoint> list) {
        DPoint dPoint;
        double d2 = ClientTraceData.b.f47a;
        DPoint dPoint2 = new DPoint();
        if (list != null) {
            try {
                double d3 = 0.0d;
                for (DPoint next : list) {
                    d3 += next.getLatitude();
                    d2 += next.getLongitude();
                }
                dPoint = new DPoint(et.b(d3 / ((double) list.size())), et.b(d2 / ((double) list.size())));
            } catch (Throwable th) {
                en.a(th, "GeoFenceUtil", "getPolygonCenter");
                return dPoint2;
            }
        } else {
            dPoint = dPoint2;
        }
        return dPoint;
    }

    private void b(int i2, Bundle bundle) {
        int i3;
        int i4;
        String a2;
        Bundle bundle2 = new Bundle();
        int i5 = 0;
        try {
            ArrayList arrayList = new ArrayList();
            if (bundle == null || bundle.isEmpty()) {
                i4 = 1;
            } else {
                ArrayList arrayList2 = new ArrayList();
                String string = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                String string2 = bundle.getString("keyWords");
                String string3 = bundle.getString("city");
                String string4 = bundle.getString("poiType");
                DPoint dPoint = (DPoint) bundle.getParcelable("centerPoint");
                int i6 = bundle.getInt("searchSize", 10);
                float f2 = bundle.getFloat("aroundRadius", 3000.0f);
                boolean z2 = true;
                if (!TextUtils.isEmpty(string2)) {
                    switch (i2) {
                        case 1:
                            if (TextUtils.isEmpty(string4)) {
                                z2 = false;
                                break;
                            }
                            break;
                        case 2:
                            if (dPoint != null) {
                                if (dPoint.getLatitude() > 90.0d || dPoint.getLatitude() < -90.0d || dPoint.getLongitude() > 180.0d || dPoint.getLongitude() < -180.0d) {
                                    a("添加围栏失败", 0, "经纬度错误，传入的纬度：" + dPoint.getLatitude() + "传入的经度:" + dPoint.getLongitude(), new String[0]);
                                    z2 = false;
                                    break;
                                }
                            } else {
                                z2 = false;
                                break;
                            }
                    }
                } else {
                    z2 = false;
                }
                if (z2) {
                    Bundle bundle3 = new Bundle();
                    bundle3.putString(GeoFence.BUNDLE_KEY_CUSTOMID, string);
                    bundle3.putString("pendingIntentAction", this.d);
                    bundle3.putLong("expiration", -1);
                    bundle3.putInt("activatesAction", this.f);
                    switch (i2) {
                        case 1:
                            bundle3.putFloat("fenceRadius", 1000.0f);
                            a2 = this.p.a(this.b, "http://restapi.amap.com/v3/place/text?", string2, string4, string3, String.valueOf(i6));
                            break;
                        case 2:
                            double b2 = et.b(dPoint.getLatitude());
                            double b3 = et.b(dPoint.getLongitude());
                            int intValue = Float.valueOf(f2).intValue();
                            bundle3.putFloat("fenceRadius", 200.0f);
                            a2 = this.p.a(this.b, "http://restapi.amap.com/v3/place/around?", string2, string4, String.valueOf(i6), String.valueOf(b2), String.valueOf(b3), String.valueOf(intValue));
                            break;
                        case 3:
                            a2 = this.p.a(this.b, "http://restapi.amap.com/v3/config/district?", string2);
                            break;
                        default:
                            a2 = null;
                            break;
                    }
                    if (a2 != null) {
                        i4 = 1 == i2 ? c.a(a2, arrayList2, bundle3) : 0;
                        if (2 == i2) {
                            i4 = c.a(a2, arrayList2, bundle3);
                        }
                        if (3 == i2) {
                            i4 = this.q.b(a2, arrayList2, bundle3);
                        }
                        if (i4 != 10000) {
                            switch (i4) {
                                case 1:
                                case 4:
                                case 5:
                                case 7:
                                case 16:
                                case 17:
                                    break;
                                case 10000:
                                    i4 = 0;
                                    break;
                                case 10001:
                                case 10002:
                                case 10007:
                                case 10008:
                                case 10009:
                                case 10012:
                                case UCAsyncTask.getPriority /*10013*/:
                                    i4 = 7;
                                    break;
                                case 10003:
                                case 10004:
                                case 10005:
                                case 10006:
                                case 10010:
                                case 10011:
                                case UCAsyncTask.getTaskCount /*10014*/:
                                case UCAsyncTask.getRootTask /*10015*/:
                                case UCAsyncTask.inThread /*10016*/:
                                case UCAsyncTask.getPercent /*10017*/:
                                    i4 = 4;
                                    break;
                                case 20000:
                                case IjkMediaPlayer.FFP_PROP_INT64_SELECTED_VIDEO_STREAM /*20001*/:
                                case IjkMediaPlayer.FFP_PROP_INT64_SELECTED_AUDIO_STREAM /*20002*/:
                                    i4 = 1;
                                    break;
                                case IjkMediaPlayer.FFP_PROP_INT64_VIDEO_DECODER /*20003*/:
                                    i4 = 8;
                                    break;
                                default:
                                    i4 = 8;
                                    break;
                            }
                            if (i4 != 0) {
                                a("添加围栏失败", i4, "searchErrCode is " + i4, new String[0]);
                            }
                        } else if (arrayList2.isEmpty()) {
                            i4 = 16;
                        } else {
                            i4 = a((List<GeoFence>) arrayList2);
                            if (i4 == 0) {
                                arrayList.addAll(arrayList2);
                            }
                        }
                    } else {
                        i4 = 4;
                    }
                } else {
                    i4 = 1;
                }
                try {
                    bundle2.putString(GeoFence.BUNDLE_KEY_CUSTOMID, string);
                    bundle2.putParcelableArrayList("resultList", arrayList);
                } catch (Throwable th) {
                    i5 = i4;
                    th = th;
                    bundle2.putInt("errorCode", i5);
                    a(1000, bundle2);
                    throw th;
                }
            }
            bundle2.putInt("errorCode", i4);
            a(1000, bundle2);
            return;
        } catch (Throwable th2) {
            th = th2;
            bundle2.putInt("errorCode", i5);
            a(1000, bundle2);
            throw th;
        }
        try {
            en.a(th, "GeoFenceManager", "doAddGeoFenceNearby");
            bundle2.putInt("errorCode", 8);
            a(1000, bundle2);
        } catch (Throwable th3) {
            th = th3;
            i5 = i3;
            bundle2.putInt("errorCode", i5);
            a(1000, bundle2);
            throw th;
        }
    }

    private static boolean b(AMapLocation aMapLocation, GeoFence geoFence) {
        Throwable th;
        boolean z2 = true;
        boolean z3 = false;
        try {
            if (a(aMapLocation, geoFence)) {
                if (geoFence.getEnterTime() == -1) {
                    if (geoFence.getStatus() != 1) {
                        geoFence.setEnterTime(et.b());
                        geoFence.setStatus(1);
                        return true;
                    }
                } else if (geoFence.getStatus() != 3 && et.b() - geoFence.getEnterTime() > 600000) {
                    geoFence.setStatus(3);
                    return true;
                }
            } else if (geoFence.getStatus() != 2) {
                try {
                    geoFence.setStatus(2);
                    geoFence.setEnterTime(-1);
                    z3 = true;
                } catch (Throwable th2) {
                    th = th2;
                    en.a(th, "Utils", "isFenceStatusChanged");
                    return z2;
                }
            }
            return z3;
        } catch (Throwable th3) {
            Throwable th4 = th3;
            z2 = false;
            th = th4;
        }
    }

    private void k() {
        if (!this.o) {
            this.o = true;
        }
        if (!this.n) {
            try {
                if (Looper.myLooper() == null) {
                    this.h = new c(this.b.getMainLooper());
                } else {
                    this.h = new c();
                }
            } catch (Throwable th) {
                en.a(th, "GeoFenceManger", "init 1");
            }
            try {
                this.l = new b("fenceActionThread");
                this.l.setPriority(5);
                this.l.start();
                this.k = new C0000a(this.l.getLooper());
            } catch (Throwable th2) {
                en.a(th2, "GeoFenceManger", "init 2");
            }
            try {
                this.p = new b(this.b);
                this.q = new c();
                this.u = new AMapLocationClientOption();
                this.r = new AMapLocationClient(this.b);
                this.u.setLocationCacheEnable(true);
                this.u.setNeedAddress(false);
                this.r.setLocationListener(this.w);
                if (this.a == null) {
                    this.a = new er();
                }
            } catch (Throwable th3) {
                en.a(th3, "GeoFenceManger", "initBase");
            }
            this.n = true;
            try {
                if (this.d != null && this.c == null) {
                    a(this.d);
                }
            } catch (Throwable th4) {
                en.a(th4, "GeoFenceManger", "init 4");
            }
            if (!A) {
                A = true;
                er.a(this.b, "O020", (JSONObject) null);
            }
        }
    }

    public final PendingIntent a(String str) {
        synchronized (this.z) {
            try {
                Intent intent = new Intent(str);
                intent.setPackage(k.c(this.b));
                this.c = PendingIntent.getBroadcast(this.b, 0, intent, 0);
                this.d = str;
                if (this.g != null && !this.g.isEmpty()) {
                    Iterator<GeoFence> it = this.g.iterator();
                    while (it.hasNext()) {
                        GeoFence next = it.next();
                        next.setPendingIntent(this.c);
                        next.setPendingIntentAction(this.d);
                    }
                }
            } catch (Throwable th) {
                en.a(th, "GeoFenceManager", "createPendingIntent");
            }
        }
        return this.c;
    }

    /* access modifiers changed from: package-private */
    public final GeoFence a(Bundle bundle, boolean z2) {
        ArrayList arrayList;
        float f2 = 1000.0f;
        GeoFence geoFence = new GeoFence();
        ArrayList arrayList2 = new ArrayList();
        DPoint dPoint = new DPoint();
        if (z2) {
            geoFence.setType(1);
            arrayList = bundle.getParcelableArrayList("pointList");
            if (arrayList != null) {
                dPoint = b((List<DPoint>) arrayList);
            }
            geoFence.setMaxDis2Center(b(dPoint, (List<DPoint>) arrayList));
            geoFence.setMinDis2Center(a(dPoint, (List<DPoint>) arrayList));
        } else {
            geoFence.setType(0);
            dPoint = (DPoint) bundle.getParcelable("centerPoint");
            if (dPoint != null) {
                arrayList2.add(dPoint);
            }
            float f3 = bundle.getFloat("fenceRadius", 1000.0f);
            if (f3 > 0.0f) {
                f2 = f3;
            }
            geoFence.setRadius(f2);
            geoFence.setMinDis2Center(f2);
            geoFence.setMaxDis2Center(f2);
            arrayList = arrayList2;
        }
        geoFence.setActivatesAction(this.f);
        geoFence.setCustomId(bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID));
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(arrayList);
        geoFence.setPointList(arrayList3);
        geoFence.setCenter(dPoint);
        geoFence.setPendingIntentAction(this.d);
        geoFence.setExpiration(-1);
        geoFence.setPendingIntent(this.c);
        geoFence.setFenceId(new StringBuilder().append(c.a()).toString());
        if (this.a != null) {
            this.a.a(this.b, 2);
        }
        return geoFence;
    }

    public final void a() {
        try {
            this.o = false;
            a(10, (Bundle) null, 0);
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "removeGeoFence");
        }
    }

    public final void a(int i2) {
        try {
            k();
            if (i2 > 7 || i2 <= 0) {
                i2 = 1;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("activatesAction", i2);
            a(9, bundle, 0);
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "setActivateAction");
        }
    }

    /* access modifiers changed from: package-private */
    public final void a(int i2, Bundle bundle) {
        try {
            synchronized (this.j) {
                if (this.h != null) {
                    Message obtainMessage = this.h.obtainMessage();
                    obtainMessage.what = i2;
                    obtainMessage.setData(bundle);
                    this.h.sendMessage(obtainMessage);
                }
            }
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "sendResultHandlerMessage");
        }
    }

    /* access modifiers changed from: package-private */
    public final void a(int i2, Bundle bundle, long j2) {
        try {
            synchronized (this.i) {
                if (this.k != null) {
                    Message obtainMessage = this.k.obtainMessage();
                    obtainMessage.what = i2;
                    obtainMessage.setData(bundle);
                    this.k.sendMessageDelayed(obtainMessage, j2);
                }
            }
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "sendActionHandlerMessage");
        }
    }

    /* access modifiers changed from: package-private */
    public final void a(Bundle bundle) {
        int i2;
        if (bundle != null) {
            try {
                i2 = bundle.getInt("activatesAction", 1);
            } catch (Throwable th) {
                en.a(th, "GeoFenceManager", "doSetActivatesAction");
                return;
            }
        } else {
            i2 = 1;
        }
        if (this.f != i2) {
            if (this.g != null && !this.g.isEmpty()) {
                Iterator<GeoFence> it = this.g.iterator();
                while (it.hasNext()) {
                    GeoFence next = it.next();
                    next.setStatus(0);
                    next.setEnterTime(-1);
                }
            }
            d();
        }
        this.f = i2;
    }

    public final void a(GeoFenceListener geoFenceListener) {
        try {
            this.e = geoFenceListener;
        } catch (Throwable th) {
        }
    }

    /* access modifiers changed from: package-private */
    public final void a(AMapLocation aMapLocation) {
        try {
            if (!this.y && this.g != null && !this.g.isEmpty() && aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                Iterator<GeoFence> it = this.g.iterator();
                while (it.hasNext()) {
                    GeoFence next = it.next();
                    if (next.isAble() && b(aMapLocation, next) && a(next, this.f)) {
                        next.setCurrentLocation(aMapLocation);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("geoFence", next);
                        a(1001, bundle);
                    }
                }
            }
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "doCheckFence");
        }
    }

    public final void a(DPoint dPoint, float f2, String str) {
        try {
            k();
            Bundle bundle = new Bundle();
            bundle.putParcelable("centerPoint", dPoint);
            bundle.putFloat("fenceRadius", f2);
            bundle.putString(GeoFence.BUNDLE_KEY_CUSTOMID, str);
            a(0, bundle, 0);
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "addRoundGeoFence");
        }
    }

    public final void a(String str, String str2) {
        try {
            k();
            Bundle bundle = new Bundle();
            bundle.putString("keyWords", str);
            bundle.putString(GeoFence.BUNDLE_KEY_CUSTOMID, str2);
            a(4, bundle, 0);
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "addDistricetGeoFence");
        }
    }

    public final void a(String str, String str2, DPoint dPoint, float f2, int i2, String str3) {
        int i3 = 25;
        try {
            k();
            if (f2 <= 0.0f || f2 > 50000.0f) {
                f2 = 3000.0f;
            }
            int i4 = i2 <= 0 ? 10 : i2;
            if (i4 <= 25) {
                i3 = i4;
            }
            Bundle bundle = new Bundle();
            bundle.putString("keyWords", str);
            bundle.putString("poiType", str2);
            bundle.putParcelable("centerPoint", dPoint);
            bundle.putFloat("aroundRadius", f2);
            bundle.putInt("searchSize", i3);
            bundle.putString(GeoFence.BUNDLE_KEY_CUSTOMID, str3);
            a(3, bundle, 0);
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "addNearbyGeoFence");
        }
    }

    public final void a(String str, String str2, String str3, int i2, String str4) {
        int i3 = 25;
        try {
            k();
            int i4 = i2 <= 0 ? 10 : i2;
            if (i4 <= 25) {
                i3 = i4;
            }
            Bundle bundle = new Bundle();
            bundle.putString("keyWords", str);
            bundle.putString("poiType", str2);
            bundle.putString("city", str3);
            bundle.putInt("searchSize", i3);
            bundle.putString(GeoFence.BUNDLE_KEY_CUSTOMID, str4);
            a(2, bundle, 0);
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "addKeywordGeoFence");
        }
    }

    public final void a(String str, boolean z2) {
        try {
            k();
            Bundle bundle = new Bundle();
            bundle.putString("fid", str);
            bundle.putBoolean("ab", z2);
            a(12, bundle, 0);
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "setGeoFenceAble");
        }
    }

    public final void a(List<DPoint> list, String str) {
        try {
            k();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("pointList", new ArrayList(list));
            bundle.putString(GeoFence.BUNDLE_KEY_CUSTOMID, str);
            a(1, bundle, 0);
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "addPolygonGeoFence");
        }
    }

    public final boolean a(GeoFence geoFence) {
        try {
            if (this.g == null || this.g.isEmpty()) {
                this.o = false;
                a(10, (Bundle) null, 0);
                return true;
            } else if (!this.g.contains(geoFence)) {
                return false;
            } else {
                if (this.g.size() == 1) {
                    this.o = false;
                }
                Bundle bundle = new Bundle();
                bundle.putParcelable("fc", geoFence);
                a(11, bundle, 0);
                return true;
            }
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "removeGeoFence(GeoFence)");
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public final int b(GeoFence geoFence) {
        try {
            if (this.g == null) {
                this.g = new ArrayList<>();
            }
            if (this.g.contains(geoFence)) {
                return 17;
            }
            this.g.add(geoFence);
            return 0;
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "addGeoFence2List");
            a("添加围栏失败", 8, th.getMessage(), new String[0]);
            return 8;
        }
    }

    public final List<GeoFence> b() {
        try {
            if (this.g == null) {
                this.g = new ArrayList<>();
            }
            return (ArrayList) this.g.clone();
        } catch (Throwable th) {
            return new ArrayList();
        }
    }

    /* access modifiers changed from: package-private */
    public final void b(int i2) {
        try {
            synchronized (this.i) {
                if (this.k != null) {
                    this.k.removeMessages(i2);
                }
            }
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "removeActionHandlerMessage");
        }
    }

    /* access modifiers changed from: package-private */
    public final void b(Bundle bundle) {
        b(2, bundle);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void c() {
        /*
            r4 = this;
            r3 = 0
            boolean r0 = r4.n     // Catch:{ Throwable -> 0x008c }
            if (r0 != 0) goto L_0x0006
        L_0x0005:
            return
        L_0x0006:
            java.util.ArrayList<com.amap.api.fence.GeoFence> r0 = r4.g     // Catch:{ Throwable -> 0x008c }
            if (r0 == 0) goto L_0x0012
            java.util.ArrayList<com.amap.api.fence.GeoFence> r0 = r4.g     // Catch:{ Throwable -> 0x008c }
            r0.clear()     // Catch:{ Throwable -> 0x008c }
            r0 = 0
            r4.g = r0     // Catch:{ Throwable -> 0x008c }
        L_0x0012:
            boolean r0 = r4.o     // Catch:{ Throwable -> 0x008c }
            if (r0 != 0) goto L_0x0005
            java.lang.Object r1 = r4.i     // Catch:{ Throwable -> 0x0081 }
            monitor-enter(r1)     // Catch:{ Throwable -> 0x0081 }
            com.loc.a$a r0 = r4.k     // Catch:{ all -> 0x007e }
            if (r0 == 0) goto L_0x0023
            com.loc.a$a r0 = r4.k     // Catch:{ all -> 0x007e }
            r2 = 0
            r0.removeCallbacksAndMessages(r2)     // Catch:{ all -> 0x007e }
        L_0x0023:
            r0 = 0
            r4.k = r0     // Catch:{ all -> 0x007e }
            monitor-exit(r1)     // Catch:{ all -> 0x007e }
        L_0x0027:
            com.amap.api.location.AMapLocationClient r0 = r4.r     // Catch:{ Throwable -> 0x008c }
            if (r0 == 0) goto L_0x0035
            com.amap.api.location.AMapLocationClient r0 = r4.r     // Catch:{ Throwable -> 0x008c }
            r0.stopLocation()     // Catch:{ Throwable -> 0x008c }
            com.amap.api.location.AMapLocationClient r0 = r4.r     // Catch:{ Throwable -> 0x008c }
            r0.onDestroy()     // Catch:{ Throwable -> 0x008c }
        L_0x0035:
            r0 = 0
            r4.r = r0     // Catch:{ Throwable -> 0x008c }
            com.loc.a$b r0 = r4.l     // Catch:{ Throwable -> 0x008c }
            if (r0 == 0) goto L_0x0047
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x008c }
            r1 = 18
            if (r0 < r1) goto L_0x008e
            com.loc.a$b r0 = r4.l     // Catch:{ Throwable -> 0x008c }
            r0.quitSafely()     // Catch:{ Throwable -> 0x008c }
        L_0x0047:
            r0 = 0
            r4.l = r0     // Catch:{ Throwable -> 0x008c }
            r0 = 0
            r4.p = r0     // Catch:{ Throwable -> 0x008c }
            java.lang.Object r1 = r4.z     // Catch:{ Throwable -> 0x008c }
            monitor-enter(r1)     // Catch:{ Throwable -> 0x008c }
            android.app.PendingIntent r0 = r4.c     // Catch:{ all -> 0x0094 }
            if (r0 == 0) goto L_0x0059
            android.app.PendingIntent r0 = r4.c     // Catch:{ all -> 0x0094 }
            r0.cancel()     // Catch:{ all -> 0x0094 }
        L_0x0059:
            r0 = 0
            r4.c = r0     // Catch:{ all -> 0x0094 }
            monitor-exit(r1)     // Catch:{ all -> 0x0094 }
            java.lang.Object r1 = r4.j     // Catch:{ Throwable -> 0x009a }
            monitor-enter(r1)     // Catch:{ Throwable -> 0x009a }
            com.loc.a$c r0 = r4.h     // Catch:{ all -> 0x0097 }
            if (r0 == 0) goto L_0x006a
            com.loc.a$c r0 = r4.h     // Catch:{ all -> 0x0097 }
            r2 = 0
            r0.removeCallbacksAndMessages(r2)     // Catch:{ all -> 0x0097 }
        L_0x006a:
            r0 = 0
            r4.h = r0     // Catch:{ all -> 0x0097 }
            monitor-exit(r1)     // Catch:{ all -> 0x0097 }
        L_0x006e:
            com.loc.er r0 = r4.a     // Catch:{ Throwable -> 0x008c }
            if (r0 == 0) goto L_0x0079
            com.loc.er r0 = r4.a     // Catch:{ Throwable -> 0x008c }
            android.content.Context r1 = r4.b     // Catch:{ Throwable -> 0x008c }
            r0.b(r1)     // Catch:{ Throwable -> 0x008c }
        L_0x0079:
            r4.m = r3
            r4.n = r3
            goto L_0x0005
        L_0x007e:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x007e }
            throw r0     // Catch:{ Throwable -> 0x0081 }
        L_0x0081:
            r0 = move-exception
            java.lang.String r1 = "GeoFenceManager"
            java.lang.String r2 = "destroyActionHandler"
            com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x008c }
            goto L_0x0027
        L_0x008c:
            r0 = move-exception
            goto L_0x0079
        L_0x008e:
            com.loc.a$b r0 = r4.l     // Catch:{ Throwable -> 0x008c }
            r0.quit()     // Catch:{ Throwable -> 0x008c }
            goto L_0x0047
        L_0x0094:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0094 }
            throw r0     // Catch:{ Throwable -> 0x008c }
        L_0x0097:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0097 }
            throw r0     // Catch:{ Throwable -> 0x009a }
        L_0x009a:
            r0 = move-exception
            java.lang.String r1 = "GeoFenceManager"
            java.lang.String r2 = "destroyResultHandler"
            com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x008c }
            goto L_0x006e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.a.c():void");
    }

    /* access modifiers changed from: package-private */
    public final void c(int i2) {
        try {
            if (this.b != null) {
                synchronized (this.z) {
                    if (this.c != null) {
                        Intent intent = new Intent();
                        intent.putExtras(a((GeoFence) null, (String) null, (String) null, 4, i2));
                        this.c.send(this.b, 0, intent);
                    }
                }
            }
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "resultRemindLocationError");
        }
    }

    /* access modifiers changed from: package-private */
    public final void c(Bundle bundle) {
        b(1, bundle);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void c(com.amap.api.fence.GeoFence r7) {
        /*
            r6 = this;
            java.lang.Object r1 = r6.z     // Catch:{ Throwable -> 0x0053 }
            monitor-enter(r1)     // Catch:{ Throwable -> 0x0053 }
            android.content.Context r0 = r6.b     // Catch:{ all -> 0x0050 }
            if (r0 == 0) goto L_0x004e
            android.app.PendingIntent r0 = r6.c     // Catch:{ all -> 0x0050 }
            if (r0 != 0) goto L_0x0013
            android.app.PendingIntent r0 = r7.getPendingIntent()     // Catch:{ all -> 0x0050 }
            if (r0 != 0) goto L_0x0013
            monitor-exit(r1)     // Catch:{ all -> 0x0050 }
        L_0x0012:
            return
        L_0x0013:
            android.content.Intent r0 = new android.content.Intent     // Catch:{ all -> 0x0050 }
            r0.<init>()     // Catch:{ all -> 0x0050 }
            java.lang.String r2 = r7.getFenceId()     // Catch:{ all -> 0x0050 }
            java.lang.String r3 = r7.getCustomId()     // Catch:{ all -> 0x0050 }
            int r4 = r7.getStatus()     // Catch:{ all -> 0x0050 }
            r5 = 0
            android.os.Bundle r2 = a((com.amap.api.fence.GeoFence) r7, (java.lang.String) r2, (java.lang.String) r3, (int) r4, (int) r5)     // Catch:{ all -> 0x0050 }
            r0.putExtras(r2)     // Catch:{ all -> 0x0050 }
            java.lang.String r2 = r6.d     // Catch:{ all -> 0x0050 }
            if (r2 == 0) goto L_0x0035
            java.lang.String r2 = r6.d     // Catch:{ all -> 0x0050 }
            r0.setAction(r2)     // Catch:{ all -> 0x0050 }
        L_0x0035:
            android.content.Context r2 = r6.b     // Catch:{ all -> 0x0050 }
            java.lang.String r2 = com.loc.k.c(r2)     // Catch:{ all -> 0x0050 }
            r0.setPackage(r2)     // Catch:{ all -> 0x0050 }
            android.app.PendingIntent r2 = r7.getPendingIntent()     // Catch:{ all -> 0x0050 }
            if (r2 == 0) goto L_0x005e
            android.app.PendingIntent r2 = r7.getPendingIntent()     // Catch:{ all -> 0x0050 }
            android.content.Context r3 = r6.b     // Catch:{ all -> 0x0050 }
            r4 = 0
            r2.send(r3, r4, r0)     // Catch:{ all -> 0x0050 }
        L_0x004e:
            monitor-exit(r1)     // Catch:{ all -> 0x0050 }
            goto L_0x0012
        L_0x0050:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0050 }
            throw r0     // Catch:{ Throwable -> 0x0053 }
        L_0x0053:
            r0 = move-exception
            java.lang.String r1 = "GeoFenceManager"
            java.lang.String r2 = "resultTriggerGeoFence"
            com.loc.en.a(r0, r1, r2)
            goto L_0x0012
        L_0x005e:
            android.app.PendingIntent r2 = r6.c     // Catch:{ all -> 0x0050 }
            android.content.Context r3 = r6.b     // Catch:{ all -> 0x0050 }
            r4 = 0
            r2.send(r3, r4, r0)     // Catch:{ all -> 0x0050 }
            goto L_0x004e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.a.c(com.amap.api.fence.GeoFence):void");
    }

    /* access modifiers changed from: package-private */
    public final void d() {
        if (!this.y && this.k != null) {
            boolean z2 = false;
            if (this.s != null && et.a(this.s) && et.b() - this.t < 10000) {
                z2 = true;
            }
            if (z2) {
                a(6, (Bundle) null, 0);
                a(5, (Bundle) null, 0);
                return;
            }
            b(7);
            a(7, (Bundle) null, 0);
        }
    }

    /* access modifiers changed from: package-private */
    public final void d(Bundle bundle) {
        b(3, bundle);
    }

    /* access modifiers changed from: package-private */
    public final void e() {
        try {
            if (this.m) {
                b(8);
            }
            if (this.r != null) {
                this.r.stopLocation();
            }
            this.m = false;
        } catch (Throwable th) {
        }
    }

    /* access modifiers changed from: package-private */
    public final void e(Bundle bundle) {
        boolean z2;
        if (bundle != null) {
            try {
                if (!bundle.isEmpty()) {
                    String string = bundle.getString("fid");
                    if (!TextUtils.isEmpty(string)) {
                        boolean z3 = bundle.getBoolean("ab", true);
                        if (this.g != null && !this.g.isEmpty()) {
                            Iterator<GeoFence> it = this.g.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                GeoFence next = it.next();
                                if (next.getFenceId().equals(string)) {
                                    next.setAble(z3);
                                    break;
                                }
                            }
                        }
                        if (!z3) {
                            if (this.g != null && !this.g.isEmpty()) {
                                Iterator<GeoFence> it2 = this.g.iterator();
                                while (true) {
                                    if (it2.hasNext()) {
                                        if (it2.next().isAble()) {
                                            z2 = false;
                                            break;
                                        }
                                    } else {
                                        z2 = true;
                                        break;
                                    }
                                }
                            } else {
                                z2 = true;
                            }
                            if (z2) {
                                h();
                                return;
                            }
                            return;
                        }
                        d();
                    }
                }
            } catch (Throwable th) {
                en.a(th, "GeoFenceManager", "doSetGeoFenceAble");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void f() {
        float f2;
        try {
            if (!this.y && et.a(this.s)) {
                AMapLocation aMapLocation = this.s;
                ArrayList<GeoFence> arrayList = this.g;
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0 && arrayList != null && !arrayList.isEmpty()) {
                    DPoint dPoint = new DPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    Iterator<GeoFence> it = arrayList.iterator();
                    float f3 = Float.MAX_VALUE;
                    while (true) {
                        if (!it.hasNext()) {
                            f2 = f3;
                            break;
                        }
                        GeoFence next = it.next();
                        if (next.isAble()) {
                            float a2 = et.a(dPoint, next.getCenter());
                            if (a2 > next.getMinDis2Center() && a2 < next.getMaxDis2Center()) {
                                f2 = 0.0f;
                                break;
                            }
                            if (a2 > next.getMaxDis2Center()) {
                                f3 = Math.min(f3, a2 - next.getMaxDis2Center());
                            }
                            f3 = a2 < next.getMinDis2Center() ? Math.min(f3, next.getMinDis2Center() - a2) : f3;
                        }
                    }
                } else {
                    f2 = Float.MAX_VALUE;
                }
                if (f2 == Float.MAX_VALUE) {
                    return;
                }
                if (f2 < 1000.0f) {
                    b(7);
                    Bundle bundle = new Bundle();
                    bundle.putLong("interval", AbstractClientManager.BIND_SERVICE_TIMEOUT);
                    a(8, bundle, 500);
                } else if (f2 < 5000.0f) {
                    e();
                    b(7);
                    a(7, (Bundle) null, 10000);
                } else {
                    e();
                    b(7);
                    a(7, (Bundle) null, (long) (((f2 - 4000.0f) / 100.0f) * 1000.0f));
                }
            }
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "doCheckLocationPolicy");
        }
    }

    /* access modifiers changed from: package-private */
    public final void f(Bundle bundle) {
        try {
            if (this.g != null) {
                GeoFence geoFence = (GeoFence) bundle.getParcelable("fc");
                if (this.g.contains(geoFence)) {
                    this.g.remove(geoFence);
                }
                if (this.g.size() <= 0) {
                    c();
                } else {
                    d();
                }
            }
        } catch (Throwable th) {
        }
    }

    public final void g() {
        try {
            k();
            this.y = true;
            a(13, (Bundle) null, 0);
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "pauseGeoFence");
        }
    }

    /* access modifiers changed from: package-private */
    public final void h() {
        try {
            b(7);
            b(8);
            if (this.r != null) {
                this.r.stopLocation();
            }
            this.m = false;
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "doPauseGeoFence");
        }
    }

    public final void i() {
        try {
            k();
            if (this.y) {
                this.y = false;
                d();
            }
        } catch (Throwable th) {
            en.a(th, "GeoFenceManager", "resumeGeoFence");
        }
    }

    public final boolean j() {
        return this.y;
    }
}
