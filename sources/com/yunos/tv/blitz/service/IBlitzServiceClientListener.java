package com.yunos.tv.blitz.service;

public interface IBlitzServiceClientListener {
    void callItemServiceListenerIface(String str);

    void returnItemCallServiceIfaceResult(String str);

    void setItemStartServiceResult(String str);
}
