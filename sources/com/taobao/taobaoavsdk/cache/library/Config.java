package com.taobao.taobaoavsdk.cache.library;

import com.taobao.taobaoavsdk.cache.library.file.DiskUsage;
import com.taobao.taobaoavsdk.cache.library.file.FileNameGenerator;
import java.io.File;

class Config {
    public final File cacheRoot;
    public final DiskUsage diskUsage;
    public final FileNameGenerator fileNameGenerator;

    Config(File cacheRoot2, FileNameGenerator fileNameGenerator2, DiskUsage diskUsage2) {
        this.cacheRoot = cacheRoot2;
        this.fileNameGenerator = fileNameGenerator2;
        this.diskUsage = diskUsage2;
    }

    /* access modifiers changed from: package-private */
    public File generateCacheFile(String url) {
        return new File(this.cacheRoot, this.fileNameGenerator.generate(url));
    }
}
