package com.bftv.fui.thirdparty.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Tips implements Parcelable {
    public static final Parcelable.Creator<Tips> CREATOR = new Parcelable.Creator<Tips>() {
        public Tips createFromParcel(Parcel in) {
            return new Tips(in);
        }

        public Tips[] newArray(int size) {
            return new Tips[size];
        }
    };
    public String content;
    public String name;
    public int type;

    public Tips() {
    }

    protected Tips(Parcel in) {
        this.name = in.readString();
        this.content = in.readString();
        this.type = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.content);
        dest.writeInt(this.type);
    }

    public int describeContents() {
        return 0;
    }
}
