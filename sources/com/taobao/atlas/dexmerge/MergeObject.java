package com.taobao.atlas.dexmerge;

import android.os.Parcel;
import android.os.Parcelable;

public class MergeObject implements Parcelable {
    public static final Parcelable.Creator<MergeObject> CREATOR = new Parcelable.Creator<MergeObject>() {
        public MergeObject createFromParcel(Parcel in) {
            return new MergeObject(in.readString(), in.readString(), in.readString());
        }

        public MergeObject[] newArray(int size) {
            return new MergeObject[size];
        }
    };
    public String mergeFile;
    public String originalFile;
    public String patchName;

    protected MergeObject(Parcel in) {
    }

    public MergeObject(String originalFile2, String patchFile, String mergeFile2) {
        this.originalFile = originalFile2;
        this.patchName = patchFile;
        this.mergeFile = mergeFile2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.originalFile);
        dest.writeString(this.patchName);
        dest.writeString(this.mergeFile);
    }
}
