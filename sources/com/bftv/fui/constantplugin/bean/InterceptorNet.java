package com.bftv.fui.constantplugin.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class InterceptorNet implements Parcelable {
    public static final Parcelable.Creator<InterceptorNet> CREATOR = new Parcelable.Creator<InterceptorNet>() {
        public InterceptorNet createFromParcel(Parcel in) {
            return new InterceptorNet(in);
        }

        public InterceptorNet[] newArray(int size) {
            return new InterceptorNet[size];
        }
    };
    public String fixPattern;
    public int fixUnit;
    public int functionType;

    public InterceptorNet(String fixPattern2, int functionType2) {
        this.fixUnit = 1;
        this.functionType = functionType2;
        this.fixPattern = fixPattern2;
    }

    protected InterceptorNet(Parcel in) {
        this.fixPattern = in.readString();
        this.functionType = in.readInt();
        this.fixUnit = in.readInt();
    }

    public InterceptorNet() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fixPattern);
        dest.writeInt(this.functionType);
        dest.writeInt(this.fixUnit);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return super.toString();
    }
}
