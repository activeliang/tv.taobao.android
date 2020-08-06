package android.taobao.windvane.packageapp.zipapp.data;

import android.taobao.windvane.packageapp.ZipAppFileManager;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.util.DigestUtils;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;
import com.taobao.ju.track.constants.Constants;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class ZipGlobalConfig {
    private String TAG = "ZipGlobalConfig";
    public String i = "0";
    private Hashtable<String, ZipAppInfo> mAppsTable = new Hashtable<>();
    private Hashtable<String, ArrayList<String>> mZcacheResConfig = new Hashtable<>();
    public String online_v = null;
    public String v = "0";

    public static class CacheFileData {
        public String appName;
        public String path;
        public long seq;
        public String v;
    }

    public Hashtable<String, ZipAppInfo> getAppsTable() {
        return this.mAppsTable;
    }

    public ZipAppInfo getAppInfo(String key) {
        if (!isAvailableData()) {
            return null;
        }
        return this.mAppsTable.get(key);
    }

    public void putAppInfo2Table(String key, ZipAppInfo value) {
        if (key != null && value != null && value.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_REACT && value.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_UNKNOWN && this.mAppsTable != null) {
            if (this.mAppsTable.containsKey(key)) {
                ZipAppInfo oldAppInfo = this.mAppsTable.get(key);
                if (value.getInfo() != ZipUpdateInfoEnum.ZIP_UPDATE_INFO_DELETE) {
                    oldAppInfo.f = value.f;
                    if (oldAppInfo.s <= value.s) {
                        oldAppInfo.s = value.s;
                        oldAppInfo.v = value.v;
                        oldAppInfo.t = value.t;
                        oldAppInfo.z = value.z;
                        oldAppInfo.isOptional = value.isOptional;
                        oldAppInfo.isPreViewApp = value.isPreViewApp;
                        if (value.folders != null && value.folders.size() > 0) {
                            oldAppInfo.folders = value.folders;
                        }
                        if (!TextUtils.isEmpty(value.mappingUrl)) {
                            oldAppInfo.mappingUrl = value.mappingUrl;
                        }
                        if (value.installedSeq > 0) {
                            oldAppInfo.installedSeq = value.installedSeq;
                        }
                        if (!value.installedVersion.equals(Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE)) {
                            oldAppInfo.installedVersion = value.installedVersion;
                        }
                    }
                } else if (oldAppInfo.isOptional || value.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
                    oldAppInfo.status = ZipAppConstants.ZIP_REMOVED;
                    oldAppInfo.f = value.f;
                } else {
                    oldAppInfo.isOptional = true;
                }
            } else {
                this.mAppsTable.put(key, value);
            }
        }
    }

    public void removeAppInfoFromTable(String key) {
        if (key != null && this.mAppsTable != null) {
            this.mAppsTable.remove(key);
        }
    }

    public boolean isAvailableData() {
        if (this.mAppsTable == null || this.mAppsTable.isEmpty()) {
            return false;
        }
        return true;
    }

    public void reset() {
        this.v = "0";
        this.i = "0";
        if (isAvailableData()) {
            this.mAppsTable.clear();
        }
        if (this.mZcacheResConfig != null) {
            this.mZcacheResConfig.clear();
        }
    }

    public boolean isAllAppUpdated() {
        if (!isAvailableData()) {
            return true;
        }
        synchronized (this.mAppsTable) {
            try {
                for (Map.Entry<String, ZipAppInfo> entry : this.mAppsTable.entrySet()) {
                    ZipAppInfo info = entry.getValue();
                    if (info.status != ZipAppConstants.ZIP_REMOVED && info.s != info.installedSeq) {
                        return false;
                    }
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public Hashtable<String, ArrayList<String>> getZcacheResConfig() {
        return this.mZcacheResConfig;
    }

    public void setZcacheResConfig(Hashtable<String, ArrayList<String>> resTable) {
        if (this.mZcacheResConfig != null) {
            this.mZcacheResConfig = resTable;
            if (TaoLog.getLogStatus()) {
                TaoLog.d(this.TAG, "ZcacheforDebug 设置Zcache 的url map size:" + (resTable != null ? resTable.size() : 0));
            }
        }
    }

    public void addZcacheResConfig(String zcachename, ArrayList<String> resList) {
        if (zcachename != null && resList != null && !resList.isEmpty()) {
            this.mZcacheResConfig.put(zcachename, resList);
            TaoLog.d(this.TAG, "ZcacheforDebug 新增zcache name:" + zcachename);
        }
    }

    public void removeZcacheRes(String zcachename) {
        if (zcachename != null) {
            this.mZcacheResConfig.remove(zcachename);
            TaoLog.d(this.TAG, "ZcacheforDebug 删除zcache name:" + zcachename);
        }
    }

    public CacheFileData isZcacheUrl(String url) {
        if (this.mZcacheResConfig != null) {
            try {
                String urlmd5 = DigestUtils.md5ToHex(WVUrlUtil.removeQueryParam(url));
                for (Map.Entry<String, ArrayList<String>> entry : this.mZcacheResConfig.entrySet()) {
                    ArrayList<String> reslist = entry.getValue();
                    String key = entry.getKey();
                    if (reslist != null && reslist.contains(urlmd5)) {
                        ZipAppInfo appInfo = this.mAppsTable.get(key);
                        if (!(this.mAppsTable == null || appInfo == null)) {
                            CacheFileData data = new CacheFileData();
                            data.appName = appInfo.name;
                            data.v = appInfo.v;
                            data.path = ZipAppFileManager.getInstance().getZipResAbsolutePath(appInfo, urlmd5, false);
                            data.seq = appInfo.s;
                            return data;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                TaoLog.d(this.TAG, "ZcacheforDebug 资源url 解析匹配异常，url=" + url);
            }
        }
        return null;
    }
}
