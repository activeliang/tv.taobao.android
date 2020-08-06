package com.loc;

import android.content.Context;

/* compiled from: OfflineLocEntity */
public final class bd {
    private Context a;
    private t b;
    private String c;

    public bd(Context context, t tVar, String str) {
        this.a = context.getApplicationContext();
        this.b = tVar;
        this.c = str;
    }

    private static String a(Context context, t tVar, String str) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("\"sdkversion\":\"").append(tVar.c()).append("\",\"product\":\"").append(tVar.a()).append("\",\"nt\":\"").append(n.d(context)).append("\",\"details\":").append(str);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return sb.toString();
    }

    /* access modifiers changed from: package-private */
    public final byte[] a() {
        return u.a(a(this.a, this.b, this.c));
    }
}
