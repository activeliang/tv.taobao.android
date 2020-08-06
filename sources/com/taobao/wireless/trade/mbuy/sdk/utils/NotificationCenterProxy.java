package com.taobao.wireless.trade.mbuy.sdk.utils;

import java.util.Observer;

public class NotificationCenterProxy implements NotificationCenter {
    private NotificationCenter notificationCenter = new NotificationCenterImpl();

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

    public boolean containsTopic(String topic) {
        return this.notificationCenter.containsTopic(topic);
    }
}
