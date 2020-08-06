package com.yunos.tvtaobao.uuid.client.exception;

import com.yunos.tvtaobao.uuid.utils.Logger;

public class SendXmlCommException extends CommunicateWithServerException {
    public void print() {
        Logger.loge("SendXmlCommException throws");
    }
}
