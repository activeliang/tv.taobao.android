package com.ali.user.open.core.util;

public class Validate {
    public static void notNull(Object arg, String name) {
        if (arg == null) {
            throw new NullPointerException("Argument '" + name + "' cannot be null");
        }
    }
}
