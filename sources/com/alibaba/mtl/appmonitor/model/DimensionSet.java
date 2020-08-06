package com.alibaba.mtl.appmonitor.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.analytics.utils.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DimensionSet implements Parcelable {
    public static final Parcelable.Creator<DimensionSet> CREATOR = new Parcelable.Creator<DimensionSet>() {
        public DimensionSet createFromParcel(Parcel source) {
            return DimensionSet.readFromParcel(source);
        }

        public DimensionSet[] newArray(int size) {
            return new DimensionSet[size];
        }
    };
    private static final int INIT_SIZE = 3;
    private static final String TAG = "DimensionSet";
    private List<Dimension> dimensions = new ArrayList(3);

    public static DimensionSet create() {
        return new DimensionSet();
    }

    public static DimensionSet create(Collection<String> dimensions2) {
        DimensionSet dimensionSet = new DimensionSet();
        if (dimensions2 != null) {
            for (String dimName : dimensions2) {
                dimensionSet.addDimension(new Dimension(dimName));
            }
        }
        return dimensionSet;
    }

    public static DimensionSet create(String[] dimensions2) {
        DimensionSet dimensionSet = new DimensionSet();
        if (dimensions2 != null) {
            for (String dimName : dimensions2) {
                dimensionSet.addDimension(new Dimension(dimName));
            }
        }
        return dimensionSet;
    }

    private DimensionSet() {
    }

    public boolean valid(DimensionValueSet dimensionValues) {
        if (this.dimensions != null) {
            if (dimensionValues == null) {
                return false;
            }
            for (Dimension dimension : this.dimensions) {
                if (!dimensionValues.containValue(dimension.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    public DimensionSet addDimension(Dimension dimension) {
        if (!this.dimensions.contains(dimension)) {
            this.dimensions.add(dimension);
        }
        return this;
    }

    public DimensionSet addDimension(String name) {
        return addDimension(new Dimension(name));
    }

    public DimensionSet addDimension(String name, String constantValue) {
        return addDimension(new Dimension(name, constantValue));
    }

    public Dimension getDimension(String name) {
        for (Dimension dim : this.dimensions) {
            if (dim.getName().equals(name)) {
                return dim;
            }
        }
        return null;
    }

    public void setConstantValue(DimensionValueSet dimensionValues) {
        if (this.dimensions != null && dimensionValues != null) {
            for (Dimension dimension : this.dimensions) {
                if (dimension.getConstantValue() != null && dimensionValues.getValue(dimension.getName()) == null) {
                    dimensionValues.setValue(dimension.getName(), dimension.getConstantValue());
                }
            }
        }
    }

    public List<Dimension> getDimensions() {
        return this.dimensions;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.dimensions != null) {
            try {
                Object[] objects = this.dimensions.toArray();
                Dimension[] parcelables = null;
                if (objects != null) {
                    parcelables = new Dimension[objects.length];
                    for (int i = 0; i < objects.length; i++) {
                        parcelables[i] = (Dimension) objects[i];
                    }
                }
                dest.writeParcelableArray(parcelables, flags);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static DimensionSet readFromParcel(Parcel source) {
        DimensionSet ret = create();
        try {
            Parcelable[] parcelables = source.readParcelableArray(DimensionSet.class.getClassLoader());
            if (parcelables != null) {
                if (ret.dimensions == null) {
                    ret.dimensions = new ArrayList();
                }
                for (int i = 0; i < parcelables.length; i++) {
                    if (parcelables[i] == null || !(parcelables[i] instanceof Dimension)) {
                        Logger.d(TAG, "parcelables[i]:", parcelables[i]);
                    } else {
                        ret.dimensions.add((Dimension) parcelables[i]);
                    }
                }
            }
        } catch (Throwable e) {
            Logger.w(TAG, "[readFromParcel]", e);
        }
        return ret;
    }
}
