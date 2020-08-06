package com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo;

import com.taobao.media.MediaConstant;

public enum OptionType {
    Today(MediaConstant.VIDEO_NOT_EXISTS_CODE),
    Brand("1300"),
    JuMingPin("1013");
    
    private String platformId;

    private OptionType(String id) {
        this.platformId = id;
    }

    public String getplatformId() {
        return this.platformId;
    }
}
