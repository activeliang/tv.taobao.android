package com.taobao.orange.util;

import com.taobao.orange.GlobalOrange;
import com.taobao.orange.OConstant;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class FileUtil {
    public static final String ORANGE_DIR = "orange_config";
    private static final String TAG = "FileUtil";
    private static File targetDir = getTargetDir();

    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:47:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void persistObject(java.lang.Object r14, java.lang.String r15) {
        /*
            r13 = 1
            r12 = 0
            boolean r8 = com.taobao.orange.util.OLog.isPrintLog(r13)
            if (r8 == 0) goto L_0x001b
            java.lang.String r8 = "FileUtil"
            java.lang.String r9 = "persistObject"
            r10 = 2
            java.lang.Object[] r10 = new java.lang.Object[r10]
            java.lang.String r11 = "filename"
            r10[r12] = r11
            r10[r13] = r15
            com.taobao.orange.util.OLog.d(r8, r9, r10)
        L_0x001b:
            r0 = 0
            r2 = 0
            r7 = 0
            java.io.File r8 = targetDir     // Catch:{ Throwable -> 0x0078 }
            boolean r8 = r8.exists()     // Catch:{ Throwable -> 0x0078 }
            if (r8 != 0) goto L_0x002b
            java.io.File r8 = targetDir     // Catch:{ Throwable -> 0x0078 }
            r8.mkdirs()     // Catch:{ Throwable -> 0x0078 }
        L_0x002b:
            java.lang.String r8 = ".tmp"
            java.io.File r9 = targetDir     // Catch:{ Throwable -> 0x0078 }
            java.io.File r7 = java.io.File.createTempFile(r15, r8, r9)     // Catch:{ Throwable -> 0x0078 }
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x0078 }
            r1.<init>(r7)     // Catch:{ Throwable -> 0x0078 }
            java.io.ObjectOutputStream r3 = new java.io.ObjectOutputStream     // Catch:{ Throwable -> 0x00d4, all -> 0x00cd }
            java.io.BufferedOutputStream r8 = new java.io.BufferedOutputStream     // Catch:{ Throwable -> 0x00d4, all -> 0x00cd }
            r8.<init>(r1)     // Catch:{ Throwable -> 0x00d4, all -> 0x00cd }
            r3.<init>(r8)     // Catch:{ Throwable -> 0x00d4, all -> 0x00cd }
            r3.writeObject(r14)     // Catch:{ Throwable -> 0x00d7, all -> 0x00d0 }
            r3.flush()     // Catch:{ Throwable -> 0x00d7, all -> 0x00d0 }
            java.io.File r6 = new java.io.File     // Catch:{ Throwable -> 0x00d7, all -> 0x00d0 }
            java.io.File r8 = targetDir     // Catch:{ Throwable -> 0x00d7, all -> 0x00d0 }
            r6.<init>(r8, r15)     // Catch:{ Throwable -> 0x00d7, all -> 0x00d0 }
            boolean r4 = r7.renameTo(r6)     // Catch:{ Throwable -> 0x00d7, all -> 0x00d0 }
            if (r4 != 0) goto L_0x0064
            java.lang.String r8 = "private_orange"
            java.lang.String r9 = "ORANGE_EXCEPTION"
            java.lang.String r10 = "persistObject rename fail"
            r12 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            com.taobao.orange.util.OrangeMonitor.commitCount(r8, r9, r10, r12)     // Catch:{ Throwable -> 0x00d7, all -> 0x00d0 }
        L_0x0064:
            com.taobao.orange.util.OrangeUtils.close(r3)
            com.taobao.orange.util.OrangeUtils.close(r1)
            if (r7 == 0) goto L_0x00db
            boolean r8 = r7.exists()
            if (r8 == 0) goto L_0x00db
            r7.delete()
            r2 = r3
            r0 = r1
        L_0x0077:
            return
        L_0x0078:
            r5 = move-exception
        L_0x0079:
            java.lang.String r8 = "private_orange"
            java.lang.String r9 = "ORANGE_EXCEPTION"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ba }
            r10.<init>()     // Catch:{ all -> 0x00ba }
            java.lang.String r11 = "persistObject: "
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x00ba }
            java.lang.String r11 = r5.getMessage()     // Catch:{ all -> 0x00ba }
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x00ba }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x00ba }
            r12 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            com.taobao.orange.util.OrangeMonitor.commitCount(r8, r9, r10, r12)     // Catch:{ all -> 0x00ba }
            java.lang.String r8 = "FileUtil"
            java.lang.String r9 = "persistObject"
            r10 = 0
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ all -> 0x00ba }
            com.taobao.orange.util.OLog.e(r8, r9, r5, r10)     // Catch:{ all -> 0x00ba }
            com.taobao.orange.util.OrangeUtils.close(r2)
            com.taobao.orange.util.OrangeUtils.close(r0)
            if (r7 == 0) goto L_0x0077
            boolean r8 = r7.exists()
            if (r8 == 0) goto L_0x0077
            r7.delete()
            goto L_0x0077
        L_0x00ba:
            r8 = move-exception
        L_0x00bb:
            com.taobao.orange.util.OrangeUtils.close(r2)
            com.taobao.orange.util.OrangeUtils.close(r0)
            if (r7 == 0) goto L_0x00cc
            boolean r9 = r7.exists()
            if (r9 == 0) goto L_0x00cc
            r7.delete()
        L_0x00cc:
            throw r8
        L_0x00cd:
            r8 = move-exception
            r0 = r1
            goto L_0x00bb
        L_0x00d0:
            r8 = move-exception
            r2 = r3
            r0 = r1
            goto L_0x00bb
        L_0x00d4:
            r5 = move-exception
            r0 = r1
            goto L_0x0079
        L_0x00d7:
            r5 = move-exception
            r2 = r3
            r0 = r1
            goto L_0x0079
        L_0x00db:
            r2 = r3
            r0 = r1
            goto L_0x0077
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.orange.util.FileUtil.persistObject(java.lang.Object, java.lang.String):void");
    }

    public static Object restoreObject(String filename) {
        if (OLog.isPrintLog(1)) {
            OLog.d(TAG, "restoreObject", "filename", filename);
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            File targetFile = new File(targetDir, filename);
            if (!targetFile.exists()) {
                if (OLog.isPrintLog(3)) {
                    OLog.w(TAG, "restoreObject not exists", "filename", filename);
                }
                OrangeUtils.close((Closeable) null);
                OrangeUtils.close((Closeable) null);
                return null;
            }
            FileInputStream fis2 = new FileInputStream(targetFile);
            try {
                ObjectInputStream ois2 = new ObjectInputStream(new BufferedInputStream(fis2));
                try {
                    Object obj = ois2.readObject();
                    OrangeUtils.close(ois2);
                    OrangeUtils.close(fis2);
                    ObjectInputStream objectInputStream = ois2;
                    FileInputStream fileInputStream = fis2;
                    return obj;
                } catch (Throwable th) {
                    th = th;
                    ois = ois2;
                    fis = fis2;
                    OrangeUtils.close(ois);
                    OrangeUtils.close(fis);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fis = fis2;
                OrangeUtils.close(ois);
                OrangeUtils.close(fis);
                throw th;
            }
        } catch (Throwable th3) {
            t = th3;
            OrangeMonitor.commitCount(OConstant.MONITOR_PRIVATE_MODULE, OConstant.POINT_EXCEPTION, "restoreObject: " + t.getMessage(), 1.0d);
            OLog.e(TAG, "restoreObject", t, new Object[0]);
            OrangeUtils.close(ois);
            OrangeUtils.close(fis);
            return null;
        }
    }

    public static void deleteConfigFile(String filename) {
        File targetFile = new File(targetDir, filename);
        if (targetFile.exists()) {
            boolean result = targetFile.delete();
            if (OLog.isPrintLog(1)) {
                OLog.d(TAG, "deleteConfigFile", "filename", filename, "result", Boolean.valueOf(result));
            }
        }
    }

    public static void clearCacheFile() {
        OLog.i(TAG, "clearCacheFile", new Object[0]);
        cleanDir(targetDir);
    }

    private static void cleanDir(File dir) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    cleanDir(file);
                }
            }
        }
    }

    private static File getTargetDir() {
        File targetDir2 = new File(new File(GlobalOrange.context.getFilesDir(), ORANGE_DIR), GlobalOrange.env.getDes());
        if (targetDir2.exists() && targetDir2.isFile()) {
            targetDir2.delete();
        }
        if (!targetDir2.exists() && !targetDir2.mkdirs()) {
            OLog.w(TAG, "getTargetDir mkdirs fail", new Object[0]);
            OrangeMonitor.commitCount(OConstant.MONITOR_PRIVATE_MODULE, OConstant.POINT_EXCEPTION, "getTargetDir mkdirs fail", 1.0d);
        }
        OLog.d(TAG, "getTargetDir", targetDir2.getAbsolutePath());
        return targetDir2;
    }
}
