package com.loc;

import java.util.List;

/* compiled from: UploadBufferBuilder */
public final class ca extends bx {
    private static ca b = new ca();

    private ca() {
        super(5120);
    }

    private static String a(String str) {
        return str == null ? "" : str;
    }

    public static ca b() {
        return b;
    }

    public final byte[] a(byte[] bArr, byte[] bArr2, List<? extends ce> list) {
        byte[] bArr3;
        if (list == null) {
            return null;
        }
        try {
            int size = list.size();
            if (size > 0 && bArr != null) {
                a();
                int a = ch.a((ev) this.a, bArr);
                int[] iArr = new int[size];
                for (int i = 0; i < size; i++) {
                    ce ceVar = (ce) list.get(i);
                    iArr[i] = cm.a(this.a, (byte) ceVar.a(), cm.a(this.a, ceVar.b()));
                }
                this.a.c(ch.a(this.a, a, bArr2 != null ? ch.b(this.a, bArr2) : 0, ch.a((ev) this.a, iArr)));
                bArr3 = this.a.c();
                return bArr3;
            }
        } catch (Throwable th) {
            dk.a(th);
        }
        bArr3 = null;
        return bArr3;
    }

    public final byte[] c() {
        super.a();
        try {
            this.a.c(dj.a(this.a, di.a(), this.a.a(di.f()), this.a.a(di.c()), (byte) di.m(), this.a.a(di.i()), this.a.a(di.h()), this.a.a(a(di.g())), this.a.a(a(di.j())), dh.a(di.n()), this.a.a(di.l()), this.a.a(di.k()), this.a.a(di.d()), this.a.a(di.e())));
            return this.a.c();
        } catch (Exception e) {
            dk.a(e);
            return null;
        }
    }
}
