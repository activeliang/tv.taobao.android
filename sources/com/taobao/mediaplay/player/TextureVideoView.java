package com.taobao.mediaplay.player;

import android.app.Activity;
import android.app.Application;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.taobao.windvane.monitor.WVPackageMonitorInterface;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Surface;
import android.view.View;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.adapter.FirstRenderAdapter;
import com.taobao.media.DWViewUtil;
import com.taobao.media.MediaAdapteManager;
import com.taobao.media.MediaConstant;
import com.taobao.media.MediaSystemUtils;
import com.taobao.media.connectionclass.ConnectionClassManager;
import com.taobao.media.connectionclass.DeviceBandwidthSampler;
import com.taobao.mediaplay.MediaContext;
import com.taobao.mediaplay.MediaPlayScreenType;
import com.taobao.mediaplay.player.IMediaRenderView;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.taobao.taobaoavsdk.cache.ApplicationUtils;
import com.taobao.taobaoavsdk.recycle.MediaLivePlayerManager;
import com.taobao.taobaoavsdk.recycle.MediaPlayerManager;
import com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler;
import com.taobao.taobaoavsdk.util.AndroidUtils;
import com.taobao.taobaoavsdk.util.DWLogUtils;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoViewConfig;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import tv.danmaku.ijk.media.player.AbstractMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.MonitorMediaPlayer;
import tv.danmaku.ijk.media.player.NativeMediaPlayer;
import tv.danmaku.ijk.media.player.TaobaoMediaPlayer;

public class TextureVideoView extends BaseVideoView implements MediaPlayerRecycler.OnRecycleListener, Application.ActivityLifecycleCallbacks, IMediaPlayer.OnLoopCompletionListener, Handler.Callback, FirstRenderAdapter, IMediaRenderView.IRenderCallback {
    public static int SDK_INT_FOR_OPTIMIZE = 21;
    private static String TAG = "TextureVideoView";
    private static final int UPDATE_PROGRESS = 0;
    private static int UPDATE_PROGRESS_TIME = 200;
    private boolean mActivityAvailable;
    private AudioManager mAudioManager;
    boolean mClosed;
    boolean mCompeleteBfRelease;
    private int mDuration;
    private Handler mHandler;
    private boolean mLooping;
    boolean mNotifyedVideoFirstRender;
    public View mRenderUIView;
    private IMediaRenderView mRenderView;
    boolean mRequestAudioFocus;
    private String mReuseToken;
    boolean mStartForFirstRender;
    private long mStartTime;
    TaoLiveVideoView.SurfaceListener mSurfaceListener;
    private IMediaSurfaceTextureListener mSurfaceTextureListener;
    private boolean mVideoAutoPaused;
    private int mVideoRotationDegree;

    public TextureVideoView(MediaContext context, String token) {
        super(context.getContext());
        this.mActivityAvailable = true;
        this.mStartTime = 0;
        this.mMediaContext = context;
        initRenderView();
        if (!this.mMediaContext.mMediaPlayContext.mTBLive) {
            SDK_INT_FOR_OPTIMIZE = 18;
        }
        this.mAudioManager = (AudioManager) this.mMediaContext.getContext().getApplicationContext().getSystemService("audio");
        this.mHandler = new Handler(this);
        this.mReuseToken = token;
        String tmp_Token = token;
        tmp_Token = TextUtils.isEmpty(tmp_Token) ? MediaPlayerManager.generateToken() : tmp_Token;
        if (this.mMediaContext.mMediaPlayContext.mTBLive) {
            this.mMediaPlayerRecycler = MediaLivePlayerManager.getInstance().getMediaRecycler(tmp_Token, this);
        } else {
            this.mMediaPlayerRecycler = MediaPlayerManager.getInstance().getMediaRecycler(tmp_Token, this);
        }
        if (MediaSystemUtils.sApplication != null) {
            MediaSystemUtils.sApplication.registerActivityLifecycleCallbacks(this);
        }
        if (MediaAdapteManager.mConfigAdapter != null && MediaAdapteManager.mABTestAdapter != null && this.mMediaContext.mMediaPlayContext.mTBLive && "LiveRoom".equals(this.mMediaContext.mMediaPlayContext.mFrom)) {
            try {
                int report_time = AndroidUtils.parseInt(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaContext.mMediaPlayContext.mConfigGroup, "heartBeatReportDuration", "60"));
                MonitorMediaPlayer.REPORT_DURATION = report_time <= 0 ? MonitorMediaPlayer.REPORT_DURATION : report_time;
                int player_report_time = AndroidUtils.parseInt(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaContext.mMediaPlayContext.mConfigGroup, "playHeartBeatReportTime", "10"));
                MonitorMediaPlayer.PLAYER_REPORT_DURATION = player_report_time <= 0 ? MonitorMediaPlayer.PLAYER_REPORT_DURATION : player_report_time;
                double decy = AndroidUtils.parseDouble(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaContext.mMediaPlayContext.mConfigGroup, "netspeed_decay", "0.05")).doubleValue();
                ConnectionClassManager.DEFAULT_DECAY_CONSTANT = decy <= ClientTraceData.b.f47a ? ConnectionClassManager.DEFAULT_DECAY_CONSTANT : decy;
            } catch (Throwable th) {
            }
        }
    }

    public TextureVideoView(MediaContext context) {
        this(context, (String) null);
    }

    /* access modifiers changed from: protected */
    public void init() {
    }

    public void setSurfaceTextureListener(IMediaSurfaceTextureListener listener) {
        this.mSurfaceTextureListener = listener;
    }

    public void setSurfaceListener(TaoLiveVideoView.SurfaceListener listener) {
        this.mSurfaceListener = listener;
    }

    /* access modifiers changed from: private */
    public void sendUpdateProgressMsg() {
        if (getVideoState() != 3 && getVideoState() != 6 && getVideoState() != 4 && this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
            this.mHandler.sendEmptyMessageDelayed(0, (long) UPDATE_PROGRESS_TIME);
        }
    }

    private void removeUpdateProgressMsg() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(0);
        }
    }

    public View getView() {
        return this.mRenderView.getView();
    }

    public void setLooping(boolean looping) {
        this.mLooping = looping;
    }

    private void initMediaPlayer() {
        MediaPlayerRecycler mediaRecycler;
        if (this.mVideoPath != null && this.mContext != null) {
            if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mPlayState == 3) {
                if (this.mMediaContext.mMediaPlayContext.mTBLive) {
                    MediaLivePlayerManager.getInstance().removePlayerFromCache(this.mMediaPlayerRecycler.mToken, this);
                } else {
                    MediaPlayerManager.getInstance().removePlayerFromCache(this.mMediaPlayerRecycler.mToken, this);
                }
                this.mMediaPlayerRecycler.mMediaPlayer = null;
            }
            if (this.mMediaContext.mMediaPlayContext.mTBLive) {
                mediaRecycler = MediaLivePlayerManager.getInstance().getMediaRecycler(this.mMediaPlayerRecycler.mToken, this);
            } else {
                mediaRecycler = MediaPlayerManager.getInstance().getMediaRecycler(this.mMediaPlayerRecycler.mToken, this);
            }
            this.mMediaPlayerRecycler = mediaRecycler;
            if (this.mMediaContext.mMute) {
                setVolume(0.0f);
            }
            if (this.mMediaPlayerRecycler.mMediaPlayer == null) {
                this.mMediaPlayerRecycler.mPlayState = 0;
                this.mMediaPlayerRecycler.mMediaPlayer = initPlayer();
            }
            if (!TextUtils.isEmpty(this.mReuseToken)) {
                bindSurfaceHolder(this.mMediaPlayerRecycler.mMediaPlayer, getHolder());
                requestVideoLayout(this.mMediaPlayerRecycler.mMediaPlayer);
            }
            setMediaplayerListener(this.mMediaPlayerRecycler.mMediaPlayer);
            this.mMediaPlayerRecycler.mMediaPlayer.setLooping(this.mLooping);
            if (this.mMediaPlayerRecycler == null) {
                return;
            }
            if ((this.mMediaPlayerRecycler.mPlayState == 5 || this.mMediaPlayerRecycler.mPlayState == 8 || this.mMediaPlayerRecycler.mPlayState == 4 || this.mMediaPlayerRecycler.mPlayState == 2 || this.mMediaPlayerRecycler.mPlayState == 1) && this.mActivityAvailable) {
                this.mMediaPlayerRecycler.mMediaPlayer.start();
                notifyVideoStart();
                sendUpdateProgressMsg();
            }
        }
    }

    private void setMediaplayerListener(AbstractMediaPlayer mediaPlayer) {
        try {
            mediaPlayer.registerOnPreparedListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            mediaPlayer.registerOnCompletionListener(this);
            mediaPlayer.registerOnErrorListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.registerOnInfoListener(this);
            mediaPlayer.registerOnLoopCompletionListener(this);
            mediaPlayer.setScreenOnWhilePlaying(true);
        } catch (Throwable th) {
        }
    }

    private void unregisterMediaplayerListener(AbstractMediaPlayer mediaPlayer) {
        try {
            mediaPlayer.setOnVideoSizeChangedListener((IMediaPlayer.OnVideoSizeChangedListener) null);
            mediaPlayer.setOnBufferingUpdateListener((IMediaPlayer.OnBufferingUpdateListener) null);
            mediaPlayer.unregisterOnPreparedListener(this);
            mediaPlayer.unregisterOnVideoSizeChangedListener(this);
            mediaPlayer.unregisterOnCompletionListener(this);
            mediaPlayer.unregisterOnErrorListener(this);
            mediaPlayer.unregisterOnBufferingUpdateListener(this);
            mediaPlayer.unregisterOnInfoListener(this);
            mediaPlayer.unregisterOnLoopCompletionListener(this);
        } catch (Throwable e) {
            if (this.mMediaContext != null && e != null) {
                DWLogUtils.e(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "unregisterMediaplayerListener##error:" + e.getMessage());
            }
        }
    }

    public void setVideoPath(String path) {
        if (!TextUtils.isEmpty(path)) {
            this.mVideoPath = path;
            if (this.mMediaPlayerRecycler != null && isInErrorState(this.mMediaPlayerRecycler.mPlayState) && !this.mClosed) {
                if ((this.mTargetState != 1 && this.mTargetState != 8) || TextUtils.isEmpty(this.mVideoPath) || this.mMediaPlayerRecycler.mRecycled) {
                    return;
                }
                if (this.mTargetState == 1) {
                    startVideo();
                } else {
                    asyncPrepare();
                }
            }
        }
    }

    public void videoPlayError() {
        this.mStartForFirstRender = false;
        notifyVideoErrorForInit();
    }

    private void initRenderView() {
        this.mRenderView = new MediaTextureView(this.mContext);
        this.mRenderView.addRenderCallback(this);
        this.mRenderView.setVideoRotation(this.mVideoRotationDegree);
        setAspectRatio(this.mMediaContext.getVideoAspectRatio());
        this.mRenderUIView = this.mRenderView.getView();
    }

    public void setPropertyFloat(int property, float value) {
        if (this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null) {
            if (this.mPropertyFloat == null) {
                this.mPropertyFloat = new SparseArray();
            }
            this.mPropertyFloat.put(property, Float.valueOf(value));
        } else if (this.mMediaPlayerRecycler.mMediaPlayer instanceof IjkMediaPlayer) {
            ((IjkMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer)._setPropertyFloat(property, value);
        } else if (this.mMediaPlayerRecycler.mMediaPlayer instanceof TaobaoMediaPlayer) {
            ((TaobaoMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer)._setPropertyFloat(property, value);
        }
    }

    public void setMediaSourceType(String mediaSourceType) {
        if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
            if (((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getConfig() != null) {
                ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getConfig().mMediaSourceType = mediaSourceType;
            }
            if (((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getCloneConfig() != null) {
                ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getCloneConfig().mMediaSourceType = mediaSourceType;
            }
        }
    }

    public void setMediaId(String mediaId) {
        if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
            if (((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getConfig() != null) {
                ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getConfig().mFeedId = mediaId;
            }
            if (((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getCloneConfig() != null) {
                ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getCloneConfig().mFeedId = mediaId;
            }
        }
    }

    public void setAccountId(String accountId) {
        if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
            if (((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getConfig() != null) {
                ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getConfig().mAccountId = accountId;
            }
            if (((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getCloneConfig() != null) {
                ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getCloneConfig().mAccountId = accountId;
            }
        }
    }

    public void destroy() {
        try {
            if (!(this.mHolder == null || this.mHolder.getSurface() == null)) {
                this.mHolder.getSurface().release();
            }
        } catch (Throwable th) {
        }
        if (MediaSystemUtils.sApplication != null) {
            MediaSystemUtils.sApplication.unregisterActivityLifecycleCallbacks(this);
        }
    }

    public void setPropertyLong(int property, long value) {
        if (this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null) {
            if (this.mPropertyLong == null) {
                this.mPropertyLong = new SparseArray();
            }
            this.mPropertyLong.put(property, Long.valueOf(value));
        } else if (this.mMediaPlayerRecycler.mMediaPlayer instanceof IjkMediaPlayer) {
            ((IjkMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer)._setPropertyLong(property, value);
        } else if (this.mMediaPlayerRecycler.mMediaPlayer instanceof TaobaoMediaPlayer) {
            ((TaobaoMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer)._setPropertyLong(property, value);
        }
    }

    private boolean needSetFusionMode() {
        String modleStr = MediaAdapteManager.mConfigAdapter != null ? MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaContext.mMediaPlayContext.mConfigGroup, "SensorFusionCalibrate", "") : null;
        if (TextUtils.isEmpty(modleStr)) {
            return false;
        }
        String phoneModel = Build.MODEL;
        String[] models = modleStr.split(SymbolExpUtil.SYMBOL_SEMICOLON);
        if (models.length <= 0) {
            return false;
        }
        for (String model : models) {
            if (TextUtils.equals(phoneModel, model)) {
                return true;
            }
        }
        return false;
    }

    public void startVideo() {
        if (this.mMediaContext != null) {
            DWLogUtils.d(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "startVideo##PlayState:" + this.mMediaPlayerRecycler.mPlayState + " VideoUrl:" + this.mVideoPath);
        }
        this.mVideoStarted = true;
        this.mClosed = false;
        this.mTargetState = 1;
        restorePauseState();
        if (this.mMediaPlayerRecycler.mRecycled) {
            this.mStartForFirstRender = false;
            if (this.mMediaPlayerRecycler.mLastState == 4) {
                this.mCompeleteBfRelease = true;
            }
            initMediaPlayerAfterRecycle();
            this.mMediaPlayerRecycler.mLastState = 1;
            return;
        }
        this.mMediaPlayerRecycler.mLastPosition = 0;
        if (!this.mStartForFirstRender) {
            this.mStartForFirstRender = true;
            this.mStartTime = System.currentTimeMillis();
        }
        if (isInErrorState(this.mMediaPlayerRecycler.mPlayState) && !TextUtils.isEmpty(this.mVideoPath)) {
            initMediaPlayer();
            if (TextUtils.isEmpty(this.mReuseToken) && this.mMediaPlayerRecycler.mPlayState != 3) {
                this.mMediaPlayerRecycler.mPlayState = 8;
                this.mMediaPlayerRecycler.mRecycled = false;
            }
        } else if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null && this.mMediaPlayerRecycler.mPlayState == 5 && !TextUtils.isEmpty(this.mVideoPath) && this.mActivityAvailable) {
            keepScreenOn();
            this.mMediaPlayerRecycler.mMediaPlayer.start();
            notifyVideoStart();
            sendUpdateProgressMsg();
        } else if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null && !TextUtils.isEmpty(this.mReuseToken) && (this.mMediaPlayerRecycler.mPlayState == 2 || this.mMediaPlayerRecycler.mPlayState == 1 || this.mMediaPlayerRecycler.mPlayState == 4)) {
            playVideo();
        } else if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null && !TextUtils.isEmpty(this.mReuseToken) && this.mMediaPlayerRecycler.mPlayState == 8) {
            setMediaplayerListener(this.mMediaPlayerRecycler.mMediaPlayer);
        }
    }

    public void keepScreenOn() {
        if (this.mContext != null && (this.mContext instanceof Activity)) {
            ((Activity) this.mContext).getWindow().addFlags(128);
        }
    }

    public void clearKeepScreenOn() {
        if (this.mContext != null && (this.mContext instanceof Activity)) {
            ((Activity) this.mContext).getWindow().clearFlags(128);
        }
    }

    public void playVideo() {
        MediaPlayerRecycler mediaRecycler;
        restorePauseState();
        this.mClosed = false;
        if (this.mMediaPlayerRecycler.mRecycled) {
            if (this.mMediaPlayerRecycler.mLastState == 4) {
                this.mCompeleteBfRelease = true;
            }
            this.mMediaPlayerRecycler.mLastState = 1;
            this.mStartTime = System.currentTimeMillis();
            initMediaPlayerAfterRecycle();
            return;
        }
        if (!this.mNotifyedVideoFirstRender) {
            this.mStartForFirstRender = true;
            this.mStartTime = System.currentTimeMillis();
        }
        try {
            if (!(this.mMediaPlayerRecycler.mVolume == 0.0f || this.mAudioManager == null)) {
                this.mAudioManager.requestAudioFocus((AudioManager.OnAudioFocusChangeListener) null, 3, 1);
                this.mRequestAudioFocus = true;
            }
        } catch (Throwable th) {
        }
        try {
            if (this.mMediaContext != null) {
                DWLogUtils.d(this.mMediaContext.mMediaPlayContext.mTLogAdapter, " playVideo##PlayState:" + this.mMediaPlayerRecycler.mPlayState);
            }
            if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null && this.mVideoStarted) {
                if ((this.mMediaPlayerRecycler.mPlayState == 2 || this.mMediaPlayerRecycler.mPlayState == 5 || this.mMediaPlayerRecycler.mPlayState == 4) && this.mActivityAvailable) {
                    if (this.mMediaContext.mMediaPlayContext.mTBLive) {
                        mediaRecycler = MediaLivePlayerManager.getInstance().getMediaRecycler(this.mMediaPlayerRecycler.mToken, this);
                    } else {
                        mediaRecycler = MediaPlayerManager.getInstance().getMediaRecycler(this.mMediaPlayerRecycler.mToken, this);
                    }
                    this.mMediaPlayerRecycler = mediaRecycler;
                    if (this.mMediaContext.mMute) {
                        setVolume(0.0f);
                    }
                    keepScreenOn();
                    this.mMediaPlayerRecycler.mMediaPlayer.start();
                    bindSurfaceHolder(this.mMediaPlayerRecycler.mMediaPlayer, getHolder());
                    if (!TextUtils.isEmpty(this.mReuseToken)) {
                        requestVideoLayout(this.mMediaPlayerRecycler.mMediaPlayer);
                    }
                    setMediaplayerListener(this.mMediaPlayerRecycler.mMediaPlayer);
                    if (this.mMediaPlayerRecycler.mPlayState == 4 || this.mMediaPlayerRecycler.mPlayState == 5) {
                        notifyVideoStart();
                    } else {
                        notifyVideoPlay();
                    }
                    sendUpdateProgressMsg();
                }
            }
        } catch (Throwable e) {
            DWLogUtils.e(TAG, "playVideo >>> " + e.getMessage());
        }
    }

    public void initMediaPlayerAfterRecycle() {
        MediaPlayerRecycler mediaRecyclerAfterRecycled;
        if (this.mMediaPlayerRecycler.mPlayState != 8) {
            this.mMediaPlayerRecycler.mPlayState = 0;
        }
        if (this.mMediaContext.mMediaPlayContext.mTBLive) {
            mediaRecyclerAfterRecycled = MediaLivePlayerManager.getInstance().getMediaRecyclerAfterRecycled(this.mMediaPlayerRecycler);
        } else {
            mediaRecyclerAfterRecycled = MediaPlayerManager.getInstance().getMediaRecyclerAfterRecycled(this.mMediaPlayerRecycler);
        }
        this.mMediaPlayerRecycler = mediaRecyclerAfterRecycled;
        if (this.mMediaPlayerRecycler.mMediaPlayer == null) {
            this.mMediaPlayerRecycler.mMediaPlayer = initPlayer();
            this.mMediaPlayerRecycler.mPlayState = 8;
        } else {
            setMediaplayerListener(this.mMediaPlayerRecycler.mMediaPlayer);
        }
        bindSurfaceHolder(this.mMediaPlayerRecycler.mMediaPlayer, getHolder());
        this.mMediaPlayerRecycler.mMediaPlayer.setLooping(this.mLooping);
    }

    public void restorePauseState() {
        int i = 1;
        this.mMediaPlayerRecycler.mLastPausedState = true;
        MediaPlayerRecycler mediaPlayerRecycler = this.mMediaPlayerRecycler;
        if (this.mMediaPlayerRecycler.mLastState != 2) {
            i = this.mMediaPlayerRecycler.mLastState;
        }
        mediaPlayerRecycler.mLastState = i;
    }

    public void pauseVideo(boolean auto) {
        boolean z;
        MediaPlayerRecycler mediaPlayerRecycler = this.mMediaPlayerRecycler;
        if (!this.mMediaPlayerRecycler.mLastPausedState || auto) {
            z = this.mMediaPlayerRecycler.mLastPausedState;
        } else {
            z = auto;
        }
        mediaPlayerRecycler.mLastPausedState = z;
        this.mVideoAutoPaused = auto;
        this.mTargetState = 2;
        if (this.mMediaContext != null) {
            DWLogUtils.d(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "pauseVideo##PlayState:" + this.mMediaPlayerRecycler.mPlayState);
        }
        if (this.mMediaPlayerRecycler.mMediaPlayer != null && this.mMediaPlayerRecycler.mPlayState == 1) {
            clearKeepScreenOn();
            this.mMediaPlayerRecycler.mMediaPlayer.pause();
            if (this.mMediaContext.mMediaPlayContext.mTBLive) {
                MediaLivePlayerManager.getInstance().reorderLruMediaPlayer();
            } else {
                MediaPlayerManager.getInstance().reorderLruMediaPlayer();
            }
            notifyVideoPause(auto);
            removeUpdateProgressMsg();
        }
    }

    public void seekTo(int progress) {
        if (progress >= 0) {
            removeUpdateProgressMsg();
            seekToWithoutNotify(progress, false);
            if ((this.mMediaPlayerRecycler.mPlayState == 5 || this.mMediaPlayerRecycler.mPlayState == 2 || this.mMediaPlayerRecycler.mPlayState == 4 || this.mMediaPlayerRecycler.mPlayState == 1) && !this.mClosed) {
                notifyVideoSeekTo(progress);
            }
        }
    }

    public void instantSeekTo(int progress) {
        if (progress >= 0) {
            removeUpdateProgressMsg();
            if (this.mMediaPlayerRecycler.mPlayState == 5 || this.mMediaPlayerRecycler.mPlayState == 2 || this.mMediaPlayerRecycler.mPlayState == 4 || this.mMediaPlayerRecycler.mPlayState == 1) {
                if (progress > getDuration()) {
                    progress = getDuration();
                }
                instantSeekTo(this.mMediaPlayerRecycler.mMediaPlayer, (long) progress);
            }
        }
    }

    public void seekToWithoutNotify(int progress, boolean bInstantSeek) {
        if (this.mMediaPlayerRecycler.mPlayState == 5 || this.mMediaPlayerRecycler.mPlayState == 2 || this.mMediaPlayerRecycler.mPlayState == 4 || this.mMediaPlayerRecycler.mPlayState == 1) {
            if (progress > getDuration()) {
                progress = getDuration();
            }
            if (bInstantSeek) {
                instantSeekTo(this.mMediaPlayerRecycler.mMediaPlayer, (long) progress);
            } else {
                seek(this.mMediaPlayerRecycler.mMediaPlayer, (long) progress);
            }
            this.mMediaPlayerRecycler.mMediaPlayer.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompletionListener() {
                public void onSeekComplete(IMediaPlayer IMediaPlayer) {
                    TextureVideoView.this.sendUpdateProgressMsg();
                }
            });
        }
    }

    public void closeVideo() {
        close();
        notifyVideoClose();
    }

    public void onVideoScreenChanged(MediaPlayScreenType videoScreenType) {
        notifyVideoScreenChanged(videoScreenType);
    }

    public boolean isPlaying() {
        if (this.mMediaPlayerRecycler.mMediaPlayer == null || this.mMediaPlayerRecycler.mPlayState == 0 || this.mMediaPlayerRecycler.mPlayState == 8 || this.mMediaPlayerRecycler.mPlayState == 3 || this.mMediaPlayerRecycler.mPlayState == 6) {
            return false;
        }
        return this.mMediaPlayerRecycler.mMediaPlayer.isPlaying();
    }

    public boolean isInPlaybackState() {
        return (this.mMediaPlayerRecycler.mMediaPlayer == null || this.mMediaPlayerRecycler.mPlayState == 0 || this.mMediaPlayerRecycler.mPlayState == 8 || this.mMediaPlayerRecycler.mPlayState == 3 || this.mMediaPlayerRecycler.mPlayState == 6) ? false : true;
    }

    public int getDestoryState() {
        return 6;
    }

    public void onCompletion(IMediaPlayer mp) {
        if (!this.mClosed && this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
            if (this.mMediaPlayerRecycler.mPlayState == 1 || this.mMediaPlayerRecycler.mPlayState == 4) {
                clearKeepScreenOn();
                MediaPlayerManager.getInstance().reorderLruMediaPlayer();
                long currentPosition = (long) getDuration();
                if (currentPosition >= 0) {
                    long duration = (long) getDuration();
                    int secondaryProgress = 0;
                    if (duration > 0) {
                        secondaryProgress = getVideoBufferPercent();
                    }
                    notifyVideoTimeChanged((int) currentPosition, secondaryProgress, (int) duration);
                }
                notifyVideoComplete();
                removeUpdateProgressMsg();
            }
        }
    }

    public boolean onError(IMediaPlayer mp, int what, int extra) {
        if (this.mMediaContext != null) {
            DWLogUtils.e(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "onError##VideoOnError >>> what: " + what + ", extra :" + extra + ",videoURL:" + this.mVideoPath);
        }
        clearKeepScreenOn();
        this.mStartForFirstRender = false;
        if (!(this.mClosed || this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null)) {
            if (this.mMediaPlayerRecycler.mRecycled) {
                this.mMediaPlayerRecycler.mRecycled = false;
            }
            MediaPlayerManager.getInstance().reorderLruMediaPlayer();
            notifyVideoError(mp, what, extra);
        }
        return true;
    }

    public boolean onInfo(IMediaPlayer iMediaPlayer, long what, long extra, long ext, Object object) {
        if (MediaSystemUtils.isApkDebuggable()) {
            DWLogUtils.d(DWLogUtils.TAG, " onInfo >>> what: " + what + ", extra :" + extra);
        }
        if (3 == what) {
            Map<String, Long> time = new HashMap<>();
            time.put(MediaConstant.RENDER_START_TIME, Long.valueOf(this.mStartTime));
            long endTime = 0;
            if (this.mStartTime != 0) {
                endTime = extra > 0 ? extra : System.currentTimeMillis();
            }
            this.mNotifyedVideoFirstRender = true;
            time.put(MediaConstant.RENDER_END_TIME, Long.valueOf(endTime));
            notifyVideoInfo(iMediaPlayer, what, extra, ext, time);
            return true;
        }
        if (711 == what && MediaSystemUtils.isApkDebuggable()) {
            DWLogUtils.d(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "-->commitMediaPlayerRender open file time:" + extra + " file_find_stream_info_time:" + ext);
        } else if (10001 == what) {
            this.mVideoRotationDegree = (int) extra;
            if (this.mRenderView != null) {
                this.mRenderView.setVideoRotation((int) extra);
            }
        } else if (715 == what) {
            String str = (String) object;
            this.mSeiData = str;
            DWLogUtils.d(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "SEI STRUCT: " + str + ",pts: " + ext);
        } else if (10003 == what && getVideoState() == 1) {
            if (this.mMediaContext.mMediaPlayContext.mTBLive) {
                MediaLivePlayerManager.getInstance().removePlayerFromCache(this.mMediaPlayerRecycler.mToken, this);
            } else {
                MediaPlayerManager.getInstance().removePlayerFromCache(this.mMediaPlayerRecycler.mToken, this);
            }
            startVideo();
        } else if (!TextUtils.isEmpty(this.mVideoPath) && ((this.mMediaContext.mScenarioType == 0 || this.mMediaContext.mScenarioType == 1) && this.mVideoPath.contains(".flv") && !this.mVideoPath.contains(".m3u8") && !this.mVideoPath.contains(".mp4") && 10004 == what && ((getVideoState() == 1 || getVideoState() == 8 || getVideoState() == 5) && MediaAdapteManager.mConfigAdapter != null && AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter.getConfig("MediaLive", "degradeMcodecDecodeError", "false")) && this.mMediaContext.mMediaPlayContext.mTBLive && this.mMediaContext.mMediaPlayContext.mTBLive))) {
            MediaLivePlayerManager.getInstance().removePlayerFromCache(this.mMediaPlayerRecycler.mToken, this);
            ApplicationUtils.bUseMediacodecForLive = false;
            this.mMediaContext.mMediaPlayContext.setHardwareHevc(false);
            this.mMediaContext.mMediaPlayContext.setHardwareAvc(false);
            startVideo();
        }
        notifyVideoInfo(iMediaPlayer, what, extra, ext, object);
        return true;
    }

    public void seek(AbstractMediaPlayer mediaPlayer, long msec) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(msec);
        }
    }

    public void instantSeekTo(AbstractMediaPlayer mediaPlayer, long msec) {
        if (mediaPlayer != null) {
            mediaPlayer.instantSeekTo(msec);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean resumeMediaPlayerAfterRecycle() {
        if (this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null) {
            return false;
        }
        int statebfRelease = this.mMediaPlayerRecycler.mLastState;
        if (statebfRelease == 2) {
            seek(this.mMediaPlayerRecycler.mMediaPlayer, (long) this.mMediaPlayerRecycler.mLastPosition);
            return true;
        } else if (statebfRelease == 4) {
            seek(this.mMediaPlayerRecycler.mMediaPlayer, (long) this.mMediaPlayerRecycler.mLastPosition);
            return true;
        } else if (statebfRelease != 1) {
            return false;
        } else {
            seek(this.mMediaPlayerRecycler.mMediaPlayer, (long) this.mMediaPlayerRecycler.mLastPosition);
            this.mMediaPlayerRecycler.mMediaPlayer.start();
            return true;
        }
    }

    public void onPrepared(IMediaPlayer mp) {
        if (!this.mClosed && this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
            if (this.mMediaContext != null) {
                DWLogUtils.d(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "onPrepared##PlayState:" + this.mMediaPlayerRecycler.mPlayState);
            }
            try {
                if (!(this.mMediaPlayerRecycler.mVolume == 0.0f || this.mAudioManager == null || this.mRequestAudioFocus)) {
                    this.mAudioManager.requestAudioFocus((AudioManager.OnAudioFocusChangeListener) null, 3, 1);
                    this.mRequestAudioFocus = true;
                }
            } catch (Throwable th) {
            }
            if (!resumeMediaPlayerAfterRecycle() || !this.mMediaPlayerRecycler.mRecycled) {
                if (!this.mVideoAutoPaused) {
                    notifyVideoPrepared(mp);
                }
                if (this.mMediaContext.mMediaPlayContext.mSeekWhenPrepared != 0) {
                    seekTo(this.mMediaContext.mMediaPlayContext.mSeekWhenPrepared);
                    this.mMediaContext.mMediaPlayContext.mSeekWhenPrepared = 0;
                }
                if (this.mTargetState == 1 && this.mMediaPlayerRecycler.mMediaPlayer != null && this.mActivityAvailable) {
                    keepScreenOn();
                    this.mMediaPlayerRecycler.mMediaPlayer.start();
                    notifyVideoStart();
                    sendUpdateProgressMsg();
                } else if ((this.mTargetState != 1 || !this.mActivityAvailable) && this.mMediaPlayerRecycler.mMediaPlayer != null) {
                    this.mMediaPlayerRecycler.mMediaPlayer.pause();
                }
                if (this.mMediaPlayerRecycler.mLastPosition > 0 && this.mMediaPlayerRecycler.mMediaPlayer != null) {
                    seek(this.mMediaPlayerRecycler.mMediaPlayer, (long) this.mMediaPlayerRecycler.mLastPosition);
                    return;
                }
                return;
            }
            if (this.mMediaPlayerRecycler.mLastState == 2) {
                this.mMediaPlayerRecycler.mPlayState = 2;
            } else if (this.mMediaPlayerRecycler.mLastState == 4) {
                this.mMediaPlayerRecycler.mPlayState = 4;
            } else if (this.mMediaPlayerRecycler.mLastState == 1) {
                sendUpdateProgressMsg();
                if (this.mCompeleteBfRelease) {
                    notifyVideoStart();
                } else {
                    notifyVideoPlay();
                }
            } else if (this.mMediaPlayerRecycler.mLastState == 5) {
                notifyVideoPrepared(mp);
            }
            this.mMediaPlayerRecycler.mRecycled = false;
            setStatebfRelease(-1);
            this.mCompeleteBfRelease = false;
        }
    }

    public void onSurfaceCreated(@NonNull IMediaRenderView.ISurfaceHolder holder, int width, int height) {
        if (this.mMediaContext != null) {
            DWLogUtils.d(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "onSurfaceTextureAvailable##PlayState=" + this.mMediaPlayerRecycler.mPlayState);
        }
        this.mHolder = holder;
        this.mSurfaceWidth = width;
        this.mSurfaceHeight = height;
        if ((this.mVideoStarted || this.mVideoPrepared) && !this.mMediaPlayerRecycler.mRecycled && this.mMediaPlayerRecycler.mPlayState != 6 && this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
            bindSurfaceHolder(this.mMediaPlayerRecycler.mMediaPlayer, getHolder());
        }
    }

    public void onSurfaceChanged(@NonNull IMediaRenderView.ISurfaceHolder holder, int format, int width, int height) {
        if (this.mMediaContext != null) {
            DWLogUtils.d(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "onSurfaceTextureAvailable##Video width:" + width + "，height:" + height);
        }
        this.mSurfaceWidth = width;
        this.mSurfaceHeight = height;
        if (holder.getRenderView() == this.mRenderView) {
            this.mHolder = holder;
            if (!(!this.mMediaContext.mVRLive || this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null || holder.getSurface() == null)) {
                this.mMediaPlayerRecycler.mMediaPlayer.setSurface(holder.getSurface());
            }
            if (this.mSurfaceListener != null) {
                this.mSurfaceListener.onSurfaceCreated();
            }
        }
    }

    public void onSurfaceDestroyed(@NonNull IMediaRenderView.ISurfaceHolder holder) {
        if (this.mMediaContext != null) {
            DWLogUtils.d(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "onSurfaceDestroyed##PlayState =" + this.mMediaPlayerRecycler.mPlayState);
        }
        if ((this.mMediaPlayerRecycler.mPlayState == 5 || this.mMediaPlayerRecycler.mPlayState == 4 || this.mMediaPlayerRecycler.mPlayState == 2 || this.mMediaPlayerRecycler.mPlayState == 1) && Build.VERSION.SDK_INT < SDK_INT_FOR_OPTIMIZE) {
            this.mMediaPlayerRecycler.mMediaPlayer.setSurface((Surface) null);
        }
        this.mMediaPlayerRecycler.mLastPosition = getCurrentPosition();
        if (this.mSurfaceListener != null) {
            this.mSurfaceListener.onSurfaceDestroyed();
        }
    }

    public void onSurfaceUpdate(IMediaRenderView.ISurfaceHolder holder) {
        if (this.mSurfaceTextureListener != null) {
            this.mSurfaceTextureListener.updated(this);
        }
    }

    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
        if (this.mMediaContext != null) {
            DWLogUtils.i(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "onVideoSizeChanged##Video width:" + width + ", height:" + height);
        }
        requestVideoLayout(mp);
    }

    private void requestVideoLayout(IMediaPlayer mp) {
        this.mVideoWidth = mp.getVideoWidth();
        this.mVideoHeight = mp.getVideoHeight();
        if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
            this.mRenderView.setVideoSize(this.mVideoWidth, this.mVideoHeight);
            int videoSarNum = mp.getVideoSarNum();
            int videoSarDen = mp.getVideoSarDen();
            if (videoSarNum > 0 && videoSarDen > 0) {
                this.mRenderView.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
            }
            this.mRenderView.requestLayout();
        }
    }

    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
        this.mVideoBufferPercent = percent;
    }

    public int getDuration() {
        if ((this.mMediaPlayerRecycler.mPlayState == 5 || this.mMediaPlayerRecycler.mPlayState == 1 || this.mMediaPlayerRecycler.mPlayState == 4 || this.mMediaPlayerRecycler.mPlayState == 2) && this.mMediaPlayerRecycler.mMediaPlayer != null) {
            this.mDuration = (int) this.mMediaPlayerRecycler.mMediaPlayer.getDuration();
        }
        return this.mDuration;
    }

    public void asyncPrepare() {
        if (MediaSystemUtils.isApkDebuggable() && this.mMediaContext != null) {
            DWLogUtils.d(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "asyncPrepareVideo##PlayState:" + this.mMediaPlayerRecycler.mPlayState);
        }
        this.mClosed = false;
        this.mVideoPrepared = true;
        this.mTargetState = 8;
        restorePauseState();
        this.mMediaPlayerRecycler.mLastPosition = 0;
        if (this.mMediaPlayerRecycler.mRecycled) {
            this.mMediaPlayerRecycler.mLastState = 5;
            initMediaPlayerAfterRecycle();
            this.mStartForFirstRender = false;
        } else if (isInErrorState(this.mMediaPlayerRecycler.mPlayState) && !TextUtils.isEmpty(this.mVideoPath)) {
            initMediaPlayer();
            if (TextUtils.isEmpty(this.mReuseToken)) {
                this.mMediaPlayerRecycler.mPlayState = 8;
                this.mMediaPlayerRecycler.mRecycled = false;
            }
        }
    }

    public AbstractMediaPlayer initPlayer() {
        AbstractMediaPlayer mediaPlayer;
        if (this.mMediaContext != null) {
            DWLogUtils.d(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "initPlayer##PlayState:" + this.mMediaPlayerRecycler.mPlayState + ",videoURL:" + this.mVideoPath);
        }
        TaoLiveVideoViewConfig config = new TaoLiveVideoViewConfig(this.mMediaContext.mMediaPlayContext.mBusinessId);
        if (MediaAdapteManager.mABTestAdapter == null || !MediaConstant.ABTEST_USE_TAOBAOPLAYER.equals(MediaAdapteManager.mABTestAdapter.getBucket(MediaConstant.ABTEST_PLAYERCORE_COMOPONENT, MediaConstant.ABTEST_PLAYERCORE_MODULE))) {
            this.mMediaContext.mMediaPlayContext.setPlayerType(1);
        } else {
            this.mMediaContext.mMediaPlayContext.setPlayerType(3);
        }
        if (MediaAdapteManager.mConfigAdapter != null && AndroidUtils.isInList(Build.MODEL, MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaContext.mMediaPlayContext.mConfigGroup, "ijkPlayerBlackList", ""))) {
            this.mMediaContext.mMediaPlayContext.setPlayerType(3);
        }
        if (MediaAdapteManager.mConfigAdapter != null) {
            String keyStr = this.mMediaContext.mMediaPlayContext.mBusinessId;
            if (!TextUtils.isEmpty(this.mMediaContext.mMediaPlayContext.mFrom)) {
                keyStr = keyStr + "-" + this.mMediaContext.mMediaPlayContext.mFrom;
            }
            this.mMediaContext.mMediaPlayContext.setPlayerType(AndroidUtils.getPlayerCore(MediaAdapteManager.mConfigAdapter, MediaConstant.DW_ORANGE_GROUP_NAME, keyStr, this.mMediaContext.mMediaPlayContext.getPlayerType()));
        }
        if (Build.VERSION.SDK_INT <= 18 && this.mMediaContext.mMediaPlayContext.getPlayerType() == 3 && MediaAdapteManager.mConfigAdapter != null && AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaContext.mMediaPlayContext.mConfigGroup, "AndroidJBUseIJK", "true"))) {
            this.mMediaContext.mMediaPlayContext.setPlayerType(1);
        }
        if (this.mMediaContext.mMediaPlayContext.getPlayerType() == 2 && this.mVideoPath.contains(".m3u8")) {
            this.mMediaContext.mMediaPlayContext.setPlayerType(1);
        }
        if (TextUtils.isEmpty(this.mVideoPath) || !this.mVideoPath.contains(TaoLiveVideoView.TBLIVE_ARTP_SCHEMA) || MediaAdapteManager.mConfigAdapter == null || AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter.getConfig(TaoLiveVideoView.TBLIVE_ORANGE_GROUP, "ARTPUseIJK", "true"))) {
        }
        config.mConfigGroup = this.mMediaContext.mMediaPlayContext.mConfigGroup;
        config.mPlayerType = this.mMediaContext.mMediaPlayContext.getPlayerType();
        config.mScenarioType = this.mMediaContext.mMediaPlayContext.mTBLive ? this.mMediaContext.mScenarioType : 2;
        config.mUserId = this.mMediaContext.mUserId;
        config.mAccountId = this.mMediaContext.mMediaPlayContext.mAccountId;
        config.mSubBusinessType = this.mMediaContext.mMediaPlayContext.mFrom;
        config.mFeedId = this.mMediaContext.mMediaPlayContext.mVideoId;
        config.mVideoDefinition = this.mMediaContext.mMediaPlayContext.getVideoDefinition();
        config.mRateAdapte = this.mMediaContext.mMediaPlayContext.getRateAdapte();
        config.mEdgePcdn = this.mMediaContext.mMediaPlayContext.getEdgePcdn();
        config.mVideoSource = this.mMediaContext.mMediaPlayContext.getVideoSource();
        config.mCacheKey = this.mMediaContext.mMediaPlayContext.getCacheKey();
        config.mSVCEnable = this.mMediaContext.mMediaPlayContext.mSVCEnable;
        config.mDropFrameForH265 = this.mMediaContext.mMediaPlayContext.mTBLive ? this.mMediaContext.mMediaPlayContext.mDropFrameForH265 : false;
        config.mLowQualityUrl = this.mMediaContext.mMediaPlayContext.mLowQualityUrl;
        if (!TextUtils.isEmpty(this.mMediaContext.mMediaPlayContext.getHighCachePath())) {
            config.mHighCachePath = this.mMediaContext.mMediaPlayContext.getHighCachePath();
            config.mVideoDefinition = this.mMediaContext.mMediaPlayContext.mHighVideoDefinition;
        }
        config.mbEnableTBNet = this.mMediaContext.mMediaPlayContext.isUseTBNet();
        if (this.mMediaContext.mMediaPlayContext.mTBLive) {
            config.mDecoderTypeH265 = (!this.mMediaContext.mMediaPlayContext.isHardwareHevc() || !ApplicationUtils.bUseMediacodecForLive) ? 0 : 1;
            config.mDecoderTypeH264 = (!this.mMediaContext.mMediaPlayContext.isHardwareAvc() || !ApplicationUtils.bUseMediacodecForLive) ? 0 : 1;
        } else {
            config.mDecoderTypeH265 = this.mMediaContext.mMediaPlayContext.isHardwareHevc() ? 1 : 0;
            config.mDecoderTypeH264 = this.mMediaContext.mMediaPlayContext.isHardwareAvc() ? 1 : 0;
        }
        config.mMediaSourceType = this.mMediaContext.mMediaPlayContext.mMediaSourceType;
        config.mSelectedUrlName = this.mMediaContext.mMediaPlayContext.mSelectedUrlName;
        config.mNewBundleSdk = true;
        config.mPlayToken = this.mMediaContext.mPlayToken;
        config.mDeviceLevel = this.mMediaContext.mMediaPlayContext.getDevicePerformanceLevel() + "/runtimeLevel:" + this.mMediaContext.mMediaPlayContext.mRuntimeLevel;
        if (MediaAdapteManager.mConfigAdapter != null && AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaContext.mMediaPlayContext.mConfigGroup, "videoLengthEnable", "true")) && this.mMediaContext.mMediaPlayContext.getVideoLength() > 0 && this.mMediaContext.mMediaPlayContext.getVideoLength() < 262144000) {
            config.mVideoLength = this.mMediaContext.mMediaPlayContext.getVideoLength();
        }
        config.mNetSpeed = this.mMediaContext.mMediaPlayContext.getNetSpeed();
        config.mbEnableDeviceMeasure = this.mMediaContext.mMediaPlayContext.isVideoDeviceMeaseureEnable();
        config.mHighPerformance = this.mMediaContext.mMediaPlayContext.mHighPerformancePlayer;
        config.mExpectedVideoInfo = this.mMediaContext.mMediaPlayContext.getRateAdaptePriority() + Constant.INTENT_JSON_MARK + (this.mMediaContext.mMediaPlayContext.isH265() ? MediaConstant.H265 : MediaConstant.H264);
        Map<String, String> utParams = this.mMediaContext.getUTParams();
        if (utParams != null) {
            String productType = utParams.get("product_type");
            if (!TextUtils.isEmpty(productType)) {
                config.mVideoChannel = productType;
            }
        }
        if (this.mMediaContext != null && this.mMediaContext.mMediaPlayContext.getPlayerType() == 1) {
            try {
                if (this.mMediaContext == null || MediaAdapteManager.mConfigAdapter == null) {
                    mediaPlayer = new IjkMediaPlayer(this.mContext);
                } else {
                    mediaPlayer = new IjkMediaPlayer(this.mContext, MediaAdapteManager.mConfigAdapter);
                }
            } catch (Throwable e) {
                if (this.mMediaContext != null) {
                    DWLogUtils.e(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "initPlayer##IjkMediaPlayer load error:" + e.getMessage());
                }
                releaseForInit((AbstractMediaPlayer) null);
                mediaPlayer = degradeMediaPlayer(config);
            }
        } else if (this.mMediaContext == null || this.mMediaContext.mMediaPlayContext.getPlayerType() != 3) {
            mediaPlayer = degradeMediaPlayer(config);
        } else {
            try {
                if (this.mMediaContext == null || MediaAdapteManager.mConfigAdapter == null) {
                    mediaPlayer = new TaobaoMediaPlayer(this.mContext);
                } else {
                    mediaPlayer = new TaobaoMediaPlayer(this.mContext, MediaAdapteManager.mConfigAdapter);
                }
            } catch (Throwable e2) {
                if (this.mMediaContext != null) {
                    DWLogUtils.e(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "initPlayer##TaobaoMediaPlayer load error:" + e2.getMessage());
                }
                releaseForInit((AbstractMediaPlayer) null);
                mediaPlayer = degradeMediaPlayer(config);
            }
        }
        try {
            prepareAysnc(mediaPlayer, config);
        } catch (Throwable th) {
            DWLogUtils.e(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "initPlayer##backup mediaplayer it error:");
            notifyVideoErrorForInit();
        }
        return mediaPlayer;
    }

    private void bindSurfaceHolder(IMediaPlayer mp, IMediaRenderView.ISurfaceHolder holder) {
        if (mp != null) {
            if (holder == null) {
                mp.setSurface((Surface) null);
            } else {
                holder.bindToMediaPlayer(mp);
            }
        }
    }

    private void releaseHolderSurface(IMediaRenderView.ISurfaceHolder holder) {
        if (holder != null && holder.getSurface() != null && Build.VERSION.SDK_INT < SDK_INT_FOR_OPTIMIZE) {
            holder.getSurface().release();
        }
    }

    private void releaseForInit(AbstractMediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Throwable th) {
            }
        }
    }

    private void notifyVideoErrorForInit() {
        try {
            notifyVideoError((IMediaPlayer) null, 1, 0);
        } catch (Throwable th) {
        }
    }

    private AbstractMediaPlayer degradeMediaPlayer(TaoLiveVideoViewConfig config) {
        AbstractMediaPlayer mediaPlayer;
        if (this.mMediaContext == null || MediaAdapteManager.mConfigAdapter == null) {
            mediaPlayer = new NativeMediaPlayer(this.mContext);
        } else {
            mediaPlayer = new NativeMediaPlayer(this.mContext, MediaAdapteManager.mConfigAdapter);
        }
        this.mMediaContext.mMediaPlayContext.setHardwareAvc(true);
        this.mMediaContext.mMediaPlayContext.setHardwareHevc(true);
        config.mDecoderTypeH265 = 1;
        config.mDecoderTypeH264 = 1;
        this.mMediaContext.mMediaPlayContext.setPlayerType(2);
        if (!TextUtils.isEmpty(this.mMediaContext.mMediaPlayContext.getBackupVideoUrl()) && this.mMediaContext.mMediaPlayContext.isH265()) {
            this.mVideoPath = this.mMediaContext.mMediaPlayContext.getBackupVideoUrl();
            this.mMediaContext.mMediaPlayContext.setVideoUrl(this.mVideoPath);
            this.mMediaContext.mMediaPlayContext.setVideoDefinition(this.mMediaContext.mMediaPlayContext.getBackupVideoDefinition());
            this.mMediaContext.mMediaPlayContext.setCacheKey(this.mMediaContext.mMediaPlayContext.getBackupCacheKey());
            if (config != null) {
                config.mVideoDefinition = this.mMediaContext.mMediaPlayContext.getBackupVideoDefinition();
                config.mCacheKey = this.mMediaContext.mMediaPlayContext.getBackupCacheKey();
            }
        }
        return mediaPlayer;
    }

    private void prepareAysnc(AbstractMediaPlayer mediaPlayer, TaoLiveVideoViewConfig config) throws Throwable {
        int lowDeviceFirstRenderTime;
        if (this.mMediaContext.mMediaPlayContext.mTBLive && !this.mMediaContext.mMediaPlayContext.isLowPerformance() && config.mScenarioType == 0 && Build.VERSION.SDK_INT >= 21) {
            if (AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter != null ? MediaAdapteManager.mConfigAdapter.getConfig("MediaLive", MonitorMediaPlayer.PCDN_FOR_LIVE_ENABLE, "false") : "false")) {
                ApplicationUtils.pcdnStartOnce();
            }
        }
        ((MonitorMediaPlayer) mediaPlayer).setTlogAdapter(this.mMediaContext.mMediaPlayContext.mTLogAdapter);
        ((MonitorMediaPlayer) mediaPlayer).setConfig(config);
        ((MonitorMediaPlayer) mediaPlayer).setExtInfo(this.mExtInfo);
        ((MonitorMediaPlayer) mediaPlayer).setFirstRenderAdapter(this);
        ((MonitorMediaPlayer) mediaPlayer).setABtestAdapter(MediaAdapteManager.mABTestAdapter);
        ((MonitorMediaPlayer) mediaPlayer).setNetworkUtilsAdapter(MediaAdapteManager.mMediaNetworkUtilsAdapter);
        mediaPlayer.registerOnPreparedListener(this);
        mediaPlayer.setOnVideoSizeChangedListener(this);
        mediaPlayer.registerOnCompletionListener(this);
        mediaPlayer.registerOnErrorListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.registerOnInfoListener(this);
        mediaPlayer.registerOnLoopCompletionListener(this);
        if (mediaPlayer instanceof NativeMediaPlayer) {
            ((NativeMediaPlayer) mediaPlayer).setAudioStreamType(3);
        } else if (mediaPlayer instanceof IjkMediaPlayer) {
            ((IjkMediaPlayer) mediaPlayer)._setPropertyLong(IjkMediaPlayer.FFP_PROP_INT64_FIRST_FRAME_RENDERING_OPT, 1);
            if (this.mMediaContext.mMediaPlayContext.mTBLive && config != null && config.mScenarioType == 0 && this.mMediaContext.mMediaPlayContext.isLowPerformance() && MediaAdapteManager.mConfigAdapter != null && (lowDeviceFirstRenderTime = AndroidUtils.parseInt(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaContext.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_LOW_DEVICE_FIRST_RENDER_TIME, "100"))) >= 50 && lowDeviceFirstRenderTime < 800 && this.mMediaContext.mMediaPlayContext.getLowDeviceFirstRender()) {
                ((IjkMediaPlayer) mediaPlayer)._setPropertyLong(IjkMediaPlayer.FFP_PROP_FIRSTPLAY_NEED_TIME, (long) lowDeviceFirstRenderTime);
                ((IjkMediaPlayer) mediaPlayer)._setPropertyLong(IjkMediaPlayer.FFP_PROP_MIN_SIZE_OR_TIME_FIRST_RENDER_OPT, 1);
            }
            if (this.mMediaContext.mMediaPlayContext.getAvdataBufferedMaxMBytes() > 0 && 15728640 > this.mMediaContext.mMediaPlayContext.getAvdataBufferedMaxMBytes()) {
                ((IjkMediaPlayer) mediaPlayer)._setPropertyLong(40001, (long) this.mMediaContext.mMediaPlayContext.getAvdataBufferedMaxMBytes());
                config.mVideoPlayBufferMsg = "initMaxBuffer:" + this.mMediaContext.mMediaPlayContext.getAvdataBufferedMaxMBytes() + WVNativeCallbackUtil.SEPERATER + "maxLevel:" + this.mMediaContext.mMediaPlayContext.getMaxLevel() + WVNativeCallbackUtil.SEPERATER + "currentLevel:" + this.mMediaContext.mMediaPlayContext.getCurrentLevel();
            }
            if (this.mMediaContext.mMediaPlayContext.mHighPerformancePlayer && this.mMediaContext.isMute() && this.mMediaContext.mMediaPlayContext.mBackgroundMode) {
                ((IjkMediaPlayer) mediaPlayer)._setPropertyLong(50001, 1);
            }
            if (this.mTargetState == 1) {
                ((IjkMediaPlayer) mediaPlayer)._setOption(4, "start-on-prepared", 1);
            }
            if (config.mScenarioType == 1) {
                ((IjkMediaPlayer) mediaPlayer)._setPropertyFloat(21009, 1.2f);
            }
            if (config.mScenarioType == 1) {
                ((IjkMediaPlayer) mediaPlayer)._setPropertyFloat(IjkMediaPlayer.FFP_PROP_AUDIO_SLOWSPEED, 0.8f);
            }
            if (AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter != null ? MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaContext.mMediaPlayContext.mConfigGroup, MediaConstant.TBLIVE_ORANGE_SENDSEI, "false") : "false")) {
                ((IjkMediaPlayer) mediaPlayer)._setPropertyLong(20111, 1);
            }
        } else if (mediaPlayer instanceof TaobaoMediaPlayer) {
            if (AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter != null ? MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaContext.mMediaPlayContext.mConfigGroup, MediaConstant.TBLIVE_ORANGE_SENDSEI, "false") : "false")) {
                ((TaobaoMediaPlayer) mediaPlayer)._setPropertyLong(20111, 1);
            }
            if (this.mTargetState != 1) {
                ((TaobaoMediaPlayer) mediaPlayer)._setPropertyLong(TaobaoMediaPlayer.FFP_PROP_INT64_START_ON_PREPARED, 0);
            }
            if (this.mMediaContext.mMediaPlayContext.mTBLive && config != null && config.mScenarioType == 0 && this.mMediaContext.mMediaPlayContext.isLowPerformance() && MediaAdapteManager.mConfigAdapter != null) {
                int lowDeviceFirstVideoCount = AndroidUtils.parseInt(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaContext.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_LOW_DEVICE_FIRST_VIDEO_COUNT, WVPackageMonitorInterface.NOT_INSTALL_FAILED));
                int lowDeviceFirstAudioCount = AndroidUtils.parseInt(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaContext.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_LOW_DEVICE_FIRST_AUDIO_COUNT, "36"));
                if (this.mMediaContext.mMediaPlayContext.getLowDeviceFirstRender()) {
                    if (lowDeviceFirstVideoCount >= 5 && lowDeviceFirstVideoCount < 20) {
                        ((TaobaoMediaPlayer) mediaPlayer)._setPropertyLong(TaobaoMediaPlayer.FFP_PROP_INT64_SOURCER_VIDEO_PIPE_START_COUNT, (long) lowDeviceFirstVideoCount);
                    }
                    if (lowDeviceFirstAudioCount >= 9 && lowDeviceFirstAudioCount < 36) {
                        ((TaobaoMediaPlayer) mediaPlayer)._setPropertyLong(TaobaoMediaPlayer.FFP_PROP_INT64_SOURCER_AUDIO_PIPE_START_COUNT, (long) lowDeviceFirstAudioCount);
                    }
                }
            }
        }
        if (this.mPropertyLong != null) {
            for (int i = 0; i < this.mPropertyLong.size(); i++) {
                int key = this.mPropertyLong.keyAt(i);
                Long value = (Long) this.mPropertyLong.valueAt(i);
                if (mediaPlayer instanceof IjkMediaPlayer) {
                    ((IjkMediaPlayer) mediaPlayer)._setPropertyLong(key, value != null ? value.longValue() : 0);
                } else if (mediaPlayer instanceof TaobaoMediaPlayer) {
                    ((TaobaoMediaPlayer) mediaPlayer)._setPropertyLong(key, value != null ? value.longValue() : 0);
                }
            }
            this.mPropertyLong.clear();
        }
        if (this.mPropertyFloat != null) {
            for (int i2 = 0; i2 < this.mPropertyFloat.size(); i2++) {
                int key2 = this.mPropertyFloat.keyAt(i2);
                Float value2 = (Float) this.mPropertyFloat.valueAt(i2);
                if (mediaPlayer instanceof IjkMediaPlayer) {
                    ((IjkMediaPlayer) mediaPlayer)._setPropertyFloat(key2, value2 != null ? value2.floatValue() : 0.0f);
                } else if (mediaPlayer instanceof TaobaoMediaPlayer) {
                    ((TaobaoMediaPlayer) mediaPlayer)._setPropertyFloat(key2, value2 != null ? value2.floatValue() : 0.0f);
                }
            }
            this.mPropertyFloat.clear();
        }
        if (this.mMediaPlayerRecycler.mVolume == 0.0f) {
            mediaPlayer.setVolume(0.0f, 0.0f);
        } else {
            mediaPlayer.setVolume(BaseVideoView.VOLUME_MULTIPLIER, BaseVideoView.VOLUME_MULTIPLIER);
        }
        mediaPlayer.setDataSource(getVideoPath());
        bindSurfaceHolder(mediaPlayer, getHolder());
        mediaPlayer.setScreenOnWhilePlaying(true);
        if (this.mMediaPlayerRecycler.mRecycled) {
            this.mStartTime = System.currentTimeMillis();
        }
        mediaPlayer.prepareAsync();
    }

    public void release(boolean cleartargetstate) {
        this.mVideoRotationDegree = 0;
        this.mStartForFirstRender = false;
        this.mMediaContext.mMediaPlayContext.mSeekWhenPrepared = 0;
        try {
            if (!(this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null)) {
                this.mMediaPlayerRecycler.mMediaPlayer.resetListeners();
                if ((this.mMediaPlayerRecycler.mMediaPlayer instanceof IjkMediaPlayer) || (this.mMediaPlayerRecycler.mMediaPlayer instanceof TaobaoMediaPlayer)) {
                    final AbstractMediaPlayer mediaPayer = this.mMediaPlayerRecycler.mMediaPlayer;
                    if (this.mMediaPlayerRecycler.mPlayState == 3) {
                        releasePlayer(mediaPayer);
                    } else {
                        new Thread(new Runnable() {
                            public void run() {
                                TextureVideoView.this.releasePlayer(mediaPayer);
                            }
                        }).start();
                    }
                } else {
                    this.mMediaPlayerRecycler.mMediaPlayer.reset();
                    this.mMediaPlayerRecycler.mMediaPlayer.release();
                }
                this.mMediaPlayerRecycler.mMediaPlayer = null;
                this.mMediaPlayerRecycler.mPlayState = 6;
                if (!this.mClosed) {
                    notifyVideoRecycled();
                }
            }
            if (this.mMediaContext.mMediaPlayContext.mTBLive && "LiveRoom".equals(this.mMediaContext.mMediaPlayContext.mFrom) && DeviceBandwidthSampler.getInstance().isSampling()) {
                DeviceBandwidthSampler.getInstance().stopSampling();
            }
        } catch (Throwable th) {
        }
    }

    /* access modifiers changed from: private */
    public void releasePlayer(AbstractMediaPlayer mediaPayer) {
        if (mediaPayer != null) {
            try {
                mediaPayer.stop();
                mediaPayer.release();
            } catch (Throwable e) {
                if (e != null) {
                    DWLogUtils.e(e.getMessage());
                }
            }
        }
    }

    public int getCurrentPosition() {
        if (this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mPlayState == 8 || this.mMediaPlayerRecycler.mPlayState == 6 || this.mMediaPlayerRecycler.mPlayState == 3) {
            return this.mCurrentPosition;
        }
        int currentPosition = (int) (this.mMediaPlayerRecycler.mMediaPlayer == null ? (long) this.mCurrentPosition : this.mMediaPlayerRecycler.mMediaPlayer.getCurrentPosition());
        this.mCurrentPosition = currentPosition;
        return currentPosition;
    }

    public void onLoopCompletion(IMediaPlayer mediaPlayer) {
        notifyVideoLoopComplete();
    }

    public float getAspectRatio() {
        return this.mRenderView.getDisplayAspectRatio();
    }

    public void setVolume(float volume) {
        if (this.mMediaPlayerRecycler.mVolume != volume) {
            this.mMediaPlayerRecycler.mVolume = volume;
            try {
                if (!(this.mMediaPlayerRecycler.mVolume == 0.0f || this.mAudioManager == null || this.mMediaPlayerRecycler.mPlayState == 0 || this.mRequestAudioFocus)) {
                    this.mAudioManager.requestAudioFocus((AudioManager.OnAudioFocusChangeListener) null, 3, 1);
                    this.mRequestAudioFocus = true;
                }
            } catch (Throwable r) {
                if (this.mMediaContext != null) {
                    DWLogUtils.e(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "setVolume##RequestAudioFocus error" + r.getMessage());
                }
            }
            if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null && this.mMediaPlayerRecycler.mPlayState != 6 && this.mMediaPlayerRecycler.mPlayState != 3) {
                try {
                    this.mMediaPlayerRecycler.mMediaPlayer.setVolume(volume, volume);
                } catch (Throwable r2) {
                    if (this.mMediaContext != null) {
                        DWLogUtils.e(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "setVolume##SetVolume error" + r2.getMessage());
                    }
                }
            }
        }
    }

    public void setSysVolume(float volume) {
        try {
            if (this.mAudioManager != null && this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mPlayState != 6 && this.mMediaPlayerRecycler.mPlayState != 3) {
                this.mAudioManager.setStreamVolume(3, (int) volume, 4);
            }
        } catch (Throwable r) {
            if (this.mMediaContext != null) {
                DWLogUtils.e(this.mMediaContext.mMediaPlayContext.mTLogAdapter, "setSysVolume##SetStreamVolume error" + r.getMessage());
            }
        }
    }

    public void close() {
        this.mStartTime = 0;
        if (!this.mClosed) {
            this.mClosed = true;
            clearKeepScreenOn();
            if (this.mHandler != null) {
                this.mHandler.removeCallbacksAndMessages((Object) null);
            }
            try {
                if (this.mAudioManager != null && this.mRequestAudioFocus) {
                    this.mAudioManager.abandonAudioFocus((AudioManager.OnAudioFocusChangeListener) null);
                }
            } catch (Throwable th) {
            }
            if (!(this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null)) {
                unregisterMediaplayerListener(this.mMediaPlayerRecycler.mMediaPlayer);
            }
            if (!TextUtils.isEmpty(this.mReuseToken) && 1 == this.mMediaPlayerRecycler.mPlayState) {
                pauseVideo(true);
            }
            if (this.mMediaContext.mMediaPlayContext.mTBLive) {
                MediaLivePlayerManager.getInstance().removePlayerFromCache(this.mMediaPlayerRecycler.mToken, this);
            } else {
                MediaPlayerManager.getInstance().removePlayerFromCache(this.mMediaPlayerRecycler.mToken, this);
            }
            this.mMediaPlayerRecycler.mLastPosition = 0;
            this.mTargetState = 0;
        }
    }

    public boolean isAvailable() {
        return this.mRenderView.isAvailable();
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                updateProgress();
                if (!(getVideoState() == 3 || getVideoState() == 6 || getVideoState() == 6 || getVideoState() == 4 || this.mHandler == null)) {
                    this.mHandler.sendEmptyMessageDelayed(0, (long) UPDATE_PROGRESS_TIME);
                    break;
                }
        }
        return false;
    }

    private void updateProgress() {
        int currentPosition;
        if (isAvailable() && this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mPlayState == 1 && (currentPosition = getCurrentPosition()) >= 0) {
            int duration = getDuration();
            int secondaryProgress = 0;
            if (duration > 0) {
                secondaryProgress = getVideoBufferPercent();
            }
            notifyVideoTimeChanged(currentPosition, secondaryProgress, duration);
        }
    }

    public boolean isCompleteHitCache() {
        return this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null && (this.mMediaPlayerRecycler.mMediaPlayer instanceof MonitorMediaPlayer) && ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).isCompleteHitCache();
    }

    public boolean isUseCache() {
        return this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null && (this.mMediaPlayerRecycler.mMediaPlayer instanceof MonitorMediaPlayer) && ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).isUseVideoCache();
    }

    public boolean isHitCache() {
        return this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null && (this.mMediaPlayerRecycler.mMediaPlayer instanceof MonitorMediaPlayer) && ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).isHitCache();
    }

    public void setPlayRate(float playRate) {
        if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
            this.mMediaPlayerRecycler.mMediaPlayer.setPlayRate(playRate);
        }
    }

    public void setAspectRatio(MediaAspectRatio mediaAspectRatio) {
        if (this.mRenderView != null && mediaAspectRatio != null) {
            switch (mediaAspectRatio) {
                case DW_FIT_CENTER:
                    this.mRenderView.setAspectRatio(0);
                    return;
                case DW_CENTER_CROP:
                    this.mRenderView.setAspectRatio(1);
                    return;
                case DW_FIT_X_Y:
                    this.mRenderView.setAspectRatio(3);
                    return;
                default:
                    return;
            }
        }
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
        if (this.mContext == activity) {
            this.mStartTime = System.currentTimeMillis();
            if (!this.mMediaContext.mMediaPlayContext.mTBLive) {
                this.mActivityAvailable = true;
                if (this.mMediaPlayerRecycler == null || !this.mMediaPlayerRecycler.mRecycled) {
                    if (!(this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null || !this.mMediaPlayerRecycler.mLastPausedState || !this.mVideoStarted || this.mMediaPlayerRecycler.mPlayState == 4)) {
                        playVideo();
                    }
                    if (this.mMediaContext == null) {
                        return;
                    }
                    if ((this.mMediaContext.screenType() == MediaPlayScreenType.PORTRAIT_FULL_SCREEN || this.mMediaContext.screenType() == MediaPlayScreenType.LANDSCAPE_FULL_SCREEN) && (this.mMediaContext.getContext() instanceof Activity)) {
                        DWViewUtil.hideNavigationBar(this.mMediaContext.getWindow() == null ? ((Activity) this.mMediaContext.getContext()).getWindow() : this.mMediaContext.getWindow());
                        return;
                    }
                    return;
                }
                if (isLastPausedState() && this.mMediaPlayerRecycler.mLastState == 2) {
                    this.mMediaPlayerRecycler.mLastState = 1;
                }
                if (this.mMediaPlayerRecycler.mLastState != 1) {
                    return;
                }
                if ((!this.mMediaContext.mMediaPlayContext.mTBLive && MediaPlayerManager.getInstance().resumeLruMediaPlayerAvailable()) || (this.mMediaContext.mMediaPlayContext.mTBLive && MediaLivePlayerManager.getInstance().resumeLruMediaPlayerAvailable())) {
                    initMediaPlayerAfterRecycle();
                }
            }
        }
    }

    public void onActivityPaused(Activity activity) {
        this.mStartTime = 0;
        if (this.mContext == activity && !this.mMediaContext.mMediaPlayContext.mTBLive) {
            this.mActivityAvailable = false;
            if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
                if (this.mMediaPlayerRecycler.mPlayState == 1 || this.mMediaPlayerRecycler.mPlayState == 5) {
                    pauseVideo(true);
                }
            }
        }
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public long getStartTime() {
        return this.mStartTime;
    }
}
