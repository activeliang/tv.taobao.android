package com.yunos.tv.tvsdk.media;

import android.content.Context;
import android.media.TimedText;
import android.net.Uri;
import android.os.Parcel;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.IOException;
import java.util.Map;

public interface IMediaPlayer {
    public static final int MEDIA_ERROR_IO = -1004;
    public static final int MEDIA_ERROR_MALFORMED = -1007;
    public static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;
    public static final int MEDIA_ERROR_SERVER_DIED = 100;
    public static final int MEDIA_ERROR_SOURCE = 300;
    public static final int MEDIA_ERROR_TIMED_OUT = -110;
    public static final int MEDIA_ERROR_UNKNOWN = 1;
    public static final int MEDIA_ERROR_UNSUPPORTED = -1010;
    public static final int MEDIA_INFO_BAD_INTERLEAVING = 800;
    public static final int MEDIA_INFO_BUFFERING_END = 702;
    public static final int MEDIA_INFO_BUFFERING_START = 701;
    public static final int MEDIA_INFO_METADATA_UPDATE = 802;
    public static final int MEDIA_INFO_NOT_SEEKABLE = 801;
    public static final int MEDIA_INFO_STARTED_AS_NEXT = 2;
    public static final int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
    public static final int MEDIA_INFO_UNKNOWN = 1;
    public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
    public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;

    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(Object obj, int i);
    }

    public interface OnCompletionListener {
        void onCompletion(Object obj);
    }

    public interface OnErrorListener {
        boolean onError(Object obj, int i, int i2);
    }

    public interface OnInfoExtendListener {
        boolean onInfoExtend(Object obj, int i, int i2, Object obj2);
    }

    public interface OnInfoListener {
        boolean onInfo(Object obj, int i, int i2);
    }

    public interface OnPreparedListener {
        void onPrepared(Object obj);
    }

    public interface OnSeekCompleteListener {
        void onSeekComplete();
    }

    public interface OnTimedTextListener {
        void onTimedText(Object obj, TimedText timedText);
    }

    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(Object obj, int i, int i2);
    }

    String getCodecInfo();

    int getCurrentPosition();

    int getDuration();

    String getHttpHeader();

    String getNetSourceURL();

    Parcel getParcelParameter(int i);

    long getSourceBitrate();

    int getVideoHeight();

    int getVideoWidth();

    boolean isPlaying();

    void pause() throws IllegalStateException;

    void prepare() throws IOException, IllegalStateException;

    void prepareAsync() throws IllegalStateException;

    void release();

    void reset();

    void seekTo(int i) throws IllegalStateException;

    void setAudioStreamType(int i);

    void setDataSource(Context context, Uri uri, Map<String, String> map) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setDisplay(SurfaceHolder surfaceHolder);

    void setHttpDNS(String str);

    void setOnBufferingUpdateListener(OnBufferingUpdateListener onBufferingUpdateListener);

    void setOnCompletionListener(OnCompletionListener onCompletionListener);

    void setOnErrorListener(OnErrorListener onErrorListener);

    void setOnInfoExtendListener(OnInfoExtendListener onInfoExtendListener);

    void setOnInfoListener(OnInfoListener onInfoListener);

    void setOnPreparedListener(OnPreparedListener onPreparedListener);

    void setOnSeekCompleteListener(OnSeekCompleteListener onSeekCompleteListener);

    void setOnTimedTextListener(OnTimedTextListener onTimedTextListener);

    void setOnVideoSizeChangedListener(OnVideoSizeChangedListener onVideoSizeChangedListener);

    boolean setPlayerParameter(int i, Parcel parcel);

    void setScreenOnWhilePlaying(boolean z);

    void setSurface(Surface surface);

    void start() throws IllegalStateException;

    void stop() throws IllegalStateException;
}
