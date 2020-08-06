package com.taobao.tao.remotebusiness.login;

public class LoginNotImplementException extends RuntimeException {
    public LoginNotImplementException() {
    }

    public LoginNotImplementException(String s) {
        super(s);
    }

    public LoginNotImplementException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public LoginNotImplementException(Throwable throwable) {
        super(throwable);
    }
}
