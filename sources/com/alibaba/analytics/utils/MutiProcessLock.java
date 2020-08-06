package com.alibaba.analytics.utils;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class MutiProcessLock {
    private static final String TAG = "MutiProcessLock";
    static FileChannel fChannel;
    static FileLock mLock;
    static File mLockFile = null;

    public static synchronized boolean lock(Context context) {
        FileLock lock;
        boolean z = true;
        synchronized (MutiProcessLock.class) {
            if (mLockFile == null) {
                mLockFile = new File(context.getFilesDir() + File.separator + "Analytics.Lock");
            }
            boolean exists = mLockFile.exists();
            if (!exists) {
                try {
                    exists = mLockFile.createNewFile();
                } catch (IOException e) {
                }
            }
            if (exists) {
                if (fChannel == null) {
                    try {
                        fChannel = new RandomAccessFile(mLockFile, "rw").getChannel();
                    } catch (Exception e2) {
                        z = false;
                    }
                }
                try {
                    lock = fChannel.tryLock();
                    if (lock != null) {
                        mLock = lock;
                    }
                } catch (Throwable th) {
                    lock = null;
                }
                Log.d("TAG", "mLock:" + lock);
                z = false;
            }
        }
        return z;
    }

    public static synchronized void release() {
        synchronized (MutiProcessLock.class) {
            if (mLock != null) {
                try {
                    mLock.release();
                    mLock = null;
                } catch (Exception e) {
                    fChannel = null;
                } catch (IOException e2) {
                    mLock = null;
                } catch (Throwable th) {
                    mLock = null;
                    throw th;
                }
            }
            if (fChannel != null) {
                fChannel.close();
                fChannel = null;
            }
        }
        return;
    }
}
