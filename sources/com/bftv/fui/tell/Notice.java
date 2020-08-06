package com.bftv.fui.tell;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;

public class Notice implements Parcelable {
    public static final Parcelable.Creator<Notice> CREATOR = new Parcelable.Creator<Notice>() {
        public Notice createFromParcel(Parcel in) {
            return new Notice(in);
        }

        public Notice[] newArray(int size) {
            return new Notice[size];
        }
    };
    public String img;
    public String message;
    public HashMap<String, String> noticeTipsMap;
    public String pck;
    public String title;

    public Notice() {
    }

    protected Notice(Parcel in) {
        this.img = in.readString();
        this.title = in.readString();
        this.message = in.readString();
        this.noticeTipsMap = in.readHashMap(HashMap.class.getClassLoader());
        this.pck = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.img);
        dest.writeString(this.title);
        dest.writeString(this.message);
        dest.writeMap(this.noticeTipsMap);
        dest.writeString(this.pck);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "Notice{img='" + this.img + '\'' + ", title='" + this.title + '\'' + ", message='" + this.message + '\'' + ", noticeTipsMap=" + this.noticeTipsMap + ", pck='" + this.pck + '\'' + '}';
    }
}
