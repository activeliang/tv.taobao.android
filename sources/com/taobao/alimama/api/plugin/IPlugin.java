package com.taobao.alimama.api.plugin;

import android.support.annotation.Keep;
import com.taobao.alimama.api.AbsServiceImpl;
import java.util.Map;

@Keep
public interface IPlugin {
    Map<Class<?>, Class<? extends AbsServiceImpl>> services();
}
