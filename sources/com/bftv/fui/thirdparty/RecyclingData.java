package com.bftv.fui.thirdparty;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;

public class RecyclingData implements Parcelable {
    public static final Parcelable.Creator<RecyclingData> CREATOR = new Parcelable.Creator<RecyclingData>() {
        public RecyclingData createFromParcel(Parcel in) {
            return new RecyclingData(in);
        }

        public RecyclingData[] newArray(int size) {
            return new RecyclingData[size];
        }
    };
    public HashMap<String, String> cacheMap;
    public String className;
    public String pck;

    public RecyclingData() {
    }

    protected RecyclingData(Parcel in) {
        this.pck = in.readString();
        this.className = in.readString();
        this.cacheMap = in.readHashMap(HashMap.class.getClassLoader());
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pck);
        dest.writeString(this.className);
        dest.writeMap(this.cacheMap);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "RecyclingData{pck='" + this.pck + '\'' + ", className='" + this.className + '\'' + ", cacheMap=" + this.cacheMap + '}';
    }
}
