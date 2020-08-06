package com.taobao.wireless.trade.mbuy.sdk.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

public class NotificationCenterImpl implements NotificationCenter {
    private Map<String, Observable> subscription = new HashMap();

    public void addObserver(String topic, Observer observer) {
        synchronized (this) {
            Observable observable = this.subscription.get(topic);
            if (observable == null) {
                observable = new Observable();
                this.subscription.put(topic, observable);
            }
            observable.addObserver(observer);
        }
    }

    public void removeObserver(String topic, Observer observer) {
        synchronized (this) {
            Observable observable = this.subscription.get(topic);
            if (observable != null) {
                observable.deleteObserver(observer);
                if (observable.countObservers() == 0) {
                    this.subscription.remove(topic);
                }
            }
        }
    }

    public void postNotification(String topic, Object object) {
        synchronized (this) {
            Observable observable = this.subscription.get(topic);
            if (observable != null) {
                observable.setChanged();
                observable.notifyObservers(object);
            }
        }
    }

    public void removeTopic(String topic) {
        synchronized (this) {
            this.subscription.remove(topic);
            if (this.subscription.isEmpty()) {
                this.subscription = new HashMap();
            }
        }
    }

    public boolean containsTopic(String topic) {
        boolean containsKey;
        synchronized (this) {
            containsKey = this.subscription.containsKey(topic);
        }
        return containsKey;
    }
}
