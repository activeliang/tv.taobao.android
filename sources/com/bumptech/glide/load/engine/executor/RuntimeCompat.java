package com.bumptech.glide.load.engine.executor;

import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

final class RuntimeCompat {
    private static final String CPU_LOCATION = "/sys/devices/system/cpu/";
    private static final String CPU_NAME_REGEX = "cpu[0-9]+";
    private static final String TAG = "GlideRuntimeCompat";

    private RuntimeCompat() {
    }

    static int availableProcessors() {
        int cpus = Runtime.getRuntime().availableProcessors();
        if (Build.VERSION.SDK_INT < 17) {
            return Math.max(getCoreCountPre17(), cpus);
        }
        return cpus;
    }

    private static int getCoreCountPre17() {
        int i;
        File[] cpus = null;
        StrictMode.ThreadPolicy originalPolicy = StrictMode.allowThreadDiskReads();
        try {
            File cpuInfo = new File(CPU_LOCATION);
            final Pattern cpuNamePattern = Pattern.compile(CPU_NAME_REGEX);
            cpus = cpuInfo.listFiles(new FilenameFilter() {
                public boolean accept(File file, String s) {
                    return cpuNamePattern.matcher(s).matches();
                }
            });
        } catch (Throwable t) {
            if (Log.isLoggable(TAG, 6)) {
                Log.e(TAG, "Failed to calculate accurate cpu count", t);
            }
        } finally {
            StrictMode.setThreadPolicy(originalPolicy);
        }
        if (cpus != null) {
            i = cpus.length;
        } else {
            i = 0;
        }
        return Math.max(1, i);
    }
}
