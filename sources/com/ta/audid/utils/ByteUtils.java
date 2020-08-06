package com.ta.audid.utils;

public class ByteUtils {
    private static final String HEX = "0123456789ABCDEF";

    public static long getLongByByte4(byte[] bb) {
        return ((((long) bb[0]) & 255) << 24) | ((((long) bb[1]) & 255) << 16) | ((((long) bb[2]) & 255) << 8) | (((long) bb[3]) & 255);
    }

    public static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(buf.length * 2);
        for (byte appendHex : buf) {
            appendHex(result, appendHex);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 15)).append(HEX.charAt(b & 15));
    }
}
