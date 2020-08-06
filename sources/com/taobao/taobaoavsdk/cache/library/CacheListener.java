package com.taobao.taobaoavsdk.cache.library;

import java.io.File;

public interface CacheListener {
    void onCacheAvailable(File file, String str, int i);
}
