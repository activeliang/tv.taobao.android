package com.taobao.taobaoavsdk.cache.library;

import java.lang.Thread;
import java.util.concurrent.atomic.AtomicInteger;

class ProxyCache {
    private static final int MAX_READ_SOURCE_ATTEMPTS = 1;
    private final Cache cache;
    private volatile int percentsAvailable = -1;
    protected HttpProxyCacheServer proxyCacheServer;
    private final AtomicInteger readSourceErrorsCount;
    private final Source source;
    private volatile Thread sourceReaderThread;
    private final Object stopLock = new Object();
    private volatile boolean stopped;
    private final Object wc = new Object();

    public ProxyCache(Source source2, Cache cache2, HttpProxyCacheServer proxyCacheServer2) {
        this.source = (Source) Preconditions.checkNotNull(source2);
        this.cache = (Cache) Preconditions.checkNotNull(cache2);
        this.proxyCacheServer = proxyCacheServer2;
        this.readSourceErrorsCount = new AtomicInteger();
    }

    public int read(byte[] buffer, long offset, int length) throws ProxyCacheException {
        ProxyCacheUtils.assertBuffer(buffer, offset, length);
        while (!this.cache.isCompleted() && ((long) this.cache.available()) < ((long) length) + offset && !this.stopped) {
            readSourceAsync();
            waitForSourceData();
            checkReadSourceErrorsCount();
        }
        int read = this.cache.read(buffer, offset, length);
        if (this.cache.isCompleted() && this.percentsAvailable != 100) {
            this.percentsAvailable = 100;
            onCachePercentsAvailableChanged(100);
        }
        return read;
    }

    private void checkReadSourceErrorsCount() throws ProxyCacheException {
        int errorsCount = this.readSourceErrorsCount.get();
        if (errorsCount >= 1) {
            this.readSourceErrorsCount.set(0);
            throw new ProxyCacheException("Error reading source " + errorsCount + " times");
        }
    }

    public void shutdown() {
        synchronized (this.stopLock) {
            try {
                this.stopped = true;
                if (this.sourceReaderThread != null) {
                    this.sourceReaderThread.interrupt();
                }
                this.cache.close();
            } catch (ProxyCacheException e) {
                onError(e);
            }
        }
    }

    private synchronized void readSourceAsync() throws ProxyCacheException {
        boolean readingInProgress = (this.sourceReaderThread == null || this.sourceReaderThread.getState() == Thread.State.TERMINATED) ? false : true;
        if (!this.stopped && !this.cache.isCompleted() && !readingInProgress) {
            this.sourceReaderThread = new Thread(new SourceReaderRunnable(), "Source reader for " + this.source);
            this.sourceReaderThread.start();
        }
    }

    private void waitForSourceData() throws ProxyCacheException {
        synchronized (this.wc) {
            try {
                this.wc.wait(500);
            } catch (InterruptedException e) {
                throw new ProxyCacheException("Waiting source data is interrupted!", e);
            }
        }
    }

    private void notifyNewCacheDataAvailable(long cacheAvailable, long sourceAvailable) {
        onCacheAvailable(cacheAvailable, sourceAvailable);
        synchronized (this.wc) {
            this.wc.notifyAll();
        }
    }

    /* access modifiers changed from: protected */
    public void onCacheAvailable(long cacheAvailable, long sourceLength) {
        boolean zeroLengthSource;
        boolean percentsChanged;
        boolean sourceLengthKnown = true;
        if (sourceLength == 0) {
            zeroLengthSource = true;
        } else {
            zeroLengthSource = false;
        }
        int percents = zeroLengthSource ? 100 : (int) ((100 * cacheAvailable) / sourceLength);
        if (percents != this.percentsAvailable) {
            percentsChanged = true;
        } else {
            percentsChanged = false;
        }
        if (sourceLength < 0) {
            sourceLengthKnown = false;
        }
        if (sourceLengthKnown && percentsChanged) {
            onCachePercentsAvailableChanged(percents);
        }
        this.percentsAvailable = percents;
    }

    /* access modifiers changed from: protected */
    public void onCachePercentsAvailableChanged(int percentsAvailable2) {
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003a, code lost:
        r2 = r2 + r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readSource() {
        /*
            r10 = this;
            r4 = -1
            r2 = 0
            com.taobao.taobaoavsdk.cache.library.Cache r5 = r10.cache     // Catch:{ Throwable -> 0x0041 }
            int r2 = r5.available()     // Catch:{ Throwable -> 0x0041 }
            com.taobao.taobaoavsdk.cache.library.Source r5 = r10.source     // Catch:{ Throwable -> 0x0041 }
            r6 = 0
            r5.open(r2, r6)     // Catch:{ Throwable -> 0x0041 }
            com.taobao.taobaoavsdk.cache.library.Source r5 = r10.source     // Catch:{ Throwable -> 0x0041 }
            int r4 = r5.length()     // Catch:{ Throwable -> 0x0041 }
            r5 = 8192(0x2000, float:1.14794E-41)
            byte[] r0 = new byte[r5]     // Catch:{ Throwable -> 0x0041 }
        L_0x0018:
            com.taobao.taobaoavsdk.cache.library.Source r5 = r10.source     // Catch:{ Throwable -> 0x0041 }
            int r3 = r5.read(r0)     // Catch:{ Throwable -> 0x0041 }
            r5 = -1
            if (r3 == r5) goto L_0x0060
            java.lang.Object r6 = r10.stopLock     // Catch:{ Throwable -> 0x0041 }
            monitor-enter(r6)     // Catch:{ Throwable -> 0x0041 }
            boolean r5 = r10.isStopped()     // Catch:{ all -> 0x0053 }
            if (r5 == 0) goto L_0x0034
            monitor-exit(r6)     // Catch:{ all -> 0x0053 }
            r10.closeSource()
            long r6 = (long) r2
            long r8 = (long) r4
            r10.notifyNewCacheDataAvailable(r6, r8)
        L_0x0033:
            return
        L_0x0034:
            com.taobao.taobaoavsdk.cache.library.Cache r5 = r10.cache     // Catch:{ all -> 0x0053 }
            r5.append(r0, r3)     // Catch:{ all -> 0x0053 }
            monitor-exit(r6)     // Catch:{ all -> 0x0053 }
            int r2 = r2 + r3
            long r6 = (long) r2
            long r8 = (long) r4
            r10.notifyNewCacheDataAvailable(r6, r8)     // Catch:{ Throwable -> 0x0041 }
            goto L_0x0018
        L_0x0041:
            r1 = move-exception
            java.util.concurrent.atomic.AtomicInteger r5 = r10.readSourceErrorsCount     // Catch:{ all -> 0x0056 }
            r5.incrementAndGet()     // Catch:{ all -> 0x0056 }
            r10.onError(r1)     // Catch:{ all -> 0x0056 }
            r10.closeSource()
            long r6 = (long) r2
            long r8 = (long) r4
            r10.notifyNewCacheDataAvailable(r6, r8)
            goto L_0x0033
        L_0x0053:
            r5 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0053 }
            throw r5     // Catch:{ Throwable -> 0x0041 }
        L_0x0056:
            r5 = move-exception
            r10.closeSource()
            long r6 = (long) r2
            long r8 = (long) r4
            r10.notifyNewCacheDataAvailable(r6, r8)
            throw r5
        L_0x0060:
            r10.tryComplete()     // Catch:{ Throwable -> 0x0041 }
            r10.closeSource()
            long r6 = (long) r2
            long r8 = (long) r4
            r10.notifyNewCacheDataAvailable(r6, r8)
            goto L_0x0033
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.taobaoavsdk.cache.library.ProxyCache.readSource():void");
    }

    private void tryComplete() throws ProxyCacheException {
        synchronized (this.stopLock) {
            if (!isStopped() && this.cache.available() == this.source.length()) {
                this.cache.complete();
            }
        }
    }

    private boolean isStopped() {
        return Thread.currentThread().isInterrupted() || this.stopped;
    }

    private void closeSource() {
        try {
            this.source.close();
        } catch (ProxyCacheException e) {
            onError(new ProxyCacheException("Error closing source " + this.source, e));
        } catch (Exception e2) {
            onError(new Exception("close source unknown exception " + this.source, e2));
        }
    }

    /* access modifiers changed from: protected */
    public final void onError(Throwable e) {
    }

    private class SourceReaderRunnable implements Runnable {
        private SourceReaderRunnable() {
        }

        public void run() {
            ProxyCache.this.readSource();
        }
    }
}
