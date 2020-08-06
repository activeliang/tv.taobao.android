package com.alibaba.appmonitor.model;

import com.alibaba.appmonitor.event.Event;
import com.alibaba.appmonitor.event.EventType;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.appmonitor.pool.Reusable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetricValueSet implements Reusable {
    private Map<Metric, Event> events = Collections.synchronizedMap(new HashMap());

    public List<Event> getEvents() {
        return new ArrayList(this.events.values());
    }

    public Event getEvent(Integer eventId, String module, String monitorPoint, String extraArg, Class<? extends Event> type) {
        Metric metric;
        boolean newMetricInstance = false;
        if (eventId.intValue() == EventType.STAT.getEventId()) {
            metric = MetricRepo.getRepo().getMetric(module, monitorPoint);
        } else {
            newMetricInstance = true;
            metric = (Metric) BalancedPool.getInstance().poll(Metric.class, module, monitorPoint, extraArg);
        }
        Event event = null;
        if (metric != null) {
            if (this.events.containsKey(metric)) {
                event = this.events.get(metric);
            } else {
                synchronized (MetricValueSet.class) {
                    event = (Event) BalancedPool.getInstance().poll(type, eventId, module, monitorPoint, extraArg);
                    this.events.put(metric, event);
                    newMetricInstance = false;
                }
            }
            if (newMetricInstance) {
                BalancedPool.getInstance().offer(metric);
            }
        }
        return event;
    }

    public void clean() {
        for (Event event : this.events.values()) {
            BalancedPool.getInstance().offer(event);
        }
        this.events.clear();
    }

    public void fill(Object... params) {
        if (this.events == null) {
            this.events = Collections.synchronizedMap(new HashMap());
        }
    }
}
