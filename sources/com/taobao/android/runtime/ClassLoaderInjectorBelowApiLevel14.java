package com.taobao.android.runtime;

import android.content.Context;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

class ClassLoaderInjectorBelowApiLevel14 {
    ClassLoaderInjectorBelowApiLevel14() {
    }

    static void injectBelowApiLevel14(Context context, ClassLoader baseDexClassLoader, Class classLoaderClass, String libPath, boolean insertAtFirst) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        DexClassLoader dexClassLoader = new DexClassLoader(libPath, context.getDir("dex", 0).getAbsolutePath(), libPath, context.getClassLoader());
        if (baseDexClassLoader instanceof PathClassLoader) {
            ReflectionUtils.setField(baseDexClassLoader, classLoaderClass, "mPaths", ArrayUtils.appendArray(ReflectionUtils.getField(baseDexClassLoader, classLoaderClass, "mPaths"), ReflectionUtils.getField(dexClassLoader, DexClassLoader.class, "mRawDexPath"), insertAtFirst));
        }
        ReflectionUtils.setField(baseDexClassLoader, classLoaderClass, "mFiles", ArrayUtils.combineArray(ReflectionUtils.getField(baseDexClassLoader, classLoaderClass, "mFiles"), ReflectionUtils.getField(dexClassLoader, DexClassLoader.class, "mFiles"), insertAtFirst));
        ReflectionUtils.setField(baseDexClassLoader, classLoaderClass, "mZips", ArrayUtils.combineArray(ReflectionUtils.getField(baseDexClassLoader, classLoaderClass, "mZips"), ReflectionUtils.getField(dexClassLoader, DexClassLoader.class, "mZips"), insertAtFirst));
        ReflectionUtils.setField(baseDexClassLoader, classLoaderClass, "mDexs", ArrayUtils.combineArray(ReflectionUtils.getField(baseDexClassLoader, classLoaderClass, "mDexs"), ReflectionUtils.getField(dexClassLoader, DexClassLoader.class, "mDexs"), insertAtFirst));
    }
}
