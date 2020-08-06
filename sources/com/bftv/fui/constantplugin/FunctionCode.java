package com.bftv.fui.constantplugin;

public class FunctionCode {
    public static final int BACK = 65536;
    public static final int BUY = 128;
    public static final int CART = 256;
    public static final int CHANGE = 32768;
    public static final int CLOSE = 524288;
    public static final int CMD_TASK_SHUTDOWN = 2048;
    public static final int COLLECT = 4096;
    public static final int DEFAULT = 4;
    public static final int DELETE = 262144;
    public static final int FILP_PAGES = 1048576;
    public static final int FORWORD = 8192;
    public static final int HDMI = 131072;
    public static final int LOOK = 16;
    public static final int NEXT = 512;
    public static final int OPEN = 32;
    public static final int PAGE = 8;
    public static final int PLAY = 64;
    public static final int PRE = 1024;
    public static final int REWIND = 16384;
    public static final int SERVICE = 2097152;
    private static final int STATUS = 2;

    public static boolean isContainPlay(int code) {
        return (code & 64) == 64;
    }

    public static boolean isContainDelete(int code) {
        return (code & 262144) == 262144;
    }

    public static boolean isContainOpen(int code) {
        return (code & 32) == 32;
    }

    public static boolean isContainClose(int code) {
        return (code & 524288) == 524288;
    }
}
