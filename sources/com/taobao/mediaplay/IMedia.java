package com.taobao.mediaplay;

import com.taobao.mediaplay.player.IMediaLoopCompleteListener;
import com.taobao.mediaplay.player.IMediaPlayLifecycleListener;
import com.taobao.mediaplay.player.MediaAspectRatio;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public interface IMedia {
    void asyncPrepareVideo();

    void blockTouchEvent(boolean z);

    void closeVideo();

    void enableVideoClickDetect(boolean z);

    int getBufferPercentage();

    int getCurrentPosition();

    int getDuration();

    int getSurfaceHeight();

    int getSurfaceWidth();

    float getSysVolume();

    int getVideoState();

    int getVideoState2();

    void instantSeekTo(int i);

    void pauseVideo();

    void playVideo();

    void registerIMediaLifecycleListener(IMediaPlayLifecycleListener iMediaPlayLifecycleListener);

    void registerIMediaLoopCompleteListener(IMediaLoopCompleteListener iMediaLoopCompleteListener);

    void registerOnVideoClickListener(IMediaPlayer.OnVideoClickListener onVideoClickListener);

    void retryVideo();

    void seekTo(int i);

    void setMediaAspectRatio(MediaAspectRatio mediaAspectRatio);

    void setPlayRate(float f);

    void setPropertyFloat(int i, float f);

    void setPropertyLong(int i, long j);

    void setSysVolume(float f);

    void setVolume(float f);

    void startVideo();

    void toggleScreen();

    void unregisterOnVideoClickListener(IMediaPlayer.OnVideoClickListener onVideoClickListener);
}
