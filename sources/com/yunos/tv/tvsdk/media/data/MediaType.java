package com.yunos.tv.tvsdk.media.data;

public enum MediaType {
    FROM_TAOTV(0),
    FROM_YOUKU(1),
    FROM_SOHU(2),
    FROM_QIYI(3);
    
    private int mSourceType;

    private MediaType(int id) {
        this.mSourceType = id;
    }

    public int getType() {
        return this.mSourceType;
    }
}
