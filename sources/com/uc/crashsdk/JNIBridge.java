package com.uc.crashsdk;

import com.alibaba.motu.crashreporter.utils.StringUtils;

public class JNIBridge {
    static String logFileNamePart1 = "native";

    public static native boolean nativeAddCachedInfo(String str, String str2);

    public static native int nativeAddCallbackInfo(String str, boolean z, boolean z2, long j);

    public static native int nativeAddDumpFile(String str, String str2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5);

    public static native void nativeAddHeaderInfo(String str, String str2);

    public static native void nativeBreakpadInited(String str);

    public static native void nativeChangeState(String str, boolean z);

    public static native void nativeCloseFile(int i);

    public static native void nativeCrash(int i);

    public static native boolean nativeCreateCachedInfo(String str, int i);

    public static native String nativeDumpThreads();

    public static native int nativeGenerateUnexpLog(long j);

    public static native String nativeGetCallbackInfo(String str, long j);

    public static native String nativeGetNativeBuildseq();

    public static native void nativeInitNative();

    public static native void nativeInstallBreakpad();

    public static native boolean nativeLockFile(int i, boolean z);

    public static native int nativeOpenFile(String str);

    public static native void nativePrepareUnexpInfos(boolean z);

    public static native void nativeRegisterCurrentThread(String str);

    public static native void nativeReserveFileHandle(int i, int i2);

    public static native void nativeSetCrashCustoms(boolean z, boolean z2, int i, int i2, int i3, int i4, boolean z3, boolean z4, boolean z5, boolean z6, int i5, boolean z7);

    public static native void nativeSetCrashLogFileNames(String str, String str2, String str3);

    public static native void nativeSetCrashLogFilesUploaded();

    public static native void nativeSetFolderNames(String str, String str2, String str3);

    public static native void nativeSetForeground(boolean z);

    public static native void nativeSetLogStrategy(boolean z);

    public static native void nativeSetMobileInfo(String str, String str2, String str3);

    public static native void nativeSetPackageInfo(String str, String str2, String str3);

    public static native void nativeSetProcessNames(String str, String str2);

    public static native void nativeSetProcessType(boolean z);

    public static native void nativeSetVersionInfo(String str, String str2, String str3, String str4);

    public static native void nativeSetZip(boolean z, String str, int i);

    public static native void nativeUninstallBreakpad();

    public static native void nativeUpdateCrashLogNames();

    private static long getMaxHeapSize() {
        return Runtime.getRuntime().maxMemory();
    }

    public static void setLogFileNamePart1(String logFileNamePart12) {
        logFileNamePart1 = logFileNamePart12;
    }

    private static String getLogFileNamePart1() {
        return StringUtils.isNotBlank(logFileNamePart1) ? logFileNamePart1 : "native";
    }

    private static String getProcessList(String paramString1, String paramString2) {
        return "";
    }

    private static String getJavaMemory() {
        return "";
    }

    protected static String getCallbackInfo(String paramString) {
        return "";
    }

    private static String getJavaStackTrace() {
        StackTraceElement[] arrayOfStackTraceElement1 = Thread.currentThread().getStackTrace();
        StringBuffer localStringBuffer = new StringBuffer();
        int i = 0;
        if (arrayOfStackTraceElement1 != null && arrayOfStackTraceElement1.length > 0) {
            int j = 0;
            StackTraceElement[] arrayOfStackTraceElement2 = arrayOfStackTraceElement1;
            int k = arrayOfStackTraceElement1.length;
            for (int m = 0; m < k; m++) {
                StackTraceElement localStackTraceElement = arrayOfStackTraceElement2[m];
                if (j != 0) {
                    i++;
                    localStringBuffer.append("  at ");
                    localStringBuffer.append(localStackTraceElement.toString());
                    localStringBuffer.append("\n");
                }
                if (j == 0 && localStackTraceElement.getMethodName().indexOf("getJavaStackTrace") >= 0) {
                    j = 1;
                }
            }
        }
        if (i == 0) {
            localStringBuffer.append("  (no java stack)\n");
        }
        return localStringBuffer.toString();
    }

    private static void onCrashLogGenerated(String paramString, boolean paramBoolean) {
    }

    private static void onCrashRestarting() {
    }

    private static void addHeaderInfo(String paramString1, String paramString2) {
    }

    private static int registerCurrentThread(String paramString, int paramInt) {
        return 0;
    }

    private static int registerInfoCallback(String paramString, int paramInt, long paramLong) {
        return 0;
    }

    private static int addDumpFile(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, int paramInt, boolean paramBoolean3) {
        return 0;
    }

    private static int createCachedInfo(String paramString, int paramInt1, int paramInt2) {
        return 0;
    }

    private static int addCachedInfo(String paramString1, String paramString2) {
        return 0;
    }

    private static boolean generateCustomLog(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5) {
        return false;
    }
}
