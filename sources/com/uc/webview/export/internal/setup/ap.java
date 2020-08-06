package com.uc.webview.export.internal.setup;

import android.util.Pair;
import com.uc.webview.export.cyclone.UCElapseTime;

/* compiled from: ProGuard */
public final class ap {
    Pair<Integer, Object> a = new Pair<>(-1, (Object) null);
    boolean b = false;

    public final Pair<Integer, Object> a(long j) {
        long j2;
        UCElapseTime uCElapseTime = new UCElapseTime();
        synchronized (this) {
            if (j <= 0) {
                j2 = 0;
            } else {
                j2 = 100;
            }
            this.b = true;
            while (true) {
                if (uCElapseTime.getMilis() < j || j <= 0) {
                    try {
                        wait(Math.max(j2, j - uCElapseTime.getMilis()));
                        if (((Integer) this.a.first).intValue() != -1) {
                            this.b = false;
                            Pair<Integer, Object> pair = this.a;
                            return pair;
                        }
                    } catch (InterruptedException e) {
                    }
                } else {
                    this.b = false;
                    return new Pair<>(1, (Object) null);
                }
            }
        }
    }
}
