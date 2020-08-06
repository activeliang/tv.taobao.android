package com.alibaba.mtl.appmonitor.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.appmonitor.pool.Reusable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MeasureValueSet implements IMerge<MeasureValueSet>, Reusable, Parcelable {
    public static final Parcelable.Creator<MeasureValueSet> CREATOR = new Parcelable.Creator<MeasureValueSet>() {
        public MeasureValueSet createFromParcel(Parcel source) {
            return MeasureValueSet.readFromParcel(source);
        }

        public MeasureValueSet[] newArray(int size) {
            return new MeasureValueSet[size];
        }
    };
    private Map<String, MeasureValue> map = new LinkedHashMap();

    public static MeasureValueSet create() {
        return (MeasureValueSet) BalancedPool.getInstance().poll(MeasureValueSet.class, new Object[0]);
    }

    @Deprecated
    public static MeasureValueSet create(int initialCapacity) {
        return (MeasureValueSet) BalancedPool.getInstance().poll(MeasureValueSet.class, new Object[0]);
    }

    public static MeasureValueSet create(Map<String, Double> values) {
        MeasureValueSet measureValueSet = (MeasureValueSet) BalancedPool.getInstance().poll(MeasureValueSet.class, new Object[0]);
        if (values != null) {
            for (String measureName : values.keySet()) {
                Double measureValue = values.get(measureName);
                if (measureValue != null) {
                    measureValueSet.map.put(measureName, BalancedPool.getInstance().poll(MeasureValue.class, measureValue));
                }
            }
        }
        return measureValueSet;
    }

    public static MeasureValueSet fromStringMap(Map<String, String> values) {
        MeasureValueSet valueSet = (MeasureValueSet) BalancedPool.getInstance().poll(MeasureValueSet.class, new Object[0]);
        if (values != null) {
            for (Map.Entry<String, String> entry : values.entrySet()) {
                Double doubleValue = toDouble(entry.getValue());
                if (doubleValue != null) {
                    valueSet.map.put(entry.getKey(), BalancedPool.getInstance().poll(MeasureValue.class, doubleValue));
                }
            }
        }
        return valueSet;
    }

    private static Double toDouble(String str) {
        try {
            return Double.valueOf(str);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public MeasureValueSet setValue(String name, double value) {
        this.map.put(name, BalancedPool.getInstance().poll(MeasureValue.class, Double.valueOf(value)));
        return this;
    }

    public void setValue(String name, MeasureValue value) {
        this.map.put(name, value);
    }

    public MeasureValue getValue(String name) {
        return this.map.get(name);
    }

    public Map<String, MeasureValue> getMap() {
        return this.map;
    }

    public void setMap(Map<String, MeasureValue> map2) {
        this.map = map2;
    }

    public boolean containValue(String name) {
        return this.map.containsKey(name);
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public void merge(MeasureValueSet t) {
        for (String measureName : this.map.keySet()) {
            this.map.get(measureName).merge(t.getValue(measureName));
        }
    }

    public void clean() {
        for (MeasureValue measureValue : this.map.values()) {
            BalancedPool.getInstance().offer(measureValue);
        }
        this.map.clear();
    }

    public void fill(Object... params) {
        if (this.map == null) {
            this.map = new LinkedHashMap();
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(this.map);
    }

    static MeasureValueSet readFromParcel(Parcel source) {
        MeasureValueSet ret = null;
        try {
            ret = create();
            ret.map = source.readHashMap(DimensionValueSet.class.getClassLoader());
            return ret;
        } catch (Throwable th) {
            return ret;
        }
    }

    public void setBuckets(List<Measure> buckets) {
        if (buckets != null) {
            for (String measureName : this.map.keySet()) {
                this.map.get(measureName).setBuckets(getMeasure(buckets, measureName));
            }
        }
    }

    private Measure getMeasure(List<Measure> measures, String measureName) {
        for (Measure m : measures) {
            if (measureName.equalsIgnoreCase(m.getName())) {
                return m;
            }
        }
        return null;
    }
}
