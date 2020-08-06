package com.taobao.taobaoavsdk.cache.library;

import android.content.Context;
import android.util.Log;
import com.taobao.taobaoavsdk.cache.library.file.DiskUsage;
import com.taobao.taobaoavsdk.cache.library.file.FileCache;
import com.taobao.taobaoavsdk.cache.library.file.FileNameGenerator;
import com.taobao.taobaoavsdk.cache.library.file.Md5FileNameGenerator;
import com.taobao.taobaoavsdk.cache.library.file.TotalSizeCountLruDiskUsage;
import com.taobao.taobaoavsdk.util.DWLogUtils;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HttpProxyCacheServer {
    private static final String PING_REQUEST = "ping";
    private static final String PING_RESPONSE = "ping ok";
    public static final String PROXY_HOST = "127.0.0.1";
    private final Object clientsLock;
    private final Map<String, HttpProxyCacheServerClients> clientsMap;
    private final Config config;
    private ArrayList<INetworkSpeedListener> mNetworkListeners;
    private ArrayList<INetworkStatisticsListener> mNetworkStatisticsListeners;
    private boolean pinged;
    private final int port;
    private final ServerSocket serverSocket;
    private final ExecutorService socketProcessor;
    private final Thread waitConnectionThread;

    public interface INetworkSpeedListener {
        void onDownloading(int i, long j);
    }

    public interface INetworkStatisticsListener {
        void onNetowrkDownloadStatistics(long j);
    }

    public void registerNetworkSpeedListener(INetworkSpeedListener listener) {
        if (listener != null) {
            if (this.mNetworkListeners == null) {
                this.mNetworkListeners = new ArrayList<>();
            }
            if (!this.mNetworkListeners.contains(listener)) {
                this.mNetworkListeners.add(listener);
            }
        }
    }

    public void unregisterNetworkSpeedListener(INetworkSpeedListener listener) {
        if (listener != null && this.mNetworkListeners != null && this.mNetworkListeners.contains(listener)) {
            this.mNetworkListeners.remove(listener);
        }
    }

    public void notifyDownloading(int downloadedSize, long time) {
        if (this.mNetworkListeners != null) {
            Iterator<INetworkSpeedListener> it = this.mNetworkListeners.iterator();
            while (it.hasNext()) {
                it.next().onDownloading(downloadedSize, time);
            }
        }
    }

    public void registerNetworkStatisticsListener(INetworkStatisticsListener listener) {
        if (listener != null) {
            if (this.mNetworkStatisticsListeners == null) {
                this.mNetworkStatisticsListeners = new ArrayList<>();
            }
            if (!this.mNetworkStatisticsListeners.contains(listener)) {
                this.mNetworkStatisticsListeners.add(listener);
            }
        }
    }

    public void unregisterNetworkStatisticsListener(INetworkStatisticsListener listener) {
        if (listener != null && this.mNetworkStatisticsListeners != null && this.mNetworkStatisticsListeners.contains(listener)) {
            this.mNetworkStatisticsListeners.remove(listener);
        }
    }

    public void notifyNetworkStatistics(long downloadedSize) {
        if (this.mNetworkStatisticsListeners != null) {
            Iterator<INetworkStatisticsListener> it = this.mNetworkStatisticsListeners.iterator();
            while (it.hasNext()) {
                it.next().onNetowrkDownloadStatistics(downloadedSize);
            }
        }
    }

    public HttpProxyCacheServer(Context context) {
        this(new Builder(context).buildConfig());
    }

    private HttpProxyCacheServer(Config config2) {
        this.clientsLock = new Object();
        this.socketProcessor = Executors.newFixedThreadPool(8);
        this.clientsMap = new ConcurrentHashMap();
        this.config = (Config) Preconditions.checkNotNull(config2);
        try {
            this.serverSocket = new ServerSocket(0, 8, InetAddress.getByName(PROXY_HOST));
            this.port = this.serverSocket.getLocalPort();
            CountDownLatch startSignal = new CountDownLatch(1);
            this.waitConnectionThread = new Thread(new WaitRequestsRunnable(startSignal));
            this.waitConnectionThread.start();
            startSignal.await();
            this.socketProcessor.submit(new Runnable() {
                public void run() {
                    HttpProxyCacheServer.this.makeSureServerWorks();
                }
            });
        } catch (Throwable e) {
            this.socketProcessor.shutdown();
            throw new IllegalStateException("Error starting local proxy server" + e.getMessage());
        }
    }

    /* access modifiers changed from: private */
    public void makeSureServerWorks() {
        int delay = 70;
        int pingAttempts = 0;
        while (pingAttempts < 3) {
            try {
                this.pinged = this.socketProcessor.submit(new PingCallable()).get((long) delay, TimeUnit.MILLISECONDS).booleanValue();
                if (!this.pinged) {
                    pingAttempts++;
                    delay *= 2;
                } else {
                    return;
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
            }
        }
        shutdown();
    }

    /* access modifiers changed from: private */
    public boolean pingServer() throws ProxyCacheException {
        boolean pingOk = false;
        HttpUrlSource source = new HttpUrlSource(appendToProxyUrl(PING_REQUEST));
        try {
            byte[] expectedResponse = PING_RESPONSE.getBytes();
            source.open(0, true);
            byte[] response = new byte[expectedResponse.length];
            source.read(response);
            pingOk = Arrays.equals(expectedResponse, response);
        } catch (ProxyCacheException e) {
        } finally {
            source.close();
        }
        return pingOk;
    }

    public boolean isCacheAvaiable() {
        return this.pinged;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void shutDownServerClient(java.lang.String r5) {
        /*
            r4 = this;
            java.lang.Object r3 = r4.clientsLock
            monitor-enter(r3)
            com.taobao.taobaoavsdk.cache.library.Config r2 = r4.config     // Catch:{ all -> 0x002d }
            com.taobao.taobaoavsdk.cache.library.file.FileNameGenerator r2 = r2.fileNameGenerator     // Catch:{ all -> 0x002d }
            java.lang.String r1 = r2.generate(r5)     // Catch:{ all -> 0x002d }
            java.util.Map<java.lang.String, com.taobao.taobaoavsdk.cache.library.HttpProxyCacheServerClients> r2 = r4.clientsMap     // Catch:{ all -> 0x002d }
            if (r2 == 0) goto L_0x002b
            java.util.Map<java.lang.String, com.taobao.taobaoavsdk.cache.library.HttpProxyCacheServerClients> r2 = r4.clientsMap     // Catch:{ all -> 0x002d }
            boolean r2 = r2.containsKey(r1)     // Catch:{ all -> 0x002d }
            if (r2 == 0) goto L_0x002b
            java.util.Map<java.lang.String, com.taobao.taobaoavsdk.cache.library.HttpProxyCacheServerClients> r2 = r4.clientsMap     // Catch:{ all -> 0x002d }
            java.lang.Object r0 = r2.get(r1)     // Catch:{ all -> 0x002d }
            com.taobao.taobaoavsdk.cache.library.HttpProxyCacheServerClients r0 = (com.taobao.taobaoavsdk.cache.library.HttpProxyCacheServerClients) r0     // Catch:{ all -> 0x002d }
            java.util.Map<java.lang.String, com.taobao.taobaoavsdk.cache.library.HttpProxyCacheServerClients> r2 = r4.clientsMap     // Catch:{ all -> 0x002d }
            r2.remove(r1)     // Catch:{ all -> 0x002d }
            if (r0 != 0) goto L_0x0028
            monitor-exit(r3)     // Catch:{ all -> 0x002d }
        L_0x0027:
            return
        L_0x0028:
            r0.shutdown()     // Catch:{ all -> 0x002d }
        L_0x002b:
            monitor-exit(r3)     // Catch:{ all -> 0x002d }
            goto L_0x0027
        L_0x002d:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x002d }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.taobaoavsdk.cache.library.HttpProxyCacheServer.shutDownServerClient(java.lang.String):void");
    }

    public String getProxyUrl(String url) {
        if (this.pinged && this.config != null && this.config.cacheRoot != null && this.config.cacheRoot.exists() && this.config.cacheRoot.canWrite()) {
            return appendToProxyUrl(url);
        }
        this.pinged = false;
        return url;
    }

    private String appendToProxyUrl(String url) {
        return String.format("http://%s:%d/%s", new Object[]{PROXY_HOST, Integer.valueOf(this.port), ProxyCacheUtils.encode(url)});
    }

    public void registerCacheListener(CacheListener cacheListener, String url) {
        Preconditions.checkAllNotNull(cacheListener, url);
        synchronized (this.clientsLock) {
            try {
                getClients(url).registerCacheListener(cacheListener);
            } catch (ProxyCacheException e) {
            }
        }
    }

    public void unregisterCacheListener(CacheListener cacheListener, String url) {
        Preconditions.checkAllNotNull(cacheListener, url);
        synchronized (this.clientsLock) {
            try {
                getClients(url).unregisterCacheListener(cacheListener);
            } catch (ProxyCacheException e) {
                Log.d("ProxyCache", "Error registering cache listener", e);
            }
        }
    }

    public void unregisterCacheListener(CacheListener cacheListener) {
        Preconditions.checkNotNull(cacheListener);
        synchronized (this.clientsLock) {
            for (HttpProxyCacheServerClients clients : this.clientsMap.values()) {
                clients.unregisterCacheListener(cacheListener);
            }
        }
    }

    public boolean isHitCache(String url) {
        File compeleteFile = this.config.generateCacheFile(url);
        File tmpFile = new File(compeleteFile.getParentFile(), compeleteFile.getName() + FileCache.TEMP_POSTFIX);
        return (tmpFile.exists() && tmpFile.length() > 0) || compeleteFile.exists();
    }

    public boolean isCompleterHitCache(String url) {
        return this.config.generateCacheFile(url).exists();
    }

    public void shutdown() {
        shutdownClients();
        this.waitConnectionThread.interrupt();
        try {
            if (!this.serverSocket.isClosed()) {
                this.serverSocket.close();
            }
        } catch (IOException e) {
        }
    }

    private void shutdownClients() {
        synchronized (this.clientsLock) {
            for (HttpProxyCacheServerClients clients : this.clientsMap.values()) {
                clients.shutdown();
            }
            this.clientsMap.clear();
        }
    }

    /* access modifiers changed from: private */
    public void waitForRequest() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket socket = this.serverSocket.accept();
                socket.setSendBufferSize(262144);
                socket.setTrafficClass(20);
                this.socketProcessor.submit(new SocketProcessorRunnable(socket));
            } catch (IOException e) {
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void processSocket(Socket socket) {
        try {
            GetRequest request = GetRequest.read(socket.getInputStream());
            String url = request.uri;
            if (PING_REQUEST.equals(url)) {
                responseToPing(socket);
            } else {
                getClients(url).processRequest(request, socket);
            }
        } catch (SocketException e) {
        } catch (ProxyCacheException e2) {
        } catch (IOException e3) {
            this.pinged = false;
        } catch (Exception e4) {
            DWLogUtils.e(DWLogUtils.getStackTrace(e4));
        } finally {
            releaseSocket(socket);
        }
    }

    private void responseToPing(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write("HTTP/1.1 200 OK\n\n".getBytes());
        out.write(PING_RESPONSE.getBytes());
    }

    private HttpProxyCacheServerClients getClients(String url) throws ProxyCacheException {
        HttpProxyCacheServerClients clients;
        synchronized (this.clientsLock) {
            String urlkey = this.config.fileNameGenerator.generate(url);
            clients = this.clientsMap.get(urlkey);
            if (clients == null) {
                clients = new HttpProxyCacheServerClients(url, this.config, this);
                this.clientsMap.put(urlkey, clients);
            }
        }
        return clients;
    }

    private int getClientsCount() {
        int count;
        synchronized (this.clientsLock) {
            count = 0;
            for (HttpProxyCacheServerClients clients : this.clientsMap.values()) {
                count += clients.getClientsCount();
            }
        }
        return count;
    }

    private void releaseSocket(Socket socket) {
        closeSocketInput(socket);
        closeSocketOutput(socket);
        closeSocket(socket);
    }

    private void closeSocketInput(Socket socket) {
        try {
            if (!socket.isInputShutdown()) {
                socket.shutdownInput();
            }
        } catch (IOException | SocketException e) {
        }
    }

    private void closeSocketOutput(Socket socket) {
        try {
            if (socket.isOutputShutdown()) {
                socket.shutdownOutput();
            }
        } catch (IOException e) {
        }
    }

    private void closeSocket(Socket socket) {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
        }
    }

    private final class WaitRequestsRunnable implements Runnable {
        private final CountDownLatch startSignal;

        public WaitRequestsRunnable(CountDownLatch startSignal2) {
            this.startSignal = startSignal2;
        }

        public void run() {
            this.startSignal.countDown();
            HttpProxyCacheServer.this.waitForRequest();
        }
    }

    private final class SocketProcessorRunnable implements Runnable {
        private final Socket socket;

        public SocketProcessorRunnable(Socket socket2) {
            this.socket = socket2;
        }

        public void run() {
            HttpProxyCacheServer.this.processSocket(this.socket);
        }
    }

    private class PingCallable implements Callable<Boolean> {
        private PingCallable() {
        }

        public Boolean call() throws Exception {
            return Boolean.valueOf(HttpProxyCacheServer.this.pingServer());
        }
    }

    public static final class Builder {
        private static final int DEFAULT_MAX_COUNT = 100;
        private static final long DEFAULT_MAX_SIZE = 419430400;
        private File cacheRoot;
        private DiskUsage diskUsage = new TotalSizeCountLruDiskUsage(DEFAULT_MAX_SIZE, 100);
        private FileNameGenerator fileNameGenerator = new Md5FileNameGenerator();

        public Builder(Context context) {
            this.cacheRoot = StorageUtils.getIndividualCacheDirectory(context);
        }

        public Builder cacheDirectory(File file) {
            this.cacheRoot = (File) Preconditions.checkNotNull(file);
            return this;
        }

        public Builder fileNameGenerator(FileNameGenerator fileNameGenerator2) {
            this.fileNameGenerator = (FileNameGenerator) Preconditions.checkNotNull(fileNameGenerator2);
            return this;
        }

        public Builder maxCacheSizeCount(long maxSize, int count) {
            this.diskUsage = new TotalSizeCountLruDiskUsage(maxSize, count);
            return this;
        }

        public HttpProxyCacheServer build() {
            return new HttpProxyCacheServer(buildConfig());
        }

        /* access modifiers changed from: private */
        public Config buildConfig() {
            return new Config(this.cacheRoot, this.fileNameGenerator, this.diskUsage);
        }
    }
}
