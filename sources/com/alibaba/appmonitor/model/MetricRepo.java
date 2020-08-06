package com.alibaba.appmonitor.model;

import com.alibaba.appmonitor.offline.TempEventMgr;
import java.util.ArrayList;
import java.util.List;

public class MetricRepo {
    private static final int INIT_SIZE = 3;
    private static MetricRepo instance;
    public List<Metric> metrics;

    public static MetricRepo getRepo() {
        if (instance == null) {
            instance = new MetricRepo(3);
        }
        return instance;
    }

    public static MetricRepo getRepo(int capacity) {
        return new MetricRepo(capacity);
    }

    private MetricRepo(int capacity) {
        this.metrics = new ArrayList(capacity);
    }

    public void add(Metric metric) {
        if (this.metrics.contains(metric)) {
            this.metrics.remove(metric);
        }
        this.metrics.add(metric);
    }

    public boolean remove(Metric metric) {
        if (this.metrics.contains(metric)) {
            return this.metrics.remove(metric);
        }
        return true;
    }

    public Metric getMetric(String module, String monitorPoint) {
        if (module == null || monitorPoint == null || this.metrics == null) {
            return null;
        }
        int count = this.metrics.size();
        for (int i = 0; i < count; i++) {
            Metric metric = this.metrics.get(i);
            if (metric != null && metric.getModule().equals(module) && metric.getMonitorPoint().equals(monitorPoint)) {
                return metric;
            }
        }
        Metric metric2 = TempEventMgr.getInstance().getMetric(module, monitorPoint);
        if (metric2 == null) {
            return metric2;
        }
        this.metrics.add(metric2);
        return metric2;
    }
}
