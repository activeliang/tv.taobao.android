package com.taobao.media;

import android.text.TextUtils;
import com.taobao.adapter.ConfigAdapter;
import com.taobao.orange.OrangeConfig;

public class MediaConfigAdapter implements ConfigAdapter {
    public String getConfig(String nameSpace, String key, String defaultValue) {
        if (TextUtils.isEmpty(nameSpace)) {
            return OrangeConfig.getInstance().getConfig(MediaConstant.DW_ORANGE_GROUP_NAME, key, defaultValue);
        }
        return OrangeConfig.getInstance().getConfig(nameSpace, key, defaultValue);
    }
}
