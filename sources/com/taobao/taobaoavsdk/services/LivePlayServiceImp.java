package com.taobao.taobaoavsdk.services;

import android.content.Context;
import android.view.View;
import com.taobao.taobaoavsdk.IAVObject;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoViewConfig;
import com.taobao.taolive.api.ITBLivePlayService;
import com.taobao.taolive.api.TaoLivePlayConfig;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class LivePlayServiceImp implements IAVObject, ITBLivePlayService, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnPreparedListener {
    private ITBLivePlayService.TaoLivePlayListener mListener;
    private TaoLiveVideoView mVideoView;

    public void initConfig(Context context, TaoLivePlayConfig config) {
        if (this.mVideoView != null) {
            release();
        }
        this.mVideoView = new TaoLiveVideoView(context);
        this.mVideoView.registerOnCompletionListener(this);
        this.mVideoView.registerOnErrorListener(this);
        this.mVideoView.registerOnPreparedListener(this);
        if (config != null) {
            TaoLiveVideoViewConfig liveConfig = new TaoLiveVideoViewConfig(config.mBusinessId, config.mUserId);
            liveConfig.mAccountId = config.mAccountId;
            liveConfig.mCoverResId = config.mCoverResId;
            liveConfig.mDecoderType = config.mDecoderType;
            liveConfig.mFeedId = config.mFeedId;
            liveConfig.mPlayerType = config.mPlayerType;
            liveConfig.mScaleType = config.mScaleType;
            liveConfig.mRenderType = config.mRenderType;
            liveConfig.mScenarioType = config.mScenarioType;
            this.mVideoView.initConfig(liveConfig);
        }
    }

    public View getPlayView() {
        return this.mVideoView;
    }

    public void setListener(ITBLivePlayService.TaoLivePlayListener listener) {
        this.mListener = listener;
    }

    public void setVideoPath(String path) {
        if (this.mVideoView != null) {
            this.mVideoView.setVideoPath(path);
        }
    }

    public void start() {
        if (this.mVideoView != null) {
            this.mVideoView.start();
        }
    }

    public void pause() {
        if (this.mVideoView != null) {
            this.mVideoView.pause();
        }
    }

    public void release() {
        if (this.mVideoView != null) {
            this.mVideoView.unregisterOnCompletionListener(this);
            this.mVideoView.unregisterOnErrorListener(this);
            this.mVideoView.unregisterOnPreparedListener(this);
            this.mVideoView.release();
            this.mVideoView = null;
        }
    }

    public void seedTo(int msec) {
        if (this.mVideoView != null) {
            this.mVideoView.seekTo(msec);
        }
    }

    public int getVideoWidth() {
        if (this.mVideoView != null) {
            return this.mVideoView.getVideoWidth();
        }
        return 0;
    }

    public int getVideoHeight() {
        if (this.mVideoView != null) {
            return this.mVideoView.getVideoHeight();
        }
        return 0;
    }

    public int getDuration() {
        if (this.mVideoView != null) {
            return this.mVideoView.getDuration();
        }
        return 0;
    }

    public int getCurrentPoistion() {
        if (this.mVideoView != null) {
            return this.mVideoView.getCurrentPosition();
        }
        return 0;
    }

    public boolean isPlaying() {
        if (this.mVideoView != null) {
            return this.mVideoView.isPlaying();
        }
        return false;
    }

    public void setMuted(boolean muted) {
        if (this.mVideoView != null) {
            this.mVideoView.setMuted(muted);
        }
    }

    public void setVolume(float left_volume, float right_volume) {
        if (this.mVideoView != null) {
            this.mVideoView.setVolume(left_volume, right_volume);
        }
    }

    public void setScenarioType(int type) {
        if (this.mVideoView != null) {
            this.mVideoView.setScenarioType(type);
        }
    }

    public void setTimeout(int msec) {
        if (this.mVideoView != null) {
            this.mVideoView.setTimeout((long) msec);
        }
    }

    public int getState() {
        if (this.mVideoView != null) {
            return this.mVideoView.getCurrentState();
        }
        return 0;
    }

    public void onCompletion(IMediaPlayer mp) {
        if (this.mListener != null) {
            this.mListener.onComplete();
        }
    }

    public boolean onError(IMediaPlayer mp, int what, int extra) {
        if (this.mListener == null) {
            return true;
        }
        this.mListener.onError(what, extra);
        return true;
    }

    public void onPrepared(IMediaPlayer mp) {
        if (this.mListener != null) {
            this.mListener.onStarted();
        }
    }
}
