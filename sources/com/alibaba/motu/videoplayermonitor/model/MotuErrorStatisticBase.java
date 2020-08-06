package com.alibaba.motu.videoplayermonitor.model;

import java.util.HashMap;
import java.util.Map;

public class MotuErrorStatisticBase {
    public Map<String, Double> extStatisticsData = null;

    public Map<String, Double> toBaseMap() {
        Map<String, Double> map = new HashMap<>();
        if (this.extStatisticsData != null && this.extStatisticsData.size() > 0) {
            map.putAll(this.extStatisticsData);
        }
        return map;
    }
}
