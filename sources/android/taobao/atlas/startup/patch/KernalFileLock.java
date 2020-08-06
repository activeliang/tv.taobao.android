package android.taobao.atlas.startup.patch;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KernalFileLock {
    private static KernalFileLock singleton;
    private Map<String, FileLockCount> mRefCountMap = new ConcurrentHashMap();

    public static KernalFileLock getInstance() {
        if (singleton == null) {
            singleton = new KernalFileLock();
        }
        return singleton;
    }

    private class FileLockCount {
        FileChannel fChannel;
        RandomAccessFile fOs;
        FileLock mFileLock;
        int mRefCount;

        FileLockCount(FileLock mFileLock2, int mRefCount2, RandomAccessFile fOs2, FileChannel fChannel2) {
            this.mFileLock = mFileLock2;
            this.mRefCount = mRefCount2;
            this.fOs = fOs2;
            this.fChannel = fChannel2;
        }
    }

    private int RefCntInc(String name, FileLock fileLock, RandomAccessFile fOs, FileChannel fChannel) {
        Integer val;
        if (this.mRefCountMap.containsKey(name)) {
            FileLockCount fileLockCount = this.mRefCountMap.get(name);
            int i = fileLockCount.mRefCount;
            fileLockCount.mRefCount = i + 1;
            val = Integer.valueOf(i);
        } else {
            val = 1;
            this.mRefCountMap.put(name, new FileLockCount(fileLock, val.intValue(), fOs, fChannel));
        }
        return val.intValue();
    }

    private int RefCntDec(String name) {
        Integer val = 0;
        if (this.mRefCountMap.containsKey(name)) {
            FileLockCount fileLockCount = this.mRefCountMap.get(name);
            int i = fileLockCount.mRefCount - 1;
            fileLockCount.mRefCount = i;
            val = Integer.valueOf(i);
            if (val.intValue() <= 0) {
                this.mRefCountMap.remove(name);
            }
        }
        return val.intValue();
    }

    public boolean LockExclusive(File orgFile) {
        File file;
        if (orgFile == null) {
            return false;
        }
        try {
            if (!orgFile.exists() || !orgFile.isDirectory()) {
                file = new File(orgFile.getParentFile().getAbsolutePath().concat("/lock"));
            } else {
                file = new File(orgFile.getAbsolutePath().concat("/lock"));
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile fOs = new RandomAccessFile(file.getAbsolutePath(), "rw");
            try {
                FileChannel fChannel = fOs.getChannel();
                FileLock fFileLock = fChannel.lock();
                if (fFileLock.isValid()) {
                    RefCntInc(file.getAbsolutePath(), fFileLock, fOs, fChannel);
                    RandomAccessFile randomAccessFile = fOs;
                    return true;
                }
                return false;
            } catch (Exception e) {
                e = e;
                RandomAccessFile randomAccessFile2 = fOs;
                e.printStackTrace();
                return false;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public void unLock(File orgFile) {
        File file;
        FileLockCount flc;
        if (!orgFile.exists() || !orgFile.isDirectory()) {
            file = new File(orgFile.getParentFile().getAbsolutePath().concat("/lock"));
        } else {
            file = new File(orgFile.getAbsolutePath().concat("/lock"));
        }
        if (file.exists()) {
            if ((file == null || this.mRefCountMap.containsKey(file.getAbsolutePath())) && (flc = this.mRefCountMap.get(file.getAbsolutePath())) != null) {
                FileLock fl = flc.mFileLock;
                RandomAccessFile fOs = flc.fOs;
                FileChannel fChannel = flc.fChannel;
                try {
                    if (RefCntDec(file.getAbsolutePath()) <= 0) {
                        if (fl != null && fl.isValid()) {
                            fl.release();
                        }
                        if (fOs != null) {
                            fOs.close();
                        }
                        if (fChannel != null) {
                            fChannel.close();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
