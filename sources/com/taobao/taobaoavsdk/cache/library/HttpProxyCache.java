package com.taobao.taobaoavsdk.cache.library;

import android.text.TextUtils;
import com.taobao.taobaoavsdk.cache.library.file.FileCache;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

class HttpProxyCache extends ProxyCache {
    private static final float NO_CACHE_BARRIER = 0.2f;
    private final Cache cache;
    private FlowListener flowListener;
    private CacheListener listener;
    public final HttpUrlSource source;

    public HttpProxyCache(HttpUrlSource source2, Cache cache2, HttpProxyCacheServer proxyCacheServer) {
        super(source2, cache2, proxyCacheServer);
        this.cache = cache2;
        this.source = source2;
    }

    public void registerCacheListener(CacheListener cacheListener) {
        this.listener = cacheListener;
    }

    public void registerFlowListener(FlowListener flowListener2) {
        this.flowListener = flowListener2;
    }

    public void processRequest(GetRequest request, Socket socket) throws IOException, ProxyCacheException {
        OutputStream out = new BufferedOutputStream(socket.getOutputStream());
        out.write(newResponseHeaders(request).getBytes("UTF-8"));
        long offset = request.rangeOffset;
        if (isUseCache(request)) {
            responseWithCache(out, offset);
        } else {
            responseWithoutCache(out, offset);
        }
    }

    private boolean isUseCache(GetRequest request) throws ProxyCacheException {
        boolean sourceLengthKnown;
        if (this.cache != null && this.cache.isCompleted()) {
            return true;
        }
        int sourceLength = this.source.length();
        if (sourceLength == -1) {
            return false;
        }
        if (sourceLength > 0) {
            sourceLengthKnown = true;
        } else {
            sourceLengthKnown = false;
        }
        int cacheAvailable = this.cache.available();
        if (!sourceLengthKnown || !request.partial || ((float) request.rangeOffset) <= ((float) cacheAvailable) + (((float) sourceLength) * NO_CACHE_BARRIER)) {
            return true;
        }
        return false;
    }

    private String newResponseHeaders(GetRequest request) throws IOException, ProxyCacheException {
        boolean mimeKnown;
        boolean lengthKnown;
        boolean addRange;
        String mime = this.source.getMime();
        if (!TextUtils.isEmpty(mime)) {
            mimeKnown = true;
        } else {
            mimeKnown = false;
        }
        int length = this.cache.isCompleted() ? this.cache.available() : this.source.length();
        if (length >= 0) {
            lengthKnown = true;
        } else {
            lengthKnown = false;
        }
        long contentLength = request.partial ? ((long) length) - request.rangeOffset : (long) length;
        if (!lengthKnown || !request.partial) {
            addRange = false;
        } else {
            addRange = true;
        }
        return (request.partial ? "HTTP/1.1 206 PARTIAL CONTENT\n" : "HTTP/1.1 200 OK\n") + "Accept-Ranges: bytes\n" + (lengthKnown ? String.format("Content-Length: %d\n", new Object[]{Long.valueOf(contentLength)}) : "") + (addRange ? String.format("Content-Range: bytes %d-%d/%d\n", new Object[]{Long.valueOf(request.rangeOffset), Integer.valueOf(length - 1), Integer.valueOf(length)}) : "") + (mimeKnown ? String.format("Content-Type: %s\n", new Object[]{mime}) : "") + "\n";
    }

    private void responseWithCache(OutputStream out, long offset) throws ProxyCacheException, IOException {
        byte[] buffer = new byte[8192];
        int available = this.cache.available();
        while (true) {
            int readBytes = read(buffer, offset, buffer.length);
            if (readBytes != -1) {
                int readBytesFromCache = (int) (((long) available) - offset);
                if (readBytesFromCache < 0) {
                    readBytesFromCache = 0;
                } else if (readBytesFromCache > readBytes) {
                    readBytesFromCache = readBytes;
                }
                notifyReadingData(readBytes, readBytesFromCache);
                out.write(buffer, 0, readBytes);
                offset += (long) readBytes;
            } else {
                out.flush();
                return;
            }
        }
    }

    private void responseWithoutCache(OutputStream out, long offset) throws ProxyCacheException, IOException {
        long downloadSize = 0;
        HttpUrlSource newSourceNoCache = new HttpUrlSource(this.source);
        try {
            newSourceNoCache.open((int) offset, true);
            byte[] buffer = new byte[8192];
            while (true) {
                int readBytes = newSourceNoCache.read(buffer);
                if (readBytes != -1) {
                    out.write(buffer, 0, readBytes);
                    offset += (long) readBytes;
                    downloadSize += (long) readBytes;
                    notifyReadingData(readBytes, 0);
                } else {
                    out.flush();
                    return;
                }
            }
        } finally {
            newSourceNoCache.close();
        }
    }

    private void notifyReadingData(int readBytes, int readBytesFromCache) {
        if (this.flowListener != null) {
            this.flowListener.onReadingData(readBytes, readBytesFromCache);
        }
    }

    /* access modifiers changed from: protected */
    public void onCachePercentsAvailableChanged(int percents) {
        if (this.listener != null) {
            this.listener.onCacheAvailable(((FileCache) this.cache).file, this.source.url, percents);
        }
    }
}
