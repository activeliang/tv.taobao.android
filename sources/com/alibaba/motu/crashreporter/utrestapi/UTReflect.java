package com.alibaba.motu.crashreporter.utrestapi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UTReflect {
    public static Object invokeSuperClassMethod(Object object, String methodName, Object[] args, Class... classes) {
        try {
            Method m = object.getClass().getSuperclass().getDeclaredMethod(methodName, classes);
            m.setAccessible(true);
            return m.invoke(object, args);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
        }
        return null;
    }

    public static Object invokeSuperClassMethod(Object object, String methodName) {
        try {
            Method m = object.getClass().getSuperclass().getDeclaredMethod(methodName, new Class[0]);
            m.setAccessible(true);
            return m.invoke(object, new Object[0]);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
        }
        return null;
    }

    public static Object invokeStaticMethod(Class c, String methodName) {
        try {
            Method m = c.getDeclaredMethod(methodName, new Class[0]);
            m.setAccessible(true);
            return m.invoke((Object) null, new Object[0]);
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
            return null;
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
            return null;
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
            return null;
        }
    }

    public static Object invokeStaticMethod(Class c, String methodName, Object[] args, Class... classes) {
        try {
            Method m = c.getDeclaredMethod(methodName, classes);
            m.setAccessible(true);
            return m.invoke((Object) null, args);
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
            return null;
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
            return null;
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
            return null;
        }
    }

    public static Object invokeMethod(Object object, String methodName, Object[] args, Class... classes) {
        try {
            Method m = object.getClass().getDeclaredMethod(methodName, classes);
            m.setAccessible(true);
            return m.invoke(object, args);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
        }
        return null;
    }

    public static Object invokeMethod(Object object, String methodName) {
        try {
            Method m = object.getClass().getDeclaredMethod(methodName, new Class[0]);
            m.setAccessible(true);
            return m.invoke(object, new Object[0]);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
        }
        return null;
    }

    public static Object invokeStaticMethod(String className, String methodName) {
        try {
            return invokeStaticMethod(Class.forName(className), methodName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Object invokeStaticMethod(String className, String methodName, Object[] args, Class... classes) {
        try {
            return invokeStaticMethod(Class.forName(className), methodName, args, classes);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
