package com.yunos.tvtaobao.payment.utils;

public class ChannelUtils {
    public static final String DLT = "DLT";
    public static final String FJM = "FJM";
    public static final String HY = "HY";
    public static final String KKA = "KKA";
    public static final String XMI = "XMI";

    public static boolean isThisChannel(String channelStr) {
        return channelStr.equals("SFA");
    }

    public static boolean isThisTag(String channelStr) {
        return channelStr.equals("");
    }
}
