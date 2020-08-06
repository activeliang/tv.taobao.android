package com.tvlife.imageloader.cache.disc.impl;

import com.tvlife.imageloader.cache.disc.BaseDiscCache;
import com.tvlife.imageloader.cache.disc.naming.FileNameGenerator;
import com.tvlife.imageloader.core.DefaultConfigurationFactory;
import java.io.File;

public class UnlimitedDiscCache extends BaseDiscCache {
    public UnlimitedDiscCache(File cacheDir) {
        this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator());
    }

    public UnlimitedDiscCache(File cacheDir, FileNameGenerator fileNameGenerator) {
        super(cacheDir, fileNameGenerator);
    }

    public void put(String key, File file) {
    }
}
