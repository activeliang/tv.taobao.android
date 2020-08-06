package android.taobao.atlas.startup.patch;

import android.taobao.atlas.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CombineDexMerger extends PatchMerger {
    private static final int BUFFER_SIZE = 16384;
    private static final String CLASS_SUFFIX = "classes";
    private static final String DEX_SUFFIX = ".dex";

    public CombineDexMerger(PatchVerifier patchVerifier) {
        super(patchVerifier);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00a6, code lost:
        if (android.taobao.atlas.startup.patch.KernalBundle.kernalBundle == null) goto L_0x00a8;
     */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0073 A[SYNTHETIC, Splitter:B:24:0x0073] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x007b A[Catch:{ Exception -> 0x0209 }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0080 A[Catch:{ Exception -> 0x0209 }] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0191 A[SYNTHETIC, Splitter:B:81:0x0191] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0199 A[Catch:{ Exception -> 0x0211 }] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x019e A[Catch:{ Exception -> 0x0211 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean merge(java.io.File r21, java.io.File r22, java.io.File r23) {
        /*
            r20 = this;
            r13 = 0
            r7 = 0
            r10 = 0
            java.util.zip.ZipFile r8 = new java.util.zip.ZipFile     // Catch:{ Exception -> 0x0222 }
            r0 = r22
            r8.<init>(r0)     // Catch:{ Exception -> 0x0222 }
            java.util.zip.ZipFile r11 = new java.util.zip.ZipFile     // Catch:{ Exception -> 0x0225, all -> 0x0219 }
            r0 = r21
            r11.<init>(r0)     // Catch:{ Exception -> 0x0225, all -> 0x0219 }
            java.lang.String r16 = r22.getAbsolutePath()     // Catch:{ Exception -> 0x0229, all -> 0x021d }
            java.lang.String r17 = "dexpatch"
            boolean r3 = r16.contains(r17)     // Catch:{ Exception -> 0x0229, all -> 0x021d }
            java.util.zip.ZipOutputStream r14 = new java.util.zip.ZipOutputStream     // Catch:{ Exception -> 0x0229, all -> 0x021d }
            java.io.FileOutputStream r16 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0229, all -> 0x021d }
            r0 = r16
            r1 = r23
            r0.<init>(r1)     // Catch:{ Exception -> 0x0229, all -> 0x021d }
            r0 = r16
            r14.<init>(r0)     // Catch:{ Exception -> 0x0229, all -> 0x021d }
            r2 = 1
            java.util.Enumeration r12 = r8.entries()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
        L_0x0031:
            boolean r16 = r12.hasMoreElements()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            if (r16 == 0) goto L_0x00a0
            java.lang.Object r4 = r12.nextElement()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            java.util.zip.ZipEntry r4 = (java.util.zip.ZipEntry) r4     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            if (r3 == 0) goto L_0x004c
            java.lang.String r16 = r4.getName()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            java.lang.String r17 = "META-INF"
            boolean r16 = r16.startsWith(r17)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            if (r16 != 0) goto L_0x0031
        L_0x004c:
            java.util.zip.ZipEntry r6 = new java.util.zip.ZipEntry     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            java.lang.String r16 = r4.getName()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            r0 = r16
            r6.<init>(r0)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            r14.putNextEntry(r6)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            boolean r16 = r4.isDirectory()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            if (r16 != 0) goto L_0x0031
            java.io.InputStream r16 = r8.getInputStream(r4)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            r0 = r16
            copy(r0, r14)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            goto L_0x0031
        L_0x006a:
            r4 = move-exception
            r10 = r11
            r7 = r8
            r13 = r14
        L_0x006e:
            r4.printStackTrace()     // Catch:{ all -> 0x0216 }
            if (r13 == 0) goto L_0x0079
            r13.closeEntry()     // Catch:{ Exception -> 0x0209 }
            r13.close()     // Catch:{ Exception -> 0x0209 }
        L_0x0079:
            if (r10 == 0) goto L_0x007e
            r10.close()     // Catch:{ Exception -> 0x0209 }
        L_0x007e:
            if (r7 == 0) goto L_0x0083
            r7.close()     // Catch:{ Exception -> 0x0209 }
        L_0x0083:
            r0 = r20
            android.taobao.atlas.startup.patch.PatchVerifier r0 = r0.patchVerifier     // Catch:{ Exception -> 0x0209 }
            r16 = r0
            if (r16 == 0) goto L_0x020d
            boolean r16 = r23.exists()     // Catch:{ Exception -> 0x0209 }
            if (r16 == 0) goto L_0x020d
            r0 = r20
            android.taobao.atlas.startup.patch.PatchVerifier r0 = r0.patchVerifier     // Catch:{ Exception -> 0x0209 }
            r16 = r0
            r0 = r16
            r1 = r23
            boolean r16 = r0.verify(r1)     // Catch:{ Exception -> 0x0209 }
        L_0x009f:
            return r16
        L_0x00a0:
            if (r3 == 0) goto L_0x00a8
            if (r3 == 0) goto L_0x013f
            android.taobao.atlas.startup.patch.KernalBundle r16 = android.taobao.atlas.startup.patch.KernalBundle.kernalBundle     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            if (r16 != 0) goto L_0x013f
        L_0x00a8:
            java.lang.String r17 = "%s%s%s"
            r16 = 3
            r0 = r16
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            r18 = r0
            r16 = 0
            java.lang.String r19 = "classes"
            r18[r16] = r19     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            r19 = 1
            r16 = 1
            r0 = r16
            if (r2 <= r0) goto L_0x00fd
            java.lang.Integer r16 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
        L_0x00c6:
            r18[r19] = r16     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            r16 = 2
            java.lang.String r19 = ".dex"
            r18[r16] = r19     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            java.lang.String r16 = java.lang.String.format(r17, r18)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            r0 = r16
            java.util.zip.ZipEntry r5 = r11.getEntry(r0)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            if (r5 == 0) goto L_0x0101
            boolean r16 = r5.isDirectory()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            if (r16 != 0) goto L_0x0101
            java.util.zip.ZipEntry r15 = new java.util.zip.ZipEntry     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            java.lang.String r16 = r5.getName()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            java.lang.String r16 = getUpdatedDexEntryName(r16)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            r15.<init>(r16)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            r14.putNextEntry(r15)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            java.io.InputStream r16 = r11.getInputStream(r5)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            r0 = r16
            copy(r0, r14)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            int r2 = r2 + 1
            goto L_0x00a8
        L_0x00fd:
            java.lang.String r16 = ""
            goto L_0x00c6
        L_0x0101:
            r16 = 1
            if (r14 == 0) goto L_0x010b
            r14.closeEntry()     // Catch:{ Exception -> 0x0136 }
            r14.close()     // Catch:{ Exception -> 0x0136 }
        L_0x010b:
            if (r11 == 0) goto L_0x0110
            r11.close()     // Catch:{ Exception -> 0x0136 }
        L_0x0110:
            if (r8 == 0) goto L_0x0115
            r8.close()     // Catch:{ Exception -> 0x0136 }
        L_0x0115:
            r0 = r20
            android.taobao.atlas.startup.patch.PatchVerifier r0 = r0.patchVerifier     // Catch:{ Exception -> 0x0136 }
            r17 = r0
            if (r17 == 0) goto L_0x013a
            boolean r17 = r23.exists()     // Catch:{ Exception -> 0x0136 }
            if (r17 == 0) goto L_0x013a
            r0 = r20
            android.taobao.atlas.startup.patch.PatchVerifier r0 = r0.patchVerifier     // Catch:{ Exception -> 0x0136 }
            r17 = r0
            r0 = r17
            r1 = r23
            boolean r16 = r0.verify(r1)     // Catch:{ Exception -> 0x0136 }
            r10 = r11
            r7 = r8
            r13 = r14
            goto L_0x009f
        L_0x0136:
            r4 = move-exception
            r4.printStackTrace()
        L_0x013a:
            r10 = r11
            r7 = r8
            r13 = r14
            goto L_0x009f
        L_0x013f:
            java.util.Enumeration r9 = r11.entries()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
        L_0x0143:
            boolean r16 = r9.hasMoreElements()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            if (r16 == 0) goto L_0x01cb
            java.lang.Object r4 = r9.nextElement()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            java.util.zip.ZipEntry r4 = (java.util.zip.ZipEntry) r4     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            java.lang.String r16 = r4.getName()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            java.lang.String r17 = "classes"
            boolean r16 = r16.startsWith(r17)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            if (r16 == 0) goto L_0x01bf
            java.lang.String r16 = r4.getName()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            java.lang.String r17 = ".dex"
            boolean r16 = r16.endsWith(r17)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            if (r16 == 0) goto L_0x01bf
            java.util.zip.ZipEntry r6 = new java.util.zip.ZipEntry     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            java.lang.String r16 = r4.getName()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            java.lang.String r16 = getUpdatedDexEntryName(r16)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            r0 = r16
            r6.<init>(r0)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
        L_0x0178:
            r14.putNextEntry(r6)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            boolean r16 = r4.isDirectory()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            if (r16 != 0) goto L_0x0143
            java.io.InputStream r16 = r11.getInputStream(r4)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            r0 = r16
            copy(r0, r14)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            goto L_0x0143
        L_0x018b:
            r16 = move-exception
            r10 = r11
            r7 = r8
            r13 = r14
        L_0x018f:
            if (r13 == 0) goto L_0x0197
            r13.closeEntry()     // Catch:{ Exception -> 0x0211 }
            r13.close()     // Catch:{ Exception -> 0x0211 }
        L_0x0197:
            if (r10 == 0) goto L_0x019c
            r10.close()     // Catch:{ Exception -> 0x0211 }
        L_0x019c:
            if (r7 == 0) goto L_0x01a1
            r7.close()     // Catch:{ Exception -> 0x0211 }
        L_0x01a1:
            r0 = r20
            android.taobao.atlas.startup.patch.PatchVerifier r0 = r0.patchVerifier     // Catch:{ Exception -> 0x0211 }
            r17 = r0
            if (r17 == 0) goto L_0x0215
            boolean r17 = r23.exists()     // Catch:{ Exception -> 0x0211 }
            if (r17 == 0) goto L_0x0215
            r0 = r20
            android.taobao.atlas.startup.patch.PatchVerifier r0 = r0.patchVerifier     // Catch:{ Exception -> 0x0211 }
            r17 = r0
            r0 = r17
            r1 = r23
            boolean r16 = r0.verify(r1)     // Catch:{ Exception -> 0x0211 }
            goto L_0x009f
        L_0x01bf:
            java.util.zip.ZipEntry r6 = new java.util.zip.ZipEntry     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            java.lang.String r16 = r4.getName()     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            r0 = r16
            r6.<init>(r0)     // Catch:{ Exception -> 0x006a, all -> 0x018b }
            goto L_0x0178
        L_0x01cb:
            r16 = 1
            if (r14 == 0) goto L_0x01d5
            r14.closeEntry()     // Catch:{ Exception -> 0x0200 }
            r14.close()     // Catch:{ Exception -> 0x0200 }
        L_0x01d5:
            if (r11 == 0) goto L_0x01da
            r11.close()     // Catch:{ Exception -> 0x0200 }
        L_0x01da:
            if (r8 == 0) goto L_0x01df
            r8.close()     // Catch:{ Exception -> 0x0200 }
        L_0x01df:
            r0 = r20
            android.taobao.atlas.startup.patch.PatchVerifier r0 = r0.patchVerifier     // Catch:{ Exception -> 0x0200 }
            r17 = r0
            if (r17 == 0) goto L_0x0204
            boolean r17 = r23.exists()     // Catch:{ Exception -> 0x0200 }
            if (r17 == 0) goto L_0x0204
            r0 = r20
            android.taobao.atlas.startup.patch.PatchVerifier r0 = r0.patchVerifier     // Catch:{ Exception -> 0x0200 }
            r17 = r0
            r0 = r17
            r1 = r23
            boolean r16 = r0.verify(r1)     // Catch:{ Exception -> 0x0200 }
            r10 = r11
            r7 = r8
            r13 = r14
            goto L_0x009f
        L_0x0200:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0204:
            r10 = r11
            r7 = r8
            r13 = r14
            goto L_0x009f
        L_0x0209:
            r4 = move-exception
            r4.printStackTrace()
        L_0x020d:
            r16 = 1
            goto L_0x009f
        L_0x0211:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0215:
            throw r16
        L_0x0216:
            r16 = move-exception
            goto L_0x018f
        L_0x0219:
            r16 = move-exception
            r7 = r8
            goto L_0x018f
        L_0x021d:
            r16 = move-exception
            r10 = r11
            r7 = r8
            goto L_0x018f
        L_0x0222:
            r4 = move-exception
            goto L_0x006e
        L_0x0225:
            r4 = move-exception
            r7 = r8
            goto L_0x006e
        L_0x0229:
            r4 = move-exception
            r10 = r11
            r7 = r8
            goto L_0x006e
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.startup.patch.CombineDexMerger.merge(java.io.File, java.io.File, java.io.File):boolean");
    }

    private static String getUpdatedDexEntryName(String originalEntryName) {
        if (originalEntryName.equals(DexFormat.DEX_IN_JAR_NAME)) {
            return "classes2.dex";
        }
        return String.format("classes%s.dex", new Object[]{Integer.valueOf(Integer.parseInt(StringUtils.substringBetween(originalEntryName, "classes", ".dex")) + 1)});
    }

    public static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] readContent = new byte[16384];
        while (true) {
            int bytesRead = input.read(readContent);
            if (bytesRead != -1) {
                output.write(readContent, 0, bytesRead);
            } else {
                return;
            }
        }
    }
}
