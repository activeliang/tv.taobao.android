package com.alibaba.sdk.android.oss.model;

import java.util.Date;

public class PartSummary {
    private String eTag;
    private Date lastModified;
    private int partNumber;
    private long size;

    public int getPartNumber() {
        return this.partNumber;
    }

    public void setPartNumber(int partNumber2) {
        this.partNumber = partNumber2;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified2) {
        this.lastModified = lastModified2;
    }

    public String getETag() {
        return this.eTag;
    }

    public void setETag(String eTag2) {
        this.eTag = eTag2;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size2) {
        this.size = size2;
    }
}
