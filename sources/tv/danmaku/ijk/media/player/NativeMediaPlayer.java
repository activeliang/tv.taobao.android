package tv.danmaku.ijk.media.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.alibaba.sdk.android.oss.common.utils.HttpHeaders;
import com.taobao.adapter.ConfigAdapter;
import com.taobao.taobaoavsdk.util.AndroidUtils;
import java.io.IOException;
import java.util.HashMap;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class NativeMediaPlayer extends MonitorMediaPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnSeekCompleteListener {
    private MediaPlayer mMediaPlayer;
    private boolean mOnError = false;

    @Deprecated
    public interface OnSeekCompleteListener {
        void onSeekComplete(IMediaPlayer iMediaPlayer);
    }

    public NativeMediaPlayer() {
        initPlayer();
    }

    public NativeMediaPlayer(Context context) {
        super(context);
        initPlayer();
    }

    public NativeMediaPlayer(Context context, ConfigAdapter configAdapter) {
        super(context, configAdapter);
        initPlayer();
    }

    private void initPlayer() {
        this.mMediaPlayer = new MediaPlayer();
        this.mMediaPlayer.setOnPreparedListener(this);
        this.mMediaPlayer.setOnCompletionListener(this);
        this.mMediaPlayer.setOnBufferingUpdateListener(this);
        this.mMediaPlayer.setOnVideoSizeChangedListener(this);
        this.mMediaPlayer.setOnErrorListener(this);
        this.mMediaPlayer.setOnInfoListener(this);
        this.mMediaPlayer.setOnSeekCompleteListener(this);
    }

    public void setDisplay(SurfaceHolder sh) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setDisplay(sh);
        }
    }

    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        String url = monitorDataSource(path);
        if (this.mMediaPlayer != null && !TextUtils.isEmpty(url)) {
            if (url.startsWith("http")) {
                Uri uri = Uri.parse(url);
                HashMap<String, String> headers = new HashMap<>();
                String userAgent = AndroidUtils.getUserAgent(this.mContext);
                if (!TextUtils.isEmpty(userAgent)) {
                    headers.put(HttpHeaders.USER_AGENT, userAgent);
                }
                try {
                    if (!TextUtils.isEmpty(this.mCdnIp) && !isUseVideoCache()) {
                        String host = uri.getHost();
                        uri = Uri.parse(url.replaceFirst(host, this.mCdnIp));
                        headers.put("Host", host);
                    }
                } catch (Throwable th) {
                }
                this.mMediaPlayer.setDataSource(this.mContext, uri, headers);
                return;
            }
            this.mMediaPlayer.setDataSource(url);
        }
    }

    public void prepareAsync() throws IllegalStateException {
        if (this.mMediaPlayer != null) {
            this.mOnError = false;
            monitorPrepare();
            this.mMediaPlayer.prepareAsync();
        }
    }

    public void start() throws IllegalStateException {
        if (this.mMediaPlayer != null) {
            monitorStart();
            this.mMediaPlayer.start();
        }
    }

    public void stop() throws IllegalStateException {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
        }
    }

    public void pause() throws IllegalStateException {
        if (this.mMediaPlayer != null) {
            monitorPause();
            this.mMediaPlayer.pause();
        }
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setScreenOnWhilePlaying(screenOn);
        }
    }

    public int getVideoWidth() {
        if (this.mMediaPlayer != null) {
            return this.mMediaPlayer.getVideoWidth();
        }
        return 0;
    }

    public int getVideoHeight() {
        if (this.mMediaPlayer != null) {
            return this.mMediaPlayer.getVideoHeight();
        }
        return 0;
    }

    public int getVideoSarNum() {
        return 0;
    }

    public int getVideoSarDen() {
        return 0;
    }

    public boolean isPlaying() {
        try {
            if (this.mMediaPlayer != null) {
                return this.mMediaPlayer.isPlaying();
            }
        } catch (Exception e) {
        }
        return false;
    }

    public void seekTo(long msec) throws IllegalStateException {
        if (this.mMediaPlayer != null) {
            if (this.mVolume != 0.0f && this.bInstantSeeked) {
                this.mMediaPlayer.setVolume(this.mVolume, this.mVolume);
            }
            this.bInstantSeeked = false;
            monitorSeek();
            this.mMediaPlayer.seekTo((int) msec);
        }
    }

    public void instantSeekTo(long msec) {
        if (this.mMediaPlayer != null) {
            if (this.mVolume != 0.0f && !this.bInstantSeeked) {
                this.mMediaPlayer.setVolume(0.0f, 0.0f);
            }
            this.bInstantSeeked = true;
            monitorSeek();
            this.mMediaPlayer.seekTo((int) msec);
        }
    }

    public long getCurrentPosition() {
        if (this.mMediaPlayer != null) {
            return (long) this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public long getDuration() {
        if (this.mMediaPlayer != null) {
            return (long) this.mMediaPlayer.getDuration();
        }
        return 0;
    }

    public void release() {
        if (this.mMediaPlayer != null) {
            monitorPlayExperience();
            monitorRelease();
            this.mMediaPlayer.release();
        }
    }

    public void reset() {
        if (this.mMediaPlayer != null) {
            monitorPlayExperience();
            monitorReset();
            this.mMediaPlayer.reset();
        }
    }

    public void setVolume(float leftVolume, float rightVolume) {
        if (this.mMediaPlayer != null) {
            this.mVolume = leftVolume;
            this.mMediaPlayer.setVolume(leftVolume, rightVolume);
        }
    }

    public void setPlayRate(float playRate) {
    }

    public void setMuted(boolean muted) {
        if (this.mMediaPlayer != null) {
            float volume = muted ? 0.0f : this.mVolume;
            this.mMediaPlayer.setVolume(volume, volume);
        }
    }

    public void setAudioStreamType(int streamtype) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setAudioStreamType(streamtype);
        }
    }

    public void setSurface(Surface surface) {
        this.mSurface = surface;
        if (this.mMediaPlayer != null) {
            try {
                this.mMediaPlayer.setSurface(surface);
            } catch (Exception e) {
            }
        }
    }

    public void setSurfaceSize(int width, int height) {
    }

    public void onPrepared(MediaPlayer mp) {
        monitorPrepared(0);
        if (this.mOnPreparedListener != null) {
            this.mOnPreparedListener.onPrepared(this);
        }
        if (this.mOnPreparedListeners != null) {
            for (IMediaPlayer.OnPreparedListener listener : this.mOnPreparedListeners) {
                listener.onPrepared(this);
            }
        }
    }

    public void onCompletion(MediaPlayer mp) {
        monitorComplete();
        if (this.bLooping) {
            this.bSeeked = true;
            if (this.mOnLoopCompletionListeners != null) {
                for (IMediaPlayer.OnLoopCompletionListener listener : this.mOnLoopCompletionListeners) {
                    listener.onLoopCompletion(this);
                }
            }
            start();
            return;
        }
        if (this.mOnCompletionListener != null) {
            this.mOnCompletionListener.onCompletion(this);
        }
        if (this.mOnCompletionListeners != null) {
            for (IMediaPlayer.OnCompletionListener listener2 : this.mOnCompletionListeners) {
                listener2.onCompletion(this);
            }
        }
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (this.mOnBufferingUpdateListener != null) {
            this.mOnBufferingUpdateListener.onBufferingUpdate(this, percent);
        }
        if (this.mOnBufferingUpdateListeners != null) {
            for (IMediaPlayer.OnBufferingUpdateListener listener : this.mOnBufferingUpdateListeners) {
                listener.onBufferingUpdate(this, percent);
            }
        }
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        if (this.mOnVideoSizeChangedListener != null) {
            this.mOnVideoSizeChangedListener.onVideoSizeChanged(this, width, height, 0, 0);
        }
        if (this.mOnVideoSizeChangedListeners != null) {
            for (IMediaPlayer.OnVideoSizeChangedListener listener : this.mOnVideoSizeChangedListeners) {
                listener.onVideoSizeChanged(this, width, height, 0, 0);
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        boolean bRes = true;
        if (!this.mOnError) {
            this.mOnError = true;
            monitorError(what, extra);
            monitorPlayExperience();
            bRes = false;
            if (this.mOnErrorListener != null) {
                bRes = this.mOnErrorListener.onError(this, what, extra);
            }
            if (this.mOnErrorListeners != null) {
                for (IMediaPlayer.OnErrorListener listener : this.mOnErrorListeners) {
                    bRes = listener.onError(this, what, extra);
                }
            }
        }
        return bRes;
    }

    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (what == 701) {
            monitorBufferStart(System.currentTimeMillis());
        } else if (what == 702) {
            monitorBufferEnd(System.currentTimeMillis());
        } else if (what == 3) {
            monitorRenderStart(0);
        }
        boolean bRes = false;
        if (this.mOnInfoListener != null) {
            bRes = this.mOnInfoListener.onInfo(this, (long) what, (long) extra, 0, (Object) null);
        }
        if (this.mOnInfoListeners != null) {
            for (IMediaPlayer.OnInfoListener listener : this.mOnInfoListeners) {
                bRes = listener.onInfo(this, (long) what, (long) extra, 0, (Object) null);
            }
        }
        return bRes;
    }

    @Deprecated
    public final void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
    }

    public void onSeekComplete(MediaPlayer mp) {
        if (this.mOnSeekCompletionListener != null) {
            this.mOnSeekCompletionListener.onSeekComplete(this);
        }
        if (this.mOnSeekCompletionListeners != null) {
            for (IMediaPlayer.OnSeekCompletionListener listener : this.mOnSeekCompletionListeners) {
                listener.onSeekComplete(this);
            }
        }
    }
}
