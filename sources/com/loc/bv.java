package com.loc;

import android.content.Context;

/* compiled from: WiFiUplateStrategy */
public final class bv extends bu {
    private Context b;
    private boolean c = false;

    public bv(Context context) {
        this.b = context;
        this.c = false;
    }

    /* access modifiers changed from: protected */
    public final boolean a() {
        return n.r(this.b) == 1 || this.c;
    }
}
