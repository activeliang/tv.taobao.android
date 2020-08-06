package com.yunos.tvtaobao.biz.interfaces;

public interface IElemBindCallBack<T> {
    void cancel();

    void onSuccess(T t);
}
