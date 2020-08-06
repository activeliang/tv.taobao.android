package com.taobao.android.runtime;

import java.lang.reflect.Array;

class ArrayUtils {
    ArrayUtils() {
    }

    public static Object combineArray(Object arrayLhs, Object arrayRhs, boolean rhsAtFirst) {
        if (rhsAtFirst) {
            Object temp = arrayLhs;
            arrayLhs = arrayRhs;
            arrayRhs = temp;
        }
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; k++) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    public static Object appendArray(Object array, Object value, boolean insertAtFirst) {
        Class<?> localClass = array.getClass().getComponentType();
        int i = Array.getLength(array);
        int j = i + 1;
        Object localObject = Array.newInstance(localClass, j);
        if (insertAtFirst) {
            Array.set(localObject, 0, value);
            for (int k = 1; k < j; k++) {
                Array.set(localObject, k, Array.get(array, k - 1));
            }
        } else {
            for (int k2 = 0; k2 < j; k2++) {
                if (k2 < i) {
                    Array.set(localObject, k2, Array.get(array, k2));
                } else {
                    Array.set(localObject, k2, value);
                }
            }
        }
        return localObject;
    }
}
