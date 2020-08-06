package com.yunos.tv.core.util;

import android.content.Context;
import android.os.Build;
import com.alibaba.appmonitor.delegate.AppMonitorDelegate;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import com.yunos.tv.core.common.DeviceJudge;
import com.yunos.tv.core.config.Config;
import com.zhiping.dev.android.logger.ZpLogger;

public class MonitorUtil {
    private static boolean init = false;

    public static void init() {
        if (!init) {
            init = true;
            AppMonitorDelegate.IS_DEBUG = Config.isDebug();
            registerAppMonitors();
        }
    }

    private static void registerAppMonitors() {
        DimensionSet dimensionSet = DimensionSet.create();
        dimensionSet.addDimension("channelName");
        dimensionSet.addDimension("version");
        dimensionSet.addDimension("debug");
        dimensionSet.addDimension("domain");
        dimensionSet.addDimension("url");
        dimensionSet.addDimension(BlitzServiceUtils.CSUCCESS);
        MeasureSet measureSet = MeasureSet.create();
        measureSet.addMeasure(new Measure("loadTime", Double.valueOf(ClientTraceData.b.f47a)));
        AppMonitor.register("tvtaobao", "pageload", measureSet, dimensionSet);
        DimensionSet activityDimensionSet = DimensionSet.create();
        activityDimensionSet.addDimension("channelName");
        activityDimensionSet.addDimension("version");
        activityDimensionSet.addDimension("debug");
        activityDimensionSet.addDimension("activityName");
        activityDimensionSet.addDimension("model");
        activityDimensionSet.addDimension("brand");
        activityDimensionSet.addDimension("product");
        activityDimensionSet.addDimension("api");
        activityDimensionSet.addDimension("memory");
        MeasureSet activityMeasureSet = MeasureSet.create();
        activityMeasureSet.addMeasure(new Measure("loadTime", Double.valueOf(ClientTraceData.b.f47a)));
        AppMonitor.register("tvtaobao", "activityLoad", measureSet, dimensionSet);
        DimensionSet appLoadTimeDimensionSet = DimensionSet.create();
        activityDimensionSet.addDimension("channelName");
        activityDimensionSet.addDimension("version");
        activityDimensionSet.addDimension("model");
        activityDimensionSet.addDimension("product");
        activityDimensionSet.addDimension("api");
        activityDimensionSet.addDimension("memorySize");
        activityDimensionSet.addDimension("cpuCoresCount");
        activityDimensionSet.addDimension("cpuInfoMaxFreq");
        activityDimensionSet.addDimension("loadTime");
        MeasureSet appLoadTimeSet = MeasureSet.create();
        activityMeasureSet.addMeasure(new Measure("appLoadTime", Double.valueOf(ClientTraceData.b.f47a)));
        AppMonitor.register("tvtaobao", "appLoad", appLoadTimeSet, appLoadTimeDimensionSet);
    }

    public static DimensionValueSet createDimensionValueSet(Context context) {
        DimensionValueSet dimensionValueSet = DimensionValueSet.create();
        dimensionValueSet.setValue("channelName", Config.getChannelName());
        dimensionValueSet.setValue("version", Config.getVersionName(context));
        dimensionValueSet.setValue("debug", Config.isDebug() ? "true" : "false");
        dimensionValueSet.setValue(BlitzServiceUtils.CSUCCESS, "true");
        dimensionValueSet.setValue("model", Build.MODEL);
        dimensionValueSet.setValue("product", Build.PRODUCT);
        dimensionValueSet.setValue("api", "" + Build.VERSION.SDK_INT);
        dimensionValueSet.setValue("brand", Build.BRAND);
        dimensionValueSet.setValue("memory", DeviceJudge.getMemoryType().name());
        return dimensionValueSet;
    }

    public static void reportAppLoadTime(Context context, double loadTime) {
        try {
            DimensionValueSet dimensionValueSet = DimensionValueSet.create();
            dimensionValueSet.setValue("channelName", Config.getChannelName());
            dimensionValueSet.setValue("version", Config.getVersionName(context));
            dimensionValueSet.setValue("model", Build.MODEL);
            dimensionValueSet.setValue("product", Build.PRODUCT);
            dimensionValueSet.setValue("api", "" + Build.VERSION.SDK_INT);
            dimensionValueSet.setValue("memorySize", "" + DeviceJudge.getTotalMemorySizeInMB());
            dimensionValueSet.setValue("cpuCoresCount", "" + DeviceJudge.getCoreCount());
            dimensionValueSet.setValue("cpuInfoMaxFreq", "" + DeviceJudge.getMaxCpuFreqInKHz());
            dimensionValueSet.setValue("loadTime", "" + loadTime);
            MeasureValueSet measureValueSet = MeasureValueSet.create();
            measureValueSet.setValue("appLoadTime", loadTime);
            AppMonitor.Stat.commit("tvtaobao", "appLoad", dimensionValueSet, measureValueSet);
            ZpLogger.i(MonitorUtil.class.getSimpleName(), " reportAppLoadTime " + loadTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
