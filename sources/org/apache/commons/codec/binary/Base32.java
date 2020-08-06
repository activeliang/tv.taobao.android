package org.apache.commons.codec.binary;

import anetwork.channel.NetworkListenerState;
import org.apache.commons.codec.binary.BaseNCodec;

public class Base32 extends BaseNCodec {
    private static final int BITS_PER_ENCODED_BYTE = 5;
    private static final int BYTES_PER_ENCODED_BLOCK = 8;
    private static final int BYTES_PER_UNENCODED_BLOCK = 5;
    private static final byte[] CHUNK_SEPARATOR = {13, 10};
    private static final byte[] DECODE_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, NetworkListenerState.ALL, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
    private static final byte[] ENCODE_TABLE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 50, 51, 52, 53, 54, 55};
    private static final byte[] HEX_DECODE_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, NetworkListenerState.ALL, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, NetworkListenerState.ALL};
    private static final byte[] HEX_ENCODE_TABLE = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86};
    private static final int MASK_5BITS = 31;
    private final int decodeSize;
    private final byte[] decodeTable;
    private final int encodeSize;
    private final byte[] encodeTable;
    private final byte[] lineSeparator;

    public Base32() {
        this(false);
    }

    public Base32(byte pad) {
        this(false, pad);
    }

    public Base32(boolean useHex) {
        this(0, (byte[]) null, useHex, (byte) 61);
    }

    public Base32(boolean useHex, byte pad) {
        this(0, (byte[]) null, useHex, pad);
    }

    public Base32(int lineLength) {
        this(lineLength, CHUNK_SEPARATOR);
    }

    public Base32(int lineLength, byte[] lineSeparator2) {
        this(lineLength, lineSeparator2, false, (byte) 61);
    }

    public Base32(int lineLength, byte[] lineSeparator2, boolean useHex) {
        this(lineLength, lineSeparator2, useHex, (byte) 61);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public Base32(int lineLength, byte[] lineSeparator2, boolean useHex, byte pad) {
        super(5, 8, lineLength, lineSeparator2 == null ? 0 : lineSeparator2.length, pad);
        if (useHex) {
            this.encodeTable = HEX_ENCODE_TABLE;
            this.decodeTable = HEX_DECODE_TABLE;
        } else {
            this.encodeTable = ENCODE_TABLE;
            this.decodeTable = DECODE_TABLE;
        }
        if (lineLength <= 0) {
            this.encodeSize = 8;
            this.lineSeparator = null;
        } else if (lineSeparator2 == null) {
            throw new IllegalArgumentException("lineLength " + lineLength + " > 0, but lineSeparator is null");
        } else if (containsAlphabetOrPad(lineSeparator2)) {
            throw new IllegalArgumentException("lineSeparator must not contain Base32 characters: [" + StringUtils.newStringUtf8(lineSeparator2) + "]");
        } else {
            this.encodeSize = lineSeparator2.length + 8;
            this.lineSeparator = new byte[lineSeparator2.length];
            System.arraycopy(lineSeparator2, 0, this.lineSeparator, 0, lineSeparator2.length);
        }
        this.decodeSize = this.encodeSize - 1;
        if (isInAlphabet(pad) || isWhiteSpace(pad)) {
            throw new IllegalArgumentException("pad must not be in alphabet or whitespace");
        }
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
                int inPos3 = inPos2 + 1;
                byte b = in[inPos2];
                if (b == this.pad) {
                    context.eof = true;
                    break;
                }
                byte[] buffer = ensureBufferSize(this.decodeSize, context);
                if (b >= 0 && b < this.decodeTable.length && (result = this.decodeTable[b]) >= 0) {
                    context.modulus = (context.modulus + 1) % 8;
                    context.lbitWorkArea = (context.lbitWorkArea << 5) + ((long) result);
                    if (context.modulus == 0) {
                        int i2 = context.pos;
                        context.pos = i2 + 1;
                        buffer[i2] = (byte) ((int) ((context.lbitWorkArea >> 32) & 255));
                        int i3 = context.pos;
                        context.pos = i3 + 1;
                        buffer[i3] = (byte) ((int) ((context.lbitWorkArea >> 24) & 255));
                        int i4 = context.pos;
                        context.pos = i4 + 1;
                        buffer[i4] = (byte) ((int) ((context.lbitWorkArea >> 16) & 255));
                        int i5 = context.pos;
                        context.pos = i5 + 1;
                        buffer[i5] = (byte) ((int) ((context.lbitWorkArea >> 8) & 255));
                        int i6 = context.pos;
                        context.pos = i6 + 1;
                        buffer[i6] = (byte) ((int) (context.lbitWorkArea & 255));
                    }
                }
                i++;
                inPos2 = inPos3;
            }
            if (context.eof && context.modulus >= 2) {
                byte[] buffer2 = ensureBufferSize(this.decodeSize, context);
                switch (context.modulus) {
                    case 2:
                        int i7 = context.pos;
                        context.pos = i7 + 1;
                        buffer2[i7] = (byte) ((int) ((context.lbitWorkArea >> 2) & 255));
                        return;
                    case 3:
                        int i8 = context.pos;
                        context.pos = i8 + 1;
                        buffer2[i8] = (byte) ((int) ((context.lbitWorkArea >> 7) & 255));
                        return;
                    case 4:
                        context.lbitWorkArea >>= 4;
                        int i9 = context.pos;
                        context.pos = i9 + 1;
                        buffer2[i9] = (byte) ((int) ((context.lbitWorkArea >> 8) & 255));
                        int i10 = context.pos;
                        context.pos = i10 + 1;
                        buffer2[i10] = (byte) ((int) (context.lbitWorkArea & 255));
                        return;
                    case 5:
                        context.lbitWorkArea >>= 1;
                        int i11 = context.pos;
                        context.pos = i11 + 1;
                        buffer2[i11] = (byte) ((int) ((context.lbitWorkArea >> 16) & 255));
                        int i12 = context.pos;
                        context.pos = i12 + 1;
                        buffer2[i12] = (byte) ((int) ((context.lbitWorkArea >> 8) & 255));
                        int i13 = context.pos;
                        context.pos = i13 + 1;
                        buffer2[i13] = (byte) ((int) (context.lbitWorkArea & 255));
                        return;
                    case 6:
                        context.lbitWorkArea >>= 6;
                        int i14 = context.pos;
                        context.pos = i14 + 1;
                        buffer2[i14] = (byte) ((int) ((context.lbitWorkArea >> 16) & 255));
                        int i15 = context.pos;
                        context.pos = i15 + 1;
                        buffer2[i15] = (byte) ((int) ((context.lbitWorkArea >> 8) & 255));
                        int i16 = context.pos;
                        context.pos = i16 + 1;
                        buffer2[i16] = (byte) ((int) (context.lbitWorkArea & 255));
                        return;
                    case 7:
                        context.lbitWorkArea >>= 3;
                        int i17 = context.pos;
                        context.pos = i17 + 1;
                        buffer2[i17] = (byte) ((int) ((context.lbitWorkArea >> 24) & 255));
                        int i18 = context.pos;
                        context.pos = i18 + 1;
                        buffer2[i18] = (byte) ((int) ((context.lbitWorkArea >> 16) & 255));
                        int i19 = context.pos;
                        context.pos = i19 + 1;
                        buffer2[i19] = (byte) ((int) ((context.lbitWorkArea >> 8) & 255));
                        int i20 = context.pos;
                        context.pos = i20 + 1;
                        buffer2[i20] = (byte) ((int) (context.lbitWorkArea & 255));
                        return;
                    default:
                        throw new IllegalStateException("Impossible modulus " + context.modulus);
                }
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: byte} */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void encode(byte[] r11, int r12, int r13, org.apache.commons.codec.binary.BaseNCodec.Context r14) {
        /*
            r10 = this;
            boolean r5 = r14.eof
            if (r5 == 0) goto L_0x0005
        L_0x0004:
            return
        L_0x0005:
            if (r13 >= 0) goto L_0x0250
            r5 = 1
            r14.eof = r5
            int r5 = r14.modulus
            if (r5 != 0) goto L_0x0012
            int r5 = r10.lineLength
            if (r5 == 0) goto L_0x0004
        L_0x0012:
            int r5 = r10.encodeSize
            byte[] r1 = r10.ensureBufferSize(r5, r14)
            int r4 = r14.pos
            int r5 = r14.modulus
            switch(r5) {
                case 0: goto L_0x009d;
                case 1: goto L_0x003b;
                case 2: goto L_0x00c2;
                case 3: goto L_0x0139;
                case 4: goto L_0x01bb;
                default: goto L_0x001f;
            }
        L_0x001f:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Impossible modulus "
            java.lang.StringBuilder r6 = r6.append(r7)
            int r7 = r14.modulus
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r5.<init>(r6)
            throw r5
        L_0x003b:
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 3
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 2
            long r8 = r8 << r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte r6 = r10.pad
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte r6 = r10.pad
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte r6 = r10.pad
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte r6 = r10.pad
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte r6 = r10.pad
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte r6 = r10.pad
            r1[r5] = r6
        L_0x009d:
            int r5 = r14.currentLinePos
            int r6 = r14.pos
            int r6 = r6 - r4
            int r5 = r5 + r6
            r14.currentLinePos = r5
            int r5 = r10.lineLength
            if (r5 <= 0) goto L_0x0004
            int r5 = r14.currentLinePos
            if (r5 <= 0) goto L_0x0004
            byte[] r5 = r10.lineSeparator
            r6 = 0
            int r7 = r14.pos
            byte[] r8 = r10.lineSeparator
            int r8 = r8.length
            java.lang.System.arraycopy(r5, r6, r1, r7, r8)
            int r5 = r14.pos
            byte[] r6 = r10.lineSeparator
            int r6 = r6.length
            int r5 = r5 + r6
            r14.pos = r5
            goto L_0x0004
        L_0x00c2:
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 11
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 6
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 1
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 4
            long r8 = r8 << r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte r6 = r10.pad
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte r6 = r10.pad
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte r6 = r10.pad
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte r6 = r10.pad
            r1[r5] = r6
            goto L_0x009d
        L_0x0139:
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 19
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 14
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 9
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 4
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 1
            long r8 = r8 << r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte r6 = r10.pad
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte r6 = r10.pad
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte r6 = r10.pad
            r1[r5] = r6
            goto L_0x009d
        L_0x01bb:
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 27
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 22
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 17
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 12
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 7
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 2
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 3
            long r8 = r8 << r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte r6 = r10.pad
            r1[r5] = r6
            goto L_0x009d
        L_0x0250:
            r2 = 0
            r3 = r12
        L_0x0252:
            if (r2 >= r13) goto L_0x033e
            int r5 = r10.encodeSize
            byte[] r1 = r10.ensureBufferSize(r5, r14)
            int r5 = r14.modulus
            int r5 = r5 + 1
            int r5 = r5 % 5
            r14.modulus = r5
            int r12 = r3 + 1
            byte r0 = r11[r3]
            if (r0 >= 0) goto L_0x026a
            int r0 = r0 + 256
        L_0x026a:
            long r6 = r14.lbitWorkArea
            r5 = 8
            long r6 = r6 << r5
            long r8 = (long) r0
            long r6 = r6 + r8
            r14.lbitWorkArea = r6
            int r5 = r14.modulus
            if (r5 != 0) goto L_0x0339
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 35
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 30
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 25
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 20
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 15
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 10
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            r7 = 5
            long r8 = r8 >> r7
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.pos
            int r6 = r5 + 1
            r14.pos = r6
            byte[] r6 = r10.encodeTable
            long r8 = r14.lbitWorkArea
            int r7 = (int) r8
            r7 = r7 & 31
            byte r6 = r6[r7]
            r1[r5] = r6
            int r5 = r14.currentLinePos
            int r5 = r5 + 8
            r14.currentLinePos = r5
            int r5 = r10.lineLength
            if (r5 <= 0) goto L_0x0339
            int r5 = r10.lineLength
            int r6 = r14.currentLinePos
            if (r5 > r6) goto L_0x0339
            byte[] r5 = r10.lineSeparator
            r6 = 0
            int r7 = r14.pos
            byte[] r8 = r10.lineSeparator
            int r8 = r8.length
            java.lang.System.arraycopy(r5, r6, r1, r7, r8)
            int r5 = r14.pos
            byte[] r6 = r10.lineSeparator
            int r6 = r6.length
            int r5 = r5 + r6
            r14.pos = r5
            r5 = 0
            r14.currentLinePos = r5
        L_0x0339:
            int r2 = r2 + 1
            r3 = r12
            goto L_0x0252
        L_0x033e:
            r12 = r3
            goto L_0x0004
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.binary.Base32.encode(byte[], int, int, org.apache.commons.codec.binary.BaseNCodec$Context):void");
    }

    public boolean isInAlphabet(byte octet) {
        return octet >= 0 && octet < this.decodeTable.length && this.decodeTable[octet] != -1;
    }
}
