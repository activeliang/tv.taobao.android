package com.loc;

/* compiled from: AmapCellCdma */
public final class db extends da {
    public int j = 0;
    public int k = 0;
    public int l = 0;
    public int m;
    public int n;

    public db(boolean z, boolean z2) {
        super(z, z2);
    }

    /* renamed from: a */
    public final da clone() {
        db dbVar = new db(this.h, this.i);
        dbVar.a((da) this);
        this.j = dbVar.j;
        this.k = dbVar.k;
        this.l = dbVar.l;
        this.m = dbVar.m;
        this.n = dbVar.n;
        return dbVar;
    }

    public final String toString() {
        return "AmapCellCdma{sid=" + this.j + ", nid=" + this.k + ", bid=" + this.l + ", latitude=" + this.m + ", longitude=" + this.n + '}' + super.toString();
    }
}
