package tv.danmaku.ijk.media.player;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.alibaba.sdk.android.oss.common.utils.HttpHeaders;
import com.taobao.adapter.ConfigAdapter;
import com.taobao.adapter.CustomLibLoader;
import com.taobao.android.alinnkit.intf.NetPreparedListener;
import com.taobao.media.MediaAdapteManager;
import com.taobao.tao.remotebusiness.js.MtopJSBridge;
import com.taobao.taobaoavsdk.util.AndroidUtils;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoViewConfig;
import dalvik.system.BaseDexClassLoader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.annotations.AccessedByNative;
import tv.danmaku.ijk.media.player.annotations.CalledByNative;

public final class TaobaoMediaPlayer extends MonitorMediaPlayer {
    private static final int ABR_SAMPLE_CHECK_TIME = 100;
    private static final int ABR_SAMPLE_TEST_TIME = 1000;
    public static final int FFP_PROP_FLOAT_PLAYBACK_RATE = 10003;
    public static final int FFP_PROP_FLOAT_SYNC_PTS = 10006;
    public static final int FFP_PROP_FLOAT_VOLUME = 12001;
    public static final int FFP_PROP_INT64_ANDROID_SW_RENDER_RGB = 11019;
    public static final int FFP_PROP_INT64_ARTP_DLIB_PATH = 21802;
    public static final int FFP_PROP_INT64_AUDIO_CACHED_BYTES = 20008;
    public static final int FFP_PROP_INT64_AUDIO_DECODER_TYPE = 11011;
    public static final int FFP_PROP_INT64_AUDIO_DISABLE = 50001;
    public static final int FFP_PROP_INT64_AUDIO_PROCESSOR_TYPE = 11013;
    public static final int FFP_PROP_INT64_AUDIO_RENDERER_TYPE = 11015;
    public static final int FFP_PROP_INT64_AVFORMAT_OPEN_TIME = 20122;
    public static final int FFP_PROP_INT64_AVFORMAT_STREAM_INFO_TIME = 20123;
    public static final int FFP_PROP_INT64_AVG_BITRATE = 20115;
    public static final int FFP_PROP_INT64_AVG_DECODER_VIDEO_FPS = 20113;
    public static final int FFP_PROP_INT64_AVG_DOWNLOAD_SPEED = 21009;
    public static final int FFP_PROP_INT64_AVG_DOWNLOAD_TIME = 21010;
    public static final int FFP_PROP_INT64_AVG_DOWNLOAD_VIDEO_FPS = 20112;
    public static final int FFP_PROP_INT64_AVG_VIDEO_FPS = 20114;
    public static final int FFP_PROP_INT64_BUFFER_MONITOR_IGNORE_BUFFERING = 11026;
    public static final int FFP_PROP_INT64_DECODER_ENABLE_DYNAMIC_RELOAD = 11022;
    public static final int FFP_PROP_INT64_ENABLE_ACCURATE_SEEK = 20131;
    public static final int FFP_PROP_INT64_ENABLE_LOOP = 11004;
    public static final int FFP_PROP_INT64_FIND_STREAM_INFO_OPT = 11023;
    public static final int FFP_PROP_INT64_HEVC_DECODE_OPT = 20101;
    public static final int FFP_PROP_INT64_HEVC_SOFT_DECODE_MAX_FPS = 20137;
    public static final int FFP_PROP_INT64_IS_WAITING_SYNC = 20136;
    public static final int FFP_PROP_INT64_MAX_BUFFER_SIZE = 40001;
    public static final int FFP_PROP_INT64_MAX_BUFFER_TIME_MS = 11008;
    public static final int FFP_PROP_INT64_MAX_FAST_PLAY_COUNT = 20135;
    public static final int FFP_PROP_INT64_MAX_NORMAL_PLAY_COUNT = 20133;
    public static final int FFP_PROP_INT64_MIN_NORMAL_PLAY_COUNT = 20134;
    public static final int FFP_PROP_INT64_MUTED = 21008;
    public static final int FFP_PROP_INT64_PLAY_SCENARIO = 21007;
    public static final int FFP_PROP_INT64_SEND_SEI = 20111;
    public static final int FFP_PROP_INT64_SOURCER_AUDIO_PIPE_MAX_COUNT = 11017;
    public static final int FFP_PROP_INT64_SOURCER_AUDIO_PIPE_START_COUNT = 11021;
    public static final int FFP_PROP_INT64_SOURCER_TYPE = 11009;
    public static final int FFP_PROP_INT64_SOURCER_VIDEO_PIPE_MAX_COUNT = 11016;
    public static final int FFP_PROP_INT64_SOURCER_VIDEO_PIPE_START_COUNT = 11020;
    public static final int FFP_PROP_INT64_START_ON_PREPARED = 11007;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_BYTES = 20007;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_COUNTS = 20009;
    public static final int FFP_PROP_INT64_VIDEO_DECODER_TYPE = 11010;
    public static final int FFP_PROP_INT64_VIDEO_PROCESSOR_TYPE = 11012;
    public static final int FFP_PROP_INT64_VIDEO_RENDERER_TYPE = 11014;
    public static final int FFP_PROP_STRING_SEI_BITRATE = 21004;
    public static final int FFP_PROP_STRING_SERVER_IP = 21003;
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
    static final int MSG_ABR_CHECK = 3;
    static final int MSG_ABR_INIT = 1;
    static final int MSG_ABR_STOP = 4;
    static final int MSG_ABR_TEST_START = 2;
    public static final int MediaAudioDecoder_FFmpeg = 1;
    public static final int MediaAudioDecoder_MediaCodec = 2;
    public static final int MediaAudioDecoder_None = 0;
    public static final int MediaAudioRenderer_AudioTrack = 2;
    public static final int MediaAudioRenderer_None = 0;
    public static final int MediaAudioRenderer_OpenSLES = 4;
    public static final int MediaVideoDecoder_FFmpeg = 1;
    public static final int MediaVideoDecoder_MediaCodec = 32;
    public static final int MediaVideoDecoder_MediaCodec_H264 = 8;
    public static final int MediaVideoDecoder_MediaCodec_HEVC = 16;
    public static final int MediaVideoDecoder_None = 0;
    public static final int MediaVideoRenderer_EGL = 8;
    public static final int MediaVideoRenderer_None = 0;
    public static final int MediaVideoRenderer_Surface = 4;
    public static final int OPT_ARTP_SOURCE = 2;
    public static final int OPT_CATEGORY_CODEC = 2;
    public static final int OPT_CATEGORY_FORMAT = 1;
    public static final int OPT_CATEGORY_PLAYER = 4;
    public static final int OPT_CATEGORY_SWS = 3;
    public static final String ORANGE_ACCURATE_SEEK = "ijkAccurateSeekWhiteList";
    public static final String ORANGE_FIND_STREAM_INFO_OPT = "findStreamInfoOpt";
    public static final String ORANGE_HARDWARE_AUDIO = "audioHardwareDecode";
    public static final String ORANGE_HARDWARE_AUDIO_BLACK = "audioHardwareDecodeBlackList";
    public static final String ORANGE_SW_RENDER_RGB = "swRenderRGB";
    public static final String ORANGE_USENEWHEVC = "UseNewHEVC2";
    private static final String TAG = "AVSDK";
    private static volatile boolean mIsLibLoaded = false;
    private Handler mABRHandler;
    /* access modifiers changed from: private */
    public HandlerThread mABRHandlerThread;
    private int mABRIndex = 0;
    /* access modifiers changed from: private */
    public final Object mABRLOCK = new Object();
    /* access modifiers changed from: private */
    public ABRFullNet mABRNet;
    /* access modifiers changed from: private */
    public int mCurrentIndex = 2;
    private long mDuration;
    private EventHandler mEventHandler;
    /* access modifiers changed from: private */
    public int mFlvRetain;
    /* access modifiers changed from: private */
    public float[] mInputABRData = new float[64];
    /* access modifiers changed from: private */
    public long mLastPosition = 0;
    /* access modifiers changed from: private */
    public int mLowABRCount;
    /* access modifiers changed from: private */
    @AccessedByNative
    public long mNativeMediaPlayer;
    private float[] mPlayerData = new float[8];
    /* access modifiers changed from: private */
    public int mResultIndex = 2;
    public int mTmpIndex;
    /* access modifiers changed from: private */
    public int mVideoHeight;
    /* access modifiers changed from: private */
    public int mVideoSarDen;
    /* access modifiers changed from: private */
    public int mVideoSarNum;
    /* access modifiers changed from: private */
    public int mVideoWidth;
    /* access modifiers changed from: private */
    public boolean mbIsSwitchingPath = false;

    private native long _getDuration();

    private native void _pause() throws IllegalStateException;

    private native void _release();

    private native void _seekTo(long j) throws IllegalStateException;

    private native void _setDataSource(String str, String[] strArr, String[] strArr2) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setVideoSurface(Surface surface);

    private native void _setVideoSurfaceSize(int i, int i2);

    private native void _start() throws IllegalStateException;

    private native void _stop() throws IllegalStateException;

    private native void native_setup(Object obj);

    public native float _getPropertyFloat(int i, float f);

    public native long _getPropertyLong(int i, long j);

    public native String _getPropertyString(int i);

    public native void _prepareAsync() throws IllegalStateException, IjkMediaException;

    public native void _setOption(int i, String str, long j);

    public native void _setOption(int i, String str, String str2);

    public native void _setPropertyFloat(int i, float f);

    public native void _setPropertyLong(int i, long j);

    public native void _setPropertyString(int i, String str);

    public native int _switchPathSyncFrame(String str);

    public native long getCurrentPosition();

    public native boolean isPlaying();

    static /* synthetic */ int access$1308(TaobaoMediaPlayer x0) {
        int i = x0.mLowABRCount;
        x0.mLowABRCount = i + 1;
        return i;
    }

    public static void loadLibrariesOnce(CustomLibLoader customLibLoader) {
        synchronized (TaobaoMediaPlayer.class) {
            if (!mIsLibLoaded) {
                if (customLibLoader != null) {
                    try {
                        customLibLoader.loadLibrary("c++_shared");
                        customLibLoader.loadLibrary("tbffmpeg");
                        customLibLoader.loadLibrary("taobaoplayer");
                    } catch (Throwable e) {
                        Log.e(TAG, "loadLibrariesOnce loadLibrary fail ---" + e.getMessage() + " " + e.getStackTrace());
                    }
                } else {
                    System.loadLibrary("c++_shared");
                    System.loadLibrary("tbffmpeg");
                    System.loadLibrary("taobaoplayer");
                }
                mIsLibLoaded = true;
            }
        }
    }

    public TaobaoMediaPlayer() {
        initPlayer((CustomLibLoader) null);
    }

    public TaobaoMediaPlayer(Context context) {
        super(context);
        initPlayer((CustomLibLoader) null);
    }

    public TaobaoMediaPlayer(Context context, ConfigAdapter configAdapter) {
        super(context, configAdapter);
        initPlayer((CustomLibLoader) null);
    }

    public void startABR() {
        if (this.mABRHandlerThread == null) {
            this.mFlvRetain = AndroidUtils.parseInt(MediaAdapteManager.mConfigAdapter.getConfig("MediaLive", "abrFlvRetainTime", "5"));
            if (this.mFlvRetain < 0 || this.mFlvRetain >= 15) {
                this.mFlvRetain = 0;
            }
            this.mABRMSG.append("2");
            this.mABRHandlerThread = new HandlerThread("ABRThread");
            this.mABRHandlerThread.start();
            this.mABRHandler = new SamplingHandler(this.mABRHandlerThread.getLooper());
            this.mABRHandler.sendEmptyMessage(1);
        }
    }

    public TaobaoMediaPlayer(Context context, ConfigAdapter configAdapter, CustomLibLoader customLibLoader) {
        super(context, configAdapter);
        initPlayer(customLibLoader);
    }

    public void setConfig(TaoLiveVideoViewConfig config) {
        super.setConfig(config);
        if (this.mConfig != null) {
            if (this.mConfig.mDropFrameForH265) {
                _setPropertyLong(20137, 18);
            }
            _setPropertyLong(21007, (long) this.mConfig.mScenarioType);
            long vdType = 1;
            if (Build.VERSION.SDK_INT >= 21) {
                if (this.mConfig.mDecoderTypeH265 == 1) {
                    vdType = 1 | 16;
                }
                if (this.mConfig.mDecoderTypeH264 == 1) {
                    vdType |= 8;
                }
                _setPropertyLong(FFP_PROP_INT64_VIDEO_DECODER_TYPE, vdType);
            }
            if (this.mConfig.mScenarioType == 2) {
                _setPropertyLong(FFP_PROP_INT64_SOURCER_VIDEO_PIPE_MAX_COUNT, 100);
                _setPropertyLong(FFP_PROP_INT64_SOURCER_AUDIO_PIPE_MAX_COUNT, 175);
            }
            if (Build.VERSION.SDK_INT < 21) {
                _setPropertyLong(FFP_PROP_INT64_ANDROID_SW_RENDER_RGB, 1);
            }
            if (this.mConfigAdapter != null) {
                _setPropertyLong(20101, AndroidUtils.parseBoolean(this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, "UseNewHEVC2", "true")) ? 1 : 0);
                _setPropertyLong(20131, AndroidUtils.isInList(this.mConfig.mSubBusinessType, this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, "ijkAccurateSeekWhiteList", "")) ? 1 : 0);
                if (AndroidUtils.parseBoolean(this.mConfigAdapter.getConfig("MediaLive", ORANGE_SW_RENDER_RGB, "false"))) {
                    _setPropertyLong(FFP_PROP_INT64_ANDROID_SW_RENDER_RGB, 1);
                }
                if (!AndroidUtils.parseBoolean(this.mConfigAdapter.getConfig("MediaLive", ORANGE_FIND_STREAM_INFO_OPT, "true"))) {
                    _setPropertyLong(FFP_PROP_INT64_FIND_STREAM_INFO_OPT, 0);
                }
            }
            _setPropertyLong(FFP_PROP_INT64_AUDIO_DECODER_TYPE, 1);
            if (!TextUtils.isEmpty(this.mPlayUrl) && !this.mPlayUrl.contains(TaoLiveVideoView.TBLIVE_ARTP_SCHEMA) && "LiveRoom".equals(this.mConfig.mSubBusinessType) && MediaAdapteManager.mConfigAdapter != null && useABROrange()) {
                _setPropertyLong(FFP_PROP_INT64_SOURCER_VIDEO_PIPE_MAX_COUNT, 500);
                _setPropertyLong(FFP_PROP_INT64_SOURCER_AUDIO_PIPE_MAX_COUNT, 900);
            }
        }
    }

    private void initPlayer(CustomLibLoader customLibLoader) {
        loadLibrariesOnce(customLibLoader);
        Looper looper = Looper.getMainLooper();
        if (looper != null) {
            this.mEventHandler = new EventHandler(this, looper);
        } else {
            this.mEventHandler = null;
        }
        native_setup(new WeakReference(this));
        _setOption(1, MtopJSBridge.MtopJSParam.TIMEOUT, 4000000);
    }

    public void setDisplay(SurfaceHolder sh) {
        Surface surface;
        if (sh != null) {
            surface = sh.getSurface();
        } else {
            surface = null;
        }
        _setVideoSurface(surface);
    }

    public void setSurface(Surface surface) {
        this.mSurface = surface;
        _setVideoSurface(surface);
    }

    public void setSurfaceSize(int width, int height) {
        _setVideoSurfaceSize(width, height);
    }

    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        String url = monitorDataSource(path);
        if (this.mConfig != null) {
            this.mLowQualityUrl = this.mConfig.mLowQualityUrl;
        }
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
            } else if (url.startsWith("artp") && !TextUtils.isEmpty(this.mCdnIp)) {
                _setOption(1, "cdn_ip", this.mCdnIp);
            }
            if (!TextUtils.isEmpty(url) && url.startsWith(TaoLiveVideoView.TBLIVE_ARTP_SCHEMA)) {
                _setPropertyString(21802, ((BaseDexClassLoader) this.mContext.getClassLoader()).findLibrary("artc_engine"));
                _setPropertyLong(FFP_PROP_INT64_BUFFER_MONITOR_IGNORE_BUFFERING, 1);
                _setPropertyLong(FFP_PROP_INT64_SOURCER_TYPE, 2);
                _setPropertyLong(FFP_PROP_INT64_AUDIO_DECODER_TYPE, 1);
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
        monitorStart();
        _start();
    }

    public void stop() throws IllegalStateException {
        _pause();
    }

    public void pause() throws IllegalStateException {
        monitorPause();
        _pause();
        synchronized (this.mABRLOCK) {
            if (this.mABRHandler != null) {
                this.mABRHandler.removeCallbacksAndMessages((Object) null);
                if (this.mABRHandlerThread != null) {
                    this.mABRHandlerThread.quit();
                    this.mABRHandlerThread = null;
                }
                this.mInputABRData = null;
                this.mABRIndex = 0;
                this.mResultIndex = 2;
                this.mPlayerData = null;
                if (this.mABRNet != null) {
                    this.mABRNet.release();
                    this.mABRNet = null;
                }
            }
        }
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
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
        monitorPlayExperience();
        monitorRelease();
        synchronized (TaobaoMediaPlayer.class) {
            if (this.mEventHandler != null) {
                this.mEventHandler.removeCallbacksAndMessages((Object) null);
                this.mEventHandler = null;
            }
            this.mVideoWidth = 0;
            this.mVideoHeight = 0;
            this.mDuration = 0;
        }
        _release();
        synchronized (this.mABRLOCK) {
            if (this.mABRHandler != null) {
                this.mABRHandler.removeCallbacksAndMessages((Object) null);
                if (this.mABRHandlerThread != null) {
                    this.mABRHandlerThread.quit();
                    this.mABRHandlerThread = null;
                }
                this.mInputABRData = null;
                this.mABRIndex = 0;
                this.mResultIndex = 2;
                this.mPlayerData = null;
                if (this.mABRNet != null) {
                    this.mABRNet.release();
                    this.mABRNet = null;
                }
            }
        }
    }

    public void reset() {
        monitorPlayExperience();
        monitorReset();
        synchronized (TaobaoMediaPlayer.class) {
            if (this.mEventHandler != null) {
                this.mEventHandler.removeCallbacksAndMessages((Object) null);
            }
        }
        _release();
    }

    public boolean isHardwareDecode() {
        return _getPropertyLong(FFP_PROP_INT64_VIDEO_DECODER_TYPE, 0) == 32;
    }

    public boolean isAudioHardwareDecode() {
        return _getPropertyLong(FFP_PROP_INT64_AUDIO_DECODER_TYPE, 0) == 2;
    }

    public void setVolume(float leftVolume, float rightVolume) {
        this.mVolume = leftVolume;
        _setPropertyFloat(FFP_PROP_FLOAT_VOLUME, leftVolume);
        if (leftVolume == 0.0f && rightVolume == 0.0f) {
            _setPropertyLong(21008, 1);
        } else {
            _setPropertyLong(21008, 0);
        }
    }

    public void setPlayRate(float playRate) {
        _setPropertyFloat(10003, playRate);
    }

    public void setMuted(boolean muted) {
        _setPropertyLong(21008, muted ? 1 : 0);
    }

    private static class EventHandler extends Handler {
        private boolean bFirstFrameRendered = false;
        private WeakReference<TaobaoMediaPlayer> mWeakPlayer;

        public EventHandler(TaobaoMediaPlayer mp, Looper looper) {
            super(looper);
            this.mWeakPlayer = new WeakReference<>(mp);
        }

        public void handleMessage(Message msg) {
            TaobaoMediaPlayer player = (TaobaoMediaPlayer) this.mWeakPlayer.get();
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
                        }
                        player.mEncodeType = (String) event_data.obj;
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
                            return;
                        }
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
                        int unused = player.mVideoWidth = (int) event_data.arg2;
                        int unused2 = player.mVideoHeight = (int) event_data.arg3;
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
                            return;
                        }
                        return;
                    case 200:
                        if (event_data.arg1 == 701) {
                            player.monitorBufferStart(event_data.arg2);
                        } else if (event_data.arg1 == 702) {
                            player.monitorBufferEnd(event_data.arg2);
                        } else if (event_data.arg1 == 3) {
                            this.bFirstFrameRendered = true;
                            player.monitorRenderStart(event_data.arg2);
                        } else if (event_data.arg1 == 10004) {
                            player.monitorMediacodecError();
                            if (this.bFirstFrameRendered) {
                                return;
                            }
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
                        int unused3 = player.mVideoSarNum = (int) event_data.arg2;
                        int unused4 = player.mVideoSarDen = (int) event_data.arg3;
                        return;
                    default:
                        return;
                }
            }
        }
    }

    @CalledByNative
    private static void postEventFromNative(Object weakThiz, int what, long arg1, long arg2, long arg3, Object obj) {
        TaobaoMediaPlayer mp;
        if (weakThiz != null && (mp = (TaobaoMediaPlayer) ((WeakReference) weakThiz).get()) != null) {
            synchronized (TaobaoMediaPlayer.class) {
                if (mp.mEventHandler != null) {
                    Message m = mp.mEventHandler.obtainMessage(what);
                    EventData event_data = new EventData();
                    event_data.arg1 = arg1;
                    event_data.arg2 = arg2;
                    event_data.arg3 = arg3;
                    event_data.obj = obj;
                    m.obj = event_data;
                    if (what == 1 || (what == 200 && arg1 == 3)) {
                        mp.mEventHandler.sendMessageAtFrontOfQueue(m);
                    } else {
                        mp.mEventHandler.sendMessage(m);
                    }
                }
            }
        }
    }

    public void switchVideoPathSyncFrame(String path, int index) {
        synchronized (this.mABRLOCK) {
            if (!this.mbIsSwitchingPath && this.mInputABRData != null) {
                this.mTmpIndex = index;
                this.mbIsSwitchingPath = true;
                if (_switchPathSyncFrame(path) != 0) {
                    if (this.mOnInfoListener != null) {
                        this.mOnInfoListener.onInfo((IMediaPlayer) null, 718, 0, 0, (Object) null);
                    }
                    if (this.mOnInfoListeners != null) {
                        for (IMediaPlayer.OnInfoListener listener : this.mOnInfoListeners) {
                            if (listener != null) {
                                listener.onInfo((IMediaPlayer) null, 718, 0, 0, (Object) null);
                            }
                        }
                    }
                    this.mbIsSwitchingPath = false;
                }
            }
        }
    }

    public boolean addSample() {
        float f;
        float f2;
        synchronized (this.mABRLOCK) {
            try {
                if (this.mInputABRData == null || this.mResultIndex > 2 || this.mResultIndex < 0) {
                    return false;
                }
                float[] fArr = this.mPlayerData;
                if (this.mResultIndex == 2) {
                    f = 1.0f;
                } else {
                    f = 0.0f;
                }
                fArr[0] = f;
                float bitrate = (float) _getPropertyLong(20115, 0);
                this.mPlayerData[1] = ((float) _getPropertyLong(20009, 0)) / 250.0f;
                float[] fArr2 = this.mPlayerData;
                if (this.mPlayerData[1] - 0.1f < 0.0f) {
                    f2 = 0.01f;
                } else {
                    f2 = this.mPlayerData[1] - 0.1f;
                }
                fArr2[1] = f2;
                if (((double) this.mPlayerData[1]) >= 0.6d) {
                    this.mPlayerData[1] = 0.6f;
                }
                this.mPlayerData[2] = (((float) _getPropertyLong(21009, 0)) / 1024.0f) / 1000.0f;
                this.mPlayerData[3] = (((float) _getPropertyLong(FFP_PROP_INT64_AVG_DOWNLOAD_TIME, 0)) / 1000000.0f) / 10.0f;
                if (this.mPlayerData[2] <= 0.0f) {
                    this.mPlayerData[2] = 0.001f;
                }
                if (this.mPlayerData[3] <= 0.0f) {
                    this.mPlayerData[3] = 1.0f;
                }
                this.mPlayerData[6] = (bitrate < 1200.0f || bitrate >= 5000.0f) ? 0.4f : ((2.0f * bitrate) / 1024.0f) / 8.0f;
                this.mPlayerData[5] = 0.125f;
                this.mPlayerData[4] = 0.0625f;
                this.mPlayerData[7] = ((float) (47 - (this.mABRIndex > 24 ? 24 : this.mABRIndex))) / 48.0f;
                for (int j = 0; j < 8; j++) {
                    if (this.mABRIndex == 0) {
                        for (int i = 0; i < 8; i++) {
                            this.mInputABRData[(j * 8) + i] = this.mPlayerData[j];
                        }
                    } else {
                        for (int k = 0; k < 7; k++) {
                            this.mInputABRData[(j * 8) + k] = this.mInputABRData[(j * 8) + k + 1];
                        }
                        this.mInputABRData[(j * 8) + 7] = this.mPlayerData[j];
                    }
                }
                this.mABRIndex++;
                return true;
            } catch (Throwable th) {
                return false;
            }
        }
    }

    private class SamplingHandler extends Handler {
        public SamplingHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        ABRFullNet.prepareNet(TaobaoMediaPlayer.this.mContext, new NetPreparedListener<ABRFullNet>() {
                            public void onSucceeded(ABRFullNet abrFullNet) {
                                synchronized (TaobaoMediaPlayer.this.mABRLOCK) {
                                    ABRFullNet unused = TaobaoMediaPlayer.this.mABRNet = abrFullNet;
                                    if (TaobaoMediaPlayer.this.mABRNet != null) {
                                        if (TaobaoMediaPlayer.this.mABRHandlerThread != null) {
                                            SamplingHandler.this.sendEmptyMessageDelayed(3, 100);
                                        } else {
                                            TaobaoMediaPlayer.this.mABRNet.release();
                                            ABRFullNet unused2 = TaobaoMediaPlayer.this.mABRNet = null;
                                        }
                                    }
                                }
                            }

                            public void onProgressUpdate(int i) {
                            }

                            public void onFailed(Throwable throwable) {
                            }
                        }, "dihQvZ71qvQ60k7AShUvt9tHFjUf4O/oFxVnhqItObyP604+9rV2uMuvyb0Va3HRgddRc8B7EIQckQYPnJA3hpRuYBp8o79k3Lw+YTrhkk8=");
                        return;
                    } catch (Throwable e) {
                        Log.e("avsdk", "abr init error:" + e.getMessage());
                        return;
                    }
                case 2:
                    synchronized (TaobaoMediaPlayer.this.mABRLOCK) {
                        TaobaoMediaPlayer.this.mUseABR = true;
                        if (TaobaoMediaPlayer.this.mABRNet != null && TaobaoMediaPlayer.this.addSample()) {
                            try {
                                float[] result = TaobaoMediaPlayer.this.mABRNet.inference(TaobaoMediaPlayer.this.mInputABRData);
                                int tmpIndex = result[0] > result[1] ? 0 : 1;
                                if (result[2] > result[0] && result[2] > result[1] && result[2] >= 0.9f) {
                                    tmpIndex = 2;
                                }
                                Log.d("avsdk", "avsdk abr execute index0:" + result[0] + " index1:" + result[1] + " index2:" + result[2]);
                                if (tmpIndex != TaobaoMediaPlayer.this.mCurrentIndex) {
                                    if (tmpIndex != 2 || TaobaoMediaPlayer.this.mbIsSwitchingPath) {
                                        if (TaobaoMediaPlayer.this.mCurrentIndex == 1 || TaobaoMediaPlayer.this.mbIsSwitchingPath) {
                                            int unused = TaobaoMediaPlayer.this.mLowABRCount = 0;
                                        } else {
                                            StringBuilder appendQuery = new StringBuilder(20);
                                            appendQuery.append("ali_flv_retain=2s");
                                            TaobaoMediaPlayer.this.switchVideoPathSyncFrame(AndroidUtils.appendUri(TaobaoMediaPlayer.this.mLowQualityUrl, appendQuery), 1);
                                        }
                                    } else if (TaobaoMediaPlayer.this.mLowABRCount >= 8) {
                                        StringBuilder appendQuery2 = new StringBuilder(20);
                                        appendQuery2.append("ali_flv_retain=" + TaobaoMediaPlayer.this.mFlvRetain + "s");
                                        TaobaoMediaPlayer.this.switchVideoPathSyncFrame(AndroidUtils.appendUri(TaobaoMediaPlayer.this.mPlayUrl, appendQuery2), tmpIndex);
                                        int unused2 = TaobaoMediaPlayer.this.mLowABRCount = 0;
                                    } else {
                                        TaobaoMediaPlayer.access$1308(TaobaoMediaPlayer.this);
                                    }
                                }
                            } catch (Throwable e2) {
                                Log.e("avsdk", "abr execute error:" + e2.getMessage());
                            }
                        }
                        sendEmptyMessageDelayed(3, 100);
                    }
                    return;
                case 3:
                    long diff = TaobaoMediaPlayer.this.getCurrentPosition() - TaobaoMediaPlayer.this.mLastPosition;
                    if (diff >= 1000) {
                        long unused3 = TaobaoMediaPlayer.this.mLastPosition = TaobaoMediaPlayer.this.getCurrentPosition();
                        sendEmptyMessage(2);
                        return;
                    } else if (diff < 0) {
                        long unused4 = TaobaoMediaPlayer.this.mLastPosition = TaobaoMediaPlayer.this.getCurrentPosition();
                        synchronized (TaobaoMediaPlayer.this.mABRLOCK) {
                            int unused5 = TaobaoMediaPlayer.this.mCurrentIndex = TaobaoMediaPlayer.this.mTmpIndex;
                            boolean unused6 = TaobaoMediaPlayer.this.mbIsSwitchingPath = false;
                            int unused7 = TaobaoMediaPlayer.this.mResultIndex = TaobaoMediaPlayer.this.mCurrentIndex;
                            TaobaoMediaPlayer.this.mABRMSG.append("_" + TaobaoMediaPlayer.this.mTmpIndex);
                            int time = 1000;
                            if (TaobaoMediaPlayer.this.mCurrentIndex == 1) {
                                time = 20000;
                            }
                            sendEmptyMessageDelayed(3, (long) time);
                        }
                        return;
                    } else {
                        sendEmptyMessageDelayed(3, 100);
                        return;
                    }
                case 4:
                    removeMessages(2);
                    return;
                default:
                    Log.d(TaobaoMediaPlayer.TAG, "ABR Unknown what=" + msg.what);
                    return;
            }
        }
    }
}
