package com.loc;

import java.util.ArrayList;
import java.util.List;

/* compiled from: RssiManager */
public final class cx {

    /* compiled from: RssiManager */
    public static class a implements cv {
        private int a;
        private int b;
        private int c;

        a(int i, int i2, int i3) {
            this.a = i;
            this.b = i2;
            this.c = i3;
        }

        public final long a() {
            return cx.a(this.a, this.b);
        }

        public final int b() {
            return this.c;
        }
    }

    /* compiled from: RssiManager */
    public static class b implements cv {
        private long a;
        private int b;

        b(long j, int i) {
            this.a = j;
            this.b = i;
        }

        public final long a() {
            return this.a;
        }

        public final int b() {
            return this.b;
        }
    }

    public static long a(int i, int i2) {
        return ((((long) i) & 4294967295L) << 32) | (((long) i2) & 4294967295L);
    }

    public static synchronized short a(long j) {
        short a2;
        synchronized (cx.class) {
            a2 = cw.a().a(j);
        }
        return a2;
    }

    public static synchronized void a(List<da> list) {
        synchronized (cx.class) {
            if (list != null) {
                if (!list.isEmpty()) {
                    ArrayList arrayList = new ArrayList(list.size());
                    for (da next : list) {
                        if (next instanceof dc) {
                            dc dcVar = (dc) next;
                            arrayList.add(new a(dcVar.j, dcVar.k, dcVar.c));
                        } else if (next instanceof dd) {
                            dd ddVar = (dd) next;
                            arrayList.add(new a(ddVar.j, ddVar.k, ddVar.c));
                        } else if (next instanceof de) {
                            de deVar = (de) next;
                            arrayList.add(new a(deVar.j, deVar.k, deVar.c));
                        } else if (next instanceof db) {
                            db dbVar = (db) next;
                            arrayList.add(new a(dbVar.k, dbVar.l, dbVar.c));
                        }
                    }
                    cw.a().a((List<cv>) arrayList);
                }
            }
        }
    }

    public static synchronized short b(long j) {
        short b2;
        synchronized (cx.class) {
            b2 = cw.a().b(j);
        }
        return b2;
    }

    public static synchronized void b(List<dh> list) {
        synchronized (cx.class) {
            if (list != null) {
                if (!list.isEmpty()) {
                    ArrayList arrayList = new ArrayList(list.size());
                    for (dh next : list) {
                        arrayList.add(new b(next.a, next.c));
                    }
                    cw.a().b((List<cv>) arrayList);
                }
            }
        }
    }
}
