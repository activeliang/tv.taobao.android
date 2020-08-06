package com.taobao.alimama.common;

import android.app.Application;
import com.taobao.alimama.AlimamaAdUrlHandler;
import com.taobao.alimama.api.AbsServiceImpl;
import com.taobao.alimama.cpm.ifs.IfsBuilder;
import com.taobao.alimama.sdk.common.CommonService;
import com.taobao.alimama.utils.KeySteps;

public class a extends AbsServiceImpl implements CommonService {
    public IfsBuilder buildIfsExposure(Application application, String str) {
        return new IfsBuilder(application, str);
    }

    public String handleAdUrl(String str) {
        String handleAdUrl = handleAdUrl(str, true);
        KeySteps.mark("common_handler_url", "original_uri=" + str, "new_url=" + handleAdUrl);
        return handleAdUrl;
    }

    public String handleAdUrl(String str, boolean z) {
        return handleAdUrl(str, z, false);
    }

    public String handleAdUrl(String str, boolean z, boolean z2) {
        return AlimamaAdUrlHandler.getDefault().handleAdUrl(str, z, z2);
    }

    public String handleAdUrlForClickId(String str, String str2) {
        return AlimamaAdUrlHandler.getDefault().a(str, str2);
    }

    public String handleAdUrlForClickId(String str, boolean z) {
        return AlimamaAdUrlHandler.getDefault().handleAdUrlForClickid(str, z);
    }
}
