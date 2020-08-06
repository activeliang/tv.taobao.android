package com.taobao.mediaplay;

import android.content.Context;
import com.taobao.adapter.ITLogAdapter;
import com.taobao.media.MediaConstant;
import com.taobao.mediaplay.common.ITBVideoSourceAdapter;
import com.taobao.mediaplay.common.IYKVideoSourceAdapter;
import com.taobao.mediaplay.model.MediaLiveInfo;
import java.io.Serializable;

public class MediaPlayControlContext implements Serializable {
    public String mAccountId;
    private int mAvdataBufferedMaxMBytes;
    private int mAvdataBufferedMaxTime;
    public boolean mBackgroundMode = true;
    private String mBackupCacheKey;
    private String mBackupVideoDefinition;
    private int mBackupVideoLength;
    private String mBackupVideoUrl;
    public String mBusinessId;
    private String mCacheKey;
    public String mConfigGroup;
    public Context mContext;
    private int mCurrentBitRate;
    private int mCurrentLevel;
    private String mDevicePerformanceLevel;
    public boolean mDropFrameForH265;
    private boolean mEdgePcdn = false;
    public String mFrom;
    private boolean mH265 = false;
    public boolean mH265Enable = true;
    private boolean mHardwareAvc;
    private boolean mHardwareHevc;
    public String mHighCachePath;
    public boolean mHighPerformancePlayer = false;
    public String mHighVideoDefinition;
    public boolean mLocalVideo;
    private boolean mLowDeviceFirstRender = true;
    public boolean mLowDeviceSVC;
    private boolean mLowPerformance;
    public String mLowQualityUrl;
    private int mMaxLevel;
    public MediaLiveInfo mMediaLiveInfo;
    public String mMediaSourceType;
    private int mNetSpeed;
    private int mPlayerType = 1;
    private boolean mRateAdapte = false;
    private String mRateAdaptePriority;
    public int mRuntimeLevel;
    public boolean mSVCEnable;
    public int mSeekWhenPrepared;
    public String mSelectedUrlName;
    public boolean mTBLive;
    public ITBVideoSourceAdapter mTBVideoSourceAdapter;
    public ITLogAdapter mTLogAdapter;
    private boolean mTopAnchor = false;
    private boolean mTransH265 = true;
    public boolean mUseArtp = true;
    private boolean mUseTBNet;
    private String mVideoDefinition;
    private boolean mVideoDeviceMeaseureEnable;
    public String mVideoId;
    private int mVideoLength;
    public String mVideoSource;
    private String mVideoToken;
    private String mVideoUrl;
    public IYKVideoSourceAdapter mYKVideoSourceAdapter;

    public void setHighCachePath(String highCachePath) {
        this.mHighCachePath = highCachePath;
    }

    public String getHighCachePath() {
        return this.mHighCachePath;
    }

    public MediaPlayControlContext(Context context) {
        this.mContext = context;
    }

    public boolean isUseTBNet() {
        return this.mUseTBNet;
    }

    public void setUseTBNet(boolean useTBNet) {
        this.mUseTBNet = useTBNet;
    }

    public String getVideoSource() {
        return this.mVideoSource;
    }

    public int getPlayerType() {
        return this.mPlayerType;
    }

    public void setPlayerType(int mPlayerType2) {
        this.mPlayerType = mPlayerType2;
    }

    public String getVideoDefinition() {
        return this.mVideoDefinition;
    }

    public void setVideoDefinition(String videoDefinition) {
        this.mVideoDefinition = videoDefinition;
    }

    public void setCacheKey(String cacheKey) {
        this.mCacheKey = cacheKey;
    }

    public String getCacheKey() {
        return this.mCacheKey;
    }

    public void setBackupCacheKey(String cacheKey) {
        this.mBackupCacheKey = cacheKey;
    }

    public String getBackupCacheKey() {
        return this.mBackupCacheKey;
    }

    public boolean isHardwareHevc() {
        return this.mHardwareHevc;
    }

    public void setHardwareHevc(boolean hardwareHevc) {
        this.mHardwareHevc = hardwareHevc;
    }

    public String getVideoUrl() {
        return this.mVideoUrl;
    }

    public void setVideoUrl(String mVideoUrl2) {
        this.mVideoUrl = mVideoUrl2;
    }

    public boolean isVideoDeviceMeaseureEnable() {
        return this.mVideoDeviceMeaseureEnable;
    }

    public void setVdeoDeviceMeaseureEnable(boolean videoDeviceMeaseureEnable) {
        this.mVideoDeviceMeaseureEnable = videoDeviceMeaseureEnable;
    }

    public boolean isH265() {
        return this.mH265;
    }

    public void setH265(boolean mH2652) {
        this.mH265 = mH2652;
    }

    public void setCurrentBitRate(int currentBitRate) {
        this.mCurrentBitRate = currentBitRate;
    }

    public int getCurrentBitRate() {
        return this.mCurrentBitRate;
    }

    public void setMaxLevel(int maxLevel) {
        this.mMaxLevel = maxLevel;
    }

    public int getMaxLevel() {
        return this.mMaxLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.mCurrentLevel = currentLevel;
    }

    public int getCurrentLevel() {
        return this.mCurrentLevel;
    }

    public void setVideoLength(int videoLength) {
        this.mVideoLength = videoLength;
    }

    public int getVideoLength() {
        return this.mVideoLength;
    }

    public void setDevicePerformanceLevel(String devicePerformanceLevel) {
        this.mDevicePerformanceLevel = devicePerformanceLevel;
    }

    public String getDevicePerformanceLevel() {
        return this.mDevicePerformanceLevel;
    }

    public void setNetSpeed(int netSpeed) {
        this.mNetSpeed = netSpeed;
    }

    public int getNetSpeed() {
        return this.mNetSpeed;
    }

    public int getAvdataBufferedMaxMBytes() {
        return this.mAvdataBufferedMaxMBytes;
    }

    public boolean isHardwareAvc() {
        return this.mHardwareAvc;
    }

    public void setHardwareAvc(boolean hardwareAvc) {
        this.mHardwareAvc = hardwareAvc;
    }

    public void setAvdataBufferedMaxMBytes(int avdataBufferedMaxMBytes) {
        this.mAvdataBufferedMaxMBytes = avdataBufferedMaxMBytes;
    }

    public int getAvdataBufferedMaxTime() {
        return this.mAvdataBufferedMaxTime;
    }

    public void setAvdataBufferedMaxTime(int avdataBufferedMaxTime) {
        this.mAvdataBufferedMaxTime = avdataBufferedMaxTime;
    }

    public String getRateAdaptePriority() {
        return this.mRateAdaptePriority;
    }

    public void setRateAdaptePriority(String rateAdaptePriority) {
        this.mRateAdaptePriority = rateAdaptePriority;
    }

    public void setBackupVideoDetail(String backupVideoUrl, String videoDefinition) {
        this.mBackupVideoUrl = backupVideoUrl;
        this.mBackupVideoDefinition = videoDefinition;
    }

    public String getBackupVideoDefinition() {
        return this.mBackupVideoDefinition;
    }

    public void setBackupVideoLength(int backupVideoLength) {
        this.mBackupVideoLength = backupVideoLength;
    }

    public int getBackupVideoLength() {
        return this.mBackupVideoLength;
    }

    public String getVideoToken() {
        return this.mVideoToken;
    }

    public void setVideoToken(String videoToken) {
        this.mVideoToken = videoToken;
    }

    public String getBackupVideoUrl() {
        return this.mBackupVideoUrl;
    }

    public void setUseArtp(boolean useArtp) {
        this.mUseArtp = useArtp;
    }

    public void setTransH265(boolean transH265) {
        this.mTransH265 = transH265;
    }

    public boolean useArtp() {
        return this.mUseArtp;
    }

    public void setBusinessId(String businessId) {
        this.mBusinessId = businessId;
        this.mTBLive = MediaConstant.LBLIVE_SOURCE.equals(this.mBusinessId);
    }

    public boolean useTransH265() {
        return this.mTransH265;
    }

    public void setTopAnchor(boolean topAnchor) {
        this.mTopAnchor = topAnchor;
    }

    public void setEdgePcdn(boolean edgePcdn) {
        this.mEdgePcdn = edgePcdn;
    }

    public boolean getTopAnchor() {
        return this.mTopAnchor;
    }

    public boolean getEdgePcdn() {
        return this.mEdgePcdn;
    }

    public boolean getLowDeviceFirstRender() {
        return this.mLowDeviceFirstRender;
    }

    public void setLowDeviceFirstRender(boolean open) {
        this.mLowDeviceFirstRender = open;
    }

    public void setRateAdapte(boolean rateAdapte) {
        this.mRateAdapte = rateAdapte;
    }

    public boolean getRateAdapte() {
        return this.mRateAdapte;
    }

    public void setLowPerformance(boolean isLowPerformance) {
        this.mLowPerformance = isLowPerformance;
    }

    public boolean isLowPerformance() {
        return this.mLowPerformance;
    }
}
