package com.alibaba.wireless.security.framework.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class d {
    private final Object a;
    private final boolean b = false;

    private static class a {
        private a() {
        }
    }

    private d(Object obj) {
        this.a = obj;
    }

    public static d a(Object obj) {
        return new d(obj);
    }

    private static d a(Method method, Object obj, Object... objArr) throws e {
        try {
            a(method);
            if (method.getReturnType() != Void.TYPE) {
                return a(method.invoke(obj, objArr));
            }
            method.invoke(obj, objArr);
            return a(obj);
        } catch (Exception e) {
            throw new e(e);
        }
    }

    public static Class<?> a(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        return cls.isPrimitive() ? Boolean.TYPE == cls ? Boolean.class : Integer.TYPE == cls ? Integer.class : Long.TYPE == cls ? Long.class : Short.TYPE == cls ? Short.class : Byte.TYPE == cls ? Byte.class : Double.TYPE == cls ? Double.class : Float.TYPE == cls ? Float.class : Character.TYPE == cls ? Character.class : Void.TYPE == cls ? Void.class : cls : cls;
    }

    public static <T extends AccessibleObject> T a(T t) {
        if (t == null) {
            return null;
        }
        if (t instanceof Member) {
            Member member = (Member) t;
            if (Modifier.isPublic(member.getModifiers()) && Modifier.isPublic(member.getDeclaringClass().getModifiers())) {
                return t;
            }
        }
        if (t.isAccessible()) {
            return t;
        }
        t.setAccessible(true);
        return t;
    }

    private Method a(String str, Class<?>[] clsArr) throws NoSuchMethodException {
        Class b2 = b();
        try {
            return b2.getMethod(str, clsArr);
        } catch (NoSuchMethodException e) {
            do {
                try {
                    return b2.getDeclaredMethod(str, clsArr);
                } catch (NoSuchMethodException e2) {
                    b2 = b2.getSuperclass();
                    if (b2 != null) {
                        throw new NoSuchMethodException();
                    }
                }
            } while (b2 != null);
            throw new NoSuchMethodException();
        }
    }

    private boolean a(Method method, String str, Class<?>[] clsArr) {
        return method.getName().equals(str) && a((Class<?>[]) method.getParameterTypes(), clsArr);
    }

    private boolean a(Class<?>[] clsArr, Class<?>[] clsArr2) {
        if (clsArr.length != clsArr2.length) {
            return false;
        }
        for (int i = 0; i < clsArr2.length; i++) {
            if (clsArr2[i] != a.class && !a(clsArr[i]).isAssignableFrom(a(clsArr2[i]))) {
                return false;
            }
        }
        return true;
    }

    private static Class<?>[] a(Object... objArr) {
        if (objArr == null) {
            return new Class[0];
        }
        Class[] clsArr = new Class[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            Object obj = objArr[i];
            clsArr[i] = obj == null ? a.class : obj.getClass();
        }
        return clsArr;
    }

    private Method b(String str, Class<?>[] clsArr) throws NoSuchMethodException {
        Class b2 = b();
        for (Method method : b2.getMethods()) {
            if (a(method, str, clsArr)) {
                return method;
            }
        }
        do {
            for (Method method2 : b2.getDeclaredMethods()) {
                if (a(method2, str, clsArr)) {
                    return method2;
                }
            }
            b2 = b2.getSuperclass();
        } while (b2 != null);
        throw new NoSuchMethodException("No similar method " + str + " with params " + Arrays.toString(clsArr) + " could be found on type " + b() + ".");
    }

    public d a(String str, Object... objArr) throws e {
        Class[] a2 = a(objArr);
        try {
            return a(a(str, (Class<?>[]) a2), this.a, objArr);
        } catch (NoSuchMethodException e) {
            try {
                return a(b(str, a2), this.a, objArr);
            } catch (NoSuchMethodException e2) {
                throw new e(e2);
            }
        }
    }

    public <T> T a() {
        return this.a;
    }

    public Class<?> b() {
        return this.b ? (Class) this.a : this.a.getClass();
    }

    public boolean equals(Object obj) {
        if (obj instanceof d) {
            return this.a.equals(((d) obj).a());
        }
        return false;
    }

    public int hashCode() {
        return this.a.hashCode();
    }

    public String toString() {
        return this.a.toString();
    }
}
