package com.yunos.tv.tvsdk.media.view;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.yunos.adoplayer.aidl.InfoExtend;
import com.yunos.tv.blitz.utils.NetworkUtil;
import com.yunos.tv.sdk.lib.http.HttpConstant;
import com.yunos.tv.tvsdk.base.WorkAsyncTask;
import com.yunos.tv.tvsdk.media.IMediaPlayer;
import com.yunos.tv.tvsdk.media.data.MTopInfoBase;
import com.yunos.tv.tvsdk.media.data.MediaMTopDao;
import com.yunos.tv.tvsdk.media.data.MediaMTopParams;
import com.yunos.tv.tvsdk.media.data.MediaType;
import com.yunos.tv.tvsdk.media.error.ErrorDetail;
import com.yunos.tv.tvsdk.media.error.ErrorType;
import com.yunos.tv.tvsdk.media.error.IMediaError;
import com.yunos.tv.tvsdk.media.view.IBaseVideo;
import com.yunos.tv.tvsdk.media.view.IVideo;
import java.lang.ref.WeakReference;

public class YunosVideoView extends FrameLayout implements IVideo, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener, IBaseVideo.OnAudioInfoListener, IBaseVideo.OnAdRemainTimeListener, SurfaceHolder.Callback, IBaseVideo.OnThrowableCallback, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnInfoExtendListener, IBaseVideo.OnFirstFrameListener, IBaseVideo.VideoRequestTsListener, IBaseVideo.VideoHttpDnsListener, IBaseVideo.OnDefinitionChangedListener, IBaseVideo.OnSkipHeadTailInfoListener, IBaseVideo.OnPreviewInfoListener {
    private static final int MESSAGE_PREPARE_TIMEOUT = 1;
    private static final int PREPARING_DEFAULT_TIMEOUT = 30000;
    private static final String TAG = "YunosVideoView";
    private static final int TS_MAX_TIME = 1000;
    public static final int VIDEOVIEW_TYPE_CUSTOM = 2;
    public static final int VIDEOVIEW_TYPE_QIYI = 3;
    public static final int VIDEOVIEW_TYPE_SOHU = 1;
    public static final int VIDEOVIEW_TYPE_YUNOS = 0;
    protected WorkAsyncTask<?> mAsyncTask;
    private int mCurrentHeight = 0;
    private int mCurrentState = 0;
    private int mCurrentWidth = 0;
    private IBaseVideo.OnDefinitionChangedListener mDefinitionChangedListenter;
    private int mDimenMode = 0;
    private int mErrorCode = 0;
    private int mFullHeight = 0;
    private FullScreenChangedListener mFullScreenChangedListener = null;
    private FrameLayout.LayoutParams mFullScreenLayoutParams = null;
    private int mFullWidth = 0;
    private InternalMessageHandler mHandler;
    private boolean mIsCustomError = false;
    private boolean mIsFullScreen = false;
    private boolean mIsPreview;
    private int mMediaType = 2;
    private IBaseVideo.OnAdRemainTimeListener mOnAdRemainTimeListener;
    private IBaseVideo.OnAdRemainTimeListener mOnAdRemainTimeListenerForMediaCenterView;
    private IBaseVideo.OnAudioInfoListener mOnAudioInfoListener;
    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IBaseVideo.OnFirstFrameListener mOnFirstFrameListener;
    private IMediaPlayer.OnInfoExtendListener mOnInfoExtendListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private IVideo.OnPreparingTimeoutListener mOnPreparingTimeoutListener;
    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    private IBaseVideo.OnThrowableCallback mOnThrowableCallback;
    private IBaseVideo.VideoHttpDnsListener mOnVideoHttpDnsListener;
    private IBaseVideo.VideoRequestTsListener mOnVideoRequestTsListener;
    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;
    private IVideo.VideoStateChangeListener mOnVideoStateChangeListener;
    private IVideo.VideoStateChangeListener mOnVideoStateChangeListenerForMediaCenterView;
    private int mOriginHeight = 0;
    private ViewGroup.LayoutParams mOriginLayoutParams = null;
    private int mOriginWidth = 0;
    private ViewGroup mParent;
    private int mPreparingTimeout = 30000;
    private int mPreviewEndTime;
    private IBaseVideo.OnPreviewInfoListener mPreviewInfoListener;
    private int mProgressPercent;
    private SurfaceView mReplaceSurfaceView;
    private ViewGroup mRootView;
    private int mSeekWhenPrepared;
    private IBaseVideo.OnSkipHeadTailInfoListener mSkipHeadTailInfoListener;
    private SurfaceHolder.Callback mSurfaceCallback;
    private int mTargetState = 0;
    private IBaseVideo mVideo;
    private int mVideoBottomMargin = 0;
    private Object[] mVideoInfoParams;
    private int mVideoLeftMargin = 0;
    private int mVideoRightMargin = 0;
    private int mVideoTopMargin = 0;
    private int mVideoViewBgColor = ViewCompat.MEASURED_STATE_MASK;
    private int mVideoViewPosition = 0;
    public int mVideoViewType = 0;
    private boolean mbPrepared = false;

    public interface FullScreenChangedListener {
        void onAfterFullScreen();

        void onAfterUnFullScreen();

        void onBeforeFullScreen();

        void onBeforeUnFullScreen();
    }

    public YunosVideoView(Context context) {
        super(context);
        init(context, (AttributeSet) null);
    }

    public YunosVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public YunosVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mVideoBottomMargin = 0;
        this.mVideoRightMargin = 0;
        this.mVideoTopMargin = 0;
        this.mVideoLeftMargin = 0;
        this.mReplaceSurfaceView = new SurfaceView(getContext());
        addViewInLayout(this.mReplaceSurfaceView, 0, generateDefaultLayoutParams());
        this.mHandler = new InternalMessageHandler(this);
    }

    public void setVideoInfo(Object... params) {
        if (this.mVideo == null) {
            Log.e(TAG, "videoview is not created!");
            return;
        }
        this.mVideoInfoParams = params;
        this.mVideo.setVideoInfo(params);
        setCurrentState(1);
        this.mbPrepared = false;
    }

    public void setMediaPlayerType(int type) {
        Log.i(TAG, "YunosVideoView.setMediaPlayerType.type = " + type);
        this.mMediaType = type;
        if (this.mVideo != null) {
            this.mVideo.setMediaPlayerType(type);
            this.mMediaType = this.mVideo.getMediaPlayerType();
        }
    }

    public int getMediaPlayerType() {
        return this.mMediaType;
    }

    public void start() {
        if (this.mVideo != null && this.mbPrepared) {
            if (this.mVideo.isPlaying()) {
                setCurrentState(3);
            } else {
                this.mVideo.start();
            }
        }
        this.mTargetState = 3;
    }

    public void replay(Object... params) {
        Log.i(TAG, "replay...");
        if (params != null && params.length != 0) {
            if ((this.mVideoViewType == 1 || this.mVideoViewType == 3) && this.mVideo != null) {
                this.mVideo.replay(params);
                this.mbPrepared = false;
                this.mTargetState = 3;
            }
        }
    }

    public void pause() {
        if (isAdPlaying()) {
            Log.d(TAG, "invalid pause! ad is playing");
            return;
        }
        if (this.mVideo != null && this.mVideo.isPlaying()) {
            this.mVideo.pause();
            if (isInPlaybackState() && this.mCurrentState != 6) {
                setCurrentState(4);
            }
        }
        this.mTargetState = 4;
    }

    public void resume() {
        if (this.mVideo != null && !this.mVideo.isPlaying()) {
            this.mIsCustomError = false;
            this.mErrorCode = 0;
            this.mVideo.resume();
            if (this.mVideo.isPlaying()) {
                setCurrentState(3);
            }
        }
        this.mTargetState = 3;
    }

    public void stopPlayback() {
        this.mbPrepared = false;
        cancelMTopInfo();
        if (this.mVideo != null) {
            this.mVideo.stopPlayback();
            setCurrentState(0);
            this.mTargetState = 0;
        }
        if (this.mHandler != null) {
            this.mHandler.removeMessages(1);
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
    }

    public void release() {
        this.mbPrepared = false;
        cancelMTopInfo();
        if (this.mReplaceSurfaceView != null) {
            this.mReplaceSurfaceView.getHolder().getSurface().release();
        }
        if (this.mVideo != null) {
            setCurrentState(0);
            this.mTargetState = 0;
            this.mVideo.release();
        }
        if (this.mHandler != null) {
            this.mHandler.removeMessages(1);
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
    }

    public int getDuration() {
        if (isInPlaybackState()) {
            return this.mVideo.getDuration();
        }
        return -1;
    }

    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return this.mVideo.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int pos) {
        this.mSeekWhenPrepared = pos;
        if (this.mVideo == null || !this.mbPrepared) {
            Log.w(TAG, "seeTo invoke before videoView is created!");
            return;
        }
        int duration = getDuration();
        if (duration <= 0 || isAdoPlayer()) {
            Log.d(TAG, "seek to duration <= 0");
        } else if (pos >= duration - 1000) {
            pos = duration - 1000;
        }
        this.mVideo.seekTo(pos);
        this.mSeekWhenPrepared = 0;
        this.mTargetState = 3;
        Log.d(TAG, "isInPlaybackState:" + isInPlaybackState() + ",duration:" + duration);
        if (!isInPlaybackState() || duration > 0) {
        }
    }

    public boolean isPlaying() {
        return isInPlaybackState() && this.mVideo.isPlaying();
    }

    public boolean isPause() {
        return isInPlaybackState() && this.mCurrentState == 4;
    }

    public boolean canPause() {
        if (this.mVideo != null) {
            return this.mVideo.canPause();
        }
        return false;
    }

    public boolean canSeekBackward() {
        if (this.mVideo != null) {
            return this.mVideo.canSeekBackward();
        }
        return false;
    }

    public boolean canSeekForward() {
        if (this.mVideo != null) {
            return this.mVideo.canSeekForward();
        }
        return false;
    }

    public boolean isInPlaybackState() {
        return (this.mVideo == null || this.mCurrentState == -1 || this.mCurrentState == 0 || this.mCurrentState == 1) ? false : true;
    }

    public int getCurrentState() {
        return this.mCurrentState;
    }

    public int getTargetState() {
        return this.mTargetState;
    }

    public boolean isCustomError() {
        return this.mIsCustomError;
    }

    public int getAudioType() {
        if (this.mVideo != null) {
            return this.mVideo.getAudioType();
        }
        return -1;
    }

    public long getRadio() {
        if (this.mVideo != null) {
            return this.mVideo.getRadio();
        }
        return 0;
    }

    public long getBitRate() {
        if (this.mVideo != null) {
            return this.mVideo.getBitRate();
        }
        return 0;
    }

    private static class InternalMessageHandler extends Handler {
        private final WeakReference<YunosVideoView> mVideoView;

        public InternalMessageHandler(YunosVideoView videoview) {
            this.mVideoView = new WeakReference<>(videoview);
        }

        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.d(YunosVideoView.TAG, "preparing timeout!");
                    if (this.mVideoView != null && this.mVideoView.get() != null) {
                        ((YunosVideoView) this.mVideoView.get()).dispatchPreparingTimeout();
                        return;
                    }
                    return;
                default:
                    Log.w(YunosVideoView.TAG, "invalid message:" + msg.what);
                    return;
            }
        }
    }

    public void setScreenOnWhilePlaying(boolean isScreenOn) {
        SurfaceHolder holder;
        if (getVideoViewType() != 1 || this.mVideo == null) {
            setKeepScreenOn(isScreenOn);
            SurfaceView surface = getSurfaceView();
            if (surface != null && (holder = surface.getHolder()) != null) {
                holder.setKeepScreenOn(isScreenOn);
                return;
            }
            return;
        }
        Log.w(TAG, "YunosVideoView.setScreenOnWhilePlaying.not support sohu video!");
    }

    public void setCurrentState(int state) {
        Log.d(TAG, "YunosVideoView.setCurrentState.state:" + state + " mCurrentState = " + this.mCurrentState);
        this.mHandler.removeMessages(1);
        if (state == 1 && this.mCurrentState == state) {
            Log.d(TAG, "send timeout delay");
            this.mHandler.sendEmptyMessageDelayed(1, (long) this.mPreparingTimeout);
        } else if (this.mCurrentState != state) {
            this.mCurrentState = state;
            if (this.mOnVideoStateChangeListenerForMediaCenterView != null) {
                this.mOnVideoStateChangeListenerForMediaCenterView.onStateChange(state);
            }
            if (this.mOnVideoStateChangeListener != null) {
                this.mOnVideoStateChangeListener.onStateChange(state);
            }
        }
    }

    public void setVideoView(IBaseVideo video) {
        if (video == null) {
            Log.e(TAG, "video is null!");
            return;
        }
        if (this.mVideo == video && this.mVideoViewType != 1) {
            Log.w(TAG, "same video!");
        }
        if (this.mVideo != null) {
            Log.w(TAG, "VideoView already set,remove prev video");
            setVideoListener((YunosVideoView) null);
            this.mVideo.stopPlayback();
            removeView(this.mVideo.getPlayerView());
        }
        this.mVideo = video;
        if (!(this.mVideo instanceof VideoView)) {
            this.mVideoViewType = 2;
        }
        setVideoListener(this);
        this.mVideo.setStretch(true);
        this.mVideo.setMediaPlayerType(this.mMediaType);
        this.mMediaType = this.mVideo.getMediaPlayerType();
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -1);
        lp.gravity = 17;
        lp.leftMargin = this.mVideoLeftMargin;
        lp.topMargin = this.mVideoTopMargin;
        lp.rightMargin = this.mVideoRightMargin;
        lp.bottomMargin = this.mVideoBottomMargin;
        removeAllViewsInLayout();
        addView(this.mVideo.getPlayerView(), lp);
    }

    public void setVideoListener(YunosVideoView aView) {
        this.mVideo.setOnPreparedListener(aView);
        this.mVideo.setOnErrorListener(aView);
        this.mVideo.setOnInfoListener(aView);
        this.mVideo.setOnInfoExtendListener(aView);
        this.mVideo.setOnCompletionListener(aView);
        this.mVideo.setOnBufferingUpdateListener(aView);
        this.mVideo.setOnSeekCompleteListener(aView);
        this.mVideo.setOnAdRemainTimeListener(aView);
        this.mVideo.setOnAudioInfoListener(aView);
        this.mVideo.setSurfaceCallback(aView);
        this.mVideo.setOnThrowableCallback(aView);
        this.mVideo.setOnVideoSizeChangeListener(aView);
        this.mVideo.setOnFirstFrameListener(aView);
        this.mVideo.setOnVideoHttpDnsListener(aView);
        this.mVideo.setOnVideoRequestTsListener(aView);
        this.mVideo.setSkipHeadTailInfoListener(aView);
        this.mVideo.setDefinitionChangedListener(aView);
        this.mVideo.setPreviewInfoListener(aView);
    }

    public void resetVideoView() {
        if (this.mVideoViewType == 1) {
            Log.d(TAG, "reset sohu videoview!");
            setVideoViewType(1);
        } else if (this.mVideoViewType == 3) {
            Log.d(TAG, "reset sohu videoview!");
            setVideoViewType(3);
        } else {
            Log.d(TAG, "use origin videoview!");
        }
    }

    public void setVideoViewType(int type) {
        IBaseVideo video = null;
        if (type == 0 || type == 1 || type == 3) {
            this.mVideoViewType = type;
        } else {
            this.mVideoViewType = 0;
        }
        if (this.mVideoViewType == 1) {
            Log.w(TAG, "YunosVideoView.setVideoView.not support sohu video!");
        } else if (this.mVideoViewType == 3) {
            Log.w(TAG, "YunosVideoView.setVideoView.not support qiyi video!");
        } else {
            video = new VideoView(getContext());
        }
        setVideoView(video);
    }

    public void setSdkParams(Object... sdkParams) {
        if (sdkParams == null || sdkParams.length <= 0) {
            Log.w(TAG, "invoking YunosVideoView setSdkParsms method, sdkParams is null");
        }
        if (sdkParams.length <= 0 || (sdkParams[0] instanceof String)) {
        }
    }

    public int getVideoViewType() {
        return this.mVideoViewType;
    }

    public void customError(int error, int extend) {
        this.mIsCustomError = true;
        this.mErrorCode = error;
        if (this.mOnErrorListener != null) {
            this.mOnErrorListener.onError((Object) null, 0, 0);
        }
        if (isPlaying()) {
            stopPlayback();
        }
        setCurrentState(-1);
    }

    public boolean isAdoPlayer() {
        if (this.mVideo instanceof IVideo) {
            return ((IVideo) this.mVideo).isAdoPlayer();
        }
        return false;
    }

    public int getProgressPercent() {
        if (isAdoPlayer()) {
            return ((IVideo) this.mVideo).getProgressPercent();
        }
        return this.mProgressPercent;
    }

    public int getErrorcode() {
        return this.mErrorCode;
    }

    public int getErrorExtend() {
        return (this.mVideo != null ? Integer.valueOf(this.mVideo.getErrorExtend()) : null).intValue();
    }

    public InfoExtend getErrorInfoExtend() {
        if (this.mVideo != null) {
            return this.mVideo.getErrorInfoExtend();
        }
        return null;
    }

    public int getAudioSessionId() {
        return 0;
    }

    public int getVideoWidth() {
        if (this.mVideo != null) {
            return this.mVideo.getVideoWidth();
        }
        return 0;
    }

    public int getVideoHeight() {
        if (this.mVideo != null) {
            return this.mVideo.getVideoHeight();
        }
        return 0;
    }

    public View getPlayerView() {
        return null;
    }

    public SurfaceView getSurfaceView() {
        if (this.mVideo != null) {
            return this.mVideo.getSurfaceView();
        }
        return null;
    }

    public void setDefinition(int definition, int currentPosition) {
        if (this.mVideo != null) {
            this.mVideo.setDefinition(definition, currentPosition);
            Log.w(TAG, "setDefinition pos:" + currentPosition);
            this.mSeekWhenPrepared = currentPosition;
            this.mTargetState = 3;
            return;
        }
        Log.w(TAG, "setDefinition,bug mVideo not created!");
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isFullScreen()) {
            return;
        }
        if (this.mOriginWidth != getMeasuredWidth() || this.mOriginHeight != getMeasuredHeight()) {
            this.mOriginWidth = getMeasuredWidth();
            this.mOriginHeight = getMeasuredHeight();
        }
    }

    private void setDimensionByDimenMode() {
        int layoutHeight;
        int pw;
        int ph;
        if (this.mVideo != null) {
            int vWidth = getVideoWidth();
            int vHeight = getVideoHeight();
            int layoutWidth = isFullScreen() ? this.mFullWidth : (this.mOriginWidth - this.mVideoLeftMargin) - this.mVideoRightMargin;
            if (isFullScreen()) {
                layoutHeight = this.mFullHeight;
            } else {
                layoutHeight = (this.mOriginHeight - this.mVideoTopMargin) - this.mVideoBottomMargin;
            }
            if (vWidth > 0 && vHeight > 0 && layoutWidth > 0 && layoutHeight > 0) {
                if (this.mDimenMode == 0) {
                    pw = vWidth;
                    ph = vHeight;
                } else if (this.mDimenMode == 2) {
                    pw = 16;
                    ph = 9;
                } else if (this.mDimenMode == 3) {
                    pw = 4;
                    ph = 3;
                } else {
                    pw = layoutWidth;
                    ph = layoutHeight;
                }
                int[] size = getLargestSizeByProportion(layoutWidth, layoutHeight, pw, ph);
                int width = size[0];
                int height = size[1];
                if (width <= 0 || height <= 0) {
                    width = layoutWidth;
                    height = layoutHeight;
                }
                Log.d(TAG, "layoutsize(" + layoutWidth + "," + layoutHeight + "),video(" + width + "," + height + "),pw:" + pw + ",ph:" + ph + ",size:" + size[0] + "," + size[1]);
                setDimension(width, height);
            }
        }
    }

    private int[] getLargestSizeByProportion(int width, int height, int pw, int ph) {
        if (((long) (width * ph)) > ((long) (height * pw))) {
            return new int[]{(int) (((long) (height * pw)) / ((long) ph)), height};
        }
        return new int[]{width, (int) (((long) (width * ph)) / ((long) pw))};
    }

    public void setStretch(boolean isStreach) {
        if (this.mVideo != null) {
            this.mVideo.setStretch(true);
        }
    }

    public void setDimension(int width, int height) {
        Log.d(TAG, "setDimension:width:" + width + ",height:" + height);
        if (width == this.mCurrentWidth && height == this.mCurrentHeight) {
            Log.d(TAG, "same size!");
        } else if (this.mVideo != null) {
            this.mCurrentWidth = width;
            this.mCurrentHeight = height;
            this.mVideo.setDimension(width, height);
        }
    }

    public void setDimensionOrigin() {
        if (this.mDimenMode != 0) {
            this.mDimenMode = 0;
            setDimensionByDimenMode();
        }
    }

    public void setDimensionFull() {
        if (this.mDimenMode != 1) {
            this.mDimenMode = 1;
            setDimensionByDimenMode();
        }
    }

    public void setDimension_16_9() {
        if (this.mDimenMode != 2) {
            this.mDimenMode = 2;
            setDimensionByDimenMode();
        }
    }

    public void setDimension_4_3() {
        if (this.mDimenMode != 3) {
            this.mDimenMode = 3;
            setDimensionByDimenMode();
        }
    }

    public void setPauseADTopMarginPercent(int topMarginPercent) {
        if (this.mVideo != null) {
            this.mVideo.setPauseADTopMarginPercent(topMarginPercent);
        }
    }

    public void hidePauseAd() {
        if (this.mVideo != null) {
            this.mVideo.hidePauseAd();
        }
    }

    public boolean isAdPlaying() {
        if (this.mVideo != null) {
            return this.mVideo.isAdPlaying();
        }
        return false;
    }

    public int getAdRemainTime() {
        if (this.mVideo != null) {
            return this.mVideo.getAdRemainTime();
        }
        return -1;
    }

    public void onActivityResume(Activity activity) {
        if (this.mVideo != null) {
            this.mVideo.onActivityResume(activity);
            if (this.mVideoViewType == 1 || this.mVideoViewType == 3) {
                this.mbPrepared = false;
                this.mTargetState = 3;
            }
        }
    }

    public void onActivityStop(Activity activity) {
        stopPlayback();
    }

    public void setOnAudioInfoListener(IBaseVideo.OnAudioInfoListener l) {
        this.mOnAudioInfoListener = l;
    }

    public void onAudioInfo(int audioType) {
        Log.d(TAG, "onAudioInfo:" + audioType);
        if (this.mOnAudioInfoListener != null) {
            this.mOnAudioInfoListener.onAudioInfo(audioType);
        }
    }

    public void setOnVideoStateChangeListener(IVideo.VideoStateChangeListener l) {
        this.mOnVideoStateChangeListener = l;
    }

    public void setOnVideoSizeChangeListener(IMediaPlayer.OnVideoSizeChangedListener l) {
        this.mOnVideoSizeChangedListener = l;
    }

    public void throwableCallBack(Throwable throwable) {
        if (this.mOnThrowableCallback != null) {
            this.mOnThrowableCallback.throwableCallBack(throwable);
        }
    }

    public void setOnThrowableCallback(IBaseVideo.OnThrowableCallback l) {
        this.mOnThrowableCallback = l;
    }

    public void setOnFirstFrameListener(IBaseVideo.OnFirstFrameListener l) {
        this.mOnFirstFrameListener = l;
    }

    public void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener l) {
        this.mOnBufferingUpdateListener = l;
    }

    public void setOnInfoExtendListener(IMediaPlayer.OnInfoExtendListener l) {
        this.mOnInfoExtendListener = l;
    }

    public void setOnVideoStateChangeListenerForMediaCenterview(IVideo.VideoStateChangeListener l) {
        this.mOnVideoStateChangeListenerForMediaCenterView = l;
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        this.mOnPreparedListener = l;
    }

    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
        this.mOnErrorListener = l;
    }

    public void setOnInfoListener(IMediaPlayer.OnInfoListener l) {
        this.mOnInfoListener = l;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout changed==" + changed + " mOriginWidth=" + this.mOriginWidth + " getMeasuredWidth=" + getMeasuredWidth());
        if (changed && !isFullScreen()) {
            setDimensionByDimenMode();
        }
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        this.mOnCompletionListener = l;
    }

    public void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener l) {
        this.mOnSeekCompleteListener = l;
    }

    public void setOnAdRemainTimeListenerForMediaCenterView(IBaseVideo.OnAdRemainTimeListener l) {
        this.mOnAdRemainTimeListenerForMediaCenterView = l;
    }

    public void setOnAdRemainTimeListener(IBaseVideo.OnAdRemainTimeListener l) {
        this.mOnAdRemainTimeListener = l;
    }

    public void dispatchPreparingTimeout() {
        Log.d(TAG, "dispatchPreparingTimeout");
        if (this.mOnPreparingTimeoutListener == null || !this.mOnPreparingTimeoutListener.preparingTimeout()) {
            setVideoInfo(this.mVideoInfoParams);
        }
    }

    public void setPreparingTimeout(int timeout) {
        this.mPreparingTimeout = timeout;
    }

    public void setOnPreaparingTimeout(IVideo.OnPreparingTimeoutListener l) {
        this.mOnPreparingTimeoutListener = l;
    }

    public void setSurfaceCallback(SurfaceHolder.Callback surfaceCallback) {
        this.mSurfaceCallback = surfaceCallback;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (this.mSurfaceCallback != null) {
            this.mSurfaceCallback.surfaceCreated(holder);
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (this.mSurfaceCallback != null) {
            this.mSurfaceCallback.surfaceChanged(holder, format, width, height);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.mCurrentWidth = 0;
        this.mCurrentHeight = 0;
        setCurrentState(0);
        this.mTargetState = 0;
        if (this.mHandler != null) {
            this.mHandler.removeMessages(1);
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        if (this.mSurfaceCallback != null) {
            this.mSurfaceCallback.surfaceDestroyed(holder);
        }
    }

    public void adRemainTime(int remain) {
        if (this.mOnAdRemainTimeListener != null) {
            this.mOnAdRemainTimeListener.adRemainTime(remain);
        }
        if (this.mOnAdRemainTimeListenerForMediaCenterView != null) {
            this.mOnAdRemainTimeListenerForMediaCenterView.adRemainTime(remain);
        }
        if (remain == -1) {
            Log.d(TAG, "ad finished!");
            setCurrentState(1);
        }
    }

    public void onSeekComplete() {
        if (this.mOnSeekCompleteListener != null) {
            this.mOnSeekCompleteListener.onSeekComplete();
        }
        if (this.mTargetState != 4) {
            this.mbPrepared = true;
            if (isPlaying()) {
                setCurrentState(this.mTargetState);
            } else if (!this.mIsPreview) {
                start();
            } else if (getCurrentPosition() < this.mPreviewEndTime) {
                start();
            } else {
                setCurrentState(this.mTargetState);
            }
        }
    }

    public void onBufferingUpdate(Object mp, int percent) {
        this.mProgressPercent = percent;
        if (this.mOnBufferingUpdateListener != null) {
            this.mOnBufferingUpdateListener.onBufferingUpdate(mp, percent);
        }
    }

    public void onPrepared(Object mp) {
        Log.d(TAG, "onPrepared:" + this.mTargetState);
        this.mbPrepared = true;
        setDimensionByDimenMode();
        setCurrentState(2);
        if (this.mTargetState == 3) {
            if (!(this.mSeekWhenPrepared == 0 || this.mSeekWhenPrepared == this.mVideo.getCurrentPosition())) {
                seekTo(this.mSeekWhenPrepared);
            }
            if (!(this.mVideo == null || this.mTargetState == 4)) {
                if (!this.mVideo.isPlaying()) {
                    this.mVideo.start();
                }
                setCurrentState(3);
            }
        }
        if (this.mOnPreparedListener != null) {
            this.mOnPreparedListener.onPrepared(mp);
        }
    }

    public void onCompletion(Object mp) {
        Log.i(TAG, "onCompletion....");
        setCurrentState(5);
        this.mTargetState = 5;
        if (this.mOnCompletionListener != null) {
            this.mOnCompletionListener.onCompletion(mp);
        }
    }

    public boolean onInfo(Object mp, int what, int extra) {
        Log.d(TAG, "YunosVideoView.onInfo mp = " + mp + ", what = " + what + ", extra = " + extra);
        if (this.mOnInfoListener != null && this.mOnInfoListener.onInfo(mp, what, extra)) {
            return true;
        }
        switch (what) {
            case 701:
                Log.d(TAG, "onInfo buffer start!");
                if (isAdoPlayer() || NetworkUtil.isNetworkAvailable()) {
                    setCurrentState(6);
                    return true;
                }
                this.mOnErrorListener.onError(mp, what, extra);
                this.mErrorCode = -1004;
                return true;
            case 702:
                this.mErrorCode = -1;
                setCurrentState(this.mTargetState);
                return true;
            default:
                return false;
        }
    }

    public boolean onError(Object mp, int what, int extra) {
        Log.v(TAG, "YunosVideoView, onError mp = " + mp + ", what = " + what + ", extra = " + extra);
        this.mErrorCode = what;
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        if (this.mOnErrorListener == null || !this.mOnErrorListener.onError(mp, what, extra)) {
            setCurrentState(-1);
            this.mTargetState = -1;
            if (isAdoPlayer() && -1004 == what) {
                stopPlayback();
            }
        }
        return true;
    }

    public void onFirstFrame() {
        if (this.mOnFirstFrameListener != null) {
            this.mOnFirstFrameListener.onFirstFrame();
        }
    }

    public boolean onInfoExtend(Object arg0, int what, int extra, Object obj) {
        if (304 == what && (obj instanceof InfoExtend) && ((InfoExtend) obj).getProgressPrecent() > 0) {
            this.mHandler.removeMessages(1);
            if (this.mCurrentState == 1) {
                Log.d(TAG, "send timeout delay");
                this.mHandler.sendEmptyMessageDelayed(1, (long) this.mPreparingTimeout);
            }
        }
        if (this.mOnInfoExtendListener != null) {
            return this.mOnInfoExtendListener.onInfoExtend(arg0, what, extra, obj);
        }
        return false;
    }

    public void onVideoSizeChanged(Object mp, int width, int height) {
        if (this.mOnVideoSizeChangedListener != null) {
            this.mOnVideoSizeChangedListener.onVideoSizeChanged(mp, width, height);
        }
    }

    public void onHttpDns(long abnormalSubCode) {
        if (this.mOnVideoHttpDnsListener != null) {
            this.mOnVideoHttpDnsListener.onHttpDns(abnormalSubCode);
        }
    }

    public void onRequestTs(InfoExtend info) {
        if (this.mOnVideoRequestTsListener != null) {
            this.mOnVideoRequestTsListener.onRequestTs(info);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!getIgnoreDestroy()) {
            hidePauseAd();
        }
    }

    public void setIgnoreDestroy(boolean ignore) {
        this.mVideo.setIgnoreDestroy(ignore);
    }

    public boolean getIgnoreDestroy() {
        if (this.mVideo != null) {
            return this.mVideo.getIgnoreDestroy();
        }
        return false;
    }

    private boolean isInit() {
        return (this.mOriginLayoutParams == null || this.mParent == null || this.mFullScreenLayoutParams == null || getContainGroupView() == null || this.mFullWidth == 0 || this.mFullHeight == 0) ? false : true;
    }

    private void initParams() {
        if (!isInit()) {
            if (this.mOriginLayoutParams == null) {
                this.mOriginLayoutParams = getLayoutParams();
            }
            if (this.mParent == null) {
                this.mParent = (ViewGroup) getParent();
            }
            if (this.mFullScreenLayoutParams == null) {
                this.mFullScreenLayoutParams = new FrameLayout.LayoutParams(-1, -1);
            }
            if (this.mFullWidth == 0 || this.mFullHeight == 0) {
                DisplayMetrics dm = new DisplayMetrics();
                ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay().getMetrics(dm);
                this.mFullWidth = dm.widthPixels;
                this.mFullHeight = dm.heightPixels;
            }
        }
    }

    private ViewGroup getContainGroupView() {
        if (this.mRootView == null) {
            this.mRootView = (ViewGroup) getRootView();
        }
        return this.mRootView;
    }

    public void setContainGroupView(ViewGroup rootView) {
        this.mRootView = rootView;
    }

    public void setFullScreenChangedListener(FullScreenChangedListener l) {
        this.mFullScreenChangedListener = l;
    }

    public boolean isFullScreen() {
        return this.mIsFullScreen;
    }

    public void setVideoViewPosition(int pos) {
        this.mVideoViewPosition = pos;
    }

    public void toggleScreen() {
        if (isFullScreen()) {
            unFullScreen();
        } else {
            fullScreen();
        }
    }

    public void fullScreen() {
        initParams();
        if (!isInit() || this.mVideo == null) {
            Log.e(TAG, "fullscreen,but params not inited!");
        } else if (this.mIsFullScreen) {
            Log.d(TAG, "already fullScreen!");
        } else {
            if (this.mFullScreenChangedListener != null) {
                this.mFullScreenChangedListener.onBeforeFullScreen();
            }
            setActivated(true);
            this.mIsFullScreen = true;
            this.mVideo.setIgnoreDestroy(true);
            this.mParent.removeView(this);
            setDimensionByDimenMode();
            getContainGroupView().addView(this, this.mFullScreenLayoutParams);
            this.mVideo.setIgnoreDestroy(false);
            setFocusable(true);
            setFocusableInTouchMode(true);
            requestFocus();
            if (this.mFullScreenChangedListener != null) {
                this.mFullScreenChangedListener.onAfterFullScreen();
            }
        }
    }

    public void unFullScreen() {
        if (!isInit() || this.mVideo == null) {
            Log.w(TAG, "unFullScreen,but params not inited!");
        } else if (!this.mIsFullScreen) {
            Log.d(TAG, "already unFullScreen!");
        } else {
            if (this.mFullScreenChangedListener != null) {
                this.mFullScreenChangedListener.onBeforeUnFullScreen();
            }
            setActivated(false);
            this.mIsFullScreen = false;
            this.mVideo.setIgnoreDestroy(true);
            getContainGroupView().removeView(this);
            setDimensionByDimenMode();
            this.mParent.addView(this, this.mVideoViewPosition, this.mOriginLayoutParams);
            this.mVideo.setIgnoreDestroy(false);
            if (this.mFullScreenChangedListener != null) {
                this.mFullScreenChangedListener.onAfterUnFullScreen();
            }
        }
    }

    public void setVideoViewBackgrounodColor(int color) {
        this.mVideoViewBgColor = color;
    }

    public void setVideoViewVisible() {
        if (getSurfaceView() != null) {
            getSurfaceView().setVisibility(0);
        }
    }

    public void setVideoViewInvisible() {
        if (getSurfaceView() != null) {
            getSurfaceView().setVisibility(4);
        }
    }

    public void clearVideoViewBg() {
        if (getSurfaceView() != null) {
            getSurfaceView().setBackgroundColor(0);
        }
    }

    public void setVideoViewBg() {
        if (getSurfaceView() != null) {
            getSurfaceView().setBackgroundColor(this.mVideoViewBgColor);
        }
    }

    public String getCodecInfo() {
        if (this.mVideo != null) {
            return this.mVideo.getCodecInfo();
        }
        return null;
    }

    public long getSourceBitrate() {
        if (this.mVideo != null) {
            return this.mVideo.getSourceBitrate();
        }
        return 0;
    }

    public String getNetSourceURL() {
        if (this.mVideo != null) {
            return this.mVideo.getNetSourceURL();
        }
        return null;
    }

    public String getHttpHeader() {
        if (this.mVideo != null) {
            return this.mVideo.getHttpHeader();
        }
        return null;
    }

    public void setHttpDNS(String httpdns) {
        if (this.mVideo != null) {
            this.mVideo.setHttpDNS(httpdns);
        } else {
            Log.w(TAG, "setHttpDNS error! mVideo is null!");
        }
    }

    public void setNetadaption(String netadaption) {
        if (this.mVideo != null) {
            this.mVideo.setNetadaption(netadaption);
        } else {
            Log.w(TAG, "setNetadaption error! mVideo is null!");
        }
    }

    public void setOnVideoRequestTsListener(IBaseVideo.VideoRequestTsListener l) {
        this.mOnVideoRequestTsListener = l;
    }

    public void setOnVideoHttpDnsListener(IBaseVideo.VideoHttpDnsListener l) {
        this.mOnVideoHttpDnsListener = l;
    }

    public IMediaPlayer getIMediaPlayer() {
        if (this.mVideo != null) {
            return this.mVideo.getIMediaPlayer();
        }
        return null;
    }

    public void setDefinitionChangedListener(IBaseVideo.OnDefinitionChangedListener lis) {
        this.mDefinitionChangedListenter = lis;
    }

    public void setSkipHeadTailInfoListener(IBaseVideo.OnSkipHeadTailInfoListener lis) {
        this.mSkipHeadTailInfoListener = lis;
    }

    public void setPreviewInfoListener(IBaseVideo.OnPreviewInfoListener lis) {
        this.mPreviewInfoListener = lis;
    }

    public void onPreviewInfoReady(boolean isPreview, int previewEndTimeInSecond) {
        this.mIsPreview = isPreview;
        this.mPreviewEndTime = previewEndTimeInSecond;
        if (this.mPreviewInfoListener != null) {
            this.mPreviewInfoListener.onPreviewInfoReady(isPreview, previewEndTimeInSecond);
        }
    }

    public void onPreviewCompleted() {
        if (this.mPreviewInfoListener != null) {
            this.mPreviewInfoListener.onPreviewCompleted();
        }
    }

    public void onHeaderTailerInfoReady(int headerEndTime, int tailerStartTime) {
        if (this.mSkipHeadTailInfoListener != null) {
            this.mSkipHeadTailInfoListener.onHeaderTailerInfoReady(headerEndTime, tailerStartTime);
        }
    }

    public void onDefinitionChange(boolean changed, int definition) {
        if (this.mDefinitionChangedListenter != null) {
            this.mDefinitionChangedListenter.onDefinitionChange(changed, definition);
        }
    }

    public final void cancelMTopInfo() {
        if (this.mAsyncTask != null && !this.mAsyncTask.isCancelled() && this.mAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mAsyncTask.cancel(true);
            this.mAsyncTask = null;
        }
    }

    public final boolean isCancelled() {
        if (this.mAsyncTask != null) {
            return this.mAsyncTask.isCancelled();
        }
        return true;
    }

    public final <T extends MTopInfoBase> void getMTopInfo(MediaType sourceType, NetworkUtil.HttpMethod method, MediaMTopParams params, MTopVideoCallback<T> callback, Class<T> c) {
        cancelMTopInfo();
        final HttpConstant.HttpMethod httpMethod = method == NetworkUtil.HttpMethod.Get ? HttpConstant.HttpMethod.GET : HttpConstant.HttpMethod.POST;
        final MTopVideoCallback<T> mTopVideoCallback = callback;
        final MediaType mediaType = sourceType;
        final MediaMTopParams mediaMTopParams = params;
        final Class<T> cls = c;
        this.mAsyncTask = new WorkAsyncTask<T>(getContext()) {
            public void onPre() throws Exception {
                super.onPre();
                mTopVideoCallback.onPre();
            }

            public T doProgress() throws Exception {
                mTopVideoCallback.beforeDoProgress();
                T result = MediaMTopDao.getMTopInfo(mediaType, httpMethod, mediaMTopParams, cls);
                if (result == null) {
                    result = (MTopInfoBase) cls.newInstance();
                    result.setMediaError(MediaMTopDao.createMediaError(mediaType, ErrorType.AUTH_ERROR, 34, ""));
                }
                mTopVideoCallback.doProgress(result);
                return result;
            }

            public void onCancel(boolean isSuccess) {
                super.onCancel(isSuccess);
                mTopVideoCallback.onCancel(isSuccess);
            }

            public void onError(Exception e) {
                super.onError(e);
                mTopVideoCallback.onError(e);
            }

            public void onPost(boolean isSuccess, T result) throws Exception {
                if (result == null) {
                    result = (MTopInfoBase) cls.newInstance();
                    IMediaError error = MediaMTopDao.createMediaError(mediaType, ErrorType.AUTH_ERROR, 34, "");
                    Exception exception = getmException();
                    if (exception instanceof ErrorDetail) {
                        ErrorDetail detailTemp = (ErrorDetail) exception;
                        ErrorDetail detail = error.getErrorDetail();
                        if (detail != null) {
                            detail.setCode(detailTemp.getCode());
                            detail.setErrorMessage(detailTemp.getErrorMessage());
                        }
                    }
                    result.setMediaError(error);
                }
                if (result.getErrorInfo() != null) {
                    isSuccess = false;
                }
                YunosVideoView.this.onAuthorityResult(isSuccess, result);
                super.onPost(isSuccess, result);
                mTopVideoCallback.onPost(isSuccess, result);
            }

            public void onUpdate(Object... values) throws Exception {
                super.onUpdate(values);
                mTopVideoCallback.onUpdate(values);
            }
        };
        this.mAsyncTask.execute(new Object[0]);
    }

    public void onAuthorityResult(boolean isSuccess, MTopInfoBase result) {
        if (this.mVideo != null) {
            this.mVideo.onAuthorityResult(isSuccess, result);
        }
    }
}
