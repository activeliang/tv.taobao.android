package com.taobao.taobaoavsdk.recycle;

import java.util.LinkedList;
import java.util.List;
import tv.danmaku.ijk.media.player.AbstractMediaPlayer;

public class MediaPlayerRecycler {
    public boolean mLastPausedState;
    public int mLastPosition;
    public int mLastState;
    public AbstractMediaPlayer mMediaPlayer;
    public AbstractMediaPlayer mNextMediaPlayer;
    public int mPlayState;
    public List<OnRecycleListener> mRecycleListeners;
    public boolean mRecycled;
    public final String mToken;
    public float mVolume;

    public interface OnRecycleListener {
        int getCurrentPosition();

        int getDestoryState();

        AbstractMediaPlayer initPlayer();

        boolean isPlaying();

        void release(boolean z);
    }

    public MediaPlayerRecycler(String token) {
        this.mVolume = -1.0f;
        this.mLastPausedState = true;
        this.mToken = token;
    }

    public MediaPlayerRecycler(String token, OnRecycleListener listener) {
        this.mVolume = -1.0f;
        this.mLastPausedState = true;
        this.mRecycleListeners = new LinkedList();
        this.mRecycleListeners.add(listener);
        this.mToken = token;
    }

    private MediaPlayerRecycler() {
        this.mVolume = -1.0f;
        this.mLastPausedState = true;
        this.mToken = null;
    }
}
