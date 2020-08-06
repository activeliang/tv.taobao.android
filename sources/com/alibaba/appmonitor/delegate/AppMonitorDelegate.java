package com.alibaba.appmonitor.delegate;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.network.NetworkUtil;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorConfigMgr;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather;
import com.alibaba.analytics.core.selfmonitor.exception.AppMonitorException;
import com.alibaba.analytics.core.selfmonitor.exception.ExceptionEventBuilder;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.appmonitor.event.EventRepo;
import com.alibaba.appmonitor.event.EventType;
import com.alibaba.appmonitor.model.Metric;
import com.alibaba.appmonitor.model.MetricRepo;
import com.alibaba.appmonitor.offline.TempAlarm;
import com.alibaba.appmonitor.offline.TempCounter;
import com.alibaba.appmonitor.offline.TempEventMgr;
import com.alibaba.appmonitor.sample.AMSamplingMgr;
import com.alibaba.appmonitor.sample.SampleConfigConstant;
import com.alibaba.mtl.appmonitor.Transaction;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import com.ut.mini.core.sign.UTBaseRequestAuthentication;
import com.ut.mini.core.sign.UTSecurityThridRequestAuthentication;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public final class AppMonitorDelegate {
    public static boolean IS_DEBUG = false;
    private static final String TAG = "AppMonitorDelegate";
    private static Application application;
    public static SelfMonitorEventDispather mMonitor = new SelfMonitorEventDispather();
    static volatile boolean sdkInit = false;

    public static synchronized void init(Application application2) {
        synchronized (AppMonitorDelegate.class) {
            Logger.d(TAG, "start init");
            try {
                if (!sdkInit) {
                    application = application2;
                    CleanTask.init();
                    CommitTask.init();
                    BackgroundTrigger.init(application2);
                    sdkInit = true;
                }
            } catch (Throwable th) {
                destroy();
            }
        }
        return;
    }

    public static synchronized void destroy() {
        synchronized (AppMonitorDelegate.class) {
            try {
                Logger.d(TAG, "start destory");
                if (sdkInit) {
                    CommitTask.uploadAllEvent();
                    CommitTask.destroy();
                    CleanTask.destroy();
                    if (application != null) {
                        NetworkUtil.unRegister(application.getApplicationContext());
                    }
                    sdkInit = false;
                }
            } catch (Throwable t) {
                ExceptionEventBuilder.log(ExceptionEventBuilder.ExceptionType.AP, t);
            }
        }
        return;
    }

    public static synchronized void triggerUpload() {
        synchronized (AppMonitorDelegate.class) {
            try {
                Logger.d(TAG, "triggerUpload");
                if (sdkInit && Variables.isNotDisAM()) {
                    CommitTask.uploadAllEvent();
                }
            } catch (Throwable t) {
                ExceptionEventBuilder.log(ExceptionEventBuilder.ExceptionType.AP, t);
            }
        }
        return;
    }

    public static void setStatisticsInterval(int statisticsInterval) {
        for (EventType eventType : EventType.values()) {
            eventType.setStatisticsInterval(statisticsInterval);
            setStatisticsInterval(eventType, statisticsInterval);
        }
    }

    public static void setSampling(int sampling) {
        Logger.d(TAG, "[setSampling]");
        for (EventType eventType : EventType.values()) {
            eventType.setDefaultSampling(sampling);
            AMSamplingMgr.getInstance().setEventTypeSampling(eventType, sampling);
        }
    }

    public static void enableLog(boolean open) {
        Logger.d(TAG, "[enableLog]");
        Logger.setDebug(open);
    }

    public static void register(String module, String monitorPoint, MeasureSet measures) {
        register(module, monitorPoint, measures, (DimensionSet) null);
    }

    public static void register(String module, String monitorPoint, MeasureSet measures, boolean isCommitDetail) {
        register(module, monitorPoint, measures, (DimensionSet) null, isCommitDetail);
    }

    public static void register(String module, String monitorPoint, MeasureSet measures, DimensionSet dimensions) {
        register(module, monitorPoint, measures, dimensions, false);
    }

    public static void register(String module, String monitorPoint, MeasureSet measures, DimensionSet dimensions, boolean isCommitDetail) {
        try {
            if (!sdkInit) {
                return;
            }
            if (StringUtils.isBlank(module) || StringUtils.isBlank(monitorPoint)) {
                Logger.d(TAG, "register stat event. module: ", module, " monitorPoint: ", monitorPoint);
                if (IS_DEBUG) {
                    throw new AppMonitorException("register error. module and monitorPoint can't be null");
                }
                return;
            }
            Metric metric = new Metric(module, monitorPoint, measures, dimensions, isCommitDetail);
            MetricRepo.getRepo().add(metric);
            TempEventMgr.getInstance().add(metric);
            MeasureSet configMeasureSet = SelfMonitorConfigMgr.getInstance().getMeasureSet(module, monitorPoint);
            if (configMeasureSet != null) {
                MetricRepo.getRepo().add(new Metric(module + "_abtest", monitorPoint, configMeasureSet, dimensions, false));
            }
        } catch (Throwable t) {
            ExceptionEventBuilder.log(ExceptionEventBuilder.ExceptionType.AP, t);
        }
    }

    public static void updateMeasure(String module, String monitorPoint, String measureName, double min, double max, double defaultValue) {
        Metric metric;
        Logger.d(TAG, "[updateMeasure]");
        try {
            if (sdkInit && !StringUtils.isBlank(module) && !StringUtils.isBlank(monitorPoint) && (metric = MetricRepo.getRepo().getMetric(module, monitorPoint)) != null && metric.getMeasureSet() != null) {
                metric.getMeasureSet().upateMeasure(new Measure(measureName, Double.valueOf(defaultValue), Double.valueOf(min), Double.valueOf(max)));
            }
        } catch (Exception e) {
        }
    }

    public static class Alarm {
        public static void setStatisticsInterval(int statisticsInterval) {
            EventType.ALARM.setStatisticsInterval(statisticsInterval);
            AppMonitorDelegate.setStatisticsInterval(EventType.ALARM, statisticsInterval);
        }

        public static void setSampling(int sampling) {
            AMSamplingMgr.getInstance().setEventTypeSampling(EventType.ALARM, sampling);
        }

        @Deprecated
        public static boolean checkSampled(String module, String monitorPoint) {
            return AMSamplingMgr.getInstance().checkAlarmSampled(module, monitorPoint, true, (Map<String, String>) null);
        }

        public static void commitSuccess(String module, String monitorPoint) {
            commitSuccess(module, monitorPoint, (String) null);
        }

        public static void commitSuccess(String module, String monitorPoint, String arg) {
            try {
                if (TextUtils.isEmpty(module) || TextUtils.isEmpty(monitorPoint)) {
                    Logger.w(AppMonitorDelegate.TAG, "module & monitorPoint must not null");
                    return;
                }
                if (SelfMonitorConfigMgr.getInstance().isNeedMonitorForAM(EventType.ALARM, module, monitorPoint)) {
                    AppMonitorDelegate.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.INTERFACE, EventType.ALARM + SymbolExpUtil.SYMBOL_COLON + module + SymbolExpUtil.SYMBOL_COLON + monitorPoint, Double.valueOf(1.0d)));
                }
                if (!AppMonitorDelegate.sdkInit || !Variables.isNotDisAM() || !EventType.ALARM.isOpen() || (!AppMonitorDelegate.IS_DEBUG && !AMSamplingMgr.getInstance().checkAlarmSampled(module, monitorPoint, true, (Map<String, String>) null))) {
                    Logger.d("log discard !", "module", module, SampleConfigConstant.MONITORPOINT, monitorPoint, "arg", arg);
                    return;
                }
                Logger.d("commitSuccess", "module", module, SampleConfigConstant.MONITORPOINT, monitorPoint, "arg", arg);
                if (AMSamplingMgr.getInstance().isOffline(EventType.ALARM, module, monitorPoint)) {
                    Context context = Variables.getInstance().getContext();
                    String offlineModule = module;
                    if (SelfMonitorConfigMgr.getInstance().isNeedMonitorForOffline(EventType.ALARM, module, monitorPoint)) {
                        offlineModule = module + "_abtest";
                        EventRepo.getRepo().alarmEventSuccessIncr(EventType.ALARM.getEventId(), module, monitorPoint, arg);
                    }
                    TempEventMgr.getInstance().add(EventType.ALARM, new TempAlarm(offlineModule, monitorPoint, arg, (String) null, (String) null, true, NetworkUtil.getAccess(context), NetworkUtil.getAccsssSubType(context)));
                    return;
                }
                EventRepo.getRepo().alarmEventSuccessIncr(EventType.ALARM.getEventId(), module, monitorPoint, arg);
            } catch (Throwable t) {
                ExceptionEventBuilder.log(ExceptionEventBuilder.ExceptionType.AP, t);
            }
        }

        public static void commitFail(String module, String monitorPoint, String errorCode, String errorMsg) {
            commitFail(module, monitorPoint, (String) null, errorCode, errorMsg);
        }

        public static void commitFail(String module, String monitorPoint, String arg, String errorCode, String errorMsg) {
            try {
                if (TextUtils.isEmpty(module) || TextUtils.isEmpty(monitorPoint)) {
                    Logger.w(AppMonitorDelegate.TAG, "module & monitorPoint must not null");
                    return;
                }
                if (SelfMonitorConfigMgr.getInstance().isNeedMonitorForAM(EventType.ALARM, module, monitorPoint)) {
                    AppMonitorDelegate.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.INTERFACE, EventType.ALARM + SymbolExpUtil.SYMBOL_COLON + module + SymbolExpUtil.SYMBOL_COLON + monitorPoint, Double.valueOf(1.0d)));
                }
                Map<String, String> extras = new HashMap<>();
                extras.put("_status", "0");
                if (!AppMonitorDelegate.sdkInit || !Variables.isNotDisAM() || !EventType.ALARM.isOpen() || (!AppMonitorDelegate.IS_DEBUG && !AMSamplingMgr.getInstance().checkAlarmSampled(module, monitorPoint, false, extras))) {
                    Logger.d("log discard !", "module", module, SampleConfigConstant.MONITORPOINT, monitorPoint, "errorCode:", errorCode, "errorMsg:", errorMsg);
                    return;
                }
                Logger.d("commitFail ", "module", module, SampleConfigConstant.MONITORPOINT, monitorPoint, "errorCode:", errorCode, "errorMsg:", errorMsg);
                if (AMSamplingMgr.getInstance().isOffline(EventType.ALARM, module, monitorPoint)) {
                    String offlineModule = module;
                    if (SelfMonitorConfigMgr.getInstance().isNeedMonitorForOffline(EventType.ALARM, module, monitorPoint)) {
                        EventRepo.getRepo().alarmEventFailIncr(EventType.ALARM.getEventId(), module, monitorPoint, arg, errorCode, errorMsg);
                        offlineModule = module + "_abtest";
                    }
                    Context context = Variables.getInstance().getContext();
                    TempEventMgr.getInstance().add(EventType.ALARM, new TempAlarm(offlineModule, monitorPoint, arg, errorCode, errorMsg, false, NetworkUtil.getAccess(context), NetworkUtil.getAccsssSubType(context)));
                    return;
                }
                EventRepo.getRepo().alarmEventFailIncr(EventType.ALARM.getEventId(), module, monitorPoint, arg, errorCode, errorMsg);
            } catch (Throwable t) {
                ExceptionEventBuilder.log(ExceptionEventBuilder.ExceptionType.AP, t);
            }
        }
    }

    public static class Counter {
        public static void setStatisticsInterval(int statisticsInterval) {
            EventType.COUNTER.setStatisticsInterval(statisticsInterval);
            AppMonitorDelegate.setStatisticsInterval(EventType.COUNTER, statisticsInterval);
        }

        public static void setSampling(int sampling) {
            AMSamplingMgr.getInstance().setEventTypeSampling(EventType.COUNTER, sampling);
        }

        @Deprecated
        public static boolean checkSampled(String module, String monitorPoint) {
            return AMSamplingMgr.getInstance().checkSampled(EventType.COUNTER, module, monitorPoint);
        }

        public static void commit(String module, String monitorPoint, double value) {
            commit(module, monitorPoint, (String) null, value);
        }

        public static void commit(String module, String monitorPoint, String arg, double value) {
            try {
                if (TextUtils.isEmpty(module) || TextUtils.isEmpty(monitorPoint)) {
                    Logger.w(AppMonitorDelegate.TAG, "module & monitorPoint must not null");
                    return;
                }
                if (SelfMonitorConfigMgr.getInstance().isNeedMonitorForAM(EventType.COUNTER, module, monitorPoint)) {
                    AppMonitorDelegate.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.INTERFACE, EventType.COUNTER + SymbolExpUtil.SYMBOL_COLON + module + SymbolExpUtil.SYMBOL_COLON + monitorPoint, Double.valueOf(1.0d)));
                }
                if (!AppMonitorDelegate.sdkInit || !Variables.isNotDisAM() || !EventType.COUNTER.isOpen() || (!AppMonitorDelegate.IS_DEBUG && !AMSamplingMgr.getInstance().checkSampled(EventType.COUNTER, module, monitorPoint))) {
                    Logger.d("log discard !", "module", module, SampleConfigConstant.MONITORPOINT, monitorPoint, "args", arg, "value", Double.valueOf(value));
                    return;
                }
                Logger.d("commitCount", "module", module, SampleConfigConstant.MONITORPOINT, monitorPoint, "args", arg, "value", Double.valueOf(value));
                if (AMSamplingMgr.getInstance().isOffline(EventType.COUNTER, module, monitorPoint)) {
                    Context context = Variables.getInstance().getContext();
                    String offlineModule = module;
                    if (SelfMonitorConfigMgr.getInstance().isNeedMonitorForOffline(EventType.COUNTER, module, monitorPoint)) {
                        EventRepo.getRepo().countEventCommit(EventType.COUNTER.getEventId(), module, monitorPoint, arg, value);
                        offlineModule = module + "_abtest";
                    }
                    TempEventMgr.getInstance().add(EventType.COUNTER, new TempCounter(offlineModule, monitorPoint, arg, value, NetworkUtil.getAccess(context), NetworkUtil.getAccsssSubType(context)));
                    return;
                }
                EventRepo.getRepo().countEventCommit(EventType.COUNTER.getEventId(), module, monitorPoint, arg, value);
            } catch (Throwable t) {
                ExceptionEventBuilder.log(ExceptionEventBuilder.ExceptionType.AP, t);
            }
        }
    }

    public static class OffLineCounter {
        public static void setStatisticsInterval(int statisticsInterval) {
            EventType.COUNTER.setStatisticsInterval(statisticsInterval);
            AppMonitorDelegate.setStatisticsInterval(EventType.COUNTER, statisticsInterval);
        }

        public static void setSampling(int sampling) {
            AMSamplingMgr.getInstance().setEventTypeSampling(EventType.COUNTER, sampling);
        }

        @Deprecated
        public static boolean checkSampled(String module, String monitorPoint) {
            return AMSamplingMgr.getInstance().checkSampled(EventType.COUNTER, module, monitorPoint);
        }

        public static void commit(String module, String monitorPoint, double value) {
            try {
                if (TextUtils.isEmpty(module) || TextUtils.isEmpty(monitorPoint)) {
                    Logger.w(AppMonitorDelegate.TAG, "module & monitorPoint must not null");
                } else if (!AppMonitorDelegate.sdkInit || !Variables.isNotDisAM() || !EventType.COUNTER.isOpen() || (!AppMonitorDelegate.IS_DEBUG && !AMSamplingMgr.getInstance().checkSampled(EventType.COUNTER, module, monitorPoint))) {
                    Logger.d("log discard !", "");
                } else {
                    Logger.d("commitOffLineCount", "module", module, SampleConfigConstant.MONITORPOINT, monitorPoint, "value", Double.valueOf(value));
                    EventRepo.getRepo().countEventCommit(EventType.COUNTER.getEventId(), module, monitorPoint, (String) null, value);
                }
            } catch (Throwable t) {
                ExceptionEventBuilder.log(ExceptionEventBuilder.ExceptionType.AP, t);
            }
        }
    }

    public static class Stat {
        public static void setStatisticsInterval(int statisticsInterval) {
            EventType.STAT.setStatisticsInterval(statisticsInterval);
            AppMonitorDelegate.setStatisticsInterval(EventType.STAT, statisticsInterval);
        }

        public static void setSampling(int sampling) {
            AMSamplingMgr.getInstance().setEventTypeSampling(EventType.STAT, sampling);
        }

        @Deprecated
        public static boolean checkSampled(String module, String monitorPoint) {
            return AMSamplingMgr.getInstance().checkSampled(EventType.STAT, module, monitorPoint);
        }

        public static void begin(String module, String monitorPoint, String measureName) {
            try {
                if (!AppMonitorDelegate.sdkInit || !Variables.isNotDisAM() || !EventType.STAT.isOpen() || (!AppMonitorDelegate.IS_DEBUG && !AMSamplingMgr.getInstance().checkSampled(EventType.STAT, module, monitorPoint))) {
                    Logger.d("log discard !", "");
                    return;
                }
                Logger.d(AppMonitorDelegate.TAG, "statEvent begin. module: ", module, " monitorPoint: ", monitorPoint, " measureName: ", measureName);
                EventRepo.getRepo().beginStatEvent(Integer.valueOf(EventType.STAT.getEventId()), module, monitorPoint, measureName);
            } catch (Throwable t) {
                ExceptionEventBuilder.log(ExceptionEventBuilder.ExceptionType.AP, t);
            }
        }

        public static void end(String module, String monitorPoint, String measureName) {
            try {
                if (!AppMonitorDelegate.sdkInit || !Variables.isNotDisAM() || !EventType.STAT.isOpen() || (!AppMonitorDelegate.IS_DEBUG && !AMSamplingMgr.getInstance().checkSampled(EventType.STAT, module, monitorPoint))) {
                    Logger.d("log discard !", " module ", module, SampleConfigConstant.MONITORPOINT, monitorPoint, " measureName", measureName);
                    return;
                }
                Logger.d("statEvent end. module: ", module, " monitorPoint: ", monitorPoint, " measureName: ", measureName);
                EventRepo.getRepo().endStatEvent(module, monitorPoint, measureName);
            } catch (Throwable t) {
                ExceptionEventBuilder.log(ExceptionEventBuilder.ExceptionType.AP, t);
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

        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0027, code lost:
            if (com.alibaba.appmonitor.sample.AMSamplingMgr.getInstance().checkSampled(com.alibaba.appmonitor.event.EventType.STAT, r11, r12, r13 != null ? r13.getMap() : null) != false) goto L_0x0029;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static void commit(java.lang.String r11, java.lang.String r12, com.alibaba.mtl.appmonitor.model.DimensionValueSet r13, double r14) {
            /*
                r9 = 1
                boolean r5 = com.alibaba.appmonitor.delegate.AppMonitorDelegate.sdkInit     // Catch:{ Throwable -> 0x0090 }
                if (r5 == 0) goto L_0x0080
                boolean r5 = com.alibaba.analytics.core.Variables.isNotDisAM()     // Catch:{ Throwable -> 0x0090 }
                if (r5 == 0) goto L_0x0080
                com.alibaba.appmonitor.event.EventType r5 = com.alibaba.appmonitor.event.EventType.STAT     // Catch:{ Throwable -> 0x0090 }
                boolean r5 = r5.isOpen()     // Catch:{ Throwable -> 0x0090 }
                if (r5 == 0) goto L_0x0080
                boolean r5 = com.alibaba.appmonitor.delegate.AppMonitorDelegate.IS_DEBUG     // Catch:{ Throwable -> 0x0090 }
                if (r5 != 0) goto L_0x0029
                com.alibaba.appmonitor.sample.AMSamplingMgr r6 = com.alibaba.appmonitor.sample.AMSamplingMgr.getInstance()     // Catch:{ Throwable -> 0x0090 }
                com.alibaba.appmonitor.event.EventType r7 = com.alibaba.appmonitor.event.EventType.STAT     // Catch:{ Throwable -> 0x0090 }
                if (r13 == 0) goto L_0x007e
                java.util.Map r5 = r13.getMap()     // Catch:{ Throwable -> 0x0090 }
            L_0x0023:
                boolean r5 = r6.checkSampled(r7, r11, r12, r5)     // Catch:{ Throwable -> 0x0090 }
                if (r5 == 0) goto L_0x0080
            L_0x0029:
                java.lang.String r5 = "AppMonitorDelegate"
                r6 = 4
                java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x0090 }
                r7 = 0
                java.lang.String r8 = "statEvent commit. module: "
                r6[r7] = r8     // Catch:{ Throwable -> 0x0090 }
                r7 = 1
                r6[r7] = r11     // Catch:{ Throwable -> 0x0090 }
                r7 = 2
                java.lang.String r8 = " monitorPoint: "
                r6[r7] = r8     // Catch:{ Throwable -> 0x0090 }
                r7 = 3
                r6[r7] = r12     // Catch:{ Throwable -> 0x0090 }
                com.alibaba.analytics.utils.Logger.d((java.lang.String) r5, (java.lang.Object[]) r6)     // Catch:{ Throwable -> 0x0090 }
                com.alibaba.appmonitor.model.MetricRepo r5 = com.alibaba.appmonitor.model.MetricRepo.getRepo()     // Catch:{ Throwable -> 0x0090 }
                com.alibaba.appmonitor.model.Metric r3 = r5.getMetric(r11, r12)     // Catch:{ Throwable -> 0x0090 }
                if (r3 == 0) goto L_0x007d
                com.alibaba.mtl.appmonitor.model.MeasureSet r5 = r3.getMeasureSet()     // Catch:{ Throwable -> 0x0090 }
                java.util.List r2 = r5.getMeasures()     // Catch:{ Throwable -> 0x0090 }
                int r5 = r2.size()     // Catch:{ Throwable -> 0x0090 }
                if (r5 != r9) goto L_0x007d
                r5 = 0
                java.lang.Object r5 = r2.get(r5)     // Catch:{ Throwable -> 0x0090 }
                com.alibaba.mtl.appmonitor.model.Measure r5 = (com.alibaba.mtl.appmonitor.model.Measure) r5     // Catch:{ Throwable -> 0x0090 }
                java.lang.String r0 = r5.getName()     // Catch:{ Throwable -> 0x0090 }
                com.alibaba.appmonitor.pool.BalancedPool r5 = com.alibaba.appmonitor.pool.BalancedPool.getInstance()     // Catch:{ Throwable -> 0x0090 }
                java.lang.Class<com.alibaba.mtl.appmonitor.model.MeasureValueSet> r6 = com.alibaba.mtl.appmonitor.model.MeasureValueSet.class
                r7 = 0
                java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Throwable -> 0x0090 }
                com.alibaba.appmonitor.pool.Reusable r5 = r5.poll(r6, r7)     // Catch:{ Throwable -> 0x0090 }
                com.alibaba.mtl.appmonitor.model.MeasureValueSet r5 = (com.alibaba.mtl.appmonitor.model.MeasureValueSet) r5     // Catch:{ Throwable -> 0x0090 }
                com.alibaba.mtl.appmonitor.model.MeasureValueSet r1 = r5.setValue((java.lang.String) r0, (double) r14)     // Catch:{ Throwable -> 0x0090 }
                commit((java.lang.String) r11, (java.lang.String) r12, (com.alibaba.mtl.appmonitor.model.DimensionValueSet) r13, (com.alibaba.mtl.appmonitor.model.MeasureValueSet) r1)     // Catch:{ Throwable -> 0x0090 }
            L_0x007d:
                return
            L_0x007e:
                r5 = 0
                goto L_0x0023
            L_0x0080:
                java.lang.String r5 = "log discard !"
                r6 = 1
                java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x0090 }
                r7 = 0
                java.lang.String r8 = ""
                r6[r7] = r8     // Catch:{ Throwable -> 0x0090 }
                com.alibaba.analytics.utils.Logger.d((java.lang.String) r5, (java.lang.Object[]) r6)     // Catch:{ Throwable -> 0x0090 }
                goto L_0x007d
            L_0x0090:
                r4 = move-exception
                com.alibaba.analytics.core.selfmonitor.exception.ExceptionEventBuilder$ExceptionType r5 = com.alibaba.analytics.core.selfmonitor.exception.ExceptionEventBuilder.ExceptionType.AP
                com.alibaba.analytics.core.selfmonitor.exception.ExceptionEventBuilder.log(r5, r4)
                goto L_0x007d
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.appmonitor.delegate.AppMonitorDelegate.Stat.commit(java.lang.String, java.lang.String, com.alibaba.mtl.appmonitor.model.DimensionValueSet, double):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0068, code lost:
            if (com.alibaba.appmonitor.sample.AMSamplingMgr.getInstance().checkSampled(com.alibaba.appmonitor.event.EventType.STAT, r11, r12, r13 != null ? r13.getMap() : null) != false) goto L_0x006a;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static void commit(java.lang.String r11, java.lang.String r12, com.alibaba.mtl.appmonitor.model.DimensionValueSet r13, com.alibaba.mtl.appmonitor.model.MeasureValueSet r14) {
            /*
                com.alibaba.analytics.core.selfmonitor.SelfMonitorConfigMgr r0 = com.alibaba.analytics.core.selfmonitor.SelfMonitorConfigMgr.getInstance()     // Catch:{ Throwable -> 0x00f6 }
                com.alibaba.appmonitor.event.EventType r1 = com.alibaba.appmonitor.event.EventType.STAT     // Catch:{ Throwable -> 0x00f6 }
                boolean r0 = r0.isNeedMonitorForAM(r1, r11, r12)     // Catch:{ Throwable -> 0x00f6 }
                if (r0 == 0) goto L_0x0042
                com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather r0 = com.alibaba.appmonitor.delegate.AppMonitorDelegate.mMonitor     // Catch:{ Throwable -> 0x00f6 }
                int r1 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.INTERFACE     // Catch:{ Throwable -> 0x00f6 }
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00f6 }
                r2.<init>()     // Catch:{ Throwable -> 0x00f6 }
                com.alibaba.appmonitor.event.EventType r3 = com.alibaba.appmonitor.event.EventType.STAT     // Catch:{ Throwable -> 0x00f6 }
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x00f6 }
                java.lang.String r3 = ":"
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x00f6 }
                java.lang.StringBuilder r2 = r2.append(r11)     // Catch:{ Throwable -> 0x00f6 }
                java.lang.String r3 = ":"
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x00f6 }
                java.lang.StringBuilder r2 = r2.append(r12)     // Catch:{ Throwable -> 0x00f6 }
                java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x00f6 }
                r4 = 4607182418800017408(0x3ff0000000000000, double:1.0)
                java.lang.Double r3 = java.lang.Double.valueOf(r4)     // Catch:{ Throwable -> 0x00f6 }
                com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent r1 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.buildCountEvent(r1, r2, r3)     // Catch:{ Throwable -> 0x00f6 }
                r0.onEvent(r1)     // Catch:{ Throwable -> 0x00f6 }
            L_0x0042:
                boolean r0 = com.alibaba.appmonitor.delegate.AppMonitorDelegate.sdkInit     // Catch:{ Throwable -> 0x00f6 }
                if (r0 == 0) goto L_0x00fd
                boolean r0 = com.alibaba.analytics.core.Variables.isNotDisAM()     // Catch:{ Throwable -> 0x00f6 }
                if (r0 == 0) goto L_0x00fd
                com.alibaba.appmonitor.event.EventType r0 = com.alibaba.appmonitor.event.EventType.STAT     // Catch:{ Throwable -> 0x00f6 }
                boolean r0 = r0.isOpen()     // Catch:{ Throwable -> 0x00f6 }
                if (r0 == 0) goto L_0x00fd
                boolean r0 = com.alibaba.appmonitor.delegate.AppMonitorDelegate.IS_DEBUG     // Catch:{ Throwable -> 0x00f6 }
                if (r0 != 0) goto L_0x006a
                com.alibaba.appmonitor.sample.AMSamplingMgr r1 = com.alibaba.appmonitor.sample.AMSamplingMgr.getInstance()     // Catch:{ Throwable -> 0x00f6 }
                com.alibaba.appmonitor.event.EventType r2 = com.alibaba.appmonitor.event.EventType.STAT     // Catch:{ Throwable -> 0x00f6 }
                if (r13 == 0) goto L_0x00b4
                java.util.Map r0 = r13.getMap()     // Catch:{ Throwable -> 0x00f6 }
            L_0x0064:
                boolean r0 = r1.checkSampled(r2, r11, r12, r0)     // Catch:{ Throwable -> 0x00f6 }
                if (r0 == 0) goto L_0x00fd
            L_0x006a:
                java.lang.String r0 = "statEvent commit"
                r1 = 4
                java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x00f6 }
                r2 = 0
                java.lang.String r3 = "module"
                r1[r2] = r3     // Catch:{ Throwable -> 0x00f6 }
                r2 = 1
                r1[r2] = r11     // Catch:{ Throwable -> 0x00f6 }
                r2 = 2
                java.lang.String r3 = "monitorPoint"
                r1[r2] = r3     // Catch:{ Throwable -> 0x00f6 }
                r2 = 3
                r1[r2] = r12     // Catch:{ Throwable -> 0x00f6 }
                com.alibaba.analytics.utils.Logger.d((java.lang.String) r0, (java.lang.Object[]) r1)     // Catch:{ Throwable -> 0x00f6 }
                com.alibaba.appmonitor.sample.AMSamplingMgr r0 = com.alibaba.appmonitor.sample.AMSamplingMgr.getInstance()     // Catch:{ Throwable -> 0x00f6 }
                com.alibaba.appmonitor.event.EventType r1 = com.alibaba.appmonitor.event.EventType.STAT     // Catch:{ Throwable -> 0x00f6 }
                boolean r0 = r0.isOffline(r1, r11, r12)     // Catch:{ Throwable -> 0x00f6 }
                if (r0 == 0) goto L_0x00b6
                com.alibaba.analytics.core.Variables r0 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ Throwable -> 0x00f6 }
                android.content.Context r7 = r0.getContext()     // Catch:{ Throwable -> 0x00f6 }
                com.alibaba.appmonitor.offline.TempEventMgr r9 = com.alibaba.appmonitor.offline.TempEventMgr.getInstance()     // Catch:{ Throwable -> 0x00f6 }
                com.alibaba.appmonitor.event.EventType r10 = com.alibaba.appmonitor.event.EventType.STAT     // Catch:{ Throwable -> 0x00f6 }
                com.alibaba.appmonitor.offline.TempStat r0 = new com.alibaba.appmonitor.offline.TempStat     // Catch:{ Throwable -> 0x00f6 }
                java.lang.String r5 = com.alibaba.analytics.core.network.NetworkUtil.getAccess(r7)     // Catch:{ Throwable -> 0x00f6 }
                java.lang.String r6 = com.alibaba.analytics.core.network.NetworkUtil.getAccsssSubType(r7)     // Catch:{ Throwable -> 0x00f6 }
                r1 = r11
                r2 = r12
                r3 = r13
                r4 = r14
                r0.<init>(r1, r2, r3, r4, r5, r6)     // Catch:{ Throwable -> 0x00f6 }
                r9.add(r10, r0)     // Catch:{ Throwable -> 0x00f6 }
            L_0x00b3:
                return
            L_0x00b4:
                r0 = 0
                goto L_0x0064
            L_0x00b6:
                com.alibaba.appmonitor.event.EventRepo r0 = com.alibaba.appmonitor.event.EventRepo.getRepo()     // Catch:{ Throwable -> 0x00f6 }
                com.alibaba.appmonitor.event.EventType r1 = com.alibaba.appmonitor.event.EventType.STAT     // Catch:{ Throwable -> 0x00f6 }
                int r1 = r1.getEventId()     // Catch:{ Throwable -> 0x00f6 }
                r2 = r11
                r3 = r12
                r4 = r14
                r5 = r13
                r0.commitStatEvent(r1, r2, r3, r4, r5)     // Catch:{ Throwable -> 0x00f6 }
                com.alibaba.analytics.core.selfmonitor.SelfMonitorConfigMgr r0 = com.alibaba.analytics.core.selfmonitor.SelfMonitorConfigMgr.getInstance()     // Catch:{ Throwable -> 0x00f6 }
                boolean r0 = r0.isNeedMonitorForBucket(r11, r12)     // Catch:{ Throwable -> 0x00f6 }
                if (r0 == 0) goto L_0x00b3
                com.alibaba.appmonitor.event.EventRepo r0 = com.alibaba.appmonitor.event.EventRepo.getRepo()     // Catch:{ Throwable -> 0x00f6 }
                com.alibaba.appmonitor.event.EventType r1 = com.alibaba.appmonitor.event.EventType.STAT     // Catch:{ Throwable -> 0x00f6 }
                int r1 = r1.getEventId()     // Catch:{ Throwable -> 0x00f6 }
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00f6 }
                r2.<init>()     // Catch:{ Throwable -> 0x00f6 }
                java.lang.StringBuilder r2 = r2.append(r11)     // Catch:{ Throwable -> 0x00f6 }
                java.lang.String r3 = "_abtest"
                java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x00f6 }
                java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x00f6 }
                r3 = r12
                r4 = r14
                r5 = r13
                r0.commitStatEvent(r1, r2, r3, r4, r5)     // Catch:{ Throwable -> 0x00f6 }
                goto L_0x00b3
            L_0x00f6:
                r8 = move-exception
                com.alibaba.analytics.core.selfmonitor.exception.ExceptionEventBuilder$ExceptionType r0 = com.alibaba.analytics.core.selfmonitor.exception.ExceptionEventBuilder.ExceptionType.AP
                com.alibaba.analytics.core.selfmonitor.exception.ExceptionEventBuilder.log(r0, r8)
                goto L_0x00b3
            L_0x00fd:
                java.lang.String r0 = "log discard !"
                r1 = 4
                java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x00f6 }
                r2 = 0
                java.lang.String r3 = "module"
                r1[r2] = r3     // Catch:{ Throwable -> 0x00f6 }
                r2 = 1
                r1[r2] = r11     // Catch:{ Throwable -> 0x00f6 }
                r2 = 2
                java.lang.String r3 = "monitorPoint"
                r1[r2] = r3     // Catch:{ Throwable -> 0x00f6 }
                r2 = 3
                r1[r2] = r12     // Catch:{ Throwable -> 0x00f6 }
                com.alibaba.analytics.utils.Logger.d((java.lang.String) r0, (java.lang.Object[]) r1)     // Catch:{ Throwable -> 0x00f6 }
                goto L_0x00b3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.appmonitor.delegate.AppMonitorDelegate.Stat.commit(java.lang.String, java.lang.String, com.alibaba.mtl.appmonitor.model.DimensionValueSet, com.alibaba.mtl.appmonitor.model.MeasureValueSet):void");
        }
    }

    public static void setStatisticsInterval(EventType eventType, int statisticsInterval) {
        try {
            if (sdkInit && eventType != null) {
                CommitTask.setStatisticsInterval(eventType.getEventId(), statisticsInterval);
                if (statisticsInterval > 0) {
                    eventType.setOpen(true);
                } else {
                    eventType.setOpen(false);
                }
            }
        } catch (Throwable t) {
            ExceptionEventBuilder.log(ExceptionEventBuilder.ExceptionType.AP, t);
        }
    }

    public static void setRequestAuthInfo(boolean isSecurity, boolean isEncode, String appkey, String secret) {
        IUTRequestAuthentication requestAuth;
        if (isSecurity) {
            requestAuth = new UTSecurityThridRequestAuthentication(appkey, secret);
        } else {
            requestAuth = new UTBaseRequestAuthentication(appkey, secret, isEncode);
        }
        Variables.getInstance().setRequestAuthenticationInstance(requestAuth);
    }

    public static void setChannel(String channel) {
        Variables.getInstance().setChannel(channel);
    }
}
