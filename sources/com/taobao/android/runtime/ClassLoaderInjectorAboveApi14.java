package com.taobao.android.runtime;

import android.content.Context;
import android.util.Log;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import java.lang.reflect.Array;

class ClassLoaderInjectorAboveApi14 {
    private static final String TAG = ClassLoaderInjectorAboveApi14.class.getSimpleName();

    ClassLoaderInjectorAboveApi14() {
    }

    static void injectAboveEqualApiLevel14(Context context, ClassLoader baseDexClassLoader, String libPath, boolean insertAtFirst) {
        Object dexElements = ArrayUtils.combineArray(getDexElements(getPathList(baseDexClassLoader)), getDexElements(getPathList(new DexClassLoader(libPath, context.getDir("dex", 0).getAbsolutePath(), libPath, context.getClassLoader()))), insertAtFirst);
        Object pathList = getPathList(baseDexClassLoader);
        try {
            ReflectionUtils.setField(pathList, pathList.getClass(), "dexElements", dexElements);
            Log.e(TAG, "ClassLoader" + baseDexClassLoader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object[] getCookies(BaseDexClassLoader classloader) {
        Object dexElements = getDexElements(getPathList(classloader));
        int length = Array.getLength(dexElements);
        Object[] cookies = new Object[length];
        for (int i = 0; i < length; i++) {
            cookies[i] = getCookie(getDexFile(Array.get(dexElements, i)));
        }
        return cookies;
    }

    public static DexFile[] getDexFiles(BaseDexClassLoader classloader) {
        Object dexElements = getDexElements(getPathList(classloader));
        int length = Array.getLength(dexElements);
        DexFile[] dexFiles = new DexFile[length];
        for (int i = 0; i < length; i++) {
            dexFiles[i] = getDexFile(Array.get(dexElements, i));
        }
        return dexFiles;
    }

    private static Object getPathList(Object baseDexClassLoader) {
        try {
            return ReflectionUtils.getField(baseDexClassLoader, BaseDexClassLoader.class, "pathList");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object getDexElements(Object paramObject) {
        try {
            return ReflectionUtils.getField(paramObject, "dexElements");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static DexFile getDexFile(Object elements) {
        try {
            return (DexFile) ReflectionUtils.getField(elements, "dexFile");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static Object getCookie(DexFile dexFile) {
        try {
            return ReflectionUtils.getField(dexFile, "mCookie");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
