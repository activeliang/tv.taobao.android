package com.loc;

import android.os.SystemClock;
import com.loc.cb;
import java.util.List;

/* compiled from: FpsCollector */
public final class cc {
    private static volatile cc g = null;
    private static Object h = new Object();
    private cb a = new cb();
    private cd b = new cd();
    private long c;
    private dg d;
    private by e = new by();
    private dg f = new dg();

    /* compiled from: FpsCollector */
    public static class a {
        public dg a;
        public List<dh> b;
        public long c;
        public long d;
        public boolean e;
        public long f;
        public byte g;
        public String h;
        public List<da> i;
        public boolean j;
    }

    private cc() {
    }

    public static cc a() {
        if (g == null) {
            synchronized (h) {
                if (g == null) {
                    g = new cc();
                }
            }
        }
        return g;
    }

    public final ce a(a aVar) {
        ce ceVar;
        if (aVar == null) {
            return null;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (this.d != null && aVar.a.a(this.d) < 10.0d) {
            return null;
        }
        cb.a a2 = this.a.a(aVar.a, aVar.j, aVar.g, aVar.h, aVar.i);
        List<dh> a3 = this.b.a(aVar.a, aVar.b, aVar.e, aVar.d, currentTimeMillis);
        if (a2 == null && a3 == null) {
            ceVar = null;
        } else {
            dg dgVar = this.f;
            dg dgVar2 = aVar.a;
            long j = aVar.f;
            dgVar.k = j;
            dgVar.b = j;
            dgVar.c = currentTimeMillis;
            dgVar.e = dgVar2.e;
            dgVar.d = dgVar2.d;
            dgVar.f = dgVar2.f;
            dgVar.i = dgVar2.i;
            dgVar.g = dgVar2.g;
            dgVar.h = dgVar2.h;
            ceVar = new ce(0, this.e.a(this.f, a2, aVar.c, a3));
        }
        this.d = aVar.a;
        this.c = elapsedRealtime;
        return ceVar;
    }
}
