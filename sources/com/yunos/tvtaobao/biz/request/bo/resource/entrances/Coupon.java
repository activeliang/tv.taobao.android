package com.yunos.tvtaobao.biz.request.bo.resource.entrances;

public class Coupon {
    private String icon;
    private String link;
    private String linkText;
    private String text;

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon2) {
        this.icon = icon2;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text2) {
        this.text = text2;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link2) {
        this.link = link2;
    }

    public String getLinkText() {
        return this.linkText;
    }

    public void setLinkText(String linkText2) {
        this.linkText = linkText2;
    }

    public String toString() {
        return "Coupon{icon='" + this.icon + '\'' + ", text='" + this.text + '\'' + ", link='" + this.link + '\'' + ", linkText='" + this.linkText + '\'' + '}';
    }
}
