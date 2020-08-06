package com.alibaba.mtl.appmonitor.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.appmonitor.pool.Reusable;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeasureValue implements IMerge<MeasureValue>, Reusable, Parcelable {
    public static final Parcelable.Creator<MeasureValue> CREATOR = new Parcelable.Creator<MeasureValue>() {
        public MeasureValue createFromParcel(Parcel source) {
            return MeasureValue.readFromParcel(source);
        }

        public MeasureValue[] newArray(int size) {
            return new MeasureValue[size];
        }
    };
    private List<Bucket> buckets;
    private boolean finish;
    private Double offset;
    private double value;

    @Deprecated
    public MeasureValue() {
    }

    @Deprecated
    public MeasureValue(double value2) {
        this.value = value2;
    }

    @Deprecated
    public MeasureValue(double value2, double offset2) {
        this.offset = Double.valueOf(offset2);
        this.value = value2;
        this.finish = false;
    }

    public static MeasureValue create() {
        return (MeasureValue) BalancedPool.getInstance().poll(MeasureValue.class, new Object[0]);
    }

    public static MeasureValue create(double value2) {
        return (MeasureValue) BalancedPool.getInstance().poll(MeasureValue.class, Double.valueOf(value2));
    }

    public static MeasureValue create(double value2, double offset2) {
        return (MeasureValue) BalancedPool.getInstance().poll(MeasureValue.class, Double.valueOf(value2), Double.valueOf(offset2));
    }

    public Double getOffset() {
        return this.offset;
    }

    public boolean isFinish() {
        return this.finish;
    }

    public void setFinish(boolean finish2) {
        this.finish = finish2;
    }

    public void setOffset(double offset2) {
        this.offset = Double.valueOf(offset2);
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value2) {
        this.value = value2;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void merge(com.alibaba.mtl.appmonitor.model.MeasureValue r7) {
        /*
            r6 = this;
            monitor-enter(r6)
            if (r7 != 0) goto L_0x0005
        L_0x0003:
            monitor-exit(r6)
            return
        L_0x0005:
            double r2 = r6.value     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
            double r4 = r7.getValue()     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
            double r2 = r2 + r4
            r6.value = r2     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
            java.lang.Double r1 = r7.getOffset()     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
            if (r1 == 0) goto L_0x0035
            java.lang.Double r1 = r6.offset     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
            if (r1 != 0) goto L_0x0020
            r2 = 0
            java.lang.Double r1 = java.lang.Double.valueOf(r2)     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
            r6.offset = r1     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
        L_0x0020:
            java.lang.Double r1 = r6.offset     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
            double r2 = r1.doubleValue()     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
            java.lang.Double r1 = r7.getOffset()     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
            double r4 = r1.doubleValue()     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
            double r2 = r2 + r4
            java.lang.Double r1 = java.lang.Double.valueOf(r2)     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
            r6.offset = r1     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
        L_0x0035:
            double r2 = r7.getValue()     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
            com.alibaba.mtl.appmonitor.model.MeasureValue$Bucket r0 = r6.getBucket(r2)     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
            if (r0 == 0) goto L_0x0003
            r0.increase()     // Catch:{ Throwable -> 0x0043, all -> 0x0045 }
            goto L_0x0003
        L_0x0043:
            r1 = move-exception
            goto L_0x0003
        L_0x0045:
            r1 = move-exception
            monitor-exit(r6)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.mtl.appmonitor.model.MeasureValue.merge(com.alibaba.mtl.appmonitor.model.MeasureValue):void");
    }

    public synchronized void clean() {
        this.value = ClientTraceData.b.f47a;
        this.offset = null;
        this.finish = false;
        this.buckets = null;
    }

    public synchronized void fill(Object... params) {
        if (params != null) {
            if (params.length > 0) {
                this.value = params[0].doubleValue();
            }
            if (params.length > 1) {
                this.offset = params[1];
                this.finish = false;
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        try {
            dest.writeInt(this.finish ? 1 : 0);
            dest.writeDouble(this.offset == null ? ClientTraceData.b.f47a : this.offset.doubleValue());
            dest.writeDouble(this.value);
        } catch (Throwable th) {
        }
    }

    static MeasureValue readFromParcel(Parcel source) {
        try {
            boolean finish2 = source.readInt() != 0;
            Double offset2 = Double.valueOf(source.readDouble());
            double value2 = source.readDouble();
            MeasureValue ret = create();
            ret.finish = finish2;
            ret.offset = offset2;
            ret.value = value2;
            return ret;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized Map<String, Double> getBuckets() {
        Map bucketValueMap;
        Object access$100;
        Object access$200;
        if (this.buckets == null) {
            bucketValueMap = null;
        } else {
            bucketValueMap = new HashMap();
            for (Bucket b : this.buckets) {
                if (b.count > 0) {
                    StringBuilder sb = new StringBuilder();
                    if (b.min == null) {
                        access$100 = "-∞";
                    } else {
                        access$100 = b.min;
                    }
                    StringBuilder append = sb.append(access$100).append(",");
                    if (b.max == null) {
                        access$200 = "∞";
                    } else {
                        access$200 = b.max;
                    }
                    bucketValueMap.put(append.append(access$200).toString(), Long.valueOf(b.count));
                }
            }
        }
        return bucketValueMap;
    }

    /* access modifiers changed from: protected */
    public synchronized void setBuckets(Measure measure) {
        List<Double> bounds = measure.getBounds();
        if (bounds != null && bounds.size() >= 2) {
            if (this.buckets == null) {
                this.buckets = new ArrayList();
                for (int i = 0; i + 1 < bounds.size(); i++) {
                    this.buckets.add(new Bucket(bounds.get(i), bounds.get(i + 1)));
                }
                Bucket bucket = getBucket(this.value);
                if (bucket != null) {
                    bucket.increase();
                }
            }
        }
    }

    private class Bucket {
        /* access modifiers changed from: private */
        public long count = 0;
        /* access modifiers changed from: private */
        public Double max;
        /* access modifiers changed from: private */
        public Double min;

        public Bucket(Double min2, Double max2) {
            this.min = min2;
            this.max = max2;
        }

        public boolean in(Double value) {
            if (value == null) {
                return false;
            }
            Double min2 = this.min;
            Double max2 = this.max;
            if (min2 == null) {
                min2 = Double.valueOf(Double.MIN_VALUE);
            }
            if (max2 == null) {
                max2 = Double.valueOf(Double.MAX_VALUE);
            }
            if (value.doubleValue() < min2.doubleValue() || value.doubleValue() >= max2.doubleValue()) {
                return false;
            }
            return true;
        }

        public void increase() {
            this.count++;
        }
    }

    private Bucket getBucket(double value2) {
        if (this.buckets != null) {
            for (int i = 0; i < this.buckets.size(); i++) {
                if (this.buckets.get(i).in(Double.valueOf(value2))) {
                    return this.buckets.get(i);
                }
            }
        }
        return null;
    }
}
