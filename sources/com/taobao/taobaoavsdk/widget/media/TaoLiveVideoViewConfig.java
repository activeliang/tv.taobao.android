package com.taobao.taobaoavsdk.widget.media;

public class TaoLiveVideoViewConfig {
    public static final int DECODER_TYPE_HARDWARE = 1;
    public static final int DECODER_TYPE_SOFTWARE = 0;
    public static final int PLAYER_TYPE_FF = 1;
    public static final int PLAYER_TYPE_MEDIA = 2;
    public static final int PLAYER_TYPE_TAOBAO = 3;
    public static final int RENDER_TYPE_SURFACE_VIEW = 1;
    public static final int RENDER_TYPE_TEXTURE_VIEW = 2;
    public static final int RENDER_TYPE_VR_VIEW = 3;
    public static final int SCALE_TYPE_CENTER_CROP = 1;
    public static final int SCALE_TYPE_FIT_CENTER = 0;
    public static final int SCALE_TYPE_FIT_XY = 3;
    public static final int SCENARIO_TYPE_LINKLIVE = 1;
    public static final int SCENARIO_TYPE_LIVE = 0;
    public static final int SCENARIO_TYPE_PLAYBACK = 2;
    public static final int VR_RENDER_TYPE_180_LEFT_RIGHT = 3;
    public static final int VR_RENDER_TYPE_180_UP_DOWN = 2;
    public static final int VR_RENDER_TYPE_360_EQUIRECTANGULAR = 1;
    public String mAccountId;
    public String mBusinessId;
    public String mCacheKey;
    public String mConfigGroup;
    public int mCoverResId;
    @Deprecated
    public int mDecoderType = 0;
    public int mDecoderTypeAudio = 0;
    public int mDecoderTypeH264 = 0;
    public int mDecoderTypeH265 = 0;
    public String mDeviceLevel;
    public boolean mDropFrameForH265;
    public boolean mEdgePcdn;
    public String mExpectedVideoInfo;
    public String mFeedId;
    public String mHighCachePath;
    public boolean mHighPerformance;
    public String mLowQualityUrl;
    public String mMediaSourceType;
    public int mNetSpeed = -1;
    public boolean mNewBundleSdk;
    public String mPlayToken;
    public int mPlayerType = 1;
    public boolean mRateAdapte;
    public int mRenderType = 2;
    public boolean mSVCEnable;
    public int mScaleType = 1;
    public int mScenarioType = 0;
    public String mSelectedUrlName;
    public String mSpmUrl;
    public String mSubBusinessType;
    public String mToken;
    public boolean mTopAnchor;
    public String mUserId;
    public int mVRLat = -1;
    public int mVRLng = -1;
    public int mVRRenderType = 1;
    public String mVideoChannel;
    public String mVideoDefinition;
    public int mVideoLength;
    public String mVideoPlayBufferMsg;
    public String mVideoSource;
    public boolean mbEnableDeviceMeasure;
    public boolean mbEnableRecycle = true;
    public boolean mbEnableTBNet;
    public boolean mbShowNoWifiToast = true;

    private TaoLiveVideoViewConfig() {
    }

    public TaoLiveVideoViewConfig(String businessId) {
        this.mBusinessId = businessId;
    }

    public TaoLiveVideoViewConfig(String businessId, String userId) {
        this.mBusinessId = businessId;
        this.mUserId = userId;
    }

    public TaoLiveVideoViewConfig clone() {
        TaoLiveVideoViewConfig config = new TaoLiveVideoViewConfig(this.mBusinessId);
        config.mAccountId = this.mAccountId;
        config.mbEnableDeviceMeasure = this.mbEnableDeviceMeasure;
        config.mbEnableRecycle = this.mbEnableRecycle;
        config.mbEnableTBNet = this.mbEnableTBNet;
        config.mbShowNoWifiToast = this.mbShowNoWifiToast;
        config.mCacheKey = this.mCacheKey;
        config.mConfigGroup = this.mConfigGroup;
        config.mCoverResId = this.mCoverResId;
        config.mDecoderTypeH264 = this.mDecoderTypeH264;
        config.mDecoderTypeH265 = this.mDecoderTypeH265;
        config.mDeviceLevel = this.mDeviceLevel;
        config.mExpectedVideoInfo = this.mExpectedVideoInfo;
        config.mFeedId = this.mFeedId;
        config.mHighPerformance = this.mHighPerformance;
        config.mMediaSourceType = this.mMediaSourceType;
        config.mNetSpeed = this.mNetSpeed;
        config.mPlayerType = this.mPlayerType;
        config.mPlayToken = this.mPlayToken;
        config.mRenderType = this.mRenderType;
        config.mScaleType = this.mScaleType;
        config.mScenarioType = this.mScenarioType;
        config.mSpmUrl = this.mSpmUrl;
        config.mSubBusinessType = this.mSubBusinessType;
        config.mToken = this.mToken;
        config.mUserId = this.mUserId;
        config.mVideoChannel = this.mVideoChannel;
        config.mVideoDefinition = this.mVideoDefinition;
        config.mVideoLength = this.mVideoLength;
        config.mVideoPlayBufferMsg = this.mVideoPlayBufferMsg;
        config.mVideoSource = this.mVideoSource;
        config.mVRLat = this.mVRLat;
        config.mVRLng = this.mVRLng;
        config.mVRRenderType = this.mVRRenderType;
        config.mHighCachePath = this.mHighCachePath;
        config.mNewBundleSdk = this.mNewBundleSdk;
        config.mDropFrameForH265 = this.mDropFrameForH265;
        config.mRateAdapte = this.mRateAdapte;
        config.mSelectedUrlName = this.mSelectedUrlName;
        config.mSVCEnable = this.mSVCEnable;
        config.mTopAnchor = this.mTopAnchor;
        config.mEdgePcdn = this.mEdgePcdn;
        config.mLowQualityUrl = this.mLowQualityUrl;
        return config;
    }
}
