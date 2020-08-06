package com.bumptech.glide.load.engine;

import android.support.annotation.NonNull;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DataFetcherGenerator;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.File;
import java.util.List;

class ResourceCacheGenerator implements DataFetcherGenerator, DataFetcher.DataCallback<Object> {
    private File cacheFile;
    private final DataFetcherGenerator.FetcherReadyCallback cb;
    private ResourceCacheKey currentKey;
    private final DecodeHelper<?> helper;
    private volatile ModelLoader.LoadData<?> loadData;
    private int modelLoaderIndex;
    private List<ModelLoader<File, ?>> modelLoaders;
    private int resourceClassIndex = -1;
    private int sourceIdIndex;
    private Key sourceKey;

    ResourceCacheGenerator(DecodeHelper<?> helper2, DataFetcherGenerator.FetcherReadyCallback cb2) {
        this.helper = helper2;
        this.cb = cb2;
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 134 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean startNext() {
        /*
            r13 = this;
            r12 = 0
            com.bumptech.glide.load.engine.DecodeHelper<?> r0 = r13.helper
            java.util.List r11 = r0.getCacheKeys()
            boolean r0 = r11.isEmpty()
            if (r0 == 0) goto L_0x000e
        L_0x000d:
            return r12
        L_0x000e:
            com.bumptech.glide.load.engine.DecodeHelper<?> r0 = r13.helper
            java.util.List r10 = r0.getRegisteredResourceClasses()
            boolean r0 = r10.isEmpty()
            if (r0 == 0) goto L_0x00b6
            java.lang.Class<java.io.File> r0 = java.io.File.class
            com.bumptech.glide.load.engine.DecodeHelper<?> r1 = r13.helper
            java.lang.Class r1 = r1.getTranscodeClass()
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x000d
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "Failed to find any load path from "
            java.lang.StringBuilder r1 = r1.append(r3)
            com.bumptech.glide.load.engine.DecodeHelper<?> r3 = r13.helper
            java.lang.Class r3 = r3.getModelClass()
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r3 = " to "
            java.lang.StringBuilder r1 = r1.append(r3)
            com.bumptech.glide.load.engine.DecodeHelper<?> r3 = r13.helper
            java.lang.Class r3 = r3.getTranscodeClass()
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x0059:
            r13.resourceClassIndex = r12
        L_0x005b:
            int r0 = r13.sourceIdIndex
            java.lang.Object r2 = r11.get(r0)
            com.bumptech.glide.load.Key r2 = (com.bumptech.glide.load.Key) r2
            int r0 = r13.resourceClassIndex
            java.lang.Object r7 = r10.get(r0)
            java.lang.Class r7 = (java.lang.Class) r7
            com.bumptech.glide.load.engine.DecodeHelper<?> r0 = r13.helper
            com.bumptech.glide.load.Transformation r6 = r0.getTransformation(r7)
            com.bumptech.glide.load.engine.ResourceCacheKey r0 = new com.bumptech.glide.load.engine.ResourceCacheKey
            com.bumptech.glide.load.engine.DecodeHelper<?> r1 = r13.helper
            com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool r1 = r1.getArrayPool()
            com.bumptech.glide.load.engine.DecodeHelper<?> r3 = r13.helper
            com.bumptech.glide.load.Key r3 = r3.getSignature()
            com.bumptech.glide.load.engine.DecodeHelper<?> r4 = r13.helper
            int r4 = r4.getWidth()
            com.bumptech.glide.load.engine.DecodeHelper<?> r5 = r13.helper
            int r5 = r5.getHeight()
            com.bumptech.glide.load.engine.DecodeHelper<?> r8 = r13.helper
            com.bumptech.glide.load.Options r8 = r8.getOptions()
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            r13.currentKey = r0
            com.bumptech.glide.load.engine.DecodeHelper<?> r0 = r13.helper
            com.bumptech.glide.load.engine.cache.DiskCache r0 = r0.getDiskCache()
            com.bumptech.glide.load.engine.ResourceCacheKey r1 = r13.currentKey
            java.io.File r0 = r0.get(r1)
            r13.cacheFile = r0
            java.io.File r0 = r13.cacheFile
            if (r0 == 0) goto L_0x00b6
            r13.sourceKey = r2
            com.bumptech.glide.load.engine.DecodeHelper<?> r0 = r13.helper
            java.io.File r1 = r13.cacheFile
            java.util.List r0 = r0.getModelLoaders(r1)
            r13.modelLoaders = r0
            r13.modelLoaderIndex = r12
        L_0x00b6:
            java.util.List<com.bumptech.glide.load.model.ModelLoader<java.io.File, ?>> r0 = r13.modelLoaders
            if (r0 == 0) goto L_0x00c0
            boolean r0 = r13.hasNextModelLoader()
            if (r0 != 0) goto L_0x00de
        L_0x00c0:
            int r0 = r13.resourceClassIndex
            int r0 = r0 + 1
            r13.resourceClassIndex = r0
            int r0 = r13.resourceClassIndex
            int r1 = r10.size()
            if (r0 < r1) goto L_0x005b
            int r0 = r13.sourceIdIndex
            int r0 = r0 + 1
            r13.sourceIdIndex = r0
            int r0 = r13.sourceIdIndex
            int r1 = r11.size()
            if (r0 < r1) goto L_0x0059
            goto L_0x000d
        L_0x00de:
            r0 = 0
            r13.loadData = r0
            r12 = 0
        L_0x00e2:
            if (r12 != 0) goto L_0x000d
            boolean r0 = r13.hasNextModelLoader()
            if (r0 == 0) goto L_0x000d
            java.util.List<com.bumptech.glide.load.model.ModelLoader<java.io.File, ?>> r0 = r13.modelLoaders
            int r1 = r13.modelLoaderIndex
            int r3 = r1 + 1
            r13.modelLoaderIndex = r3
            java.lang.Object r9 = r0.get(r1)
            com.bumptech.glide.load.model.ModelLoader r9 = (com.bumptech.glide.load.model.ModelLoader) r9
            java.io.File r0 = r13.cacheFile
            com.bumptech.glide.load.engine.DecodeHelper<?> r1 = r13.helper
            int r1 = r1.getWidth()
            com.bumptech.glide.load.engine.DecodeHelper<?> r3 = r13.helper
            int r3 = r3.getHeight()
            com.bumptech.glide.load.engine.DecodeHelper<?> r4 = r13.helper
            com.bumptech.glide.load.Options r4 = r4.getOptions()
            com.bumptech.glide.load.model.ModelLoader$LoadData r0 = r9.buildLoadData(r0, r1, r3, r4)
            r13.loadData = r0
            com.bumptech.glide.load.model.ModelLoader$LoadData<?> r0 = r13.loadData
            if (r0 == 0) goto L_0x00e2
            com.bumptech.glide.load.engine.DecodeHelper<?> r0 = r13.helper
            com.bumptech.glide.load.model.ModelLoader$LoadData<?> r1 = r13.loadData
            com.bumptech.glide.load.data.DataFetcher<Data> r1 = r1.fetcher
            java.lang.Class r1 = r1.getDataClass()
            boolean r0 = r0.hasLoadPath(r1)
            if (r0 == 0) goto L_0x00e2
            r12 = 1
            com.bumptech.glide.load.model.ModelLoader$LoadData<?> r0 = r13.loadData
            com.bumptech.glide.load.data.DataFetcher<Data> r0 = r0.fetcher
            com.bumptech.glide.load.engine.DecodeHelper<?> r1 = r13.helper
            com.bumptech.glide.Priority r1 = r1.getPriority()
            r0.loadData(r1, r13)
            goto L_0x00e2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.engine.ResourceCacheGenerator.startNext():boolean");
    }

    private boolean hasNextModelLoader() {
        return this.modelLoaderIndex < this.modelLoaders.size();
    }

    public void cancel() {
        ModelLoader.LoadData<?> local = this.loadData;
        if (local != null) {
            local.fetcher.cancel();
        }
    }

    public void onDataReady(Object data) {
        this.cb.onDataFetcherReady(this.sourceKey, data, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE, this.currentKey);
    }

    public void onLoadFailed(@NonNull Exception e) {
        this.cb.onDataFetcherFailed(this.currentKey, e, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE);
    }
}
