package com.alibaba.motu.videoplayermonitor.model;

public enum MotuVideoCode {
    H264(0),
    HEVC(1),
    VP9(2),
    H263(3),
    MP4(4),
    OTHER(5);
    
    private final int value;

    private MotuVideoCode(int value2) {
        this.value = value2;
    }

    public int getValue() {
        return this.value;
    }
}
