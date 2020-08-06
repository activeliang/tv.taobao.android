package com.taobao.alimama.net.pojo.request;

import mtopsdk.mtop.domain.IMTOPDataObject;

public class SendCpmInfoRequest implements IMTOPDataObject {
    public String API_NAME = "mtop.wdcmunion.sendCpmClickLog";
    public boolean NEED_ECODE = false;
    public boolean NEED_SESSION = true;
    public String VERSION = "1.0";
    private String accept;
    private String clickid;
    private String cna;
    private String e;
    private String ext;
    private String referer;
    private String useragent;
    private String utdid;

    public String getAccept() {
        return this.accept;
    }

    public String getClickid() {
        return this.clickid;
    }

    public String getCna() {
        return this.cna;
    }

    public String getE() {
        return this.e;
    }

    public String getExt() {
        return this.ext;
    }

    public String getReferer() {
        return this.referer;
    }

    public String getUseragent() {
        return this.useragent;
    }

    public String getUtdid() {
        return this.utdid;
    }

    public void setAccept(String str) {
        this.accept = str;
    }

    public void setClickid(String str) {
        this.clickid = str;
    }

    public void setCna(String str) {
        this.cna = str;
    }

    public void setE(String str) {
        this.e = str;
    }

    public void setExt(String str) {
        this.ext = str;
    }

    public void setReferer(String str) {
        this.referer = str;
    }

    public void setUseragent(String str) {
        this.useragent = str;
    }

    public void setUtdid(String str) {
        this.utdid = str;
    }
}
