package com.amap.api.location;

import android.content.Context;
import com.loc.en;
import com.loc.et;

public class CoordinateConverter {
    private static int b = 0;
    private static int c = 1;
    private static int d = 2;
    private static int e = 4;
    private static int f = 8;
    private static int g = 16;
    private static int h = 32;
    private static int i = 64;
    DPoint a = null;
    private Context j;
    private CoordType k = null;
    private DPoint l = null;

    public enum CoordType {
        BAIDU,
        MAPBAR,
        MAPABC,
        SOSOMAP,
        ALIYUN,
        GOOGLE,
        GPS
    }

    public CoordinateConverter(Context context) {
        this.j = context;
    }

    public static float calculateLineDistance(DPoint dPoint, DPoint dPoint2) {
        try {
            return et.a(dPoint, dPoint2);
        } catch (Throwable th) {
            return 0.0f;
        }
    }

    public static boolean isAMapDataAvailable(double d2, double d3) {
        return en.a(d2, d3);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized com.amap.api.location.DPoint convert() throws java.lang.Exception {
        /*
            r6 = this;
            r1 = 1
            monitor-enter(r6)
            com.amap.api.location.CoordinateConverter$CoordType r0 = r6.k     // Catch:{ all -> 0x000f }
            if (r0 != 0) goto L_0x0012
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x000f }
            java.lang.String r1 = "转换坐标类型不能为空"
            r0.<init>(r1)     // Catch:{ all -> 0x000f }
            throw r0     // Catch:{ all -> 0x000f }
        L_0x000f:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        L_0x0012:
            com.amap.api.location.DPoint r0 = r6.l     // Catch:{ all -> 0x000f }
            if (r0 != 0) goto L_0x001f
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x000f }
            java.lang.String r1 = "转换坐标源不能为空"
            r0.<init>(r1)     // Catch:{ all -> 0x000f }
            throw r0     // Catch:{ all -> 0x000f }
        L_0x001f:
            com.amap.api.location.DPoint r0 = r6.l     // Catch:{ all -> 0x000f }
            double r2 = r0.getLongitude()     // Catch:{ all -> 0x000f }
            r4 = 4640537203540230144(0x4066800000000000, double:180.0)
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 > 0) goto L_0x003d
            com.amap.api.location.DPoint r0 = r6.l     // Catch:{ all -> 0x000f }
            double r2 = r0.getLongitude()     // Catch:{ all -> 0x000f }
            r4 = -4582834833314545664(0xc066800000000000, double:-180.0)
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 >= 0) goto L_0x0046
        L_0x003d:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x000f }
            java.lang.String r1 = "请传入合理经度"
            r0.<init>(r1)     // Catch:{ all -> 0x000f }
            throw r0     // Catch:{ all -> 0x000f }
        L_0x0046:
            com.amap.api.location.DPoint r0 = r6.l     // Catch:{ all -> 0x000f }
            double r2 = r0.getLatitude()     // Catch:{ all -> 0x000f }
            r4 = 4636033603912859648(0x4056800000000000, double:90.0)
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 > 0) goto L_0x0064
            com.amap.api.location.DPoint r0 = r6.l     // Catch:{ all -> 0x000f }
            double r2 = r0.getLatitude()     // Catch:{ all -> 0x000f }
            r4 = -4587338432941916160(0xc056800000000000, double:-90.0)
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 >= 0) goto L_0x006d
        L_0x0064:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x000f }
            java.lang.String r1 = "请传入合理纬度"
            r0.<init>(r1)     // Catch:{ all -> 0x000f }
            throw r0     // Catch:{ all -> 0x000f }
        L_0x006d:
            r2 = 0
            r0 = 0
            int[] r3 = com.amap.api.location.CoordinateConverter.AnonymousClass1.a     // Catch:{ all -> 0x000f }
            com.amap.api.location.CoordinateConverter$CoordType r4 = r6.k     // Catch:{ all -> 0x000f }
            int r4 = r4.ordinal()     // Catch:{ all -> 0x000f }
            r3 = r3[r4]     // Catch:{ all -> 0x000f }
            switch(r3) {
                case 1: goto L_0x009c;
                case 2: goto L_0x00b6;
                case 3: goto L_0x00d2;
                case 4: goto L_0x00e8;
                case 5: goto L_0x00fe;
                case 6: goto L_0x0115;
                case 7: goto L_0x012c;
                default: goto L_0x007c;
            }     // Catch:{ all -> 0x000f }
        L_0x007c:
            r1 = r2
        L_0x007d:
            if (r1 == 0) goto L_0x0098
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ all -> 0x000f }
            r1.<init>()     // Catch:{ all -> 0x000f }
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x000f }
            if (r2 != 0) goto L_0x0090
            java.lang.String r2 = "amap_loc_coordinate"
            r1.put(r2, r0)     // Catch:{ all -> 0x000f }
        L_0x0090:
            android.content.Context r0 = r6.j     // Catch:{ all -> 0x000f }
            java.lang.String r2 = "O021"
            com.loc.er.a((android.content.Context) r0, (java.lang.String) r2, (org.json.JSONObject) r1)     // Catch:{ all -> 0x000f }
        L_0x0098:
            com.amap.api.location.DPoint r0 = r6.a     // Catch:{ all -> 0x000f }
            monitor-exit(r6)
            return r0
        L_0x009c:
            com.amap.api.location.DPoint r3 = r6.l     // Catch:{ all -> 0x000f }
            com.amap.api.location.DPoint r3 = com.loc.ep.a((com.amap.api.location.DPoint) r3)     // Catch:{ all -> 0x000f }
            r6.a = r3     // Catch:{ all -> 0x000f }
            int r3 = b     // Catch:{ all -> 0x000f }
            int r4 = c     // Catch:{ all -> 0x000f }
            r3 = r3 & r4
            if (r3 != 0) goto L_0x007c
            java.lang.String r0 = "baidu"
            int r2 = b     // Catch:{ all -> 0x000f }
            int r3 = c     // Catch:{ all -> 0x000f }
            r2 = r2 | r3
            b = r2     // Catch:{ all -> 0x000f }
            goto L_0x007d
        L_0x00b6:
            android.content.Context r3 = r6.j     // Catch:{ all -> 0x000f }
            com.amap.api.location.DPoint r4 = r6.l     // Catch:{ all -> 0x000f }
            com.amap.api.location.DPoint r3 = com.loc.ep.b((android.content.Context) r3, (com.amap.api.location.DPoint) r4)     // Catch:{ all -> 0x000f }
            r6.a = r3     // Catch:{ all -> 0x000f }
            int r3 = b     // Catch:{ all -> 0x000f }
            int r4 = d     // Catch:{ all -> 0x000f }
            r3 = r3 & r4
            if (r3 != 0) goto L_0x007c
            java.lang.String r0 = "mapbar"
            int r2 = b     // Catch:{ all -> 0x000f }
            int r3 = d     // Catch:{ all -> 0x000f }
            r2 = r2 | r3
            b = r2     // Catch:{ all -> 0x000f }
            goto L_0x007d
        L_0x00d2:
            int r3 = b     // Catch:{ all -> 0x000f }
            int r4 = e     // Catch:{ all -> 0x000f }
            r3 = r3 & r4
            if (r3 != 0) goto L_0x0151
            java.lang.String r0 = "mapabc"
            int r2 = b     // Catch:{ all -> 0x000f }
            int r3 = e     // Catch:{ all -> 0x000f }
            r2 = r2 | r3
            b = r2     // Catch:{ all -> 0x000f }
        L_0x00e3:
            com.amap.api.location.DPoint r2 = r6.l     // Catch:{ all -> 0x000f }
            r6.a = r2     // Catch:{ all -> 0x000f }
            goto L_0x007d
        L_0x00e8:
            int r3 = b     // Catch:{ all -> 0x000f }
            int r4 = f     // Catch:{ all -> 0x000f }
            r3 = r3 & r4
            if (r3 != 0) goto L_0x014f
            java.lang.String r0 = "sosomap"
            int r2 = b     // Catch:{ all -> 0x000f }
            int r3 = f     // Catch:{ all -> 0x000f }
            r2 = r2 | r3
            b = r2     // Catch:{ all -> 0x000f }
        L_0x00f9:
            com.amap.api.location.DPoint r2 = r6.l     // Catch:{ all -> 0x000f }
            r6.a = r2     // Catch:{ all -> 0x000f }
            goto L_0x007d
        L_0x00fe:
            int r3 = b     // Catch:{ all -> 0x000f }
            int r4 = g     // Catch:{ all -> 0x000f }
            r3 = r3 & r4
            if (r3 != 0) goto L_0x014d
            java.lang.String r0 = "aliyun"
            int r2 = b     // Catch:{ all -> 0x000f }
            int r3 = g     // Catch:{ all -> 0x000f }
            r2 = r2 | r3
            b = r2     // Catch:{ all -> 0x000f }
        L_0x010f:
            com.amap.api.location.DPoint r2 = r6.l     // Catch:{ all -> 0x000f }
            r6.a = r2     // Catch:{ all -> 0x000f }
            goto L_0x007d
        L_0x0115:
            int r3 = b     // Catch:{ all -> 0x000f }
            int r4 = h     // Catch:{ all -> 0x000f }
            r3 = r3 & r4
            if (r3 != 0) goto L_0x014b
            java.lang.String r0 = "google"
            int r2 = b     // Catch:{ all -> 0x000f }
            int r3 = h     // Catch:{ all -> 0x000f }
            r2 = r2 | r3
            b = r2     // Catch:{ all -> 0x000f }
        L_0x0126:
            com.amap.api.location.DPoint r2 = r6.l     // Catch:{ all -> 0x000f }
            r6.a = r2     // Catch:{ all -> 0x000f }
            goto L_0x007d
        L_0x012c:
            int r3 = b     // Catch:{ all -> 0x000f }
            int r4 = i     // Catch:{ all -> 0x000f }
            r3 = r3 & r4
            if (r3 != 0) goto L_0x0149
            java.lang.String r0 = "gps"
            int r2 = b     // Catch:{ all -> 0x000f }
            int r3 = i     // Catch:{ all -> 0x000f }
            r2 = r2 | r3
            b = r2     // Catch:{ all -> 0x000f }
        L_0x013d:
            android.content.Context r2 = r6.j     // Catch:{ all -> 0x000f }
            com.amap.api.location.DPoint r3 = r6.l     // Catch:{ all -> 0x000f }
            com.amap.api.location.DPoint r2 = com.loc.ep.a((android.content.Context) r2, (com.amap.api.location.DPoint) r3)     // Catch:{ all -> 0x000f }
            r6.a = r2     // Catch:{ all -> 0x000f }
            goto L_0x007d
        L_0x0149:
            r1 = r2
            goto L_0x013d
        L_0x014b:
            r1 = r2
            goto L_0x0126
        L_0x014d:
            r1 = r2
            goto L_0x010f
        L_0x014f:
            r1 = r2
            goto L_0x00f9
        L_0x0151:
            r1 = r2
            goto L_0x00e3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.location.CoordinateConverter.convert():com.amap.api.location.DPoint");
    }

    public synchronized CoordinateConverter coord(DPoint dPoint) throws Exception {
        if (dPoint == null) {
            throw new IllegalArgumentException("传入经纬度对象为空");
        } else if (dPoint.getLongitude() > 180.0d || dPoint.getLongitude() < -180.0d) {
            throw new IllegalArgumentException("请传入合理经度");
        } else if (dPoint.getLatitude() > 90.0d || dPoint.getLatitude() < -90.0d) {
            throw new IllegalArgumentException("请传入合理纬度");
        } else {
            this.l = dPoint;
        }
        return this;
    }

    public synchronized CoordinateConverter from(CoordType coordType) {
        this.k = coordType;
        return this;
    }
}
