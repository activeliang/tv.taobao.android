package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;
import com.taobao.atlas.dexmerge.dx.io.Opcodes;
import com.taobao.ju.track.csv.CsvReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.MalformedInputException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Properties;
import org.apache.commons.codec.language.Soundex;

public class IOUtils {
    public static final char[] ASCII_CHARS = {'0', '0', '0', '1', '0', '2', '0', '3', '0', '4', '0', '5', '0', '6', '0', '7', '0', '8', '0', '9', '0', 'A', '0', 'B', '0', 'C', '0', 'D', '0', 'E', '0', 'F', '1', '0', '1', '1', '1', '2', '1', '3', '1', '4', '1', '5', '1', '6', '1', '7', '1', '8', '1', '9', '1', 'A', '1', 'B', '1', 'C', '1', 'D', '1', 'E', '1', 'F', '2', '0', '2', '1', '2', '2', '2', '3', '2', '4', '2', '5', '2', '6', '2', '7', '2', '8', '2', '9', '2', 'A', '2', 'B', '2', 'C', '2', 'D', '2', 'E', '2', 'F'};
    public static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    public static final Properties DEFAULT_PROPERTIES = new Properties();
    public static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static final char[] DigitOnes = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    static final char[] DigitTens = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
    public static final String FASTJSON_COMPATIBLEWITHFIELDNAME = "fastjson.compatibleWithFieldName";
    public static final String FASTJSON_COMPATIBLEWITHJAVABEAN = "fastjson.compatibleWithJavaBean";
    public static final String FASTJSON_PROPERTIES = "fastjson.properties";
    public static final int[] IA = new int[256];
    public static final Charset UTF8 = Charset.forName("UTF-8");
    static final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    public static final boolean[] firstIdentifierFlags = new boolean[256];
    public static final boolean[] identifierFlags = new boolean[256];
    public static final char[] replaceChars = new char[93];
    static final int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE};
    public static final byte[] specicalFlags_doubleQuotes = new byte[161];
    public static final boolean[] specicalFlags_doubleQuotesFlags = new boolean[161];
    public static final byte[] specicalFlags_singleQuotes = new byte[161];
    public static final boolean[] specicalFlags_singleQuotesFlags = new boolean[161];

    static {
        boolean z;
        boolean z2;
        for (char c = 0; c < firstIdentifierFlags.length; c = (char) (c + 1)) {
            if (c >= 'A' && c <= 'Z') {
                firstIdentifierFlags[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                firstIdentifierFlags[c] = true;
            } else if (c == '_') {
                firstIdentifierFlags[c] = true;
            }
        }
        for (char c2 = 0; c2 < identifierFlags.length; c2 = (char) (c2 + 1)) {
            if (c2 >= 'A' && c2 <= 'Z') {
                identifierFlags[c2] = true;
            } else if (c2 >= 'a' && c2 <= 'z') {
                identifierFlags[c2] = true;
            } else if (c2 == '_') {
                identifierFlags[c2] = true;
            } else if (c2 >= '0' && c2 <= '9') {
                identifierFlags[c2] = true;
            }
        }
        try {
            new PropertiesInitializer().autoConfig();
        } catch (Throwable th) {
        }
        specicalFlags_doubleQuotes[0] = 4;
        specicalFlags_doubleQuotes[1] = 4;
        specicalFlags_doubleQuotes[2] = 4;
        specicalFlags_doubleQuotes[3] = 4;
        specicalFlags_doubleQuotes[4] = 4;
        specicalFlags_doubleQuotes[5] = 4;
        specicalFlags_doubleQuotes[6] = 4;
        specicalFlags_doubleQuotes[7] = 4;
        specicalFlags_doubleQuotes[8] = 1;
        specicalFlags_doubleQuotes[9] = 1;
        specicalFlags_doubleQuotes[10] = 1;
        specicalFlags_doubleQuotes[11] = 4;
        specicalFlags_doubleQuotes[12] = 1;
        specicalFlags_doubleQuotes[13] = 1;
        specicalFlags_doubleQuotes[34] = 1;
        specicalFlags_doubleQuotes[92] = 1;
        specicalFlags_singleQuotes[0] = 4;
        specicalFlags_singleQuotes[1] = 4;
        specicalFlags_singleQuotes[2] = 4;
        specicalFlags_singleQuotes[3] = 4;
        specicalFlags_singleQuotes[4] = 4;
        specicalFlags_singleQuotes[5] = 4;
        specicalFlags_singleQuotes[6] = 4;
        specicalFlags_singleQuotes[7] = 4;
        specicalFlags_singleQuotes[8] = 1;
        specicalFlags_singleQuotes[9] = 1;
        specicalFlags_singleQuotes[10] = 1;
        specicalFlags_singleQuotes[11] = 4;
        specicalFlags_singleQuotes[12] = 1;
        specicalFlags_singleQuotes[13] = 1;
        specicalFlags_singleQuotes[92] = 1;
        specicalFlags_singleQuotes[39] = 1;
        for (int i = 14; i <= 31; i++) {
            specicalFlags_doubleQuotes[i] = 4;
            specicalFlags_singleQuotes[i] = 4;
        }
        for (int i2 = 127; i2 < 160; i2++) {
            specicalFlags_doubleQuotes[i2] = 4;
            specicalFlags_singleQuotes[i2] = 4;
        }
        for (int i3 = 0; i3 < 161; i3++) {
            boolean[] zArr = specicalFlags_doubleQuotesFlags;
            if (specicalFlags_doubleQuotes[i3] != 0) {
                z = true;
            } else {
                z = false;
            }
            zArr[i3] = z;
            boolean[] zArr2 = specicalFlags_singleQuotesFlags;
            if (specicalFlags_singleQuotes[i3] != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            zArr2[i3] = z2;
        }
        replaceChars[0] = '0';
        replaceChars[1] = '1';
        replaceChars[2] = '2';
        replaceChars[3] = '3';
        replaceChars[4] = '4';
        replaceChars[5] = '5';
        replaceChars[6] = '6';
        replaceChars[7] = '7';
        replaceChars[8] = 'b';
        replaceChars[9] = 't';
        replaceChars[10] = 'n';
        replaceChars[11] = 'v';
        replaceChars[12] = 'f';
        replaceChars[13] = 'r';
        replaceChars[34] = '\"';
        replaceChars[39] = '\'';
        replaceChars[47] = '/';
        replaceChars[92] = '\\';
        Arrays.fill(IA, -1);
        int iS = CA.length;
        for (int i4 = 0; i4 < iS; i4++) {
            IA[CA[i4]] = i4;
        }
        IA[61] = 0;
    }

    static class PropertiesInitializer {
        PropertiesInitializer() {
        }

        public void autoConfig() {
            IOUtils.loadPropertiesFromFile();
            TypeUtils.compatibleWithJavaBean = "true".equals(IOUtils.getStringProperty(IOUtils.FASTJSON_COMPATIBLEWITHJAVABEAN));
            TypeUtils.compatibleWithFieldName = "true".equals(IOUtils.getStringProperty(IOUtils.FASTJSON_COMPATIBLEWITHFIELDNAME));
        }
    }

    public static String getStringProperty(String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        } catch (SecurityException e) {
        }
        return prop == null ? DEFAULT_PROPERTIES.getProperty(name) : prop;
    }

    public static void loadPropertiesFromFile() {
        InputStream imputStream = (InputStream) AccessController.doPrivileged(new PrivilegedAction<InputStream>() {
            public InputStream run() {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                if (cl != null) {
                    return cl.getResourceAsStream(IOUtils.FASTJSON_PROPERTIES);
                }
                return ClassLoader.getSystemResourceAsStream(IOUtils.FASTJSON_PROPERTIES);
            }
        });
        if (imputStream != null) {
            try {
                DEFAULT_PROPERTIES.load(imputStream);
                imputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public static void close(Closeable x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception e) {
            }
        }
    }

    public static int stringSize(long x) {
        long p = 10;
        for (int i = 1; i < 19; i++) {
            if (x < p) {
                return i;
            }
            p *= 10;
        }
        return 19;
    }

    public static void getChars(long i, int index, char[] buf) {
        int charPos = index;
        char sign = 0;
        if (i < 0) {
            sign = Soundex.SILENT_MARKER;
            i = -i;
        }
        while (i > 2147483647L) {
            long q = i / 100;
            int r = (int) (i - (((q << 6) + (q << 5)) + (q << 2)));
            i = q;
            int charPos2 = charPos - 1;
            buf[charPos2] = DigitOnes[r];
            charPos = charPos2 - 1;
            buf[charPos] = DigitTens[r];
        }
        int i2 = (int) i;
        while (i2 >= 65536) {
            int q2 = i2 / 100;
            int r2 = i2 - (((q2 << 6) + (q2 << 5)) + (q2 << 2));
            i2 = q2;
            int charPos3 = charPos - 1;
            buf[charPos3] = DigitOnes[r2];
            charPos = charPos3 - 1;
            buf[charPos] = DigitTens[r2];
        }
        do {
            int q22 = (52429 * i2) >>> 19;
            charPos--;
            buf[charPos] = digits[i2 - ((q22 << 3) + (q22 << 1))];
            i2 = q22;
        } while (i2 != 0);
        if (sign != 0) {
            buf[charPos - 1] = sign;
        }
    }

    public static void getChars(int i, int index, char[] buf) {
        int charPos = index;
        char sign = 0;
        if (i < 0) {
            sign = Soundex.SILENT_MARKER;
            i = -i;
        }
        while (i >= 65536) {
            int q = i / 100;
            int r = i - (((q << 6) + (q << 5)) + (q << 2));
            i = q;
            int charPos2 = charPos - 1;
            buf[charPos2] = DigitOnes[r];
            charPos = charPos2 - 1;
            buf[charPos] = DigitTens[r];
        }
        do {
            int q2 = (52429 * i) >>> 19;
            charPos--;
            buf[charPos] = digits[i - ((q2 << 3) + (q2 << 1))];
            i = q2;
        } while (i != 0);
        if (sign != 0) {
            buf[charPos - 1] = sign;
        }
    }

    public static void getChars(byte b, int index, char[] buf) {
        int i = b;
        int charPos = index;
        char sign = 0;
        if (i < 0) {
            sign = Soundex.SILENT_MARKER;
            i = -i;
        }
        do {
            int q = (52429 * i) >>> 19;
            charPos--;
            buf[charPos] = digits[i - ((q << 3) + (q << 1))];
            i = q;
        } while (i != 0);
        if (sign != 0) {
            buf[charPos - 1] = sign;
        }
    }

    public static int stringSize(int x) {
        int i = 0;
        while (x > sizeTable[i]) {
            i++;
        }
        return i + 1;
    }

    public static void decode(CharsetDecoder charsetDecoder, ByteBuffer byteBuf, CharBuffer charByte) {
        try {
            CoderResult cr = charsetDecoder.decode(byteBuf, charByte, true);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
            CoderResult cr2 = charsetDecoder.flush(charByte);
            if (!cr2.isUnderflow()) {
                cr2.throwException();
            }
        } catch (CharacterCodingException x) {
            throw new JSONException("utf8 decode error, " + x.getMessage(), x);
        }
    }

    public static boolean firstIdentifier(char ch) {
        return ch < firstIdentifierFlags.length && firstIdentifierFlags[ch];
    }

    public static boolean isIdent(char ch) {
        return ch < identifierFlags.length && identifierFlags[ch];
    }

    public static byte[] decodeBase64(char[] chars, int offset, int charsLen) {
        int sepCnt;
        int sIx;
        if (charsLen == 0) {
            return new byte[0];
        }
        int sIx2 = offset;
        int eIx = (offset + charsLen) - 1;
        while (sIx2 < eIx && IA[chars[sIx2]] < 0) {
            sIx2++;
        }
        while (eIx > 0 && IA[chars[eIx]] < 0) {
            eIx--;
        }
        int pad = chars[eIx] == '=' ? chars[eIx + -1] == '=' ? 2 : 1 : 0;
        int cCnt = (eIx - sIx2) + 1;
        if (charsLen > 76) {
            sepCnt = (chars[76] == 13 ? cCnt / 78 : 0) << 1;
        } else {
            sepCnt = 0;
        }
        int len = (((cCnt - sepCnt) * 6) >> 3) - pad;
        byte[] bytes = new byte[len];
        int cc = 0;
        int eLen = (len / 3) * 3;
        int d = 0;
        int sIx3 = sIx2;
        while (d < eLen) {
            int sIx4 = sIx3 + 1;
            int sIx5 = sIx4 + 1;
            int sIx6 = sIx5 + 1;
            int sIx7 = sIx6 + 1;
            int i = (IA[chars[sIx3]] << 18) | (IA[chars[sIx4]] << 12) | (IA[chars[sIx5]] << 6) | IA[chars[sIx6]];
            int d2 = d + 1;
            bytes[d] = (byte) (i >> 16);
            int d3 = d2 + 1;
            bytes[d2] = (byte) (i >> 8);
            int d4 = d3 + 1;
            bytes[d3] = (byte) i;
            if (sepCnt <= 0 || (cc = cc + 1) != 19) {
                sIx = sIx7;
            } else {
                sIx = sIx7 + 2;
                cc = 0;
            }
            d = d4;
            sIx3 = sIx;
        }
        if (d < len) {
            int i2 = 0;
            int j = 0;
            while (sIx3 <= eIx - pad) {
                i2 |= IA[chars[sIx3]] << (18 - (j * 6));
                j++;
                sIx3++;
            }
            int r = 16;
            while (d < len) {
                bytes[d] = (byte) (i2 >> r);
                r -= 8;
                d++;
            }
        }
        int i3 = d;
        int i4 = sIx3;
        return bytes;
    }

    public static byte[] decodeBase64(String chars, int offset, int charsLen) {
        int sepCnt;
        int sIx;
        if (charsLen == 0) {
            return new byte[0];
        }
        int sIx2 = offset;
        int eIx = (offset + charsLen) - 1;
        while (sIx2 < eIx && IA[chars.charAt(sIx2)] < 0) {
            sIx2++;
        }
        while (eIx > 0 && IA[chars.charAt(eIx)] < 0) {
            eIx--;
        }
        int pad = chars.charAt(eIx) == '=' ? chars.charAt(eIx + -1) == '=' ? 2 : 1 : 0;
        int cCnt = (eIx - sIx2) + 1;
        if (charsLen > 76) {
            sepCnt = (chars.charAt(76) == 13 ? cCnt / 78 : 0) << 1;
        } else {
            sepCnt = 0;
        }
        int len = (((cCnt - sepCnt) * 6) >> 3) - pad;
        byte[] bytes = new byte[len];
        int cc = 0;
        int eLen = (len / 3) * 3;
        int d = 0;
        int sIx3 = sIx2;
        while (d < eLen) {
            int sIx4 = sIx3 + 1;
            int sIx5 = sIx4 + 1;
            int sIx6 = sIx5 + 1;
            int sIx7 = sIx6 + 1;
            int i = (IA[chars.charAt(sIx3)] << 18) | (IA[chars.charAt(sIx4)] << 12) | (IA[chars.charAt(sIx5)] << 6) | IA[chars.charAt(sIx6)];
            int d2 = d + 1;
            bytes[d] = (byte) (i >> 16);
            int d3 = d2 + 1;
            bytes[d2] = (byte) (i >> 8);
            int d4 = d3 + 1;
            bytes[d3] = (byte) i;
            if (sepCnt <= 0 || (cc = cc + 1) != 19) {
                sIx = sIx7;
            } else {
                sIx = sIx7 + 2;
                cc = 0;
            }
            d = d4;
            sIx3 = sIx;
        }
        if (d < len) {
            int i2 = 0;
            int j = 0;
            while (sIx3 <= eIx - pad) {
                i2 |= IA[chars.charAt(sIx3)] << (18 - (j * 6));
                j++;
                sIx3++;
            }
            int r = 16;
            while (d < len) {
                bytes[d] = (byte) (i2 >> r);
                r -= 8;
                d++;
            }
        }
        int i3 = d;
        int i4 = sIx3;
        return bytes;
    }

    public static byte[] decodeBase64(String s) {
        int sepCnt;
        int sIx;
        int sLen = s.length();
        if (sLen == 0) {
            return new byte[0];
        }
        int sIx2 = 0;
        int eIx = sLen - 1;
        while (sIx2 < eIx && IA[s.charAt(sIx2) & 255] < 0) {
            sIx2++;
        }
        while (eIx > 0 && IA[s.charAt(eIx) & 255] < 0) {
            eIx--;
        }
        int pad = s.charAt(eIx) == '=' ? s.charAt(eIx + -1) == '=' ? 2 : 1 : 0;
        int cCnt = (eIx - sIx2) + 1;
        if (sLen > 76) {
            sepCnt = (s.charAt(76) == 13 ? cCnt / 78 : 0) << 1;
        } else {
            sepCnt = 0;
        }
        int len = (((cCnt - sepCnt) * 6) >> 3) - pad;
        byte[] dArr = new byte[len];
        int cc = 0;
        int eLen = (len / 3) * 3;
        int d = 0;
        int sIx3 = sIx2;
        while (d < eLen) {
            int sIx4 = sIx3 + 1;
            int sIx5 = sIx4 + 1;
            int sIx6 = sIx5 + 1;
            int sIx7 = sIx6 + 1;
            int i = (IA[s.charAt(sIx3)] << 18) | (IA[s.charAt(sIx4)] << 12) | (IA[s.charAt(sIx5)] << 6) | IA[s.charAt(sIx6)];
            int d2 = d + 1;
            dArr[d] = (byte) (i >> 16);
            int d3 = d2 + 1;
            dArr[d2] = (byte) (i >> 8);
            int d4 = d3 + 1;
            dArr[d3] = (byte) i;
            if (sepCnt <= 0 || (cc = cc + 1) != 19) {
                sIx = sIx7;
            } else {
                sIx = sIx7 + 2;
                cc = 0;
            }
            d = d4;
            sIx3 = sIx;
        }
        if (d < len) {
            int i2 = 0;
            int j = 0;
            while (sIx3 <= eIx - pad) {
                i2 |= IA[s.charAt(sIx3)] << (18 - (j * 6));
                j++;
                sIx3++;
            }
            int r = 16;
            while (d < len) {
                dArr[d] = (byte) (i2 >> r);
                r -= 8;
                d++;
            }
        }
        int i3 = d;
        int i4 = sIx3;
        return dArr;
    }

    public static int encodeUTF8(char[] sa, int sp, int len, byte[] da) {
        int dp;
        int uc;
        int sl = sp + len;
        int dlASCII = 0 + Math.min(len, da.length);
        int dp2 = 0;
        int sp2 = sp;
        while (dp2 < dlASCII && sa[sp2] < 128) {
            da[dp2] = (byte) sa[sp2];
            dp2++;
            sp2++;
        }
        while (sp2 < sl) {
            int sp3 = sp2 + 1;
            char c = sa[sp2];
            if (c < 128) {
                dp = dp2 + 1;
                da[dp2] = (byte) c;
            } else if (c < 2048) {
                int dp3 = dp2 + 1;
                da[dp2] = (byte) ((c >> 6) | 192);
                da[dp3] = (byte) ((c & '?') | 128);
                dp = dp3 + 1;
            } else if (c < 55296 || c >= 57344) {
                int dp4 = dp2 + 1;
                da[dp2] = (byte) ((c >> CsvReader.Letters.FORM_FEED) | Opcodes.SHL_INT_LIT8);
                int dp5 = dp4 + 1;
                da[dp4] = (byte) (((c >> 6) & 63) | 128);
                dp = dp5 + 1;
                da[dp5] = (byte) ((c & '?') | 128);
            } else {
                int ip = sp3 - 1;
                if (Character.isHighSurrogate(c)) {
                    if (sl - ip < 2) {
                        uc = -1;
                    } else {
                        char d = sa[ip + 1];
                        if (Character.isLowSurrogate(d)) {
                            uc = Character.toCodePoint(c, d);
                        } else {
                            throw new JSONException("encodeUTF8 error", new MalformedInputException(1));
                        }
                    }
                } else if (Character.isLowSurrogate(c)) {
                    throw new JSONException("encodeUTF8 error", new MalformedInputException(1));
                } else {
                    uc = c;
                }
                if (uc < 0) {
                    dp = dp2 + 1;
                    da[dp2] = 63;
                } else {
                    int dp6 = dp2 + 1;
                    da[dp2] = (byte) ((uc >> 18) | 240);
                    int dp7 = dp6 + 1;
                    da[dp6] = (byte) (((uc >> 12) & 63) | 128);
                    int dp8 = dp7 + 1;
                    da[dp7] = (byte) (((uc >> 6) & 63) | 128);
                    da[dp8] = (byte) ((uc & 63) | 128);
                    sp3++;
                    dp = dp8 + 1;
                }
            }
            dp2 = dp;
            sp2 = sp3;
        }
        return dp2;
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 163 */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0033, code lost:
        r17[r8] = (char) r1;
        r8 = r8 + 1;
        r10 = r15;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int decodeUTF8(byte[] r14, int r15, int r16, char[] r17) {
        /*
            int r9 = r15 + r16
            r7 = 0
            r0 = r17
            int r12 = r0.length
            r0 = r16
            int r6 = java.lang.Math.min(r0, r12)
            r8 = r7
            r10 = r15
        L_0x000e:
            if (r8 >= r6) goto L_0x002b
            byte r12 = r14[r10]
            if (r12 < 0) goto L_0x002b
            int r7 = r8 + 1
            int r15 = r10 + 1
            byte r12 = r14[r10]
            char r12 = (char) r12
            r17[r8] = r12
            r8 = r7
            r10 = r15
            goto L_0x000e
        L_0x0020:
            int r7 = r8 + 1
            int r12 = r1 << 6
            r12 = r12 ^ r2
            r12 = r12 ^ 3968(0xf80, float:5.56E-42)
            char r12 = (char) r12
            r17[r8] = r12
            r8 = r7
        L_0x002b:
            if (r10 >= r9) goto L_0x00f1
            int r15 = r10 + 1
            byte r1 = r14[r10]
            if (r1 < 0) goto L_0x003b
            int r7 = r8 + 1
            char r12 = (char) r1
            r17[r8] = r12
            r8 = r7
            r10 = r15
            goto L_0x002b
        L_0x003b:
            int r12 = r1 >> 5
            r13 = -2
            if (r12 != r13) goto L_0x0055
            r12 = r1 & 30
            if (r12 == 0) goto L_0x0055
            if (r15 >= r9) goto L_0x0053
            int r10 = r15 + 1
            byte r2 = r14[r15]
            r12 = r2 & 192(0xc0, float:2.69E-43)
            r13 = 128(0x80, float:1.794E-43)
            if (r12 == r13) goto L_0x0020
            r8 = -1
            r15 = r10
        L_0x0052:
            return r8
        L_0x0053:
            r8 = -1
            goto L_0x0052
        L_0x0055:
            int r12 = r1 >> 4
            r13 = -2
            if (r12 != r13) goto L_0x009a
            int r12 = r15 + 1
            if (r12 >= r9) goto L_0x0098
            int r10 = r15 + 1
            byte r2 = r14[r15]
            int r15 = r10 + 1
            byte r3 = r14[r10]
            r12 = -32
            if (r1 != r12) goto L_0x0070
            r12 = r2 & 224(0xe0, float:3.14E-43)
            r13 = 128(0x80, float:1.794E-43)
            if (r12 == r13) goto L_0x007c
        L_0x0070:
            r12 = r2 & 192(0xc0, float:2.69E-43)
            r13 = 128(0x80, float:1.794E-43)
            if (r12 != r13) goto L_0x007c
            r12 = r3 & 192(0xc0, float:2.69E-43)
            r13 = 128(0x80, float:1.794E-43)
            if (r12 == r13) goto L_0x007e
        L_0x007c:
            r8 = -1
            goto L_0x0052
        L_0x007e:
            int r12 = r1 << 12
            int r13 = r2 << 6
            r12 = r12 ^ r13
            r13 = -123008(0xfffffffffffe1f80, float:NaN)
            r13 = r13 ^ r3
            r12 = r12 ^ r13
            char r5 = (char) r12
            boolean r12 = java.lang.Character.isSurrogate(r5)
            if (r12 == 0) goto L_0x0091
            r8 = -1
            goto L_0x0052
        L_0x0091:
            int r7 = r8 + 1
            r17[r8] = r5
            r8 = r7
            r10 = r15
            goto L_0x002b
        L_0x0098:
            r8 = -1
            goto L_0x0052
        L_0x009a:
            int r12 = r1 >> 3
            r13 = -2
            if (r12 != r13) goto L_0x00ee
            int r12 = r15 + 2
            if (r12 >= r9) goto L_0x00eb
            int r10 = r15 + 1
            byte r2 = r14[r15]
            int r15 = r10 + 1
            byte r3 = r14[r10]
            int r10 = r15 + 1
            byte r4 = r14[r15]
            int r12 = r1 << 18
            int r13 = r2 << 12
            r12 = r12 ^ r13
            int r13 = r3 << 6
            r12 = r12 ^ r13
            r13 = 3678080(0x381f80, float:5.154088E-39)
            r13 = r13 ^ r4
            r11 = r12 ^ r13
            r12 = r2 & 192(0xc0, float:2.69E-43)
            r13 = 128(0x80, float:1.794E-43)
            if (r12 != r13) goto L_0x00d5
            r12 = r3 & 192(0xc0, float:2.69E-43)
            r13 = 128(0x80, float:1.794E-43)
            if (r12 != r13) goto L_0x00d5
            r12 = r4 & 192(0xc0, float:2.69E-43)
            r13 = 128(0x80, float:1.794E-43)
            if (r12 != r13) goto L_0x00d5
            boolean r12 = java.lang.Character.isSupplementaryCodePoint(r11)
            if (r12 != 0) goto L_0x00d9
        L_0x00d5:
            r8 = -1
            r15 = r10
            goto L_0x0052
        L_0x00d9:
            int r7 = r8 + 1
            char r12 = java.lang.Character.highSurrogate(r11)
            r17[r8] = r12
            int r8 = r7 + 1
            char r12 = java.lang.Character.lowSurrogate(r11)
            r17[r7] = r12
            goto L_0x002b
        L_0x00eb:
            r8 = -1
            goto L_0x0052
        L_0x00ee:
            r8 = -1
            goto L_0x0052
        L_0x00f1:
            r15 = r10
            goto L_0x0052
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.IOUtils.decodeUTF8(byte[], int, int, char[]):int");
    }

    public static String readAll(Reader reader) {
        StringBuilder buf = new StringBuilder();
        try {
            char[] chars = new char[2048];
            while (true) {
                int len = reader.read(chars, 0, chars.length);
                if (len < 0) {
                    return buf.toString();
                }
                buf.append(chars, 0, len);
            }
        } catch (Exception ex) {
            throw new JSONException("read string from reader error", ex);
        }
    }
}
