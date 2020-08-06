package com.loc;

/* compiled from: TCell */
public final class cj extends ew {
    public static int a(ev evVar, int i, byte b, int i2, int i3) {
        evVar.b(4);
        evVar.b(3, i3);
        evVar.b(2, i2);
        evVar.b(0, i);
        evVar.a(1, b);
        return evVar.b();
    }

    public static int a(ev evVar, int[] iArr) {
        evVar.a(4, iArr.length, 4);
        for (int length = iArr.length - 1; length >= 0; length--) {
            evVar.a(iArr[length]);
        }
        return evVar.a();
    }

    public static int b(ev evVar, int[] iArr) {
        evVar.a(4, iArr.length, 4);
        for (int length = iArr.length - 1; length >= 0; length--) {
            evVar.a(iArr[length]);
        }
        return evVar.a();
    }
}
