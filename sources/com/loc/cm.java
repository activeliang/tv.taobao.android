package com.loc;

/* compiled from: TData */
public final class cm extends ew {
    public static int a(ev evVar, byte b, int i) {
        evVar.b(2);
        evVar.b(1, i);
        evVar.a(0, b);
        return evVar.b();
    }

    public static int a(ev evVar, byte[] bArr) {
        evVar.a(1, bArr.length, 1);
        for (int length = bArr.length - 1; length >= 0; length--) {
            evVar.a(bArr[length]);
        }
        return evVar.a();
    }
}
