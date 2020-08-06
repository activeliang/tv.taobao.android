package com.zhiping.tvtao.payment.alipay.request.base;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseMtopRequest {
    private HashMap<String, String> extraParams;
    private boolean isPost;
    private boolean needAuth = false;
    private boolean needCache = false;
    private boolean needEcode = false;
    private boolean needLogin = false;
    private boolean needUmt = false;
    private boolean needWua = true;
    private int timeOut = -1;

    public abstract String getApi();

    public abstract String getApiVersion();

    public boolean isPost() {
        return this.isPost;
    }

    public void setPost(boolean post) {
        this.isPost = post;
    }

    public void setNeedAuth(boolean needAuth2) {
        this.needAuth = needAuth2;
    }

    public boolean isNeedAuth() {
        return this.needAuth;
    }

    public void setNeedCache(boolean needCache2) {
        this.needCache = needCache2;
    }

    public boolean isNeedCache() {
        return this.needCache;
    }

    public void setNeedLogin(boolean needLogin2) {
        this.needLogin = needLogin2;
    }

    public boolean isNeedLogin() {
        return this.needLogin;
    }

    public void setNeedWua(boolean needWua2) {
        this.needWua = needWua2;
    }

    public boolean isNeedWua() {
        return this.needWua;
    }

    public void setNeedUmt(boolean needUmt2) {
        this.needUmt = needUmt2;
    }

    public boolean isNeedUmt() {
        return this.needUmt;
    }

    public void setTimeOut(int timeOut2) {
        this.timeOut = timeOut2;
    }

    public int getTimeOut() {
        return this.timeOut;
    }

    public boolean isNeedEcode() {
        return this.needEcode;
    }

    public void setNeedEcode(boolean needEcode2) {
        this.needEcode = needEcode2;
    }

    public Map<String, String> getParams() {
        return null;
    }

    public final void addExtraParams(Map<String, String> extParams) {
        if (extParams != null && !extParams.isEmpty()) {
            if (this.extraParams == null) {
                this.extraParams = new HashMap<>();
            }
            this.extraParams.putAll(extParams);
        }
    }

    public final void putExtraParams(String key, String value) {
        if (!TextUtils.isEmpty(key) && value != null) {
            if (this.extraParams == null) {
                this.extraParams = new HashMap<>();
            }
            this.extraParams.put(key, value);
        }
    }

    public final HashMap<String, String> getExtraParams() {
        return this.extraParams;
    }
}
