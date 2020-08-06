package com.taobao.alimama.net.pojo.request;

import mtopsdk.mtop.domain.IMTOPDataObject;

@Deprecated
public class SendCpsInfoRequest implements IMTOPDataObject {
    public String API_NAME = "mtop.wdcmunion.sendCpsClickLog";
    public boolean NEED_ECODE = false;
    public boolean NEED_SESSION = true;
    public String VERSION = "1.0";
    private String accept;
    private String cna;
    private String e;
    private String ext;
    private int ismall;
    private String itemid;
    private String referer;
    private String sellerid;
    private String shopid;
    private String sid;
    private String unid;
    private String utdid;

    public String getAccept() {
        return this.accept;
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

    public int getIsmall() {
        return this.ismall;
    }

    public String getItemid() {
        return this.itemid;
    }

    public String getReferer() {
        return this.referer;
    }

    public String getSellerid() {
        return this.sellerid;
    }

    public String getShopid() {
        return this.shopid;
    }

    public String getSid() {
        return this.sid;
    }

    public String getUnid() {
        return this.unid;
    }

    public String getUtdid() {
        return this.utdid;
    }

    public void setAccept(String str) {
        this.accept = str;
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

    public void setIsmall(int i) {
        this.ismall = i;
    }

    public void setItemid(String str) {
        this.itemid = str;
    }

    public void setReferer(String str) {
        this.referer = str;
    }

    public void setSellerid(String str) {
        this.sellerid = str;
    }

    public void setShopid(String str) {
        this.shopid = str;
    }

    public void setSid(String str) {
        this.sid = str;
    }

    public void setUnid(String str) {
        this.unid = str;
    }

    public void setUtdid(String str) {
        this.utdid = str;
    }
}
