package com.alibaba.sdk.android.oss.model;

import java.util.ArrayList;
import java.util.List;

public class DeleteMultipleObjectResult extends OSSResult {
    private List<String> deletedObjects;
    private List<String> failedObjects;
    private boolean isQuiet;

    public void clear() {
        if (this.deletedObjects != null) {
            this.deletedObjects.clear();
        }
        if (this.failedObjects != null) {
            this.failedObjects.clear();
        }
    }

    public void addDeletedObject(String object) {
        if (this.deletedObjects == null) {
            this.deletedObjects = new ArrayList();
        }
        this.deletedObjects.add(object);
    }

    public void addFailedObjects(String object) {
        if (this.failedObjects == null) {
            this.failedObjects = new ArrayList();
        }
        this.failedObjects.add(object);
    }

    public List<String> getDeletedObjects() {
        return this.deletedObjects;
    }

    public List<String> getFailedObjects() {
        return this.failedObjects;
    }

    public boolean getQuiet() {
        return this.isQuiet;
    }

    public void setQuiet(boolean isQuiet2) {
        this.isQuiet = isQuiet2;
    }
}
