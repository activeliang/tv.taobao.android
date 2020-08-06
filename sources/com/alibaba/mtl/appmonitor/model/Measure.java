package com.alibaba.mtl.appmonitor.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.analytics.utils.Logger;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Measure implements Parcelable {
    public static final Parcelable.Creator<Measure> CREATOR = new Parcelable.Creator<Measure>() {
        public Measure createFromParcel(Parcel source) {
            return Measure.readFromParcel(source);
        }

        public Measure[] newArray(int size) {
            return new Measure[size];
        }
    };
    private static List<Double> nullList;
    private List<Double> bounds;
    protected Double constantValue;
    public String name;

    Measure() {
        this.constantValue = Double.valueOf(ClientTraceData.b.f47a);
    }

    static {
        nullList = null;
        nullList = new ArrayList(1);
        nullList.add((Object) null);
    }

    public Measure(String name2) {
        this(name2, Double.valueOf(ClientTraceData.b.f47a));
    }

    public Measure(String name2, Double constantValue2, List<Double> bounds2) {
        double d = ClientTraceData.b.f47a;
        this.constantValue = Double.valueOf(ClientTraceData.b.f47a);
        if (bounds2 != null) {
            if (bounds2.removeAll(nullList)) {
                Logger.w("bounds entity must not be null", new Object[0]);
            }
            Collections.sort(bounds2);
            this.bounds = bounds2;
        }
        this.name = name2;
        this.constantValue = Double.valueOf(constantValue2 != null ? constantValue2.doubleValue() : d);
    }

    public Measure(String name2, Double constantValue2) {
        this(name2, constantValue2, (Double) null, (Double) null);
    }

    public Measure(String name2, Double constantValue2, Double min, Double max) {
        this(name2, constantValue2, (List<Double>) null);
        if (min != null || max != null) {
            setRange(min, max);
        }
    }

    public void setRange(Double min, Double max) {
        if (min == null || max == null) {
            Logger.w("min or max must not be null", new Object[0]);
            return;
        }
        this.bounds = new ArrayList(2);
        this.bounds.add(min);
        this.bounds.add(max);
    }

    public Double getMin() {
        if (this.bounds == null || this.bounds.size() < 1) {
            return null;
        }
        return this.bounds.get(0);
    }

    public Double getMax() {
        if (this.bounds == null || this.bounds.size() < 2) {
            return null;
        }
        return this.bounds.get(this.bounds.size() - 1);
    }

    public List<Double> getBounds() {
        return this.bounds;
    }

    public String getName() {
        return this.name;
    }

    public Double getConstantValue() {
        return this.constantValue;
    }

    public void setConstantValue(Double constantValue2) {
        this.constantValue = constantValue2;
    }

    public boolean valid(MeasureValue measureValue) {
        Double value = Double.valueOf(measureValue.getValue());
        return value != null && (getMin() == null || value.doubleValue() >= getMin().doubleValue()) && (getMax() == null || value.doubleValue() < getMax().doubleValue());
    }

    public int hashCode() {
        return (this.name == null ? 0 : this.name.hashCode()) + 31;
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
        Measure other = (Measure) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
            return true;
        } else if (!this.name.equals(other.name)) {
            return false;
        } else {
            return true;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        try {
            dest.writeList(this.bounds);
            dest.writeString(this.name);
            dest.writeInt(this.constantValue == null ? 0 : 1);
            if (this.constantValue != null) {
                dest.writeDouble(this.constantValue.doubleValue());
            }
        } catch (Throwable e) {
            Logger.e("writeToParcel", e, new Object[0]);
        }
    }

    static Measure readFromParcel(Parcel source) {
        boolean constantValueIsNull;
        try {
            List<Double> bounds2 = source.readArrayList(Measure.class.getClassLoader());
            String name2 = source.readString();
            if (source.readInt() == 0) {
                constantValueIsNull = true;
            } else {
                constantValueIsNull = false;
            }
            Double constantValue2 = null;
            if (!constantValueIsNull) {
                constantValue2 = Double.valueOf(source.readDouble());
            }
            return new Measure(name2, constantValue2, bounds2);
        } catch (Throwable e) {
            e.printStackTrace();
            Logger.e("readFromParcel", e, new Object[0]);
            return null;
        }
    }
}
