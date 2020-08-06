package com.taobao.alimama.common.plugin;

import android.support.annotation.Keep;
import com.taobao.alimama.api.AbsServiceImpl;
import com.taobao.alimama.api.plugin.IPlugin;
import com.taobao.alimama.common.a;
import com.taobao.alimama.sdk.common.CommonService;
import java.util.HashMap;
import java.util.Map;

@Keep
public class Plugin implements IPlugin {
    public Map<Class<?>, Class<? extends AbsServiceImpl>> services() {
        HashMap hashMap = new HashMap(1);
        hashMap.put(CommonService.class, a.class);
        return hashMap;
    }
}
