package com.nostra13.universalimageloader.cache.disc;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
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

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v10, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: java.io.File} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v11, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v8, resolved type: java.io.File} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v12, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v10, resolved type: java.lang.Long} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int removeNext() {
        /*
            r16 = this;
            r0 = r16
            java.util.Map<java.io.File, java.lang.Long> r8 = r0.lastUsageDates
            boolean r8 = r8.isEmpty()
            if (r8 == 0) goto L_0x000c
            r4 = -1
        L_0x000b:
            return r4
        L_0x000c:
            r7 = 0
            r6 = 0
            r0 = r16
            java.util.Map<java.io.File, java.lang.Long> r8 = r0.lastUsageDates
            java.util.Set r2 = r8.entrySet()
            r0 = r16
            java.util.Map<java.io.File, java.lang.Long> r9 = r0.lastUsageDates
            monitor-enter(r9)
            java.util.Iterator r10 = r2.iterator()     // Catch:{ all -> 0x0078 }
        L_0x001f:
            boolean r8 = r10.hasNext()     // Catch:{ all -> 0x0078 }
            if (r8 == 0) goto L_0x005a
            java.lang.Object r3 = r10.next()     // Catch:{ all -> 0x0078 }
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3     // Catch:{ all -> 0x0078 }
            if (r6 != 0) goto L_0x003e
            java.lang.Object r8 = r3.getKey()     // Catch:{ all -> 0x0078 }
            r0 = r8
            java.io.File r0 = (java.io.File) r0     // Catch:{ all -> 0x0078 }
            r6 = r0
            java.lang.Object r8 = r3.getValue()     // Catch:{ all -> 0x0078 }
            r0 = r8
            java.lang.Long r0 = (java.lang.Long) r0     // Catch:{ all -> 0x0078 }
            r7 = r0
            goto L_0x001f
        L_0x003e:
            java.lang.Object r5 = r3.getValue()     // Catch:{ all -> 0x0078 }
            java.lang.Long r5 = (java.lang.Long) r5     // Catch:{ all -> 0x0078 }
            long r12 = r5.longValue()     // Catch:{ all -> 0x0078 }
            long r14 = r7.longValue()     // Catch:{ all -> 0x0078 }
            int r8 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
            if (r8 >= 0) goto L_0x001f
            r7 = r5
            java.lang.Object r8 = r3.getKey()     // Catch:{ all -> 0x0078 }
            r0 = r8
            java.io.File r0 = (java.io.File) r0     // Catch:{ all -> 0x0078 }
            r6 = r0
            goto L_0x001f
        L_0x005a:
            monitor-exit(r9)     // Catch:{ all -> 0x0078 }
            r4 = 0
            if (r6 == 0) goto L_0x000b
            boolean r8 = r6.exists()
            if (r8 == 0) goto L_0x007b
            r0 = r16
            int r4 = r0.getSize(r6)
            boolean r8 = r6.delete()
            if (r8 == 0) goto L_0x000b
            r0 = r16
            java.util.Map<java.io.File, java.lang.Long> r8 = r0.lastUsageDates
            r8.remove(r6)
            goto L_0x000b
        L_0x0078:
            r8 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x0078 }
            throw r8
        L_0x007b:
            r0 = r16
            java.util.Map<java.io.File, java.lang.Long> r8 = r0.lastUsageDates
            r8.remove(r6)
            goto L_0x000b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.cache.disc.LimitedDiscCache.removeNext():int");
    }
}
