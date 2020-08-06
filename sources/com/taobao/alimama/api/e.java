package com.taobao.alimama.api;

import android.util.Log;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class e {
    private final Map<String, a> a = new HashMap();

    private static class a {
        private final Map<String, Method> a = new HashMap();
        private AbsServiceImpl b;

        a(Class<?> cls, Class<? extends AbsServiceImpl> cls2) {
            for (Method method : cls.getDeclaredMethods()) {
                this.a.put(d.b(method), method);
            }
            try {
                this.b = (AbsServiceImpl) cls2.newInstance();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        /* access modifiers changed from: package-private */
        public Object a(d dVar) throws Throwable {
            return this.a.get(dVar.d()).invoke(this.b, dVar.e());
        }
    }

    e() {
        for (Map.Entry next : com.taobao.alimama.api.plugin.a.b().a().entrySet()) {
            a((Class) next.getKey(), (Class) next.getValue());
        }
        Log.i(a.a, "register service completed, count=" + this.a.size());
    }

    private void a(Class<?> cls, Class<? extends AbsServiceImpl> cls2) {
        Log.i(a.a, "build service, service=" + cls.getSimpleName() + ", impl=" + cls2.getCanonicalName());
        this.a.put(cls.getSimpleName(), new a(cls, cls2));
    }

    /* access modifiers changed from: package-private */
    public Object a(d dVar) {
        a aVar = this.a.get(dVar.c());
        if (aVar == null) {
            Log.e(a.a, "service not found, transaction= " + dVar);
            return null;
        }
        try {
            Log.v(a.a, "execute transaction " + dVar);
            return aVar.a(dVar);
        } catch (Throwable th) {
            th.printStackTrace();
            Log.e(a.a, "execute " + dVar + " error", th);
            return null;
        }
    }
}
