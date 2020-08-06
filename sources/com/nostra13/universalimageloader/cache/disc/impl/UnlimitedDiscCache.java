package com.nostra13.universalimageloader.cache.disc.impl;

import com.nostra13.universalimageloader.cache.disc.BaseDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
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
