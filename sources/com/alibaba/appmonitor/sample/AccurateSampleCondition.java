package com.alibaba.appmonitor.sample;

import android.text.TextUtils;
import java.util.Set;

public class AccurateSampleCondition {
    private String name;
    private AccurateType type;
    private Set<String> values;

    public AccurateSampleCondition(String name2, Set<String> values2, int type2) {
        this.name = name2;
        this.values = values2;
        this.type = AccurateType.getAccurateType(type2);
    }

    public boolean hitCondition(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        boolean hit = this.values.contains(value);
        if (this.type == AccurateType.IN) {
            return hit;
        }
        if (!hit) {
            return true;
        }
        return false;
    }

    public String getName() {
        return this.name;
    }

    private enum AccurateType {
        IN,
        NOT_IN;

        public static AccurateType getAccurateType(int type) {
            if (type == 0) {
                return NOT_IN;
            }
            return IN;
        }
    }
}
