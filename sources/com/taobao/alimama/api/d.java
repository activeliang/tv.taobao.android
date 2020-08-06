package com.taobao.alimama.api;

import android.taobao.windvane.util.WVNativeCallbackUtil;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class d {
    private int a = a.a();
    private transient b b = new b();

    private static final class a {
        private static AtomicInteger a = new AtomicInteger(0);

        private a() {
        }

        public static int a() {
            return a.incrementAndGet();
        }
    }

    static final class b {
        Method a;
        String b;
        Object[] c;

        b() {
        }

        public String toString() {
            return " method: " + this.b;
        }
    }

    d() {
    }

    static String b(Method method) {
        StringBuilder sb = new StringBuilder(method.getName());
        for (Class simpleName : method.getParameterTypes()) {
            sb.append("_").append(simpleName.getSimpleName());
        }
        return sb.toString();
    }

    public d a(String str) {
        this.b.b = str;
        return this;
    }

    /* access modifiers changed from: package-private */
    public d a(Method method) {
        this.b.a = method;
        this.b.b = c() + WVNativeCallbackUtil.SEPERATER + d();
        return this;
    }

    /* access modifiers changed from: package-private */
    public d a(Object[] objArr) {
        this.b.c = objArr;
        return this;
    }

    /* access modifiers changed from: package-private */
    public Method a() {
        return this.b.a;
    }

    /* access modifiers changed from: package-private */
    public String b() {
        return this.b.b;
    }

    /* access modifiers changed from: package-private */
    public String c() {
        return this.b.a.getDeclaringClass().getSimpleName();
    }

    /* access modifiers changed from: package-private */
    public String d() {
        return b(this.b.a);
    }

    /* access modifiers changed from: package-private */
    public Object[] e() {
        return this.b.c;
    }

    /* access modifiers changed from: package-private */
    public int f() {
        return this.a;
    }

    public String toString() {
        return "Transaction: [id: " + this.a + ", " + this.b + "]";
    }
}
