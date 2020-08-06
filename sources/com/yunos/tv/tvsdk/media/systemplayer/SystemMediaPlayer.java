package com.yunos.tv.tvsdk.media.systemplayer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.Parcel;
import com.yunos.tv.tvsdk.media.IMediaPlayer;

public class SystemMediaPlayer extends MediaPlayer implements IMediaPlayer {
    public void setOnPreparedListener(final IMediaPlayer.OnPreparedListener listener) {
        super.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                if (listener != null && (mp instanceof IMediaPlayer)) {
                    listener.onPrepared((IMediaPlayer) mp);
                }
            }
        });
    }

    public void setOnCompletionListener(final IMediaPlayer.OnCompletionListener listener) {
        super.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if (listener != null && (mp instanceof IMediaPlayer)) {
                    listener.onCompletion((IMediaPlayer) mp);
                }
            }
        });
    }

    public void setOnBufferingUpdateListener(final IMediaPlayer.OnBufferingUpdateListener listener) {
        super.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (listener != null && (mp instanceof IMediaPlayer)) {
                    listener.onBufferingUpdate(mp, percent);
                }
            }
        });
    }

    public void setOnSeekCompleteListener(final IMediaPlayer.OnSeekCompleteListener listener) {
        super.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            public void onSeekComplete(MediaPlayer mp) {
                if (listener != null) {
                    listener.onSeekComplete();
                }
            }
        });
    }

    public void setOnVideoSizeChangedListener(final IMediaPlayer.OnVideoSizeChangedListener listener) {
        super.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                if (listener != null && (mp instanceof IMediaPlayer)) {
                    listener.onVideoSizeChanged((IMediaPlayer) mp, width, height);
                }
            }
        });
    }

    @SuppressLint({"NewApi"})
    public void setOnTimedTextListener(final IMediaPlayer.OnTimedTextListener listener) {
        super.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
            public void onTimedText(MediaPlayer mp, TimedText text) {
                if (listener != null && (mp instanceof IMediaPlayer)) {
                    listener.onTimedText((IMediaPlayer) mp, text);
                }
            }
        });
    }

    public void setOnErrorListener(final IMediaPlayer.OnErrorListener listener) {
        super.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (listener == null || !(mp instanceof IMediaPlayer)) {
                    return false;
                }
                return listener.onError((IMediaPlayer) mp, what, extra);
            }
        });
    }

    public void setOnInfoListener(final IMediaPlayer.OnInfoListener listener) {
        super.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (listener == null || !(mp instanceof IMediaPlayer)) {
                    return false;
                }
                return listener.onInfo((IMediaPlayer) mp, what, extra);
            }
        });
    }

    public void setOnInfoExtendListener(IMediaPlayer.OnInfoExtendListener listener) {
    }

    public long getSourceBitrate() {
        return 0;
    }

    public String getNetSourceURL() {
        return null;
    }

    public boolean setPlayerParameter(int key, Parcel params) {
        return false;
    }

    public Parcel getParcelParameter(int key) {
        return null;
    }

    public String getHttpHeader() {
        return null;
    }

    public void setHttpDNS(String httpdns) {
    }

    public String getCodecInfo() {
        return null;
    }
}
