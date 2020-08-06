package com.nostra13.universalimageloader.cache.memory.impl;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.memory.LimitedMemoryCache;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LargestLimitedMemoryCache extends LimitedMemoryCache<String, Bitmap> {
    private final Map<Bitmap, Integer> valueSizes = Collections.synchronizedMap(new HashMap());

    public LargestLimitedMemoryCache(int sizeLimit) {
        super(sizeLimit);
    }

    public boolean put(String key, Bitmap value) {
        if (!super.put(key, value)) {
            return false;
        }
        this.valueSizes.put(value, Integer.valueOf(getSize(value)));
        return true;
    }

    public void remove(String key) {
        Bitmap value = (Bitmap) super.get(key);
        if (value != null) {
            this.valueSizes.remove(value);
        }
        super.remove(key);
    }

    public void clear() {
        this.valueSizes.clear();
        super.clear();
    }

    /* access modifiers changed from: protected */
    public int getSize(Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: android.graphics.Bitmap} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v6, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: android.graphics.Bitmap} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v7, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: java.lang.Integer} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Bitmap removeNext() {
        /*
            r10 = this;
            r4 = 0
            r3 = 0
            java.util.Map<android.graphics.Bitmap, java.lang.Integer> r6 = r10.valueSizes
            java.util.Set r1 = r6.entrySet()
            java.util.Map<android.graphics.Bitmap, java.lang.Integer> r7 = r10.valueSizes
            monitor-enter(r7)
            java.util.Iterator r8 = r1.iterator()     // Catch:{ all -> 0x004f }
        L_0x000f:
            boolean r6 = r8.hasNext()     // Catch:{ all -> 0x004f }
            if (r6 == 0) goto L_0x0048
            java.lang.Object r2 = r8.next()     // Catch:{ all -> 0x004f }
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2     // Catch:{ all -> 0x004f }
            if (r3 != 0) goto L_0x002e
            java.lang.Object r6 = r2.getKey()     // Catch:{ all -> 0x004f }
            r0 = r6
            android.graphics.Bitmap r0 = (android.graphics.Bitmap) r0     // Catch:{ all -> 0x004f }
            r3 = r0
            java.lang.Object r6 = r2.getValue()     // Catch:{ all -> 0x004f }
            r0 = r6
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ all -> 0x004f }
            r4 = r0
            goto L_0x000f
        L_0x002e:
            java.lang.Object r5 = r2.getValue()     // Catch:{ all -> 0x004f }
            java.lang.Integer r5 = (java.lang.Integer) r5     // Catch:{ all -> 0x004f }
            int r6 = r5.intValue()     // Catch:{ all -> 0x004f }
            int r9 = r4.intValue()     // Catch:{ all -> 0x004f }
            if (r6 <= r9) goto L_0x000f
            r4 = r5
            java.lang.Object r6 = r2.getKey()     // Catch:{ all -> 0x004f }
            r0 = r6
            android.graphics.Bitmap r0 = (android.graphics.Bitmap) r0     // Catch:{ all -> 0x004f }
            r3 = r0
            goto L_0x000f
        L_0x0048:
            monitor-exit(r7)     // Catch:{ all -> 0x004f }
            java.util.Map<android.graphics.Bitmap, java.lang.Integer> r6 = r10.valueSizes
            r6.remove(r3)
            return r3
        L_0x004f:
            r6 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x004f }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.cache.memory.impl.LargestLimitedMemoryCache.removeNext():android.graphics.Bitmap");
    }

    /* access modifiers changed from: protected */
    public Reference<Bitmap> createReference(Bitmap value) {
        return new WeakReference(value);
    }
}
