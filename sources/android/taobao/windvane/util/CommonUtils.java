package android.taobao.windvane.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import java.io.File;
import java.io.FileFilter;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;
import mtopsdk.common.util.SymbolExpUtil;

public class CommonUtils {
    private static final String TAG = "CommonUtils";

    public static int compareVer(String ver1, String ver2) {
        int maxLen;
        if (ver1 == null) {
            ver1 = "0";
        }
        if (ver2 == null) {
            ver2 = "0";
        }
        String[] v1 = ver1.split("\\.");
        String[] v2 = ver2.split("\\.");
        int len1 = v1.length;
        int len2 = v2.length;
        if (len1 > len2) {
            maxLen = len1;
        } else {
            maxLen = len2;
        }
        for (int i = 0; i < maxLen; i++) {
            if (i >= len1 || i >= len2) {
                int n = len1 > len2 ? toInt(v1[i]) : toInt(v2[i]) * -1;
                if (n != 0) {
                    return n;
                }
            } else {
                int n1 = toInt(v1[i]);
                int n2 = toInt(v2[i]);
                if (n1 != n2) {
                    return n1 - n2;
                }
            }
        }
        return 0;
    }

    public static int toInt(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String parseCharset(String contentType) {
        int index;
        if (TextUtils.isEmpty(contentType) || (index = contentType.indexOf(WVConstants.CHARSET)) == -1 || contentType.indexOf("=", index) == -1) {
            return "";
        }
        String contentType2 = contentType.substring(contentType.indexOf("=", index) + 1);
        int index2 = contentType2.indexOf(SymbolExpUtil.SYMBOL_SEMICOLON);
        if (index2 != -1) {
            contentType2 = contentType2.substring(0, index2).trim();
        }
        return contentType2.trim();
    }

    public static String parseMimeType(String contentType) {
        if (TextUtils.isEmpty(contentType)) {
            return "";
        }
        int index = contentType.indexOf(SymbolExpUtil.SYMBOL_SEMICOLON);
        if (index == -1) {
            return contentType.trim();
        }
        return contentType.substring(0, index).trim();
    }

    public static long parseMaxAge(String cacheControl) {
        if (!TextUtils.isEmpty(cacheControl) && cacheControl.indexOf("max-age=") != -1) {
            StringBuilder sb = new StringBuilder();
            String maxAge = cacheControl.substring(8);
            int i = 0;
            while (i < maxAge.length() && Character.isDigit(maxAge.charAt(i))) {
                sb.append(maxAge.charAt(i));
                i++;
            }
            try {
                return Long.parseLong(sb.toString()) * 1000;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static long parseDate(String dateString) {
        if (!TextUtils.isEmpty(dateString)) {
            try {
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
                DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
                return DATE_FORMAT.parse(dateString.trim()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static boolean isImage(String mimeType) {
        return !TextUtils.isEmpty(mimeType) && mimeType.toLowerCase().startsWith("image");
    }

    public static boolean isHtml(String mimeType) {
        return !TextUtils.isEmpty(mimeType) && mimeType.equalsIgnoreCase("text/html");
    }

    public static String formatDate(long milliseconds) {
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH).format(new Date(milliseconds));
    }

    public static boolean isAppInstalled(Context ctx, String pkg) {
        if (ctx == null || TextUtils.isEmpty(pkg)) {
            return false;
        }
        try {
            PackageManager pkgMgr = ctx.getPackageManager();
            if (pkgMgr == null || pkgMgr.getPackageInfo(pkg, 0) == null) {
                return false;
            }
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isLowendPhone() {
        int processor = getNumCores();
        int ram = getTotalRAM();
        TaoLog.d("HomePageNetwork", "processorCore = " + processor + " ram = " + ram + " MB");
        if (processor != 1 || ram >= 800) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0019  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0050 A[SYNTHETIC, Splitter:B:25:0x0050] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getTotalRAM() {
        /*
            r4 = 0
            r2 = 0
            java.io.RandomAccessFile r5 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x003e }
            java.lang.String r6 = "/proc/meminfo"
            java.lang.String r7 = "r"
            r5.<init>(r6, r7)     // Catch:{ IOException -> 0x003e }
            java.lang.String r2 = r5.readLine()     // Catch:{ IOException -> 0x0063, all -> 0x0060 }
            if (r5 == 0) goto L_0x0066
            r5.close()     // Catch:{ IOException -> 0x0038 }
            r4 = r5
        L_0x0017:
            if (r2 == 0) goto L_0x005d
            java.lang.String r6 = "kB"
            java.lang.String r7 = ""
            java.lang.String r6 = r2.replace(r6, r7)
            java.lang.String r7 = "MemTotal:"
            java.lang.String r8 = ""
            java.lang.String r3 = r6.replace(r7, r8)
            java.lang.String r6 = r3.trim()     // Catch:{ NumberFormatException -> 0x0059 }
            int r6 = java.lang.Integer.parseInt(r6)     // Catch:{ NumberFormatException -> 0x0059 }
            int r6 = r6 / 1000
        L_0x0037:
            return r6
        L_0x0038:
            r0 = move-exception
            r0.printStackTrace()
            r4 = r5
            goto L_0x0017
        L_0x003e:
            r1 = move-exception
        L_0x003f:
            r1.printStackTrace()     // Catch:{ all -> 0x004d }
            if (r4 == 0) goto L_0x0017
            r4.close()     // Catch:{ IOException -> 0x0048 }
            goto L_0x0017
        L_0x0048:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0017
        L_0x004d:
            r6 = move-exception
        L_0x004e:
            if (r4 == 0) goto L_0x0053
            r4.close()     // Catch:{ IOException -> 0x0054 }
        L_0x0053:
            throw r6
        L_0x0054:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0053
        L_0x0059:
            r1 = move-exception
            r1.printStackTrace()
        L_0x005d:
            r6 = 1024(0x400, float:1.435E-42)
            goto L_0x0037
        L_0x0060:
            r6 = move-exception
            r4 = r5
            goto L_0x004e
        L_0x0063:
            r1 = move-exception
            r4 = r5
            goto L_0x003f
        L_0x0066:
            r4 = r5
            goto L_0x0017
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.util.CommonUtils.getTotalRAM():int");
    }

    public static int getNumCores() {
        try {
            File[] files = new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                        return true;
                    }
                    return false;
                }
            });
            TaoLog.d("HomePageNetwork", "CPU Count: " + files.length);
            return files.length;
        } catch (Exception e) {
            TaoLog.d("HomePageNetwork", "CPU Count: Failed.");
            e.printStackTrace();
            return 1;
        }
    }

    public static boolean hasSDCardMounted() {
        String state = Environment.getExternalStorageState();
        if (state == null || !state.equals("mounted")) {
            return false;
        }
        return true;
    }

    public static File getExternalCacheDir(Context context) {
        if (Build.VERSION.SDK_INT >= 8) {
            try {
                File path = context.getExternalCacheDir();
                if (path != null) {
                    if (path.exists()) {
                        return path;
                    }
                    path.mkdirs();
                    return path;
                }
            } catch (Exception e) {
            }
        }
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + ("/Android/data/" + context.getPackageName() + "/cache/"));
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return dir;
    }

    public static boolean canWriteFile(String strContent, String strFilePath) {
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
            if (!file.exists()) {
                return false;
            }
            file.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            File file2 = new File(strFilePath);
            if (!file2.exists()) {
                return false;
            }
            file2.delete();
            return false;
        }
    }

    public static Bitmap base64ToBitmap(String base64String) {
        if (TextUtils.isEmpty(base64String)) {
            return null;
        }
        try {
            byte[] decodedString = Base64.decode(base64String.replace(' ', '+'), 0);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isPicture(String url) {
        if (url == null || -1 == url.lastIndexOf(".")) {
            return false;
        }
        String tmpName = url.substring(url.lastIndexOf(".") + 1, url.length());
        String[] imgeArray = {"png", "jpeg", "jpg", "webp"};
        for (String equals : imgeArray) {
            if (equals.equals(tmpName)) {
                return true;
            }
        }
        return false;
    }
}
