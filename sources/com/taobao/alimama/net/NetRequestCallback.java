package com.taobao.alimama.net;

public interface NetRequestCallback {
    void onFinalFailed(String str, String str2);

    void onSuccess(String str, Object obj);

    void onTempFailed(String str, String str2);
}
