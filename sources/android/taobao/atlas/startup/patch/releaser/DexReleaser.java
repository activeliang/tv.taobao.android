package android.taobao.atlas.startup.patch.releaser;

import android.os.Build;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipFile;

public class DexReleaser {
    private static final int BUFFER_SIZE = 16384;
    private static final String CLASS_SUFFIX = "classes";
    private static final String DEX_SUFFIX = ".dex";

    /* JADX WARNING: Removed duplicated region for block: B:35:0x007a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean releaseDexes(java.io.File r12, java.io.File r13, boolean r14) throws java.io.IOException {
        /*
            r9 = 1
            java.lang.String r10 = r12.getAbsolutePath()
            java.lang.String r11 = "dexpatch"
            boolean r1 = r10.contains(r11)
            r7 = 0
            java.util.zip.ZipFile r8 = new java.util.zip.ZipFile     // Catch:{ Exception -> 0x0081 }
            r8.<init>(r12)     // Catch:{ Exception -> 0x0081 }
            boolean r5 = hasDexFile(r8)     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            if (r5 != 0) goto L_0x001f
            if (r8 == 0) goto L_0x001d
            r8.close()
        L_0x001d:
            r7 = r8
        L_0x001e:
            return r9
        L_0x001f:
            int r10 = android.taobao.atlas.runtime.RuntimeVariables.patchVersion     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            r11 = 2
            if (r10 == r11) goto L_0x002a
            boolean r10 = isArt()     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            if (r10 != 0) goto L_0x006c
        L_0x002a:
            java.util.Enumeration r3 = r8.entries()     // Catch:{ Exception -> 0x0060, all -> 0x007e }
        L_0x002e:
            boolean r10 = r3.hasMoreElements()     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            if (r10 == 0) goto L_0x0070
            java.lang.Object r6 = r3.nextElement()     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            java.util.zip.ZipEntry r6 = (java.util.zip.ZipEntry) r6     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            java.lang.String r10 = r6.getName()     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            java.lang.String r11 = ".dex"
            boolean r10 = r10.endsWith(r11)     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            if (r10 == 0) goto L_0x002e
            java.io.File r0 = new java.io.File     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            java.lang.String r10 = r6.getName()     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            r0.<init>(r13, r10)     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            r4.<init>(r0)     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            java.io.InputStream r10 = r8.getInputStream(r6)     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            copy(r10, r4)     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            r4.close()     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            goto L_0x002e
        L_0x0060:
            r2 = move-exception
            r7 = r8
        L_0x0062:
            r2.printStackTrace()     // Catch:{ all -> 0x0077 }
            if (r7 == 0) goto L_0x006a
            r7.close()
        L_0x006a:
            r9 = 0
            goto L_0x001e
        L_0x006c:
            int r10 = android.taobao.atlas.runtime.RuntimeVariables.patchVersion     // Catch:{ Exception -> 0x0060, all -> 0x007e }
            if (r10 != r9) goto L_0x0070
        L_0x0070:
            if (r8 == 0) goto L_0x0075
            r8.close()
        L_0x0075:
            r7 = r8
            goto L_0x001e
        L_0x0077:
            r9 = move-exception
        L_0x0078:
            if (r7 == 0) goto L_0x007d
            r7.close()
        L_0x007d:
            throw r9
        L_0x007e:
            r9 = move-exception
            r7 = r8
            goto L_0x0078
        L_0x0081:
            r2 = move-exception
            goto L_0x0062
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.startup.patch.releaser.DexReleaser.releaseDexes(java.io.File, java.io.File, boolean):boolean");
    }

    public static boolean isArt() {
        return Build.VERSION.SDK_INT > 20;
    }

    private static boolean hasDexFile(ZipFile zipFile) {
        return zipFile.getEntry(DexFormat.DEX_IN_JAR_NAME) != null;
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
