package com.yunos.tv.blitz.ucache;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import com.yunos.tv.blitz.utils.NetworkUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class BlitzUCacheBridge implements NetworkUtil.BzNetworkChangeListener {
    private static final int E_OPER_GET_SHPRE = 1;
    private static final int E_OPER_SET_SHPRE = 2;
    /* access modifiers changed from: private */
    public static final String TAG = BlitzUCacheBridge.class.getSimpleName();
    private static BlitzUCacheBridge sBlitzUCacheBridgeInst = null;
    private Context mContext;

    private native boolean nativeLoadUCacheData(String str);

    private native boolean registerNativeUCacheCallBack();

    public static BlitzUCacheBridge getInstance(Context context) {
        if (sBlitzUCacheBridgeInst == null) {
            sBlitzUCacheBridgeInst = new BlitzUCacheBridge(context);
        }
        return sBlitzUCacheBridgeInst;
    }

    public BlitzUCacheBridge(Context context) {
        this.mContext = context;
        registerNativeUCacheCallBack();
        NetworkUtil.getInstance().addNetworkChangeListner(this);
    }

    public void updateBlitzUCache() {
        Log.i(TAG, "updateBlitzUCache...");
        try {
            JSONObject appInfoJson = new JSONObject();
            PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0);
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            appInfoJson.put("app_package_name", this.mContext.getPackageName());
            appInfoJson.put("app_version_name", versionName);
            appInfoJson.put("app_version_code", versionCode);
            appInfoJson.put("app_cache_dir", this.mContext.getFilesDir().getAbsolutePath() + "/ucache/");
            appInfoJson.put("app_cache_dir_init", "file:///android_asset/ucache/");
            checkValidBlockSize();
            Log.d(TAG, "updateBlitzUCache, appInfoJson = " + appInfoJson.toString());
            onLoadUCacheData(appInfoJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeCopyPreUcache(final Context context, final String ucacheDir) {
        new Thread(new Runnable() {
            public void run() {
                boolean unused = BlitzUCacheBridge.this.initPreCacheConfig(context, ucacheDir);
            }
        }).start();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x011b A[SYNTHETIC, Splitter:B:30:0x011b] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x012d A[SYNTHETIC, Splitter:B:39:0x012d] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x013c A[SYNTHETIC, Splitter:B:46:0x013c] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:27:0x0116=Splitter:B:27:0x0116, B:36:0x0128=Splitter:B:36:0x0128} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean initPreCacheConfig(android.content.Context r22, java.lang.String r23) {
        /*
            r21 = this;
            java.lang.String r19 = "ucache_flag"
            r20 = 0
            r0 = r22
            r1 = r19
            r2 = r20
            android.content.SharedPreferences r15 = r0.getSharedPreferences(r1, r2)
            java.lang.String r19 = "ucache_init"
            r20 = 0
            r0 = r19
            r1 = r20
            boolean r9 = r15.getBoolean(r0, r1)
            if (r9 == 0) goto L_0x0021
            r19 = 0
        L_0x0020:
            return r19
        L_0x0021:
            java.lang.StringBuilder r19 = new java.lang.StringBuilder
            r19.<init>()
            java.lang.String r20 = "ucache"
            java.lang.StringBuilder r19 = r19.append(r20)
            java.lang.String r20 = java.io.File.separator
            java.lang.StringBuilder r19 = r19.append(r20)
            java.lang.String r20 = "conf.json"
            java.lang.StringBuilder r19 = r19.append(r20)
            java.lang.String r5 = r19.toString()
            r13 = 0
            java.io.BufferedReader r14 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0115, JSONException -> 0x0127 }
            java.io.InputStreamReader r19 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0115, JSONException -> 0x0127 }
            android.content.res.AssetManager r20 = r22.getAssets()     // Catch:{ IOException -> 0x0115, JSONException -> 0x0127 }
            r0 = r20
            java.io.InputStream r20 = r0.open(r5)     // Catch:{ IOException -> 0x0115, JSONException -> 0x0127 }
            r19.<init>(r20)     // Catch:{ IOException -> 0x0115, JSONException -> 0x0127 }
            r0 = r19
            r14.<init>(r0)     // Catch:{ IOException -> 0x0115, JSONException -> 0x0127 }
            java.lang.String r4 = ""
        L_0x0058:
            java.lang.String r16 = r14.readLine()     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            if (r16 == 0) goto L_0x0076
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            r19.<init>()     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            r0 = r19
            java.lang.StringBuilder r19 = r0.append(r4)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            r0 = r19
            r1 = r16
            java.lang.StringBuilder r19 = r0.append(r1)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            java.lang.String r4 = r19.toString()     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            goto L_0x0058
        L_0x0076:
            java.lang.String r19 = ""
            r0 = r19
            boolean r19 = r4.equalsIgnoreCase(r0)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            if (r19 != 0) goto L_0x0103
            org.json.JSONArray r10 = new org.json.JSONArray     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            r10.<init>(r4)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            r8 = 0
        L_0x0087:
            int r19 = r10.length()     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            r0 = r19
            if (r8 >= r0) goto L_0x0103
            java.lang.Object r11 = r10.get(r8)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            org.json.JSONObject r11 = (org.json.JSONObject) r11     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            java.lang.String r19 = "module"
            r0 = r19
            java.lang.String r12 = r11.getString(r0)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            java.lang.String r19 = "version"
            r0 = r19
            java.lang.String r17 = r11.getString(r0)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            java.lang.String r19 = "src"
            r0 = r19
            java.lang.String r7 = r11.getString(r0)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            java.lang.String r19 = "standalone"
            r0 = r19
            boolean r19 = r12.equalsIgnoreCase(r0)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            if (r19 == 0) goto L_0x010c
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            r19.<init>()     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            r0 = r19
            r1 = r23
            java.lang.StringBuilder r19 = r0.append(r1)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            java.lang.String r20 = "standalone/"
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            r0 = r19
            r1 = r17
            java.lang.StringBuilder r19 = r0.append(r1)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            java.lang.String r20 = "/"
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            java.lang.String r18 = r19.toString()     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            r19.<init>()     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            java.lang.String r20 = "ucache"
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            java.lang.String r20 = java.io.File.separator     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            java.lang.String r19 = r19.toString()     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
            r0 = r21
            r1 = r22
            r2 = r19
            r3 = r18
            r0.copyZipToDataFolder(r1, r2, r7, r3)     // Catch:{ IOException -> 0x014e, JSONException -> 0x014b, all -> 0x0148 }
        L_0x0103:
            if (r14 == 0) goto L_0x0108
            r14.close()     // Catch:{ IOException -> 0x0110 }
        L_0x0108:
            r19 = 1
            goto L_0x0020
        L_0x010c:
            int r8 = r8 + 1
            goto L_0x0087
        L_0x0110:
            r6 = move-exception
            r6.printStackTrace()
            goto L_0x0108
        L_0x0115:
            r6 = move-exception
        L_0x0116:
            r6.printStackTrace()     // Catch:{ all -> 0x0139 }
            if (r13 == 0) goto L_0x011e
            r13.close()     // Catch:{ IOException -> 0x0122 }
        L_0x011e:
            r19 = 1
            goto L_0x0020
        L_0x0122:
            r6 = move-exception
            r6.printStackTrace()
            goto L_0x011e
        L_0x0127:
            r6 = move-exception
        L_0x0128:
            r6.printStackTrace()     // Catch:{ all -> 0x0139 }
            if (r13 == 0) goto L_0x0130
            r13.close()     // Catch:{ IOException -> 0x0134 }
        L_0x0130:
            r19 = 1
            goto L_0x0020
        L_0x0134:
            r6 = move-exception
            r6.printStackTrace()
            goto L_0x0130
        L_0x0139:
            r19 = move-exception
        L_0x013a:
            if (r13 == 0) goto L_0x013f
            r13.close()     // Catch:{ IOException -> 0x0143 }
        L_0x013f:
            r19 = 1
            goto L_0x0020
        L_0x0143:
            r6 = move-exception
            r6.printStackTrace()
            goto L_0x013f
        L_0x0148:
            r19 = move-exception
            r13 = r14
            goto L_0x013a
        L_0x014b:
            r6 = move-exception
            r13 = r14
            goto L_0x0128
        L_0x014e:
            r6 = move-exception
            r13 = r14
            goto L_0x0116
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.blitz.ucache.BlitzUCacheBridge.initPreCacheConfig(android.content.Context, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0089 A[SYNTHETIC, Splitter:B:31:0x0089] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x008e A[Catch:{ IOException -> 0x00a7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x009b A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ac A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00b1 A[SYNTHETIC, Splitter:B:48:0x00b1] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00b6 A[Catch:{ IOException -> 0x00c6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00c3 A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00cb A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean copyZipToDataFolder(android.content.Context r15, java.lang.String r16, java.lang.String r17, java.lang.String r18) {
        /*
            r14 = this;
            r11 = 0
            r5 = 0
            r9 = 0
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            r0 = r18
            java.lang.StringBuilder r13 = r13.append(r0)
            r0 = r17
            java.lang.StringBuilder r13 = r13.append(r0)
            java.lang.String r8 = r13.toString()
            java.io.File r3 = new java.io.File     // Catch:{ IOException -> 0x00d1 }
            r0 = r18
            r3.<init>(r0)     // Catch:{ IOException -> 0x00d1 }
            boolean r13 = r3.exists()     // Catch:{ IOException -> 0x00d1 }
            if (r13 != 0) goto L_0x003f
            r3.mkdirs()     // Catch:{ IOException -> 0x00d1 }
            r11 = 0
        L_0x0029:
            if (r5 == 0) goto L_0x002e
            r5.close()     // Catch:{ IOException -> 0x00a0 }
        L_0x002e:
            if (r9 == 0) goto L_0x0033
            r9.close()     // Catch:{ IOException -> 0x00a0 }
        L_0x0033:
            if (r11 == 0) goto L_0x00a5
            r0 = r18
            boolean r13 = r14.unZipUcacheFile(r15, r0, r8)
            if (r13 == 0) goto L_0x00a5
            r13 = 1
        L_0x003e:
            return r13
        L_0x003f:
            java.io.File r7 = new java.io.File     // Catch:{ IOException -> 0x00d1 }
            r0 = r18
            r1 = r17
            r7.<init>(r0, r1)     // Catch:{ IOException -> 0x00d1 }
            boolean r13 = r7.exists()     // Catch:{ IOException -> 0x00d1 }
            if (r13 == 0) goto L_0x0051
            r7.delete()     // Catch:{ IOException -> 0x00d1 }
        L_0x0051:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00d1 }
            r13.<init>()     // Catch:{ IOException -> 0x00d1 }
            r0 = r16
            java.lang.StringBuilder r13 = r13.append(r0)     // Catch:{ IOException -> 0x00d1 }
            r0 = r17
            java.lang.StringBuilder r13 = r13.append(r0)     // Catch:{ IOException -> 0x00d1 }
            java.lang.String r12 = r13.toString()     // Catch:{ IOException -> 0x00d1 }
            android.content.res.AssetManager r13 = r15.getAssets()     // Catch:{ IOException -> 0x00d1 }
            java.io.InputStream r5 = r13.open(r12)     // Catch:{ IOException -> 0x00d1 }
            java.io.FileOutputStream r10 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x00d1 }
            r10.<init>(r7)     // Catch:{ IOException -> 0x00d1 }
            r13 = 2048(0x800, float:2.87E-42)
            byte[] r2 = new byte[r13]     // Catch:{ IOException -> 0x0082, all -> 0x00ce }
        L_0x0077:
            int r6 = r5.read(r2)     // Catch:{ IOException -> 0x0082, all -> 0x00ce }
            if (r6 <= 0) goto L_0x009d
            r13 = 0
            r10.write(r2, r13, r6)     // Catch:{ IOException -> 0x0082, all -> 0x00ce }
            goto L_0x0077
        L_0x0082:
            r4 = move-exception
            r9 = r10
        L_0x0084:
            r4.printStackTrace()     // Catch:{ all -> 0x00ae }
            if (r5 == 0) goto L_0x008c
            r5.close()     // Catch:{ IOException -> 0x00a7 }
        L_0x008c:
            if (r9 == 0) goto L_0x0091
            r9.close()     // Catch:{ IOException -> 0x00a7 }
        L_0x0091:
            if (r11 == 0) goto L_0x00ac
            r0 = r18
            boolean r13 = r14.unZipUcacheFile(r15, r0, r8)
            if (r13 == 0) goto L_0x00ac
            r13 = 1
            goto L_0x003e
        L_0x009d:
            r11 = 1
            r9 = r10
            goto L_0x0029
        L_0x00a0:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0033
        L_0x00a5:
            r13 = 0
            goto L_0x003e
        L_0x00a7:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0091
        L_0x00ac:
            r13 = 0
            goto L_0x003e
        L_0x00ae:
            r13 = move-exception
        L_0x00af:
            if (r5 == 0) goto L_0x00b4
            r5.close()     // Catch:{ IOException -> 0x00c6 }
        L_0x00b4:
            if (r9 == 0) goto L_0x00b9
            r9.close()     // Catch:{ IOException -> 0x00c6 }
        L_0x00b9:
            if (r11 == 0) goto L_0x00cb
            r0 = r18
            boolean r13 = r14.unZipUcacheFile(r15, r0, r8)
            if (r13 == 0) goto L_0x00cb
            r13 = 1
            goto L_0x003e
        L_0x00c6:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x00b9
        L_0x00cb:
            r13 = 0
            goto L_0x003e
        L_0x00ce:
            r13 = move-exception
            r9 = r10
            goto L_0x00af
        L_0x00d1:
            r4 = move-exception
            goto L_0x0084
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.blitz.ucache.BlitzUCacheBridge.copyZipToDataFolder(android.content.Context, java.lang.String, java.lang.String, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x00b2 A[SYNTHETIC, Splitter:B:36:0x00b2] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00b7 A[Catch:{ IOException -> 0x00f5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00bc A[Catch:{ IOException -> 0x00f5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00c1  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00fd A[SYNTHETIC, Splitter:B:49:0x00fd] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0102 A[Catch:{ IOException -> 0x0140 }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0107 A[Catch:{ IOException -> 0x0140 }] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x010c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean unZipUcacheFile(android.content.Context r19, java.lang.String r20, java.lang.String r21) {
        /*
            r18 = this;
            r11 = 0
            java.io.File r4 = new java.io.File
            r0 = r21
            r4.<init>(r0)
            boolean r16 = r4.exists()
            if (r16 != 0) goto L_0x0011
            r16 = r11
        L_0x0010:
            return r16
        L_0x0011:
            r7 = 0
            r14 = 0
            r9 = 0
            java.io.FileInputStream r8 = new java.io.FileInputStream     // Catch:{ IOException -> 0x00ac }
            r0 = r21
            r8.<init>(r0)     // Catch:{ IOException -> 0x00ac }
            java.util.zip.ZipInputStream r15 = new java.util.zip.ZipInputStream     // Catch:{ IOException -> 0x0151, all -> 0x0145 }
            r15.<init>(r8)     // Catch:{ IOException -> 0x0151, all -> 0x0145 }
            r10 = r9
        L_0x0021:
            java.util.zip.ZipEntry r13 = r15.getNextEntry()     // Catch:{ IOException -> 0x0155, all -> 0x0148 }
            if (r13 == 0) goto L_0x0061
            java.io.FileOutputStream r9 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x0155, all -> 0x0148 }
            java.lang.StringBuilder r16 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0155, all -> 0x0148 }
            r16.<init>()     // Catch:{ IOException -> 0x0155, all -> 0x0148 }
            r0 = r16
            r1 = r20
            java.lang.StringBuilder r16 = r0.append(r1)     // Catch:{ IOException -> 0x0155, all -> 0x0148 }
            java.lang.String r17 = r13.getName()     // Catch:{ IOException -> 0x0155, all -> 0x0148 }
            java.lang.StringBuilder r16 = r16.append(r17)     // Catch:{ IOException -> 0x0155, all -> 0x0148 }
            java.lang.String r16 = r16.toString()     // Catch:{ IOException -> 0x0155, all -> 0x0148 }
            r0 = r16
            r9.<init>(r0)     // Catch:{ IOException -> 0x0155, all -> 0x0148 }
            int r3 = r15.read()     // Catch:{ IOException -> 0x015b, all -> 0x014d }
        L_0x004b:
            r16 = -1
            r0 = r16
            if (r3 == r0) goto L_0x0059
            r9.write(r3)     // Catch:{ IOException -> 0x015b, all -> 0x014d }
            int r3 = r15.read()     // Catch:{ IOException -> 0x015b, all -> 0x014d }
            goto L_0x004b
        L_0x0059:
            r15.closeEntry()     // Catch:{ IOException -> 0x015b, all -> 0x014d }
            r9.close()     // Catch:{ IOException -> 0x015b, all -> 0x014d }
            r10 = r9
            goto L_0x0021
        L_0x0061:
            r11 = 1
            if (r8 == 0) goto L_0x0067
            r8.close()     // Catch:{ IOException -> 0x00a7 }
        L_0x0067:
            if (r10 == 0) goto L_0x006c
            r10.close()     // Catch:{ IOException -> 0x00a7 }
        L_0x006c:
            if (r15 == 0) goto L_0x0071
            r15.close()     // Catch:{ IOException -> 0x00a7 }
        L_0x0071:
            if (r11 == 0) goto L_0x009b
            java.lang.String r16 = "ucacheFlag"
            r0 = r18
            android.content.Context r0 = r0.mContext
            r17 = r0
            r17 = 0
            r0 = r19
            r1 = r16
            r2 = r17
            android.content.SharedPreferences r12 = r0.getSharedPreferences(r1, r2)
            android.content.SharedPreferences$Editor r6 = r12.edit()
            java.lang.String r16 = "ucacheInited"
            r17 = 1
            r0 = r16
            r1 = r17
            r6.putBoolean(r0, r1)
            r6.apply()
        L_0x009b:
            r0 = r18
            r1 = r20
            r2 = r21
            boolean r16 = r0.clearZipFile(r11, r1, r2)
            goto L_0x0010
        L_0x00a7:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0071
        L_0x00ac:
            r5 = move-exception
        L_0x00ad:
            r5.printStackTrace()     // Catch:{ all -> 0x00fa }
            if (r7 == 0) goto L_0x00b5
            r7.close()     // Catch:{ IOException -> 0x00f5 }
        L_0x00b5:
            if (r9 == 0) goto L_0x00ba
            r9.close()     // Catch:{ IOException -> 0x00f5 }
        L_0x00ba:
            if (r14 == 0) goto L_0x00bf
            r14.close()     // Catch:{ IOException -> 0x00f5 }
        L_0x00bf:
            if (r11 == 0) goto L_0x00e9
            java.lang.String r16 = "ucacheFlag"
            r0 = r18
            android.content.Context r0 = r0.mContext
            r17 = r0
            r17 = 0
            r0 = r19
            r1 = r16
            r2 = r17
            android.content.SharedPreferences r12 = r0.getSharedPreferences(r1, r2)
            android.content.SharedPreferences$Editor r6 = r12.edit()
            java.lang.String r16 = "ucacheInited"
            r17 = 1
            r0 = r16
            r1 = r17
            r6.putBoolean(r0, r1)
            r6.apply()
        L_0x00e9:
            r0 = r18
            r1 = r20
            r2 = r21
            boolean r16 = r0.clearZipFile(r11, r1, r2)
            goto L_0x0010
        L_0x00f5:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x00bf
        L_0x00fa:
            r16 = move-exception
        L_0x00fb:
            if (r7 == 0) goto L_0x0100
            r7.close()     // Catch:{ IOException -> 0x0140 }
        L_0x0100:
            if (r9 == 0) goto L_0x0105
            r9.close()     // Catch:{ IOException -> 0x0140 }
        L_0x0105:
            if (r14 == 0) goto L_0x010a
            r14.close()     // Catch:{ IOException -> 0x0140 }
        L_0x010a:
            if (r11 == 0) goto L_0x0134
            java.lang.String r16 = "ucacheFlag"
            r0 = r18
            android.content.Context r0 = r0.mContext
            r17 = r0
            r17 = 0
            r0 = r19
            r1 = r16
            r2 = r17
            android.content.SharedPreferences r12 = r0.getSharedPreferences(r1, r2)
            android.content.SharedPreferences$Editor r6 = r12.edit()
            java.lang.String r16 = "ucacheInited"
            r17 = 1
            r0 = r16
            r1 = r17
            r6.putBoolean(r0, r1)
            r6.apply()
        L_0x0134:
            r0 = r18
            r1 = r20
            r2 = r21
            boolean r16 = r0.clearZipFile(r11, r1, r2)
            goto L_0x0010
        L_0x0140:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x010a
        L_0x0145:
            r16 = move-exception
            r7 = r8
            goto L_0x00fb
        L_0x0148:
            r16 = move-exception
            r9 = r10
            r14 = r15
            r7 = r8
            goto L_0x00fb
        L_0x014d:
            r16 = move-exception
            r14 = r15
            r7 = r8
            goto L_0x00fb
        L_0x0151:
            r5 = move-exception
            r7 = r8
            goto L_0x00ad
        L_0x0155:
            r5 = move-exception
            r9 = r10
            r14 = r15
            r7 = r8
            goto L_0x00ad
        L_0x015b:
            r5 = move-exception
            r14 = r15
            r7 = r8
            goto L_0x00ad
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tv.blitz.ucache.BlitzUCacheBridge.unZipUcacheFile(android.content.Context, java.lang.String, java.lang.String):boolean");
    }

    private boolean clearZipFile(boolean zipSuccess, String versionPath, String zipFile) {
        if (zipSuccess) {
            File file = new File(zipFile);
            if (!file.exists()) {
                return true;
            }
            file.delete();
            return true;
        }
        deleteRecursive(new File(versionPath));
        return true;
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    private void usePreLoadUCahce(Context context, String oldPath, String newPath) {
        Context context2 = this.mContext;
        final SharedPreferences sharedPreferences = context.getSharedPreferences("ucacheFlag", 0);
        boolean isInit = sharedPreferences.getBoolean("ucacheInited", false);
        Log.d(TAG, "usePreLoadUCahce, isInit = " + isInit);
        if (!isInit) {
            final Context context3 = context;
            final String str = oldPath;
            final String str2 = newPath;
            new Thread(new Runnable() {
                public void run() {
                    Log.d(BlitzUCacheBridge.TAG, "copyPreCacheToDataFolder begin...");
                    if (BlitzUCacheBridge.this.copyPreCacheToDataFolder(context3, str, str2)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("ucacheInited", true);
                        editor.apply();
                    }
                    Log.d(BlitzUCacheBridge.TAG, "copyPreCacheToDataFolder end...");
                }
            }).start();
        }
    }

    /* access modifiers changed from: private */
    public boolean copyPreCacheToDataFolder(Context context, String oldPath, String newPath) {
        InputStream inStream;
        boolean result = false;
        try {
            String[] files = context.getResources().getAssets().list(oldPath);
            File dstPath = new File(newPath);
            if (!dstPath.exists()) {
                dstPath.mkdirs();
            }
            int length = files.length;
            for (int i = 0; i < length; i++) {
                String file = files[i];
                if (context.getResources().getAssets().list(oldPath + File.separator + file).length <= 0) {
                    File outFile = new File(newPath, file);
                    if (outFile.exists()) {
                        outFile.delete();
                    }
                    if (oldPath.length() != 0) {
                        inStream = context.getAssets().open(oldPath + File.separator + file);
                    } else {
                        inStream = context.getAssets().open(file);
                    }
                    OutputStream outStream = new FileOutputStream(outFile);
                    byte[] buf = new byte[2048];
                    while (true) {
                        int len = inStream.read(buf);
                        if (len <= 0) {
                            break;
                        }
                        outStream.write(buf, 0, len);
                    }
                    inStream.close();
                    outStream.close();
                    result = true;
                } else if (oldPath.length() == 0) {
                    copyPreCacheToDataFolder(context, file, newPath + file + File.separator);
                } else {
                    copyPreCacheToDataFolder(context, oldPath + File.separator + file, newPath + file + File.separator);
                }
            }
            return result;
        } catch (IOException e) {
            while (true) {
                e.printStackTrace();
                return result;
            }
        } catch (Throwable th) {
            return result;
        }
    }

    private boolean checkValidBlockSize() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long availableBlocks = ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
        Log.d(TAG, "checkValidBlockSize, availableBlocks = " + availableBlocks);
        if (availableBlocks >= 10485760) {
            return true;
        }
        Log.e(TAG, "checkValidBlockSize, System available block size is lower!!!");
        return false;
    }

    private void onLoadUCacheData(String mtopAppInfo) {
        nativeLoadUCacheData(mtopAppInfo);
    }

    public void onNetworkChanged(boolean isAvailable, String netType) {
        if (isAvailable) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        BlitzUCacheBridge.this.updateBlitzUCache();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private String native2JavaCallBackFunc(String params, int operType) {
        Log.i(TAG, "native2JavaCallBackFunc operType = " + operType + ", native2JavaCallBackFunc...params = " + params);
        switch (operType) {
            case 1:
                boolean result = getUcacheInitFlagValue();
                JSONObject jsonResult = new JSONObject();
                try {
                    jsonResult.put("result", result);
                    return jsonResult.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    return jsonResult.toString();
                } finally {
                    String resultString = jsonResult.toString();
                }
            case 2:
                boolean result2 = setUcacheInitFlagValue(params);
                JSONObject jsonResult2 = new JSONObject();
                try {
                    jsonResult2.put("result", result2);
                    return jsonResult2.toString();
                } catch (JSONException e2) {
                    e2.printStackTrace();
                    return jsonResult2.toString();
                } finally {
                    String resultString2 = jsonResult2.toString();
                }
            default:
                return "";
        }
    }

    private boolean getUcacheInitFlagValue() {
        Log.d(TAG, "getUcacheInitFlagValue");
        Context context = this.mContext;
        Context context2 = this.mContext;
        return context.getSharedPreferences("ucacheFlag", 0).getBoolean("ucacheInited", false);
    }

    private boolean setUcacheInitFlagValue(String params) {
        Log.d(TAG, "setUcacheInitFlagValue, params = " + params);
        boolean inited = false;
        try {
            inited = new JSONObject(params).getBoolean("ucacheInited");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Context context = this.mContext;
        Context context2 = this.mContext;
        SharedPreferences.Editor editor = context.getSharedPreferences("ucacheFlag", 0).edit();
        editor.putBoolean("ucacheInited", inited);
        return editor.commit();
    }
}
