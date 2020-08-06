package com.uc.webview.export.cyclone;

import mtopsdk.common.util.SymbolExpUtil;

/* compiled from: ProGuard */
public class UCKnownException extends RuntimeException {
    private int a;

    public UCKnownException(int i, String str, Throwable th) {
        super(str, th);
        this.a = -99999;
        this.a = i;
    }

    public UCKnownException(int i, Throwable th) {
        this(th);
        this.a = i;
    }

    public UCKnownException(int i, String str) {
        super(str);
        this.a = -99999;
        this.a = i;
    }

    public UCKnownException(Throwable th) {
        super(th);
        this.a = -99999;
        Throwable th2 = th;
        while (th2 != null) {
            if (th2 instanceof UCKnownException) {
                this.a = ((UCKnownException) th2).a;
                return;
            } else if (th2.getCause() != null && th2.getCause() != th2) {
                th2 = th2.getCause();
            } else {
                return;
            }
        }
    }

    public static UCKnownException create(Throwable th, int i) {
        if (th instanceof UCKnownException) {
            return (UCKnownException) th;
        }
        return new UCKnownException(i, th);
    }

    public UCKnownException(String str) {
        super(str);
        this.a = -99999;
    }

    public final int errCode() {
        return this.a;
    }

    public final String toString() {
        return this.a + SymbolExpUtil.SYMBOL_COLON + super.toString();
    }

    public final Runnable toRunnable() {
        return new Runnable() {
            public final void run() {
                throw UCKnownException.this;
            }
        };
    }

    public final Throwable getRootCause() {
        while (true) {
            Throwable cause = this.getCause();
            this = this;
            if (cause == null || this.getCause() == this) {
                return this;
            }
            this = this.getCause();
        }
        return this;
    }
}
