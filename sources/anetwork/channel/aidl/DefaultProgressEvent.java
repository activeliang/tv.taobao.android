package anetwork.channel.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import anetwork.channel.NetworkEvent;

public class DefaultProgressEvent implements NetworkEvent.ProgressEvent, Parcelable {
    public static final Parcelable.Creator<DefaultProgressEvent> CREATOR = new Parcelable.Creator<DefaultProgressEvent>() {
        public DefaultProgressEvent createFromParcel(Parcel source) {
            return DefaultProgressEvent.readFromParcel(source);
        }

        public DefaultProgressEvent[] newArray(int size) {
            return new DefaultProgressEvent[size];
        }
    };
    private static final String TAG = "anet.DefaultProgressEvent";
    Object context;
    String desc;
    int index;
    byte[] out;
    int size;
    int total;

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc2) {
        this.desc = desc2;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size2) {
        this.size = size2;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total2) {
        this.total = total2;
    }

    public Object getContext() {
        return this.context;
    }

    public void setContext(Object context2) {
        this.context = context2;
    }

    public byte[] getBytedata() {
        return this.out;
    }

    public void setBytedata(byte[] out2) {
        this.out = out2;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index2) {
        this.index = index2;
    }

    public String toString() {
        return "DefaultProgressEvent [index=" + this.index + ", size=" + this.size + ", total=" + this.total + ", desc=" + this.desc + "]";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
        dest.writeInt(this.size);
        dest.writeInt(this.total);
        dest.writeString(this.desc);
        dest.writeInt(this.out != null ? this.out.length : 0);
        dest.writeByteArray(this.out);
    }

    public static DefaultProgressEvent readFromParcel(Parcel soruce) {
        DefaultProgressEvent event = new DefaultProgressEvent();
        try {
            event.index = soruce.readInt();
            event.size = soruce.readInt();
            event.total = soruce.readInt();
            event.desc = soruce.readString();
            int len = soruce.readInt();
            if (len > 0) {
                byte[] o = new byte[len];
                soruce.readByteArray(o);
                event.out = o;
            }
        } catch (Exception e) {
        }
        return event;
    }
}
