package android.taobao.windvane.cache;

import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.file.FileManager;
import android.taobao.windvane.util.StorageMgr;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;

public class WVFileCacheFactory {
    private static WVFileCacheFactory cacheFactory;

    public static synchronized WVFileCacheFactory getInstance() {
        WVFileCacheFactory wVFileCacheFactory;
        synchronized (WVFileCacheFactory.class) {
            if (cacheFactory == null) {
                cacheFactory = new WVFileCacheFactory();
            }
            wVFileCacheFactory = cacheFactory;
        }
        return wVFileCacheFactory;
    }

    private WVFileCacheFactory() {
    }

    public WVFileCache createFileCache(String rootDir, String url, int capacity, boolean sdcard) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("FileCacheFactory", "createFileCache: " + rootDir + WVNativeCallbackUtil.SEPERATER + url + " capacity: " + capacity + " sdcard: " + sdcard);
        }
        if (url == null || capacity < 10) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d("FileCacheFactory", "createFileCache: url is null, or capacity is too small");
            }
            return null;
        }
        boolean sdcard2 = sdcard && StorageMgr.checkSDCard();
        String baseDir = FileManager.createBaseDir(GlobalConfig.context, rootDir, url, sdcard2);
        String infoDir = FileManager.createInnerfileStorage(GlobalConfig.context, rootDir, url);
        if (TaoLog.getLogStatus()) {
            TaoLog.d("FileCacheFactory", "base dir: " + baseDir);
        }
        WVFileCache fileCache = new WVFileCache(baseDir, infoDir, capacity, sdcard2);
        if (fileCache.init()) {
            return fileCache;
        }
        TaoLog.w("FileCacheFactory", "init FileCache failed");
        return null;
    }
}
