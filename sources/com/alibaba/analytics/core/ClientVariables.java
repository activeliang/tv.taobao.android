package com.alibaba.analytics.core;

import android.content.Context;

public class ClientVariables {
    public static final ClientVariables s_instance = new ClientVariables();
    private volatile String appKey;
    private volatile Context mContext = null;
    private volatile boolean mIs1010AutoTrackClosed = false;
    private volatile boolean mIsAliyunOSPlatform = false;
    private volatile String mOutsideTTID = null;

    private ClientVariables() {
    }

    public void setToAliyunOSPlatform() {
        this.mIsAliyunOSPlatform = true;
    }

    public boolean isAliyunOSPlatform() {
        return this.mIsAliyunOSPlatform;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey2) {
        this.appKey = appKey2;
    }

    public static ClientVariables getInstance() {
        return s_instance;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void setContext(Context aContext) {
        this.mContext = aContext;
    }

    public void set1010AutoTrackClose() {
        this.mIs1010AutoTrackClosed = true;
    }

    public boolean is1010AutoTrackClosed() {
        return this.mIs1010AutoTrackClosed;
    }

    public void setOutsideTTID(String aTTID) {
        this.mOutsideTTID = aTTID;
    }

    public String getOutsideTTID() {
        return this.mOutsideTTID;
    }
}
