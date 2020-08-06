package com.nostra13.universalimageloader.cache.disc.impl;

import com.nostra13.universalimageloader.cache.disc.LimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import java.io.File;

public class FileCountLimitedDiscCache extends LimitedDiscCache {
    public FileCountLimitedDiscCache(File cacheDir, int maxFileCount) {
        this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator(), maxFileCount);
    }

    public FileCountLimitedDiscCache(File cacheDir, FileNameGenerator fileNameGenerator, int maxFileCount) {
        super(cacheDir, fileNameGenerator, maxFileCount);
    }

    /* access modifiers changed from: protected */
    public int getSize(File file) {
        return 1;
    }
}
