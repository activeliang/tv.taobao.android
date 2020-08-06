package com.taobao.mediaplay.model;

import android.text.TextUtils;
import com.alibaba.motu.videoplayermonitor.VPMConstants;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class DWVideoInfoData {
    private int mAvdataBufferedMaxKBytes;
    private int mAvdataBufferedMaxTime;
    private int mCurrentLevel;
    private JSONObject mData;
    private int mMaxLevel;
    private Map<String, CacheKeyDefinition> mVideoCacheKeyMap;
    private String mVideoCoverUrl;
    private Map<String, DWVideoDefinition> mVideoDefinitionMap;
    private long mVideoDuration;

    public DWVideoInfoData(JSONObject data) {
        Object playerBufferSettingObj;
        this.mData = data;
        if (this.mData != null) {
            Object obCoverPicUrl = this.mData.opt("coverUrl");
            this.mVideoCoverUrl = obCoverPicUrl == null ? null : String.valueOf(obCoverPicUrl);
            Object obVideoDuration = this.mData.opt(VPMConstants.MEASURE_DURATION);
            this.mVideoDuration = (obVideoDuration == null || !TextUtils.isDigitsOnly(obVideoDuration.toString())) ? 0 : Long.parseLong(obVideoDuration.toString());
            Object resourcesObj = this.mData.opt("resources");
            if (resourcesObj != null && (resourcesObj instanceof JSONArray) && ((JSONArray) resourcesObj).length() > 0) {
                JSONArray resources = (JSONArray) resourcesObj;
                this.mVideoDefinitionMap = new HashMap();
                int resourcesCount = resources.length();
                for (int i = 0; i < resourcesCount; i++) {
                    DWVideoDefinition videoDefinition = new DWVideoDefinition(resources.optJSONObject(i));
                    if (!TextUtils.isEmpty(videoDefinition.getDefinition()) && !TextUtils.isEmpty(videoDefinition.getVideoUrl())) {
                        this.mVideoDefinitionMap.put(videoDefinition.getDefinition(), videoDefinition);
                    }
                }
            }
            Object configurationObj = this.mData.opt("configuration");
            if (configurationObj != null && (configurationObj instanceof JSONObject) && (playerBufferSettingObj = ((JSONObject) configurationObj).opt("playerBufferSetting")) != null && (playerBufferSettingObj instanceof JSONObject)) {
                JSONObject playerBufferSettingJob = (JSONObject) playerBufferSettingObj;
                Object avdataBufferedMaxMBytesObj = playerBufferSettingJob.opt("avdataBufferedMaxBytes");
                this.mAvdataBufferedMaxKBytes = (avdataBufferedMaxMBytesObj == null || !TextUtils.isDigitsOnly(avdataBufferedMaxMBytesObj.toString())) ? 0 : Integer.parseInt(avdataBufferedMaxMBytesObj.toString());
                Object avdataBufferedMaxTimeObj = playerBufferSettingJob.opt("avdataBufferedMaxTime");
                this.mAvdataBufferedMaxTime = (avdataBufferedMaxTimeObj == null || !TextUtils.isDigitsOnly(avdataBufferedMaxTimeObj.toString())) ? 0 : Integer.parseInt(avdataBufferedMaxTimeObj.toString());
                Object currentLevel = playerBufferSettingJob.opt("currentLevel");
                this.mCurrentLevel = (currentLevel == null || !TextUtils.isDigitsOnly(currentLevel.toString())) ? 0 : Integer.parseInt(currentLevel.toString());
                Object maxLevel = playerBufferSettingJob.opt("maxLevel");
                this.mMaxLevel = (maxLevel == null || !TextUtils.isDigitsOnly(maxLevel.toString())) ? 0 : Integer.parseInt(maxLevel.toString());
            }
            Object cachesObj = this.mData.opt("caches");
            if (cachesObj != null && (cachesObj instanceof JSONArray) && ((JSONArray) cachesObj).length() > 0) {
                JSONArray caches = (JSONArray) cachesObj;
                this.mVideoCacheKeyMap = new HashMap();
                int resourcesCount2 = caches.length();
                for (int i2 = 0; i2 < resourcesCount2; i2++) {
                    CacheKeyDefinition cacheKeyDefinition = new CacheKeyDefinition(caches.optJSONObject(i2));
                    if (!TextUtils.isEmpty(cacheKeyDefinition.getDefinition()) && !TextUtils.isEmpty(cacheKeyDefinition.getCacheKey())) {
                        this.mVideoCacheKeyMap.put(cacheKeyDefinition.getDefinition(), cacheKeyDefinition);
                    }
                }
            }
        }
    }

    public Map<String, DWVideoDefinition> getVideoDefinitionMap() {
        return this.mVideoDefinitionMap;
    }

    public Map<String, CacheKeyDefinition> getCacheDefinitionMap() {
        return this.mVideoCacheKeyMap;
    }

    public int getCurrentLevel() {
        return this.mCurrentLevel;
    }

    public int getMaxLevel() {
        return this.mMaxLevel;
    }

    public int getBufferedMaxMBytes() {
        return this.mAvdataBufferedMaxKBytes * 1024;
    }

    public int getAvdataBufferedMaxTime() {
        return this.mAvdataBufferedMaxTime;
    }

    public String getCoverUrl() {
        return this.mVideoCoverUrl;
    }
}
