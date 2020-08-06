package com.yunos.tv.blitz.service;

public interface IBlitzServiceCallAndroidListener {
    boolean bindService(String str);

    String callInterfaceMethod(String str);

    boolean unBindService(String str);
}
