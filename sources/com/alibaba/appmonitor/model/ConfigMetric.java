package com.alibaba.appmonitor.model;

import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;

@Deprecated
public class ConfigMetric extends Metric {
    public ConfigMetric(String module, String monitorPoint, MeasureSet measureSet) {
        super(module, monitorPoint, measureSet, (DimensionSet) null, false);
    }
}
