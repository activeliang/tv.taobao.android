package com.edge.pcdn;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class MD5Util {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 240) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 15]);
        }
        return sb.toString();
    }

    public static String md5sum(String filename) {
        byte[] buffer = new byte[1024];
        try {
            InputStream fis = new FileInputStream(filename);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            while (true) {
                int numRead = fis.read(buffer);
                if (numRead > 0) {
                    md5.update(buffer, 0, numRead);
                } else {
                    fis.close();
                    return toHexString(md5.digest());
                }
            }
        } catch (Exception e) {
            PcdnLog.d(PcdnLog.toString(e));
            return null;
        }
    }
}
