package com.alibaba.motu.videoplayermonitor.errorStatistics;

import com.alibaba.motu.videoplayermonitor.model.MotuErrorStatisticBase;
import java.util.Map;

public class MotuRequestErrStatisticsInfo extends MotuErrorStatisticBase {
    public Map<String, Double> toMap() {
        return toBaseMap();
    }
}
