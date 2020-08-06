package com.taobao.taobaoavsdk.cache;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import com.taobao.taobaoavsdk.cache.library.HttpProxyCacheServer;
import com.taobao.taobaoavsdk.cache.library.StorageUtils;
import com.taobao.taobaoavsdk.cache.library.file.Md5FileNameGenerator;
import java.io.File;

public class PlayerEnvironment {
    public static final String ALI_DROP_0_REF_VF = "ali_drop_0_ref_vf";
    public static final String ALI_DROP_SKIP_REF_VF = "ali_drop_skip_ref_vf";
    public static final String ALI_FLV_RETAIN = "ali_flv_retain";
    public static final String CDN_IP = "cdnIp";
    public static final String PLAY_TOKEN_ID = "playTokenId";
    public static final String TOP_ANCHOR = "top_anchor";
    public static final String USE_TBNET_PROXY = "useTBNetProxy";
    public static final String VIDEO_CACHE_ID = "videoCacheId";
    public static final String VIDEO_LENGTH = "videoLength";
    private static String path;
    private static HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        if (proxy != null) {
            return proxy;
        }
        HttpProxyCacheServer newProxy = newProxy(context);
        proxy = newProxy;
        return newProxy;
    }

    private static HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer.Builder(context.getApplicationContext()).build();
    }

    public static String getCompleteCachePath(Context context, String url) {
        try {
            if (TextUtils.isEmpty(path)) {
                path = StorageUtils.getIndividualCacheDirectory(context).getAbsolutePath();
            }
            String name = new Md5FileNameGenerator().generate(url);
            if (TextUtils.isEmpty(name)) {
                return null;
            }
            File file = new File(path, name);
            if (!file.exists() || !file.canRead() || file.length() <= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
                return null;
            }
            return file.getAbsolutePath();
        } catch (Throwable th) {
            return null;
        }
    }

    public static String getCachePathForCacheKey(Context context, String cacheKey) {
        try {
            if (TextUtils.isEmpty(cacheKey) || context == null) {
                return null;
            }
            if (TextUtils.isEmpty(path)) {
                path = StorageUtils.getIndividualCacheDirectory(context).getAbsolutePath();
            }
            File file = new File(path, cacheKey);
            if (!file.exists() || !file.canRead() || file.length() <= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
                return null;
            }
            return file.getAbsolutePath();
        } catch (Throwable th) {
            return null;
        }
    }
}
