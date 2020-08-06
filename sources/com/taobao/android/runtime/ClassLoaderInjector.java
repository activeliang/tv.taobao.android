package com.taobao.android.runtime;

import android.content.Context;
import android.util.Log;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.io.FileNotFoundException;

public final class ClassLoaderInjector {
    private static final String CLASS_LOADER_BASE_DEX = "dalvik.system.BaseDexClassLoader";
    private static final String TAG = "ClassLoaderInjector";

    public static void inject(Context context, String libPath, String antiLazyClass, boolean injectAtFirst) throws Exception {
        inject(context, (PathClassLoader) context.getClassLoader(), libPath, antiLazyClass, injectAtFirst);
    }

    public static void inject(Context context, ClassLoader localClassLoader, String libPath, String antiLazyClass, boolean injectAtFirst) throws Exception {
        Class classLoaderClass;
        Class classLoaderClass2;
        if (libPath != null) {
            File file = new File(libPath);
            if (!file.exists()) {
                throw new FileNotFoundException("Hotpatch file does not exist:" + file.getAbsolutePath());
            } else if (ClassLoaderInjectorAliyunOs.isAliyunOs()) {
                if (localClassLoader instanceof PathClassLoader) {
                    classLoaderClass2 = PathClassLoader.class;
                } else {
                    classLoaderClass2 = DexClassLoader.class;
                }
                ClassLoaderInjectorAliyunOs.injectInAliyunOs(context, localClassLoader, classLoaderClass2, libPath, injectAtFirst);
            } else if (!hasBaseDexClassLoader()) {
                try {
                    if (localClassLoader instanceof PathClassLoader) {
                        classLoaderClass = PathClassLoader.class;
                    } else {
                        classLoaderClass = DexClassLoader.class;
                    }
                    ClassLoaderInjectorBelowApiLevel14.injectBelowApiLevel14(context, localClassLoader, classLoaderClass, libPath, injectAtFirst);
                } catch (Throwable e) {
                    Log.e(TAG, "fail to inject file " + file.getAbsolutePath(), e);
                    throw new Exception(e);
                }
            } else {
                try {
                    ClassLoaderInjectorAboveApi14.injectAboveEqualApiLevel14(context, localClassLoader, libPath, injectAtFirst);
                } catch (Throwable e2) {
                    Log.e(TAG, "fail to inject file " + file.getAbsolutePath(), e2);
                    throw new Exception(e2);
                }
            }
        }
    }

    static boolean hasBaseDexClassLoader() {
        try {
            Class.forName(CLASS_LOADER_BASE_DEX);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
