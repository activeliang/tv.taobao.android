package com.bftv.fui.thirdparty;

import android.os.Parcel;
import android.os.Parcelable;
import com.bftv.fui.thirdparty.bean.MiddleData;
import com.bftv.fui.thirdparty.bean.Tips;
import java.util.List;

public class VoiceFeedback implements Parcelable {
    public static final Parcelable.Creator<VoiceFeedback> CREATOR = new Parcelable.Creator<VoiceFeedback>() {
        public VoiceFeedback createFromParcel(Parcel in) {
            return new VoiceFeedback(in);
        }

        public VoiceFeedback[] newArray(int size) {
            return new VoiceFeedback[size];
        }
    };
    public static final int TYPE_CMD = 1;
    public static final int TYPE_MIDDLE = 2;
    public String feedback;
    public boolean isHasResult;
    public List<MiddleData> listMiddleData;
    public List<String> listPrompts;
    public List<Tips> listTips;
    public int type;

    public VoiceFeedback() {
    }

    protected VoiceFeedback(Parcel in) {
        this.feedback = in.readString();
        this.listPrompts = in.createStringArrayList();
        this.isHasResult = in.readByte() != 0;
        this.type = in.readInt();
        this.listMiddleData = in.createTypedArrayList(MiddleData.CREATOR);
        this.listTips = in.createTypedArrayList(Tips.CREATOR);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.feedback);
        dest.writeStringList(this.listPrompts);
        dest.writeByte((byte) (this.isHasResult ? 1 : 0));
        dest.writeInt(this.type);
        dest.writeTypedList(this.listMiddleData);
        dest.writeTypedList(this.listTips);
    }

    public int describeContents() {
        return 0;
    }
}
