package com.alibaba.sdk.android.oss.model;

public enum StorageClass {
    Standard("Standard"),
    IA("IA"),
    Archive("Archive"),
    Unknown("Unknown");
    
    private String storageClassString;

    private StorageClass(String storageClassString2) {
        this.storageClassString = storageClassString2;
    }

    public static StorageClass parse(String storageClassString2) {
        for (StorageClass st : values()) {
            if (st.toString().equals(storageClassString2)) {
                return st;
            }
        }
        throw new IllegalArgumentException("Unable to parse " + storageClassString2);
    }

    public String toString() {
        return this.storageClassString;
    }
}
