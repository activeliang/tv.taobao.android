package com.uc.webview.export.cyclone;

import android.content.Context;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.taobao.atlas.dexmerge.MergeConstants;
import java.io.Closeable;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.apache.commons.codec.language.Soundex;

/* compiled from: ProGuard */
public class UCCyclone {
    public static String dataFolder = "cyclone";
    public static boolean enableDebugLog = true;
    public static ValueCallback<String> loadLibraryCallback = null;
    public static ValueCallback<Pair<String, HashMap<String, String>>> statCallback = null;

    public static File expectCreateDirFile(File file) {
        if (file.exists() || file.mkdirs()) {
            return file;
        }
        throw new UCKnownException(1003, String.format("Directory [%s] mkdir failed.", new Object[]{file.getAbsolutePath()}));
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }

    static Object a(Class<?> cls, String str, Class[] clsArr, Object[] objArr) throws Exception {
        return b(cls, str, clsArr, objArr);
    }

    private static Object b(Class<?> cls, String str, Class[] clsArr, Object[] objArr) throws Exception {
        Method method;
        try {
            method = cls.getDeclaredMethod(str, clsArr);
        } catch (Throwable th) {
            method = cls.getMethod(str, clsArr);
        }
        return a(method, objArr);
    }

    static Object a(Method method, Object[] objArr) throws Exception {
        method.setAccessible(true);
        try {
            return method.invoke((Object) null, objArr);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof Exception) {
                throw ((Exception) targetException);
            } else if (targetException instanceof Error) {
                throw ((Error) targetException);
            } else {
                throw new RuntimeException(targetException);
            }
        }
    }

    static void a(String str, HashMap<String, String> hashMap) {
        ValueCallback<Pair<String, HashMap<String, String>>> valueCallback = statCallback;
        if (valueCallback != null) {
            try {
                valueCallback.onReceiveValue(new Pair(str, hashMap));
            } catch (Exception e) {
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0044 A[SYNTHETIC, Splitter:B:23:0x0044] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0049 A[SYNTHETIC, Splitter:B:26:0x0049] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void a(byte[][] r5, java.io.File r6) throws java.lang.Throwable {
        /*
            r1 = 0
            java.lang.String r0 = "sdk_7z_f"
            r2 = 0
            a((java.lang.String) r0, (java.util.HashMap<java.lang.String, java.lang.String>) r2)     // Catch:{ all -> 0x0040 }
            java.io.File r0 = r6.getParentFile()     // Catch:{ all -> 0x0040 }
            boolean r2 = r0.exists()     // Catch:{ all -> 0x0040 }
            if (r2 != 0) goto L_0x001b
            boolean r2 = r0.isDirectory()     // Catch:{ all -> 0x0040 }
            if (r2 == 0) goto L_0x001b
            r0.mkdirs()     // Catch:{ all -> 0x0040 }
        L_0x001b:
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ all -> 0x0040 }
            r2.<init>(r6)     // Catch:{ all -> 0x0040 }
            java.io.BufferedOutputStream r3 = new java.io.BufferedOutputStream     // Catch:{ all -> 0x0055 }
            r3.<init>(r2)     // Catch:{ all -> 0x0055 }
            r0 = 0
        L_0x0026:
            r1 = 10
            if (r0 >= r1) goto L_0x0032
            r1 = r5[r0]     // Catch:{ all -> 0x005a }
            r3.write(r1)     // Catch:{ all -> 0x005a }
            int r0 = r0 + 1
            goto L_0x0026
        L_0x0032:
            java.lang.String r0 = "sdk_7z_fs"
            r1 = 0
            a((java.lang.String) r0, (java.util.HashMap<java.lang.String, java.lang.String>) r1)     // Catch:{ all -> 0x005a }
            r3.close()     // Catch:{ IOException -> 0x004d }
        L_0x003c:
            r2.close()     // Catch:{ IOException -> 0x004f }
        L_0x003f:
            return
        L_0x0040:
            r0 = move-exception
            r2 = r1
        L_0x0042:
            if (r2 == 0) goto L_0x0047
            r2.close()     // Catch:{ IOException -> 0x0051 }
        L_0x0047:
            if (r1 == 0) goto L_0x004c
            r1.close()     // Catch:{ IOException -> 0x0053 }
        L_0x004c:
            throw r0
        L_0x004d:
            r0 = move-exception
            goto L_0x003c
        L_0x004f:
            r0 = move-exception
            goto L_0x003f
        L_0x0051:
            r2 = move-exception
            goto L_0x0047
        L_0x0053:
            r1 = move-exception
            goto L_0x004c
        L_0x0055:
            r0 = move-exception
            r4 = r2
            r2 = r1
            r1 = r4
            goto L_0x0042
        L_0x005a:
            r0 = move-exception
            r1 = r2
            r2 = r3
            goto L_0x0042
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.UCCyclone.a(byte[][], java.io.File):void");
    }

    static File a(Context context) throws UCKnownException {
        UCLogger create;
        File dir;
        UCLogger uCLogger = null;
        try {
            dir = expectCreateDirFile(new File(context.getCacheDir(), dataFolder));
        } catch (Throwable th) {
            if (create != null) {
                create.print("getDataFolder: from dir app_* Exception:", th);
            }
            throw new UCKnownException(1003, th);
        }
        if (enableDebugLog) {
            uCLogger = UCLogger.create(UploadQueueMgr.MSGTYPE_INTERVAL, "cyclone");
        }
        if (uCLogger != null) {
            uCLogger.print("getDataFolder: ok.", new Throwable[0]);
        }
        return dir;
    }

    public static File expectFile(File file, String str) throws UCKnownException {
        return expectFile(new File(file, str));
    }

    public static File expectFile(String str) throws UCKnownException {
        return expectFile(new File(str));
    }

    public static File expectFile(File file) throws UCKnownException {
        if (file.exists()) {
            return file;
        }
        throw new UCKnownException(1001, String.format("File [%s] not exists.", new Object[]{file.getAbsolutePath()}));
    }

    static String a(File file) {
        return getDecompressSourceHash(file.getAbsolutePath(), file.length(), file.lastModified(), false);
    }

    public static String getDecompressSourceHash(String str, long j, long j2, boolean z) {
        StringBuilder sb = new StringBuilder();
        if (!z) {
            str = getSourceHash(str);
        }
        return sb.append(str).append("_").append(getSourceHash(j, j2)).toString();
    }

    public static String getSourceHash(String str) {
        return String.valueOf(str.hashCode()).replace(Soundex.SILENT_MARKER, '_');
    }

    public static String getSourceHash(long j, long j2) {
        return j + "_" + j2;
    }

    public static synchronized boolean decompressIfNeeded(Context context, boolean z, File file, File file2, FilenameFilter filenameFilter, boolean z2) throws UCKnownException {
        boolean decompressIfNeeded;
        synchronized (UCCyclone.class) {
            decompressIfNeeded = decompressIfNeeded(context, z, file.getAbsolutePath(), file.length(), file.lastModified(), file, file2, filenameFilter, z2);
        }
        return decompressIfNeeded;
    }

    public static boolean isDecompressFinished(File file) {
        String[] split = file.getName().split("_");
        if (split.length == 2) {
            return a(file.getParentFile().getName(), Long.valueOf(split[0]).longValue(), Long.valueOf(split[1]).longValue(), file, true);
        }
        return false;
    }

    private static boolean a(String str, long j, long j2, File file, boolean z) {
        return b(str, j, j2, file, z).exists() && !c(str, j, j2, file, z).exists();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0035, code lost:
        throw ((com.uc.webview.export.cyclone.UCKnownException) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x007a, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        close(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x007e, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
        close(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0126, code lost:
        if (r2 != null) goto L_0x0133;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0132, code lost:
        throw new com.uc.webview.export.cyclone.UCKnownException(2002, "No entry exists in zip file. Make sure specify a valid zip file url.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0133, code lost:
        a(com.uc.webview.export.internal.interfaces.IWaStat.DEC_ZIP_SUCCESS, (java.util.HashMap<java.lang.String, java.lang.String>) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0148, code lost:
        throw new com.uc.webview.export.cyclone.UCKnownException((int) android.taobao.windvane.service.WVEventId.PAGE_onReceivedTitle, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0028, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0029, code lost:
        a(com.uc.webview.export.internal.interfaces.IWaStat.DEC_EXCEPTION, (java.util.HashMap<java.lang.String, java.lang.String>) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0031, code lost:
        if ((r0 instanceof com.uc.webview.export.cyclone.UCKnownException) != false) goto L_0x0033;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:2:0x000a, B:16:0x004e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void a(boolean r10, android.content.Context r11, java.lang.String r12, java.lang.String r13, java.io.FilenameFilter r14) throws com.uc.webview.export.cyclone.UCKnownException {
        /*
            r0 = 0
            r3 = 0
            java.lang.String r1 = "sdk_dec"
            a((java.lang.String) r1, (java.util.HashMap<java.lang.String, java.lang.String>) r3)
            if (r10 == 0) goto L_0x0036
            int r0 = com.uc.webview.export.cyclone.UCSevenZip.dec(r11, r12, r13)     // Catch:{ Throwable -> 0x0028 }
            if (r0 == 0) goto L_0x013a
            com.uc.webview.export.cyclone.UCKnownException r1 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x0028 }
            r2 = 2001(0x7d1, float:2.804E-42)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0028 }
            java.lang.String r5 = "Error on 7z decoding:"
            r4.<init>(r5)     // Catch:{ Throwable -> 0x0028 }
            java.lang.StringBuilder r0 = r4.append(r0)     // Catch:{ Throwable -> 0x0028 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0028 }
            r1.<init>((int) r2, (java.lang.String) r0)     // Catch:{ Throwable -> 0x0028 }
            throw r1     // Catch:{ Throwable -> 0x0028 }
        L_0x0028:
            r0 = move-exception
            java.lang.String r1 = "sdk_dec_e"
            a((java.lang.String) r1, (java.util.HashMap<java.lang.String, java.lang.String>) r3)
            boolean r1 = r0 instanceof com.uc.webview.export.cyclone.UCKnownException
            if (r1 == 0) goto L_0x0141
            com.uc.webview.export.cyclone.UCKnownException r0 = (com.uc.webview.export.cyclone.UCKnownException) r0
            throw r0
        L_0x0036:
            java.lang.String r1 = "sdk_decz"
            r2 = 0
            a((java.lang.String) r1, (java.util.HashMap<java.lang.String, java.lang.String>) r2)     // Catch:{ Throwable -> 0x0028 }
            java.util.zip.ZipInputStream r4 = new java.util.zip.ZipInputStream     // Catch:{ Throwable -> 0x0028 }
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch:{ Throwable -> 0x0028 }
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0028 }
            r2.<init>(r12)     // Catch:{ Throwable -> 0x0028 }
            r1.<init>(r2)     // Catch:{ Throwable -> 0x0028 }
            r4.<init>(r1)     // Catch:{ Throwable -> 0x0028 }
            r1 = r0
            r2 = r3
        L_0x004e:
            java.util.zip.ZipEntry r5 = r4.getNextEntry()     // Catch:{ all -> 0x007a }
            if (r5 == 0) goto L_0x0123
            r6 = 4096(0x1000, float:5.74E-42)
            byte[] r6 = new byte[r6]     // Catch:{ all -> 0x007a }
            java.lang.String r5 = r5.getName()     // Catch:{ all -> 0x007a }
            java.lang.String r7 = ".."
            boolean r7 = r5.contains(r7)     // Catch:{ all -> 0x007a }
            if (r7 == 0) goto L_0x007f
            com.uc.webview.export.cyclone.UCKnownException r0 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ all -> 0x007a }
            r1 = 2012(0x7dc, float:2.82E-42)
            java.lang.String r2 = "Zip entry [%s] not valid."
            r6 = 1
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x007a }
            r7 = 0
            r6[r7] = r5     // Catch:{ all -> 0x007a }
            java.lang.String r2 = java.lang.String.format(r2, r6)     // Catch:{ all -> 0x007a }
            r0.<init>((int) r1, (java.lang.String) r2)     // Catch:{ all -> 0x007a }
            throw r0     // Catch:{ all -> 0x007a }
        L_0x007a:
            r0 = move-exception
            close(r4)     // Catch:{ Throwable -> 0x0028 }
            throw r0     // Catch:{ Throwable -> 0x0028 }
        L_0x007f:
            if (r14 == 0) goto L_0x0094
            java.io.File r7 = new java.io.File     // Catch:{ all -> 0x007a }
            r7.<init>(r5)     // Catch:{ all -> 0x007a }
            java.io.File r8 = r7.getParentFile()     // Catch:{ all -> 0x007a }
            java.lang.String r7 = r7.getName()     // Catch:{ all -> 0x007a }
            boolean r7 = r14.accept(r8, r7)     // Catch:{ all -> 0x007a }
            if (r7 == 0) goto L_0x004e
        L_0x0094:
            java.io.File r7 = new java.io.File     // Catch:{ all -> 0x007a }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x007a }
            r8.<init>()     // Catch:{ all -> 0x007a }
            java.lang.StringBuilder r8 = r8.append(r13)     // Catch:{ all -> 0x007a }
            java.lang.String r9 = "/"
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ all -> 0x007a }
            java.lang.StringBuilder r8 = r8.append(r5)     // Catch:{ all -> 0x007a }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x007a }
            r7.<init>(r8)     // Catch:{ all -> 0x007a }
            java.lang.String r8 = "/"
            boolean r8 = r5.endsWith(r8)     // Catch:{ all -> 0x007a }
            if (r8 != 0) goto L_0x00c3
            java.lang.String r8 = "\\"
            boolean r5 = r5.endsWith(r8)     // Catch:{ all -> 0x007a }
            if (r5 == 0) goto L_0x00cd
        L_0x00c3:
            boolean r5 = r7.exists()     // Catch:{ all -> 0x007a }
            if (r5 != 0) goto L_0x004e
            r7.mkdirs()     // Catch:{ all -> 0x007a }
            goto L_0x004e
        L_0x00cd:
            java.io.File r2 = new java.io.File     // Catch:{ all -> 0x007a }
            java.lang.String r5 = r7.getParent()     // Catch:{ all -> 0x007a }
            r2.<init>(r5)     // Catch:{ all -> 0x007a }
            boolean r5 = r2.exists()     // Catch:{ all -> 0x007a }
            if (r5 != 0) goto L_0x00df
            r2.mkdirs()     // Catch:{ all -> 0x007a }
        L_0x00df:
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ all -> 0x007a }
            r5.<init>(r7)     // Catch:{ all -> 0x007a }
            java.io.BufferedOutputStream r2 = new java.io.BufferedOutputStream     // Catch:{ all -> 0x007a }
            r7 = 4096(0x1000, float:5.74E-42)
            r2.<init>(r5, r7)     // Catch:{ all -> 0x007a }
        L_0x00eb:
            r5 = 0
            r7 = 4096(0x1000, float:5.74E-42)
            int r5 = r4.read(r6, r5, r7)     // Catch:{ all -> 0x007a }
            r7 = -1
            if (r5 == r7) goto L_0x0109
            r7 = 0
            r2.write(r6, r7, r5)     // Catch:{ all -> 0x007a }
            int r0 = r0 + r5
            r5 = 104857600(0x6400000, float:3.6111186E-35)
            if (r0 <= r5) goto L_0x00eb
            com.uc.webview.export.cyclone.UCKnownException r0 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ all -> 0x007a }
            r1 = 2010(0x7da, float:2.817E-42)
            java.lang.String r2 = "Zip contents is too big."
            r0.<init>((int) r1, (java.lang.String) r2)     // Catch:{ all -> 0x007a }
            throw r0     // Catch:{ all -> 0x007a }
        L_0x0109:
            r2.flush()     // Catch:{ all -> 0x007a }
            r2.close()     // Catch:{ all -> 0x007a }
            r4.closeEntry()     // Catch:{ all -> 0x007a }
            int r1 = r1 + 1
            r5 = 1024(0x400, float:1.435E-42)
            if (r1 <= r5) goto L_0x004e
            com.uc.webview.export.cyclone.UCKnownException r0 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ all -> 0x007a }
            r1 = 2011(0x7db, float:2.818E-42)
            java.lang.String r2 = "Too many files to unzip."
            r0.<init>((int) r1, (java.lang.String) r2)     // Catch:{ all -> 0x007a }
            throw r0     // Catch:{ all -> 0x007a }
        L_0x0123:
            close(r4)     // Catch:{ Throwable -> 0x0028 }
            if (r2 != 0) goto L_0x0133
            com.uc.webview.export.cyclone.UCKnownException r0 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x0028 }
            r1 = 2002(0x7d2, float:2.805E-42)
            java.lang.String r2 = "No entry exists in zip file. Make sure specify a valid zip file url."
            r0.<init>((int) r1, (java.lang.String) r2)     // Catch:{ Throwable -> 0x0028 }
            throw r0     // Catch:{ Throwable -> 0x0028 }
        L_0x0133:
            java.lang.String r0 = "sdk_decz_s"
            r1 = 0
            a((java.lang.String) r0, (java.util.HashMap<java.lang.String, java.lang.String>) r1)     // Catch:{ Throwable -> 0x0028 }
        L_0x013a:
            java.lang.String r0 = "sdk_dec_s"
            a((java.lang.String) r0, (java.util.HashMap<java.lang.String, java.lang.String>) r3)
            return
        L_0x0141:
            com.uc.webview.export.cyclone.UCKnownException r1 = new com.uc.webview.export.cyclone.UCKnownException
            r2 = 2005(0x7d5, float:2.81E-42)
            r1.<init>((int) r2, (java.lang.Throwable) r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.UCCyclone.a(boolean, android.content.Context, java.lang.String, java.lang.String, java.io.FilenameFilter):void");
    }

    public static synchronized boolean decompressIfNeeded(Context context, boolean z, String str, long j, long j2, File file, File file2, FilenameFilter filenameFilter, boolean z2) throws UCKnownException {
        File file3;
        boolean z3;
        boolean z4;
        synchronized (UCCyclone.class) {
            if (a(str, j, j2, file2, false)) {
                z4 = false;
            } else if (!file.exists()) {
                throw new UCKnownException(1001, String.format("File [%s] not exists.", new Object[]{file.getAbsolutePath()}));
            } else {
                do {
                    file3 = new File(context.getCacheDir(), "temp_dec_" + String.valueOf(System.currentTimeMillis()));
                } while (file3.exists());
                expectCreateDirFile(file3);
                try {
                    System.currentTimeMillis();
                    if (z) {
                        z3 = true;
                    } else {
                        String absolutePath = file.getAbsolutePath();
                        z3 = absolutePath.endsWith(".7z") || absolutePath.contains("_7z_");
                    }
                    a(z3, context, file.getAbsolutePath(), file3.getAbsolutePath(), filenameFilter);
                    File c = c(str, j, j2, file2, false);
                    if (c.exists() || c.createNewFile()) {
                        File[] listFiles = file3.listFiles();
                        if (listFiles != null) {
                            for (File file4 : listFiles) {
                                File file5 = new File(file2, file4.getName());
                                if (file5.exists()) {
                                    if (file5.isDirectory()) {
                                        recursiveDelete(file5, false, (Object) null);
                                    } else if (!file5.delete()) {
                                        throw new UCKnownException(1004, String.format("File [%s] delete failed.", new Object[]{file5}));
                                    }
                                }
                                if (!file4.renameTo(file5)) {
                                    throw new UCKnownException(1005, String.format("File [%s] renameTo [%s] failed.", new Object[]{file4, file5}));
                                }
                            }
                            File b = b(str, j, j2, file2, false);
                            if (b.exists() || b.createNewFile()) {
                                File c2 = c(str, j, j2, file2, false);
                                if (!c2.exists() || c2.delete()) {
                                    recursiveDelete(file3, false, (Object) null);
                                    if (!z2 || file.delete()) {
                                        z4 = true;
                                    } else {
                                        throw new UCKnownException(1004, String.format("File [%s] delete failed.", new Object[]{file.getAbsolutePath()}));
                                    }
                                } else {
                                    throw new Exception("delete File return false");
                                }
                            } else {
                                throw new Exception("createNewFile return false");
                            }
                        } else {
                            throw new UCKnownException(2008, String.format("Zip [%s] decompress success but no items found.", new Object[]{file}));
                        }
                    } else {
                        throw new Exception("createNewFile return false");
                    }
                } catch (Throwable th) {
                    recursiveDelete(file3, false, (Object) null);
                    throw th;
                }
            }
        }
        return z4;
    }

    private static File b(String str, long j, long j2, File file, boolean z) {
        return new File(file, getDecompressSourceHash(str, j, j2, z));
    }

    private static File c(String str, long j, long j2, File file, boolean z) {
        return new File(file, getDecompressSourceHash(str, j, j2, z) + "_start");
    }

    public static void recursiveDelete(File file, boolean z, Object obj) {
        File[] listFiles;
        File[] fileArr;
        if (file.exists()) {
            if (obj != null) {
                if (obj instanceof File) {
                    fileArr = new File[]{(File) obj};
                } else {
                    fileArr = obj instanceof File[] ? (File[]) obj : null;
                }
                if (fileArr != null) {
                    int length = fileArr.length;
                    int i = 0;
                    while (i < length) {
                        File file2 = fileArr[i];
                        if (file2 == null || !file.getAbsolutePath().equals(file2.getAbsolutePath())) {
                            i++;
                        } else {
                            return;
                        }
                    }
                }
            }
            if (file.isDirectory() && (listFiles = file.listFiles()) != null) {
                for (File recursiveDelete : listFiles) {
                    recursiveDelete(recursiveDelete, false, obj);
                }
            }
            if (!z) {
                file.delete();
            }
        }
    }

    public static File optimizedFileFor(String str, String str2) {
        String name = new File(str).getName();
        if (!name.endsWith(MergeConstants.DEX_SUFFIX)) {
            int lastIndexOf = name.lastIndexOf(".");
            if (lastIndexOf < 0) {
                name = name + MergeConstants.DEX_SUFFIX;
            } else {
                StringBuilder sb = new StringBuilder(lastIndexOf + 4);
                sb.append(name, 0, lastIndexOf);
                sb.append(MergeConstants.DEX_SUFFIX);
                name = sb.toString();
            }
        }
        return new File(str2, name);
    }
}
