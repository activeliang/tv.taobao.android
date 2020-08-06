package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class TopicsEntityItem implements Serializable {
    private static final long serialVersionUID = 3036583815162246231L;
    private String URI;
    private String img;
    private String name;
    private String tms_url;

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img2) {
        this.img = img2;
    }

    public String getTms_url() {
        return this.tms_url;
    }

    public void setTms_url(String tms_url2) {
        this.tms_url = tms_url2;
    }

    public String getURI() {
        return this.URI;
    }

    public void setURI(String uRI) {
        this.URI = uRI;
    }
}
