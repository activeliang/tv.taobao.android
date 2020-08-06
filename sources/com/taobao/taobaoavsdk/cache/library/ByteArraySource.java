package com.taobao.taobaoavsdk.cache.library;

import java.io.ByteArrayInputStream;

public class ByteArraySource implements Source {
    private ByteArrayInputStream arrayInputStream;
    private final byte[] data;

    public ByteArraySource(byte[] data2) {
        this.data = data2;
    }

    public int read(byte[] buffer) throws ProxyCacheException {
        return this.arrayInputStream.read(buffer, 0, buffer.length);
    }

    public int length() throws ProxyCacheException {
        return this.data.length;
    }

    public void open(int offset, boolean withoutCache) throws ProxyCacheException {
        this.arrayInputStream = new ByteArrayInputStream(this.data);
        this.arrayInputStream.skip((long) offset);
    }

    public void close() throws ProxyCacheException {
    }
}
