package com.loc;

import android.text.TextUtils;
import com.amap.api.location.AMapLocation;

/* compiled from: LocFilter */
public final class dv {
    dw a = null;
    long b = 0;
    long c = 0;
    int d = 0;
    long e = 0;
    AMapLocation f = null;
    long g = 0;
    private boolean h = true;

    private dw b(dw dwVar) {
        if (et.a(dwVar)) {
            if (!this.h || !em.b(dwVar.getTime())) {
                dwVar.setLocationType(this.d);
            } else if (dwVar.getLocationType() == 5 || dwVar.getLocationType() == 6) {
                dwVar.setLocationType(4);
            }
        }
        return dwVar;
    }

    public final AMapLocation a(AMapLocation aMapLocation) {
        if (!et.a(aMapLocation)) {
            return aMapLocation;
        }
        long b2 = et.b() - this.g;
        this.g = et.b();
        if (b2 > 5000) {
            return aMapLocation;
        }
        if (this.f == null) {
            this.f = aMapLocation;
            return aMapLocation;
        } else if (1 != this.f.getLocationType() && !"gps".equalsIgnoreCase(this.f.getProvider())) {
            this.f = aMapLocation;
            return aMapLocation;
        } else if (this.f.getAltitude() == aMapLocation.getAltitude() && this.f.getLongitude() == aMapLocation.getLongitude()) {
            this.f = aMapLocation;
            return aMapLocation;
        } else {
            long abs = Math.abs(aMapLocation.getTime() - this.f.getTime());
            if (30000 < abs) {
                this.f = aMapLocation;
                return aMapLocation;
            }
            if (et.a(aMapLocation, this.f) > ((((float) abs) * (this.f.getSpeed() + aMapLocation.getSpeed())) / 2000.0f) + (2.0f * (this.f.getAccuracy() + aMapLocation.getAccuracy())) + 3000.0f) {
                return this.f;
            }
            this.f = aMapLocation;
            return aMapLocation;
        }
    }

    public final dw a(dw dwVar) {
        if (et.b() - this.e > 30000) {
            this.a = dwVar;
            this.e = et.b();
            return this.a;
        }
        this.e = et.b();
        if (!et.a(this.a) || !et.a(dwVar)) {
            this.b = et.b();
            this.a = dwVar;
            return this.a;
        } else if (dwVar.getTime() == this.a.getTime() && dwVar.getAccuracy() < 300.0f) {
            return dwVar;
        } else {
            if (dwVar.getProvider().equals("gps")) {
                this.b = et.b();
                this.a = dwVar;
                return this.a;
            } else if (dwVar.c() != this.a.c()) {
                this.b = et.b();
                this.a = dwVar;
                return this.a;
            } else if (dwVar.getBuildingId().equals(this.a.getBuildingId()) || TextUtils.isEmpty(dwVar.getBuildingId())) {
                this.d = dwVar.getLocationType();
                float a2 = et.a((AMapLocation) dwVar, (AMapLocation) this.a);
                float accuracy = this.a.getAccuracy();
                float accuracy2 = dwVar.getAccuracy();
                float f2 = accuracy2 - accuracy;
                long b2 = et.b();
                long j = b2 - this.b;
                boolean z = accuracy <= 100.0f && accuracy2 > 299.0f;
                boolean z2 = accuracy > 299.0f && accuracy2 > 299.0f;
                if (z || z2) {
                    if (this.c == 0) {
                        this.c = b2;
                    } else if (b2 - this.c > 30000) {
                        this.b = b2;
                        this.a = dwVar;
                        this.c = 0;
                        return this.a;
                    }
                    this.a = b(this.a);
                    return this.a;
                } else if (accuracy2 >= 100.0f || accuracy <= 299.0f) {
                    if (accuracy2 <= 299.0f) {
                        this.c = 0;
                    }
                    if (a2 >= 10.0f || ((double) a2) <= 0.1d || accuracy2 <= 5.0f) {
                        if (f2 < 300.0f) {
                            this.b = et.b();
                            this.a = dwVar;
                            return this.a;
                        } else if (j >= 30000) {
                            this.b = et.b();
                            this.a = dwVar;
                            return this.a;
                        } else {
                            this.a = b(this.a);
                            return this.a;
                        }
                    } else if (f2 >= -300.0f) {
                        this.a = b(this.a);
                        return this.a;
                    } else if (accuracy / accuracy2 >= 2.0f) {
                        this.b = b2;
                        this.a = dwVar;
                        return this.a;
                    } else {
                        this.a = b(this.a);
                        return this.a;
                    }
                } else {
                    this.b = b2;
                    this.a = dwVar;
                    this.c = 0;
                    return this.a;
                }
            } else {
                this.b = et.b();
                this.a = dwVar;
                return this.a;
            }
        }
    }

    public final void a() {
        this.a = null;
        this.b = 0;
        this.c = 0;
        this.f = null;
        this.g = 0;
    }

    public final void a(boolean z) {
        this.h = z;
    }
}
