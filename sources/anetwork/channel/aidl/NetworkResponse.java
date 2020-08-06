package anetwork.channel.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anetwork.channel.Response;
import anetwork.channel.statist.StatisticData;
import java.util.List;
import java.util.Map;

public class NetworkResponse implements Response, Parcelable {
    public static final Parcelable.Creator<NetworkResponse> CREATOR = new Parcelable.Creator<NetworkResponse>() {
        public NetworkResponse createFromParcel(Parcel source) {
            return NetworkResponse.readFromParcel(source);
        }

        public NetworkResponse[] newArray(int size) {
            return new NetworkResponse[size];
        }
    };
    private static final String TAG = "anet.NetworkResponse";
    byte[] bytedata;
    private Map<String, List<String>> connHeadFields;
    private String desc;
    private Throwable error;
    private StatisticData statisticData;
    int statusCode;

    public void setStatusCode(int statusCode2) {
        this.statusCode = statusCode2;
        this.desc = ErrorConstant.getErrMsg(statusCode2);
    }

    public byte[] getBytedata() {
        return this.bytedata;
    }

    public void setBytedata(byte[] bytedata2) {
        this.bytedata = bytedata2;
    }

    public void setConnHeadFields(Map<String, List<String>> connHeadFields2) {
        this.connHeadFields = connHeadFields2;
    }

    public Map<String, List<String>> getConnHeadFields() {
        return this.connHeadFields;
    }

    public void setDesc(String desc2) {
        this.desc = desc2;
    }

    public String getDesc() {
        return this.desc;
    }

    public boolean isHttpSuccess() {
        return true;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("NetworkResponse [");
        builder.append("statusCode=").append(this.statusCode);
        builder.append(", desc=").append(this.desc);
        builder.append(", connHeadFields=").append(this.connHeadFields);
        builder.append(", bytedata=").append(this.bytedata != null ? new String(this.bytedata) : "");
        builder.append(", error=").append(this.error);
        builder.append(", statisticData=").append(this.statisticData).append("]");
        return builder.toString();
    }

    public NetworkResponse() {
    }

    public NetworkResponse(int httpcode) {
        this(httpcode, (byte[]) null, (Map<String, List<String>>) null);
    }

    public NetworkResponse(int httpcode, byte[] b, Map<String, List<String>> connHeadFields2) {
        this.statusCode = httpcode;
        this.desc = ErrorConstant.getErrMsg(httpcode);
        this.bytedata = b;
        this.connHeadFields = connHeadFields2;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public Throwable getError() {
        return this.error;
    }

    public void setError(Throwable error2) {
        this.error = error2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.statusCode);
        dest.writeString(this.desc);
        int byteLen = 0;
        if (this.bytedata != null) {
            byteLen = this.bytedata.length;
        }
        dest.writeInt(byteLen);
        if (byteLen > 0) {
            dest.writeByteArray(this.bytedata);
        }
        dest.writeMap(this.connHeadFields);
        if (this.statisticData != null) {
            dest.writeSerializable(this.statisticData);
        }
    }

    public static NetworkResponse readFromParcel(Parcel source) {
        NetworkResponse response = new NetworkResponse();
        try {
            response.statusCode = source.readInt();
            response.desc = source.readString();
            int byteLen = source.readInt();
            if (byteLen > 0) {
                response.bytedata = new byte[byteLen];
                source.readByteArray(response.bytedata);
            }
            response.connHeadFields = source.readHashMap(NetworkResponse.class.getClassLoader());
            try {
                response.statisticData = (StatisticData) source.readSerializable();
            } catch (Throwable th) {
                ALog.i(TAG, "[readFromParcel] source.readSerializable() error", (String) null, new Object[0]);
            }
        } catch (Exception e) {
            ALog.w(TAG, "[readFromParcel]", (String) null, e, new Object[0]);
        }
        return response;
    }

    public void setStatisticData(StatisticData statisticData2) {
        this.statisticData = statisticData2;
    }

    public StatisticData getStatisticData() {
        return this.statisticData;
    }
}
