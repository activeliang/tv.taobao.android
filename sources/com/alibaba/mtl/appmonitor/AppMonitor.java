package com.alibaba.mtl.appmonitor;

import android.app.Application;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.analytics.AnalyticsMgr;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.appmonitor.event.EventType;
import com.alibaba.appmonitor.sample.SampleConfigConstant;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.util.Arrays;
import java.util.Map;
import org.json.JSONArray;

public final class AppMonitor {
    private static final String TAG = "AppMonitor";

    static {
        try {
            System.loadLibrary("ut_c_api");
            Log.i("AppMonitor", "load ut_c_api.so success");
        } catch (Throwable th) {
            Log.w("AppMonitor", "load ut_c_api.so failed");
        }
    }

    @Deprecated
    public static synchronized void init(Application application) {
        synchronized (AppMonitor.class) {
            Logger.d("AppMonitor", "[init]");
            AnalyticsMgr.init(application);
        }
    }

    @Deprecated
    public static void setRequestAuthInfo(boolean isSecurity, String appkey, String secret) {
        throw new RuntimeException("this interface is deprecated after sdk version 6.3.0，please call Analytics.getInstance().setAppApplicationInstance(Application application,IUTApplication utcallback) ");
    }

    @Deprecated
    public static void setChannel(String channel) {
        AnalyticsMgr.setChanel(channel);
    }

    @Deprecated
    public static void turnOnRealTimeDebug(Map<String, String> params) {
        AnalyticsMgr.turnOnRealTimeDebug(params);
    }

    @Deprecated
    public static void turnOffRealTimeDebug() {
        AnalyticsMgr.turnOffRealTimeDebug();
    }

    @Deprecated
    public static synchronized void destroy() {
        synchronized (AppMonitor.class) {
            if (checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.destroy();
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }
    }

    @Deprecated
    public static synchronized void triggerUpload() {
        synchronized (AppMonitor.class) {
            if (checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.triggerUpload();
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }
    }

    public static void setStatisticsInterval(final int statisticsInterval) {
        if (checkInit()) {
            AnalyticsMgr.handler.postWatingTask(new Runnable() {
                public void run() {
                    try {
                        AnalyticsMgr.iAnalytics.setStatisticsInterval1(statisticsInterval);
                    } catch (RemoteException e) {
                        AnalyticsMgr.handleRemoteException(e);
                    }
                }
            });
        }
    }

    public static void setSampling(final int sampling) {
        if (checkInit()) {
            AnalyticsMgr.handler.postWatingTask(new Runnable() {
                public void run() {
                    try {
                        AnalyticsMgr.iAnalytics.setSampling(sampling);
                    } catch (RemoteException e) {
                        AnalyticsMgr.handleRemoteException(e);
                    }
                }
            });
        }
    }

    public static void enableLog(final boolean open) {
        if (checkInit()) {
            AnalyticsMgr.handler.postWatingTask(new Runnable() {
                public void run() {
                    try {
                        AnalyticsMgr.iAnalytics.enableLog(open);
                    } catch (RemoteException e) {
                        AnalyticsMgr.handleRemoteException(e);
                    }
                }
            });
        }
    }

    public static void register(final String module, final String monitorPoint, final MeasureSet measures) {
        if (checkInit()) {
            AnalyticsMgr.handler.postWatingTask(new Runnable() {
                public void run() {
                    try {
                        AnalyticsMgr.iAnalytics.register1(module, monitorPoint, measures);
                    } catch (RemoteException e) {
                        AnalyticsMgr.handleRemoteException(e);
                    }
                }
            });
            addRegisterEntity(module, monitorPoint, measures, (DimensionSet) null, false);
        }
    }

    public static void register(final String module, final String monitorPoint, final MeasureSet measures, final boolean isCommitDetail) {
        if (checkInit()) {
            AnalyticsMgr.handler.postWatingTask(new Runnable() {
                public void run() {
                    try {
                        AnalyticsMgr.iAnalytics.register2(module, monitorPoint, measures, isCommitDetail);
                    } catch (RemoteException e) {
                        AnalyticsMgr.handleRemoteException(e);
                    }
                }
            });
            addRegisterEntity(module, monitorPoint, measures, (DimensionSet) null, isCommitDetail);
        }
    }

    public static void register(final String module, final String monitorPoint, final MeasureSet measures, final DimensionSet dimensions) {
        Logger.d("外注册任务被业务方调用", "module", module, SampleConfigConstant.MONITORPOINT, monitorPoint);
        if (checkInit()) {
            AnalyticsMgr.handler.postWatingTask(new Runnable() {
                public void run() {
                    try {
                        Logger.d("外注册任务开始执行", "module", module, SampleConfigConstant.MONITORPOINT, monitorPoint);
                        AnalyticsMgr.iAnalytics.register3(module, monitorPoint, measures, dimensions);
                    } catch (RemoteException e) {
                        AnalyticsMgr.handleRemoteException(e);
                    }
                }
            });
            addRegisterEntity(module, monitorPoint, measures, dimensions, false);
        }
    }

    public static void register(String module, String monitorPoint, MeasureSet measures, DimensionSet dimensions, boolean isCommitDetail) {
        if (checkInit()) {
            registerInternal(module, monitorPoint, measures, dimensions, isCommitDetail, false);
        }
    }

    private static void registerInternal(String module, String monitorPoint, MeasureSet measures, DimensionSet dimensions, boolean isCommitDetail, boolean isInternal) {
        if (checkInit()) {
            Logger.d("AppMonitor", "[registerInternal] : module:", module, "monitorPoint:", monitorPoint, "measures:", measures, "dimensions:", dimensions, "isCommitDetail:", Boolean.valueOf(isCommitDetail), "isInternal:", Boolean.valueOf(isInternal));
            if (!isInternal) {
                addRegisterEntity(module, monitorPoint, measures, dimensions, isCommitDetail);
            }
            AnalyticsMgr.handler.postWatingTask(createRegisterTask(module, monitorPoint, measures, dimensions, isCommitDetail));
        }
    }

    private static void addRegisterEntity(String module, String monitorPoint, MeasureSet measures, DimensionSet dimensions, boolean isCommitDetail) {
        try {
            AnalyticsMgr.Entity entity = new AnalyticsMgr.Entity();
            entity.module = module;
            entity.monitorPoint = monitorPoint;
            entity.measureSet = measures;
            entity.dimensionSet = dimensions;
            entity.isCommitDetail = isCommitDetail;
            AnalyticsMgr.mRegisterList.add(entity);
        } catch (Throwable th) {
        }
    }

    public static void register(String module, String monitorPoint, String[] measures, String[] dimensions, boolean isCommitDetail) {
        Object[] objArr = new Object[8];
        objArr[0] = "module:";
        objArr[1] = module;
        objArr[2] = "measures:";
        objArr[3] = measures == null ? Constant.NULL : new JSONArray(Arrays.asList(measures)).toString();
        objArr[4] = "dimensions:";
        objArr[5] = dimensions == null ? Constant.NULL : new JSONArray(Arrays.asList(dimensions)).toString();
        objArr[6] = "isCommitDetail:";
        objArr[7] = Boolean.valueOf(isCommitDetail);
        Logger.d("[c interface ]", objArr);
        if (measures != null) {
            MeasureSet measureSet = MeasureSet.create();
            for (String addMeasure : measures) {
                measureSet.addMeasure(addMeasure);
            }
            DimensionSet dimensionSet = null;
            if (dimensions != null) {
                dimensionSet = DimensionSet.create();
                for (String addDimension : dimensions) {
                    dimensionSet.addDimension(addDimension);
                }
            }
            register(module, monitorPoint, measureSet, dimensionSet, isCommitDetail);
            return;
        }
        Logger.d("AppMonitor", "register failed:no mearsure");
    }

    public static void updateMeasure(String module, String monitorPoint, String name, double min, double max, double defaultValue) {
        Logger.d("AppMonitor", "[updateMeasure]");
        if (checkInit()) {
            final String str = module;
            final String str2 = monitorPoint;
            final String str3 = name;
            final double d = min;
            final double d2 = max;
            final double d3 = defaultValue;
            AnalyticsMgr.handler.post(new Runnable() {
                public void run() {
                    try {
                        AnalyticsMgr.iAnalytics.updateMeasure(str, str2, str3, d, d2, d3);
                    } catch (RemoteException e) {
                        AnalyticsMgr.handleRemoteException(e);
                    }
                }
            });
        }
    }

    public static class Alarm {
        public static void setStatisticsInterval(final int statisticsInterval) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.alarm_setStatisticsInterval(statisticsInterval);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        public static void setSampling(final int sampling) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.alarm_setSampling(sampling);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        @Deprecated
        public static boolean checkSampled(String module, String monitorPoint) {
            if (AnalyticsMgr.iAnalytics == null) {
                return false;
            }
            try {
                return AnalyticsMgr.iAnalytics.alarm_checkSampled(module, monitorPoint);
            } catch (RemoteException e) {
                return false;
            }
        }

        public static void commitSuccess(final String module, final String monitorPoint) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.alarm_commitSuccess1(module, monitorPoint);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        public static void commitSuccess(final String module, final String monitorPoint, final String arg) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.alarm_commitSuccess2(module, monitorPoint, arg);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        public static void commitFail(final String module, final String monitorPoint, final String errorCode, final String errorMsg) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.alarm_commitFail1(module, monitorPoint, errorCode, errorMsg);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        public static void commitFail(String module, String monitorPoint, String arg, String errorCode, String errorMsg) {
            if (AppMonitor.checkInit()) {
                final String str = module;
                final String str2 = monitorPoint;
                final String str3 = arg;
                final String str4 = errorCode;
                final String str5 = errorMsg;
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.alarm_commitFail2(str, str2, str3, str4, str5);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }
    }

    public static class Counter {
        public static void setStatisticsInterval(final int statisticsInterval) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.counter_setStatisticsInterval(statisticsInterval);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        public static void setSampling(final int sampling) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.counter_setSampling(sampling);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        @Deprecated
        public static boolean checkSampled(String module, String monitorPoint) {
            if (AnalyticsMgr.iAnalytics == null) {
                return false;
            }
            try {
                return AnalyticsMgr.iAnalytics.counter_checkSampled(module, monitorPoint);
            } catch (RemoteException e) {
                return false;
            }
        }

        public static void commit(final String module, final String monitorPoint, final double value) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.counter_commit1(module, monitorPoint, value);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        public static void commit(String module, String monitorPoint, String arg, double value) {
            if (AppMonitor.checkInit()) {
                final String str = module;
                final String str2 = monitorPoint;
                final String str3 = arg;
                final double d = value;
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.counter_commit2(str, str2, str3, d);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }
    }

    public static class OffLineCounter {
        public static void setStatisticsInterval(final int statisticsInterval) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.offlinecounter_setStatisticsInterval(statisticsInterval);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        public static void setSampling(final int sampling) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.offlinecounter_setSampling(sampling);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        @Deprecated
        public static boolean checkSampled(String module, String monitorPoint) {
            if (AnalyticsMgr.iAnalytics == null) {
                return false;
            }
            try {
                return AnalyticsMgr.iAnalytics.offlinecounter_checkSampled(module, monitorPoint);
            } catch (RemoteException e) {
                return false;
            }
        }

        public static void commit(final String module, final String monitorPoint, final double value) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.offlinecounter_commit(module, monitorPoint, value);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }
    }

    public static class Stat {
        public static void setStatisticsInterval(final int statisticsInterval) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.stat_setStatisticsInterval(statisticsInterval);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        public static void setSampling(final int sampling) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.stat_setSampling(sampling);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        public static boolean checkSampled(String module, String monitorPoint) {
            if (AnalyticsMgr.iAnalytics == null) {
                return false;
            }
            try {
                return AnalyticsMgr.iAnalytics.stat_checkSampled(module, monitorPoint);
            } catch (RemoteException e) {
                return false;
            }
        }

        public static void begin(final String module, final String monitorPoint, final String measureName) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.stat_begin(module, monitorPoint, measureName);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        public static void end(final String module, final String monitorPoint, final String measureName) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.stat_end(module, monitorPoint, measureName);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        public static Transaction createTransaction(String module, String monitorPoint) {
            return createTransaction(module, monitorPoint, (DimensionValueSet) null);
        }

        public static Transaction createTransaction(String module, String monitorPoint, DimensionValueSet dimensionValues) {
            return new Transaction(Integer.valueOf(EventType.STAT.getEventId()), module, monitorPoint, dimensionValues);
        }

        public static void commit(String module, String monitorPoint, double value) {
            commit(module, monitorPoint, (DimensionValueSet) null, value);
        }

        public static void commit(String module, String monitorPoint, DimensionValueSet dimensionValues, double value) {
            if (AppMonitor.checkInit()) {
                final String str = module;
                final String str2 = monitorPoint;
                final DimensionValueSet dimensionValueSet = dimensionValues;
                final double d = value;
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.stat_commit2(str, str2, dimensionValueSet, d);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        public static void commit(final String module, final String monitorPoint, final DimensionValueSet dimensionValues, final MeasureValueSet measureValues) {
            if (AppMonitor.checkInit()) {
                AnalyticsMgr.handler.postWatingTask(new Runnable() {
                    public void run() {
                        try {
                            AnalyticsMgr.iAnalytics.stat_commit3(module, monitorPoint, dimensionValues, measureValues);
                        } catch (RemoteException e) {
                            AnalyticsMgr.handleRemoteException(e);
                        }
                    }
                });
            }
        }

        public static void commit(String module, String monitorPoint, String[] dimensionNames, String[] dimensionValues, String[] measureNames, String[] measureValues) {
            Logger.d("[commit from jni]", new Object[0]);
            DimensionValueSet dimensionValueSet = null;
            MeasureValueSet measureValueSet = null;
            if (!(dimensionNames == null || dimensionValues == null || dimensionNames.length != dimensionValues.length)) {
                dimensionValueSet = DimensionValueSet.create();
                for (int i = 0; i < dimensionValues.length; i++) {
                    dimensionValueSet.setValue(dimensionNames[i], dimensionValues[i]);
                }
            }
            if (measureNames == null || measureValues == null || measureNames.length != measureValues.length) {
                Logger.d("measure is null ,or lenght not match", new Object[0]);
            } else {
                measureValueSet = MeasureValueSet.create();
                for (int i2 = 0; i2 < measureValues.length; i2++) {
                    double value = ClientTraceData.b.f47a;
                    if (!TextUtils.isEmpty(measureValues[i2])) {
                        try {
                            value = Double.valueOf(measureValues[i2]).doubleValue();
                        } catch (Exception e) {
                            Logger.d("measure's value cannot convert to double. measurevalue:" + measureValues[i2], new Object[0]);
                        }
                    }
                    measureValueSet.setValue(measureNames[i2], value);
                }
            }
            commit(module, monitorPoint, dimensionValueSet, measureValueSet);
        }
    }

    public static void setStatisticsInterval(EventType eventType, final int statisticsInterval) {
        if (checkInit()) {
            final int event = getEvent(eventType);
            AnalyticsMgr.handler.postWatingTask(new Runnable() {
                public void run() {
                    try {
                        AnalyticsMgr.iAnalytics.setStatisticsInterval2(event, statisticsInterval);
                    } catch (RemoteException e) {
                        AnalyticsMgr.handleRemoteException(e);
                    }
                }
            });
        }
    }

    private static int getEvent(EventType eventType) {
        return eventType.getEventId();
    }

    /* access modifiers changed from: private */
    public static boolean checkInit() {
        if (!AnalyticsMgr.isInit) {
            Logger.d("AppMonitor", "Please call init() before call other method");
        }
        return AnalyticsMgr.isInit;
    }

    private static Runnable createRegisterTask(String module, String monitorPoint, MeasureSet measures, DimensionSet dimensions, boolean isCommitDetail) {
        final String str = module;
        final String str2 = monitorPoint;
        final MeasureSet measureSet = measures;
        final DimensionSet dimensionSet = dimensions;
        final boolean z = isCommitDetail;
        return new Runnable() {
            public void run() {
                try {
                    Logger.d("AppMonitor", "register stat event. module: ", str, " monitorPoint: ", str2);
                    AnalyticsMgr.iAnalytics.register4(str, str2, measureSet, dimensionSet, z);
                } catch (RemoteException e) {
                    AnalyticsMgr.handleRemoteException(e);
                }
            }
        };
    }
}
