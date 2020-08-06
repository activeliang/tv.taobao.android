package com.alibaba.motu.videoplayermonitor;

import java.util.HashMap;
import java.util.Map;

public class MotuStatisticsInfo {
    public double adPlayDuration;
    public double adPlayerPrepare;
    public double adUrlReqTime;
    public double avgKeyFrameSize;
    public double avgVideoBitrate;
    public double bufferLatency;
    public double cdnUrlReqDuration;
    public double duration;
    public Map<String, Double> extStatisticsData = null;
    public double impairmentDegree;
    public double impairmentDuration;
    public double impairmentFrequency;
    public double seekCount;
    public double seekDuration;
    public double videoFirstFrameDuration;
    public double videoFrameRate;
    public double videoLocalCacheSize;
    public double videoPlayDuration;
    public double videoPlayerPrepare;
    public double videoUrlReqTime;

    public Map<String, Double> toMap() {
        Map<String, Double> map = new HashMap<>();
        map.put(VPMConstants.MEASURE_ADPLAYDURATION, Double.valueOf(this.adPlayDuration));
        map.put(VPMConstants.MEASURE_VIDEOPLAYDURATION, Double.valueOf(this.videoPlayDuration));
        map.put(VPMConstants.MEASURE_BUFFERLATENCY, Double.valueOf(this.bufferLatency));
        map.put(VPMConstants.MEASURE_VIDEOFIRSTFRAMEDURATION, Double.valueOf(this.videoFirstFrameDuration));
        map.put(VPMConstants.MEASURE_VIDEOFRAMERATE, Double.valueOf(this.videoFrameRate));
        map.put(VPMConstants.MEASURE_AVG_VIDEOBITRATE, Double.valueOf(this.avgVideoBitrate));
        map.put(VPMConstants.MEASURE_AVG_KEYFRAMESIZE, Double.valueOf(this.avgKeyFrameSize));
        map.put(VPMConstants.MEASURE_IMPAIRMENTFREQUENCY, Double.valueOf(this.impairmentFrequency));
        map.put("impairmentDuration", Double.valueOf(this.impairmentDuration));
        map.put(VPMConstants.MEASURE_IMPAIRMENTDEGREE, Double.valueOf(this.impairmentDegree));
        map.put(VPMConstants.MEASURE_DURATION, Double.valueOf(this.duration));
        map.put(VPMConstants.MEASURE_ADURLREQTIME, Double.valueOf(this.adUrlReqTime));
        map.put(VPMConstants.MEASURE_ADPLAYERPREPARE, Double.valueOf(this.adPlayerPrepare));
        map.put(VPMConstants.MEASURE_VIDEOURLREQTIME, Double.valueOf(this.videoUrlReqTime));
        map.put(VPMConstants.MEASURE_VIDEOPLAYERPREPARE, Double.valueOf(this.videoPlayerPrepare));
        map.put(VPMConstants.MEASURE_SEEKDURATION, Double.valueOf(this.seekDuration));
        map.put(VPMConstants.MEASURE_CDNURLREQDURATION, Double.valueOf(this.cdnUrlReqDuration));
        map.put(VPMConstants.MEASURE_SEEKCOUNT, Double.valueOf(this.seekCount));
        map.put(VPMConstants.MEASURE_VIDEOLOCALCACHESIZE, Double.valueOf(this.videoLocalCacheSize));
        if (this.extStatisticsData != null && this.extStatisticsData.size() > 0) {
            map.putAll(this.extStatisticsData);
        }
        return map;
    }
}
