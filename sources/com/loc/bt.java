package com.loc;

import android.content.Context;
import android.text.TextUtils;

/* compiled from: TimeUpdateStrategy */
public final class bt extends bu {
    private int b;
    private long c;
    private String d;
    private Context e;

    public bt(Context context, int i, String str, bu buVar) {
        super(buVar);
        this.b = i;
        this.d = str;
        this.e = context;
    }

    public final void a(boolean z) {
        super.a(z);
        if (z) {
            String str = this.d;
            long currentTimeMillis = System.currentTimeMillis();
            this.c = currentTimeMillis;
            z.a(this.e, str, String.valueOf(currentTimeMillis));
        }
    }

    /* access modifiers changed from: protected */
    public final boolean a() {
        long j = 0;
        if (this.c == 0) {
            String a = z.a(this.e, this.d);
            if (!TextUtils.isEmpty(a)) {
                j = Long.parseLong(a);
            }
            this.c = j;
        }
        return System.currentTimeMillis() - this.c >= ((long) this.b);
    }
}
