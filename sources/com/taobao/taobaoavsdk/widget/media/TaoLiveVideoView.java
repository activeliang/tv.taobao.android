package com.taobao.taobaoavsdk.widget.media;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.taobao.adapter.ConfigAdapter;
import com.taobao.adapter.CustomLibLoader;
import com.taobao.adapter.FirstRenderAdapter;
import com.taobao.adapter.LogAdapter;
import com.taobao.adapter.MonitorAdapter;
import com.taobao.media.MediaAdapteManager;
import com.taobao.media.MediaConstant;
import com.taobao.statistic.CT;
import com.taobao.statistic.TBS;
import com.taobao.tao.remotebusiness.js.MtopJSBridge;
import com.taobao.taobaoavsdk.R;
import com.taobao.taobaoavsdk.cache.ApplicationUtils;
import com.taobao.taobaoavsdk.recycle.MediaPlayerManager;
import com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler;
import com.taobao.taobaoavsdk.util.AndroidUtils;
import com.taobao.taobaoavsdk.widget.extra.TaobaoABTestAdapter;
import com.taobao.taobaoavsdk.widget.media.IRenderView;
import com.yunos.tv.tvsdk.media.data.HuasuVideo;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import tv.danmaku.ijk.media.player.AbstractMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaException;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.MonitorMediaPlayer;
import tv.danmaku.ijk.media.player.NativeMediaPlayer;
import tv.danmaku.ijk.media.player.TaobaoMediaPlayer;

public class TaoLiveVideoView extends FrameLayout implements MediaPlayerRecycler.OnRecycleListener, Application.ActivityLifecycleCallbacks {
    public static final int ARTP_ERRCODE_PACKETRECVTIMEOUT = -10610;
    public static final int ARTP_ERRCODE_SPSPPSAaACCONFTIMEOUT = -10609;
    public static final int ARTP_ERRCODE_STARTPLAYTIMEOUT = -10608;
    public static final int ARTP_ERRCODE_STOPBYSFUBASE = -10000;
    public static final int ARTP_ERRCODE_STREAMHASSTOPPED = -10605;
    public static final int ARTP_ERRCODE_STREAMILLEGAL = -10603;
    public static final int ARTP_ERRCODE_STREAMNOTFOUND = -10604;
    public static final int ARTP_ERRCODE_UDP_NOUSABLE = -10611;
    public static final String MornitorBuffering = "taolive_buffering";
    public static final String MornitorFirstFrameRender = "first_frame_render";
    public static int SDK_INT_FOR_OPTIMIZE = 23;
    static final int STATE_ERROR = -1;
    static final int STATE_IDLE = 0;
    static final int STATE_PAUSED = 4;
    static final int STATE_PLAYBACK_COMPLETED = 5;
    static final int STATE_PLAYING = 3;
    static final int STATE_PREPARED = 2;
    static final int STATE_PREPARING = 1;
    private static final String TAG = "AVSDK";
    public static final String TBLIVE_ARTP_SCHEMA = "artp://";
    private static final String TBLIVE_BUSIINESS_ID = "TBLive";
    public static final String TBLIVE_ORANGE_ACCELERATESPEED = "AccelerateSpeed";
    public static final String TBLIVE_ORANGE_ACCELERATESPEED_LINK = "AudioAccelerateSpeedLink";
    public static final String TBLIVE_ORANGE_FAST_LOADING = "fast_loading";
    public static final String TBLIVE_ORANGE_FIRSTPLAY_BUFFER_TIME = "FirstBufferMS";
    public static final String TBLIVE_ORANGE_FUSION_MODE = "SensorFusionCalibrate";
    public static final String TBLIVE_ORANGE_GROUP = "tblive";
    public static final String TBLIVE_ORANGE_NETWORK_TRAFFIC_REPORT = "NetworkTrafficReportTrigger";
    public static final String TBLIVE_ORANGE_PLAYBUFFER_TIME = "PlayBufferMS";
    public static final String TBLIVE_ORANGE_REPORT_INTERNAL = "LogReportIntervalSeconds";
    public static final String TBLIVE_ORANGE_RETAIN_FLV = "RetainFlv";
    public static final String TBLIVE_ORANGE_SENDSEI = "SendSEI";
    public static final String TBLIVE_ORANGE_SLOWSPEED = "AudioSlowSpeed";
    public static final String TBLIVE_ORANGE_SLOWSPEED_LINK = "AudioSlowSpeedLink";
    private static final int TBMPBBufferLoadCountLimit = 3;
    private static final int TBMPBBufferLoadCountTimeInterval = 15000;
    public static final String mornitorNetShake = "net_shake";
    public static final String mornitorOnePlay = "playExperience";
    public static final String mornitorPlayerError = "playerError";
    public static final String mornitorPtsDts = "pts_dts";
    boolean bAudioOnly;
    boolean bAutoPause;
    boolean bFirstFrameRendered;
    boolean bLooping;
    boolean bPlayerTypeChanged;
    boolean bmuted;
    int bufferLoadCountLimit;
    int bufferLoadCountTimeInterval;
    int bufferLoadFrequencyCounter;
    private String cdn_ip;
    long lastBufferLoadTime;
    private boolean mBlockTouchEvent;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnCompletionListener mCompletionListener;
    TaoLiveVideoViewConfig mConfig;
    ConfigAdapter mConfigAdapter;
    /* access modifiers changed from: private */
    public Context mContext;
    ImageView mCoverImgView;
    int mCurrentBufferPercentage;
    CustomLibLoader mCustomLibLoader;
    private boolean mEnableVideoDetect;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnErrorListener mErrorListener;
    private Map<String, String> mExtInfo;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnInfoListener mInfoListener;
    boolean mIsConnected;
    int mLastNetworkType;
    LogAdapter mLogAdapter;
    /* access modifiers changed from: private */
    public MediaPlayerRecycler mMediaPlayerRecycler;
    BroadcastReceiver mNetworkReceiver;
    /* access modifiers changed from: private */
    public IRenderView mNextRenderView;
    IRenderView.IRenderCallback mNextSHCallback;
    /* access modifiers changed from: private */
    public IRenderView.ISurfaceHolder mNextSurfaceHolder;
    /* access modifiers changed from: private */
    public List<IMediaPlayer.OnBufferingUpdateListener> mOnBufferingUpdateListeners;
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private List<IMediaPlayer.OnCompletionListener> mOnCompletionListeners;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnErrorListener mOnErrorListener;
    /* access modifiers changed from: private */
    public List<IMediaPlayer.OnErrorListener> mOnErrorListeners;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnInfoListener mOnInfoListener;
    /* access modifiers changed from: private */
    public List<IMediaPlayer.OnInfoListener> mOnInfoListeners;
    private List<OnPauseListener> mOnPauseListeners;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnPreparedListener mOnPreparedListener;
    /* access modifiers changed from: private */
    public List<IMediaPlayer.OnPreparedListener> mOnPreparedListeners;
    private List<OnStartListener> mOnStartListeners;
    private List<IMediaPlayer.OnVideoClickListener> mOnVideoClickListeners;
    String mPlayUrl;
    /* access modifiers changed from: private */
    public IMediaPlayer.OnPreparedListener mPreparedListener;
    private SparseArray<Float> mPropertyFloat;
    private SparseArray<Long> mPropertyLong;
    View mRenderUIView;
    IRenderView mRenderView;
    IRenderView.IRenderCallback mSHCallback;
    int mSeekWhenPrepared;
    /* access modifiers changed from: private */
    public String mSeiData;
    IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener;
    /* access modifiers changed from: private */
    public long mStartTime;
    IRenderView.ISurfaceHolder mSurfaceHolder;
    /* access modifiers changed from: private */
    public SurfaceListener mSurfaceListener;
    /* access modifiers changed from: private */
    public Runnable mSwitchRunnable;
    int mTargetState;
    private float mTouchX;
    private float mTouchY;
    int mVideoHeight;
    int mVideoRotationDegree;
    int mVideoSarDen;
    int mVideoSarNum;
    int mVideoWidth;
    /* access modifiers changed from: private */
    public boolean mbIsSwitchingPath;
    long timeOutUs;

    public interface OnPauseListener {
        void onPause(IMediaPlayer iMediaPlayer);
    }

    public interface OnStartListener {
        void onStart(IMediaPlayer iMediaPlayer);
    }

    public interface SurfaceListener {
        void onSurfaceCreated();

        void onSurfaceDestroyed();
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
        if (this.mContext == activity) {
            this.mStartTime = System.currentTimeMillis();
        }
        if (this.mConfig != null && this.mConfig.mbEnableRecycle && this.mContext == activity && this.bAutoPause) {
            start();
        }
    }

    public void onActivityPaused(Activity activity) {
        this.mStartTime = 0;
        if (this.mConfig != null && this.mConfig.mbEnableRecycle && this.mContext == activity) {
            pause(false);
        }
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public String getMediaPlayUrl() {
        return this.mPlayUrl;
    }

    public void setPlayRate(float playRate) {
        if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
            this.mMediaPlayerRecycler.mMediaPlayer.setPlayRate(playRate);
        }
    }

    public void onCompletion() {
        if (this.mLogAdapter != null) {
            this.mLogAdapter.onLogi(TAG, "player onCompletion");
        }
        if (this.mConfig != null && this.mConfig.mbEnableRecycle) {
            MediaPlayerManager.getInstance().reorderLruMediaPlayer();
        }
        this.mMediaPlayerRecycler.mPlayState = 5;
        this.mTargetState = 5;
        clearKeepScreenOn();
        if (this.mOnCompletionListener != null) {
            this.mOnCompletionListener.onCompletion(this.mMediaPlayerRecycler.mMediaPlayer);
        }
        if (this.mOnCompletionListeners != null) {
            for (IMediaPlayer.OnCompletionListener listener : this.mOnCompletionListeners) {
                if (listener != null) {
                    listener.onCompletion(this.mMediaPlayerRecycler.mMediaPlayer);
                }
            }
        }
    }

    public TaoLiveVideoView(Context context) {
        this(context, (AttributeSet) null);
    }

    public TaoLiveVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaoLiveVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.cdn_ip = "";
        this.mIsConnected = true;
        this.mLastNetworkType = -1;
        this.mPlayUrl = "";
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.bufferLoadCountTimeInterval = TBMPBBufferLoadCountTimeInterval;
        this.bufferLoadCountLimit = 3;
        this.lastBufferLoadTime = 0;
        this.bufferLoadFrequencyCounter = 0;
        this.timeOutUs = 10000000;
        this.bmuted = false;
        this.bLooping = false;
        this.bFirstFrameRendered = false;
        this.bPlayerTypeChanged = false;
        this.bAudioOnly = false;
        this.mEnableVideoDetect = false;
        this.mBlockTouchEvent = false;
        this.mStartTime = 0;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                if (TaoLiveVideoView.this.mLogAdapter != null) {
                    TaoLiveVideoView.this.mLogAdapter.onLogi(TaoLiveVideoView.TAG, "player onVideoSizeChanged, width: " + width + " height: " + height + " sarNum: " + sarNum + " sarDen: " + sarDen);
                }
                TaoLiveVideoView.this.changeVideoSize(width, height, sarNum, sarDen);
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() {
            public void onCompletion(IMediaPlayer mp) {
                TaoLiveVideoView.this.onCompletion();
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() {
            public boolean onInfo(IMediaPlayer mp, long arg1, long arg2, long arg3, Object obj) {
                if (TaoLiveVideoView.this.mLogAdapter != null) {
                    TaoLiveVideoView.this.mLogAdapter.onLogi(TaoLiveVideoView.TAG, "player onInfo, arg1: " + arg1 + " arg2: " + arg2 + " arg3: " + arg3);
                }
                if (TaoLiveVideoView.this.mOnInfoListener != null) {
                    TaoLiveVideoView.this.mOnInfoListener.onInfo(mp, arg1, arg2, arg3, obj);
                }
                if (TaoLiveVideoView.this.mOnInfoListeners != null) {
                    for (IMediaPlayer.OnInfoListener listener : TaoLiveVideoView.this.mOnInfoListeners) {
                        if (listener != null) {
                            listener.onInfo(mp, arg1, arg2, arg3, obj);
                        }
                    }
                }
                switch ((int) arg1) {
                    case 3:
                        Log.d(TaoLiveVideoView.TAG, "~~~~ MEDIA_INFO_VIDEO_RENDERING_START ~~~~~~");
                        TaoLiveVideoView.this.bFirstFrameRendered = true;
                        if (TaoLiveVideoView.this.mCoverImgView == null) {
                            return true;
                        }
                        TaoLiveVideoView.startViewFadeAnimation(TaoLiveVideoView.this.mCoverImgView);
                        return true;
                    case 701:
                        long currentTime = System.currentTimeMillis();
                        if (TaoLiveVideoView.this.lastBufferLoadTime == 0) {
                            TaoLiveVideoView.this.bufferLoadFrequencyCounter++;
                        } else if (currentTime - TaoLiveVideoView.this.lastBufferLoadTime > ((long) TaoLiveVideoView.this.bufferLoadCountTimeInterval)) {
                            TaoLiveVideoView.this.bufferLoadFrequencyCounter = 0;
                        } else {
                            TaoLiveVideoView.this.bufferLoadFrequencyCounter++;
                        }
                        TaoLiveVideoView.this.lastBufferLoadTime = currentTime;
                        if (TaoLiveVideoView.this.bufferLoadFrequencyCounter < TaoLiveVideoView.this.bufferLoadCountLimit) {
                            return true;
                        }
                        if (TaoLiveVideoView.this.mLogAdapter != null) {
                            TaoLiveVideoView.this.mLogAdapter.onLogi(TaoLiveVideoView.TAG, "adapt: " + TaoLiveVideoView.this.bufferLoadFrequencyCounter + " , " + TaoLiveVideoView.TBMPBBufferLoadCountTimeInterval);
                        }
                        if (TaoLiveVideoView.this.mOnInfoListener != null) {
                            TaoLiveVideoView.this.mOnInfoListener.onInfo(mp, 903, 0, 0, (Object) null);
                        }
                        if (TaoLiveVideoView.this.mOnInfoListeners == null) {
                            return true;
                        }
                        for (IMediaPlayer.OnInfoListener listener2 : TaoLiveVideoView.this.mOnInfoListeners) {
                            listener2.onInfo(mp, 903, 0, 0, (Object) null);
                        }
                        return true;
                    case 705:
                        if (TaoLiveVideoView.this.mLogAdapter == null) {
                            return true;
                        }
                        TaoLiveVideoView.this.mLogAdapter.onLogi(TaoLiveVideoView.TAG, "MEDIA_INFO_STREAM_ABNORMAL_ADJOIN: " + arg2 + " -> " + arg3);
                        return true;
                    case IMediaPlayer.MEDIA_INFO_STREAM_ABNORMAL_VIDEO /*706*/:
                        if (TaoLiveVideoView.this.mLogAdapter == null) {
                            return true;
                        }
                        TaoLiveVideoView.this.mLogAdapter.onLogi(TaoLiveVideoView.TAG, "MEDIA_INFO_STREAM_ABNORMAL_VIDEO: " + arg2 + " -> " + arg3);
                        return true;
                    case IMediaPlayer.MEDIA_INFO_STREAM_ABNORMAL_AUDIO /*707*/:
                        if (TaoLiveVideoView.this.mLogAdapter == null) {
                            return true;
                        }
                        TaoLiveVideoView.this.mLogAdapter.onLogi(TaoLiveVideoView.TAG, "MEDIA_INFO_STREAM_ABNORMAL_AUDIO: " + arg2 + " -> " + arg3);
                        return true;
                    case IMediaPlayer.MEDIA_INFO_STREAM_ABNORMAL_AVSTREAM /*708*/:
                        if (TaoLiveVideoView.this.mLogAdapter == null) {
                            return true;
                        }
                        TaoLiveVideoView.this.mLogAdapter.onLogi(TaoLiveVideoView.TAG, "MEDIA_INFO_STREAM_ABNORMAL_AVSTREAM: " + arg2 + " -> " + arg3);
                        return true;
                    case IMediaPlayer.MEDIA_INFO_STREAM_ABNORMAL_VIDEO_DTS /*709*/:
                        if (TaoLiveVideoView.this.mLogAdapter == null) {
                            return true;
                        }
                        TaoLiveVideoView.this.mLogAdapter.onLogi(TaoLiveVideoView.TAG, "MEDIA_INFO_STREAM_ABNORMAL_VIDEO_DTS");
                        return true;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_SHAKE /*710*/:
                        if (TaoLiveVideoView.this.mLogAdapter != null) {
                            TaoLiveVideoView.this.mLogAdapter.onLogi(TaoLiveVideoView.TAG, "MEDIA_INFO_NETWORK_SHAKE: " + arg2);
                            break;
                        }
                        break;
                    case IMediaPlayer.MEDIA_INFO_AVFORMAT_TIME /*711*/:
                        if (TaoLiveVideoView.this.mLogAdapter == null) {
                            return true;
                        }
                        TaoLiveVideoView.this.mLogAdapter.onLogi(TaoLiveVideoView.TAG, "MEDIA_INFO_STREAM_ABNORMAL_ADJOIN: " + arg2 + " -> " + arg3);
                        return true;
                    case IMediaPlayer.MEDIA_INFO_FRAME_QUEUE_NULL /*713*/:
                        if (TaoLiveVideoView.this.mLogAdapter == null) {
                            return true;
                        }
                        TaoLiveVideoView.this.mLogAdapter.onLogi(TaoLiveVideoView.TAG, "MEDIA_INFO_FRAME_QUEUE_NULL");
                        return true;
                    case IMediaPlayer.MEDIA_INFO_SEI_USERDEFINED_STRUCT /*715*/:
                        String str = (String) obj;
                        String unused = TaoLiveVideoView.this.mSeiData = str;
                        if (TaoLiveVideoView.this.mLogAdapter == null) {
                            return true;
                        }
                        TaoLiveVideoView.this.mLogAdapter.onLogi(TaoLiveVideoView.TAG, "SEI STRUCT: " + str + ",pts: " + arg3);
                        return true;
                    case IMediaPlayer.MEDIA_INFO_SWITCH_PATH_SYNC_FRAME /*717*/:
                        if (!TaoLiveVideoView.this.mbIsSwitchingPath || TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer == null || TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer == null) {
                            return true;
                        }
                        ((IjkMediaPlayer) TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer)._setPropertyLong(20136, 0);
                        return true;
                    case 10001:
                        TaoLiveVideoView.this.mVideoRotationDegree = (int) arg2;
                        if (TaoLiveVideoView.this.mRenderView == null) {
                            return true;
                        }
                        TaoLiveVideoView.this.mRenderView.setVideoRotation((int) arg2);
                        return true;
                    case 10003:
                        break;
                    default:
                        return true;
                }
                if (TaoLiveVideoView.this.mLogAdapter == null) {
                    return true;
                }
                TaoLiveVideoView.this.mLogAdapter.onLogi(TaoLiveVideoView.TAG, "MEDIA_INFO_VIDEO_CODEC_ID_CHANGE:arg1" + arg1 + " arg2:" + arg2);
                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() {
            public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                boolean res = true;
                String error_log = "player onError, framework_err: " + framework_err + ", impl_err: " + impl_err;
                if (TaoLiveVideoView.this.mLogAdapter != null) {
                    TaoLiveVideoView.this.mLogAdapter.onLoge(TaoLiveVideoView.TAG, error_log);
                }
                TaoLiveVideoView.this.mMediaPlayerRecycler.mPlayState = -1;
                TaoLiveVideoView.this.mTargetState = -1;
                TaoLiveVideoView.this.clearKeepScreenOn();
                if (TaoLiveVideoView.this.mConfig != null && TaoLiveVideoView.this.mConfig.mbEnableRecycle) {
                    MediaPlayerManager.getInstance().reorderLruMediaPlayer();
                }
                if ((TaoLiveVideoView.this.mOnErrorListener == null || !TaoLiveVideoView.this.mOnErrorListener.onError(TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer, framework_err, impl_err)) && TaoLiveVideoView.this.mOnErrorListeners != null) {
                    res = false;
                    for (IMediaPlayer.OnErrorListener listener : TaoLiveVideoView.this.mOnErrorListeners) {
                        res = listener.onError(TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer, framework_err, impl_err);
                    }
                }
                return res;
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                TaoLiveVideoView.this.mCurrentBufferPercentage = percent;
                if (TaoLiveVideoView.this.mOnBufferingUpdateListeners != null) {
                    for (IMediaPlayer.OnBufferingUpdateListener listener : TaoLiveVideoView.this.mOnBufferingUpdateListeners) {
                        if (listener != null) {
                            listener.onBufferingUpdate(TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer, percent);
                        }
                    }
                }
            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() {
            public void onPrepared(IMediaPlayer mp) {
                if (TaoLiveVideoView.this.mLogAdapter != null) {
                    TaoLiveVideoView.this.mLogAdapter.onLogi(TaoLiveVideoView.TAG, "player onPrepared");
                }
                TaoLiveVideoView.this.mMediaPlayerRecycler.mPlayState = 2;
                if (TaoLiveVideoView.this.mOnPreparedListener != null) {
                    TaoLiveVideoView.this.mOnPreparedListener.onPrepared(TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer);
                }
                if (TaoLiveVideoView.this.mOnPreparedListeners != null) {
                    for (IMediaPlayer.OnPreparedListener listener : TaoLiveVideoView.this.mOnPreparedListeners) {
                        if (listener != null) {
                            listener.onPrepared(TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer);
                        }
                    }
                }
                int seekToPosition = TaoLiveVideoView.this.mSeekWhenPrepared;
                if (seekToPosition != 0) {
                    TaoLiveVideoView.this.seekTo(seekToPosition);
                }
                if (TaoLiveVideoView.this.mTargetState == 3) {
                    TaoLiveVideoView.this.start();
                }
            }
        };
        this.mNextSHCallback = new IRenderView.IRenderCallback() {
            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
                IRenderView.ISurfaceHolder unused = TaoLiveVideoView.this.mNextSurfaceHolder = holder;
                TaoLiveVideoView.this.bindSurfaceHolder(TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer, TaoLiveVideoView.this.mNextSurfaceHolder);
            }

            public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder holder, int format, int width, int height) {
            }

            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
                IRenderView.ISurfaceHolder unused = TaoLiveVideoView.this.mNextSurfaceHolder = null;
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() {
            public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder holder, int format, int w, int h) {
                if (holder.getRenderView() == TaoLiveVideoView.this.mRenderView) {
                    TaoLiveVideoView.this.mSurfaceHolder = holder;
                    if (TaoLiveVideoView.this.mMediaPlayerRecycler != null && TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer != null) {
                        TaoLiveVideoView.this.bindSurfaceHolder(TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer, holder);
                        TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer.setSurfaceSize(w, h);
                        if (TaoLiveVideoView.this.mTargetState == 3 && TaoLiveVideoView.this.mMediaPlayerRecycler.mPlayState != 3) {
                            if (TaoLiveVideoView.this.mSeekWhenPrepared != 0) {
                                TaoLiveVideoView.this.seekTo(TaoLiveVideoView.this.mSeekWhenPrepared);
                            }
                            TaoLiveVideoView.this.start();
                        }
                    }
                } else if (TaoLiveVideoView.this.mLogAdapter != null) {
                    TaoLiveVideoView.this.mLogAdapter.onLoge(TaoLiveVideoView.TAG, "onSurfaceChanged: unmatched render callback\n");
                }
            }

            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
                if (holder.getRenderView() == TaoLiveVideoView.this.mRenderView) {
                    TaoLiveVideoView.this.mSurfaceHolder = holder;
                    if (!(TaoLiveVideoView.this.mMediaPlayerRecycler == null || TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer == null)) {
                        TaoLiveVideoView.this.bindSurfaceHolder(TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer, holder);
                        if (TaoLiveVideoView.this.mTargetState == 3 && TaoLiveVideoView.this.mMediaPlayerRecycler.mPlayState != 3) {
                            if (TaoLiveVideoView.this.mSeekWhenPrepared != 0) {
                                TaoLiveVideoView.this.seekTo(TaoLiveVideoView.this.mSeekWhenPrepared);
                            }
                            TaoLiveVideoView.this.start();
                        }
                    }
                    if (TaoLiveVideoView.this.mSurfaceListener != null) {
                        TaoLiveVideoView.this.mSurfaceListener.onSurfaceCreated();
                    }
                } else if (TaoLiveVideoView.this.mLogAdapter != null) {
                    TaoLiveVideoView.this.mLogAdapter.onLoge(TaoLiveVideoView.TAG, "onSurfaceCreated: unmatched render callback\n");
                }
            }

            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
                if (holder.getRenderView() == TaoLiveVideoView.this.mRenderView) {
                    TaoLiveVideoView.this.releaseHolderSurface(TaoLiveVideoView.this.mSurfaceHolder);
                    TaoLiveVideoView.this.mSurfaceHolder = null;
                    if (TaoLiveVideoView.this.mSurfaceListener != null) {
                        TaoLiveVideoView.this.mSurfaceListener.onSurfaceDestroyed();
                    }
                } else if (TaoLiveVideoView.this.mLogAdapter != null) {
                    TaoLiveVideoView.this.mLogAdapter.onLoge(TaoLiveVideoView.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                }
            }
        };
        initVideoView(context);
    }

    public void setCustomLibLoader(CustomLibLoader customLibLoader) {
        this.mCustomLibLoader = customLibLoader;
    }

    public void initConfig(TaoLiveVideoViewConfig config) {
        if (this.mConfig == null && config != null) {
            this.mConfig = config;
            this.mConfig.mNewBundleSdk = false;
            setBusinessId(this.mConfig.mBusinessId);
            _setRenderType(this.mConfig.mRenderType, this.mConfig.mVRRenderType, this.mConfig.mVRLng, this.mConfig.mVRLat);
            setCoverImg(this.mConfig.mCoverResId);
            if (TextUtils.isEmpty(this.mConfig.mConfigGroup)) {
                this.mConfig.mConfigGroup = TBLIVE_ORANGE_GROUP;
            }
            if (TextUtils.isEmpty(this.mConfig.mToken)) {
                this.mConfig.mToken = MediaPlayerManager.generateToken();
            }
            if (TextUtils.isEmpty(this.mConfig.mPlayToken) && "TBLive".equals(this.mConfig.mBusinessId)) {
                this.mConfig.mPlayToken = MediaPlayerManager.generateToken();
            }
            if ("TBLive".equals(this.mConfig.mBusinessId)) {
                this.mConfig.mbEnableRecycle = false;
            }
            if (this.mConfig.mbEnableRecycle) {
                this.mMediaPlayerRecycler = MediaPlayerManager.getInstance().getMediaRecycler(this.mConfig.mToken, this);
            } else {
                this.mMediaPlayerRecycler = new MediaPlayerRecycler(this.mConfig.mToken, this);
            }
            if (this.mMediaPlayerRecycler.mMediaPlayer != null) {
                this.mMediaPlayerRecycler.mMediaPlayer.registerOnPreparedListener(this.mPreparedListener);
                this.mMediaPlayerRecycler.mMediaPlayer.registerOnVideoSizeChangedListener(this.mSizeChangedListener);
                this.mMediaPlayerRecycler.mMediaPlayer.registerOnCompletionListener(this.mCompletionListener);
                this.mMediaPlayerRecycler.mMediaPlayer.registerOnErrorListener(this.mErrorListener);
                this.mMediaPlayerRecycler.mMediaPlayer.registerOnInfoListener(this.mInfoListener);
                this.mMediaPlayerRecycler.mMediaPlayer.registerOnBufferingUpdateListener(this.mBufferingUpdateListener);
            }
        }
    }

    public TaoLiveVideoViewConfig getConfig() {
        return this.mConfig;
    }

    public MediaPlayerRecycler getMediaPlayerRecycler() {
        if (!this.mConfig.mbEnableRecycle) {
            return this.mMediaPlayerRecycler;
        }
        return null;
    }

    public void setBusinessId(String businessId) {
        if (this.mConfig != null) {
            this.mConfig.mBusinessId = businessId;
        }
    }

    public void blockTouchEvent(boolean block) {
        this.mBlockTouchEvent = block;
    }

    public void enableVideoClickDetect(boolean enableVideoDetect) {
        this.mEnableVideoDetect = enableVideoDetect;
    }

    public void setCdnIP(String cdnip) {
        this.cdn_ip = cdnip.replaceAll(" ", "");
        Log.d(TAG, "CDN IP: " + this.cdn_ip);
    }

    public void setScenarioType(int type) {
        if (this.mConfig != null) {
            this.mConfig.mScenarioType = type;
        }
    }

    public void setPlayerType(int type) {
        if (this.mConfig != null && this.mConfig.mPlayerType != type) {
            this.mConfig.mPlayerType = type;
            this.bPlayerTypeChanged = true;
        }
    }

    public void setFeedId(String feedId) {
        if (this.mConfig != null) {
            this.mConfig.mFeedId = feedId;
            if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
                if (((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getConfig() != null) {
                    ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getConfig().mFeedId = feedId;
                }
                if (((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getCloneConfig() != null) {
                    ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getCloneConfig().mFeedId = feedId;
                }
            }
        }
    }

    public void setMediaSourceType(String mediaSourceType) {
        if (this.mConfig != null) {
            this.mConfig.mMediaSourceType = mediaSourceType;
            if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
                if (((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getConfig() != null) {
                    ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getConfig().mMediaSourceType = mediaSourceType;
                }
                if (((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getCloneConfig() != null) {
                    ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getCloneConfig().mMediaSourceType = mediaSourceType;
                }
            }
        }
    }

    public void setAccountId(String accountId) {
        if (this.mConfig != null) {
            this.mConfig.mAccountId = accountId;
            if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
                if (((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getConfig() != null) {
                    ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getConfig().mAccountId = accountId;
                }
                if (((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getConfig() != null) {
                    ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer).getConfig().mAccountId = accountId;
                }
            }
        }
    }

    public void setVideoDefinition(String videoDefinition) {
        if (this.mConfig != null) {
            this.mConfig.mVideoDefinition = videoDefinition;
        }
    }

    public void setConfigAdapter(ConfigAdapter adapter) {
        this.mConfigAdapter = adapter;
    }

    public void setLogAdapter(LogAdapter adapter) {
        this.mLogAdapter = adapter;
    }

    @Deprecated
    public void setMonitorAdapter(MonitorAdapter adapter) {
    }

    public IRenderView getRenderView() {
        return this.mRenderView;
    }

    public void setPropertyLong(int property, long value) {
        if (this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null) {
            if (this.mPropertyLong == null) {
                this.mPropertyLong = new SparseArray<>();
            }
            this.mPropertyLong.put(property, Long.valueOf(value));
        } else if (this.mMediaPlayerRecycler.mMediaPlayer instanceof IjkMediaPlayer) {
            ((IjkMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer)._setPropertyLong(property, value);
        } else if (this.mMediaPlayerRecycler.mMediaPlayer instanceof TaobaoMediaPlayer) {
            ((TaobaoMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer)._setPropertyLong(property, value);
        }
    }

    public void setPropertyFloat(int property, float value) {
        if (this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null) {
            if (this.mPropertyFloat == null) {
                this.mPropertyFloat = new SparseArray<>();
            }
            this.mPropertyFloat.put(property, Float.valueOf(value));
        } else if (this.mMediaPlayerRecycler.mMediaPlayer instanceof IjkMediaPlayer) {
            ((IjkMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer)._setPropertyFloat(property, value);
        } else if (this.mMediaPlayerRecycler.mMediaPlayer instanceof TaobaoMediaPlayer) {
            ((TaobaoMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer)._setPropertyFloat(property, value);
        }
    }

    public long getPropertyLong(int property, long defaultValue) {
        if (this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null || !(this.mMediaPlayerRecycler.mMediaPlayer instanceof IjkMediaPlayer)) {
            return 0;
        }
        return ((IjkMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer)._getPropertyLong(property, defaultValue);
    }

    public Object getPropertyObject(int property) {
        if (this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null || !(this.mMediaPlayerRecycler.mMediaPlayer instanceof IjkMediaPlayer)) {
            return null;
        }
        return ((IjkMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer)._getPropertyObject(property);
    }

    private void initVideoView(Context context) {
        this.mContext = context;
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        this.mVideoSarNum = 0;
        this.mVideoSarDen = 0;
        this.mTargetState = 0;
        setBackgroundColor(context.getResources().getColor(17170444));
    }

    private void setCoverImg(int coverResId) {
        if (coverResId != 0 && !isInPlaybackState()) {
            if (this.mCoverImgView == null) {
                this.mCoverImgView = new ImageView(this.mContext);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
                params.gravity = 17;
                addView(this.mCoverImgView, params);
            }
            this.mCoverImgView.setVisibility(0);
            this.mCoverImgView.setImageResource(coverResId);
        }
    }

    public void setCoverImg(Drawable coverDrawable, boolean bFullscreen) {
        if (coverDrawable != null && !isInPlaybackState()) {
            if (this.mCoverImgView == null) {
                this.mCoverImgView = new ImageView(this.mContext);
                addView(this.mCoverImgView);
            }
            if (bFullscreen) {
                this.mCoverImgView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
                this.mCoverImgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
                params.gravity = 17;
                this.mCoverImgView.setLayoutParams(params);
            }
            this.mCoverImgView.setVisibility(0);
            this.mCoverImgView.setImageDrawable(coverDrawable);
        }
    }

    public void setAspectRatio(int aspectRatio) {
        if (this.mConfig != null) {
            this.mConfig.mScaleType = aspectRatio;
            if (this.mRenderView != null) {
                this.mRenderView.setAspectRatio(aspectRatio);
            }
        }
    }

    public void setRenderType(int type) {
        if (this.mConfig != null) {
            setRenderType(type, this.mConfig.mVRRenderType, this.mConfig.mVRLng, this.mConfig.mVRLat);
        }
    }

    public void setFirstRenderTime() {
        this.mStartTime = System.currentTimeMillis();
    }

    public void setRenderType(int type, int vrRenderType, int vrLng, int vrLat) {
        if (this.mConfig == null) {
            return;
        }
        if (this.mConfig.mRenderType != type || this.mConfig.mVRRenderType != vrRenderType || this.mConfig.mVRLng != vrLng || this.mConfig.mVRLat != vrLat) {
            _setRenderType(type, vrRenderType, vrLng, vrLat);
        }
    }

    private void _setRenderType(int type, int vrRenderType, int vrLng, int vrLat) {
        if (this.mRenderView != null) {
            if (!(this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null)) {
                this.mMediaPlayerRecycler.mMediaPlayer.setSurface((Surface) null);
            }
            removeView(this.mRenderView.getView());
            this.mRenderView.removeRenderCallback(this.mSHCallback);
            this.mRenderView = null;
        }
        IRenderView renderView = null;
        if (type == 1) {
            renderView = new SurfaceRenderView(getContext());
        } else if (type == 2) {
            renderView = new TextureRenderView(getContext());
        } else if (type == 3) {
            renderView = new TextureRenderView(getContext());
        }
        if (renderView != null) {
            this.mConfig.mRenderType = type;
            this.mConfig.mVRRenderType = vrRenderType;
            this.mConfig.mVRLng = vrLng;
            this.mConfig.mVRLat = vrLat;
            this.mRenderView = renderView;
            renderView.setAspectRatio(this.mConfig.mScaleType);
            if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
                renderView.setVideoSize(this.mVideoWidth, this.mVideoHeight);
            }
            if (this.mVideoSarNum > 0 && this.mVideoSarDen > 0) {
                renderView.setVideoSampleAspectRatio(this.mVideoSarNum, this.mVideoSarDen);
            }
            View renderUIView = this.mRenderView.getView();
            renderUIView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
            addView(renderUIView);
            this.mRenderUIView = renderUIView;
            this.mRenderView.addRenderCallback(this.mSHCallback);
            this.mRenderView.setVideoRotation(this.mVideoRotationDegree);
        }
    }

    private void setNextRenderView() {
        if (this.mConfig != null) {
            if (this.mConfig.mRenderType == 1) {
                this.mNextRenderView = new SurfaceRenderView(getContext());
            } else if (this.mConfig.mRenderType == 2) {
                this.mNextRenderView = new TextureRenderView(getContext());
            }
            this.mNextRenderView.setAspectRatio(this.mConfig.mScaleType);
            if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
                this.mNextRenderView.setVideoSize(this.mVideoWidth, this.mVideoHeight);
            }
            if (this.mVideoSarNum > 0 && this.mVideoSarDen > 0) {
                this.mNextRenderView.setVideoSampleAspectRatio(this.mVideoSarNum, this.mVideoSarDen);
            }
            addView(this.mNextRenderView.getView(), 0, new FrameLayout.LayoutParams(-2, -2, 17));
            this.mNextRenderView.addRenderCallback(this.mNextSHCallback);
            this.mNextRenderView.setVideoRotation(this.mVideoRotationDegree);
        }
    }

    public AbstractMediaPlayer initPlayer() {
        return openVideo(this.mPlayUrl, true, false);
    }

    public void release() {
        try {
            if (this.mNetworkReceiver != null) {
                this.mContext.unregisterReceiver(this.mNetworkReceiver);
            }
        } catch (Exception e) {
        }
        if (this.mConfig == null || !this.mConfig.mbEnableRecycle) {
            release(true);
        } else {
            MediaPlayerManager.getInstance().removePlayerFromCache(this.mConfig.mToken, this);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0080  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void release(boolean r7) {
        /*
            r6 = this;
            r5 = 0
            r4 = 0
            com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler r2 = r6.mMediaPlayerRecycler
            if (r2 != 0) goto L_0x0007
        L_0x0006:
            return
        L_0x0007:
            if (r7 == 0) goto L_0x0039
            java.lang.Runnable r2 = r6.mSwitchRunnable
            if (r2 == 0) goto L_0x0014
            java.lang.Runnable r2 = r6.mSwitchRunnable
            r6.removeCallbacks(r2)
            r6.mSwitchRunnable = r5
        L_0x0014:
            java.util.List<com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView$OnPauseListener> r2 = r6.mOnPauseListeners
            if (r2 == 0) goto L_0x0034
            java.util.List<com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView$OnPauseListener> r2 = r6.mOnPauseListeners
            java.util.Iterator r2 = r2.iterator()
        L_0x001e:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0034
            java.lang.Object r0 = r2.next()
            com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView$OnPauseListener r0 = (com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView.OnPauseListener) r0
            if (r0 == 0) goto L_0x001e
            com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler r3 = r6.mMediaPlayerRecycler
            tv.danmaku.ijk.media.player.AbstractMediaPlayer r3 = r3.mMediaPlayer
            r0.onPause(r3)
            goto L_0x001e
        L_0x0034:
            r6.mSeekWhenPrepared = r4
            r6.clearKeepScreenOn()
        L_0x0039:
            android.content.Context r2 = r6.mContext
            if (r2 == 0) goto L_0x0048
            android.content.Context r2 = r6.mContext
            android.content.Context r2 = r2.getApplicationContext()
            android.app.Application r2 = (android.app.Application) r2
            r2.unregisterActivityLifecycleCallbacks(r6)
        L_0x0048:
            com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler r2 = r6.mMediaPlayerRecycler
            tv.danmaku.ijk.media.player.AbstractMediaPlayer r2 = r2.mMediaPlayer
            if (r2 == 0) goto L_0x0093
            com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler r2 = r6.mMediaPlayerRecycler
            tv.danmaku.ijk.media.player.AbstractMediaPlayer r2 = r2.mMediaPlayer
            r2.resetListeners()
            com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler r2 = r6.mMediaPlayerRecycler     // Catch:{ Throwable -> 0x00b7 }
            tv.danmaku.ijk.media.player.AbstractMediaPlayer r2 = r2.mMediaPlayer     // Catch:{ Throwable -> 0x00b7 }
            boolean r2 = r2 instanceof tv.danmaku.ijk.media.player.IjkMediaPlayer     // Catch:{ Throwable -> 0x00b7 }
            if (r2 != 0) goto L_0x0065
            com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler r2 = r6.mMediaPlayerRecycler     // Catch:{ Throwable -> 0x00b7 }
            tv.danmaku.ijk.media.player.AbstractMediaPlayer r2 = r2.mMediaPlayer     // Catch:{ Throwable -> 0x00b7 }
            boolean r2 = r2 instanceof tv.danmaku.ijk.media.player.TaobaoMediaPlayer     // Catch:{ Throwable -> 0x00b7 }
            if (r2 == 0) goto L_0x00af
        L_0x0065:
            com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler r2 = r6.mMediaPlayerRecycler     // Catch:{ Throwable -> 0x00b7 }
            tv.danmaku.ijk.media.player.AbstractMediaPlayer r1 = r2.mMediaPlayer     // Catch:{ Throwable -> 0x00b7 }
            java.lang.Thread r2 = new java.lang.Thread     // Catch:{ Throwable -> 0x00b7 }
            com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView$7 r3 = new com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView$7     // Catch:{ Throwable -> 0x00b7 }
            r3.<init>(r1)     // Catch:{ Throwable -> 0x00b7 }
            r2.<init>(r3)     // Catch:{ Throwable -> 0x00b7 }
            r2.start()     // Catch:{ Throwable -> 0x00b7 }
        L_0x0076:
            com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler r2 = r6.mMediaPlayerRecycler
            r2.mMediaPlayer = r5
            com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler r2 = r6.mMediaPlayerRecycler
            r2.mPlayState = r4
            if (r7 == 0) goto L_0x0093
            com.taobao.taobaoavsdk.widget.media.IRenderView r2 = r6.mRenderView
            if (r2 == 0) goto L_0x0091
            com.taobao.taobaoavsdk.widget.media.IRenderView r2 = r6.mRenderView
            boolean r2 = r2 instanceof com.taobao.taobaoavsdk.widget.media.TextureRenderView
            if (r2 == 0) goto L_0x0091
            com.taobao.taobaoavsdk.widget.media.IRenderView r2 = r6.mRenderView
            com.taobao.taobaoavsdk.widget.media.TextureRenderView r2 = (com.taobao.taobaoavsdk.widget.media.TextureRenderView) r2
            r2.releaseSurface()
        L_0x0091:
            r6.mTargetState = r4
        L_0x0093:
            if (r7 == 0) goto L_0x0006
            com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler r2 = r6.mMediaPlayerRecycler
            tv.danmaku.ijk.media.player.AbstractMediaPlayer r2 = r2.mNextMediaPlayer
            if (r2 == 0) goto L_0x0006
            com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler r2 = r6.mMediaPlayerRecycler
            tv.danmaku.ijk.media.player.AbstractMediaPlayer r2 = r2.mNextMediaPlayer
            r2.resetListeners()
            com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler r2 = r6.mMediaPlayerRecycler
            tv.danmaku.ijk.media.player.AbstractMediaPlayer r2 = r2.mNextMediaPlayer
            r2.release()
            com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler r2 = r6.mMediaPlayerRecycler
            r2.mNextMediaPlayer = r5
            goto L_0x0006
        L_0x00af:
            com.taobao.taobaoavsdk.recycle.MediaPlayerRecycler r2 = r6.mMediaPlayerRecycler     // Catch:{ Throwable -> 0x00b7 }
            tv.danmaku.ijk.media.player.AbstractMediaPlayer r2 = r2.mMediaPlayer     // Catch:{ Throwable -> 0x00b7 }
            r2.release()     // Catch:{ Throwable -> 0x00b7 }
            goto L_0x0076
        L_0x00b7:
            r2 = move-exception
            goto L_0x0076
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView.release(boolean):void");
    }

    public int getVideoWidth() {
        if (this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null) {
            return 0;
        }
        return this.mMediaPlayerRecycler.mMediaPlayer.getVideoWidth();
    }

    public int getVideoHeight() {
        if (this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null) {
            return 0;
        }
        return this.mMediaPlayerRecycler.mMediaPlayer.getVideoHeight();
    }

    public void setLooping(boolean looping) {
        this.bLooping = looping;
        if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
            this.mMediaPlayerRecycler.mMediaPlayer.setLooping(looping);
        }
    }

    public void setVolume(float leftVolume, float rightVolume) {
        if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
            this.mMediaPlayerRecycler.mMediaPlayer.setVolume(leftVolume, rightVolume);
        }
    }

    public void setMuted(boolean muted) {
        this.bmuted = muted;
        if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null) {
            this.mMediaPlayerRecycler.mMediaPlayer.setMuted(muted);
        }
    }

    public void setAudioOnly(boolean audioOnly) {
        if (this.mConfig != null && this.mConfig.mScenarioType == 0) {
            this.bAudioOnly = audioOnly;
        }
    }

    public void requestAudioFocus(boolean bFocus) {
        try {
            AudioManager am = (AudioManager) this.mContext.getSystemService("audio");
            if (bFocus) {
                am.requestAudioFocus((AudioManager.OnAudioFocusChangeListener) null, 3, 1);
            } else {
                am.abandonAudioFocus((AudioManager.OnAudioFocusChangeListener) null);
            }
        } catch (Exception e) {
        }
    }

    public void setTimeout(long timeout_us) {
        if (timeout_us > 0) {
            this.timeOutUs = timeout_us;
        } else {
            this.timeOutUs = 10000000;
        }
        if (this.mMediaPlayerRecycler != null && this.mMediaPlayerRecycler.mMediaPlayer != null && (this.mMediaPlayerRecycler.mMediaPlayer instanceof IjkMediaPlayer)) {
            ((IjkMediaPlayer) this.mMediaPlayerRecycler.mMediaPlayer)._setOption(1, MtopJSBridge.MtopJSParam.TIMEOUT, this.timeOutUs);
        }
    }

    private AbstractMediaPlayer openVideo(String url, boolean bCurPlayer, boolean bDegrade) {
        AbstractMediaPlayer mediaPlayer;
        if (TextUtils.isEmpty(url) || this.mConfig == null || this.mMediaPlayerRecycler == null || this.mContext == null) {
            return null;
        }
        AbstractMediaPlayer mediaPlayer2 = null;
        if (bCurPlayer) {
            try {
                ((Application) this.mContext.getApplicationContext()).registerActivityLifecycleCallbacks(this);
            } catch (Throwable th) {
            }
        }
        if (this.bPlayerTypeChanged) {
            if (this.mRenderUIView != null) {
                this.mRenderUIView.setVisibility(8);
                this.mRenderUIView.setVisibility(0);
            }
            this.bPlayerTypeChanged = false;
        }
        if (!bDegrade) {
            if (MediaAdapteManager.mABTestAdapter != null && MediaConstant.ABTEST_USE_TAOBAOPLAYER.equals(MediaAdapteManager.mABTestAdapter.getBucket(MediaConstant.ABTEST_PLAYERCORE_COMOPONENT, MediaConstant.ABTEST_PLAYERCORE_MODULE))) {
                this.mConfig.mPlayerType = 3;
            }
            if (MediaAdapteManager.mConfigAdapter != null) {
                String keyStr = this.mConfig.mBusinessId;
                if (!TextUtils.isEmpty(this.mConfig.mSubBusinessType)) {
                    keyStr = keyStr + "-" + this.mConfig.mSubBusinessType;
                }
                this.mConfig.mPlayerType = AndroidUtils.getPlayerCore(MediaAdapteManager.mConfigAdapter, MediaConstant.DW_ORANGE_GROUP_NAME, keyStr, this.mConfig.mPlayerType);
                if ("TBLive".equals(this.mConfig.mBusinessId) && this.mConfig.mScenarioType == 0) {
                    setH264Hardware(this.mConfig);
                    setH265Hardware(this.mConfig);
                }
            }
            if (this.mConfig.mScenarioType == 2 && this.mConfig.mPlayerType == 3 && !TextUtils.isEmpty(this.mPlayUrl) && !this.mPlayUrl.contains(HuasuVideo.TAG_M3U8) && this.mPlayUrl.contains("homepage") && !this.mPlayUrl.startsWith("http") && !this.mPlayUrl.startsWith("artp") && MediaAdapteManager.mConfigAdapter != null && AndroidUtils.isInList(Build.MODEL, MediaAdapteManager.mConfigAdapter.getConfig(MediaConstant.DW_ORANGE_GROUP_NAME, "mediaDeviceWhiteList", "[\"SM-C5000\",\"TRT-AL00\",\"TRT-AL00A\",\"Redmi 6\",\"TRT-TL10A\"]"))) {
                this.mConfig.mPlayerType = 2;
            }
            if (Build.VERSION.SDK_INT <= 18 && this.mConfig.mPlayerType == 3) {
                this.mConfig.mPlayerType = 1;
            }
        }
        if (this.mConfig.mPlayerType == 1) {
            mediaPlayer = new IjkMediaPlayer(this.mContext, this.mConfigAdapter, this.mCustomLibLoader);
            try {
                ((IjkMediaPlayer) mediaPlayer)._setPropertyLong(IjkMediaPlayer.FFP_PROP_INT64_FIRST_FRAME_RENDERING_OPT, 1);
                ((IjkMediaPlayer) mediaPlayer)._setOption(1, MtopJSBridge.MtopJSParam.TIMEOUT, this.timeOutUs);
                long nettrafic = AndroidUtils.parseLong(this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, TBLIVE_ORANGE_NETWORK_TRAFFIC_REPORT, "2000000") : "2000000");
                IjkMediaPlayer ijkMediaPlayer = (IjkMediaPlayer) mediaPlayer;
                if (nettrafic < 614400) {
                    nettrafic = 614400;
                }
                ijkMediaPlayer._setPropertyLong(IjkMediaPlayer.FFP_PROP_NETWORK_TRAFFIC_REPORT_TRIGGER, nettrafic);
                float accelerate_speed = 1.0f;
                if (this.mConfig.mScenarioType == 0) {
                    accelerate_speed = AndroidUtils.parseFloat(this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, TBLIVE_ORANGE_ACCELERATESPEED, "1.0") : "1.0");
                } else if (this.mConfig.mScenarioType == 1) {
                    accelerate_speed = AndroidUtils.parseFloat(this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, TBLIVE_ORANGE_ACCELERATESPEED_LINK, "1.0") : "1.0");
                }
                if (accelerate_speed > 1.2f) {
                    accelerate_speed = 1.2f;
                }
                if (accelerate_speed > 1.0f) {
                    ((IjkMediaPlayer) mediaPlayer)._setPropertyFloat(21009, accelerate_speed);
                }
                float slow_speed = 1.0f;
                if (this.mConfig.mScenarioType == 0) {
                    slow_speed = AndroidUtils.parseFloat(this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, TBLIVE_ORANGE_SLOWSPEED, "1.0") : "1.0");
                } else if (this.mConfig.mScenarioType == 1) {
                    slow_speed = AndroidUtils.parseFloat(this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, TBLIVE_ORANGE_SLOWSPEED_LINK, "1.0") : "1.0");
                }
                if (slow_speed < 0.8f) {
                    slow_speed = 0.8f;
                }
                if (slow_speed < 1.0f) {
                    ((IjkMediaPlayer) mediaPlayer)._setPropertyFloat(IjkMediaPlayer.FFP_PROP_AUDIO_SLOWSPEED, slow_speed);
                }
                ((IjkMediaPlayer) mediaPlayer)._setPropertyLong(21008, this.bmuted ? 1 : 0);
                if (AndroidUtils.parseBoolean(this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, TBLIVE_ORANGE_SENDSEI, "false") : "false")) {
                    ((IjkMediaPlayer) mediaPlayer)._setPropertyLong(20111, 1);
                }
                if (this.mConfig.mScenarioType == 0) {
                    ((IjkMediaPlayer) mediaPlayer)._setPropertyLong(IjkMediaPlayer.FFP_PROP_FIRSTPLAY_NEED_TIME, AndroidUtils.parseLong(this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, TBLIVE_ORANGE_FIRSTPLAY_BUFFER_TIME, "800") : "800"));
                    ((IjkMediaPlayer) mediaPlayer)._setPropertyLong(IjkMediaPlayer.FFP_PROP_PLAYBUFFER_NEED_TIME, AndroidUtils.parseLong(this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, TBLIVE_ORANGE_PLAYBUFFER_TIME, "3000") : "3000"));
                }
                if (this.bAudioOnly) {
                    ((IjkMediaPlayer) mediaPlayer)._setPropertyLong(IjkMediaPlayer.FFP_PROP_ONLY_ONE_STREAM, 1);
                }
                mediaPlayer2 = mediaPlayer;
            } catch (Throwable th2) {
                mediaPlayer2 = mediaPlayer;
                if (bCurPlayer) {
                    this.mMediaPlayerRecycler.mPlayState = -1;
                    this.mTargetState = -1;
                }
                if (this.mConfig.mPlayerType == 3) {
                    release(false);
                    this.mConfig.mPlayerType = 1;
                    return openVideo(url, bCurPlayer, true);
                } else if (this.mConfig.mPlayerType == 1 && this.mConfig.mScenarioType == 2) {
                    release(false);
                    this.mConfig.mPlayerType = 2;
                    return openVideo(url, bCurPlayer, true);
                } else if (!bCurPlayer) {
                    return null;
                } else {
                    this.mErrorListener.onError(mediaPlayer2, 1, 0);
                    return mediaPlayer2;
                }
            }
        } else {
            if (this.mConfig.mPlayerType == 2) {
                mediaPlayer = new NativeMediaPlayer(this.mContext, this.mConfigAdapter);
                ((NativeMediaPlayer) mediaPlayer).setAudioStreamType(3);
                if (this.bmuted) {
                    mediaPlayer.setVolume(0.0f, 0.0f);
                    mediaPlayer2 = mediaPlayer;
                }
            } else if (this.mConfig.mPlayerType == 3) {
                mediaPlayer = new TaobaoMediaPlayer(this.mContext, this.mConfigAdapter);
                ((TaobaoMediaPlayer) mediaPlayer)._setPropertyLong(21008, this.bmuted ? 1 : 0);
                if (AndroidUtils.parseBoolean(this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, TBLIVE_ORANGE_SENDSEI, "false") : "false")) {
                    ((TaobaoMediaPlayer) mediaPlayer)._setPropertyLong(20111, 1);
                }
            }
            mediaPlayer2 = mediaPlayer;
        }
        ((MonitorMediaPlayer) mediaPlayer2).setConfig(this.mConfig);
        if ("TBLive".equals(this.mConfig.mBusinessId) && Build.VERSION.SDK_INT >= 21) {
            if (AndroidUtils.parseBoolean(this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(MediaConstant.DW_ORANGE_GROUP_NAME, MonitorMediaPlayer.PCDN_FOR_LIVE_ENABLE, "false") : "false")) {
                ApplicationUtils.pcdnStartOnce();
                ((MonitorMediaPlayer) mediaPlayer2).setABtestAdapter(new TaobaoABTestAdapter());
            }
        }
        if (this.mExtInfo != null) {
            ((MonitorMediaPlayer) mediaPlayer2).setExtInfo(this.mExtInfo);
            this.mExtInfo = null;
        }
        ((MonitorMediaPlayer) mediaPlayer2).setFirstRenderAdapter(new FirstRenderAdapter() {
            public long getStartTime() {
                return TaoLiveVideoView.this.mStartTime;
            }
        });
        mediaPlayer2.setLooping(this.bLooping);
        if (bCurPlayer) {
            mediaPlayer2.registerOnPreparedListener(this.mPreparedListener);
            mediaPlayer2.registerOnVideoSizeChangedListener(this.mSizeChangedListener);
            mediaPlayer2.registerOnCompletionListener(this.mCompletionListener);
            mediaPlayer2.registerOnErrorListener(this.mErrorListener);
            mediaPlayer2.registerOnInfoListener(this.mInfoListener);
            mediaPlayer2.registerOnBufferingUpdateListener(this.mBufferingUpdateListener);
            this.mCurrentBufferPercentage = 0;
            setVideoPath(url, mediaPlayer2);
        } else {
            mediaPlayer2.setDataSource(url);
        }
        if ((mediaPlayer2 instanceof IjkMediaPlayer) && url.contains(TBLIVE_ARTP_SCHEMA)) {
            ((IjkMediaPlayer) mediaPlayer2)._setOption(1, "cdn_ip", this.cdn_ip);
        } else if ((mediaPlayer2 instanceof TaobaoMediaPlayer) && url.contains(TBLIVE_ARTP_SCHEMA)) {
            ((TaobaoMediaPlayer) mediaPlayer2)._setOption(1, "cdn_ip", this.cdn_ip);
        }
        if (this.mPropertyLong != null) {
            for (int i = 0; i < this.mPropertyLong.size(); i++) {
                int key = this.mPropertyLong.keyAt(i);
                Long value = this.mPropertyLong.valueAt(i);
                if (mediaPlayer2 instanceof IjkMediaPlayer) {
                    ((IjkMediaPlayer) mediaPlayer2)._setPropertyLong(key, value != null ? value.longValue() : 0);
                } else if (mediaPlayer2 instanceof TaobaoMediaPlayer) {
                    ((TaobaoMediaPlayer) mediaPlayer2)._setPropertyLong(key, value != null ? value.longValue() : 0);
                }
            }
            this.mPropertyLong.clear();
        }
        if (this.mPropertyFloat != null) {
            for (int i2 = 0; i2 < this.mPropertyFloat.size(); i2++) {
                int key2 = this.mPropertyFloat.keyAt(i2);
                Float value2 = this.mPropertyFloat.valueAt(i2);
                if (mediaPlayer2 instanceof IjkMediaPlayer) {
                    ((IjkMediaPlayer) mediaPlayer2)._setPropertyFloat(key2, value2 != null ? value2.floatValue() : 0.0f);
                } else if (mediaPlayer2 instanceof TaobaoMediaPlayer) {
                    ((TaobaoMediaPlayer) mediaPlayer2)._setPropertyFloat(key2, value2 != null ? value2.floatValue() : 0.0f);
                }
            }
            this.mPropertyFloat.clear();
        }
        if (bCurPlayer) {
            bindSurfaceHolder(mediaPlayer2, this.mSurfaceHolder);
        }
        mediaPlayer2.setScreenOnWhilePlaying(true);
        this.bufferLoadCountTimeInterval = Integer.parseInt(this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, "BufferLoadCountTimeIntervalMilliseconds", "5") : "5");
        if (this.bufferLoadCountTimeInterval <= 0) {
            this.bufferLoadCountTimeInterval = TBMPBBufferLoadCountTimeInterval;
        }
        this.bufferLoadCountLimit = Integer.parseInt(this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, "BufferLoadCountLimit", "5") : "5");
        if (this.bufferLoadCountLimit <= 0) {
            this.bufferLoadCountLimit = 3;
        }
        if (this.mLogAdapter != null) {
            this.mLogAdapter.onLogi(TAG, "player prepareAsync start");
        }
        mediaPlayer2.prepareAsync();
        if (this.mLogAdapter != null) {
            this.mLogAdapter.onLogi(TAG, "player prepareAsync end");
        }
        if (!bCurPlayer) {
            return mediaPlayer2;
        }
        this.mMediaPlayerRecycler.mPlayState = 1;
        return mediaPlayer2;
    }

    public void prepareAsync() {
        if (this.mConfig != null && this.mMediaPlayerRecycler != null) {
            if (this.mConfig.mbEnableRecycle) {
                if (this.mMediaPlayerRecycler.mRecycled) {
                    this.mMediaPlayerRecycler = MediaPlayerManager.getInstance().getMediaRecyclerAfterRecycled(this.mMediaPlayerRecycler);
                } else {
                    this.mMediaPlayerRecycler = MediaPlayerManager.getInstance().getMediaRecycler(this.mConfig.mToken, this);
                }
            }
            if (this.mMediaPlayerRecycler.mMediaPlayer == null) {
                this.mMediaPlayerRecycler.mMediaPlayer = initPlayer();
            }
            if (this.mConfig.mbEnableRecycle && this.mMediaPlayerRecycler.mRecycled) {
                this.mMediaPlayerRecycler.mRecycled = false;
            }
            if (this.mMediaPlayerRecycler.mMediaPlayer != null && this.mMediaPlayerRecycler.mPlayState == 0) {
                try {
                    this.mMediaPlayerRecycler.mMediaPlayer.prepareAsync();
                } catch (IjkMediaException e) {
                    e.printStackTrace();
                }
                this.mMediaPlayerRecycler.mPlayState = 1;
            }
            this.mTargetState = 1;
        }
    }

    public void start() {
        if (this.mConfig != null && this.mMediaPlayerRecycler != null) {
            this.bAutoPause = false;
            if (this.mLogAdapter != null) {
                this.mLogAdapter.onLogi(TAG, "player start,mMediaPlayer :" + String.valueOf(this.mMediaPlayerRecycler.mMediaPlayer));
            }
            if (this.mConfig.mbEnableRecycle) {
                if (this.mMediaPlayerRecycler.mRecycled) {
                    this.mMediaPlayerRecycler = MediaPlayerManager.getInstance().getMediaRecyclerAfterRecycled(this.mMediaPlayerRecycler);
                } else {
                    this.mMediaPlayerRecycler = MediaPlayerManager.getInstance().getMediaRecycler(this.mConfig.mToken, this);
                }
            }
            if (this.mMediaPlayerRecycler.mMediaPlayer == null) {
                if (this.mLogAdapter != null) {
                    this.mLogAdapter.onLogi(TAG, "player start init");
                }
                this.mMediaPlayerRecycler.mMediaPlayer = initPlayer();
            }
            if (this.mConfig.mbEnableRecycle) {
                if (this.mMediaPlayerRecycler.mRecycled) {
                    this.mMediaPlayerRecycler.mRecycled = false;
                    if (this.mMediaPlayerRecycler.mMediaPlayer != null) {
                        if (this.mMediaPlayerRecycler.mLastState == 4) {
                            seekTo(this.mMediaPlayerRecycler.mLastPosition);
                        } else if (this.mMediaPlayerRecycler.mLastState == 5) {
                            seekTo(0);
                        } else if (this.mMediaPlayerRecycler.mLastState == 3) {
                            seekTo(this.mMediaPlayerRecycler.mLastPosition);
                            start();
                        }
                    }
                } else if (this.mMediaPlayerRecycler.mMediaPlayer != null) {
                    changeVideoSize(this.mMediaPlayerRecycler.mMediaPlayer.getVideoWidth(), this.mMediaPlayerRecycler.mMediaPlayer.getVideoHeight(), this.mMediaPlayerRecycler.mMediaPlayer.getVideoSarNum(), this.mMediaPlayerRecycler.mMediaPlayer.getVideoSarDen());
                }
            }
            if (isInPlaybackState() && this.mSurfaceHolder != null) {
                if (this.mLogAdapter != null) {
                    this.mLogAdapter.onLogi(TAG, "player start begin");
                }
                bindSurfaceHolder(this.mMediaPlayerRecycler.mMediaPlayer, this.mSurfaceHolder);
                this.mMediaPlayerRecycler.mMediaPlayer.start();
                keepScreenOn();
                try {
                    if (this.mNetworkReceiver == null) {
                        this.mNetworkReceiver = new NetworkBroadcastReceiver();
                    }
                    this.mContext.registerReceiver(this.mNetworkReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
                } catch (Exception e) {
                }
                if (this.mOnStartListeners != null) {
                    for (OnStartListener listener : this.mOnStartListeners) {
                        if (listener != null) {
                            listener.onStart(this.mMediaPlayerRecycler.mMediaPlayer);
                        }
                    }
                }
                if (this.mLogAdapter != null) {
                    this.mLogAdapter.onLogi(TAG, "player start end");
                }
                this.mMediaPlayerRecycler.mPlayState = 3;
            }
            this.mTargetState = 3;
        }
    }

    @Deprecated
    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        this.mOnPreparedListener = l;
    }

    public void registerOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        if (this.mOnPreparedListeners == null) {
            this.mOnPreparedListeners = new LinkedList();
        }
        this.mOnPreparedListeners.add(l);
    }

    public void unregisterOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        if (this.mOnPreparedListeners != null) {
            this.mOnPreparedListeners.remove(l);
        }
    }

    public void registerOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener l) {
        if (this.mOnBufferingUpdateListeners == null) {
            this.mOnBufferingUpdateListeners = new LinkedList();
        }
        this.mOnBufferingUpdateListeners.add(l);
    }

    public void unregisterOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener l) {
        if (this.mOnBufferingUpdateListeners != null) {
            this.mOnBufferingUpdateListeners.remove(l);
        }
    }

    @Deprecated
    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        this.mOnCompletionListener = l;
    }

    public void registerOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        if (this.mOnCompletionListeners == null) {
            this.mOnCompletionListeners = new LinkedList();
        }
        this.mOnCompletionListeners.add(l);
    }

    public void unregisterOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        if (this.mOnCompletionListeners != null) {
            this.mOnCompletionListeners.remove(l);
        }
    }

    @Deprecated
    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
        this.mOnErrorListener = l;
    }

    public void registerOnErrorListener(IMediaPlayer.OnErrorListener l) {
        if (this.mOnErrorListeners == null) {
            this.mOnErrorListeners = new LinkedList();
        }
        this.mOnErrorListeners.add(l);
    }

    public void unregisterOnErrorListener(IMediaPlayer.OnErrorListener l) {
        if (this.mOnErrorListeners != null) {
            this.mOnErrorListeners.remove(l);
        }
    }

    @Deprecated
    public void setOnInfoListener(IMediaPlayer.OnInfoListener l) {
        this.mOnInfoListener = l;
    }

    public void registerOnInfoListener(IMediaPlayer.OnInfoListener l) {
        if (this.mOnInfoListeners == null) {
            this.mOnInfoListeners = new LinkedList();
        }
        this.mOnInfoListeners.add(l);
    }

    public void unregisterOnInfoListener(IMediaPlayer.OnInfoListener l) {
        if (this.mOnInfoListeners != null) {
            this.mOnInfoListeners.remove(l);
        }
    }

    public void registerOnStartListener(OnStartListener l) {
        if (this.mOnStartListeners == null) {
            this.mOnStartListeners = new LinkedList();
        }
        this.mOnStartListeners.add(l);
    }

    public void unregisterOnStartListener(OnStartListener l) {
        if (this.mOnStartListeners != null) {
            this.mOnStartListeners.remove(l);
        }
    }

    public void registerOnPauseListener(OnPauseListener l) {
        if (this.mOnPauseListeners == null) {
            this.mOnPauseListeners = new LinkedList();
        }
        this.mOnPauseListeners.add(l);
    }

    public void unregisterOnPauseListener(OnPauseListener l) {
        if (this.mOnPauseListeners != null) {
            this.mOnPauseListeners.remove(l);
        }
    }

    public void registerOnVideoClickListener(IMediaPlayer.OnVideoClickListener listener) {
        if (this.mOnVideoClickListeners == null) {
            this.mOnVideoClickListeners = new LinkedList();
        }
        this.mOnVideoClickListeners.add(listener);
    }

    public void unregisterOnVideoClickListener(IMediaPlayer.OnVideoClickListener listener) {
        if (this.mOnVideoClickListeners != null) {
            this.mOnVideoClickListeners.remove(listener);
        }
    }

    private void notifyOnVideoClick(int x, int y, int w, int h, int index, String seiData) {
        if (this.mOnVideoClickListeners != null) {
            for (IMediaPlayer.OnVideoClickListener listener : this.mOnVideoClickListeners) {
                listener.onClick(x, y, w, h, index, seiData);
            }
        }
    }

    private void setVideoPath(String path, AbstractMediaPlayer player) {
        if (path != null) {
            this.mPlayUrl = path;
            if (this.mPlayUrl.startsWith(WVUtils.URL_SEPARATOR)) {
                this.mPlayUrl = "http:" + this.mPlayUrl;
            }
            if (this.mMediaPlayerRecycler != null && player != null && this.mMediaPlayerRecycler.mPlayState == 0) {
                String playUrl = this.mPlayUrl;
                if (this.bAudioOnly) {
                    StringBuilder appendQuery = new StringBuilder(50);
                    appendQuery.append("onlyaudio=1");
                    playUrl = AndroidUtils.appendUri(this.mPlayUrl, appendQuery);
                }
                try {
                    player.setDataSource(playUrl);
                } catch (Exception e) {
                    this.mMediaPlayerRecycler.mPlayState = -1;
                    this.mTargetState = -1;
                    this.mErrorListener.onError(player, 1, 0);
                }
            }
        }
    }

    public void setVideoPath(String path) {
        if (this.mMediaPlayerRecycler != null) {
            setVideoPath(path, this.mMediaPlayerRecycler.mMediaPlayer);
        }
    }

    private void pause(boolean byUser) {
        if (this.mMediaPlayerRecycler != null && this.mTargetState != 4) {
            if (!byUser) {
                this.bAutoPause = true;
            }
            if (this.mMediaPlayerRecycler.mMediaPlayer != null && isPlaying()) {
                if (this.mLogAdapter != null) {
                    this.mLogAdapter.onLogi(TAG, "player pause begin");
                }
                try {
                    if (this.mNetworkReceiver != null) {
                        this.mContext.unregisterReceiver(this.mNetworkReceiver);
                    }
                } catch (Exception e) {
                }
                this.mMediaPlayerRecycler.mMediaPlayer.pause();
                clearKeepScreenOn();
                if (this.mOnPauseListeners != null) {
                    for (OnPauseListener listener : this.mOnPauseListeners) {
                        if (listener != null) {
                            listener.onPause(this.mMediaPlayerRecycler.mMediaPlayer);
                        }
                    }
                }
                if (this.mLogAdapter != null) {
                    this.mLogAdapter.onLogi(TAG, "player pause end");
                }
                if (this.mConfig != null && this.mConfig.mbEnableRecycle) {
                    MediaPlayerManager.getInstance().reorderLruMediaPlayer();
                }
                this.mMediaPlayerRecycler.mPlayState = 4;
            }
            this.mTargetState = 4;
        }
    }

    public void pause() {
        this.bAutoPause = false;
        pause(true);
    }

    public int getDuration() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayerRecycler.mMediaPlayer.getDuration();
        }
        return -1;
    }

    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayerRecycler.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getCurrentState() {
        if (this.mMediaPlayerRecycler != null) {
            return this.mMediaPlayerRecycler.mPlayState;
        }
        return 0;
    }

    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            if (this.mLogAdapter != null) {
                this.mLogAdapter.onLogi(TAG, "player seekTo begin: " + msec);
            }
            this.mMediaPlayerRecycler.mMediaPlayer.seekTo((long) msec);
            if (this.mLogAdapter != null) {
                this.mLogAdapter.onLogi(TAG, "player seekTo end: " + msec);
            }
            this.mSeekWhenPrepared = 0;
            return;
        }
        this.mSeekWhenPrepared = msec;
    }

    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayerRecycler.mMediaPlayer.isPlaying();
    }

    public int getDestoryState() {
        return 0;
    }

    public int getBufferPercentage() {
        if (this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null) {
            return 0;
        }
        return this.mCurrentBufferPercentage;
    }

    public void switchVideoPathSyncFrame(String path) {
        if (this.mbIsSwitchingPath || TextUtils.isEmpty(path) || this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mNextMediaPlayer != null || this.mMediaPlayerRecycler.mMediaPlayer == null || !(this.mMediaPlayerRecycler.mMediaPlayer instanceof IjkMediaPlayer) || !isPlaying()) {
            switchPathError(path, -748);
            return;
        }
        this.mbIsSwitchingPath = true;
        int retain = this.mConfigAdapter != null ? AndroidUtils.parseInt(this.mConfigAdapter.getConfig(MediaConstant.DW_ORANGE_GROUP_NAME, TBLIVE_ORANGE_RETAIN_FLV, "0")) : 0;
        if (retain > 0) {
            if (path.contains(WVUtils.URL_DATA_CHAR)) {
                path = path + "&ali_flv_retain=" + retain + "s";
            } else {
                path = path + "?ali_flv_retain=" + retain + "s";
            }
        }
        final String switchPath = path;
        if (this.mSwitchRunnable == null) {
            this.mSwitchRunnable = new Runnable() {
                public void run() {
                    TaoLiveVideoView.this.switchPathError(switchPath, -1314);
                }
            };
        }
        postDelayed(this.mSwitchRunnable, 15000);
        this.mMediaPlayerRecycler.mNextMediaPlayer = openVideo(path, false, false);
        if (this.mMediaPlayerRecycler.mNextMediaPlayer != null) {
            setNextRenderView();
            ((MonitorMediaPlayer) this.mMediaPlayerRecycler.mNextMediaPlayer).bNeedCommitPlayExperience = false;
            this.mMediaPlayerRecycler.mNextMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                public void onPrepared(IMediaPlayer mp) {
                    if (TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer != null && TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer != null) {
                        TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer.setOnErrorListener((IMediaPlayer.OnErrorListener) null);
                        TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer.setOnPreparedListener((IMediaPlayer.OnPreparedListener) null);
                        ((IjkMediaPlayer) TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer)._setPropertyFloat(10006, (float) ((IjkMediaPlayer) TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer)._switchPathSyncFrame());
                        TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer.start();
                    }
                }
            });
            this.mMediaPlayerRecycler.mNextMediaPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
                public boolean onError(IMediaPlayer mp, int what, int extra) {
                    TaoLiveVideoView.this.switchPathError(switchPath, what);
                    return false;
                }
            });
            this.mMediaPlayerRecycler.mNextMediaPlayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
                public boolean onInfo(IMediaPlayer mp, long what, long extra, long ext, Object obj) {
                    if (!TaoLiveVideoView.this.mbIsSwitchingPath || what != 3) {
                        return false;
                    }
                    boolean unused = TaoLiveVideoView.this.mbIsSwitchingPath = false;
                    if (TaoLiveVideoView.this.mSwitchRunnable != null) {
                        TaoLiveVideoView.this.removeCallbacks(TaoLiveVideoView.this.mSwitchRunnable);
                        Runnable unused2 = TaoLiveVideoView.this.mSwitchRunnable = null;
                    }
                    if (TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer == null || TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer == null) {
                        return false;
                    }
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer.setOnInfoListener((IMediaPlayer.OnInfoListener) null);
                    ((MonitorMediaPlayer) TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer).bNeedCommitPlayExperience = false;
                    ((MonitorMediaPlayer) TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer).bNeedCommitPlayExperience = true;
                    ((MonitorMediaPlayer) TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer).mFirstRenderTime = ((MonitorMediaPlayer) TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer).mFirstRenderTime;
                    ((MonitorMediaPlayer) TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer).mUserFirstRenderTime = ((MonitorMediaPlayer) TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer).mUserFirstRenderTime;
                    TaoLiveVideoView.this.removeView(TaoLiveVideoView.this.mRenderUIView);
                    TaoLiveVideoView.this.release(false);
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer = TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer;
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer = null;
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mPlayState = 3;
                    TaoLiveVideoView.this.mRenderView = TaoLiveVideoView.this.mNextRenderView;
                    TaoLiveVideoView.this.mSurfaceHolder = TaoLiveVideoView.this.mNextSurfaceHolder;
                    TaoLiveVideoView.this.mRenderView.removeRenderCallback(TaoLiveVideoView.this.mNextSHCallback);
                    TaoLiveVideoView.this.mRenderView.addRenderCallback(TaoLiveVideoView.this.mSHCallback);
                    TaoLiveVideoView.this.mRenderUIView = TaoLiveVideoView.this.mRenderView.getView();
                    IRenderView unused3 = TaoLiveVideoView.this.mNextRenderView = null;
                    IRenderView.ISurfaceHolder unused4 = TaoLiveVideoView.this.mNextSurfaceHolder = null;
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer.registerOnPreparedListener(TaoLiveVideoView.this.mPreparedListener);
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer.registerOnVideoSizeChangedListener(TaoLiveVideoView.this.mSizeChangedListener);
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer.registerOnCompletionListener(TaoLiveVideoView.this.mCompletionListener);
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer.registerOnErrorListener(TaoLiveVideoView.this.mErrorListener);
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer.registerOnInfoListener(TaoLiveVideoView.this.mInfoListener);
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer.registerOnBufferingUpdateListener(TaoLiveVideoView.this.mBufferingUpdateListener);
                    if (TaoLiveVideoView.this.mInfoListener != null) {
                        TaoLiveVideoView.this.mInfoListener.onInfo(TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer, 719, 0, 0, (Object) null);
                    }
                    try {
                        TBS.Adv.ctrlClicked("Page_Video", CT.Button, "SwitchPath", "feed_id=" + TaoLiveVideoView.this.mConfig.mFeedId, "url_before=" + TaoLiveVideoView.this.mPlayUrl, "url_after=" + switchPath, "is_success=1", "error_code=0");
                    } catch (Throwable th) {
                    }
                    TaoLiveVideoView.this.mPlayUrl = switchPath;
                    return false;
                }
            });
        }
    }

    private void setH265Hardware(TaoLiveVideoViewConfig config) {
        if (config.mDecoderTypeH265 != 1) {
            if (Build.VERSION.SDK_INT >= 23 && this.mConfigAdapter != null && AndroidUtils.parseBoolean(this.mConfigAdapter.getConfig(config.mConfigGroup, "h265EnableHardware", "true"))) {
                if (AndroidUtils.isInList(AndroidUtils.getCPUName(), this.mConfigAdapter.getConfig(config.mConfigGroup, "h265HardwareDecodeWhiteList", ""))) {
                    if (!AndroidUtils.isInList(Build.MODEL, this.mConfigAdapter.getConfig(config.mConfigGroup, "h265HardwareDecodeBlackList", ""))) {
                        config.mDecoderTypeH265 = 1;
                    }
                }
            }
        }
    }

    private void setH264Hardware(TaoLiveVideoViewConfig config) {
        if (config.mDecoderTypeH264 != 1) {
            if (Build.VERSION.SDK_INT >= 23 && this.mConfigAdapter != null && AndroidUtils.parseBoolean(this.mConfigAdapter.getConfig(config.mConfigGroup, "h264EnableHardware", "true"))) {
                if (AndroidUtils.isInList(AndroidUtils.getCPUName(), this.mConfigAdapter.getConfig(config.mConfigGroup, MediaConstant.ORANGE_HARDWARE_H264_WHITE, ""))) {
                    if (!AndroidUtils.isInList(Build.MODEL, this.mConfigAdapter.getConfig(config.mConfigGroup, MediaConstant.ORANGE_HARDWARE_H264_BLACK, ""))) {
                        config.mDecoderTypeH264 = 1;
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void switchPathError(String path, int errorCode) {
        this.mbIsSwitchingPath = false;
        if (this.mSwitchRunnable != null) {
            removeCallbacks(this.mSwitchRunnable);
            this.mSwitchRunnable = null;
        }
        if (this.mMediaPlayerRecycler.mNextMediaPlayer != null) {
            this.mMediaPlayerRecycler.mNextMediaPlayer.resetListeners();
            this.mMediaPlayerRecycler.mNextMediaPlayer.release();
            this.mMediaPlayerRecycler.mNextMediaPlayer = null;
        }
        if (this.mNextRenderView != null) {
            removeView(this.mNextRenderView.getView());
            this.mNextRenderView = null;
        }
        this.mNextSurfaceHolder = null;
        if (this.mInfoListener != null) {
            this.mInfoListener.onInfo((IMediaPlayer) null, 718, 0, 0, (Object) null);
        }
        try {
            TBS.Adv.ctrlClicked("Page_Video", CT.Button, "SwitchPath", "feed_id=" + this.mConfig.mFeedId, "url_before=" + this.mPlayUrl, "url_after=" + path, "is_success=0", "error_code=" + errorCode);
        } catch (Throwable th) {
        }
    }

    public boolean switchVideoPath(String path, final boolean bNeedSeek) {
        if (this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mNextMediaPlayer != null) {
            return false;
        }
        this.mMediaPlayerRecycler.mNextMediaPlayer = openVideo(path, false, false);
        if (this.mMediaPlayerRecycler.mNextMediaPlayer == null) {
            return false;
        }
        this.mMediaPlayerRecycler.mNextMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            public void onPrepared(IMediaPlayer mp) {
                if (TaoLiveVideoView.this.mMediaPlayerRecycler != null && TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer != null) {
                    int position = 0;
                    if (bNeedSeek && TaoLiveVideoView.this.mConfig != null && TaoLiveVideoView.this.mConfig.mScenarioType == 2) {
                        position = TaoLiveVideoView.this.getCurrentPosition();
                    }
                    TaoLiveVideoView.this.release(false);
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer = TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer;
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mNextMediaPlayer = null;
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mPlayState = 2;
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer.registerOnPreparedListener(TaoLiveVideoView.this.mPreparedListener);
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer.registerOnVideoSizeChangedListener(TaoLiveVideoView.this.mSizeChangedListener);
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer.registerOnCompletionListener(TaoLiveVideoView.this.mCompletionListener);
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer.registerOnErrorListener(TaoLiveVideoView.this.mErrorListener);
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer.registerOnInfoListener(TaoLiveVideoView.this.mInfoListener);
                    TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer.registerOnBufferingUpdateListener(TaoLiveVideoView.this.mBufferingUpdateListener);
                    TaoLiveVideoView.this.start();
                    if (bNeedSeek && TaoLiveVideoView.this.mConfig != null && TaoLiveVideoView.this.mConfig.mScenarioType == 2) {
                        TaoLiveVideoView.this.seekTo(position);
                    }
                }
            }
        });
        return true;
    }

    public boolean isInPlaybackState() {
        return (this.mMediaPlayerRecycler == null || this.mMediaPlayerRecycler.mMediaPlayer == null || this.mMediaPlayerRecycler.mPlayState == -1 || this.mMediaPlayerRecycler.mPlayState == 0 || this.mMediaPlayerRecycler.mPlayState == 1) ? false : true;
    }

    public void setSurfaceListener(SurfaceListener listener) {
        this.mSurfaceListener = listener;
    }

    /* access modifiers changed from: private */
    public void bindSurfaceHolder(IMediaPlayer mp, IRenderView.ISurfaceHolder holder) {
        if (mp != null) {
            if (holder == null) {
                mp.setSurface((Surface) null);
                return;
            }
            releaseHolderSurface(holder);
            holder.bindToMediaPlayer(mp);
        }
    }

    /* access modifiers changed from: private */
    public void releaseHolderSurface(IRenderView.ISurfaceHolder holder) {
        if (holder != null && holder.getSurface() != null && Build.VERSION.SDK_INT < SDK_INT_FOR_OPTIMIZE) {
            holder.getSurface().release();
        }
    }

    public class NetworkBroadcastReceiver extends BroadcastReceiver {
        public NetworkBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            boolean isConnect = netInfo != null && netInfo.isConnected();
            if (isConnect) {
                int networkType = netInfo.getType();
                if (!(networkType == 1 || networkType == TaoLiveVideoView.this.mLastNetworkType || TaoLiveVideoView.this.mConfig == null || !TaoLiveVideoView.this.mConfig.mbShowNoWifiToast)) {
                    Toast.makeText(TaoLiveVideoView.this.mContext, TaoLiveVideoView.this.mContext.getString(R.string.avsdk_mobile_network_hint), 1).show();
                }
                if ((!TaoLiveVideoView.this.mIsConnected || !(networkType == TaoLiveVideoView.this.mLastNetworkType || TaoLiveVideoView.this.mLastNetworkType == -1)) && TaoLiveVideoView.this.mPlayUrl != null) {
                    int position = 0;
                    if (!(TaoLiveVideoView.this.mConfig == null || TaoLiveVideoView.this.mConfig.mScenarioType != 2 || TaoLiveVideoView.this.mMediaPlayerRecycler == null || TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer == null)) {
                        position = (int) TaoLiveVideoView.this.mMediaPlayerRecycler.mMediaPlayer.getCurrentPosition();
                    }
                    TaoLiveVideoView.this.release();
                    TaoLiveVideoView.this.start();
                    if (TaoLiveVideoView.this.mConfig != null && TaoLiveVideoView.this.mConfig.mScenarioType == 2) {
                        TaoLiveVideoView.this.seekTo(position);
                    }
                }
                TaoLiveVideoView.this.mLastNetworkType = networkType;
            }
            TaoLiveVideoView.this.mIsConnected = isConnect;
        }
    }

    public static void startViewFadeAnimation(final View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (view != null) {
                    view.setVisibility(8);
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(alphaAnimation);
    }

    private boolean needSetFusionMode() {
        String modleStr = this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, "SensorFusionCalibrate", "") : null;
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

    /* access modifiers changed from: private */
    public void changeVideoSize(int width, int height, int sarNum, int sarDen) {
        if (width != 0 && height != 0) {
            if (width != this.mVideoWidth || height != this.mVideoHeight) {
                this.mVideoWidth = width;
                this.mVideoHeight = height;
                this.mVideoSarNum = sarNum;
                this.mVideoSarDen = sarDen;
                if (this.mRenderView != null) {
                    this.mRenderView.setVideoSize(this.mVideoWidth, this.mVideoHeight);
                    this.mRenderView.setVideoSampleAspectRatio(this.mVideoSarNum, this.mVideoSarDen);
                }
            }
        }
    }

    private void keepScreenOn() {
        if (this.mContext != null && (this.mContext instanceof Activity)) {
            ((Activity) this.mContext).getWindow().addFlags(128);
        }
    }

    /* access modifiers changed from: private */
    public void clearKeepScreenOn() {
        if (this.mContext != null && (this.mContext instanceof Activity)) {
            ((Activity) this.mContext).getWindow().clearFlags(128);
        }
    }
}
