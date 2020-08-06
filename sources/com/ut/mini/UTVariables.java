package com.ut.mini;

class UTVariables {
    private static UTVariables s_instance = new UTVariables();
    private UTMI1010_2001Event m1010and2001EventInstance = null;
    private String mBackupH5Url = null;
    private String mH5RefPage = null;
    private String mH5Url = null;
    private String mRefPage = null;

    UTVariables() {
    }

    public synchronized void setUTMI1010_2001EventInstance(UTMI1010_2001Event aInstance) {
        this.m1010and2001EventInstance = aInstance;
    }

    public synchronized UTMI1010_2001Event getUTMI1010_2001EventInstance() {
        return this.m1010and2001EventInstance;
    }

    public static UTVariables getInstance() {
        return s_instance;
    }

    public String getH5Url() {
        return this.mH5Url;
    }

    public void setH5Url(String aH5Url) {
        this.mH5Url = aH5Url;
    }

    public void setBackupH5Url(String aH5Url) {
        this.mBackupH5Url = aH5Url;
    }

    public String getBackupH5Url() {
        return this.mBackupH5Url;
    }

    public String getRefPage() {
        return this.mRefPage;
    }

    public void setRefPage(String aRefPage) {
        this.mRefPage = aRefPage;
    }

    public String getH5RefPage() {
        return this.mH5RefPage;
    }

    public void setH5RefPage(String aH5PrePage) {
        this.mH5RefPage = aH5PrePage;
    }
}
