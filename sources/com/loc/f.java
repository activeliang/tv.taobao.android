package com.loc;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.text.TextUtils;

/* compiled from: ApsServiceCore */
public final class f {
    e a = null;
    Context b = null;
    Messenger c = null;

    public f(Context context) {
        this.b = context.getApplicationContext();
        this.a = new e(this.b);
    }

    public final IBinder a(Intent intent) {
        e eVar = this.a;
        String stringExtra = intent.getStringExtra("a");
        if (!TextUtils.isEmpty(stringExtra)) {
            l.a(eVar.e, stringExtra);
        }
        eVar.a = intent.getStringExtra("b");
        k.a(eVar.a);
        String stringExtra2 = intent.getStringExtra("d");
        if (!TextUtils.isEmpty(stringExtra2)) {
            n.a(stringExtra2);
        }
        em.a = intent.getBooleanExtra("f", true);
        e eVar2 = this.a;
        if ("true".equals(intent.getStringExtra("as")) && eVar2.d != null) {
            eVar2.d.sendEmptyMessageDelayed(9, 100);
        }
        this.c = new Messenger(this.a.d);
        return this.c.getBinder();
    }

    public final void a() {
        try {
            e.d();
            this.a.j = et.b();
            this.a.k = et.a();
            this.a.a();
        } catch (Throwable th) {
            en.a(th, "ApsServiceCore", "onCreate");
        }
    }

    public final void b() {
        try {
            if (this.a != null) {
                this.a.d.sendEmptyMessage(11);
            }
        } catch (Throwable th) {
            en.a(th, "ApsServiceCore", "onDestroy");
        }
    }
}
