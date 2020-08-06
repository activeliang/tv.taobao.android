package com.alibaba.mtl.appmonitor.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.appmonitor.pool.Reusable;
import com.bftv.fui.constantplugin.Constant;
import java.util.LinkedHashMap;
import java.util.Map;

public class DimensionValueSet implements Reusable, Parcelable {
    public static final Parcelable.Creator<DimensionValueSet> CREATOR = new Parcelable.Creator<DimensionValueSet>() {
        public DimensionValueSet createFromParcel(Parcel source) {
            return DimensionValueSet.readFromParcel(source);
        }

        public DimensionValueSet[] newArray(int size) {
            return new DimensionValueSet[size];
        }
    };
    protected Map<String, String> map;

    public static DimensionValueSet create() {
        return (DimensionValueSet) BalancedPool.getInstance().poll(DimensionValueSet.class, new Object[0]);
    }

    @Deprecated
    public static DimensionValueSet create(int initialCapacity) {
        return (DimensionValueSet) BalancedPool.getInstance().poll(DimensionValueSet.class, new Object[0]);
    }

    @Deprecated
    public DimensionValueSet() {
        if (this.map == null) {
            this.map = new LinkedHashMap();
        }
    }

    public static DimensionValueSet fromStringMap(Map<String, String> values) {
        DimensionValueSet valueSet = (DimensionValueSet) BalancedPool.getInstance().poll(DimensionValueSet.class, new Object[0]);
        for (Map.Entry<String, String> entry : values.entrySet()) {
            valueSet.map.put(entry.getKey(), entry.getValue() != null ? entry.getValue() : Constant.NULL);
        }
        return valueSet;
    }

    public DimensionValueSet setValue(String name, String value) {
        Map<String, String> map2 = this.map;
        if (value == null) {
            value = Constant.NULL;
        }
        map2.put(name, value);
        return this;
    }

    public DimensionValueSet addValues(DimensionValueSet dimensionValues) {
        Map<String, String> dimensionValueMap;
        if (!(dimensionValues == null || (dimensionValueMap = dimensionValues.getMap()) == null)) {
            for (Map.Entry<String, String> entry : dimensionValueMap.entrySet()) {
                this.map.put(entry.getKey(), entry.getValue() != null ? entry.getValue() : Constant.NULL);
            }
        }
        return this;
    }

    public boolean containValue(String name) {
        return this.map.containsKey(name);
    }

    public String getValue(String name) {
        return this.map.get(name);
    }

    public Map<String, String> getMap() {
        return this.map;
    }

    public void setMap(Map<String, String> map2) {
        for (Map.Entry<String, String> entry : map2.entrySet()) {
            this.map.put(entry.getKey(), entry.getValue() != null ? entry.getValue() : Constant.NULL);
        }
    }

    public int hashCode() {
        return (this.map == null ? 0 : this.map.hashCode()) + 31;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DimensionValueSet other = (DimensionValueSet) obj;
        if (this.map == null) {
            if (other.map != null) {
                return false;
            }
            return true;
        } else if (!this.map.equals(other.map)) {
            return false;
        } else {
            return true;
        }
    }

    public void clean() {
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

    static DimensionValueSet readFromParcel(Parcel source) {
        DimensionValueSet ret = null;
        try {
            ret = create();
            ret.map = source.readHashMap(DimensionValueSet.class.getClassLoader());
            return ret;
        } catch (Throwable e) {
            e.printStackTrace();
            return ret;
        }
    }
}
