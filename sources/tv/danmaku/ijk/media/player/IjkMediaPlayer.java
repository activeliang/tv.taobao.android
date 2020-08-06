package tv.danmaku.ijk.media.player;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.alibaba.sdk.android.oss.common.utils.HttpHeaders;
import com.taobao.adapter.ConfigAdapter;
import com.taobao.adapter.CustomLibLoader;
import com.taobao.media.MediaConstant;
import com.taobao.statistic.CT;
import com.taobao.statistic.TBS;
import com.taobao.taobaoavsdk.util.AndroidUtils;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoViewConfig;
import dalvik.system.BaseDexClassLoader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.SymbolExpUtil;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.LDNetTraceRoute;
import tv.danmaku.ijk.media.player.annotations.AccessedByNative;
import tv.danmaku.ijk.media.player.annotations.CalledByNative;
import tv.danmaku.ijk.media.player.misc.FaceDetectInfo;
import tv.danmaku.ijk.media.player.misc.IFaceDetectListener;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;

public final class IjkMediaPlayer extends MonitorMediaPlayer {
    public static final int FFP_PROPV_DECODER_AVCODEC = 1;
    public static final int FFP_PROPV_DECODER_MEDIACODEC = 2;
    public static final int FFP_PROPV_DECODER_UNKNOWN = 0;
    public static final int FFP_PROPV_DECODER_VIDEOTOOLBOX = 3;
    public static final int FFP_PROP_ARTP_DLIB_PATH = 21802;
    public static final int FFP_PROP_ARTP_FFDECODER_THREAD = 21801;
    public static final int FFP_PROP_AUDIO_ACCELERATE = 21009;
    public static final int FFP_PROP_AUDIO_DISABLE = 50001;
    public static final int FFP_PROP_AUDIO_SLOWSPEED = 20110;
    public static final int FFP_PROP_AVFORMAT_OPEN_TIME = 20122;
    public static final int FFP_PROP_AVFORMAT_STREAM_INFO_TIME = 20123;
    public static final int FFP_PROP_AV_SYNC_TYPE = 20132;
    public static final int FFP_PROP_CONSUMED_TRAFFIC = 22006;
    public static final int FFP_PROP_DECODER_VIDEO_FPS = 20113;
    public static final int FFP_PROP_DOWNLOAD_BITRATE = 20115;
    public static final int FFP_PROP_DOWNLOAD_VIDEO_FPS = 20112;
    public static final int FFP_PROP_ENABLE_ACCURATE_SEEK = 20131;
    public static final int FFP_PROP_ENABLE_PLAYBACK_RATE = 30002;
    public static final int FFP_PROP_FIRSTPLAY_NEED_TIME = 20117;
    public static final int FFP_PROP_FIRST_PKT_NEED_TIME = 20119;
    public static final int FFP_PROP_FLOAT_AVDELAY = 10004;
    public static final int FFP_PROP_FLOAT_AVDIFF = 10005;
    public static final int FFP_PROP_FLOAT_PLAYBACK_RATE = 10003;
    public static final int FFP_PROP_FLOAT_SYNC_PTS = 10006;
    public static final int FFP_PROP_HEVC_DECODE_OPT = 20101;
    public static final int FFP_PROP_HTTP_OPEN_TIME = 20118;
    public static final int FFP_PROP_INT64_AUDIO_CACHED_BYTES = 20008;
    public static final int FFP_PROP_INT64_AUDIO_CACHED_DURATION = 20006;
    public static final int FFP_PROP_INT64_AUDIO_CACHED_FRAMES = 20012;
    public static final int FFP_PROP_INT64_AUDIO_CACHED_PACKETS = 20010;
    public static final int FFP_PROP_INT64_AUDIO_DECODER = 20004;
    public static final int FFP_PROP_INT64_AVCODEC_DECODE_TIME = 20013;
    public static final int FFP_PROP_INT64_FIRST_FRAME_RENDERING_OPT = 21002;
    public static final int FFP_PROP_INT64_HEVC_SOFT_DECODE_MAX_FPS = 20137;
    public static final int FFP_PROP_INT64_IS_WAITING_SYNC = 20136;
    public static final int FFP_PROP_INT64_SELECTED_AUDIO_STREAM = 20002;
    public static final int FFP_PROP_INT64_SELECTED_VIDEO_STREAM = 20001;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_BYTES = 20007;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_DURATION = 20005;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_FRAMES = 20011;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_PACKETS = 20009;
    public static final int FFP_PROP_INT64_VIDEO_DECODER = 20003;
    public static final int FFP_PROP_INTERVAL_DOWNLOAD_BITRATE = 20130;
    public static final int FFP_PROP_MAX_BUFFER_SIZE = 40001;
    public static final int FFP_PROP_MAX_FAST_PLAY_COUNT = 20135;
    public static final int FFP_PROP_MAX_NORMAL_PLAY_COUNT = 20133;
    public static final int FFP_PROP_MIN_NORMAL_PLAY_COUNT = 20134;
    public static final int FFP_PROP_MIN_SIZE_OR_TIME_FIRST_RENDER_OPT = 20138;
    public static final int FFP_PROP_NEED_DROP_FRAME = 50002;
    public static final int FFP_PROP_NETWORK_TRAFFIC = 21006;
    public static final int FFP_PROP_NETWORK_TRAFFIC_REPORT_TRIGGER = 21005;
    public static final int FFP_PROP_OBJ_VIDEO_FPS = 30001;
    public static final int FFP_PROP_ONLY_ONE_STREAM = 20125;
    public static final int FFP_PROP_PLAYBUFFER_NEED_TIME = 20120;
    public static final int FFP_PROP_PLAYER_MUTED = 21008;
    public static final int FFP_PROP_PLAY_ARTP_MODE = 21800;
    public static final int FFP_PROP_PLAY_TYPE = 21007;
    public static final int FFP_PROP_RENDER_VIDEO_FPS = 20114;
    public static final int FFP_PROP_SEND_SEI = 20111;
    public static final int FFP_PROP_SEND_VIA = 20121;
    public static final int FFP_PROP_STRING_CDNIP = 20124;
    public static final int FFP_PROP_STRING_LOCAL_IP = 21004;
    public static final int FFP_PROP_STRING_SERVER_IP = 21003;
    public static final int FFP_PROP_USE_OPENSL = 20129;
    public static final int FFP_PROP_VIDEO_CODEC_INFO = 20116;
    public static final int IJK_LOG_DEBUG = 3;
    public static final int IJK_LOG_DEFAULT = 1;
    public static final int IJK_LOG_ERROR = 6;
    public static final int IJK_LOG_FATAL = 7;
    public static final int IJK_LOG_INFO = 4;
    public static final int IJK_LOG_SILENT = 8;
    public static final int IJK_LOG_UNKNOWN = 0;
    public static final int IJK_LOG_VERBOSE = 2;
    public static final int IJK_LOG_WARN = 5;
    private static final int MEDIA_BUFFERING_UPDATE = 3;
    private static final int MEDIA_ERROR = 100;
    private static final int MEDIA_INFO = 200;
    private static final int MEDIA_NOP = 0;
    private static final int MEDIA_OUT_OF_BUFFERING = 300;
    private static final int MEDIA_PLAYBACK_COMPLETE = 2;
    private static final int MEDIA_PREPARED = 1;
    private static final int MEDIA_RESUME_BUFFERING = 301;
    private static final int MEDIA_SEEK_COMPLETE = 4;
    protected static final int MEDIA_SET_VIDEO_SAR = 10001;
    private static final int MEDIA_SET_VIDEO_SIZE = 5;
    private static final int MEDIA_TIMED_TEXT = 99;
    public static final int OPT_CATEGORY_CODEC = 2;
    public static final int OPT_CATEGORY_FORMAT = 1;
    public static final int OPT_CATEGORY_PLAYER = 4;
    public static final int OPT_CATEGORY_SWS = 3;
    public static final String ORANGE_ACCURATE_SEEK = "ijkAccurateSeekWhiteList";
    public static final String ORANGE_FIRST_RENDER_BUFFER_TIME = "firstRenderTime";
    public static final String ORANGE_TRACEROUTE = "TraceRouteOnError";
    public static final String ORANGE_USENEWHEVC = "UseNewHEVC2";
    public static final String ORANGE_USE_OPENSL = "useOpensl";
    public static final int PROP_FLOAT_VIDEO_DECODE_FRAMES_PER_SECOND = 10001;
    public static final int PROP_FLOAT_VIDEO_OUTPUT_FRAMES_PER_SECOND = 10002;
    public static final int RTCSTREAM_STATSTYPE_FEC_RECOVERED_NUM = 21906;
    public static final int RTCSTREAM_STATSTYPE_NACK_REQ_RSP_NUM = 21905;
    public static final int RTCSTREAM_STATSTYPE_NET_RECV_RATE = 21907;
    public static final int RTCSTREAM_STATSTYPE_RECV_BUFFER_DELAY = 21902;
    public static final int RTCSTREAM_STATSTYPE_RECV_JITTER = 21903;
    public static final int RTCSTREAM_STATSTYPE_RECV_LOSS_RATE = 21904;
    public static final int RTCSTREAM_STATSTYPE_RTT = 21901;
    public static final int RTCSTREAM_TRANSPORT_DELAY_INFO = 21993;
    public static final int RTCSTREAM_TRANSPORT_INFO_SNAPSHOT = 21992;
    public static final int RTCSTREAM_TRANSPORT_START_INFO = 21991;
    public static final int RTCSTREAM_TRANSPORT_STATS_INCYCLE = 21994;
    public static final int RTCSTREAM_TRANSPORT_STREAM_INFO = 21990;
    public static final int SDL_FCC_RV16 = 909203026;
    public static final int SDL_FCC_RV32 = 842225234;
    public static final int SDL_FCC_YV12 = 842094169;
    private static final String TAG = "AVSDK";
    private static volatile boolean mIsLibLoaded = false;
    private static volatile boolean mIsNativeInitialized = false;
    public static Map<String, String> mediacodecMap = new ConcurrentHashMap(2);
    private long mDuration;
    private EventHandler mEventHandler;
    /* access modifiers changed from: private */
    public IFaceDetectListener mFdListener;
    private String mHost;
    @AccessedByNative
    private int mListenerContext;
    @AccessedByNative
    private long mNativeMediaDataSource;
    /* access modifiers changed from: private */
    @AccessedByNative
    public long mNativeMediaPlayer;
    @AccessedByNative
    private int mNativeSurfaceTexture;
    private boolean mScreenOnWhilePlaying;
    private boolean mStayAwake;
    private SurfaceHolder mSurfaceHolder;
    /* access modifiers changed from: private */
    public int mVideoHeight;
    /* access modifiers changed from: private */
    public int mVideoSarDen;
    /* access modifiers changed from: private */
    public int mVideoSarNum;
    /* access modifiers changed from: private */
    public int mVideoWidth;
    private PowerManager.WakeLock mWakeLock = null;

    public interface OnMediaCodecSelectListener {
        String onMediaCodecSelect(IMediaPlayer iMediaPlayer, String str, int i, int i2);
    }

    private native void _enableFaceDetect(boolean z, String str, String str2, String str3, String str4, String str5);

    private native String _getAudioCodecInfo();

    private static native String _getColorFormatName(int i);

    private native long _getDuration();

    private native int _getLoopCount();

    private native Bundle _getMediaMeta();

    private native String _getVideoCodecInfo();

    private native void _pause() throws IllegalStateException;

    private native void _release();

    private native void _reset();

    private native void _seekTo(long j) throws IllegalStateException;

    private native void _setDataSource(String str, String[] strArr, String[] strArr2) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setDataSource(IMediaDataSource iMediaDataSource) throws IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setDataSourceFd(int i) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setLoopCount(int i);

    private native void _setStreamSelected(int i, boolean z);

    private native void _setVideoSurface(Surface surface);

    private native void _setVolume(float f, float f2);

    private native void _start() throws IllegalStateException;

    private native void _stop() throws IllegalStateException;

    private native void native_finalize();

    private static native void native_init();

    private native void native_message_loop(Object obj);

    public static native void native_profileBegin(String str);

    public static native void native_profileEnd();

    public static native void native_setLogLevel(int i);

    private native void native_setup(Object obj);

    public native float _getPropertyFloat(int i, float f);

    public native long _getPropertyLong(int i, long j);

    public native Object _getPropertyObject(int i);

    public native String _getPropertyString(int i);

    public native void _prepareAsync() throws IllegalStateException, IjkMediaException;

    public native void _setOption(int i, String str, long j);

    public native void _setOption(int i, String str, String str2);

    public native void _setPropertyFloat(int i, float f);

    public native void _setPropertyLong(int i, long j);

    public native void _setPropertyString(int i, String str);

    public native double _switchPathSyncFrame();

    public native int getAudioSessionId();

    public native long getCurrentPosition();

    public native boolean isPlaying();

    public static void loadLibrariesOnce(CustomLibLoader customLibLoader) {
        synchronized (IjkMediaPlayer.class) {
            if (!mIsLibLoaded) {
                if (customLibLoader != null) {
                    try {
                        customLibLoader.loadLibrary("tbffmpeg");
                        customLibLoader.loadLibrary("tbsdl");
                        customLibLoader.loadLibrary("tbplayer");
                    } catch (Throwable e) {
                        Log.e(TAG, "loadLibrariesOnce loadLibrary fail ---" + e.getMessage() + " " + e.getStackTrace());
                    }
                } else {
                    System.loadLibrary("tbffmpeg");
                    System.loadLibrary("tbsdl");
                    System.loadLibrary("tbplayer");
                }
                mIsLibLoaded = true;
            }
        }
    }

    private static void initNativeOnce() {
        synchronized (IjkMediaPlayer.class) {
            if (mIsLibLoaded && !mIsNativeInitialized) {
                native_init();
                mIsNativeInitialized = true;
            }
        }
    }

    public IjkMediaPlayer() {
        initPlayer((CustomLibLoader) null);
    }

    public IjkMediaPlayer(Context context) {
        super(context);
        initPlayer((CustomLibLoader) null);
    }

    public IjkMediaPlayer(Context context, ConfigAdapter configAdapter) {
        super(context, configAdapter);
        initPlayer((CustomLibLoader) null);
    }

    public IjkMediaPlayer(Context context, ConfigAdapter configAdapter, CustomLibLoader customLibLoader) {
        super(context, configAdapter);
        initPlayer(customLibLoader);
    }

    public void setConfig(TaoLiveVideoViewConfig config) {
        super.setConfig(config);
        if (this.mConfig != null) {
            Log.i(TAG, "**** CPU name:" + AndroidUtils.getCPUName() + ",phone model:" + Build.MODEL);
            _setPropertyLong(21007, (long) this.mConfig.mScenarioType);
            _setOption(4, "mediacodec-hevc", this.mConfig.mDecoderTypeH265 == 1 ? 1 : 0);
            _setOption(4, "mediacodec-avc", this.mConfig.mDecoderTypeH264 == 1 ? 1 : 0);
            if (this.mConfig.mDropFrameForH265) {
                _setPropertyLong(20137, 18);
            }
            if (this.mConfigAdapter != null) {
                _setPropertyLong(20101, AndroidUtils.parseBoolean(this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, "UseNewHEVC2", "true")) ? 1 : 0);
                if (Build.VERSION.SDK_INT > 19) {
                    _setPropertyLong(FFP_PROP_USE_OPENSL, AndroidUtils.parseBoolean(this.mConfigAdapter.getConfig(MediaConstant.DW_ORANGE_GROUP_NAME, ORANGE_USE_OPENSL, "false")) ? 1 : 0);
                } else {
                    _setPropertyLong(FFP_PROP_USE_OPENSL, 1);
                }
                _setPropertyLong(20131, AndroidUtils.isInList(this.mConfig.mSubBusinessType, this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, "ijkAccurateSeekWhiteList", "")) ? 1 : 0);
                int first_render_Time = AndroidUtils.parseInt(this.mConfigAdapter.getConfig(MediaConstant.DW_ORANGE_GROUP_NAME, ORANGE_FIRST_RENDER_BUFFER_TIME, "800"));
                if (first_render_Time > 50 && first_render_Time < 800) {
                    _setPropertyLong(FFP_PROP_FIRSTPLAY_NEED_TIME, (long) first_render_Time);
                }
            }
        }
    }

    private void initPlayer(CustomLibLoader customLibLoader) {
        loadLibrariesOnce(customLibLoader);
        initNativeOnce();
        Looper looper = Looper.getMainLooper();
        if (looper != null) {
            this.mEventHandler = new EventHandler(this, looper);
        } else {
            this.mEventHandler = null;
        }
        native_setup(new WeakReference(this));
        _setOption(4, "start-on-prepared", 0);
    }

    public void setDisplay(SurfaceHolder sh) {
        Surface surface;
        this.mSurfaceHolder = sh;
        if (sh != null) {
            surface = sh.getSurface();
        } else {
            surface = null;
        }
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    public void setSurface(Surface surface) {
        this.mSurfaceHolder = null;
        this.mSurface = surface;
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    public void setSurfaceSize(int width, int height) {
    }

    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        String url = monitorDataSource(path);
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http")) {
                HashMap<String, String> headers = new HashMap<>();
                String userAgent = AndroidUtils.getUserAgent(this.mContext);
                if (!TextUtils.isEmpty(userAgent)) {
                    headers.put(HttpHeaders.USER_AGENT, userAgent);
                }
                if (isUsePcdn() && getConfig() != null && !TextUtils.isEmpty(getConfig().mPlayToken)) {
                    headers.put("play_token", getConfig().mPlayToken);
                }
                if (!headers.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        sb.append(entry.getKey());
                        sb.append(SymbolExpUtil.SYMBOL_COLON);
                        if (!TextUtils.isEmpty(entry.getValue())) {
                            sb.append(entry.getValue());
                        }
                        sb.append("\r\n");
                        _setOption(1, "headers", sb.toString());
                    }
                }
                if (!TextUtils.isEmpty(this.mCdnIp) && !isUseVideoCache()) {
                    _setOption(1, "cdn_ip", this.mCdnIp);
                    _setOption(1, "hls_cdn_ip", this.mCdnIp);
                }
                Uri uri = Uri.parse(this.mPlayUrl);
                if (uri != null) {
                    this.mHost = uri.getHost();
                }
            } else if (url.startsWith("artp")) {
                _setPropertyString(21802, ((BaseDexClassLoader) this.mContext.getClassLoader()).findLibrary("artc_engine"));
                _setPropertyLong(FFP_PROP_PLAY_ARTP_MODE, 1);
                _setPropertyLong(20111, 1);
            }
            _setDataSource(url, (String[]) null, (String[]) null);
        }
    }

    public void prepareAsync() throws IllegalStateException, IjkMediaException {
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        this.mVideoSarNum = 0;
        this.mVideoSarDen = 0;
        this.mDuration = 0;
        monitorPrepare();
        _prepareAsync();
    }

    public void start() throws IllegalStateException {
        stayAwake(true);
        monitorStart();
        _start();
    }

    public void stop() throws IllegalStateException {
        stayAwake(false);
        _pause();
        _stop();
    }

    public void pause() throws IllegalStateException {
        stayAwake(false);
        monitorPause();
        _pause();
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
        if (this.mScreenOnWhilePlaying != screenOn) {
            this.mScreenOnWhilePlaying = screenOn;
            updateSurfaceScreenOn();
        }
    }

    /* access modifiers changed from: private */
    @SuppressLint({"Wakelock"})
    public void stayAwake(boolean awake) {
        if (this.mWakeLock != null) {
            if (awake && !this.mWakeLock.isHeld()) {
                this.mWakeLock.acquire();
            } else if (!awake && this.mWakeLock.isHeld()) {
                this.mWakeLock.release();
            }
        }
        this.mStayAwake = awake;
        updateSurfaceScreenOn();
    }

    private void updateSurfaceScreenOn() {
        if (this.mSurfaceHolder != null) {
            this.mSurfaceHolder.setKeepScreenOn(this.mScreenOnWhilePlaying && this.mStayAwake);
        }
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    public int getVideoSarNum() {
        return this.mVideoSarNum;
    }

    public int getVideoSarDen() {
        return this.mVideoSarDen;
    }

    public void seekTo(long msec) throws IllegalStateException {
        if (this.mVolume != 0.0f && this.bInstantSeeked) {
            _setPropertyLong(21008, 0);
        }
        this.bInstantSeeked = false;
        monitorSeek();
        _seekTo(msec);
    }

    public void instantSeekTo(long msec) {
        if (this.mVolume != 0.0f && !this.bInstantSeeked) {
            _setPropertyLong(21008, 1);
        }
        this.bInstantSeeked = true;
        monitorSeek();
        _seekTo(msec);
    }

    public long getDuration() {
        if (this.mDuration <= 0) {
            this.mDuration = _getDuration();
        }
        return this.mDuration;
    }

    public void release() {
        stayAwake(false);
        monitorPlayExperience();
        monitorRelease();
        updateSurfaceScreenOn();
        synchronized (IjkMediaPlayer.class) {
            if (this.mEventHandler != null) {
                this.mEventHandler.removeCallbacksAndMessages((Object) null);
                this.mEventHandler = null;
            }
            this.mVideoWidth = 0;
            this.mVideoHeight = 0;
            this.mDuration = 0;
        }
        _release();
    }

    public void reset() {
        stayAwake(false);
        monitorPlayExperience();
        monitorReset();
        _reset();
        synchronized (IjkMediaPlayer.class) {
            if (this.mEventHandler != null) {
                this.mEventHandler.removeCallbacksAndMessages((Object) null);
            }
        }
    }

    public boolean isHardwareDecode() {
        return _getPropertyLong(FFP_PROP_INT64_VIDEO_DECODER, 1) == 2;
    }

    public void setVolume(float leftVolume, float rightVolume) {
        this.mVolume = leftVolume;
        _setVolume(leftVolume, rightVolume);
        if (leftVolume == 0.0f && rightVolume == 0.0f) {
            _setPropertyLong(21008, 1);
        } else {
            _setPropertyLong(21008, 0);
        }
    }

    public void setPlayRate(float playRate) {
        if (playRate != 1.0f) {
            _setPropertyLong(FFP_PROP_ENABLE_PLAYBACK_RATE, 1);
            _setPropertyFloat(10003, playRate);
            return;
        }
        _setPropertyLong(FFP_PROP_ENABLE_PLAYBACK_RATE, 0);
    }

    public void setMuted(boolean muted) {
        _setPropertyLong(21008, muted ? 1 : 0);
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        native_finalize();
    }

    private static class EventHandler extends Handler {
        private WeakReference<IjkMediaPlayer> mWeakPlayer;

        public EventHandler(IjkMediaPlayer mp, Looper looper) {
            super(looper);
            this.mWeakPlayer = new WeakReference<>(mp);
        }

        public void handleMessage(Message msg) {
            FaceDetectInfo info;
            IjkMediaPlayer player = (IjkMediaPlayer) this.mWeakPlayer.get();
            if (player != null && player.mNativeMediaPlayer != 0) {
                EventData event_data = (EventData) msg.obj;
                switch (msg.what) {
                    case 1:
                        player.monitorPrepared(event_data.arg2);
                        if (player.mOnPreparedListener != null) {
                            player.mOnPreparedListener.onPrepared(player);
                        }
                        if (player.mOnPreparedListeners != null) {
                            for (IMediaPlayer.OnPreparedListener listener : player.mOnPreparedListeners) {
                                listener.onPrepared(player);
                            }
                            return;
                        }
                        return;
                    case 2:
                        player.monitorComplete();
                        if (player.bLooping) {
                            player.bSeeked = true;
                            if (player.mOnLoopCompletionListeners != null) {
                                for (IMediaPlayer.OnLoopCompletionListener listener2 : player.mOnLoopCompletionListeners) {
                                    listener2.onLoopCompletion(player);
                                }
                            }
                            player.start();
                            return;
                        }
                        if (player.mOnCompletionListener != null) {
                            player.mOnCompletionListener.onCompletion(player);
                        }
                        if (player.mOnCompletionListeners != null) {
                            for (IMediaPlayer.OnCompletionListener listener3 : player.mOnCompletionListeners) {
                                listener3.onCompletion(player);
                            }
                        }
                        player.stayAwake(false);
                        return;
                    case 3:
                        int percent = (int) event_data.arg2;
                        if (percent > 100) {
                            percent = 100;
                        }
                        if (player.mOnBufferingUpdateListener != null) {
                            player.mOnBufferingUpdateListener.onBufferingUpdate(player, percent);
                        }
                        if (player.mOnBufferingUpdateListeners != null) {
                            for (IMediaPlayer.OnBufferingUpdateListener listener4 : player.mOnBufferingUpdateListeners) {
                                listener4.onBufferingUpdate(player, percent);
                            }
                            return;
                        }
                        return;
                    case 4:
                        if (player.mOnSeekCompletionListener != null) {
                            player.mOnSeekCompletionListener.onSeekComplete(player);
                        }
                        if (player.mOnSeekCompletionListeners != null) {
                            for (IMediaPlayer.OnSeekCompletionListener listener5 : player.mOnSeekCompletionListeners) {
                                listener5.onSeekComplete(player);
                            }
                            return;
                        }
                        return;
                    case 5:
                        int unused = player.mVideoWidth = (int) event_data.arg1;
                        int unused2 = player.mVideoHeight = (int) event_data.arg2;
                        if (player.mOnVideoSizeChangedListener != null) {
                            player.mOnVideoSizeChangedListener.onVideoSizeChanged(player, player.mVideoWidth, player.mVideoHeight, player.mVideoSarNum, player.mVideoSarDen);
                        }
                        if (player.mOnVideoSizeChangedListeners != null) {
                            for (IMediaPlayer.OnVideoSizeChangedListener listener6 : player.mOnVideoSizeChangedListeners) {
                                listener6.onVideoSizeChanged(player, player.mVideoWidth, player.mVideoHeight, player.mVideoSarNum, player.mVideoSarDen);
                            }
                            return;
                        }
                        return;
                    case 100:
                        player.monitorError((int) event_data.arg2, (int) event_data.arg3);
                        player.monitorPlayExperience();
                        if ((player.mOnErrorListener == null || !player.mOnErrorListener.onError(player, (int) event_data.arg2, (int) event_data.arg3)) && player.mOnCompletionListener != null) {
                            player.mOnCompletionListener.onCompletion(player);
                        }
                        if (player.mOnErrorListeners != null) {
                            for (IMediaPlayer.OnErrorListener listener7 : player.mOnErrorListeners) {
                                listener7.onError(player, (int) event_data.arg2, (int) event_data.arg3);
                            }
                        }
                        player.stayAwake(false);
                        player.traceRoute((String) null);
                        return;
                    case 200:
                        if (event_data.arg1 == 701) {
                            player.monitorBufferStart(System.currentTimeMillis());
                        } else if (event_data.arg1 == 702) {
                            player.monitorBufferEnd(System.currentTimeMillis());
                        } else if (event_data.arg1 == 3) {
                            player.monitorRenderStart(event_data.arg2);
                        } else if (event_data.arg1 == 716) {
                            if (!(player.mFdListener == null || (info = (FaceDetectInfo) event_data.obj) == null)) {
                                player.mFdListener.onFaceDetected((int) event_data.arg2, info.faceRect, info.faceLandmarks);
                            }
                        } else if (event_data.arg1 == 820) {
                            player.artpEndtoEndDelayMsg((String) event_data.obj);
                        } else if (event_data.arg1 == 720) {
                            player.traceRoute((String) event_data.obj);
                        }
                        if (player.mOnInfoListeners != null) {
                            for (IMediaPlayer.OnInfoListener listener8 : player.mOnInfoListeners) {
                                listener8.onInfo(player, event_data.arg1, event_data.arg2, event_data.arg3, event_data.obj);
                            }
                        }
                        if (player.mOnInfoListener != null) {
                            player.mOnInfoListener.onInfo(player, event_data.arg1, event_data.arg2, event_data.arg3, event_data.obj);
                            return;
                        }
                        return;
                    case 300:
                        if (player.mOnInfoListener != null) {
                            player.mOnInfoListener.onInfo(player, (long) msg.what, 0, 0, (Object) null);
                        }
                        if (player.mOnInfoListeners != null) {
                            for (IMediaPlayer.OnInfoListener listener9 : player.mOnInfoListeners) {
                                listener9.onInfo(player, (long) msg.what, 0, 0, (Object) null);
                            }
                            return;
                        }
                        return;
                    case 301:
                        if (player.mOnInfoListener != null) {
                            player.mOnInfoListener.onInfo(player, (long) msg.what, 0, 0, (Object) null);
                        }
                        if (player.mOnInfoListeners != null) {
                            for (IMediaPlayer.OnInfoListener listener10 : player.mOnInfoListeners) {
                                listener10.onInfo(player, (long) msg.what, 0, 0, (Object) null);
                            }
                            return;
                        }
                        return;
                    case 10001:
                        int unused3 = player.mVideoSarNum = (int) event_data.arg1;
                        int unused4 = player.mVideoSarDen = (int) event_data.arg2;
                        return;
                    default:
                        return;
                }
            }
        }
    }

    @CalledByNative
    private static void postEventFromNative(Object weakThiz, int what, long arg1, long arg2, long arg3, Object obj) {
        IjkMediaPlayer mp;
        if (weakThiz != null && (mp = (IjkMediaPlayer) ((WeakReference) weakThiz).get()) != null) {
            if (what == 200 && arg1 == 2) {
                mp.start();
            }
            synchronized (IjkMediaPlayer.class) {
                if (mp.mEventHandler != null) {
                    Message m = mp.mEventHandler.obtainMessage(what);
                    EventData event_data = new EventData();
                    event_data.arg1 = arg1;
                    event_data.arg2 = arg2;
                    event_data.arg3 = arg3;
                    event_data.obj = obj;
                    m.obj = event_data;
                    if (what == 1 || (what == 200 && (arg1 == 3 || arg1 == 717))) {
                        mp.mEventHandler.sendMessageAtFrontOfQueue(m);
                    } else {
                        mp.mEventHandler.sendMessage(m);
                    }
                }
            }
        }
    }

    @CalledByNative
    private static boolean onNativeInvoke(Object weakThiz, int what, Bundle args) {
        return false;
    }

    @CalledByNative
    private static String onSelectCodec(Object weakThiz, String mimeType, int profile, int level) {
        IjkMediaPlayer player;
        if (weakThiz == null || !(weakThiz instanceof WeakReference) || (player = (IjkMediaPlayer) ((WeakReference) weakThiz).get()) == null) {
            return null;
        }
        return DefaultMediaCodecSelector.sInstance.onMediaCodecSelect(player, mimeType, profile, level);
    }

    public static class DefaultMediaCodecSelector implements OnMediaCodecSelectListener {
        public static DefaultMediaCodecSelector sInstance = new DefaultMediaCodecSelector();

        @TargetApi(16)
        public String onMediaCodecSelect(IMediaPlayer mp, String mimeType, int profile, int level) {
            String[] types;
            IjkMediaCodecInfo candidate;
            if (Build.VERSION.SDK_INT < 16 || TextUtils.isEmpty(mimeType)) {
                return null;
            }
            ArrayList<IjkMediaCodecInfo> candidateCodecList = new ArrayList<>();
            int numCodecs = MediaCodecList.getCodecCount();
            for (int i = 0; i < numCodecs; i++) {
                MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
                if (!codecInfo.isEncoder() && (types = codecInfo.getSupportedTypes()) != null) {
                    for (String type : types) {
                        if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase(mimeType) && (candidate = IjkMediaCodecInfo.setupCandidate(codecInfo, mimeType)) != null) {
                            candidateCodecList.add(candidate);
                        }
                    }
                }
            }
            if (candidateCodecList.isEmpty()) {
                return null;
            }
            IjkMediaCodecInfo bestCodec = candidateCodecList.get(0);
            Iterator<IjkMediaCodecInfo> it = candidateCodecList.iterator();
            while (it.hasNext()) {
                IjkMediaCodecInfo codec = it.next();
                if (codec.mRank > bestCodec.mRank) {
                    bestCodec = codec;
                }
            }
            if (bestCodec.mRank < IjkMediaCodecInfo.RANK_LAST_CHANCE) {
                return null;
            }
            String codecName = bestCodec.mCodecInfo.getName();
            if (TextUtils.isEmpty(codecName) || TextUtils.isEmpty(mimeType)) {
                return codecName;
            }
            IjkMediaPlayer.mediacodecMap.put(mimeType, codecName);
            return codecName;
        }
    }

    public void enableFaceDetect(boolean enable, String lvGraphPath, String lvWeightPath, String pts106GraphPath, String pts106WeightPath, IFaceDetectListener listener) {
        if (this.mContext == null || !(this.mContext instanceof Activity) || TextUtils.isEmpty(lvGraphPath) || TextUtils.isEmpty(lvWeightPath) || TextUtils.isEmpty(pts106GraphPath) || TextUtils.isEmpty(pts106WeightPath)) {
            Log.e(TAG, "enableFaceDetect null param");
            return;
        }
        try {
            this.mFdListener = listener;
            _enableFaceDetect(enable, ((BaseDexClassLoader) this.mContext.getClassLoader()).findLibrary("alinnkit-v7a"), lvGraphPath, lvWeightPath, pts106GraphPath, pts106WeightPath);
        } catch (Throwable th) {
        }
    }

    private static class TraceTask extends AsyncTask<Void, Void, String> {
        private String mCdnIp;
        private TaoLiveVideoViewConfig mConfig;
        private int mLastErrorCode;
        private int mLastExtra;
        private String mPlayUrl;
        private String mServerIP;
        private String mTraceHost;

        TraceTask(String traceHost, TaoLiveVideoViewConfig config, String cdnIp, String mediaUrl, String serverIp, int errorCode, int extra) {
            this.mTraceHost = traceHost;
            this.mConfig = config;
            this.mCdnIp = cdnIp;
            this.mServerIP = serverIp;
            this.mPlayUrl = mediaUrl;
            this.mLastErrorCode = errorCode;
            this.mLastExtra = extra;
        }

        /* access modifiers changed from: protected */
        public String doInBackground(Void... voids) {
            final StringBuilder stringBuilder = new StringBuilder();
            LDNetTraceRoute traceRoute = LDNetTraceRoute.getInstance();
            traceRoute.initListenter(new LDNetTraceRoute.LDNetTraceRouteListener() {
                public void OnNetTraceUpdated(String log) {
                    stringBuilder.append(log);
                }

                public void OnNetTraceFinished() {
                }
            });
            traceRoute.startTraceRoute(this.mTraceHost);
            return stringBuilder.toString();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String s) {
            int i = 1;
            try {
                CT ct = CT.Button;
                String[] strArr = new String[14];
                strArr[0] = "play_scenario=" + this.mConfig.mScenarioType;
                strArr[1] = "server_ip=" + this.mServerIP;
                strArr[2] = "cdn_ip=" + this.mCdnIp;
                strArr[3] = "media_url=" + this.mPlayUrl;
                strArr[4] = "feed_id=" + this.mConfig.mFeedId;
                strArr[5] = "anchor_account_id=" + this.mConfig.mAccountId;
                strArr[6] = "business_type=" + this.mConfig.mBusinessId;
                strArr[7] = "sub_business_type=" + this.mConfig.mSubBusinessType;
                StringBuilder append = new StringBuilder().append("is_tbnet=");
                if (!this.mConfig.mbEnableTBNet) {
                    i = 0;
                }
                strArr[8] = append.append(i).toString();
                strArr[9] = "play_token=" + this.mConfig.mPlayToken;
                strArr[10] = "error_code=" + this.mLastErrorCode;
                strArr[11] = "extra=" + this.mLastExtra;
                strArr[12] = "trace_host=" + this.mTraceHost;
                strArr[13] = "trace_route=" + s;
                TBS.Adv.ctrlClicked("Page_Video", ct, "TraceRoute", strArr);
            } catch (Throwable th) {
            }
        }
    }

    /* access modifiers changed from: private */
    public void traceRoute(String host) {
        boolean traceroute;
        String traceHost = !TextUtils.isEmpty(host) ? host : this.mHost;
        if (!TextUtils.isEmpty(traceHost)) {
            if (this.mConfigAdapter == null || !AndroidUtils.parseBoolean(this.mConfigAdapter.getConfig(MediaConstant.DW_ORANGE_GROUP_NAME, ORANGE_TRACEROUTE, "false"))) {
                traceroute = false;
            } else {
                traceroute = true;
            }
            if (traceroute) {
                new TraceTask(traceHost, this.mConfig, this.mCdnIp, this.mPlayUrl, this.mServerIP, this.mLastErrorCode, this.mLastExtra).execute(new Void[0]);
            }
        }
    }
}
