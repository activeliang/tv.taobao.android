package com.taobao.wireless.detail.api;

import java.io.Serializable;

public class ApiResult<T> implements Serializable {
    protected String api;
    protected T data;
    public String[] ret = {"SUCCESS::调用成功"};
    protected String v;

    public T getData() {
        return this.data;
    }

    public void setData(T data2) {
        this.data = data2;
    }

    public String getApi() {
        return this.api;
    }

    public void setApi(String api2) {
        this.api = api2;
    }

    public String getV() {
        return this.v;
    }

    public void setV(String v2) {
        this.v = v2;
    }

    public String[] getRet() {
        return this.ret;
    }

    public void setRet(String[] ret2) {
        this.ret = ret2;
    }
}
