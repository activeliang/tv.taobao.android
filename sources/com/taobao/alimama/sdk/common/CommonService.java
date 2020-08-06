package com.taobao.alimama.sdk.common;

import android.app.Application;
import android.support.annotation.Keep;
import com.taobao.alimama.cpm.ifs.IfsBuilder;

@Keep
public interface CommonService {
    IfsBuilder buildIfsExposure(Application application, String str);

    String handleAdUrl(String str);

    String handleAdUrl(String str, boolean z);

    String handleAdUrl(String str, boolean z, boolean z2);

    String handleAdUrlForClickId(String str, String str2);

    String handleAdUrlForClickId(String str, boolean z);
}
