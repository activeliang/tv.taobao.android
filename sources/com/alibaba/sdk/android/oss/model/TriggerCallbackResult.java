package com.alibaba.sdk.android.oss.model;

public class TriggerCallbackResult extends OSSResult {
    private String mServerCallbackReturnBody;

    public String getServerCallbackReturnBody() {
        return this.mServerCallbackReturnBody;
    }

    public void setServerCallbackReturnBody(String serverCallbackReturnBody) {
        this.mServerCallbackReturnBody = serverCallbackReturnBody;
    }
}
