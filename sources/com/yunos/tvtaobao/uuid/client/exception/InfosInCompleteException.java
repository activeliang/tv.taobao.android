package com.yunos.tvtaobao.uuid.client.exception;

import com.yunos.tvtaobao.uuid.utils.Logger;

public class InfosInCompleteException extends Exception {
    private String mInfo;

    public InfosInCompleteException(String info) {
        this.mInfo = info;
    }

    public void print() {
        Logger.loge("InfosInComplete: " + this.mInfo);
    }
}
