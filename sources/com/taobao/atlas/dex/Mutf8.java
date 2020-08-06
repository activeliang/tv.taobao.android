package com.taobao.atlas.dex;

import com.taobao.atlas.dex.util.ByteInput;
import com.taobao.atlas.dexmerge.dx.io.Opcodes;
import com.taobao.ju.track.csv.CsvReader;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.io.UTFDataFormatException;

public final class Mutf8 {
    private Mutf8() {
    }

    public static String decode(ByteInput in, char[] out) throws UTFDataFormatException {
        int s = 0;
        while (true) {
            char a = (char) (in.readByte() & OnReminderListener.RET_FULL);
            if (a == 0) {
                return new String(out, 0, s);
            }
            out[s] = a;
            if (a < 128) {
                s++;
            } else if ((a & 224) == 192) {
                int b = in.readByte() & 255;
                if ((b & 192) != 128) {
                    throw new UTFDataFormatException("bad second byte");
                }
                out[s] = (char) (((a & 31) << 6) | (b & 63));
                s++;
            } else if ((a & 240) == 224) {
                int b2 = in.readByte() & 255;
                int c = in.readByte() & 255;
                if ((b2 & 192) == 128 && (c & 192) == 128) {
                    out[s] = (char) (((a & 15) << CsvReader.Letters.FORM_FEED) | ((b2 & 63) << 6) | (c & 63));
                    s++;
                }
            } else {
                throw new UTFDataFormatException("bad byte");
            }
        }
        throw new UTFDataFormatException("bad second or third byte");
    }

    private static long countBytes(String s, boolean shortLength) throws UTFDataFormatException {
        long result = 0;
        int length = s.length();
        int i = 0;
        while (i < length) {
            char ch = s.charAt(i);
            if (ch != 0 && ch <= 127) {
                result++;
            } else if (ch <= 2047) {
                result += 2;
            } else {
                result += 3;
            }
            if (!shortLength || result <= 65535) {
                i++;
            } else {
                throw new UTFDataFormatException("String more than 65535 UTF bytes long");
            }
        }
        return result;
    }

    public static void encode(byte[] dst, int offset, String s) {
        int offset2;
        int length = s.length();
        int i = 0;
        int offset3 = offset;
        while (i < length) {
            char ch = s.charAt(i);
            if (ch != 0 && ch <= 127) {
                offset2 = offset3 + 1;
                dst[offset3] = (byte) ch;
            } else if (ch <= 2047) {
                int offset4 = offset3 + 1;
                dst[offset3] = (byte) (((ch >> 6) & 31) | 192);
                dst[offset4] = (byte) ((ch & '?') | 128);
                offset2 = offset4 + 1;
            } else {
                int offset5 = offset3 + 1;
                dst[offset3] = (byte) (((ch >> CsvReader.Letters.FORM_FEED) & 15) | Opcodes.SHL_INT_LIT8);
                int offset6 = offset5 + 1;
                dst[offset5] = (byte) (((ch >> 6) & 63) | 128);
                offset2 = offset6 + 1;
                dst[offset6] = (byte) ((ch & '?') | 128);
            }
            i++;
            offset3 = offset2;
        }
    }

    public static byte[] encode(String s) throws UTFDataFormatException {
        byte[] result = new byte[((int) countBytes(s, true))];
        encode(result, 0, s);
        return result;
    }
}
