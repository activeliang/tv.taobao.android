package com.ta.audid.upload;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.ta.audid.Variables;
import com.ta.audid.utils.ByteUtils;
import com.ta.audid.utils.RC4;
import com.ta.audid.utils.UtdidLogger;
import com.ta.utdid2.android.utils.Base64;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppsResponse {
    private static final String GET_APPLIST_URL = "https://audid-api.taobao.com/v2.0/a/audid/apps?version=";
    private static final String TAG_APPS = "apps";
    private static final String TAG_DATA = "data";
    private static final String TAG_VERSION = "version";
    private static final AppsResponse mInstance = new AppsResponse();
    private boolean bCollectFinished = false;
    public ArrayList<String> mAppList = new ArrayList<>();
    public int mVersion = 0;

    private AppsResponse() {
    }

    public static AppsResponse getInstance() {
        return mInstance;
    }

    public void requestAppList() {
        parseAppFile();
        String url = GET_APPLIST_URL + this.mVersion;
        UtdidLogger.sd("", url);
        HttpResponse response = HttpUtils.sendRequest(url, "", false);
        String result = "";
        try {
            result = new String(response.data, "UTF-8");
        } catch (Exception e) {
            UtdidLogger.d("", e);
        }
        if (HttpResponse.checkSignature(result, response.signature)) {
            parseAppResult(result);
        }
    }

    /* access modifiers changed from: package-private */
    public void parseAppResult(String content) {
        JSONObject dataJson;
        try {
            JSONObject json = new JSONObject(content);
            if (json.has("data") && (dataJson = json.getJSONObject("data")) != null && dataJson.has("version")) {
                int version = dataJson.getInt("version");
                UtdidLogger.sd("", Integer.valueOf(version));
                if (version < 0) {
                    UtdidKeyFile.writeAppsFile("");
                } else if (dataJson.has(TAG_APPS)) {
                    String apps = dataJson.getString(TAG_APPS);
                    UtdidLogger.sd("", apps);
                    if (!TextUtils.isEmpty(apps)) {
                        Map<String, String> map = new HashMap<>();
                        map.put("version", "" + version);
                        map.put(TAG_APPS, apps);
                        UtdidKeyFile.writeAppsFile(new JSONObject(map).toString());
                        parseAppFile();
                        return;
                    }
                    UtdidKeyFile.writeAppsFile("");
                }
            }
        } catch (JSONException e) {
            UtdidLogger.d("", e);
        }
    }

    public synchronized void parseAppFile() {
        if (!this.bCollectFinished) {
            this.mVersion = 0;
            this.mAppList.clear();
            String content = UtdidKeyFile.readAppsFile();
            if (!TextUtils.isEmpty(content)) {
                try {
                    JSONObject dataJson = new JSONObject(content);
                    if (dataJson.has("version")) {
                        this.mVersion = dataJson.getInt("version");
                        UtdidLogger.sd("", Integer.valueOf(this.mVersion));
                        if (dataJson.has(TAG_APPS)) {
                            String apps = dataJson.getString(TAG_APPS);
                            UtdidLogger.sd("", apps);
                            if (!TextUtils.isEmpty(apps)) {
                                String decodeApps = unGzip(RC4.rc4(Base64.decode(apps, 2)));
                                UtdidLogger.sd("", decodeApps);
                                JSONArray decodeAppsJson = new JSONArray(decodeApps);
                                for (int i = 0; i < decodeAppsJson.length(); i++) {
                                    this.mAppList.add(decodeAppsJson.getString(i));
                                }
                                UtdidLogger.sd("", "size", Integer.valueOf(this.mAppList.size()), "applist", this.mAppList);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public synchronized String getAppsList() {
        String result;
        if (this.mVersion <= 0) {
            result = "00000000";
        } else {
            int len = 4;
            int appListLen = 0;
            if (this.mAppList != null && this.mAppList.size() > 0) {
                appListLen = this.mAppList.size();
                len = 4 + ((appListLen - 1) / 8) + 1;
            }
            byte[] appBytes = new byte[len];
            appBytes[0] = (byte) ((this.mVersion >> 24) & 255);
            appBytes[1] = (byte) ((this.mVersion >> 16) & 255);
            appBytes[2] = (byte) ((this.mVersion >> 8) & 255);
            appBytes[3] = (byte) (this.mVersion & 255);
            if (appListLen > 0) {
                List<String> appList = getDeviceAppList();
                UtdidLogger.sd("", appList);
                for (int i = 0; i < appListLen; i++) {
                    if (appList.contains(this.mAppList.get(i))) {
                        int i2 = (i / 8) + 4;
                        appBytes[i2] = (byte) (appBytes[i2] | ((byte) (1 << (7 - (i % 8)))));
                    }
                }
            }
            result = ByteUtils.toHex(appBytes);
        }
        this.mAppList.clear();
        this.bCollectFinished = true;
        UtdidLogger.sd("", result);
        return result;
    }

    private List<String> getDeviceAppList() {
        PackageManager packageManager = Variables.getInstance().getContext().getPackageManager();
        List<String> appList = new ArrayList<>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                if ((packageInfo.applicationInfo.flags & 1) == 0) {
                    appList.add(packageInfo.packageName);
                }
            }
        } catch (Exception e) {
        }
        return appList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x002e A[SYNTHETIC, Splitter:B:18:0x002e] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0033 A[SYNTHETIC, Splitter:B:21:0x0033] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0038 A[SYNTHETIC, Splitter:B:24:0x0038] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x007f A[SYNTHETIC, Splitter:B:54:0x007f] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0084 A[SYNTHETIC, Splitter:B:57:0x0084] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0089 A[SYNTHETIC, Splitter:B:60:0x0089] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String unGzip(byte[] r13) {
        /*
            r12 = this;
            r9 = 0
            r0 = 0
            r6 = 0
            r2 = 0
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x00a8 }
            r1.<init>(r13)     // Catch:{ Exception -> 0x00a8 }
            java.util.zip.GZIPInputStream r7 = new java.util.zip.GZIPInputStream     // Catch:{ Exception -> 0x00aa, all -> 0x009c }
            r7.<init>(r1)     // Catch:{ Exception -> 0x00aa, all -> 0x009c }
            r10 = 1024(0x400, float:1.435E-42)
            byte[] r4 = new byte[r10]     // Catch:{ Exception -> 0x00ae, all -> 0x009f }
            java.io.ByteArrayOutputStream r3 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x00ae, all -> 0x009f }
            r3.<init>()     // Catch:{ Exception -> 0x00ae, all -> 0x009f }
        L_0x0017:
            r10 = 0
            int r11 = r4.length     // Catch:{ Exception -> 0x0025, all -> 0x00a3 }
            int r8 = r7.read(r4, r10, r11)     // Catch:{ Exception -> 0x0025, all -> 0x00a3 }
            r10 = -1
            if (r8 == r10) goto L_0x0041
            r10 = 0
            r3.write(r4, r10, r8)     // Catch:{ Exception -> 0x0025, all -> 0x00a3 }
            goto L_0x0017
        L_0x0025:
            r5 = move-exception
            r2 = r3
            r6 = r7
            r0 = r1
        L_0x0029:
            r5.printStackTrace()     // Catch:{ all -> 0x007c }
            if (r2 == 0) goto L_0x0031
            r2.close()     // Catch:{ Exception -> 0x006d }
        L_0x0031:
            if (r6 == 0) goto L_0x0036
            r6.close()     // Catch:{ IOException -> 0x0072 }
        L_0x0036:
            if (r0 == 0) goto L_0x003b
            r0.close()     // Catch:{ IOException -> 0x0077 }
        L_0x003b:
            java.lang.String r10 = new java.lang.String
            r10.<init>(r9)
            return r10
        L_0x0041:
            r3.flush()     // Catch:{ Exception -> 0x0025, all -> 0x00a3 }
            byte[] r9 = r3.toByteArray()     // Catch:{ Exception -> 0x0025, all -> 0x00a3 }
            if (r3 == 0) goto L_0x004d
            r3.close()     // Catch:{ Exception -> 0x005b }
        L_0x004d:
            if (r7 == 0) goto L_0x0052
            r7.close()     // Catch:{ IOException -> 0x0060 }
        L_0x0052:
            if (r1 == 0) goto L_0x00b3
            r1.close()     // Catch:{ IOException -> 0x0065 }
            r2 = r3
            r6 = r7
            r0 = r1
            goto L_0x003b
        L_0x005b:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x004d
        L_0x0060:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0052
        L_0x0065:
            r5 = move-exception
            r5.printStackTrace()
            r2 = r3
            r6 = r7
            r0 = r1
            goto L_0x003b
        L_0x006d:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0031
        L_0x0072:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0036
        L_0x0077:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x003b
        L_0x007c:
            r10 = move-exception
        L_0x007d:
            if (r2 == 0) goto L_0x0082
            r2.close()     // Catch:{ Exception -> 0x008d }
        L_0x0082:
            if (r6 == 0) goto L_0x0087
            r6.close()     // Catch:{ IOException -> 0x0092 }
        L_0x0087:
            if (r0 == 0) goto L_0x008c
            r0.close()     // Catch:{ IOException -> 0x0097 }
        L_0x008c:
            throw r10
        L_0x008d:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0082
        L_0x0092:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0087
        L_0x0097:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x008c
        L_0x009c:
            r10 = move-exception
            r0 = r1
            goto L_0x007d
        L_0x009f:
            r10 = move-exception
            r6 = r7
            r0 = r1
            goto L_0x007d
        L_0x00a3:
            r10 = move-exception
            r2 = r3
            r6 = r7
            r0 = r1
            goto L_0x007d
        L_0x00a8:
            r5 = move-exception
            goto L_0x0029
        L_0x00aa:
            r5 = move-exception
            r0 = r1
            goto L_0x0029
        L_0x00ae:
            r5 = move-exception
            r6 = r7
            r0 = r1
            goto L_0x0029
        L_0x00b3:
            r2 = r3
            r6 = r7
            r0 = r1
            goto L_0x003b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.upload.AppsResponse.unGzip(byte[]):java.lang.String");
    }
}
