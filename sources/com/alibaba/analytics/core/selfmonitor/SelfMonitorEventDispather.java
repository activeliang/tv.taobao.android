package com.alibaba.analytics.core.selfmonitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelfMonitorEventDispather {
    private static SelfMonitorEventListener testListener;
    private List<SelfMonitorEventListener> listeners = Collections.synchronizedList(new ArrayList());

    public void regiserListener(SelfMonitorEventListener listener) {
        this.listeners.add(listener);
    }

    public void unRegisterListener(SelfMonitorEventListener listener) {
        this.listeners.remove(listener);
    }

    public void onEvent(SelfMonitorEvent event) {
        if (testListener != null) {
            testListener.onEvent(event);
        }
        for (int i = 0; i < this.listeners.size(); i++) {
            this.listeners.get(i).onEvent(event);
        }
    }

    public static void setTestListener(SelfMonitorEventListener testListener2) {
        testListener = testListener2;
    }
}
