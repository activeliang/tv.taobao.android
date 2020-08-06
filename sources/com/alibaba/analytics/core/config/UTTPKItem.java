package com.alibaba.analytics.core.config;

public class UTTPKItem {
    public static final String TYPE_FAR = "far";
    public static final String TYPE_NEARBY = "nearby";
    private String mKname;
    private String mKvalue;
    private String mType;

    public String getKname() {
        return this.mKname;
    }

    public void setKname(String aKname) {
        this.mKname = aKname;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String aType) {
        this.mType = aType;
    }

    public String getKvalue() {
        return this.mKvalue;
    }

    public void setKvalue(String aKvalue) {
        this.mKvalue = aKvalue;
    }
}
