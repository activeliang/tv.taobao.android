package com.ali.auth.third.core.model;

import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.message.MessageUtils;

public class ResultCode {
    public static final ResultCode CAPTCHA_RELOGIN = new ResultCode(103, "CAPTCHA_RELOGIN");
    public static final ResultCode CHECK = new ResultCode(108, "CHECK");
    public static final ResultCode CONTINUE_LOGIN = new ResultCode(105, Constants.ACTION_CONTINUELOGIN);
    public static final ResultCode IGNORE = new ResultCode(-100, "IGNORE");
    public static final ResultCode SUCCESS = new ResultCode(100, "SUCCESS");
    public static final ResultCode SYSTEM_EXCEPTION = new ResultCode(10010, "SYSTEM_EXCEPTION");
    public static final ResultCode TRUST_LOGIN = new ResultCode(104, "TRUST_LOGIN");
    public static final ResultCode USER_CANCEL = new ResultCode(10003, "USER_CANCEL");
    public int code;
    public String message;

    public ResultCode(int code2) {
        this(code2, (String) null);
    }

    public ResultCode(int code2, String message2) {
        this.code = code2;
        this.message = message2;
    }

    public boolean isSuccess() {
        return this.code == 100;
    }

    public static ResultCode create(Message message2) {
        return new ResultCode(message2.code, message2.message);
    }

    public static ResultCode create(int code2, Object... args) {
        return new ResultCode(code2, MessageUtils.getMessageContent(code2, args));
    }

    public int hashCode() {
        return this.code + 31;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this.code != ((ResultCode) obj).code) {
            return false;
        }
        return true;
    }
}
