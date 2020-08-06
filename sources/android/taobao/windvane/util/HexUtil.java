package android.taobao.windvane.util;

import java.io.ByteArrayOutputStream;

public class HexUtil {
    private static String hexString = "0123456789ABCDEF";

    public static String stringToHexString(String strPart) {
        String hexString2 = "";
        for (int i = 0; i < strPart.length(); i++) {
            hexString2 = hexString2 + Integer.toHexString(strPart.charAt(i));
        }
        return hexString2;
    }

    public static String encode(String str) {
        byte[] bytes = str.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(hexString.charAt((b & 240) >> 4));
            sb.append(hexString.charAt((b & 15) >> 0));
        }
        return sb.toString();
    }

    public static String decode(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        for (int i = 0; i < bytes.length(); i += 2) {
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4) | hexString.indexOf(bytes.charAt(i + 1)));
        }
        return new String(baos.toByteArray());
    }

    private static byte uniteBytes(byte src0, byte src1) {
        return (byte) (((byte) (Byte.decode("0x" + new String(new byte[]{src0})).byteValue() << 4)) | Byte.decode("0x" + new String(new byte[]{src1})).byteValue());
    }

    public static byte[] HexString2Bytes(String src) {
        byte[] ret = new byte[6];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < 6; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[(i * 2) + 1]);
        }
        return ret;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder(128);
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            String hv = Integer.toHexString(b & 255);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStringToBytes(String hexString2) {
        if (hexString2 == null || hexString2.equals("")) {
            return null;
        }
        String hexString3 = hexString2.toUpperCase();
        int length = hexString3.length() / 2;
        char[] hexChars = hexString3.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
