package com.alibaba.appmonitor.event;

import com.alibaba.appmonitor.model.Metric;
import com.alibaba.appmonitor.model.MetricRepo;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.appmonitor.pool.ReuseJSONArray;
import com.alibaba.appmonitor.pool.ReuseJSONObject;
import com.alibaba.appmonitor.sample.SampleConfigConstant;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureValue;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatEvent extends Event {
    /* access modifiers changed from: private */
    public Metric metric;
    private Map<DimensionValueSet, Entity> values;

    /* JADX WARNING: type inference failed for: r5v16, types: [com.alibaba.appmonitor.pool.Reusable] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void commit(com.alibaba.mtl.appmonitor.model.DimensionValueSet r9, com.alibaba.mtl.appmonitor.model.MeasureValueSet r10) {
        /*
            r8 = this;
            monitor-enter(r8)
            r2 = 0
            if (r9 != 0) goto L_0x0015
            com.alibaba.appmonitor.pool.BalancedPool r5 = com.alibaba.appmonitor.pool.BalancedPool.getInstance()     // Catch:{ all -> 0x0071 }
            java.lang.Class<com.alibaba.mtl.appmonitor.model.DimensionValueSet> r6 = com.alibaba.mtl.appmonitor.model.DimensionValueSet.class
            r7 = 0
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x0071 }
            com.alibaba.appmonitor.pool.Reusable r5 = r5.poll(r6, r7)     // Catch:{ all -> 0x0071 }
            r0 = r5
            com.alibaba.mtl.appmonitor.model.DimensionValueSet r0 = (com.alibaba.mtl.appmonitor.model.DimensionValueSet) r0     // Catch:{ all -> 0x0071 }
            r9 = r0
        L_0x0015:
            java.util.Map<com.alibaba.mtl.appmonitor.model.DimensionValueSet, com.alibaba.appmonitor.event.StatEvent$Entity> r5 = r8.values     // Catch:{ all -> 0x0071 }
            boolean r5 = r5.containsKey(r9)     // Catch:{ all -> 0x0071 }
            if (r5 == 0) goto L_0x0040
            java.util.Map<com.alibaba.mtl.appmonitor.model.DimensionValueSet, com.alibaba.appmonitor.event.StatEvent$Entity> r5 = r8.values     // Catch:{ all -> 0x0071 }
            java.lang.Object r5 = r5.get(r9)     // Catch:{ all -> 0x0071 }
            r0 = r5
            com.alibaba.appmonitor.event.StatEvent$Entity r0 = (com.alibaba.appmonitor.event.StatEvent.Entity) r0     // Catch:{ all -> 0x0071 }
            r2 = r0
        L_0x0027:
            r4 = 0
            com.alibaba.appmonitor.model.Metric r5 = r8.metric     // Catch:{ all -> 0x0071 }
            if (r5 == 0) goto L_0x0032
            com.alibaba.appmonitor.model.Metric r5 = r8.metric     // Catch:{ all -> 0x0071 }
            boolean r4 = r5.valid(r9, r10)     // Catch:{ all -> 0x0071 }
        L_0x0032:
            if (r4 == 0) goto L_0x005e
            r2.incrCount()     // Catch:{ all -> 0x0071 }
            r2.commit(r10)     // Catch:{ all -> 0x0071 }
        L_0x003a:
            r5 = 0
            super.commit(r5)     // Catch:{ all -> 0x0071 }
            monitor-exit(r8)
            return
        L_0x0040:
            com.alibaba.appmonitor.pool.BalancedPool r5 = com.alibaba.appmonitor.pool.BalancedPool.getInstance()     // Catch:{ all -> 0x0071 }
            java.lang.Class<com.alibaba.mtl.appmonitor.model.DimensionValueSet> r6 = com.alibaba.mtl.appmonitor.model.DimensionValueSet.class
            r7 = 0
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x0071 }
            com.alibaba.appmonitor.pool.Reusable r1 = r5.poll(r6, r7)     // Catch:{ all -> 0x0071 }
            com.alibaba.mtl.appmonitor.model.DimensionValueSet r1 = (com.alibaba.mtl.appmonitor.model.DimensionValueSet) r1     // Catch:{ all -> 0x0071 }
            r1.addValues(r9)     // Catch:{ all -> 0x0071 }
            com.alibaba.appmonitor.event.StatEvent$Entity r3 = new com.alibaba.appmonitor.event.StatEvent$Entity     // Catch:{ all -> 0x0071 }
            r3.<init>()     // Catch:{ all -> 0x0071 }
            java.util.Map<com.alibaba.mtl.appmonitor.model.DimensionValueSet, com.alibaba.appmonitor.event.StatEvent$Entity> r5 = r8.values     // Catch:{ all -> 0x0074 }
            r5.put(r1, r3)     // Catch:{ all -> 0x0074 }
            r2 = r3
            goto L_0x0027
        L_0x005e:
            r2.incrNoise()     // Catch:{ all -> 0x0071 }
            com.alibaba.appmonitor.model.Metric r5 = r8.metric     // Catch:{ all -> 0x0071 }
            if (r5 == 0) goto L_0x003a
            com.alibaba.appmonitor.model.Metric r5 = r8.metric     // Catch:{ all -> 0x0071 }
            boolean r5 = r5.isCommitDetail()     // Catch:{ all -> 0x0071 }
            if (r5 == 0) goto L_0x003a
            r2.commit(r10)     // Catch:{ all -> 0x0071 }
            goto L_0x003a
        L_0x0071:
            r5 = move-exception
        L_0x0072:
            monitor-exit(r8)
            throw r5
        L_0x0074:
            r5 = move-exception
            r2 = r3
            goto L_0x0072
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.appmonitor.event.StatEvent.commit(com.alibaba.mtl.appmonitor.model.DimensionValueSet, com.alibaba.mtl.appmonitor.model.MeasureValueSet):void");
    }

    public synchronized JSONObject dumpToJSONObject() {
        JSONObject jobject;
        jobject = super.dumpToJSONObject();
        if (this.metric != null) {
            jobject.put("isCommitDetail", (Object) String.valueOf(this.metric.isCommitDetail()));
        }
        JSONArray array = (JSONArray) BalancedPool.getInstance().poll(ReuseJSONArray.class, new Object[0]);
        if (this.values != null) {
            for (Map.Entry<DimensionValueSet, Entity> entry : this.values.entrySet()) {
                JSONObject obj = (JSONObject) BalancedPool.getInstance().poll(ReuseJSONObject.class, new Object[0]);
                DimensionValueSet dimensionValues = entry.getKey();
                Entity entity = entry.getValue();
                Integer count = Integer.valueOf(entity.count);
                Integer noise = Integer.valueOf(entity.noise);
                obj.put("count", (Object) count);
                obj.put("noise", (Object) noise);
                obj.put(SampleConfigConstant.DIMENSIONS, (Object) dimensionValues != null ? new HashMap(dimensionValues.getMap()) : null);
                obj.put("measures", (Object) entity.getValues());
                array.add(obj);
            }
        }
        jobject.put(SampleConfigConstant.VALUES, (Object) array);
        return jobject;
    }

    public Metric getMetric() {
        return this.metric;
    }

    public void setMetric(Metric metric2) {
        this.metric = metric2;
    }

    public synchronized void clean() {
        super.clean();
        this.metric = null;
        for (DimensionValueSet dimensionValueSet : this.values.keySet()) {
            BalancedPool.getInstance().offer(dimensionValueSet);
        }
        this.values.clear();
    }

    public void fill(Object... params) {
        super.fill(params);
        if (this.values == null) {
            this.values = new HashMap();
        }
        this.metric = MetricRepo.getRepo().getMetric(this.module, this.monitorPoint);
    }

    public class Entity {
        /* access modifiers changed from: private */
        public int count = 0;
        private List<MeasureValueSet> measureValueList = new ArrayList();
        /* access modifiers changed from: private */
        public int noise = 0;

        public Entity() {
        }

        public void commit(MeasureValueSet measureValues) {
            if (measureValues == null) {
                return;
            }
            if (StatEvent.this.metric != null && StatEvent.this.metric.isCommitDetail()) {
                this.measureValueList.add(formatMeasureValueSet(measureValues));
            } else if (this.measureValueList.isEmpty()) {
                MeasureValueSet ms = formatMeasureValueSet(measureValues);
                if (!(StatEvent.this.metric == null || StatEvent.this.metric.getMeasureSet() == null)) {
                    ms.setBuckets(StatEvent.this.metric.getMeasureSet().getMeasures());
                }
                this.measureValueList.add(ms);
            } else {
                this.measureValueList.get(0).merge(measureValues);
            }
        }

        private MeasureValueSet formatMeasureValueSet(MeasureValueSet originalValues) {
            List<Measure> measures;
            MeasureValueSet values = (MeasureValueSet) BalancedPool.getInstance().poll(MeasureValueSet.class, new Object[0]);
            if (!(StatEvent.this.metric == null || StatEvent.this.metric.getMeasureSet() == null || (measures = StatEvent.this.metric.getMeasureSet().getMeasures()) == null)) {
                int size = measures.size();
                for (int j = 0; j < size; j++) {
                    Measure measure = measures.get(j);
                    if (measure != null) {
                        MeasureValue value = (MeasureValue) BalancedPool.getInstance().poll(MeasureValue.class, new Object[0]);
                        MeasureValue originalValue = originalValues.getValue(measure.getName());
                        if (originalValue.getOffset() != null) {
                            value.setOffset(originalValue.getOffset().doubleValue());
                        }
                        value.setValue(originalValue.getValue());
                        values.setValue(measure.getName(), value);
                    }
                }
            }
            return values;
        }

        public List<Map<String, Map<String, Object>>> getValues() {
            Map<String, MeasureValue> map;
            if (this.measureValueList == null || this.measureValueList.isEmpty()) {
                return null;
            }
            List<Map<String, Map<String, Object>>> valueList = new ArrayList<>();
            int size = this.measureValueList.size();
            for (int i = 0; i < size; i++) {
                MeasureValueSet measureValues = this.measureValueList.get(i);
                if (!(measureValues == null || (map = measureValues.getMap()) == null || map.isEmpty())) {
                    Map<String, Map<String, Object>> values = new HashMap<>();
                    for (Map.Entry<String, MeasureValue> entry : map.entrySet()) {
                        Map<String, Object> value = new HashMap<>();
                        String measureName = entry.getKey();
                        MeasureValue measureValue = entry.getValue();
                        value.put("value", Double.valueOf(measureValue.getValue()));
                        if (measureValue.getOffset() != null) {
                            value.put("offset", measureValue.getOffset());
                        }
                        Map buckets = measureValue.getBuckets();
                        if (buckets != null) {
                            value.put("buckets", buckets);
                        }
                        values.put(measureName, value);
                    }
                    valueList.add(values);
                }
            }
            return valueList;
        }

        public void incrCount() {
            this.count++;
        }

        public void incrNoise() {
            this.noise++;
        }
    }
}
