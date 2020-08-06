package com.taobao.mediaplay.player;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.AudioManager;
import android.util.SparseArray;
import android.view.View;
import com.taobao.mediaplay.MediaContext;
import com.taobao.mediaplay.MediaPlayScreenType;
import com.taobao.mediaplay.player.IMediaRenderView;
import com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public abstract class BaseVideoView implements IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnBufferingUpdateListener {
    static final String ON_VIDEO_START_ACTION = "com.taobao.avplayer.start";
    public static float VOLUME_MULTIPLIER = 0.8f;
    protected float mAspectRatio;
    protected boolean mBlockTouchEvent = false;
    protected Context mContext;
    protected int mCurrentPosition;
    protected boolean mEnableVideoDetect = false;
    protected Map<String, String> mExtInfo;
    protected IMediaRenderView.ISurfaceHolder mHolder;
    protected MediaContext mMediaContext;
    protected ArrayList<IMediaPlayLifecycleListener> mMediaPlayerLifecycleListeners;
    public MediaPlayerRecycler mMediaPlayerRecycler;
    private IMediaRecycleListener mMediaRecycleListener;
    public List<IMediaPlayer.OnVideoClickListener> mOnVideoClickListeners;
    protected SparseArray<Float> mPropertyFloat;
    protected SparseArray<Long> mPropertyLong;
    protected String mSeiData;
    protected Rect mSurfaceFrame = new Rect();
    protected int mSurfaceHeight;
    protected int mSurfaceWidth;
    protected int mTargetState;
    protected float mTouchX;
    protected float mTouchY;
    protected int mVideoBufferPercent;
    protected int mVideoHeight;
    private List<IMediaLoopCompleteListener> mVideoMediaCompleteListeners;
    protected String mVideoPath;
    protected boolean mVideoPrepared;
    protected boolean mVideoStarted;
    protected int mVideoWidth;

    public abstract void asyncPrepare();

    public abstract void close();

    public abstract void closeVideo();

    public abstract void destroy();

    public abstract float getAspectRatio();

    public abstract int getCurrentPosition();

    public abstract int getDuration();

    public abstract View getView();

    /* access modifiers changed from: protected */
    public abstract void init();

    public abstract void instantSeekTo(int i);

    public abstract boolean isAvailable();

    public abstract boolean isCompleteHitCache();

    public abstract boolean isHitCache();

    public abstract boolean isInPlaybackState();

    public abstract boolean isPlaying();

    public abstract boolean isUseCache();

    public abstract void onVideoScreenChanged(MediaPlayScreenType mediaPlayScreenType);

    public abstract void pauseVideo(boolean z);

    public abstract void playVideo();

    public abstract void seekTo(int i);

    public abstract void seekToWithoutNotify(int i, boolean z);

    public abstract void setAccountId(String str);

    public abstract void setAspectRatio(MediaAspectRatio mediaAspectRatio);

    public abstract void setLooping(boolean z);

    public abstract void setMediaId(String str);

    public abstract void setMediaSourceType(String str);

    public abstract void setPlayRate(float f);

    public abstract void setPropertyFloat(int i, float f);

    public abstract void setPropertyLong(int i, long j);

    public abstract void setSurfaceListener(TaoLiveVideoView.SurfaceListener surfaceListener);

    public abstract void setSysVolume(float f);

    public abstract void setVideoPath(String str);

    public abstract void setVolume(float f);

    public abstract void startVideo();

    public float getSysVolume() {
        try {
            return (float) ((AudioManager) this.mContext.getApplicationContext().getSystemService("audio")).getStreamVolume(3);
        } catch (Exception e) {
            return 0.5f;
        }
    }

    public String getToken() {
        return this.mMediaPlayerRecycler.mToken;
    }

    public BaseVideoView(Context context) {
        this.mContext = context;
        init();
    }

    public String getVideoPath() {
        return this.mVideoPath;
    }

    public void registerIMediaLifecycleListener(IMediaPlayLifecycleListener listener) {
        if (listener != null) {
            if (this.mMediaPlayerLifecycleListeners == null) {
                this.mMediaPlayerLifecycleListeners = new ArrayList<>();
            }
            if (!this.mMediaPlayerLifecycleListeners.contains(listener)) {
                this.mMediaPlayerLifecycleListeners.add(listener);
            }
        }
    }

    public void unregisterIVideoLifecycleListener(IMediaPlayLifecycleListener listener) {
        if (listener != null && this.mMediaPlayerLifecycleListeners != null) {
            this.mMediaPlayerLifecycleListeners.remove(listener);
        }
    }

    public boolean isLastPausedState() {
        return this.mMediaPlayerRecycler.mLastPausedState;
    }

    public int getVideoState() {
        return this.mMediaPlayerRecycler.mPlayState;
    }

    public boolean isRecycled() {
        return this.mMediaPlayerRecycler.mRecycled;
    }

    public int getStatebfRelease() {
        return this.mMediaPlayerRecycler.mLastState;
    }

    public void setStatebfRelease(int mStatebfRelease) {
        this.mMediaPlayerRecycler.mLastState = mStatebfRelease;
    }

    public boolean isInErrorState(int playState) {
        return playState == 3 || playState == 0 || playState == 6;
    }

    public int getSurfaceWidth() {
        return this.mSurfaceWidth;
    }

    public int getSurfaceHeight() {
        return this.mSurfaceHeight;
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    public IMediaRenderView.ISurfaceHolder getHolder() {
        return this.mHolder;
    }

    public int getVideoBufferPercent() {
        return this.mVideoBufferPercent;
    }

    private void onVideoStartBroadcast() {
        Intent i = new Intent(ON_VIDEO_START_ACTION);
        if (this.mContext != null) {
            this.mContext.sendBroadcast(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyVideoStart() {
        if (!this.mMediaContext.mMediaPlayContext.mTBLive) {
            onVideoStartBroadcast();
        }
        this.mMediaPlayerRecycler.mPlayState = 1;
        if (this.mMediaPlayerLifecycleListeners != null) {
            Iterator<IMediaPlayLifecycleListener> it = this.mMediaPlayerLifecycleListeners.iterator();
            while (it.hasNext()) {
                it.next().onMediaStart();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyVideoPlay() {
        this.mMediaPlayerRecycler.mPlayState = 1;
        if (this.mMediaPlayerLifecycleListeners != null) {
            Iterator<IMediaPlayLifecycleListener> it = this.mMediaPlayerLifecycleListeners.iterator();
            while (it.hasNext()) {
                it.next().onMediaPlay();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyVideoPause(boolean auto) {
        this.mMediaPlayerRecycler.mPlayState = 2;
        if (this.mMediaPlayerLifecycleListeners != null) {
            Iterator<IMediaPlayLifecycleListener> it = this.mMediaPlayerLifecycleListeners.iterator();
            while (it.hasNext()) {
                it.next().onMediaPause(auto);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyVideoComplete() {
        this.mMediaPlayerRecycler.mPlayState = 4;
        if (this.mMediaPlayerLifecycleListeners != null) {
            Iterator<IMediaPlayLifecycleListener> it = this.mMediaPlayerLifecycleListeners.iterator();
            while (it.hasNext()) {
                it.next().onMediaComplete();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyVideoClose() {
        if (this.mMediaPlayerLifecycleListeners != null) {
            Iterator<IMediaPlayLifecycleListener> it = this.mMediaPlayerLifecycleListeners.iterator();
            while (it.hasNext()) {
                it.next().onMediaClose();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyVideoError(IMediaPlayer mp, int what, int extra) {
        this.mMediaPlayerRecycler.mPlayState = 3;
        if (this.mMediaPlayerLifecycleListeners != null) {
            Iterator<IMediaPlayLifecycleListener> it = this.mMediaPlayerLifecycleListeners.iterator();
            while (it.hasNext()) {
                it.next().onMediaError(mp, what, extra);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyVideoInfo(IMediaPlayer mp, long what, long extra, long ext, Object object) {
        if (this.mMediaPlayerLifecycleListeners != null) {
            Iterator<IMediaPlayLifecycleListener> it = this.mMediaPlayerLifecycleListeners.iterator();
            while (it.hasNext()) {
                it.next().onMediaInfo(mp, what, extra, ext, object);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyVideoPrepared(IMediaPlayer mp) {
        this.mMediaPlayerRecycler.mPlayState = 5;
        if (this.mMediaPlayerLifecycleListeners != null) {
            Iterator<IMediaPlayLifecycleListener> it = this.mMediaPlayerLifecycleListeners.iterator();
            while (it.hasNext()) {
                it.next().onMediaPrepared(mp);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyVideoSeekTo(int percent) {
        if (this.mMediaPlayerLifecycleListeners != null) {
            Iterator<IMediaPlayLifecycleListener> it = this.mMediaPlayerLifecycleListeners.iterator();
            while (it.hasNext()) {
                it.next().onMediaSeekTo(percent);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyVideoScreenChanged(MediaPlayScreenType type) {
        if (this.mMediaPlayerLifecycleListeners != null) {
            Iterator<IMediaPlayLifecycleListener> it = this.mMediaPlayerLifecycleListeners.iterator();
            while (it.hasNext()) {
                it.next().onMediaScreenChanged(type);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyVideoTimeChanged(int currentPosition, int bufferPercent, int total) {
        if (this.mMediaPlayerLifecycleListeners != null) {
            int count = this.mMediaPlayerLifecycleListeners.size();
            for (int i = 0; i < count; i++) {
                this.mMediaPlayerLifecycleListeners.get(i).onMediaProgressChanged(currentPosition, bufferPercent, total);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setPlayState(int videoState) {
        this.mMediaPlayerRecycler.mPlayState = videoState;
    }

    public void registerVideoRecycleListener(IMediaRecycleListener videoRecycleListener) {
        this.mMediaRecycleListener = videoRecycleListener;
    }

    public void registerIVideoLoopCompleteListener(IMediaLoopCompleteListener videoLoopCompleteListener) {
        if (this.mVideoMediaCompleteListeners == null) {
            this.mVideoMediaCompleteListeners = new ArrayList();
        }
        if (!this.mVideoMediaCompleteListeners.contains(videoLoopCompleteListener)) {
            this.mVideoMediaCompleteListeners.add(videoLoopCompleteListener);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyVideoRecycled() {
        if (this.mMediaRecycleListener != null) {
            this.mMediaRecycleListener.onMediaRecycled();
        }
    }

    public void registerOnVideoClickListener(IMediaPlayer.OnVideoClickListener listener) {
        if (this.mOnVideoClickListeners == null) {
            this.mOnVideoClickListeners = new LinkedList();
        }
        this.mOnVideoClickListeners.add(listener);
    }

    public void unregisterOnVideoClickListener(IMediaPlayer.OnVideoClickListener listener) {
        if (this.mOnVideoClickListeners != null) {
            this.mOnVideoClickListeners.remove(listener);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyOnVideoClick(int x, int y, int w, int h, int index, String seiData) {
        if (this.mOnVideoClickListeners != null) {
            for (IMediaPlayer.OnVideoClickListener listener : this.mOnVideoClickListeners) {
                listener.onClick(x, y, w, h, index, seiData);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyVideoLoopComplete() {
        if (this.mVideoMediaCompleteListeners != null) {
            for (IMediaLoopCompleteListener listener : this.mVideoMediaCompleteListeners) {
                listener.onLoopCompletion();
            }
        }
    }

    public void enableVideoClickDetect(boolean enableVideoDetect) {
        this.mEnableVideoDetect = enableVideoDetect;
    }

    public void blockTouchEvent(boolean block) {
        this.mBlockTouchEvent = block;
    }

    public void setLastPosition(int lastPosition) {
        this.mMediaPlayerRecycler.mLastPosition = lastPosition;
    }

    public void setMonitorData(Map<String, String> monitorData) {
        this.mExtInfo = monitorData;
    }

    public void videoPlayError() {
    }
}
