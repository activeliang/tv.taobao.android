package com.loc;

/* compiled from: RootTUploadData */
public final class ch extends ew {
    public static int a(ev evVar, int i, int i2, int i3) {
        evVar.b(3);
        evVar.b(2, i3);
        evVar.b(1, i2);
        evVar.b(0, i);
        return evVar.b();
    }

    public static int a(ev evVar, byte[] bArr) {
        evVar.a(1, bArr.length, 1);
        for (int length = bArr.length - 1; length >= 0; length--) {
            evVar.a(bArr[length]);
        }
        return evVar.a();
    }

    public static int a(ev evVar, int[] iArr) {
        evVar.a(4, iArr.length, 4);
        for (int length = iArr.length - 1; length >= 0; length--) {
            evVar.a(iArr[length]);
        }
        return evVar.a();
    }

    public static int b(ev evVar, byte[] bArr) {
        evVar.a(1, bArr.length, 1);
        for (int length = bArr.length - 1; length >= 0; length--) {
            evVar.a(bArr[length]);
        }
        return evVar.a();
    }
}
