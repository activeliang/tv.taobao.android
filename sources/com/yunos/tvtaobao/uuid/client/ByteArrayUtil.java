package com.yunos.tvtaobao.uuid.client;

import com.yunos.tvtaobao.biz.listener.OnReminderListener;

public class ByteArrayUtil {
    private static final String FILL_SYMBOL = "0";

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            String s = Integer.toHexString(b & OnReminderListener.RET_FULL);
            if (s.length() < 2) {
                sb.append("0");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static int toInt(byte[] b, int offset) {
        int value = 0;
        if (b == null || b.length < offset + 4) {
            return -1;
        }
        for (int i = 0; i < 4; i++) {
            value += (b[i + offset] & OnReminderListener.RET_FULL) << (i << 3);
        }
        return value;
    }
}
