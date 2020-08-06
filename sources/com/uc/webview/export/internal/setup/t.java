package com.uc.webview.export.internal.setup;

import android.util.Pair;

/* compiled from: ProGuard */
final class t implements Runnable {
    final /* synthetic */ ap a;
    final /* synthetic */ q b;

    t(q qVar, ap apVar) {
        this.b = qVar;
        this.a = apVar;
    }

    public final void run() {
        try {
            this.b.a();
            ap apVar = this.a;
            synchronized (apVar) {
                apVar.a = new Pair<>(0, (Object) null);
                try {
                    apVar.notify();
                } catch (Exception e) {
                }
            }
        } catch (Throwable th) {
            ap apVar2 = this.a;
            synchronized (apVar2) {
                apVar2.a = new Pair<>(3, th);
                try {
                    apVar2.notify();
                } catch (Exception e2) {
                }
            }
        }
    }
}
