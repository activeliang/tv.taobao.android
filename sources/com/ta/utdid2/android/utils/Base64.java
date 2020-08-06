package com.ta.utdid2.android.utils;

import android.annotation.SuppressLint;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.CharEncoding;

public class Base64 {
    static final /* synthetic */ boolean $assertionsDisabled = (!Base64.class.desiredAssertionStatus());
    public static final int CRLF = 4;
    public static final int DEFAULT = 0;
    public static final int NO_CLOSE = 16;
    public static final int NO_PADDING = 1;
    public static final int NO_WRAP = 2;
    public static final int URL_SAFE = 8;

    static abstract class Coder {
        public int op;
        public byte[] output;

        public abstract int maxOutputSize(int i);

        public abstract boolean process(byte[] bArr, int i, int i2, boolean z);

        Coder() {
        }
    }

    public static byte[] decode(String str, int flags) {
        return decode(str.getBytes(), flags);
    }

    public static byte[] decode(byte[] input, int flags) {
        return decode(input, 0, input.length, flags);
    }

    public static byte[] decode(byte[] input, int offset, int len, int flags) {
        Decoder decoder = new Decoder(flags, new byte[((len * 3) / 4)]);
        if (!decoder.process(input, offset, len, true)) {
            throw new IllegalArgumentException("bad base-64");
        } else if (decoder.op == decoder.output.length) {
            return decoder.output;
        } else {
            byte[] temp = new byte[decoder.op];
            System.arraycopy(decoder.output, 0, temp, 0, decoder.op);
            return temp;
        }
    }

    static class Decoder extends Coder {
        private static final int[] DECODE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int[] DECODE_WEBSAFE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int EQUALS = -2;
        private static final int SKIP = -1;
        private final int[] alphabet;
        private int state;
        private int value;

        public Decoder(int flags, byte[] output) {
            this.output = output;
            this.alphabet = (flags & 8) == 0 ? DECODE : DECODE_WEBSAFE;
            this.state = 0;
            this.value = 0;
        }

        public int maxOutputSize(int len) {
            return ((len * 3) / 4) + 10;
        }

        public boolean process(byte[] input, int offset, int len, boolean finish) {
            int op;
            int op2;
            if (this.state == 6) {
                return false;
            }
            int p = offset;
            int len2 = len + offset;
            int state2 = this.state;
            int value2 = this.value;
            int op3 = 0;
            byte[] output = this.output;
            int[] alphabet2 = this.alphabet;
            while (true) {
                if (p < len2) {
                    if (state2 == 0) {
                        while (p + 4 <= len2 && (value2 = (alphabet2[input[p] & OnReminderListener.RET_FULL] << 18) | (alphabet2[input[p + 1] & OnReminderListener.RET_FULL] << 12) | (alphabet2[input[p + 2] & OnReminderListener.RET_FULL] << 6) | alphabet2[input[p + 3] & OnReminderListener.RET_FULL]) >= 0) {
                            output[op3 + 2] = (byte) value2;
                            output[op3 + 1] = (byte) (value2 >> 8);
                            output[op3] = (byte) (value2 >> 16);
                            op3 += 3;
                            p += 4;
                        }
                        if (p >= len2) {
                            op = op3;
                        }
                    }
                    int p2 = p + 1;
                    int d = alphabet2[input[p] & OnReminderListener.RET_FULL];
                    switch (state2) {
                        case 0:
                            if (d < 0) {
                                if (d == -1) {
                                    break;
                                } else {
                                    this.state = 6;
                                    return false;
                                }
                            } else {
                                value2 = d;
                                state2++;
                                break;
                            }
                        case 1:
                            if (d < 0) {
                                if (d == -1) {
                                    break;
                                } else {
                                    this.state = 6;
                                    return false;
                                }
                            } else {
                                value2 = (value2 << 6) | d;
                                state2++;
                                break;
                            }
                        case 2:
                            if (d < 0) {
                                if (d != -2) {
                                    if (d == -1) {
                                        break;
                                    } else {
                                        this.state = 6;
                                        return false;
                                    }
                                } else {
                                    output[op3] = (byte) (value2 >> 4);
                                    state2 = 4;
                                    op3++;
                                    break;
                                }
                            } else {
                                value2 = (value2 << 6) | d;
                                state2++;
                                break;
                            }
                        case 3:
                            if (d < 0) {
                                if (d != -2) {
                                    if (d == -1) {
                                        break;
                                    } else {
                                        this.state = 6;
                                        return false;
                                    }
                                } else {
                                    output[op3 + 1] = (byte) (value2 >> 2);
                                    output[op3] = (byte) (value2 >> 10);
                                    op3 += 2;
                                    state2 = 5;
                                    break;
                                }
                            } else {
                                value2 = (value2 << 6) | d;
                                output[op3 + 2] = (byte) value2;
                                output[op3 + 1] = (byte) (value2 >> 8);
                                output[op3] = (byte) (value2 >> 16);
                                op3 += 3;
                                state2 = 0;
                                break;
                            }
                        case 4:
                            if (d != -2) {
                                if (d == -1) {
                                    break;
                                } else {
                                    this.state = 6;
                                    return false;
                                }
                            } else {
                                state2++;
                                break;
                            }
                        case 5:
                            if (d == -1) {
                                break;
                            } else {
                                this.state = 6;
                                return false;
                            }
                    }
                    p = p2;
                } else {
                    op = op3;
                }
            }
            if (!finish) {
                this.state = state2;
                this.value = value2;
                this.op = op;
                return true;
            }
            switch (state2) {
                case 0:
                    op2 = op;
                    break;
                case 1:
                    this.state = 6;
                    return false;
                case 2:
                    op2 = op + 1;
                    output[op] = (byte) (value2 >> 4);
                    break;
                case 3:
                    int op4 = op + 1;
                    output[op] = (byte) (value2 >> 10);
                    output[op4] = (byte) (value2 >> 2);
                    op2 = op4 + 1;
                    break;
                case 4:
                    this.state = 6;
                    return false;
                default:
                    op2 = op;
                    break;
            }
            this.state = state2;
            this.op = op2;
            return true;
        }
    }

    public static String encodeToString(byte[] input, int flags) {
        try {
            return new String(encode(input, flags), CharEncoding.US_ASCII);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static String encodeToString(byte[] input, int offset, int len, int flags) {
        try {
            return new String(encode(input, offset, len, flags), CharEncoding.US_ASCII);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static byte[] encode(byte[] input, int flags) {
        return encode(input, 0, input.length, flags);
    }

    @SuppressLint({"Assert"})
    public static byte[] encode(byte[] input, int offset, int len, int flags) {
        int i;
        Encoder encoder = new Encoder(flags, (byte[]) null);
        int output_len = (len / 3) * 4;
        if (!encoder.do_padding) {
            switch (len % 3) {
                case 1:
                    output_len += 2;
                    break;
                case 2:
                    output_len += 3;
                    break;
            }
        } else if (len % 3 > 0) {
            output_len += 4;
        }
        if (encoder.do_newline && len > 0) {
            int i2 = ((len - 1) / 57) + 1;
            if (encoder.do_cr) {
                i = 2;
            } else {
                i = 1;
            }
            output_len += i * i2;
        }
        encoder.output = new byte[output_len];
        encoder.process(input, offset, len, true);
        if ($assertionsDisabled || encoder.op == output_len) {
            return encoder.output;
        }
        throw new AssertionError();
    }

    static class Encoder extends Coder {
        static final /* synthetic */ boolean $assertionsDisabled;
        private static final byte[] ENCODE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
        private static final byte[] ENCODE_WEBSAFE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
        public static final int LINE_GROUPS = 19;
        private final byte[] alphabet;
        private int count;
        public final boolean do_cr;
        public final boolean do_newline;
        public final boolean do_padding;
        private final byte[] tail;
        int tailLen;

        static {
            boolean z;
            if (!Base64.class.desiredAssertionStatus()) {
                z = true;
            } else {
                z = false;
            }
            $assertionsDisabled = z;
        }

        public Encoder(int flags, byte[] output) {
            boolean z;
            boolean z2 = true;
            this.output = output;
            this.do_padding = (flags & 1) == 0;
            if ((flags & 2) == 0) {
                z = true;
            } else {
                z = false;
            }
            this.do_newline = z;
            this.do_cr = (flags & 4) == 0 ? false : z2;
            this.alphabet = (flags & 8) == 0 ? ENCODE : ENCODE_WEBSAFE;
            this.tail = new byte[2];
            this.tailLen = 0;
            this.count = this.do_newline ? 19 : -1;
        }

        public int maxOutputSize(int len) {
            return ((len * 8) / 5) + 10;
        }

        /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
            	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
            	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
            */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x0058  */
        /* JADX WARNING: Removed duplicated region for block: B:25:0x00fe  */
        /* JADX WARNING: Removed duplicated region for block: B:44:0x015d  */
        /* JADX WARNING: Removed duplicated region for block: B:76:0x01fe  */
        /* JADX WARNING: Removed duplicated region for block: B:82:0x020c  */
        /* JADX WARNING: Removed duplicated region for block: B:93:0x00fc A[SYNTHETIC] */
        public boolean process(byte[] r15, int r16, int r17, boolean r18) {
            /*
                r14 = this;
                byte[] r1 = r14.alphabet
                byte[] r5 = r14.output
                r3 = 0
                int r2 = r14.count
                r6 = r16
                int r17 = r17 + r16
                r10 = -1
                int r11 = r14.tailLen
                switch(r11) {
                    case 0: goto L_0x0011;
                    case 1: goto L_0x00b0;
                    case 2: goto L_0x00d5;
                    default: goto L_0x0011;
                }
            L_0x0011:
                r11 = -1
                if (r10 == r11) goto L_0x0247
                int r4 = r3 + 1
                int r11 = r10 >> 18
                r11 = r11 & 63
                byte r11 = r1[r11]
                r5[r3] = r11
                int r3 = r4 + 1
                int r11 = r10 >> 12
                r11 = r11 & 63
                byte r11 = r1[r11]
                r5[r4] = r11
                int r4 = r3 + 1
                int r11 = r10 >> 6
                r11 = r11 & 63
                byte r11 = r1[r11]
                r5[r3] = r11
                int r3 = r4 + 1
                r11 = r10 & 63
                byte r11 = r1[r11]
                r5[r4] = r11
                int r2 = r2 + -1
                if (r2 != 0) goto L_0x0247
                boolean r11 = r14.do_cr
                if (r11 == 0) goto L_0x0049
                int r4 = r3 + 1
                r11 = 13
                r5[r3] = r11
                r3 = r4
            L_0x0049:
                int r4 = r3 + 1
                r11 = 10
                r5[r3] = r11
                r2 = 19
                r7 = r6
            L_0x0052:
                int r11 = r7 + 3
                r0 = r17
                if (r11 > r0) goto L_0x00fc
                byte r11 = r15[r7]
                r11 = r11 & 255(0xff, float:3.57E-43)
                int r11 = r11 << 16
                int r12 = r7 + 1
                byte r12 = r15[r12]
                r12 = r12 & 255(0xff, float:3.57E-43)
                int r12 = r12 << 8
                r11 = r11 | r12
                int r12 = r7 + 2
                byte r12 = r15[r12]
                r12 = r12 & 255(0xff, float:3.57E-43)
                r10 = r11 | r12
                int r11 = r10 >> 18
                r11 = r11 & 63
                byte r11 = r1[r11]
                r5[r4] = r11
                int r11 = r4 + 1
                int r12 = r10 >> 12
                r12 = r12 & 63
                byte r12 = r1[r12]
                r5[r11] = r12
                int r11 = r4 + 2
                int r12 = r10 >> 6
                r12 = r12 & 63
                byte r12 = r1[r12]
                r5[r11] = r12
                int r11 = r4 + 3
                r12 = r10 & 63
                byte r12 = r1[r12]
                r5[r11] = r12
                int r6 = r7 + 3
                int r3 = r4 + 4
                int r2 = r2 + -1
                if (r2 != 0) goto L_0x0247
                boolean r11 = r14.do_cr
                if (r11 == 0) goto L_0x00a6
                int r4 = r3 + 1
                r11 = 13
                r5[r3] = r11
                r3 = r4
            L_0x00a6:
                int r4 = r3 + 1
                r11 = 10
                r5[r3] = r11
                r2 = 19
                r7 = r6
                goto L_0x0052
            L_0x00b0:
                int r11 = r6 + 2
                r0 = r17
                if (r11 > r0) goto L_0x0011
                byte[] r11 = r14.tail
                r12 = 0
                byte r11 = r11[r12]
                r11 = r11 & 255(0xff, float:3.57E-43)
                int r11 = r11 << 16
                int r7 = r6 + 1
                byte r12 = r15[r6]
                r12 = r12 & 255(0xff, float:3.57E-43)
                int r12 = r12 << 8
                r11 = r11 | r12
                int r6 = r7 + 1
                byte r12 = r15[r7]
                r12 = r12 & 255(0xff, float:3.57E-43)
                r10 = r11 | r12
                r11 = 0
                r14.tailLen = r11
                goto L_0x0011
            L_0x00d5:
                int r11 = r6 + 1
                r0 = r17
                if (r11 > r0) goto L_0x0011
                byte[] r11 = r14.tail
                r12 = 0
                byte r11 = r11[r12]
                r11 = r11 & 255(0xff, float:3.57E-43)
                int r11 = r11 << 16
                byte[] r12 = r14.tail
                r13 = 1
                byte r12 = r12[r13]
                r12 = r12 & 255(0xff, float:3.57E-43)
                int r12 = r12 << 8
                r11 = r11 | r12
                int r7 = r6 + 1
                byte r12 = r15[r6]
                r12 = r12 & 255(0xff, float:3.57E-43)
                r10 = r11 | r12
                r11 = 0
                r14.tailLen = r11
                r6 = r7
                goto L_0x0011
            L_0x00fc:
                if (r18 == 0) goto L_0x020c
                int r11 = r14.tailLen
                int r11 = r7 - r11
                int r12 = r17 + -1
                if (r11 != r12) goto L_0x0168
                r8 = 0
                int r11 = r14.tailLen
                if (r11 <= 0) goto L_0x0163
                byte[] r11 = r14.tail
                int r9 = r8 + 1
                byte r11 = r11[r8]
                r8 = r9
                r6 = r7
            L_0x0113:
                r11 = r11 & 255(0xff, float:3.57E-43)
                int r10 = r11 << 4
                int r11 = r14.tailLen
                int r11 = r11 - r8
                r14.tailLen = r11
                int r3 = r4 + 1
                int r11 = r10 >> 6
                r11 = r11 & 63
                byte r11 = r1[r11]
                r5[r4] = r11
                int r4 = r3 + 1
                r11 = r10 & 63
                byte r11 = r1[r11]
                r5[r3] = r11
                boolean r11 = r14.do_padding
                if (r11 == 0) goto L_0x013e
                int r3 = r4 + 1
                r11 = 61
                r5[r4] = r11
                int r4 = r3 + 1
                r11 = 61
                r5[r3] = r11
            L_0x013e:
                r3 = r4
                boolean r11 = r14.do_newline
                if (r11 == 0) goto L_0x0155
                boolean r11 = r14.do_cr
                if (r11 == 0) goto L_0x014e
                int r4 = r3 + 1
                r11 = 13
                r5[r3] = r11
                r3 = r4
            L_0x014e:
                int r4 = r3 + 1
                r11 = 10
                r5[r3] = r11
            L_0x0154:
                r3 = r4
            L_0x0155:
                boolean r11 = $assertionsDisabled
                if (r11 != 0) goto L_0x01fe
                int r11 = r14.tailLen
                if (r11 == 0) goto L_0x01fe
                java.lang.AssertionError r11 = new java.lang.AssertionError
                r11.<init>()
                throw r11
            L_0x0163:
                int r6 = r7 + 1
                byte r11 = r15[r7]
                goto L_0x0113
            L_0x0168:
                int r11 = r14.tailLen
                int r11 = r7 - r11
                int r12 = r17 + -2
                if (r11 != r12) goto L_0x01e0
                r8 = 0
                int r11 = r14.tailLen
                r12 = 1
                if (r11 <= r12) goto L_0x01d5
                byte[] r11 = r14.tail
                int r9 = r8 + 1
                byte r11 = r11[r8]
                r8 = r9
                r6 = r7
            L_0x017e:
                r11 = r11 & 255(0xff, float:3.57E-43)
                int r12 = r11 << 10
                int r11 = r14.tailLen
                if (r11 <= 0) goto L_0x01da
                byte[] r11 = r14.tail
                int r9 = r8 + 1
                byte r11 = r11[r8]
                r8 = r9
            L_0x018d:
                r11 = r11 & 255(0xff, float:3.57E-43)
                int r11 = r11 << 2
                r10 = r12 | r11
                int r11 = r14.tailLen
                int r11 = r11 - r8
                r14.tailLen = r11
                int r3 = r4 + 1
                int r11 = r10 >> 12
                r11 = r11 & 63
                byte r11 = r1[r11]
                r5[r4] = r11
                int r4 = r3 + 1
                int r11 = r10 >> 6
                r11 = r11 & 63
                byte r11 = r1[r11]
                r5[r3] = r11
                int r3 = r4 + 1
                r11 = r10 & 63
                byte r11 = r1[r11]
                r5[r4] = r11
                boolean r11 = r14.do_padding
                if (r11 == 0) goto L_0x01bf
                int r4 = r3 + 1
                r11 = 61
                r5[r3] = r11
                r3 = r4
            L_0x01bf:
                boolean r11 = r14.do_newline
                if (r11 == 0) goto L_0x0155
                boolean r11 = r14.do_cr
                if (r11 == 0) goto L_0x01ce
                int r4 = r3 + 1
                r11 = 13
                r5[r3] = r11
                r3 = r4
            L_0x01ce:
                int r4 = r3 + 1
                r11 = 10
                r5[r3] = r11
                goto L_0x0154
            L_0x01d5:
                int r6 = r7 + 1
                byte r11 = r15[r7]
                goto L_0x017e
            L_0x01da:
                int r7 = r6 + 1
                byte r11 = r15[r6]
                r6 = r7
                goto L_0x018d
            L_0x01e0:
                boolean r11 = r14.do_newline
                if (r11 == 0) goto L_0x01fa
                if (r4 <= 0) goto L_0x01fa
                r11 = 19
                if (r2 == r11) goto L_0x01fa
                boolean r11 = r14.do_cr
                if (r11 == 0) goto L_0x0245
                int r3 = r4 + 1
                r11 = 13
                r5[r4] = r11
            L_0x01f4:
                int r4 = r3 + 1
                r11 = 10
                r5[r3] = r11
            L_0x01fa:
                r6 = r7
                r3 = r4
                goto L_0x0155
            L_0x01fe:
                boolean r11 = $assertionsDisabled
                if (r11 != 0) goto L_0x021e
                r0 = r17
                if (r6 == r0) goto L_0x021e
                java.lang.AssertionError r11 = new java.lang.AssertionError
                r11.<init>()
                throw r11
            L_0x020c:
                int r11 = r17 + -1
                if (r7 != r11) goto L_0x0224
                byte[] r11 = r14.tail
                int r12 = r14.tailLen
                int r13 = r12 + 1
                r14.tailLen = r13
                byte r13 = r15[r7]
                r11[r12] = r13
                r6 = r7
                r3 = r4
            L_0x021e:
                r14.op = r3
                r14.count = r2
                r11 = 1
                return r11
            L_0x0224:
                int r11 = r17 + -2
                if (r7 != r11) goto L_0x0242
                byte[] r11 = r14.tail
                int r12 = r14.tailLen
                int r13 = r12 + 1
                r14.tailLen = r13
                byte r13 = r15[r7]
                r11[r12] = r13
                byte[] r11 = r14.tail
                int r12 = r14.tailLen
                int r13 = r12 + 1
                r14.tailLen = r13
                int r13 = r7 + 1
                byte r13 = r15[r13]
                r11[r12] = r13
            L_0x0242:
                r6 = r7
                r3 = r4
                goto L_0x021e
            L_0x0245:
                r3 = r4
                goto L_0x01f4
            L_0x0247:
                r7 = r6
                r4 = r3
                goto L_0x0052
            */
            throw new UnsupportedOperationException("Method not decompiled: com.ta.utdid2.android.utils.Base64.Encoder.process(byte[], int, int, boolean):boolean");
        }
    }

    private Base64() {
    }
}
