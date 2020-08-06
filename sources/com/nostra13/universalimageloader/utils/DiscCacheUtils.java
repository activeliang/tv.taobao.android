package com.nostra13.universalimageloader.utils;

import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import java.io.File;

public final class DiscCacheUtils {
    private DiscCacheUtils() {
    }

    public static File findInCache(String imageUri, DiscCacheAware discCache) {
        File image = discCache.get(imageUri);
        if (image.exists()) {
            return image;
        }
        return null;
    }

    public static boolean removeFromCache(String imageUri, DiscCacheAware discCache) {
        return discCache.get(imageUri).delete();
    }
}
