package com.alibaba.motu.crashreporter.utrestapi;

public class UTRestPostKVPair {
    String mKey;
    byte[] mValue;

    public void setKey(String aKey) {
        this.mKey = aKey;
    }

    public String getKey() {
        return this.mKey;
    }

    public void setValue(byte[] aData) {
        this.mValue = aData;
    }

    public byte[] getData() {
        return this.mValue;
    }
}
