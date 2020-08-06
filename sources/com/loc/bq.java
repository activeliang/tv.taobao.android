package com.loc;

import java.io.File;

/* compiled from: FileNumUpdateStrategy */
public final class bq extends bu {
    private int b = 30;
    private String c;

    public bq(String str, bu buVar) {
        super(buVar);
        this.c = str;
    }

    private static int a(String str) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                return 0;
            }
            return file.list().length;
        } catch (Throwable th) {
            ab.b(th, "fus", "gfn");
            return 0;
        }
    }

    /* access modifiers changed from: protected */
    public final boolean a() {
        return a(this.c) >= this.b;
    }
}
