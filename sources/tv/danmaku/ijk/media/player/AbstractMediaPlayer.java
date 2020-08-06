package tv.danmaku.ijk.media.player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public abstract class AbstractMediaPlayer implements IMediaPlayer {
    protected IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    protected List<IMediaPlayer.OnBufferingUpdateListener> mOnBufferingUpdateListeners;
    protected IMediaPlayer.OnCompletionListener mOnCompletionListener;
    protected List<IMediaPlayer.OnCompletionListener> mOnCompletionListeners;
    protected IMediaPlayer.OnErrorListener mOnErrorListener;
    protected List<IMediaPlayer.OnErrorListener> mOnErrorListeners;
    protected IMediaPlayer.OnInfoListener mOnInfoListener;
    protected List<IMediaPlayer.OnInfoListener> mOnInfoListeners;
    protected List<IMediaPlayer.OnLoopCompletionListener> mOnLoopCompletionListeners;
    protected IMediaPlayer.OnPreparedListener mOnPreparedListener;
    protected List<IMediaPlayer.OnPreparedListener> mOnPreparedListeners;
    protected IMediaPlayer.OnSeekCompletionListener mOnSeekCompletionListener;
    protected List<IMediaPlayer.OnSeekCompletionListener> mOnSeekCompletionListeners;
    protected IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;
    protected List<IMediaPlayer.OnVideoSizeChangedListener> mOnVideoSizeChangedListeners;

    @Deprecated
    public final void setOnPreparedListener(IMediaPlayer.OnPreparedListener listener) {
        this.mOnPreparedListener = listener;
    }

    public final void registerOnPreparedListener(IMediaPlayer.OnPreparedListener listener) {
        if (this.mOnPreparedListeners == null) {
            this.mOnPreparedListeners = new CopyOnWriteArrayList();
        }
        if (!this.mOnPreparedListeners.contains(listener)) {
            this.mOnPreparedListeners.add(listener);
        }
    }

    public void unregisterOnPreparedListener(IMediaPlayer.OnPreparedListener listener) {
        if (this.mOnPreparedListeners != null) {
            this.mOnPreparedListeners.remove(listener);
        }
    }

    @Deprecated
    public final void setOnCompletionListener(IMediaPlayer.OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }

    public void registerOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        if (this.mOnCompletionListeners == null) {
            this.mOnCompletionListeners = new CopyOnWriteArrayList();
        }
        if (!this.mOnCompletionListeners.contains(l)) {
            this.mOnCompletionListeners.add(l);
        }
    }

    public void unregisterOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        if (this.mOnCompletionListeners != null) {
            this.mOnCompletionListeners.remove(l);
        }
    }

    @Deprecated
    public final void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompletionListener listener) {
        this.mOnSeekCompletionListener = listener;
    }

    public void registerOnSeekCompleteListener(IMediaPlayer.OnSeekCompletionListener l) {
        if (this.mOnSeekCompletionListeners == null) {
            this.mOnSeekCompletionListeners = new CopyOnWriteArrayList();
        }
        if (!this.mOnSeekCompletionListeners.contains(l)) {
            this.mOnSeekCompletionListeners.add(l);
        }
    }

    public void unregisterOnSeekCompleteListener(IMediaPlayer.OnSeekCompletionListener l) {
        if (this.mOnSeekCompletionListeners != null) {
            this.mOnSeekCompletionListeners.remove(l);
        }
    }

    public void registerOnLoopCompletionListener(IMediaPlayer.OnLoopCompletionListener l) {
        if (this.mOnLoopCompletionListeners == null) {
            this.mOnLoopCompletionListeners = new CopyOnWriteArrayList();
        }
        if (!this.mOnLoopCompletionListeners.contains(l)) {
            this.mOnLoopCompletionListeners.add(l);
        }
    }

    public void unregisterOnLoopCompletionListener(IMediaPlayer.OnLoopCompletionListener l) {
        if (this.mOnLoopCompletionListeners != null) {
            this.mOnLoopCompletionListeners.remove(l);
        }
    }

    @Deprecated
    public final void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener listener) {
        this.mOnBufferingUpdateListener = listener;
    }

    public void registerOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener l) {
        if (this.mOnBufferingUpdateListeners == null) {
            this.mOnBufferingUpdateListeners = new CopyOnWriteArrayList();
        }
        if (!this.mOnBufferingUpdateListeners.contains(l)) {
            this.mOnBufferingUpdateListeners.add(l);
        }
    }

    public void unregisterOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener l) {
        if (this.mOnBufferingUpdateListeners != null) {
            this.mOnBufferingUpdateListeners.remove(l);
        }
    }

    @Deprecated
    public final void setOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener listener) {
        this.mOnVideoSizeChangedListener = listener;
    }

    public void registerOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener l) {
        if (this.mOnVideoSizeChangedListeners == null) {
            this.mOnVideoSizeChangedListeners = new CopyOnWriteArrayList();
        }
        if (!this.mOnVideoSizeChangedListeners.contains(l)) {
            this.mOnVideoSizeChangedListeners.add(l);
        }
    }

    public void unregisterOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener l) {
        if (this.mOnVideoSizeChangedListeners != null) {
            this.mOnVideoSizeChangedListeners.remove(l);
        }
    }

    @Deprecated
    public final void setOnErrorListener(IMediaPlayer.OnErrorListener listener) {
        this.mOnErrorListener = listener;
    }

    public void registerOnErrorListener(IMediaPlayer.OnErrorListener l) {
        if (this.mOnErrorListeners == null) {
            this.mOnErrorListeners = new CopyOnWriteArrayList();
        }
        if (!this.mOnErrorListeners.contains(l)) {
            this.mOnErrorListeners.add(l);
        }
    }

    public void unregisterOnErrorListener(IMediaPlayer.OnErrorListener l) {
        if (this.mOnErrorListeners != null) {
            this.mOnErrorListeners.remove(l);
        }
    }

    @Deprecated
    public final void setOnInfoListener(IMediaPlayer.OnInfoListener listener) {
        this.mOnInfoListener = listener;
    }

    public void registerOnInfoListener(IMediaPlayer.OnInfoListener l) {
        if (this.mOnInfoListeners == null) {
            this.mOnInfoListeners = new CopyOnWriteArrayList();
        }
        if (!this.mOnInfoListeners.contains(l)) {
            this.mOnInfoListeners.add(l);
        }
    }

    public void unregisterOnInfoListener(IMediaPlayer.OnInfoListener l) {
        if (this.mOnInfoListeners != null) {
            this.mOnInfoListeners.remove(l);
        }
    }

    public void resetListeners() {
        this.mOnPreparedListener = null;
        this.mOnBufferingUpdateListener = null;
        this.mOnCompletionListener = null;
        this.mOnSeekCompletionListener = null;
        this.mOnVideoSizeChangedListener = null;
        this.mOnErrorListener = null;
        this.mOnInfoListener = null;
        if (this.mOnPreparedListeners != null) {
            this.mOnPreparedListeners.clear();
        }
        if (this.mOnBufferingUpdateListeners != null) {
            this.mOnBufferingUpdateListeners.clear();
        }
        if (this.mOnCompletionListeners != null) {
            this.mOnCompletionListeners.clear();
        }
        if (this.mOnSeekCompletionListeners != null) {
            this.mOnSeekCompletionListeners.clear();
        }
        if (this.mOnLoopCompletionListeners != null) {
            this.mOnLoopCompletionListeners.clear();
        }
        if (this.mOnVideoSizeChangedListeners != null) {
            this.mOnVideoSizeChangedListeners.clear();
        }
        if (this.mOnErrorListeners != null) {
            this.mOnErrorListeners.clear();
        }
        if (this.mOnInfoListeners != null) {
            this.mOnInfoListeners.clear();
        }
    }
}
