package com.alibaba.sdk.android.oss.model;

public enum ObjectPermission {
    Private("private"),
    PublicRead("public-read"),
    PublicReadWrite("public-read-write"),
    Default("default"),
    Unknown("");
    
    private String permissionString;

    private ObjectPermission(String permissionString2) {
        this.permissionString = permissionString2;
    }

    public static ObjectPermission parsePermission(String str) {
        for (ObjectPermission permission : new ObjectPermission[]{Private, PublicRead, PublicReadWrite, Default}) {
            if (permission.permissionString.equals(str)) {
                return permission;
            }
        }
        return Unknown;
    }

    public String toString() {
        return this.permissionString;
    }
}
