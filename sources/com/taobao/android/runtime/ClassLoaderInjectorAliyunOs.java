package com.taobao.android.runtime;

import android.content.Context;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

class ClassLoaderInjectorAliyunOs {
    public static final String CLASS_LOADER_ALIYUN = "dalvik.system.LexClassLoader";

    ClassLoaderInjectorAliyunOs() {
    }

    public static void injectInAliyunOs(Context context, ClassLoader localClassLoader, Class classLoaderClass, String libPath, boolean insertAtFirst) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        new DexClassLoader(libPath, context.getDir("dex", 0).getAbsolutePath(), libPath, localClassLoader);
        String lexFileName = new File(libPath).getName().replaceAll("\\.[a-zA-Z0-9]+", ".lex");
        Class<?> classLexClassLoader = Class.forName(CLASS_LOADER_ALIYUN);
        Object localLexClassLoader = classLexClassLoader.getConstructor(new Class[]{String.class, String.class, String.class, ClassLoader.class}).newInstance(new Object[]{context.getDir("dex", 0).getAbsolutePath() + File.separator + lexFileName, context.getDir("dex", 0).getAbsolutePath(), libPath, localClassLoader});
        if (localClassLoader instanceof PathClassLoader) {
            ReflectionUtils.setField(localClassLoader, classLoaderClass, "mPaths", ArrayUtils.appendArray(ReflectionUtils.getField(localClassLoader, classLoaderClass, "mPaths"), ReflectionUtils.getField(localLexClassLoader, classLexClassLoader, "mRawDexPath"), insertAtFirst));
        }
        ReflectionUtils.setField(localClassLoader, classLoaderClass, "mFiles", ArrayUtils.combineArray(ReflectionUtils.getField(localClassLoader, classLoaderClass, "mFiles"), ReflectionUtils.getField(localLexClassLoader, classLexClassLoader, "mFiles"), insertAtFirst));
        ReflectionUtils.setField(localClassLoader, classLoaderClass, "mZips", ArrayUtils.combineArray(ReflectionUtils.getField(localClassLoader, classLoaderClass, "mZips"), ReflectionUtils.getField(localLexClassLoader, classLexClassLoader, "mZips"), insertAtFirst));
        ReflectionUtils.setField(localClassLoader, classLoaderClass, "mLexs", ArrayUtils.combineArray(ReflectionUtils.getField(localClassLoader, classLoaderClass, "mLexs"), ReflectionUtils.getField(localLexClassLoader, classLexClassLoader, "mDexs"), insertAtFirst));
    }

    public static boolean isAliyunOs() {
        try {
            Class.forName(CLASS_LOADER_ALIYUN);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
