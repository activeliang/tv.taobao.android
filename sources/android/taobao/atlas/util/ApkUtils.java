package android.taobao.atlas.util;

import android.taobao.atlas.runtime.RuntimeVariables;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

public class ApkUtils {
    static final int SYSTEM_ROOT_STATE_DISABLE = 0;
    static final int SYSTEM_ROOT_STATE_ENABLE = 1;
    static final int SYSTEM_ROOT_STATE_UNKNOW = -1;
    private static ZipFile sApkZip;
    private static int systemRootState = -1;

    public static ZipFile getApk() {
        if (sApkZip == null) {
            loadZip();
        }
        return sApkZip;
    }

    private static synchronized void loadZip() {
        synchronized (ApkUtils.class) {
            if (sApkZip == null) {
                try {
                    sApkZip = new ZipFile(RuntimeVariables.androidApplication.getApplicationInfo().sourceDir);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0050 A[SYNTHETIC, Splitter:B:29:0x0050] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x005c A[SYNTHETIC, Splitter:B:35:0x005c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final java.lang.String[] getApkPublicKey(java.lang.String r11) {
        /*
            r4 = 0
            java.util.jar.JarFile r5 = new java.util.jar.JarFile     // Catch:{ IOException -> 0x004d, all -> 0x0059 }
            r5.<init>(r11)     // Catch:{ IOException -> 0x004d, all -> 0x0059 }
            java.lang.String r10 = "classes.dex"
            java.util.jar.JarEntry r6 = r5.getJarEntry(r10)     // Catch:{ IOException -> 0x0068, all -> 0x0065 }
            if (r6 == 0) goto L_0x003f
            r10 = 4096(0x1000, float:5.74E-42)
            byte[] r9 = new byte[r10]     // Catch:{ IOException -> 0x0068, all -> 0x0065 }
            java.security.cert.Certificate[] r1 = loadCertificates(r5, r6, r9)     // Catch:{ IOException -> 0x0068, all -> 0x0065 }
            if (r1 == 0) goto L_0x003f
            int r10 = r1.length     // Catch:{ IOException -> 0x0068, all -> 0x0065 }
            java.lang.String[] r8 = new java.lang.String[r10]     // Catch:{ IOException -> 0x0068, all -> 0x0065 }
            r3 = 0
        L_0x001d:
            int r10 = r1.length     // Catch:{ IOException -> 0x0068, all -> 0x0065 }
            if (r3 >= r10) goto L_0x0033
            r0 = r1[r3]     // Catch:{ IOException -> 0x0068, all -> 0x0065 }
            java.security.PublicKey r7 = r0.getPublicKey()     // Catch:{ IOException -> 0x0068, all -> 0x0065 }
            byte[] r10 = r7.getEncoded()     // Catch:{ IOException -> 0x0068, all -> 0x0065 }
            java.lang.String r10 = bytesToHexString(r10)     // Catch:{ IOException -> 0x0068, all -> 0x0065 }
            r8[r3] = r10     // Catch:{ IOException -> 0x0068, all -> 0x0065 }
            int r3 = r3 + 1
            goto L_0x001d
        L_0x0033:
            if (r5 == 0) goto L_0x0038
            r5.close()     // Catch:{ IOException -> 0x003a }
        L_0x0038:
            r4 = r5
        L_0x0039:
            return r8
        L_0x003a:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0038
        L_0x003f:
            if (r5 == 0) goto L_0x006b
            r5.close()     // Catch:{ IOException -> 0x0047 }
            r4 = r5
        L_0x0045:
            r8 = 0
            goto L_0x0039
        L_0x0047:
            r2 = move-exception
            r2.printStackTrace()
            r4 = r5
            goto L_0x0045
        L_0x004d:
            r10 = move-exception
        L_0x004e:
            if (r4 == 0) goto L_0x0045
            r4.close()     // Catch:{ IOException -> 0x0054 }
            goto L_0x0045
        L_0x0054:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0045
        L_0x0059:
            r10 = move-exception
        L_0x005a:
            if (r4 == 0) goto L_0x005f
            r4.close()     // Catch:{ IOException -> 0x0060 }
        L_0x005f:
            throw r10
        L_0x0060:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x005f
        L_0x0065:
            r10 = move-exception
            r4 = r5
            goto L_0x005a
        L_0x0068:
            r10 = move-exception
            r4 = r5
            goto L_0x004e
        L_0x006b:
            r4 = r5
            goto L_0x0045
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.util.ApkUtils.getApkPublicKey(java.lang.String):java.lang.String[]");
    }

    private static Certificate[] loadCertificates(JarFile jarFile, JarEntry je, byte[] readBuffer) {
        try {
            InputStream is = new BufferedInputStream(jarFile.getInputStream(je));
            do {
            } while (is.read(readBuffer, 0, readBuffer.length) != -1);
            is.close();
            if (je != null) {
                return je.getCertificates();
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (RuntimeException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static final String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            String hv = Integer.toHexString(b & 255);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static final void chmod(File file) {
        if (file != null && !file.exists()) {
            file.mkdirs();
            try {
                Runtime.getRuntime().exec("chmod 555 " + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isRootSystem() {
        if (systemRootState == -1) {
            for (String searchPath : new String[]{"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"}) {
                if (new File(searchPath, "su").exists()) {
                    systemRootState = 1;
                    return true;
                }
            }
            return false;
        } else if (systemRootState == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static void copyDirectory(File sourceDir, File targetDir) throws Exception {
        for (File childFile : sourceDir.listFiles()) {
            if (childFile.isFile()) {
                File dir = new File(targetDir.getAbsolutePath());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                copyInputStreamToFile(new FileInputStream(childFile), new File(dir, childFile.getName()));
            } else {
                copyDirectory(childFile, new File(targetDir, childFile.getName()));
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0034 A[SYNTHETIC, Splitter:B:15:0x0034] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0039 A[SYNTHETIC, Splitter:B:18:0x0039] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x003e A[SYNTHETIC, Splitter:B:21:0x003e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copyInputStreamToFile(java.io.InputStream r9, java.io.File r10) throws java.io.IOException {
        /*
            r3 = 0
            r1 = 0
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x0073 }
            r4.<init>(r10)     // Catch:{ IOException -> 0x0073 }
            java.nio.channels.FileChannel r1 = r4.getChannel()     // Catch:{ IOException -> 0x001e, all -> 0x0070 }
            r6 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r6]     // Catch:{ IOException -> 0x001e, all -> 0x0070 }
        L_0x000f:
            int r5 = r9.read(r0)     // Catch:{ IOException -> 0x001e, all -> 0x0070 }
            if (r5 <= 0) goto L_0x0042
            r6 = 0
            java.nio.ByteBuffer r6 = java.nio.ByteBuffer.wrap(r0, r6, r5)     // Catch:{ IOException -> 0x001e, all -> 0x0070 }
            r1.write(r6)     // Catch:{ IOException -> 0x001e, all -> 0x0070 }
            goto L_0x000f
        L_0x001e:
            r2 = move-exception
            r3 = r4
        L_0x0020:
            android.taobao.atlas.util.log.impl.AtlasMonitor r6 = android.taobao.atlas.util.log.impl.AtlasMonitor.getInstance()     // Catch:{ all -> 0x0031 }
            java.lang.String r7 = "container_bundle_unzip_fail"
            r8 = 0
            r6.report(r7, r8, r2)     // Catch:{ all -> 0x0031 }
            java.io.IOException r6 = new java.io.IOException     // Catch:{ all -> 0x0031 }
            r6.<init>(r2)     // Catch:{ all -> 0x0031 }
            throw r6     // Catch:{ all -> 0x0031 }
        L_0x0031:
            r6 = move-exception
        L_0x0032:
            if (r9 == 0) goto L_0x0037
            r9.close()     // Catch:{ Exception -> 0x0061 }
        L_0x0037:
            if (r1 == 0) goto L_0x003c
            r1.close()     // Catch:{ Exception -> 0x0066 }
        L_0x003c:
            if (r3 == 0) goto L_0x0041
            r3.close()     // Catch:{ Exception -> 0x006b }
        L_0x0041:
            throw r6
        L_0x0042:
            if (r9 == 0) goto L_0x0047
            r9.close()     // Catch:{ Exception -> 0x0052 }
        L_0x0047:
            if (r1 == 0) goto L_0x004c
            r1.close()     // Catch:{ Exception -> 0x0057 }
        L_0x004c:
            if (r4 == 0) goto L_0x0051
            r4.close()     // Catch:{ Exception -> 0x005c }
        L_0x0051:
            return
        L_0x0052:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0047
        L_0x0057:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x004c
        L_0x005c:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0051
        L_0x0061:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0037
        L_0x0066:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x003c
        L_0x006b:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0041
        L_0x0070:
            r6 = move-exception
            r3 = r4
            goto L_0x0032
        L_0x0073:
            r2 = move-exception
            goto L_0x0020
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.atlas.util.ApkUtils.copyInputStreamToFile(java.io.InputStream, java.io.File):void");
    }
}
