package anetwork.channel.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import anet.channel.util.ErrorConstant;
import anetwork.channel.NetworkEvent;
import anetwork.channel.statist.StatisticData;

public class DefaultFinishEvent implements NetworkEvent.FinishEvent, Parcelable {
    public static final Parcelable.Creator<DefaultFinishEvent> CREATOR = new Parcelable.Creator<DefaultFinishEvent>() {
        public DefaultFinishEvent createFromParcel(Parcel source) {
            return DefaultFinishEvent.readFromParcel(source);
        }

        public DefaultFinishEvent[] newArray(int size) {
            return new DefaultFinishEvent[size];
        }
    };
    private static final String TAG = "anet.DefaultFinishEvent";
    int code;
    Object context;
    String desc;
    StatisticData statisticData;

    public DefaultFinishEvent() {
    }

    public Object getContext() {
        return this.context;
    }

    public void setContext(Object context2) {
        this.context = context2;
    }

    public int getHttpCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setCode(int code2) {
        this.code = code2;
    }

    public void setDesc(String desc2) {
        this.desc = desc2;
    }

    public void setStatisticData(StatisticData data) {
        this.statisticData = data;
    }

    public StatisticData getStatisticData() {
        return this.statisticData;
    }

    public DefaultFinishEvent(int code2) {
        this(code2, (String) null, (StatisticData) null);
    }

    public DefaultFinishEvent(int code2, String msg, StatisticData statisticData2) {
        this.code = code2;
        this.desc = msg == null ? ErrorConstant.getErrMsg(code2) : msg;
        this.statisticData = statisticData2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("DefaultFinishEvent [");
        builder.append("code=").append(this.code);
        builder.append(", desc=").append(this.desc);
        builder.append(", context=").append(this.context);
        builder.append(", statisticData=").append(this.statisticData);
        builder.append("]");
        return builder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.desc);
        if (this.statisticData != null) {
            dest.writeSerializable(this.statisticData);
        }
    }

    static DefaultFinishEvent readFromParcel(Parcel source) {
        DefaultFinishEvent ret = new DefaultFinishEvent();
        try {
            ret.code = source.readInt();
            ret.desc = source.readString();
            try {
                ret.statisticData = (StatisticData) source.readSerializable();
            } catch (Throwable th) {
            }
        } catch (Throwable th2) {
        }
        return ret;
    }
}
