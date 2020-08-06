package com.loc;

import android.os.SystemClock;
import com.loc.cb;
import java.util.List;

/* compiled from: FpsBufferBuilder */
public final class by extends bx {
    public by() {
        super(2048);
    }

    public final byte[] a(dg dgVar, cb.a aVar, long j, List<dh> list) {
        int i;
        int i2;
        int i3;
        int i4;
        super.a();
        try {
            int a = cn.a(this.a, dgVar.c, dgVar.k, (int) (dgVar.e * 1000000.0d), (int) (dgVar.d * 1000000.0d), (int) dgVar.f, (int) dgVar.i, (int) dgVar.g, (short) ((int) dgVar.h), dgVar.l);
            if (aVar == null || aVar.f == null || aVar.f.size() <= 0) {
                i = -1;
            } else {
                List<da> list2 = aVar.f;
                if (!(list2 == null || list2.size() == 0)) {
                    for (da next : list2) {
                        if (next instanceof dc) {
                            dc dcVar = (dc) next;
                            next.g = cx.a(cx.a(dcVar.j, dcVar.k));
                        } else if (next instanceof dd) {
                            dd ddVar = (dd) next;
                            next.g = cx.a(cx.a(ddVar.j, ddVar.k));
                        } else if (next instanceof de) {
                            de deVar = (de) next;
                            next.g = cx.a(cx.a(deVar.j, deVar.k));
                        } else if (next instanceof db) {
                            db dbVar = (db) next;
                            next.g = cx.a(cx.a(dbVar.k, dbVar.l));
                        }
                    }
                }
                int size = aVar.f.size();
                int[] iArr = new int[size];
                int i5 = 0;
                while (true) {
                    if (i5 < size) {
                        da daVar = aVar.f.get(i5);
                        byte b = 0;
                        int i6 = -1;
                        if (daVar instanceof dc) {
                            dc dcVar2 = (dc) daVar;
                            if (!dcVar2.i) {
                                i6 = co.a(this.a, dcVar2.j, dcVar2.k, dcVar2.c, dcVar2.l);
                                b = 1;
                            } else {
                                i6 = co.a(this.a, dcVar2.b(), dcVar2.c(), dcVar2.j, dcVar2.k, dcVar2.c, dcVar2.m, dcVar2.n, dcVar2.d, dcVar2.l);
                                b = 1;
                            }
                        } else if (daVar instanceof dd) {
                            dd ddVar2 = (dd) daVar;
                            i6 = cp.a(this.a, ddVar2.b(), ddVar2.c(), ddVar2.j, ddVar2.k, ddVar2.l, ddVar2.c, ddVar2.m, ddVar2.d);
                            b = 3;
                        } else if (daVar instanceof db) {
                            db dbVar2 = (db) daVar;
                            if (!dbVar2.i) {
                                i6 = ci.a(this.a, dbVar2.j, dbVar2.k, dbVar2.l, dbVar2.m, dbVar2.n, dbVar2.c);
                                b = 2;
                            } else {
                                i6 = ci.a(this.a, dbVar2.j, dbVar2.k, dbVar2.l, dbVar2.m, dbVar2.n, dbVar2.c, dbVar2.d);
                                b = 2;
                            }
                        } else if (daVar instanceof de) {
                            de deVar2 = (de) daVar;
                            i6 = cs.a(this.a, deVar2.b(), deVar2.c(), deVar2.j, deVar2.k, deVar2.l, deVar2.c, deVar2.m, deVar2.d);
                            b = 4;
                        }
                        if (i6 == -1) {
                            i = -1;
                            break;
                        }
                        iArr[i5] = cl.a(this.a, (byte) (daVar.h ? 1 : 0), (byte) (daVar.i ? 1 : 0), (short) daVar.g, b, i6);
                        i5++;
                    } else {
                        int a2 = this.a.a(aVar.b);
                        int a3 = cj.a(this.a, iArr);
                        int size2 = aVar.g.size();
                        int[] iArr2 = new int[size2];
                        for (int i7 = 0; i7 < size2; i7++) {
                            da daVar2 = aVar.g.get(i7);
                            long elapsedRealtime = (SystemClock.elapsedRealtime() - daVar2.e) / 1000;
                            if (elapsedRealtime > 32767 || elapsedRealtime < 0) {
                                elapsedRealtime = 32767;
                            }
                            if (daVar2 instanceof dc) {
                                dc dcVar3 = (dc) daVar2;
                                i4 = cr.a(this.a, dcVar3.j, dcVar3.k, (short) ((int) elapsedRealtime));
                                i3 = 1;
                            } else if (daVar2 instanceof dd) {
                                dd ddVar3 = (dd) daVar2;
                                i4 = cr.a(this.a, ddVar3.j, ddVar3.k, (short) ((int) elapsedRealtime));
                                i3 = 1;
                            } else if (daVar2 instanceof db) {
                                db dbVar3 = (db) daVar2;
                                i4 = cq.a(this.a, dbVar3.j, dbVar3.k, dbVar3.l, (short) ((int) elapsedRealtime));
                                i3 = 2;
                            } else if (daVar2 instanceof de) {
                                de deVar3 = (de) daVar2;
                                i4 = cr.a(this.a, deVar3.j, deVar3.k, (short) ((int) elapsedRealtime));
                                i3 = 1;
                            } else {
                                i3 = 0;
                                i4 = 0;
                            }
                            iArr2[i7] = ck.a(this.a, (byte) i3, i4);
                        }
                        i = cj.a(this.a, a2, aVar.a, a3, cj.b(this.a, iArr2));
                    }
                }
            }
            if (list == null || list.size() <= 0) {
                i2 = -1;
            } else {
                for (dh next2 : list) {
                    next2.g = cx.b(next2.a);
                }
                int size3 = list.size();
                if (size3 > 0) {
                    int[] iArr3 = new int[size3];
                    for (int i8 = 0; i8 < size3; i8++) {
                        boolean z = false;
                        dh dhVar = list.get(i8);
                        int a4 = this.a.a(dhVar.b);
                        if (dhVar.a == j && dhVar.a != -1) {
                            z = true;
                        }
                        iArr3[i8] = cu.a(this.a, z, dhVar.a, (short) dhVar.c, a4, dhVar.g, (short) dhVar.d);
                    }
                    i2 = ct.a((ev) this.a, ct.a((ev) this.a, iArr3));
                } else {
                    i2 = -1;
                }
            }
            cg.a(this.a);
            cg.a(this.a, a);
            if (i > 0) {
                cg.c(this.a, i);
            }
            if (i2 > 0) {
                cg.b(this.a, i2);
            }
            this.a.c(cg.b(this.a));
            return this.a.c();
        } catch (Throwable th) {
            dk.a(th);
            return null;
        }
    }
}
