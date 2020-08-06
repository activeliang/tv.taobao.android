package android.taobao.windvane.packageapp.cleanup;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.monitor.UserTrackUtil;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class WVPackageAppCleanup {
    private static final int CLEAN_CAUSE_AT_CLEAN_PERIOD = 0;
    private static final int CLEAN_CAUSE_LEVEL_HIGH = 1;
    public static final long PER_APP_SPACE = 700000;
    private static String SP_IFNO_KEY = "sp_ifno_key";
    private static String SP_KEY = "lastDel";
    private static String SP_NAME = "WVpackageApp";
    private static final String TAG = "WVPackageAppCleanup";
    private static WVPackageAppCleanup instance;
    HashMap<String, InfoSnippet> infoMap = new HashMap<>();
    private long lastDelTime = ConfigStorage.getLongVal(SP_NAME, SP_KEY, 0);
    private UninstallListener listener;
    private boolean needWriteToDisk = false;

    public interface UninstallListener {
        void onUninstall(List<String> list);
    }

    private WVPackageAppCleanup() {
    }

    public void init() {
        WVEventService.getInstance().addEventListener(new WVPageEventListener());
    }

    public static WVPackageAppCleanup getInstance() {
        if (instance == null) {
            instance = new WVPackageAppCleanup();
        }
        return instance;
    }

    public void updateAccessTimes(ZipAppInfo appInfo) {
        initCheanUpInfoIfNeed();
        InfoSnippet infoSnippet = this.infoMap.get(appInfo.name);
        if (infoSnippet != null) {
            long cur = System.currentTimeMillis();
            if (infoSnippet.lastAccessTime + ((long) WVCommonConfig.commonConfig.packageAccessInterval) < cur) {
                this.needWriteToDisk = true;
                infoSnippet.count++;
                infoSnippet.lastAccessTime = cur;
            }
        }
    }

    public boolean needInstall(ZipAppInfo onlineAppInfo) {
        boolean shouldInstall = true;
        initCheanUpInfoIfNeed();
        addInfoIfNeed(onlineAppInfo);
        ZipGlobalConfig locConfig = ConfigManager.getLocGlobalConfig();
        int installNum = getInstallNum(locConfig.getAppsTable());
        if (locConfig.isAvailableData()) {
            if (installNum < getMaxInstallCapacity()) {
                shouldInstall = true;
            } else {
                shouldInstall = false;
            }
        }
        if (!atCleanUpPeriod()) {
            if (!shouldInstall && onlineAppInfo.getPriority() >= 9 && onlineAppInfo.status != ZipAppConstants.ZIP_REMOVED && onlineAppInfo.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
                shouldInstall = true;
                updateLastDelTime();
                cleanUp(1);
            }
        } else if (atCleanUpPeriod() && getStorageCapacity() < installNum) {
            updateLastDelTime();
            cleanUp(0);
        }
        if (onlineAppInfo.status == ZipAppConstants.ZIP_REMOVED) {
            return false;
        }
        if (onlineAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
            return true;
        }
        return shouldInstall;
    }

    private void initCheanUpInfoIfNeed() {
        if (!checkCleanUpCacheExist()) {
            initCleanUpInfo();
        }
    }

    private boolean atCleanUpPeriod() {
        return this.lastDelTime + ((long) WVCommonConfig.commonConfig.packageRemoveInterval) < System.currentTimeMillis();
    }

    @Deprecated
    public void saveInfoSnippetToDisk() {
        String content = "{}";
        if (this.infoMap == null || this.infoMap.size() != 0) {
            try {
                content = JsonUtil.getJsonString(this.infoMap);
            } catch (Exception ex) {
                TaoLog.e(TAG, "saveInfoSnippetToDisk exception : " + ex.getMessage());
            }
            ConfigStorage.putStringVal(SP_NAME, SP_IFNO_KEY, content);
        }
    }

    @Deprecated
    public void saveInfoSnippetToDiskInner() {
        String content = "{}";
        if (this.infoMap != null && this.infoMap.size() == 0) {
            return;
        }
        if (!this.needWriteToDisk) {
            this.needWriteToDisk = false;
            return;
        }
        try {
            content = JsonUtil.getJsonString(this.infoMap);
        } catch (Exception ex) {
            TaoLog.e(TAG, "saveInfoSnippetToDisk exception : " + ex.getMessage());
        }
        ConfigStorage.putStringVal(SP_NAME, SP_IFNO_KEY, content);
    }

    private void addInfoIfNeed(ZipAppInfo info) {
        if (info != null && !TextUtils.isEmpty(info.name) && this.infoMap.get(info.name) == null && info.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
            this.infoMap.put(info.name, new InfoSnippet(info.name, 0, 0));
        }
    }

    private void initCleanUpInfo() {
        String content = ConfigStorage.getStringVal(SP_NAME, SP_IFNO_KEY, "{}");
        if (TextUtils.isEmpty(content) || content.equals("{}")) {
            initCleanupInfoFromLocal();
            return;
        }
        try {
            this.infoMap = (HashMap) JSON.parseObject(content, new TypeReference<HashMap<String, InfoSnippet>>() {
            }, new Feature[0]);
        } catch (Exception ex) {
            TaoLog.e(TAG, "parse KEY_CLEAN_UP_INFO Exception:" + ex.getMessage());
        }
    }

    private void initCleanupInfoFromLocal() {
        ZipGlobalConfig locConfig = ConfigManager.getLocGlobalConfig();
        if (locConfig == null || !locConfig.isAvailableData()) {
            this.infoMap = new HashMap<>();
            return;
        }
        Hashtable<String, ZipAppInfo> appsTable = locConfig.getAppsTable();
        Enumeration<String> iterator = appsTable.keys();
        while (iterator.hasMoreElements()) {
            String key = iterator.nextElement();
            ZipAppInfo zipAppInfo = appsTable.get(key);
            if (this.infoMap.get(key) == null && zipAppInfo.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
                this.infoMap.put(key, new InfoSnippet(zipAppInfo.name, 0, System.currentTimeMillis()));
            }
        }
    }

    private boolean checkCleanUpCacheExist() {
        return this.infoMap.size() != 0;
    }

    public void registerUninstallListener(UninstallListener listener2) {
        this.listener = listener2;
    }

    public List<String> cleanUp(int cleanCause) {
        Hashtable<String, ZipAppInfo> packageApps = ConfigManager.getLocGlobalConfig().getAppsTable();
        appMonior(cleanCause);
        List<String> toRetainedList = calcToRetainApp(packageApps);
        if (this.listener != null && toRetainedList.size() > 0) {
            this.listener.onUninstall(toRetainedList);
        } else if (!WVCommonConfig.commonConfig.isCheckCleanup) {
            this.listener.onUninstall(toRetainedList);
        }
        clearCount();
        saveInfoSnippetToDisk();
        return toRetainedList;
    }

    private void appMonior(int cleanCause) {
        Hashtable<String, ZipAppInfo> packageApps = ConfigManager.getLocGlobalConfig().getAppsTable();
        long beforeDelSpace = getDevAvailableSpace();
        int expectedNum = getStorageCapacity();
        int installedNum = getInstallNum(packageApps);
        int willDeleteCount = installedNum - expectedNum < 0 ? 0 : installedNum - expectedNum;
        float customRadio = getCustomRadio(packageApps);
        int noCacheCount = getCountByType(packageApps, ZipAppConstants.ZIP_REMOVED);
        int normalCount = getCountByType(packageApps, ZipAppConstants.ZIP_NEWEST);
        float noCacheRatio = getNoCacheRatio(noCacheCount, normalCount);
        if (WVMonitorService.getPackageMonitorInterface() != null) {
            WVMonitorService.getPackageMonitorInterface().onStartCleanAppCache(beforeDelSpace, expectedNum, installedNum, willDeleteCount, customRadio, noCacheCount, normalCount, noCacheRatio, cleanCause);
        }
    }

    private float getNoCacheRatio(int noCacheCount, int normalCount) {
        int totalCount = noCacheCount + normalCount;
        if (totalCount == 0) {
            return 0.0f;
        }
        return ((float) noCacheCount) / ((float) totalCount);
    }

    private int getCountByType(Hashtable<String, ZipAppInfo> packageApps, int state) {
        InfoSnippet infoSnippet;
        int count = 0;
        for (ZipAppInfo info : packageApps.values()) {
            if (!(info.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE || info.status != state || (infoSnippet = this.infoMap.get(info.name)) == null)) {
                count = (int) (((long) count) + infoSnippet.count);
            }
        }
        return count;
    }

    private float getCustomRadio(Hashtable<String, ZipAppInfo> packageApps) {
        int totalCount = 0;
        int customAppCount = 0;
        for (ZipAppInfo info : packageApps.values()) {
            if (info.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
                if (info.isOptional) {
                    customAppCount++;
                }
                totalCount++;
            }
        }
        if (totalCount == 0) {
            return 0.0f;
        }
        return ((float) customAppCount) / ((float) totalCount);
    }

    private long getDevAvailableSpace() {
        try {
            StatFs devStatFs = new StatFs(Environment.getDataDirectory().getPath());
            if (Build.VERSION.SDK_INT >= 18) {
                return devStatFs.getAvailableBytes();
            }
            return ((long) devStatFs.getAvailableBlocks()) * ((long) devStatFs.getBlockSize());
        } catch (RuntimeException e) {
            UserTrackUtil.commitEvent(UserTrackUtil.EVENTID_ERROR, e.toString(), "", "");
            return 2147483647L;
        }
    }

    private List<String> calcToRetainApp(Hashtable<String, ZipAppInfo> allApps) {
        removeUnusedSnippetInfo(allApps);
        updateAppPriority(allApps);
        List<InfoSnippet> list = new ArrayList<>(this.infoMap.values());
        Collections.sort(list, new Comparator<InfoSnippet>() {
            public int compare(InfoSnippet lhs, InfoSnippet rhs) {
                if (lhs.count < rhs.count) {
                    return -1;
                }
                return lhs.count == rhs.count ? 0 : 1;
            }
        });
        return getMostFrequentUsedApp(allApps, list);
    }

    private void removeUnusedSnippetInfo(Hashtable<String, ZipAppInfo> allApps) {
        for (InfoSnippet info : new HashMap<>(this.infoMap).values()) {
            if (allApps.get(info.name) == null) {
                this.infoMap.remove(info.name);
            }
        }
    }

    private void updateLastDelTime() {
        this.lastDelTime = System.currentTimeMillis();
        ConfigStorage.putLongVal(SP_NAME, SP_KEY, this.lastDelTime);
    }

    private void updateAppPriority(Hashtable<String, ZipAppInfo> allApps) {
        for (InfoSnippet info : new HashMap<>(this.infoMap).values()) {
            ZipAppInfo zipAppInfo = allApps.get(info.name);
            if (zipAppInfo == null || zipAppInfo.status == ZipAppConstants.ZIP_REMOVED) {
                this.infoMap.remove(info.name);
            } else if (zipAppInfo.getPriority() >= 9) {
                info.count = Long.MAX_VALUE;
            }
        }
    }

    private void clearCount() {
        for (InfoSnippet next : this.infoMap.values()) {
            next.count = 0;
        }
    }

    private List<String> getMostFrequentUsedApp(Hashtable<String, ZipAppInfo> allApps, List<InfoSnippet> listToSort) {
        int capacity = getStorageCapacity();
        int installNum = getInstallNum(allApps);
        if (installNum - capacity > 0) {
            return getToRetainList(allApps, listToSort, installNum - capacity);
        }
        return new ArrayList<>();
    }

    private List<String> getToRetainList(Hashtable<String, ZipAppInfo> allApps, List<InfoSnippet> sortedList, int toRemoveNum) {
        for (InfoSnippet infoSnippet : new ArrayList<>(sortedList)) {
            ZipAppInfo zipAppInfo = allApps.get(infoSnippet.name);
            if (zipAppInfo != null && zipAppInfo.status == ZipAppConstants.ZIP_REMOVED) {
                sortedList.remove(infoSnippet);
            } else if (zipAppInfo == null) {
                sortedList.remove(infoSnippet);
            }
        }
        if (toRemoveNum >= sortedList.size()) {
            TaoLog.e(TAG, "缓存清理算法出错 ： 待清理的App数量不应大于清理队列中的长度");
        } else {
            sortedList = sortedList.subList(toRemoveNum, sortedList.size());
        }
        List<String> nameList = new ArrayList<>(sortedList.size());
        for (int i = 0; i < sortedList.size(); i++) {
            nameList.add(sortedList.get(i).name);
        }
        return nameList;
    }

    private int getInstallNum(Hashtable<String, ZipAppInfo> allApps) {
        int installNum = 0;
        for (ZipAppInfo zipAppInfo : allApps.values()) {
            if (zipAppInfo.isAppInstalled() && zipAppInfo.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
                installNum++;
            }
        }
        return installNum;
    }

    private int getStorageCapacity() {
        long availableSpace = getAvailableSpace();
        if (availableSpace < 52428800) {
            return 30;
        }
        if (availableSpace <= 52428800 || availableSpace >= ZipAppConstants.LIMITED_APP_SPACE) {
            return 100;
        }
        return 50;
    }

    private long getAvailableSpace() {
        return getDevAvailableSpace() + getInstalledSpace();
    }

    private int getMaxInstallCapacity() {
        long availableSpace = getAvailableSpace();
        if (availableSpace < 52428800) {
            return 30;
        }
        if (availableSpace <= 52428800 || availableSpace >= ZipAppConstants.LIMITED_APP_SPACE) {
            return 150;
        }
        return 75;
    }

    private long getInstalledSpace() {
        ZipGlobalConfig locConfig = ConfigManager.getLocGlobalConfig();
        if (locConfig.isAvailableData()) {
            return ((long) getInstallNum(locConfig.getAppsTable())) * PER_APP_SPACE;
        }
        return 0;
    }

    public static class WVPageEventListener implements WVEventListener {
        public WVEventResult onEvent(int id, WVEventContext ctx, Object... obj) {
            switch (id) {
                case WVEventId.PAGE_destroy /*3003*/:
                    WVPackageAppCleanup.getInstance().saveInfoSnippetToDiskInner();
                    TaoLog.d(WVPackageAppCleanup.TAG, "onEvent  PAGE_destroy");
                    return null;
                case WVEventId.PACKAGE_UPLOAD_COMPLETE /*6001*/:
                    WVPackageAppCleanup.getInstance().saveInfoSnippetToDisk();
                    TaoLog.d(WVPackageAppCleanup.TAG, "onEvent  PACKAGE_UPLOAD_COMPLETE");
                    return null;
                default:
                    return null;
            }
        }
    }
}
