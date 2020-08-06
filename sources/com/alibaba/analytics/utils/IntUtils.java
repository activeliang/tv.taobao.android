package com.alibaba.analytics.utils;

public class IntUtils {
    public static byte[] getBytes(int i) {
        byte[] bInt = new byte[4];
        int value = i;
        bInt[3] = (byte) (value % 256);
        int value2 = value >> 8;
        bInt[2] = (byte) (value2 % 256);
        int value3 = value2 >> 8;
        bInt[1] = (byte) (value3 % 256);
        bInt[0] = (byte) ((value3 >> 8) % 256);
        return bInt;
    }

    public static byte[] getBytes(byte[] buffer, int i) {
        if (buffer.length != 4) {
            return null;
        }
        int value = i;
        buffer[3] = (byte) (value % 256);
        int value2 = value >> 8;
        buffer[2] = (byte) (value2 % 256);
        int value3 = value2 >> 8;
        buffer[1] = (byte) (value3 % 256);
        buffer[0] = (byte) ((value3 >> 8) % 256);
        return buffer;
    }
}
