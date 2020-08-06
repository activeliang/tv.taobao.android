package com.ali.auth.third.core.message;

public class Message implements Cloneable {
    public String action;
    public int code;
    public String message;
    public String type;

    public static Message create(int code2, Object... args) {
        return MessageUtils.createMessage(code2, args);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
