package com.loc;

/* compiled from: AmapCellWcdma */
public final class de extends da {
    public int j = 0;
    public int k = 0;
    public int l = Integer.MAX_VALUE;
    public int m = Integer.MAX_VALUE;

    public de(boolean z, boolean z2) {
        super(z, z2);
    }

    /* renamed from: a */
    public final da clone() {
        de deVar = new de(this.h, this.i);
        deVar.a((da) this);
        deVar.j = this.j;
        deVar.k = this.k;
        deVar.l = this.l;
        deVar.m = this.m;
        return deVar;
    }

    public final String toString() {
        return "AmapCellWcdma{lac=" + this.j + ", cid=" + this.k + ", psc=" + this.l + ", uarfcn=" + this.m + '}' + super.toString();
    }
}
