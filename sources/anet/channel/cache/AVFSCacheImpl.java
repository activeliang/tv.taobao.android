package anet.channel.cache;

import android.taobao.windvane.jsbridge.api.WVFile;
import anet.channel.util.ALog;
import anet.channel.util.StringUtils;
import anetwork.channel.cache.Cache;
import com.taobao.alivfssdk.cache.AVFSCache;
import com.taobao.alivfssdk.cache.AVFSCacheConfig;
import com.taobao.alivfssdk.cache.AVFSCacheManager;
import com.taobao.alivfssdk.cache.IAVFSCache;

public class AVFSCacheImpl implements Cache {
    private static final String MODULE_NAME = "networksdk.httpcache";
    private static final String TAG = "anet.AVFSCacheImpl";
    private static boolean isAvfsCacheExist;
    private static Object nullAllObjectRemoveCallback;
    private static Object nullObjectSetCallback;

    static {
        isAvfsCacheExist = true;
        nullObjectSetCallback = null;
        nullAllObjectRemoveCallback = null;
        try {
            Class.forName("com.taobao.alivfssdk.cache.AVFSCacheManager");
            nullObjectSetCallback = new IAVFSCache.OnObjectSetCallback() {
                public void onObjectSetCallback(String s, boolean b) {
                }
            };
            nullAllObjectRemoveCallback = new IAVFSCache.OnAllObjectRemoveCallback() {
                public void onAllObjectRemoveCallback(boolean b) {
                }
            };
        } catch (ClassNotFoundException e) {
            isAvfsCacheExist = false;
            ALog.w(TAG, "no alivfs sdk!", (String) null, new Object[0]);
        }
    }

    public void initialize() {
        AVFSCache moduleCache;
        if (isAvfsCacheExist && (moduleCache = AVFSCacheManager.getInstance().cacheForModule(MODULE_NAME)) != null) {
            AVFSCacheConfig config = new AVFSCacheConfig();
            config.limitSize = Long.valueOf(WVFile.FILE_MAX_SIZE);
            config.fileMemMaxSize = 1048576;
            moduleCache.moduleConfig(config);
        }
    }

    public Cache.Entry get(String key) {
        if (!isAvfsCacheExist) {
            return null;
        }
        try {
            IAVFSCache avfsCache = getFileCache();
            if (avfsCache != null) {
                return (Cache.Entry) avfsCache.objectForKey(StringUtils.md5ToHex(key));
            }
        } catch (Exception e) {
            ALog.e(TAG, "get cache failed", (String) null, e, new Object[0]);
        }
        return null;
    }

    public void put(String key, Cache.Entry entry) {
        if (isAvfsCacheExist) {
            try {
                IAVFSCache avfsCache = getFileCache();
                if (avfsCache != null) {
                    avfsCache.setObjectForKey(StringUtils.md5ToHex(key), entry, (IAVFSCache.OnObjectSetCallback) nullObjectSetCallback);
                }
            } catch (Exception e) {
                ALog.e(TAG, "put cache failed", (String) null, e, new Object[0]);
            }
        }
    }

    public void clear() {
        if (isAvfsCacheExist) {
            try {
                IAVFSCache avfsCache = getFileCache();
                if (avfsCache != null) {
                    avfsCache.removeAllObject((IAVFSCache.OnAllObjectRemoveCallback) nullAllObjectRemoveCallback);
                }
            } catch (Exception e) {
                ALog.e(TAG, "clear cache failed", (String) null, e, new Object[0]);
            }
        }
    }

    private IAVFSCache getFileCache() {
        AVFSCache moduleCache = AVFSCacheManager.getInstance().cacheForModule(MODULE_NAME);
        if (moduleCache != null) {
            return moduleCache.getFileCache();
        }
        return null;
    }
}
