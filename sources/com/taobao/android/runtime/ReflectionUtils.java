package com.taobao.android.runtime;

import java.lang.reflect.Field;

class ReflectionUtils {
    ReflectionUtils() {
    }

    public static Object getField(Object obj, String field) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        return getField(obj, obj.getClass(), field);
    }

    public static Object getField(Object obj, Class<?> cl, String field) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    public static void setField(Object obj, String field, Object value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        setField(obj, obj.getClass(), field, value);
    }

    public static void setField(Object obj, Class<?> cl, String field, Object value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        localField.set(obj, value);
    }
}
