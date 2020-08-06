package com.alibaba.sdk.android.oss.model;

import java.util.Date;

public class OSSBucketSummary {
    private CannedAccessControlList acl;
    public Date createDate;
    public String extranetEndpoint;
    public String intranetEndpoint;
    public String location;
    public String name;
    public Owner owner;
    public String storageClass;

    public String getAcl() {
        if (this.acl != null) {
            return this.acl.toString();
        }
        return null;
    }

    public void setAcl(String aclString) {
        this.acl = CannedAccessControlList.parseACL(aclString);
    }

    public String toString() {
        if (this.storageClass == null) {
            return "OSSBucket [name=" + this.name + ", creationDate=" + this.createDate + ", owner=" + this.owner.toString() + ", location=" + this.location + "]";
        }
        return "OSSBucket [name=" + this.name + ", creationDate=" + this.createDate + ", owner=" + this.owner.toString() + ", location=" + this.location + ", storageClass=" + this.storageClass + "]";
    }
}
