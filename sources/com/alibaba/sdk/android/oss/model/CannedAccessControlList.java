package com.alibaba.sdk.android.oss.model;

public enum CannedAccessControlList {
    Private("private"),
    PublicRead("public-read"),
    PublicReadWrite("public-read-write"),
    Default("default");
    
    private String ACLString;

    private CannedAccessControlList(String acl) {
        this.ACLString = acl;
    }

    public static CannedAccessControlList parseACL(String aclStr) {
        for (CannedAccessControlList acl : values()) {
            if (acl.toString().equals(aclStr)) {
                return acl;
            }
        }
        return null;
    }

    public String toString() {
        return this.ACLString;
    }
}
