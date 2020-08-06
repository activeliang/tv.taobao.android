package com.loc;

import android.content.Context;
import com.taobao.media.MediaConstant;
import java.util.Iterator;
import java.util.List;

/* compiled from: SDKDBOperation */
public final class ak {
    private af a;
    private Context b;

    public ak(Context context, boolean z) {
        this.b = context;
        this.a = a(this.b, z);
    }

    private static af a(Context context, boolean z) {
        try {
            return new af(context, af.a((Class<? extends ae>) aj.class));
        } catch (Throwable th) {
            if (!z) {
                ab.b(th, MediaConstant.DEFINITION_SD, "gdb");
            }
            return null;
        }
    }

    public final List<t> a() {
        try {
            return this.a.a(t.g(), t.class, true);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public final void a(t tVar) {
        boolean z;
        if (tVar != null) {
            try {
                if (this.a == null) {
                    this.a = a(this.b, false);
                }
                String a2 = t.a(tVar.a());
                List<t> a3 = this.a.a(a2, t.class, false);
                if (a3.size() == 0) {
                    this.a.a(tVar);
                    return;
                }
                Iterator<t> it = a3.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (it.next().equals(tVar)) {
                            z = false;
                            break;
                        }
                    } else {
                        z = true;
                        break;
                    }
                }
                if (z) {
                    this.a.a(a2, (Object) tVar);
                }
            } catch (Throwable th) {
                ab.b(th, MediaConstant.DEFINITION_SD, "it");
            }
        }
    }
}
