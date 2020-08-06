package com.taobao.adapter;

public class MonitorMeasure {
    public double max;
    public double min;
    public String name;
    public double value;

    public MonitorMeasure(String name2) {
        this.name = name2;
    }

    public void setRange(double min2, double max2) {
        this.min = min2;
        this.max = max2;
    }
}
