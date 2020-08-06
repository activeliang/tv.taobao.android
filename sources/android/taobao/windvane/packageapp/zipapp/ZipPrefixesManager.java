package android.taobao.windvane.packageapp.zipapp;

import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.packageapp.WVPackageAppPrefixesConfig;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import org.json.JSONObject;

public class ZipPrefixesManager {
    public static final String DATA_KEY = "WVZipPrefixesData";
    public static final String SPNAME = "WVZipPrefixes";
    private static ZipPrefixesManager zipPrefixesManager = null;
    private String TAG = "PackageApp-ZipPrefixesManager";
    private Hashtable<String, Hashtable<String, String>> localPrefixes = null;
    private HashSet<String> zipAppsName = null;

    public static ZipPrefixesManager getInstance() {
        if (zipPrefixesManager == null) {
            synchronized (ZipPrefixesManager.class) {
                if (zipPrefixesManager == null) {
                    zipPrefixesManager = new ZipPrefixesManager();
                    String prefixes = ConfigStorage.getStringVal(SPNAME, DATA_KEY, "");
                    if (!TextUtils.isEmpty(prefixes)) {
                        zipPrefixesManager.localPrefixes = ZipAppUtils.parsePrefixes(prefixes);
                        if (zipPrefixesManager.localPrefixes == null || zipPrefixesManager.localPrefixes.size() <= 0) {
                            TaoLog.w("ZipPrefixesManager", "zipPrefixes parse failed");
                            WVPackageAppPrefixesConfig.getInstance().resetConfig();
                            ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, WVConfigManager.CONFIGNAME_PREFIXES, "0");
                        } else {
                            zipPrefixesManager.parseZipAppsName();
                            TaoLog.i("ZipPrefixesManager", "zipPrefixes parse success");
                        }
                    } else {
                        TaoLog.w("ZipPrefixesManager", "zipPrefixes readFile is empty data");
                    }
                }
            }
        }
        return zipPrefixesManager;
    }

    public String getZipAppName(String url) {
        if (this.localPrefixes == null || this.localPrefixes.size() == 0) {
            return null;
        }
        String matchingUrl = url.replaceFirst("http:", "").replaceFirst("https:", "");
        Enumeration<String> it = this.localPrefixes.keys();
        while (it.hasMoreElements()) {
            String localKey = it.nextElement();
            if (matchingUrl.startsWith(localKey)) {
                matchingUrl = matchingUrl.replaceFirst(localKey, "");
                Hashtable<String, String> prefixData = this.localPrefixes.get(localKey);
                if (prefixData.containsKey("*")) {
                    return prefixData.get("*");
                }
                Enumeration<String> keys = prefixData.keys();
                boolean hasEmptyRule = false;
                while (keys.hasMoreElements()) {
                    String prefixAppKey = keys.nextElement();
                    if ("".equals(prefixAppKey)) {
                        hasEmptyRule = true;
                    } else if (!prefixAppKey.endsWith(WVNativeCallbackUtil.SEPERATER)) {
                        if (matchingUrl.equals(prefixAppKey)) {
                            return prefixData.get(prefixAppKey);
                        }
                    } else if (matchingUrl.startsWith(prefixAppKey)) {
                        return prefixData.get(prefixAppKey);
                    }
                }
                if (hasEmptyRule && !matchingUrl.contains(WVNativeCallbackUtil.SEPERATER)) {
                    return prefixData.get("");
                }
            }
        }
        TaoLog.i(this.TAG, url + " is not zipApp");
        return null;
    }

    public synchronized void saveLocalPrefixesData() {
        if (this.localPrefixes != null) {
            String data = new JSONObject(this.localPrefixes).toString();
            ConfigStorage.putStringVal(SPNAME, DATA_KEY, data);
            parseZipAppsName();
            TaoLog.i(this.TAG, "saveLocalPrefixesData : " + data);
        }
    }

    private synchronized void parseZipAppsName() {
        if (this.localPrefixes != null && this.localPrefixes.size() > 0) {
            if (this.zipAppsName == null) {
                this.zipAppsName = new HashSet<>();
            }
            Enumeration<String> it = this.localPrefixes.keys();
            while (it.hasMoreElements()) {
                Hashtable<String, String> data = this.localPrefixes.get(it.nextElement());
                if (data != null) {
                    this.zipAppsName.addAll(data.values());
                }
            }
            TaoLog.i(this.TAG, this.zipAppsName.toString());
        } else if (this.zipAppsName != null) {
            this.zipAppsName.clear();
        }
    }

    public synchronized boolean isAvailableApp(String appName) {
        boolean z;
        if (this.zipAppsName == null || this.zipAppsName.size() == 0) {
            z = true;
        } else {
            z = this.zipAppsName.contains(appName);
        }
        return z;
    }

    public synchronized void clear() {
        this.localPrefixes = new Hashtable<>();
    }

    public synchronized boolean mergePrefixes(Hashtable<String, Hashtable<String, String>> onlinePrefixes) {
        if (this.localPrefixes == null) {
            this.localPrefixes = new Hashtable<>();
        }
        Enumeration<String> it = onlinePrefixes.keys();
        while (it.hasMoreElements()) {
            String onlineKey = it.nextElement();
            Hashtable<String, String> onlineData = onlinePrefixes.get(onlineKey);
            if (!onlineKey.startsWith(WVUtils.URL_SEPARATOR)) {
                onlineKey = WVUtils.URL_SEPARATOR + onlineKey;
            }
            if (onlineData.containsKey("*")) {
                String onLinePrefixAppName = onlineData.get("*");
                if (TextUtils.isEmpty(onLinePrefixAppName)) {
                    continue;
                } else if (onLinePrefixAppName.equals("-1")) {
                    this.localPrefixes.remove(onlineKey);
                    TaoLog.i(this.TAG, "mergPrefixes : removeAll :" + onlineKey);
                } else {
                    Hashtable<String, String> localData = new Hashtable<>();
                    localData.put("*", onLinePrefixAppName);
                    this.localPrefixes.put(onlineKey, localData);
                }
            } else {
                Hashtable<String, String> needSaveData = new Hashtable<>();
                Hashtable<String, String> localData2 = this.localPrefixes.get(onlineKey);
                if (localData2 != null) {
                    Enumeration<String> localkeys = localData2.keys();
                    while (localkeys.hasMoreElements()) {
                        String appKey = localkeys.nextElement();
                        String appName = localData2.get(appKey);
                        if (!"*".equals(appKey)) {
                            if (appKey.startsWith(WVNativeCallbackUtil.SEPERATER)) {
                                appKey.replaceFirst(WVNativeCallbackUtil.SEPERATER, "");
                            }
                            needSaveData.put(appKey, appName);
                            TaoLog.i(this.TAG, "mergPrefixes : retain :" + onlineKey + ";  appPrefix : " + appKey);
                        }
                    }
                }
                Enumeration<String> onlinekeys = onlineData.keys();
                while (onlinekeys.hasMoreElements()) {
                    String appKey2 = onlinekeys.nextElement();
                    String appName2 = onlineData.get(appKey2);
                    if (!"-1".equals(appName2)) {
                        if (appKey2.startsWith(WVNativeCallbackUtil.SEPERATER)) {
                            appKey2.replaceFirst(WVNativeCallbackUtil.SEPERATER, "");
                        }
                        needSaveData.put(appKey2, appName2);
                        TaoLog.i(this.TAG, "mergPrefixes : add :" + onlineKey + ";  appPrefix : " + appKey2);
                    } else if (needSaveData.containsKey(appKey2)) {
                        needSaveData.remove(appKey2);
                        TaoLog.i(this.TAG, "mergPrefixes : remove :" + onlineKey + ";  appPrefix : " + appKey2);
                    }
                }
                this.localPrefixes.put(onlineKey, needSaveData);
            }
        }
        saveLocalPrefixesData();
        return true;
    }
}
