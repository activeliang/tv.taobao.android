package com.alibaba.motu.crashreporter;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.alibaba.motu.crashreporter.utils.Base64;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import mtopsdk.common.util.SymbolExpUtil;

public class Utils {
    private static final int kSystemRootStateDisable = 0;
    private static final int kSystemRootStateEnable = 1;
    private static final int kSystemRootStateUnknow = -1;
    private static int systemRootState = -1;

    public interface ReaderListener {
        boolean onReadLine(String str);
    }

    public static void getMTLMetaData(Map<String, Object> meta, Context context) {
        if (context != null) {
            try {
                if (!meta.containsKey("pt")) {
                    String pt = getMTLString(context, "package_type");
                    if (!TextUtils.isEmpty(pt)) {
                        meta.put("pt", pt);
                    }
                }
                if (!meta.containsKey("pid")) {
                    String pid = getMTLString(context, "project_id");
                    if (!TextUtils.isEmpty(pid)) {
                        meta.put("pid", pid);
                    }
                }
                if (!meta.containsKey("bid")) {
                    String bid = getMTLString(context, "build_id");
                    if (!TextUtils.isEmpty(bid)) {
                        meta.put("bid", bid);
                    }
                }
                if (!meta.containsKey("bv")) {
                    String bv = getMTLString(context, "base_version");
                    if (!TextUtils.isEmpty(bv)) {
                        meta.put("bv", bv);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public static String getMTLString(Context context, String paramString) {
        if (context == null) {
            return null;
        }
        int id = 0;
        try {
            id = context.getResources().getIdentifier(paramString, "string", context.getPackageName());
        } catch (Exception e) {
        }
        if (id != 0) {
            return context.getString(id);
        }
        return null;
    }

    public static Long getContextFirstInstallTime(Context context) {
        if (context != null) {
            try {
                if (Build.VERSION.SDK_INT >= 9) {
                    return Long.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime);
                }
            } catch (Exception e) {
                LogUtil.d("get context first install time failure");
            }
        }
        return null;
    }

    public static Long getContextLastUpdateTime(Context context) {
        if (context != null) {
            try {
                if (Build.VERSION.SDK_INT >= 9) {
                    return Long.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).lastUpdateTime);
                }
            } catch (Exception e) {
                LogUtil.d("get context last update time failure");
            }
        }
        return null;
    }

    public static String getContextAppVersion(Context context) {
        if (context != null) {
            try {
                return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (Exception e) {
                LogUtil.d("get context app version failure");
            }
        }
        return null;
    }

    public static byte[] IntGetBytes(int i) {
        byte[] bInt = new byte[4];
        int value = i;
        bInt[3] = (byte) (value % 256);
        int value2 = value >> 8;
        bInt[2] = (byte) (value2 % 256);
        int value3 = value2 >> 8;
        bInt[1] = (byte) (value3 % 256);
        bInt[0] = (byte) ((value3 >> 8) % 256);
        return bInt;
    }

    public static final String getUniqueID() {
        try {
            int t1 = (int) (System.currentTimeMillis() / 1000);
            int t3 = new Random().nextInt();
            int t4 = new Random().nextInt();
            byte[] b1 = IntGetBytes(t1);
            byte[] b2 = IntGetBytes((int) System.nanoTime());
            byte[] b3 = IntGetBytes(t3);
            byte[] b4 = IntGetBytes(t4);
            byte[] bUniqueID = new byte[16];
            System.arraycopy(b1, 0, bUniqueID, 0, 4);
            System.arraycopy(b2, 0, bUniqueID, 4, 4);
            System.arraycopy(b3, 0, bUniqueID, 8, 4);
            System.arraycopy(b4, 0, bUniqueID, 12, 4);
            return Base64.encodeBase64String(bUniqueID);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getImei(Context context) {
        String imei = null;
        if (context != null) {
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
                if (tm != null) {
                    imei = tm.getDeviceId();
                }
            } catch (Exception e) {
            }
        }
        if (StringUtils.isEmpty(imei)) {
            return getUniqueID();
        }
        return imei;
    }

    public static String getImsi(Context context) {
        String imsi = null;
        if (context != null) {
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
                if (tm != null) {
                    imsi = tm.getSubscriberId();
                }
            } catch (Exception e) {
            }
        }
        if (StringUtils.isEmpty(imsi)) {
            return getUniqueID();
        }
        return imsi;
    }

    public static String getGMT8Time(long timestamp) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return simpleDateFormat.format(new Date(timestamp));
        } catch (Exception e) {
            LogUtil.e("getGMT8Time", e);
            return "";
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                LogUtil.e("close.", e);
            }
        }
    }

    public static void readLine(File file, ReaderListener listener) {
        String line;
        BufferedReader reader = null;
        try {
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            do {
                try {
                    line = reader2.readLine();
                    if (line == null) {
                        closeQuietly(reader2);
                        BufferedReader bufferedReader = reader2;
                        return;
                    }
                } catch (IOException e) {
                    e = e;
                    reader = reader2;
                    try {
                        LogUtil.e("readLine", e);
                        closeQuietly(reader);
                    } catch (Throwable th) {
                        th = th;
                        closeQuietly(reader);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    reader = reader2;
                    closeQuietly(reader);
                    throw th;
                }
            } while (!listener.onReadLine(line));
            closeQuietly(reader2);
            BufferedReader bufferedReader2 = reader2;
        } catch (IOException e2) {
            e = e2;
            LogUtil.e("readLine", e);
            closeQuietly(reader);
        }
    }

    public static List<String> readLines(File file, int n) {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = null;
        try {
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            int count = 0;
            while (true) {
                try {
                    String line = reader2.readLine();
                    if (line == null) {
                        break;
                    }
                    count++;
                    lines.add(line);
                    if (n > 0 && count >= n) {
                        break;
                    }
                } catch (IOException e) {
                    e = e;
                    reader = reader2;
                    try {
                        LogUtil.e("readLine", e);
                        closeQuietly(reader);
                        return lines;
                    } catch (Throwable th) {
                        th = th;
                        closeQuietly(reader);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    reader = reader2;
                    closeQuietly(reader);
                    throw th;
                }
            }
            closeQuietly(reader2);
            BufferedReader bufferedReader = reader2;
        } catch (IOException e2) {
            e = e2;
            LogUtil.e("readLine", e);
            closeQuietly(reader);
            return lines;
        }
        return lines;
    }

    public static String readFully(File file) {
        InputStreamReader input;
        StringBuilder builder = new StringBuilder();
        FileInputStream in = null;
        InputStreamReader input2 = null;
        try {
            FileInputStream in2 = new FileInputStream(file);
            try {
                input = new InputStreamReader(in2);
            } catch (Exception e) {
                e = e;
                in = in2;
                try {
                    LogUtil.e("readFully.", e);
                    closeQuietly(input2);
                    closeQuietly(in);
                    return builder.toString();
                } catch (Throwable th) {
                    th = th;
                    closeQuietly(input2);
                    closeQuietly(in);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                in = in2;
                closeQuietly(input2);
                closeQuietly(in);
                throw th;
            }
            try {
                char[] buffer = new char[4096];
                while (true) {
                    int n = input.read(buffer);
                    if (-1 == n) {
                        break;
                    }
                    builder.append(buffer, 0, n);
                }
                closeQuietly(input);
                closeQuietly(in2);
                InputStreamReader inputStreamReader = input;
                FileInputStream fileInputStream = in2;
            } catch (Exception e2) {
                e = e2;
                input2 = input;
                in = in2;
                LogUtil.e("readFully.", e);
                closeQuietly(input2);
                closeQuietly(in);
                return builder.toString();
            } catch (Throwable th3) {
                th = th3;
                input2 = input;
                in = in2;
                closeQuietly(input2);
                closeQuietly(in);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            LogUtil.e("readFully.", e);
            closeQuietly(input2);
            closeQuietly(in);
            return builder.toString();
        }
        return builder.toString();
    }

    public static String readLine(File file) {
        List<String> lines = readLines(file, 1);
        return !lines.isEmpty() ? lines.get(0) : "";
    }

    public static String readLine(String filePath) {
        return readLine(new File(filePath));
    }

    public static String readLineAndDel(File file) {
        String line = readLine(file);
        try {
            file.delete();
        } catch (Exception e) {
            LogUtil.e("readLineAndDel", e);
        }
        return line;
    }

    public static boolean writeFile(File file, String str) {
        return writeFile(file, str, false);
    }

    public static boolean writeFile(File file, String str, boolean append) {
        FileWriter writer = null;
        try {
            FileWriter writer2 = new FileWriter(file, append);
            try {
                writer2.write(str);
                writer2.flush();
                closeQuietly(writer2);
                FileWriter fileWriter = writer2;
                return true;
            } catch (IOException e) {
                e = e;
                writer = writer2;
                try {
                    LogUtil.e("writeFile", e);
                    closeQuietly(writer);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    closeQuietly(writer);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                writer = writer2;
                closeQuietly(writer);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            LogUtil.e("writeFile", e);
            closeQuietly(writer);
            return false;
        }
    }

    public static String getMyProcessNameByCmdline() {
        return readLine("/proc/self/cmdline").trim();
    }

    public static Boolean isBackgroundRunning(Context context) {
        try {
            if (Integer.parseInt(readLine("/proc/self/oom_adj").trim()) == 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getMyStatus() {
        return readFully(new File("/proc/self/status")).trim();
    }

    public static String getMeminfo() {
        return readFully(new File("/proc/meminfo")).trim();
    }

    public static String getMyProcessNameByAppProcessInfo(Context context) {
        if (context != null) {
            int pid = Process.myPid();
            try {
                for (ActivityManager.RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
                    if (appProcess.pid == pid) {
                        return appProcess.processName;
                    }
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static String reverse(String str) {
        if (StringUtils.isBlank(str)) {
            return "LLUN";
        }
        int len = 0;
        if (str.length() > 48) {
            len = str.length() - 48;
            str = str.substring(0, 48);
        }
        StringBuilder sb = new StringBuilder();
        byte[] bytes = str.getBytes();
        for (int blen = bytes.length - 1; blen >= 0; blen--) {
            byte b = bytes[blen];
            if (b == 46) {
                sb.append('0');
            } else if (b == 58) {
                sb.append('1');
            } else if (b >= 97 && b <= 122) {
                sb.append((char) ((b + 65) - 97));
            } else if (b >= 65 && b <= 90) {
                sb.append((char) b);
            } else if (b < 48 || b > 57) {
                sb.append('2');
            } else {
                sb.append((char) b);
            }
        }
        if (len > 0) {
            sb.append(String.valueOf(len));
        }
        return sb.toString();
    }

    public static Boolean isServiceProcess(String processName) {
        if (processName != null) {
            return Boolean.valueOf(processName.contains(SymbolExpUtil.SYMBOL_COLON));
        }
        return false;
    }

    public static Boolean isUIProcess(Context context, String processName) {
        if (context == null || processName == null) {
            return false;
        }
        return Boolean.valueOf(context.getPackageName().equals(processName));
    }

    public static Boolean isMainThread(Thread thread) {
        boolean z = false;
        if (thread == null) {
            return false;
        }
        if (Looper.getMainLooper().getThread() == thread) {
            z = true;
        }
        return Boolean.valueOf(z);
    }

    public static Boolean isLockScreenOn(Context context) {
        try {
            if (((KeyguardManager) context.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            LogUtil.e("isLockScreenOn", e);
            return false;
        }
    }

    public static boolean isRootSystem() {
        File f;
        if (systemRootState == 1) {
            return true;
        }
        if (systemRootState == 0) {
            return false;
        }
        File f2 = null;
        String[] kSuSearchPaths = {"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};
        int i = 0;
        while (true) {
            try {
                f = f2;
                if (i >= kSuSearchPaths.length) {
                    File file = f;
                    break;
                }
                f2 = new File(kSuSearchPaths[i] + "su");
                if (f2 != null) {
                    try {
                        if (f2.exists()) {
                            systemRootState = 1;
                            return true;
                        }
                    } catch (Exception e) {
                        e = e;
                        LogUtil.e("isRootSystem", e);
                        systemRootState = 0;
                        return false;
                    }
                }
                i++;
            } catch (Exception e2) {
                e = e2;
                File file2 = f;
                LogUtil.e("isRootSystem", e);
                systemRootState = 0;
                return false;
            }
        }
        systemRootState = 0;
        return false;
    }

    public static boolean isInstallOnSDCard(Context context) {
        try {
            if ((context.getApplicationInfo().flags & 262144) != 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            LogUtil.e("isInstallOnSDCard", e);
        }
    }

    public static void stopService(Context context) {
        try {
            int uid = Process.myUid();
            String packageName = context.getPackageName();
            for (ActivityManager.RunningServiceInfo localRunningServiceInfo : ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningServices(Integer.MAX_VALUE)) {
                if (localRunningServiceInfo.uid == uid && packageName.equals(localRunningServiceInfo.service.getPackageName()) && localRunningServiceInfo.started) {
                    ComponentName serviceCMP = localRunningServiceInfo.service;
                    Intent intent = new Intent();
                    intent.setComponent(serviceCMP);
                    context.stopService(intent);
                }
            }
        } catch (Exception e) {
            LogUtil.e("stopService", e);
        }
    }

    public static String dumpThread(Thread thread) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(String.format("Thread Name: '%s'\n", new Object[]{thread.getName()}));
            sb.append(String.format("\"%s\" prio=%d tid=%d %s\n", new Object[]{thread.getName(), Integer.valueOf(thread.getPriority()), Long.valueOf(thread.getId()), thread.getState()}));
            StackTraceElement[] arr$ = thread.getStackTrace();
            int len$ = arr$.length;
            for (int i$ = 0; i$ < len$; i$++) {
                sb.append(String.format("\tat %s\n", new Object[]{arr$[i$].toString()}));
            }
        } catch (Exception e) {
            LogUtil.e("dumpThread", e);
        }
        return sb.toString();
    }

    public static String dumpMeminfo(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            Integer threshold = null;
            if (activityManager != null) {
                activityManager.getMemoryInfo(memoryInfo);
                threshold = Integer.valueOf((int) (memoryInfo.threshold >> 10));
            }
            return "JavaTotal:" + Runtime.getRuntime().totalMemory() + " JavaFree:" + Runtime.getRuntime().freeMemory() + " NativeHeap:" + Debug.getNativeHeapSize() + " NativeAllocated:" + Debug.getNativeHeapAllocatedSize() + " NativeFree:" + Debug.getNativeHeapFreeSize() + " threshold:" + (threshold != null ? threshold + " KB" : "not valid");
        } catch (Exception e) {
            LogUtil.e("dumpMeminfo", e);
            return "";
        }
    }

    private static long[] getSizes(String path) {
        long blockSize;
        long blockCount;
        long freeBlocks;
        long availableBlocks;
        long[] sizes = {-1, -1, -1};
        try {
            StatFs statFs = new StatFs(path);
            if (Build.VERSION.SDK_INT < 18) {
                blockSize = (long) statFs.getBlockSize();
                blockCount = (long) statFs.getBlockCount();
                freeBlocks = (long) statFs.getFreeBlocks();
                availableBlocks = (long) statFs.getAvailableBlocks();
            } else {
                blockSize = statFs.getBlockSizeLong();
                blockCount = statFs.getBlockCountLong();
                freeBlocks = statFs.getFreeBlocksLong();
                availableBlocks = statFs.getAvailableBlocksLong();
            }
            sizes[0] = blockSize * blockCount;
            sizes[1] = blockSize * freeBlocks;
            sizes[2] = blockSize * availableBlocks;
        } catch (Exception e) {
            LogUtil.e("getSizes", e);
        }
        return sizes;
    }

    public static String dumpStorage(Context context) {
        StringBuilder stringBuffer = new StringBuilder();
        boolean hasSDCard = false;
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                hasSDCard = true;
            }
        } catch (Exception e) {
            LogUtil.w("hasSDCard", e);
        }
        boolean installSDCard = false;
        try {
            if ((context.getApplicationInfo().flags & 262144) != 0) {
                installSDCard = true;
            }
        } catch (Exception e2) {
            LogUtil.w("installSDCard", e2);
        }
        stringBuffer.append("hasSDCard: " + hasSDCard + "\n");
        stringBuffer.append("installSDCard: " + installSDCard + "\n");
        try {
            File rootDir = Environment.getRootDirectory();
            if (rootDir != null) {
                long[] sizes = getSizes(rootDir.getAbsolutePath());
                stringBuffer.append("RootDirectory: " + rootDir.getAbsolutePath() + " ");
                stringBuffer.append(String.format("TotalSize: %s FreeSize: %s AvailableSize: %s \n", new Object[]{Long.valueOf(sizes[0]), Long.valueOf(sizes[1]), Long.valueOf(sizes[2])}));
            }
            File dataDir = Environment.getDataDirectory();
            if (dataDir != null) {
                long[] sizes2 = getSizes(dataDir.getAbsolutePath());
                stringBuffer.append("DataDirectory: " + dataDir.getAbsolutePath() + " ");
                stringBuffer.append(String.format("TotalSize: %s FreeSize: %s AvailableSize: %s \n", new Object[]{Long.valueOf(sizes2[0]), Long.valueOf(sizes2[1]), Long.valueOf(sizes2[2])}));
            }
            File externalStorageDir = Environment.getExternalStorageDirectory();
            if (dataDir != null) {
                stringBuffer.append("ExternalStorageDirectory: " + externalStorageDir.getAbsolutePath() + " ");
                long[] sizes3 = getSizes(externalStorageDir.getAbsolutePath());
                stringBuffer.append(String.format("TotalSize: %s FreeSize: %s AvailableSize: %s \n", new Object[]{Long.valueOf(sizes3[0]), Long.valueOf(sizes3[1]), Long.valueOf(sizes3[2])}));
            }
            File downloadCacheDir = Environment.getDownloadCacheDirectory();
            if (downloadCacheDir != null) {
                stringBuffer.append("DownloadCacheDirectory: " + downloadCacheDir.getAbsolutePath() + " ");
                long[] sizes4 = getSizes(downloadCacheDir.getAbsolutePath());
                stringBuffer.append(String.format("TotalSize: %s FreeSize: %s AvailableSize: %s \n", new Object[]{Long.valueOf(sizes4[0]), Long.valueOf(sizes4[1]), Long.valueOf(sizes4[2])}));
            }
        } catch (Exception e3) {
            LogUtil.e("getSizes", e3);
        }
        return stringBuffer.toString();
    }

    public static class SystemPropertiesUtils {
        static boolean initSuccess;
        static Class<?> mSystemPropertiesClazz;
        static Method mSystemPropertiesClazz_getMethod;
        static Method mSystemPropertiesClazz_setMethod;

        static {
            initSuccess = false;
            try {
                mSystemPropertiesClazz = Class.forName("android.os.SystemProperties");
                mSystemPropertiesClazz_getMethod = mSystemPropertiesClazz.getMethod("get", new Class[]{String.class});
                mSystemPropertiesClazz_setMethod = mSystemPropertiesClazz.getMethod("set", new Class[]{String.class, String.class});
                initSuccess = true;
            } catch (Exception e) {
                LogUtil.e("init system properties utils");
            }
        }

        public static String get(String key) {
            if (!initSuccess) {
                return null;
            }
            if (StringUtils.isBlank(key)) {
                return null;
            }
            try {
                return (String) mSystemPropertiesClazz_getMethod.invoke(mSystemPropertiesClazz, new Object[]{key});
            } catch (Exception e) {
                LogUtil.e("invoke system properties get", e);
                return null;
            }
        }

        public static void set(String key, String val) {
            if (initSuccess && !StringUtils.isBlank(key) && !StringUtils.isBlank(val)) {
                try {
                    mSystemPropertiesClazz_setMethod.invoke(mSystemPropertiesClazz, new Object[]{key, val});
                } catch (Exception e) {
                    LogUtil.e("invoke system properties set", e);
                }
            }
        }
    }

    public static class VMRuntimeUtils {
        static boolean initSuccess;
        static Object mRuntime;
        static Class<?> mVMRuntimeClazz;
        static Method mVMRuntimeClazz_DisableJitCompilationMethod;
        static Method mVMRuntimeClazz_IsDebuggerActiveMethod;
        static Method mVMRuntimeClazz_StartJitCompilationMethod;

        static {
            initSuccess = false;
            try {
                mVMRuntimeClazz = Class.forName("dalvik.system.VMRuntime");
                mRuntime = mVMRuntimeClazz.getMethod("getRuntime", new Class[0]).invoke(mVMRuntimeClazz, new Object[0]);
                mVMRuntimeClazz_IsDebuggerActiveMethod = mVMRuntimeClazz.getMethod("isDebuggerActive", new Class[0]);
                mVMRuntimeClazz_StartJitCompilationMethod = mVMRuntimeClazz.getMethod("startJitCompilation", new Class[0]);
                mVMRuntimeClazz_DisableJitCompilationMethod = mVMRuntimeClazz.getMethod("disableJitCompilation", new Class[0]);
                initSuccess = true;
            } catch (Exception e) {
                LogUtil.e("init system properties utils");
            }
        }

        public static boolean isDebuggerActive() {
            if (!initSuccess) {
                return false;
            }
            try {
                return ((Boolean) mVMRuntimeClazz_IsDebuggerActiveMethod.invoke(mRuntime, new Object[0])).booleanValue();
            } catch (Exception e) {
                LogUtil.e("isDebuggerActive", e);
                return false;
            }
        }

        public static boolean startJitCompilation() {
            if (!initSuccess) {
                return false;
            }
            try {
                mVMRuntimeClazz_StartJitCompilationMethod.invoke(mRuntime, new Object[0]);
                return true;
            } catch (Exception e) {
                LogUtil.e("startJitCompilation", e);
                return false;
            }
        }

        public static boolean disableJitCompilation() {
            if (!initSuccess) {
                return false;
            }
            try {
                mVMRuntimeClazz_DisableJitCompilationMethod.invoke(mRuntime, new Object[0]);
                return true;
            } catch (Exception e) {
                LogUtil.e("disableJitCompilation", e);
                return false;
            }
        }
    }
}
