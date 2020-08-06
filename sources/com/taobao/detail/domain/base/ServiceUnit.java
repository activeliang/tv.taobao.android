package com.taobao.detail.domain.base;

import java.io.Serializable;

public class ServiceUnit implements Serializable, Cloneable {
    private boolean free;
    private boolean multi;
    private String price;
    private boolean selected;
    private long serId;
    private long uniqId = 0;

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected2) {
        this.selected = selected2;
    }

    public boolean isFree() {
        return this.free;
    }

    public void setFree(boolean free2) {
        this.free = free2;
    }

    public boolean isMulti() {
        return this.multi;
    }

    public void setMulti(boolean multi2) {
        this.multi = multi2;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public long getSerId() {
        return this.serId;
    }

    public void setSerId(long serId2) {
        this.serId = serId2;
    }

    public long getUniqId() {
        return this.uniqId;
    }

    public void setUniqId(long uniqId2) {
        this.uniqId = uniqId2;
    }

    public ServiceUnit clone() {
        try {
            Object cloneObj = super.clone();
            if (cloneObj instanceof ServiceUnit) {
                return (ServiceUnit) cloneObj;
            }
            return null;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
