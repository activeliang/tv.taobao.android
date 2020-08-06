package com.taobao.mediaplay;

import android.os.Build;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import com.taobao.media.MediaAdapteManager;
import com.taobao.media.MediaConstant;
import com.taobao.media.MediaDeviceUtils;
import com.taobao.mediaplay.common.IDWVideourlCallBack;
import com.taobao.mediaplay.common.IVideoNetworkListener;
import com.taobao.mediaplay.model.CacheKeyDefinition;
import com.taobao.mediaplay.model.DWVideoDefinition;
import com.taobao.mediaplay.model.DWVideoInfoData;
import com.taobao.mediaplay.model.MediaLiveInfo;
import com.taobao.mediaplay.model.MediaVideoResponse;
import com.taobao.mediaplay.model.QualityLiveItem;
import com.taobao.taobaoavsdk.cache.PlayerEnvironment;
import com.taobao.taobaoavsdk.util.AndroidUtils;
import com.taobao.taobaoavsdk.util.DWLogUtils;
import com.taobao.taobaoavsdk.util.TBAVNetworkUtils;
import java.util.ArrayList;
import java.util.Map;

public class MediaPlayControlManager {
    private final String[] m4G3GPriority = {MediaConstant.DEFINITION_SD, MediaConstant.DEFINITION_LD, MediaConstant.DEFINITION_HD, MediaConstant.DEFINITION_UD};
    private final String m4G3GPriority2 = "sd#ld#hd#ud";
    private final String[] mDefaultPriority = {MediaConstant.DEFINITION_LD, MediaConstant.DEFINITION_SD, MediaConstant.DEFINITION_HD, MediaConstant.DEFINITION_UD};
    private final String mDefaultPriority2 = "ld#sd#hd#ud";
    private final String[][] mH2654G3GPriority = {new String[]{MediaConstant.DEFINITION_SD_H265, MediaConstant.DEFINITION_SD}, new String[]{MediaConstant.DEFINITION_LD_H265, MediaConstant.DEFINITION_LD}};
    private final String[][] mH265DefaultPriority = {new String[]{MediaConstant.DEFINITION_LD_H265, MediaConstant.DEFINITION_LD}};
    private final String[][] mH265WifiPriority = {new String[]{MediaConstant.DEFINITION_HD_H265, MediaConstant.DEFINITION_HD}, new String[]{MediaConstant.DEFINITION_SD_H265, MediaConstant.DEFINITION_SD}, new String[]{MediaConstant.DEFINITION_LD_H265, MediaConstant.DEFINITION_LD}};
    private final String[] mLiveDefaultPriority = {MediaConstant.DEFINITION_SD, MediaConstant.DEFINITION_MD};
    private final String[] mLiveLowQualityPriority = {MediaConstant.DEFINITION_LLD};
    private final String[] mLiveWifiPriority = {MediaConstant.DEFINITION_MD};
    /* access modifiers changed from: private */
    public MediaPlayControlContext mMediaPlayContext;
    /* access modifiers changed from: private */
    public volatile boolean mPicking;
    private final String[] mWifiPriority = {MediaConstant.DEFINITION_HD, MediaConstant.DEFINITION_SD, MediaConstant.DEFINITION_LD, MediaConstant.DEFINITION_UD};
    private final String mWifiPriority2 = "hd#sd#ld#ud";

    public MediaPlayControlManager(MediaPlayControlContext context) {
        this.mMediaPlayContext = context;
    }

    public void pickVideoUrl(IMediaUrlPickCallBack videoUrlPickCallBack) {
        if (!this.mPicking) {
            if (this.mMediaPlayContext != null) {
                DWLogUtils.d(this.mMediaPlayContext.mTLogAdapter, "pickVideoUrl##VideoSource:" + this.mMediaPlayContext.getVideoSource());
            }
            if (videoUrlPickCallBack == null) {
                DWLogUtils.d(this.mMediaPlayContext.mTLogAdapter, "pickVideoUrl##videoUrlPickCallBack = null");
                return;
            }
            this.mMediaPlayContext.setTopAnchor(false);
            this.mMediaPlayContext.setEdgePcdn(false);
            if ((TextUtils.isEmpty(this.mMediaPlayContext.mVideoId) || (!MediaConstant.YOUKU_SOURCE.equals(this.mMediaPlayContext.getVideoSource()) && !MediaConstant.TBVIDEO_SOURCE.equals(this.mMediaPlayContext.getVideoSource()))) && !this.mMediaPlayContext.mTBLive) {
                videoUrlPickCallBack.onPick(false, "");
            } else if (!MediaConstant.TBVIDEO_SOURCE.equals(this.mMediaPlayContext.getVideoSource()) && !this.mMediaPlayContext.mTBLive) {
                pickYKVideoUrl(videoUrlPickCallBack);
            } else if (!MediaConstant.TBVIDEO_SOURCE.equals(this.mMediaPlayContext.getVideoSource()) || !(this.mMediaPlayContext == null || MediaAdapteManager.mConfigAdapter == null || AndroidUtils.isInList(this.mMediaPlayContext.mFrom, MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.PLAY_MANAGER_BIZCODES_BLACK_LIST, "[\"TRAVEL_HOME\"]")))) {
                pickTBMediaUrl(videoUrlPickCallBack);
            } else {
                videoUrlPickCallBack.onPick(false, "");
                if (this.mMediaPlayContext != null) {
                    DWLogUtils.d(this.mMediaPlayContext.mTLogAdapter, "pickTBVideoUrl##PlayManager is not available");
                }
            }
        }
    }

    private void pickTBLiveUrl(IMediaUrlPickCallBack mediaUrlPickCallBack, boolean useH265, int rateAdapterLevel) {
        mediaUrlPickCallBack.onPick(setLiveUrl(useH265, rateAdapterLevel), "");
        this.mPicking = false;
    }

    private boolean setLiveUrl(boolean useH265, int rateAdapterLevel) {
        if (this.mMediaPlayContext.mMediaLiveInfo == null || this.mMediaPlayContext.mMediaLiveInfo.liveUrlList == null || this.mMediaPlayContext.mMediaLiveInfo.liveUrlList.size() <= 0) {
            return false;
        }
        setLiveRateAdapteUrl(this.mMediaPlayContext.mMediaLiveInfo, useH265, rateAdapterLevel);
        String netType = TBAVNetworkUtils.getNetworkType(MediaAdapteManager.mMediaNetworkUtilsAdapter, this.mMediaPlayContext.mContext);
        if (!"LiveRoom".equals(this.mMediaPlayContext.mFrom) || TextUtils.isEmpty(this.mMediaPlayContext.getVideoUrl()) || this.mMediaPlayContext.getVideoUrl().contains(".m3u8") || TextUtils.isEmpty(netType)) {
            return true;
        }
        if ((!MediaConstant.DEVICE_LEVEL_LOW.equals(this.mMediaPlayContext.getDevicePerformanceLevel()) && !netType.equals("3G") && !netType.equals("2G")) || MediaAdapteManager.mConfigAdapter == null || !AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, "lowDeviceSVCEnable", "false")) || !this.mMediaPlayContext.getVideoUrl().contains("liveng.alicdn.com")) {
            return true;
        }
        this.mMediaPlayContext.mSVCEnable = true;
        return true;
    }

    private void pickTBMediaUrl(IMediaUrlPickCallBack videoUrlPickCallBack) {
        boolean custom = true;
        this.mPicking = true;
        int netSpeed = Integer.MAX_VALUE;
        boolean isLowPerformance = Boolean.FALSE.booleanValue();
        String netType = TBAVNetworkUtils.getNetworkType(MediaAdapteManager.mMediaNetworkUtilsAdapter, this.mMediaPlayContext.mContext);
        if (MediaAdapteManager.mMeasureAdapter != null) {
            netSpeed = MediaAdapteManager.mMeasureAdapter.getNetSpeedValue();
            if (!(this.mMediaPlayContext == null || MediaAdapteManager.mConfigAdapter == null || !AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, "videoDeviceScoreEnable", "false")))) {
                isLowPerformance = MediaAdapteManager.mMeasureAdapter.isLowPerformance(this.mMediaPlayContext);
            }
            this.mMediaPlayContext.setLowPerformance(isLowPerformance);
            this.mMediaPlayContext.setDevicePerformanceLevel(isLowPerformance ? MediaConstant.DEVICE_LEVEL_LOW : MediaConstant.DEVICE_LEVEL_HIGHT);
            this.mMediaPlayContext.setNetSpeed(netSpeed);
        }
        boolean useH265 = setH265Hardware();
        setH264Hardware();
        this.mMediaPlayContext.setH265(useH265);
        if (!this.mMediaPlayContext.mTBLive || this.mMediaPlayContext.mMediaLiveInfo != null) {
            int rateAdapterLevel = getRateAdapterType(netType, netSpeed, isLowPerformance);
            if (!this.mMediaPlayContext.mTBLive || this.mMediaPlayContext.mMediaLiveInfo == null) {
                if (!this.mMediaPlayContext.mHighPerformancePlayer || !this.mMediaPlayContext.mBackgroundMode) {
                    custom = false;
                }
                queryAndPickVideoUrl(videoUrlPickCallBack, this.mMediaPlayContext.isH265(), rateAdapterLevel, custom);
                return;
            }
            pickTBLiveUrl(videoUrlPickCallBack, this.mMediaPlayContext.isH265(), rateAdapterLevel);
            return;
        }
        videoUrlPickCallBack.onPick(false, "");
        this.mPicking = false;
    }

    private boolean setH265Hardware() {
        boolean useH265;
        boolean useH2652;
        boolean z;
        boolean z2;
        if (this.mMediaPlayContext.getPlayerType() == 2 || this.mMediaPlayContext.mConfigGroup == null || !AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, "h265Enable", "true"))) {
            useH265 = false;
        } else {
            useH265 = true;
        }
        if (useH2652) {
            if (Build.VERSION.SDK_INT >= 21) {
                String white = MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_HARDWARE_HEVC_WHITE, "");
                MediaPlayControlContext mediaPlayControlContext = this.mMediaPlayContext;
                if (((Build.VERSION.SDK_INT < 23 || !this.mMediaPlayContext.mTBLive) && (Build.VERSION.SDK_INT < 21 || this.mMediaPlayContext.mTBLive)) || !AndroidUtils.isInList(AndroidUtils.getCPUName(), white)) {
                    z = false;
                } else {
                    z = true;
                }
                mediaPlayControlContext.setHardwareHevc(z);
                if (this.mMediaPlayContext.isHardwareHevc()) {
                    String black = MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_HARDWARE_HEVC_BLACK, "[\"m1 note\",\"PRO 7 Plus\",\"PRO 7-H\",\"OPPO A73\",\"OPPO R9tm\",\"OPPO R9sk\"]");
                    String bizcodeBlack = MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_HARDWARE_HEVC_BIZCODE_BLACK, "[\"WEITAO\"]");
                    if (AndroidUtils.isInList(Build.MODEL, black) || TextUtils.isEmpty(this.mMediaPlayContext.mFrom) || (Build.VERSION.SDK_INT < 23 && AndroidUtils.isInList(this.mMediaPlayContext.mFrom, bizcodeBlack))) {
                        this.mMediaPlayContext.setHardwareHevc(false);
                    }
                }
                if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23 && this.mMediaPlayContext.mTBLive) {
                    String white_live = MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_HARDWARE_HEVC_WHITE_FOR_L, "");
                    MediaPlayControlContext mediaPlayControlContext2 = this.mMediaPlayContext;
                    if (!AndroidUtils.isInList(AndroidUtils.getCPUName(), white_live) || MediaAdapteManager.mABTestAdapter == null || !MediaConstant.ABTEST_USE_HARDWAR.equals(MediaAdapteManager.mABTestAdapter.getBucket(MediaConstant.ABTEST_LIVE_LOWDEVICE_HARDWARE_COMOPONENT, MediaConstant.ABTEST_LIVE_LOWDEVICE_HARDWARE_MODULE))) {
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                    mediaPlayControlContext2.setHardwareHevc(z2);
                    if (this.mMediaPlayContext.isHardwareHevc()) {
                        String black_live = MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_HARDWARE_HEVC_BLACK_FOR_L, "[\"m1 note\",\"PRO 7 Plus\",\"PRO 7-H\",\"OPPO A73\",\"OPPO A59\",\"vivo X9\",\"OPPO R9tm\",\"OPPO R9sk\"]");
                        String bizcodeBlackLive = MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_HARDWARE_HEVC_BIZCODE_BLACK_FOR_L, "");
                        if (AndroidUtils.isInList(Build.MODEL, black_live) || TextUtils.isEmpty(this.mMediaPlayContext.mFrom) || (Build.VERSION.SDK_INT < 23 && AndroidUtils.isInList(this.mMediaPlayContext.mFrom, bizcodeBlackLive))) {
                            this.mMediaPlayContext.setHardwareHevc(false);
                        }
                    }
                }
                if (!this.mMediaPlayContext.isHardwareHevc()) {
                    useH2652 = MediaDeviceUtils.isSupportH265(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, "h265MaxFreq", "1.8"));
                }
            }
            if (!useH2652 && this.mMediaPlayContext.mTBLive) {
                if (MediaAdapteManager.mConfigAdapter == null || !AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, "lowDeviceH265Enable", "true"))) {
                    useH2652 = false;
                } else {
                    useH2652 = true;
                }
                String black2 = MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_LIVE_HARDWARE_HEVC_BLACK, "");
                if (AndroidUtils.isInList(Build.MODEL, black2) || AndroidUtils.isInList(AndroidUtils.getCPUName(), black2)) {
                    useH2652 = false;
                }
                if (useH2652 && MediaConstant.DEVICE_LEVEL_LOW.equals(this.mMediaPlayContext.getDevicePerformanceLevel())) {
                    this.mMediaPlayContext.mDropFrameForH265 = true;
                }
            }
        }
        return useH2652;
    }

    private void setH264Hardware() {
        if ((Build.VERSION.SDK_INT >= 23 && this.mMediaPlayContext.mTBLive) || (Build.VERSION.SDK_INT >= 21 && !this.mMediaPlayContext.mTBLive)) {
            if (AndroidUtils.isInList(AndroidUtils.getCPUName(), MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_HARDWARE_H264_WHITE, ""))) {
                String black = MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_HARDWARE_H264_BLACK, "");
                String bizcodeBlack = MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_HARDWARE_H264_BIZCODE_BLACK, "[\"WEITAO\"]");
                if (!AndroidUtils.isInList(Build.MODEL, black) && !TextUtils.isEmpty(this.mMediaPlayContext.mFrom) && (Build.VERSION.SDK_INT >= 23 || !AndroidUtils.isInList(this.mMediaPlayContext.mFrom, bizcodeBlack))) {
                    this.mMediaPlayContext.setHardwareAvc(Boolean.TRUE.booleanValue());
                }
            }
        }
        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23 && this.mMediaPlayContext.mTBLive && MediaAdapteManager.mABTestAdapter != null && MediaConstant.ABTEST_USE_HARDWAR.equals(MediaAdapteManager.mABTestAdapter.getBucket(MediaConstant.ABTEST_LIVE_LOWDEVICE_HARDWARE_COMOPONENT, MediaConstant.ABTEST_LIVE_LOWDEVICE_HARDWARE_MODULE))) {
            if (AndroidUtils.isInList(AndroidUtils.getCPUName(), MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_HARDWARE_H264_WHITE_FOR_L, ""))) {
                String black2 = MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_HARDWARE_H264_BLACK_FOR_L, "");
                String bizcodeBlack2 = MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.ORANGE_HARDWARE_H264_BIZCODE_BLACK_FOR_L, "");
                if (!AndroidUtils.isInList(Build.MODEL, black2) && !TextUtils.isEmpty(this.mMediaPlayContext.mFrom)) {
                    if (Build.VERSION.SDK_INT >= 23 || !AndroidUtils.isInList(this.mMediaPlayContext.mFrom, bizcodeBlack2)) {
                        this.mMediaPlayContext.setHardwareAvc(Boolean.TRUE.booleanValue());
                    }
                }
            }
        }
    }

    private void queryAndPickVideoUrl(final IMediaUrlPickCallBack videoUrlPickCallBack, final boolean useH265, final int rateAdapterLevel, boolean custom) {
        this.mMediaPlayContext.setRateAdaptePriority(getRateAdapterPriority(rateAdapterLevel, custom));
        if (!this.mMediaPlayContext.mLocalVideo && TextUtils.isEmpty(this.mMediaPlayContext.getVideoToken())) {
            this.mMediaPlayContext.setVideoUrl("");
            this.mMediaPlayContext.setVideoDefinition("");
        }
        this.mMediaPlayContext.mTBVideoSourceAdapter.queryVideoConfigData(new IVideoNetworkListener() {
            public void onSuccess(MediaVideoResponse response) {
                if (response == null || response.data == null) {
                    if (MediaPlayControlManager.this.mMediaPlayContext != null) {
                        DWLogUtils.e(MediaPlayControlManager.this.mMediaPlayContext.mTLogAdapter, "pickTBVideoUrl.onSuccess##Response no data");
                    }
                } else if (!TextUtils.isEmpty(MediaPlayControlManager.this.mMediaPlayContext.getVideoToken())) {
                    boolean unused = MediaPlayControlManager.this.mPicking = false;
                    videoUrlPickCallBack.onPick(true, "");
                    return;
                } else {
                    DWVideoInfoData videoInfoData = new DWVideoInfoData(response.data);
                    MediaPlayControlManager.this.mMediaPlayContext.setUseTBNet(MediaPlayControlManager.this.useTBNet());
                    MediaPlayControlManager.this.setVideoUrl(MediaPlayControlManager.this.mMediaPlayContext, videoInfoData.getVideoDefinitionMap(), videoInfoData.getCacheDefinitionMap(), useH265, rateAdapterLevel);
                    MediaPlayControlManager.this.setBufferController(videoInfoData, (float) MediaPlayControlManager.this.mMediaPlayContext.getCurrentBitRate());
                }
                if (MediaPlayControlManager.this.mMediaPlayContext != null) {
                    DWLogUtils.d(MediaPlayControlManager.this.mMediaPlayContext.mTLogAdapter, "pickTBVideoUrl.onSuccess##UseH265:" + useH265 + " videoUrl:" + MediaPlayControlManager.this.mMediaPlayContext.getVideoUrl() + " BackupVideoUrl:" + MediaPlayControlManager.this.mMediaPlayContext.getBackupVideoUrl() + " playerType:" + MediaPlayControlManager.this.mMediaPlayContext.getPlayerType());
                }
                boolean unused2 = MediaPlayControlManager.this.mPicking = false;
                videoUrlPickCallBack.onPick(true, "");
            }

            public void onError(MediaVideoResponse response) {
                if (MediaPlayControlManager.this.mMediaPlayContext != null) {
                    DWLogUtils.e(MediaPlayControlManager.this.mMediaPlayContext.mTLogAdapter, new StringBuilder().append("pickTBVideoUrl.onError##Response msg:").append(response).toString() == null ? null : response.errorMsg + "code:" + response.errorCode);
                }
                boolean unused = MediaPlayControlManager.this.mPicking = false;
                videoUrlPickCallBack.onPick(true, response.errorCode);
            }
        });
    }

    private void pickYKVideoUrl(final IMediaUrlPickCallBack videoUrlPickCallBack) {
        try {
            this.mPicking = true;
            this.mMediaPlayContext.mYKVideoSourceAdapter.getVideoUrlInfo(this.mMediaPlayContext.mContext, this.mMediaPlayContext.mVideoId, new IDWVideourlCallBack() {
                public void getVideoUrl(String url) {
                    MediaPlayControlManager.this.mMediaPlayContext.setVideoUrl(MediaPlayControlManager.this.addVideoUrlScheme(url));
                    boolean unused = MediaPlayControlManager.this.mPicking = false;
                    videoUrlPickCallBack.onPick(true, "");
                    if (MediaPlayControlManager.this.mMediaPlayContext != null) {
                        DWLogUtils.d("", "pickYKVideoUrl.onSuccess##VideoURL:" + MediaPlayControlManager.this.mMediaPlayContext.getVideoUrl());
                    }
                }
            });
        } catch (Exception e) {
            this.mPicking = false;
            videoUrlPickCallBack.onPick(true, "");
            DWLogUtils.e(this.mMediaPlayContext.mTLogAdapter, " pickYKVideoUrl##Get youku video url error" + DWLogUtils.getStackTrace(e));
        }
    }

    private void setHighCachePath(MediaPlayControlContext context, Map<String, CacheKeyDefinition> cacheDefinitionMap, String definition, String videoDefinition) {
        if (!TextUtils.isEmpty(definition) && cacheDefinitionMap != null && cacheDefinitionMap.get(definition) != null) {
            String cacheKey = cacheDefinitionMap.get(definition).getCacheKey();
            if (!TextUtils.isEmpty(cacheKey)) {
                String highCachePath = PlayerEnvironment.getCachePathForCacheKey(context.mContext, cacheKey);
                if (!TextUtils.isEmpty(highCachePath)) {
                    context.setHighCachePath(highCachePath);
                    context.mHighVideoDefinition = videoDefinition;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void setBufferController(DWVideoInfoData videoInfoData, float currentBitRate) {
        if (videoInfoData != null && this.mMediaPlayContext != null) {
            int maxBufferedSize = videoInfoData.getBufferedMaxMBytes();
            if (MediaAdapteManager.mConfigAdapter != null && AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, MediaConstant.BUFFER_CONTROLLER_ENABLE, "false")) && ((float) this.mMediaPlayContext.getNetSpeed()) >= 3.0f * currentBitRate && currentBitRate > 10.0f && videoInfoData.getCurrentLevel() > 0 && videoInfoData.getMaxLevel() > videoInfoData.getCurrentLevel()) {
                maxBufferedSize = ((int) (((float) maxBufferedSize) / ((float) videoInfoData.getMaxLevel()))) * videoInfoData.getCurrentLevel();
            }
            this.mMediaPlayContext.setMaxLevel(videoInfoData.getMaxLevel());
            this.mMediaPlayContext.setCurrentLevel(videoInfoData.getCurrentLevel());
            this.mMediaPlayContext.setAvdataBufferedMaxMBytes(maxBufferedSize);
            this.mMediaPlayContext.setAvdataBufferedMaxTime(videoInfoData.getAvdataBufferedMaxTime());
        }
    }

    /* access modifiers changed from: private */
    public String addVideoUrlScheme(String url) {
        if (TextUtils.isEmpty(url) || !url.startsWith(WVUtils.URL_SEPARATOR)) {
            return url;
        }
        return "http:" + url;
    }

    /* access modifiers changed from: private */
    public void setVideoUrl(MediaPlayControlContext dwContext, Map<String, DWVideoDefinition> videoDefinitionMap, Map<String, CacheKeyDefinition> cacheDefinitionMap, boolean useH265, int rateAdapterLevel) {
        if (videoDefinitionMap == null || videoDefinitionMap.size() == 0 || dwContext == null) {
            if (dwContext != null) {
                DWLogUtils.e(this.mMediaPlayContext.mTLogAdapter, "pickTBVideoUrl.setVideoUrl##VideoDefinitionMap empty");
            }
        } else if (!this.mMediaPlayContext.mHighPerformancePlayer || !this.mMediaPlayContext.mBackgroundMode || !setCustomUrl(dwContext, videoDefinitionMap)) {
            if (useH265) {
                setH265RateAdapteUrl(dwContext, videoDefinitionMap, rateAdapterLevel);
                getHighCacheKey(dwContext, cacheDefinitionMap);
                if (dwContext.isH265()) {
                    setH264RateAdapteUrl(dwContext, videoDefinitionMap, true, rateAdapterLevel);
                    return;
                }
            }
            setH264RateAdapteUrl(dwContext, videoDefinitionMap, false, rateAdapterLevel);
            getHighCacheKey(dwContext, cacheDefinitionMap);
        }
    }

    private void getHighCacheKey(MediaPlayControlContext dwContext, Map<String, CacheKeyDefinition> cacheDefinitionMap) {
        if (!TextUtils.isEmpty(this.mMediaPlayContext.getVideoDefinition())) {
            if (dwContext.isH265()) {
                if (AndroidUtils.getVideoDefinition(MediaConstant.H265, MediaConstant.DEFINITION_SD).equals(this.mMediaPlayContext.getVideoDefinition())) {
                    setHighCachePath(dwContext, cacheDefinitionMap, MediaConstant.DEFINITION_HD_H265, AndroidUtils.getVideoDefinition(MediaConstant.H265, MediaConstant.DEFINITION_HD));
                }
            } else if (AndroidUtils.getVideoDefinition(MediaConstant.H264, MediaConstant.DEFINITION_SD).equals(this.mMediaPlayContext.getVideoDefinition())) {
                setHighCachePath(dwContext, cacheDefinitionMap, MediaConstant.DEFINITION_HD, AndroidUtils.getVideoDefinition(MediaConstant.H264, MediaConstant.DEFINITION_HD));
            } else if (AndroidUtils.getVideoDefinition(MediaConstant.H264, MediaConstant.DEFINITION_LD).equals(this.mMediaPlayContext.getVideoDefinition())) {
                setHighCachePath(dwContext, cacheDefinitionMap, MediaConstant.DEFINITION_HD, AndroidUtils.getVideoDefinition(MediaConstant.H264, MediaConstant.DEFINITION_HD));
                if (TextUtils.isEmpty(dwContext.getHighCachePath())) {
                    setHighCachePath(dwContext, cacheDefinitionMap, MediaConstant.DEFINITION_SD, AndroidUtils.getVideoDefinition(MediaConstant.H264, MediaConstant.DEFINITION_SD));
                }
            }
        }
    }

    private void setH264RateAdapteUrl(MediaPlayControlContext dwContext, Map<String, DWVideoDefinition> videoDefinitionMap, boolean backup, int definitionType) {
        String[] priority;
        String definition = null;
        String videoUrl = null;
        String cacheKey = null;
        int videoLength = -1;
        int bitRate = 0;
        switch (definitionType) {
            case 1:
                priority = this.mWifiPriority;
                break;
            case 2:
                priority = this.m4G3GPriority;
                break;
            default:
                priority = this.mDefaultPriority;
                break;
        }
        if (priority != null && priority.length != 0 && videoDefinitionMap != null && videoDefinitionMap.size() != 0) {
            int i = 0;
            while (true) {
                if (i < priority.length) {
                    DWVideoDefinition videoDefinition = videoDefinitionMap.get(priority[i]);
                    if (videoDefinition == null || TextUtils.isEmpty(videoDefinition.getVideoUrl())) {
                        i++;
                    } else {
                        videoUrl = videoDefinition.getVideoUrl();
                        cacheKey = videoDefinition.getCacheKey();
                        definition = priority[i];
                        bitRate = videoDefinition.getVideoBitrate();
                        videoLength = videoDefinition.getVideoSize();
                    }
                }
            }
            if (!TextUtils.isEmpty(videoUrl)) {
                String videoUrl2 = addVideoUrlScheme(videoUrl);
                if (backup) {
                    dwContext.setBackupVideoDetail(videoUrl2, AndroidUtils.getVideoDefinition(MediaConstant.H264, definition));
                    dwContext.setBackupCacheKey(cacheKey);
                    dwContext.setBackupVideoLength(videoLength);
                    return;
                }
                dwContext.setVideoUrl(videoUrl2);
                dwContext.setVideoDefinition(AndroidUtils.getVideoDefinition(MediaConstant.H264, definition));
                dwContext.setCacheKey(cacheKey);
                dwContext.setCurrentBitRate(bitRate);
                dwContext.setVideoLength(videoLength);
            }
        }
    }

    private String getRateAdapterPriority(int rateAdapterType, boolean custom) {
        String priority;
        switch (rateAdapterType) {
            case 1:
                priority = "hd#sd#ld#ud";
                break;
            case 2:
                priority = "sd#ld#hd#ud";
                break;
            default:
                priority = "ld#sd#hd#ud";
                break;
        }
        if (custom) {
            return "custom#" + priority;
        }
        return priority;
    }

    /* access modifiers changed from: private */
    public boolean useTBNet() {
        if (this.mMediaPlayContext == null || MediaAdapteManager.mConfigAdapter == null || MediaAdapteManager.mABTestAdapter == null || MediaAdapteManager.mMeasureAdapter == null || MediaAdapteManager.mConfigAdapter == null || !AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, "tbNetEnable", "true")) || MediaAdapteManager.mABTestAdapter == null || !MediaConstant.ABTEST_USE_TBNET.equals(MediaAdapteManager.mABTestAdapter.getBucket(MediaConstant.ABTEST_PROXY_NETWORK_COMOPONENT, MediaConstant.ABTEST_PROXY_NETWORK_MODULE))) {
            return false;
        }
        return true;
    }

    private boolean setCustomUrl(MediaPlayControlContext dwContext, Map<String, DWVideoDefinition> videoDefinitionMap) {
        DWVideoDefinition videoDefinition;
        if (videoDefinitionMap == null || videoDefinitionMap.size() == 0 || (videoDefinition = videoDefinitionMap.get("custom")) == null || TextUtils.isEmpty(videoDefinition.getVideoUrl())) {
            return false;
        }
        dwContext.setVideoUrl(addVideoUrlScheme(videoDefinition.getVideoUrl()));
        dwContext.setVideoDefinition(AndroidUtils.getVideoDefinition(MediaConstant.H264, "custom"));
        dwContext.setCacheKey(videoDefinition.getCacheKey());
        dwContext.setCurrentBitRate(videoDefinition.getVideoBitrate());
        dwContext.setVideoLength(videoDefinition.getVideoSize());
        return true;
    }

    private void setLiveRateAdapteUrl(MediaLiveInfo mediaLiveInfo, boolean useH265, int rateAdapterLevel) {
        int index;
        String[] priority;
        boolean z = true;
        this.mMediaPlayContext.setVideoUrl("");
        mediaLiveInfo.rateAdapte = false;
        this.mMediaPlayContext.setTopAnchor(mediaLiveInfo.rateAdapte);
        this.mMediaPlayContext.setEdgePcdn(mediaLiveInfo.edgePcdn);
        this.mMediaPlayContext.setRateAdapte(Boolean.FALSE.booleanValue());
        if (!TextUtils.isEmpty(mediaLiveInfo.mediaSourceType)) {
            this.mMediaPlayContext.mMediaSourceType = mediaLiveInfo.mediaSourceType;
        }
        if (!TextUtils.isEmpty(mediaLiveInfo.liveId)) {
            this.mMediaPlayContext.mVideoId = mediaLiveInfo.liveId;
        }
        if (!TextUtils.isEmpty(mediaLiveInfo.anchorId)) {
            this.mMediaPlayContext.mAccountId = mediaLiveInfo.anchorId;
        }
        if (MediaAdapteManager.mConfigAdapter != null && AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, "useRateAdapte", "true"))) {
            if (mediaLiveInfo.rateAdapte) {
                switch (rateAdapterLevel) {
                    case 1:
                    case 2:
                        priority = this.mLiveWifiPriority;
                        break;
                    default:
                        this.mMediaPlayContext.setRateAdapte(Boolean.TRUE.booleanValue());
                        priority = this.mLiveDefaultPriority;
                        break;
                }
            } else {
                priority = this.mLiveWifiPriority;
            }
            if (!this.mMediaPlayContext.isLowPerformance() && Build.VERSION.SDK_INT >= 23) {
                String[] lowpriority = this.mLiveLowQualityPriority;
                for (int j = 0; j < lowpriority.length; j++) {
                    for (int i = 0; i < mediaLiveInfo.liveUrlList.size(); i++) {
                        String definition = mediaLiveInfo.liveUrlList.get(i).definition;
                        if (mediaLiveInfo.liveUrlList.get(i) != null && lowpriority[j].equals(definition) && !TextUtils.isEmpty(mediaLiveInfo.liveUrlList.get(i).flvUrl)) {
                            this.mMediaPlayContext.mLowQualityUrl = mediaLiveInfo.liveUrlList.get(i).flvUrl;
                        }
                    }
                }
            }
            for (int j2 = 0; j2 < priority.length; j2++) {
                for (int i2 = 0; i2 < mediaLiveInfo.liveUrlList.size(); i2++) {
                    String definition2 = mediaLiveInfo.liveUrlList.get(i2).definition;
                    if (mediaLiveInfo.liveUrlList.get(i2) != null && priority[j2].equals(definition2)) {
                        if (setLiveUrl(mediaLiveInfo.h265, mediaLiveInfo.liveUrlList.get(i2), useH265 && this.mMediaPlayContext.mH265Enable)) {
                            return;
                        }
                    }
                }
            }
        }
        if (TextUtils.isEmpty(this.mMediaPlayContext.getVideoUrl()) && (index = getDefalutQualityIndex(this.mMediaPlayContext.mMediaLiveInfo)) >= 0) {
            boolean z2 = mediaLiveInfo.h265;
            QualityLiveItem qualityLiveItem = this.mMediaPlayContext.mMediaLiveInfo.liveUrlList.get(index);
            if (!useH265 || !this.mMediaPlayContext.mH265Enable) {
                z = false;
            }
            setLiveUrl(z2, qualityLiveItem, z);
        }
    }

    public void changeQuality(int qualityIndex, IMediaUrlPickCallBack urlPickCallBack) {
        int index;
        boolean useH265 = setH265Hardware();
        setH264Hardware();
        this.mMediaPlayContext.setH265(useH265);
        if (MediaAdapteManager.mMeasureAdapter != null) {
            this.mMediaPlayContext.setNetSpeed(MediaAdapteManager.mMeasureAdapter.getNetSpeedValue());
        }
        this.mMediaPlayContext.setVideoUrl("");
        if (this.mMediaPlayContext.mMediaLiveInfo.liveUrlList.get(qualityIndex) != null) {
            urlPickCallBack.onPick(setLiveUrl(this.mMediaPlayContext.mMediaLiveInfo.h265, this.mMediaPlayContext.mMediaLiveInfo.liveUrlList.get(qualityIndex), useH265), "");
        } else if (TextUtils.isEmpty(this.mMediaPlayContext.getVideoUrl()) && (index = getDefalutQualityIndex(this.mMediaPlayContext.mMediaLiveInfo)) > 0) {
            setLiveUrl(this.mMediaPlayContext.mMediaLiveInfo.h265, this.mMediaPlayContext.mMediaLiveInfo.liveUrlList.get(index), useH265);
        }
    }

    private boolean setLiveUrl(boolean wholeH265, QualityLiveItem liveItem, boolean useH265) {
        this.mMediaPlayContext.mSVCEnable = false;
        if (!TextUtils.isEmpty(liveItem.videoUrl)) {
            this.mMediaPlayContext.setVideoUrl(liveItem.videoUrl);
            this.mMediaPlayContext.setVideoDefinition(AndroidUtils.getVideoDefinition(MediaConstant.H264, liveItem.definition));
            this.mMediaPlayContext.mSelectedUrlName = "videoUrl";
            return true;
        } else if (!TextUtils.isEmpty(liveItem.replayUrl)) {
            this.mMediaPlayContext.setVideoUrl(liveItem.replayUrl);
            this.mMediaPlayContext.setVideoDefinition(AndroidUtils.getVideoDefinition(MediaConstant.H264, liveItem.definition));
            this.mMediaPlayContext.mSelectedUrlName = "replayUrl";
            return true;
        } else {
            if (useH265 && wholeH265 && this.mMediaPlayContext.useArtp() && !TextUtils.isEmpty(liveItem.wholeH265ArtpUrl)) {
                this.mMediaPlayContext.setVideoUrl(liveItem.wholeH265ArtpUrl);
                this.mMediaPlayContext.setVideoDefinition(AndroidUtils.getVideoDefinition(MediaConstant.H265_ARTP, liveItem.definition));
                this.mMediaPlayContext.mSelectedUrlName = "wholeH265ArtpUrl";
            } else if (useH265 && wholeH265 && !TextUtils.isEmpty(liveItem.wholeH265FlvUrl)) {
                this.mMediaPlayContext.setVideoUrl(liveItem.wholeH265FlvUrl);
                this.mMediaPlayContext.setVideoDefinition(AndroidUtils.getVideoDefinition(MediaConstant.H265, liveItem.definition));
                this.mMediaPlayContext.mSelectedUrlName = "wholeH265FlvUrl";
                return true;
            } else if (useH265 && !wholeH265 && !TextUtils.isEmpty(liveItem.h265Url) && this.mMediaPlayContext.useTransH265()) {
                this.mMediaPlayContext.setVideoUrl(liveItem.h265Url);
                this.mMediaPlayContext.setVideoDefinition(AndroidUtils.getVideoDefinition(MediaConstant.H265, liveItem.definition));
                this.mMediaPlayContext.mSelectedUrlName = "h265Url";
                return true;
            } else if (this.mMediaPlayContext.useArtp() && !wholeH265 && !TextUtils.isEmpty(liveItem.artpUrl)) {
                this.mMediaPlayContext.setVideoUrl(liveItem.artpUrl);
                this.mMediaPlayContext.setVideoDefinition(AndroidUtils.getVideoDefinition(MediaConstant.H264_ARTP, liveItem.definition));
                this.mMediaPlayContext.mSelectedUrlName = "artpUrl";
                return true;
            } else if (!TextUtils.isEmpty(liveItem.flvUrl)) {
                this.mMediaPlayContext.setVideoUrl(liveItem.flvUrl);
                this.mMediaPlayContext.setVideoDefinition(AndroidUtils.getVideoDefinition(MediaConstant.H264, liveItem.definition));
                this.mMediaPlayContext.mSelectedUrlName = "flvUrl";
                return true;
            }
            return false;
        }
    }

    public static int getDefalutQualityIndex(MediaLiveInfo info) {
        ArrayList<QualityLiveItem> liveUrlList;
        if (!(info == null || (liveUrlList = info.liveUrlList) == null || liveUrlList.size() <= 0)) {
            if (liveUrlList.size() == 1) {
                return 0;
            }
            if (liveUrlList.size() < 2) {
                return -1;
            }
            return 1;
        }
        return -1;
    }

    private void setH265RateAdapteUrl(MediaPlayControlContext dwContext, Map<String, DWVideoDefinition> videoDefinitionMap, int definitionType) {
        String[][] h265Priority;
        String definition = null;
        String videoUrl = null;
        String cacheKey = null;
        int videoLength = -1;
        int bitRate = 0;
        switch (definitionType) {
            case 1:
                h265Priority = this.mH265WifiPriority;
                break;
            case 2:
                h265Priority = this.mH2654G3GPriority;
                break;
            default:
                h265Priority = this.mH265DefaultPriority;
                break;
        }
        if (h265Priority != null && h265Priority.length != 0 && videoDefinitionMap != null && videoDefinitionMap.size() != 0) {
            int i = 0;
            while (true) {
                if (i < h265Priority.length) {
                    DWVideoDefinition videoDefinition = videoDefinitionMap.get(h265Priority[i][0]);
                    if (videoDefinition == null || TextUtils.isEmpty(videoDefinition.getVideoUrl())) {
                        i++;
                    } else {
                        videoUrl = videoDefinition.getVideoUrl();
                        cacheKey = videoDefinition.getCacheKey();
                        definition = h265Priority[i][1];
                        bitRate = videoDefinition.getVideoBitrate();
                        videoLength = videoDefinition.getVideoSize();
                    }
                }
            }
            if (!TextUtils.isEmpty(videoUrl)) {
                dwContext.setVideoUrl(addVideoUrlScheme(videoUrl));
                dwContext.setVideoDefinition(AndroidUtils.getVideoDefinition(MediaConstant.H265, definition));
                dwContext.setH265(true);
                dwContext.setCacheKey(cacheKey);
                dwContext.setCurrentBitRate(bitRate);
                dwContext.setVideoLength(videoLength);
                return;
            }
            dwContext.setH265(false);
        }
    }

    public int getRateAdapterType(String netType, int netSpeed, boolean isLowPerformance) {
        if (TextUtils.isEmpty(netType) || netSpeed <= 0 || this.mMediaPlayContext == null || MediaAdapteManager.mMeasureAdapter == null || MediaAdapteManager.mConfigAdapter == null || !AndroidUtils.parseBoolean(MediaAdapteManager.mConfigAdapter.getConfig(this.mMediaPlayContext.mConfigGroup, "videoDeviceMeaseureEnable", "true")) || MediaAdapteManager.mABTestAdapter == null || !MediaConstant.ABTEST_USE_MEASURE.equals(MediaAdapteManager.mABTestAdapter.getBucket(MediaConstant.ABTEST_DEVICEMEASURE_COMOPONENT, MediaConstant.ABTEST_DEVICEMEASURE_MODULE))) {
            char c = 65535;
            switch (netType.hashCode()) {
                case 1652:
                    if (netType.equals("3G")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1683:
                    if (netType.equals("4G")) {
                        c = 1;
                        break;
                    }
                    break;
                case 2664213:
                    if (netType.equals("WIFI")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                    return 1;
                case 2:
                    return 2;
                default:
                    return 3;
            }
        } else if (this.mMediaPlayContext.mTBLive) {
            return getDeviceVideoPerformanceType(netType, 20000, isLowPerformance);
        } else {
            return getDeviceVideoPerformanceType(netType, netSpeed, isLowPerformance);
        }
    }

    private int getDeviceVideoPerformanceType(String netType, int netSpeed, boolean isLowPerformance) {
        if (this.mMediaPlayContext != null) {
            this.mMediaPlayContext.setVdeoDeviceMeaseureEnable(true);
        }
        if ((("WIFI".equals(netType) && netSpeed > 1500) || ("4G".equals(netType) && netSpeed > 2000)) && !isLowPerformance) {
            return 1;
        }
        if (netSpeed <= 1000 || "2G".equals(netType)) {
            return 3;
        }
        return 2;
    }
}
