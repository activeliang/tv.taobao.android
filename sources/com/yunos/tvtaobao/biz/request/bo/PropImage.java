package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class PropImage implements Serializable {
    private static final long serialVersionUID = 3718608175947206234L;
    private String itemImage;
    private Long pid;

    public PropImage(Long pid2, String image) {
        this.pid = pid2;
        this.itemImage = image;
    }

    public String getItemImage() {
        return this.itemImage;
    }

    public Long getPid() {
        return this.pid;
    }
}
