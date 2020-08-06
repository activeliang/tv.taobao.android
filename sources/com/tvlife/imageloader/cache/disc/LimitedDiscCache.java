package com.tvlife.imageloader.cache.disc;

import com.tvlife.imageloader.cache.disc.naming.FileNameGenerator;
import com.tvlife.imageloader.core.DefaultConfigurationFactory;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class LimitedDiscCache extends BaseDiscCache {
    private static final int INVALID_SIZE = -1;
    /* access modifiers changed from: private */
    public final AtomicInteger cacheSize;
    /* access modifiers changed from: private */
    public final Map<File, Long> lastUsageDates;
    private final int sizeLimit;

    /* access modifiers changed from: protected */
    public abstract int getSize(File file);

    public LimitedDiscCache(File cacheDir, int sizeLimit2) {
        this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator(), sizeLimit2);
    }

    public LimitedDiscCache(File cacheDir, FileNameGenerator fileNameGenerator, int sizeLimit2) {
        super(cacheDir, fileNameGenerator);
        this.lastUsageDates = Collections.synchronizedMap(new HashMap());
        this.sizeLimit = sizeLimit2;
        this.cacheSize = new AtomicInteger();
        calculateCacheSizeAndFillUsageMap();
    }

    private void calculateCacheSizeAndFillUsageMap() {
        new Thread(new Runnable() {
            public void run() {
                int size = 0;
                File[] cachedFiles = LimitedDiscCache.this.cacheDir.listFiles();
                if (cachedFiles != null) {
                    for (File cachedFile : cachedFiles) {
                        size += LimitedDiscCache.this.getSize(cachedFile);
                        LimitedDiscCache.this.lastUsageDates.put(cachedFile, Long.valueOf(cachedFile.lastModified()));
                    }
                    LimitedDiscCache.this.cacheSize.set(size);
                }
            }
        }).start();
    }

    public void put(String key, File file) {
        int freedSize;
        int valueSize = getSize(file);
        int curCacheSize = this.cacheSize.get();
        while (curCacheSize + valueSize > this.sizeLimit && (freedSize = removeNext()) != -1) {
            curCacheSize = this.cacheSize.addAndGet(-freedSize);
        }
        this.cacheSize.addAndGet(valueSize);
        Long currentTime = Long.valueOf(System.currentTimeMillis());
        file.setLastModified(currentTime.longValue());
        this.lastUsageDates.put(file, currentTime);
    }

    public File get(String key) {
        File file = super.get(key);
        Long currentTime = Long.valueOf(System.currentTimeMillis());
        file.setLastModified(currentTime.longValue());
        this.lastUsageDates.put(file, currentTime);
        return file;
    }

    public void clear() {
        this.lastUsageDates.clear();
        this.cacheSize.set(0);
        super.clear();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v8, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: java.io.File} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v9, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v8, resolved type: java.io.File} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v10, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v10, resolved type: java.lang.Long} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int removeNext() {
        /*
            r18 = this;
            r0 = r18
            java.util.Map<java.io.File, java.lang.Long> r10 = r0.lastUsageDates
            boolean r10 = r10.isEmpty()
            if (r10 == 0) goto L_0x000c
            r6 = -1
        L_0x000b:
            return r6
        L_0x000c:
            r9 = 0
            r8 = 0
            r0 = r18
            java.util.Map<java.io.File, java.lang.Long> r10 = r0.lastUsageDates
            java.util.Set r3 = r10.entrySet()
            r0 = r18
            java.util.Map<java.io.File, java.lang.Long> r11 = r0.lastUsageDates
            monitor-enter(r11)
            java.util.Iterator r12 = r3.iterator()     // Catch:{ all -> 0x007a }
        L_0x001f:
            boolean r10 = r12.hasNext()     // Catch:{ all -> 0x007a }
            if (r10 == 0) goto L_0x005a
            java.lang.Object r4 = r12.next()     // Catch:{ all -> 0x007a }
            java.util.Map$Entry r4 = (java.util.Map.Entry) r4     // Catch:{ all -> 0x007a }
            if (r8 != 0) goto L_0x003e
            java.lang.Object r10 = r4.getKey()     // Catch:{ all -> 0x007a }
            r0 = r10
            java.io.File r0 = (java.io.File) r0     // Catch:{ all -> 0x007a }
            r8 = r0
            java.lang.Object r10 = r4.getValue()     // Catch:{ all -> 0x007a }
            r0 = r10
            java.lang.Long r0 = (java.lang.Long) r0     // Catch:{ all -> 0x007a }
            r9 = r0
            goto L_0x001f
        L_0x003e:
            java.lang.Object r7 = r4.getValue()     // Catch:{ all -> 0x007a }
            java.lang.Long r7 = (java.lang.Long) r7     // Catch:{ all -> 0x007a }
            long r14 = r7.longValue()     // Catch:{ all -> 0x007a }
            long r16 = r9.longValue()     // Catch:{ all -> 0x007a }
            int r10 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r10 >= 0) goto L_0x001f
            r9 = r7
            java.lang.Object r10 = r4.getKey()     // Catch:{ all -> 0x007a }
            r0 = r10
            java.io.File r0 = (java.io.File) r0     // Catch:{ all -> 0x007a }
            r8 = r0
            goto L_0x001f
        L_0x005a:
            monitor-exit(r11)     // Catch:{ all -> 0x007a }
            r6 = 0
            r2 = 0
            r5 = 0
            if (r8 == 0) goto L_0x000b
            boolean r5 = r8.exists()
            if (r5 == 0) goto L_0x007d
            r0 = r18
            int r6 = r0.getSize(r8)
            boolean r2 = r8.delete()
            if (r2 == 0) goto L_0x000b
            r0 = r18
            java.util.Map<java.io.File, java.lang.Long> r10 = r0.lastUsageDates
            r10.remove(r8)
            goto L_0x000b
        L_0x007a:
            r10 = move-exception
            monitor-exit(r11)     // Catch:{ all -> 0x007a }
            throw r10
        L_0x007d:
            r0 = r18
            java.util.Map<java.io.File, java.lang.Long> r10 = r0.lastUsageDates
            r10.remove(r8)
            goto L_0x000b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tvlife.imageloader.cache.disc.LimitedDiscCache.removeNext():int");
    }
}
