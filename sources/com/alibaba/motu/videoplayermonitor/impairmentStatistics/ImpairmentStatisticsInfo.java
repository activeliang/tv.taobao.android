package com.alibaba.motu.videoplayermonitor.impairmentStatistics;

import com.alibaba.motu.videoplayermonitor.VPMConstants;
import java.util.HashMap;
import java.util.Map;

public class ImpairmentStatisticsInfo {
    public Map<String, Double> extStatisticsData = null;
    public double impairmentDuration;
    public double impairmentInterval;

    public Map<String, Double> toMap() {
        Map<String, Double> map = new HashMap<>();
        map.put("impairmentDuration", Double.valueOf(this.impairmentDuration));
        map.put(VPMConstants.MEASURE_IMP_IMPAIRMENTINTERVAL, Double.valueOf(this.impairmentInterval));
        if (this.extStatisticsData != null && this.extStatisticsData.size() > 0) {
            map.putAll(this.extStatisticsData);
        }
        return map;
    }
}
