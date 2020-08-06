package android.taobao.windvane.file;

import java.io.File;
import java.nio.ByteBuffer;

public class FileAccesser {
    public static boolean exists(String filePath) {
        if (filePath == null) {
            return false;
        }
        return new File(filePath).exists();
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x004a A[SYNTHETIC, Splitter:B:30:0x004a] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0053 A[SYNTHETIC, Splitter:B:35:0x0053] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0058 A[SYNTHETIC, Splitter:B:38:0x0058] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] read(java.io.File r8) {
        /*
            r5 = 0
            r3 = 0
            r1 = 0
            boolean r6 = r8.exists()     // Catch:{ Exception -> 0x0039 }
            if (r6 != 0) goto L_0x0014
            if (r3 == 0) goto L_0x000e
            r3.close()     // Catch:{ IOException -> 0x005c }
        L_0x000e:
            if (r1 == 0) goto L_0x0013
            r1.close()     // Catch:{ IOException -> 0x005e }
        L_0x0013:
            return r5
        L_0x0014:
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0039 }
            r4.<init>(r8)     // Catch:{ Exception -> 0x0039 }
            java.nio.channels.FileChannel r1 = r4.getChannel()     // Catch:{ Exception -> 0x006d, all -> 0x006a }
            long r6 = r1.size()     // Catch:{ Exception -> 0x006d, all -> 0x006a }
            int r6 = (int) r6     // Catch:{ Exception -> 0x006d, all -> 0x006a }
            java.nio.ByteBuffer r0 = java.nio.ByteBuffer.allocate(r6)     // Catch:{ Exception -> 0x006d, all -> 0x006a }
            r1.read(r0)     // Catch:{ Exception -> 0x006d, all -> 0x006a }
            byte[] r5 = r0.array()     // Catch:{ Exception -> 0x006d, all -> 0x006a }
            if (r4 == 0) goto L_0x0032
            r4.close()     // Catch:{ IOException -> 0x0060 }
        L_0x0032:
            if (r1 == 0) goto L_0x0037
            r1.close()     // Catch:{ IOException -> 0x0062 }
        L_0x0037:
            r3 = r4
            goto L_0x0013
        L_0x0039:
            r2 = move-exception
        L_0x003a:
            java.lang.String r6 = "FileAccesser"
            java.lang.String r7 = "read loacl file failed"
            android.taobao.windvane.util.TaoLog.w(r6, r7)     // Catch:{ all -> 0x0050 }
            if (r3 == 0) goto L_0x0048
            r3.close()     // Catch:{ IOException -> 0x0064 }
        L_0x0048:
            if (r1 == 0) goto L_0x0013
            r1.close()     // Catch:{ IOException -> 0x004e }
            goto L_0x0013
        L_0x004e:
            r6 = move-exception
            goto L_0x0013
        L_0x0050:
            r5 = move-exception
        L_0x0051:
            if (r3 == 0) goto L_0x0056
            r3.close()     // Catch:{ IOException -> 0x0066 }
        L_0x0056:
            if (r1 == 0) goto L_0x005b
            r1.close()     // Catch:{ IOException -> 0x0068 }
        L_0x005b:
            throw r5
        L_0x005c:
            r6 = move-exception
            goto L_0x000e
        L_0x005e:
            r6 = move-exception
            goto L_0x0013
        L_0x0060:
            r6 = move-exception
            goto L_0x0032
        L_0x0062:
            r6 = move-exception
            goto L_0x0037
        L_0x0064:
            r6 = move-exception
            goto L_0x0048
        L_0x0066:
            r6 = move-exception
            goto L_0x0056
        L_0x0068:
            r6 = move-exception
            goto L_0x005b
        L_0x006a:
            r5 = move-exception
            r3 = r4
            goto L_0x0051
        L_0x006d:
            r2 = move-exception
            r3 = r4
            goto L_0x003a
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.file.FileAccesser.read(java.io.File):byte[]");
    }

    public static byte[] read(String filePath) {
        if (filePath == null) {
            return null;
        }
        return read(new File(filePath));
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x004e A[Catch:{ all -> 0x0057 }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x005a A[SYNTHETIC, Splitter:B:29:0x005a] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x005f A[SYNTHETIC, Splitter:B:32:0x005f] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0063  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean write(java.io.File r8, java.nio.ByteBuffer r9) throws android.taobao.windvane.file.NotEnoughSpace {
        /*
            r5 = 1
            r6 = 0
            r2 = 0
            r0 = 0
            boolean r7 = r8.exists()     // Catch:{ Exception -> 0x003e }
            if (r7 != 0) goto L_0x0014
            java.io.File r7 = r8.getParentFile()     // Catch:{ Exception -> 0x003e }
            r7.mkdirs()     // Catch:{ Exception -> 0x003e }
            r8.createNewFile()     // Catch:{ Exception -> 0x003e }
        L_0x0014:
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x003e }
            r3.<init>(r8)     // Catch:{ Exception -> 0x003e }
            java.nio.channels.FileChannel r0 = r3.getChannel()     // Catch:{ Exception -> 0x008e, all -> 0x008b }
            r7 = 0
            r9.position(r7)     // Catch:{ Exception -> 0x008e, all -> 0x008b }
            r0.write(r9)     // Catch:{ Exception -> 0x008e, all -> 0x008b }
            r7 = 1
            r0.force(r7)     // Catch:{ Exception -> 0x008e, all -> 0x008b }
            if (r3 == 0) goto L_0x002d
            r3.close()     // Catch:{ IOException -> 0x0034 }
        L_0x002d:
            if (r0 == 0) goto L_0x0032
            r0.close()     // Catch:{ IOException -> 0x0039 }
        L_0x0032:
            r2 = r3
        L_0x0033:
            return r5
        L_0x0034:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x002d
        L_0x0039:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0032
        L_0x003e:
            r1 = move-exception
        L_0x003f:
            java.lang.String r4 = r1.getMessage()     // Catch:{ all -> 0x0057 }
            if (r4 == 0) goto L_0x0063
            java.lang.String r5 = "ENOSPC"
            boolean r5 = r4.contains(r5)     // Catch:{ all -> 0x0057 }
            if (r5 == 0) goto L_0x0063
            android.taobao.windvane.file.NotEnoughSpace r5 = new android.taobao.windvane.file.NotEnoughSpace     // Catch:{ all -> 0x0057 }
            java.lang.String r6 = "not enouth space in flash"
            r5.<init>(r6)     // Catch:{ all -> 0x0057 }
            throw r5     // Catch:{ all -> 0x0057 }
        L_0x0057:
            r5 = move-exception
        L_0x0058:
            if (r2 == 0) goto L_0x005d
            r2.close()     // Catch:{ IOException -> 0x0081 }
        L_0x005d:
            if (r0 == 0) goto L_0x0062
            r0.close()     // Catch:{ IOException -> 0x0086 }
        L_0x0062:
            throw r5
        L_0x0063:
            if (r8 == 0) goto L_0x0068
            r8.delete()     // Catch:{ all -> 0x0057 }
        L_0x0068:
            r1.printStackTrace()     // Catch:{ all -> 0x0057 }
            if (r2 == 0) goto L_0x0070
            r2.close()     // Catch:{ IOException -> 0x0077 }
        L_0x0070:
            if (r0 == 0) goto L_0x0075
            r0.close()     // Catch:{ IOException -> 0x007c }
        L_0x0075:
            r5 = r6
            goto L_0x0033
        L_0x0077:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0070
        L_0x007c:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0075
        L_0x0081:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x005d
        L_0x0086:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0062
        L_0x008b:
            r5 = move-exception
            r2 = r3
            goto L_0x0058
        L_0x008e:
            r1 = move-exception
            r2 = r3
            goto L_0x003f
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.file.FileAccesser.write(java.io.File, java.nio.ByteBuffer):boolean");
    }

    public static boolean write(String filePath, ByteBuffer data) throws NotEnoughSpace {
        if (filePath == null) {
            return false;
        }
        return write(new File(filePath), data);
    }

    public static boolean deleteFile(String filePath) {
        if (filePath == null) {
            return false;
        }
        return deleteFile(new File(filePath), true);
    }

    public static boolean deleteFile(File file) {
        return deleteFile(file, true);
    }

    public static boolean deleteFile(File file, boolean isSelf) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                try {
                    for (File f : file.listFiles()) {
                        if (f.isDirectory()) {
                            deleteFile(f, true);
                        } else {
                            try {
                                f.delete();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (NullPointerException e2) {
                    e2.printStackTrace();
                }
            }
            if (isSelf) {
                try {
                    return file.delete();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
        return false;
    }
}
