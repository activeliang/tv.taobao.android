package com.ali.auth.third.core.exception;

import com.ali.auth.third.core.message.Message;

public class AlibabaSDKException extends RuntimeException {
    private static final long serialVersionUID = 1357689949294215654L;
    private Message message;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AlibabaSDKException(Message message2, Throwable throwable) {
        super(message2 == null ? "" : message2.message, throwable);
        this.message = message2;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AlibabaSDKException(Message message2) {
        super(message2 == null ? "" : message2.message);
        this.message = message2;
    }

    public Message getSDKMessage() {
        return this.message;
    }
}
