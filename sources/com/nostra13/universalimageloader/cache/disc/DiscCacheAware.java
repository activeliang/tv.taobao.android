package com.nostra13.universalimageloader.cache.disc;

import java.io.File;

public interface DiscCacheAware {
    void clear();

    File get(String str);

    void put(String str, File file);
}
