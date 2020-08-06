package com.yunos.tv.tvsdk.media.view;

import android.app.Activity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.yunos.adoplayer.aidl.InfoExtend;
import com.yunos.tv.tvsdk.media.IMediaPlayer;
import com.yunos.tv.tvsdk.media.data.MTopInfoBase;
import com.yunos.tv.tvsdk.media.error.IMediaError;

public interface IBaseVideo {
    public static final int AUDIO_TYPE_DOBLY = 3;
    public static final int AUDIO_TYPE_DOBLY_PLUS = 4;
    public static final int AUDIO_TYPE_DTS = 0;
    public static final int AUDIO_TYPE_DTS_EXPRESS = 1;
    public static final int AUDIO_TYPE_DTS_HD_MASTER_AUDIO = 2;
    public static final int AUDIO_TYPE_NONE = -1;

    public interface OnAdRemainTimeListener {
        void adRemainTime(int i);
    }

    public interface OnAudioInfoListener {
        void onAudioInfo(int i);
    }

    public interface OnDefinitionChangedListener {
        void onDefinitionChange(boolean z, int i);
    }

    public interface OnFirstFrameListener {
        void onFirstFrame();
    }

    public interface OnPreviewInfoListener {
        void onPreviewCompleted();

        void onPreviewInfoReady(boolean z, int i);
    }

    public interface OnSkipHeadTailInfoListener {
        void onHeaderTailerInfoReady(int i, int i2);
    }

    public interface OnThrowableCallback {
        void throwableCallBack(Throwable th);
    }

    public interface OnVideoErrorListener {
        void onError(IMediaError iMediaError, Object... objArr);
    }

    public interface VideoHttpDnsListener {
        void onHttpDns(long j);
    }

    public interface VideoRequestTsListener {
        void onRequestTs(InfoExtend infoExtend);
    }

    boolean canPause();

    boolean canSeekBackward();

    boolean canSeekForward();

    int getAdRemainTime();

    int getAudioSessionId();

    int getAudioType();

    long getBitRate();

    String getCodecInfo();

    int getCurrentPosition();

    int getDuration();

    int getErrorExtend();

    InfoExtend getErrorInfoExtend();

    String getHttpHeader();

    IMediaPlayer getIMediaPlayer();

    boolean getIgnoreDestroy();

    int getMediaPlayerType();

    String getNetSourceURL();

    View getPlayerView();

    long getRadio();

    long getSourceBitrate();

    SurfaceView getSurfaceView();

    int getVideoHeight();

    int getVideoWidth();

    void hidePauseAd();

    boolean isAdPlaying();

    boolean isPlaying();

    void onActivityResume(Activity activity);

    void onActivityStop(Activity activity);

    void onAuthorityResult(boolean z, MTopInfoBase mTopInfoBase);

    void pause();

    void release();

    void replay(Object... objArr);

    void resume();

    void seekTo(int i);

    void setDefinition(int i, int i2);

    void setDefinitionChangedListener(OnDefinitionChangedListener onDefinitionChangedListener);

    void setDimension(int i, int i2);

    void setHttpDNS(String str);

    void setIgnoreDestroy(boolean z);

    void setMediaPlayerType(int i);

    void setNetadaption(String str);

    void setOnAdRemainTimeListener(OnAdRemainTimeListener onAdRemainTimeListener);

    void setOnAudioInfoListener(OnAudioInfoListener onAudioInfoListener);

    void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener);

    void setOnCompletionListener(IMediaPlayer.OnCompletionListener onCompletionListener);

    void setOnErrorListener(IMediaPlayer.OnErrorListener onErrorListener);

    void setOnFirstFrameListener(OnFirstFrameListener onFirstFrameListener);

    void setOnInfoExtendListener(IMediaPlayer.OnInfoExtendListener onInfoExtendListener);

    void setOnInfoListener(IMediaPlayer.OnInfoListener onInfoListener);

    void setOnPreparedListener(IMediaPlayer.OnPreparedListener onPreparedListener);

    void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener);

    void setOnThrowableCallback(OnThrowableCallback onThrowableCallback);

    void setOnVideoHttpDnsListener(VideoHttpDnsListener videoHttpDnsListener);

    void setOnVideoRequestTsListener(VideoRequestTsListener videoRequestTsListener);

    void setOnVideoSizeChangeListener(IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener);

    void setPauseADTopMarginPercent(int i);

    void setPreviewInfoListener(OnPreviewInfoListener onPreviewInfoListener);

    void setSkipHeadTailInfoListener(OnSkipHeadTailInfoListener onSkipHeadTailInfoListener);

    void setStretch(boolean z);

    void setSurfaceCallback(SurfaceHolder.Callback callback);

    void setVideoInfo(Object... objArr);

    void start();

    void stopPlayback();
}
