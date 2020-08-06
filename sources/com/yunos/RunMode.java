package com.yunos;

public final class RunMode {
    public static boolean isYunos() {
        return false;
    }

    public static boolean needInitDevice() {
        return false;
    }

    public static boolean needDomainCompliance() {
        return false;
    }
}
