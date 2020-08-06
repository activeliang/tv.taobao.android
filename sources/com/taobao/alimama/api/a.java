package com.taobao.alimama.api;

import android.content.Context;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class a {
    public static final String a = "api_framework";
    private final Map<Class<?>, Object> b = new HashMap();

    /* renamed from: com.taobao.alimama.api.a$a  reason: collision with other inner class name */
    private static class C0003a {
        /* access modifiers changed from: private */
        public static final a a = new a();

        private C0003a() {
        }
    }

    static a a() {
        return C0003a.a;
    }

    /* access modifiers changed from: package-private */
    public <T> T a(Class<T> cls) {
        T t;
        if (!cls.isInterface()) {
            throw new IllegalArgumentException("only accept interface: " + cls);
        }
        synchronized (this.b) {
            t = this.b.get(cls);
            if (t == null) {
                t = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new c());
                this.b.put(cls, t);
            }
        }
        return t;
    }

    /* access modifiers changed from: package-private */
    public void a(Context context) {
        com.taobao.alimama.api.plugin.a.b().a(context.getApplicationContext());
        b.b().a();
    }
}
