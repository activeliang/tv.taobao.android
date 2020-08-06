package com.taobao.taobaoavsdk.cache.library;

import anetwork.channel.aidl.ParcelableInputStream;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.InputStream;

public class InputStreamProxy {
    private BufferedInputStream mBufferedInputStream;
    private ParcelableInputStream mParcelableInputStream;

    private InputStreamProxy() {
    }

    public InputStreamProxy(ParcelableInputStream inputStream) {
        this.mParcelableInputStream = inputStream;
    }

    public InputStreamProxy(InputStream inputStream) {
        this.mBufferedInputStream = new BufferedInputStream(inputStream, 8192);
    }

    public void close() throws Exception {
        if (this.mParcelableInputStream != null) {
            ProxyCacheUtils.close(this.mParcelableInputStream);
        }
        if (this.mBufferedInputStream != null) {
            ProxyCacheUtils.close((Closeable) this.mBufferedInputStream);
        }
    }

    public int read(byte[] buffer) throws Exception {
        if (this.mParcelableInputStream != null) {
            return this.mParcelableInputStream.read(buffer);
        }
        if (this.mBufferedInputStream != null) {
            return this.mBufferedInputStream.read(buffer);
        }
        return -1;
    }
}
