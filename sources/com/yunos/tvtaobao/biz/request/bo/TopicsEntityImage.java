package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class TopicsEntityImage implements Serializable {
    private static final long serialVersionUID = 2653241089853570197L;
    private String bg_1080p;
    private String bg_720p;

    public String getBg_720p() {
        return this.bg_720p;
    }

    public void setBg_720p(String bg_720p2) {
        this.bg_720p = bg_720p2;
    }

    public String getBg_1080p() {
        return this.bg_1080p;
    }

    public void setBg_1080p(String bg_1080p2) {
        this.bg_1080p = bg_1080p2;
    }
}
