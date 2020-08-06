package com.bftv.fui.thirdparty.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AllIntent implements Parcelable {
    public static final Parcelable.Creator<AllIntent> CREATOR = new Parcelable.Creator<AllIntent>() {
        public AllIntent createFromParcel(Parcel in) {
            return new AllIntent(in);
        }

        public AllIntent[] newArray(int size) {
            return new AllIntent[size];
        }
    };
    public String action_name;
    public String activity_name;
    public String category;
    public String entranceWords;
    public String flag;
    public String name;
    public String package_name;
    public String parameter;
    public String type;
    public String uri;

    public AllIntent() {
    }

    protected AllIntent(Parcel in) {
        this.parameter = in.readString();
        this.type = in.readString();
        this.flag = in.readString();
        this.name = in.readString();
        this.activity_name = in.readString();
        this.package_name = in.readString();
        this.action_name = in.readString();
        this.uri = in.readString();
        this.category = in.readString();
        this.entranceWords = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.parameter);
        dest.writeString(this.type);
        dest.writeString(this.flag);
        dest.writeString(this.name);
        dest.writeString(this.activity_name);
        dest.writeString(this.package_name);
        dest.writeString(this.action_name);
        dest.writeString(this.uri);
        dest.writeString(this.category);
        dest.writeString(this.entranceWords);
    }

    public int describeContents() {
        return 0;
    }
}
