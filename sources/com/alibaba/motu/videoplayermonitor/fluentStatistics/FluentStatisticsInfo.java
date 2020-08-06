package com.alibaba.motu.videoplayermonitor.fluentStatistics;

import com.alibaba.motu.videoplayermonitor.VPMConstants;
import java.util.HashMap;
import java.util.Map;

public class FluentStatisticsInfo {
    public Map<String, Double> extStatisticsData = null;
    public double playFluentSlices;
    public double playSlices;

    public Map<String, Double> toMap() {
        Map<String, Double> map = new HashMap<>();
        map.put(VPMConstants.MEASURE_FLUENT_PLAYFLUENTSLICES, Double.valueOf(this.playFluentSlices));
        map.put(VPMConstants.MEASURE_FLUENT_PLAYSLICES, Double.valueOf(this.playSlices));
        if (this.extStatisticsData != null && this.extStatisticsData.size() > 0) {
            map.putAll(this.extStatisticsData);
        }
        return map;
    }
}
