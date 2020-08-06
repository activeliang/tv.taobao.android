package com.tvlife.imageloader.utils;

import android.graphics.Bitmap;
import com.tvlife.imageloader.cache.memory.MemoryCacheAware;
import com.tvlife.imageloader.core.assist.ImageSize;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MemoryCacheUtils {
    private static final String URI_AND_SIZE_SEPARATOR = "_";
    private static final String WIDTH_AND_HEIGHT_SEPARATOR = "x";

    private MemoryCacheUtils() {
    }

    public static String generateKey(String imageUri, ImageSize targetSize) {
        return imageUri + "_" + targetSize.getWidth() + WIDTH_AND_HEIGHT_SEPARATOR + targetSize.getHeight();
    }

    public static Comparator<String> createFuzzyKeyComparator() {
        return new Comparator<String>() {
            public int compare(String key1, String key2) {
                return key1.substring(0, key1.length() - 1).compareTo(key2.substring(0, key2.length() - 1));
            }
        };
    }

    public static List<Bitmap> findCachedBitmapsForImageUri(String imageUri, MemoryCacheAware<String, Bitmap> memoryCache) {
        List<Bitmap> values = new ArrayList<>();
        for (String key : memoryCache.keys()) {
            if (key.startsWith(imageUri)) {
                values.add(memoryCache.get(key));
            }
        }
        return values;
    }

    public static List<String> findCacheKeysForImageUri(String imageUri, MemoryCacheAware<String, Bitmap> memoryCache) {
        List<String> values = new ArrayList<>();
        for (String key : memoryCache.keys()) {
            if (key.startsWith(imageUri)) {
                values.add(key);
            }
        }
        return values;
    }

    public static void removeFromCache(String imageUri, MemoryCacheAware<String, Bitmap> memoryCache) {
        List<String> keysToRemove = new ArrayList<>();
        for (String key : memoryCache.keys()) {
            if (key.startsWith(imageUri)) {
                keysToRemove.add(key);
            }
        }
        for (String keyToRemove : keysToRemove) {
            memoryCache.remove(keyToRemove);
        }
    }
}
