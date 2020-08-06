package com.loc;

import android.content.Context;
import android.text.TextUtils;
import mtopsdk.common.util.SymbolExpUtil;

/* compiled from: MobileUpdateStrategy */
public final class br extends bu {
    private String b = "iKey";
    private Context c;
    private boolean d;
    private int e;
    private int f;

    public br(Context context, boolean z, int i, int i2, String str) {
        this.c = context;
        this.d = z;
        this.e = i;
        this.f = i2;
        this.b = str;
    }

    public final void a(int i) {
        if (n.r(this.c) != 1) {
            String a = u.a(System.currentTimeMillis(), "yyyyMMdd");
            String a2 = z.a(this.c, this.b);
            if (!TextUtils.isEmpty(a2)) {
                String[] split = a2.split(SymbolExpUtil.SYMBOL_VERTICALBAR);
                if (split == null || split.length < 2) {
                    z.b(this.c, this.b);
                } else if (a.equals(split[0])) {
                    i += Integer.parseInt(split[1]);
                }
            }
            z.a(this.c, this.b, a + "|" + i);
        }
    }

    /* access modifiers changed from: protected */
    public final boolean a() {
        if (n.r(this.c) == 1) {
            return true;
        }
        if (!this.d) {
            return false;
        }
        String a = z.a(this.c, this.b);
        if (TextUtils.isEmpty(a)) {
            return true;
        }
        String[] split = a.split(SymbolExpUtil.SYMBOL_VERTICALBAR);
        if (split == null || split.length < 2) {
            z.b(this.c, this.b);
            return true;
        }
        return !u.a(System.currentTimeMillis(), "yyyyMMdd").equals(split[0]) || Integer.parseInt(split[1]) < this.f;
    }

    public final int b() {
        int i = (n.r(this.c) == 1 || this.e <= 0) ? Integer.MAX_VALUE : this.e;
        return this.a != null ? Math.max(i, this.a.b()) : i;
    }
}
