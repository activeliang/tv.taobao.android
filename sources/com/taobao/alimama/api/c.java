package com.taobao.alimama.api;

import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class c implements InvocationHandler {

    private static class a {
        private static final Map<Class, Object> a = new HashMap(7);

        static {
            a.put(Integer.TYPE, 0);
            a.put(Long.TYPE, 0L);
            a.put(Boolean.TYPE, false);
            a.put(Byte.TYPE, (byte) 0);
            a.put(Float.TYPE, Float.valueOf(0.0f));
            a.put(Double.TYPE, Double.valueOf(ClientTraceData.b.f47a));
            a.put(Character.TYPE, 0);
        }

        private a() {
        }

        /* access modifiers changed from: private */
        public static Object b(Class cls, Object obj) {
            return (obj != null || !a.containsKey(cls)) ? obj : a.get(cls);
        }
    }

    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        d dVar = new d();
        dVar.a(objArr);
        dVar.a(method);
        return a.b(method.getReturnType(), b.b().a(dVar));
    }
}
