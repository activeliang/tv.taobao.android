package com.loc;

/* compiled from: AmapCellLte */
public final class dd extends da {
    public int j = 0;
    public int k = 0;
    public int l = Integer.MAX_VALUE;
    public int m = Integer.MAX_VALUE;
    public int n = Integer.MAX_VALUE;

    public dd(boolean z) {
        super(z, true);
    }

    /* renamed from: a */
    public final da clone() {
        dd ddVar = new dd(this.h);
        ddVar.a((da) this);
        ddVar.j = this.j;
        ddVar.k = this.k;
        ddVar.l = this.l;
        ddVar.m = this.m;
        ddVar.n = this.n;
        return ddVar;
    }

    public final String toString() {
        return "AmapCellLte{lac=" + this.j + ", cid=" + this.k + ", pci=" + this.l + ", earfcn=" + this.m + ", timingAdvance=" + this.n + '}' + super.toString();
    }
}
