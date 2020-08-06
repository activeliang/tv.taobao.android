package com.nostra13.universalimageloader.cache.memory.impl;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.memory.BaseMemoryCache;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class WeakMemoryCache extends BaseMemoryCache<String, Bitmap> {
    /* access modifiers changed from: protected */
    public Reference<Bitmap> createReference(Bitmap value) {
        return new WeakReference(value);
    }
}
