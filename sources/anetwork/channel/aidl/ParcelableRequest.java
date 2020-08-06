package anetwork.channel.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import anet.channel.request.BodyEntry;
import anet.channel.util.ALog;
import anetwork.channel.Header;
import anetwork.channel.Param;
import anetwork.channel.Request;
import anetwork.channel.entity.BasicHeader;
import anetwork.channel.entity.StringParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParcelableRequest implements Parcelable {
    public static final Parcelable.Creator<ParcelableRequest> CREATOR = new Parcelable.Creator<ParcelableRequest>() {
        public ParcelableRequest createFromParcel(Parcel source) {
            return ParcelableRequest.readFromParcel(source);
        }

        public ParcelableRequest[] newArray(int size) {
            return new ParcelableRequest[size];
        }
    };
    private static final String TAG = "anet.ParcelableRequest";
    private String bizId;
    private BodyEntry bodyEntry;
    private String charset;
    private int connectTimeout;
    private Map<String, String> extProperties;
    private List<Header> headers = new ArrayList();
    private boolean isRedirect;
    private String method;
    private List<Param> params = new ArrayList();
    private int readTimeout;
    public long reqStartTime;
    private Request request;
    private int retryTime;
    private String seqNo;
    private String url;

    public ParcelableRequest(Request request2) {
        this.request = request2;
        if (request2 != null) {
            if (request2.getURI() != null) {
                this.url = request2.getURI().toString();
            } else if (request2.getURL() != null) {
                this.url = request2.getURL().toString();
            }
            this.retryTime = request2.getRetryTime();
            this.charset = request2.getCharset();
            this.isRedirect = request2.getFollowRedirects();
            this.headers = request2.getHeaders();
            this.method = request2.getMethod();
            this.params = request2.getParams();
            this.bodyEntry = request2.getBodyEntry();
            this.connectTimeout = request2.getConnectTimeout();
            this.readTimeout = request2.getReadTimeout();
            this.bizId = request2.getBizId();
            this.seqNo = request2.getSeqNo();
            this.extProperties = request2.getExtProperties();
        }
        this.reqStartTime = System.currentTimeMillis();
    }

    public ParcelableRequest() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.request != null) {
            try {
                dest.writeInt(this.request.getRetryTime());
                dest.writeString(this.url.toString());
                dest.writeString(this.request.getCharset());
                dest.writeBooleanArray(new boolean[]{this.request.getFollowRedirects()});
                dest.writeString(this.request.getMethod());
                ArrayList<String> hList = new ArrayList<>();
                if (this.request.getHeaders() != null) {
                    for (int i = 0; i < this.request.getHeaders().size(); i++) {
                        if (this.request.getHeaders().get(i) != null) {
                            hList.add(this.request.getHeaders().get(i).getName() + "&" + this.request.getHeaders().get(i).getValue());
                        }
                    }
                }
                dest.writeList(hList);
                List<Param> params2 = this.request.getParams();
                ArrayList<String> pList = new ArrayList<>();
                if (params2 != null) {
                    for (int i2 = 0; i2 < params2.size(); i2++) {
                        Param param = params2.get(i2);
                        if (param != null) {
                            pList.add(param.getKey() + "&" + param.getValue());
                        }
                    }
                }
                dest.writeList(pList);
                dest.writeParcelable(this.bodyEntry, 0);
                dest.writeLong(this.reqStartTime);
                dest.writeInt(this.request.getConnectTimeout());
                dest.writeInt(this.request.getReadTimeout());
                dest.writeString(this.request.getBizId());
                dest.writeString(this.request.getSeqNo());
                Map<String, String> map = this.request.getExtProperties();
                dest.writeInt(map == null ? 0 : 1);
                if (map != null) {
                    dest.writeMap(map);
                }
            } catch (Throwable e) {
                ALog.w(TAG, "[writeToParcel]", (String) null, e, new Object[0]);
            }
        }
    }

    public static ParcelableRequest readFromParcel(Parcel source) {
        int pos;
        int pos2;
        ParcelableRequest wrapper = new ParcelableRequest();
        try {
            wrapper.retryTime = source.readInt();
            wrapper.url = source.readString();
            wrapper.charset = source.readString();
            boolean[] redirect = new boolean[1];
            source.readBooleanArray(redirect);
            wrapper.isRedirect = redirect[0];
            wrapper.method = source.readString();
            ArrayList<String> headerTmp = new ArrayList<>();
            source.readList(headerTmp, ParcelableRequest.class.getClassLoader());
            if (headerTmp != null) {
                for (int i = 0; i < headerTmp.size(); i++) {
                    String tmp = headerTmp.get(i);
                    if (!(tmp == null || (pos2 = tmp.indexOf("&")) == -1 || pos2 == tmp.length() - 1)) {
                        wrapper.headers.add(new BasicHeader(tmp.substring(0, pos2), tmp.substring(pos2 + 1)));
                    }
                }
            }
            List<String> paramList = source.readArrayList(ParcelableRequest.class.getClassLoader());
            if (paramList != null) {
                for (int i2 = 0; i2 < paramList.size(); i2++) {
                    String paramStr = paramList.get(i2);
                    if (!(paramStr == null || (pos = paramStr.indexOf("&")) == -1 || pos == paramStr.length() - 1)) {
                        wrapper.params.add(new StringParam(paramStr.substring(0, pos), paramStr.substring(pos + 1)));
                    }
                }
            }
            wrapper.bodyEntry = (BodyEntry) source.readParcelable(ParcelableRequest.class.getClassLoader());
            wrapper.reqStartTime = source.readLong();
            wrapper.connectTimeout = source.readInt();
            wrapper.readTimeout = source.readInt();
            wrapper.bizId = source.readString();
            wrapper.seqNo = source.readString();
            if (source.readInt() != 0) {
                wrapper.extProperties = source.readHashMap(ParcelableRequest.class.getClassLoader());
            }
        } catch (Throwable e) {
            ALog.w(TAG, "[readFromParcel]", (String) null, e, new Object[0]);
        }
        return wrapper;
    }

    public String getCharset() {
        return this.charset;
    }

    public String getMethod() {
        return this.method;
    }

    public String getURL() {
        return this.url;
    }

    public boolean getFollowRedirects() {
        return this.isRedirect;
    }

    public BodyEntry getBodyEntry() {
        return this.bodyEntry;
    }

    public int getRetryTime() {
        return this.retryTime;
    }

    public List<Param> getParams() {
        return this.params;
    }

    public List<Header> getHeaders() {
        return this.headers;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public String getBizId() {
        return this.bizId;
    }

    public String getSeqNo() {
        return this.seqNo;
    }

    public String getExtProperty(String key) {
        if (this.extProperties == null) {
            return null;
        }
        return this.extProperties.get(key);
    }
}
