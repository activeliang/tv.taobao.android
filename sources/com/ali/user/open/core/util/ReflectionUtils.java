package com.ali.user.open.core.util;

import com.ali.user.open.core.trace.SDKLogger;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {
    private static final Map<String, Class<?>> PRIMITIVE_CLASSES = new HashMap();
    private static final String TAG = ReflectionUtils.class.getSimpleName();

    static {
        PRIMITIVE_CLASSES.put("short", Short.TYPE);
        PRIMITIVE_CLASSES.put("int", Integer.TYPE);
        PRIMITIVE_CLASSES.put("long", Long.TYPE);
        PRIMITIVE_CLASSES.put("double", Double.TYPE);
        PRIMITIVE_CLASSES.put("float", Float.TYPE);
        PRIMITIVE_CLASSES.put("char", Character.TYPE);
        PRIMITIVE_CLASSES.put("boolean", Boolean.TYPE);
    }

    public static Object invoke(String className, String methodName, String[] paramTypeNames, Object instance, Object[] paramValues) {
        Method method;
        try {
            Class<?> clazz = Class.forName(className);
            if (paramTypeNames == null || paramTypeNames.length == 0) {
                method = clazz.getMethod(methodName, new Class[0]);
            } else {
                method = clazz.getMethod(methodName, toClasses(paramTypeNames));
            }
            return method.invoke(instance, paramValues);
        } catch (Exception e) {
            SDKLogger.e(TAG, "Fail to post the " + className + "." + methodName + ", the error is " + e.getMessage());
            return null;
        }
    }

    public static <T> T newInstance(Class<T> clazz, Class<?>[] paramTypes, Object[] paramValues) {
        if (paramTypes != null) {
            try {
                if (paramTypes.length != 0) {
                    return clazz.getConstructor(paramTypes).newInstance(paramValues);
                }
            } catch (Exception e) {
                SDKLogger.e(TAG, "Fail to create the instance of type " + clazz.getName() + ", the error is " + e.getMessage());
                return null;
            }
        }
        return clazz.newInstance();
    }

    public static Object newInstance(String className, String[] paramTypeNames, Object[] paramValues) {
        try {
            return newInstance(Class.forName(className), (Class<?>[]) toClasses(paramTypeNames), paramValues);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            SDKLogger.e(TAG, "Fail to create the instance of type " + className + ", the error is " + e2.getMessage());
            return null;
        }
    }

    public static Class<?>[] toClasses(String[] classNames) throws Exception {
        if (classNames == null) {
            return null;
        }
        Class<?>[] classes = new Class[classNames.length];
        int length = classNames.length;
        for (int i = 0; i < length; i++) {
            String className = classNames[i];
            if (className.length() < 8) {
                classes[i] = PRIMITIVE_CLASSES.get(className);
            }
            if (classes[i] == null) {
                classes[i] = Class.forName(className);
            }
        }
        return classes;
    }

    public static Class<?> loadClassQuietly(String className) {
        try {
            return Class.forName(className);
        } catch (Throwable th) {
            return null;
        }
    }
}
