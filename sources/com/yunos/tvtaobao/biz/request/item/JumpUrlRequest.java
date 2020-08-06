package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseHttpRequest;
import java.util.Map;
import java.util.TreeMap;

public class JumpUrlRequest extends BaseHttpRequest {
    public String resolveResult(String result) throws Exception {
        return result;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        new TreeMap();
        return null;
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return "https://fragment.tmall.com/yunos/voice/uri/mapping";
    }
}
