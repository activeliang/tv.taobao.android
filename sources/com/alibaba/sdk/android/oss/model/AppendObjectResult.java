package com.alibaba.sdk.android.oss.model;

public class AppendObjectResult extends OSSResult {
    private long nextPosition;
    private String objectCRC64;

    public long getNextPosition() {
        return this.nextPosition;
    }

    public void setNextPosition(Long nextPosition2) {
        this.nextPosition = nextPosition2.longValue();
    }

    public String getObjectCRC64() {
        return this.objectCRC64;
    }

    public void setObjectCRC64(String objectCRC642) {
        this.objectCRC64 = objectCRC642;
    }
}
