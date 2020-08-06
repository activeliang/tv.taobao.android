package com.taobao.adapter;

public interface LogAdapter {
    void onLogd(String str, String str2);

    void onLoge(String str, String str2);

    void onLogi(String str, String str2);

    void onLogv(String str, String str2);

    void onLogw(String str, String str2);
}
