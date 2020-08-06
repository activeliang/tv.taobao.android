package com.alibaba.sdk.android.oss.model;

public class PartETag {
    private long crc64;
    private String eTag;
    private int partNumber;
    private long partSize;

    public PartETag(int partNumber2, String eTag2) {
        setPartNumber(partNumber2);
        setETag(eTag2);
    }

    public int getPartNumber() {
        return this.partNumber;
    }

    public void setPartNumber(int partNumber2) {
        this.partNumber = partNumber2;
    }

    public String getETag() {
        return this.eTag;
    }

    public void setETag(String eTag2) {
        this.eTag = eTag2;
    }

    public long getPartSize() {
        return this.partSize;
    }

    public void setPartSize(long partSize2) {
        this.partSize = partSize2;
    }

    public long getCRC64() {
        return this.crc64;
    }

    public void setCRC64(long crc642) {
        this.crc64 = crc642;
    }
}
