package com.taobao.taobaoavsdk.recycle;

import android.os.Build;
import android.text.TextUtils;
import com.taobao.media.MediaConstant;
import com.taobao.orange.OrangeConfig;
import com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler;
import com.taobao.taobaoavsdk.util.AndroidUtils;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class MediaPlayerManager {
    private static int MAX_MEDIAPLAYER_NUMS;
    private static MediaPlayerRecycler.OnRecycleListener mRecycleListener;
    private static MediaPlayerRecycler mRecycler;
    private static MediaPlayerLruCache mediaPlayerLruCache;
    private static MediaPlayerManager singleton;

    private MediaPlayerManager() {
        if (Build.VERSION.SDK_INT < 19) {
            MAX_MEDIAPLAYER_NUMS = 2;
            return;
        }
        int maxCoreNums = 4;
        try {
            maxCoreNums = AndroidUtils.parseInt(OrangeConfig.getInstance().getConfig(MediaConstant.DW_ORANGE_GROUP_NAME, "maxPlayerNums", "4"));
        } catch (Throwable th) {
        }
        MAX_MEDIAPLAYER_NUMS = (maxCoreNums > 4 || maxCoreNums < 0) ? 4 : maxCoreNums;
    }

    public static synchronized MediaPlayerManager getInstance() {
        MediaPlayerManager mediaPlayerManager;
        synchronized (MediaPlayerManager.class) {
            if (singleton == null) {
                singleton = new MediaPlayerManager();
                mediaPlayerLruCache = new MediaPlayerLruCache(MAX_MEDIAPLAYER_NUMS);
            }
            mediaPlayerManager = singleton;
        }
        return mediaPlayerManager;
    }

    public static String generateToken() {
        return System.currentTimeMillis() + "_" + new Random().nextInt(1000);
    }

    public void removePlayerFromCache(String token, MediaPlayerRecycler.OnRecycleListener listener) {
        if (!TextUtils.isEmpty(token)) {
            for (String key : mediaPlayerLruCache.snapshot().keySet()) {
                if (token.equals(key)) {
                    MediaPlayerRecycler recycler = (MediaPlayerRecycler) mediaPlayerLruCache.get(key);
                    if (recycler.mRecycleListeners != null) {
                        recycler.mRecycleListeners.remove(listener);
                        if (recycler.mRecycleListeners.size() == 0) {
                            mRecycleListener = listener;
                            mediaPlayerLruCache.remove(token);
                        }
                    }
                }
            }
        }
    }

    public MediaPlayerRecycler getMediaRecyclerAfterRecycled(MediaPlayerRecycler recycler) {
        if (recycler == null || TextUtils.isEmpty(recycler.mToken)) {
            return recycler;
        }
        if (mediaPlayerLruCache == null) {
            mediaPlayerLruCache = new MediaPlayerLruCache(MAX_MEDIAPLAYER_NUMS);
        }
        for (String key : mediaPlayerLruCache.snapshot().keySet()) {
            if (recycler.mToken.equals(key)) {
                return (MediaPlayerRecycler) mediaPlayerLruCache.get(key);
            }
        }
        mRecycler = recycler;
        return (MediaPlayerRecycler) mediaPlayerLruCache.get(recycler.mToken);
    }

    public MediaPlayerRecycler getMediaRecycler(String token, MediaPlayerRecycler.OnRecycleListener listener) {
        if (TextUtils.isEmpty(token) || listener == null) {
            return null;
        }
        if (mediaPlayerLruCache == null) {
            mediaPlayerLruCache = new MediaPlayerLruCache(MAX_MEDIAPLAYER_NUMS);
        }
        for (String key : mediaPlayerLruCache.snapshot().keySet()) {
            if (token.equals(key)) {
                MediaPlayerRecycler recycler = (MediaPlayerRecycler) mediaPlayerLruCache.get(key);
                if (recycler.mRecycleListeners == null) {
                    recycler.mRecycleListeners = new LinkedList();
                }
                if (recycler.mRecycleListeners.contains(listener)) {
                    return recycler;
                }
                recycler.mRecycleListeners.add(0, listener);
                return recycler;
            }
        }
        mRecycleListener = listener;
        return (MediaPlayerRecycler) mediaPlayerLruCache.get(token);
    }

    public void reorderLruMediaPlayer() {
        if (mediaPlayerLruCache != null) {
            Map<String, MediaPlayerRecycler> lruSnapshot = mediaPlayerLruCache.snapshot();
            if (!lruSnapshot.isEmpty()) {
                try {
                    for (MediaPlayerRecycler recycler : lruSnapshot.values()) {
                        if (recycler.mRecycleListeners != null && recycler.mRecycleListeners.size() > 0 && recycler.mRecycleListeners.get(0).isPlaying()) {
                            mediaPlayerLruCache.get(recycler.mToken);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public boolean resumeLruMediaPlayerAvailable() {
        if (mediaPlayerLruCache == null || mediaPlayerLruCache.size() >= MAX_MEDIAPLAYER_NUMS) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public MediaPlayerRecycler create(String token) {
        if (TextUtils.isEmpty(token)) {
            return null;
        }
        if (mRecycler != null) {
            MediaPlayerRecycler recycler = new MediaPlayerRecycler(token);
            recycler.mRecycleListeners = mRecycler.mRecycleListeners;
            recycler.mLastPosition = mRecycler.mLastPosition;
            recycler.mLastState = mRecycler.mLastState;
            recycler.mRecycled = mRecycler.mRecycled;
            recycler.mLastPausedState = mRecycler.mLastPausedState;
            recycler.mVolume = mRecycler.mVolume;
            mRecycler = null;
            return recycler;
        }
        MediaPlayerRecycler mediaPlayerRecycler = new MediaPlayerRecycler(token, mRecycleListener);
        mRecycleListener = null;
        return mediaPlayerRecycler;
    }

    /* access modifiers changed from: package-private */
    public void entryRemoved(boolean evicted, String token, MediaPlayerRecycler recycler) {
        MediaPlayerRecycler.OnRecycleListener listener;
        if (!TextUtils.isEmpty(token) && recycler != null && recycler.mRecycleListeners != null) {
            if (mRecycleListener != null) {
                mRecycleListener.release(true);
                mRecycleListener = null;
            } else if (recycler.mRecycleListeners.size() > 0 && recycler.mMediaPlayer != null && (listener = recycler.mRecycleListeners.get(0)) != null) {
                recycler.mLastPosition = listener.getCurrentPosition();
                recycler.mLastState = recycler.mPlayState;
                recycler.mRecycled = true;
                recycler.mPlayState = listener.getDestoryState();
                listener.release(true);
            }
        }
    }

    public Map<String, MediaPlayerRecycler> getAllPlayer() {
        if (mediaPlayerLruCache == null) {
            mediaPlayerLruCache = new MediaPlayerLruCache(MAX_MEDIAPLAYER_NUMS);
        }
        return mediaPlayerLruCache.snapshot();
    }
}
