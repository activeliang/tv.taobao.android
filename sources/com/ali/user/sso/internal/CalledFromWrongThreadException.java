package com.ali.user.sso.internal;

public class CalledFromWrongThreadException extends RuntimeException {
    public CalledFromWrongThreadException() {
        super("Only the original thread that created a view hierarchy can touch its views.");
    }
}
