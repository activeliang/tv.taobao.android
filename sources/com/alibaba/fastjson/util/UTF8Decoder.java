package com.alibaba.fastjson.util;

import com.taobao.atlas.dexmerge.dx.io.Opcodes;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

public class UTF8Decoder extends CharsetDecoder {
    private static final Charset charset = Charset.forName("UTF-8");

    public UTF8Decoder() {
        super(charset, 1.0f, 1.0f);
    }

    private static boolean isNotContinuation(int b) {
        return (b & 192) != 128;
    }

    private static boolean isMalformed2(int b1, int b2) {
        return (b1 & 30) == 0 || (b2 & 192) != 128;
    }

    private static boolean isMalformed3(int b1, int b2, int b3) {
        return ((b1 != -32 || (b2 & Opcodes.SHL_INT_LIT8) != 128) && (b2 & 192) == 128 && (b3 & 192) == 128) ? false : true;
    }

    private static boolean isMalformed4(int b2, int b3, int b4) {
        return ((b2 & 192) == 128 && (b3 & 192) == 128 && (b4 & 192) == 128) ? false : true;
    }

    private static CoderResult lookupN(ByteBuffer src, int n) {
        for (int i = 1; i < n; i++) {
            if (isNotContinuation(src.get())) {
                return CoderResult.malformedForLength(i);
            }
        }
        return CoderResult.malformedForLength(n);
    }

    public static CoderResult malformedN(ByteBuffer src, int nb) {
        int i = 2;
        switch (nb) {
            case 1:
                int b1 = src.get();
                if ((b1 >> 2) == -2) {
                    if (src.remaining() < 4) {
                        return CoderResult.UNDERFLOW;
                    }
                    return lookupN(src, 5);
                } else if ((b1 >> 1) != -2) {
                    return CoderResult.malformedForLength(1);
                } else {
                    if (src.remaining() < 5) {
                        return CoderResult.UNDERFLOW;
                    }
                    return lookupN(src, 6);
                }
            case 2:
                return CoderResult.malformedForLength(1);
            case 3:
                int b12 = src.get();
                int b2 = src.get();
                if ((b12 == -32 && (b2 & Opcodes.SHL_INT_LIT8) == 128) || isNotContinuation(b2)) {
                    i = 1;
                }
                return CoderResult.malformedForLength(i);
            case 4:
                int b13 = src.get() & 255;
                int b22 = src.get() & 255;
                if (b13 > 244 || ((b13 == 240 && (b22 < 144 || b22 > 191)) || ((b13 == 244 && (b22 & 240) != 128) || isNotContinuation(b22)))) {
                    return CoderResult.malformedForLength(1);
                }
                if (isNotContinuation(src.get())) {
                    return CoderResult.malformedForLength(2);
                }
                return CoderResult.malformedForLength(3);
            default:
                throw new IllegalStateException();
        }
    }

    private static CoderResult malformed(ByteBuffer src, int sp, CharBuffer dst, int dp, int nb) {
        src.position(sp - src.arrayOffset());
        CoderResult cr = malformedN(src, nb);
        updatePositions(src, sp, dst, dp);
        return cr;
    }

    private static CoderResult xflow(Buffer src, int sp, int sl, Buffer dst, int dp, int nb) {
        updatePositions(src, sp, dst, dp);
        return (nb == 0 || sl - sp < nb) ? CoderResult.UNDERFLOW : CoderResult.OVERFLOW;
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 153 */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x006d, code lost:
        if ((r8 >> 5) != -2) goto L_0x00a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0072, code lost:
        if ((r4 - r3) < 2) goto L_0x0076;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0074, code lost:
        if (r6 >= r13) goto L_0x0076;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0076, code lost:
        r2 = xflow(r20, r3, r4, r21, r6, 2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0080, code lost:
        r9 = r16[r3 + 1];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0088, code lost:
        if (isMalformed2(r8, r9) == false) goto L_0x0094;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x008a, code lost:
        r2 = malformed(r20, r3, r21, r6, 2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0094, code lost:
        r12[r6] = (char) (((r8 << 6) ^ r9) ^ 3968);
        r3 = r3 + 2;
        r6 = r6 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x00a5, code lost:
        if ((r8 >> 4) != -2) goto L_0x00e2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00aa, code lost:
        if ((r4 - r3) < 3) goto L_0x00ae;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00ac, code lost:
        if (r6 >= r13) goto L_0x00ae;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00ae, code lost:
        r2 = xflow(r20, r3, r4, r21, r6, 3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00b8, code lost:
        r9 = r16[r3 + 1];
        r10 = r16[r3 + 2];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00c4, code lost:
        if (isMalformed3(r8, r9, r10) == false) goto L_0x00d0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00c6, code lost:
        r2 = malformed(r20, r3, r21, r6, 3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00d0, code lost:
        r12[r6] = (char) ((((r8 << 12) ^ (r9 << 6)) ^ r10) ^ 8064);
        r3 = r3 + 3;
        r6 = r6 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00e5, code lost:
        if ((r8 >> 3) != -2) goto L_0x0145;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00ea, code lost:
        if ((r4 - r3) < 4) goto L_0x00f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00ef, code lost:
        if ((r13 - r6) >= 2) goto L_0x00fc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00f1, code lost:
        r2 = xflow(r20, r3, r4, r21, r6, 4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00fc, code lost:
        r9 = r16[r3 + 1];
        r10 = r16[r3 + 2];
        r11 = r16[r3 + 3];
        r18 = ((((r8 & 7) << 18) | ((r9 & 63) << 12)) | ((r10 & 63) << 6)) | (r11 & 63);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x011e, code lost:
        if (isMalformed4(r9, r10, r11) != false) goto L_0x0126;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0124, code lost:
        if (com.alibaba.fastjson.util.UTF8Decoder.Surrogate.neededFor(r18) != false) goto L_0x0131;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0126, code lost:
        r2 = malformed(r20, r3, r21, r6, 4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0131, code lost:
        r15 = r6 + 1;
        r12[r6] = com.alibaba.fastjson.util.UTF8Decoder.Surrogate.high(r18);
        r6 = r15 + 1;
        r12[r15] = com.alibaba.fastjson.util.UTF8Decoder.Surrogate.low(r18);
        r3 = r3 + 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0145, code lost:
        r2 = malformed(r20, r3, r21, r6, 1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.nio.charset.CoderResult decodeArrayLoop(java.nio.ByteBuffer r20, java.nio.CharBuffer r21) {
        /*
            r19 = this;
            byte[] r16 = r20.array()
            int r2 = r20.arrayOffset()
            int r5 = r20.position()
            int r3 = r2 + r5
            int r2 = r20.arrayOffset()
            int r5 = r20.limit()
            int r4 = r2 + r5
            char[] r12 = r21.array()
            int r2 = r21.arrayOffset()
            int r5 = r21.position()
            int r6 = r2 + r5
            int r2 = r21.arrayOffset()
            int r5 = r21.limit()
            int r13 = r2 + r5
            int r2 = r4 - r3
            int r5 = r13 - r6
            int r2 = java.lang.Math.min(r2, r5)
            int r14 = r6 + r2
            r15 = r6
            r17 = r3
        L_0x003d:
            if (r15 >= r14) goto L_0x015b
            byte r2 = r16[r17]
            if (r2 < 0) goto L_0x015b
            int r6 = r15 + 1
            int r3 = r17 + 1
            byte r2 = r16[r17]
            char r2 = (char) r2
            r12[r15] = r2
            r15 = r6
            r17 = r3
            goto L_0x003d
        L_0x0050:
            int r15 = r6 + 1
            char r2 = (char) r8
            r12[r6] = r2
            int r3 = r3 + 1
            r6 = r15
        L_0x0058:
            if (r3 >= r4) goto L_0x0150
            byte r8 = r16[r3]
            if (r8 < 0) goto L_0x006a
            if (r6 < r13) goto L_0x0050
            r7 = 1
            r2 = r20
            r5 = r21
            java.nio.charset.CoderResult r2 = xflow(r2, r3, r4, r5, r6, r7)
        L_0x0069:
            return r2
        L_0x006a:
            int r2 = r8 >> 5
            r5 = -2
            if (r2 != r5) goto L_0x00a2
            int r2 = r4 - r3
            r5 = 2
            if (r2 < r5) goto L_0x0076
            if (r6 < r13) goto L_0x0080
        L_0x0076:
            r7 = 2
            r2 = r20
            r5 = r21
            java.nio.charset.CoderResult r2 = xflow(r2, r3, r4, r5, r6, r7)
            goto L_0x0069
        L_0x0080:
            int r2 = r3 + 1
            byte r9 = r16[r2]
            boolean r2 = isMalformed2(r8, r9)
            if (r2 == 0) goto L_0x0094
            r2 = 2
            r0 = r20
            r1 = r21
            java.nio.charset.CoderResult r2 = malformed(r0, r3, r1, r6, r2)
            goto L_0x0069
        L_0x0094:
            int r15 = r6 + 1
            int r2 = r8 << 6
            r2 = r2 ^ r9
            r2 = r2 ^ 3968(0xf80, float:5.56E-42)
            char r2 = (char) r2
            r12[r6] = r2
            int r3 = r3 + 2
            r6 = r15
            goto L_0x0058
        L_0x00a2:
            int r2 = r8 >> 4
            r5 = -2
            if (r2 != r5) goto L_0x00e2
            int r2 = r4 - r3
            r5 = 3
            if (r2 < r5) goto L_0x00ae
            if (r6 < r13) goto L_0x00b8
        L_0x00ae:
            r7 = 3
            r2 = r20
            r5 = r21
            java.nio.charset.CoderResult r2 = xflow(r2, r3, r4, r5, r6, r7)
            goto L_0x0069
        L_0x00b8:
            int r2 = r3 + 1
            byte r9 = r16[r2]
            int r2 = r3 + 2
            byte r10 = r16[r2]
            boolean r2 = isMalformed3(r8, r9, r10)
            if (r2 == 0) goto L_0x00d0
            r2 = 3
            r0 = r20
            r1 = r21
            java.nio.charset.CoderResult r2 = malformed(r0, r3, r1, r6, r2)
            goto L_0x0069
        L_0x00d0:
            int r15 = r6 + 1
            int r2 = r8 << 12
            int r5 = r9 << 6
            r2 = r2 ^ r5
            r2 = r2 ^ r10
            r2 = r2 ^ 8064(0x1f80, float:1.13E-41)
            char r2 = (char) r2
            r12[r6] = r2
            int r3 = r3 + 3
            r6 = r15
            goto L_0x0058
        L_0x00e2:
            int r2 = r8 >> 3
            r5 = -2
            if (r2 != r5) goto L_0x0145
            int r2 = r4 - r3
            r5 = 4
            if (r2 < r5) goto L_0x00f1
            int r2 = r13 - r6
            r5 = 2
            if (r2 >= r5) goto L_0x00fc
        L_0x00f1:
            r7 = 4
            r2 = r20
            r5 = r21
            java.nio.charset.CoderResult r2 = xflow(r2, r3, r4, r5, r6, r7)
            goto L_0x0069
        L_0x00fc:
            int r2 = r3 + 1
            byte r9 = r16[r2]
            int r2 = r3 + 2
            byte r10 = r16[r2]
            int r2 = r3 + 3
            byte r11 = r16[r2]
            r2 = r8 & 7
            int r2 = r2 << 18
            r5 = r9 & 63
            int r5 = r5 << 12
            r2 = r2 | r5
            r5 = r10 & 63
            int r5 = r5 << 6
            r2 = r2 | r5
            r5 = r11 & 63
            r18 = r2 | r5
            boolean r2 = isMalformed4(r9, r10, r11)
            if (r2 != 0) goto L_0x0126
            boolean r2 = com.alibaba.fastjson.util.UTF8Decoder.Surrogate.neededFor(r18)
            if (r2 != 0) goto L_0x0131
        L_0x0126:
            r2 = 4
            r0 = r20
            r1 = r21
            java.nio.charset.CoderResult r2 = malformed(r0, r3, r1, r6, r2)
            goto L_0x0069
        L_0x0131:
            int r15 = r6 + 1
            char r2 = com.alibaba.fastjson.util.UTF8Decoder.Surrogate.high(r18)
            r12[r6] = r2
            int r6 = r15 + 1
            char r2 = com.alibaba.fastjson.util.UTF8Decoder.Surrogate.low(r18)
            r12[r15] = r2
            int r3 = r3 + 4
            goto L_0x0058
        L_0x0145:
            r2 = 1
            r0 = r20
            r1 = r21
            java.nio.charset.CoderResult r2 = malformed(r0, r3, r1, r6, r2)
            goto L_0x0069
        L_0x0150:
            r7 = 0
            r2 = r20
            r5 = r21
            java.nio.charset.CoderResult r2 = xflow(r2, r3, r4, r5, r6, r7)
            goto L_0x0069
        L_0x015b:
            r6 = r15
            r3 = r17
            goto L_0x0058
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.UTF8Decoder.decodeArrayLoop(java.nio.ByteBuffer, java.nio.CharBuffer):java.nio.charset.CoderResult");
    }

    /* access modifiers changed from: protected */
    public CoderResult decodeLoop(ByteBuffer src, CharBuffer dst) {
        return decodeArrayLoop(src, dst);
    }

    static void updatePositions(Buffer src, int sp, Buffer dst, int dp) {
        src.position(sp);
        dst.position(dp);
    }

    private static class Surrogate {
        static final /* synthetic */ boolean $assertionsDisabled = (!UTF8Decoder.class.desiredAssertionStatus());
        public static final int UCS4_MAX = 1114111;
        public static final int UCS4_MIN = 65536;

        private Surrogate() {
        }

        public static boolean neededFor(int uc) {
            return uc >= 65536 && uc <= 1114111;
        }

        public static char high(int uc) {
            if ($assertionsDisabled || neededFor(uc)) {
                return (char) (55296 | (((uc - 65536) >> 10) & 1023));
            }
            throw new AssertionError();
        }

        public static char low(int uc) {
            if ($assertionsDisabled || neededFor(uc)) {
                return (char) (56320 | ((uc - 65536) & 1023));
            }
            throw new AssertionError();
        }
    }
}
