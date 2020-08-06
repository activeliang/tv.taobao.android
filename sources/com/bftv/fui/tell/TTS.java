package com.bftv.fui.tell;

import android.os.Parcel;
import android.os.Parcelable;

public class TTS implements Parcelable {
    public static final Parcelable.Creator<TTS> CREATOR = new Parcelable.Creator<TTS>() {
        public TTS createFromParcel(Parcel in) {
            return new TTS(in);
        }

        public TTS[] newArray(int size) {
            return new TTS[size];
        }
    };
    public boolean isDisplayLayout = true;
    public boolean isTTSClose;
    public boolean isTTSEnd;
    public String pck;
    public String tts;
    public String userTxt;

    public TTS() {
    }

    protected TTS(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.isTTSClose = in.readByte() != 0;
        this.tts = in.readString();
        this.pck = in.readString();
        if (in.readByte() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isTTSEnd = z;
        this.isDisplayLayout = in.readByte() == 0 ? false : z2;
        this.userTxt = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeByte((byte) (this.isTTSClose ? 1 : 0));
        dest.writeString(this.tts);
        dest.writeString(this.pck);
        if (this.isTTSEnd) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (!this.isDisplayLayout) {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
        dest.writeString(this.userTxt);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "TTS{isTTSClose=" + this.isTTSClose + ", tts='" + this.tts + '\'' + ", pck='" + this.pck + '\'' + ", isTTSEnd=" + this.isTTSEnd + ", isDisplayLayout=" + this.isDisplayLayout + ", userTxt='" + this.userTxt + '\'' + '}';
    }
}
