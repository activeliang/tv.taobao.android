package com.alibaba.mtl.appmonitor.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MeasureSet implements Parcelable {
    public static final Parcelable.Creator<MeasureSet> CREATOR = new Parcelable.Creator<MeasureSet>() {
        public MeasureSet createFromParcel(Parcel source) {
            return MeasureSet.readFromParcel(source);
        }

        public MeasureSet[] newArray(int size) {
            return new MeasureSet[size];
        }
    };
    private static final int INIT_SIZE = 3;
    private List<Measure> measures = new ArrayList(3);

    public static MeasureSet create() {
        return new MeasureSet();
    }

    public static MeasureSet create(Collection<String> measures2) {
        MeasureSet measureSet = new MeasureSet();
        if (measures2 != null) {
            for (String measure : measures2) {
                measureSet.addMeasure(measure);
            }
        }
        return measureSet;
    }

    public static MeasureSet create(String[] measures2) {
        MeasureSet measureSet = new MeasureSet();
        if (measures2 != null) {
            for (String measure : measures2) {
                measureSet.addMeasure(measure);
            }
        }
        return measureSet;
    }

    private MeasureSet() {
    }

    public boolean valid(MeasureValueSet measureValues) {
        if (this.measures != null) {
            if (measureValues == null) {
                return false;
            }
            for (int i = 0; i < this.measures.size(); i++) {
                Measure measure = this.measures.get(i);
                if (measure != null) {
                    String measureName = measure.getName();
                    if (!measureValues.containValue(measureName) || !measure.valid(measureValues.getValue(measureName))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public MeasureSet addMeasure(Measure measure) {
        if (!this.measures.contains(measure)) {
            this.measures.add(measure);
        }
        return this;
    }

    public MeasureSet addMeasure(String name) {
        return addMeasure(new Measure(name));
    }

    public Measure getMeasure(String name) {
        for (Measure meas : this.measures) {
            if (meas.getName().equals(name)) {
                return meas;
            }
        }
        return null;
    }

    public List<Measure> getMeasures() {
        return this.measures;
    }

    public void setConstantValue(MeasureValueSet measureValues) {
        if (this.measures != null && measureValues != null) {
            for (Measure measure : this.measures) {
                if (measure.getConstantValue() != null && measureValues.getValue(measure.getName()) == null) {
                    measureValues.setValue(measure.getName(), measure.getConstantValue().doubleValue());
                }
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.measures != null) {
            try {
                Object[] objects = this.measures.toArray();
                Measure[] parcelables = null;
                if (objects != null) {
                    parcelables = new Measure[objects.length];
                    for (int i = 0; i < objects.length; i++) {
                        parcelables[i] = (Measure) objects[i];
                    }
                }
                dest.writeParcelableArray(parcelables, flags);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static MeasureSet readFromParcel(Parcel source) {
        MeasureSet ret = create();
        try {
            Parcelable[] parcelables = source.readParcelableArray(MeasureSet.class.getClassLoader());
            if (parcelables != null) {
                ArrayList<Measure> measureList = new ArrayList<>(parcelables.length);
                for (Parcelable parcelable : parcelables) {
                    measureList.add((Measure) parcelable);
                }
                ret.measures = measureList;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void upateMeasures(List<Measure> configMeasures) {
        int measureSize = this.measures.size();
        int configMeasureSize = configMeasures.size();
        for (int i = 0; i < measureSize; i++) {
            for (int k = 0; k < configMeasureSize; k++) {
                if (TextUtils.equals(this.measures.get(i).name, configMeasures.get(k).name)) {
                    this.measures.get(i).setRange(configMeasures.get(k).getMin(), configMeasures.get(k).getMax());
                }
            }
        }
    }

    public void upateMeasure(Measure newMeasure) {
        int measureSize = this.measures.size();
        for (int i = 0; i < measureSize; i++) {
            if (TextUtils.equals(this.measures.get(i).name, newMeasure.name)) {
                this.measures.get(i).setRange(newMeasure.getMin(), newMeasure.getMax());
                this.measures.get(i).setConstantValue(newMeasure.getConstantValue());
            }
        }
    }
}
