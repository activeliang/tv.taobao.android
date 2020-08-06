package org.apache.commons.codec.binary;

import anetwork.channel.NetworkListenerState;
import java.math.BigInteger;
import org.apache.commons.codec.binary.BaseNCodec;

public class Base64 extends BaseNCodec {
    private static final int BITS_PER_ENCODED_BYTE = 6;
    private static final int BYTES_PER_ENCODED_BLOCK = 4;
    private static final int BYTES_PER_UNENCODED_BLOCK = 3;
    static final byte[] CHUNK_SEPARATOR = {13, 10};
    private static final byte[] DECODE_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, NetworkListenerState.ALL, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
    private static final int MASK_6BITS = 63;
    private static final byte[] STANDARD_ENCODE_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] URL_SAFE_ENCODE_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    private final int decodeSize;
    private final byte[] decodeTable;
    private final int encodeSize;
    private final byte[] encodeTable;
    private final byte[] lineSeparator;

    public Base64() {
        this(0);
    }

    public Base64(boolean urlSafe) {
        this(76, CHUNK_SEPARATOR, urlSafe);
    }

    public Base64(int lineLength) {
        this(lineLength, CHUNK_SEPARATOR);
    }

    public Base64(int lineLength, byte[] lineSeparator2) {
        this(lineLength, lineSeparator2, false);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Base64(int r7, byte[] r8, boolean r9) {
        /*
            r6 = this;
            r5 = 0
            r4 = 4
            r2 = 0
            r3 = 3
            if (r8 != 0) goto L_0x003b
            r1 = r2
        L_0x0007:
            r6.<init>(r3, r4, r7, r1)
            byte[] r1 = DECODE_TABLE
            r6.decodeTable = r1
            if (r8 == 0) goto L_0x0061
            boolean r1 = r6.containsAlphabetOrPad(r8)
            if (r1 == 0) goto L_0x003d
            java.lang.String r0 = org.apache.commons.codec.binary.StringUtils.newStringUtf8(r8)
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "lineSeparator must not contain base64 characters: ["
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r0)
            java.lang.String r3 = "]"
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L_0x003b:
            int r1 = r8.length
            goto L_0x0007
        L_0x003d:
            if (r7 <= 0) goto L_0x005c
            int r1 = r8.length
            int r1 = r1 + 4
            r6.encodeSize = r1
            int r1 = r8.length
            byte[] r1 = new byte[r1]
            r6.lineSeparator = r1
            byte[] r1 = r6.lineSeparator
            int r3 = r8.length
            java.lang.System.arraycopy(r8, r2, r1, r2, r3)
        L_0x004f:
            int r1 = r6.encodeSize
            int r1 = r1 + -1
            r6.decodeSize = r1
            if (r9 == 0) goto L_0x0066
            byte[] r1 = URL_SAFE_ENCODE_TABLE
        L_0x0059:
            r6.encodeTable = r1
            return
        L_0x005c:
            r6.encodeSize = r4
            r6.lineSeparator = r5
            goto L_0x004f
        L_0x0061:
            r6.encodeSize = r4
            r6.lineSeparator = r5
            goto L_0x004f
        L_0x0066:
            byte[] r1 = STANDARD_ENCODE_TABLE
            goto L_0x0059
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.binary.Base64.<init>(int, byte[], boolean):void");
    }

    public boolean isUrlSafe() {
        return this.encodeTable == URL_SAFE_ENCODE_TABLE;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: byte} */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void encode(byte[] r10, int r11, int r12, org.apache.commons.codec.binary.BaseNCodec.Context r13) {
        /*
            r9 = this;
            r8 = 0
            boolean r5 = r13.eof
            if (r5 == 0) goto L_0x0006
        L_0x0005:
            return
        L_0x0006:
            if (r12 >= 0) goto L_0x00e5
            r5 = 1
            r13.eof = r5
            int r5 = r13.modulus
            if (r5 != 0) goto L_0x0013
            int r5 = r9.lineLength
            if (r5 == 0) goto L_0x0005
        L_0x0013:
            int r5 = r9.encodeSize
            byte[] r1 = r9.ensureBufferSize(r5, r13)
            int r4 = r13.pos
            int r5 = r13.modulus
            switch(r5) {
                case 0: goto L_0x007a;
                case 1: goto L_0x003c;
                case 2: goto L_0x009e;
                default: goto L_0x0020;
            }
        L_0x0020:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Impossible modulus "
            java.lang.StringBuilder r6 = r6.append(r7)
            int r7 = r13.modulus
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r5.<init>(r6)
            throw r5
        L_0x003c:
            int r5 = r13.pos
            int r6 = r5 + 1
            r13.pos = r6
            byte[] r6 = r9.encodeTable
            int r7 = r13.ibitWorkArea
            int r7 = r7 >> 2
            r7 = r7 & 63
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r13.pos
            int r6 = r5 + 1
            r13.pos = r6
            byte[] r6 = r9.encodeTable
            int r7 = r13.ibitWorkArea
            int r7 = r7 << 4
            r7 = r7 & 63
            byte r6 = r6[r7]
            r1[r5] = r6
            byte[] r5 = r9.encodeTable
            byte[] r6 = STANDARD_ENCODE_TABLE
            if (r5 != r6) goto L_0x007a
            int r5 = r13.pos
            int r6 = r5 + 1
            r13.pos = r6
            byte r6 = r9.pad
            r1[r5] = r6
            int r5 = r13.pos
            int r6 = r5 + 1
            r13.pos = r6
            byte r6 = r9.pad
            r1[r5] = r6
        L_0x007a:
            int r5 = r13.currentLinePos
            int r6 = r13.pos
            int r6 = r6 - r4
            int r5 = r5 + r6
            r13.currentLinePos = r5
            int r5 = r9.lineLength
            if (r5 <= 0) goto L_0x0005
            int r5 = r13.currentLinePos
            if (r5 <= 0) goto L_0x0005
            byte[] r5 = r9.lineSeparator
            int r6 = r13.pos
            byte[] r7 = r9.lineSeparator
            int r7 = r7.length
            java.lang.System.arraycopy(r5, r8, r1, r6, r7)
            int r5 = r13.pos
            byte[] r6 = r9.lineSeparator
            int r6 = r6.length
            int r5 = r5 + r6
            r13.pos = r5
            goto L_0x0005
        L_0x009e:
            int r5 = r13.pos
            int r6 = r5 + 1
            r13.pos = r6
            byte[] r6 = r9.encodeTable
            int r7 = r13.ibitWorkArea
            int r7 = r7 >> 10
            r7 = r7 & 63
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r13.pos
            int r6 = r5 + 1
            r13.pos = r6
            byte[] r6 = r9.encodeTable
            int r7 = r13.ibitWorkArea
            int r7 = r7 >> 4
            r7 = r7 & 63
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r13.pos
            int r6 = r5 + 1
            r13.pos = r6
            byte[] r6 = r9.encodeTable
            int r7 = r13.ibitWorkArea
            int r7 = r7 << 2
            r7 = r7 & 63
            byte r6 = r6[r7]
            r1[r5] = r6
            byte[] r5 = r9.encodeTable
            byte[] r6 = STANDARD_ENCODE_TABLE
            if (r5 != r6) goto L_0x007a
            int r5 = r13.pos
            int r6 = r5 + 1
            r13.pos = r6
            byte r6 = r9.pad
            r1[r5] = r6
            goto L_0x007a
        L_0x00e5:
            r2 = 0
            r3 = r11
        L_0x00e7:
            if (r2 >= r12) goto L_0x0179
            int r5 = r9.encodeSize
            byte[] r1 = r9.ensureBufferSize(r5, r13)
            int r5 = r13.modulus
            int r5 = r5 + 1
            int r5 = r5 % 3
            r13.modulus = r5
            int r11 = r3 + 1
            byte r0 = r10[r3]
            if (r0 >= 0) goto L_0x00ff
            int r0 = r0 + 256
        L_0x00ff:
            int r5 = r13.ibitWorkArea
            int r5 = r5 << 8
            int r5 = r5 + r0
            r13.ibitWorkArea = r5
            int r5 = r13.modulus
            if (r5 != 0) goto L_0x0174
            int r5 = r13.pos
            int r6 = r5 + 1
            r13.pos = r6
            byte[] r6 = r9.encodeTable
            int r7 = r13.ibitWorkArea
            int r7 = r7 >> 18
            r7 = r7 & 63
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r13.pos
            int r6 = r5 + 1
            r13.pos = r6
            byte[] r6 = r9.encodeTable
            int r7 = r13.ibitWorkArea
            int r7 = r7 >> 12
            r7 = r7 & 63
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r13.pos
            int r6 = r5 + 1
            r13.pos = r6
            byte[] r6 = r9.encodeTable
            int r7 = r13.ibitWorkArea
            int r7 = r7 >> 6
            r7 = r7 & 63
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r13.pos
            int r6 = r5 + 1
            r13.pos = r6
            byte[] r6 = r9.encodeTable
            int r7 = r13.ibitWorkArea
            r7 = r7 & 63
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r13.currentLinePos
            int r5 = r5 + 4
            r13.currentLinePos = r5
            int r5 = r9.lineLength
            if (r5 <= 0) goto L_0x0174
            int r5 = r9.lineLength
            int r6 = r13.currentLinePos
            if (r5 > r6) goto L_0x0174
            byte[] r5 = r9.lineSeparator
            int r6 = r13.pos
            byte[] r7 = r9.lineSeparator
            int r7 = r7.length
            java.lang.System.arraycopy(r5, r8, r1, r6, r7)
            int r5 = r13.pos
            byte[] r6 = r9.lineSeparator
            int r6 = r6.length
            int r5 = r5 + r6
            r13.pos = r5
            r13.currentLinePos = r8
        L_0x0174:
            int r2 = r2 + 1
            r3 = r11
            goto L_0x00e7
        L_0x0179:
            r11 = r3
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.binary.Base64.encode(byte[], int, int, org.apache.commons.codec.binary.BaseNCodec$Context):void");
    }

    /* access modifiers changed from: package-private */
    public void decode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context) {
        byte result;
        if (!context.eof) {
            if (inAvail < 0) {
                context.eof = true;
            }
            int i = 0;
            int inPos2 = inPos;
            while (true) {
                if (i >= inAvail) {
                    break;
                }
                byte[] buffer = ensureBufferSize(this.decodeSize, context);
                int inPos3 = inPos2 + 1;
                byte b = in[inPos2];
                if (b == this.pad) {
                    context.eof = true;
                    break;
                }
                if (b >= 0 && b < DECODE_TABLE.length && (result = DECODE_TABLE[b]) >= 0) {
                    context.modulus = (context.modulus + 1) % 4;
                    context.ibitWorkArea = (context.ibitWorkArea << 6) + result;
                    if (context.modulus == 0) {
                        int i2 = context.pos;
                        context.pos = i2 + 1;
                        buffer[i2] = (byte) ((context.ibitWorkArea >> 16) & 255);
                        int i3 = context.pos;
                        context.pos = i3 + 1;
                        buffer[i3] = (byte) ((context.ibitWorkArea >> 8) & 255);
                        int i4 = context.pos;
                        context.pos = i4 + 1;
                        buffer[i4] = (byte) (context.ibitWorkArea & 255);
                    }
                }
                i++;
                inPos2 = inPos3;
            }
            if (context.eof && context.modulus != 0) {
                byte[] buffer2 = ensureBufferSize(this.decodeSize, context);
                switch (context.modulus) {
                    case 1:
                        return;
                    case 2:
                        context.ibitWorkArea >>= 4;
                        int i5 = context.pos;
                        context.pos = i5 + 1;
                        buffer2[i5] = (byte) (context.ibitWorkArea & 255);
                        return;
                    case 3:
                        context.ibitWorkArea >>= 2;
                        int i6 = context.pos;
                        context.pos = i6 + 1;
                        buffer2[i6] = (byte) ((context.ibitWorkArea >> 8) & 255);
                        int i7 = context.pos;
                        context.pos = i7 + 1;
                        buffer2[i7] = (byte) (context.ibitWorkArea & 255);
                        return;
                    default:
                        throw new IllegalStateException("Impossible modulus " + context.modulus);
                }
            }
        }
    }

    @Deprecated
    public static boolean isArrayByteBase64(byte[] arrayOctet) {
        return isBase64(arrayOctet);
    }

    public static boolean isBase64(byte octet) {
        return octet == 61 || (octet >= 0 && octet < DECODE_TABLE.length && DECODE_TABLE[octet] != -1);
    }

    public static boolean isBase64(String base64) {
        return isBase64(StringUtils.getBytesUtf8(base64));
    }

    public static boolean isBase64(byte[] arrayOctet) {
        for (int i = 0; i < arrayOctet.length; i++) {
            if (!isBase64(arrayOctet[i]) && !isWhiteSpace(arrayOctet[i])) {
                return false;
            }
        }
        return true;
    }

    public static byte[] encodeBase64(byte[] binaryData) {
        return encodeBase64(binaryData, false);
    }

    public static String encodeBase64String(byte[] binaryData) {
        return StringUtils.newStringUsAscii(encodeBase64(binaryData, false));
    }

    public static byte[] encodeBase64URLSafe(byte[] binaryData) {
        return encodeBase64(binaryData, false, true);
    }

    public static String encodeBase64URLSafeString(byte[] binaryData) {
        return StringUtils.newStringUsAscii(encodeBase64(binaryData, false, true));
    }

    public static byte[] encodeBase64Chunked(byte[] binaryData) {
        return encodeBase64(binaryData, true);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
        return encodeBase64(binaryData, isChunked, false);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe) {
        return encodeBase64(binaryData, isChunked, urlSafe, Integer.MAX_VALUE);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe, int maxResultSize) {
        if (binaryData == null || binaryData.length == 0) {
            return binaryData;
        }
        Base64 b64 = isChunked ? new Base64(urlSafe) : new Base64(0, CHUNK_SEPARATOR, urlSafe);
        long len = b64.getEncodedLength(binaryData);
        if (len <= ((long) maxResultSize)) {
            return b64.encode(binaryData);
        }
        throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + len + ") than the specified maximum size of " + maxResultSize);
    }

    public static byte[] decodeBase64(String base64String) {
        return new Base64().decode(base64String);
    }

    public static byte[] decodeBase64(byte[] base64Data) {
        return new Base64().decode(base64Data);
    }

    public static BigInteger decodeInteger(byte[] pArray) {
        return new BigInteger(1, decodeBase64(pArray));
    }

    public static byte[] encodeInteger(BigInteger bigInt) {
        if (bigInt != null) {
            return encodeBase64(toIntegerBytes(bigInt), false);
        }
        throw new NullPointerException("encodeInteger called with null parameter");
    }

    static byte[] toIntegerBytes(BigInteger bigInt) {
        int bitlen = ((bigInt.bitLength() + 7) >> 3) << 3;
        byte[] bigBytes = bigInt.toByteArray();
        if (bigInt.bitLength() % 8 != 0 && (bigInt.bitLength() / 8) + 1 == bitlen / 8) {
            return bigBytes;
        }
        int startSrc = 0;
        int len = bigBytes.length;
        if (bigInt.bitLength() % 8 == 0) {
            startSrc = 1;
            len--;
        }
        byte[] resizedBytes = new byte[(bitlen / 8)];
        System.arraycopy(bigBytes, startSrc, resizedBytes, (bitlen / 8) - len, len);
        return resizedBytes;
    }

    /* access modifiers changed from: protected */
    public boolean isInAlphabet(byte octet) {
        return octet >= 0 && octet < this.decodeTable.length && this.decodeTable[octet] != -1;
    }
}
