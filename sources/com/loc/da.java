package com.loc;

/* compiled from: AmapCell */
public abstract class da {
    public String a = "";
    public String b = "";
    public int c = 99;
    public int d = Integer.MAX_VALUE;
    public long e = 0;
    public long f = 0;
    public int g = 0;
    public boolean h;
    public boolean i = true;

    public da(boolean z, boolean z2) {
        this.h = z;
        this.i = z2;
    }

    private static int a(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e2) {
            dk.a(e2);
            return 0;
        }
    }

    /* renamed from: a */
    public abstract da clone();

    public final void a(da daVar) {
        if (daVar != null) {
            this.a = daVar.a;
            this.b = daVar.b;
            this.c = daVar.c;
            this.d = daVar.d;
            this.e = daVar.e;
            this.f = daVar.f;
            this.g = daVar.g;
            this.h = daVar.h;
            this.i = daVar.i;
        }
    }

    public final int b() {
        return a(this.a);
    }

    public final int c() {
        return a(this.b);
    }

    public String toString() {
        return "AmapCell{mcc=" + this.a + ", mnc=" + this.b + ", signalStrength=" + this.c + ", asulevel=" + this.d + ", lastUpdateSystemMills=" + this.e + ", lastUpdateUtcMills=" + this.f + ", age=" + this.g + ", main=" + this.h + ", newapi=" + this.i + '}';
    }
}
