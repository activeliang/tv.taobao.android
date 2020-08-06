package com.yunos.tv.tvsdk.media.view;

import com.yunos.tv.tvsdk.media.view.IBaseVideo;

public interface IVideo extends IBaseVideo {
    public static final int DIMEN_MODE_16_9 = 2;
    public static final int DIMEN_MODE_4_3 = 3;
    public static final int DIMEN_MODE_FULL = 1;
    public static final int DIMEN_MODE_ORIGIN = 0;
    public static final int DIMEN_MODE_UNSET = -1;
    public static final int NETWORK_STATE_401 = 401;
    public static final int NETWORK_STATE_403 = 403;
    public static final int NETWORK_STATE_404 = 404;
    public static final int NETWORK_STATE_408 = 408;
    public static final int STATE_ERROR = -1;
    public static final int STATE_ERROR_CUSTOMER_ERROR = -2;
    public static final int STATE_IDLE = 0;
    public static final int STATE_LOADING = 6;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_PLAYBACK_COMPLETED = 5;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PREPARING = 1;

    public interface OnPreparingTimeoutListener {
        boolean preparingTimeout();
    }

    public interface VideoStateChangeListener {
        void onStateChange(int i);
    }

    void customError(int i, int i2);

    int getCurrentState();

    int getErrorcode();

    int getProgressPercent();

    int getTargetState();

    boolean isAdoPlayer();

    boolean isCustomError();

    boolean isInPlaybackState();

    boolean isPause();

    void setDimensionFull();

    void setDimensionOrigin();

    void setDimension_16_9();

    void setDimension_4_3();

    void setOnAdRemainTimeListenerForMediaCenterView(IBaseVideo.OnAdRemainTimeListener onAdRemainTimeListener);

    void setOnVideoStateChangeListener(VideoStateChangeListener videoStateChangeListener);

    void setOnVideoStateChangeListenerForMediaCenterview(VideoStateChangeListener videoStateChangeListener);
}
