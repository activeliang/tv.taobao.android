package com.alibaba.motu.videoplayermonitor.fluentStatistics;

import com.alibaba.motu.videoplayermonitor.model.MotuMediaBase;
import java.util.Map;

public class FluentInfo extends MotuMediaBase {
    public String playType;

    public Map<String, String> toMap() {
        Map<String, String> map = toBaseMap();
        if (this.playType != null) {
            map.put("playType", this.playType);
        }
        return map;
    }
}
