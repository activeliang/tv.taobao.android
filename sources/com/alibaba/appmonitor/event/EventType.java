package com.alibaba.appmonitor.event;

import com.alibaba.analytics.utils.Logger;
import com.alibaba.appmonitor.sample.AlarmConfig;
import com.alibaba.appmonitor.sample.CounterConfig;
import com.alibaba.appmonitor.sample.StatConfig;

public enum EventType {
    ALARM(65501, 30, "alarmData", 1000, "ap_alarm", AlarmConfig.class),
    COUNTER(65502, 30, "counterData", 1000, "ap_counter", CounterConfig.class),
    STAT(65503, 30, "statData", 1000, "ap_stat", StatConfig.class);
    
    static String TAG;
    private String aggregateEventArgsKey;
    private int backgroundStatisticsInterval;
    private Class cls;
    private int defaultSampling;
    private int eventId;
    private int foregroundStatisticsInterval;
    private String namespce;
    private boolean open;
    private int triggerCount;

    static {
        TAG = "EventType";
    }

    private EventType(int eventId2, int triggerCount2, String aggregateEventArgsKey2, int defaultSampling2, String namespace, Class cls2) {
        this.foregroundStatisticsInterval = 25;
        this.backgroundStatisticsInterval = 300;
        this.eventId = eventId2;
        this.triggerCount = triggerCount2;
        this.open = true;
        this.aggregateEventArgsKey = aggregateEventArgsKey2;
        this.defaultSampling = defaultSampling2;
        this.namespce = namespace;
        this.cls = cls2;
    }

    public int getEventId() {
        return this.eventId;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open2) {
        this.open = open2;
    }

    public int getTriggerCount() {
        return this.triggerCount;
    }

    @Deprecated
    public void setTriggerCount(int triggerCount2) {
        Logger.d(TAG, "[setTriggerCount]", this.aggregateEventArgsKey, triggerCount2 + "");
        this.triggerCount = triggerCount2;
    }

    public static EventType getEventType(int eventId2) {
        EventType[] types = values();
        for (EventType type : types) {
            if (type != null && type.getEventId() == eventId2) {
                return type;
            }
        }
        return null;
    }

    public String getAggregateEventArgsKey() {
        return this.aggregateEventArgsKey;
    }

    public int getForegroundStatisticsInterval() {
        return this.foregroundStatisticsInterval;
    }

    public int getBackgroundStatisticsInterval() {
        return this.backgroundStatisticsInterval;
    }

    public void setStatisticsInterval(int statisticsInterval) {
        this.foregroundStatisticsInterval = statisticsInterval;
    }

    public int getDefaultSampling() {
        return this.defaultSampling;
    }

    public void setDefaultSampling(int defaultSampling2) {
        this.defaultSampling = defaultSampling2;
    }

    public static EventType getEventTypeByNameSpace(String namespace) {
        if (namespace == null) {
            return null;
        }
        EventType[] types = values();
        for (EventType type : types) {
            if (type != null && namespace.equalsIgnoreCase(type.getNameSpace())) {
                return type;
            }
        }
        return null;
    }

    private String getNameSpace() {
        return this.namespce;
    }

    public Class getCls() {
        return this.cls;
    }
}
