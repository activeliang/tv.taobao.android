package com.alibaba.motu.videoplayermonitor.model;

public enum MotuMediaType {
    VOD(0),
    LIVE(1);
    
    private final int value;

    private MotuMediaType(int value2) {
        this.value = value2;
    }

    public int getValue() {
        return this.value;
    }
}
