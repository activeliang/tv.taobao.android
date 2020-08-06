package android.taobao.windvane.xmlmanager;

import android.support.v4.util.LruCache;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

class WVReflectUtils {
    private static final LruCache<String, Method> methodsCache = new LruCache<>(8);

    WVReflectUtils() {
    }

    public static Object getValue(Object target, String name) {
        try {
            if (target instanceof Class) {
                Field field = ((Class) target).getField(name);
                field.setAccessible(true);
                return field.get((Object) null);
            }
            Field field2 = target.getClass().getField(name);
            field2.setAccessible(true);
            return field2.get(target);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object invoke(Object target, String name, Object... args) {
        try {
            Method method = findMethod(target, name, args);
            if (method == null) {
                return null;
            }
            if (target instanceof Class) {
                return method.invoke((Object) null, args);
            }
            return method.invoke(target, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Method findMethod(Object target, String name, Object... args) {
        Class<?> c;
        if (target instanceof Class) {
            c = (Class) target;
        } else {
            c = target.getClass();
        }
        String key = c.getName() + "_" + name;
        Method cache = methodsCache.get(key);
        if (cache != null) {
            return cache;
        }
        while (c != null) {
            Method[] arr$ = c.getDeclaredMethods();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Method method = arr$[i$];
                if (!name.equals(method.getName()) || !isMatchParameterTypes(method.getGenericParameterTypes(), args)) {
                    i$++;
                } else {
                    method.setAccessible(true);
                    methodsCache.put(key, method);
                    return method;
                }
            }
            c = c.getSuperclass();
        }
        return null;
    }

    private static boolean isMatchParameterTypes(Type[] types, Object... args) {
        if (types.length != args.length) {
            return false;
        }
        if (types.length == 0) {
            return true;
        }
        for (int i = 0; i < args.length; i++) {
            Object object = args[i];
            Class<?> type = types[i];
            Class<?> cls = null;
            if (type instanceof Class) {
                cls = type;
            }
            if (cls == null) {
                return true;
            }
            if (object.getClass() == type) {
            }
        }
        return true;
    }
}
