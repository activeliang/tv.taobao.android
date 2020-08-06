package com.alibaba.appmonitor.event;

import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.network.NetworkUtil;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import com.alibaba.appmonitor.model.Metric;
import com.alibaba.appmonitor.model.MetricRepo;
import com.alibaba.appmonitor.model.MetricValueSet;
import com.alibaba.appmonitor.model.UTDimensionValueSet;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.appmonitor.util.UTUtil;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EventRepo {
    private static final String TAG = "EventRepo";
    private static final String TAG_COMMIT_DAY = "commitDay";
    private static EventRepo eventRepo;
    private Map<String, DurationEvent> durationEventMap = new ConcurrentHashMap();
    private Map<UTDimensionValueSet, MetricValueSet> eventMap = new ConcurrentHashMap();
    private AtomicInteger mAlarmCounter = new AtomicInteger(0);
    private AtomicInteger mCountCounter = new AtomicInteger(0);
    private AtomicInteger mSTATCounter = new AtomicInteger(0);
    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd");

    public static synchronized EventRepo getRepo() {
        EventRepo eventRepo2;
        synchronized (EventRepo.class) {
            if (eventRepo == null) {
                eventRepo = new EventRepo();
            }
            eventRepo2 = eventRepo;
        }
        return eventRepo2;
    }

    private EventRepo() {
    }

    private UTDimensionValueSet fetchUTDimensionValues(int eventId, Long commitTime, String access, String subAccessType) {
        UTDimensionValueSet values = (UTDimensionValueSet) BalancedPool.getInstance().poll(UTDimensionValueSet.class, new Object[0]);
        if (TextUtils.isEmpty(access) || TextUtils.isEmpty(subAccessType)) {
            values.setValue(LogField.ACCESS.toString(), NetworkUtil.getAccess(Variables.getInstance().getContext()));
            values.setValue(LogField.ACCESS_SUBTYPE.toString(), NetworkUtil.getAccsssSubType(Variables.getInstance().getContext()));
        } else {
            values.setValue(LogField.ACCESS.toString(), access);
            values.setValue(LogField.ACCESS_SUBTYPE.toString(), subAccessType);
        }
        values.setValue(LogField.USERID.toString(), Variables.getInstance().getUserid());
        values.setValue(LogField.USERNICK.toString(), Variables.getInstance().getUsernick());
        values.setValue(LogField.EVENTID.toString(), String.valueOf(eventId));
        if (commitTime == null) {
            commitTime = Long.valueOf(System.currentTimeMillis() / 1000);
        }
        values.setValue(TAG_COMMIT_DAY, this.mSdf.format(new Date(commitTime.longValue() * 1000)));
        return values;
    }

    public void alarmEventSuccessIncr(int eventId, String module, String monitorPoint, String arg) {
        alarmEventSuccessIncr(eventId, module, monitorPoint, arg, (Long) null, (String) null, (String) null);
    }

    public void alarmEventSuccessIncr(int eventId, String module, String monitorPoint, String arg, Long commitTime, String access, String subAccessType) {
        UTDimensionValueSet utDimensionValues = fetchUTDimensionValues(eventId, commitTime, access, subAccessType);
        AlarmEvent alarmEvent = (AlarmEvent) getEvent(utDimensionValues, module, monitorPoint, arg, AlarmEvent.class);
        if (alarmEvent != null) {
            alarmEvent.incrSuccess(commitTime);
        }
        if (Variables.getInstance().isApRealTimeDebugging()) {
            AlarmEvent event = (AlarmEvent) BalancedPool.getInstance().poll(AlarmEvent.class, Integer.valueOf(eventId), module, monitorPoint, arg);
            event.incrSuccess(commitTime);
            UTUtil.sendRealDebugEvent(utDimensionValues, event);
        }
        checkUploadEvent(EventType.getEventType(eventId), this.mAlarmCounter);
    }

    public void alarmEventFailIncr(int eventId, String module, String monitorPoint, String arg, String errorCode, String errorMsg) {
        alarmEventFailIncr(eventId, module, monitorPoint, arg, errorCode, errorMsg, (Long) null, (String) null, (String) null);
    }

    public void alarmEventFailIncr(int eventId, String module, String monitorPoint, String arg, String errorCode, String errorMsg, Long commitTime, String access, String subAccessType) {
        UTDimensionValueSet utDimensionValues = fetchUTDimensionValues(eventId, commitTime, access, subAccessType);
        AlarmEvent alarmEvent = (AlarmEvent) getEvent(utDimensionValues, module, monitorPoint, arg, AlarmEvent.class);
        if (alarmEvent != null) {
            alarmEvent.incrFail(commitTime);
            alarmEvent.addError(errorCode, errorMsg);
        }
        if (Variables.getInstance().isApRealTimeDebugging()) {
            AlarmEvent event = (AlarmEvent) BalancedPool.getInstance().poll(AlarmEvent.class, Integer.valueOf(eventId), module, monitorPoint, arg);
            event.incrFail(commitTime);
            event.addError(errorCode, errorMsg);
            UTUtil.sendRealDebugEvent(utDimensionValues, event);
        }
        checkUploadEvent(EventType.getEventType(eventId), this.mAlarmCounter);
    }

    public void countEventCommit(int eventId, String module, String monitorPoint, String arg, double value) {
        countEventCommit(eventId, module, monitorPoint, arg, value, (Long) null, (String) null, (String) null);
    }

    public void countEventCommit(int eventId, String module, String monitorPoint, String arg, double value, Long commitTime, String access, String subAccessType) {
        UTDimensionValueSet utDimensionValues = fetchUTDimensionValues(eventId, commitTime, access, subAccessType);
        CountEvent countEvent = (CountEvent) getEvent(utDimensionValues, module, monitorPoint, arg, CountEvent.class);
        if (countEvent != null) {
            countEvent.addValue(value, commitTime);
        }
        if (Variables.getInstance().isApRealTimeDebugging()) {
            CountEvent event = (CountEvent) BalancedPool.getInstance().poll(CountEvent.class, Integer.valueOf(eventId), module, monitorPoint, arg);
            event.addValue(value, commitTime);
            UTUtil.sendRealDebugEvent(utDimensionValues, event);
        }
        checkUploadEvent(EventType.getEventType(eventId), this.mCountCounter);
    }

    public void commitStatEvent(int eventId, String module, String monitorPoint, MeasureValueSet measureValues, DimensionValueSet dimensionValues) {
        commitStatEvent(eventId, module, monitorPoint, measureValues, dimensionValues, (Long) null, (String) null, (String) null);
    }

    public void commitStatEvent(int eventId, String module, String monitorPoint, MeasureValueSet measureValues, DimensionValueSet dimensionValues, Long commitTime, String access, String subAccessType) {
        Metric metric = MetricRepo.getRepo().getMetric(module, monitorPoint);
        if (metric != null) {
            if (metric.getDimensionSet() != null) {
                metric.getDimensionSet().setConstantValue(dimensionValues);
            }
            if (metric.getMeasureSet() != null) {
                metric.getMeasureSet().setConstantValue(measureValues);
            }
            UTDimensionValueSet utDimensionValues = fetchUTDimensionValues(eventId, commitTime, access, subAccessType);
            StatEvent statEvent = (StatEvent) getEvent(utDimensionValues, module, monitorPoint, (String) null, StatEvent.class);
            if (statEvent != null) {
                statEvent.commit(dimensionValues, measureValues);
            }
            if (Variables.getInstance().isApRealTimeDebugging()) {
                StatEvent event = (StatEvent) BalancedPool.getInstance().poll(StatEvent.class, Integer.valueOf(eventId), module, monitorPoint);
                event.commit(dimensionValues, measureValues);
                UTUtil.sendRealDebugEvent(utDimensionValues, event);
            }
            checkUploadEvent(EventType.getEventType(eventId), this.mSTATCounter);
            return;
        }
        Logger.e("metric is null,stat commit failed,please call AppMonitor.register() once before AppMonitor.STAT.commit()", new Object[0]);
    }

    public void beginStatEvent(Integer eventId, String module, String monitorPoint, String measureName) {
        String transactionId = getTransactionId(module, monitorPoint);
        if (transactionId != null) {
            beginStatEvent(transactionId, eventId, module, monitorPoint, measureName);
        }
    }

    /* JADX WARNING: type inference failed for: r4v8, types: [com.alibaba.appmonitor.pool.Reusable] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void beginStatEvent(java.lang.String r10, java.lang.Integer r11, java.lang.String r12, java.lang.String r13, java.lang.String r14) {
        /*
            r9 = this;
            r5 = 0
            com.alibaba.appmonitor.model.MetricRepo r4 = com.alibaba.appmonitor.model.MetricRepo.getRepo()
            com.alibaba.appmonitor.model.Metric r3 = r4.getMetric(r12, r13)
            if (r3 == 0) goto L_0x0052
            com.alibaba.mtl.appmonitor.model.MeasureSet r4 = r3.getMeasureSet()
            if (r4 == 0) goto L_0x0052
            com.alibaba.mtl.appmonitor.model.MeasureSet r4 = r3.getMeasureSet()
            com.alibaba.mtl.appmonitor.model.Measure r2 = r4.getMeasure(r14)
            if (r2 == 0) goto L_0x004e
            r1 = 0
            java.lang.Class<com.alibaba.appmonitor.event.DurationEvent> r5 = com.alibaba.appmonitor.event.DurationEvent.class
            monitor-enter(r5)
            java.util.Map<java.lang.String, com.alibaba.appmonitor.event.DurationEvent> r4 = r9.durationEventMap     // Catch:{ all -> 0x004f }
            java.lang.Object r4 = r4.get(r10)     // Catch:{ all -> 0x004f }
            r0 = r4
            com.alibaba.appmonitor.event.DurationEvent r0 = (com.alibaba.appmonitor.event.DurationEvent) r0     // Catch:{ all -> 0x004f }
            r1 = r0
            if (r1 != 0) goto L_0x004a
            com.alibaba.appmonitor.pool.BalancedPool r4 = com.alibaba.appmonitor.pool.BalancedPool.getInstance()     // Catch:{ all -> 0x004f }
            java.lang.Class<com.alibaba.appmonitor.event.DurationEvent> r6 = com.alibaba.appmonitor.event.DurationEvent.class
            r7 = 3
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x004f }
            r8 = 0
            r7[r8] = r11     // Catch:{ all -> 0x004f }
            r8 = 1
            r7[r8] = r12     // Catch:{ all -> 0x004f }
            r8 = 2
            r7[r8] = r13     // Catch:{ all -> 0x004f }
            com.alibaba.appmonitor.pool.Reusable r4 = r4.poll(r6, r7)     // Catch:{ all -> 0x004f }
            r0 = r4
            com.alibaba.appmonitor.event.DurationEvent r0 = (com.alibaba.appmonitor.event.DurationEvent) r0     // Catch:{ all -> 0x004f }
            r1 = r0
            java.util.Map<java.lang.String, com.alibaba.appmonitor.event.DurationEvent> r4 = r9.durationEventMap     // Catch:{ all -> 0x004f }
            r4.put(r10, r1)     // Catch:{ all -> 0x004f }
        L_0x004a:
            monitor-exit(r5)     // Catch:{ all -> 0x004f }
            r1.start(r14)
        L_0x004e:
            return
        L_0x004f:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x004f }
            throw r4
        L_0x0052:
            java.lang.String r4 = "log discard!,metric is null,please call AppMonitor.register() once before Transaction.begin(measure)"
            java.lang.Object[] r5 = new java.lang.Object[r5]
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r4, (java.lang.Object[]) r5)
            goto L_0x004e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.appmonitor.event.EventRepo.beginStatEvent(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String):void");
    }

    public void endStatEvent(String module, String monitorPoint, String measureName) {
        String transactionId = getTransactionId(module, monitorPoint);
        if (transactionId != null) {
            endStatEvent(transactionId, measureName, true);
        }
    }

    public void endStatEvent(String transactionId, String measureName, boolean resetTransactionId) {
        DurationEvent durationEvent = this.durationEventMap.get(transactionId);
        if (durationEvent != null && durationEvent.end(measureName)) {
            this.durationEventMap.remove(transactionId);
            if (resetTransactionId) {
                resetTransactionId(durationEvent.module, durationEvent.monitorPoint);
            }
            commitStatEvent(durationEvent.eventId, durationEvent.module, durationEvent.monitorPoint, durationEvent.getMeasureValues(), durationEvent.getDimensionValues());
            BalancedPool.getInstance().offer(durationEvent);
        }
    }

    /* JADX WARNING: type inference failed for: r2v4, types: [com.alibaba.appmonitor.pool.Reusable] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void commitElapseEventDimensionValue(java.lang.String r8, java.lang.Integer r9, java.lang.String r10, java.lang.String r11, com.alibaba.mtl.appmonitor.model.DimensionValueSet r12) {
        /*
            r7 = this;
            r1 = 0
            java.lang.Class<com.alibaba.appmonitor.event.DurationEvent> r3 = com.alibaba.appmonitor.event.DurationEvent.class
            monitor-enter(r3)
            java.util.Map<java.lang.String, com.alibaba.appmonitor.event.DurationEvent> r2 = r7.durationEventMap     // Catch:{ all -> 0x0034 }
            java.lang.Object r2 = r2.get(r8)     // Catch:{ all -> 0x0034 }
            r0 = r2
            com.alibaba.appmonitor.event.DurationEvent r0 = (com.alibaba.appmonitor.event.DurationEvent) r0     // Catch:{ all -> 0x0034 }
            r1 = r0
            if (r1 != 0) goto L_0x002f
            com.alibaba.appmonitor.pool.BalancedPool r2 = com.alibaba.appmonitor.pool.BalancedPool.getInstance()     // Catch:{ all -> 0x0034 }
            java.lang.Class<com.alibaba.appmonitor.event.DurationEvent> r4 = com.alibaba.appmonitor.event.DurationEvent.class
            r5 = 3
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x0034 }
            r6 = 0
            r5[r6] = r9     // Catch:{ all -> 0x0034 }
            r6 = 1
            r5[r6] = r10     // Catch:{ all -> 0x0034 }
            r6 = 2
            r5[r6] = r11     // Catch:{ all -> 0x0034 }
            com.alibaba.appmonitor.pool.Reusable r2 = r2.poll(r4, r5)     // Catch:{ all -> 0x0034 }
            r0 = r2
            com.alibaba.appmonitor.event.DurationEvent r0 = (com.alibaba.appmonitor.event.DurationEvent) r0     // Catch:{ all -> 0x0034 }
            r1 = r0
            java.util.Map<java.lang.String, com.alibaba.appmonitor.event.DurationEvent> r2 = r7.durationEventMap     // Catch:{ all -> 0x0034 }
            r2.put(r8, r1)     // Catch:{ all -> 0x0034 }
        L_0x002f:
            monitor-exit(r3)     // Catch:{ all -> 0x0034 }
            r1.commitDimensionValue(r12)
            return
        L_0x0034:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0034 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.appmonitor.event.EventRepo.commitElapseEventDimensionValue(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, com.alibaba.mtl.appmonitor.model.DimensionValueSet):void");
    }

    private String getTransactionId(String module, String monitorPoint) {
        Metric metric = MetricRepo.getRepo().getMetric(module, monitorPoint);
        if (metric != null) {
            return metric.getTransactionId();
        }
        return null;
    }

    private void resetTransactionId(String module, String monitorPoint) {
        Metric metric = MetricRepo.getRepo().getMetric(module, monitorPoint);
        if (metric != null) {
            metric.resetTransactionId();
        }
    }

    /* JADX WARNING: type inference failed for: r3v9, types: [com.alibaba.appmonitor.pool.Reusable] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.alibaba.appmonitor.event.Event getEvent(com.alibaba.appmonitor.model.UTDimensionValueSet r9, java.lang.String r10, java.lang.String r11, java.lang.String r12, java.lang.Class<? extends com.alibaba.appmonitor.event.Event> r13) {
        /*
            r8 = this;
            boolean r3 = com.alibaba.analytics.utils.StringUtils.isNotBlank(r10)
            if (r3 == 0) goto L_0x0057
            boolean r3 = com.alibaba.analytics.utils.StringUtils.isNotBlank(r11)
            if (r3 == 0) goto L_0x0057
            java.lang.Integer r2 = r9.getEventId()
            if (r2 == 0) goto L_0x0057
            r1 = 0
            java.util.Map<com.alibaba.appmonitor.model.UTDimensionValueSet, com.alibaba.appmonitor.model.MetricValueSet> r4 = r8.eventMap
            monitor-enter(r4)
            java.util.Map<com.alibaba.appmonitor.model.UTDimensionValueSet, com.alibaba.appmonitor.model.MetricValueSet> r3 = r8.eventMap     // Catch:{ all -> 0x0054 }
            java.lang.Object r3 = r3.get(r9)     // Catch:{ all -> 0x0054 }
            r0 = r3
            com.alibaba.appmonitor.model.MetricValueSet r0 = (com.alibaba.appmonitor.model.MetricValueSet) r0     // Catch:{ all -> 0x0054 }
            r1 = r0
            if (r1 != 0) goto L_0x004a
            com.alibaba.appmonitor.pool.BalancedPool r3 = com.alibaba.appmonitor.pool.BalancedPool.getInstance()     // Catch:{ all -> 0x0054 }
            java.lang.Class<com.alibaba.appmonitor.model.MetricValueSet> r5 = com.alibaba.appmonitor.model.MetricValueSet.class
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x0054 }
            com.alibaba.appmonitor.pool.Reusable r3 = r3.poll(r5, r6)     // Catch:{ all -> 0x0054 }
            r0 = r3
            com.alibaba.appmonitor.model.MetricValueSet r0 = (com.alibaba.appmonitor.model.MetricValueSet) r0     // Catch:{ all -> 0x0054 }
            r1 = r0
            java.util.Map<com.alibaba.appmonitor.model.UTDimensionValueSet, com.alibaba.appmonitor.model.MetricValueSet> r3 = r8.eventMap     // Catch:{ all -> 0x0054 }
            r3.put(r9, r1)     // Catch:{ all -> 0x0054 }
            java.lang.String r3 = "EventRepo"
            r5 = 2
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x0054 }
            r6 = 0
            java.lang.String r7 = "put in Map utDimensionValues"
            r5[r6] = r7     // Catch:{ all -> 0x0054 }
            r6 = 1
            r5[r6] = r9     // Catch:{ all -> 0x0054 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r3, (java.lang.Object[]) r5)     // Catch:{ all -> 0x0054 }
        L_0x004a:
            monitor-exit(r4)     // Catch:{ all -> 0x0054 }
            r3 = r10
            r4 = r11
            r5 = r12
            r6 = r13
            com.alibaba.appmonitor.event.Event r3 = r1.getEvent(r2, r3, r4, r5, r6)
        L_0x0053:
            return r3
        L_0x0054:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0054 }
            throw r3
        L_0x0057:
            r3 = 0
            goto L_0x0053
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.appmonitor.event.EventRepo.getEvent(com.alibaba.appmonitor.model.UTDimensionValueSet, java.lang.String, java.lang.String, java.lang.String, java.lang.Class):com.alibaba.appmonitor.event.Event");
    }

    private void checkUploadEvent(EventType eventType, AtomicInteger counter) {
        int size = counter.incrementAndGet();
        Logger.i(eventType.toString(), " EVENT size:", String.valueOf(size));
        if (size >= eventType.getTriggerCount()) {
            Logger.d(TAG, eventType.toString(), " event size exceed trigger count.");
            uploadEvent(eventType.getEventId());
        }
    }

    public Map<UTDimensionValueSet, List<Event>> getUploadEvent(int eventId) {
        Map<UTDimensionValueSet, List<Event>> uploadEventMap = new HashMap<>();
        synchronized (this.eventMap) {
            Iterator<Map.Entry<UTDimensionValueSet, MetricValueSet>> it = this.eventMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<UTDimensionValueSet, MetricValueSet> entity = it.next();
                UTDimensionValueSet utDimensionValues = entity.getKey();
                MetricValueSet ms = entity.getValue();
                if (utDimensionValues.getEventId().intValue() == eventId) {
                    if (ms != null) {
                        uploadEventMap.put(utDimensionValues, ms.getEvents());
                    } else {
                        Logger.d("error", "utDimensionValues", utDimensionValues);
                    }
                    it.remove();
                }
            }
        }
        getCounter(eventId).set(0);
        return uploadEventMap;
    }

    public void cleanExpiredEvent() {
        List<String> transactionIds = new ArrayList<>(this.durationEventMap.keySet());
        int size = transactionIds.size();
        for (int i = 0; i < size; i++) {
            String transactionId = transactionIds.get(i);
            DurationEvent event = this.durationEventMap.get(transactionId);
            if (event != null && event.isExpired()) {
                this.durationEventMap.remove(transactionId);
            }
        }
    }

    public void uploadEvent(int eventId) {
        final Map<UTDimensionValueSet, List<Event>> eventMap2 = getUploadEvent(eventId);
        TaskExecutor.getInstance().submit(new Runnable() {
            public void run() {
                UTUtil.uploadEvent(eventMap2);
            }
        });
    }

    private AtomicInteger getCounter(int eventId) {
        if (65501 == eventId) {
            return this.mAlarmCounter;
        }
        if (65502 == eventId) {
            return this.mCountCounter;
        }
        if (65503 == eventId) {
            return this.mSTATCounter;
        }
        return null;
    }
}
