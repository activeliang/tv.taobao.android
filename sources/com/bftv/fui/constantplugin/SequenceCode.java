package com.bftv.fui.constantplugin;

public class SequenceCode {
    private static final int STATUS = 1;
    public static final int TYPE_BACK = 8;
    public static final int TYPE_CUSTOM = 32;
    public static final int TYPE_NUM = 2;
    public static final int TYPE_PAGE = 4;
    public static final int TYPE_PLAYER = 16;

    public static boolean isContainNum(int code) {
        return (code & 2) == 2;
    }

    public static boolean isContainPage(int code) {
        return (code & 4) == 4;
    }

    public static boolean isContainBack(int code) {
        return (code & 8) == 8;
    }

    public static boolean isContainPlayer(int code) {
        return (code & 16) == 16;
    }
}
