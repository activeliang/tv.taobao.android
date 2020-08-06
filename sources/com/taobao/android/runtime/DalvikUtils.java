package com.taobao.android.runtime;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexFile;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public class DalvikUtils {
    public static final int OPTIMIZE_MODE_ALL = 3;
    public static final int OPTIMIZE_MODE_FULL = 4;
    public static final int OPTIMIZE_MODE_NONE = 1;
    public static final int OPTIMIZE_MODE_UNKNOWN = 0;
    public static final int OPTIMIZE_MODE_VERIFIED = 2;
    private static final String TAG = DalvikUtils.class.getSimpleName();
    public static final int VERIFY_MODE_ALL = 3;
    public static final int VERIFY_MODE_NONE = 1;
    public static final int VERIFY_MODE_REMOTE = 2;
    public static final int VERIFY_MODE_UNKNOWN = 0;
    private static boolean sInit;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ClassVerifyMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DexOptMode {
    }

    private static native boolean addBootClassPathNative(String[] strArr, int[] iArr);

    private static native String bootClassPathNative();

    private static native boolean disableJitCompilationNative();

    private static native boolean dvmJdwpStartupNative(short s);

    private static native int getClassVerifyModeNative();

    private static native int getDexOptModeNative();

    private static native boolean nativeInit();

    private static native boolean setClassVerifyModeNative(int i);

    private static native boolean setDexOptModeNative(int i);

    public static boolean init() {
        try {
            System.loadLibrary("dalvikhack");
            sInit = nativeInit();
            return sInit;
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    public static DexFile loadDex(@NonNull String sourcePathName, @NonNull String outputPathName, int flags) throws IOException {
        long start = System.currentTimeMillis();
        DexFile dexFile = DexFile.loadDex(sourcePathName, outputPathName, flags);
        long currentTimeMillis = System.currentTimeMillis() - start;
        return dexFile;
    }

    @Nullable
    public static Boolean setDexOptMode(int dexOptMode) {
        if (!VMUtil.IS_VM_ART && sInit) {
            return Boolean.valueOf(setDexOptModeNative(dexOptMode));
        }
        return null;
    }

    public static int getDexOptMode() {
        if (!VMUtil.IS_VM_ART && sInit) {
            return getDexOptModeNative();
        }
        return 0;
    }

    @Nullable
    public static Boolean setClassVerifyMode(int classVerifyMode) {
        if (!VMUtil.IS_VM_ART && sInit) {
            return Boolean.valueOf(setClassVerifyModeNative(classVerifyMode));
        }
        return null;
    }

    @Nullable
    public static Boolean disableJitCompilation() {
        if (!VMUtil.IS_VM_ART && sInit) {
            return Boolean.valueOf(disableJitCompilationNative());
        }
        return null;
    }

    @Nullable
    public static Boolean dvmJdwpStartup(short port) {
        if (!VMUtil.IS_VM_ART && sInit) {
            return Boolean.valueOf(dvmJdwpStartupNative(port));
        }
        return null;
    }

    public static int getClassVerifyMode() {
        if (!VMUtil.IS_VM_ART && sInit) {
            return getClassVerifyModeNative();
        }
        return 0;
    }

    public static String bootClassPath() {
        if (!VMUtil.IS_VM_ART && sInit) {
            return bootClassPathNative();
        }
        return null;
    }

    public static Boolean addBootClassPath(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("Illegal null classLoader argument");
        } else if (VMUtil.IS_VM_ART || !sInit) {
            return null;
        } else {
            if (bootClassPath() == null) {
                return false;
            }
            DexFile[] dexFiles = ClassLoaderInjectorAboveApi14.getDexFiles((BaseDexClassLoader) classLoader);
            String[] classPaths = new String[dexFiles.length];
            int[] cookies = new int[dexFiles.length];
            for (int i = 0; i < dexFiles.length; i++) {
                classPaths[i] = dexFiles[i].getName();
            }
            boolean success = addBootClassPathNative(classPaths, cookies);
            Log.d(TAG, "- DalvikUtils addBootClassPath: classPath=" + Arrays.toString(classPaths) + ", success=" + success);
            return Boolean.valueOf(success);
        }
    }
}
