package com.uc.webview.export.internal;

import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.setup.UCSetupException;

/* compiled from: ProGuard */
final class f implements Runnable {
    f() {
    }

    public final void run() {
        while (true) {
            try {
                Runnable runnable = (Runnable) d.b.a.poll();
                if (runnable != null) {
                    runnable.run();
                } else {
                    return;
                }
            } catch (Exception e) {
                d.b.a.clear();
                UCSetupException unused = d.b.b = new UCSetupException(4008, (Throwable) e);
                return;
            }
        }
    }
}
