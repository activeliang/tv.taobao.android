package com.ta.audid.device;

public class UtdidObj {
    private boolean isValid = false;
    private long timestamp;
    private int version;

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp2) {
        this.timestamp = timestamp2;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version2) {
        this.version = version2;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void setValid(boolean isValid2) {
        this.isValid = isValid2;
    }
}
