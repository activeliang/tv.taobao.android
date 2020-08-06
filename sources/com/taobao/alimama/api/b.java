package com.taobao.alimama.api;

import android.os.SystemClock;
import android.util.Log;

class b {
    private e a;

    private static class a {
        /* access modifiers changed from: private */
        public static final b a = new b();

        private a() {
        }
    }

    b() {
    }

    static b b() {
        return a.a;
    }

    /* access modifiers changed from: package-private */
    public Object a(d dVar) {
        if (this.a != null) {
            return this.a.a(dVar);
        }
        throw new IllegalStateException("SDK Not initialized!");
    }

    /* access modifiers changed from: package-private */
    public void a() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Log.i(a.a, "init invocation manager...");
        this.a = new e();
        Log.i(a.a, "invocation manager load done, cost=" + (SystemClock.elapsedRealtime() - elapsedRealtime));
    }
}
