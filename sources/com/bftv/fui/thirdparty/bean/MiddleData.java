package com.bftv.fui.thirdparty.bean;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class MiddleData implements Parcelable {
    public static final Parcelable.Creator<MiddleData> CREATOR = new Parcelable.Creator<MiddleData>() {
        public MiddleData createFromParcel(Parcel in) {
            return new MiddleData(in);
        }

        public MiddleData[] newArray(int size) {
            return new MiddleData[size];
        }
    };
    public String content;
    public String detail;
    public int index;
    public boolean isBuyTips = true;
    public boolean isCommodity = true;
    public boolean isFastTips = true;
    public String label;
    public List<AllIntent> listIntent;
    public List<String> listLabel;
    public String middleName;
    public String middlePic;
    public String price;
    public String sales;
    public String title;

    public MiddleData() {
    }

    protected MiddleData(Parcel in) {
        boolean z;
        boolean z2;
        boolean z3 = true;
        this.middleName = in.readString();
        this.middlePic = in.readString();
        this.price = in.readString();
        this.sales = in.readString();
        this.title = in.readString();
        this.detail = in.readString();
        this.listLabel = in.createStringArrayList();
        this.label = in.readString();
        this.listIntent = in.createTypedArrayList(AllIntent.CREATOR);
        this.content = in.readString();
        if (in.readByte() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isCommodity = z;
        if (in.readByte() != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.isFastTips = z2;
        this.isBuyTips = in.readByte() == 0 ? false : z3;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2;
        int i3 = 1;
        dest.writeString(this.middleName);
        dest.writeString(this.middlePic);
        dest.writeString(this.price);
        dest.writeString(this.sales);
        dest.writeString(this.title);
        dest.writeString(this.detail);
        dest.writeStringList(this.listLabel);
        dest.writeString(this.label);
        dest.writeTypedList(this.listIntent);
        dest.writeString(this.content);
        if (this.isCommodity) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (this.isFastTips) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
        if (!this.isBuyTips) {
            i3 = 0;
        }
        dest.writeByte((byte) i3);
    }

    public int describeContents() {
        return 0;
    }
}
