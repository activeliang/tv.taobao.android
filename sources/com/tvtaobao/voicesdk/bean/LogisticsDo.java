package com.tvtaobao.voicesdk.bean;

import java.io.Serializable;

public class LogisticsDo implements Serializable {
    private String auctionId;
    private String auctionPicUrl;
    private String auctionTitle;
    private String cpCode;
    private String mailNo;
    private String traceDesc;
    private String traceGmtCreate;
    private String traceStanderdDesc;
    private String traceStatusDesc;

    public String getAuctionId() {
        return this.auctionId;
    }

    public void setAuctionId(String auctionId2) {
        this.auctionId = auctionId2;
    }

    public String getAuctionPicUrl() {
        return this.auctionPicUrl;
    }

    public void setAuctionPicUrl(String auctionPicUrl2) {
        this.auctionPicUrl = auctionPicUrl2;
    }

    public String getAuctionTitle() {
        return this.auctionTitle;
    }

    public void setAuctionTitle(String auctionTitle2) {
        this.auctionTitle = auctionTitle2;
    }

    public String getCpCode() {
        return this.cpCode;
    }

    public void setCpCode(String cpCode2) {
        this.cpCode = cpCode2;
    }

    public String getMailNo() {
        return this.mailNo;
    }

    public void setMailNo(String mailNo2) {
        this.mailNo = mailNo2;
    }

    public String getTraceDesc() {
        return this.traceDesc;
    }

    public void setTraceDesc(String traceDesc2) {
        this.traceDesc = traceDesc2;
    }

    public String getTraceGmtCreate() {
        return this.traceGmtCreate;
    }

    public void setTraceGmtCreate(String traceGmtCreate2) {
        this.traceGmtCreate = traceGmtCreate2;
    }

    public String getTraceStanderdDesc() {
        return this.traceStanderdDesc;
    }

    public void setTraceStanderdDesc(String traceStanderdDesc2) {
        this.traceStanderdDesc = traceStanderdDesc2;
    }

    public String getTraceStatusDesc() {
        return this.traceStatusDesc;
    }

    public void setTraceStatusDesc(String traceStatusDesc2) {
        this.traceStatusDesc = traceStatusDesc2;
    }
}
