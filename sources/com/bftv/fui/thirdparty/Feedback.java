package com.bftv.fui.thirdparty;

import android.os.Parcel;
import android.os.Parcelable;

public class Feedback implements Parcelable {
    public static final Parcelable.Creator<Feedback> CREATOR = new Parcelable.Creator<Feedback>() {
        public Feedback createFromParcel(Parcel in) {
            return new Feedback(in);
        }

        public Feedback[] newArray(int size) {
            return new Feedback[size];
        }
    };
    public boolean isHasResult;
    public String ttsContent;
    public String type;
    public String url;

    public Feedback() {
    }

    protected Feedback(Parcel in) {
        this.ttsContent = in.readString();
        this.isHasResult = in.readByte() != 0;
        this.type = in.readString();
        this.url = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ttsContent);
        dest.writeByte((byte) (this.isHasResult ? 1 : 0));
        dest.writeString(this.type);
        dest.writeString(this.url);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "Feedback{ttsContent='" + this.ttsContent + '\'' + ", isHasResult=" + this.isHasResult + ", type='" + this.type + '\'' + ", url='" + this.url + '\'' + '}';
    }
}
