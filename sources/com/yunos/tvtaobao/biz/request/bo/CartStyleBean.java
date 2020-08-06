package com.yunos.tvtaobao.biz.request.bo;

public class CartStyleBean {
    private String cartUrl;
    private String description;
    private String endAt;
    private String id;
    private String name;
    private String startAt;
    private String tagCueUrl;
    private String tagUrl;

    public String getCartUrl() {
        return this.cartUrl;
    }

    public void setCartUrl(String cartUrl2) {
        this.cartUrl = cartUrl2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getTagCueUrl() {
        return this.tagCueUrl;
    }

    public void setTagCueUrl(String tagCueUrl2) {
        this.tagCueUrl = tagCueUrl2;
    }

    public String getTagUrl() {
        return this.tagUrl;
    }

    public void setTagUrl(String tagUrl2) {
        this.tagUrl = tagUrl2;
    }

    public String getStartAt() {
        return this.startAt;
    }

    public void setStartAt(String startAt2) {
        this.startAt = startAt2;
    }

    public String getEndAt() {
        return this.endAt;
    }

    public void setEndAt(String endAt2) {
        this.endAt = endAt2;
    }
}
