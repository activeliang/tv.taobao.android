package com.yunos.tv.alitvasrsdk;

public interface OnTtsListener {
    void onException(String str);

    void onTtsStart(String str);

    void onTtsStop();
}
