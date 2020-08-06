package com.tvtaobao.voicesdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PageReturn implements Parcelable {
    public static final Parcelable.Creator<PageReturn> CREATOR = new Parcelable.Creator<PageReturn>() {
        public PageReturn createFromParcel(Parcel in) {
            return new PageReturn(in);
        }

        public PageReturn[] newArray(int size) {
            return new PageReturn[size];
        }
    };
    public String feedback = "";
    public boolean isHandler = false;

    public PageReturn() {
    }

    protected PageReturn(Parcel in) {
        boolean z = false;
        this.isHandler = in.readByte() != 0 ? true : z;
        this.feedback = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (this.isHandler ? 1 : 0));
        dest.writeString(this.feedback);
    }
}
