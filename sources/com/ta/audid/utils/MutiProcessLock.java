package com.ta.audid.utils;

import com.ta.audid.upload.UtdidKeyFile;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class MutiProcessLock {
    private static FileChannel fChannel;
    private static FileChannel fUploadChannel;
    private static FileLock mLock;
    private static File mLockFile = null;
    private static FileLock mUploadLock;
    private static File mUploadLockFile = null;

    public static synchronized void lockUtdidFile() {
        synchronized (MutiProcessLock.class) {
            UtdidLogger.d();
            if (mLockFile == null) {
                mLockFile = new File(UtdidKeyFile.getFileLockPath());
            }
            if (!mLockFile.exists()) {
                try {
                    mLockFile.createNewFile();
                } catch (Exception e) {
                }
            }
            if (fChannel == null) {
                try {
                    fChannel = new RandomAccessFile(mLockFile, "rw").getChannel();
                } catch (Exception e2) {
                }
            }
            try {
                mLock = fChannel.lock();
            } catch (Throwable th) {
            }
        }
    }

    public static synchronized void releaseUtdidFile() {
        synchronized (MutiProcessLock.class) {
            UtdidLogger.d();
            if (mLock != null) {
                try {
                    mLock.release();
                    mLock = null;
                } catch (Exception e) {
                    fChannel = null;
                } catch (Exception e2) {
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

    public static synchronized boolean trylockUpload() {
        boolean z = false;
        synchronized (MutiProcessLock.class) {
            UtdidLogger.d();
            if (mUploadLockFile == null) {
                mUploadLockFile = new File(UtdidKeyFile.getUploadFileLockPath());
            }
            if (!mUploadLockFile.exists()) {
                try {
                    mUploadLockFile.createNewFile();
                } catch (Exception e) {
                }
            }
            if (fUploadChannel == null) {
                try {
                    fUploadChannel = new RandomAccessFile(mUploadLockFile, "rw").getChannel();
                } catch (Exception e2) {
                }
            }
            try {
                FileLock lock = fUploadChannel.tryLock();
                if (lock != null) {
                    mUploadLock = lock;
                    z = true;
                }
            } catch (Throwable th) {
            }
        }
        return z;
    }

    public static synchronized void releaseUpload() {
        synchronized (MutiProcessLock.class) {
            UtdidLogger.d();
            if (mUploadLock != null) {
                try {
                    mUploadLock.release();
                    mUploadLock = null;
                } catch (Exception e) {
                    fUploadChannel = null;
                } catch (Exception e2) {
                    mUploadLock = null;
                } catch (Throwable th) {
                    mUploadLock = null;
                    throw th;
                }
            }
            if (fUploadChannel != null) {
                fUploadChannel.close();
                fUploadChannel = null;
            }
        }
        return;
    }
}
