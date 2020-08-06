package com.loc;

import android.os.SystemClock;
import android.util.LongSparseArray;
import java.util.List;

/* compiled from: RssiInfoManager */
public final class cw {
    private static volatile cw g = null;
    private static Object h = new Object();
    private LongSparseArray<a> a = new LongSparseArray<>();
    private LongSparseArray<a> b = new LongSparseArray<>();
    private LongSparseArray<a> c = new LongSparseArray<>();
    private LongSparseArray<a> d = new LongSparseArray<>();
    private Object e = new Object();
    private Object f = new Object();

    /* compiled from: RssiInfoManager */
    private static class a {
        int a;
        long b;
        boolean c;

        private a() {
        }

        /* synthetic */ a(byte b2) {
            this();
        }
    }

    private cw() {
    }

    public static cw a() {
        if (g == null) {
            synchronized (h) {
                if (g == null) {
                    g = new cw();
                }
            }
        }
        return g;
    }

    private static short a(LongSparseArray<a> longSparseArray, long j) {
        short s;
        synchronized (longSparseArray) {
            a aVar = longSparseArray.get(j);
            if (aVar == null) {
                s = 0;
            } else {
                short max = (short) ((int) Math.max(1, Math.min(32767, (SystemClock.elapsedRealtime() - aVar.b) / 1000)));
                s = aVar.c ? max : (short) (-max);
            }
        }
        return s;
    }

    private static void a(List<cv> list, LongSparseArray<a> longSparseArray, LongSparseArray<a> longSparseArray2) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (longSparseArray.size() == 0) {
            for (cv next : list) {
                a aVar = new a((byte) 0);
                aVar.a = next.b();
                aVar.b = elapsedRealtime;
                aVar.c = false;
                longSparseArray2.put(next.a(), aVar);
            }
            return;
        }
        for (cv next2 : list) {
            long a2 = next2.a();
            a aVar2 = longSparseArray.get(a2);
            if (aVar2 == null) {
                aVar2 = new a((byte) 0);
                aVar2.a = next2.b();
                aVar2.b = elapsedRealtime;
                aVar2.c = true;
            } else if (aVar2.a != next2.b()) {
                aVar2.a = next2.b();
                aVar2.b = elapsedRealtime;
                aVar2.c = true;
            }
            longSparseArray2.put(a2, aVar2);
        }
    }

    /* access modifiers changed from: package-private */
    public final short a(long j) {
        return a(this.a, j);
    }

    /* access modifiers changed from: package-private */
    public final void a(List<cv> list) {
        if (!list.isEmpty()) {
            synchronized (this.e) {
                a(list, this.a, this.b);
                LongSparseArray<a> longSparseArray = this.a;
                this.a = this.b;
                this.b = longSparseArray;
                this.b.clear();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final short b(long j) {
        return a(this.c, j);
    }

    /* access modifiers changed from: package-private */
    public final void b(List<cv> list) {
        if (!list.isEmpty()) {
            synchronized (this.f) {
                a(list, this.c, this.d);
                LongSparseArray<a> longSparseArray = this.c;
                this.c = this.d;
                this.d = longSparseArray;
                this.d.clear();
            }
        }
    }
}
