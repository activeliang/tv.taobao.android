package com.alibaba.analytics.utils;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ZipDictUtils {
    private static final int MAX_SIZE_DICT = 32768;
    private static final int MAX_SIZE_HEAD = 1024;
    private static final int MAX_SIZE_LOG = 256;
    private static int mDictIndex = 0;
    private static int mDictLength = 0;
    private static HashMap<String, String> mZipDictMap = new HashMap<>();

    public static synchronized byte[] getHeadBytes(String key) throws IOException {
        byte[] bytes;
        synchronized (ZipDictUtils.class) {
            bytes = getBytes(key, true);
        }
        return bytes;
    }

    public static synchronized byte[] getBytes(String key) throws IOException {
        byte[] bytes;
        synchronized (ZipDictUtils.class) {
            bytes = getBytes(key, false);
        }
        return bytes;
    }

    private static synchronized byte[] getBytes(String key, boolean isHead) throws IOException {
        byte[] buf;
        synchronized (ZipDictUtils.class) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (checkEnableKey(key, isHead)) {
                int index = -1;
                try {
                    index = Integer.parseInt(mZipDictMap.get(key));
                } catch (Exception e) {
                }
                if (index >= 0) {
                    baos.write(getLengthBytes(1, index));
                } else {
                    put(key);
                    baos.write(getLengthBytes(2, key.getBytes().length));
                    baos.write(key.getBytes());
                }
            } else if (TextUtils.isEmpty(key)) {
                baos.write(getLengthBytes(3, 0));
            } else {
                baos.write(getLengthBytes(3, key.getBytes().length));
                baos.write(key.getBytes());
            }
            buf = baos.toByteArray();
            try {
                baos.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return buf;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0014, code lost:
        if (r4.length() <= 1024) goto L_0x0016;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002a, code lost:
        if (r4.length() > 256) goto L_0x000a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized boolean checkEnableKey(java.lang.String r4, boolean r5) {
        /*
            r0 = 0
            java.lang.Class<com.alibaba.analytics.utils.ZipDictUtils> r1 = com.alibaba.analytics.utils.ZipDictUtils.class
            monitor-enter(r1)
            boolean r2 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x002d }
            if (r2 == 0) goto L_0x000c
        L_0x000a:
            monitor-exit(r1)
            return r0
        L_0x000c:
            if (r5 == 0) goto L_0x0024
            int r2 = r4.length()     // Catch:{ all -> 0x002d }
            r3 = 1024(0x400, float:1.435E-42)
            if (r2 > r3) goto L_0x000a
        L_0x0016:
            int r2 = mDictLength     // Catch:{ all -> 0x002d }
            int r3 = r4.length()     // Catch:{ all -> 0x002d }
            int r2 = r2 + r3
            r3 = 32768(0x8000, float:4.5918E-41)
            if (r2 > r3) goto L_0x000a
            r0 = 1
            goto L_0x000a
        L_0x0024:
            int r2 = r4.length()     // Catch:{ all -> 0x002d }
            r3 = 256(0x100, float:3.59E-43)
            if (r2 <= r3) goto L_0x0016
            goto L_0x000a
        L_0x002d:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.utils.ZipDictUtils.checkEnableKey(java.lang.String, boolean):boolean");
    }

    private static synchronized void put(String key) {
        synchronized (ZipDictUtils.class) {
            mZipDictMap.put(key, "" + mDictIndex);
            mDictLength += key.length();
            mDictIndex++;
        }
    }

    public static synchronized void clear() {
        synchronized (ZipDictUtils.class) {
            mZipDictMap.clear();
            mDictLength = 0;
            mDictIndex = 0;
        }
    }

    public static byte[] getLengthBytes(int n, int length) {
        byte b = (byte) (1 << (8 - n));
        int x = (1 << (8 - n)) - 1;
        if (length < x) {
            return ByteUtils.intToBytes1(length | b);
        }
        byte[] tempbyte = new byte[4];
        tempbyte[0] = (byte) ((x | b) & OnReminderListener.RET_FULL);
        int length2 = length - x;
        int i = 1;
        while (length2 >= 128) {
            tempbyte[i] = (byte) (((length2 % 128) | 128) & 255);
            length2 /= 128;
            i++;
        }
        tempbyte[i] = (byte) (length2 & 127);
        return ByteUtils.subBytes(tempbyte, 0, i + 1);
    }

    public static byte[] getLengthBytes(int length) {
        return getLengthBytes(0, length);
    }
}
