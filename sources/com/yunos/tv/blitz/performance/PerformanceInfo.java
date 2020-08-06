package com.yunos.tv.blitz.performance;

import android.os.Parcel;
import android.os.Parcelable;

public class PerformanceInfo implements Parcelable {
    public static final Parcelable.Creator<PerformanceInfo> CREATOR = new Parcelable.Creator<PerformanceInfo>() {
        public PerformanceInfo createFromParcel(Parcel source) {
            return new PerformanceInfo(source);
        }

        public PerformanceInfo[] newArray(int size) {
            return new PerformanceInfo[size];
        }
    };
    public float currentCpu;
    public int dalvMaxSize;
    public int dalvUsedSize;
    public int dalvikPrivateDirty;
    public int dalvikPss;
    public int dalvikSharedDirty;
    public double fps;
    public int loadTime;
    public int nativePrivateDirty;
    public int nativePss;
    public int nativeSharedDirty;
    public int otherPrivateDirty;
    public int otherPss;
    public int otherSharedDirty;
    public float totalCpu;
    public int totalPss;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dalvikPss);
        dest.writeInt(this.dalvikPrivateDirty);
        dest.writeInt(this.dalvikSharedDirty);
        dest.writeInt(this.nativePss);
        dest.writeInt(this.nativePrivateDirty);
        dest.writeInt(this.nativeSharedDirty);
        dest.writeInt(this.otherPss);
        dest.writeInt(this.otherPrivateDirty);
        dest.writeInt(this.otherSharedDirty);
        dest.writeInt(this.totalPss);
        dest.writeInt(this.dalvMaxSize);
        dest.writeInt(this.dalvUsedSize);
        dest.writeInt(this.loadTime);
        dest.writeFloat(this.totalCpu);
        dest.writeFloat(this.currentCpu);
        dest.writeDouble(this.fps);
    }

    public void readFromParcel(Parcel source) {
        this.dalvikPss = source.readInt();
        this.dalvikPrivateDirty = source.readInt();
        this.dalvikSharedDirty = source.readInt();
        this.nativePss = source.readInt();
        this.nativePrivateDirty = source.readInt();
        this.nativeSharedDirty = source.readInt();
        this.otherPss = source.readInt();
        this.otherPrivateDirty = source.readInt();
        this.otherSharedDirty = source.readInt();
        this.totalPss = source.readInt();
        this.dalvMaxSize = source.readInt();
        this.dalvUsedSize = source.readInt();
        this.loadTime = source.readInt();
        this.totalCpu = source.readFloat();
        this.currentCpu = source.readFloat();
        this.fps = source.readDouble();
    }

    private PerformanceInfo(Parcel source) {
        readFromParcel(source);
    }

    public PerformanceInfo() {
    }

    public String toString() {
        return "fps: " + this.fps + " loadTime: " + this.loadTime + " totalPss: " + this.totalPss + " totalcpu: " + this.totalCpu + " currentcpu: " + this.currentCpu;
    }
}
