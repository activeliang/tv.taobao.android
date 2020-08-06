package com.ali.auth.third.core.rpc.protocol;

public class SecurityThreadLocal {
    private static ThreadLocal<String> sThreadKey = new ThreadLocal<>();

    public static String get() {
        return sThreadKey.get();
    }

    public static void set(String securityKey) {
        sThreadKey.set(securityKey);
    }
}
