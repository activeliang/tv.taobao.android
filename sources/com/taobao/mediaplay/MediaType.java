package com.taobao.mediaplay;

import com.taobao.media.MediaConstant;
import com.yunos.tv.alitvasrsdk.DMAction;

public enum MediaType {
    GIF("DWGif"),
    VIDEO(DMAction.VIDEO),
    LIVE(MediaConstant.LBLIVE_SOURCE),
    PIC("Pic");
    
    private String value;

    private MediaType(String name) {
        this.value = name;
    }

    public String getValue() {
        return this.value;
    }
}
