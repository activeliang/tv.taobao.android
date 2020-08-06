package com.taobao.taobaoavsdk.cache.library;

public interface Cache {
    void append(byte[] bArr, int i) throws ProxyCacheException;

    int available() throws ProxyCacheException;

    void close() throws ProxyCacheException;

    void complete() throws ProxyCacheException;

    boolean isCompleted();

    int read(byte[] bArr, long j, int i) throws ProxyCacheException;
}
