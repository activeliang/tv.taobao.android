package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseHttpRequest;
import com.yunos.tvtaobao.payment.request.GlobalConfig;
import java.util.Map;

public class GlobalConfigRequest extends BaseHttpRequest {
    private static final String HOST = "https://fragment.tmall.com/yunos/quanjupeizhi?spm=a312d.7832054.0.0.w14BnY";

    public GlobalConfig resolveResult(String result) throws Exception {
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        return (GlobalConfig) JSON.parseObject(result, GlobalConfig.class);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return HOST;
    }
}
