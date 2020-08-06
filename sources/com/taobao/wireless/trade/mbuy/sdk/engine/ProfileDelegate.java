package com.taobao.wireless.trade.mbuy.sdk.engine;

public interface ProfileDelegate {
    void commitEvent(int i);

    void commitEvent(int i, Object obj);

    void commitEvent(int i, Object obj, Object obj2);

    void commitEvent(int i, Object obj, Object obj2, Object obj3);

    void commitEvent(int i, Object obj, Object obj2, Object obj3, String... strArr);

    void commitEvent(String str, int i);

    void commitEvent(String str, int i, Object obj);

    void commitEvent(String str, int i, Object obj, Object obj2);

    void commitEvent(String str, int i, Object obj, Object obj2, Object obj3);

    void commitEvent(String str, int i, Object obj, Object obj2, Object obj3, String... strArr);
}
