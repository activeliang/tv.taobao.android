package com.yunos.tvtaobao.biz.request.bo;

public class DynamicRecommend {
    private String imgUrl;
    private String link;
    private String status;

    public boolean isValid() {
        if (!"0".equals(this.status) && "1".equals(this.status)) {
            return true;
        }
        return false;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl2) {
        this.imgUrl = imgUrl2;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link2) {
        this.link = link2;
    }
}
