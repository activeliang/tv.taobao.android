package com.taobao.wireless.trade.mcart.sdk.utils;

public class CartManageUtil {
    static boolean mIsManageEnable = false;
    static boolean mIsManaging = false;

    public static void enableCartManage(boolean enable) {
        mIsManageEnable = enable;
    }

    public static boolean isManageEnable() {
        return mIsManageEnable;
    }

    public static void setManaging(boolean b) {
        mIsManaging = b;
    }

    public static boolean isManaging() {
        return mIsManageEnable && mIsManaging;
    }
}
