package tv.danmaku.ijk.media.player;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import anet.channel.strategy.HttpDnsAdapter;
import com.alibaba.appmonitor.sample.SampleConfigConstant;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.bftv.fui.constantplugin.Constant;
import com.edge.pcdn.PcdnManager;
import com.edge.pcdn.PcdnType;
import com.taobao.adapter.ABTestAdapter;
import com.taobao.adapter.ConfigAdapter;
import com.taobao.adapter.FirstRenderAdapter;
import com.taobao.adapter.INetworkUtilsAdapter;
import com.taobao.adapter.ITLogAdapter;
import com.taobao.atlas.dexmerge.dx.io.Opcodes;
import com.taobao.ju.track.constants.Constants;
import com.taobao.media.MediaAdapteManager;
import com.taobao.media.MediaConstant;
import com.taobao.media.MediaSystemUtils;
import com.taobao.media.connectionclass.ConnectionClassManager;
import com.taobao.media.connectionclass.DeviceBandwidthSampler;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.taobao.statistic.CT;
import com.taobao.statistic.TBS;
import com.taobao.taobaoavsdk.cache.ApplicationUtils;
import com.taobao.taobaoavsdk.cache.PlayerEnvironment;
import com.taobao.taobaoavsdk.cache.library.HttpProxyCacheServer;
import com.taobao.taobaoavsdk.util.AndroidUtils;
import com.taobao.taobaoavsdk.util.TBAVNetworkUtils;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoView;
import com.taobao.taobaoavsdk.widget.media.TaoLiveVideoViewConfig;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import mtopsdk.common.util.SymbolExpUtil;

public abstract class MonitorMediaPlayer extends AbstractMediaPlayer {
    public static final String ABTEST_USE_CACHE_COMOPONENT = "useCacheComponent";
    public static final String ABTEST_USE_CACHE_ENABLE = "useCache";
    public static final String ABTEST_USE_CACHE_MODULE = "useCacheModule";
    public static final String ABTEST_USE_CDNIP_COMOPONENT = "useCDNIPComponent";
    public static final String ABTEST_USE_CDNIP_ENABLE = "useCDNIP";
    public static final String ABTEST_USE_CDNIP_MODULE = "useCDNIPModule";
    public static final String ABTEST_USE_PCDN_COMOPONENT = "usePcdnComponent";
    public static final String ABTEST_USE_PCDN_ENABLE = "usePcdn";
    public static final String ABTEST_USE_PCDN_MODULE = "usePcdnModule";
    public static final String DEFAULT_NO_TRAFFIC_HOST = "";
    public static final String KEY_NO_TRAFFIC_HOST = "PolicyHost";
    public static final int MAX_EVENT_NUM = 30;
    public static final String MornitorBufferingNew = "stalled";
    public static final String PCDN_FOR_LIVE_ENABLE = "pcdnLiveEnable2";
    public static final int PLAYER_EVENT_COMPLETE = 6;
    public static final int PLAYER_EVENT_ERROR = 7;
    public static final int PLAYER_EVENT_FIRST_RENDER = 8;
    public static final int PLAYER_EVENT_PAUSE = 3;
    public static final int PLAYER_EVENT_PREPARED = 1;
    public static final int PLAYER_EVENT_SEEK = 5;
    public static final int PLAYER_EVENT_STALLED = 4;
    public static final int PLAYER_EVENT_START = 2;
    public static int PLAYER_REPORT_DURATION = 10;
    public static int REPORT_DURATION = 60;
    public static final String RTCSTREAM_MAIDIAN_DETAIL = "RtcStreamStats_Detail";
    public static final String RTCSTREAM_MAIDIAN_DOUDI = "RtcStreamStats_Degrade";
    public static final String RTCSTREAM_MAIDIAN_INFO = "RtcStreamStats_Info";
    public static final String VIDEO_CACHE_BLACK = "videoCacheBlackList";
    public static final String VIDEO_CACHE_ENABLE = "videoCacheEnable3";
    public static final String VIDEO_CDNIP_ENABLE = "videoCDNIpEnable3";
    public static final String VIDEO_CLIP_CDNIP_ENABLE = "videoClipCDNIpEnable3";
    private static final int maxCongestBuffer = 1000;
    public static final String mornitorPlayerError = "playerError";
    protected String AppMonitor_Module;
    boolean autoPause;
    protected boolean bFirstFrameRendered;
    protected boolean bInstantSeeked;
    protected volatile boolean bIsCompleteHitCache;
    protected volatile boolean bIsHitCache;
    protected volatile boolean bLooping;
    public boolean bMediacodeError;
    public boolean bNeedCommitPlayExperience;
    protected boolean bPaused;
    protected boolean bSeeked;
    protected volatile boolean bUsePcdn;
    protected volatile boolean bUseVideoCache;
    private String end2endDelay;
    StringBuilder mABRMSG;
    protected ABTestAdapter mAbTestAdapter;
    long mAudioBytes;
    public volatile int mBeatCount;
    protected long mBufferingCount;
    protected long mBufferingHeartBeatCount;
    protected StringBuilder mBufferingHeartBeatMsg;
    protected long mBufferingHeartBeatTotalTime;
    protected long mBufferingStart;
    protected long mBufferingTotalTime;
    protected String mCdnIp;
    protected boolean mCommitPlayError;
    protected TaoLiveVideoViewConfig mConfig;
    protected ConfigAdapter mConfigAdapter;
    protected TaoLiveVideoViewConfig mConfigClone;
    protected Context mContext;
    int mDurTimeInHeartBeatLCycle;
    String mEncodeType;
    protected Map<String, String> mExtInfo;
    long mFirstEndtime;
    protected FirstRenderAdapter mFirstRenderAdapter;
    public long mFirstRenderTime;
    Handler mHandler;
    private long mHeartBeatCount;
    StringBuilder mHeartBeatDecodeFPS;
    StringBuilder mHeartBeatDownloadFPS;
    StringBuilder mHeartBeatFPS;
    StringBuilder mHeartBeatNetSpeed;
    private long mHttpOpenTime;
    protected long mLastBuffering;
    protected long mLastCommitPlaying;
    int mLastErrorCode;
    int mLastExtra;
    int mLastIsConnected;
    protected long mLastPlayTime;
    String mLocalIP;
    private final Object mLock;
    protected int mLoopCount;
    protected String mLowQualityUrl;
    private AtomicInteger mNetCounter;
    String mNetType;
    protected INetworkUtilsAdapter mNetworkUtilsAdapter;
    private long mOpenFileTime;
    protected String mPageUrl;
    protected PhoneStateListener mPhoneStateListener;
    protected String mPlayUrl;
    protected LinkedList<Integer> mPlayerEventList;
    /* access modifiers changed from: private */
    public Runnable mPlayerRun;
    protected long mPrepareStartTime;
    protected long mPreparedTime;
    String mServerIP;
    protected long mStartTime;
    protected Surface mSurface;
    protected TelephonyManager mTelephonyManager;
    protected ITLogAdapter mTlogAdapter;
    protected long mTotalPlayTime;
    /* access modifiers changed from: private */
    public Runnable mUTRun;
    protected boolean mUseABR;
    public long mUserFirstRenderTime;
    protected long mUserPreparedTime;
    String mVia;
    long mVideoBytes;
    long mVideoDuration;
    private String mVideoToken;
    protected float mVolume;

    public MonitorMediaPlayer(Context context) {
        this(context, (ConfigAdapter) null);
    }

    public MonitorMediaPlayer(Context context, ConfigAdapter configAdapter) {
        this.AppMonitor_Module = "";
        this.mBufferingStart = 0;
        this.mLastBuffering = 0;
        this.mLastCommitPlaying = 0;
        this.mPrepareStartTime = 0;
        this.mStartTime = 0;
        this.mPreparedTime = 0;
        this.mUserPreparedTime = 0;
        this.mFirstRenderTime = 0;
        this.mUserFirstRenderTime = 0;
        this.mLastPlayTime = 0;
        this.mTotalPlayTime = 0;
        this.mBufferingCount = 0;
        this.mBufferingTotalTime = 0;
        this.mBufferingHeartBeatCount = 0;
        this.mBufferingHeartBeatTotalTime = 0;
        this.mLock = new Object();
        this.bNeedCommitPlayExperience = false;
        this.bMediacodeError = false;
        this.bFirstFrameRendered = false;
        this.mCommitPlayError = false;
        this.mPlayerEventList = new LinkedList<>();
        this.mLastErrorCode = 0;
        this.mLastExtra = 0;
        this.mLastIsConnected = 1;
        this.mHandler = null;
        this.mAudioBytes = 0;
        this.mVideoBytes = 0;
        this.mVideoDuration = 0;
        this.autoPause = false;
        this.bPaused = false;
        this.end2endDelay = null;
        this.mUTRun = null;
        this.mPlayerRun = null;
        this.mNetCounter = new AtomicInteger();
        this.mHeartBeatFPS = new StringBuilder(10);
        this.mHeartBeatDownloadFPS = new StringBuilder(10);
        this.mHeartBeatDecodeFPS = new StringBuilder(10);
        this.mHeartBeatNetSpeed = new StringBuilder(10);
        this.mABRMSG = new StringBuilder(50);
        this.mContext = context;
        if (!(this.mContext == null || this.mContext.getApplicationContext() == null)) {
            ApplicationUtils.setApplicationOnce((Application) this.mContext.getApplicationContext());
        }
        this.mConfigAdapter = configAdapter;
        if (!(this.mContext == null || Looper.myLooper() == null)) {
            if (this.mPhoneStateListener == null) {
                this.mPhoneStateListener = new PhoneStateListener() {
                    public void onCallStateChanged(int state, String incomingNumber) {
                        super.onCallStateChanged(state, incomingNumber);
                        switch (state) {
                            case 0:
                                if (MonitorMediaPlayer.this.autoPause) {
                                    MonitorMediaPlayer.this.autoPause = false;
                                    try {
                                        MonitorMediaPlayer.this.start();
                                        return;
                                    } catch (Exception e) {
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            case 1:
                                if (MonitorMediaPlayer.this.isPlaying()) {
                                    MonitorMediaPlayer.this.autoPause = true;
                                    MonitorMediaPlayer.this.pause();
                                    return;
                                }
                                return;
                            default:
                                return;
                        }
                    }
                };
            }
            this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
            if (!(this.mTelephonyManager == null || this.mPhoneStateListener == null)) {
                this.mTelephonyManager.listen(this.mPhoneStateListener, 32);
            }
        }
        if (this.mHandler == null) {
            this.mHandler = new Handler();
        }
    }

    public MonitorMediaPlayer() {
        this.AppMonitor_Module = "";
        this.mBufferingStart = 0;
        this.mLastBuffering = 0;
        this.mLastCommitPlaying = 0;
        this.mPrepareStartTime = 0;
        this.mStartTime = 0;
        this.mPreparedTime = 0;
        this.mUserPreparedTime = 0;
        this.mFirstRenderTime = 0;
        this.mUserFirstRenderTime = 0;
        this.mLastPlayTime = 0;
        this.mTotalPlayTime = 0;
        this.mBufferingCount = 0;
        this.mBufferingTotalTime = 0;
        this.mBufferingHeartBeatCount = 0;
        this.mBufferingHeartBeatTotalTime = 0;
        this.mLock = new Object();
        this.bNeedCommitPlayExperience = false;
        this.bMediacodeError = false;
        this.bFirstFrameRendered = false;
        this.mCommitPlayError = false;
        this.mPlayerEventList = new LinkedList<>();
        this.mLastErrorCode = 0;
        this.mLastExtra = 0;
        this.mLastIsConnected = 1;
        this.mHandler = null;
        this.mAudioBytes = 0;
        this.mVideoBytes = 0;
        this.mVideoDuration = 0;
        this.autoPause = false;
        this.bPaused = false;
        this.end2endDelay = null;
        this.mUTRun = null;
        this.mPlayerRun = null;
        this.mNetCounter = new AtomicInteger();
        this.mHeartBeatFPS = new StringBuilder(10);
        this.mHeartBeatDownloadFPS = new StringBuilder(10);
        this.mHeartBeatDecodeFPS = new StringBuilder(10);
        this.mHeartBeatNetSpeed = new StringBuilder(10);
        this.mABRMSG = new StringBuilder(50);
    }

    public void setConfig(TaoLiveVideoViewConfig config) {
        this.mConfig = config;
        this.mConfigClone = config;
        if (this.mConfig != null && this.mConfig.mBusinessId != null) {
            if (this.mConfig.mBusinessId.replaceAll(" ", "").equals(MediaConstant.LBLIVE_SOURCE)) {
                this.AppMonitor_Module = "TBMediaPlayerBundle-android";
            } else {
                this.AppMonitor_Module = "TBMediaPlayerBundle-video";
            }
            registerMonitor();
        }
    }

    public TaoLiveVideoViewConfig getConfig() {
        return this.mConfig;
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public void setFirstRenderAdapter(FirstRenderAdapter adapter) {
        this.mFirstRenderAdapter = adapter;
    }

    public void setTlogAdapter(ITLogAdapter adapter) {
        this.mTlogAdapter = adapter;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.mExtInfo = extInfo;
    }

    public void setLooping(boolean looping) {
        this.bLooping = looping;
    }

    public boolean isUseVideoCache() {
        return this.bUseVideoCache;
    }

    public boolean isUsePcdn() {
        return this.bUsePcdn;
    }

    private boolean useCache() {
        boolean bCache = false;
        if (this.mConfig == null || this.mConfig.mScenarioType != 2 || TextUtils.isEmpty(this.mConfig.mCacheKey) || this.mPlayUrl == null || this.mPlayUrl.contains(".m3u8") || !this.mPlayUrl.startsWith("http")) {
            return false;
        }
        if (AndroidUtils.parseBoolean(this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, VIDEO_CACHE_ENABLE, "true") : "true") && (this.mAbTestAdapter == null || ABTEST_USE_CACHE_ENABLE.equals(this.mAbTestAdapter.getBucket(ABTEST_USE_CACHE_COMOPONENT, ABTEST_USE_CACHE_MODULE)))) {
            bCache = true;
        }
        if (!bCache || this.mConfigAdapter == null || !AndroidUtils.isInList(this.mConfig.mSubBusinessType, this.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, VIDEO_CACHE_BLACK, ""))) {
            return bCache;
        }
        return false;
    }

    private boolean usePCDNForLive() {
        boolean z;
        String bucket;
        if (this.mConfig != null && ApplicationUtils.mIsPCDNStarted && "LiveRoom".equals(this.mConfig.mSubBusinessType) && this.mConfig.mScenarioType == 0 && !TextUtils.isEmpty(this.mPlayUrl) && this.mPlayUrl.contains(".flv") && !this.mPlayUrl.contains(".m3u8") && this.mPlayUrl.startsWith("http") && (this.mConfig.mPlayerType == 1 || this.mConfig.mPlayerType == 3)) {
            if (AndroidUtils.parseBoolean(this.mConfigAdapter != null ? this.mConfigAdapter.getConfig("MediaLive", PCDN_FOR_LIVE_ENABLE, "false") : "false")) {
                if (this.mAbTestAdapter == null) {
                    bucket = "";
                } else {
                    bucket = this.mAbTestAdapter.getBucket(ABTEST_USE_PCDN_COMOPONENT, ABTEST_USE_PCDN_MODULE);
                }
                if (ABTEST_USE_PCDN_ENABLE.equals(bucket)) {
                    z = true;
                    this.bUsePcdn = z;
                    return this.bUsePcdn;
                }
            }
        }
        z = false;
        this.bUsePcdn = z;
        return this.bUsePcdn;
    }

    /* access modifiers changed from: protected */
    public boolean useABROrange() {
        return (MediaAdapteManager.mMeasureAdapter == null || MediaAdapteManager.mConfigAdapter == null || !AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter.getConfig("MediaLive", "abrEnable", "false")) || MediaAdapteManager.mABTestAdapter == null || !MediaConstant.ABTEST_USE_ABR.equals(MediaAdapteManager.mABTestAdapter.getBucket(MediaConstant.ABTEST_ABR_COMOPONENT, MediaConstant.ABTEST_ABR_MODULE))) ? false : true;
    }

    private boolean useABR() {
        return isHardwareDecode() && !isAudioHardwareDecode() && AndroidUtils.isInListWildcards(this.mConfig.mAccountId, MediaAdapteManager.mConfigAdapter.getConfig("MediaLive", MediaConstant.ABR_ANCHORID_WHITE_LIST, ""), "ALL_ID");
    }

    public boolean isHardwareDecode() {
        return true;
    }

    public boolean isAudioHardwareDecode() {
        return true;
    }

    public boolean isHitCache() {
        return this.bIsHitCache;
    }

    public boolean isCompleteHitCache() {
        return this.bIsCompleteHitCache;
    }

    /* access modifiers changed from: protected */
    public boolean useNoTraffic() {
        return false;
    }

    public static String getProxyVideoUrl(Context context, TaoLiveVideoViewConfig config, String url) {
        if (context == null || config == null) {
            return url;
        }
        try {
            if (TextUtils.isEmpty(url) || TextUtils.isEmpty(config.mCacheKey) || url.contains(".m3u8") || !url.startsWith("http")) {
                return url;
            }
            StringBuilder appendQuery = new StringBuilder(100);
            if (!TextUtils.isEmpty(config.mPlayToken)) {
                if (!TextUtils.isEmpty(appendQuery)) {
                    appendQuery.append("&");
                }
                appendQuery.append("playTokenId=" + config.mPlayToken);
            }
            if (!TextUtils.isEmpty(appendQuery)) {
                appendQuery.append("&");
            }
            appendQuery.append("videoCacheId=" + config.mCacheKey);
            String playTmpUrl = AndroidUtils.appendUri(url, appendQuery);
            if (config.mbEnableTBNet) {
                StringBuilder appendNet = new StringBuilder(20);
                appendNet.append("useTBNetProxy=" + config.mbEnableTBNet);
                playTmpUrl = AndroidUtils.appendUri(playTmpUrl, appendNet);
            }
            return PlayerEnvironment.getProxy(context).getProxyUrl(playTmpUrl);
        } catch (Exception e) {
            return url;
        }
    }

    /* access modifiers changed from: protected */
    public String monitorDataSource(String path) {
        boolean z = true;
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        this.bUseVideoCache = false;
        this.bIsHitCache = false;
        this.bIsCompleteHitCache = false;
        this.mPlayUrl = path;
        this.mCdnIp = getCdnIp();
        if (useCache()) {
            StringBuilder appendQuery = new StringBuilder(100);
            if (!TextUtils.isEmpty(this.mCdnIp)) {
                appendQuery.append("cdnIp=" + this.mCdnIp);
            }
            if (!TextUtils.isEmpty(this.mConfig.mPlayToken)) {
                if (!TextUtils.isEmpty(appendQuery)) {
                    appendQuery.append("&");
                }
                appendQuery.append("playTokenId=" + this.mConfig.mPlayToken);
            }
            if (this.mConfig.mVideoLength != Integer.MAX_VALUE && this.mConfig.mVideoLength > 0) {
                if (!TextUtils.isEmpty(appendQuery)) {
                    appendQuery.append("&");
                }
                appendQuery.append("videoLength=" + this.mConfig.mVideoLength);
            }
            if (!TextUtils.isEmpty(appendQuery)) {
                appendQuery.append("&");
            }
            appendQuery.append("videoCacheId=" + this.mConfig.mCacheKey);
            String playTmpUrl = AndroidUtils.appendUri(this.mPlayUrl, appendQuery);
            String proxyUrl = !TextUtils.isEmpty(this.mConfig.mHighCachePath) ? this.mConfig.mHighCachePath : PlayerEnvironment.getCompleteCachePath(this.mContext, playTmpUrl);
            if (TextUtils.isEmpty(proxyUrl)) {
                if (this.mConfig.mbEnableTBNet) {
                    StringBuilder appendNet = new StringBuilder(20);
                    appendNet.append("useTBNetProxy=" + this.mConfig.mbEnableTBNet);
                    playTmpUrl = AndroidUtils.appendUri(playTmpUrl, appendNet);
                }
                HttpProxyCacheServer proxyCacheServer = PlayerEnvironment.getProxy(this.mContext);
                String proxyUrl2 = proxyCacheServer.getProxyUrl(playTmpUrl);
                this.bUseVideoCache = proxyCacheServer.isCacheAvaiable();
                this.bIsCompleteHitCache = false;
                if (!this.bUseVideoCache || !proxyCacheServer.isHitCache(playTmpUrl)) {
                    z = false;
                }
                this.bIsHitCache = z;
                if (!this.bUseVideoCache) {
                    return this.mPlayUrl;
                }
                this.mPlayUrl = playTmpUrl;
                return proxyUrl2;
            }
            this.bUseVideoCache = true;
            this.bIsCompleteHitCache = true;
            this.bIsHitCache = true;
            return proxyUrl;
        }
        if (usePCDNForLive() && Build.VERSION.SDK_INT >= 21 && this.mNetType == "WIFI") {
            if (this.mConfig.mEdgePcdn) {
                StringBuilder appendQuery2 = new StringBuilder(10);
                appendQuery2.append("top_anchor=true");
                this.mPlayUrl = AndroidUtils.appendUri(this.mPlayUrl, appendQuery2);
            }
            this.mPlayUrl = PcdnManager.PCDNAddress(PcdnType.LIVE, this.mPlayUrl);
            if (TextUtils.isEmpty(this.mPlayUrl) || !this.mPlayUrl.contains("http://127.0.0.1:")) {
                this.bUsePcdn = false;
            } else {
                this.mCdnIp = null;
                this.bUsePcdn = true;
            }
        } else if (this.mConfig.mSVCEnable && !TextUtils.isEmpty(this.mPlayUrl) && this.mPlayUrl.startsWith("http") && "LiveRoom".equals(this.mConfig.mSubBusinessType) && this.mPlayUrl.contains("liveng.alicdn.com") && this.mPlayUrl.contains(".flv")) {
            StringBuilder appendQuery3 = new StringBuilder(30);
            appendQuery3.append("ali_drop_0_ref_vf=on");
            appendQuery3.append("&ali_drop_skip_ref_vf=" + AndroidUtils.parseInt(MediaAdapteManager.mConfigAdapter.getConfig("MediaLive", "dropFrameLevel", "1")));
            this.mPlayUrl = AndroidUtils.appendUri(this.mPlayUrl, appendQuery3);
        }
        return this.mPlayUrl;
    }

    public void setNetworkUtilsAdapter(INetworkUtilsAdapter networkUtilsAdapter) {
        this.mNetworkUtilsAdapter = networkUtilsAdapter;
    }

    public void setABtestAdapter(ABTestAdapter abTestAdapter) {
        this.mAbTestAdapter = abTestAdapter;
    }

    /* access modifiers changed from: protected */
    public void monitorPrepare() {
        this.bNeedCommitPlayExperience = true;
        this.bFirstFrameRendered = false;
        this.mBufferingHeartBeatCount = 0;
        this.mBufferingHeartBeatTotalTime = 0;
        this.mBufferingHeartBeatMsg = new StringBuilder(20);
        this.mBufferingCount = 0;
        this.mBufferingTotalTime = 0;
        this.mFirstRenderTime = 0;
        this.mPreparedTime = 0;
        this.mLastBuffering = 0;
        this.mPrepareStartTime = System.currentTimeMillis();
        this.mCommitPlayError = false;
        this.mLastErrorCode = 0;
        this.mLastExtra = 0;
        this.mLastIsConnected = 1;
        this.mHeartBeatCount = 0;
    }

    /* access modifiers changed from: private */
    public void commitPlaying(int duration) {
        int i;
        int i2;
        int i3;
        int i4;
        if (duration > 0 && this.mUTRun != null && this.mPlayerRun != null && !TextUtils.isEmpty(this.mPlayUrl) && !this.mPlayUrl.contains(TaoLiveVideoView.TBLIVE_ARTP_SCHEMA) && "LiveRoom".equals(this.mConfig.mSubBusinessType) && !this.mPlayUrl.contains(".m3u8")) {
            this.mDurTimeInHeartBeatLCycle = 0;
            this.mLastCommitPlaying = System.currentTimeMillis();
            if (!this.bPaused) {
                try {
                    if (!(this.mContext == null || MediaAdapteManager.mMediaNetworkUtilsAdapter == null)) {
                        this.mLastIsConnected = TBAVNetworkUtils.isConnected(MediaAdapteManager.mMediaNetworkUtilsAdapter, this.mContext) ? 1 : 0;
                    }
                    String playerType = null;
                    if (this instanceof IjkMediaPlayer) {
                        IjkMediaPlayer player = (IjkMediaPlayer) this;
                        this.mAudioBytes = player._getPropertyLong(20008, 0);
                        this.mVideoBytes = player._getPropertyLong(20007, 0);
                        playerType = "ijkplayer";
                        this.mHttpOpenTime = player._getPropertyLong(20122, 0);
                        this.mOpenFileTime = player._getPropertyLong(20123, 0);
                        this.mServerIP = player._getPropertyString(21003);
                    } else if (this instanceof TaobaoMediaPlayer) {
                        TaobaoMediaPlayer player2 = (TaobaoMediaPlayer) this;
                        this.mAudioBytes = player2._getPropertyLong(20008, 0);
                        this.mVideoBytes = player2._getPropertyLong(20007, 0);
                        playerType = "taobaoplayer";
                        this.mHttpOpenTime = player2._getPropertyLong(20122, 0);
                        this.mOpenFileTime = player2._getPropertyLong(20123, 0);
                        if (!this.bUsePcdn || this.mConfig == null || TextUtils.isEmpty(this.mConfig.mPlayToken)) {
                            this.mServerIP = player2._getPropertyString(21003);
                        } else {
                            try {
                                this.mServerIP = PcdnManager.PCDNGet(PcdnType.LIVE, this.mConfig.mPlayToken, "");
                                String[] result = this.mServerIP.split(SymbolExpUtil.SYMBOL_SEMICOLON);
                                if (result != null && result.length >= 3) {
                                    this.mServerIP = result[2];
                                }
                            } catch (Exception e) {
                            }
                        }
                    } else if (this instanceof NativeMediaPlayer) {
                        playerType = "mediaplayer";
                    }
                    CT ct = CT.Button;
                    String[] strArr = new String[39];
                    strArr[0] = "player_type=" + playerType;
                    strArr[1] = "play_scenario=" + this.mConfig.mScenarioType;
                    strArr[2] = "server_ip=" + this.mServerIP;
                    strArr[3] = "route_nodes=" + this.mVia;
                    strArr[4] = "media_url=" + this.mPlayUrl;
                    strArr[5] = "feed_id=" + this.mConfig.mFeedId;
                    strArr[6] = "abnormal_count=" + this.mBufferingHeartBeatCount;
                    strArr[7] = "abnormal_total_time=" + this.mBufferingHeartBeatTotalTime;
                    strArr[8] = "bufferHeartBeatMsg=" + this.mBufferingHeartBeatMsg;
                    strArr[9] = "anchor_account_id=" + this.mConfig.mAccountId;
                    strArr[10] = "video_definition=" + this.mConfig.mVideoDefinition;
                    strArr[11] = "business_type=" + this.mConfig.mBusinessId;
                    strArr[12] = "sub_business_type=" + this.mConfig.mSubBusinessType;
                    strArr[13] = "video_width=" + getVideoWidth();
                    strArr[14] = "video_height=" + getVideoHeight();
                    strArr[15] = "player_status_nodes=" + getPlayerEvent();
                    strArr[16] = "encode_type=" + this.mEncodeType;
                    strArr[17] = "video_duration=" + this.mVideoDuration;
                    strArr[18] = "play_time=" + (((long) duration) + (this.mHeartBeatCount * ((long) REPORT_DURATION)));
                    strArr[19] = "duration=" + duration;
                    strArr[20] = "audio_cache_btyes=" + this.mAudioBytes;
                    strArr[21] = "video_cache_btyes=" + this.mVideoBytes;
                    strArr[22] = "hardware_hevc=" + this.mConfigClone.mDecoderTypeH265;
                    strArr[23] = "hardware_avc=" + this.mConfigClone.mDecoderTypeH264;
                    strArr[24] = "first_frame_rendering_time=" + this.mFirstRenderTime;
                    strArr[25] = "user_first_frame_time=" + this.mUserFirstRenderTime;
                    StringBuilder append = new StringBuilder().append("is_tbnet=");
                    if (this.mConfigClone.mbEnableTBNet) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    strArr[26] = append.append(i).toString();
                    StringBuilder append2 = new StringBuilder().append("is_usecache=");
                    if (this.bUseVideoCache) {
                        i2 = 1;
                    } else {
                        i2 = 0;
                    }
                    strArr[27] = append2.append(i2).toString();
                    strArr[28] = "play_token=" + this.mConfigClone.mPlayToken;
                    strArr[29] = "error_code=" + this.mLastErrorCode;
                    strArr[30] = "extra=" + this.mLastExtra;
                    strArr[31] = "is_connected=" + this.mLastIsConnected;
                    strArr[32] = "video_avg_fps=" + this.mHeartBeatFPS;
                    strArr[33] = "video_avg_decode_fps=" + this.mHeartBeatDecodeFPS;
                    strArr[34] = "video_avg_download_fps=" + this.mHeartBeatDownloadFPS;
                    strArr[35] = "net_speed=" + this.mHeartBeatNetSpeed;
                    strArr[36] = "first_render_timestamp=" + this.mFirstEndtime;
                    strArr[37] = "netspeed_interval=" + PLAYER_REPORT_DURATION;
                    strArr[38] = "avformat_time=httpOpenTime:" + this.mHttpOpenTime + "/fileOpenTime:" + this.mOpenFileTime;
                    TBS.Adv.ctrlClicked("Page_Video", ct, "Playing", strArr);
                    if (MediaSystemUtils.isApkDebuggable()) {
                        StringBuilder append3 = new StringBuilder().append("commit heartbeat play msg:player_type=").append(playerType).append(" ").append("play_scenario=").append(this.mConfig.mScenarioType).append(" ").append("server_ip=").append(this.mServerIP).append(" ").append("route_nodes=").append(this.mVia).append(" ").append(" ").append("feed_id=").append(this.mConfig.mFeedId).append(" ").append("abnormal_count=").append(this.mBufferingHeartBeatCount).append("abnormal_total_time=").append(this.mBufferingHeartBeatTotalTime).append(" ").append("bufferHeartBeatMsg=").append(this.mBufferingHeartBeatMsg).append(" ").append("anchor_account_id=").append(this.mConfig.mAccountId).append(" ").append("video_definition=").append(this.mConfig.mVideoDefinition).append(" ").append("business_type=").append(this.mConfig.mBusinessId).append(" ").append("sub_business_type=").append(this.mConfig.mSubBusinessType).append(" ").append("video_width=").append(getVideoWidth()).append(" ").append("video_height=").append(getVideoHeight()).append(" ").append("player_status_nodes=").append(getPlayerEvent()).append(" ").append("encode_type=").append(this.mEncodeType).append(" ").append("video_duration=").append(this.mVideoDuration).append(" ").append("play_time=").append(((long) duration) + (this.mHeartBeatCount * ((long) REPORT_DURATION))).append(" ").append("duration=").append(duration).append(" ").append("audio_cache_btyes=").append(this.mAudioBytes).append(" ").append("video_cache_btyes=").append(this.mVideoBytes).append(" ").append("hardware_hevc=").append(this.mConfigClone.mDecoderTypeH265).append(" ").append("hardware_avc=").append(this.mConfigClone.mDecoderTypeH264).append(" ").append("first_frame_rendering_time=").append(this.mFirstRenderTime).append(" ").append("user_first_frame_time=").append(this.mUserFirstRenderTime).append(" ").append("is_tbnet=");
                        if (this.mConfigClone.mbEnableTBNet) {
                            i3 = 1;
                        } else {
                            i3 = 0;
                        }
                        StringBuilder append4 = append3.append(i3).append(" ").append("is_usecache=");
                        if (this.bUseVideoCache) {
                            i4 = 1;
                        } else {
                            i4 = 0;
                        }
                        Log.d("AVSDK", append4.append(i4).append(" ").append("play_token=").append(this.mConfigClone.mPlayToken).append(" ").append("error_code=").append(" ").append(this.mLastErrorCode).append(" ").append("extra=").append(this.mLastExtra).append(" ").append("is_connected=").append(this.mLastIsConnected).append(" ").append("video_avg_fps=").append(this.mHeartBeatFPS).append(" ").append("video_avg_decode_fps=").append(this.mHeartBeatDecodeFPS).append(" ").append("video_avg_download_fps=").append(this.mHeartBeatDownloadFPS).append(" ").append("net_speed=").append(this.mHeartBeatNetSpeed).append(" ").append("avformat_time=").append("httpOpenTime:").append(this.mHttpOpenTime).append("/fileOpenTime:").append(this.mOpenFileTime).toString());
                    }
                    this.mBufferingHeartBeatCount = 0;
                    this.mBufferingHeartBeatTotalTime = 0;
                    this.mHeartBeatFPS.setLength(0);
                    this.mHeartBeatDecodeFPS.setLength(0);
                    this.mHeartBeatDownloadFPS.setLength(0);
                    this.mHeartBeatNetSpeed.setLength(0);
                    this.mBufferingHeartBeatMsg.setLength(0);
                    this.mHeartBeatCount++;
                } catch (Throwable th) {
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void monitorPrepared(long preparedTime) {
        long endtime = preparedTime > 0 ? preparedTime : System.currentTimeMillis();
        this.mPreparedTime = endtime - this.mPrepareStartTime;
        if (this.mFirstRenderAdapter == null || this.mFirstRenderAdapter.getStartTime() <= 0) {
            this.mUserPreparedTime = this.mPreparedTime;
        } else {
            this.mUserPreparedTime = endtime - this.mFirstRenderAdapter.getStartTime();
        }
        this.mStartTime = endtime;
        this.mVideoDuration = getDuration() / 1000;
        if (this.mConfig != null) {
            this.mConfigClone = this.mConfig.clone();
        }
        if (!(this.mContext == null || !(this.mContext instanceof Activity) || ((Activity) this.mContext).getIntent() == null || ((Activity) this.mContext).getIntent().getData() == null || TextUtils.isEmpty(((Activity) this.mContext).getIntent().getData().toString()))) {
            this.mPageUrl = ((Activity) this.mContext).getIntent().getData().toString();
        }
        if (this instanceof IjkMediaPlayer) {
            IjkMediaPlayer player = (IjkMediaPlayer) this;
            this.mServerIP = player._getPropertyString(21003);
            this.mLocalIP = player._getPropertyString(21004);
            this.mVia = player._getPropertyString(IjkMediaPlayer.FFP_PROP_SEND_VIA);
        }
        monitorPlayerEvent(1);
    }

    public void artpDegradeMonitor(int errorCode, String playUrl) {
        if (this.mPlayUrl != null && this.mPlayUrl.contains(TaoLiveVideoView.TBLIVE_ARTP_SCHEMA) && this.mConfig != null && this.mConfig.mPlayerType != 2) {
            if ((this instanceof IjkMediaPlayer) || (this instanceof TaobaoMediaPlayer)) {
                String statsString = ("SeqNO=9997" + ",feed_id=" + this.mConfigClone.mFeedId) + ",anchor_account_id=" + this.mConfigClone.mAccountId;
                if (this instanceof TaobaoMediaPlayer) {
                    statsString = (statsString + "," + ((TaobaoMediaPlayer) this)._getPropertyString(IjkMediaPlayer.RTCSTREAM_TRANSPORT_STREAM_INFO)) + ",play_time=" + this.mTotalPlayTime;
                }
                if (this instanceof IjkMediaPlayer) {
                    statsString = statsString + "," + ((IjkMediaPlayer) this)._getPropertyString(IjkMediaPlayer.RTCSTREAM_TRANSPORT_STREAM_INFO);
                }
                try {
                    TBS.Adv.ctrlClicked("Page_Video", CT.Button, RTCSTREAM_MAIDIAN_DOUDI, (statsString + ",error_code=" + errorCode) + "," + playUrl);
                } catch (Throwable th) {
                }
            }
        }
    }

    public void artpEndtoEndDelayMsg(String msg) {
        this.end2endDelay = msg;
    }

    /* access modifiers changed from: protected */
    public void monitorError(int errorCode, int extra) {
        if (this.mConfig != null && !TextUtils.isEmpty(this.AppMonitor_Module) && !this.mCommitPlayError) {
            this.bPaused = true;
            if (this.mPlayUrl != null && this.mPlayUrl.contains(TaoLiveVideoView.TBLIVE_ARTP_SCHEMA)) {
                if (errorCode <= -10000 && errorCode >= -10611) {
                    artpDegradeMonitor(errorCode, this.mPlayUrl);
                }
                if (-10605 == errorCode || -10604 == errorCode || -10611 == errorCode) {
                    return;
                }
            }
            if (this instanceof IjkMediaPlayer) {
                IjkMediaPlayer player = (IjkMediaPlayer) this;
                this.mServerIP = player._getPropertyString(21003);
                this.mLocalIP = player._getPropertyString(21004);
                this.mVia = player._getPropertyString(IjkMediaPlayer.FFP_PROP_SEND_VIA);
            } else if (this instanceof TaobaoMediaPlayer) {
                this.mServerIP = ((TaobaoMediaPlayer) this)._getPropertyString(21003);
            }
            monitorPlayerEvent(7);
            try {
                this.mCommitPlayError = true;
                this.mLastErrorCode = errorCode;
                this.mLastExtra = extra;
                HashMap<String, String> dimensionValues_buffer = getBaseDimensionValues();
                dimensionValues_buffer.put("error_code", String.valueOf(errorCode));
                dimensionValues_buffer.put(SampleConfigConstant.ACCURATE, String.valueOf(extra));
                if (!(this.mContext == null || !(this.mContext instanceof Activity) || ((Activity) this.mContext).getIntent() == null || ((Activity) this.mContext).getIntent().getData() == null || TextUtils.isEmpty(((Activity) this.mContext).getIntent().getData().toString()))) {
                    dimensionValues_buffer.put("page_url", ((Activity) this.mContext).getIntent().getData().toString());
                }
                HashMap<String, Double> measureValues_buffer = new HashMap<>();
                measureValues_buffer.put("timestamp", Double.valueOf((double) System.currentTimeMillis()));
                measureValues_buffer.put("is_tbnet", Double.valueOf(this.mConfig.mbEnableTBNet ? 1.0d : ClientTraceData.b.f47a));
                measureValues_buffer.put("hardware_hevc", Double.valueOf((double) this.mConfig.mDecoderTypeH265));
                measureValues_buffer.put("hardware_avc", Double.valueOf((double) this.mConfig.mDecoderTypeH264));
                measureValues_buffer.put("is_usecache", Double.valueOf(this.bUseVideoCache ? 1.0d : ClientTraceData.b.f47a));
                if (this.mContext != null) {
                    this.mLastIsConnected = TBAVNetworkUtils.isConnected(this.mNetworkUtilsAdapter, this.mContext) ? 1 : 0;
                    measureValues_buffer.put("is_connected", Double.valueOf((double) this.mLastIsConnected));
                }
                if (this instanceof IjkMediaPlayer) {
                    measureValues_buffer.put("video_interval_bit_rate", Double.valueOf((double) ((IjkMediaPlayer) this)._getPropertyLong(IjkMediaPlayer.FFP_PROP_INTERVAL_DOWNLOAD_BITRATE, 0)));
                }
                measureValues_buffer.put("cur_position", Double.valueOf((double) (getCurrentPosition() / 1000)));
                AppMonitor.Stat.commit(this.AppMonitor_Module, "playerError", DimensionValueSet.fromStringMap(dimensionValues_buffer), MeasureValueSet.create((Map<String, Double>) measureValues_buffer));
            } catch (Throwable th) {
            }
            synchronized (this.mLock) {
                if (this.mHandler != null) {
                    this.mHandler.removeCallbacksAndMessages((Object) null);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void monitorStart() {
        this.mStartTime = System.currentTimeMillis();
        monitorPlayerEvent(2);
        this.bPaused = false;
    }

    private void heartBeatMonitorStart() {
        synchronized (this.mLock) {
            try {
                if (Build.VERSION.SDK_INT <= 28 && Build.VERSION.SDK_INT >= 21 && this.mConfig != null && !TextUtils.isEmpty(this.mConfig.mDeviceLevel) && this.mConfig.mDeviceLevel.contains(MediaConstant.DEVICE_LEVEL_HIGHT) && !TextUtils.isEmpty(this.mPlayUrl) && !this.mPlayUrl.contains(TaoLiveVideoView.TBLIVE_ARTP_SCHEMA) && "LiveRoom".equals(this.mConfig.mSubBusinessType) && MediaAdapteManager.mConfigAdapter != null && MediaAdapteManager.mABTestAdapter != null && this.mNetCounter.getAndIncrement() == 0 && AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter.getConfig(this.mConfig.mConfigGroup, "startNetSamplingEnable", "false")) && MediaConstant.ABTEST_NETSPEED.equals(MediaAdapteManager.mABTestAdapter.getBucket(MediaConstant.ABTEST_NETSPEED_COMOPONENT, MediaConstant.ABTEST_NETSPEED_MODULE))) {
                    DeviceBandwidthSampler.getInstance().startSampling();
                }
            } catch (Throwable th) {
            }
            if (this.mHandler != null) {
                if (this.mUTRun == null) {
                    this.mUTRun = new Runnable() {
                        public void run() {
                            MonitorMediaPlayer.this.commitPlaying(MonitorMediaPlayer.REPORT_DURATION);
                            if (MonitorMediaPlayer.this.mHandler != null) {
                                MonitorMediaPlayer.this.mHandler.postDelayed(MonitorMediaPlayer.this.mUTRun, (long) (MonitorMediaPlayer.REPORT_DURATION * 1000));
                            }
                        }
                    };
                    this.mLastCommitPlaying = System.currentTimeMillis();
                    if ((REPORT_DURATION * 1000) - this.mDurTimeInHeartBeatLCycle <= 0 || this.mDurTimeInHeartBeatLCycle <= 0) {
                        this.mHandler.postDelayed(this.mUTRun, (long) (REPORT_DURATION * 1000));
                    } else {
                        this.mHandler.postDelayed(this.mUTRun, (long) ((REPORT_DURATION * 1000) - this.mDurTimeInHeartBeatLCycle));
                    }
                }
                if (this.mPlayerRun == null) {
                    this.mPlayerRun = new Runnable() {
                        public void run() {
                            MonitorMediaPlayer.this.getPlayerMsg();
                            if (MonitorMediaPlayer.this.mHandler != null) {
                                MonitorMediaPlayer.this.mHandler.postDelayed(MonitorMediaPlayer.this.mPlayerRun, (long) (MonitorMediaPlayer.PLAYER_REPORT_DURATION * 1000));
                            }
                        }
                    };
                    this.mHandler.postDelayed(this.mPlayerRun, 1000);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void getPlayerMsg() {
        if (this instanceof IjkMediaPlayer) {
            this.mHeartBeatFPS.append(((IjkMediaPlayer) this)._getPropertyLong(20114, 0) + Constant.INTENT_JSON_MARK);
            this.mHeartBeatDecodeFPS.append(((IjkMediaPlayer) this)._getPropertyLong(20113, 0) + Constant.INTENT_JSON_MARK);
        } else if (this instanceof TaobaoMediaPlayer) {
            this.mHeartBeatFPS.append(((TaobaoMediaPlayer) this)._getPropertyLong(20114, 0) + Constant.INTENT_JSON_MARK);
            this.mHeartBeatDecodeFPS.append(((TaobaoMediaPlayer) this)._getPropertyLong(20113, 0) + Constant.INTENT_JSON_MARK);
        }
        this.mHeartBeatNetSpeed.append(((int) ConnectionClassManager.getInstance().getDownloadKBitsPerSecond()) + Constant.INTENT_JSON_MARK);
    }

    /* access modifiers changed from: protected */
    public void monitorPause() {
        this.bPaused = true;
        monitorPlayerEvent(3);
        synchronized (this.mLock) {
            if (this.mHandler != null) {
                this.mHandler.removeCallbacksAndMessages((Object) null);
                this.mDurTimeInHeartBeatLCycle = (int) (System.currentTimeMillis() - this.mLastCommitPlaying);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void monitorComplete() {
        this.bPaused = true;
        monitorPlayerEvent(6);
        this.mLoopCount++;
    }

    /* access modifiers changed from: protected */
    public void monitorSeek() {
        this.bSeeked = true;
        monitorPlayerEvent(5);
    }

    /* access modifiers changed from: protected */
    public void monitorReset() {
    }

    /* access modifiers changed from: protected */
    public void monitorRelease() {
        String statsString;
        if (this.bUseVideoCache && !this.bIsCompleteHitCache) {
            PlayerEnvironment.getProxy(this.mContext).shutDownServerClient(this.mPlayUrl);
        }
        if (!(this.mTelephonyManager == null || this.mPhoneStateListener == null)) {
            this.mTelephonyManager.listen(this.mPhoneStateListener, 0);
            this.mPhoneStateListener = null;
        }
        String playerType = "taobaoplayer";
        if (!(this.mPlayUrl == null || !this.mPlayUrl.contains(TaoLiveVideoView.TBLIVE_ARTP_SCHEMA) || this.mConfig == null || this.mConfig.mPlayerType == 2 || (!(this instanceof IjkMediaPlayer) && !(this instanceof TaobaoMediaPlayer)))) {
            String statsString2 = ("SeqNO=9998" + ",feed_id=" + this.mConfigClone.mFeedId) + ",anchor_account_id=" + this.mConfigClone.mAccountId;
            if (this instanceof TaobaoMediaPlayer) {
                statsString = statsString2 + "," + ((TaobaoMediaPlayer) this)._getPropertyString(IjkMediaPlayer.RTCSTREAM_TRANSPORT_STREAM_INFO);
            } else {
                playerType = "ijkplayer";
                statsString = ((statsString2 + "," + ((IjkMediaPlayer) this)._getPropertyString(IjkMediaPlayer.RTCSTREAM_TRANSPORT_STREAM_INFO)) + "," + ((IjkMediaPlayer) this)._getPropertyString(IjkMediaPlayer.RTCSTREAM_TRANSPORT_INFO_SNAPSHOT)) + "," + ((IjkMediaPlayer) this)._getPropertyString(IjkMediaPlayer.RTCSTREAM_TRANSPORT_START_INFO);
            }
            String urlParams = "";
            if (this.mPlayUrl.contains("&")) {
                urlParams = this.mPlayUrl.substring(this.mPlayUrl.indexOf("&"));
            }
            try {
                TBS.Adv.ctrlClicked("Page_Video", CT.Button, RTCSTREAM_MAIDIAN_INFO, ((statsString + ",media_url=" + urlParams) + ",abnormal_count=" + this.mBufferingCount) + ",player_type=" + playerType);
            } catch (Throwable th) {
            }
        }
        synchronized (this.mLock) {
            this.mBeatCount = 0;
            if (this.mHandler != null) {
                this.bPaused = false;
                commitPlaying((this.mDurTimeInHeartBeatLCycle / 1000) + ((int) ((System.currentTimeMillis() - this.mLastCommitPlaying) / 1000)));
                this.bPaused = true;
                this.mHandler.removeCallbacksAndMessages((Object) null);
                this.mHandler = null;
                this.mUTRun = null;
                this.mPlayerRun = null;
                this.mHeartBeatCount = 0;
            }
        }
    }

    /* Debug info: failed to restart local var, previous not found, register: 11 */
    /* access modifiers changed from: protected */
    public void monitorRenderStart(long renderTime) {
        long j;
        this.bFirstFrameRendered = true;
        if (renderTime <= 0) {
            renderTime = System.currentTimeMillis();
        }
        this.mFirstEndtime = renderTime;
        this.mFirstRenderTime = (this.mStartTime <= 0 || this.mFirstEndtime - this.mStartTime < 0) ? this.mFirstEndtime - this.mPrepareStartTime : this.mPreparedTime + (this.mFirstEndtime - this.mStartTime);
        if (this.mStartTime <= 0 || this.mFirstEndtime - this.mStartTime < 0) {
            j = this.mFirstEndtime - ((this.mFirstRenderAdapter == null || this.mFirstRenderAdapter.getStartTime() <= 0) ? this.mPrepareStartTime : this.mFirstRenderAdapter.getStartTime());
        } else {
            j = this.mUserPreparedTime + (this.mFirstEndtime - this.mStartTime);
        }
        this.mUserFirstRenderTime = j;
        if (this instanceof IjkMediaPlayer) {
            switch ((int) ((IjkMediaPlayer) this)._getPropertyLong(IjkMediaPlayer.FFP_PROP_VIDEO_CODEC_INFO, 0)) {
                case 13:
                    this.mEncodeType = "MP4";
                    break;
                case 20:
                    this.mEncodeType = "H263";
                    break;
                case 28:
                    this.mEncodeType = "H264";
                    break;
                case Opcodes.MUL_FLOAT:
                    this.mEncodeType = "VP9";
                    break;
                case Opcodes.DIV_DOUBLE:
                    this.mEncodeType = "HEVC";
                    break;
            }
        }
        monitorPlayerEvent(8);
        if (!TextUtils.isEmpty(this.mPlayUrl) && !this.mPlayUrl.contains(TaoLiveVideoView.TBLIVE_ARTP_SCHEMA) && "LiveRoom".equals(this.mConfig.mSubBusinessType) && MediaAdapteManager.mABTestAdapter != null && MediaConstant.ABTEST_USE_HEARTBEAT.equals(MediaAdapteManager.mABTestAdapter.getBucket(MediaConstant.ABTEST_HEARTBEAT_COMOPONENT, MediaConstant.ABTEST_HEARTBEAT_MODULE))) {
            heartBeatMonitorStart();
        }
        if (!TextUtils.isEmpty(this.mPlayUrl) && this.mPlayUrl.startsWith("http") && this.mConfig != null && this.mConfig.mScenarioType == 0 && "LiveRoom".equals(this.mConfig.mSubBusinessType) && MediaAdapteManager.mABTestAdapter != null && (this instanceof TaobaoMediaPlayer) && this.mPlayUrl.contains(".flv") && !this.mPlayUrl.contains(".m3u8") && !TextUtils.isEmpty(this.mLowQualityUrl) && this.mLowQualityUrl.contains("liveng-270p") && useABR() && useABROrange()) {
            ((TaobaoMediaPlayer) this).startABR();
        }
    }

    /* Debug info: failed to restart local var, previous not found, register: 9 */
    /* access modifiers changed from: protected */
    public void monitorBufferStart(long time) {
        if (this.bFirstFrameRendered) {
            if (time <= 0) {
                time = System.currentTimeMillis();
            }
            this.mBufferingStart = time;
        }
        monitorPlayerEvent(4);
        if (this.mPlayUrl != null && this.mPlayUrl.contains(TaoLiveVideoView.TBLIVE_ARTP_SCHEMA) && this.mConfig != null && this.mConfig.mPlayerType != 2) {
            if ((this instanceof IjkMediaPlayer) || (this instanceof TaobaoMediaPlayer)) {
                String statsString = (("SeqNO=9999" + ",feed_id=" + this.mConfigClone.mFeedId) + ",anchor_account_id=" + this.mConfigClone.mAccountId) + "," + this.mPlayUrl;
                long playTime = 0;
                if (this.mLastPlayTime > 0) {
                    playTime = System.currentTimeMillis() - this.mLastPlayTime;
                }
                if (this instanceof TaobaoMediaPlayer) {
                    statsString = (statsString + "," + ((TaobaoMediaPlayer) this)._getPropertyString(IjkMediaPlayer.RTCSTREAM_TRANSPORT_STREAM_INFO)) + ",play_time=" + playTime;
                }
                if (this instanceof IjkMediaPlayer) {
                    statsString = (((statsString + "," + ((IjkMediaPlayer) this)._getPropertyString(IjkMediaPlayer.RTCSTREAM_STATSTYPE_RECV_JITTER)) + "," + ((IjkMediaPlayer) this)._getPropertyString(IjkMediaPlayer.RTCSTREAM_STATSTYPE_RTT)) + "," + ((IjkMediaPlayer) this)._getPropertyString(IjkMediaPlayer.RTCSTREAM_STATSTYPE_RECV_LOSS_RATE)) + "," + ((IjkMediaPlayer) this)._getPropertyString(IjkMediaPlayer.RTCSTREAM_TRANSPORT_STREAM_INFO);
                }
                try {
                    TBS.Adv.ctrlClicked("Page_Video", CT.Button, RTCSTREAM_MAIDIAN_DETAIL, statsString);
                } catch (Throwable th) {
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void monitorBufferEnd(long time) {
        if (this.mConfig != null && this.bFirstFrameRendered && this.mBufferingStart > 0) {
            if (this.bSeeked) {
                this.bSeeked = false;
                return;
            }
            long buffering_end = time > 0 ? time : System.currentTimeMillis();
            long duration = buffering_end - this.mBufferingStart;
            if (duration >= 0 && duration <= 10000) {
                this.mLastBuffering = buffering_end;
                this.mBufferingCount++;
                this.mBufferingTotalTime += duration;
                this.mBufferingHeartBeatCount++;
                this.mBufferingHeartBeatMsg.append(System.currentTimeMillis() + SymbolExpUtil.SYMBOL_COLON + duration + Constant.INTENT_JSON_MARK);
                this.mBufferingHeartBeatTotalTime += duration;
            }
        }
    }

    /* access modifiers changed from: protected */
    public long getRecData() {
        if (this instanceof IjkMediaPlayer) {
            return ((IjkMediaPlayer) this)._getPropertyLong(IjkMediaPlayer.FFP_PROP_NETWORK_TRAFFIC, 0);
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public long getConsumedData() {
        if (this instanceof IjkMediaPlayer) {
            return ((IjkMediaPlayer) this)._getPropertyLong(IjkMediaPlayer.FFP_PROP_CONSUMED_TRAFFIC, 0);
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void monitorMediacodecError() {
        this.bMediacodeError = true;
    }

    /* access modifiers changed from: protected */
    public void monitorPlayExperience() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        if (this.mConfig != null && this.bNeedCommitPlayExperience) {
            this.bNeedCommitPlayExperience = false;
            try {
                if (this.mLastPlayTime > 0) {
                    this.mTotalPlayTime += System.currentTimeMillis() - this.mLastPlayTime;
                    this.mLastPlayTime = 0;
                }
                String playerType = null;
                long fps = -1;
                long decode_fps = -1;
                long download_fps = -1;
                long bitrate = -1;
                String videoDecoder = "unknown";
                String audioDecoder = "unknown";
                int sourcer = -1;
                if (this instanceof IjkMediaPlayer) {
                    playerType = "ijkplayer";
                    fps = ((IjkMediaPlayer) this)._getPropertyLong(20114, 0);
                    decode_fps = ((IjkMediaPlayer) this)._getPropertyLong(20113, 0);
                    download_fps = ((IjkMediaPlayer) this)._getPropertyLong(20112, 0);
                    bitrate = ((IjkMediaPlayer) this)._getPropertyLong(20115, 0);
                    String _getPropertyString = ((IjkMediaPlayer) this)._getPropertyString(21003);
                    this.mServerIP = _getPropertyString;
                    this.mCdnIp = _getPropertyString;
                    audioDecoder = "FFmpeg";
                    videoDecoder = isHardwareDecode() ? "MediaCodec" : "FFmpeg";
                    if (this.mPlayUrl != null && this.mPlayUrl.contains(TaoLiveVideoView.TBLIVE_ARTP_SCHEMA)) {
                        this.mBufferingTotalTime += 1000 * this.mBufferingCount;
                    }
                } else if (this instanceof NativeMediaPlayer) {
                    playerType = "mediaplayer";
                    videoDecoder = "MediaCodec";
                    audioDecoder = "MediaCodec";
                } else if (this instanceof TaobaoMediaPlayer) {
                    playerType = "taobaoplayer";
                    fps = ((TaobaoMediaPlayer) this)._getPropertyLong(20114, 0);
                    decode_fps = ((TaobaoMediaPlayer) this)._getPropertyLong(20113, 0);
                    download_fps = ((TaobaoMediaPlayer) this)._getPropertyLong(20112, 0);
                    bitrate = ((TaobaoMediaPlayer) this)._getPropertyLong(20115, 0);
                    if (!this.bUsePcdn || this.mConfig == null || TextUtils.isEmpty(this.mConfig.mPlayToken)) {
                        this.mServerIP = ((TaobaoMediaPlayer) this)._getPropertyString(21003);
                    } else {
                        try {
                            this.mServerIP = PcdnManager.PCDNGet(PcdnType.LIVE, this.mConfig.mPlayToken, "");
                            String[] result = this.mServerIP.split(SymbolExpUtil.SYMBOL_SEMICOLON);
                            if (result != null && result.length >= 3) {
                                this.mServerIP = result[2];
                            }
                        } catch (Exception e) {
                        }
                    }
                    videoDecoder = isHardwareDecode() ? "MediaCodec" : "FFmpeg";
                    long adType = ((TaobaoMediaPlayer) this)._getPropertyLong(TaobaoMediaPlayer.FFP_PROP_INT64_AUDIO_DECODER_TYPE, 0);
                    if (adType == 2) {
                        audioDecoder = "MediaCodec";
                    } else if (adType == 1) {
                        audioDecoder = "FFmpeg";
                    }
                    if (!TextUtils.isEmpty(this.mPlayUrl) && this.mPlayUrl.contains(TaoLiveVideoView.TBLIVE_ARTP_SCHEMA)) {
                        this.mBufferingTotalTime += 1000 * this.mBufferingCount;
                    }
                    sourcer = (int) ((TaobaoMediaPlayer) this)._getPropertyLong(TaobaoMediaPlayer.FFP_PROP_INT64_SOURCER_TYPE, -1);
                }
                if (TextUtils.isEmpty(this.mPageUrl) && this.mContext != null && (this.mContext instanceof Activity) && ((Activity) this.mContext).getIntent() != null && ((Activity) this.mContext).getIntent().getData() != null && !TextUtils.isEmpty(((Activity) this.mContext).getIntent().getData().toString())) {
                    this.mPageUrl = ((Activity) this.mContext).getIntent().getData().toString();
                }
                CT ct = CT.Button;
                String[] strArr = new String[64];
                strArr[0] = "player_type=" + playerType;
                strArr[1] = "play_scenario=" + this.mConfigClone.mScenarioType;
                strArr[2] = "server_ip=" + this.mServerIP;
                strArr[3] = "route_nodes=" + this.mVia;
                strArr[4] = "media_url=" + this.mPlayUrl;
                strArr[5] = "feed_id=" + this.mConfigClone.mFeedId;
                strArr[6] = "anchor_account_id=" + this.mConfigClone.mAccountId;
                strArr[7] = "video_definition=" + this.mConfigClone.mVideoDefinition;
                strArr[8] = "business_type=" + this.mConfigClone.mBusinessId;
                strArr[9] = "sub_business_type=" + this.mConfigClone.mSubBusinessType;
                strArr[10] = "video_channel=" + this.mConfigClone.mVideoChannel;
                strArr[11] = "video_width=" + getVideoWidth();
                strArr[12] = "video_height=" + getVideoHeight();
                strArr[13] = "player_status_nodes=" + getPlayerEvent();
                strArr[14] = "video_duration=" + this.mVideoDuration;
                strArr[15] = "screen_size=" + new DecimalFormat(Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE).format(AndroidUtils.getScreenSize(this.mContext));
                strArr[16] = "encode_type=" + this.mEncodeType;
                strArr[17] = "abnormal_count=" + this.mBufferingCount;
                strArr[18] = "abnormal_total_time=" + this.mBufferingTotalTime;
                strArr[19] = "first_frame_rendering_time=" + this.mFirstRenderTime;
                strArr[20] = "user_first_frame_time=" + this.mUserFirstRenderTime;
                strArr[21] = "play_time=" + this.mTotalPlayTime;
                strArr[22] = "video_avg_fps=" + fps;
                strArr[23] = "video_avg_decode_fps=" + decode_fps;
                strArr[24] = "video_avg_download_fps=" + download_fps;
                strArr[25] = "video_avg_bit_rate=" + bitrate;
                strArr[26] = "loop_count=" + this.mLoopCount;
                strArr[27] = "hardware_hevc=" + this.mConfigClone.mDecoderTypeH265;
                strArr[28] = "hardware_avc=" + this.mConfigClone.mDecoderTypeH264;
                StringBuilder append = new StringBuilder().append("complete_hit_cache=");
                if (this.bIsCompleteHitCache) {
                    i = 1;
                } else {
                    i = 0;
                }
                strArr[29] = append.append(i).toString();
                StringBuilder append2 = new StringBuilder().append("hit_cache=");
                if (this.bIsHitCache) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                strArr[30] = append2.append(i2).toString();
                StringBuilder append3 = new StringBuilder().append("is_tbnet=");
                if (this.mConfigClone.mbEnableTBNet) {
                    i3 = 1;
                } else {
                    i3 = 0;
                }
                strArr[31] = append3.append(i3).toString();
                StringBuilder append4 = new StringBuilder().append("is_usecache=");
                if (this.bUseVideoCache) {
                    i4 = 1;
                } else {
                    i4 = 0;
                }
                strArr[32] = append4.append(i4).toString();
                strArr[33] = "play_token=" + this.mConfigClone.mPlayToken;
                strArr[34] = "error_code=" + this.mLastErrorCode;
                strArr[35] = "extra=" + this.mLastExtra;
                strArr[36] = "is_connected=" + this.mLastIsConnected;
                strArr[37] = "cpu_name=" + AndroidUtils.getCPUName();
                strArr[38] = "page_url=" + this.mPageUrl;
                strArr[39] = "media_source_type=" + this.mConfigClone.mMediaSourceType;
                strArr[40] = "spm=" + this.mConfigClone.mSpmUrl;
                strArr[41] = "device_level=" + this.mConfigClone.mDeviceLevel;
                strArr[42] = "net_speed=" + this.mConfigClone.mNetSpeed;
                strArr[43] = "recv_bytes=" + getRecData();
                strArr[44] = "highPerformance=" + (this.mConfigClone.mHighPerformance ? 1 : 0);
                strArr[45] = "consumed_bytes=" + getConsumedData();
                StringBuilder append5 = new StringBuilder().append("use_device_measure=");
                if (this.mConfigClone.mbEnableDeviceMeasure) {
                    i5 = 1;
                } else {
                    i5 = 0;
                }
                strArr[46] = append5.append(i5).toString();
                strArr[47] = "use_pcdn=" + (this.bUsePcdn ? 1 : 0);
                strArr[48] = "bufferMsg=" + this.mConfigClone.mVideoPlayBufferMsg;
                strArr[49] = "expectedVideoInfo=" + this.mConfigClone.mExpectedVideoInfo;
                strArr[50] = "cdn_ip=" + this.mCdnIp;
                strArr[51] = "new_bundle_sdk=" + (this.mConfigClone.mNewBundleSdk ? 1 : 0);
                strArr[52] = "selected_url_name=" + this.mConfigClone.mSelectedUrlName;
                strArr[53] = "rate_adapte=" + (this.mConfigClone.mRateAdapte ? 1 : 0);
                strArr[54] = "drop_frame=" + (this.mConfigClone.mDropFrameForH265 ? 1 : 0);
                strArr[55] = "first_render_timestamp=" + this.mFirstEndtime;
                StringBuilder append6 = new StringBuilder().append("use_hcachefile=");
                if (!TextUtils.isEmpty(this.mConfig.mHighCachePath)) {
                    i6 = 1;
                } else {
                    i6 = 0;
                }
                strArr[56] = append6.append(i6).toString();
                strArr[57] = "video_decoder=" + videoDecoder;
                strArr[58] = "audio_decoder=" + audioDecoder;
                StringBuilder append7 = new StringBuilder().append("use_abr=");
                if (this.mUseABR) {
                    i7 = 1;
                } else {
                    i7 = 0;
                }
                strArr[59] = append7.append(i7).toString();
                strArr[60] = "abr_msg=" + this.mABRMSG;
                strArr[61] = "net_type=" + this.mNetType;
                StringBuilder append8 = new StringBuilder().append("mediacodec_error=");
                if (this.bMediacodeError) {
                    i8 = 1;
                } else {
                    i8 = 0;
                }
                strArr[62] = append8.append(i8).toString();
                strArr[63] = "sourcer=" + sourcer;
                TBS.Adv.ctrlClicked("Page_Video", ct, "PlayExperience", strArr);
            } catch (Throwable th) {
            }
        }
    }

    private void registerMonitor() {
        try {
            if (!TextUtils.isEmpty(this.AppMonitor_Module)) {
                DimensionSet dimensionSet = DimensionSet.create();
                dimensionSet.addDimension("player_type", "");
                dimensionSet.addDimension("media_url", "");
                dimensionSet.addDimension("server_ip", "");
                dimensionSet.addDimension("local_ip", "");
                dimensionSet.addDimension("feed_id", "");
                dimensionSet.addDimension("anchor_account_id", "");
                dimensionSet.addDimension("user_id", "");
                dimensionSet.addDimension("play_scenario", "");
                dimensionSet.addDimension("error_code", "");
                dimensionSet.addDimension("video_width", "");
                dimensionSet.addDimension("video_height", "");
                dimensionSet.addDimension("encode_type", "");
                dimensionSet.addDimension("screen_size", "");
                dimensionSet.addDimension("video_definition", "");
                dimensionSet.addDimension("route_nodes", "");
                dimensionSet.addDimension("business_type", "");
                dimensionSet.addDimension("sub_business_type", "");
                dimensionSet.addDimension("player_status_nodes", "");
                dimensionSet.addDimension("video_duration", "");
                dimensionSet.addDimension(SampleConfigConstant.ACCURATE, "");
                dimensionSet.addDimension("page_url", "");
                MeasureSet bufferingNew = MeasureSet.create();
                Measure measure = new Measure("buffering_start_time");
                measure.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(Double.MAX_VALUE));
                Measure end2New = new Measure("buffering_end_time");
                end2New.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(Double.MAX_VALUE));
                Measure duration2New = new Measure("buffering_duration");
                duration2New.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(10000.0d));
                Measure intervalNew = new Measure("buffering_interval");
                intervalNew.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(Double.MAX_VALUE));
                Measure videoDecodeFpsNew = new Measure("video_decode_fps");
                videoDecodeFpsNew.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(Double.MAX_VALUE));
                Measure measure2 = new Measure("video_cache");
                measure2.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(Double.MAX_VALUE));
                Measure audioCacheNew = new Measure("audio_cache");
                audioCacheNew.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(Double.MAX_VALUE));
                bufferingNew.addMeasure(measure);
                bufferingNew.addMeasure(end2New);
                bufferingNew.addMeasure(duration2New);
                bufferingNew.addMeasure(intervalNew);
                bufferingNew.addMeasure(videoDecodeFpsNew);
                bufferingNew.addMeasure(measure2);
                bufferingNew.addMeasure(audioCacheNew);
                AppMonitor.register(this.AppMonitor_Module, MornitorBufferingNew, bufferingNew, dimensionSet);
                MeasureSet playerError = MeasureSet.create();
                Measure measure3 = new Measure("time_stamp");
                measure3.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(Double.MAX_VALUE));
                Measure isConnected = new Measure("is_connected");
                isConnected.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(Double.MAX_VALUE));
                Measure isTBNet = new Measure("is_tbnet");
                isTBNet.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(Double.MAX_VALUE));
                Measure hardware_hevc = new Measure("hardware_hevc");
                hardware_hevc.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(Double.MAX_VALUE));
                Measure hardware_avc = new Measure("hardware_avc");
                Measure measure4 = new Measure("is_usecache");
                measure4.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(Double.MAX_VALUE));
                hardware_avc.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(Double.MAX_VALUE));
                Measure intervalBitrate = new Measure("video_interval_bit_rate");
                intervalBitrate.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(Double.MAX_VALUE));
                Measure curPosition = new Measure("cur_position");
                curPosition.setRange(Double.valueOf(ClientTraceData.b.f47a), Double.valueOf(Double.MAX_VALUE));
                playerError.addMeasure(measure3);
                playerError.addMeasure(isConnected);
                playerError.addMeasure(intervalBitrate);
                playerError.addMeasure(curPosition);
                playerError.addMeasure(isTBNet);
                playerError.addMeasure(hardware_hevc);
                playerError.addMeasure(hardware_avc);
                playerError.addMeasure(measure4);
                AppMonitor.register(this.AppMonitor_Module, "playerError", playerError, dimensionSet);
            }
        } catch (Throwable th) {
        }
    }

    private HashMap<String, String> getBaseDimensionValues() {
        HashMap<String, String> dimensionValues_buffer = new HashMap<>();
        if (this instanceof IjkMediaPlayer) {
            dimensionValues_buffer.put("player_type", "ijkplayer");
        } else if (this instanceof NativeMediaPlayer) {
            dimensionValues_buffer.put("player_type", "mediaplayer");
        } else if (this instanceof TaobaoMediaPlayer) {
            dimensionValues_buffer.put("player_type", "taobaoplayer");
        }
        dimensionValues_buffer.put("play_scenario", String.valueOf(this.mConfig.mScenarioType));
        if (!TextUtils.isEmpty(this.mServerIP)) {
            dimensionValues_buffer.put("server_ip", this.mServerIP);
        }
        if (!TextUtils.isEmpty(this.mLocalIP)) {
            dimensionValues_buffer.put("local_ip", this.mLocalIP);
        }
        if (!TextUtils.isEmpty(this.mVia)) {
            dimensionValues_buffer.put("route_nodes", this.mVia);
        }
        if (!TextUtils.isEmpty(this.mEncodeType)) {
            dimensionValues_buffer.put("encode_type", this.mEncodeType);
        }
        if (!TextUtils.isEmpty(this.mPlayUrl)) {
            dimensionValues_buffer.put("media_url", this.mPlayUrl);
        }
        if (!TextUtils.isEmpty(this.mConfig.mFeedId)) {
            dimensionValues_buffer.put("feed_id", this.mConfig.mFeedId);
        }
        if (!TextUtils.isEmpty(this.mConfig.mAccountId)) {
            dimensionValues_buffer.put("anchor_account_id", this.mConfig.mAccountId);
        }
        if (!TextUtils.isEmpty(this.mConfig.mUserId)) {
            dimensionValues_buffer.put("user_id", this.mConfig.mUserId);
        }
        if (!TextUtils.isEmpty(this.mConfig.mVideoDefinition)) {
            dimensionValues_buffer.put("video_definition", this.mConfig.mVideoDefinition);
        }
        if (!TextUtils.isEmpty(this.mConfig.mBusinessId)) {
            dimensionValues_buffer.put("business_type", this.mConfig.mBusinessId);
        }
        if (!TextUtils.isEmpty(this.mConfig.mSubBusinessType)) {
            dimensionValues_buffer.put("sub_business_type", this.mConfig.mSubBusinessType);
        }
        dimensionValues_buffer.put("video_width", String.valueOf(getVideoWidth()));
        dimensionValues_buffer.put("video_height", String.valueOf(getVideoHeight()));
        dimensionValues_buffer.put("player_status_nodes", getPlayerEvent());
        dimensionValues_buffer.put("video_duration", String.valueOf(this.mVideoDuration));
        return dimensionValues_buffer;
    }

    private void monitorPlayerEvent(int event) {
        if (event == 2) {
            try {
                this.mLastPlayTime = System.currentTimeMillis();
            } catch (Exception e) {
                return;
            }
        } else if (this.mLastPlayTime > 0 && (event == 6 || event == 7 || event == 3)) {
            this.mTotalPlayTime += System.currentTimeMillis() - this.mLastPlayTime;
            this.mLastPlayTime = 0;
        }
        this.mPlayerEventList.offer(Integer.valueOf(event));
        if (this.mPlayerEventList.size() > 30) {
            this.mPlayerEventList.poll();
        }
    }

    private String getPlayerEvent() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.mPlayerEventList.size(); i++) {
            if (i != 0) {
                stringBuilder.append("_");
            }
            stringBuilder.append(this.mPlayerEventList.get(i));
        }
        return stringBuilder.toString();
    }

    /* access modifiers changed from: protected */
    public String getCdnIp() {
        try {
            if (this.mPlayUrl == null || !this.mPlayUrl.startsWith("http:") || this.mContext == null) {
                if (this.mPlayUrl != null && this.mPlayUrl.startsWith("artp:") && this.mConfigAdapter != null && AndroidUtils.parseBoolean(this.mConfigAdapter.getConfig(TaoLiveVideoView.TBLIVE_ORANGE_GROUP, "ARTPHTTPDNSEnabled", "false"))) {
                    return HttpDnsAdapter.getIpByHttpDns(Uri.parse(this.mPlayUrl).getHost());
                }
                return null;
            }
            this.mNetType = TBAVNetworkUtils.getNetworkType(this.mNetworkUtilsAdapter, this.mContext);
            if (!"WIFI".equals(this.mNetType)) {
                String host = this.mConfigAdapter != null ? this.mConfigAdapter.getConfig(MediaConstant.DW_ORANGE_GROUP_NAME, KEY_NO_TRAFFIC_HOST, "") : "";
                if (!TextUtils.isEmpty(host)) {
                    return HttpDnsAdapter.getIpByHttpDns(host);
                }
            }
            if (this.mConfig != null && this.mConfig.mScenarioType != 2 && this.mConfigAdapter != null && AndroidUtils.parseBoolean(this.mConfigAdapter.getConfig(MediaConstant.DW_ORANGE_GROUP_NAME, VIDEO_CDNIP_ENABLE, "false"))) {
                return HttpDnsAdapter.getIpByHttpDns(Uri.parse(this.mPlayUrl).getHost());
            }
            if (this.mConfig != null && this.mConfig.mScenarioType == 2 && this.mConfigAdapter != null && AndroidUtils.parseBoolean(this.mConfigAdapter.getConfig(MediaConstant.DW_ORANGE_GROUP_NAME, VIDEO_CLIP_CDNIP_ENABLE, "true"))) {
                return HttpDnsAdapter.getIpByHttpDns(Uri.parse(this.mPlayUrl).getHost());
            }
            return null;
        } catch (Throwable th) {
        }
    }

    public TaoLiveVideoViewConfig getCloneConfig() {
        return this.mConfigClone;
    }

    public String getPlayUrl() {
        return this.mPlayUrl;
    }

    public String getCdnIpForDebug() {
        return this.mCdnIp;
    }

    public int getLastErrorCode() {
        return this.mLastErrorCode;
    }

    public String getEncodeType() {
        return this.mEncodeType;
    }

    public String getABRMSG() {
        return this.mABRMSG.toString();
    }

    public boolean isABR() {
        return this.mUseABR;
    }

    public LinkedList<Integer> getPlayerEventListForDebug() {
        return this.mPlayerEventList;
    }
}
