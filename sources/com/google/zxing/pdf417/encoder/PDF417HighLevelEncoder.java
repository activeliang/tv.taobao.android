package com.google.zxing.pdf417.encoder;

import com.google.zxing.WriterException;
import com.google.zxing.common.CharacterSetECI;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

final class PDF417HighLevelEncoder {
    private static final int BYTE_COMPACTION = 1;
    private static final Charset DEFAULT_ENCODING = StandardCharsets.ISO_8859_1;
    private static final int ECI_CHARSET = 927;
    private static final int ECI_GENERAL_PURPOSE = 926;
    private static final int ECI_USER_DEFINED = 925;
    private static final int LATCH_TO_BYTE = 924;
    private static final int LATCH_TO_BYTE_PADDED = 901;
    private static final int LATCH_TO_NUMERIC = 902;
    private static final int LATCH_TO_TEXT = 900;
    private static final byte[] MIXED = new byte[128];
    private static final int NUMERIC_COMPACTION = 2;
    private static final byte[] PUNCTUATION = new byte[128];
    private static final int SHIFT_TO_BYTE = 913;
    private static final int SUBMODE_ALPHA = 0;
    private static final int SUBMODE_LOWER = 1;
    private static final int SUBMODE_MIXED = 2;
    private static final int SUBMODE_PUNCTUATION = 3;
    private static final int TEXT_COMPACTION = 0;
    private static final byte[] TEXT_MIXED_RAW = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 38, 13, 9, 44, 58, 35, 45, 46, 36, 47, 43, 37, 42, 61, 94, 0, 32, 0, 0, 0};
    private static final byte[] TEXT_PUNCTUATION_RAW = {59, 60, 62, 64, 91, 92, 93, 95, 96, 126, 33, 13, 9, 44, 58, 10, 45, 46, 36, 47, 34, 124, 42, 40, 41, 63, 123, 125, 39, 0};

    static {
        Arrays.fill(MIXED, (byte) -1);
        for (int i = 0; i < TEXT_MIXED_RAW.length; i++) {
            byte b = TEXT_MIXED_RAW[i];
            if (b > 0) {
                MIXED[b] = (byte) i;
            }
        }
        Arrays.fill(PUNCTUATION, (byte) -1);
        for (int i2 = 0; i2 < TEXT_PUNCTUATION_RAW.length; i2++) {
            byte b2 = TEXT_PUNCTUATION_RAW[i2];
            if (b2 > 0) {
                PUNCTUATION[b2] = (byte) i2;
            }
        }
    }

    private PDF417HighLevelEncoder() {
    }

    static String encodeHighLevel(String msg, Compaction compaction, Charset encoding) throws WriterException {
        CharacterSetECI eci;
        StringBuilder sb = new StringBuilder(msg.length());
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        } else if (!DEFAULT_ENCODING.equals(encoding) && (eci = CharacterSetECI.getCharacterSetECIByName(encoding.name())) != null) {
            encodingECI(eci.getValue(), sb);
        }
        int len = msg.length();
        int p = 0;
        int textSubMode = 0;
        switch (compaction) {
            case TEXT:
                encodeText(msg, 0, len, sb, 0);
                break;
            case BYTE:
                byte[] msgBytes = msg.getBytes(encoding);
                encodeBinary(msgBytes, 0, msgBytes.length, 1, sb);
                break;
            case NUMERIC:
                sb.append(902);
                encodeNumeric(msg, 0, len, sb);
                break;
            default:
                int encodingMode = 0;
                while (p < len) {
                    int n = determineConsecutiveDigitCount(msg, p);
                    if (n >= 13) {
                        sb.append(902);
                        encodingMode = 2;
                        textSubMode = 0;
                        encodeNumeric(msg, p, n, sb);
                        p += n;
                    } else {
                        int t = determineConsecutiveTextCount(msg, p);
                        if (t >= 5 || n == len) {
                            if (encodingMode != 0) {
                                sb.append(900);
                                encodingMode = 0;
                                textSubMode = 0;
                            }
                            textSubMode = encodeText(msg, p, t, sb, textSubMode);
                            p += t;
                        } else {
                            int b = determineConsecutiveBinaryCount(msg, p, encoding);
                            if (b == 0) {
                                b = 1;
                            }
                            byte[] bytes = msg.substring(p, p + b).getBytes(encoding);
                            if (bytes.length == 1 && encodingMode == 0) {
                                encodeBinary(bytes, 0, 1, 0, sb);
                            } else {
                                encodeBinary(bytes, 0, bytes.length, encodingMode, sb);
                                encodingMode = 1;
                                textSubMode = 0;
                            }
                            p += b;
                        }
                    }
                }
                break;
        }
        return sb.toString();
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int encodeText(java.lang.CharSequence r9, int r10, int r11, java.lang.StringBuilder r12, int r13) {
        /*
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>(r11)
            r5 = r13
            r3 = 0
        L_0x0007:
            int r7 = r10 + r3
            char r0 = r9.charAt(r7)
            switch(r5) {
                case 0: goto L_0x003f;
                case 1: goto L_0x007e;
                case 2: goto L_0x00c5;
                default: goto L_0x0010;
            }
        L_0x0010:
            boolean r7 = isPunctuation(r0)
            if (r7 == 0) goto L_0x011c
            byte[] r7 = PUNCTUATION
            byte r7 = r7[r0]
            char r7 = (char) r7
            r6.append(r7)
        L_0x001e:
            int r3 = r3 + 1
            if (r3 < r11) goto L_0x0007
            r1 = 0
            int r4 = r6.length()
            r2 = 0
        L_0x0028:
            if (r2 >= r4) goto L_0x012d
            int r7 = r2 % 2
            if (r7 == 0) goto L_0x0124
            r7 = 1
        L_0x002f:
            if (r7 == 0) goto L_0x0127
            int r7 = r1 * 30
            char r8 = r6.charAt(r2)
            int r7 = r7 + r8
            char r1 = (char) r7
            r12.append(r1)
        L_0x003c:
            int r2 = r2 + 1
            goto L_0x0028
        L_0x003f:
            boolean r7 = isAlphaUpper(r0)
            if (r7 == 0) goto L_0x0056
            r7 = 32
            if (r0 != r7) goto L_0x004f
            r7 = 26
            r6.append(r7)
            goto L_0x001e
        L_0x004f:
            int r7 = r0 + -65
            char r7 = (char) r7
            r6.append(r7)
            goto L_0x001e
        L_0x0056:
            boolean r7 = isAlphaLower(r0)
            if (r7 == 0) goto L_0x0063
            r5 = 1
            r7 = 27
            r6.append(r7)
            goto L_0x0007
        L_0x0063:
            boolean r7 = isMixed(r0)
            if (r7 == 0) goto L_0x0070
            r5 = 2
            r7 = 28
            r6.append(r7)
            goto L_0x0007
        L_0x0070:
            r7 = 29
            r6.append(r7)
            byte[] r7 = PUNCTUATION
            byte r7 = r7[r0]
            char r7 = (char) r7
            r6.append(r7)
            goto L_0x001e
        L_0x007e:
            boolean r7 = isAlphaLower(r0)
            if (r7 == 0) goto L_0x0095
            r7 = 32
            if (r0 != r7) goto L_0x008e
            r7 = 26
            r6.append(r7)
            goto L_0x001e
        L_0x008e:
            int r7 = r0 + -97
            char r7 = (char) r7
            r6.append(r7)
            goto L_0x001e
        L_0x0095:
            boolean r7 = isAlphaUpper(r0)
            if (r7 == 0) goto L_0x00a8
            r7 = 27
            r6.append(r7)
            int r7 = r0 + -65
            char r7 = (char) r7
            r6.append(r7)
            goto L_0x001e
        L_0x00a8:
            boolean r7 = isMixed(r0)
            if (r7 == 0) goto L_0x00b6
            r5 = 2
            r7 = 28
            r6.append(r7)
            goto L_0x0007
        L_0x00b6:
            r7 = 29
            r6.append(r7)
            byte[] r7 = PUNCTUATION
            byte r7 = r7[r0]
            char r7 = (char) r7
            r6.append(r7)
            goto L_0x001e
        L_0x00c5:
            boolean r7 = isMixed(r0)
            if (r7 == 0) goto L_0x00d5
            byte[] r7 = MIXED
            byte r7 = r7[r0]
            char r7 = (char) r7
            r6.append(r7)
            goto L_0x001e
        L_0x00d5:
            boolean r7 = isAlphaUpper(r0)
            if (r7 == 0) goto L_0x00e3
            r5 = 0
            r7 = 28
            r6.append(r7)
            goto L_0x0007
        L_0x00e3:
            boolean r7 = isAlphaLower(r0)
            if (r7 == 0) goto L_0x00f1
            r5 = 1
            r7 = 27
            r6.append(r7)
            goto L_0x0007
        L_0x00f1:
            int r7 = r10 + r3
            int r7 = r7 + 1
            if (r7 >= r11) goto L_0x010d
            int r7 = r10 + r3
            int r7 = r7 + 1
            char r7 = r9.charAt(r7)
            boolean r7 = isPunctuation(r7)
            if (r7 == 0) goto L_0x010d
            r5 = 3
            r7 = 25
            r6.append(r7)
            goto L_0x0007
        L_0x010d:
            r7 = 29
            r6.append(r7)
            byte[] r7 = PUNCTUATION
            byte r7 = r7[r0]
            char r7 = (char) r7
            r6.append(r7)
            goto L_0x001e
        L_0x011c:
            r5 = 0
            r7 = 29
            r6.append(r7)
            goto L_0x0007
        L_0x0124:
            r7 = 0
            goto L_0x002f
        L_0x0127:
            char r1 = r6.charAt(r2)
            goto L_0x003c
        L_0x012d:
            int r7 = r4 % 2
            if (r7 == 0) goto L_0x0139
            int r7 = r1 * 30
            int r7 = r7 + 29
            char r7 = (char) r7
            r12.append(r7)
        L_0x0139:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.encoder.PDF417HighLevelEncoder.encodeText(java.lang.CharSequence, int, int, java.lang.StringBuilder, int):int");
    }

    private static void encodeBinary(byte[] bytes, int startpos, int count, int startmode, StringBuilder sb) {
        if (count == 1 && startmode == 0) {
            sb.append(913);
        } else if (count % 6 == 0) {
            sb.append(924);
        } else {
            sb.append(901);
        }
        int idx = startpos;
        if (count >= 6) {
            char[] chars = new char[5];
            while ((startpos + count) - idx >= 6) {
                long t = 0;
                for (int i = 0; i < 6; i++) {
                    t = (t << 8) + ((long) (bytes[idx + i] & OnReminderListener.RET_FULL));
                }
                for (int i2 = 0; i2 < 5; i2++) {
                    chars[i2] = (char) ((int) (t % 900));
                    t /= 900;
                }
                for (int i3 = 4; i3 >= 0; i3--) {
                    sb.append(chars[i3]);
                }
                idx += 6;
            }
        }
        for (int i4 = idx; i4 < startpos + count; i4++) {
            sb.append((char) (bytes[i4] & 255));
        }
    }

    private static void encodeNumeric(String msg, int startpos, int count, StringBuilder sb) {
        int idx = 0;
        StringBuilder tmp = new StringBuilder((count / 3) + 1);
        BigInteger num900 = BigInteger.valueOf(900);
        BigInteger num0 = BigInteger.valueOf(0);
        while (idx < count) {
            tmp.setLength(0);
            int len = Math.min(44, count - idx);
            BigInteger bigint = new BigInteger("1" + msg.substring(startpos + idx, startpos + idx + len));
            do {
                tmp.append((char) bigint.mod(num900).intValue());
                bigint = bigint.divide(num900);
            } while (!bigint.equals(num0));
            for (int i = tmp.length() - 1; i >= 0; i--) {
                sb.append(tmp.charAt(i));
            }
            idx += len;
        }
    }

    private static boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private static boolean isAlphaUpper(char ch) {
        return ch == ' ' || (ch >= 'A' && ch <= 'Z');
    }

    private static boolean isAlphaLower(char ch) {
        return ch == ' ' || (ch >= 'a' && ch <= 'z');
    }

    private static boolean isMixed(char ch) {
        return MIXED[ch] != -1;
    }

    private static boolean isPunctuation(char ch) {
        return PUNCTUATION[ch] != -1;
    }

    private static boolean isText(char ch) {
        return ch == 9 || ch == 10 || ch == 13 || (ch >= ' ' && ch <= '~');
    }

    private static int determineConsecutiveDigitCount(CharSequence msg, int startpos) {
        int count = 0;
        int len = msg.length();
        int idx = startpos;
        if (startpos < len) {
            char ch = msg.charAt(startpos);
            while (isDigit(ch) && idx < len) {
                count++;
                idx++;
                if (idx < len) {
                    ch = msg.charAt(idx);
                }
            }
        }
        return count;
    }

    private static int determineConsecutiveTextCount(CharSequence msg, int startpos) {
        int len = msg.length();
        int idx = startpos;
        while (idx < len) {
            char ch = msg.charAt(idx);
            int numericCount = 0;
            while (numericCount < 13 && isDigit(ch) && idx < len) {
                numericCount++;
                idx++;
                if (idx < len) {
                    ch = msg.charAt(idx);
                }
            }
            if (numericCount < 13) {
                if (numericCount <= 0) {
                    if (!isText(msg.charAt(idx))) {
                        break;
                    }
                    idx++;
                }
            } else {
                return (idx - startpos) - numericCount;
            }
        }
        return idx - startpos;
    }

    private static int determineConsecutiveBinaryCount(String msg, int startpos, Charset encoding) throws WriterException {
        CharsetEncoder encoder = encoding.newEncoder();
        int len = msg.length();
        int idx = startpos;
        while (idx < len) {
            char ch = msg.charAt(idx);
            int numericCount = 0;
            while (numericCount < 13 && isDigit(ch)) {
                numericCount++;
                int i = idx + numericCount;
                if (i >= len) {
                    break;
                }
                ch = msg.charAt(i);
            }
            if (numericCount >= 13) {
                return idx - startpos;
            }
            char ch2 = msg.charAt(idx);
            if (!encoder.canEncode(ch2)) {
                throw new WriterException("Non-encodable character detected: " + ch2 + " (Unicode: " + ch2 + ')');
            }
            idx++;
        }
        return idx - startpos;
    }

    private static void encodingECI(int eci, StringBuilder sb) throws WriterException {
        if (eci >= 0 && eci < 900) {
            sb.append(927);
            sb.append((char) eci);
        } else if (eci < 810900) {
            sb.append(926);
            sb.append((char) ((eci / 900) - 1));
            sb.append((char) (eci % 900));
        } else if (eci < 811800) {
            sb.append(925);
            sb.append((char) (810900 - eci));
        } else {
            throw new WriterException("ECI number not in valid range from 0..811799, but was " + eci);
        }
    }
}
