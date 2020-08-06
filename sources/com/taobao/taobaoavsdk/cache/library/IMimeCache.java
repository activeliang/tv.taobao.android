package com.taobao.taobaoavsdk.cache.library;

public interface IMimeCache {
    UrlMime getMime(String str);

    void putMime(String str, int i, String str2);
}
