package android.taobao.atlas.startup.patch;

import java.io.File;

public class CombineDexVerifier implements PatchVerifier {
    private static final String CLASS_SUFFIX = "classes";
    private static final String DEX_SUFFIX = ".dex";

    public boolean verify(File mergeFile) {
        if (mergeFile == null || !mergeFile.exists() || !isNewBundleFileValid(mergeFile)) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x002a A[SYNTHETIC, Splitter:B:19:0x002a] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0036 A[SYNTHETIC, Splitter:B:25:0x0036] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isNewBundleFileValid(java.io.File r5) {
        /*
            r1 = 1
            r2 = 0
            java.util.zip.ZipFile r3 = new java.util.zip.ZipFile     // Catch:{ Throwable -> 0x0027, all -> 0x0033 }
            r3.<init>(r5)     // Catch:{ Throwable -> 0x0027, all -> 0x0033 }
            java.lang.String r4 = "classes.dex"
            java.util.zip.ZipEntry r4 = r3.getEntry(r4)     // Catch:{ Throwable -> 0x0042, all -> 0x003f }
            if (r4 == 0) goto L_0x0020
            java.lang.String r4 = "classes2.dex"
            java.util.zip.ZipEntry r4 = r3.getEntry(r4)     // Catch:{ Throwable -> 0x0042, all -> 0x003f }
            if (r4 == 0) goto L_0x0020
        L_0x0019:
            if (r3 == 0) goto L_0x001e
            r3.close()     // Catch:{ IOException -> 0x0022 }
        L_0x001e:
            r2 = r3
        L_0x001f:
            return r1
        L_0x0020:
            r1 = 0
            goto L_0x0019
        L_0x0022:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x001e
        L_0x0027:
            r0 = move-exception
        L_0x0028:
            if (r2 == 0) goto L_0x001f
            r2.close()     // Catch:{ IOException -> 0x002e }
            goto L_0x001f
        L_0x002e:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x001f
        L_0x0033:
            r4 = move-exception
        L_0x0034:
            if (r2 == 0) goto L_0x0039
            r2.close()     // Catch:{ IOException -> 0x003a }
        L_0x0039:
            throw r4
        L_0x003a:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0039
        L_0x003f:
            r4 = move-exception
            r2 = r3
            goto L_0x0034
        L_0x0042:
            r0 = move-exception
            r2 = r3
            goto L_0x0028
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.startup.patch.CombineDexVerifier.isNewBundleFileValid(java.io.File):boolean");
    }
}
