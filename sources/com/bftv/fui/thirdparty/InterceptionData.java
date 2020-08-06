package com.bftv.fui.thirdparty;

import android.os.Parcel;
import android.os.Parcelable;

public class InterceptionData implements Parcelable {
    public static final Parcelable.Creator<InterceptionData> CREATOR = new Parcelable.Creator<InterceptionData>() {
        public InterceptionData createFromParcel(Parcel in) {
            return new InterceptionData(in);
        }

        public InterceptionData[] newArray(int size) {
            return new InterceptionData[size];
        }
    };
    public int age;
    public String className;
    public String flag;
    public int functionType;
    public int groupId;
    public int index;
    public String needValue;
    public String nlpType;
    public String pck;
    public int sex;
    public int tellType;
    public String temp1;
    public String temp2;

    public InterceptionData() {
    }

    protected InterceptionData(Parcel in) {
        this.age = in.readInt();
        this.sex = in.readInt();
        this.index = in.readInt();
        this.pck = in.readString();
        this.className = in.readString();
        this.tellType = in.readInt();
        this.flag = in.readString();
        this.temp1 = in.readString();
        this.temp2 = in.readString();
        this.needValue = in.readString();
        this.nlpType = in.readString();
        this.functionType = in.readInt();
        this.groupId = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.age);
        dest.writeInt(this.sex);
        dest.writeInt(this.index);
        dest.writeString(this.pck);
        dest.writeString(this.className);
        dest.writeInt(this.tellType);
        dest.writeString(this.flag);
        dest.writeString(this.temp1);
        dest.writeString(this.temp2);
        dest.writeString(this.needValue);
        dest.writeString(this.nlpType);
        dest.writeInt(this.functionType);
        dest.writeInt(this.groupId);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "InterceptionData{age=" + this.age + ", sex=" + this.sex + ", index=" + this.index + ", pck='" + this.pck + '\'' + ", className='" + this.className + '\'' + ", tellType=" + this.tellType + ", flag='" + this.flag + '\'' + ", temp1='" + this.temp1 + '\'' + ", temp2='" + this.temp2 + '\'' + ", needValue='" + this.needValue + '\'' + ", nlpType='" + this.nlpType + '\'' + ", functionType=" + this.functionType + ", groupId=" + this.groupId + '}';
    }
}
