package com.alibaba.mtl.appmonitor;

import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;

@Deprecated
public class AppMonitorStatTable {
    private String module;
    private String monitorPoint;

    public AppMonitorStatTable(String module2, String monitorPoint2) {
        this.module = module2;
        this.monitorPoint = monitorPoint2;
    }

    public AppMonitorStatTable registerRowAndColumn(DimensionSet rows, MeasureSet columns, boolean isDetail) {
        AppMonitor.register(this.module, this.monitorPoint, columns, rows, isDetail);
        return this;
    }

    public AppMonitorStatTable update(DimensionValueSet rowValues, MeasureValueSet columnValues) {
        AppMonitor.Stat.commit(this.module, this.monitorPoint, rowValues, columnValues);
        return this;
    }
}
