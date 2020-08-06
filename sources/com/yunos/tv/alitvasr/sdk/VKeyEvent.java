package com.yunos.tv.alitvasr.sdk;

import android.os.Parcel;
import android.os.Parcelable;

public class VKeyEvent implements Parcelable {
    public static final Parcelable.Creator<VKeyEvent> CREATOR = new Parcelable.Creator<VKeyEvent>() {
        public VKeyEvent createFromParcel(Parcel in) {
            return new VKeyEvent(in);
        }

        public VKeyEvent[] newArray(int size) {
            return new VKeyEvent[size];
        }
    };
    public int[] actions;
    public int[] keyCodes;
    public int size;

    public VKeyEvent(Parcel in) {
        this.size = in.readInt();
        this.keyCodes = in.createIntArray();
        this.actions = in.createIntArray();
    }

    public VKeyEvent(int action, int keyCode) {
        this.size = 1;
        this.keyCodes = new int[1];
        this.keyCodes[0] = keyCode;
        this.actions = new int[1];
        this.actions[0] = action;
    }

    public VKeyEvent(int size2, int[] keyCodes2, int[] actions2) {
        this.size = size2;
        this.keyCodes = keyCodes2;
        this.actions = actions2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.size);
        parcel.writeIntArray(this.keyCodes);
        parcel.writeIntArray(this.actions);
    }
}
