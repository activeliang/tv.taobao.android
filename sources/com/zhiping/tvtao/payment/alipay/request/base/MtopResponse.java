package com.zhiping.tvtao.payment.alipay.request.base;

import org.json.JSONObject;

public class MtopResponse {
    private String errorCode;
    private String errorMsg;
    private String httpCode;
    private boolean isSuccess;
    private JSONObject jsonData;

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorCode(String errorCode2) {
        this.errorCode = errorCode2;
    }

    public void setErrorMsg(String errorMsg2) {
        this.errorMsg = errorMsg2;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public String getHttpCode() {
        return this.httpCode;
    }

    public void setHttpCode(String httpCode2) {
        this.httpCode = httpCode2;
    }

    public JSONObject getJsonData() {
        return this.jsonData;
    }

    public void setJsonData(JSONObject jsonData2) {
        this.jsonData = jsonData2;
    }
}
