package com.loc;

import java.util.Locale;

/* compiled from: Cgi */
public final class dz {
    public int a = 0;
    public int b = 0;
    public int c = 0;
    public int d = 0;
    public int e = 0;
    public int f = 0;
    public int g = 0;
    public int h = 0;
    public int i = 0;
    public int j = -113;
    public int k = 0;
    public short l = 0;
    public long m = 0;
    public boolean n = false;
    public int o = 32767;
    public boolean p = true;

    public dz(int i2, boolean z) {
        this.k = i2;
        this.n = z;
    }

    public final boolean equals(Object obj) {
        if (obj == null || !(obj instanceof dz)) {
            return false;
        }
        dz dzVar = (dz) obj;
        switch (dzVar.k) {
            case 1:
                return this.k == 1 && dzVar.c == this.c && dzVar.d == this.d && dzVar.b == this.b;
            case 2:
                return this.k == 2 && dzVar.i == this.i && dzVar.h == this.h && dzVar.g == this.g;
            case 3:
                return this.k == 3 && dzVar.c == this.c && dzVar.d == this.d && dzVar.b == this.b;
            case 4:
                return this.k == 4 && dzVar.c == this.c && dzVar.d == this.d && dzVar.b == this.b;
            default:
                return false;
        }
    }

    public final int hashCode() {
        int hashCode = String.valueOf(this.k).hashCode();
        return this.k == 2 ? hashCode + String.valueOf(this.i).hashCode() + String.valueOf(this.h).hashCode() + String.valueOf(this.g).hashCode() : hashCode + String.valueOf(this.c).hashCode() + String.valueOf(this.d).hashCode() + String.valueOf(this.b).hashCode();
    }

    public final String toString() {
        switch (this.k) {
            case 1:
                return String.format(Locale.CHINA, "GSM lac=%d, cid=%d, mnc=%s, valid=%b, sig=%d, age=%d, reg=%b", new Object[]{Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.b), Boolean.valueOf(this.p), Integer.valueOf(this.j), Short.valueOf(this.l), Boolean.valueOf(this.n)});
            case 2:
                return String.format(Locale.CHINA, "CDMA bid=%d, nid=%d, sid=%d, valid=%b, sig=%d, age=%d, reg=%b", new Object[]{Integer.valueOf(this.i), Integer.valueOf(this.h), Integer.valueOf(this.g), Boolean.valueOf(this.p), Integer.valueOf(this.j), Short.valueOf(this.l), Boolean.valueOf(this.n)});
            case 3:
                return String.format(Locale.CHINA, "LTE lac=%d, cid=%d, mnc=%s, valid=%b, sig=%d, age=%d, reg=%b, pci=%d", new Object[]{Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.b), Boolean.valueOf(this.p), Integer.valueOf(this.j), Short.valueOf(this.l), Boolean.valueOf(this.n), Integer.valueOf(this.o)});
            case 4:
                return String.format(Locale.CHINA, "WCDMA lac=%d, cid=%d, mnc=%s, valid=%b, sig=%d, age=%d, reg=%b, pci=%d", new Object[]{Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.b), Boolean.valueOf(this.p), Integer.valueOf(this.j), Short.valueOf(this.l), Boolean.valueOf(this.n), Integer.valueOf(this.o)});
            default:
                return "unknown";
        }
    }
}
