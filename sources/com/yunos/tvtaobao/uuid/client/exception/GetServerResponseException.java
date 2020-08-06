package com.yunos.tvtaobao.uuid.client.exception;

import com.yunos.tvtaobao.uuid.utils.Logger;

public class GetServerResponseException extends CommunicateWithServerException {
    public void print() {
        Logger.loge("GetServerResponseException throws");
    }
}
