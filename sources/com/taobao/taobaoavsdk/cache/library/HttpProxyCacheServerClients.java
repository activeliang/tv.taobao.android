package com.taobao.taobaoavsdk.cache.library;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.statistic.CT;
import com.taobao.statistic.TBS;
import com.taobao.taobaoavsdk.cache.library.file.FileCache;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

final class HttpProxyCacheServerClients implements FlowListener, IMimeCache {
    private String cdnIp;
    private final AtomicInteger clientsCount = new AtomicInteger(0);
    private final Config config;
    private final List<CacheListener> listeners = new CopyOnWriteArrayList();
    private long mReadBytes;
    private long mReadBytesFromCache;
    private String playToken;
    private volatile HttpProxyCache proxyCache;
    private final HttpProxyCacheServer proxyCacheServer;
    public HttpUrlSource source;
    private final CacheListener uiCacheListener;
    private final String url;
    private Map<String, UrlMime> urlMimeMap = new ConcurrentHashMap(6);
    private boolean useNewNet;
    private String userAgent;
    private int videoLength;

    public HttpProxyCacheServerClients(String url2, Config config2, HttpProxyCacheServer proxyCacheServer2) {
        this.url = (String) Preconditions.checkNotNull(url2);
        this.config = (Config) Preconditions.checkNotNull(config2);
        this.uiCacheListener = new UiListenerHandler(url2, this.listeners);
        this.proxyCacheServer = proxyCacheServer2;
    }

    public void processRequest(GetRequest request, Socket socket) throws ProxyCacheException, IOException {
        if (request != null) {
            this.userAgent = request.userAgent;
            this.useNewNet = request.useTBNet;
            this.playToken = request.playToken;
            this.cdnIp = request.cdnIp;
            this.videoLength = request.length;
        }
        startProcessRequest();
        try {
            this.clientsCount.incrementAndGet();
            this.proxyCache.processRequest(request, socket);
        } finally {
            finishProcessRequest();
        }
    }

    private void commitTBNetData() {
        if (this.proxyCache != null && this.proxyCache.source != null) {
            try {
                String data = this.proxyCache.source.getStatisticData();
                if (!TextUtils.isEmpty(data)) {
                    TBS.Adv.ctrlClicked("Page_Video", CT.Button, "TBNetStatistic", data.split(","));
                    Log.d("TBNetStatistic", data);
                    try {
                        TBS.Adv.ctrlClicked("Page_VideoCache", CT.Button, "PlayerCache", "play_token=" + this.playToken, "read_from_download=" + (this.mReadBytes - this.mReadBytesFromCache), "read_from_cache=" + this.mReadBytesFromCache);
                    } catch (Throwable th) {
                    }
                }
            } catch (Exception e) {
                Log.e("TBNetStatistic", "commitTBNetData error:" + e.getMessage());
            }
        }
    }

    private synchronized void startProcessRequest() throws IOException {
        this.proxyCache = this.proxyCache == null ? newHttpProxyCache() : this.proxyCache;
    }

    private synchronized void finishProcessRequest() {
        if (this.clientsCount.decrementAndGet() <= 0 && this.proxyCache != null) {
            commitTBNetData();
            this.proxyCache.registerCacheListener((CacheListener) null);
            this.proxyCache.registerFlowListener((FlowListener) null);
            this.proxyCache.shutdown();
            this.proxyCache = null;
        }
    }

    public void registerCacheListener(CacheListener cacheListener) {
        this.listeners.add(cacheListener);
    }

    public void unregisterCacheListener(CacheListener cacheListener) {
        this.listeners.remove(cacheListener);
    }

    public synchronized void shutdown() {
        this.listeners.clear();
        if (this.proxyCache != null) {
            this.proxyCache.registerCacheListener((CacheListener) null);
            this.proxyCache.registerFlowListener((FlowListener) null);
            this.proxyCache.shutdown();
        }
        if (this.urlMimeMap != null) {
            this.urlMimeMap.clear();
        }
        this.clientsCount.set(0);
    }

    public int getClientsCount() {
        return this.clientsCount.get();
    }

    private HttpProxyCache newHttpProxyCache() throws IOException {
        this.source = new HttpUrlSource(this, this.url, this.userAgent, this.useNewNet, this.playToken, this.cdnIp, this.videoLength);
        HttpProxyCache httpProxyCache = new HttpProxyCache(this.source, new FileCache(this.config.generateCacheFile(this.url), this.config.diskUsage), this.proxyCacheServer);
        httpProxyCache.registerCacheListener(this.uiCacheListener);
        httpProxyCache.registerFlowListener(this);
        return httpProxyCache;
    }

    public void onReadingData(int readBytes, int readBytesFromCache) {
        this.mReadBytes += (long) readBytes;
        this.mReadBytesFromCache += (long) readBytesFromCache;
    }

    public void putMime(String url2, int length, String mime) {
        if (!TextUtils.isEmpty(url2) && this.urlMimeMap != null && this.config != null && this.config.fileNameGenerator != null) {
            String key = this.config.fileNameGenerator.generate(url2);
            if (!TextUtils.isEmpty(key)) {
                UrlMime urlMime = new UrlMime();
                urlMime.setLength(length);
                urlMime.setMime(mime);
                this.urlMimeMap.put(key, urlMime);
            }
        }
    }

    public UrlMime getMime(String url2) {
        if (TextUtils.isEmpty(url2) || this.urlMimeMap == null || this.urlMimeMap.isEmpty() || this.config == null || this.config.fileNameGenerator == null) {
            return null;
        }
        String key = this.config.fileNameGenerator.generate(url2);
        if (!TextUtils.isEmpty(key)) {
            return this.urlMimeMap.get(key);
        }
        return null;
    }

    private static final class UiListenerHandler extends Handler implements CacheListener {
        private final List<CacheListener> listeners;
        private final String url;

        public UiListenerHandler(String url2, List<CacheListener> listeners2) {
            super(Looper.getMainLooper());
            this.url = url2;
            this.listeners = listeners2;
        }

        public void onCacheAvailable(File file, String url2, int percentsAvailable) {
            Message message = obtainMessage();
            message.arg1 = percentsAvailable;
            message.obj = file;
            sendMessage(message);
        }

        public void handleMessage(Message msg) {
            for (CacheListener cacheListener : this.listeners) {
                cacheListener.onCacheAvailable((File) msg.obj, this.url, msg.arg1);
            }
        }
    }
}
