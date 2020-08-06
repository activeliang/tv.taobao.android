package com.yunos.tvtaobao.biz.request.bo;

public class DeliveryListBean {
    private DeliveryListExt ext;
    private String icon;
    private String id;
    private String status;
    private String statusIcon;
    private String time;
    private String tips;
    private String title;
    private String type;
    private String url;

    public void setExt(DeliveryListExt ext2) {
        this.ext = ext2;
    }

    public DeliveryListExt getExt() {
        return this.ext;
    }

    public void setIcon(String icon2) {
        this.icon = icon2;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setStatusIcon(String statusIcon2) {
        this.statusIcon = statusIcon2;
    }

    public String getStatusIcon() {
        return this.statusIcon;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getId() {
        return this.id;
    }

    public void setTime(String time2) {
        this.time = time2;
    }

    public String getTime() {
        return this.time;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getType() {
        return this.type;
    }

    public void setTips(String tips2) {
        this.tips = tips2;
    }

    public String getTips() {
        return this.tips;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public String getUrl() {
        return this.url;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getStatus() {
        return this.status;
    }
}
