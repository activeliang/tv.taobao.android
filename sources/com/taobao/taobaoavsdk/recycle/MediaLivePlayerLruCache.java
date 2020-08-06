package com.taobao.taobaoavsdk.recycle;

import android.util.LruCache;

class MediaLivePlayerLruCache extends LruCache<String, MediaPlayerRecycler> {
    public MediaLivePlayerLruCache(int maxSize) {
        super(maxSize);
    }

    /* access modifiers changed from: protected */
    public final int sizeOf(String key, MediaPlayerRecycler value) {
        return 1;
    }

    /* access modifiers changed from: protected */
    public final MediaPlayerRecycler create(String key) {
        return MediaLivePlayerManager.getInstance().create(key);
    }

    /* access modifiers changed from: protected */
    public final void entryRemoved(boolean evicted, String key, MediaPlayerRecycler oldValue, MediaPlayerRecycler newValue) {
        MediaLivePlayerManager.getInstance().entryRemoved(evicted, key, oldValue);
    }
}
