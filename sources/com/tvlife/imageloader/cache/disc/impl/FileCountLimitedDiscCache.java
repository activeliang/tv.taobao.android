package com.tvlife.imageloader.cache.disc.impl;

import com.tvlife.imageloader.cache.disc.LimitedDiscCache;
import com.tvlife.imageloader.cache.disc.naming.FileNameGenerator;
import com.tvlife.imageloader.core.DefaultConfigurationFactory;
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
