package com.alibaba.motu.crashreporter;

import android.content.Context;
import com.alibaba.motu.crashreporter.utils.StringUtils;
import com.taobao.orange.model.NameSpaceDO;
import java.io.File;
import java.io.FileFilter;

final class StorageManager {
    final Context mContext;
    final File mProcessTombstoneDir;
    final String mProcessTombstoneDirPath;
    final File mTombstoneDir;
    final String mTombstoneDirPath;

    StorageManager(Context context, String processName) {
        this.mContext = context;
        this.mTombstoneDir = this.mContext.getDir("tombstone", 0);
        this.mTombstoneDirPath = this.mTombstoneDir.getAbsolutePath();
        this.mProcessTombstoneDirPath = this.mTombstoneDirPath + File.separator + processName;
        this.mProcessTombstoneDir = new File(this.mProcessTombstoneDirPath);
        if (this.mProcessTombstoneDir.exists() && this.mProcessTombstoneDir.isFile()) {
            this.mProcessTombstoneDir.delete();
        }
        this.mProcessTombstoneDir.mkdirs();
    }

    StorageManager(Context context, String tombstoneDir, String processName) {
        this.mContext = context;
        this.mTombstoneDirPath = tombstoneDir;
        this.mTombstoneDir = new File(this.mTombstoneDirPath);
        this.mProcessTombstoneDirPath = tombstoneDir + File.separator + (!StringUtils.isNotBlank(processName) ? NameSpaceDO.LEVEL_DEFAULT : processName);
        this.mProcessTombstoneDir = new File(this.mProcessTombstoneDirPath);
        if (this.mProcessTombstoneDir.exists() && this.mProcessTombstoneDir.isFile()) {
            this.mProcessTombstoneDir.delete();
        }
        this.mProcessTombstoneDir.mkdirs();
    }

    public File getProcessTombstoneFile(String fileName) {
        if (!StringUtils.isBlank(fileName) && !fileName.contains(File.separator)) {
            return new File(this.mProcessTombstoneDirPath + File.separator + fileName);
        }
        throw new IllegalArgumentException("file name can't not empty or contains " + File.separator);
    }

    public File[] listProcessTombstoneFiles(FileFilter fileFilter) {
        return this.mProcessTombstoneDir.listFiles(fileFilter);
    }
}
