package com.taobao.wireless.trade.mcart.sdk.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

public class NotificationCenterImpl implements NotificationCenter {
    private static volatile NotificationCenter instance = null;
    private Map<String, Observable> subscription = new HashMap();

    private NotificationCenterImpl() {
    }

    public static NotificationCenter getInstance() {
        if (instance == null) {
            synchronized (NotificationCenterImpl.class) {
                if (instance == null) {
                    instance = new NotificationCenterImpl();
                }
            }
        }
        return instance;
    }

    public synchronized void addObserver(String topic, Observer observer) {
        Observable observable = this.subscription.get(topic);
        if (observable == null) {
            observable = new Observable();
            this.subscription.put(topic, observable);
        }
        observable.addObserver(observer);
    }

    public synchronized void removeObserver(String topic, Observer observer) {
        Observable observable = this.subscription.get(topic);
        if (observable != null) {
            observable.deleteObserver(observer);
            if (observable.countObservers() == 0) {
                this.subscription.remove(topic);
            }
        }
    }

    public synchronized void postNotification(String topic, Object object) {
        Observable observable = this.subscription.get(topic);
        if (observable != null) {
            observable.setChanged();
            observable.notifyObservers(object);
        }
    }

    public synchronized void removeTopic(String topic) {
        this.subscription.remove(topic);
        if (this.subscription.isEmpty()) {
            this.subscription = new HashMap();
        }
    }
}
