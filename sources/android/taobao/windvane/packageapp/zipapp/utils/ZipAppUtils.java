package android.taobao.windvane.packageapp.zipapp.utils;

import android.content.Context;
import android.net.Uri;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.connect.api.ApiConstants;
import android.taobao.windvane.file.FileAccesser;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.WVPackageAppPrefixesConfig;
import android.taobao.windvane.packageapp.WVPackageAppRuntime;
import android.taobao.windvane.packageapp.ZipAppFileManager;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.data.AppResConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.utils.ConfigDataUtils;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONArray;
import org.json.JSONObject;

public class ZipAppUtils {
    private static final String SPNAME = "WVpackageApp";
    private static final String TAG = "ZipAppUtils";
    public static String ZIP_APP_PATH = "app";

    public static String parseUrlSuffix(ZipAppInfo appInfo, String url) {
        if (url == null || appInfo == null) {
            return null;
        }
        String matchingUrl = url.replaceFirst("http:", "").replaceFirst("https:", "");
        boolean isMatched = false;
        if (TextUtils.isEmpty(appInfo.mappingUrl)) {
            if (!(WVMonitorService.getPackageMonitorInterface() == null || appInfo.installedSeq == 0)) {
                WVMonitorService.getPackageMonitorInterface().commitPackageVisitError(appInfo.getNameAndSeq(), appInfo.s + WVNativeCallbackUtil.SEPERATER + appInfo.installedSeq + SymbolExpUtil.SYMBOL_COLON + url, "13");
            }
            return null;
        }
        if (appInfo.folders != null && appInfo.folders.size() != 0) {
            int i = 0;
            while (true) {
                if (i >= appInfo.folders.size()) {
                    break;
                } else if (matchingUrl.startsWith(appInfo.mappingUrl + appInfo.folders.get(i))) {
                    isMatched = true;
                    break;
                } else {
                    i++;
                }
            }
        } else if (matchingUrl.startsWith(appInfo.mappingUrl)) {
            isMatched = true;
        } else if (WVMonitorService.getPackageMonitorInterface() != null) {
            WVMonitorService.getPackageMonitorInterface().commitPackageVisitError(appInfo.getNameAndSeq(), url, "14");
        }
        if (!isMatched) {
            return null;
        }
        String path = Uri.parse(url).getPath();
        return url.substring(url.indexOf(appInfo.mappingUrl) + appInfo.mappingUrl.length());
    }

    public static String parseZcacheMap2String(Hashtable<String, ArrayList<String>> zcacheTable) {
        JSONObject object = new JSONObject(zcacheTable);
        if (object != null) {
            return object.toString();
        }
        return "{}";
    }

    public static AppResConfig parseAppResConfig(String content, boolean valid) {
        try {
            ConfigDataUtils.ConfigData configData = ConfigDataUtils.parseConfig(content, valid, true);
            if (configData == null) {
                return null;
            }
            JSONObject flist = new JSONObject(content);
            if (flist == null) {
                TaoLog.w(TAG, "parseAppResinfo:parse json fail" + configData.json);
                return null;
            }
            AppResConfig appresinfo = new AppResConfig();
            appresinfo.tk = configData.tk;
            Iterator<String> iter = flist.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                JSONObject val = flist.getJSONObject(key);
                if (!(key == null || val == null)) {
                    appresinfo.getClass();
                    AppResConfig.FileInfo mfileinfo = new AppResConfig.FileInfo();
                    mfileinfo.path = key;
                    mfileinfo.v = val.getString("v");
                    mfileinfo.url = val.getString("url");
                    appresinfo.mResfileMap.put(key, mfileinfo);
                }
            }
            return appresinfo;
        } catch (Exception e) {
            TaoLog.e(TAG, "parseAppResConfig Exception:" + e.getMessage());
            return null;
        }
    }

    public static Hashtable<String, ArrayList<String>> parseZcacheConfig(String content) {
        Hashtable<String, ArrayList<String>> zcacheTable = new Hashtable<>();
        if (content != null) {
            try {
                JSONObject dataObj = new JSONObject(content);
                Iterator<String> apps = dataObj.keys();
                while (apps.hasNext()) {
                    String app = apps.next();
                    JSONArray md5s = dataObj.optJSONArray(app);
                    if (md5s != null) {
                        ArrayList<String> md5List = new ArrayList<>();
                        int size = md5s.length();
                        for (int i = 0; i < size; i++) {
                            md5List.add(md5s.getString(i));
                        }
                        zcacheTable.put(app, md5List);
                    }
                }
            } catch (Exception e) {
            }
        }
        return zcacheTable;
    }

    public static Hashtable<String, Hashtable<String, String>> parsePrefixes(String content) {
        Hashtable<String, Hashtable<String, String>> prefixesTable = new Hashtable<>();
        int updateCount = 0;
        if (content == null) {
            WVPackageAppPrefixesConfig.getInstance().updateCount = 0;
        } else {
            try {
                JSONObject dataObj = new JSONObject(content);
                Iterator<String> prefixs = dataObj.keys();
                while (prefixs.hasNext()) {
                    String prefix = prefixs.next();
                    JSONObject foldersObj = dataObj.optJSONObject(prefix);
                    if (foldersObj != null) {
                        Hashtable<String, String> foldersTable = new Hashtable<>();
                        Iterator<String> foldersIt = foldersObj.keys();
                        while (foldersIt.hasNext()) {
                            String folder = foldersIt.next();
                            foldersTable.put(folder, foldersObj.getString(folder));
                            updateCount++;
                        }
                        prefixesTable.put(prefix, foldersTable);
                    }
                }
            } catch (Exception e) {
                TaoLog.e(TAG, "parse prefixes Exception:" + e.getMessage());
            }
            WVPackageAppPrefixesConfig.getInstance().updateCount = updateCount;
        }
        return prefixesTable;
    }

    public static String parseGlobalConfig2String(ZipGlobalConfig config) {
        JSONObject data = new JSONObject();
        try {
            data.put("v", config.v);
            data.put(UploadQueueMgr.MSGTYPE_INTERVAL, config.i);
            JSONObject appsObj = new JSONObject();
            Hashtable<String, ZipAppInfo> appsTable = config.getAppsTable();
            Enumeration<String> appNames = appsTable.keys();
            while (appNames.hasMoreElements()) {
                String appName = appNames.nextElement();
                ZipAppInfo appInfo = appsTable.get(appName);
                JSONObject appInfoObj = new JSONObject();
                appInfoObj.put("v", appInfo.v);
                appInfoObj.put("f", appInfo.f);
                appInfoObj.put("z", appInfo.z);
                appInfoObj.put("s", appInfo.s);
                appInfoObj.put("t", appInfo.t);
                appInfoObj.put("status", appInfo.status);
                appInfoObj.put("mappingUrl", appInfo.mappingUrl);
                appInfoObj.put("installedSeq", appInfo.installedSeq);
                appInfoObj.put("installedVersion", appInfo.installedVersion);
                appInfoObj.put("isOptional", appInfo.isOptional);
                appInfoObj.put("isPreViewApp", appInfo.isPreViewApp);
                appInfoObj.put("name", appInfo.name);
                appInfoObj.put("folders", appInfo.folders);
                appsObj.put(appName, appInfoObj);
            }
            data.put("apps", appsObj);
            String configDataString = data.toString();
            if (!TaoLog.getLogStatus()) {
                return configDataString;
            }
            TaoLog.v("parseGlobalConfig2String", configDataString);
            return configDataString;
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0060, code lost:
        r3 = r9.next();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig parseString2GlobalConfig(java.lang.String r18) {
        /*
            boolean r14 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r14 == 0) goto L_0x000e
            java.lang.String r14 = "parseString2GlobalConfig"
            r0 = r18
            android.taobao.windvane.util.TaoLog.v(r14, r0)
        L_0x000e:
            android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig r4 = new android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig
            r4.<init>()
            org.json.JSONObject r6 = new org.json.JSONObject     // Catch:{ Exception -> 0x004d }
            r0 = r18
            r6.<init>(r0)     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "v"
            java.lang.String r15 = ""
            java.lang.String r11 = r6.optString(r14, r15)     // Catch:{ Exception -> 0x004d }
            boolean r14 = android.text.TextUtils.isEmpty(r11)     // Catch:{ Exception -> 0x004d }
            if (r14 != 0) goto L_0x0047
            r4.v = r11     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "i"
            java.lang.String r15 = "0"
            java.lang.String r14 = r6.optString(r14, r15)     // Catch:{ Exception -> 0x004d }
            r4.i = r14     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "zcache"
            org.json.JSONObject r12 = r6.optJSONObject(r14)     // Catch:{ Exception -> 0x004d }
            if (r12 == 0) goto L_0x004f
            java.lang.String r14 = "0"
            r4.v = r14     // Catch:{ Exception -> 0x004d }
        L_0x0046:
            return r4
        L_0x0047:
            java.lang.String r14 = ""
            r4.v = r14     // Catch:{ Exception -> 0x004d }
            goto L_0x0046
        L_0x004d:
            r14 = move-exception
            goto L_0x0046
        L_0x004f:
            java.lang.String r14 = "apps"
            org.json.JSONObject r5 = r6.optJSONObject(r14)     // Catch:{ Exception -> 0x004d }
            java.util.Iterator r9 = r5.keys()     // Catch:{ Exception -> 0x004d }
        L_0x005a:
            boolean r14 = r9.hasNext()     // Catch:{ Exception -> 0x004d }
            if (r14 == 0) goto L_0x0046
            java.lang.Object r3 = r9.next()     // Catch:{ Exception -> 0x004d }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x004d }
            org.json.JSONObject r2 = r5.getJSONObject(r3)     // Catch:{ Exception -> 0x004d }
            if (r2 == 0) goto L_0x0046
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r13 = new android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo     // Catch:{ Exception -> 0x004d }
            r13.<init>()     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "f"
            r16 = 5
            r0 = r16
            long r14 = r2.optLong(r14, r0)     // Catch:{ Exception -> 0x004d }
            r13.f = r14     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "v"
            java.lang.String r15 = ""
            java.lang.String r14 = r2.optString(r14, r15)     // Catch:{ Exception -> 0x004d }
            r13.v = r14     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "s"
            r16 = 0
            r0 = r16
            long r14 = r2.optLong(r14, r0)     // Catch:{ Exception -> 0x004d }
            r13.s = r14     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "t"
            r16 = 5
            r0 = r16
            long r14 = r2.optLong(r14, r0)     // Catch:{ Exception -> 0x004d }
            r13.t = r14     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "z"
            java.lang.String r15 = ""
            java.lang.String r14 = r2.optString(r14, r15)     // Catch:{ Exception -> 0x004d }
            r13.z = r14     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "isOptional"
            r15 = 0
            boolean r14 = r2.optBoolean(r14, r15)     // Catch:{ Exception -> 0x004d }
            r13.isOptional = r14     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "isPreViewApp"
            r15 = 0
            boolean r14 = r2.optBoolean(r14, r15)     // Catch:{ Exception -> 0x004d }
            r13.isPreViewApp = r14     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "installedSeq"
            r16 = 0
            r0 = r16
            long r14 = r2.optLong(r14, r0)     // Catch:{ Exception -> 0x004d }
            r13.installedSeq = r14     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "installedVersion"
            java.lang.String r15 = "0.0"
            java.lang.String r14 = r2.optString(r14, r15)     // Catch:{ Exception -> 0x004d }
            r13.installedVersion = r14     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "status"
            r15 = 0
            int r14 = r2.optInt(r14, r15)     // Catch:{ Exception -> 0x004d }
            r13.status = r14     // Catch:{ Exception -> 0x004d }
            r13.name = r3     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "folders"
            org.json.JSONArray r7 = r2.optJSONArray(r14)     // Catch:{ Exception -> 0x004d }
            if (r7 == 0) goto L_0x0105
            int r10 = r7.length()     // Catch:{ Exception -> 0x004d }
            r8 = 0
        L_0x00f7:
            if (r8 >= r10) goto L_0x0105
            java.util.ArrayList<java.lang.String> r14 = r13.folders     // Catch:{ Exception -> 0x004d }
            java.lang.String r15 = r7.getString(r8)     // Catch:{ Exception -> 0x004d }
            r14.add(r15)     // Catch:{ Exception -> 0x004d }
            int r8 = r8 + 1
            goto L_0x00f7
        L_0x0105:
            java.lang.String r14 = "mappingUrl"
            java.lang.String r15 = ""
            java.lang.String r14 = r2.optString(r14, r15)     // Catch:{ Exception -> 0x004d }
            r13.mappingUrl = r14     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = r13.mappingUrl     // Catch:{ Exception -> 0x004d }
            boolean r14 = android.text.TextUtils.isEmpty(r14)     // Catch:{ Exception -> 0x004d }
            if (r14 == 0) goto L_0x011d
            r14 = 0
            android.taobao.windvane.packageapp.zipapp.ZipAppManager.parseUrlMappingInfo(r13, r14)     // Catch:{ Exception -> 0x004d }
        L_0x011d:
            android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum r14 = r13.getAppType()     // Catch:{ Exception -> 0x004d }
            android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum r15 = android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE     // Catch:{ Exception -> 0x004d }
            if (r14 != r15) goto L_0x0138
            int r14 = r13.status     // Catch:{ Exception -> 0x004d }
            int r15 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants.ZIP_REMOVED     // Catch:{ Exception -> 0x004d }
            if (r14 != r15) goto L_0x0138
            int r14 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants.ZIP_NEWEST     // Catch:{ Exception -> 0x004d }
            r13.status = r14     // Catch:{ Exception -> 0x004d }
            r14 = 0
            r13.installedSeq = r14     // Catch:{ Exception -> 0x004d }
            java.lang.String r14 = "0.0"
            r13.installedVersion = r14     // Catch:{ Exception -> 0x004d }
        L_0x0138:
            r4.putAppInfo2Table(r3, r13)     // Catch:{ Exception -> 0x004d }
            goto L_0x005a
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils.parseString2GlobalConfig(java.lang.String):android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig");
    }

    public static boolean isNeedPreInstall(Context context) {
        String preV = ConfigStorage.getStringVal(SPNAME, ApiConstants.H5APP_TTID, "");
        String ttid = GlobalConfig.getInstance().getTtid();
        boolean isNeedPreInstall = ttid != null && !preV.equals(ttid);
        if (!isNeedPreInstall) {
            return isNeedPreInstall;
        }
        ConfigStorage.putStringVal(SPNAME, ApiConstants.H5APP_TTID, ttid);
        return true;
    }

    public static String getLocPathByUrl(String url, boolean needNewest) {
        if (GlobalConfig.context == null) {
            TaoLog.e(TAG, "WindVane is not init");
            return null;
        }
        ZipAppInfo appInfo = WVPackageAppRuntime.getAppInfoByUrl(url);
        if (appInfo != null) {
            if (!needNewest || appInfo.installedSeq == appInfo.s) {
                String resPath = parseUrlSuffix(appInfo, url);
                if (resPath != null) {
                    return ZipAppFileManager.getInstance().getZipResAbsolutePath(appInfo, resPath, false);
                }
            } else {
                TaoLog.i(TAG, url + " is not installed newest app");
                return null;
            }
        }
        ZipGlobalConfig.CacheFileData filedata = ConfigManager.getLocGlobalConfig().isZcacheUrl(url);
        if (filedata != null) {
            return filedata.path;
        }
        return null;
    }

    public static String getLocPathByUrl(String url) {
        return getLocPathByUrl(url, false);
    }

    public static String getStreamByUrl(String url) {
        String path = getLocPathByUrl(url);
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        try {
            return new String(FileAccesser.read(path), "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    public static String getStreamByUrl(String appName, String url) {
        if (GlobalConfig.context == null || appName == null || url == null) {
            TaoLog.e(TAG, "WindVane is not init or param is null");
            return null;
        }
        StringBuilder path = new StringBuilder(128);
        path.append(GlobalConfig.context.getFilesDir().getAbsolutePath());
        path.append(File.separator);
        path.append(ZipAppConstants.ZIPAPP_ROOT_APPS_DIR);
        path.append(File.separator + appName);
        String[] version = new File(path.toString()).list();
        if (version == null) {
            return null;
        }
        path.append(File.separator);
        path.append(version[0]);
        String lastPath = null;
        if (url.contains(appName)) {
            lastPath = url.substring(url.indexOf(appName) + appName.length());
        }
        path.append(lastPath);
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        try {
            return new String(FileAccesser.read(path.toString()), "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean savaZcacheMapToLoc(Hashtable<String, ArrayList<String>> table) {
        if (table == null) {
            return false;
        }
        try {
            return ZipAppFileManager.getInstance().saveZcacheConfig(parseZcacheMap2String(table).getBytes(), false);
        } catch (Exception e) {
            e.printStackTrace();
            TaoLog.e(TAG, "Zcache 本地配置保存异常失败:" + e.toString());
            return false;
        }
    }
}
