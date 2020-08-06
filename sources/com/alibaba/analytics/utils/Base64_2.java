package com.alibaba.analytics.utils;

import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.MotionEventCompat;
import anetwork.channel.NetworkListenerState;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Base64_2 {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final int DECODE = 0;
    public static final int DONT_GUNZIP = 4;
    public static final int DO_BREAK_LINES = 8;
    public static final int ENCODE = 1;
    private static final byte EQUALS_SIGN = 61;
    private static final byte EQUALS_SIGN_ENC = -1;
    public static final int GZIP = 2;
    private static final int MAX_LINE_LENGTH = 76;
    private static final byte NEW_LINE = 10;
    public static final int NO_OPTIONS = 0;
    public static final int ORDERED = 32;
    private static final String PREFERRED_ENCODING = "US-ASCII";
    public static final int URL_SAFE = 16;
    private static final byte WHITE_SPACE_ENC = -5;
    private static final byte[] _ORDERED_ALPHABET = {45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};
    private static final byte[] _ORDERED_DECODABET;
    private static final byte[] _STANDARD_ALPHABET = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] _STANDARD_DECODABET = {-9, -9, -9, -9, -9, -9, -9, -9, -9, WHITE_SPACE_ENC, WHITE_SPACE_ENC, -9, -9, WHITE_SPACE_ENC, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, WHITE_SPACE_ENC, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, EQUALS_SIGN, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, NEW_LINE, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, NetworkListenerState.ALL, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};
    private static final byte[] _URL_SAFE_ALPHABET = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    private static final byte[] _URL_SAFE_DECODABET = {-9, -9, -9, -9, -9, -9, -9, -9, -9, WHITE_SPACE_ENC, WHITE_SPACE_ENC, -9, -9, WHITE_SPACE_ENC, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, WHITE_SPACE_ENC, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, EQUALS_SIGN, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, NEW_LINE, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, NetworkListenerState.ALL, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};

    static {
        boolean z;
        if (!Base64_2.class.desiredAssertionStatus()) {
            z = true;
        } else {
            z = false;
        }
        $assertionsDisabled = z;
        byte[] bArr = new byte[InputDeviceCompat.SOURCE_KEYBOARD];
        // fill-array-data instruction
        bArr[0] = -9;
        bArr[1] = -9;
        bArr[2] = -9;
        bArr[3] = -9;
        bArr[4] = -9;
        bArr[5] = -9;
        bArr[6] = -9;
        bArr[7] = -9;
        bArr[8] = -9;
        bArr[9] = -5;
        bArr[10] = -5;
        bArr[11] = -9;
        bArr[12] = -9;
        bArr[13] = -5;
        bArr[14] = -9;
        bArr[15] = -9;
        bArr[16] = -9;
        bArr[17] = -9;
        bArr[18] = -9;
        bArr[19] = -9;
        bArr[20] = -9;
        bArr[21] = -9;
        bArr[22] = -9;
        bArr[23] = -9;
        bArr[24] = -9;
        bArr[25] = -9;
        bArr[26] = -9;
        bArr[27] = -9;
        bArr[28] = -9;
        bArr[29] = -9;
        bArr[30] = -9;
        bArr[31] = -9;
        bArr[32] = -5;
        bArr[33] = -9;
        bArr[34] = -9;
        bArr[35] = -9;
        bArr[36] = -9;
        bArr[37] = -9;
        bArr[38] = -9;
        bArr[39] = -9;
        bArr[40] = -9;
        bArr[41] = -9;
        bArr[42] = -9;
        bArr[43] = -9;
        bArr[44] = -9;
        bArr[45] = 0;
        bArr[46] = -9;
        bArr[47] = -9;
        bArr[48] = 1;
        bArr[49] = 2;
        bArr[50] = 3;
        bArr[51] = 4;
        bArr[52] = 5;
        bArr[53] = 6;
        bArr[54] = 7;
        bArr[55] = 8;
        bArr[56] = 9;
        bArr[57] = 10;
        bArr[58] = -9;
        bArr[59] = -9;
        bArr[60] = -9;
        bArr[61] = -1;
        bArr[62] = -9;
        bArr[63] = -9;
        bArr[64] = -9;
        bArr[65] = 11;
        bArr[66] = 12;
        bArr[67] = 13;
        bArr[68] = 14;
        bArr[69] = 15;
        bArr[70] = 16;
        bArr[71] = 17;
        bArr[72] = 18;
        bArr[73] = 19;
        bArr[74] = 20;
        bArr[75] = 21;
        bArr[76] = 22;
        bArr[77] = 23;
        bArr[78] = 24;
        bArr[79] = 25;
        bArr[80] = 26;
        bArr[81] = 27;
        bArr[82] = 28;
        bArr[83] = 29;
        bArr[84] = 30;
        bArr[85] = 31;
        bArr[86] = 32;
        bArr[87] = 33;
        bArr[88] = 34;
        bArr[89] = 35;
        bArr[90] = 36;
        bArr[91] = -9;
        bArr[92] = -9;
        bArr[93] = -9;
        bArr[94] = -9;
        bArr[95] = 37;
        bArr[96] = -9;
        bArr[97] = 38;
        bArr[98] = 39;
        bArr[99] = 40;
        bArr[100] = 41;
        bArr[101] = 42;
        bArr[102] = 43;
        bArr[103] = 44;
        bArr[104] = 45;
        bArr[105] = 46;
        bArr[106] = 47;
        bArr[107] = 48;
        bArr[108] = 49;
        bArr[109] = 50;
        bArr[110] = 51;
        bArr[111] = 52;
        bArr[112] = 53;
        bArr[113] = 54;
        bArr[114] = 55;
        bArr[115] = 56;
        bArr[116] = 57;
        bArr[117] = 58;
        bArr[118] = 59;
        bArr[119] = 60;
        bArr[120] = 61;
        bArr[121] = 62;
        bArr[122] = 63;
        bArr[123] = -9;
        bArr[124] = -9;
        bArr[125] = -9;
        bArr[126] = -9;
        bArr[127] = -9;
        bArr[128] = -9;
        bArr[129] = -9;
        bArr[130] = -9;
        bArr[131] = -9;
        bArr[132] = -9;
        bArr[133] = -9;
        bArr[134] = -9;
        bArr[135] = -9;
        bArr[136] = -9;
        bArr[137] = -9;
        bArr[138] = -9;
        bArr[139] = -9;
        bArr[140] = -9;
        bArr[141] = -9;
        bArr[142] = -9;
        bArr[143] = -9;
        bArr[144] = -9;
        bArr[145] = -9;
        bArr[146] = -9;
        bArr[147] = -9;
        bArr[148] = -9;
        bArr[149] = -9;
        bArr[150] = -9;
        bArr[151] = -9;
        bArr[152] = -9;
        bArr[153] = -9;
        bArr[154] = -9;
        bArr[155] = -9;
        bArr[156] = -9;
        bArr[157] = -9;
        bArr[158] = -9;
        bArr[159] = -9;
        bArr[160] = -9;
        bArr[161] = -9;
        bArr[162] = -9;
        bArr[163] = -9;
        bArr[164] = -9;
        bArr[165] = -9;
        bArr[166] = -9;
        bArr[167] = -9;
        bArr[168] = -9;
        bArr[169] = -9;
        bArr[170] = -9;
        bArr[171] = -9;
        bArr[172] = -9;
        bArr[173] = -9;
        bArr[174] = -9;
        bArr[175] = -9;
        bArr[176] = -9;
        bArr[177] = -9;
        bArr[178] = -9;
        bArr[179] = -9;
        bArr[180] = -9;
        bArr[181] = -9;
        bArr[182] = -9;
        bArr[183] = -9;
        bArr[184] = -9;
        bArr[185] = -9;
        bArr[186] = -9;
        bArr[187] = -9;
        bArr[188] = -9;
        bArr[189] = -9;
        bArr[190] = -9;
        bArr[191] = -9;
        bArr[192] = -9;
        bArr[193] = -9;
        bArr[194] = -9;
        bArr[195] = -9;
        bArr[196] = -9;
        bArr[197] = -9;
        bArr[198] = -9;
        bArr[199] = -9;
        bArr[200] = -9;
        bArr[201] = -9;
        bArr[202] = -9;
        bArr[203] = -9;
        bArr[204] = -9;
        bArr[205] = -9;
        bArr[206] = -9;
        bArr[207] = -9;
        bArr[208] = -9;
        bArr[209] = -9;
        bArr[210] = -9;
        bArr[211] = -9;
        bArr[212] = -9;
        bArr[213] = -9;
        bArr[214] = -9;
        bArr[215] = -9;
        bArr[216] = -9;
        bArr[217] = -9;
        bArr[218] = -9;
        bArr[219] = -9;
        bArr[220] = -9;
        bArr[221] = -9;
        bArr[222] = -9;
        bArr[223] = -9;
        bArr[224] = -9;
        bArr[225] = -9;
        bArr[226] = -9;
        bArr[227] = -9;
        bArr[228] = -9;
        bArr[229] = -9;
        bArr[230] = -9;
        bArr[231] = -9;
        bArr[232] = -9;
        bArr[233] = -9;
        bArr[234] = -9;
        bArr[235] = -9;
        bArr[236] = -9;
        bArr[237] = -9;
        bArr[238] = -9;
        bArr[239] = -9;
        bArr[240] = -9;
        bArr[241] = -9;
        bArr[242] = -9;
        bArr[243] = -9;
        bArr[244] = -9;
        bArr[245] = -9;
        bArr[246] = -9;
        bArr[247] = -9;
        bArr[248] = -9;
        bArr[249] = -9;
        bArr[250] = -9;
        bArr[251] = -9;
        bArr[252] = -9;
        bArr[253] = -9;
        bArr[254] = -9;
        bArr[255] = -9;
        bArr[256] = -9;
        _ORDERED_DECODABET = bArr;
    }

    private static final byte[] getAlphabet(int options) {
        if ((options & 16) == 16) {
            return _URL_SAFE_ALPHABET;
        }
        if ((options & 32) == 32) {
            return _ORDERED_ALPHABET;
        }
        return _STANDARD_ALPHABET;
    }

    /* access modifiers changed from: private */
    public static final byte[] getDecodabet(int options) {
        if ((options & 16) == 16) {
            return _URL_SAFE_DECODABET;
        }
        if ((options & 32) == 32) {
            return _ORDERED_DECODABET;
        }
        return _STANDARD_DECODABET;
    }

    private Base64_2() {
    }

    /* access modifiers changed from: private */
    public static byte[] encode3to4(byte[] b4, byte[] threeBytes, int numSigBytes, int options) {
        encode3to4(threeBytes, 0, numSigBytes, b4, 0, options);
        return b4;
    }

    /* access modifiers changed from: private */
    public static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, int options) {
        int i;
        int i2 = 0;
        byte[] ALPHABET = getAlphabet(options);
        if (numSigBytes > 0) {
            i = (source[srcOffset] << 24) >>> 8;
        } else {
            i = 0;
        }
        int i3 = (numSigBytes > 1 ? (source[srcOffset + 1] << 24) >>> 16 : 0) | i;
        if (numSigBytes > 2) {
            i2 = (source[srcOffset + 2] << 24) >>> 24;
        }
        int inBuff = i3 | i2;
        switch (numSigBytes) {
            case 1:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = EQUALS_SIGN;
                destination[destOffset + 3] = EQUALS_SIGN;
                break;
            case 2:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = EQUALS_SIGN;
                break;
            case 3:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = ALPHABET[inBuff & 63];
                break;
        }
        return destination;
    }

    public static void encode(ByteBuffer raw, ByteBuffer encoded) {
        byte[] raw3 = new byte[3];
        byte[] enc4 = new byte[4];
        while (raw.hasRemaining()) {
            int rem = Math.min(3, raw.remaining());
            raw.get(raw3, 0, rem);
            encode3to4(enc4, raw3, rem, 0);
            encoded.put(enc4);
        }
    }

    public static void encode(ByteBuffer raw, CharBuffer encoded) {
        byte[] raw3 = new byte[3];
        byte[] enc4 = new byte[4];
        while (raw.hasRemaining()) {
            int rem = Math.min(3, raw.remaining());
            raw.get(raw3, 0, rem);
            encode3to4(enc4, raw3, rem, 0);
            for (int i = 0; i < 4; i++) {
                encoded.put((char) (enc4[i] & 255));
            }
        }
    }

    public static String encodeObject(Serializable serializableObject) throws IOException {
        return encodeObject(serializableObject, 0);
    }

    public static String encodeObject(Serializable serializableObject, int options) throws IOException {
        GZIPOutputStream gzos;
        if (serializableObject == null) {
            throw new NullPointerException("Cannot serialize a null object.");
        }
        ByteArrayOutputStream baos = null;
        java.io.OutputStream b64os = null;
        GZIPOutputStream gzos2 = null;
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            try {
                java.io.OutputStream b64os2 = new OutputStream(baos2, options | 1);
                if ((options & 2) != 0) {
                    try {
                        gzos = new GZIPOutputStream(b64os2);
                    } catch (IOException e) {
                        e = e;
                        b64os = b64os2;
                        baos = baos2;
                        try {
                            throw e;
                        } catch (Throwable th) {
                            th = th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        b64os = b64os2;
                        baos = baos2;
                        try {
                            oos.close();
                        } catch (Exception e2) {
                        }
                        try {
                            gzos2.close();
                        } catch (Exception e3) {
                        }
                        try {
                            b64os.close();
                        } catch (Exception e4) {
                        }
                        try {
                            baos.close();
                        } catch (Exception e5) {
                        }
                        throw th;
                    }
                    try {
                        oos = new ObjectOutputStream(gzos);
                        gzos2 = gzos;
                    } catch (IOException e6) {
                        e = e6;
                        gzos2 = gzos;
                        b64os = b64os2;
                        baos = baos2;
                        throw e;
                    } catch (Throwable th3) {
                        th = th3;
                        gzos2 = gzos;
                        b64os = b64os2;
                        baos = baos2;
                        oos.close();
                        gzos2.close();
                        b64os.close();
                        baos.close();
                        throw th;
                    }
                } else {
                    oos = new ObjectOutputStream(b64os2);
                }
                oos.writeObject(serializableObject);
                try {
                    oos.close();
                } catch (Exception e7) {
                }
                try {
                    gzos2.close();
                } catch (Exception e8) {
                }
                try {
                    b64os2.close();
                } catch (Exception e9) {
                }
                try {
                    baos2.close();
                } catch (Exception e10) {
                }
                try {
                    return new String(baos2.toByteArray(), "US-ASCII");
                } catch (UnsupportedEncodingException e11) {
                    return new String(baos2.toByteArray());
                }
            } catch (IOException e12) {
                e = e12;
                baos = baos2;
                throw e;
            } catch (Throwable th4) {
                th = th4;
                baos = baos2;
                oos.close();
                gzos2.close();
                b64os.close();
                baos.close();
                throw th;
            }
        } catch (IOException e13) {
            e = e13;
            throw e;
        }
    }

    public static String encodeBytes(byte[] source) {
        String encoded = null;
        try {
            encoded = encodeBytes(source, 0, source.length, 0);
        } catch (IOException ex) {
            if (!$assertionsDisabled) {
                throw new AssertionError(ex.getMessage());
            }
        }
        if ($assertionsDisabled || encoded != null) {
            return encoded;
        }
        throw new AssertionError();
    }

    public static String encodeBytes(byte[] source, int options) throws IOException {
        return encodeBytes(source, 0, source.length, options);
    }

    public static String encodeBytes(byte[] source, int off, int len) {
        String encoded = null;
        try {
            encoded = encodeBytes(source, off, len, 0);
        } catch (IOException ex) {
            if (!$assertionsDisabled) {
                throw new AssertionError(ex.getMessage());
            }
        }
        if ($assertionsDisabled || encoded != null) {
            return encoded;
        }
        throw new AssertionError();
    }

    public static String encodeBytes(byte[] source, int off, int len, int options) throws IOException {
        byte[] encoded = encodeBytesToBytes(source, off, len, options);
        try {
            return new String(encoded, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            return new String(encoded);
        }
    }

    public static byte[] encodeBytesToBytes(byte[] source) {
        try {
            return encodeBytesToBytes(source, 0, source.length, 0);
        } catch (IOException ex) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError("IOExceptions only come from GZipping, which is turned off: " + ex.getMessage());
        }
    }

    public static byte[] encodeBytesToBytes(byte[] source, int off, int len, int options) throws IOException {
        GZIPOutputStream gZIPOutputStream;
        if (source == null) {
            throw new NullPointerException("Cannot serialize a null array.");
        } else if (off < 0) {
            throw new IllegalArgumentException("Cannot have negative offset: " + off);
        } else if (len < 0) {
            throw new IllegalArgumentException("Cannot have length offset: " + len);
        } else if (off + len > source.length) {
            throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", new Object[]{Integer.valueOf(off), Integer.valueOf(len), Integer.valueOf(source.length)}));
        } else if ((options & 2) != 0) {
            ByteArrayOutputStream baos = null;
            GZIPOutputStream gzos = null;
            OutputStream b64os = null;
            try {
                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                try {
                    OutputStream b64os2 = new OutputStream(baos2, options | 1);
                    try {
                        gZIPOutputStream = new GZIPOutputStream(b64os2);
                    } catch (IOException e) {
                        e = e;
                        b64os = b64os2;
                        baos = baos2;
                        try {
                            throw e;
                        } catch (Throwable th) {
                            th = th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        b64os = b64os2;
                        baos = baos2;
                        try {
                            gzos.close();
                        } catch (Exception e2) {
                        }
                        try {
                            b64os.close();
                        } catch (Exception e3) {
                        }
                        try {
                            baos.close();
                        } catch (Exception e4) {
                        }
                        throw th;
                    }
                    try {
                        gZIPOutputStream.write(source, off, len);
                        gZIPOutputStream.close();
                        try {
                            gZIPOutputStream.close();
                        } catch (Exception e5) {
                        }
                        try {
                            b64os2.close();
                        } catch (Exception e6) {
                        }
                        try {
                            baos2.close();
                        } catch (Exception e7) {
                        }
                        return baos2.toByteArray();
                    } catch (IOException e8) {
                        e = e8;
                        b64os = b64os2;
                        gzos = gZIPOutputStream;
                        baos = baos2;
                        throw e;
                    } catch (Throwable th3) {
                        th = th3;
                        b64os = b64os2;
                        gzos = gZIPOutputStream;
                        baos = baos2;
                        gzos.close();
                        b64os.close();
                        baos.close();
                        throw th;
                    }
                } catch (IOException e9) {
                    e = e9;
                    baos = baos2;
                    throw e;
                } catch (Throwable th4) {
                    th = th4;
                    baos = baos2;
                    gzos.close();
                    b64os.close();
                    baos.close();
                    throw th;
                }
            } catch (IOException e10) {
                e = e10;
                throw e;
            }
        } else {
            boolean breakLines = (options & 8) != 0;
            int encLen = ((len / 3) * 4) + (len % 3 > 0 ? 4 : 0);
            if (breakLines) {
                encLen += encLen / 76;
            }
            byte[] outBuff = new byte[encLen];
            int d = 0;
            int e11 = 0;
            int len2 = len - 2;
            int lineLength = 0;
            while (d < len2) {
                encode3to4(source, d + off, 3, outBuff, e11, options);
                lineLength += 4;
                if (breakLines && lineLength >= 76) {
                    outBuff[e11 + 4] = NEW_LINE;
                    e11++;
                    lineLength = 0;
                }
                d += 3;
                e11 += 4;
            }
            if (d < len) {
                encode3to4(source, d + off, len - d, outBuff, e11, options);
                e11 += 4;
            }
            if (e11 > outBuff.length - 1) {
                return outBuff;
            }
            byte[] finalOut = new byte[e11];
            System.arraycopy(outBuff, 0, finalOut, 0, e11);
            return finalOut;
        }
    }

    /* access modifiers changed from: private */
    public static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset, int options) {
        if (source == null) {
            throw new NullPointerException("Source array was null.");
        } else if (destination == null) {
            throw new NullPointerException("Destination array was null.");
        } else if (srcOffset < 0 || srcOffset + 3 >= source.length) {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", new Object[]{Integer.valueOf(source.length), Integer.valueOf(srcOffset)}));
        } else if (destOffset < 0 || destOffset + 2 >= destination.length) {
            throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", new Object[]{Integer.valueOf(destination.length), Integer.valueOf(destOffset)}));
        } else {
            byte[] DECODABET = getDecodabet(options);
            if (source[srcOffset + 2] == 61) {
                destination[destOffset] = (byte) ((((DECODABET[source[srcOffset]] & 255) << 18) | ((DECODABET[source[srcOffset + 1]] & 255) << 12)) >>> 16);
                return 1;
            } else if (source[srcOffset + 3] == 61) {
                int outBuff = ((DECODABET[source[srcOffset]] & 255) << 18) | ((DECODABET[source[srcOffset + 1]] & 255) << 12) | ((DECODABET[source[srcOffset + 2]] & 255) << 6);
                destination[destOffset] = (byte) (outBuff >>> 16);
                destination[destOffset + 1] = (byte) (outBuff >>> 8);
                return 2;
            } else {
                int outBuff2 = ((DECODABET[source[srcOffset]] & 255) << 18) | ((DECODABET[source[srcOffset + 1]] & 255) << 12) | ((DECODABET[source[srcOffset + 2]] & 255) << 6) | (DECODABET[source[srcOffset + 3]] & 255);
                destination[destOffset] = (byte) (outBuff2 >> 16);
                destination[destOffset + 1] = (byte) (outBuff2 >> 8);
                destination[destOffset + 2] = (byte) outBuff2;
                return 3;
            }
        }
    }

    public static byte[] decode(byte[] source) throws IOException {
        return decode(source, 0, source.length, 0);
    }

    public static String encodeBase64String(byte[] source) {
        if (source == null || source.length <= 0) {
            return "";
        }
        return encodeBytes(source);
    }

    public static byte[] decode(byte[] source, int off, int len, int options) throws IOException {
        if (source == null) {
            throw new NullPointerException("Cannot decode null source array.");
        } else if (off < 0 || off + len > source.length) {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and process %d bytes.", new Object[]{Integer.valueOf(source.length), Integer.valueOf(off), Integer.valueOf(len)}));
        } else if (len == 0) {
            return new byte[0];
        } else {
            if (len < 4) {
                throw new IllegalArgumentException("Base64-encoded string must have at least four characters, but length specified was " + len);
            }
            byte[] DECODABET = getDecodabet(options);
            byte[] outBuff = new byte[((len * 3) / 4)];
            int outBuffPosn = 0;
            byte[] b4 = new byte[4];
            int b4Posn = 0;
            int i = off;
            while (true) {
                int b4Posn2 = b4Posn;
                if (i >= off + len) {
                    int i2 = b4Posn2;
                    break;
                }
                byte sbiDecode = DECODABET[source[i] & 255];
                if (sbiDecode >= -5) {
                    if (sbiDecode >= -1) {
                        b4Posn = b4Posn2 + 1;
                        b4[b4Posn2] = source[i];
                        if (b4Posn > 3) {
                            outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, options);
                            b4Posn = 0;
                            if (source[i] == 61) {
                                break;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        b4Posn = b4Posn2;
                    }
                    i++;
                } else {
                    throw new IOException(String.format("Bad Base64 input character decimal %d in array position %d", new Object[]{Integer.valueOf(source[i] & 255), Integer.valueOf(i)}));
                }
            }
            byte[] out = new byte[outBuffPosn];
            System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
            return out;
        }
    }

    public static byte[] decode(String s) throws IOException {
        return decode(s, 0);
    }

    public static byte[] decode(String s, int options) throws IOException {
        byte[] bytes;
        if (s == null) {
            throw new NullPointerException("Input string was null.");
        }
        try {
            bytes = s.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            bytes = s.getBytes();
        }
        byte[] bytes2 = decode(bytes, 0, bytes.length, options);
        boolean dontGunzip = (options & 4) != 0;
        if (bytes2 != null && bytes2.length >= 4 && !dontGunzip && 35615 == ((bytes2[0] & 255) | ((bytes2[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK))) {
            ByteArrayInputStream bais = null;
            GZIPInputStream gzis = null;
            ByteArrayOutputStream baos = null;
            byte[] buffer = new byte[2048];
            try {
                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                try {
                    ByteArrayInputStream bais2 = new ByteArrayInputStream(bytes2);
                    try {
                        GZIPInputStream gzis2 = new GZIPInputStream(bais2);
                        while (true) {
                            try {
                                int length = gzis2.read(buffer);
                                if (length < 0) {
                                    break;
                                }
                                baos2.write(buffer, 0, length);
                            } catch (IOException e2) {
                                e = e2;
                                baos = baos2;
                                gzis = gzis2;
                                bais = bais2;
                            } catch (Throwable th) {
                                th = th;
                                baos = baos2;
                                gzis = gzis2;
                                bais = bais2;
                                try {
                                    baos.close();
                                } catch (Exception e3) {
                                }
                                try {
                                    gzis.close();
                                } catch (Exception e4) {
                                }
                                try {
                                    bais.close();
                                } catch (Exception e5) {
                                }
                                throw th;
                            }
                        }
                        bytes2 = baos2.toByteArray();
                        try {
                            baos2.close();
                        } catch (Exception e6) {
                        }
                        try {
                            gzis2.close();
                        } catch (Exception e7) {
                        }
                        try {
                            bais2.close();
                        } catch (Exception e8) {
                        }
                    } catch (IOException e9) {
                        e = e9;
                        baos = baos2;
                        bais = bais2;
                        try {
                            e.printStackTrace();
                            try {
                                baos.close();
                            } catch (Exception e10) {
                            }
                            try {
                                gzis.close();
                            } catch (Exception e11) {
                            }
                            try {
                                bais.close();
                            } catch (Exception e12) {
                            }
                            return bytes2;
                        } catch (Throwable th2) {
                            th = th2;
                            baos.close();
                            gzis.close();
                            bais.close();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        baos = baos2;
                        bais = bais2;
                        baos.close();
                        gzis.close();
                        bais.close();
                        throw th;
                    }
                } catch (IOException e13) {
                    e = e13;
                    baos = baos2;
                    e.printStackTrace();
                    baos.close();
                    gzis.close();
                    bais.close();
                    return bytes2;
                } catch (Throwable th4) {
                    th = th4;
                    baos = baos2;
                    baos.close();
                    gzis.close();
                    bais.close();
                    throw th;
                }
            } catch (IOException e14) {
                e = e14;
                e.printStackTrace();
                baos.close();
                gzis.close();
                bais.close();
                return bytes2;
            }
        }
        return bytes2;
    }

    public static Object decodeToObject(String encodedObject) throws IOException, ClassNotFoundException {
        return decodeToObject(encodedObject, 0, (ClassLoader) null);
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:25:0x0031=Splitter:B:25:0x0031, B:16:0x0027=Splitter:B:16:0x0027} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object decodeToObject(java.lang.String r9, int r10, final java.lang.ClassLoader r11) throws java.io.IOException, java.lang.ClassNotFoundException {
        /*
            byte[] r4 = decode(r9, r10)
            r0 = 0
            r5 = 0
            r3 = 0
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream     // Catch:{ IOException -> 0x0026, ClassNotFoundException -> 0x0030 }
            r1.<init>(r4)     // Catch:{ IOException -> 0x0026, ClassNotFoundException -> 0x0030 }
            if (r11 != 0) goto L_0x001f
            java.io.ObjectInputStream r6 = new java.io.ObjectInputStream     // Catch:{ IOException -> 0x0040, ClassNotFoundException -> 0x003d, all -> 0x003a }
            r6.<init>(r1)     // Catch:{ IOException -> 0x0040, ClassNotFoundException -> 0x003d, all -> 0x003a }
            r5 = r6
        L_0x0014:
            java.lang.Object r3 = r5.readObject()     // Catch:{ IOException -> 0x0040, ClassNotFoundException -> 0x003d, all -> 0x003a }
            r1.close()     // Catch:{ Exception -> 0x0032 }
        L_0x001b:
            r5.close()     // Catch:{ Exception -> 0x0034 }
        L_0x001e:
            return r3
        L_0x001f:
            com.alibaba.analytics.utils.Base64_2$1 r6 = new com.alibaba.analytics.utils.Base64_2$1     // Catch:{ IOException -> 0x0040, ClassNotFoundException -> 0x003d, all -> 0x003a }
            r6.<init>(r1, r11)     // Catch:{ IOException -> 0x0040, ClassNotFoundException -> 0x003d, all -> 0x003a }
            r5 = r6
            goto L_0x0014
        L_0x0026:
            r2 = move-exception
        L_0x0027:
            throw r2     // Catch:{ all -> 0x0028 }
        L_0x0028:
            r7 = move-exception
        L_0x0029:
            r0.close()     // Catch:{ Exception -> 0x0036 }
        L_0x002c:
            r5.close()     // Catch:{ Exception -> 0x0038 }
        L_0x002f:
            throw r7
        L_0x0030:
            r2 = move-exception
        L_0x0031:
            throw r2     // Catch:{ all -> 0x0028 }
        L_0x0032:
            r7 = move-exception
            goto L_0x001b
        L_0x0034:
            r7 = move-exception
            goto L_0x001e
        L_0x0036:
            r8 = move-exception
            goto L_0x002c
        L_0x0038:
            r8 = move-exception
            goto L_0x002f
        L_0x003a:
            r7 = move-exception
            r0 = r1
            goto L_0x0029
        L_0x003d:
            r2 = move-exception
            r0 = r1
            goto L_0x0031
        L_0x0040:
            r2 = move-exception
            r0 = r1
            goto L_0x0027
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.utils.Base64_2.decodeToObject(java.lang.String, int, java.lang.ClassLoader):java.lang.Object");
    }

    public static void encodeToFile(byte[] dataToEncode, String filename) throws IOException {
        if (dataToEncode == null) {
            throw new NullPointerException("Data to encode was null.");
        }
        OutputStream bos = null;
        try {
            OutputStream bos2 = new OutputStream(new FileOutputStream(filename), 1);
            try {
                bos2.write(dataToEncode);
                try {
                    bos2.close();
                } catch (Exception e) {
                }
            } catch (IOException e2) {
                e = e2;
                bos = bos2;
                try {
                    throw e;
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Throwable th2) {
                th = th2;
                bos = bos2;
                try {
                    bos.close();
                } catch (Exception e3) {
                }
                throw th;
            }
        } catch (IOException e4) {
            e = e4;
            throw e;
        }
    }

    public static void decodeToFile(String dataToDecode, String filename) throws IOException {
        OutputStream bos = null;
        try {
            OutputStream bos2 = new OutputStream(new FileOutputStream(filename), 0);
            try {
                bos2.write(dataToDecode.getBytes("US-ASCII"));
                try {
                    bos2.close();
                } catch (Exception e) {
                }
            } catch (IOException e2) {
                e = e2;
                bos = bos2;
                try {
                    throw e;
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Throwable th2) {
                th = th2;
                bos = bos2;
                try {
                    bos.close();
                } catch (Exception e3) {
                }
                throw th;
            }
        } catch (IOException e4) {
            e = e4;
            throw e;
        }
    }

    public static byte[] decodeFromFile(String filename) throws IOException {
        InputStream bis = null;
        try {
            File file = new File(filename);
            int length = 0;
            if (file.length() > 2147483647L) {
                throw new IOException("File is too big for this convenience method (" + file.length() + " bytes).");
            }
            byte[] buffer = new byte[((int) file.length())];
            InputStream bis2 = new InputStream(new BufferedInputStream(new FileInputStream(file)), 0);
            while (true) {
                try {
                    int numBytes = bis2.read(buffer, length, 4096);
                    if (numBytes < 0) {
                        break;
                    }
                    length += numBytes;
                } catch (IOException e) {
                    e = e;
                    bis = bis2;
                    try {
                        throw e;
                    } catch (Throwable th) {
                        th = th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bis = bis2;
                    try {
                        bis.close();
                    } catch (Exception e2) {
                    }
                    throw th;
                }
            }
            byte[] decodedData = new byte[length];
            System.arraycopy(buffer, 0, decodedData, 0, length);
            try {
                bis2.close();
            } catch (Exception e3) {
            }
            return decodedData;
        } catch (IOException e4) {
            e = e4;
            throw e;
        }
    }

    public static String encodeFromFile(String filename) throws IOException {
        InputStream bis = null;
        try {
            File file = new File(filename);
            byte[] buffer = new byte[Math.max((int) ((((double) file.length()) * 1.4d) + 1.0d), 40)];
            int length = 0;
            InputStream bis2 = new InputStream(new BufferedInputStream(new FileInputStream(file)), 1);
            while (true) {
                try {
                    int numBytes = bis2.read(buffer, length, 4096);
                    if (numBytes < 0) {
                        break;
                    }
                    length += numBytes;
                } catch (IOException e) {
                    e = e;
                    bis = bis2;
                    try {
                        throw e;
                    } catch (Throwable th) {
                        th = th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bis = bis2;
                    try {
                        bis.close();
                    } catch (Exception e2) {
                    }
                    throw th;
                }
            }
            String encodedData = new String(buffer, 0, length, "US-ASCII");
            try {
                bis2.close();
            } catch (Exception e3) {
            }
            return encodedData;
        } catch (IOException e4) {
            e = e4;
            throw e;
        }
    }

    public static void encodeFileToFile(String infile, String outfile) throws IOException {
        String encoded = encodeFromFile(infile);
        java.io.OutputStream out = null;
        try {
            java.io.OutputStream out2 = new BufferedOutputStream(new FileOutputStream(outfile));
            try {
                out2.write(encoded.getBytes("US-ASCII"));
                try {
                    out2.close();
                } catch (Exception e) {
                }
            } catch (IOException e2) {
                e = e2;
                out = out2;
                try {
                    throw e;
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Throwable th2) {
                th = th2;
                out = out2;
                try {
                    out.close();
                } catch (Exception e3) {
                }
                throw th;
            }
        } catch (IOException e4) {
            e = e4;
            throw e;
        }
    }

    public static void decodeFileToFile(String infile, String outfile) throws IOException {
        byte[] decoded = decodeFromFile(infile);
        java.io.OutputStream out = null;
        try {
            java.io.OutputStream out2 = new BufferedOutputStream(new FileOutputStream(outfile));
            try {
                out2.write(decoded);
                try {
                    out2.close();
                } catch (Exception e) {
                }
            } catch (IOException e2) {
                e = e2;
                out = out2;
                try {
                    throw e;
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Throwable th2) {
                th = th2;
                out = out2;
                try {
                    out.close();
                } catch (Exception e3) {
                }
                throw th;
            }
        } catch (IOException e4) {
            e = e4;
            throw e;
        }
    }

    public static class InputStream extends FilterInputStream {
        private boolean breakLines;
        private byte[] buffer;
        private int bufferLength;
        private byte[] decodabet;
        private boolean encode;
        private int lineLength;
        private int numSigBytes;
        private int options;
        private int position;

        public InputStream(java.io.InputStream in) {
            this(in, 0);
        }

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public InputStream(java.io.InputStream in, int options2) {
            super(in);
            boolean z = true;
            this.options = options2;
            this.breakLines = (options2 & 8) > 0;
            this.encode = (options2 & 1) <= 0 ? false : z;
            this.bufferLength = this.encode ? 4 : 3;
            this.buffer = new byte[this.bufferLength];
            this.position = -1;
            this.lineLength = 0;
            this.decodabet = Base64_2.getDecodabet(options2);
        }

        /* JADX WARNING: Removed duplicated region for block: B:27:0x0067 A[LOOP:1: B:18:0x0042->B:27:0x0067, LOOP_END] */
        /* JADX WARNING: Removed duplicated region for block: B:48:0x0058 A[EDGE_INSN: B:48:0x0058->B:25:0x0058 ?: BREAK  , SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int read() throws java.io.IOException {
            /*
                r11 = this;
                r4 = 3
                r10 = 4
                r9 = -1
                r1 = 0
                int r3 = r11.position
                if (r3 >= 0) goto L_0x0030
                boolean r3 = r11.encode
                if (r3 == 0) goto L_0x003e
                byte[] r0 = new byte[r4]
                r2 = 0
                r8 = 0
            L_0x0010:
                if (r8 >= r4) goto L_0x0022
                java.io.InputStream r3 = r11.in
                int r6 = r3.read()
                if (r6 < 0) goto L_0x0022
                byte r3 = (byte) r6
                r0[r8] = r3
                int r2 = r2 + 1
                int r8 = r8 + 1
                goto L_0x0010
            L_0x0022:
                if (r2 <= 0) goto L_0x003c
                byte[] r3 = r11.buffer
                int r5 = r11.options
                r4 = r1
                byte[] unused = com.alibaba.analytics.utils.Base64_2.encode3to4(r0, r1, r2, r3, r4, r5)
                r11.position = r1
                r11.numSigBytes = r10
            L_0x0030:
                int r3 = r11.position
                if (r3 < 0) goto L_0x00a8
                int r3 = r11.position
                int r4 = r11.numSigBytes
                if (r3 < r4) goto L_0x007a
                r1 = r9
            L_0x003b:
                return r1
            L_0x003c:
                r1 = r9
                goto L_0x003b
            L_0x003e:
                byte[] r7 = new byte[r10]
                r8 = 0
                r8 = 0
            L_0x0042:
                if (r8 >= r10) goto L_0x0058
                r6 = 0
            L_0x0045:
                java.io.InputStream r3 = r11.in
                int r6 = r3.read()
                if (r6 < 0) goto L_0x0056
                byte[] r3 = r11.decodabet
                r4 = r6 & 127(0x7f, float:1.78E-43)
                byte r3 = r3[r4]
                r4 = -5
                if (r3 <= r4) goto L_0x0045
            L_0x0056:
                if (r6 >= 0) goto L_0x0067
            L_0x0058:
                if (r8 != r10) goto L_0x006d
                byte[] r3 = r11.buffer
                int r4 = r11.options
                int r3 = com.alibaba.analytics.utils.Base64_2.decode4to3(r7, r1, r3, r1, r4)
                r11.numSigBytes = r3
                r11.position = r1
                goto L_0x0030
            L_0x0067:
                byte r3 = (byte) r6
                r7[r8] = r3
                int r8 = r8 + 1
                goto L_0x0042
            L_0x006d:
                if (r8 != 0) goto L_0x0071
                r1 = r9
                goto L_0x003b
            L_0x0071:
                java.io.IOException r1 = new java.io.IOException
                java.lang.String r3 = "Improperly padded Base64 input."
                r1.<init>(r3)
                throw r1
            L_0x007a:
                boolean r3 = r11.encode
                if (r3 == 0) goto L_0x008d
                boolean r3 = r11.breakLines
                if (r3 == 0) goto L_0x008d
                int r3 = r11.lineLength
                r4 = 76
                if (r3 < r4) goto L_0x008d
                r11.lineLength = r1
                r1 = 10
                goto L_0x003b
            L_0x008d:
                int r1 = r11.lineLength
                int r1 = r1 + 1
                r11.lineLength = r1
                byte[] r1 = r11.buffer
                int r3 = r11.position
                int r4 = r3 + 1
                r11.position = r4
                byte r6 = r1[r3]
                int r1 = r11.position
                int r3 = r11.bufferLength
                if (r1 < r3) goto L_0x00a5
                r11.position = r9
            L_0x00a5:
                r1 = r6 & 255(0xff, float:3.57E-43)
                goto L_0x003b
            L_0x00a8:
                java.io.IOException r1 = new java.io.IOException
                java.lang.String r3 = "Error in Base64 code reading stream."
                r1.<init>(r3)
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.utils.Base64_2.InputStream.read():int");
        }

        public int read(byte[] dest, int off, int len) throws IOException {
            int i = 0;
            while (i < len) {
                int b = read();
                if (b >= 0) {
                    dest[off + i] = (byte) b;
                    i++;
                } else if (i == 0) {
                    return -1;
                } else {
                    return i;
                }
            }
            return i;
        }
    }

    public static class OutputStream extends FilterOutputStream {
        private byte[] b4;
        private boolean breakLines;
        private byte[] buffer;
        private int bufferLength;
        private byte[] decodabet;
        private boolean encode;
        private int lineLength;
        private int options;
        private int position;
        private boolean suspendEncoding;

        public OutputStream(java.io.OutputStream out) {
            this(out, 1);
        }

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public OutputStream(java.io.OutputStream out, int options2) {
            super(out);
            int i;
            boolean z = true;
            this.breakLines = (options2 & 8) != 0;
            this.encode = (options2 & 1) == 0 ? false : z;
            if (this.encode) {
                i = 3;
            } else {
                i = 4;
            }
            this.bufferLength = i;
            this.buffer = new byte[this.bufferLength];
            this.position = 0;
            this.lineLength = 0;
            this.suspendEncoding = false;
            this.b4 = new byte[4];
            this.options = options2;
            this.decodabet = Base64_2.getDecodabet(options2);
        }

        public void write(int theByte) throws IOException {
            if (this.suspendEncoding) {
                this.out.write(theByte);
            } else if (this.encode) {
                byte[] bArr = this.buffer;
                int i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) theByte;
                if (this.position >= this.bufferLength) {
                    this.out.write(Base64_2.encode3to4(this.b4, this.buffer, this.bufferLength, this.options));
                    this.lineLength += 4;
                    if (this.breakLines && this.lineLength >= 76) {
                        this.out.write(10);
                        this.lineLength = 0;
                    }
                    this.position = 0;
                }
            } else if (this.decodabet[theByte & 127] > -5) {
                byte[] bArr2 = this.buffer;
                int i2 = this.position;
                this.position = i2 + 1;
                bArr2[i2] = (byte) theByte;
                if (this.position >= this.bufferLength) {
                    this.out.write(this.b4, 0, Base64_2.decode4to3(this.buffer, 0, this.b4, 0, this.options));
                    this.position = 0;
                }
            } else if (this.decodabet[theByte & 127] != -5) {
                throw new IOException("Invalid character in Base64 data.");
            }
        }

        public void write(byte[] theBytes, int off, int len) throws IOException {
            if (this.suspendEncoding) {
                this.out.write(theBytes, off, len);
                return;
            }
            for (int i = 0; i < len; i++) {
                write(theBytes[off + i]);
            }
        }

        public void flushBase64() throws IOException {
            if (this.position <= 0) {
                return;
            }
            if (this.encode) {
                this.out.write(Base64_2.encode3to4(this.b4, this.buffer, this.position, this.options));
                this.position = 0;
                return;
            }
            throw new IOException("Base64 input not properly padded.");
        }

        public void close() throws IOException {
            flushBase64();
            super.close();
            this.buffer = null;
            this.out = null;
        }

        public void suspendEncoding() throws IOException {
            flushBase64();
            this.suspendEncoding = true;
        }

        public void resumeEncoding() {
            this.suspendEncoding = false;
        }
    }
}
