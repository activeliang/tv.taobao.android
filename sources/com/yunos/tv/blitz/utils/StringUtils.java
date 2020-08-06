package com.yunos.tv.blitz.utils;

import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.regex.Pattern;

public class StringUtils {
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String ENCODE_CHARSET = "UTF-8";
    private static final String MESSAGEDIGEST_TYPE = "MD5";

    public static String getSubStr(String s, int length) throws Exception {
        return getSubStr(s, length, true);
    }

    public static String getSubStr(String s, int length, Boolean ellipsis) throws Exception {
        byte[] bytes = s.getBytes("Unicode");
        int n = 0;
        int i = 2;
        while (i < bytes.length && n < length) {
            if (i % 2 != 0) {
                n++;
            } else if (bytes[i] != 0) {
                n++;
            }
            i++;
        }
        if (i % 2 != 0) {
            if (bytes[i - 1] != 0) {
                i--;
            } else {
                i++;
            }
        }
        String result = new String(bytes, 0, i, "Unicode");
        if (strLength(s) <= length || !ellipsis.booleanValue()) {
            return result;
        }
        return result + "...";
    }

    public static int strLength(String value) {
        double d;
        double valueLength = ClientTraceData.b.f47a;
        for (int i = 0; i < value.length(); i++) {
            if (value.substring(i, i + 1).matches("[一-龥]")) {
                d = 1.0d;
            } else {
                d = 0.5d;
            }
            valueLength += d;
        }
        return ((int) Math.ceil(valueLength)) * 2;
    }

    public static String md5Hex(String source) {
        if (source == null) {
            return null;
        }
        try {
            return md5Hex(source.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String md5Hex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return String.valueOf(encode2Hex(encode2MD5(bytes)));
    }

    public static byte[] encode2MD5(byte[] source) {
        byte[] result = new byte[0];
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            return md.digest();
        } catch (Exception e) {
            return result;
        }
    }

    private static char[] encode2Hex(byte[] data) {
        int len = data.length;
        char[] out = new char[(len << 1)];
        int j = 0;
        for (int i = 0; i < len; i++) {
            int j2 = j + 1;
            out[j] = DIGITS[(data[i] & 240) >>> 4];
            j = j2 + 1;
            out[j2] = DIGITS[data[i] & 15];
        }
        return out;
    }

    public static boolean isEqual(String compareFrom, String compareTo) {
        if (compareFrom == compareTo) {
            return true;
        }
        if (compareFrom == null || compareTo == null) {
            return false;
        }
        return compareFrom.equals(compareTo);
    }

    public static boolean isArrEqual(String[] comparesFrom, String[] comparesTo) {
        if (comparesFrom == comparesTo) {
            return true;
        }
        if (comparesFrom == null || comparesTo == null) {
            return false;
        }
        if (comparesFrom.length != comparesFrom.length) {
            return false;
        }
        for (int i = 0; i < comparesFrom.length; i++) {
            if (!isEqual(comparesFrom[i], comparesTo[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(String input) {
        if (isEmpty(input)) {
            return false;
        }
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric2(String str) {
        if (!isEmpty(str) && Pattern.compile("-?[0-9]+.?[0-9]+").matcher(str).matches()) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String input) {
        return input == null || input.length() == 0;
    }

    public static int adjustFontSize(int screenWidth, int screenHeight) {
        if (screenWidth <= 240) {
            return 10;
        }
        if (screenWidth <= 320) {
            return 14;
        }
        if (screenWidth <= 480) {
            return 24;
        }
        if (screenWidth <= 540) {
            return 26;
        }
        if (screenWidth <= 800) {
        }
        return 30;
    }

    public static boolean checkDate(String date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        try {
            return date.equals(df.format(df.parse(date)));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkEmail(String email) {
        if (Pattern.compile("^/w+([-.]/w+)*@/w+([-]/w+)*/.(/w+([-]/w+)*/.)*[a-z]{2,3}$").matcher(email).matches()) {
            return true;
        }
        return false;
    }

    public boolean checkPhone(String phone) {
        if (Pattern.compile("^13/d{9}||15[8,9]/d{8}$").matcher(phone).matches()) {
            return true;
        }
        return false;
    }

    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append("abcdefghijklmnopqrstuvwxyz0123456789".charAt(random.nextInt("abcdefghijklmnopqrstuvwxyz0123456789".length())));
        }
        return sb.toString();
    }
}
