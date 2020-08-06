package com.yunos.tv.blitz.request.core;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class ServiceResponse<T> {
    private T data;
    private String errorCode;
    private String errorMsg;
    private List<RequestErrorListener> mRequestErrorListenerList = new ArrayList();
    private String raw_data;
    JSONArray retdata;
    private Integer statusCode = Integer.valueOf(ServiceCode.SERVICE_OK.getCode());

    public interface RequestErrorListener {
        boolean onError(int i, String str);
    }

    public void setRetArray(JSONArray arr) {
        this.retdata = arr;
    }

    public JSONArray getRetArray() {
        return this.retdata;
    }

    public void setRawData(String data2) {
        this.raw_data = data2;
    }

    public String getRawData() {
        return this.raw_data;
    }

    public String toString() {
        return "ServiceResponse [statusCode=" + this.statusCode + ", errorMsg=" + this.errorMsg + ", errorCode=" + this.errorCode + ", data=" + this.data + "]";
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(Integer code) {
        this.statusCode = code;
    }

    public void update(int statusCode2, String errorCode2, String errorMsg2, T data2) {
        update(statusCode2, errorCode2, errorMsg2);
        this.data = data2;
    }

    public void update(int statusCode2, String errorCode2, String errorMsg2) {
        this.statusCode = Integer.valueOf(statusCode2);
        this.errorCode = errorCode2;
        this.errorMsg = errorMsg2;
        this.data = null;
    }

    public void update(ServiceCode serviceCode) {
        if (serviceCode != null) {
            this.statusCode = Integer.valueOf(serviceCode.getCode());
            this.errorCode = String.valueOf(serviceCode.getCode());
            this.errorMsg = serviceCode.getMsg();
        }
    }

    public void update(ServiceCode serviceCode, T data2) {
        update(serviceCode);
        this.data = data2;
    }

    public boolean isSucess() {
        return this.statusCode.intValue() == ServiceCode.SERVICE_OK.getCode();
    }

    public boolean isNetError() {
        return this.statusCode.intValue() == ServiceCode.HTTP_ERROR.getCode();
    }

    public boolean isNotLogin() {
        return this.statusCode != null && this.statusCode.intValue() == ServiceCode.API_NOT_LOGIN.getCode();
    }

    public boolean isSessionTimeout() {
        return this.statusCode != null && this.statusCode.intValue() == ServiceCode.API_SID_INVALID.getCode();
    }

    public boolean isApiError() {
        return this.statusCode != null && this.statusCode.intValue() == ServiceCode.API_ERROR.getCode();
    }

    public boolean isAuthReject() {
        return this.statusCode != null && this.statusCode.intValue() == ServiceCode.API_ERRCODE_AUTH_REJECT.getCode();
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data2) {
        this.data = data2;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String msg) {
        this.errorMsg = msg;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode2) {
        this.errorCode = errorCode2;
    }

    public void addErrorListener(RequestErrorListener listener) {
        this.mRequestErrorListenerList.add(listener);
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x000c A[LOOP:0: B:3:0x000c->B:6:0x0024, LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void actionErrorListener() {
        /*
            r5 = this;
            boolean r2 = r5.isSucess()
            if (r2 != 0) goto L_0x0026
            java.util.List<com.yunos.tv.blitz.request.core.ServiceResponse$RequestErrorListener> r2 = r5.mRequestErrorListenerList
            java.util.Iterator r2 = r2.iterator()
        L_0x000c:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0026
            java.lang.Object r0 = r2.next()
            com.yunos.tv.blitz.request.core.ServiceResponse$RequestErrorListener r0 = (com.yunos.tv.blitz.request.core.ServiceResponse.RequestErrorListener) r0
            java.lang.Integer r3 = r5.statusCode
            int r3 = r3.intValue()
            java.lang.String r4 = r5.errorMsg
            boolean r1 = r0.onError(r3, r4)
            if (r1 == 0) goto L_0x000c
        L_0x0026:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.blitz.request.core.ServiceResponse.actionErrorListener():void");
    }
}
