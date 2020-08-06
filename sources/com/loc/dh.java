package com.loc;

/* compiled from: AmapWifi */
public final class dh {
    public long a;
    public String b;
    public int c = -113;
    public int d;
    public long e;
    public long f = 0;
    public short g;
    public boolean h;

    public dh(boolean z) {
        this.h = z;
    }

    public static long a(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        long j = 0;
        int i = 0;
        for (int length = str.length() - 1; length >= 0; length--) {
            long charAt = (long) str.charAt(length);
            if (charAt >= 48 && charAt <= 57) {
                j += (charAt - 48) << i;
                i += 4;
            } else if (charAt >= 97 && charAt <= 102) {
                j += ((charAt - 97) + 10) << i;
                i += 4;
            } else if (charAt >= 65 && charAt <= 70) {
                j += ((charAt - 65) + 10) << i;
                i += 4;
            } else if (!(charAt == 58 || charAt == 124)) {
                return 0;
            }
        }
        if (i != 48) {
            return 0;
        }
        return j;
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        dh dhVar = new dh(this.h);
        dhVar.a = this.a;
        dhVar.b = this.b;
        dhVar.c = this.c;
        dhVar.d = this.d;
        dhVar.e = this.e;
        dhVar.f = this.f;
        dhVar.g = this.g;
        dhVar.h = this.h;
        return dhVar;
    }

    public final String toString() {
        return "AmapWifi{mac=" + this.a + ", ssid='" + this.b + '\'' + ", rssi=" + this.c + ", frequency=" + this.d + ", timestamp=" + this.e + ", lastUpdateUtcMills=" + this.f + ", freshness=" + this.g + ", connected=" + this.h + '}';
    }
}
