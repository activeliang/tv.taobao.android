package com.loc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import java.util.ArrayList;
import java.util.List;

/* compiled from: GpsLocation */
public final class g {
    static AMapLocation j = null;
    static long k = 0;
    static Object l = new Object();
    static long q = 0;
    static boolean t = false;
    static boolean u = false;
    private long A = 0;
    private int B = 0;
    /* access modifiers changed from: private */
    public int C = 0;
    /* access modifiers changed from: private */
    public GpsStatus D = null;
    private GpsStatus.Listener E = new GpsStatus.Listener() {
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void onGpsStatusChanged(int r5) {
            /*
                r4 = this;
                r1 = 0
                com.loc.g r0 = com.loc.g.this     // Catch:{ Throwable -> 0x0026 }
                android.location.LocationManager r0 = r0.b     // Catch:{ Throwable -> 0x0026 }
                if (r0 != 0) goto L_0x0008
            L_0x0007:
                return
            L_0x0008:
                com.loc.g r0 = com.loc.g.this     // Catch:{ Throwable -> 0x0026 }
                com.loc.g r2 = com.loc.g.this     // Catch:{ Throwable -> 0x0026 }
                android.location.LocationManager r2 = r2.b     // Catch:{ Throwable -> 0x0026 }
                com.loc.g r3 = com.loc.g.this     // Catch:{ Throwable -> 0x0026 }
                android.location.GpsStatus r3 = r3.D     // Catch:{ Throwable -> 0x0026 }
                android.location.GpsStatus r2 = r2.getGpsStatus(r3)     // Catch:{ Throwable -> 0x0026 }
                android.location.GpsStatus unused = r0.D = r2     // Catch:{ Throwable -> 0x0026 }
                switch(r5) {
                    case 1: goto L_0x0007;
                    case 2: goto L_0x001f;
                    case 3: goto L_0x0007;
                    case 4: goto L_0x0031;
                    default: goto L_0x001e;
                }     // Catch:{ Throwable -> 0x0026 }
            L_0x001e:
                goto L_0x0007
            L_0x001f:
                com.loc.g r0 = com.loc.g.this     // Catch:{ Throwable -> 0x0026 }
                r1 = 0
                int unused = r0.C = r1     // Catch:{ Throwable -> 0x0026 }
                goto L_0x0007
            L_0x0026:
                r0 = move-exception
                java.lang.String r1 = "GpsLocation"
                java.lang.String r2 = "onGpsStatusChanged"
                com.loc.en.a(r0, r1, r2)
                goto L_0x0007
            L_0x0031:
                com.loc.g r0 = com.loc.g.this     // Catch:{ Throwable -> 0x006b }
                android.location.GpsStatus r0 = r0.D     // Catch:{ Throwable -> 0x006b }
                if (r0 == 0) goto L_0x0075
                com.loc.g r0 = com.loc.g.this     // Catch:{ Throwable -> 0x006b }
                android.location.GpsStatus r0 = r0.D     // Catch:{ Throwable -> 0x006b }
                java.lang.Iterable r0 = r0.getSatellites()     // Catch:{ Throwable -> 0x006b }
                if (r0 == 0) goto L_0x0075
                java.util.Iterator r2 = r0.iterator()     // Catch:{ Throwable -> 0x006b }
                com.loc.g r0 = com.loc.g.this     // Catch:{ Throwable -> 0x006b }
                android.location.GpsStatus r0 = r0.D     // Catch:{ Throwable -> 0x006b }
                int r3 = r0.getMaxSatellites()     // Catch:{ Throwable -> 0x006b }
            L_0x0053:
                boolean r0 = r2.hasNext()     // Catch:{ Throwable -> 0x006b }
                if (r0 == 0) goto L_0x0075
                if (r1 >= r3) goto L_0x0075
                java.lang.Object r0 = r2.next()     // Catch:{ Throwable -> 0x006b }
                android.location.GpsSatellite r0 = (android.location.GpsSatellite) r0     // Catch:{ Throwable -> 0x006b }
                boolean r0 = r0.usedInFix()     // Catch:{ Throwable -> 0x006b }
                if (r0 == 0) goto L_0x007b
                int r0 = r1 + 1
            L_0x0069:
                r1 = r0
                goto L_0x0053
            L_0x006b:
                r0 = move-exception
                java.lang.String r2 = "GpsLocation"
                java.lang.String r3 = "GPS_EVENT_SATELLITE_STATUS"
                com.loc.en.a(r0, r2, r3)     // Catch:{ Throwable -> 0x0026 }
            L_0x0075:
                com.loc.g r0 = com.loc.g.this     // Catch:{ Throwable -> 0x0026 }
                int unused = r0.C = r1     // Catch:{ Throwable -> 0x0026 }
                goto L_0x0007
            L_0x007b:
                r0 = r1
                goto L_0x0069
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.g.AnonymousClass1.onGpsStatusChanged(int):void");
        }
    };
    private String F = null;
    private boolean G = false;
    private int H = 0;
    private boolean I = false;
    Handler a;
    LocationManager b;
    AMapLocationClientOption c;
    long d = 0;
    boolean e = false;
    dv f = null;
    int g = 240;
    int h = 80;
    AMapLocation i = null;
    long m = 0;
    float n = 0.0f;
    Object o = new Object();
    Object p = new Object();
    AMapLocationClientOption.GeoLanguage r = AMapLocationClientOption.GeoLanguage.DEFAULT;
    boolean s = true;
    long v = 0;
    int w = 0;
    LocationListener x = null;
    public AMapLocation y = null;
    private Context z;

    /* compiled from: GpsLocation */
    static class a implements LocationListener {
        private g a;

        a(g gVar) {
            this.a = gVar;
        }

        /* access modifiers changed from: package-private */
        public final void a() {
            this.a = null;
        }

        public final void onLocationChanged(Location location) {
            try {
                if (this.a != null) {
                    g.a(this.a, location);
                }
            } catch (Throwable th) {
            }
        }

        public final void onProviderDisabled(String str) {
            try {
                if (this.a != null) {
                    g.a(this.a, str);
                }
            } catch (Throwable th) {
            }
        }

        public final void onProviderEnabled(String str) {
        }

        public final void onStatusChanged(String str, int i, Bundle bundle) {
            try {
                if (this.a != null) {
                    g.a(this.a, i);
                }
            } catch (Throwable th) {
            }
        }
    }

    public g(Context context, Handler handler) {
        this.z = context;
        this.a = handler;
        try {
            this.b = (LocationManager) this.z.getSystemService("location");
        } catch (Throwable th) {
            en.a(th, "GpsLocation", "<init>");
        }
        this.f = new dv();
    }

    private void a(int i2, int i3, String str, long j2) {
        try {
            if (this.a != null && this.c.getLocationMode() == AMapLocationClientOption.AMapLocationMode.Device_Sensors) {
                Message obtain = Message.obtain();
                AMapLocation aMapLocation = new AMapLocation("");
                aMapLocation.setProvider("gps");
                aMapLocation.setErrorCode(i3);
                aMapLocation.setLocationDetail(str);
                aMapLocation.setLocationType(1);
                obtain.obj = aMapLocation;
                obtain.what = i2;
                this.a.sendMessageDelayed(obtain, j2);
            }
        } catch (Throwable th) {
        }
    }

    private void a(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() == 15 && !AMapLocationClientOption.AMapLocationMode.Device_Sensors.equals(this.c.getLocationMode())) {
            return;
        }
        if (this.c.getLocationMode().equals(AMapLocationClientOption.AMapLocationMode.Device_Sensors) && this.c.getDeviceModeDistanceFilter() > 0.0f) {
            b(aMapLocation);
        } else if (et.b() - this.v >= this.c.getInterval() - 200) {
            this.v = et.b();
            b(aMapLocation);
        }
    }

    static /* synthetic */ void a(g gVar, int i2) {
        if (i2 == 0) {
            try {
                gVar.d = 0;
                gVar.C = 0;
            } catch (Throwable th) {
            }
        }
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        	at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        	at java.util.ArrayList.get(ArrayList.java:433)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x0225 A[SYNTHETIC, Splitter:B:107:0x0225] */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x0238 A[SYNTHETIC, Splitter:B:114:0x0238] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0102 A[Catch:{ Throwable -> 0x022f }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0126 A[Catch:{ Throwable -> 0x0218, Throwable -> 0x009b }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0132 A[Catch:{ Throwable -> 0x0218, Throwable -> 0x009b }] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0139 A[Catch:{ Throwable -> 0x0218, Throwable -> 0x009b }] */
    static /* synthetic */ void a(com.loc.g r9, android.location.Location r10) {
        /*
            r8 = 0
            android.os.Handler r0 = r9.a
            if (r0 == 0) goto L_0x000c
            android.os.Handler r0 = r9.a
            r1 = 8
            r0.removeMessages(r1)
        L_0x000c:
            if (r10 != 0) goto L_0x000f
        L_0x000e:
            return
        L_0x000f:
            com.amap.api.location.AMapLocation r0 = new com.amap.api.location.AMapLocation     // Catch:{ Throwable -> 0x009b }
            r0.<init>((android.location.Location) r10)     // Catch:{ Throwable -> 0x009b }
            boolean r1 = com.loc.et.a((com.amap.api.location.AMapLocation) r0)     // Catch:{ Throwable -> 0x009b }
            if (r1 == 0) goto L_0x000e
            java.lang.String r1 = "gps"
            r0.setProvider(r1)     // Catch:{ Throwable -> 0x009b }
            r1 = 1
            r0.setLocationType(r1)     // Catch:{ Throwable -> 0x009b }
            boolean r1 = r9.e     // Catch:{ Throwable -> 0x009b }
            if (r1 != 0) goto L_0x0049
            boolean r1 = com.loc.et.a((com.amap.api.location.AMapLocation) r0)     // Catch:{ Throwable -> 0x009b }
            if (r1 == 0) goto L_0x0049
            android.content.Context r1 = r9.z     // Catch:{ Throwable -> 0x009b }
            long r2 = com.loc.et.b()     // Catch:{ Throwable -> 0x009b }
            long r4 = r9.A     // Catch:{ Throwable -> 0x009b }
            long r2 = r2 - r4
            double r4 = r0.getLatitude()     // Catch:{ Throwable -> 0x009b }
            double r6 = r0.getLongitude()     // Catch:{ Throwable -> 0x009b }
            boolean r4 = com.loc.en.a((double) r4, (double) r6)     // Catch:{ Throwable -> 0x009b }
            com.loc.er.a((android.content.Context) r1, (long) r2, (boolean) r4)     // Catch:{ Throwable -> 0x009b }
            r1 = 1
            r9.e = r1     // Catch:{ Throwable -> 0x009b }
        L_0x0049:
            int r1 = r9.C     // Catch:{ Throwable -> 0x009b }
            boolean r1 = com.loc.et.a((android.location.Location) r10, (int) r1)     // Catch:{ Throwable -> 0x009b }
            if (r1 == 0) goto L_0x00af
            r1 = 1
            r0.setMock(r1)     // Catch:{ Throwable -> 0x009b }
            r1 = 4
            r0.setTrustedLevel(r1)     // Catch:{ Throwable -> 0x009b }
            com.amap.api.location.AMapLocationClientOption r1 = r9.c     // Catch:{ Throwable -> 0x009b }
            boolean r1 = r1.isMockEnable()     // Catch:{ Throwable -> 0x009b }
            if (r1 != 0) goto L_0x00b2
            int r1 = r9.w     // Catch:{ Throwable -> 0x009b }
            r2 = 3
            if (r1 <= r2) goto L_0x00a7
            r1 = 0
            r2 = 2152(0x868, float:3.016E-42)
            com.loc.er.a((java.lang.String) r1, (int) r2)     // Catch:{ Throwable -> 0x009b }
            r1 = 15
            r0.setErrorCode(r1)     // Catch:{ Throwable -> 0x009b }
            java.lang.String r1 = "GpsLocation has been mocked!#1501"
            r0.setLocationDetail(r1)     // Catch:{ Throwable -> 0x009b }
            r2 = 0
            r0.setLatitude(r2)     // Catch:{ Throwable -> 0x009b }
            r2 = 0
            r0.setLongitude(r2)     // Catch:{ Throwable -> 0x009b }
            r2 = 0
            r0.setAltitude(r2)     // Catch:{ Throwable -> 0x009b }
            r1 = 0
            r0.setSpeed(r1)     // Catch:{ Throwable -> 0x009b }
            r1 = 0
            r0.setAccuracy(r1)     // Catch:{ Throwable -> 0x009b }
            r1 = 0
            r0.setBearing(r1)     // Catch:{ Throwable -> 0x009b }
            r1 = 0
            r0.setExtras(r1)     // Catch:{ Throwable -> 0x009b }
            r9.a((com.amap.api.location.AMapLocation) r0)     // Catch:{ Throwable -> 0x009b }
            goto L_0x000e
        L_0x009b:
            r0 = move-exception
            java.lang.String r1 = "GpsLocation"
            java.lang.String r2 = "onLocationChanged"
            com.loc.en.a(r0, r1, r2)
            goto L_0x000e
        L_0x00a7:
            int r0 = r9.w     // Catch:{ Throwable -> 0x009b }
            int r0 = r0 + 1
            r9.w = r0     // Catch:{ Throwable -> 0x009b }
            goto L_0x000e
        L_0x00af:
            r1 = 0
            r9.w = r1     // Catch:{ Throwable -> 0x009b }
        L_0x00b2:
            int r1 = r9.C     // Catch:{ Throwable -> 0x009b }
            r0.setSatellites(r1)     // Catch:{ Throwable -> 0x009b }
            double r2 = r0.getLatitude()     // Catch:{ Throwable -> 0x0218 }
            double r4 = r0.getLongitude()     // Catch:{ Throwable -> 0x0218 }
            boolean r1 = com.loc.en.a((double) r2, (double) r4)     // Catch:{ Throwable -> 0x0218 }
            if (r1 == 0) goto L_0x020c
            com.amap.api.location.AMapLocationClientOption r1 = r9.c     // Catch:{ Throwable -> 0x0218 }
            boolean r1 = r1.isOffset()     // Catch:{ Throwable -> 0x0218 }
            if (r1 == 0) goto L_0x020c
            android.content.Context r1 = r9.z     // Catch:{ Throwable -> 0x0218 }
            com.amap.api.location.DPoint r2 = new com.amap.api.location.DPoint     // Catch:{ Throwable -> 0x0218 }
            double r4 = r0.getLatitude()     // Catch:{ Throwable -> 0x0218 }
            double r6 = r0.getLongitude()     // Catch:{ Throwable -> 0x0218 }
            r2.<init>(r4, r6)     // Catch:{ Throwable -> 0x0218 }
            com.amap.api.location.DPoint r1 = com.loc.ep.a((android.content.Context) r1, (com.amap.api.location.DPoint) r2)     // Catch:{ Throwable -> 0x0218 }
            double r2 = r1.getLatitude()     // Catch:{ Throwable -> 0x0218 }
            r0.setLatitude(r2)     // Catch:{ Throwable -> 0x0218 }
            double r2 = r1.getLongitude()     // Catch:{ Throwable -> 0x0218 }
            r0.setLongitude(r2)     // Catch:{ Throwable -> 0x0218 }
            com.amap.api.location.AMapLocationClientOption r1 = r9.c     // Catch:{ Throwable -> 0x0218 }
            boolean r1 = r1.isOffset()     // Catch:{ Throwable -> 0x0218 }
            r0.setOffset(r1)     // Catch:{ Throwable -> 0x0218 }
            java.lang.String r1 = "GCJ02"
            r0.setCoordType(r1)     // Catch:{ Throwable -> 0x0218 }
        L_0x00fd:
            int r1 = r9.C     // Catch:{ Throwable -> 0x022f }
            r2 = 4
            if (r1 < r2) goto L_0x0225
            r1 = 1
            r0.setGpsAccuracyStatus(r1)     // Catch:{ Throwable -> 0x022f }
        L_0x0106:
            boolean r1 = com.loc.et.a((com.amap.api.location.AMapLocation) r0)     // Catch:{ Throwable -> 0x009b }
            if (r1 == 0) goto L_0x012c
            boolean r1 = com.loc.em.z()     // Catch:{ Throwable -> 0x009b }
            if (r1 == 0) goto L_0x012c
            long r2 = r0.getTime()     // Catch:{ Throwable -> 0x009b }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x009b }
            int r1 = com.loc.em.A()     // Catch:{ Throwable -> 0x009b }
            long r6 = com.loc.eo.a(r2, r4, r1)     // Catch:{ Throwable -> 0x009b }
            int r1 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r1 == 0) goto L_0x012c
            r0.setTime(r6)     // Catch:{ Throwable -> 0x009b }
            com.loc.er.a((long) r2, (long) r4)     // Catch:{ Throwable -> 0x009b }
        L_0x012c:
            boolean r1 = com.loc.et.a((com.amap.api.location.AMapLocation) r0)     // Catch:{ Throwable -> 0x009b }
            if (r1 != 0) goto L_0x0238
            r1 = r0
        L_0x0133:
            boolean r0 = com.loc.et.a((com.amap.api.location.AMapLocation) r1)     // Catch:{ Throwable -> 0x009b }
            if (r0 == 0) goto L_0x0155
            long r2 = com.loc.et.b()     // Catch:{ Throwable -> 0x009b }
            r9.d = r2     // Catch:{ Throwable -> 0x009b }
            java.lang.Object r2 = l     // Catch:{ Throwable -> 0x009b }
            monitor-enter(r2)     // Catch:{ Throwable -> 0x009b }
            long r4 = com.loc.et.b()     // Catch:{ all -> 0x0277 }
            k = r4     // Catch:{ all -> 0x0277 }
            com.amap.api.location.AMapLocation r0 = r1.clone()     // Catch:{ all -> 0x0277 }
            j = r0     // Catch:{ all -> 0x0277 }
            monitor-exit(r2)     // Catch:{ all -> 0x0277 }
            int r0 = r9.B     // Catch:{ Throwable -> 0x009b }
            int r0 = r0 + 1
            r9.B = r0     // Catch:{ Throwable -> 0x009b }
        L_0x0155:
            boolean r0 = com.loc.et.a((com.amap.api.location.AMapLocation) r1)     // Catch:{ Throwable -> 0x009b }
            if (r0 == 0) goto L_0x01b7
            android.os.Handler r0 = r9.a     // Catch:{ Throwable -> 0x009b }
            if (r0 == 0) goto L_0x01b7
            com.amap.api.location.AMapLocationClientOption r0 = r9.c     // Catch:{ Throwable -> 0x009b }
            boolean r0 = r0.isNeedAddress()     // Catch:{ Throwable -> 0x009b }
            if (r0 == 0) goto L_0x01b7
            long r2 = com.loc.et.b()     // Catch:{ Throwable -> 0x009b }
            com.amap.api.location.AMapLocationClientOption r0 = r9.c     // Catch:{ Throwable -> 0x009b }
            long r4 = r0.getInterval()     // Catch:{ Throwable -> 0x009b }
            r6 = 8000(0x1f40, double:3.9525E-320)
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r0 <= 0) goto L_0x0187
            long r4 = r9.v     // Catch:{ Throwable -> 0x009b }
            long r2 = r2 - r4
            com.amap.api.location.AMapLocationClientOption r0 = r9.c     // Catch:{ Throwable -> 0x009b }
            long r4 = r0.getInterval()     // Catch:{ Throwable -> 0x009b }
            r6 = 8000(0x1f40, double:3.9525E-320)
            long r4 = r4 - r6
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x01b7
        L_0x0187:
            android.os.Bundle r0 = new android.os.Bundle     // Catch:{ Throwable -> 0x009b }
            r0.<init>()     // Catch:{ Throwable -> 0x009b }
            java.lang.String r2 = "lat"
            double r4 = r1.getLatitude()     // Catch:{ Throwable -> 0x009b }
            r0.putDouble(r2, r4)     // Catch:{ Throwable -> 0x009b }
            java.lang.String r2 = "lon"
            double r4 = r1.getLongitude()     // Catch:{ Throwable -> 0x009b }
            r0.putDouble(r2, r4)     // Catch:{ Throwable -> 0x009b }
            android.os.Message r2 = android.os.Message.obtain()     // Catch:{ Throwable -> 0x009b }
            r2.setData(r0)     // Catch:{ Throwable -> 0x009b }
            r0 = 5
            r2.what = r0     // Catch:{ Throwable -> 0x009b }
            java.lang.Object r3 = r9.o     // Catch:{ Throwable -> 0x009b }
            monitor-enter(r3)     // Catch:{ Throwable -> 0x009b }
            com.amap.api.location.AMapLocation r0 = r9.y     // Catch:{ all -> 0x028e }
            if (r0 != 0) goto L_0x027a
            android.os.Handler r0 = r9.a     // Catch:{ all -> 0x028e }
            r0.sendMessage(r2)     // Catch:{ all -> 0x028e }
        L_0x01b6:
            monitor-exit(r3)     // Catch:{ all -> 0x028e }
        L_0x01b7:
            java.lang.Object r2 = r9.o     // Catch:{ Throwable -> 0x009b }
            monitor-enter(r2)     // Catch:{ Throwable -> 0x009b }
            com.amap.api.location.AMapLocation r0 = r9.y     // Catch:{ all -> 0x0291 }
            if (r0 == 0) goto L_0x01d4
            com.amap.api.location.AMapLocationClientOption r3 = r9.c     // Catch:{ all -> 0x0291 }
            boolean r3 = r3.isNeedAddress()     // Catch:{ all -> 0x0291 }
            if (r3 == 0) goto L_0x01d4
            float r3 = com.loc.et.a((com.amap.api.location.AMapLocation) r1, (com.amap.api.location.AMapLocation) r0)     // Catch:{ all -> 0x0291 }
            int r4 = r9.g     // Catch:{ all -> 0x0291 }
            float r4 = (float) r4     // Catch:{ all -> 0x0291 }
            int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
            if (r3 >= 0) goto L_0x01d4
            com.loc.en.a((com.amap.api.location.AMapLocation) r1, (com.amap.api.location.AMapLocation) r0)     // Catch:{ all -> 0x0291 }
        L_0x01d4:
            monitor-exit(r2)     // Catch:{ all -> 0x0291 }
            boolean r0 = com.loc.et.a((com.amap.api.location.AMapLocation) r1)     // Catch:{ Throwable -> 0x0297 }
            if (r0 == 0) goto L_0x0207
            com.amap.api.location.AMapLocation r0 = r9.i     // Catch:{ Throwable -> 0x0297 }
            if (r0 == 0) goto L_0x01f4
            long r2 = r10.getTime()     // Catch:{ Throwable -> 0x0297 }
            com.amap.api.location.AMapLocation r0 = r9.i     // Catch:{ Throwable -> 0x0297 }
            long r4 = r0.getTime()     // Catch:{ Throwable -> 0x0297 }
            long r2 = r2 - r4
            r9.m = r2     // Catch:{ Throwable -> 0x0297 }
            com.amap.api.location.AMapLocation r0 = r9.i     // Catch:{ Throwable -> 0x0297 }
            float r0 = com.loc.et.a((com.amap.api.location.AMapLocation) r0, (com.amap.api.location.AMapLocation) r1)     // Catch:{ Throwable -> 0x0297 }
            r9.n = r0     // Catch:{ Throwable -> 0x0297 }
        L_0x01f4:
            java.lang.Object r2 = r9.p     // Catch:{ Throwable -> 0x0297 }
            monitor-enter(r2)     // Catch:{ Throwable -> 0x0297 }
            com.amap.api.location.AMapLocation r0 = r1.clone()     // Catch:{ all -> 0x0294 }
            r9.i = r0     // Catch:{ all -> 0x0294 }
            monitor-exit(r2)     // Catch:{ all -> 0x0294 }
            r0 = 0
            r9.F = r0     // Catch:{ Throwable -> 0x0297 }
            r0 = 0
            r9.G = r0     // Catch:{ Throwable -> 0x0297 }
            r0 = 0
            r9.H = r0     // Catch:{ Throwable -> 0x0297 }
        L_0x0207:
            r9.a((com.amap.api.location.AMapLocation) r1)     // Catch:{ Throwable -> 0x009b }
            goto L_0x000e
        L_0x020c:
            r1 = 0
            r0.setOffset(r1)     // Catch:{ Throwable -> 0x0218 }
            java.lang.String r1 = "WGS84"
            r0.setCoordType(r1)     // Catch:{ Throwable -> 0x0218 }
            goto L_0x00fd
        L_0x0218:
            r1 = move-exception
            r1 = 0
            r0.setOffset(r1)     // Catch:{ Throwable -> 0x009b }
            java.lang.String r1 = "WGS84"
            r0.setCoordType(r1)     // Catch:{ Throwable -> 0x009b }
            goto L_0x00fd
        L_0x0225:
            int r1 = r9.C     // Catch:{ Throwable -> 0x022f }
            if (r1 != 0) goto L_0x0232
            r1 = -1
            r0.setGpsAccuracyStatus(r1)     // Catch:{ Throwable -> 0x022f }
            goto L_0x0106
        L_0x022f:
            r1 = move-exception
            goto L_0x0106
        L_0x0232:
            r1 = 0
            r0.setGpsAccuracyStatus(r1)     // Catch:{ Throwable -> 0x022f }
            goto L_0x0106
        L_0x0238:
            int r1 = r9.B     // Catch:{ Throwable -> 0x009b }
            r2 = 3
            if (r1 >= r2) goto L_0x0240
            r1 = r0
            goto L_0x0133
        L_0x0240:
            float r1 = r0.getAccuracy()     // Catch:{ Throwable -> 0x009b }
            int r1 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r1 < 0) goto L_0x0253
            float r1 = r0.getAccuracy()     // Catch:{ Throwable -> 0x009b }
            r2 = 2139095039(0x7f7fffff, float:3.4028235E38)
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 != 0) goto L_0x0257
        L_0x0253:
            r1 = 0
            r0.setAccuracy(r1)     // Catch:{ Throwable -> 0x009b }
        L_0x0257:
            float r1 = r0.getSpeed()     // Catch:{ Throwable -> 0x009b }
            int r1 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r1 < 0) goto L_0x026a
            float r1 = r0.getSpeed()     // Catch:{ Throwable -> 0x009b }
            r2 = 2139095039(0x7f7fffff, float:3.4028235E38)
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 != 0) goto L_0x026e
        L_0x026a:
            r1 = 0
            r0.setSpeed(r1)     // Catch:{ Throwable -> 0x009b }
        L_0x026e:
            com.loc.dv r1 = r9.f     // Catch:{ Throwable -> 0x009b }
            com.amap.api.location.AMapLocation r0 = r1.a((com.amap.api.location.AMapLocation) r0)     // Catch:{ Throwable -> 0x009b }
            r1 = r0
            goto L_0x0133
        L_0x0277:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0277 }
            throw r0     // Catch:{ Throwable -> 0x009b }
        L_0x027a:
            com.amap.api.location.AMapLocation r0 = r9.y     // Catch:{ all -> 0x028e }
            float r0 = com.loc.et.a((com.amap.api.location.AMapLocation) r1, (com.amap.api.location.AMapLocation) r0)     // Catch:{ all -> 0x028e }
            int r4 = r9.h     // Catch:{ all -> 0x028e }
            float r4 = (float) r4     // Catch:{ all -> 0x028e }
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x01b6
            android.os.Handler r0 = r9.a     // Catch:{ all -> 0x028e }
            r0.sendMessage(r2)     // Catch:{ all -> 0x028e }
            goto L_0x01b6
        L_0x028e:
            r0 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x028e }
            throw r0     // Catch:{ Throwable -> 0x009b }
        L_0x0291:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0291 }
            throw r0     // Catch:{ Throwable -> 0x009b }
        L_0x0294:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0294 }
            throw r0     // Catch:{ Throwable -> 0x0297 }
        L_0x0297:
            r0 = move-exception
            java.lang.String r2 = "GpsLocation"
            java.lang.String r3 = "onLocationChangedLast"
            com.loc.en.a(r0, r2, r3)     // Catch:{ Throwable -> 0x009b }
            goto L_0x0207
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.g.a(com.loc.g, android.location.Location):void");
    }

    static /* synthetic */ void a(g gVar, String str) {
        try {
            if ("gps".equalsIgnoreCase(str)) {
                gVar.d = 0;
                gVar.C = 0;
            }
        } catch (Throwable th) {
        }
    }

    private static boolean a(LocationManager locationManager) {
        try {
            if (t) {
                return u;
            }
            List<String> allProviders = locationManager.getAllProviders();
            if (allProviders == null || allProviders.size() <= 0) {
                u = false;
            } else {
                u = allProviders.contains("gps");
            }
            t = true;
            return u;
        } catch (Throwable th) {
            return u;
        }
    }

    private boolean a(String str) {
        try {
            ArrayList<String> d2 = et.d(str);
            ArrayList<String> d3 = et.d(this.F);
            if (d2.size() < 8 || d3.size() < 8) {
                return false;
            }
            return et.a(this.F, str);
        } catch (Throwable th) {
            return false;
        }
    }

    private void b(AMapLocation aMapLocation) {
        if (this.a != null) {
            Message obtain = Message.obtain();
            obtain.obj = aMapLocation;
            obtain.what = 2;
            this.a.sendMessage(obtain);
        }
    }

    private static boolean e() {
        try {
            return ((Boolean) eq.a(u.c("KY29tLmFtYXAuYXBpLm5hdmkuQU1hcE5hdmk="), u.c("UaXNOYXZpU3RhcnRlZA=="), (Object[]) null, (Class<?>[]) null)).booleanValue();
        } catch (Throwable th) {
            return false;
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.amap.api.location.AMapLocation f() {
        /*
            r15 = this;
            r1 = 0
            r2 = 0
            com.amap.api.location.AMapLocation r0 = r15.i     // Catch:{ Throwable -> 0x0112 }
            boolean r0 = com.loc.et.a((com.amap.api.location.AMapLocation) r0)     // Catch:{ Throwable -> 0x0112 }
            if (r0 != 0) goto L_0x000c
            r0 = r2
        L_0x000b:
            return r0
        L_0x000c:
            boolean r0 = com.loc.em.p()     // Catch:{ Throwable -> 0x0112 }
            if (r0 != 0) goto L_0x0014
            r0 = r2
            goto L_0x000b
        L_0x0014:
            boolean r0 = e()     // Catch:{ Throwable -> 0x0112 }
            if (r0 == 0) goto L_0x0113
            java.lang.String r0 = "KY29tLmFtYXAuYXBpLm5hdmkuQU1hcE5hdmk="
            java.lang.String r0 = com.loc.u.c((java.lang.String) r0)     // Catch:{ Throwable -> 0x0112 }
            java.lang.String r3 = "UZ2V0TmF2aUxvY2F0aW9u"
            java.lang.String r3 = com.loc.u.c((java.lang.String) r3)     // Catch:{ Throwable -> 0x0112 }
            r4 = 0
            r5 = 0
            java.lang.Object r0 = com.loc.eq.a((java.lang.String) r0, (java.lang.String) r3, (java.lang.Object[]) r4, (java.lang.Class<?>[]) r5)     // Catch:{ Throwable -> 0x0112 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Throwable -> 0x0112 }
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Throwable -> 0x0112 }
            r3.<init>(r0)     // Catch:{ Throwable -> 0x0112 }
            java.lang.String r0 = "time"
            long r6 = r3.optLong(r0)     // Catch:{ Throwable -> 0x0112 }
            boolean r0 = r15.I     // Catch:{ Throwable -> 0x0112 }
            if (r0 != 0) goto L_0x004c
            r0 = 1
            r15.I = r0     // Catch:{ Throwable -> 0x0112 }
            java.lang.String r0 = "useNaviLoc"
            java.lang.String r4 = "use NaviLoc"
            com.loc.er.a((java.lang.String) r0, (java.lang.String) r4)     // Catch:{ Throwable -> 0x0112 }
        L_0x004c:
            long r4 = com.loc.et.a()     // Catch:{ Throwable -> 0x0112 }
            long r4 = r4 - r6
            r8 = 5500(0x157c, double:2.7174E-320)
            int r0 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r0 > 0) goto L_0x0113
            java.lang.String r0 = "lat"
            r4 = 0
            double r8 = r3.optDouble(r0, r4)     // Catch:{ Throwable -> 0x0112 }
            java.lang.String r0 = "lng"
            r4 = 0
            double r10 = r3.optDouble(r0, r4)     // Catch:{ Throwable -> 0x0112 }
            java.lang.String r0 = "accuracy"
            java.lang.String r4 = "0"
            java.lang.String r0 = r3.optString(r0, r4)     // Catch:{ NumberFormatException -> 0x0105 }
            float r0 = java.lang.Float.parseFloat(r0)     // Catch:{ NumberFormatException -> 0x0105 }
            r4 = r0
        L_0x0078:
            java.lang.String r0 = "altitude"
            r12 = 0
            double r12 = r3.optDouble(r0, r12)     // Catch:{ Throwable -> 0x0112 }
            java.lang.String r0 = "bearing"
            java.lang.String r5 = "0"
            java.lang.String r0 = r3.optString(r0, r5)     // Catch:{ NumberFormatException -> 0x0109 }
            float r0 = java.lang.Float.parseFloat(r0)     // Catch:{ NumberFormatException -> 0x0109 }
        L_0x008f:
            java.lang.String r5 = "speed"
            java.lang.String r14 = "0"
            java.lang.String r3 = r3.optString(r5, r14)     // Catch:{ NumberFormatException -> 0x010c }
            float r1 = java.lang.Float.parseFloat(r3)     // Catch:{ NumberFormatException -> 0x010c }
            r3 = 1092616192(0x41200000, float:10.0)
            float r1 = r1 * r3
            r3 = 1108344832(0x42100000, float:36.0)
            float r1 = r1 / r3
            r3 = r1
        L_0x00a4:
            com.amap.api.location.AMapLocation r1 = new com.amap.api.location.AMapLocation     // Catch:{ Throwable -> 0x0112 }
            java.lang.String r5 = "lbs"
            r1.<init>((java.lang.String) r5)     // Catch:{ Throwable -> 0x0112 }
            r5 = 9
            r1.setLocationType(r5)     // Catch:{ Throwable -> 0x0112 }
            r1.setLatitude(r8)     // Catch:{ Throwable -> 0x0112 }
            r1.setLongitude(r10)     // Catch:{ Throwable -> 0x0112 }
            r1.setAccuracy(r4)     // Catch:{ Throwable -> 0x0112 }
            r1.setAltitude(r12)     // Catch:{ Throwable -> 0x0112 }
            r1.setBearing(r0)     // Catch:{ Throwable -> 0x0112 }
            r1.setSpeed(r3)     // Catch:{ Throwable -> 0x0112 }
            r1.setTime(r6)     // Catch:{ Throwable -> 0x0112 }
            java.lang.String r5 = "GCJ02"
            r1.setCoordType(r5)     // Catch:{ Throwable -> 0x0112 }
            com.amap.api.location.AMapLocation r5 = r15.i     // Catch:{ Throwable -> 0x0112 }
            float r5 = com.loc.et.a((com.amap.api.location.AMapLocation) r1, (com.amap.api.location.AMapLocation) r5)     // Catch:{ Throwable -> 0x0112 }
            r12 = 1133903872(0x43960000, float:300.0)
            int r5 = (r5 > r12 ? 1 : (r5 == r12 ? 0 : -1))
            if (r5 > 0) goto L_0x0116
            java.lang.Object r5 = r15.p     // Catch:{ Throwable -> 0x0112 }
            monitor-enter(r5)     // Catch:{ Throwable -> 0x0112 }
            com.amap.api.location.AMapLocation r12 = r15.i     // Catch:{ all -> 0x010f }
            r12.setLongitude(r10)     // Catch:{ all -> 0x010f }
            com.amap.api.location.AMapLocation r10 = r15.i     // Catch:{ all -> 0x010f }
            r10.setLatitude(r8)     // Catch:{ all -> 0x010f }
            com.amap.api.location.AMapLocation r8 = r15.i     // Catch:{ all -> 0x010f }
            r8.setAccuracy(r4)     // Catch:{ all -> 0x010f }
            com.amap.api.location.AMapLocation r4 = r15.i     // Catch:{ all -> 0x010f }
            r4.setBearing(r0)     // Catch:{ all -> 0x010f }
            com.amap.api.location.AMapLocation r0 = r15.i     // Catch:{ all -> 0x010f }
            r0.setSpeed(r3)     // Catch:{ all -> 0x010f }
            com.amap.api.location.AMapLocation r0 = r15.i     // Catch:{ all -> 0x010f }
            r0.setTime(r6)     // Catch:{ all -> 0x010f }
            com.amap.api.location.AMapLocation r0 = r15.i     // Catch:{ all -> 0x010f }
            java.lang.String r3 = "GCJ02"
            r0.setCoordType(r3)     // Catch:{ all -> 0x010f }
            monitor-exit(r5)     // Catch:{ all -> 0x010f }
            r0 = r1
            goto L_0x000b
        L_0x0105:
            r0 = move-exception
            r4 = r1
            goto L_0x0078
        L_0x0109:
            r0 = move-exception
            r0 = r1
            goto L_0x008f
        L_0x010c:
            r3 = move-exception
            r3 = r1
            goto L_0x00a4
        L_0x010f:
            r0 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x010f }
            throw r0     // Catch:{ Throwable -> 0x0112 }
        L_0x0112:
            r0 = move-exception
        L_0x0113:
            r0 = r2
            goto L_0x000b
        L_0x0116:
            r0 = r2
            goto L_0x000b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.g.f():com.amap.api.location.AMapLocation");
    }

    public final AMapLocation a(AMapLocation aMapLocation, String str) {
        long j2;
        if (this.i == null) {
            return aMapLocation;
        }
        if ((!this.c.isMockEnable() && this.i.isMock()) || !et.a(this.i)) {
            return aMapLocation;
        }
        AMapLocation f2 = f();
        if (f2 == null || !et.a(f2)) {
            float speed = this.i.getSpeed();
            if (speed == 0.0f && this.m > 0 && this.m < 8 && this.n > 0.0f) {
                speed = this.n / ((float) this.m);
            }
            if (aMapLocation == null || !et.a(aMapLocation)) {
                j2 = 30000;
            } else if (aMapLocation.getAccuracy() < 200.0f) {
                this.H++;
                if (this.F == null && this.H >= 2) {
                    this.G = true;
                }
                j2 = speed > 5.0f ? 10000 : 15000;
            } else {
                if (!TextUtils.isEmpty(this.F)) {
                    this.G = false;
                    this.H = 0;
                }
                j2 = speed > 5.0f ? 20000 : 30000;
            }
            long b2 = et.b() - this.d;
            if (b2 > 30000) {
                return aMapLocation;
            }
            if (b2 < j2) {
                if (this.F == null && this.H >= 2) {
                    this.F = str;
                }
                AMapLocation clone = this.i.clone();
                clone.setTrustedLevel(2);
                return clone;
            } else if (!this.G || !a(str)) {
                this.F = null;
                this.H = 0;
                synchronized (this.p) {
                    this.i = null;
                }
                this.m = 0;
                this.n = 0.0f;
                return aMapLocation;
            } else {
                AMapLocation clone2 = this.i.clone();
                clone2.setTrustedLevel(3);
                return clone2;
            }
        } else {
            f2.setTrustedLevel(2);
            return f2;
        }
    }

    public final void a() {
        if (this.b != null) {
            try {
                if (this.x != null) {
                    this.b.removeUpdates(this.x);
                    ((a) this.x).a();
                    this.x = null;
                }
            } catch (Throwable th) {
            }
            try {
                if (this.E != null) {
                    this.b.removeGpsStatusListener(this.E);
                }
            } catch (Throwable th2) {
            }
            try {
                if (this.a != null) {
                    this.a.removeMessages(8);
                }
            } catch (Throwable th3) {
            }
            this.C = 0;
            this.A = 0;
            this.v = 0;
            this.d = 0;
            this.B = 0;
            this.w = 0;
            this.f.a();
            this.i = null;
            this.m = 0;
            this.n = 0.0f;
            this.F = null;
            this.I = false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00fa, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00fb, code lost:
        r12.s = false;
        com.loc.er.a((java.lang.String) null, 2121);
        a(2, 12, r0.getMessage() + "#1201", 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        return;
     */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00fa A[ExcHandler: SecurityException (r0v4 'e' java.lang.SecurityException A[CUSTOM_DECLARE]), Splitter:B:26:0x00a7] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void a(com.amap.api.location.AMapLocationClientOption r13) {
        /*
            r12 = this;
            r10 = 0
            r8 = 0
            r7 = 0
            r12.c = r13
            com.amap.api.location.AMapLocationClientOption r0 = r12.c
            if (r0 != 0) goto L_0x0011
            com.amap.api.location.AMapLocationClientOption r0 = new com.amap.api.location.AMapLocationClientOption
            r0.<init>()
            r12.c = r0
        L_0x0011:
            android.content.Context r0 = r12.z     // Catch:{ Throwable -> 0x0151 }
            java.lang.String r1 = "pref"
            java.lang.String r2 = "lagt"
            long r4 = q     // Catch:{ Throwable -> 0x0151 }
            long r0 = com.loc.es.a((android.content.Context) r0, (java.lang.String) r1, (java.lang.String) r2, (long) r4)     // Catch:{ Throwable -> 0x0151 }
            q = r0     // Catch:{ Throwable -> 0x0151 }
        L_0x0021:
            android.location.LocationManager r0 = r12.b
            if (r0 != 0) goto L_0x0026
        L_0x0025:
            return
        L_0x0026:
            long r0 = com.loc.et.b()     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            long r2 = k     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            long r0 = r0 - r2
            r2 = 5000(0x1388, double:2.4703E-320)
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 > 0) goto L_0x0056
            com.amap.api.location.AMapLocation r0 = j     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            boolean r0 = com.loc.et.a((com.amap.api.location.AMapLocation) r0)     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            if (r0 == 0) goto L_0x0056
            com.amap.api.location.AMapLocationClientOption r0 = r12.c     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            boolean r0 = r0.isMockEnable()     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            if (r0 != 0) goto L_0x004b
            com.amap.api.location.AMapLocation r0 = j     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            boolean r0 = r0.isMock()     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            if (r0 != 0) goto L_0x0056
        L_0x004b:
            long r0 = com.loc.et.b()     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            r12.d = r0     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            com.amap.api.location.AMapLocation r0 = j     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            r12.a((com.amap.api.location.AMapLocation) r0)     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
        L_0x0056:
            r0 = 1
            r12.s = r0     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            android.os.Looper r6 = android.os.Looper.myLooper()     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            if (r6 != 0) goto L_0x0065
            android.content.Context r0 = r12.z     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            android.os.Looper r6 = r0.getMainLooper()     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
        L_0x0065:
            long r0 = com.loc.et.b()     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            r12.A = r0     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            android.location.LocationManager r0 = r12.b     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            boolean r0 = a((android.location.LocationManager) r0)     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            if (r0 == 0) goto L_0x013f
            long r0 = com.loc.et.a()     // Catch:{ Throwable -> 0x014e, SecurityException -> 0x00fa }
            long r2 = q     // Catch:{ Throwable -> 0x014e, SecurityException -> 0x00fa }
            long r0 = r0 - r2
            r2 = 259200000(0xf731400, double:1.280618154E-315)
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 < 0) goto L_0x00a7
            android.location.LocationManager r0 = r12.b     // Catch:{ Throwable -> 0x014e, SecurityException -> 0x00fa }
            java.lang.String r1 = "gps"
            java.lang.String r2 = "force_xtra_injection"
            r3 = 0
            r0.sendExtraCommand(r1, r2, r3)     // Catch:{ Throwable -> 0x014e, SecurityException -> 0x00fa }
            long r0 = com.loc.et.a()     // Catch:{ Throwable -> 0x014e, SecurityException -> 0x00fa }
            q = r0     // Catch:{ Throwable -> 0x014e, SecurityException -> 0x00fa }
            android.content.Context r0 = r12.z     // Catch:{ Throwable -> 0x014e, SecurityException -> 0x00fa }
            java.lang.String r1 = "pref"
            android.content.SharedPreferences$Editor r0 = com.loc.es.a(r0, r1)     // Catch:{ Throwable -> 0x014e, SecurityException -> 0x00fa }
            java.lang.String r1 = "lagt"
            long r2 = q     // Catch:{ Throwable -> 0x014e, SecurityException -> 0x00fa }
            com.loc.es.a((android.content.SharedPreferences.Editor) r0, (java.lang.String) r1, (long) r2)     // Catch:{ Throwable -> 0x014e, SecurityException -> 0x00fa }
            com.loc.es.a(r0)     // Catch:{ Throwable -> 0x014e, SecurityException -> 0x00fa }
        L_0x00a7:
            android.location.LocationListener r0 = r12.x     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            if (r0 != 0) goto L_0x00b2
            com.loc.g$a r0 = new com.loc.g$a     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            r0.<init>(r12)     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            r12.x = r0     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
        L_0x00b2:
            com.amap.api.location.AMapLocationClientOption r0 = r12.c     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            com.amap.api.location.AMapLocationClientOption$AMapLocationMode r0 = r0.getLocationMode()     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            com.amap.api.location.AMapLocationClientOption$AMapLocationMode r1 = com.amap.api.location.AMapLocationClientOption.AMapLocationMode.Device_Sensors     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            boolean r0 = r0.equals(r1)     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            if (r0 == 0) goto L_0x0125
            com.amap.api.location.AMapLocationClientOption r0 = r12.c     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            float r0 = r0.getDeviceModeDistanceFilter()     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            int r0 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            if (r0 <= 0) goto L_0x0125
            android.location.LocationManager r0 = r12.b     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            java.lang.String r1 = "gps"
            com.amap.api.location.AMapLocationClientOption r2 = r12.c     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            long r2 = r2.getInterval()     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            com.amap.api.location.AMapLocationClientOption r4 = r12.c     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            float r4 = r4.getDeviceModeDistanceFilter()     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            android.location.LocationListener r5 = r12.x     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            r0.requestLocationUpdates(r1, r2, r4, r5, r6)     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
        L_0x00e0:
            android.location.LocationManager r0 = r12.b     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            android.location.GpsStatus$Listener r1 = r12.E     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            r0.addGpsStatusListener(r1)     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            r1 = 8
            r2 = 14
            java.lang.String r3 = "no enough satellites#1401"
            com.amap.api.location.AMapLocationClientOption r0 = r12.c     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            long r4 = r0.getHttpTimeOut()     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            r0 = r12
            r0.a(r1, r2, r3, r4)     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            goto L_0x0025
        L_0x00fa:
            r0 = move-exception
            r1 = 0
            r12.s = r1
            r1 = 2121(0x849, float:2.972E-42)
            com.loc.er.a((java.lang.String) r8, (int) r1)
            r1 = 2
            r2 = 12
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r0 = r0.getMessage()
            java.lang.StringBuilder r0 = r3.append(r0)
            java.lang.String r3 = "#1201"
            java.lang.StringBuilder r0 = r0.append(r3)
            java.lang.String r3 = r0.toString()
            r0 = r12
            r4 = r10
            r0.a(r1, r2, r3, r4)
            goto L_0x0025
        L_0x0125:
            android.location.LocationManager r0 = r12.b     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            java.lang.String r1 = "gps"
            r2 = 900(0x384, double:4.447E-321)
            r4 = 0
            android.location.LocationListener r5 = r12.x     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            r0.requestLocationUpdates(r1, r2, r4, r5, r6)     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            goto L_0x00e0
        L_0x0133:
            r0 = move-exception
            java.lang.String r1 = "GpsLocation"
            java.lang.String r2 = "requestLocationUpdates part2"
            com.loc.en.a(r0, r1, r2)
            goto L_0x0025
        L_0x013f:
            r1 = 8
            r2 = 14
            java.lang.String r3 = "no gps provider#1402"
            r4 = 0
            r0 = r12
            r0.a(r1, r2, r3, r4)     // Catch:{ SecurityException -> 0x00fa, Throwable -> 0x0133 }
            goto L_0x0025
        L_0x014e:
            r0 = move-exception
            goto L_0x00a7
        L_0x0151:
            r0 = move-exception
            goto L_0x0021
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.g.a(com.amap.api.location.AMapLocationClientOption):void");
    }

    public final boolean b() {
        return et.b() - this.d <= 2800;
    }

    @SuppressLint({"NewApi"})
    public final int c() {
        if (this.b == null || !a(this.b)) {
            return 1;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            int i2 = Settings.Secure.getInt(this.z.getContentResolver(), "location_mode", 0);
            if (i2 == 0) {
                return 2;
            }
            if (i2 == 2) {
                return 3;
            }
        } else if (!this.b.isProviderEnabled("gps")) {
            return 2;
        }
        return !this.s ? 4 : 0;
    }

    public final int d() {
        return this.C;
    }
}
