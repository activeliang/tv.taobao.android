package com.alibaba.fastjson.util;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import mtopsdk.common.util.SymbolExpUtil;

public class ASMUtils {
    public static final boolean IS_ANDROID = isAndroid(JAVA_VM_NAME);
    public static final String JAVA_VM_NAME = System.getProperty("java.vm.name");

    public static boolean isAndroid(String vmName) {
        if (vmName == null) {
            return false;
        }
        String lowerVMName = vmName.toLowerCase();
        if (lowerVMName.contains("dalvik") || lowerVMName.contains("lemur")) {
            return true;
        }
        return false;
    }

    public static String desc(Method method) {
        Class<?>[] types = method.getParameterTypes();
        StringBuilder buf = new StringBuilder((types.length + 1) << 4);
        buf.append('(');
        for (Class<?> desc : types) {
            buf.append(desc(desc));
        }
        buf.append(')');
        buf.append(desc(method.getReturnType()));
        return buf.toString();
    }

    public static String desc(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            return getPrimitiveLetter(returnType);
        }
        if (returnType.isArray()) {
            return "[" + desc(returnType.getComponentType());
        }
        return "L" + type(returnType) + SymbolExpUtil.SYMBOL_SEMICOLON;
    }

    public static String type(Class<?> parameterType) {
        if (parameterType.isArray()) {
            return "[" + desc(parameterType.getComponentType());
        }
        if (!parameterType.isPrimitive()) {
            return parameterType.getName().replace('.', '/');
        }
        return getPrimitiveLetter(parameterType);
    }

    public static String getPrimitiveLetter(Class<?> type) {
        if (Integer.TYPE == type) {
            return "I";
        }
        if (Void.TYPE == type) {
            return "V";
        }
        if (Boolean.TYPE == type) {
            return "Z";
        }
        if (Character.TYPE == type) {
            return "C";
        }
        if (Byte.TYPE == type) {
            return "B";
        }
        if (Short.TYPE == type) {
            return "S";
        }
        if (Float.TYPE == type) {
            return "F";
        }
        if (Long.TYPE == type) {
            return "J";
        }
        if (Double.TYPE == type) {
            return "D";
        }
        throw new IllegalStateException("Type: " + type.getCanonicalName() + " is not a primitive type");
    }

    public static Type getMethodType(Class<?> clazz, String methodName) {
        try {
            return clazz.getMethod(methodName, new Class[0]).getGenericReturnType();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean checkName(String name) {
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c < 1 || c > 127 || c == '.') {
                return false;
            }
        }
        return true;
    }
}
