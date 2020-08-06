package com.yunos.tvtaobao.tvsdk.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectUtils {
    protected static final boolean DEBUG = false;

    public static boolean getInternalBoolean(String inerClass, String fieldName) {
        try {
            Class c = Class.forName(inerClass);
            Object obj = c.newInstance();
            Field field = c.getField(fieldName);
            makeAccessible(field);
            return field.getBoolean(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getInternalInt(String inerClass, String fieldName) {
        try {
            Class c = Class.forName(inerClass);
            Object obj = c.newInstance();
            Field field = c.getField(fieldName);
            makeAccessible(field);
            return field.getInt(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int[] getInternalIntArray(String inerClass, String fieldName) {
        int[] id = new int[0];
        try {
            Class c = Class.forName(inerClass);
            Object obj = c.newInstance();
            Field field = c.getField(fieldName);
            makeAccessible(field);
            return (int[]) field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        }
        method.setAccessible(true);
        try {
            return method.invoke(object, parameters);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    public static Object invokeMethod(Object object, String methodName, Object[] parameters) {
        Class[] parameterTypes = new Class[parameters.length];
        int j = parameters.length;
        for (int i = 0; i < j; i++) {
            parameterTypes[i] = parameters[i].getClass();
        }
        Method method = getDeclaredMethod(object, methodName, (Class<?>[]) parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        }
        method.setAccessible(true);
        try {
            return method.invoke(object, parameters);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
        return getDeclaredMethod(object.getClass(), methodName, parameterTypes);
    }

    private static Method getDeclaredMethod(Class<?> object, String methodName, Class<?>[] parameterTypes) {
        Class<?> superClass = object;
        while (superClass != Object.class) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                superClass = superClass.getSuperclass();
            }
        }
        return null;
    }

    public static Object invokeSuperMethod(Object owner, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Exception {
        Method method = getDeclaredMethod((Class<?>) owner.getClass().getSuperclass(), methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + owner + "]");
        }
        method.setAccessible(true);
        try {
            return method.invoke(owner, parameters);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    public static Object getProperty(Object object, String fieldName) throws Exception {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    private static Field getDeclaredField(Object object, String fieldName) {
        Class cls = object.getClass();
        while (cls != Object.class) {
            try {
                return cls.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            }
        }
        return null;
    }

    private static Field getDeclaredField(Class<?> object, String fieldName) {
        Class<?> superClass = object;
        while (superClass != Object.class) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                superClass = superClass.getSuperclass();
            }
        }
        return null;
    }

    public static void setProperty(Object object, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object getStaticProperty(String className, String fieldName) {
        try {
            Class<?> ownerClass = Class.forName(className);
            Field field = getDeclaredField(ownerClass, fieldName);
            if (field == null) {
                throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + className + "]");
            }
            makeAccessible(field);
            try {
                return field.get(ownerClass);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static Object invokeStaticMethod(String className, String methodName, Object[] args) throws Exception {
        Class[] argsClass = new Class[args.length];
        int j = args.length;
        for (int i = 0; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        return invokeStaticMethod(className, methodName, argsClass, args);
    }

    public static Object invokeStaticMethod(String className, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        try {
            Method method = getDeclaredMethod(Class.forName(className), methodName, parameterTypes);
            if (method == null) {
                throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + className + "]");
            }
            method.setAccessible(true);
            return method.invoke((Object) null, parameters);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    public static Object newInstance(String className, Object[] args) throws Exception {
        Class<?> newoneClass = Class.forName(className);
        Class[] argsClass = new Class[args.length];
        int j = args.length;
        for (int i = 0; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Constructor<?> cons = newoneClass.getConstructor(argsClass);
        cons.setAccessible(true);
        return cons.newInstance(args);
    }

    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        return convertReflectionExceptionToUnchecked((String) null, e);
    }

    public static RuntimeException convertReflectionExceptionToUnchecked(String desc, Exception e) {
        if (desc == null) {
            desc = "Unexpected Checked Exception.";
        }
        if ((e instanceof IllegalAccessException) || (e instanceof IllegalArgumentException) || (e instanceof NoSuchMethodException)) {
            return new IllegalArgumentException(desc, e);
        }
        if (e instanceof InvocationTargetException) {
            return new RuntimeException(desc, ((InvocationTargetException) e).getTargetException());
        }
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException(desc, e);
    }
}
