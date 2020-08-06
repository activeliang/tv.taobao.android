package com.yunos.tv.core.common;

public class FrequentLock {
    private final int DELAY_TIME = 1000;
    private boolean haveLock = false;
    private long lastTimeLock = 0;

    public boolean isLock() {
        long current = System.currentTimeMillis();
        if (this.haveLock && this.lastTimeLock > 0 && current - this.lastTimeLock > 1000) {
            clearLoack();
        }
        if (this.haveLock) {
            this.lastTimeLock = System.currentTimeMillis();
            return true;
        }
        this.haveLock = true;
        this.lastTimeLock = System.currentTimeMillis();
        return false;
    }

    public void clearLoack() {
        this.haveLock = false;
        this.lastTimeLock = 0;
    }
}
