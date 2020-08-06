package android.taobao.windvane.cache;

import android.content.Context;
import android.net.Uri;
import android.taobao.windvane.util.CommonUtils;
import android.taobao.windvane.util.DigestUtils;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVConstants;
import android.text.TextUtils;
import java.io.File;
import java.nio.ByteBuffer;

public class WVCacheManager {
    private static final String TAG = "WVCacheManager";
    private static WVCacheManager cacheManager;
    private WVFileCache fileCache;
    private WVFileCache imagePool;

    public static synchronized WVCacheManager getInstance() {
        WVCacheManager wVCacheManager;
        synchronized (WVCacheManager.class) {
            if (cacheManager == null) {
                cacheManager = new WVCacheManager();
            }
            wVCacheManager = cacheManager;
        }
        return wVCacheManager;
    }

    private WVCacheManager() {
    }

    public void init(Context context) {
        init(context, (String) null, 0);
    }

    public synchronized void init(Context context, String rootDir, int cacheMode) {
        if (context == null) {
            throw new NullPointerException("CacheManager init error, context is null");
        }
        TaoLog.d(TAG, "start init.");
        long time = System.currentTimeMillis();
        if (this.fileCache == null) {
            this.fileCache = WVFileCacheFactory.getInstance().createFileCache(rootDir, WVConstants.WEBCACHE_FOLDER, 250, true);
            this.imagePool = WVFileCacheFactory.getInstance().createFileCache(rootDir, WVConstants.IMAGE_CACHE_FOLDER, 300, true);
        }
        if (TaoLog.getLogStatus()) {
            TaoLog.d(TAG, "init finish.  cost time: " + (System.currentTimeMillis() - time));
        }
    }

    public boolean isCacheEnabled(String url) {
        if (!url.contains(WVConstants.LOCAL_CACHE_TAG)) {
            return false;
        }
        Uri uri = Uri.parse(url);
        if (uri == null || !uri.isHierarchical() || !TextUtils.isEmpty(uri.getQueryParameter(WVConstants.LOCAL_CACHE_TAG)) || !"0".equals(uri.getQueryParameter(WVConstants.LOCAL_CACHE_TAG))) {
            return true;
        }
        return false;
    }

    public boolean writeToFile(WVFileInfo fileInfo, byte[] data) {
        if (checkCacheDirIsNull()) {
            return false;
        }
        ByteBuffer buffer = ByteBuffer.wrap(data);
        if (CommonUtils.isImage(fileInfo.mimeType)) {
            return this.imagePool.write(fileInfo, buffer);
        }
        String secToken = DigestUtils.sha256ToHex(data);
        if (secToken == null) {
            return false;
        }
        fileInfo.sha256ToHex = secToken;
        return this.fileCache.write(fileInfo, buffer);
    }

    public String getCacheDir(boolean image) {
        if (checkCacheDirIsNull()) {
            return null;
        }
        if (image) {
            return this.imagePool.getDirPath();
        }
        return this.fileCache.getDirPath();
    }

    public File getTempDir(boolean image) {
        String tempDir;
        if (checkCacheDirIsNull()) {
            return null;
        }
        if (image) {
            tempDir = this.imagePool.getDirPath() + File.separator + "temp";
        } else {
            tempDir = this.fileCache.getDirPath() + File.separator + "temp";
        }
        File tempFile = new File(tempDir);
        if (tempFile.exists()) {
            return tempFile;
        }
        tempFile.mkdir();
        return tempFile;
    }

    private boolean checkCacheDirIsNull() {
        return this.fileCache == null || this.imagePool == null;
    }
}
