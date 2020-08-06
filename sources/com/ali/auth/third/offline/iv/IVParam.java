package com.ali.auth.third.offline.iv;

import android.os.Parcel;
import android.os.Parcelable;

public class IVParam implements Parcelable {
    public static final Parcelable.Creator<IVParam> CREATOR = new Parcelable.Creator<IVParam>() {
        public IVParam createFromParcel(Parcel source) {
            return new IVParam(source);
        }

        public IVParam[] newArray(int size) {
            return new IVParam[size];
        }
    };
    public String checkCode;
    public String ivToken;
    public String locale;
    public String mobileNum;
    public String ncSessionId;
    public String ncSignature;
    public String ncToken;
    public String sessionId;
    public String validatorTag;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.checkCode);
        dest.writeString(this.ivToken);
        dest.writeString(this.validatorTag);
        dest.writeString(this.mobileNum);
        dest.writeString(this.sessionId);
        dest.writeString(this.ncSessionId);
        dest.writeString(this.ncToken);
        dest.writeString(this.ncSignature);
    }

    public IVParam() {
    }

    protected IVParam(Parcel in) {
        this.checkCode = in.readString();
        this.ivToken = in.readString();
        this.validatorTag = in.readString();
        this.mobileNum = in.readString();
        this.sessionId = in.readString();
        this.ncSessionId = in.readString();
        this.ncToken = in.readString();
        this.ncSignature = in.readString();
    }
}
