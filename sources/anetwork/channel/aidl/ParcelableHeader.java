package anetwork.channel.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import anet.channel.util.ALog;
import java.util.List;
import java.util.Map;

public class ParcelableHeader implements Parcelable {
    public static Parcelable.Creator<ParcelableHeader> CREATOR = new Parcelable.Creator<ParcelableHeader>() {
        public ParcelableHeader createFromParcel(Parcel source) {
            return ParcelableHeader.readFromParcel(source);
        }

        public ParcelableHeader[] newArray(int size) {
            return new ParcelableHeader[size];
        }
    };
    private static final String TAG = "anet.ParcelableHeader";
    public Map<String, List<String>> header;
    public int responseCode;

    public ParcelableHeader(int responseCode2, Map<String, List<String>> header2) {
        this.header = header2;
        this.responseCode = responseCode2;
    }

    ParcelableHeader() {
    }

    public Map<String, List<String>> getHeader() {
        return this.header;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.header != null) {
            dest.writeInt(1);
            dest.writeMap(this.header);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(this.responseCode);
    }

    static ParcelableHeader readFromParcel(Parcel source) {
        ParcelableHeader ret = new ParcelableHeader();
        try {
            if (source.readInt() == 1) {
                ret.header = source.readHashMap(ParcelableHeader.class.getClassLoader());
            }
            ret.responseCode = source.readInt();
        } catch (Throwable e) {
            ALog.e(TAG, "[readFromParcel]", (String) null, e, new Object[0]);
        }
        return ret;
    }

    public String toString() {
        return "ParcelableResponseHeader [responseCode=" + this.responseCode + ", header=" + this.header + "]";
    }
}
