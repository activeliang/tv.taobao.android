package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseHttpRequest;
import java.util.Map;
import java.util.TreeMap;

public class TypeWordsRequest extends BaseHttpRequest {
    private String orgin = "";

    public TypeWordsRequest(String orgin2) {
        this.orgin = orgin2;
    }

    public String resolveResult(String result) throws Exception {
        return result;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> params = new TreeMap<>();
        params.put("sentence", this.orgin);
        return params;
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return "http://121.196.200.124:8888/nlp/pos_tagging";
    }
}
