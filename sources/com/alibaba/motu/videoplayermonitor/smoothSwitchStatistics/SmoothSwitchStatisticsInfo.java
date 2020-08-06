package com.alibaba.motu.videoplayermonitor.smoothSwitchStatistics;

import com.alibaba.motu.videoplayermonitor.VPMConstants;
import java.util.HashMap;
import java.util.Map;

public class SmoothSwitchStatisticsInfo {
    public Map<String, Double> extStatisticsData = null;
    public double smoothSwitchCounts;
    public double smoothSwitchSuccess;

    public Map<String, Double> toMap() {
        Map<String, Double> map = new HashMap<>();
        map.put(VPMConstants.MEASURE_SMOOTHSWITCHSUCCESS, Double.valueOf(this.smoothSwitchSuccess));
        map.put(VPMConstants.MEASURE_SMOOTHSWITCHCOUNTS, Double.valueOf(this.smoothSwitchCounts));
        if (this.extStatisticsData != null && this.extStatisticsData.size() > 0) {
            map.putAll(this.extStatisticsData);
        }
        return map;
    }
}
