package com.alibaba.appmonitor.sample;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class AccurateSampling extends AMConifg {
    private Map<String, AccurateSampleCondition> conditions = new HashMap();

    public AccurateSampling(int sampling) {
    }

    public Boolean isSampled(int samplingSeed, Map<String, String> map) {
        if (map == null || this.conditions == null) {
            return false;
        }
        for (String conditionName : this.conditions.keySet()) {
            if (!this.conditions.get(conditionName).hitCondition(map.get(conditionName))) {
                return false;
            }
        }
        return Boolean.valueOf(checkSelfSampling(samplingSeed));
    }
}
