package android.taobao.windvane.jsbridge.utils;

import android.net.Uri;
import android.text.TextUtils;
import anet.channel.util.HttpConstant;

public class WVUtils {
    public static final String LOCAL_CAPTURE_IMAGE = "//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t=";
    public static final String URL_DATA_CHAR = "?";
    public static final String URL_SEPARATOR = "//";

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0069  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0070  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long saveBitmapToCache(android.graphics.Bitmap r14) throws java.io.IOException {
        /*
            r13 = 1
            r12 = 0
            long r2 = java.lang.System.currentTimeMillis()
            android.taobao.windvane.cache.WVFileInfo r5 = new android.taobao.windvane.cache.WVFileInfo
            r5.<init>()
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t="
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r2)
            java.lang.String r10 = r10.toString()
            java.lang.String r10 = android.taobao.windvane.util.DigestUtils.md5ToHex((java.lang.String) r10)
            r5.fileName = r10
            java.lang.String r10 = "image/jpeg"
            r5.mimeType = r10
            long r6 = java.lang.System.currentTimeMillis()
            r10 = 2592000000(0x9a7ec800, double:1.280618154E-314)
            long r10 = r10 + r6
            r5.expireTime = r10
            byte[] r0 = new byte[r13]
            r0[r12] = r12
            android.taobao.windvane.cache.WVCacheManager r10 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            r10.writeToFile(r5, r0)
            java.io.File r4 = new java.io.File
            android.taobao.windvane.cache.WVCacheManager r10 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            java.lang.String r10 = r10.getCacheDir(r13)
            java.lang.String r11 = r5.fileName
            r4.<init>(r10, r11)
            r8 = 0
            java.io.FileOutputStream r9 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0064, all -> 0x006d }
            r9.<init>(r4)     // Catch:{ Exception -> 0x0064, all -> 0x006d }
            android.graphics.Bitmap$CompressFormat r10 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ Exception -> 0x0077, all -> 0x0074 }
            r11 = 100
            r14.compress(r10, r11, r9)     // Catch:{ Exception -> 0x0077, all -> 0x0074 }
            if (r9 == 0) goto L_0x0062
            r9.close()
        L_0x0062:
            r8 = r9
        L_0x0063:
            return r2
        L_0x0064:
            r1 = move-exception
        L_0x0065:
            r2 = 0
            if (r8 == 0) goto L_0x0063
            r8.close()
            goto L_0x0063
        L_0x006d:
            r10 = move-exception
        L_0x006e:
            if (r8 == 0) goto L_0x0073
            r8.close()
        L_0x0073:
            throw r10
        L_0x0074:
            r10 = move-exception
            r8 = r9
            goto L_0x006e
        L_0x0077:
            r1 = move-exception
            r8 = r9
            goto L_0x0065
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.utils.WVUtils.saveBitmapToCache(android.graphics.Bitmap):long");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x002f A[SYNTHETIC, Splitter:B:15:0x002f] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003b A[SYNTHETIC, Splitter:B:20:0x003b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String bitmapToBase64(android.graphics.Bitmap r7) {
        /*
            r4 = 0
            r0 = 0
            if (r7 == 0) goto L_0x0020
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0029 }
            r1.<init>()     // Catch:{ IOException -> 0x0029 }
            android.graphics.Bitmap$CompressFormat r5 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ IOException -> 0x0047, all -> 0x0044 }
            r6 = 100
            r7.compress(r5, r6, r1)     // Catch:{ IOException -> 0x0047, all -> 0x0044 }
            r1.flush()     // Catch:{ IOException -> 0x0047, all -> 0x0044 }
            r1.close()     // Catch:{ IOException -> 0x0047, all -> 0x0044 }
            byte[] r2 = r1.toByteArray()     // Catch:{ IOException -> 0x0047, all -> 0x0044 }
            r5 = 0
            java.lang.String r4 = android.util.Base64.encodeToString(r2, r5)     // Catch:{ IOException -> 0x0047, all -> 0x0044 }
            r0 = r1
        L_0x0020:
            if (r0 == 0) goto L_0x0028
            r0.flush()     // Catch:{ Exception -> 0x004a }
            r0.close()     // Catch:{ Exception -> 0x004a }
        L_0x0028:
            return r4
        L_0x0029:
            r3 = move-exception
        L_0x002a:
            r3.printStackTrace()     // Catch:{ all -> 0x0038 }
            if (r0 == 0) goto L_0x0028
            r0.flush()     // Catch:{ Exception -> 0x0036 }
            r0.close()     // Catch:{ Exception -> 0x0036 }
            goto L_0x0028
        L_0x0036:
            r5 = move-exception
            goto L_0x0028
        L_0x0038:
            r5 = move-exception
        L_0x0039:
            if (r0 == 0) goto L_0x0041
            r0.flush()     // Catch:{ Exception -> 0x0042 }
            r0.close()     // Catch:{ Exception -> 0x0042 }
        L_0x0041:
            throw r5
        L_0x0042:
            r6 = move-exception
            goto L_0x0041
        L_0x0044:
            r5 = move-exception
            r0 = r1
            goto L_0x0039
        L_0x0047:
            r3 = move-exception
            r0 = r1
            goto L_0x002a
        L_0x004a:
            r5 = move-exception
            goto L_0x0028
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.utils.WVUtils.bitmapToBase64(android.graphics.Bitmap):java.lang.String");
    }

    public static String getVirtualPath(Long time) {
        return "//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t=" + time;
    }

    public static String getHost(String orgUrl) {
        String url;
        if (TextUtils.isEmpty(orgUrl)) {
            return null;
        }
        int index = orgUrl.indexOf(URL_DATA_CHAR);
        if (index != -1) {
            url = orgUrl.substring(0, index);
        } else {
            url = orgUrl;
        }
        if (url.startsWith(URL_SEPARATOR)) {
            url = "http:" + url;
        } else if (!url.contains(HttpConstant.SCHEME_SPLIT)) {
            url = "http://" + url;
        }
        Uri uri = Uri.parse(url);
        if (uri != null) {
            return uri.getHost();
        }
        return null;
    }
}
