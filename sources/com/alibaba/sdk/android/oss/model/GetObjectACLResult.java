package com.alibaba.sdk.android.oss.model;

public class GetObjectACLResult extends OSSResult {
    private CannedAccessControlList objectACL;
    private Owner objectOwner = new Owner();

    public Owner getOwner() {
        return this.objectOwner;
    }

    public String getObjectOwner() {
        return this.objectOwner.getDisplayName();
    }

    public void setObjectOwner(String ownerName) {
        this.objectOwner.setDisplayName(ownerName);
    }

    public String getObjectOwnerID() {
        return this.objectOwner.getId();
    }

    public void setObjectOwnerID(String id) {
        this.objectOwner.setId(id);
    }

    public String getObjectACL() {
        if (this.objectACL != null) {
            return this.objectACL.toString();
        }
        return null;
    }

    public void setObjectACL(String objectACL2) {
        this.objectACL = CannedAccessControlList.parseACL(objectACL2);
    }
}
