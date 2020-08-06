package android.taobao.windvane.file;

import android.app.Application;
import android.content.Context;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.util.CommonUtils;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileManager {
    static final int BUFFER = 1024;
    private static final String TAG = "FileManager";

    public static File createFolder(Context context, String rootDir) {
        String dir = context.getFilesDir().getAbsolutePath();
        if (!TextUtils.isEmpty(rootDir)) {
            dir = dir + File.separator + rootDir;
        }
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static void unzip(String srcFile, String destDir) {
        if (srcFile != null && destDir != null) {
            try {
                unzip((InputStream) new FileInputStream(srcFile), destDir);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean unzip(byte[] data, String destDir) {
        return unzip((InputStream) new ByteArrayInputStream(data), destDir);
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x00c5 A[SYNTHETIC, Splitter:B:38:0x00c5] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ca A[Catch:{ IOException -> 0x00d4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00cf A[Catch:{ IOException -> 0x00d4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00fd A[SYNTHETIC, Splitter:B:51:0x00fd] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0102 A[Catch:{ IOException -> 0x013e }] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0107 A[Catch:{ IOException -> 0x013e }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean unzip(java.io.InputStream r14, java.lang.String r15) {
        /*
            r10 = 0
            if (r14 == 0) goto L_0x0005
            if (r15 != 0) goto L_0x0006
        L_0x0005:
            return r10
        L_0x0006:
            java.lang.String r11 = "/"
            boolean r11 = r15.endsWith(r11)
            if (r11 != 0) goto L_0x0023
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.StringBuilder r11 = r11.append(r15)
            java.lang.String r12 = "/"
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r15 = r11.toString()
        L_0x0023:
            r4 = 0
            r8 = 0
            java.util.zip.ZipInputStream r9 = new java.util.zip.ZipInputStream     // Catch:{ Exception -> 0x0164 }
            r9.<init>(r14)     // Catch:{ Exception -> 0x0164 }
            r7 = 0
            r11 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r11]     // Catch:{ Exception -> 0x00a3, all -> 0x00f9 }
            java.lang.StringBuffer r6 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x00a3, all -> 0x00f9 }
            r11 = 200(0xc8, float:2.8E-43)
            r6.<init>(r11)     // Catch:{ Exception -> 0x00a3, all -> 0x00f9 }
            r5 = r4
        L_0x0037:
            java.util.zip.ZipEntry r7 = r9.getNextEntry()     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            if (r7 == 0) goto L_0x010b
            java.lang.String r11 = r7.getName()     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            r6.append(r11)     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            java.lang.String r11 = r6.toString()     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            java.lang.String r12 = "../"
            boolean r11 = r11.contains(r12)     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            if (r11 != 0) goto L_0x0037
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            r11.<init>()     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            java.lang.StringBuilder r11 = r11.append(r15)     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            java.lang.String r12 = r6.toString()     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            r3.<init>(r11)     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            r11 = 0
            int r12 = r6.length()     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            r6.delete(r11, r12)     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            boolean r11 = r7.isDirectory()     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            if (r11 == 0) goto L_0x007f
            r3.mkdirs()     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            r4 = r5
        L_0x007d:
            r5 = r4
            goto L_0x0037
        L_0x007f:
            java.io.File r11 = r3.getParentFile()     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            boolean r11 = r11.exists()     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            if (r11 != 0) goto L_0x0090
            java.io.File r11 = r3.getParentFile()     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            r11.mkdirs()     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
        L_0x0090:
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
            r4.<init>(r3)     // Catch:{ Exception -> 0x0167, all -> 0x0160 }
        L_0x0095:
            r11 = 0
            r12 = 1024(0x400, float:1.435E-42)
            int r2 = r9.read(r0, r11, r12)     // Catch:{ Exception -> 0x00a3, all -> 0x00f9 }
            if (r2 <= 0) goto L_0x00f5
            r11 = 0
            r4.write(r0, r11, r2)     // Catch:{ Exception -> 0x00a3, all -> 0x00f9 }
            goto L_0x0095
        L_0x00a3:
            r1 = move-exception
            r8 = r9
        L_0x00a5:
            java.lang.String r11 = "FileManager"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x015e }
            r12.<init>()     // Catch:{ all -> 0x015e }
            java.lang.String r13 = "unzip: IOException:"
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ all -> 0x015e }
            java.lang.String r13 = r1.getMessage()     // Catch:{ all -> 0x015e }
            java.lang.StringBuilder r12 = r12.append(r13)     // Catch:{ all -> 0x015e }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x015e }
            android.taobao.windvane.util.TaoLog.e(r11, r12)     // Catch:{ all -> 0x015e }
            if (r4 == 0) goto L_0x00c8
            r4.close()     // Catch:{ IOException -> 0x00d4 }
        L_0x00c8:
            if (r8 == 0) goto L_0x00cd
            r8.close()     // Catch:{ IOException -> 0x00d4 }
        L_0x00cd:
            if (r14 == 0) goto L_0x0005
            r14.close()     // Catch:{ IOException -> 0x00d4 }
            goto L_0x0005
        L_0x00d4:
            r1 = move-exception
            java.lang.String r11 = "FileManager"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "close Stream Exception:"
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r13 = r1.getMessage()
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r12 = r12.toString()
            android.taobao.windvane.util.TaoLog.e(r11, r12)
            goto L_0x0005
        L_0x00f5:
            r4.close()     // Catch:{ Exception -> 0x00a3, all -> 0x00f9 }
            goto L_0x007d
        L_0x00f9:
            r10 = move-exception
            r8 = r9
        L_0x00fb:
            if (r4 == 0) goto L_0x0100
            r4.close()     // Catch:{ IOException -> 0x013e }
        L_0x0100:
            if (r8 == 0) goto L_0x0105
            r8.close()     // Catch:{ IOException -> 0x013e }
        L_0x0105:
            if (r14 == 0) goto L_0x010a
            r14.close()     // Catch:{ IOException -> 0x013e }
        L_0x010a:
            throw r10
        L_0x010b:
            r10 = 1
            if (r5 == 0) goto L_0x0111
            r5.close()     // Catch:{ IOException -> 0x011d }
        L_0x0111:
            if (r9 == 0) goto L_0x0116
            r9.close()     // Catch:{ IOException -> 0x011d }
        L_0x0116:
            if (r14 == 0) goto L_0x0005
            r14.close()     // Catch:{ IOException -> 0x011d }
            goto L_0x0005
        L_0x011d:
            r1 = move-exception
            java.lang.String r11 = "FileManager"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "close Stream Exception:"
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r13 = r1.getMessage()
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r12 = r12.toString()
            android.taobao.windvane.util.TaoLog.e(r11, r12)
            goto L_0x0005
        L_0x013e:
            r1 = move-exception
            java.lang.String r11 = "FileManager"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "close Stream Exception:"
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r13 = r1.getMessage()
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r12 = r12.toString()
            android.taobao.windvane.util.TaoLog.e(r11, r12)
            goto L_0x010a
        L_0x015e:
            r10 = move-exception
            goto L_0x00fb
        L_0x0160:
            r10 = move-exception
            r8 = r9
            r4 = r5
            goto L_0x00fb
        L_0x0164:
            r1 = move-exception
            goto L_0x00a5
        L_0x0167:
            r1 = move-exception
            r8 = r9
            r4 = r5
            goto L_0x00a5
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.file.FileManager.unzip(java.io.InputStream, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0044 A[SYNTHETIC, Splitter:B:17:0x0044] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x007b A[SYNTHETIC, Splitter:B:32:0x007b] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0087 A[SYNTHETIC, Splitter:B:38:0x0087] */
    /* JADX WARNING: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:56:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copyFile(java.io.InputStream r8, java.io.File r9) {
        /*
            if (r8 == 0) goto L_0x0004
            if (r9 != 0) goto L_0x0005
        L_0x0004:
            return
        L_0x0005:
            r3 = 0
            r9.createNewFile()     // Catch:{ FileNotFoundException -> 0x0096, IOException -> 0x005a }
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x0096, IOException -> 0x005a }
            r4.<init>(r9)     // Catch:{ FileNotFoundException -> 0x0096, IOException -> 0x005a }
            r5 = 2048(0x800, float:2.87E-42)
            byte[] r0 = new byte[r5]     // Catch:{ FileNotFoundException -> 0x0022, IOException -> 0x0093, all -> 0x0090 }
            r2 = 0
        L_0x0013:
            r5 = 0
            r6 = 2048(0x800, float:2.87E-42)
            int r2 = r8.read(r0, r5, r6)     // Catch:{ FileNotFoundException -> 0x0022, IOException -> 0x0093, all -> 0x0090 }
            r5 = -1
            if (r2 == r5) goto L_0x004d
            r5 = 0
            r4.write(r0, r5, r2)     // Catch:{ FileNotFoundException -> 0x0022, IOException -> 0x0093, all -> 0x0090 }
            goto L_0x0013
        L_0x0022:
            r1 = move-exception
            r3 = r4
        L_0x0024:
            java.lang.String r5 = "FileManager"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0084 }
            r6.<init>()     // Catch:{ all -> 0x0084 }
            java.lang.String r7 = "copyFile: dest FileNotFoundException:"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0084 }
            java.lang.String r7 = r1.getMessage()     // Catch:{ all -> 0x0084 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0084 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x0084 }
            android.taobao.windvane.util.TaoLog.e(r5, r6)     // Catch:{ all -> 0x0084 }
            if (r3 == 0) goto L_0x0004
            r3.close()     // Catch:{ IOException -> 0x0048 }
            goto L_0x0004
        L_0x0048:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0004
        L_0x004d:
            if (r4 == 0) goto L_0x0098
            r4.close()     // Catch:{ IOException -> 0x0054 }
            r3 = r4
            goto L_0x0004
        L_0x0054:
            r1 = move-exception
            r1.printStackTrace()
            r3 = r4
            goto L_0x0004
        L_0x005a:
            r1 = move-exception
        L_0x005b:
            java.lang.String r5 = "FileManager"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0084 }
            r6.<init>()     // Catch:{ all -> 0x0084 }
            java.lang.String r7 = "copyFile: write IOException:"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0084 }
            java.lang.String r7 = r1.getMessage()     // Catch:{ all -> 0x0084 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0084 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x0084 }
            android.taobao.windvane.util.TaoLog.e(r5, r6)     // Catch:{ all -> 0x0084 }
            if (r3 == 0) goto L_0x0004
            r3.close()     // Catch:{ IOException -> 0x007f }
            goto L_0x0004
        L_0x007f:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0004
        L_0x0084:
            r5 = move-exception
        L_0x0085:
            if (r3 == 0) goto L_0x008a
            r3.close()     // Catch:{ IOException -> 0x008b }
        L_0x008a:
            throw r5
        L_0x008b:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x008a
        L_0x0090:
            r5 = move-exception
            r3 = r4
            goto L_0x0085
        L_0x0093:
            r1 = move-exception
            r3 = r4
            goto L_0x005b
        L_0x0096:
            r1 = move-exception
            goto L_0x0024
        L_0x0098:
            r3 = r4
            goto L_0x0004
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.file.FileManager.copyFile(java.io.InputStream, java.io.File):void");
    }

    public static String createBaseDir(Application context, String rootDir, String myDir, boolean hasSDCard) {
        String baseDir = null;
        StringBuilder sb = null;
        if (hasSDCard) {
            File file = context.getExternalCacheDir();
            if (file != null) {
                sb = new StringBuilder().append(file.getAbsolutePath()).append(File.separator);
            } else {
                File file2 = CommonUtils.getExternalCacheDir(context);
                if (file2 != null) {
                    sb = new StringBuilder().append(file2.getAbsolutePath()).append(File.separator);
                }
            }
            if (!TextUtils.isEmpty(rootDir) && sb != null) {
                sb.append(rootDir).append(File.separator);
                baseDir = sb.append(myDir).toString();
            }
        }
        if (baseDir == null) {
            baseDir = createInnerCacheStorage(context, rootDir, myDir);
        }
        TaoLog.d(TAG, "createBaseDir path:" + baseDir);
        return baseDir;
    }

    public static String createInnerCacheStorage(Application context, String rootDir, String myDir) {
        if (context.getFilesDir() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder().append(context.getCacheDir().getAbsolutePath());
        if (!TextUtils.isEmpty(rootDir)) {
            sb.append(File.separator).append(rootDir);
        }
        String baseDir = sb.append(File.separator).append(myDir).toString();
        TaoLog.d(TAG, "createInnerCacheStorage path:" + baseDir);
        return baseDir;
    }

    public static String createInnerfileStorage(Application context, String rootDir, String myDir) {
        if (context.getFilesDir() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder().append(context.getFilesDir().getAbsolutePath());
        if (!TextUtils.isEmpty(rootDir)) {
            sb.append(File.separator).append(rootDir);
        }
        String baseDir = sb.append(File.separator).append(myDir).toString();
        TaoLog.d(TAG, "createInnerfileStorage path:" + baseDir);
        return baseDir;
    }

    public static boolean copy(String srcPath, String destPath) {
        return copy(new File(srcPath), new File(destPath));
    }

    public static boolean copy(File srcFile, File destFile) {
        return copy(srcFile, destFile, (byte[]) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x005a, code lost:
        if (r12.length < 10) goto L_0x005c;
     */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0074 A[SYNTHETIC, Splitter:B:38:0x0074] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0079 A[SYNTHETIC, Splitter:B:41:0x0079] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00a5 A[SYNTHETIC, Splitter:B:63:0x00a5] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00aa A[SYNTHETIC, Splitter:B:66:0x00aa] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copy(java.io.File r10, java.io.File r11, byte[] r12) {
        /*
            r6 = 0
            r2 = 0
            r4 = 0
            boolean r7 = r10.exists()     // Catch:{ Exception -> 0x00bf }
            if (r7 != 0) goto L_0x0042
            boolean r7 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ Exception -> 0x00bf }
            if (r7 == 0) goto L_0x002d
            java.lang.String r7 = "FileManager"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00bf }
            r8.<init>()     // Catch:{ Exception -> 0x00bf }
            java.lang.String r9 = "src file not exist, "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Exception -> 0x00bf }
            java.io.File r9 = r10.getAbsoluteFile()     // Catch:{ Exception -> 0x00bf }
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Exception -> 0x00bf }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x00bf }
            android.taobao.windvane.util.TaoLog.w(r7, r8)     // Catch:{ Exception -> 0x00bf }
        L_0x002d:
            if (r2 == 0) goto L_0x0032
            r2.close()     // Catch:{ IOException -> 0x0038 }
        L_0x0032:
            if (r4 == 0) goto L_0x0037
            r4.close()     // Catch:{ IOException -> 0x003d }
        L_0x0037:
            return r6
        L_0x0038:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0032
        L_0x003d:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0037
        L_0x0042:
            boolean r7 = r11.exists()     // Catch:{ Exception -> 0x00bf }
            if (r7 != 0) goto L_0x004b
            r11.createNewFile()     // Catch:{ Exception -> 0x00bf }
        L_0x004b:
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00bf }
            r3.<init>(r10)     // Catch:{ Exception -> 0x00bf }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            r5.<init>(r11)     // Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            if (r12 == 0) goto L_0x005c
            int r7 = r12.length     // Catch:{ Exception -> 0x006c, all -> 0x00bb }
            r8 = 10
            if (r7 >= r8) goto L_0x0060
        L_0x005c:
            r7 = 2048(0x800, float:2.87E-42)
            byte[] r12 = new byte[r7]     // Catch:{ Exception -> 0x006c, all -> 0x00bb }
        L_0x0060:
            int r0 = r3.read(r12)     // Catch:{ Exception -> 0x006c, all -> 0x00bb }
            r7 = -1
            if (r0 == r7) goto L_0x0082
            r7 = 0
            r5.write(r12, r7, r0)     // Catch:{ Exception -> 0x006c, all -> 0x00bb }
            goto L_0x0060
        L_0x006c:
            r1 = move-exception
            r4 = r5
            r2 = r3
        L_0x006f:
            r1.printStackTrace()     // Catch:{ all -> 0x00a2 }
            if (r2 == 0) goto L_0x0077
            r2.close()     // Catch:{ IOException -> 0x009d }
        L_0x0077:
            if (r4 == 0) goto L_0x0037
            r4.close()     // Catch:{ IOException -> 0x007d }
            goto L_0x0037
        L_0x007d:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0037
        L_0x0082:
            r5.flush()     // Catch:{ Exception -> 0x006c, all -> 0x00bb }
            r6 = 1
            if (r3 == 0) goto L_0x008b
            r3.close()     // Catch:{ IOException -> 0x0093 }
        L_0x008b:
            if (r5 == 0) goto L_0x0090
            r5.close()     // Catch:{ IOException -> 0x0098 }
        L_0x0090:
            r4 = r5
            r2 = r3
            goto L_0x0037
        L_0x0093:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x008b
        L_0x0098:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0090
        L_0x009d:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0077
        L_0x00a2:
            r6 = move-exception
        L_0x00a3:
            if (r2 == 0) goto L_0x00a8
            r2.close()     // Catch:{ IOException -> 0x00ae }
        L_0x00a8:
            if (r4 == 0) goto L_0x00ad
            r4.close()     // Catch:{ IOException -> 0x00b3 }
        L_0x00ad:
            throw r6
        L_0x00ae:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00a8
        L_0x00b3:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00ad
        L_0x00b8:
            r6 = move-exception
            r2 = r3
            goto L_0x00a3
        L_0x00bb:
            r6 = move-exception
            r4 = r5
            r2 = r3
            goto L_0x00a3
        L_0x00bf:
            r1 = move-exception
            goto L_0x006f
        L_0x00c1:
            r1 = move-exception
            r2 = r3
            goto L_0x006f
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.file.FileManager.copy(java.io.File, java.io.File, byte[]):boolean");
    }

    public static boolean copyDir(String srcDir, String targetDir) {
        String srcDir2 = formatUrl(srcDir);
        String targetDir2 = formatUrl(targetDir);
        new File(targetDir2).mkdirs();
        File[] file = new File(srcDir2).listFiles();
        byte[] buffer = new byte[2048];
        if (file == null) {
            return false;
        }
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile() && !copy(file[i], new File(new File(targetDir2).getAbsolutePath() + File.separator + file[i].getName()), buffer)) {
                return false;
            }
            if (file[i].isDirectory() && !copyDir(srcDir2 + File.separator + file[i].getName(), targetDir2 + File.separator + file[i].getName())) {
                return false;
            }
        }
        return true;
    }

    private static String formatUrl(String path) {
        String path2 = path.replaceAll(WVUtils.URL_SEPARATOR, WVNativeCallbackUtil.SEPERATER);
        if (path2.endsWith(WVNativeCallbackUtil.SEPERATER)) {
            return path2.substring(0, path2.length() - 1);
        }
        return path2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:101:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x008c A[SYNTHETIC, Splitter:B:25:0x008c] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0092 A[SYNTHETIC, Splitter:B:29:0x0092] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00bc A[SYNTHETIC, Splitter:B:41:0x00bc] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x010a A[SYNTHETIC, Splitter:B:65:0x010a] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean unZipByFilePath(java.lang.String r22, java.lang.String r23) {
        /*
            long r14 = java.lang.System.currentTimeMillis()
            r6 = 0
            r13 = 0
            r16 = 0
            java.io.File r12 = new java.io.File     // Catch:{ Exception -> 0x020d }
            r0 = r23
            r12.<init>(r0)     // Catch:{ Exception -> 0x020d }
            boolean r18 = r12.exists()     // Catch:{ Exception -> 0x020d }
            if (r18 != 0) goto L_0x0018
            r12.mkdirs()     // Catch:{ Exception -> 0x020d }
        L_0x0018:
            java.util.zip.ZipFile r17 = new java.util.zip.ZipFile     // Catch:{ Exception -> 0x020d }
            r0 = r17
            r1 = r22
            r0.<init>(r1)     // Catch:{ Exception -> 0x020d }
            java.util.Enumeration r9 = r17.entries()     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            r7 = r6
        L_0x0026:
            boolean r18 = r9.hasMoreElements()     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            if (r18 == 0) goto L_0x0170
            java.lang.Object r10 = r9.nextElement()     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            java.util.zip.ZipEntry r10 = (java.util.zip.ZipEntry) r10     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            r18 = 1024(0x400, float:1.435E-42)
            r0 = r18
            byte[] r4 = new byte[r0]     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            r18.<init>()     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            r0 = r18
            r1 = r23
            java.lang.StringBuilder r18 = r0.append(r1)     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            java.lang.String r19 = "/"
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            java.lang.String r19 = r10.getName()     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            java.lang.String r8 = r18.toString()     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            r5.<init>(r8)     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            boolean r18 = r10.isDirectory()     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            if (r18 == 0) goto L_0x00c0
            r5.mkdirs()     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
        L_0x0066:
            r0 = r17
            java.io.InputStream r13 = r0.getInputStream(r10)     // Catch:{ all -> 0x0210 }
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch:{ all -> 0x0210 }
            r6.<init>(r8)     // Catch:{ all -> 0x0210 }
            r18 = 1024(0x400, float:1.435E-42)
            r0 = r18
            byte[] r2 = new byte[r0]     // Catch:{ all -> 0x0089 }
        L_0x0077:
            int r3 = r13.read(r2)     // Catch:{ all -> 0x0089 }
            r18 = -1
            r0 = r18
            if (r3 == r0) goto L_0x00d6
            r18 = 0
            r0 = r18
            r6.write(r2, r0, r3)     // Catch:{ all -> 0x0089 }
            goto L_0x0077
        L_0x0089:
            r18 = move-exception
        L_0x008a:
            if (r6 == 0) goto L_0x0090
            r6.close()     // Catch:{ IOException -> 0x012e }
            r6 = 0
        L_0x0090:
            if (r13 == 0) goto L_0x0096
            r13.close()     // Catch:{ IOException -> 0x014f }
            r13 = 0
        L_0x0096:
            throw r18     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
        L_0x0097:
            r9 = move-exception
            r16 = r17
        L_0x009a:
            java.lang.String r18 = "FileManager"
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ all -> 0x0204 }
            r19.<init>()     // Catch:{ all -> 0x0204 }
            java.lang.String r20 = "unZipByFilePath failed : "
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ all -> 0x0204 }
            java.lang.String r20 = r9.getMessage()     // Catch:{ all -> 0x0204 }
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ all -> 0x0204 }
            java.lang.String r19 = r19.toString()     // Catch:{ all -> 0x0204 }
            android.taobao.windvane.util.TaoLog.e(r18, r19)     // Catch:{ all -> 0x0204 }
            r18 = 0
            if (r16 == 0) goto L_0x00bf
            r16.close()     // Catch:{ IOException -> 0x01c2 }
        L_0x00bf:
            return r18
        L_0x00c0:
            boolean r18 = r5.exists()     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            if (r18 != 0) goto L_0x0066
            java.io.File r18 = r5.getParentFile()     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            r18.mkdirs()     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            r5.createNewFile()     // Catch:{ Exception -> 0x00d1, all -> 0x0207 }
            goto L_0x0066
        L_0x00d1:
            r9 = move-exception
            r16 = r17
            r6 = r7
            goto L_0x009a
        L_0x00d6:
            if (r6 == 0) goto L_0x00dc
            r6.close()     // Catch:{ IOException -> 0x00e5 }
            r6 = 0
        L_0x00dc:
            if (r13 == 0) goto L_0x00e2
            r13.close()     // Catch:{ IOException -> 0x010e }
            r13 = 0
        L_0x00e2:
            r7 = r6
            goto L_0x0026
        L_0x00e5:
            r11 = move-exception
            java.lang.String r18 = "FileManager"
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            r19.<init>()     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.String r20 = "stream failed to close : "
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.String r20 = r11.getMessage()     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.String r19 = r19.toString()     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            android.taobao.windvane.util.TaoLog.e(r18, r19)     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            goto L_0x00dc
        L_0x0105:
            r18 = move-exception
            r16 = r17
        L_0x0108:
            if (r16 == 0) goto L_0x010d
            r16.close()     // Catch:{ IOException -> 0x01e3 }
        L_0x010d:
            throw r18
        L_0x010e:
            r11 = move-exception
            java.lang.String r18 = "FileManager"
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            r19.<init>()     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.String r20 = "stream failed to close : "
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.String r20 = r11.getMessage()     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.String r19 = r19.toString()     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            android.taobao.windvane.util.TaoLog.e(r18, r19)     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            goto L_0x00e2
        L_0x012e:
            r11 = move-exception
            java.lang.String r19 = "FileManager"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            r20.<init>()     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.String r21 = "stream failed to close : "
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.String r21 = r11.getMessage()     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            android.taobao.windvane.util.TaoLog.e(r19, r20)     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            goto L_0x0090
        L_0x014f:
            r11 = move-exception
            java.lang.String r19 = "FileManager"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            r20.<init>()     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.String r21 = "stream failed to close : "
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.String r21 = r11.getMessage()     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            android.taobao.windvane.util.TaoLog.e(r19, r20)     // Catch:{ Exception -> 0x0097, all -> 0x0105 }
            goto L_0x0096
        L_0x0170:
            if (r17 == 0) goto L_0x0175
            r17.close()     // Catch:{ IOException -> 0x01a2 }
        L_0x0175:
            boolean r18 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r18 == 0) goto L_0x019b
            java.lang.String r18 = "FileManager"
            java.lang.StringBuilder r19 = new java.lang.StringBuilder
            r19.<init>()
            java.lang.String r20 = "unZipByFilePath use time(ms) : "
            java.lang.StringBuilder r19 = r19.append(r20)
            long r20 = java.lang.System.currentTimeMillis()
            long r20 = r20 - r14
            java.lang.StringBuilder r19 = r19.append(r20)
            java.lang.String r19 = r19.toString()
            android.taobao.windvane.util.TaoLog.d(r18, r19)
        L_0x019b:
            r18 = 1
            r16 = r17
            r6 = r7
            goto L_0x00bf
        L_0x01a2:
            r9 = move-exception
            java.lang.String r18 = "FileManager"
            java.lang.StringBuilder r19 = new java.lang.StringBuilder
            r19.<init>()
            java.lang.String r20 = "zipfile failed to close : "
            java.lang.StringBuilder r19 = r19.append(r20)
            java.lang.String r20 = r9.getMessage()
            java.lang.StringBuilder r19 = r19.append(r20)
            java.lang.String r19 = r19.toString()
            android.taobao.windvane.util.TaoLog.e(r18, r19)
            goto L_0x0175
        L_0x01c2:
            r9 = move-exception
            java.lang.String r19 = "FileManager"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder
            r20.<init>()
            java.lang.String r21 = "zipfile failed to close : "
            java.lang.StringBuilder r20 = r20.append(r21)
            java.lang.String r21 = r9.getMessage()
            java.lang.StringBuilder r20 = r20.append(r21)
            java.lang.String r20 = r20.toString()
            android.taobao.windvane.util.TaoLog.e(r19, r20)
            goto L_0x00bf
        L_0x01e3:
            r9 = move-exception
            java.lang.String r19 = "FileManager"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder
            r20.<init>()
            java.lang.String r21 = "zipfile failed to close : "
            java.lang.StringBuilder r20 = r20.append(r21)
            java.lang.String r21 = r9.getMessage()
            java.lang.StringBuilder r20 = r20.append(r21)
            java.lang.String r20 = r20.toString()
            android.taobao.windvane.util.TaoLog.e(r19, r20)
            goto L_0x010d
        L_0x0204:
            r18 = move-exception
            goto L_0x0108
        L_0x0207:
            r18 = move-exception
            r16 = r17
            r6 = r7
            goto L_0x0108
        L_0x020d:
            r9 = move-exception
            goto L_0x009a
        L_0x0210:
            r18 = move-exception
            r6 = r7
            goto L_0x008a
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.file.FileManager.unZipByFilePath(java.lang.String, java.lang.String):boolean");
    }
}
