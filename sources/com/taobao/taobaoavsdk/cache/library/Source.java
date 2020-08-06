package com.taobao.taobaoavsdk.cache.library;

public interface Source {
    void close() throws ProxyCacheException;

    int length() throws ProxyCacheException;

    void open(int i, boolean z) throws ProxyCacheException;

    int read(byte[] bArr) throws ProxyCacheException;
}
