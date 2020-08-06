package com.taobao.wireless.trade.mcart.sdk.co.business;

public class DataProcessResult {
    private long fastJsonParseTime = 0;
    private long parseModuleParseStructTime = 0;
    private long successTotalTime = 0;

    public long getSuccessTotalTime() {
        return this.successTotalTime;
    }

    public void setSuccessTotalTime(long successTotalTime2) {
        this.successTotalTime = successTotalTime2;
    }

    public long getFastJsonParseTime() {
        return this.fastJsonParseTime;
    }

    public void setFastJsonParseTime(long fastJsonParseTime2) {
        this.fastJsonParseTime = fastJsonParseTime2;
    }

    public long getParseModuleParseStructTime() {
        return this.parseModuleParseStructTime;
    }

    public void setParseModuleParseStructTime(long parseModuleParseStructTime2) {
        this.parseModuleParseStructTime = parseModuleParseStructTime2;
    }
}
