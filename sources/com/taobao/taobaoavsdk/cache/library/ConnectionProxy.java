package com.taobao.taobaoavsdk.cache.library;

import android.os.RemoteException;
import android.util.Log;
import anet.channel.util.HttpHelper;
import anetwork.channel.aidl.Connection;
import java.net.HttpURLConnection;

public class ConnectionProxy {
    private HttpURLConnection mHttpUrlConnection;
    private Connection mNetConnetion;

    private ConnectionProxy() {
    }

    public ConnectionProxy(Connection connection) {
        this.mNetConnetion = connection;
    }

    public ConnectionProxy(HttpURLConnection connection) {
        this.mHttpUrlConnection = connection;
    }

    public String getStatisticData() {
        try {
            if (this.mNetConnetion != null && this.mNetConnetion.getStatisticData() != null) {
                return this.mNetConnetion.getStatisticData().sumNetStat() + ",netType=TBNet" + ",xCache=" + getHeaderField("X-Cache");
            }
            if (this.mHttpUrlConnection != null) {
                StringBuilder sumBuilder = new StringBuilder(128);
                sumBuilder.append(",host=").append(this.mHttpUrlConnection.getRequestProperty("Host"));
                sumBuilder.append(",resultCode=").append(this.mHttpUrlConnection.getResponseCode());
                sumBuilder.append(",connType=").append(this.mHttpUrlConnection.getHeaderField("X-Android-Selected-Protocol"));
                sumBuilder.append(",recDataTime=").append(Long.valueOf(this.mHttpUrlConnection.getHeaderField("X-Android-Received-Millis")).longValue() - Long.valueOf(this.mHttpUrlConnection.getHeaderField("X-Android-Sent-Millis")).longValue());
                sumBuilder.append(",totalSize=").append(this.mHttpUrlConnection.getContentLength());
                sumBuilder.append(",netType=").append("SYSNet");
                sumBuilder.append(",xCache=").append(this.mHttpUrlConnection.getHeaderField("X-Cache"));
                return sumBuilder.toString();
            }
            return "";
        } catch (RemoteException e) {
            Log.e("TBNetStatistic", "getStatisticData error:" + e.getMessage());
        } catch (Exception e2) {
            Log.e("TBNetStatistic", "getStatisticData error:" + e2.getMessage());
        }
    }

    public InputStreamProxy getInputStream() throws Exception {
        if (this.mNetConnetion != null) {
            return new InputStreamProxy(this.mNetConnetion.getInputStream());
        }
        if (this.mHttpUrlConnection != null) {
            return new InputStreamProxy(this.mHttpUrlConnection.getInputStream());
        }
        return null;
    }

    public int getResponseCode() throws Exception {
        if (this.mNetConnetion != null) {
            return this.mNetConnetion.getStatusCode();
        }
        if (this.mHttpUrlConnection != null) {
            return this.mHttpUrlConnection.getResponseCode();
        }
        return 0;
    }

    public String getHeaderContentType(String field, String defaultValue) {
        try {
            return getHeaderField(field);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int getHeaderFieldInt(String field, int defaultValue) {
        try {
            return Integer.parseInt(getHeaderField(field));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public String getHeaderField(String key) {
        try {
            if (this.mNetConnetion != null) {
                return HttpHelper.getSingleHeaderFieldByKey(this.mNetConnetion.getConnHeadFields(), key);
            }
            if (this.mHttpUrlConnection != null) {
                return this.mHttpUrlConnection.getHeaderField(key);
            }
            return "";
        } catch (Exception e) {
        }
    }

    public void disconnect() throws Exception {
        if (this.mNetConnetion != null) {
            this.mNetConnetion.cancel();
        }
        if (this.mHttpUrlConnection != null) {
            this.mHttpUrlConnection.disconnect();
        }
    }
}
