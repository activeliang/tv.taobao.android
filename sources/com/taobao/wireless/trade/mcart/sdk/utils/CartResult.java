package com.taobao.wireless.trade.mcart.sdk.utils;

public class CartResult {
    private String errorCode;
    private String errorMessage;
    private String flowType;
    private String orderByNative;
    private String orderH5Url;
    private boolean success = true;

    public String getOrderByNative() {
        return this.orderByNative;
    }

    public void setOrderByNative(String orderByNative2) {
        this.orderByNative = orderByNative2;
    }

    public String getFlowType() {
        return this.flowType;
    }

    public void setFlowType(String flowType2) {
        this.flowType = flowType2;
    }

    public String getOrderH5Url() {
        return this.orderH5Url;
    }

    public void setOrderH5Url(String orderH5Url2) {
        this.orderH5Url = orderH5Url2;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success2) {
        this.success = success2;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode2) {
        this.errorCode = errorCode2;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage2) {
        this.errorMessage = errorMessage2;
    }
}
