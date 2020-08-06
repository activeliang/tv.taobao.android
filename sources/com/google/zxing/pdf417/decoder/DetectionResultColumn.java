package com.google.zxing.pdf417.decoder;

class DetectionResultColumn {
    private static final int MAX_NEARBY_DISTANCE = 5;
    private final BoundingBox boundingBox;
    private final Codeword[] codewords;

    DetectionResultColumn(BoundingBox boundingBox2) {
        this.boundingBox = new BoundingBox(boundingBox2);
        this.codewords = new Codeword[((boundingBox2.getMaxY() - boundingBox2.getMinY()) + 1)];
    }

    /* access modifiers changed from: package-private */
    public final Codeword getCodewordNearby(int imageRow) {
        Codeword codeword;
        Codeword codeword2;
        Codeword codeword3 = getCodeword(imageRow);
        if (codeword3 != null) {
            return codeword3;
        }
        for (int i = 1; i < 5; i++) {
            int nearImageRow = imageRowToCodewordIndex(imageRow) - i;
            if (nearImageRow >= 0 && (codeword2 = this.codewords[nearImageRow]) != null) {
                return codeword2;
            }
            int nearImageRow2 = imageRowToCodewordIndex(imageRow) + i;
            if (nearImageRow2 < this.codewords.length && (codeword = this.codewords[nearImageRow2]) != null) {
                return codeword;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public final int imageRowToCodewordIndex(int imageRow) {
        return imageRow - this.boundingBox.getMinY();
    }

    /* access modifiers changed from: package-private */
    public final void setCodeword(int imageRow, Codeword codeword) {
        this.codewords[imageRowToCodewordIndex(imageRow)] = codeword;
    }

    /* access modifiers changed from: package-private */
    public final Codeword getCodeword(int imageRow) {
        return this.codewords[imageRowToCodewordIndex(imageRow)];
    }

    /* access modifiers changed from: package-private */
    public final BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    /* access modifiers changed from: package-private */
    public final Codeword[] getCodewords() {
        return this.codewords;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0051, code lost:
        r4 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0065, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0066, code lost:
        r5.addSuppressed(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x006a, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006e, code lost:
        r4 = th;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0059 A[SYNTHETIC, Splitter:B:25:0x0059] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x006a  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x006e A[ExcHandler: all (th java.lang.Throwable), Splitter:B:1:0x0008] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String toString() {
        /*
            r13 = this;
            r4 = 0
            java.util.Formatter r1 = new java.util.Formatter
            r1.<init>()
            r5 = 0
            r2 = 0
            com.google.zxing.pdf417.decoder.Codeword[] r6 = r13.codewords     // Catch:{ Throwable -> 0x0051, all -> 0x006e }
            int r7 = r6.length     // Catch:{ Throwable -> 0x0051, all -> 0x006e }
            r3 = r2
        L_0x000c:
            if (r4 >= r7) goto L_0x005d
            r0 = r6[r4]     // Catch:{ Throwable -> 0x0070, all -> 0x006e }
            if (r0 != 0) goto L_0x0028
            java.lang.String r8 = "%3d:    |   %n"
            r9 = 1
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x0070, all -> 0x006e }
            r10 = 0
            int r2 = r3 + 1
            java.lang.Integer r11 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x0051, all -> 0x006e }
            r9[r10] = r11     // Catch:{ Throwable -> 0x0051, all -> 0x006e }
            r1.format(r8, r9)     // Catch:{ Throwable -> 0x0051, all -> 0x006e }
        L_0x0024:
            int r4 = r4 + 1
            r3 = r2
            goto L_0x000c
        L_0x0028:
            java.lang.String r8 = "%3d: %3d|%3d%n"
            r9 = 3
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x0070, all -> 0x006e }
            r10 = 0
            int r2 = r3 + 1
            java.lang.Integer r11 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x0051, all -> 0x006e }
            r9[r10] = r11     // Catch:{ Throwable -> 0x0051, all -> 0x006e }
            r10 = 1
            int r11 = r0.getRowNumber()     // Catch:{ Throwable -> 0x0051, all -> 0x006e }
            java.lang.Integer r11 = java.lang.Integer.valueOf(r11)     // Catch:{ Throwable -> 0x0051, all -> 0x006e }
            r9[r10] = r11     // Catch:{ Throwable -> 0x0051, all -> 0x006e }
            r10 = 2
            int r11 = r0.getValue()     // Catch:{ Throwable -> 0x0051, all -> 0x006e }
            java.lang.Integer r11 = java.lang.Integer.valueOf(r11)     // Catch:{ Throwable -> 0x0051, all -> 0x006e }
            r9[r10] = r11     // Catch:{ Throwable -> 0x0051, all -> 0x006e }
            r1.format(r8, r9)     // Catch:{ Throwable -> 0x0051, all -> 0x006e }
            goto L_0x0024
        L_0x0051:
            r4 = move-exception
        L_0x0052:
            throw r4     // Catch:{ all -> 0x0053 }
        L_0x0053:
            r5 = move-exception
            r12 = r5
            r5 = r4
            r4 = r12
        L_0x0057:
            if (r5 == 0) goto L_0x006a
            r1.close()     // Catch:{ Throwable -> 0x0065 }
        L_0x005c:
            throw r4
        L_0x005d:
            java.lang.String r4 = r1.toString()     // Catch:{ Throwable -> 0x0070, all -> 0x006e }
            r1.close()
            return r4
        L_0x0065:
            r6 = move-exception
            r5.addSuppressed(r6)
            goto L_0x005c
        L_0x006a:
            r1.close()
            goto L_0x005c
        L_0x006e:
            r4 = move-exception
            goto L_0x0057
        L_0x0070:
            r4 = move-exception
            r2 = r3
            goto L_0x0052
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DetectionResultColumn.toString():java.lang.String");
    }
}
