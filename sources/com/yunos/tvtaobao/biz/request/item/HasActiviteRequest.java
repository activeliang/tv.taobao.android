package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseHttpRequest;
import java.util.Map;

public class HasActiviteRequest extends BaseHttpRequest {
    public String resolveResult(String result) throws Exception {
        return result;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return "https://fragment.tmall.com/yunos/quanjupeizhi";
    }
}
