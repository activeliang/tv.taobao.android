package com.alibaba.sdk.android.oss.model;

import java.util.Date;

public class CopyObjectResult extends OSSResult {
    private String etag;
    private Date lastModified;

    public String getETag() {
        return this.etag;
    }

    public void setEtag(String etag2) {
        this.etag = etag2;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified2) {
        this.lastModified = lastModified2;
    }
}
