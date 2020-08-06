package com.taobao.taobaoavsdk.cache.library;

import android.content.Context;
import android.os.Environment;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import com.taobao.android.task.Coordinator;
import java.io.File;
import java.io.Serializable;

public final class StorageUtils implements Serializable {
    private static final String INDIVIDUAL_DIR_NAME = "video-cache";
    /* access modifiers changed from: private */
    public static volatile boolean mDeleting;

    public static File getIndividualCacheDirectory(Context context) {
        File file = new File(getCacheDirectory(context, true), INDIVIDUAL_DIR_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private static File getCacheDirectory(Context context, boolean preferExternal) {
        String externalStorageState;
        File appCacheDir = null;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) {
            externalStorageState = "";
        }
        if (preferExternal && "mounted".equals(externalStorageState)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            return new File("/data/data/" + context.getPackageName() + "/cache/");
        }
        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context) {
        File appCacheDir = new File(new File(new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data"), context.getPackageName()), "cache");
        if (appCacheDir.exists() || appCacheDir.mkdirs()) {
            return appCacheDir;
        }
        return null;
    }

    public static void clearVideoCache(Context context) {
        if (!mDeleting) {
            try {
                mDeleting = true;
                final String cacheRoot = getIndividualCacheDirectory(context).getAbsolutePath();
                Coordinator.execute(new Runnable() {
                    public void run() {
                        StorageUtils.deleteFolder(cacheRoot);
                        boolean unused = StorageUtils.mDeleting = false;
                    }
                });
            } catch (Throwable th) {
                mDeleting = false;
            }
        }
    }

    public static void deleteFolder(String dir) {
        File[] oldFile = new File(dir).listFiles();
        int i = 0;
        while (i < oldFile.length) {
            try {
                if (oldFile[i].isDirectory()) {
                    deleteFolder(dir + WVNativeCallbackUtil.SEPERATER + oldFile[i].getName());
                }
                oldFile[i].delete();
                i++;
            } catch (Exception e) {
                return;
            }
        }
    }
}
