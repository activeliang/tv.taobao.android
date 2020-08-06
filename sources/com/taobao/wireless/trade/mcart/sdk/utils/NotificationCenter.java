package com.taobao.wireless.trade.mcart.sdk.utils;

import java.util.Observer;

public interface NotificationCenter {
    void addObserver(String str, Observer observer);

    void postNotification(String str, Object obj);

    void removeObserver(String str, Observer observer);

    void removeTopic(String str);
}
