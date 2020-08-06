package com.alibaba.mtl.appmonitor.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Dimension implements Parcelable {
    public static final Parcelable.Creator<Dimension> CREATOR = new Parcelable.Creator<Dimension>() {
        public Dimension createFromParcel(Parcel source) {
            return Dimension.readFromParcel(source);
        }

        public Dimension[] newArray(int size) {
            return new Dimension[size];
        }
    };
    static final String DEFAULT_NULL_VALUE = "null";
    protected String constantValue;
    protected String name;

    public Dimension() {
        this.constantValue = "null";
    }

    public Dimension(String name2) {
        this(name2, (String) null);
    }

    public Dimension(String name2, String constantValue2) {
        this.constantValue = "null";
        this.name = name2;
        this.constantValue = constantValue2 == null ? "null" : constantValue2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getConstantValue() {
        return this.constantValue;
    }

    public void setConstantValue(String constantValue2) {
        this.constantValue = constantValue2;
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
        Dimension other = (Dimension) obj;
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
        dest.writeString(this.constantValue);
        dest.writeString(this.name);
    }

    static Dimension readFromParcel(Parcel source) {
        try {
            return new Dimension(source.readString(), source.readString());
        } catch (Throwable th) {
            return null;
        }
    }
}
