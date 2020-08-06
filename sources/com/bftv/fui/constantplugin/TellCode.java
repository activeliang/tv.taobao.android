package com.bftv.fui.constantplugin;

public class TellCode {
    private static final int STATUS = 1;
    public static final int TELL_APP_CACHE = 4;
    public static final int TELL_ASR = 64;
    public static final int TELL_CORRECT = 16;
    public static final int TELL_SYSTEM = 8;
    public static final int TELL_TIPS = 32;
    public static final int TELL_VIEW_CACHE = 2;

    public static boolean isContainViewCache(int code) {
        return (code & 2) == 2;
    }

    public static boolean isContainAppCache(int code) {
        return (code & 4) == 4;
    }

    public static boolean isContainSystem(int code) {
        return (code & 8) == 8;
    }

    public static boolean isContainCorrect(int code) {
        return (code & 16) == 16;
    }

    public static boolean isContainTips(int code) {
        return (code & 32) == 32;
    }

    public static boolean isContainAsr(int code) {
        return (code & 64) == 64;
    }
}
