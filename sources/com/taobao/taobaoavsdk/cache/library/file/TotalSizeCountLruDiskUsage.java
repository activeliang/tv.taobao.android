package com.taobao.taobaoavsdk.cache.library.file;

import java.io.File;
import java.io.IOException;

public class TotalSizeCountLruDiskUsage extends LruDiskUsage {
    private final int maxCount;
    private final long maxSize;

    public /* bridge */ /* synthetic */ void touch(File file) throws IOException {
        super.touch(file);
    }

    public TotalSizeCountLruDiskUsage(long maxSize2, int maxCount2) {
        if (maxSize2 <= 0) {
            throw new IllegalArgumentException("Max size must be positive number!");
        } else if (maxCount2 <= 0) {
            throw new IllegalArgumentException("Max count must be positive number!");
        } else {
            this.maxSize = maxSize2;
            this.maxCount = maxCount2;
        }
    }

    /* access modifiers changed from: protected */
    public boolean accept(File file, long totalSize, int totalCount) {
        return totalSize <= this.maxSize && totalCount <= this.maxCount;
    }
}
