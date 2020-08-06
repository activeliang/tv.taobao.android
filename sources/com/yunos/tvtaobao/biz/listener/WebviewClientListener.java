package com.yunos.tvtaobao.biz.listener;

public interface WebviewClientListener {
    void onPageFinished();

    void onPageStarted();

    void onReceivedError(int i, String str, String str2);
}
