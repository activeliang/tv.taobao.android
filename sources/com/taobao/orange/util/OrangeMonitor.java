package com.taobao.orange.util;

import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.orange.OConstant;

public class OrangeMonitor {
    private static boolean mAppMonitorValid;

    static {
        mAppMonitorValid = false;
        try {
            Class.forName(OConstant.REFLECT_APPMONITOR);
            mAppMonitorValid = true;
        } catch (ClassNotFoundException e) {
            mAppMonitorValid = false;
        }
    }

    public static void commitSuccess(String module, String monitorPoint, String arg) {
        if (mAppMonitorValid) {
            AppMonitor.Alarm.commitSuccess(module, monitorPoint, arg);
        }
    }

    public static void commitFail(String module, String monitorPoint, String arg, String errorCode, String errorMsg) {
        if (mAppMonitorValid) {
            AppMonitor.Alarm.commitFail(module, monitorPoint, arg, errorCode, errorMsg);
        }
    }

    public static void commitCount(String module, String monitorPoint, String arg, double value) {
        if (mAppMonitorValid) {
            AppMonitor.Counter.commit(module, monitorPoint, arg, value);
        }
    }

    public static void register(String module, String monitorPoint, MeasureSet measures, DimensionSet dimensions, boolean isCommitDetail) {
        if (mAppMonitorValid) {
            AppMonitor.register(module, monitorPoint, measures, dimensions, isCommitDetail);
        }
    }

    public static void commitStat(String module, String monitorPoint, DimensionValueSet dimensionValues, MeasureValueSet measureValues) {
        if (mAppMonitorValid) {
            AppMonitor.Stat.commit(module, monitorPoint, dimensionValues, measureValues);
        }
    }
}
