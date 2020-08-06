package com.taobao.adapter;

public interface CustomLibLoader {
    void loadLibrary(String str) throws UnsatisfiedLinkError, SecurityException;
}
