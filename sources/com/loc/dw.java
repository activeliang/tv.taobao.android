package com.loc;

import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import org.json.JSONObject;

/* compiled from: AMapLocationServer */
public final class dw extends AMapLocation {
    protected String d = "";
    boolean e = true;
    String f = String.valueOf(AMapLocationClientOption.GeoLanguage.DEFAULT);
    private String g = null;
    private String h = "";
    private int i;
    private String j = "";
    private String k = "new";
    private JSONObject l = null;
    private String m = "";
    private String n = "";
    private String o = null;

    public dw(String str) {
        super(str);
    }

    public final String a() {
        return this.g;
    }

    public final void a(String str) {
        this.g = str;
    }

    public final void a(JSONObject jSONObject) {
        this.l = jSONObject;
    }

    public final void a(boolean z) {
        this.e = z;
    }

    public final String b() {
        return this.h;
    }

    public final void b(String str) {
        this.h = str;
    }

    public final void b(JSONObject jSONObject) {
        int i2 = 0;
        try {
            en.a((AMapLocation) this, jSONObject);
            this.k = jSONObject.optString("type", this.k);
            this.j = jSONObject.optString("retype", this.j);
            String optString = jSONObject.optString("cens", this.n);
            if (!TextUtils.isEmpty(optString)) {
                String[] split = optString.split("\\*");
                int length = split.length;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    String str = split[i2];
                    if (!TextUtils.isEmpty(str)) {
                        String[] split2 = str.split(",");
                        setLongitude(et.e(split2[0]));
                        setLatitude(et.e(split2[1]));
                        setAccuracy((float) et.g(split2[2]));
                        break;
                    }
                    i2++;
                }
                this.n = optString;
            }
            this.d = jSONObject.optString("desc", this.d);
            c(jSONObject.optString("coord", String.valueOf(this.i)));
            this.m = jSONObject.optString("mcell", this.m);
            this.e = jSONObject.optBoolean("isReversegeo", this.e);
            this.f = jSONObject.optString("geoLanguage", this.f);
            if (et.a(jSONObject, "poiid")) {
                setBuildingId(jSONObject.optString("poiid"));
            }
            if (et.a(jSONObject, "pid")) {
                setBuildingId(jSONObject.optString("pid"));
            }
            if (et.a(jSONObject, "floor")) {
                setFloor(jSONObject.optString("floor"));
            }
            if (et.a(jSONObject, "flr")) {
                setFloor(jSONObject.optString("flr"));
            }
        } catch (Throwable th) {
            en.a(th, "AmapLoc", "AmapLoc");
        }
    }

    public final int c() {
        return this.i;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002e  */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0016  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void c(java.lang.String r2) {
        /*
            r1 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 != 0) goto L_0x002a
            java.lang.String r0 = "0"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x001d
            r0 = 0
            r1.i = r0
        L_0x0012:
            int r0 = r1.i
            if (r0 != 0) goto L_0x002e
            java.lang.String r0 = "WGS84"
            super.setCoordType(r0)
        L_0x001c:
            return
        L_0x001d:
            java.lang.String r0 = "1"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x002a
            r0 = 1
            r1.i = r0
            goto L_0x0012
        L_0x002a:
            r0 = -1
            r1.i = r0
            goto L_0x0012
        L_0x002e:
            java.lang.String r0 = "GCJ02"
            super.setCoordType(r0)
            goto L_0x001c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.dw.c(java.lang.String):void");
    }

    public final String d() {
        return this.j;
    }

    public final void d(String str) {
        this.j = str;
    }

    public final String e() {
        return this.k;
    }

    public final void e(String str) {
        this.k = str;
    }

    public final JSONObject f() {
        return this.l;
    }

    public final void f(String str) {
        this.f = str;
    }

    public final String g() {
        return this.m;
    }

    public final void g(String str) {
        this.d = str;
    }

    public final dw h() {
        String str = this.m;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] split = str.split(",");
        if (split.length != 3) {
            return null;
        }
        dw dwVar = new dw("");
        dwVar.setProvider(getProvider());
        dwVar.setLongitude(et.e(split[0]));
        dwVar.setLatitude(et.e(split[1]));
        dwVar.setAccuracy(et.f(split[2]));
        dwVar.setCityCode(getCityCode());
        dwVar.setAdCode(getAdCode());
        dwVar.setCountry(getCountry());
        dwVar.setProvince(getProvince());
        dwVar.setCity(getCity());
        dwVar.setTime(getTime());
        dwVar.k = this.k;
        dwVar.c(String.valueOf(this.i));
        if (et.a(dwVar)) {
            return dwVar;
        }
        return null;
    }

    public final void h(String str) {
        this.o = str;
    }

    public final boolean i() {
        return this.e;
    }

    public final String j() {
        return this.f;
    }

    public final String k() {
        return this.o;
    }

    public final JSONObject toJson(int i2) {
        try {
            JSONObject json = super.toJson(i2);
            switch (i2) {
                case 1:
                    json.put("retype", this.j);
                    json.put("cens", this.n);
                    json.put("coord", this.i);
                    json.put("mcell", this.m);
                    json.put("desc", this.d);
                    json.put("address", getAddress());
                    if (this.l != null && et.a(json, "offpct")) {
                        json.put("offpct", this.l.getString("offpct"));
                        break;
                    }
                case 2:
                case 3:
                    break;
                default:
                    return json;
            }
            json.put("type", this.k);
            json.put("isReversegeo", this.e);
            json.put("geoLanguage", this.f);
            return json;
        } catch (Throwable th) {
            en.a(th, "AmapLoc", "toStr");
            return null;
        }
    }

    public final String toStr() {
        return toStr(1);
    }

    public final String toStr(int i2) {
        JSONObject jSONObject;
        try {
            jSONObject = toJson(i2);
            jSONObject.put("nb", this.o);
        } catch (Throwable th) {
            en.a(th, "AMapLocation", "toStr part2");
            jSONObject = null;
        }
        if (jSONObject == null) {
            return null;
        }
        return jSONObject.toString();
    }
}
