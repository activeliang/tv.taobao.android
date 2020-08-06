package com.alibaba.analytics.utils;

import com.yunos.tvtaobao.biz.listener.OnReminderListener;

public class ByteUtils {
    public static int bytesToInt(byte[] buf, int offset, int len) {
        if (buf == null || offset < 0 || len < 0 || buf.length < offset + len) {
            return 0;
        }
        byte[] tempbuf = new byte[len];
        for (int i = 0; i < len; i++) {
            tempbuf[i] = buf[offset];
            offset++;
        }
        return bytesToInt(tempbuf);
    }

    public static int bytesToInt(byte[] buf) {
        if (buf == null || buf.length > 4) {
            return 0;
        }
        int ret = 0;
        for (int i = 0; i < buf.length; i++) {
            ret |= (buf[i] & OnReminderListener.RET_FULL) << (((buf.length - i) - 1) * 8);
        }
        return ret;
    }

    public static String bytes2UTF8String(byte[] buf) {
        try {
            return new String(buf, "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

    public static String bytes2UTF8string(byte[] buf, int offset, int len) {
        if (buf == null || offset < 0 || len < 0 || buf.length < offset + len) {
            return "";
        }
        byte[] tempbuf = new byte[len];
        for (int i = 0; i < len; i++) {
            tempbuf[i] = buf[offset];
            offset++;
        }
        return bytes2UTF8String(tempbuf);
    }

    public static byte[] intToBytes(int i, int n) {
        if (n > 4 || n < 1) {
            return null;
        }
        byte[] buf = new byte[n];
        for (int j = 0; j < n; j++) {
            buf[j] = (byte) ((i >> (((n - j) - 1) * 8)) & 255);
        }
        return buf;
    }

    public static byte[] intToBytes1(int i) {
        return new byte[]{(byte) (i & 255)};
    }

    public static byte[] intToBytes2(int i) {
        return new byte[]{(byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public static byte[] intToBytes3(int i) {
        return new byte[]{(byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public static byte[] intToBytes4(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public static byte[] subBytes(byte[] buf, int offset, int len) {
        if (buf == null || offset < 0 || len < 0 || buf.length < offset + len) {
            return null;
        }
        byte[] bs = new byte[len];
        for (int i = offset; i < offset + len; i++) {
            bs[i - offset] = buf[i];
        }
        return bs;
    }
}
