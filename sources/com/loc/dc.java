package com.loc;

/* compiled from: AmapCellGsm */
public final class dc extends da {
    public int j = 0;
    public int k = 0;
    public int l = Integer.MAX_VALUE;
    public int m = Integer.MAX_VALUE;
    public int n = Integer.MAX_VALUE;
    public int o = Integer.MAX_VALUE;

    public dc(boolean z, boolean z2) {
        super(z, z2);
    }

    /* renamed from: a */
    public final da clone() {
        dc dcVar = new dc(this.h, this.i);
        dcVar.a((da) this);
        dcVar.j = this.j;
        dcVar.k = this.k;
        dcVar.l = this.l;
        dcVar.m = this.m;
        dcVar.n = this.n;
        dcVar.o = this.o;
        return dcVar;
    }

    public final String toString() {
        return "AmapCellGsm{lac=" + this.j + ", cid=" + this.k + ", psc=" + this.l + ", arfcn=" + this.m + ", bsic=" + this.n + ", timingAdvance=" + this.o + '}' + super.toString();
    }
}
