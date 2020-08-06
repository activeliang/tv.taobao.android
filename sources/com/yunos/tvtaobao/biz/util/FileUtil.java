package com.yunos.tvtaobao.biz.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import com.yunos.tv.core.degrade.ImageShowDegradeManager;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static Bitmap getBitmap(Context context, File fileName) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        if (ImageShowDegradeManager.getInstance().isImageDegrade()) {
            opt.inPreferredConfig = Bitmap.Config.ARGB_4444;
        } else {
            opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        }
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        return BitmapFactory.decodeFile(fileName.getPath(), opt);
    }

    public static void saveBitmap(Bitmap bm, String filePath) {
        if (bm != null && !bm.isRecycled()) {
            if (TextUtils.isEmpty(filePath)) {
                filePath = Environment.getExternalStorageDirectory().getPath() + "/temp.png";
            }
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static String getSdCardPath() {
        File sdDir = null;
        if (Environment.getExternalStorageState().equals("mounted")) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        if (sdDir != null) {
            return sdDir.toString();
        }
        return null;
    }

    public static String read(Context context, String file) {
        String data = "";
        try {
            FileInputStream fin = new FileInputStream(file);
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);
            String data2 = new String(buffer, "UTF-8");
            try {
                fin.close();
                return data2;
            } catch (Exception e) {
                data = data2;
                ZpLogger.d("FileUtil", "FileUtil.read:maybe no file can read!");
                return data;
            }
        } catch (Exception e2) {
            ZpLogger.d("FileUtil", "FileUtil.read:maybe no file can read!");
            return data;
        }
    }

    public static String readFromSdcard(Context context, String file) {
        String fullFilepath = file;
        String path = getSdCardPath();
        if (!TextUtils.isEmpty(path)) {
            fullFilepath = path + WVNativeCallbackUtil.SEPERATER + file;
        }
        return read(context, fullFilepath);
    }

    public static void write(Context context, String file, String content) {
        try {
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(content.getBytes("UTF-8"));
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writetoSdcard(Context context, String file, String content) {
        String fullFilepath = file;
        String path = getSdCardPath();
        if (!TextUtils.isEmpty(path)) {
            fullFilepath = path + WVNativeCallbackUtil.SEPERATER + file;
        }
        write(context, fullFilepath, content);
    }

    public static List<String> scan(File file) {
        List<String> items = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File path : files) {
                items.add(path.getPath());
            }
        } else {
            items.add(file.toString());
        }
        return items;
    }

    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        File[] files = file.listFiles();
        for (File deleteFile : files) {
            deleteFile(deleteFile);
        }
        return file.delete();
    }

    public static void createDir(String dir) {
        File destDir = new File(dir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0047 A[SYNTHETIC, Splitter:B:16:0x0047] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0083 A[SYNTHETIC, Splitter:B:34:0x0083] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x008f A[SYNTHETIC, Splitter:B:40:0x008f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getRawTextContent(android.content.Context r12, int r13) {
        /*
            r8 = 0
            r1 = 0
            r2 = 0
            r5 = 0
            android.content.res.Resources r9 = r12.getResources()     // Catch:{ NotFoundException -> 0x00a6, IOException -> 0x0063 }
            java.io.InputStream r1 = r9.openRawResource(r13)     // Catch:{ NotFoundException -> 0x00a6, IOException -> 0x0063 }
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ NotFoundException -> 0x00a6, IOException -> 0x0063 }
            r3.<init>(r1)     // Catch:{ NotFoundException -> 0x00a6, IOException -> 0x0063 }
            java.io.BufferedReader r6 = new java.io.BufferedReader     // Catch:{ NotFoundException -> 0x00a8, IOException -> 0x009f, all -> 0x0098 }
            r6.<init>(r3)     // Catch:{ NotFoundException -> 0x00a8, IOException -> 0x009f, all -> 0x0098 }
            java.lang.StringBuffer r7 = new java.lang.StringBuffer     // Catch:{ NotFoundException -> 0x0025, IOException -> 0x00a2, all -> 0x009b }
            r7.<init>()     // Catch:{ NotFoundException -> 0x0025, IOException -> 0x00a2, all -> 0x009b }
        L_0x001b:
            java.lang.String r4 = r6.readLine()     // Catch:{ NotFoundException -> 0x0025, IOException -> 0x00a2, all -> 0x009b }
            if (r4 == 0) goto L_0x004b
            r7.append(r4)     // Catch:{ NotFoundException -> 0x0025, IOException -> 0x00a2, all -> 0x009b }
            goto L_0x001b
        L_0x0025:
            r0 = move-exception
            r5 = r6
            r2 = r3
        L_0x0028:
            java.lang.String r9 = "getRawTextContent"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x008c }
            r10.<init>()     // Catch:{ all -> 0x008c }
            java.lang.String r11 = "NotFoundException rescource not found! resourceId"
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x008c }
            java.lang.StringBuilder r10 = r10.append(r13)     // Catch:{ all -> 0x008c }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x008c }
            com.zhiping.dev.android.logger.ZpLogger.e(r9, r10)     // Catch:{ all -> 0x008c }
            r0.printStackTrace()     // Catch:{ all -> 0x008c }
            if (r5 == 0) goto L_0x004a
            r5.close()     // Catch:{ IOException -> 0x005e }
        L_0x004a:
            return r8
        L_0x004b:
            java.lang.String r8 = r7.toString()     // Catch:{ NotFoundException -> 0x0025, IOException -> 0x00a2, all -> 0x009b }
            if (r6 == 0) goto L_0x0054
            r6.close()     // Catch:{ IOException -> 0x0057 }
        L_0x0054:
            r5 = r6
            r2 = r3
            goto L_0x004a
        L_0x0057:
            r0 = move-exception
            r0.printStackTrace()
            r5 = r6
            r2 = r3
            goto L_0x004a
        L_0x005e:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x004a
        L_0x0063:
            r0 = move-exception
        L_0x0064:
            java.lang.String r9 = "getRawTextContent"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x008c }
            r10.<init>()     // Catch:{ all -> 0x008c }
            java.lang.String r11 = "IOException rescource read error! resourceId"
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x008c }
            java.lang.StringBuilder r10 = r10.append(r13)     // Catch:{ all -> 0x008c }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x008c }
            com.zhiping.dev.android.logger.ZpLogger.e(r9, r10)     // Catch:{ all -> 0x008c }
            r0.printStackTrace()     // Catch:{ all -> 0x008c }
            if (r5 == 0) goto L_0x004a
            r5.close()     // Catch:{ IOException -> 0x0087 }
            goto L_0x004a
        L_0x0087:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x004a
        L_0x008c:
            r9 = move-exception
        L_0x008d:
            if (r5 == 0) goto L_0x0092
            r5.close()     // Catch:{ IOException -> 0x0093 }
        L_0x0092:
            throw r9
        L_0x0093:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0092
        L_0x0098:
            r9 = move-exception
            r2 = r3
            goto L_0x008d
        L_0x009b:
            r9 = move-exception
            r5 = r6
            r2 = r3
            goto L_0x008d
        L_0x009f:
            r0 = move-exception
            r2 = r3
            goto L_0x0064
        L_0x00a2:
            r0 = move-exception
            r5 = r6
            r2 = r3
            goto L_0x0064
        L_0x00a6:
            r0 = move-exception
            goto L_0x0028
        L_0x00a8:
            r0 = move-exception
            r2 = r3
            goto L_0x0028
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.biz.util.FileUtil.getRawTextContent(android.content.Context, int):java.lang.String");
    }

    public static String getApplicationPath(Context context) {
        Context appContext;
        File file;
        if (context == null || (appContext = context.getApplicationContext()) == null || (file = appContext.getFilesDir()) == null) {
            return null;
        }
        return file.getAbsolutePath();
    }

    public static boolean fileIsExists(String strFile) {
        try {
            if (!new File(strFile).exists()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
