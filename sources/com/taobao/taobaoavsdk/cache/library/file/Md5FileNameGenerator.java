package com.taobao.taobaoavsdk.cache.library.file;

import android.net.Uri;
import android.text.TextUtils;
import com.taobao.taobaoavsdk.cache.PlayerEnvironment;
import com.taobao.taobaoavsdk.cache.library.ProxyCacheUtils;

public class Md5FileNameGenerator implements FileNameGenerator {
    public String generate(String url) {
        String videoCacheId = Uri.parse(url).getQueryParameter(PlayerEnvironment.VIDEO_CACHE_ID);
        if (TextUtils.isEmpty(videoCacheId)) {
            return ProxyCacheUtils.computeMD5(url);
        }
        return videoCacheId;
    }
}
