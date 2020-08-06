package com.taobao.wireless.trade.mcart.sdk.utils;

import java.util.Observer;

public class NotificationCenterProxy implements NotificationCenter {
    private NotificationCenter notificationCenter = NotificationCenterImpl.getInstance();

    public void addObserver(String topic, Observer observer) {
        this.notificationCenter.addObserver(topic, observer);
    }

    public void removeObserver(String topic, Observer observer) {
        this.notificationCenter.removeObserver(topic, observer);
    }

    public void postNotification(String topic, Object object) {
        throw new UnsupportedOperationException();
    }

    public void removeTopic(String topic) {
        throw new UnsupportedOperationException();
    }
}
