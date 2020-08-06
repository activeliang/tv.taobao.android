package com.loc;

/* compiled from: Base64 */
public class dp {
    static final /* synthetic */ boolean a = (!dp.class.desiredAssertionStatus());

    /* compiled from: Base64 */
    static abstract class a {
        public byte[] a;
        public int b;

        a() {
        }
    }

    /* compiled from: Base64 */
    static class b extends a {
        private static final int[] c = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int[] d = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private int e = 0;
        private int f = 0;
        private final int[] g = c;

        public b(byte[] bArr) {
            this.a = bArr;
        }

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* JADX WARNING: Removed duplicated region for block: B:44:0x0105  */
        /* JADX WARNING: Removed duplicated region for block: B:45:0x010a  */
        /* JADX WARNING: Removed duplicated region for block: B:46:0x0113  */
        /* JADX WARNING: Removed duplicated region for block: B:47:0x0122  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean a(byte[] r14, int r15) {
            /*
                r13 = this;
                r12 = -2
                r11 = -1
                r10 = 6
                r3 = 0
                int r0 = r13.e
                if (r0 != r10) goto L_0x000a
                r0 = r3
            L_0x0009:
                return r0
            L_0x000a:
                int r6 = r15 + 0
                int r0 = r13.e
                int r1 = r13.f
                byte[] r7 = r13.a
                int[] r8 = r13.g
                r2 = r3
                r5 = r0
                r0 = r3
            L_0x0017:
                if (r2 >= r6) goto L_0x00fa
                if (r5 != 0) goto L_0x0060
            L_0x001b:
                int r4 = r2 + 4
                if (r4 > r6) goto L_0x005e
                byte r1 = r14[r2]
                r1 = r1 & 255(0xff, float:3.57E-43)
                r1 = r8[r1]
                int r1 = r1 << 18
                int r4 = r2 + 1
                byte r4 = r14[r4]
                r4 = r4 & 255(0xff, float:3.57E-43)
                r4 = r8[r4]
                int r4 = r4 << 12
                r1 = r1 | r4
                int r4 = r2 + 2
                byte r4 = r14[r4]
                r4 = r4 & 255(0xff, float:3.57E-43)
                r4 = r8[r4]
                int r4 = r4 << 6
                r1 = r1 | r4
                int r4 = r2 + 3
                byte r4 = r14[r4]
                r4 = r4 & 255(0xff, float:3.57E-43)
                r4 = r8[r4]
                r1 = r1 | r4
                if (r1 < 0) goto L_0x005e
                int r4 = r0 + 2
                byte r9 = (byte) r1
                r7[r4] = r9
                int r4 = r0 + 1
                int r9 = r1 >> 8
                byte r9 = (byte) r9
                r7[r4] = r9
                int r4 = r1 >> 16
                byte r4 = (byte) r4
                r7[r0] = r4
                int r0 = r0 + 3
                int r2 = r2 + 4
                goto L_0x001b
            L_0x005e:
                if (r2 >= r6) goto L_0x00fa
            L_0x0060:
                int r4 = r2 + 1
                byte r2 = r14[r2]
                r2 = r2 & 255(0xff, float:3.57E-43)
                r2 = r8[r2]
                switch(r5) {
                    case 0: goto L_0x006d;
                    case 1: goto L_0x007b;
                    case 2: goto L_0x008c;
                    case 3: goto L_0x00ac;
                    case 4: goto L_0x00e4;
                    case 5: goto L_0x00f3;
                    default: goto L_0x006b;
                }
            L_0x006b:
                r2 = r4
                goto L_0x0017
            L_0x006d:
                if (r2 < 0) goto L_0x0075
                int r1 = r5 + 1
                r5 = r1
                r1 = r2
                r2 = r4
                goto L_0x0017
            L_0x0075:
                if (r2 == r11) goto L_0x006b
                r13.e = r10
                r0 = r3
                goto L_0x0009
            L_0x007b:
                if (r2 < 0) goto L_0x0085
                int r1 = r1 << 6
                r1 = r1 | r2
                int r2 = r5 + 1
                r5 = r2
                r2 = r4
                goto L_0x0017
            L_0x0085:
                if (r2 == r11) goto L_0x006b
                r13.e = r10
                r0 = r3
                goto L_0x0009
            L_0x008c:
                if (r2 < 0) goto L_0x0096
                int r1 = r1 << 6
                r1 = r1 | r2
                int r2 = r5 + 1
                r5 = r2
                r2 = r4
                goto L_0x0017
            L_0x0096:
                if (r2 != r12) goto L_0x00a5
                int r2 = r0 + 1
                int r5 = r1 >> 4
                byte r5 = (byte) r5
                r7[r0] = r5
                r0 = 4
                r5 = r0
                r0 = r2
                r2 = r4
                goto L_0x0017
            L_0x00a5:
                if (r2 == r11) goto L_0x006b
                r13.e = r10
                r0 = r3
                goto L_0x0009
            L_0x00ac:
                if (r2 < 0) goto L_0x00c8
                int r1 = r1 << 6
                r1 = r1 | r2
                int r2 = r0 + 2
                byte r5 = (byte) r1
                r7[r2] = r5
                int r2 = r0 + 1
                int r5 = r1 >> 8
                byte r5 = (byte) r5
                r7[r2] = r5
                int r2 = r1 >> 16
                byte r2 = (byte) r2
                r7[r0] = r2
                int r0 = r0 + 3
                r2 = r4
                r5 = r3
                goto L_0x0017
            L_0x00c8:
                if (r2 != r12) goto L_0x00dd
                int r2 = r0 + 1
                int r5 = r1 >> 2
                byte r5 = (byte) r5
                r7[r2] = r5
                int r2 = r1 >> 10
                byte r2 = (byte) r2
                r7[r0] = r2
                int r0 = r0 + 2
                r2 = 5
                r5 = r2
                r2 = r4
                goto L_0x0017
            L_0x00dd:
                if (r2 == r11) goto L_0x006b
                r13.e = r10
                r0 = r3
                goto L_0x0009
            L_0x00e4:
                if (r2 != r12) goto L_0x00ec
                int r2 = r5 + 1
                r5 = r2
                r2 = r4
                goto L_0x0017
            L_0x00ec:
                if (r2 == r11) goto L_0x006b
                r13.e = r10
                r0 = r3
                goto L_0x0009
            L_0x00f3:
                if (r2 == r11) goto L_0x006b
                r13.e = r10
                r0 = r3
                goto L_0x0009
            L_0x00fa:
                r2 = r1
                switch(r5) {
                    case 0: goto L_0x00fe;
                    case 1: goto L_0x0105;
                    case 2: goto L_0x010a;
                    case 3: goto L_0x0113;
                    case 4: goto L_0x0122;
                    default: goto L_0x00fe;
                }
            L_0x00fe:
                r13.e = r5
                r13.b = r0
                r0 = 1
                goto L_0x0009
            L_0x0105:
                r13.e = r10
                r0 = r3
                goto L_0x0009
            L_0x010a:
                int r1 = r0 + 1
                int r2 = r2 >> 4
                byte r2 = (byte) r2
                r7[r0] = r2
                r0 = r1
                goto L_0x00fe
            L_0x0113:
                int r1 = r0 + 1
                int r3 = r2 >> 10
                byte r3 = (byte) r3
                r7[r0] = r3
                int r0 = r1 + 1
                int r2 = r2 >> 2
                byte r2 = (byte) r2
                r7[r1] = r2
                goto L_0x00fe
            L_0x0122:
                r13.e = r10
                r0 = r3
                goto L_0x0009
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.dp.b.a(byte[], int):boolean");
        }
    }

    private dp() {
    }

    public static byte[] a(byte[] bArr) {
        int length = bArr.length;
        b bVar = new b(new byte[((length * 3) / 4)]);
        if (!bVar.a(bArr, length)) {
            throw new IllegalArgumentException("bad base-64");
        } else if (bVar.b == bVar.a.length) {
            return bVar.a;
        } else {
            byte[] bArr2 = new byte[bVar.b];
            System.arraycopy(bVar.a, 0, bArr2, 0, bVar.b);
            return bArr2;
        }
    }
}
